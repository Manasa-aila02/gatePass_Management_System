package com.example.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.dao.GatePassRequestDAO;
import com.example.model.Admin;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/updateRequestStatus")
public class UpdateRequestStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private GatePassRequestDAO gatePassRequestDAO;

    @Override
    public void init() throws ServletException {
        gatePassRequestDAO = new GatePassRequestDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Read and parse JSON data
            BufferedReader reader = request.getReader();
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            
//            Admin currentAdmin = (Admin)request.getSession().getAttribute("admin");
//            String currentAdminId = currentAdmin.getAdminId();
            int requestId = json.get("requestId").getAsInt();
            String status = json.get("status").getAsString();
            String adminId = (String)request.getSession().getAttribute("adminId");
            System.out.println(requestId+" "+status+" "+adminId);
            boolean isUpdated = gatePassRequestDAO.updateRequestStatus(requestId, status, adminId);

            if (isUpdated) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}
