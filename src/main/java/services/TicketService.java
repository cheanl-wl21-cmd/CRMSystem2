/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import enums.Priority;
import enums.TicketStatus;
import models.*;
import java.util.List;
import java.util.stream.Collectors;

public class TicketService {
    private DataStore dataStore;

    public TicketService() {
        this.dataStore = DataStore.getInstance();
    }

    public Ticket createTicket(Customer customer, String productId, String description, Priority priority) {
        String ticketId = dataStore.generateTicketId();
        Ticket ticket = customer.createTicket(ticketId, productId, description, priority);
        dataStore.addTicket(ticket);
        return ticket;
    }

    public List<Ticket> getAllTickets() {
        return dataStore.getTickets();
    }

    public List<Ticket> getTicketsByStatus(TicketStatus status) {
        return dataStore.getTickets().stream()
            .filter(t -> t.getStatus() == status)
            .collect(Collectors.toList());
    }

    public List<Ticket> getTicketsByPriority(Priority priority) {
        return dataStore.getTickets().stream()
            .filter(t -> t.getPriority() == priority)
            .collect(Collectors.toList());
    }

    public List<Ticket> searchTickets(String keyword) {
        return dataStore.getTickets().stream()
            .filter(t -> t.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                        t.getTicketId().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }

    public Ticket getTicketById(String ticketId) {
        return dataStore.findTicketById(ticketId);
    }

    public void updateTicketStatus(String ticketId, TicketStatus status) {
        Ticket ticket = getTicketById(ticketId);
        if (ticket != null) {
            ticket.updateStatus(status);
        }
    }

    public void addResponseToTicket(String ticketId, String staffId, String message) {
        Ticket ticket = getTicketById(ticketId);
        if (ticket != null) {
            Message msg = new Message("MSG-" + System.currentTimeMillis(), ticketId, staffId, message);
            ticket.addMessage(msg);
        }
    }

    public void assignTicket(String ticketId, String staffId) {
        Ticket ticket = getTicketById(ticketId);
        Staff staff = dataStore.findStaffById(staffId);
        if (ticket != null && staff != null) {
            ticket.setAssignedStaffId(staffId);
            staff.takeTicket(ticket);
        }
    }

    public List<Ticket> getTicketsByCustomerId(String custId) {
        return dataStore.getTicketsByCustomer(custId);
    }
}
