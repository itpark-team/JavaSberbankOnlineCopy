package com.company.common.communication;

public class Response {
    public final static String STATUS_OK = "OK";
    public final static String STATUS_ERROR = "ERROR";

    public String Status;
    public String Message;

    public Response(String status, String message) {
        Status = status;
        Message = message;
    }
}
