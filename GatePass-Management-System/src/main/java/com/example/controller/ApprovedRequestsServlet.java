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

/**
 * Servlet implementation class ApprovedRequestsServlet
 */
@WebServlet("/approvedRequests")
public class ApprovedRequestsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private GatePassRequestDAO requestDAO;

    public void init() {
        requestDAO = new GatePassRequestDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<GatePassRequest> approvedRequests = requestDAO.getRequestsByStatus("Approved");
        String jsonData = new Gson().toJson(approvedRequests);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonData);
        out.flush();
    }

}
