package com.company.client.controllers;

import com.company.client.Main;
import com.company.client.api.ApiWorker;
import com.company.common.communication.General;
import com.company.common.communication.Response;
import com.company.common.datatools.DataStorage;
import com.company.common.entities.Client;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class AuthPageController {
    @FXML
    TextField textFieldLogin;

    @FXML
    TextField textFieldPassword;

    ApiWorker apiWorker;

    @FXML
    public void initialize() {
        apiWorker = new ApiWorker(General.SERVER_PORT);
    }

    private void ShowDialog(String message) {
        new Alert(Alert.AlertType.CONFIRMATION, message).showAndWait();
    }

    public void buttonAuthClick(MouseEvent mouseEvent) {

        String login = textFieldLogin.getText();
        String password = textFieldPassword.getText();

        if (login.length() == 0 || password.length() == 0) {
            ShowDialog("Ошибка. Пожалуйста введите и логин и пароль");
            return;
        }

        Client client = new Client(0, "", "", login, password);

        try {
            Response response = apiWorker.ClientsAuth(client);

            switch (response.Status){
                case Response.STATUS_OK:
                    Client clientFromServer = new Gson().fromJson(response.Message, Client.class);

                    DataStorage.Add("current_client", clientFromServer);

                    ShowDialog("Успешная авторизация для "+clientFromServer.FirstName);

                    Main.GoToPage(Main.WORK_PAGE);
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
