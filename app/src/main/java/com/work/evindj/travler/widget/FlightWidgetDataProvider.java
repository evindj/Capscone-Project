package com.work.evindj.travler.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.work.evindj.travler.R;
import com.work.evindj.travler.data.ItemsContract;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by evindj on 1/28/16.
 */
public class FlightWidgetDataProvider  implements RemoteViewsService.RemoteViewsFactory  {

    public static final int _ID = 0;
    public static final int TOTAL_DURATION = 1;
    public static final int PRICE = 2;
    public static final int NUMBER_STOPS = 3;
    public static final int FROM = 4;
    public static final int TO = 5;
    public double detail_match_id = 0;
    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";
    private Cursor data;
    Context mContext;
    public FlightWidgetDataProvider(Context context, Intent intent){
        mContext = context;
    }
    @Override
    public void onCreate() {
        String arg[] = new String[1];
        Uri uri = ItemsContract.Flights.buildDirUri();
        //incomplete logic missing.
        Date date = new Date(System.currentTimeMillis()+(0*86400000));
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        arg[0] = mformat.format(date);
        data = mContext.getContentResolver().query(uri, null, null, arg, null);
        int i = data.getCount();
    }

    @Override
    public void onDataSetChanged() {
        if(data != null){
            data.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        String arg[] = new String[1];
        Uri uri = ItemsContract.Flights.buildDirUri();
        Date date = new Date(System.currentTimeMillis()+(0*86400000));
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        arg[0] = mformat.format(date);
        data = mContext.getContentResolver().query(uri, null, null, arg, null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if(data!=null){
            data.close();
            data= null;
        }
    }

    @Override
    public int getCount() {
        int i = data == null ? 0 : data.getCount();
        return  i;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if(position == AdapterView.INVALID_POSITION || data==null || !data.moveToPosition(position)){
            return null;
        }
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.list_item_article);

        views.setTextViewText(R.id.price_tv, data.getString(PRICE));
        views.setTextViewText(R.id.from_txt,data.getString(FROM));
        views.setTextViewText(R.id.to_txt, data.getString(TO));
        views.setTextViewText(R.id.stops_txt, data.getString(NUMBER_STOPS));
        views.setTextViewText(R.id.stops_duration, data.getString(TOTAL_DURATION));
        final Intent fillInIntent = new Intent();
        // setup onclick here but test first

        // Button share_button = (Button) v.findViewById(R.id.share_button);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {

        return new RemoteViews(mContext.getPackageName(), R.layout.flight_widget);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
