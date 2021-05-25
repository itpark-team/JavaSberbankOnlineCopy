package com.company.common.communication;

public class Request {
    public String Command;
    public String Parameters;
    public String ApiKey;

    public Request(String command, String parameters, String apiKey) {
        Command = command;
        Parameters = parameters;
        ApiKey = apiKey;
    }
}
