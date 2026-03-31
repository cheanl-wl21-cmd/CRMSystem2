/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    public Customer(String userId, String name, String email, String password, String address) {
        super(userId, name, email, password);
        this.custId = "CUST-" + userId;
        this.address = address;
        this.tickets = new ArrayList<>();
    }

    public void register() {
        System.out.println("Customer " + name + " registered successfully with ID: " + custId);
    }

    public Ticket createTicket(String ticketId, String productId, String description, Priority priority) {
        Ticket ticket = new Ticket(ticketId, productId, description, priority);
        ticket.setCustomerId(this.custId);
        tickets.add(ticket);
        System.out.println("Ticket created successfully with ID: " + ticketId);
        return ticket;
    }

    public Ticket viewTicket(String ticketId) {
        for (Ticket ticket : tickets) {
            if (ticket.getTicketId().equals(ticketId)) {
                ticket.displayTicketDetails();
                return ticket;
            }
        }
        System.out.println("Ticket not found!");
        return null;
    }

    public void closeTicket(String ticketId) {
        for (Ticket ticket : tickets) {
            if (ticket.getTicketId().equals(ticketId)) {
                if (ticket.getStatus() == TicketStatus.RESOLVED) {
                    ticket.updateStatus(TicketStatus.CLOSED);
                    ticket.setDateClosed(new Date());
                    System.out.println("Ticket " + ticketId + " has been closed.");
                } else {
                    System.out.println("Ticket must be resolved before closing.");
                }
                return;
            }
        }
        System.out.println("Ticket not found!");
    }

    public void editTicket(String ticketId, String description, Priority priority) {
        for (Ticket ticket : tickets) {
            if (ticket.getTicketId().equals(ticketId)) {
                if (ticket.getStatus() == TicketStatus.OPEN) {
                    ticket.setDescription(description);
                    ticket.setPriority(priority);
                    System.out.println("Ticket updated successfully.");
                } else {
                    System.out.println("Cannot edit ticket that is already being processed.");
                }
                return;
            }
        }
        System.out.println("Ticket not found!");
    }

    public Feedback giveFeedback(String ticketId, int rating, String comment) {
        for (Ticket ticket : tickets) {
            if (ticket.getTicketId().equals(ticketId) && ticket.getStatus() == TicketStatus.CLOSED) {
                Feedback feedback = new Feedback(
                    "FB-" + System.currentTimeMillis(),
                    rating,
                    comment,
                    ticketId
                );
                ticket.setFeedback(feedback);
                System.out.println("Feedback submitted successfully.");
                return feedback;
            }
        }
        System.out.println("Cannot submit feedback. Ticket must be closed first.");
        return null;
    }

    public void submitTicket(Ticket ticket) {
        if (!tickets.contains(ticket)) {
            tickets.add(ticket);
        }
        ticket.updateStatus(TicketStatus.OPEN);
        System.out.println("Ticket submitted successfully.");
    }

    @Override
    public String getUserType() {
        return "Customer";
    }

    // Getters and Setters
    public String getCustId() { return custId; }
    public String getAddress() { return address; }
    public List<Ticket> getTickets() { return tickets; }
    
    public void setAddress(String address) { this.address = address; }

    @Override
    public String getDisplayInfo() {
        return String.format("Customer ID: %s | Name: %s | Email: %s | Address: %s | Total Tickets: %d",
                custId, name, email, address, tickets.size());
    }
}

