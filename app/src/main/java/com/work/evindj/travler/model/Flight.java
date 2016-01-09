package com.work.evindj.travler.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by evindj on 1/4/16.
 */
public class Flight implements Serializable {
    @Expose
    private String carrier;
    @Expose
    private String number;
    public Flight(){

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }


}
