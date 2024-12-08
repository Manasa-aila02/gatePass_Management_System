package com.example.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.dao.GatePassRequestDAO;
import com.example.model.GatePassRequest;
import com.example.model.Student;
import com.google.gson.Gson;

@WebServlet("/trackGatePass")
public class TrackGatePassServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Student student = (Student) request.getSession().getAttribute("student");
        if (student == null) {
            response.sendRedirect("login.html");
            return;
        }

        GatePassRequestDAO gatePassRequestDAO = new GatePassRequestDAO();
        List<GatePassRequest> requests = gatePassRequestDAO.getRequestsByStudent(student.getRollNo());
       
        // Convert the list to JSON and write it to the response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String json = gson.toJson(requests);
        out.write(json);
        out.flush();
    }
}
