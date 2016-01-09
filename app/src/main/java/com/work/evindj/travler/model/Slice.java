package com.work.evindj.travler.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by evindj on 1/4/16.
 */
public class Slice implements Serializable{
    @Expose
    private int duration;
    @Expose
    @SerializedName("segment")
    protected ArrayList<Segment> segments;
    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public void setSegments(ArrayList<Segment> segments) {
        this.segments = segments;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


}
