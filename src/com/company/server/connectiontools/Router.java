package com.company.server.connectiontools;

import com.company.common.communication.Response;

import java.lang.reflect.Method;

public class Router {
    public static Response Route(String command, String parameters) throws Exception {
        try {
            String className = "com.company.server.controllers." + command.split("\\.")[0] + "Controller";
            String methodName = command.split("\\.")[1];

            Class usersClass = Class.forName(className);

            Method usersClassMethod = usersClass.getMethod(methodName, String.class);
            Response response = (Response) usersClassMethod.invoke(null, parameters);

            return response;
        } catch (Exception e) {
            throw e;
        }
    }
}
