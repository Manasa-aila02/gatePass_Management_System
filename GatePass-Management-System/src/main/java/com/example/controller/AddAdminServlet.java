package com.example.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.dao.AdminDAO;
import com.example.model.Admin;

/**
 * Servlet implementation class AddAdminServlet
 */
@WebServlet("/addAdminServlet")
public class AddAdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AdminDAO adminDAO;

    @Override
    public void init() throws ServletException {
        adminDAO = new AdminDAO();
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if the user is authorized
        Admin currentAdmin = (Admin)request.getSession().getAttribute("admin");
        String currentAdminId = currentAdmin.getAdminId();
        System.out.println(currentAdmin.getAdminId()+" "+currentAdmin.getName()+" "+currentAdmin.getPassword());
        if (!currentAdminId.equals("root_admin")) {
        	System.out.println(currentAdminId);
            response.sendRedirect("addAdmin.html?message=You do not have enough permissions to perform this action!"); // Redirect to an error page
            return;
        }

        // Get parameters from request
        String adminId = request.getParameter("adminId");
        String name = request.getParameter("adminName");
        String password = request.getParameter("adminPassword");
        System.out.println(adminId+" "+name+" "+password);
        // Add the new admin to the database
        boolean isAdded = adminDAO.addAdmin(adminId, name, password);
        if (isAdded) {
            response.sendRedirect("addAdmin.html?message=Admin added successfully!"); // Redirect to admin dashboard on success
        } else {
            response.sendRedirect("addAdmin.html?error=Failed to add user!"); // Redirect back with error
        }
    }
}
