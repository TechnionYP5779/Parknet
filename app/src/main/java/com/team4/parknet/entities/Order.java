package com.team4.parknet.entities;

import java.util.Date;
import java.util.List;

public class Order {
    String offerId;
    Date startTime;
    Date endTime;
    String uid;
    List<TimeSlot> timesOrdered;


    public Order() {
    }

    public Order(String offerId, Date startTime, Date endTime, String uid, List<TimeSlot> timesOrdered) {
        this.offerId = offerId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.uid = uid;
        this.timesOrdered = timesOrdered;
    }

    public List<TimeSlot> getTimesOrdered() {
        return timesOrdered;
    }

    public void setTimesOrdered(List<TimeSlot> timesOrdered) {
        this.timesOrdered = timesOrdered;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
