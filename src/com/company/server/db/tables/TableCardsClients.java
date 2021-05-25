package com.company.server.db.tables;

public class TableCardsClients {
    private String url;
    private String login;
    private String password;

    public TableCardsClients(String url, String login, String password) {
        this.url = url;
        this.login = login;
        this.password = password;
    }
}
