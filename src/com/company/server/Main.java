package com.company.server;

import com.company.common.communication.General;
import com.company.server.connectiontools.HttpServer;

public class Main {

    public static void main(String[] args) {
        try {
            HttpServer httpServer = new HttpServer(General.SERVER_PORT);
            httpServer.Run();
        } catch (Exception e) {
            System.out.println("Server started error: " + e.getMessage());
        }
    }
}
