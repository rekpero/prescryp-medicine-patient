package com.prescywallet.presdigi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddNewAddressActivity extends AppCompatActivity implements LocationListener {

    private TextInputLayout complete_address_layout, delivery_instructions, enter_name_for_address;
    private TextView locality_address_text_view, change_locality, city_text_view, change_city;
    private LocationManager locationManager;
    private String provider;
    private RadioGroup name_for_address;
    private CardView continue_mark_address;
    Geocoder geocoder;
    List<Address> addresses;
    private String name_of_address;
    String sender_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
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

        city_text_view = findViewById(R.id.city_text_view);
        change_city = findViewById(R.id.change_city);
        complete_address_layout = findViewById(R.id.complete_address_layout);
        delivery_instructions = findViewById(R.id.delivery_instructions);
        locality_address_text_view = findViewById(R.id.locality_address_text_view);
        change_locality = findViewById(R.id.change_locality);
        enter_name_for_address = findViewById(R.id.enter_name_for_address);
        continue_mark_address = findViewById(R.id.continue_mark_address);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                System.out.println("Provider " + provider + " has been selected.");
                onLocationChanged(location);
            }
        }
        name_of_address = "Home";
        name_for_address = findViewById(R.id.name_for_address);
        name_for_address.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.home_address){
                    enter_name_for_address.setVisibility(View.GONE);
                    name_of_address = "Home";
                }else if (checkedId == R.id.work_address){
                    enter_name_for_address.setVisibility(View.GONE);
                    name_of_address = "Work";
                }else if (checkedId == R.id.other_address){
                    enter_name_for_address.setVisibility(View.VISIBLE);
                    name_of_address = "Other";
                }
            }
        });
        if (getIntent().getStringExtra("SENDER_KEY") != null){
            sender_key = getIntent().getStringExtra("SENDER_KEY");
        }

        continue_mark_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewAddressActivity.this, AddressMapsActivity.class);
                if (sender_key != null){
                    intent.putExtra("SENDER_KEY", sender_key);
                }
                intent.putExtra("City_Name", city_text_view.getText().toString());
                intent.putExtra("Locality", locality_address_text_view.getText().toString());
                intent.putExtra("Complete_Address", complete_address_layout.getEditText().getText().toString());
                intent.putExtra("Delivery_Instructions", delivery_instructions.getEditText().getText().toString());
                if (name_of_address.equalsIgnoreCase("Home") || name_of_address.equalsIgnoreCase("Work")){
                    intent.putExtra("Name_Address", name_of_address);
                }else {
                    intent.putExtra("Name_Address", enter_name_for_address.getEditText().getText().toString());
                }
                startActivity(intent);

            }
        });

        change_locality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewAddressActivity.this, ChangeDeliveryAreaActivity.class);
                intent.putExtra("SENDER_KEY", sender_key);
                startActivity(intent);
            }
        });

        change_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddNewAddressActivity.this, ChangeCityActivity.class);
                intent.putExtra("SENDER_KEY", sender_key);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String[] address_list = addresses.get(0).getAddressLine(0).split(",");
            String address = address_list[address_list.length-4].trim();
            String city = address_list[address_list.length-3].trim();
            locality_address_text_view.setText(address);
            city_text_view.setText(city);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (getIntent() != null && getIntent().getStringExtra("LOCATION_RESPONSE") != null) {
            if (getIntent().getStringExtra("LOCATION_RESPONSE").equalsIgnoreCase("current_location")) {
                geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String[] address_list = addresses.get(0).getAddressLine(0).split(",");
                    String address = address_list[address_list.length-4].trim();
                    String city = address_list[address_list.length-3].trim();
                    locality_address_text_view.setText(address);
                    city_text_view.setText(city);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (getIntent().getStringExtra("LOCATION_RESPONSE").equalsIgnoreCase("search_address")){
                String location_locality = getIntent().getStringExtra("location_locality");
                String location_latitude = getIntent().getStringExtra("location_latitude");
                String location_longitude = getIntent().getStringExtra("location_longitude");
                geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(Double.valueOf(location_latitude), Double.valueOf(location_longitude), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String[] address_list = addresses.get(0).getAddressLine(0).split(",");
                    String city = address_list[address_list.length-3].trim();
                    locality_address_text_view.setText(location_locality);
                    city_text_view.setText(city);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(provider, 400, 1, this);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }

    }

}
