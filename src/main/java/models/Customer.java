package models;

import enums.Priority;
import enums.TicketStatus;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Customer extends User {
    private String custId;
    private String address;
    private List<Ticket> tickets;
    private String profilePic = "default.png";
    
    public Customer(){
        
    }

    public Customer(String userId, String name, String email, String password, String address,String profilePic) {
        super(userId, name, email, password);
        this.custId = "CUST-" + userId;
        this.address = address;
        this.tickets = new ArrayList<>();
        this.profilePic=profilePic;
    }
    
    public String getCustId() { return custId; }
    public String getAddress() { return address; }
    public List<Ticket> getTickets() { return tickets; }
   public String getProfilePic() { return profilePic; }
    
    public void setAddress(String address) { this.address = address; }
    public void setProfilePic(String profilePic){  this.profilePic=profilePic;}

    public String register() {
        return "Customer " + name + " registered successfully with ID: " + custId;
    }

    public Ticket createTicket(String ticketId, String productId, String description, Priority priority) {
        Ticket ticket = new Ticket(ticketId, productId, description, priority);
        ticket.setCustomerId(this.custId);
        tickets.add(ticket);
        return ticket;
    }

    public Ticket viewTicket(String ticketId) {
        for (Ticket ticket : tickets) {
            if (ticket.getTicketId().equalsIgnoreCase(ticketId)) {
                return ticket; 
            }
        }
        return null;
    }

    public String closeTicket(String ticketId) {
        for (Ticket ticket : tickets) {
            if (ticket.getTicketId().equalsIgnoreCase(ticketId)) {
                if (ticket.getStatus() == TicketStatus.RESOLVED) {
                    ticket.updateStatus(TicketStatus.CLOSED);
                    return "Success: Ticket " + ticketId + " has been closed.";
                } else {
                    return "Error: Ticket must be resolved before closing.";
                }
            }
        }
        return "Error: Ticket not found!";
    }

    public String editTicket(String ticketId, String description, Priority priority) {
        for (Ticket ticket : tickets) {
            if (ticket.getTicketId().equalsIgnoreCase(ticketId)) {
                if (ticket.getStatus() == TicketStatus.OPEN) {
                    ticket.setDescription(description);
                    ticket.setPriority(priority);
                    return "Success: Ticket updated successfully.";
                } else {
                    return "Error: Cannot edit ticket that is already being processed.";
                }
            }
        }
        return "Error: Ticket not found!";
    }

    public String giveFeedback(String ticketId, int rating, String comment) {
        for (Ticket ticket : tickets) {
            if (ticket.getTicketId().equalsIgnoreCase(ticketId) && ticket.getStatus() == TicketStatus.CLOSED) {
                Feedback feedback = new Feedback(
                    "FB-" + System.currentTimeMillis(),
                    rating,
                    comment,
                    ticketId
                );
                ticket.setFeedback(feedback);
                return "Success: Feedback submitted successfully.";
            }
        }
        return "Error: Cannot submit feedback. Ticket must be closed first.";
    }

    public String submitTicket(Ticket ticket) {
        if (!tickets.contains(ticket)) {
            tickets.add(ticket);
        }
        ticket.updateStatus(TicketStatus.OPEN);
        return "Success: Ticket submitted.";
    }

    @Override
    public String getUserType() {
        return "Customer";
    }

    @Override
    public String getDisplayInfo() {
        return String.format("Customer ID: %s | Name: %s | Email: %s | Address: %s | Total Tickets: %d",
                custId, name, email, address, tickets.size());
    }
    
   @Override
    public String toCSV() {
        return "Customer," + userId + "," + name + "," + email + "," + password + "," + address+"," + profilePic;
    }
}