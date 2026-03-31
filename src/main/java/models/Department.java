/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import interfaces.Displayable;
import java.util.ArrayList;
import java.util.List;

public class Department implements Displayable {
    private String departmentId;
    private String departmentName;
    private List<Staff> staffMembers;
    private List<Ticket> departmentTickets;

    public Department(String departmentId, String departmentName) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.staffMembers = new ArrayList<>();
        this.departmentTickets = new ArrayList<>();
    }

    public void routeTicket(Ticket ticket) {
        departmentTickets.add(ticket);
        System.out.println("Ticket " + ticket.getTicketId() + " routed to " + departmentName);
    }

    public void assignTicketToStaff(Ticket ticket, Staff staff) {
        if (staffMembers.contains(staff)) {
            staff.takeTicket(ticket);
            System.out.println("Ticket assigned to " + staff.getName() + " in " + departmentName);
        } else {
            System.out.println("Staff member not in this department.");
        }
    }

    public void displayUnsolvedTicketNumber() {
        long count = departmentTickets.stream()
            .filter(t -> t.getStatus() != enums.TicketStatus.CLOSED && 
                        t.getStatus() != enums.TicketStatus.RESOLVED)
            .count();
        System.out.println("Unsolved tickets in " + departmentName + ": " + count);
    }

    public void addStaff(Staff staff) {
        staffMembers.add(staff);
        staff.setDepartmentId(departmentId);
    }

    public void removeStaff(Staff staff) {
        staffMembers.remove(staff);
    }

    @Override
    public void display() {
        System.out.println(getDisplayInfo());
    }

    @Override
    public String getDisplayInfo() {
        return String.format("Department: %s | ID: %s | Staff Count: %d | Tickets: %d",
                departmentName, departmentId, staffMembers.size(), departmentTickets.size());
    }

    // Getters and Setters
    public String getDepartmentId() { return departmentId; }
    public String getDepartmentName() { return departmentName; }
    public List<Staff> getStaffMembers() { return staffMembers; }
    public List<Ticket> getDepartmentTickets() { return departmentTickets; }

    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
}
