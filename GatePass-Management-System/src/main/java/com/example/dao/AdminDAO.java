package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.model.Admin;
import com.example.util.DBUtil;

public class AdminDAO {

    // Use DBUtil to get the connection
    private Connection getConnection() throws SQLException {
        return DBUtil.getConnection();
    }

    public Admin authenticate(String adminId, String admin_name, String password) {
        try (Connection connection = getConnection()) {
            // Use a prepared statement to prevent SQL injection
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM admins WHERE admin_id =? AND name = ? AND password = ?");
            stmt.setString(1, adminId);
            stmt.setString(2, admin_name);
            stmt.setString(3, password);
            ResultSet rs = stmt.executeQuery();
            
            // Check if a result is returned
            if (rs.next()) {
                Admin admin = new Admin();
//                admin.setAdminId(rs.getString("admin_id"));
                admin.setAdminId(rs.getString("admin_id"));
                admin.setName(rs.getString("name"));
                admin.setPassword(rs.getString("password"));
                return admin;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean addAdmin(String adminId, String name, String password) {
        String sql = "INSERT INTO admins (admin_id, name, password) VALUES (?, ?, ?)";
        
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, adminId);
            stmt.setString(2, name);
            stmt.setString(3, password); // Note: You should hash the password before storing it

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
