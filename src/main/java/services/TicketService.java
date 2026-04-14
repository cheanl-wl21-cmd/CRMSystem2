
package services;

import enums.Priority;
import enums.TicketStatus;
import java.util.ArrayList;
import models.*;
import java.util.List;


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
        List<Ticket> resultList = new ArrayList<>();
        
        
        for (Ticket t : dataStore.getTickets()) {
            if (t.getStatus() == status) {
                resultList.add(t);
            }
        }
        return resultList;
    }

    public List<Ticket> getTicketsByPriority(Priority priority) {
        List<Ticket> resultList = new ArrayList<>();
        
        for (Ticket t : dataStore.getTickets()) {
            if (t.getPriority() == priority) {
                resultList.add(t);
            }
        }
        return resultList;
    }

    public List<Ticket> searchTickets(String keyword) {
        List<Ticket> resultList = new ArrayList<>();
        String searchWord = keyword.toLowerCase(); 
        
        for (Ticket t : dataStore.getTickets()) {
            if (t.getDescription().toLowerCase().contains(searchWord) || 
                t.getTicketId().toLowerCase().contains(searchWord)) {
                
                resultList.add(t);
            }
        }
        return resultList;
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
