package com.prescywallet.presdigi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.prescywallet.presdigi.Abstract.Converter;
import com.prescywallet.presdigi.Adapters.ShowMedicineShopAdapter;
import com.prescywallet.presdigi.Interface.OnDataChangeListener;
import com.prescywallet.presdigi.Model.CartItem;
import com.prescywallet.presdigi.Model.ChemistLatLngItem;
import com.prescywallet.presdigi.Model.MedicineAvailableItem;
import com.prescywallet.presdigi.Model.MedicinePackagePriorityItem;
import com.prescywallet.presdigi.Model.SelectedChemist;
import com.prescywallet.presdigi.Model.StorePriorityItem;
import com.prescywallet.presdigi.database.CartDbHelper;
import com.prescywallet.presdigi.database.NearbyChemistDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class ShowMedicineDetailActivity extends AppCompatActivity implements LocationListener {

    private TextView medicineName, companyName, medComposition, primaryUse, medForm, requirePrescription;
    private LinearLayout requirePresIcon;
    private RecyclerView packagingDetails;
    private String medicineNameForSearch;
    private List<ChemistLatLngItem> chemistLatLngItems;
    private List<MedicineAvailableItem> medicineAvailableItems;
    private List<SelectedChemist> selectedChemistMobItem;
    private LocationManager locationManager;
    private String provider;
    private LatLng patient_latlng;
    private ScrollView medDetailLayout;
    private ProgressBar loadingMedicines;
    private String require_pres = "No";
    private List<CartItem> cartItems;
    private static int cart_count = 0;
    private String[] med_packaging_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_medicine_detail_bar_main);
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

        medicineName = findViewById(R.id.medicine_name);
        companyName = findViewById(R.id.companyName);
        medComposition = findViewById(R.id.medComposition);
        primaryUse = findViewById(R.id.primaryUse);
        medForm = findViewById(R.id.medForm);
        packagingDetails = findViewById(R.id.packagingDetails);
        requirePrescription = findViewById(R.id.require_prescription);
        requirePresIcon = findViewById(R.id.require_pres_icon);
        medDetailLayout = findViewById(R.id.medDetailLayout);
        loadingMedicines = findViewById(R.id.loadingMedicines);

        if (getIntent() != null){
            medicineNameForSearch = getIntent().getStringExtra("medicine_name");
        }
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

        chemistLatLngItems = new NearbyChemistDBHelper(ShowMedicineDetailActivity.this).getNearbyChemist();
        selectedChemistMobItem = new ArrayList<>();
        medicineAvailableItems = new ArrayList<>();


        getMedicineDetailsForStore();

        cartItems = new CartDbHelper(this).getCart();
        cart_count = cartItems.size();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cartItems = new CartDbHelper(this).getCart();
        cart_count = cartItems.size();
        invalidateOptionsMenu();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.medicine_activity_menu, menu);


        MenuItem cart = menu.findItem(R.id.action_cart);
        cart.setIcon(Converter.convertLayoutToImage(ShowMedicineDetailActivity.this, cart_count, R.mipmap.ic_cart_white_icon));
        cart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(ShowMedicineDetailActivity.this, CartActivity.class));
                return true;
            }
        });

        return true;
    }

    private void getMedicineDetailsForStore() {
        String url = "http://prescryp.com/prescriptionUpload/getMedicineDetailsPatient.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            if (success.equalsIgnoreCase("1")) {
                                medicineName.setText(jsonObject.getString("medicine_name"));
                                companyName.setText(jsonObject.getString("medicine_manu"));
                                medComposition.setText(jsonObject.getString("medicine_content"));
                                primaryUse.setText(jsonObject.getString("medicine_use"));
                                String form = "Taken as " + jsonObject.getString("medicine_form");
                                String med_packaging = jsonObject.getString("medicine_packaging");
                                med_packaging_list = med_packaging.split(",");
                                medForm.setText(form);
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
                params.put("medicine_name", medicineNameForSearch);
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }




    private void checkMedicineAvailable(final String chemistMob, final String chemistStoreName) {
        String url = "http://prescryp.com/prescriptionUpload/getMedicineAvailable.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("chemist_medicines");
                            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            if (success.equalsIgnoreCase("1")) {
                                Random rand = new Random();

                                int  n = rand.nextInt(10) + 1;
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    //getting product object from json array
                                    JSONObject product = jsonArray.getJSONObject(i);



                                    medicineAvailableItems.add(new MedicineAvailableItem(chemistMob, chemistStoreName, product.getString("qty"),
                                            product.getString("medicine_packaging"), product.getString("medicine_name"), n));

                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        latch.countDown();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        latch.countDown();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("chemist_mobile_number", chemistMob);
                params.put("medicine_name", medicineNameForSearch);
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

    CountDownLatch latch;
    private void getNearbyChemist() {
        for (ChemistLatLngItem chemistLatLngItem: chemistLatLngItems){
            selectedChemistMobItem.add(new SelectedChemist(chemistLatLngItem.getChemistMobileNumber(), chemistLatLngItem.getChemistStoreName()));
        }
        latch = new CountDownLatch(2*selectedChemistMobItem.size());
        for (final SelectedChemist chemist : selectedChemistMobItem){
            checkMedicineAvailable(chemist.getChemistMobileNumber(), chemist.getChemistStoreName());
            checkPrescriptionRequired(chemist.getChemistMobileNumber());
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                    ShowMedicineDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<MedicinePackagePriorityItem> medicinePackagePriorityItems = new ArrayList<>();
                            for (String pack : med_packaging_list){
                                List<StorePriorityItem> list = new ArrayList<>();
                                medicinePackagePriorityItems.add(new MedicinePackagePriorityItem(pack, list));
                            }
                            for (MedicineAvailableItem item : medicineAvailableItems){
                                Boolean contain = false;
                                for (MedicinePackagePriorityItem groupItem : medicinePackagePriorityItems){
                                    if (item.getPackaging().equalsIgnoreCase(groupItem.getPackage())){
                                        groupItem.getStorePriorityItems().add(new StorePriorityItem(item.getChemistMobileNumber(), item.getChemistStoreName(),
                                                item.getQuantity(), item.getPriority()));
                                        contain = true;
                                    }
                                }
                                if (!contain){
                                    List<StorePriorityItem> list = new ArrayList<>();
                                    list.add(new StorePriorityItem(item.getChemistMobileNumber(), item.getChemistStoreName(),
                                            item.getQuantity(), item.getPriority()));
                                    medicinePackagePriorityItems.add(new MedicinePackagePriorityItem(item.getPackaging(), list));
                                }
                            }

                            ShowMedicineShopAdapter adapter = new ShowMedicineShopAdapter(medicinePackagePriorityItems, ShowMedicineDetailActivity.this, medicineNameForSearch, require_pres);
                            packagingDetails.setAdapter(adapter);
                            packagingDetails.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            adapter.setOnDataChangeListener(new OnDataChangeListener() {
                                @Override
                                public void onDataChanged(int size) {
                                    cartItems = new CartDbHelper(getApplicationContext()).getCart();
                                    cart_count = cartItems.size();
                                    invalidateOptionsMenu();
                                }
                            });


                            if (require_pres.equalsIgnoreCase("Yes")){
                                requirePrescription.setVisibility(View.VISIBLE);
                                requirePresIcon.setVisibility(View.VISIBLE);
                            }else {
                                requirePrescription.setVisibility(View.GONE);
                                requirePresIcon.setVisibility(View.GONE);
                            }


                            loadingMedicines.setVisibility(View.GONE);
                            medDetailLayout.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void checkPrescriptionRequired(final String chemistMob) {
        String url = "http://prescryp.com/prescriptionUpload/checkRequirePrescription.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("require_prescription");
                            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            if (success.equalsIgnoreCase("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    //getting product object from json array
                                    JSONObject product = jsonArray.getJSONObject(i);

                                    if (product.getString("require_pres").equalsIgnoreCase("Yes") && require_pres.equalsIgnoreCase("Yes")){
                                        require_pres = "Yes";
                                    }else if (product.getString("require_pres").equalsIgnoreCase("Yes") && require_pres.equalsIgnoreCase("No")){
                                        require_pres = "Yes";
                                    }else if (product.getString("require_pres").equalsIgnoreCase("No") && require_pres.equalsIgnoreCase("Yes")){
                                        require_pres = "Yes";
                                    }else if (product.getString("require_pres").equalsIgnoreCase("No") && require_pres.equalsIgnoreCase("No")){
                                        require_pres = "No";
                                    }

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        latch.countDown();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        latch.countDown();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("chemist_mobile_number", chemistMob);
                params.put("medicine_name", medicineNameForSearch);
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }


    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        patient_latlng = new LatLng(lat, lng);

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
}
