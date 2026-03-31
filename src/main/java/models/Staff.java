/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import enums.Priority;
import enums.StaffRole;
import enums.TicketStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Staff extends User {
    private String staffId;
    private StaffRole staffRole;
    private String departmentId;
    private List<Ticket> assignedTickets;

    public Staff(String userId, String name, String email, String password, StaffRole role, String departmentId) {
        super(userId, name, email, password);
        this.staffId = "STAFF-" + userId;
        this.staffRole = role;
        this.departmentId = departmentId;
        this.assignedTickets = new ArrayList<>();
    }

    public List<Ticket> viewAllTickets(List<Ticket> allTickets) {
        System.out.println("\n===== ALL TICKETS =====");
        for (Ticket ticket : allTickets) {
            System.out.println(ticket.getDisplayInfo());
        }
        return allTickets;
    }

    public List<Ticket> searchTicketByKeyword(List<Ticket> allTickets, String keyword) {
        List<Ticket> results = allTickets.stream()
            .filter(t -> t.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                        t.getTicketId().toLowerCase().contains(keyword.toLowerCase()) ||
                        t.getProductId().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
        
        System.out.println("\n===== SEARCH RESULTS FOR: " + keyword + " =====");
        for (Ticket ticket : results) {
            System.out.println(ticket.getDisplayInfo());
        }
        return results;
    }

    public List<Ticket> filterTicketsByStatus(List<Ticket> allTickets, TicketStatus status) {
        List<Ticket> results = allTickets.stream()
            .filter(t -> t.getStatus() == status)
            .collect(Collectors.toList());
        
        System.out.println("\n===== TICKETS WITH STATUS: " + status.getDisplayName() + " =====");
        for (Ticket ticket : results) {
            System.out.println(ticket.getDisplayInfo());
        }
        return results;
    }

    public List<Ticket> filterTicketsByPriority(List<Ticket> allTickets, Priority priority) {
        List<Ticket> results = allTickets.stream()
            .filter(t -> t.getPriority() == priority)
            .collect(Collectors.toList());
        
        System.out.println("\n===== TICKETS WITH PRIORITY: " + priority.getDisplayName() + " =====");
        for (Ticket ticket : results) {
            System.out.println(ticket.getDisplayInfo());
        }
        return results;
    }

    public void updateTicketStatus(Ticket ticket, TicketStatus status) {
        ticket.updateStatus(status);
        System.out.println("Ticket " + ticket.getTicketId() + " status updated to: " + status.getDisplayName());
    }

    public void addResponse(Ticket ticket, String message) {
        Message response = new Message(
            "MSG-" + System.currentTimeMillis(),
            ticket.getTicketId(),
            this.staffId,
            message
        );
        ticket.addMessage(response);
        System.out.println("Response added to ticket " + ticket.getTicketId());
    }

    public void assignTicket(Ticket ticket, Staff targetStaff) {
        if (this.staffRole == StaffRole.MANAGER || this.staffRole == StaffRole.TEAM_LEAD) {
            ticket.setAssignedStaffId(targetStaff.getStaffId());
            targetStaff.assignedTickets.add(ticket);
            System.out.println("Ticket " + ticket.getTicketId() + " assigned to " + targetStaff.getName());
        } else {
            System.out.println("Only managers or team leads can assign tickets.");
        }
    }

    public void takeTicket(Ticket ticket) {
        ticket.setAssignedStaffId(this.staffId);
        if (!assignedTickets.contains(ticket)) {
            assignedTickets.add(ticket);
        }
        ticket.updateStatus(TicketStatus.IN_PROGRESS);
        System.out.println("Ticket " + ticket.getTicketId() + " is now assigned to you.");
    }

    @Override
    public String getUserType() {
        return "Staff - " + staffRole.getDisplayName();
    }

    // Getters and Setters
    public String getStaffId() { return staffId; }
    public StaffRole getStaffRole() { return staffRole; }
    public String getDepartmentId() { return departmentId; }
    public List<Ticket> getAssignedTickets() { return assignedTickets; }

    public void setStaffRole(StaffRole staffRole) { this.staffRole = staffRole; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    @Override
    public String getDisplayInfo() {
        return String.format("Staff ID: %s | Name: %s | Role: %s | Department: %s | Assigned Tickets: %d",
                staffId, name, staffRole.getDisplayName(), departmentId, assignedTickets.size());
    }
}

