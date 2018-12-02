package com.team4.parknet.entities;

import java.util.Date;

public class TimeSlot {

    boolean available;
    Date startTime;
    Date endTime;

    public TimeSlot(boolean available, Date startTime, Date endTime) {
        this.available = available;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeSlot() {
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
