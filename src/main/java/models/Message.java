
package models;

import interfaces.Displayable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Displayable {
    private String messageId;
    private String ticketId;
    private String senderId;
    private String text;
    private Date timestamp;
    private String departmentId;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Message(String messageId, String ticketId, String senderId, String text) {
        this.messageId = messageId;
        this.ticketId = ticketId;
        this.senderId = senderId;
        this.text = text;
        this.timestamp = new Date();
    }

    public void editText(String newText) {
        this.text = newText;
        System.out.println("Message edited successfully.");
    }

    public void displayMessage() {
        System.out.println("  [" + DATE_FORMAT.format(timestamp) + "] " + senderId + ": " + text);
    }

    @Override
    public void display() {
        displayMessage();
    }

    @Override
    public String getDisplayInfo() {
        return String.format("Message from %s at %s: %s", senderId, DATE_FORMAT.format(timestamp), text);
    }

    // Getters and Setters
    public String getMessageId() { return messageId; }
    public String getTicketId() { return ticketId; }
    public String getSenderId() { return senderId; }
    public String getText() { return text; }
    public Date getTimestamp() { return timestamp; }
    public String getDepartmentId() { return departmentId; }

    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
}
