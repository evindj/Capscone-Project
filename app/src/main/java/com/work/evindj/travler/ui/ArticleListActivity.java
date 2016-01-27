package com.work.evindj.travler.ui;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.work.evindj.travler.R;
import com.work.evindj.travler.data.ArticleLoader;
import com.work.evindj.travler.data.ItemsContract;
import com.work.evindj.travler.data.UpdaterService;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.google.android.gms.location.places.Place.TYPE_AIRPORT;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends AppCompatActivity implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private Toolbar mToolbar;
   // private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private SupportPlaceAutocompleteFragment airportOrigin;
    private SupportPlaceAutocompleteFragment airportArrival;
    private Place placeOrigin;
    private Place placeArrival;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    private EditText originAirport;
    private EditText destinationAirport;
    private SimpleDateFormat dateFormatter;
    private Button search ;


    private void setupFragment1(){
       // airportOrigin = (SupportPlaceAutocompleteFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_depart);

        airportOrigin.setFilter(new AutocompleteFilter.Builder().setTypeFilter(TYPE_AIRPORT).build());
        airportOrigin.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("", "Place: " + place.getName());
                placeOrigin = place;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("", "An error occurred: " + status);
            }
        });
    }
    private void setupFragment2(){
       // airportArrival = (SupportPlaceAutocompleteFragment)  getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_arrival);
        airportArrival.setFilter(new AutocompleteFilter.Builder().setTypeFilter(TYPE_AIRPORT).build());
        airportArrival.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("", "Place: " + place.getName());
                placeArrival = place;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("", "An error occurred: " + status);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        final View toolbarContainerView = findViewById(R.id.toolbar_container);


       // mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        getLoaderManager().initLoader(0, null, this);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        findViewsById();

        setDateTimeField();
        /*
        setupFragment1();
        setupFragment2();
        */

        if (savedInstanceState == null) {
            refresh();
        }
    }

    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.etxt_fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();
        originAirport = (EditText) findViewById(R.id.etxt_origin);
        destinationAirport = (EditText) findViewById(R.id.etxt_destination);

        toDateEtxt = (EditText) findViewById(R.id.etxt_todate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);
        search = (Button) findViewById(R.id.button_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ori = originAirport.getText().toString();
                String dest = destinationAirport.getText().toString();
                String depDate = fromDateEtxt.getText().toString();
                String arrDate = toDateEtxt.getText().toString();
                launchService(depDate,arrDate,ori,dest);
            }
        });
    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void refresh() {
        startService(new Intent(this, UpdaterService.class));
    }
    private void launchService(String dateDep, String dateComeback, String airportFrom, String airportTo){
        Intent i = new Intent(this, UpdaterService.class);
        i.putExtra("dateDep", dateDep);
        i.putExtra("dateComeback", dateComeback);
        i.putExtra("airportFrom", airportFrom);
        i.putExtra("airportTo", airportTo);
        startService(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mRefreshingReceiver,
                new IntentFilter(UpdaterService.BROADCAST_ACTION_STATE_CHANGE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mRefreshingReceiver);
    }

    private boolean mIsRefreshing = false;

    private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UpdaterService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                mIsRefreshing = intent.getBooleanExtra(UpdaterService.EXTRA_REFRESHING, false);
                updateRefreshingUI();
            }
        }
    };

    private void updateRefreshingUI() {
        //mSwipeRefreshLayout.setRefreshing(mIsRefreshing);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //return ArticleLoader.newAllArticlesInstance(this);
        return ArticleLoader.AllFlightsInstances(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Adapter adapter = new Adapter(cursor);
        adapter.setHasStableIds(true);
        mRecyclerView.setAdapter(adapter);
        int columnCount = getResources().getInteger(R.integer.list_column_count);
        columnCount =1;
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sglm);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecyclerView.setAdapter(null);
    }

    @Override
    public void onClick(View v) {
        if(v == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if(v == toDateEtxt) {
            toDatePickerDialog.show();
        }
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private Cursor mCursor;

        public Adapter(Cursor cursor) {
            mCursor = cursor;
        }

        @Override
        public long getItemId(int position) {
            mCursor.moveToPosition(position);
            return mCursor.getLong(ArticleLoader.Query._ID);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_item_article, parent, false);
            final ViewHolder vh = new ViewHolder(view);
            view.setContentDescription(getString(R.string.item_content_description));

            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            mCursor.moveToPosition(position);
            holder.priceView.setText(mCursor.getString(ArticleLoader.FlightQuery.PRICE));
            holder.durationView.setText(mCursor.getString(ArticleLoader.FlightQuery.TOTAL_DURATION) + " mins");
            holder.stopsView.setText(mCursor.getString(ArticleLoader.FlightQuery.NUMBER_STOPS)+" stop(s)");
            holder.toView.setText(mCursor.getString(ArticleLoader.FlightQuery.TO));
            holder.fromView.setText(mCursor.getString(ArticleLoader.FlightQuery.FROM));
            /*holder.thumbnailView.setImageUrl(
                    mCursor.getString(ArticleLoader.Query.THUMB_URL),
                    ImageLoaderHelper.getInstance(ArticleListActivity.this).getImageLoader());*/
            //String url = mCursor.getString(ArticleLoader.Query.THUMB_URL);
            //Picasso.with( holder.thumbnailView.getContext()).load(url).into(holder.thumbnailView);
            //holder.thumbnailView.setAspectRatio(mCursor.getFloat(ArticleLoader.Query.ASPECT_RATIO));
        }

        @Override
        public int getItemCount() {
            return mCursor.getCount();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public DynamicHeightNetworkImageView thumbnailView;
        public TextView titleView;
        public TextView subtitleView;
        public TextView priceView;
        public TextView fromView;
        public TextView toView;
        public TextView stopsView;
        public TextView durationView;

        public ViewHolder(View view) {
            super(view);

            priceView = (TextView) view.findViewById(R.id.price_tv);
            fromView = (TextView) view.findViewById(R.id.from_txt);
            toView = (TextView) view.findViewById(R.id.to_txt);
            stopsView = (TextView) view.findViewById(R.id.stops_txt);
            durationView = (TextView) view.findViewById(R.id.stops_duration);
        }
    }
}
