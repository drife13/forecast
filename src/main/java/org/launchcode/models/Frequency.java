package org.launchcode.models;

public enum Frequency {
    SINGLE("Single"),
    DAILY("Daily"),
    WEEKLY("Weekly"),
    BIWEEKLY("Bi-Weekly"),
    MONTHLY("Monthly"),
    ANNUALLY("Annually");

    private final String name;
    Frequency(String name) {
        this.name = name;
    }

    public String getName() { return name; }
}


