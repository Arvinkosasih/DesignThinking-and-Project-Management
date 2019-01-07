package com.woyobank.woyobank.models;

import java.util.HashMap;
import java.util.Map;

public class Transaction {

    public double amount;
    public double newBalance;
    public Long timestamp;
    public String dateTime;
    public String sign;
    public String title;

    public Transaction() {

    }

    public Transaction(double amount, double newBalance, Long timestamp, String dateTime, String sign, String title) {
        this.amount = amount;
        this.newBalance = newBalance;
        this.timestamp = timestamp;
        this.dateTime = dateTime;
        this.sign = sign;
        this.title = title;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("amount", amount);
        result.put("newBalance", newBalance);
        result.put("timestamp", timestamp);
        result.put("dateTime", dateTime);
        result.put("sign", sign);
        result.put("title", title);

        return result;
    }
}
