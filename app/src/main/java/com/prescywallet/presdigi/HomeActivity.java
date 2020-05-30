package com.prescywallet.presdigi;

import android.Manifest;
import android.app.Activity;
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
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.multidex.MultiDex;

import com.google.android.gms.maps.model.LatLng;
import com.prescywallet.presdigi.Misc.RunTimePermission;
import com.prescywallet.presdigi.Model.ChemistLatLngItem;
import com.prescywallet.presdigi.Model.DeliveryAddressItem;
import com.prescywallet.presdigi.Session.LocationSessionManager;
import com.prescywallet.presdigi.Session.MobileNumberSessionManager;
import com.prescywallet.presdigi.database.NearbyChemistDBHelper;
import com.prescywallet.presdigi.database.PatientAddressDBHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class HomeActivity extends Activity implements LocationListener {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    private LocationManager locationManager;
    private String provider;
    private LatLng patient_latlng;
    private List<ChemistLatLngItem> chemistLatLngItems;
    private List<DeliveryAddressItem> addressItems;
    private CountDownLatch latch;

    RunTimePermission photoRunTimePermission;
    Geocoder geocoder;
    List<Address> addresses;
    LocationSessionManager sessionManager;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (photoRunTimePermission != null) {
            photoRunTimePermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sessionManager = new LocationSessionManager(HomeActivity.this);

        new NearbyChemistDBHelper(HomeActivity.this).deleteAllNearbyChemist();
        chemistLatLngItems = new ArrayList<>();
        latch = new CountDownLatch(2);

        photoRunTimePermission = new RunTimePermission(this);
        photoRunTimePermission.requestPermission(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CALL_PHONE
        }, new RunTimePermission.RunTimePermissionListener() {

            @Override
            public void permissionGranted() {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                provider = locationManager.getBestProvider(criteria, false);
                addressItems = new PatientAddressDBHelper(HomeActivity.this).getAllDeliveryAddress();
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Location location = locationManager.getLastKnownLocation(provider);
                    if (location != null) {
                        System.out.println("Provider " + provider + " has been selected.");
                        onLocationChanged(location);
                    }
                }
            }

            @Override
            public void permissionDenied() {
            }
        });

        getChemistLatLng();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                    HomeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getNearbyChemist();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();





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
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        if (addressItems.size() == 0){
            patient_latlng = new LatLng(lat, lng);
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(patient_latlng.latitude, patient_latlng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String[] address_list = addresses.get(0).getAddressLine(0).split(",");
                String address = address_list[address_list.length-4].trim() + " , " + address_list[address_list.length-3].trim();
                sessionManager.createLocationSession(address, String.valueOf(lat), String.valueOf(lng));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            LatLng patient_current_latlng = new LatLng(lat, lng);
            double min_distance = 5000;
            for (DeliveryAddressItem addressItem : addressItems){
                Location locationA = new Location("point A");
                locationA.setLatitude(patient_current_latlng.latitude);
                locationA.setLongitude(patient_current_latlng.longitude);

                Location locationB = new Location("point B");
                locationB.setLatitude(Double.valueOf(addressItem.getDeliveryLatitude()));
                locationB.setLongitude(Double.valueOf(addressItem.getDeliveryLongitude()));


                double distance = locationA.distanceTo(locationB);

                if (distance < 500) {
                    if (min_distance > distance){
                        patient_latlng = new LatLng(Double.valueOf(addressItem.getDeliveryLatitude()), Double.valueOf(addressItem.getDeliveryLongitude()));
                        String deliver_to = addressItem.getDeliveryNickname() + " (" + addressItem.getLocality() + ")";
                        sessionManager.createLocationSession(deliver_to, String.valueOf(lat), String.valueOf(lng));
                        min_distance = distance;
                    }
                }else{
                    patient_latlng = new LatLng(lat, lng);
                    geocoder = new Geocoder(this, Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(patient_latlng.latitude, patient_latlng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String[] address_list = addresses.get(0).getAddressLine(0).split(",");
                        String address = address_list[address_list.length-4].trim() + " , " + address_list[address_list.length-3].trim();
                        sessionManager.createLocationSession(address, String.valueOf(lat), String.valueOf(lng));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        latch.countDown();



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

    private void getChemistLatLng() {
        final String session_mob;
        MobileNumberSessionManager mobileNumberSessionManager = new MobileNumberSessionManager(getApplicationContext());
        if (mobileNumberSessionManager.isMobLoggedIn()) {
            HashMap<String, String> user = mobileNumberSessionManager.geMobileDetails();
            session_mob = user.get(MobileNumberSessionManager.KEY_MOB);
        } else {
            session_mob = "9474859632";
        }
        latch.countDown();

//        String url = "http://prescryp.com/prescriptionUpload/getChemistLatLong.php";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String success = jsonObject.getString("success");
//                            String message = jsonObject.getString("message");
//                            JSONArray jsonArray = jsonObject.getJSONArray("chemist");
//                            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                            if (success.equalsIgnoreCase("1")) {
//                                for (int i = 0; i < jsonArray.length(); i++) {
//
//                                    //getting product object from json array
//                                    JSONObject product = jsonArray.getJSONObject(i);
//
//                                    chemistLatLngItems.add(new ChemistLatLngItem(product.getString("chemist_mobile_number"),
//                                            product.getString("chemist_store_name"),
//                                            new LatLng(Double.valueOf(product.getString("latitude")),
//                                                    Double.valueOf(product.getString("longitude")))));
//
//                                }
//
//                                latch.countDown();
//
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<>();
//                params.put("mobilenumber", session_mob);
//                return params;
//            }
//        };
//
//        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
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
                new NearbyChemistDBHelper(HomeActivity.this).addNearbyChemist(chemistLatLngItem);
            }
        }
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(HomeActivity.this, SignInAcyivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }


}
