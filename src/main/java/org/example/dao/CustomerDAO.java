package org.example.dao;

import org.example.model.Customer;
import org.example.model.DBUtil;
import org.example.util.PasswordUtil;

import java.sql.*;

public class CustomerDAO {

    public boolean register(Customer customer) {
        String sql = "INSERT INTO customer(name, email, phone, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhone());
            ps.setString(4, PasswordUtil.hashPassword(customer.getPassword()));
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Customer login(String email, String password) {
        String sql = "SELECT * FROM customer WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashed = rs.getString("password");
                if (PasswordUtil.checkPassword(password, hashed)) {
                    return new Customer(
                            rs.getInt("customer_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
