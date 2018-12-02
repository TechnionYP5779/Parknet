package com.team4.parknet.entities;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParkingLotOffer {
    String address;
    float price;
    Date startTime;
    Date endTime;
    String owner;

    List<Boolean> availability;

    public ParkingLotOffer() {

    }

    public ParkingLotOffer(String owner, String address, float price, Date startTime, Date endTime) {
        this.owner = owner;
        this.address = address;
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
        int hours = (int)getDurationInHours();
        this.availability = new ArrayList<>(hours);
        for (int i=0; i<hours; ++i) {
            this.availability.add(i, true);
        }
    }

    @Exclude
    public double getDurationInHours() {
        long secs = (getEndTime().getTime() - getStartTime().getTime()) / 1000;
        return (secs / 3600.0);
    }

    public String getAddress() {
        return address;
    }

    public float getPrice() {
        return price;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getOwner() {
        return owner;
    }

    public List<Boolean> getAvailability() {
        return availability;
    }
}
