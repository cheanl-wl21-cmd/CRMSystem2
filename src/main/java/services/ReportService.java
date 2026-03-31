/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
        String reportId = "RPT-" + System.currentTimeMillis();
        Report report = new Report(reportId, month, year);
        List<Ticket> allTickets = dataStore.getTickets();
        report.generateReport(allTickets);
        return report;
    }
}
