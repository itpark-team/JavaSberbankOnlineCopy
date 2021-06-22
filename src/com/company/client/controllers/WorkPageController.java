package com.company.client.controllers;

import com.company.client.api.ApiWorker;
import com.company.common.communication.General;
import com.company.common.communication.Response;
import com.company.common.datatools.DataStorage;
import com.company.common.entities.Card;
import com.company.common.entities.Client;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Observable;

public class WorkPageController {

    @FXML
    Label labelFIO;

    @FXML
    ListView listViewClientsCards;

    ApiWorker apiWorker;
    Client client;

    private void ShowDialog(String message) {
        new Alert(Alert.AlertType.CONFIRMATION, message).showAndWait();
    }

    @FXML
    public void initialize() {
        apiWorker = new ApiWorker(General.SERVER_PORT);
        client = (Client) DataStorage.Get("current_client");
        labelFIO.setText("Добро пожаловать " + client.FirstName + " " + client.LastName);

        LoadClientCards();
    }

    private ObservableList<String> CardsToStrings(ArrayList<Card> cards) {
        ObservableList<String> strings = FXCollections.observableArrayList();

        for (int i = 0; i < cards.size(); i++) {
            String string = "Номер: " + cards.get(i).Number + " Баланс(руб.): " + cards.get(i).Money;
            strings.add(string);
        }

        return strings;
    }


    private void LoadClientCards() {
        try {
            Response response = apiWorker.CardsGetAllForClient(client.Id);

            switch (response.Status) {
                case Response.STATUS_OK:

                    Type listType = new TypeToken<ArrayList<Card>>() {
                    }.getType();
                    ArrayList<Card> cards = new Gson().fromJson(response.Message, listType);

                    listViewClientsCards.setItems(CardsToStrings(cards));
                    break;
                case Response.STATUS_ERROR:
                    ShowDialog("Ошибка сервера: " + response.Message);
                    break;
            }
        } catch (Exception e) {
            ShowDialog("Ошибка отправки на сервер: " + e.toString());
        }
    }

    public void buttonLoadCardsClick(MouseEvent mouseEvent) {
        LoadClientCards();
    }

    public void buttonCreateCardClick(MouseEvent mouseEvent) {
        try {
            Response response = apiWorker.CardsAddNewForClient(client.Id);

            switch (response.Status) {
                case Response.STATUS_OK:
                    LoadClientCards();
                    break;
                case Response.STATUS_ERROR:
                    ShowDialog("Ошибка сервера: " + response.Message);
                    break;
            }
        } catch (Exception e) {
            ShowDialog("Ошибка отправки на сервер: " + e.toString());
        }
    }
}
