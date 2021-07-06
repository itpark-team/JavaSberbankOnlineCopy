package com.company.common.dto;

public class SendMoneyDto {
    public String NumberFrom;
    public String NumberTo;
    public int Money;

    public SendMoneyDto(String numberFrom, String numberTo, int money) {
        NumberFrom = numberFrom;
        NumberTo = numberTo;
        Money = money;
    }
}
