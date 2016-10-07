package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by Casa on 07/10/2016.
 */
public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private String mUrl;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    public List<Earthquake> loadInBackground() {
        List<Earthquake> dataList = null;

        try {
            // Create URL object
            URL url = QueryUtils.createUrl(mUrl);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = QueryUtils.fetchJsonFromUrl(url);

            dataList = QueryUtils.parseEarthquakeListFromString(jsonResponse);
        } catch (IOException e) {
            Log.e("EarthquakeLoader", "Error loadInBackground ", e);
        }

        return dataList;
    }
}
