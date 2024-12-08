package com.example.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.dao.AdminDAO;
import com.example.dao.StudentDAO;
import com.example.model.Admin;
import com.example.model.Student;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String id=request.getParameter("id");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role"); // "student" or "admin"
        
        if (role.equals("student")) {
            StudentDAO studentDAO = new StudentDAO();
            Student student = studentDAO.authenticate(username, password);
            if (student != null) {
                request.getSession().setAttribute("student", student);
                request.getSession().setAttribute("studentId", id);
                response.sendRedirect("studentDashboard.html");
            } else {
                response.sendRedirect("login.html?error=Invalid Credentials.");
            }
        } else if (role.equals("admin")) {
            AdminDAO adminDAO = new AdminDAO();
            Admin admin = adminDAO.authenticate(id, username, password);
            if (admin != null) {
                request.getSession().setAttribute("admin", admin);
                request.getSession().setAttribute("adminId", id);
//                System.out.println(admin.getAdminId()+" "+admin.getName()+" "+admin.getPassword());
                
//                request.getSession().setAttribute("id", admin);
                response.sendRedirect("adminDashboard.html");
            } else {
                response.sendRedirect("login.html?error=Invalid Credentials.");
            }
        }
    }
}
