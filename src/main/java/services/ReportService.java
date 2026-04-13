
package services;

import models.Report;
import models.Ticket;
import java.util.List;

public class ReportService {
    private DataStore dataStore;

    public ReportService() {
        this.dataStore = DataStore.getInstance();
    }

    public Report generateMonthlyReport(int month, int year) {
        // 1. Validate the Month
        if (month < 1 || month > 12) {
            System.out.println("Report Generation Failed: Month must be between 1 and 12.");
            return null;
        }

        // 2. Validate the Year 
        if (year < 2020 || year > 2100) {
            System.out.println("Report Generation Failed: Invalid year. Please enter a year between 2020 and 2100.");
            return null;
        }

      
        String reportId = "RPT-" + System.currentTimeMillis(); 
        Report report = new Report(reportId, month, year);
        
        List<Ticket> allTickets = dataStore.getTickets();
        report.generateReport(allTickets);
        
        System.out.println("Report successfully generated for " + month + "/" + year);
        return report;
    }
}
