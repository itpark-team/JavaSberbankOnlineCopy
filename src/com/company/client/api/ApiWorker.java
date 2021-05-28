package com.company.client.api;

import com.company.common.communication.Request;
import com.company.common.communication.Response;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiWorker {
    private int port;

    private Response SendRequestReceiveResponse(Request request) throws Exception {
        URL url = new URL(String.format("http://127.0.0.1:%d/api", port));
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

        httpConnection.setDoOutput(true);
        httpConnection.setDoInput(true);

        String requestInString = new Gson().toJson(request);
        byte[] requestInBytes = requestInString.getBytes("UTF-8");

        OutputStream outputStream = httpConnection.getOutputStream();
        outputStream.write(requestInBytes);
        outputStream.close();

        InputStream responseStream = httpConnection.getInputStream();
        Scanner scanner = new Scanner(responseStream).useDelimiter("\\A");

        String responseInString = scanner.next();

        Response response = new Gson().fromJson(responseInString, Response.class);

        return response;
    }

    public ApiWorker(int port){
        this.port = port;
    }
}
