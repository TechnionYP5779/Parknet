package com.team4.parknet.entities;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

    public static ParkingLotOffer buildFromDB(Object offer){
        final HashMap offer_data = (HashMap) ((HashMap)offer).get("data");
        final Double loc_lat = (Double)((HashMap) ((HashMap) offer_data).get("address")).get("_latitude");
        final Double loc_lon = (Double)((HashMap) ((HashMap) offer_data).get("address")).get("_longitude");
        final Object price_obj = ((HashMap) offer_data).get("price");
        final Float price =  price_obj instanceof Integer ? Float.valueOf((Integer)price_obj) : Float.valueOf(((Double)price_obj).floatValue());
        final String owner = (String)((HashMap)offer_data).get("owner");
        final Date startTime = new Date(); //TODO: FIX!!!!
        final Date endTime = new Date(); //TODO: FIX!!!!
        final List<TimeSlot> avail = new ArrayList<>();
        final List<Object> l = (List<Object>)((HashMap) offer_data).get("availability");

        for(Object o : l){
            final Boolean available= (Boolean)((HashMap)o).get("available");
            avail.add(new TimeSlot(available, new Date(), new Date())); //TODO: FIX!!!!
        }


        ParkingLotOffer offer_to_ret =  new ParkingLotOffer(owner, new GeoPoint(loc_lat, loc_lon), price, startTime, endTime);
        offer_to_ret.setAvailability(avail);

        return offer_to_ret;
    }
}
