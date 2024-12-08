package com.example.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.model.GatePassRequest;
import com.example.util.DBUtil;

public class GatePassRequestDAO {
	
	private Connection getConnection() throws SQLException {
        return DBUtil.getConnection();
    }
	
	public void createRequest(String rollNo, String reason) {
        try (Connection connection = getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO gate_pass_requests (roll_no, date_of_request, reason, status) VALUES (?, CURDATE(), ?, 'Pending')");
            stmt.setString(1, rollNo);
            stmt.setString(2, reason);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<GatePassRequest> getRequestsByStudent(String rollNo) {
        List<GatePassRequest> requests = new ArrayList<>();
        try (Connection connection = getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT gpr.request_id, gpr.roll_no, gpr.date_of_request, gpr.reason, gpr.status," +
                    " CASE" +
                    "     WHEN gpr.status = 'Pending' THEN gpr.admin_id" +
                    "     ELSE a.name" +
                    " END AS admin_name" +
                    " FROM gate_pass_requests gpr" +
                    " LEFT JOIN admins a ON gpr.admin_id = a.admin_id" +
                    " WHERE gpr.roll_no = ? ORDER BY gpr.request_id DESC");
            stmt.setString(1, rollNo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                GatePassRequest request = new GatePassRequest();
                request.setRequestId(rs.getInt("request_id"));
                request.setRollNo(rs.getString("roll_no"));
                request.setDateOfRequest(rs.getDate("date_of_request"));
                request.setReason(rs.getString("reason"));
                request.setStatus(rs.getString("status"));
                request.setAdminId(rs.getString("admin_name"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
    
    
    public List<GatePassRequest> getAllRequests() {
        List<GatePassRequest> requests = new ArrayList<>();
        try (Connection connection = getConnection()) {
        	String sql = "SELECT gpr.request_id, gpr.roll_no, s.name AS student_name, s.branch, gpr.date_of_request, gpr.reason, gpr.status," +
                    " CASE" +
                    "     WHEN gpr.status = 'Pending' THEN gpr.admin_id" +
                    "     ELSE a.name" +
                    " END AS admin_name" +
                    " FROM gate_pass_requests gpr" +
                    " JOIN students s ON gpr.roll_no = s.roll_no" +
                    " LEFT JOIN admins a ON gpr.admin_id = a.admin_id" +
                    " ORDER BY gpr.request_id DESC";


            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                GatePassRequest request = new GatePassRequest();
                request.setRequestId(rs.getInt("request_id"));
                request.setRollNo(rs.getString("roll_no"));
                request.setName(rs.getString("student_name"));  // Setting the student's name
                request.setBranch(rs.getString("branch"));
                request.setDateOfRequest(rs.getDate("date_of_request"));
                request.setReason(rs.getString("reason"));
                request.setStatus(rs.getString("status"));
                request.setAdminId(rs.getString("admin_name"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
    
    public List<GatePassRequest> getRequestsByStatus(String status) {
        List<GatePassRequest> requests = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(("SELECT gpr.request_id, gpr.roll_no, s.name AS student_name, s.branch, gpr.date_of_request, gpr.reason, gpr.status," +
                     " CASE" +
                     "     WHEN gpr.status = 'Pending' THEN gpr.admin_id" +
                     "     ELSE a.name" +
                     " END AS admin_name" +
                     " FROM gate_pass_requests gpr" +
                     " JOIN students s ON gpr.roll_no = s.roll_no" +
                     " LEFT JOIN admins a ON gpr.admin_id = a.admin_id" +
                     " WHERE gpr.status = ? ORDER BY gpr.request_id DESC" ))) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                GatePassRequest request = new GatePassRequest();
                request.setRequestId(rs.getInt("request_id"));
                request.setRollNo(rs.getString("roll_no"));
                request.setName(rs.getString("student_name"));
                request.setBranch(rs.getString("branch"));
                request.setDateOfRequest(rs.getDate("date_of_request"));
                request.setReason(rs.getString("reason"));
                request.setStatus(rs.getString("status"));
                request.setAdminId(rs.getString("admin_name"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
    
    public boolean updateRequestStatus(int requestId, String status, String adminId) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE gate_pass_requests SET status = ?, admin_id = ? WHERE request_id = ?")) {
            stmt.setString(1, status);
            stmt.setString(2, adminId);
            stmt.setInt(3, requestId);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



}
