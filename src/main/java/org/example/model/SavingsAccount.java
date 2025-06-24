package org.example.model;

public class SavingsAccount {
    private int accountNumber;
    private int customerId;
    private double balance;
    private double interestRate;

    public SavingsAccount(int accountNumber, int customerId, double balance, double interestRate) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = balance;
        this.interestRate = interestRate;
    }

    public SavingsAccount(int customerId) {
        this.customerId = customerId;
        this.balance = 0.0;
        this.interestRate = 3.5;
    }

    public int getAccountNumber() { return accountNumber; }
    public int getCustomerId() { return customerId; }
    public double getBalance() { return balance; }
    public double getInterestRate() { return interestRate; }
}
