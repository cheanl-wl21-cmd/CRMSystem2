package models;

import interfaces.Displayable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AuditLog implements Displayable {
    private String logId;
    private String actionDone;
    private String actionBy;
    private Date timestamp;
    private String adminId;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public AuditLog(){
    }
    
    public AuditLog(String logId, String actionDone, String adminId) {
        this.logId = logId;
        this.actionDone = actionDone;
        this.adminId = adminId;
        this.actionBy = adminId;
        this.timestamp = new Date();
    }
    
    public String getLogId() { return logId; }
    public String getActionDone() { return actionDone; }
    public String getActionBy() { return actionBy; }
    public Date getTimestamp() { return timestamp; }
    public String getAdminId() { return adminId; }
    
    public void recordAction(String action) {
        this.actionDone = action;
        this.timestamp = new Date();
    }

    

    @Override
    public String getDisplayInfo() {
        return String.format("Log ID: %s | Action: %s | By: %s | Time: %s",
                logId, actionDone, actionBy, DATE_FORMAT.format(timestamp));
    }

    
}