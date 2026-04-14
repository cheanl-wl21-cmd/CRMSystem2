package enums;

public enum TicketStatus {
    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    PENDING("Pending"),
    RESOLVED("Resolved"),
    CLOSED("Closed");

    private final String displayName;

    TicketStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TicketStatus fromString(String text) {
        for (TicketStatus status : TicketStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text) || status.name().equalsIgnoreCase(text)) {
                return status;
            }
        }
        return null;
    }
}

