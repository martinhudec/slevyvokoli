package io.discount.app.helpers;

/**
 * Created by root on 24.6.14.
 */
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GetLocation implements LocationListener {
    private static LocationManager locationManager;
    private static Context mContext;
    private static String provider = LocationManager.PASSIVE_PROVIDER;
    private double GPSLat;
    private double GPSLon;

    public GetLocation(Context context) {
        mContext = context;
    }

    public void getLatitude(double GPSLat) {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        Location location = locationManager.getLastKnownLocation(provider);
        this.setGPSLat((location.getLatitude()));
        //return (double) (location.getLatitude());
    };

    public void getLongitude(double GPSLon) {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        Location location = locationManager.getLastKnownLocation(provider);
        this.GPSLon = (location.getLongitude());
        //return (double) (location.getLongitude());
    };

    public Location getLocation() throws Exception {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            throw new Exception("Unable to find location");
        }
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        return locationManager.getLastKnownLocation(provider);
    }
    // try this by doing GetLocation.getLocation().getLongitude();

    public double getGPSLat() {
        return GPSLat;
    }

    public void setGPSLat(double gPSLat) {
        GPSLat = gPSLat;
    }

    public double getGPSLon() {
        return GPSLon;
    }

    public void setGPSLon(double gPSLon) {
        GPSLon = gPSLon;
    }


    @Override
    public void onLocationChanged(android.location.Location location) {

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
