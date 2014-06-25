package io.discount.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.discount.app.helpers.GetLocation;
import io.discount.app.managers.DiscountManager;
import io.discount.app.models.Discount;

public class MainActivity extends ActionBarActivity {
    /* protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        TextView songNameTextView = (TextView) this.findViewById(R.id.songName);
        TextView serverTextView = (TextView) this.findViewById(R.id.server);
        TextView playlistTextView = (TextView) this.findViewById(R.id.playlist);
        Button buttonPlay = (Button) this.findViewById(R.id.play);


        DiscountManager discountManager = new DiscountManager(getApplicationContext());

        List<Discount> discountList = discountManager.getByGPS("asd");

        // build playlist string
        String playlist = "";
        for(Discount songItem : discountList) {
            playlist += "\n" + songItem.getName();
        }

        playlistTextView.setText(playlist);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    } */

    /**
     * A placeholder fragment containing a simple view.
     */
    /*public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }*/
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
            MAP_DEFAULT_CAMERA_ZOOM = 10;

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

               // EditText statusEditText = (EditText) findViewById(R.id.map_status_edit_text);
                //statusEditText.clearFocus();
                SharedPreferences preferences = getApplicationContext().getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

                final MainActivity currContext = this;

               /* statusEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            String status = v.getText().toString();
                            if (status.equals("")) {

                            } else {
                                SharedPreferences preferences = v.getContext().getSharedPreferences(
                                        PREFERENCES_FILE,
                                        Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.commit();
                                v.clearFocus();
                            }

                            InputMethodManager inputManager = (InputMethodManager)
                                    currContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.toggleSoftInput(0, 0);

                            return true;
                        }
                        return false;
                    }
                });*/

                /*ToggleButton toggleButtonDiscounts = (ToggleButton) findViewById(R.id.map_meetup_toggle_events);

                toggleButtonDiscounts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        for (Map.Entry<Marker, Discount> entry : discountNearYouHashMap.entrySet()) {
                            Marker marker = entry.getKey();
                            marker.setVisible(true);
                        }
                    }
                });*/

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            }
        }
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
        setupMap();
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
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                GetLocation gl = new GetLocation(MainActivity.this);
                Location location = gl.getLocation();
                fetchMarkersWithLocation(location);
            }
        });

        zoomToUserLocationAndFetchMarkers();
    }

    private void zoomToUserLocationAndFetchMarkers() {
        GetLocation gl = new GetLocation(this);
        Location location = gl.getLocation();

        if (location != null) {
            // Move the camera instantly to the current location
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), MAP_DEFAULT_CAMERA_ZOOM));
            fetchMarkersWithLocation(location);

        } else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    zoomToUserLocationAndFetchMarkers();
                }
            }, 500);
        }
    }

    private void fetchMarkersWithLocation(Location location) {
        if (location == null) {
            location = map.getMyLocation();
            if (location == null) return;
        }

        DiscountManager discountManager = new DiscountManager(getApplicationContext());

        VisibleRegion visibleRegion = map.getProjection().getVisibleRegion();
        LatLng latLngLeft = visibleRegion.farLeft;
        LatLng latLngRight = visibleRegion.farRight;
        Location farLeft = new Location("farLeft");
        farLeft.setLatitude(latLngLeft.latitude);
        farLeft.setLongitude(latLngLeft.longitude);

        Location farRight = new Location("farRight");
        farRight.setLatitude(latLngRight.latitude);
        farRight.setLongitude(latLngRight.longitude);

        List<Discount> discountList = discountManager.getByGPS("asdasd"); //location.getLongitude() + "," + location.getLatitude());

        for(Discount discountItem : discountList) {
            map.addMarker(new MarkerOptions()
                    .title(discountItem.getName())
                    .snippet(discountItem.getDescription())
                    .position( new LatLng(discountItem.getLatitude(), discountItem.getLongitude())));
        }

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
