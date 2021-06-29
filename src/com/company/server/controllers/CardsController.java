package com.company.server.controllers;

import com.company.common.communication.Response;
import com.company.common.datatools.DataStorage;
import com.company.common.dto.AddMoneyDto;
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

    public static Response AddNewForClient(String parameters) throws Exception {
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
            throw e;
        }
    }

    public static Response GetAllForClient(String parameters) throws Exception {
        try {
            int idClient = Integer.parseInt(parameters);

            DbManager db = DbManager.GetInstance();

            ArrayList<Card> cards = db.TableCards.GetCardsByIdClient(idClient);

            String status = Response.STATUS_OK;
            String message = new Gson().toJson(cards);

            return new Response(status, message);
        } catch (Exception e) {
            throw e;
        }
    }

    public static Response AddMoneyToCard(String parameters) throws Exception {
        try {
            AddMoneyDto cardForAddMoney = new Gson().fromJson(parameters, AddMoneyDto.class);

            DbManager db = DbManager.GetInstance();

            boolean existCard = db.TableCards.ExistCardByNumber(cardForAddMoney.Number);

            if (existCard == false) {
                DataStorage.Add("my_exception", "Ошибка. Карты с таким номером не существует");
                throw new Exception("Ошибка. Ошибка. Карты с таким номером не существует");
            }

            db.TableCards.AddMoney(cardForAddMoney.Number, cardForAddMoney.Money);

            String status = Response.STATUS_OK;
            String message = "";

            return new Response(status, message);
        } catch (Exception e) {
            throw e;
        }
    }
}
