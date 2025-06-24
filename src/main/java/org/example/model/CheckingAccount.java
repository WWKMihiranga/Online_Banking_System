package org.example.model;

public class CheckingAccount {
    private int accountNumber;
    private int customerId;
    private double balance;

    public CheckingAccount(int accountNumber, int customerId, double balance) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = balance;
    }

    public CheckingAccount(int customerId) {
        this.customerId = customerId;
        this.balance = 0.0;
    }

    public int getAccountNumber() { return accountNumber; }
    public int getCustomerId() { return customerId; }
    public double getBalance() { return balance; }
}
