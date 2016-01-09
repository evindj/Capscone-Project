package com.work.evindj.travler.remote;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by evindj on 1/4/16.
 */
public class RequestWrapper {
    public PostRequest getRequest() {
        return request;
    }

    public void setRequest(PostRequest request) {
        this.request = request;
    }

    @Expose
    private PostRequest request;
    public  RequestWrapper(int numAdult, ArrayList<SliceRequest> slices){
      request = new  PostRequest(numAdult,slices);

    }
    class PostRequest{
        @Expose
        private Passenger passengers;

        public ArrayList<SliceRequest> getSlice() {
            return slice;
        }

        public void setSlice(ArrayList<SliceRequest> slice) {
            this.slice = slice;
        }

        public Passenger getPassengers() {
            return passengers;
        }

        public void setPassengers(Passenger passengers) {
            this.passengers = passengers;
        }

        @Expose
        private ArrayList<SliceRequest> slice;
        public PostRequest(){

        }
        public PostRequest(int numAdult, ArrayList<SliceRequest> slices){
            passengers = new Passenger(numAdult);
            slice = slices;
        }
    }
    class Passenger{
        public int getAdultCount() {
            return adultCount;
        }

        public void setAdultCount(int adultCount) {
            this.adultCount = adultCount;
        }

        @Expose
        int adultCount;
        public Passenger(){

        }
        public Passenger(int a){
            adultCount =a;
        }
    }


}
