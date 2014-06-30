package io.discount.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.discount.app.helpers.Connection;
import io.discount.app.helpers.GetLocation;
import io.discount.app.managers.DiscountManager;
import io.discount.app.models.Discount;

public class MainActivity extends ActionBarActivity {
    // Global constants
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    /*
     * Define the default zoom of the map, the one used first when the map is displayed
     */
    private final static int
            MAP_DEFAULT_CAMERA_ZOOM = 10,
            MAP_MAX_CAMERA_ZOOM = 7;

    private GoogleMap map;
    private HashMap<Marker, Discount> discountNearYouHashMap = new HashMap<Marker, Discount>();

    private Marker locationMarker;
    private static final String PREFERENCES_FILE = "shared_preferences";

    protected LocationManager locationManager;
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            if (servicesConnected()) {
                SharedPreferences preferences = getApplicationContext().getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
                final MainActivity currContext = this;
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            }
        }

        if(!Connection.isConnected(this)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.no_connection_title);
            alert.setMessage(R.string.no_connection_message);

            alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    dialog.cancel();
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                    finish();
                    dialog.cancel();
                }
            });

            alert.show();
            return;
        }
        setupMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(0, 0);
    }

    private void removeMarkers(ArrayList<Marker> markers) {
        for (Marker marker : markers) {
            marker.remove();
        }
    }

    private void setupMap() {
        if (map == null) {
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        }

        map.clear();
        map.setMyLocationEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.moveCamera(CameraUpdateFactory.zoomTo(MAP_DEFAULT_CAMERA_ZOOM));
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition position) {
                LatLng location = position.target;
            try {
                    fetchMarkersWithLocation(location.latitude, location.longitude);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        /*map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.
            }
        }*/

                zoomToUserLocationAndFetchMarkers();
    }
    private void showLocationPrompt() {
        showLocationPrompt(R.string.location_title, R.string.location_message);
    }
    private void showLocationPrompt(int title, int message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(message);
        final EditText input = new EditText(getApplicationContext());
        alert.setView(input);

        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Location location = null;
                String value = input.getText().toString();
                Geocoder geocoder = new Geocoder(getApplicationContext());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocationName(value, 5);
                    if(addresses.size() > 0) {
                        MainActivity.this.fetchMarkersWithLocation(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                        MainActivity.this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude()), MAP_DEFAULT_CAMERA_ZOOM));
                    } else {
                        MainActivity.this.showLocationPrompt(R.string.location_notfound_title, R.string.location_notfound_message);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                dialog.cancel();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                finish();
                dialog.cancel();
            }
        });

        alert.show();
    }

    private void zoomToUserLocationAndFetchMarkers() {
        GetLocation gl = new GetLocation(this);
        Location location = null;
        try {
            location = gl.getLocation();
            if (location != null) {
                // Move the camera instantly to the current location
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), MAP_DEFAULT_CAMERA_ZOOM));
                fetchMarkersWithLocation(location.getLatitude(), location.getLongitude());

            } else {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        zoomToUserLocationAndFetchMarkers();
                    }
                }, 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showLocationPrompt();
        }

    }

    private class GetMarkers extends AsyncTask<String, Void, List<Discount>> {

        private double latitude;
        private double longitude;
        private double distance;
        GetMarkers(double latitude, double longitude, double distance) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.distance = distance;
        }

        @Override
        protected List<Discount> doInBackground(String... params) {
            DiscountManager discountManager = new DiscountManager(MainActivity.this);

            List<Discount> discountList = discountManager.getByGPS(latitude, longitude, distance);

            return discountList;
        }

        @Override
        protected void onPostExecute(List<Discount> result) {
            for(Discount discountItem : result) {
                map.addMarker(new MarkerOptions()
                        .title(discountItem.getName())
                        .snippet(discountItem.getAddress())
                        .position( new LatLng(discountItem.getLatitude(), discountItem.getLongitude())));
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private void fetchMarkersWithLocation(Double latitude, Double longitude) {
        VisibleRegion visibleRegion = map.getProjection().getVisibleRegion();
        LatLng latLngLeft = visibleRegion.farLeft;
        LatLng latLngRight = visibleRegion.farRight;
        Location farLeft = new Location("farLeft");
        farLeft.setLatitude(latLngLeft.latitude);
        farLeft.setLongitude(latLngLeft.longitude);

        Location farRight = new Location("farRight");
        farRight.setLatitude(latLngRight.latitude);
        farRight.setLongitude(latLngRight.longitude);

        double distanceInMeters = farLeft.distanceTo(farRight);
        if(distanceInMeters < 100) {
            distanceInMeters = 100;
        }

        new GetMarkers(latitude, longitude, distanceInMeters).execute();
        /*DiscountManager discountManager = new DiscountManager(this);

        VisibleRegion visibleRegion = map.getProjection().getVisibleRegion();
        LatLng latLngLeft = visibleRegion.farLeft;
        LatLng latLngRight = visibleRegion.farRight;
        Location farLeft = new Location("farLeft");
        farLeft.setLatitude(latLngLeft.latitude);
        farLeft.setLongitude(latLngLeft.longitude);

        Location farRight = new Location("farRight");
        farRight.setLatitude(latLngRight.latitude);
        farRight.setLongitude(latLngRight.longitude);

        double distanceInMeters = farLeft.distanceTo(farRight);
        if(distanceInMeters < 100) {
            distanceInMeters = 100;
        }

        List<Discount> discountList = discountManager.getByGPS(latitude, longitude, distanceInMeters/1000);

        for(Discount discountItem : discountList) {
            map.addMarker(new MarkerOptions()
                    .title(discountItem.getName())
                    .snippet(discountItem.getDescription())
                    .position( new LatLng(discountItem.getLatitude(), discountItem.getLongitude())));
        }*/

    }

    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                    /*
                     * Try the request again
                     */

                        break;
                }
        }
    }

    private boolean servicesConnected() {

        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        boolean connectionSuccess = (errorCode == ConnectionResult.SUCCESS);
        if (!connectionSuccess) {
            GooglePlayServicesUtil.getErrorDialog(errorCode, this, 0).show();
        }
        return connectionSuccess;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
