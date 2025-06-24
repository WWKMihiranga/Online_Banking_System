package org.example.dao;

import org.example.model.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InterestService {

    public boolean applyMonthlyInterest() {
        String sql = "UPDATE savings_account SET balance = balance + (balance * interest_rate / 100)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

