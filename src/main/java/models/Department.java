package models;

import interfaces.Displayable;
import java.util.ArrayList;
import java.util.List;

public class Department implements Displayable {
    private String departmentId;
    private String departmentName;
    private List<Staff> staffMembers;
    private List<Ticket> departmentTickets;
    
    public Department(){
        
    }

    public Department(String departmentId, String departmentName) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.staffMembers = new ArrayList<>();
        this.departmentTickets = new ArrayList<>();
    }
    
    public String getDepartmentId() { return departmentId; }
    public String getDepartmentName() { return departmentName; }
    public List<Staff> getStaffMembers() { return staffMembers; }
    public List<Ticket> getDepartmentTickets() { return departmentTickets; }

    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String routeTicket(Ticket ticket) {
        departmentTickets.add(ticket);
        return "Ticket " + ticket.getTicketId() + " routed to " + departmentName;
    }

    public String assignTicketToStaff(Ticket ticket, Staff staff) {
        if (staffMembers.contains(staff)) {
            staff.takeTicket(ticket); 
            return "Success: Ticket assigned to " + staff.getName() + " in " + departmentName;
        } else {
            return "Error: Staff member not in this department.";
        }
    }

    public String getUnsolvedTicketNumberString() {
        int count = 0;
        for (Ticket t : departmentTickets) {
            if (t.getStatus() != enums.TicketStatus.CLOSED && 
                t.getStatus() != enums.TicketStatus.RESOLVED) {
                count++;
            }
        }
        return "Unsolved tickets in " + departmentName + ": " + count;
    }

    public void addStaff(Staff staff) {
        staffMembers.add(staff);
        staff.setDepartmentId(departmentId);
    }

    public void removeStaff(Staff staff) {
        staffMembers.remove(staff);
    }

    @Override
    public String getDisplayInfo() {
        return String.format("Department: %s | ID: %s | Staff Count: %d | Tickets: %d",
                departmentName, departmentId, staffMembers.size(), departmentTickets.size());
    }
}