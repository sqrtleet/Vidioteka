package org.example.entity.enums;

public enum OrderStatus {
    NEW("NEW"),
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED"),
    OVERDUE("OVERDUE");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}
