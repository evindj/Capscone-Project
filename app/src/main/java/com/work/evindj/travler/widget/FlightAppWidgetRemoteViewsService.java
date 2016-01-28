package com.work.evindj.travler.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by evindj on 1/28/16.
 */
public class FlightAppWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {

        return  new FlightWidgetDataProvider(getApplicationContext(),intent);
    }
}
