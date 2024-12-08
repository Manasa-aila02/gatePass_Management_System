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


@WebServlet("/pendingRequests")
public class PendingRequestsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private GatePassRequestDAO requestDAO;

    public void init() {
        requestDAO = new GatePassRequestDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<GatePassRequest> pendingRequests = requestDAO.getRequestsByStatus("Pending");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json = gson.toJson(pendingRequests);
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

}
