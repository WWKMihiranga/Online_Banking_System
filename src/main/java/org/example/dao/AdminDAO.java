package org.example.dao;

import org.example.model.Admin;
import org.example.model.DBUtil;
import org.example.util.PasswordUtil;

import java.sql.*;

public class AdminDAO {
    public Admin login(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (PasswordUtil.checkPassword(password, rs.getString("password"))) {
                    return new Admin(rs.getInt("admin_id"), username);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

