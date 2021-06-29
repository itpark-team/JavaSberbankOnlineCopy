package com.company.common.dto;

public class AuthClientDto {
    public String Login;
    public String Password;

    public AuthClientDto(String login, String password) {
        Login = login;
        Password = password;
    }
}
