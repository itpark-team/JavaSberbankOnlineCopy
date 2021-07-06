package com.company.client.controllers;

import com.company.client.Main;
import com.company.client.api.ApiWorker;
import com.company.common.communication.General;
import com.company.common.communication.Response;
import com.company.common.datatools.DataStorage;
import com.company.common.dto.AddMoneyDto;
import com.company.common.dto.AuthClientDto;
import com.company.common.dto.WorkClientDto;
import com.company.common.entities.Card;
import com.company.common.entities.Client;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class WorkPageController {

    @FXML
    Label labelFIO;

    @FXML
    ListView listViewClientsCards;

    @FXML
    TextField textFieldCardNumber;

    @FXML
    TextField textFieldCardMoney;

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

        listViewClientsCards.getSelectionModel().selectedItemProperty().addListener(listViewClientsCardsSelectedItemListener);
    }

    ChangeListener<String> listViewClientsCardsSelectedItemListener = new ChangeListener<String>(){
        public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue){
            int index = listViewClientsCards.getSelectionModel().getSelectedIndex();
            Card selectedCard = client.Cards.get(index);

            textFieldCardNumber.setText(selectedCard.Number);
        }
    };

    private ObservableList<String> ClientCardsToStrings() {
        ObservableList<String> strings = FXCollections.observableArrayList();

        for (int i = 0; i < client.Cards.size(); i++) {
            String string = "Номер: " + client.Cards.get(i).Number + " Баланс(руб.): " + client.Cards.get(i).Money;
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
                    client.Cards = new Gson().fromJson(response.Message, listType);

                    listViewClientsCards.setItems(ClientCardsToStrings());
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

    public void buttonAddMoneyClick(MouseEvent mouseEvent) {
        String number = textFieldCardNumber.getText();
        String money = textFieldCardMoney.getText();

        if (number.length() == 0 || money.length() == 0) {
            ShowDialog("Ошибка. Пожалуйста номер карты и сумму денег для пополнения");
            return;
        }

        try{
            Integer.parseInt(money);
        }catch (Exception e){
            ShowDialog("Ошибка. Сумма денег не является числом");
            return;
        }

        int moneyAsInt = Integer.parseInt(money);
        AddMoneyDto cardForAddMoney = new AddMoneyDto(number, moneyAsInt);

        try {
            Response response = apiWorker.CardsAddMoneyToCard(cardForAddMoney);

            switch (response.Status) {
                case Response.STATUS_OK:
                    ShowDialog("Сумма денег успешно добавлена");
                    textFieldCardNumber.clear();
                    textFieldCardMoney.clear();
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
