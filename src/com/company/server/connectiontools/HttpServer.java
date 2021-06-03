package com.company.server.connectiontools;

import com.company.common.communication.General;
import com.company.common.communication.Request;
import com.company.common.communication.Response;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HttpServer {
    private int port;

    private void Log(String message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.printf("%s - %s\n", dtf.format(now), message);
    }

    private class RequestHandler implements HttpHandler {
        private void SendResponse(HttpExchange clientChanel, Response response) throws Exception {
            String responseInString = new Gson().toJson(response);

            byte[] responseInBytes = responseInString.getBytes("UTF-8");
            clientChanel.sendResponseHeaders(200, responseInBytes.length);

            OutputStream outputStream = clientChanel.getResponseBody();
            outputStream.write(responseInBytes);
            outputStream.close();
        }

        private Request ReceiveRequest(HttpExchange clientChanel) throws Exception {
            InputStreamReader inputStream = new InputStreamReader(clientChanel.getRequestBody(), "utf-8");
            BufferedReader reader = new BufferedReader(inputStream);

            int currentSymbol;
            StringBuilder buffer = new StringBuilder(512);

            while ((currentSymbol = reader.read()) != -1) {
                buffer.append((char) currentSymbol);
            }

            reader.close();
            inputStream.close();

            String requestInString = buffer.toString();

            Request request = new Gson().fromJson(requestInString, Request.class);
            return request;
        }

        @Override
        public void handle(HttpExchange clientChanel) {
            Request request = null;
            Response response = null;

            try {
                request = ReceiveRequest(clientChanel);

                Log(String.format("REQUEST :: command: %s --- parameters: %s --- apikey: %s", request.Command, request.Parameters, request.ApiKey));

            } catch (Exception e) {
                Log(e.toString());
                return;
            }

            try {
                if (request.ApiKey.equals(General.API_KEY) == true) {
                    response = Router.Route(request.Command, request.Parameters);
                } else {
                    throw new Exception("Ошибка. Клиент не найден");
                }
            } catch (Exception e) {
                String status = Response.STATUS_ERROR;
                String message = e.toString();

                response = new Response(status, message);
            }

            try {
                SendResponse(clientChanel, response);

                Log(String.format("RESPONSE :: status: %s --- message: %s", response.Status, response.Message));

            } catch (Exception e) {
                Log(e.toString());
            }
        }
    }

    public HttpServer(int port) {
        this.port = port;
    }

    public void Run() throws Exception {
        com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api", new RequestHandler());
        server.setExecutor(null);
        server.start();

        Log("Server started");
    }
}
