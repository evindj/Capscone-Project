package com.work.evindj.travler.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by evindj on 1/8/16.
 */
public class Trip {
    public TripOptions getTripOptions() {
        return tripOptions;
    }

    public void setTripOptions(TripOptions tripOptions) {
        this.tripOptions = tripOptions;
    }
    @Expose
    @SerializedName("trips")
    private TripOptions tripOptions;
    public  Trip(){

    }
}
