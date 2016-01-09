package com.work.evindj.travler.data;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
            Log.w(TAG, "Not online, not refreshing.");
            return;
        }

        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));

        // Don't even inspect the intent, we only do one thing, and that's fetch content.
        ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();

        Uri dirUri = ItemsContract.Items.buildDirUri();
        SliceRequest sliceData = new SliceRequest("NSI", "ATL", "2016-02-02");
        //SliceRequest sliceDatareturn = new SliceRequest("ATL", "NSI", "2016-03-02");
        ArrayList<SliceRequest> reqData = new ArrayList<SliceRequest>();
        reqData.add(sliceData);
        // reqData.add(sliceDatareturn);
        RequestWrapper requestWrapper = new RequestWrapper(1, reqData);
        requestSearch(requestWrapper);



        // Delete all items
        cpo.add(ContentProviderOperation.newDelete(dirUri).build());

        try {
            JSONArray array = RemoteEndpointUtil.fetchJsonArray();
            if (array == null) {
                throw new JSONException("Invalid parsed item array" );
            }

            for (int i = 0; i < array.length(); i++) {
                ContentValues values = new ContentValues();
                JSONObject object = array.getJSONObject(i);
                values.put(ItemsContract.Items.SERVER_ID, object.getString("id" ));
                values.put(ItemsContract.Items.AUTHOR, object.getString("author" ));
                values.put(ItemsContract.Items.TITLE, object.getString("title" ));
                values.put(ItemsContract.Items.BODY, object.getString("body" ));
                values.put(ItemsContract.Items.THUMB_URL, object.getString("thumb" ));
                values.put(ItemsContract.Items.PHOTO_URL, object.getString("photo" ));
                values.put(ItemsContract.Items.ASPECT_RATIO, object.getString("aspect_ratio" ));
                time.parse3339(object.getString("published_date"));
                values.put(ItemsContract.Items.PUBLISHED_DATE, time.toMillis(false));
                cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(values).build());
            }

            getContentResolver().applyBatch(ItemsContract.CONTENT_AUTHORITY, cpo);

        } catch (JSONException | RemoteException | OperationApplicationException e) {
            Log.e(TAG, "Error updating content.", e);
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
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient  = new OkHttpClient();
        httpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        httpClient.interceptors().add(loggingInterceptor);
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                String url = original.url().toString();
                long body = original.body().contentLength();
                String method = original.method();
                com.squareup.okhttp.Response response = chain.proceed(original);
                final String content = getString(response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                Trip trips;
                try {
                    trips = gson.getAdapter(Trip.class).fromJson(content);
                    System.out.println("je suis la");
                }
                catch (Exception e){
                    Log.w("ErrorInConversion", e.getMessage());
                    e.printStackTrace();
                }

                Log.w("Retrofit@response", response.body().string());
                return response.newBuilder().body(ResponseBody.create(response.body().contentType(),content)).build();
            }
        });
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
                    if(trips!=null){
                        ArrayList<TripOption> options = trips.getTripOptions();
                        for (TripOption option:options) {
                            ContentValues values = new ContentValues();
                            values.put(ItemsContract.Flights.PRICE,option.getSaleTotal());
                            values.put(ItemsContract.Flights.FROM,"");
                            values.put(ItemsContract.Flights.TO, "");
                            values.put(ItemsContract.Flights.TOTAL_DURATION,"2");
                            values.put(ItemsContract.Flights.NUMBER_STOPS,option.getSlices().get(0).getSegments().size()-1
                            );
                            cpo.add(ContentProviderOperation.newInsert(dirFlightUri).withValues(values).build());
                        }
                        try{
                            getContentResolver().applyBatch(ItemsContract.CONTENT_AUTHORITY, cpo);
                        }
                        catch (OperationApplicationException e){
                            e.printStackTrace();;
                        }
                        catch (RemoteException e){
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }






    }
}
