package com.team4.parknet.entities;

import java.util.Date;

public class ParkingLotOffer {
    String address;
    float price;
    Date startTime;
    Date endTime;
    String owner;

    public ParkingLotOffer() {

    }

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
}
