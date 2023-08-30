package org.example.entity.enums;

public enum CollateralType {
    PASSPORT("Passport"),
    MONEY("Money"),
    ITEM("Item");

    private final String type;
    private String value;

    CollateralType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
