package com.work.evindj.travler.data;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;

/**
 * Helper for loading a list of articles or a single article.
 */
public class ArticleLoader extends CursorLoader {
    public static ArticleLoader newAllArticlesInstance(Context context) {
        return new ArticleLoader(context, ItemsContract.Items.buildDirUri());
    }

    public static ArticleLoader AllFlightsInstances(Context context){
        return  new ArticleLoader(context, ItemsContract.Flights.buildDirUri(),FlightQuery.PROJECTION);
    }

    public static ArticleLoader newInstanceForItemId(Context context, long itemId) {
        return new ArticleLoader(context, ItemsContract.Items.buildItemUri(itemId));
    }
    private  ArticleLoader(Context context, Uri uri, String[] projections){
        super(context, uri, projections, null, null, ItemsContract.Flights.DEFAULT_SORT);
    }
    private ArticleLoader(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null, ItemsContract.Items.DEFAULT_SORT);
    }
    public interface FlightQuery {
        String[] PROJECTION = {
                ItemsContract.Flights._ID,
                ItemsContract.Flights.FROM,
                ItemsContract.Flights.TO,
                ItemsContract.Flights.PRICE,
                ItemsContract.Flights.TOTAL_DURATION,
                ItemsContract.Flights.NUMBER_STOPS
        };

        int _ID = 0;
        int FROM = 1;
        int TO = 2;
        int PRICE = 3;
        int TOTAL_DURATION = 4;
        int NUMBER_STOPS = 5;
    }

    public interface Query {
        String[] PROJECTION = {
                ItemsContract.Items._ID,
                ItemsContract.Items.TITLE,
                ItemsContract.Items.PUBLISHED_DATE,
                ItemsContract.Items.AUTHOR,
                ItemsContract.Items.THUMB_URL,
                ItemsContract.Items.PHOTO_URL,
                ItemsContract.Items.ASPECT_RATIO,
                ItemsContract.Items.BODY,
        };

        int _ID = 0;
        int TITLE = 1;
        int PUBLISHED_DATE = 2;
        int AUTHOR = 3;
        int THUMB_URL = 4;
        int PHOTO_URL = 5;
        int ASPECT_RATIO = 6;
        int BODY = 7;
    }
}
