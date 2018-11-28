package com.team4.parknet.entities;

import java.util.Date;

public class ParkingLotOffer {
    String address;
    float price;
    Date startTime;
    Date endTime;
    String owner;

    public ParkingLotOffer(String owner, String address, float price, Date startTime, Date endTime) {
        this.owner = owner;
        this.address = address;
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public float getPrice() {
        return price;
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
}
