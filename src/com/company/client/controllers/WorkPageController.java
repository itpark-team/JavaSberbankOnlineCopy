package com.company.client.controllers;

import com.company.client.api.ApiWorker;
import com.company.common.communication.General;
import com.company.common.communication.Response;
import com.company.common.datatools.DataStorage;
import com.company.common.dto.AddMoneyToCardDto;
import com.company.common.dto.SendMoneyFromCardToCardDto;
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
    TextField textFieldCardAddNumber;

    @FXML
    TextField textFieldCardAddMoney;

    @FXML
    TextField textFieldCardSendNumberFrom;

    @FXML
    TextField textFieldCardSendNumberTo;

    @FXML
    TextField textFieldCardSendMoney;

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

    ChangeListener<String> listViewClientsCardsSelectedItemListener = new ChangeListener<String>() {
        public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue) {
            try {
                int index = listViewClientsCards.getSelectionModel().getSelectedIndex();
                Card selectedCard = client.Cards.get(index);
                textFieldCardAddNumber.setText(selectedCard.Number);
            } catch (Exception e) {
                return;
            }
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
            Response response = apiWorker.CardsGetAllCardsForClient(client.Id);

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
            Response response = apiWorker.CardsAddNewCardForClient(client.Id);

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
        String number = textFieldCardAddNumber.getText();
        String money = textFieldCardAddMoney.getText();

        if (number.length() == 0 || money.length() == 0) {
            ShowDialog("Ошибка. Пожалуйста номер карты и сумму денег для пополнения");
            return;
        }

        try {
            Integer.parseInt(money);
        } catch (Exception e) {
            ShowDialog("Ошибка. Сумма денег не является числом");
            return;
        }

        int moneyAsInt = Integer.parseInt(money);
        AddMoneyToCardDto cardForAddMoney = new AddMoneyToCardDto(number, moneyAsInt);

        try {
            Response response = apiWorker.CardsAddMoneyToCard(cardForAddMoney);

            switch (response.Status) {
                case Response.STATUS_OK:
                    ShowDialog("Сумма денег успешно добавлена");
                    textFieldCardAddNumber.clear();
                    textFieldCardAddMoney.clear();
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

    public void buttonSendMoneyClick(MouseEvent mouseEvent) {
        String numberFrom = textFieldCardSendNumberFrom.getText();
        String numberTo = textFieldCardSendNumberTo.getText();
        String money = textFieldCardSendMoney.getText();

        if (numberFrom.length() == 0 || numberTo.length() == 0 || money.length() == 0) {
            ShowDialog("Ошибка. Пожалуйста введите номера карт и сумму денег для перевода");
            return;
        }

        try {
            Integer.parseInt(money);
        } catch (Exception e) {
            ShowDialog("Ошибка. Сумма денег не является числом");
            return;
        }

        int moneyAsInt = Integer.parseInt(money);
        SendMoneyFromCardToCardDto sendMoneyFromCardToCardDto = new SendMoneyFromCardToCardDto(numberFrom, numberTo, moneyAsInt);

        try {
            Response response = apiWorker.CardsSendMoneyFromCardToCard(sendMoneyFromCardToCardDto);

            switch (response.Status) {
                case Response.STATUS_OK:
                    ShowDialog("Сумма денег успешно переведена");
                    textFieldCardSendNumberFrom.clear();
                    textFieldCardSendNumberTo.clear();
                    textFieldCardSendMoney.clear();

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
