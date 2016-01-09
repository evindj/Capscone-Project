package com.work.evindj.travler.remote;

import com.google.gson.annotations.Expose;

/**
 * Created by evindj on 1/5/16.
 */
public class SliceRequest{
    @Expose
    private String origin;
    @Expose
    private String destination;

    @Expose
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public SliceRequest(){

    }
    public SliceRequest(String origin, String destination, String date){
        this.origin= origin;
        this.date = date;
        this.destination  = destination;
    }
}
