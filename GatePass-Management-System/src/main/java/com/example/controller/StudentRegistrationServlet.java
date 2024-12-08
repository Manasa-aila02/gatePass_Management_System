package com.example.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.util.DBUtil;

@WebServlet("/register")
public class StudentRegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String CHECK_STUDENT_ROLL_EXISTS_SQL = "SELECT COUNT(*) FROM students WHERE roll_no = ?";
    private static final String CHECK_STUDENT_NAME_EXISTS_SQL = "SELECT COUNT(*) FROM students WHERE name = ?";
    private static final String INSERT_STUDENT_SQL = "INSERT INTO students (roll_no, name, branch, dob, password) VALUES (?, ?, ?, ?, ?)";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameters
        String rollNo = request.getParameter("rollNumber");
        String name = request.getParameter("name");
        String branch = request.getParameter("branch");
        String dob = request.getParameter("dob");
        String password = request.getParameter("password");

        // Validate inputs
        if (rollNo == null || name == null || branch == null || dob == null || password == null ||
            rollNo.trim().isEmpty() || name.trim().isEmpty() || branch.trim().isEmpty() ||
            dob.trim().isEmpty() || password.trim().isEmpty()) {
            response.sendRedirect("register.html?error=All fields are required.");
            return;
        }

        // Database operation
        try (Connection connection = DBUtil.getConnection()) {
            // Check if roll number or name already exists
            PreparedStatement checkStmt1 = connection.prepareStatement(CHECK_STUDENT_ROLL_EXISTS_SQL);
            PreparedStatement checkStmt2 = connection.prepareStatement(CHECK_STUDENT_NAME_EXISTS_SQL);
            checkStmt1.setString(1, rollNo);
            checkStmt2.setString(1, name);
            ResultSet rs1 = checkStmt1.executeQuery();
            rs1.next();
            int count1 = rs1.getInt(1);
            ResultSet rs2 = checkStmt2.executeQuery();
            rs2.next();
            int count2 = rs2.getInt(1);

            if (count1 > 0) {
                // Roll number already exists
                response.sendRedirect("register.html?error=The roll number already exists. Please choose another.");
            } else if (count2 > 0) {
                // Name already exists
                response.sendRedirect("register.html?error=The username already exists. Please choose another.");
            } else {
                // Proceed with registration
                try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STUDENT_SQL)) {
                    preparedStatement.setString(1, rollNo);
                    preparedStatement.setString(2, name);
                    preparedStatement.setString(3, branch);
                    preparedStatement.setString(4, dob);
                    preparedStatement.setString(5, password);

                    int result = preparedStatement.executeUpdate();

                    if (result > 0) {
                        // Registration successful, redirect to login page
                        response.sendRedirect("login.html");
                    } else {
                        // Registration failed
                        response.sendRedirect("register.html?error=Registration failed. Please try again.");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("register.html?error=Database error occurred.");
        }
    }
}
