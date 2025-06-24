package org.example.model;

public class Admin {
    private int adminId;
    private String username;

    public Admin(int adminId, String username) {
        this.adminId = adminId;
        this.username = username;
    }

    public int getAdminId() { return adminId; }
    public String getUsername() { return username; }
}

