package com.work.evindj.travler.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by evindj on 1/4/16.
 */
public class TripOption implements Serializable {

    @Expose
    private String saleTotal;
    @Expose
    @SerializedName("slice")
    private ArrayList<Slice> slices;
    public TripOption(String saleTotal) {
        this.saleTotal = saleTotal;
    }
    public ArrayList<Slice> getSlices() {
        return slices;
    }

    public void setSlices(ArrayList<Slice> slices) {
        this.slices = slices;
    }

    public String getSaleTotal() {
        return saleTotal;
    }

    public void setSaleTotal(String saleTotal) {
        this.saleTotal = saleTotal;
    }


}
