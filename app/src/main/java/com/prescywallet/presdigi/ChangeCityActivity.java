package com.prescywallet.presdigi;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.prescywallet.presdigi.Adapters.PlaceAutocompleteAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ChangeCityActivity extends AppCompatActivity implements PlaceAutocompleteAdapter.PlaceAutoCompleteInterface, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    private EditText auto_complete_delivery_area;
    private RecyclerView list_search_delivery_area;
    private ConstraintLayout current_location;
    GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    String sender_key;
    private CardView kolkata_card;

    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (getIntent().getStringExtra("SENDER_KEY") != null){
            sender_key = getIntent().getStringExtra("SENDER_KEY");
        }


        current_location = findViewById(R.id.current_location);
        current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeCityActivity.this, AddNewAddressActivity.class);
                intent.putExtra("LOCATION_RESPONSE", "current_location");
                intent.putExtra("SENDER_KEY", sender_key);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        auto_complete_delivery_area = findViewById(R.id.auto_complete_delivery_area);
        list_search_delivery_area = findViewById(R.id.list_search_delivery_area);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        Geocoder geo = new Geocoder(this, new Locale("hi"));

        AutocompleteFilter.Builder builder = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES);

        mAdapter = new PlaceAutocompleteAdapter(ChangeCityActivity.this, R.layout.view_placesearch,
                mGoogleApiClient, BOUNDS_INDIA, builder.build());
        list_search_delivery_area.setAdapter(mAdapter);
        list_search_delivery_area.setHasFixedSize(true);
        list_search_delivery_area.setLayoutManager(new LinearLayoutManager(ChangeCityActivity.this));

        auto_complete_delivery_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    //mClear.setVisibility(View.VISIBLE);
                    if (mAdapter != null) {
                        list_search_delivery_area.setAdapter(mAdapter);
                        if (list_search_delivery_area.getVisibility() == View.GONE)
                            list_search_delivery_area.setVisibility(View.VISIBLE);
                    }
                } else {
                    //mClear.setVisibility(View.GONE);
                    list_search_delivery_area.setVisibility(View.GONE);
                }
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {
//                    Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    Log.e("CLIENT", "NOT CONNECTED");
                }
            }
        });

        kolkata_card = findViewById(R.id.kolkata_card);
        kolkata_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Geocoder coder = new Geocoder(ChangeCityActivity.this);
                List<Address> address;

                try {
                    address = coder.getFromLocationName("Kolkata",1);

                    Address location = address.get(0);
                    Intent intent = new Intent(ChangeCityActivity.this, AddNewAddressActivity.class);
                    intent.putExtra("LOCATION_RESPONSE", "search_address");
                    intent.putExtra("location_locality", location.getLocality());
                    intent.putExtra("location_latitude", String.valueOf(location.getLatitude()));
                    intent.putExtra("location_longitude", String.valueOf(location.getLongitude()));
                    intent.putExtra("SENDER_KEY", sender_key);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }



    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPlaceClick(ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> mResultList, int position) {
        if (mResultList != null) {
            try {
                final String placeId = String.valueOf(mResultList.get(position).placeId);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(@NonNull PlaceBuffer places) {
                        if (places.getCount() == 1) {
                            //Do the things here on Click.....
                            Intent intent = new Intent(ChangeCityActivity.this, AddNewAddressActivity.class);
                            intent.putExtra("LOCATION_RESPONSE", "search_address");
                            intent.putExtra("location_locality", places.get(0).getName());
                            intent.putExtra("location_latitude", String.valueOf(places.get(0).getLatLng().latitude));
                            intent.putExtra("location_longitude", String.valueOf(places.get(0).getLatLng().longitude));
                            intent.putExtra("SENDER_KEY", sender_key);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            //Toast.makeText(getApplicationContext(), "Clickkkkkkkkkk", Toast.LENGTH_SHORT).show();
                            hideSoftInput(ChangeCityActivity.this);
                            //finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception ignored) {

            }

        }
    }

    public static void hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
