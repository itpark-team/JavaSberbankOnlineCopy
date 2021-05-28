package com.company.server.db.tables;


import com.company.common.entities.Client;

import java.sql.*;

public class TableClients {
    private String url;
    private String login;
    private String password;

    public TableClients(String url, String login, String password) {
        this.url = url;
        this.login = login;
        this.password = password;
    }

    public Client GetClientByLoginAndPassword(String clientLogin, String clientPassword) throws Exception {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection connection = DriverManager.getConnection(url, login, password);

            Statement statement = connection.createStatement();

            String query = String.format("SELECT * FROM \"Clients\" WHERE \"Login\"='%s' AND \"Password\"='%s'", clientLogin, clientPassword);

            ResultSet resultSet = statement.executeQuery(query);

            boolean findClientResult = resultSet.next();
            Client client = null;

            if (findClientResult == false) {
                throw new Exception("Ошибка. Клиент не найден");
            } else {
                client = new Client(
                        resultSet.getInt("Id"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("Login"),
                        resultSet.getString("Password")
                );
            }

            connection.close();

            return client;
        } catch (Exception e) {
            throw e;
        }
    }
}
