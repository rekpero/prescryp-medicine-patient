package com.prescywallet.presdigi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.prescywallet.presdigi.Model.DeliveryAddressItem;
import com.prescywallet.presdigi.database.PatientAddressDBHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AddressMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "STORE ADDRESS";
    private GoogleMap mMap;
    private String city_name, locality, complete_address, delivery_instruction, name_address;
    private static final float DEFAULT_ZOOM = 16f;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    private TextView store_address_text_view, nickName;
    private LatLng final_location_latlng;
    private CardView saveAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_map_bar_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        getLocationPermission();


        store_address_text_view = findViewById(R.id.store_address_text_view);
        nickName = findViewById(R.id.nickName);
        if (getIntent() != null) {
            city_name = getIntent().getStringExtra("City_Name");
            locality = getIntent().getStringExtra("Locality");
            complete_address = getIntent().getStringExtra("Complete_Address");
            delivery_instruction = getIntent().getStringExtra("Delivery_Instructions");
            name_address = getIntent().getStringExtra("Name_Address");

            nickName.setText(name_address);
            store_address_text_view.setText(complete_address);
        }
        saveAddress = findViewById(R.id.save_address_point);
        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // do your actual work here
                        AddressMapsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (getIntent().getStringExtra("SENDER_KEY") != null){
                                    new PatientAddressDBHelper(getApplicationContext()).addDeliveryAddress(new DeliveryAddressItem(locality, complete_address,
                                            delivery_instruction, name_address, String.valueOf(final_location_latlng.latitude),
                                            String.valueOf(final_location_latlng.longitude)));
                                    if (getIntent().getStringExtra("SENDER_KEY").equalsIgnoreCase("for_medicine")){
                                        Intent intent = new Intent(AddressMapsActivity.this, SelectAddressActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }else if (getIntent().getStringExtra("SENDER_KEY").equalsIgnoreCase("for_checkout")){
                                        Intent intent = new Intent(AddressMapsActivity.this, SelectAddressForCheckoutActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }

                            }
                        });

                    }
                }, 600);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }


        // Add a marker in Sydney and move the camera
        Geocoder geocoder = new Geocoder(AddressMapsActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            if (!complete_address.contains(locality)){
                complete_address = complete_address + " " + locality;
            }if (!complete_address.contains(city_name)){
                complete_address = complete_address + " " + city_name;
            }
            list = geocoder.getFromLocationName(complete_address, 1);
        }catch (IOException e){
            Log.e(TAG, "geolocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0){
            Address address = list.get(0);
            LatLng storeAddressLocation = new LatLng(address.getLatitude(), address.getLongitude());
            final_location_latlng = storeAddressLocation;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeAddressLocation, DEFAULT_ZOOM));
        }

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                final_location_latlng = mMap.getCameraPosition().target;
                Log.e(TAG, "Final location: lat " + final_location_latlng.latitude + " lon " + final_location_latlng.longitude);

            }
        });

    }


    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }
}
