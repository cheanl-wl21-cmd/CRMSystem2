/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import interfaces.Reportable;
import java.util.ArrayList;
import java.util.List;

public class Admin extends User implements Reportable {
    private String adminId;
    private List<AuditLog> auditLogs;

    public Admin(String userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.adminId = "ADMIN-" + userId;
        this.auditLogs = new ArrayList<>();
    }

    public Report generateMonthlyReport(int month, int year, List<Ticket> allTickets) {
        Report report = new Report("RPT-" + System.currentTimeMillis(), month, year);
        report.generateReport(allTickets);
        logAction("Generated monthly report for " + month + "/" + year);
        return report;
    }

    public void viewAuditLogs() {
        System.out.println("\n===== AUDIT LOGS =====");
        for (AuditLog log : auditLogs) {
            log.displayLogEntry();
        }
    }

    public void logAction(String action) {
        AuditLog log = new AuditLog(
            "LOG-" + System.currentTimeMillis(),
            action,
            this.adminId
        );
        auditLogs.add(log);
    }

    public void addUser(User user, List<User> userList) {
        userList.add(user);
        logAction("Added new user: " + user.getName() + " (" + user.getUserType() + ")");
        System.out.println("User added successfully.");
    }

    public void removeUser(String userId, List<User> userList) {
        userList.removeIf(u -> u.getUserId().equals(userId));
        logAction("Removed user with ID: " + userId);
        System.out.println("User removed successfully.");
    }

    @Override
    public String getUserType() {
        return "Administrator";
    }

    @Override
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== ADMIN AUDIT REPORT =====\n");
        sb.append("Admin: ").append(name).append("\n");
        sb.append("Total Audit Logs: ").append(auditLogs.size()).append("\n");
        return sb.toString();
    }

    @Override
    public void displayReport() {
        System.out.println(generateReport());
    }

    // Getters
    public String getAdminId() { return adminId; }
    public List<AuditLog> getAuditLogs() { return auditLogs; }

    @Override
    public String getDisplayInfo() {
        return String.format("Admin ID: %s | Name: %s | Email: %s | Audit Logs: %d",
                adminId, name, email, auditLogs.size());
    }
}

