package models;

import enums.Priority;
import enums.StaffRole;
import enums.TicketStatus;
import java.util.ArrayList;
import java.util.List;

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

    // --- LIST FILTERING METHODS (No Streams, No Printing) ---

    public List<Ticket> searchTicketByKeyword(List<Ticket> allTickets, String keyword) {
        List<Ticket> results = new ArrayList<>();
        String searchWord = keyword.toLowerCase();
        
        for (Ticket t : allTickets) {
            if (t.getDescription().toLowerCase().contains(searchWord) ||
                t.getTicketId().toLowerCase().contains(searchWord) ||
                t.getProductId().toLowerCase().contains(searchWord)) {
                results.add(t);
            }
        }
        return results;
    }

    public List<Ticket> filterTicketsByStatus(List<Ticket> allTickets, TicketStatus status) {
        List<Ticket> results = new ArrayList<>();
        for (Ticket t : allTickets) {
            if (t.getStatus() == status) {
                results.add(t);
            }
        }
        return results;
    }

    public List<Ticket> filterTicketsByPriority(List<Ticket> allTickets, Priority priority) {
        List<Ticket> results = new ArrayList<>();
        for (Ticket t : allTickets) {
            if (t.getPriority() == priority) {
                results.add(t);
            }
        }
        return results;
    }

    // --- ACTION METHODS (Returning Strings instead of Printing) ---

    public String updateTicketStatus(Ticket ticket, TicketStatus status) {
        ticket.updateStatus(status);
        return "Success: Ticket " + ticket.getTicketId() + " status updated to " + status.getDisplayName();
    }

    public String addResponse(Ticket ticket, String message) {
        Message response = new Message(
            "MSG-" + System.currentTimeMillis(),
            ticket.getTicketId(),
            this.staffId,
            message
        );
        ticket.addMessage(response);
        return "Success: Response added to ticket " + ticket.getTicketId();
    }

    public String assignTicket(Ticket ticket, Staff targetStaff) {
        if (this.staffRole == StaffRole.MANAGER || this.staffRole == StaffRole.TEAM_LEAD) {
            ticket.setAssignedStaffId(targetStaff.getStaffId());
            targetStaff.assignedTickets.add(ticket);
            return "Success: Ticket " + ticket.getTicketId() + " assigned to " + targetStaff.getName();
        } else {
            return "Error: Only managers or team leads can assign tickets.";
        }
    }

    public String takeTicket(Ticket ticket) {
        ticket.setAssignedStaffId(this.staffId);
        if (!assignedTickets.contains(ticket)) {
            assignedTickets.add(ticket);
        }
        ticket.updateStatus(TicketStatus.IN_PROGRESS);
        return "Success: Ticket " + ticket.getTicketId() + " is now assigned to you.";
    }

    

    @Override
    public String getUserType() {
        return "Staff - " + staffRole.getDisplayName();
    }

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