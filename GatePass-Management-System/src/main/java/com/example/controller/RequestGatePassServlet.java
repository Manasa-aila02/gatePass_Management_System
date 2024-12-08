package com.example.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.dao.GatePassRequestDAO;
import com.example.model.Student;

@WebServlet("/submitGatePass")
public class RequestGatePassServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    Student currentStudent = new Student();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rollNo = request.getParameter("rollNo");
        String reason = request.getParameter("reason");
        currentStudent = (Student)request.getSession().getAttribute("student");
        String currentRollNo = currentStudent.getRollNo();
        if (!currentRollNo.equals(rollNo)) {
        	System.out.println(currentRollNo);
            response.sendRedirect("requestGatePass.html?error=Enter Correct Roll Number!"); // Redirect to an error page
            return;
        }

        GatePassRequestDAO gatePassRequestDAO = new GatePassRequestDAO();
        gatePassRequestDAO.createRequest(rollNo, reason);

        // Redirect to the TrackGatePassServlet instead of the HTML page
        response.sendRedirect("requestGatePass.html?message=Request submitted successfully!");
    }
}

