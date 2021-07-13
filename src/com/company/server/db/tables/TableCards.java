package com.company.server.db.tables;

import com.company.common.entities.Card;

import java.sql.*;
import java.util.ArrayList;

public class TableCards {
    private String url;
    private String login;
    private String password;

    public TableCards(String url, String login, String password) {
        this.url = url;
        this.login = login;
        this.password = password;
    }

    public boolean ExistCardByNumber(String number) throws Exception {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection connection = DriverManager.getConnection(url, login, password);

            Statement statement = connection.createStatement();

            String query = String.format("SELECT * FROM \"Cards\" WHERE \"Number\"='%s'", number);

            ResultSet resultSet = statement.executeQuery(query);

            boolean findCardResult = resultSet.next();

            connection.close();

            return findCardResult;
        } catch (Exception e) {
            throw e;
        }
    }

    public void InsertNewCard(String number, int money) throws Exception {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection connection = DriverManager.getConnection(url, login, password);

            Statement statement = connection.createStatement();

            String query = String.format("INSERT INTO \"Cards\" (\"Number\", \"Money\") VALUES ('%s',%d)", number, money);

            statement.executeUpdate(query);
        } catch (Exception e) {
            throw e;
        }
    }

    public void AddMoneyToCard(String number, int money) throws Exception{
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection connection = DriverManager.getConnection(url, login, password);

            Statement statement = connection.createStatement();

            String query = String.format("UPDATE \"Cards\" SET \"Money\"=\"Money\"+%d WHERE \"Number\"='%s'", money, number);

            statement.executeUpdate(query);
        } catch (Exception e) {
            throw e;
        }
    }

    public int GetCardMoneyByNumber(String number) throws Exception {
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection connection = DriverManager.getConnection(url, login, password);

            Statement statement = connection.createStatement();

            String query = String.format("SELECT \"Money\" FROM \"Cards\" WHERE \"Number\"='%s'", number);

            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();
            int money = resultSet.getInt(1);

            connection.close();

            return money;
        }catch (Exception e){
            throw e;
        }
    }

    public void SendMoneyFromCardToCard(String numberFrom, String numberTo, int money) throws Exception{
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection connection = DriverManager.getConnection(url, login, password);

            Statement statement = connection.createStatement();

            String query = String.format("UPDATE \"Cards\" SET \"Money\"=\"Money\"-%d WHERE \"Number\"='%s'", money, numberFrom);

            statement.executeUpdate(query);

            query = String.format("UPDATE \"Cards\" SET \"Money\"=\"Money\"+%d WHERE \"Number\"='%s'", money, numberTo);

            statement.executeUpdate(query);

        } catch (Exception e) {
            throw e;
        }
    }

    public int GetLastInsertedCardId() throws Exception {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection connection = DriverManager.getConnection(url, login, password);

            Statement statement = connection.createStatement();

            String query = String.format("SELECT MAX(\"Id\") FROM  \"Cards\"");

            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();
            int idCard = resultSet.getInt(1);

            connection.close();

            return idCard;
        } catch (Exception e) {
            throw e;
        }
    }

    public ArrayList<Card> GetCardsByIdClient(int idClient) throws Exception {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection connection = DriverManager.getConnection(url, login, password);

            Statement statement = connection.createStatement();

            String query = String.format("SELECT  * FROM  \"Cards\" WHERE \"Id\" IN (SELECT \"IdCard\" FROM \"ClientsCards\" WHERE \"IdClient\"=%d)", idClient);

            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<Card> cards = new ArrayList<>();

            while (resultSet.next() == true) {

                Card card = new Card(
                        resultSet.getInt("Id"),
                        resultSet.getString("Number"),
                        resultSet.getInt("Money")
                );

                cards.add(card);
            }

            connection.close();

            return cards;
        } catch (Exception e) {
            throw e;
        }
    }
}
