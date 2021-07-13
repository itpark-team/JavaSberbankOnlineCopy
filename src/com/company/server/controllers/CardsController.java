package com.company.server.controllers;

import com.company.common.communication.Response;
import com.company.common.datatools.DataStorage;
import com.company.common.dto.AddMoneyToCardDto;
import com.company.common.dto.SendMoneyFromCardToCardDto;
import com.company.common.entities.Card;
import com.company.server.db.DbManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

public class CardsController {

    private static String GetRandomCardNumber() {
        String output = "";
        int numberLength = 10;
        Random random = new Random();

        for (int i = 0; i < numberLength; i++) {
            int currentDigit = random.nextInt(10);

            if (i == 0 && currentDigit == 0) {
                currentDigit = 1;
            }

            output += currentDigit;
        }

        return output;
    }

    public static Response AddNewCardForClient(String parameters) throws Exception {
        try {
            int idClient = Integer.parseInt(parameters);

            DbManager db = DbManager.GetInstance();

            boolean isNumberCardExist;
            String cardNumber;

            do {
                cardNumber = GetRandomCardNumber();
                isNumberCardExist = db.TableCards.ExistCardByNumber(cardNumber);
            } while (isNumberCardExist == true);

            db.TableCards.InsertNewCard(cardNumber, 0);

            int idCard = db.TableCards.GetLastInsertedCardId();

            db.TableCardsClients.InsertNewCardForClient(idClient, idCard);

            String status = Response.STATUS_OK;
            String message = "";

            return new Response(status, message);
        } catch (Exception e) {
            DataStorage.Add("my_exception", e.getMessage());
            throw e;
        }
    }

    public static Response GetAllCardsForClient(String parameters) throws Exception {
        try {
            int idClient = Integer.parseInt(parameters);

            DbManager db = DbManager.GetInstance();

            ArrayList<Card> cards = db.TableCards.GetCardsByIdClient(idClient);

            String status = Response.STATUS_OK;
            String message = new Gson().toJson(cards);

            return new Response(status, message);
        } catch (Exception e) {
            DataStorage.Add("my_exception", e.getMessage());
            throw e;
        }
    }

    public static Response AddMoneyToCard(String parameters) throws Exception {
        try {
            AddMoneyToCardDto addMoneyToCardDto = new Gson().fromJson(parameters, AddMoneyToCardDto.class);

            DbManager db = DbManager.GetInstance();

            boolean existCard = db.TableCards.ExistCardByNumber(addMoneyToCardDto.Number);

            if (existCard == false) {
                throw new Exception("Ошибка. Карты с таким номером не существует");
            }

            if (addMoneyToCardDto.Money <= 0) {
                throw new Exception("Ошибка. Добавить сумму денег меньше или равную 0 невозможно");
            }

            db.TableCards.AddMoneyToCard(addMoneyToCardDto.Number, addMoneyToCardDto.Money);

            String status = Response.STATUS_OK;
            String message = "";

            return new Response(status, message);
        } catch (Exception e) {
            DataStorage.Add("my_exception", e.getMessage());
            throw e;
        }
    }

    public static Response SendMoneyFromCardToCard(String parameters) throws Exception {
        try {
            SendMoneyFromCardToCardDto sendMoneyFromCardToCardDto = new Gson().fromJson(parameters, SendMoneyFromCardToCardDto.class);

            DbManager db = DbManager.GetInstance();

            boolean existCardFrom = db.TableCards.ExistCardByNumber(sendMoneyFromCardToCardDto.NumberFrom);

            if (existCardFrom == false) {
                throw new Exception("Ошибка. Карты отправителя с таким номером не существует");
            }

            boolean existCardTo = db.TableCards.ExistCardByNumber(sendMoneyFromCardToCardDto.NumberTo);

            if (existCardTo == false) {
                throw new Exception("Ошибка. Карты получателя с таким номером не существует");
            }

            if (sendMoneyFromCardToCardDto.Money <= 0) {
                throw new Exception("Ошибка. Отправить сумму денег меньше или равную 0 невозможно");
            }

            int moneyInCardFrom = db.TableCards.GetCardMoneyByNumber(sendMoneyFromCardToCardDto.NumberFrom);

            if (moneyInCardFrom < sendMoneyFromCardToCardDto.Money) {
                throw new Exception("Ошибка. На карте отправителя меньше денег чем вы хотите отправить");
            }

            db.TableCards.SendMoneyFromCardToCard(sendMoneyFromCardToCardDto.NumberFrom, sendMoneyFromCardToCardDto.NumberTo, sendMoneyFromCardToCardDto.Money);

            String status = Response.STATUS_OK;
            String message = "";

            return new Response(status, message);

        } catch (Exception e) {
            DataStorage.Add("my_exception", e.getMessage());
            throw e;
        }
    }
}
