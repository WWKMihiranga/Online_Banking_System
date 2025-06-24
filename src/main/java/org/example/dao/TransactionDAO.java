package org.example.dao;

import org.example.model.DBUtil;
import org.example.model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public boolean deposit(int accountNumber, double amount, int customerId, String type) {
        String updateSql = "UPDATE checking_account SET balance = balance + ? WHERE account_number = ?";
        String insertTxn = "INSERT INTO transactions (from_account, to_account, amount, type, customer_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement update = conn.prepareStatement(updateSql);
            update.setDouble(1, amount);
            update.setInt(2, accountNumber);

            int rows = update.executeUpdate();
            if (rows == 0) {
                conn.rollback();
                return false;
            }

            PreparedStatement insert = conn.prepareStatement(insertTxn);
            insert.setInt(1, 0); // from_account is 0 for deposit
            insert.setInt(2, accountNumber);
            insert.setDouble(3, amount);
            insert.setString(4, type);
            insert.setInt(5, customerId);
            insert.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean withdraw(int accountNumber, double amount, int customerId) {
        String getBalance = "SELECT balance FROM checking_account WHERE account_number = ?";
        String updateSql = "UPDATE checking_account SET balance = balance - ? WHERE account_number = ?";
        String insertTxn = "INSERT INTO transactions (from_account, to_account, amount, type, customer_id) VALUES (?, ?, ?, 'Withdraw', ?)";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement check = conn.prepareStatement(getBalance);
            check.setInt(1, accountNumber);
            ResultSet rs = check.executeQuery();
            if (rs.next() && rs.getDouble("balance") >= amount) {
                PreparedStatement update = conn.prepareStatement(updateSql);
                update.setDouble(1, amount);
                update.setInt(2, accountNumber);
                update.executeUpdate();

                PreparedStatement insert = conn.prepareStatement(insertTxn);
                insert.setInt(1, accountNumber);
                insert.setInt(2, 0);
                insert.setDouble(3, amount);
                insert.setInt(4, customerId);
                insert.executeUpdate();

                conn.commit();
                return true;
            } else {
                conn.rollback();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean transfer(int fromAcc, int toAcc, double amount, int customerId) {
        String checkSql = "SELECT balance FROM checking_account WHERE account_number = ?";
        String debitSql = "UPDATE checking_account SET balance = balance - ? WHERE account_number = ?";
        String creditSql = "UPDATE checking_account SET balance = balance + ? WHERE account_number = ?";
        String insertTxn = "INSERT INTO transactions (from_account, to_account, amount, type, customer_id) VALUES (?, ?, ?, 'Transfer', ?)";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement check = conn.prepareStatement(checkSql);
            check.setInt(1, fromAcc);
            ResultSet rs = check.executeQuery();
            if (rs.next() && rs.getDouble("balance") >= amount) {
                conn.prepareStatement(debitSql).executeUpdate();
                PreparedStatement debit = conn.prepareStatement(debitSql);
                debit.setDouble(1, amount);
                debit.setInt(2, fromAcc);
                debit.executeUpdate();

                PreparedStatement credit = conn.prepareStatement(creditSql);
                credit.setDouble(1, amount);
                credit.setInt(2, toAcc);
                credit.executeUpdate();

                PreparedStatement insert = conn.prepareStatement(insertTxn);
                insert.setInt(1, fromAcc);
                insert.setInt(2, toAcc);
                insert.setDouble(3, amount);
                insert.setInt(4, customerId);
                insert.executeUpdate();

                conn.commit();
                return true;
            } else {
                conn.rollback();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Transaction> getTransactions(int customerId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE customer_id = ? ORDER BY date DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("from_account"),
                        rs.getInt("to_account"),
                        rs.getDouble("amount"),
                        rs.getString("type"),
                        rs.getTimestamp("date"),
                        customerId
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }
}

