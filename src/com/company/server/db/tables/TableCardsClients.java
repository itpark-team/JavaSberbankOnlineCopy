package com.company.server.db.tables;

import com.company.common.entities.Card;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class TableCardsClients {
    private String url;
    private String login;
    private String password;

    public TableCardsClients(String url, String login, String password) {
        this.url = url;
        this.login = login;
        this.password = password;
    }

    public void InsertNewCardForClient(int idClient, int idCard) throws Exception{
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection connection = DriverManager.getConnection(url, login, password);

            Statement statement = connection.createStatement();

            String query = String.format("INSERT INTO \"ClientsCards\" (\"IdClient\", \"IdCard\") VALUES (%d,%d)", idClient, idCard);

            statement.executeUpdate(query);
        }catch (Exception e){
            throw e;
        }
    }
}
