package com.company.common.entities;

import java.util.ArrayList;

public class Client {
    public int Id;
    public String FirstName;
    public String LastName;
    public String Login;
    public String Password;
    public ArrayList<Card> Cards;

    public Client(int id, String firstName, String lastName, String login, String password) {
        Id = id;
        FirstName = firstName;
        LastName = lastName;
        Login = login;
        Password = password;
        Cards = new ArrayList<>();
    }
}
