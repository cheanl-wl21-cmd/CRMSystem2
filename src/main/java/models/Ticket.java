/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import enums.Priority;
import enums.TicketStatus;
import interfaces.Displayable;
import java.text.SimpleDateFormat;
import java.util.ArrayList; // create arraylist for sorting purpose 
import java.util.Date;
import java.util.List;// create arraylist for sorting purpose 

public class Ticket implements Displayable {  //Abstraction
    private String ticketId;
    private String productId;
    private String customerId;
    private String assignedStaffId;
    private String description;
    private Priority priority;
    private TicketStatus status;
    private Date dateSubmitted;
    private Date dateClosed;
    private List<Message> messages;
    private List<Attachment> attachments;
    private Feedback feedback;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// make a constant data format

    // No-argument constructor
    public Ticket() { 
       
    }
    public Ticket(String ticketId, String productId, String description, Priority priority) {
        this.ticketId = ticketId;
        this.productId = productId;
        this.description = description;
        this.priority = priority;
        this.status = TicketStatus.OPEN;
        this.dateSubmitted = new Date();
        this.messages = new ArrayList<>();
        this.attachments = new ArrayList<>();
    }

    public void addMessage(Message msg) {
        messages.add(msg);
        System.out.println("Message added to ticket.");
    }

    public void updateStatus(TicketStatus status) {
        this.status = status;
        if (status == TicketStatus.CLOSED) {
            this.dateClosed = new Date();
        }
    }

    public void addAttachment(Attachment att) {
        attachments.add(att);
        System.out.println("Attachment added: " + att.getFileName());
    }

    public void displayTicketDetails() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TICKET DETAILS");
        System.out.println("=".repeat(50));
        System.out.println("Ticket ID: " + ticketId);
        System.out.println("Product ID: " + productId);
        System.out.println("Customer ID: " + customerId);
        System.out.println("Assigned Staff: " + (assignedStaffId != null ? assignedStaffId : "Unassigned"));
        System.out.println("Description: " + description);
        System.out.println("Priority: " + priority.getDisplayName());
        System.out.println("Status: " + status.getDisplayName());
        System.out.println("Date Submitted: " + DATE_FORMAT.format(dateSubmitted));
        if (dateClosed != null) {
            System.out.println("Date Closed: " + DATE_FORMAT.format(dateClosed));
        }
        
        if (!messages.isEmpty()) {
            System.out.println("\n--- Messages ---");
            for (Message msg : messages) {
                msg.displayMessage();
            }
        }
        
        if (!attachments.isEmpty()) {
            System.out.println("\n--- Attachments ---");
            for (Attachment att : attachments) {
                System.out.println("  - " + att.getFileName() + " (" + att.getFileSizeMB() + " MB)");
            }
        }
        
        if (feedback != null) {
            System.out.println("\n--- Feedback ---");
            feedback.displayFeedbackDetails();
        }
        System.out.println("=".repeat(50));
    }

    @Override
    public void display() {
        displayTicketDetails();
    }

    @Override
    public String getDisplayInfo() {
        return String.format("[%s] %s | Priority: %s | Status: %s | Submitted: %s",
                ticketId, 
                description.length() > 30 ? description.substring(0, 30) + "..." : description,
                priority.getDisplayName(), 
                status.getDisplayName(),
                DATE_FORMAT.format(dateSubmitted));
    }

    // Getters and Setters
    public String getTicketId() { 
        return ticketId; 
    }
    public String getProductId() { 
        return productId;
    }
    public String getCustomerId() { 
        return customerId; 
    }
    public String getAssignedStaffId() { 
        return assignedStaffId; 
    }
    public String getDescription() {
        return description; 
    }
    public Priority getPriority() { 
        return priority; 
    }
    public TicketStatus getStatus() { 
        return status;
    }
    public Date getDateSubmitted() {
        return dateSubmitted; 
    }
    public Date getDateClosed() { 
        return dateClosed;
    }
    public List<Message> getMessages() { 
        return messages;
    }
    public List<Attachment> getAttachments() { 
        return attachments;
    }
    public Feedback getFeedback() { 
        return feedback;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public void setAssignedStaffId(String assignedStaffId) { 
        this.assignedStaffId = assignedStaffId;
    }
    public void setDescription(String description) { 
        this.description = description; 
    }
    public void setPriority(Priority priority) {
        this.priority = priority; 
    }
    public void setDateClosed(Date dateClosed) { 
        this.dateClosed = dateClosed; 
    }
    public void setFeedback(Feedback feedback) { 
        this.feedback = feedback;
    }
}
