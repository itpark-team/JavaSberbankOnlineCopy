package com.company.common.dto;

public class SendMoneyFromCardToCardDto {
    public String NumberFrom;
    public String NumberTo;
    public int Money;

    public SendMoneyFromCardToCardDto(String numberFrom, String numberTo, int money) {
        NumberFrom = numberFrom;
        NumberTo = numberTo;
        Money = money;
    }
}
