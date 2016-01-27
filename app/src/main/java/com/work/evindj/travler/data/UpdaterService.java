package com.work.evindj.travler.data;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Time;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.work.evindj.travler.model.Trip;
import com.work.evindj.travler.model.TripOption;
import com.work.evindj.travler.model.TripOptions;
import com.work.evindj.travler.remote.IFlight;
import com.work.evindj.travler.remote.RemoteEndpointUtil;
import com.work.evindj.travler.remote.RequestWrapper;
import com.work.evindj.travler.remote.SliceRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.*;


public class UpdaterService extends IntentService {
    private static final String TAG = "UpdaterService";

    public static final String BROADCAST_ACTION_STATE_CHANGE
            = "com.work.evindj.travler.intent.action.STATE_CHANGE";
    public static final String EXTRA_REFRESHING
            = "com.work.evindj.travler.intent.extra.REFRESHING";

    public UpdaterService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Time time = new Time();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {

            return;
        }

        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));

        // Don't even inspect the intent, we only do one thing, and that's fetch content.
        ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();
        Uri dirUri = ItemsContract.Items.buildDirUri();
        Bundle extras = intent.getExtras();
        if(extras!= null){
            String to = extras.getString("airportTo");
            String from = extras.getString("airportFrom");
            String date = extras.getString("dateDep");
            SliceRequest sliceData = new SliceRequest(to, from, date);
            // new SliceRequest("NSI", "ATL", "2016-02-02");
            //SliceRequest sliceDatareturn = new SliceRequest("ATL", "NSI", "2016-03-02");
            ArrayList<SliceRequest> reqData = new ArrayList<SliceRequest>();
            reqData.add(sliceData);
            // reqData.add(sliceDatareturn);
            RequestWrapper requestWrapper = new RequestWrapper(1, reqData);
            requestSearch(requestWrapper);
        }


        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
    }
    private  String getString(com.squareup.okhttp.Response response ){
        try{
            return new String(response.body().bytes(),"UTF-8");
        }catch (Exception e){
            return  ";";
        }
    }
    public  void requestSearch(RequestWrapper request){
        //HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        //loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient  = new OkHttpClient();
        httpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        //httpClient.interceptors().add(loggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IFlight.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        IFlight flightService = retrofit.create(IFlight.class);
        try{
            Call<Trip> call = flightService.getOptions(request );
            call.enqueue(new Callback<Trip>() {
                @Override
                public void onResponse(Response<Trip> response, Retrofit retrofit) {
                    Uri dirFlightUri = ItemsContract.Flights.buildDirUri();

                    TripOptions trips = response.body().getTripOptions();
                    ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();
                    // Delete all items
                    cpo.add(ContentProviderOperation.newDelete(dirFlightUri).build());
                    if(trips!=null){
                        List<TripOption> options = trips.getTripOptions(20);
                        for (TripOption option:options) {
                            ContentValues values = new ContentValues();
                            values.put(ItemsContract.Flights.PRICE,option.getSaleTotal());
                            values.put(ItemsContract.Flights.FROM,"Destination");
                            values.put(ItemsContract.Flights.TO, "Arrive");
                            values.put(ItemsContract.Flights.TOTAL_DURATION,option.getDuration());
                            values.put(ItemsContract.Flights.NUMBER_STOPS,option.getSlices().get(0).getSegments().size()-1
                            );
                            cpo.add(ContentProviderOperation.newInsert(dirFlightUri).withValues(values).build());
                        }
                        try{
                            getContentResolver().applyBatch(ItemsContract.CONTENT_AUTHORITY, cpo);
                        }
                        catch (OperationApplicationException e){

                        }
                        catch (RemoteException e){

                        }

                    }
                }

                @Override
                public void onFailure(Throwable t) {


                }
            });
        }
        catch (Exception e){

        }






    }
}
