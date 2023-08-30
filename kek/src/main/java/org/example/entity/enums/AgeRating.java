package org.example.entity.enums;

public enum AgeRating {
    G("General Audiences"),
    PG("Parental Guidance Suggested"),
    PG13("Parents Strongly Cautioned"),
    R("Restricted"),
    NC17("No One 17 and Under Admitted");

    private final String description;

    AgeRating(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
