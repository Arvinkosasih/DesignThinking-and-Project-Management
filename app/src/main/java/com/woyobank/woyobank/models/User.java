package com.woyobank.woyobank.models;

public class User {

    public String email;
    public String phone;
    public String pin;
    public String name;
    public String cardNum;
    public String dueDate;
    public String cvc;
    public double balance;

    public User() {

    }

    public User(String email, String phone, String pin,
                String name, String cardNum, String dueDate,
                String cvc, double balance) {
        this.email = email;
        this.phone = phone;
        this.pin = pin;
        this.name = name;
        this.cardNum = cardNum;
        this.dueDate = dueDate;
        this.cvc = cvc;
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPin() {
        return pin;
    }

    public String getName() {
        return name;
    }

    public String getCardNum() {
        return cardNum;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getCvc() {
        return cvc;
    }

    public double getBalance() {
        return balance;
    }
}
