package com.aviral.bookmyslot.Models;

public class TimeSlot {

    private final int initialTime;
    private final int startTime;
    private final int interval;

    public TimeSlot(int initialTime, int startTime, int interval) {
        this.initialTime = initialTime;
        this.startTime = startTime;
        this.interval = interval;
    }

    public int getInitialTime() {
        return initialTime;
    }

    public int getInterval() {
        return interval;
    }

    public int getStartTime() {
        return startTime;
    }
}
