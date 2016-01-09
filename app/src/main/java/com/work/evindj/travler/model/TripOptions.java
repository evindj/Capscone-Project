package com.work.evindj.travler.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by evindj on 1/5/16.
 */
public class TripOptions implements Serializable {
    public TripOptions(){

    }
    public ArrayList<TripOption> getTripOptions() {
        return tripOptions;
    }

    public void setTripOptions(ArrayList<TripOption> tripOptions) {
        this.tripOptions = tripOptions;
    }
    @Expose
    @SerializedName("tripOption")
    private ArrayList<TripOption> tripOptions;
}
