package org.example.model;

import java.sql.Timestamp;

public class Transaction {
    private int transactionId;
    private int fromAccount;
    private int toAccount;
    private double amount;
    private String type;
    private Timestamp date;
    private int customerId;

    public Transaction(int transactionId, int fromAccount, int toAccount, double amount, String type, Timestamp date, int customerId) {
        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.customerId = customerId;
    }

    // Getters only (readonly model)
    public int getTransactionId() { return transactionId; }
    public int getFromAccount() { return fromAccount; }
    public int getToAccount() { return toAccount; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public Timestamp getDate() { return date; }
    public int getCustomerId() { return customerId; }
}
