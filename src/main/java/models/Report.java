package models;

import enums.TicketStatus;
import interfaces.Displayable;
import interfaces.Reportable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Report implements Displayable, Reportable {
    private String reportId;
    private Date reportDate;
    private int reportMonth;
    private int reportYear;
    private int totalTicketsSubmitted;
    private int totalTicketsResolved;
    private double averageResponseTime; // in hours

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    public Report(){
        
    }

    public Report(String reportId, int month, int year) {
        this.reportId = reportId;
        this.reportMonth = month;
        this.reportYear = year;
        this.reportDate = new Date();
    }
    
    public String getReportId() { return reportId; }
    public Date getReportDate() { return reportDate; }
    public int getTotalTicketsSubmitted() { return totalTicketsSubmitted; }
    public int getTotalTicketsResolved() { return totalTicketsResolved; }
    public double getAverageResponseTime() { return averageResponseTime; }

   
    public String generateReport(List<Ticket> allTickets) {
        Calendar calendar = Calendar.getInstance();
        
        totalTicketsSubmitted = 0;
        totalTicketsResolved = 0;
        long totalResponseTime = 0;
        int responsesCount = 0;

        for (Ticket ticket : allTickets) {
            calendar.setTime(ticket.getDateSubmitted());
            if (calendar.get(Calendar.MONTH) + 1 == reportMonth && calendar.get(Calendar.YEAR) == reportYear) {
                totalTicketsSubmitted++;
                
                if (ticket.getStatus() == TicketStatus.RESOLVED || ticket.getStatus() == TicketStatus.CLOSED) {
                    totalTicketsResolved++;
                    
                    if (ticket.getDateClosed() != null) {
                        long different = ticket.getDateClosed().getTime() - ticket.getDateSubmitted().getTime();
                        totalResponseTime += different;
                        responsesCount++;
                    }
                }
            }
        }

        if (responsesCount > 0) {
            averageResponseTime = (totalResponseTime / responsesCount) / (1000.0 * 60 * 60); // Convert to hours
        }

        return "Report generated successfully.";
    }

    @Override
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("=".repeat(50)).append("\n");
        sb.append("MONTHLY REPORT - ").append(reportMonth).append("/").append(reportYear).append("\n");
        sb.append("=".repeat(50)).append("\n");
        sb.append("Report ID: ").append(reportId).append("\n");
        sb.append("Generated on: ").append(DATE_FORMAT.format(reportDate)).append("\n");
        sb.append("-".repeat(50)).append("\n");
        sb.append("Total Tickets Submitted: ").append(totalTicketsSubmitted).append("\n");
        sb.append("Total Tickets Resolved: ").append(totalTicketsResolved).append("\n");
        sb.append("Resolution Rate: ").append(
            totalTicketsSubmitted > 0 ? 
            String.format("%.1f%%", (totalTicketsResolved * 100.0 / totalTicketsSubmitted)) : 
            "N/A"
        ).append("\n");
        sb.append("Average Response Time: ").append(String.format("%.2f hours", averageResponseTime)).append("\n");
        sb.append("=".repeat(50));
        return sb.toString();
    }

    @Override
    public void displayReport() {
       
    }

    
    @Override
    public String getDisplayInfo() {
        return String.format("Report: %s | Period: %d/%d | Submitted: %d | Resolved: %d",
                reportId, reportMonth, reportYear, totalTicketsSubmitted, totalTicketsResolved);
    }
}