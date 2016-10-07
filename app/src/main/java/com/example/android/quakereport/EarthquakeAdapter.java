package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Casa on 03/10/2016.
 */
public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    private static SimpleDateFormat sfDateFormat = new SimpleDateFormat("MMM dd, yyyy");
    private static SimpleDateFormat sfTimeFormat = new SimpleDateFormat("h:mm a");
    private static DecimalFormat dfMagFormat = new DecimalFormat("0.0");

    public EarthquakeAdapter(Activity context, List<Earthquake> quakes) {
        super(context, 0, quakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Earthquake quake = getItem(position);

        TextView txtMagnitude = (TextView) listItemView.findViewById(R.id.magnitude);
        TextView txtPlace     = (TextView) listItemView.findViewById(R.id.primary_location);
        TextView txtDetPlace  = (TextView) listItemView.findViewById(R.id.location_offset);
        TextView txtDate      = (TextView) listItemView.findViewById(R.id.date);
        TextView txtTime      = (TextView) listItemView.findViewById(R.id.time);
        Date date             = new Date(quake.getTimeInMilliseconds());
        String[] places       = quake.getPlace().split(" of ");
        GradientDrawable mCir = (GradientDrawable) txtMagnitude.getBackground();
        int magnitudeColor    = getMagnitudeColor( quake.getMagnitude() );

        txtMagnitude.setText ( formattedMagnitude( quake.getMagnitude() ) );
        txtDetPlace.setText  ( places.length > 1 ? places[places.length-2] + " of" : "" );
        txtPlace.setText     ( places[places.length-1] );
        txtDate.setText      ( formattedDate( date ) );
        txtTime.setText      ( formattedTime( date ) );
        mCir.setColor        ( magnitudeColor );

        return listItemView;
    }

    private int getMagnitudeColor(double magnitude) {
        int id_color;
        int val_mag = (int) Math.floor( magnitude );

        switch ( val_mag ) {
            case 0:
            case 1:
                id_color = R.color.magnitude1;
                break;
            case 2:
                id_color = R.color.magnitude2;
                break;
            case 3:
                id_color = R.color.magnitude3;
                break;
            case 4:
                id_color = R.color.magnitude4;
                break;
            case 5:
                id_color = R.color.magnitude5;
                break;
            case 6:
                id_color = R.color.magnitude6;
                break;
            case 7:
                id_color = R.color.magnitude7;
                break;
            case 8:
                id_color = R.color.magnitude8;
                break;
            case 9:
                id_color = R.color.magnitude9;
                break;
            default:
                id_color = R.color.magnitude10plus;
                break;
        }

        return ContextCompat.getColor(getContext(), id_color);
    }

    private static String formattedMagnitude(double mag) {
        return dfMagFormat.format( mag );
    }

    public static String formattedDate(Date date) {
        return sfDateFormat.format(date);
    }

    public static String formattedTime(Date date) {
        return sfTimeFormat.format(date);
    }

    public void setEarthquakeList(List<Earthquake> earthquakeList) {
        addAll( earthquakeList );
        notifyDataSetChanged( );
    }
}
