
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {
    private static final String REQUEST_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query";
    private EarthquakeAdapter mAdapter;
    private View mEmptyView;
    private View mProgressBar;
    private ListView mEarthquakeListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Find a reference to the {@link ListView} in the layout
        mEarthquakeListView = (ListView) findViewById(R.id.list);
        mEmptyView = findViewById(R.id.listview_empty);
        mProgressBar = findViewById(R.id.progress_bar);

        // Set the adapter on the {@link ListView} so the list can be populated in the user interface
        mEarthquakeListView.setAdapter( mAdapter );

        getLoaderManager().initLoader(0, null, this).forceLoad();

        mEarthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake earthquake = mAdapter.getItem( position );

                if (earthquake.getUrl() != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(earthquake.getUrl()));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        Log.d("*****EarthQuakeActivity", "onCreateLoader");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        String limit = sharedPrefs.getString(
                getString(R.string.settings_max_quantity_key),
                getString(R.string.settings_max_quantity_default)
        );

        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        //eventtype=earthquake
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", limit);
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("eventtype", "earthquake");
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(EarthquakeActivity.this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {
        if (mProgressBar != null) mProgressBar.setVisibility(View.GONE);

        mAdapter.setEarthquakeList( data );

        updateEmptyView( );
    }

    private void updateEmptyView() {
        if ( mAdapter.getCount() == 0 ) {
            TextView tv =  (TextView) mEmptyView;
            if ( null != tv ) {
                // if cursor is empty, why? do we have an invalid location
                int message = R.string.empty_list;

                if (!QueryUtils.isNetworkAvailable(getApplicationContext()) ) {
                    message = R.string.empty_forecast_list_no_network;
                }
                //@SunshineSyncAdapter.LocationStatus int location = Utility.getLocationStatus(getActivity());
                /*switch (location) {
                    case SunshineSyncAdapter.LOCATION_STATUS_SERVER_DOWN:
                        message = R.string.empty_forecast_list_server_down;
                        break;
                    case SunshineSyncAdapter.LOCATION_STATUS_SERVER_INVALID:
                        message = R.string.empty_forecast_list_server_error;
                        break;
                    case SunshineSyncAdapter.LOCATION_STATUS_INVALID:
                        message = R.string.empty_forecast_list_invalid_location;
                        break;
                    default:
                        message = R.string.empty_list;
                        if (!Utility.isNetworkAvailable(getActivity()) ) {
                            message = R.string.empty_forecast_list_no_network;
                        }
                }*/
                tv.setText(message);
                mEarthquakeListView.setEmptyView(mEmptyView);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        mAdapter.setEarthquakeList( new ArrayList<Earthquake>() );
    }
}


