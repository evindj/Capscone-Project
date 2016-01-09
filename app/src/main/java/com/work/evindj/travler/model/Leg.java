package com.work.evindj.travler.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by evindj on 1/4/16.
 */
public class Leg implements Serializable {
    @Expose
    private String aircraft;
    @Expose
    private String arrivalTime;
    @Expose
    private String departureTime;
    @Expose
    private String origin;
    @Expose
    private String destination;
    @Expose
    private long duration;
    @Expose
    private long mileage;
    @Expose
    private boolean secure;

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getAircraft() {
        return aircraft;
    }

    public void setAircraft(String aircraft) {
        this.aircraft = aircraft;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getMileage() {
        return mileage;
    }

    public void setMileage(long mileage) {
        this.mileage = mileage;
    }


}
