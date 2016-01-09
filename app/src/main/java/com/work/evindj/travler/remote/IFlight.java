package com.work.evindj.travler.remote;
import com.work.evindj.travler.model.Trip;
import com.work.evindj.travler.model.TripOption;
import com.work.evindj.travler.model.TripOptions;

import retrofit.Call;
import  retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import  retrofit.http.POST;
import  retrofit.http.Path;

/**
 * Created by evindj on 1/4/16.
 */
public interface IFlight {
    String TAG = IFlight.class.getName();
    public static final String ROOT_URL = "https://www.googleapis.com/qpxExpress/v1/trips/";
    @Headers({"Content-Type: application/json","Accept:application/json"})
    @POST("search?key=AIzaSyAaGlDmu9q1AuTSKoaLCtRC-nF3qjxU6ac&fields=trips/tripOption(saleTotal%2C%20slice%2Fduration%2Cslice%2Fsegment)")
    public Call<Trip> getOptions( @Body RequestWrapper wrapper );
}
