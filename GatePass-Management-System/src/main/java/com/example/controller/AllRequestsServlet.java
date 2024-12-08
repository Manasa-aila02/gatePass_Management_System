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
import com.google.gson.Gson;

@WebServlet("/allRequests")
public class AllRequestsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private GatePassRequestDAO gatePassRequestDAO;

    @Override
    public void init() throws ServletException {
        gatePassRequestDAO = new GatePassRequestDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve all gate pass requests from the database
        List<GatePassRequest> requests = gatePassRequestDAO.getAllRequests();
        
        // Convert the list of requests to JSON format
//        String json = new Gson().toJson(requests);
        
        // Set response content type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String json = gson.toJson(requests);
        out.write(json);
        out.flush();
    }
}
