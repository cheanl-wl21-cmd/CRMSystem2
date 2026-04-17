package models;

import interfaces.Displayable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Feedback implements Displayable {
    private String feedbackId;
    private int rating; // 1-5
    private String comment;
    private Date feedbackDate;
    private String ticketId;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Feedback(){
        
    }
    public Feedback(String feedbackId, int rating, String comment, String ticketId) {
        this.feedbackId = feedbackId;
        this.rating = Math.max(1, Math.min(5, rating)); // Ensure rating is between 1-5
        this.comment = comment;
        this.ticketId = ticketId;
        this.feedbackDate = new Date();
    }
    
    public String getFeedbackId() { return feedbackId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public Date getFeedbackDate() { return feedbackDate; }
    public String getTicketId() { return ticketId; }

    public void setRating(int rating) { this.rating = Math.max(1, Math.min(5, rating)); }
    public void setComment(String comment) { this.comment = comment; }

   
    public String submitFeedback() {
        return "Feedback submitted successfully!\n" + getFeedbackDetailsString();
    }

   
    public String getFeedbackDetailsString() {
        return "Rating: " + "★".repeat(rating) + "☆".repeat(5 - rating) + " (" + rating + "/5)\n" +
               "Comment: " + comment + "\n" +
               "Date: " + DATE_FORMAT.format(feedbackDate);
    }

    
    @Override
    public String getDisplayInfo() {
        return String.format("Feedback ID: %s | Rating: %d/5 | Ticket: %s", feedbackId, rating, ticketId);
    }
}