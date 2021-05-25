package com.company.server.controllers;

import com.company.common.communication.Response;
import com.company.common.entities.Client;
import com.company.server.db.DbManager;
import com.google.gson.Gson;

public class ClientsController {

    public static Response Auth(String parameters) {

        try {
            Client searchClient = new Gson().fromJson(parameters, Client.class);

            DbManager db = DbManager.GetInstance();

            Client findClient = db.TableClients.GetClientByLoginAndPassword(searchClient.Login, searchClient.Password);

            String status = Response.STATUS_OK;
            String message = new Gson().toJson(findClient);

            return new Response(status, message);

        } catch (Exception e) {
            String status = Response.STATUS_ERROR;
            String message = e.getMessage();

            return new Response(status, message);
        }
    }
}
