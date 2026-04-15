
package enums;

public enum Priority {
    LOW("Low", 1),
    MEDIUM("Medium", 2),
    HIGH("High", 3);

    private final String displayName;
    private final int level;

    Priority(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevel() {
        return level;
    }

    public static Priority fromString(String text) {
        for (Priority p : Priority.values()) {
            if (p.displayName.equalsIgnoreCase(text) || p.name().equalsIgnoreCase(text)) {
                return p;
            }
        }
        return null;
    }
}
