package com.aviral.bookmyslot.Models;

public class TimeModel {

    private final String time;
    private final boolean isBooked;

    public TimeModel(String time, boolean isBooked) {
        this.time = time;
        this.isBooked = isBooked;
    }

    public String getTime() {
        return time;
    }

    public boolean isBooked() {
        return isBooked;
    }
}
