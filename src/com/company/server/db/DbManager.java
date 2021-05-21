package com.company.server.db;

import com.company.server.db.tables.TableCards;
import com.company.server.db.tables.TableClients;

public class DbManager {
    private static DbManager instance = null;

    private String URL = "jdbc:oracle:thin:@localhost:55002:XE";
    private String LOGIN = "c##SBERBANK";
    private String PASSWORD = "1234";

    public TableCards TableCards;
    public TableClients TableClients;

    private DbManager() {
        TableCards = new TableCards(URL, LOGIN, PASSWORD);
        TableClients = new TableClients(URL, LOGIN, PASSWORD);
    }

    public static DbManager GetInstance() {
        if (instance == null) {
            instance = new DbManager();
        }
        return instance;
    }
}
