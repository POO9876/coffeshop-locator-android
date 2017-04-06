package edu.depaul.csc472.coffeeshoplocator;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;

import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements LocationProvider.LocationCallback,OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiAutoComplete;

    private AutoCompleteTextView mAutocompleteTextView;
    private ImageView currentLocation;
    private Button findCoffeeBtn;
    private PlaceArrayAdapter placeArrayAdapter;
    private LocationProvider locationProvider;

    private static final String LOG_TAG = "MainActivity";

    private static final int GOOGLE_API_CLIENT_ID = 0;

    private double latitude;
    private double longitude;

    private Geocoder geocoder;

    private static final LatLngBounds BOUNDS_CHICAGO = new LatLngBounds(new LatLng(41.751277, -87.605229),
            new LatLng(42.012767, -87.664367));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geocoder = new Geocoder(this, Locale.getDefault());

        buildGoogleApiClient();
        locationProvider = new LocationProvider(this,this);

        final Intent intent = new Intent(MainActivity.this,ListActivity.class);

        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);


        mAutocompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final PlaceArrayAdapter.PlaceAutocomplete item = placeArrayAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                Log.i(LOG_TAG, "Selected: " + item.description);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiAutoComplete, placeId);
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
                Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
            }
        });
        currentLocation = (ImageView)findViewById(R.id.currentLocation);
        currentLocation.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View v){

                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                    String address = addresses.get(0).getAddressLine(0);
                    mAutocompleteTextView.setText(address);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        findCoffeeBtn = (Button) findViewById(R.id.findCoffeeBtn);
        findCoffeeBtn.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View v) {
                intent.putExtra("mainLat",latitude);
                intent.putExtra("mainLong",longitude);
                startActivity(intent);
            }
        });
        placeArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_CHICAGO, null);
        mAutocompleteTextView.setAdapter(placeArrayAdapter);


    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final com.google.android.gms.location.places.Place place = places.get(0);
            latitude = place.getLatLng().latitude;
            longitude = place.getLatLng().longitude;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        locationProvider.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationProvider.disconnect();
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiAutoComplete = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this,GOOGLE_API_CLIENT_ID,this)
                .addConnectionCallbacks(mGoogleApiAutoCompleteCallback)
                .build();
    }

    public void handleNewLocation(Location location){
        Log.d(LOG_TAG,location.toString());
        latitude  = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

    }

    GoogleApiClient.ConnectionCallbacks mGoogleApiAutoCompleteCallback = new GoogleApiClient.ConnectionCallbacks() {

        @Override
        public void onConnected(Bundle bundle) {
            placeArrayAdapter.setGoogleApiClient(mGoogleApiAutoComplete);
        }

        @Override
        public void onConnectionSuspended(int i) {
            placeArrayAdapter.setGoogleApiClient(null);
        }
    };


}
