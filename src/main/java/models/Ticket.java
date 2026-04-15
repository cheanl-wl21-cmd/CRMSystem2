package models;

import enums.Priority;
import enums.TicketStatus;
import interfaces.Displayable;
import java.text.SimpleDateFormat;
import java.util.ArrayList; 
import java.util.Date;
import java.util.List;

public class Ticket implements Displayable {  
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

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
    
    public String getTicketId() { return ticketId; }
    public String getProductId() { return productId; }
    public String getCustomerId() { return customerId; }
    public String getAssignedStaffId() { return assignedStaffId; }
    public String getDescription() { return description; }
    public Priority getPriority() { return priority; }
    public TicketStatus getStatus() { return status; }
    public Date getDateSubmitted() { return dateSubmitted; }
    public Date getDateClosed() { return dateClosed; }
    public List<Message> getMessages() { return messages; }
    public List<Attachment> getAttachments() { return attachments; }
    public Feedback getFeedback() { return feedback; }

    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setAssignedStaffId(String assignedStaffId) { this.assignedStaffId = assignedStaffId; }
    public void setDescription(String description) { this.description = description; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setDateClosed(Date dateClosed) { this.dateClosed = dateClosed; }
    public void setFeedback(Feedback feedback) { this.feedback = feedback; }


    
    public void addMessage(Message msg) {
        messages.add(msg);
    }

    public void updateStatus(TicketStatus status) {
        this.status = status;
        if (status == TicketStatus.CLOSED) {
            this.dateClosed = new Date();
        }
    }

    
    public void addAttachment(Attachment att) {
        attachments.add(att);
    }

    
    public String getTicketDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("=".repeat(50)).append("\n");
        sb.append("TICKET DETAILS\n");
        sb.append("=".repeat(50)).append("\n");
        sb.append("Ticket ID: ").append(ticketId).append("\n");
        sb.append("Product ID: ").append(productId).append("\n");
        sb.append("Customer ID: ").append(customerId).append("\n");
        sb.append("Assigned Staff: ").append(assignedStaffId != null ? assignedStaffId : "Unassigned").append("\n");
        sb.append("Description: ").append(description).append("\n");
        sb.append("Priority: ").append(priority.getDisplayName()).append("\n");
        sb.append("Status: ").append(status.getDisplayName()).append("\n");
        sb.append("Date Submitted: ").append(DATE_FORMAT.format(dateSubmitted)).append("\n");
        
        if (dateClosed != null) {
            sb.append("Date Closed: ").append(DATE_FORMAT.format(dateClosed)).append("\n");
        }
        
        if (!messages.isEmpty()) {
            sb.append("\n--- Messages ---\n");
            for (Message msg : messages) {
               
                sb.append(msg.getDisplayInfo()).append("\n"); 
            }
        }
        
        if (!attachments.isEmpty()) {
            sb.append("\n--- Attachments ---\n");
            for (Attachment att : attachments) {
                sb.append("  - ").append(att.getFileName()).append(" (").append(att.getFileSizeMB()).append(" MB)\n");
            }
        }
        
        if (feedback != null) {
            sb.append("\n--- Feedback ---\n");
            
            sb.append(feedback.getDisplayInfo()).append("\n"); 
        }
        sb.append("=".repeat(50));
        
        return sb.toString();
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
    
    @Override
    public void display() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}