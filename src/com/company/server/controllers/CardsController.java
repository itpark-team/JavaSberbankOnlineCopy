package com.company.server.controllers;

import com.company.common.communication.Response;
import com.company.common.entities.Card;
import com.company.server.db.DbManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

public class CardsController {

    private static String GetRandomCardNumber() {
        String output = "";
        int numberLength = 20;
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

            Card insertCard = new Card(0, cardNumber, 0);
            db.TableCards.InsertNewCard(insertCard);

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

            ArrayList<Card> cards = db.TableCards.GetCardByIdClient(idClient);

            String status = Response.STATUS_OK;
            String message = new Gson().toJson(cards);

            return new Response(status, message);
        } catch (Exception e) {
            throw e;
        }
    }
}
