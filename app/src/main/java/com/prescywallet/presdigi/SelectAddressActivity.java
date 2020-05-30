package com.prescywallet.presdigi;

import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.prescywallet.presdigi.Adapters.PlaceAutocompleteAdapter;
import com.prescywallet.presdigi.Adapters.SavedAddressListAdapter;
import com.prescywallet.presdigi.Model.DeliveryAddressItem;
import com.prescywallet.presdigi.Session.LocationSessionManager;
import com.prescywallet.presdigi.database.PatientAddressDBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectAddressActivity extends AppCompatActivity implements PlaceAutocompleteAdapter.PlaceAutoCompleteInterface, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private ConstraintLayout current_location, add_address;
    private RecyclerView saved_address_recycler_view;
    private SavedAddressListAdapter addressListAdapter;
    private List<DeliveryAddressItem> addressItems;
    GoogleApiClient mGoogleApiClient;
    private EditText auto_complete;
    private RecyclerView list_search;
    private PlaceAutocompleteAdapter mAdapter;
    LocationSessionManager sessionManager;


    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
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

        current_location = findViewById(R.id.current_location_text_view);
        current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectAddressActivity.this, MedicineActivity.class);
                intent.putExtra("LOCATION_RESPONSE", "current_location");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        add_address = findViewById(R.id.add_address_text_view);
        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectAddressActivity.this, AddNewAddressActivity.class);
                intent.putExtra("SENDER_KEY", "for_medicine");
                startActivity(intent);
            }
        });

        saved_address_recycler_view = findViewById(R.id.saved_address_recycler_view);
        addressItems = new PatientAddressDBHelper(SelectAddressActivity.this).getAllDeliveryAddress();

        addressListAdapter = new SavedAddressListAdapter(addressItems, SelectAddressActivity.this, "Medicine");
        saved_address_recycler_view.setAdapter(addressListAdapter);
        saved_address_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        auto_complete = findViewById(R.id.auto_complete);
        list_search = findViewById(R.id.list_search);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        Geocoder geo = new Geocoder(this, new Locale("hi"));

        mAdapter = new PlaceAutocompleteAdapter(SelectAddressActivity.this, R.layout.view_placesearch,
                mGoogleApiClient, BOUNDS_INDIA, null);
        list_search.setAdapter(mAdapter);
        list_search.setHasFixedSize(true);
        list_search.setLayoutManager(new LinearLayoutManager(SelectAddressActivity.this));

        auto_complete.addTextChangedListener(new TextWatcher() {
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
                        list_search.setAdapter(mAdapter);
                        if (list_search.getVisibility() == View.GONE)
                            list_search.setVisibility(View.VISIBLE);
                    }
                } else {
                    //mClear.setVisibility(View.GONE);
                    list_search.setVisibility(View.GONE);
                }
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {
//                    Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    Log.e("CLIENT", "NOT CONNECTED");
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
                            sessionManager.createLocationSession(places.get(0).getName().toString(), String.valueOf(places.get(0).getLatLng().latitude),
                                    String.valueOf(places.get(0).getLatLng().longitude));
                            Intent intent = new Intent(SelectAddressActivity.this, MedicineActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            //Toast.makeText(getApplicationContext(), "Clickkkkkkkkkk", Toast.LENGTH_SHORT).show();
                            hideSoftInput(SelectAddressActivity.this);
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
