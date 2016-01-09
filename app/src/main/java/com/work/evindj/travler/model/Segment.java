package com.work.evindj.travler.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by evindj on 1/4/16.
 */
public class Segment implements Serializable {
    @Expose
    private  long duration;
    @Expose
    protected Flight flight;
    @Expose
    @SerializedName("leg")
    protected ArrayList<Leg> legs;
    @Expose
    private long connectionDuration;
    public Segment(){

    }

    public long getConnectionDuration() {
        return connectionDuration;
    }

    public void setConnectionDuration(long connectionDuration) {
        this.connectionDuration = connectionDuration;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public ArrayList<Leg> getLegs() {
        return legs;
    }

    public void setLegs(ArrayList<Leg> legs) {
        this.legs = legs;
    }


}
