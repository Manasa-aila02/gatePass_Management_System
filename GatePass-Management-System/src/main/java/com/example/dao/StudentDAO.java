package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.model.Student;
import com.example.util.DBUtil;

public class StudentDAO {
	
	private Connection getConnection() throws SQLException {
        return DBUtil.getConnection();
    }
	
	public Student authenticate(String username, String password) {
        try (Connection connection = getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM students WHERE name = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Student student = new Student();
                student.setRollNo(rs.getString("roll_no"));
                student.setName(rs.getString("name"));
                student.setBranch(rs.getString("branch"));
//                student.setDob(rs.getDate("dob"));
                student.setPassword(rs.getString("password"));
                return student;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
