package com.work.evindj.travler.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class ItemsContract {
	public static final String CONTENT_AUTHORITY = "com.work.evindj.travler";
	public static final Uri BASE_URI = Uri.parse("content://com.work.evindj.travler");

	interface ItemsColumns {
		/** Type: INTEGER PRIMARY KEY AUTOINCREMENT */
		String _ID = "_id";
		/** Type: TEXT */
		String SERVER_ID = "server_id";
		/** Type: TEXT NOT NULL */
		String TITLE = "title";
		/** Type: TEXT NOT NULL */
		String AUTHOR = "author";
		/** Type: TEXT NOT NULL */
		String BODY = "body";
        /** Type: TEXT NOT NULL */
        String THUMB_URL = "thumb_url";
		/** Type: TEXT NOT NULL */
		String PHOTO_URL = "photo_url";
		/** Type: REAL NOT NULL DEFAULT 1.5 */
		String ASPECT_RATIO = "aspect_ratio";
		/** Type: INTEGER NOT NULL DEFAULT 0 */
		String PUBLISHED_DATE = "published_date";

	}

	public static class Items implements ItemsColumns {
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.work.evindj.travler.items";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.work.evindj.travler.items";


        public static final String DEFAULT_SORT = PUBLISHED_DATE + " DESC";

		/** Matches: /items/ */
		public static Uri buildDirUri() {
			return BASE_URI.buildUpon().appendPath("items").build();
		}

		/** Matches: /items/[_id]/ */
		public static Uri buildItemUri(long _id) {
			return BASE_URI.buildUpon().appendPath("items").appendPath(Long.toString(_id)).build();
		}

        /** Read item ID item detail URI. */
        public static long getItemId(Uri itemUri) {
            return Long.parseLong(itemUri.getPathSegments().get(1));
        }
	}

	public static class Flights implements BaseColumns{
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.work.evindj.travler.flights";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.work.evindj.travler.flights";

		public static final String TABLE_NAME = "flights";
		public static final String _ID = "flight_id";
		public static final String TOTAL_DURATION = "duration";
		public static final String PRICE = "price";
		public static final String NUMBER_STOPS = "stops";
		public static final String FROM = "origin";
		public static final String TO = "destination";
		public static final String getCreateTableQuery(){
			return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( "+
					_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
					TOTAL_DURATION + " TEXT, " +
					PRICE + " TEXT, "+
					NUMBER_STOPS + " TEXT, "+
					FROM + " TEXT, "+
					TO + " TEXT )";
		}
		public static final String getDropTableQuery(){
			return "DROP TABLE IF EXISTS "+ TABLE_NAME;
		}
		/** Matches: /items/ */
		public static Uri buildDirUri() {
			return BASE_URI.buildUpon().appendPath("flights").build();
		}

		/** Matches: /items/[_id]/ */
		public static Uri buildItemUri(long _id) {
			return BASE_URI.buildUpon().appendPath("flights").appendPath(Long.toString(_id)).build();
		}

		/** Read item ID item detail URI. */
		public static long getFlightId(Uri flightUri) {
			return Long.parseLong(flightUri.getPathSegments().get(1));
		}
	}

	public static class StopsEntry implements BaseColumns{
		//not yet completed.
		public static final String TABLE_NAME = "stop";
		public static final String _ID = "flight_id";
		public static final String COLUMN_STOP_KEY = "stop_id";
		public static final String TOTAL_DURATION = "duration";
		public static final String PRICE = "price";
		public static final String NUMBER_STOPS = "stops";
		public static final String FROM = "origin";
		public static final String TO = "destination";
		public static final String getCreateTableQuery() {
			return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
					_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, " +
					TOTAL_DURATION + " TEXT, " +
					PRICE + " TEXT, " +
					NUMBER_STOPS + " TEXT, " +
					FROM + " TEXT, " +
					TO + " TEXT )";
		}
		public static final String getDropTableQuery(){
			return "DROP TABLE IF EXISTS "+ TABLE_NAME;
		}
	}




	private ItemsContract() {
	}
}
