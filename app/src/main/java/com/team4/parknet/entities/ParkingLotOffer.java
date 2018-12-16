package com.team4.parknet.entities;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParkingLotOffer {
    GeoPoint address;
    float price;
    Date startTime;
    Date endTime;
    String owner;

    List<TimeSlot> availability;

    public ParkingLotOffer() {

    }

    public ParkingLotOffer(String owner, GeoPoint address, float price, Date startTime, Date endTime) {
        this.owner = owner;
        this.address = address;
        this.price = price;
        this.startTime = new Date(startTime.getTime() - startTime.getTime() % (3600 * 1000));
        this.endTime = new Date(endTime.getTime() - endTime.getTime() % (3600 * 1000));
        int hours = (int) getDurationInHours();
        this.availability = new ArrayList<>(hours);
        for (int i = 0; i < hours; ++i) {
            Date start = new Date(this.startTime.getTime() + 1000 * 3600 * i);
            Date end = new Date(start.getTime() + 1000 * 3600);
            this.availability.add(i, new TimeSlot(true, start, end));
        }
    }

    @Exclude
    public float getDurationInHours() {
        long secs = (getEndTime().getTime() - getStartTime().getTime()) / 1000;
        return (secs / 3600.0f);
    }

    public GeoPoint getAddress() {
        return address;
    }

    public void setAddress(GeoPoint address) {
        this.address = address;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<TimeSlot> getAvailability() {
        return availability;
    }

    public void setAvailability(List<TimeSlot> availability) {
        this.availability = availability;
    }

    public void setAvailable(int position, Boolean avail) {
        availability.get(position).setAvailable(avail);
    }
}
