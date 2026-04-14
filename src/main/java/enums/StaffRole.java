
package enums;

public enum StaffRole {
    SUPPORT_AGENT("Support Agent"),
    SENIOR_AGENT("Senior Agent"),
    TEAM_LEAD("Team Lead"),
    MANAGER("Manager");

    private final String displayName;

    StaffRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static StaffRole fromString(String text) {
        for (StaffRole role : StaffRole.values()) {
            if (role.displayName.equalsIgnoreCase(text) || role.name().equalsIgnoreCase(text)) {
                return role;
            }
        }
        return null;
    }
}

