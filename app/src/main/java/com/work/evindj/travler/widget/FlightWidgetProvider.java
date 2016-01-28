package com.work.evindj.travler.widget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.work.evindj.travler.R;

/**
 * Created by evindj on 1/28/16.
 */
public class FlightWidgetProvider extends AppWidgetProvider {
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[]  appWidgetIds){
        for(int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.flight_widget);

            Intent intent = new Intent(context,FlightAppWidgetRemoteViewsService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(context,views,intent);
            }
            else {
                setRemoteAdapterV11(context, views,intent);
            }
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }
    @Override
    public  void onReceive(@NonNull Context context, @NonNull Intent intent){
        super.onReceive(context, intent);
       /* AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.scores_list_widget);*/
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void setRemoteAdapter(Context context, @NonNull final RemoteViews views, Intent intent){
        views.setRemoteAdapter(R.id.flights_list_widget, intent);
    }
    @SuppressWarnings("deprecation")
    public void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views, Intent
            intent){
        views.setRemoteAdapter(0,R.id.flights_list_widget, intent);
    }
}
