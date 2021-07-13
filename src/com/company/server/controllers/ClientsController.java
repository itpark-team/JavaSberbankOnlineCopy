package com.company.server.controllers;

import com.company.common.communication.Response;
import com.company.common.datatools.DataStorage;
import com.company.common.dto.AuthClientDto;
import com.company.common.dto.WorkClientDto;
import com.company.common.entities.Client;
import com.company.server.db.DbManager;
import com.google.gson.Gson;

public class ClientsController {

    public static Response Auth(String parameters) throws Exception {

        try {
            AuthClientDto authClientDto = new Gson().fromJson(parameters, AuthClientDto.class);

            DbManager db = DbManager.GetInstance();

            Client findClient = db.TableClients.GetClientByLoginAndPassword(authClientDto.Login, authClientDto.Password);

            WorkClientDto findClientDto = new WorkClientDto(findClient.Id, findClient.FirstName, findClient.LastName);

            String status = Response.STATUS_OK;
            String message = new Gson().toJson(findClientDto);

            return new Response(status, message);

        } catch (Exception e) {
            DataStorage.Add("my_exception", e.getMessage());
            throw e;
        }
    }
}
