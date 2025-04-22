package com.card_management.shared;

public enum TimeRange {
    DAY(1),
    WEEK(7),
    MONTH(30),
    YEAR(360);

    private final int days;

    TimeRange(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }
}
