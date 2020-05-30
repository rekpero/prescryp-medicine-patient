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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.prescywallet.presdigi.Abstract.Converter;
import com.prescywallet.presdigi.Model.CartItem;
import com.prescywallet.presdigi.Model.ChemistLatLngItem;
import com.prescywallet.presdigi.Model.DeliveryAddressItem;
import com.prescywallet.presdigi.Session.LocationSessionManager;
import com.prescywallet.presdigi.Session.MobileNumberSessionManager;
import com.prescywallet.presdigi.database.CartDbHelper;
import com.prescywallet.presdigi.database.NearbyChemistDBHelper;
import com.prescywallet.presdigi.database.PatientAddressDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class MedicineActivity extends AppCompatActivity implements LocationListener {
    private List<CartItem> cartItems;
    private static int cart_count = 0;
    private LinearLayout search_medicines;
    private TextView search_med_text_view, delivering_address_text;
    private LocationManager locationManager;
    private String provider;
    private LatLng patient_latlng;
    Geocoder geocoder;
    List<Address> addresses;
    private ConstraintLayout change_delivering_address;
    private List<ChemistLatLngItem> chemistLatLngItems;
    LocationSessionManager sessionManager;
    private CountDownLatch latch1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
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
        sessionManager = new LocationSessionManager(getApplicationContext());

        delivering_address_text = findViewById(R.id.delivering_address_text);
        latch1 = new CountDownLatch(1);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        List<DeliveryAddressItem> addressItems = new PatientAddressDBHelper(MedicineActivity.this).getAllDeliveryAddress();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                System.out.println("Provider " + provider + " has been selected.");
                onLocationChanged(location);
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch1.await();
                    MedicineActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (getIntent() != null && getIntent().getStringExtra("LOCATION_RESPONSE") != null){
                                if (getIntent().getStringExtra("LOCATION_RESPONSE").equalsIgnoreCase("current_location")){
                                    patient_latlng = new LatLng(patient_latlng.latitude, patient_latlng.longitude);
                                    geocoder = new Geocoder(MedicineActivity.this, Locale.getDefault());
                                    try {
                                        addresses = geocoder.getFromLocation(patient_latlng.latitude, patient_latlng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                        String[] address_list = addresses.get(0).getAddressLine(0).split(",");
                                        String address = address_list[address_list.length-4].trim() + " , " + address_list[address_list.length-3].trim();
                                        delivering_address_text.setText(address);
                                        sessionManager.createLocationSession(address, String.valueOf(patient_latlng.latitude), String.valueOf(patient_latlng.longitude));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }else {
                                if (sessionManager.isLocationLoggedIn()){
                                    HashMap<String, String> user = sessionManager.getLocationDetails();
                                    String location_name = user.get(LocationSessionManager.KEY_LOCATION_NAME);
                                    String location_lat = user.get(LocationSessionManager.KEY_LOCATION_LATITUDE);
                                    String location_lng = user.get(LocationSessionManager.KEY_LOCATION_LONGITUDE);
                                    delivering_address_text.setText(location_name);
                                    patient_latlng = new LatLng(Double.valueOf(location_lat), Double.valueOf(location_lng));
                                }
                            }

                            new NearbyChemistDBHelper(MedicineActivity.this).deleteAllNearbyChemist();
                            chemistLatLngItems = new ArrayList<>();


                            getChemistLatLng();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();



        cartItems = new CartDbHelper(this).getCart();
        cart_count = cartItems.size();
        search_med_text_view = findViewById(R.id.search_med_text_view);
        search_med_text_view.setText("Search Medicines");
        search_medicines = findViewById(R.id.search_medicines);
        search_medicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MedicineActivity.this, SearchMedicineActivity.class));
            }
        });
        change_delivering_address = findViewById(R.id.change_delivering_address);
        change_delivering_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MedicineActivity.this, SelectAddressActivity.class));
            }
        });





    }



    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.medicine_activity_menu, menu);


        MenuItem cart = menu.findItem(R.id.action_cart);
        cart.setIcon(Converter.convertLayoutToImage(MedicineActivity.this, cart_count, R.mipmap.ic_cart_white_icon));
        cart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(MedicineActivity.this, CartActivity.class));
                return true;
            }
        });

        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(provider, 400, 1, this);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cartItems = new CartDbHelper(this).getCart();
        cart_count = cartItems.size();
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        patient_latlng = new LatLng(lat, lng);
        latch1.countDown();
    }

    private void getChemistLatLng() {
        MobileNumberSessionManager mobileNumberSessionManager = new MobileNumberSessionManager(getApplicationContext());
        HashMap<String, String> user = mobileNumberSessionManager.geMobileDetails();
        final String session_mob = user.get(MobileNumberSessionManager.KEY_MOB);
        String url = "http://prescryp.com/prescriptionUpload/getChemistLatLong.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("chemist");
                            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            if (success.equalsIgnoreCase("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    //getting product object from json array
                                    JSONObject product = jsonArray.getJSONObject(i);

                                    chemistLatLngItems.add(new ChemistLatLngItem(product.getString("chemist_mobile_number"),
                                            product.getString("chemist_store_name"),
                                            new LatLng(Double.valueOf(product.getString("latitude")),
                                                    Double.valueOf(product.getString("longitude")))));

                                }

                                getNearbyChemist();
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
                params.put("mobilenumber", session_mob);
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }


    private void getNearbyChemist() {
        for (ChemistLatLngItem chemistLatLngItem : chemistLatLngItems) {

            Location locationA = new Location("point A");
            locationA.setLatitude(patient_latlng.latitude);
            locationA.setLongitude(patient_latlng.longitude);

            Location locationB = new Location("point B");
            locationB.setLatitude(chemistLatLngItem.getLatLng().latitude);
            locationB.setLongitude(chemistLatLngItem.getLatLng().longitude);


            double distance = locationA.distanceTo(locationB);

            if (distance < 5000) {
                new NearbyChemistDBHelper(MedicineActivity.this).addNearbyChemist(chemistLatLngItem);
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

    public class MedicineDisplayItem{
        private String medicineName;
        private String companyName;

        public MedicineDisplayItem(String medicineName, String companyName) {
            this.medicineName = medicineName;
            this.companyName = companyName;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MedicineActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
