package org.example.dao;

import org.example.model.CheckingAccount;
import org.example.model.SavingsAccount;
import org.example.model.DBUtil;

import java.sql.*;

public class AccountDAO {

    public boolean createCheckingAccount(int customerId) {
        String sql = "INSERT INTO checking_account (customer_id, balance) VALUES (?, 0.0)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createSavingsAccount(int customerId) {
        String sql = "INSERT INTO savings_account (customer_id, balance, interest_rate) VALUES (?, 0.0, 3.5)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public CheckingAccount getCheckingAccount(int customerId) {
        String sql = "SELECT * FROM checking_account WHERE customer_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CheckingAccount(rs.getInt("account_number"), customerId, rs.getDouble("balance"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SavingsAccount getSavingsAccount(int customerId) {
        String sql = "SELECT * FROM savings_account WHERE customer_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new SavingsAccount(rs.getInt("account_number"), customerId, rs.getDouble("balance"), rs.getDouble("interest_rate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

