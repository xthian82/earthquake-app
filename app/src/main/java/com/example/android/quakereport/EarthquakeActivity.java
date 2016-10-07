
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {
    private static final String REQUEST_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    private EarthquakeAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Set the adapter on the {@link ListView} so the list can be populated in the user interface
        earthquakeListView.setAdapter( mAdapter );

        getLoaderManager().initLoader(0, null, this).forceLoad();

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        return new EarthquakeLoader(EarthquakeActivity.this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {
        mAdapter.setEarthquakeList( data );
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        mAdapter.setEarthquakeList( new ArrayList<Earthquake>() );
    }


    /**
     * Handles URL Connection and parsing
     */


    /*private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>> {

        @Override
        protected List<Earthquake> doInBackground(String... params) {
            if (params == null || params.length < 1)
                return null;

            List<Earthquake> dataList = null;
            try {
                dataList = fetchJsonEartquakeData(params[0]);
            } catch (IOException e) {
                Log.e("EarthquakeAsyncTask", "Error fetchJsonEartquakeData on URL ", e);
            }

            return dataList;
        }



        @Override
        protected void onPostExecute(List<Earthquake> data) {
            super.onPostExecute(data);
            updateUI( data );
        }
    }*/
}

