package com.company.common.dto;

public class AddMoneyToCardDto {
    public String Number;
    public int Money;

    public AddMoneyToCardDto(String number, int money) {
        Number = number;
        Money = money;
    }
}
