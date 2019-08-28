package com.prescywallet.presdigi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.prescywallet.presdigi.Session.MobileNumberSessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements LocationListener {

    Toolbar toolbar;
    CircleImageView profileImg;
    TextView editPhoto;
    TextInputLayout name, phone_number, email, location_input;
    CardView updateCard;
    String Name, phoneNumber, Email, ProfileImg;
    private LocationManager locationManager;
    private String provider;
    MobileNumberSessionManager mobileNumberSessionManager;
    private boolean firsttime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_bar_main);

        profileImg = (CircleImageView) findViewById(R.id.profile_pic);
        name = (TextInputLayout) findViewById(R.id.name_input);
        phone_number = (TextInputLayout) findViewById(R.id.phone_no_input);
        email = (TextInputLayout) findViewById(R.id.email_input);
        location_input = (TextInputLayout) findViewById(R.id.loc_input);
        updateCard = (CardView) findViewById(R.id.saveProfileUpdate);

        toolbar = (Toolbar) findViewById(R.id.editProfileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(firsttime == false){
            loadName();
            firsttime = true;
        }

        if (getIntent().getStringExtra("MOBILE_NUMBER") != null) {
            phoneNumber = getIntent().getStringExtra("MOBILE_NUMBER");
            ProfileImg = getIntent().getStringExtra("PROFILE_PIC_URL");
        }


        Picasso.get().load(ProfileImg).into(profileImg);
        name.getEditText().setText(Name);
        name.setVisibility(View.VISIBLE);
        phone_number.getEditText().setText(phoneNumber);
        phone_number.getEditText().setEnabled(false);
        phone_number.setVisibility(View.VISIBLE);
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

        updateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateName();

            }
        });


    }

    private void loadName() {
        String url = "http://prescryp.com/prescriptionUpload/getName.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            String getname = jsonObject.getString("name");
                            String getemail = jsonObject.getString("email");
                            if (success.equalsIgnoreCase("1")) {
                                Name = getname;
                                name.getEditText().setText(getname);
                                name.setVisibility(View.VISIBLE);
                                Email = getemail;
                                email.getEditText().setText(getemail);
                                email.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("mobilenumber", phoneNumber);
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

    private void updateName() {
        mobileNumberSessionManager = new MobileNumberSessionManager(getApplicationContext());
        String url =  "http://prescryp.com/prescriptionUpload/updateName.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            if (success.equalsIgnoreCase("1")){
                                mobileNumberSessionManager.createMobileNumberSession(name.getEditText().getText().toString(), phoneNumber);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("mobilenumber", phoneNumber);
                params.put("name", name.getEditText().getText().toString());
                params.put("email", email.getEditText().getText().toString());
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
            if (address.size() > 0){
                String cityName = address.get(0).getLocality();
                location_input.getEditText().setText(cityName);
                location_input.getEditText().setEnabled(false);
                location_input.setVisibility(View.VISIBLE);
            }

             //This will display the final address.

        } catch (IOException e) {
            // Handle IOException
        } catch (NullPointerException e) {
            // Handle NullPointerException
        }

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

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}
