package com.prescywallet.presdigi.Adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.prescywallet.presdigi.Interface.OnDataClickListener;
import com.prescywallet.presdigi.Model.StorePriorityItem;
import com.prescywallet.presdigi.R;
import com.prescywallet.presdigi.Session.LocationSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChangeSellerAdapter extends RecyclerView.Adapter<ChangeSellerAdapter.ViewHolder>{

    private List<StorePriorityItem> listItems;
    private Context context;
    OnDataClickListener onDataClickListener;


    public ChangeSellerAdapter(List<StorePriorityItem> listItems, Context context){
        this.listItems = listItems;
        this.context = context;
    }

    public void setOnDataClickListener(OnDataClickListener onDataClickListener){

        this.onDataClickListener = onDataClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seller_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final StorePriorityItem listItem = listItems.get(position);

        getStoreAddress(listItem, holder, position);


    }

    private void getStoreAddress(final StorePriorityItem listItem, final ViewHolder holder, final int position) {
        String url = "http://prescryp.com/prescriptionUpload/getSpecificSellerLatLng.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            JSONArray chemist = jsonObject.getJSONArray("chemist");

                            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            if (success.equalsIgnoreCase("1")) {
                                String store_latitude = chemist.getJSONObject(0).getString("latitude");
                                String store_longitude = chemist.getJSONObject(0).getString("longitude");
                                LatLng store_latlng = new LatLng(Double.valueOf(store_latitude), Double.valueOf(store_longitude));
                                Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                                holder.seller_name.setText(listItem.getChemistName());

                                holder.seller_layout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        onDataClickListener.onDataClick(position);
                                    }
                                });
                                String quantity = listItem.getQuantity() + " QTY";
                                holder.quantity.setText(quantity);
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(store_latlng.latitude, store_latlng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                    String[] address_list = addresses.get(0).getAddressLine(0).split(",");
                                    String store_address = address_list[address_list.length-4].trim().toUpperCase();
                                    holder.store_address.setText(store_address);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                LocationSessionManager sessionManager;
                                sessionManager = new LocationSessionManager(context);
                                if (sessionManager.isLocationLoggedIn()){
                                    HashMap<String, String> user = sessionManager.getLocationDetails();
                                    String location_lat = user.get(LocationSessionManager.KEY_LOCATION_LATITUDE);
                                    String location_lng = user.get(LocationSessionManager.KEY_LOCATION_LONGITUDE);
                                    LatLng patient_latlng = new LatLng(Double.valueOf(location_lat), Double.valueOf(location_lng));
                                    Location locationA = new Location("Patient Location");
                                    locationA.setLatitude(patient_latlng.latitude);
                                    locationA.setLongitude(patient_latlng.longitude);

                                    Location locationB = new Location("Store Location");
                                    locationB.setLatitude(store_latlng.latitude);
                                    locationB.setLongitude(store_latlng.longitude);


                                    double distance = locationA.distanceTo(locationB);
                                    int store_dist = (int) distance;
                                    String distance_shown;
                                    if (store_dist > 1000){
                                        double store_dist_double = store_dist/1000.00;
                                        distance_shown = store_dist_double + " km";
                                    }else {
                                        distance_shown = store_dist + " m";
                                    }
                                    holder.distance_store.setText(distance_shown);

                                }


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
                params.put("mobilenumber", listItem.getChemistMobile());
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView seller_name, quantity,distance_store, store_address;
        public ConstraintLayout seller_layout;

        public ViewHolder(View itemview){
            super(itemview);
            seller_name = itemview.findViewById(R.id.seller_name);
            seller_layout = itemview.findViewById(R.id.seller_layout);
            quantity = itemview.findViewById(R.id.quantity);
            store_address = itemview.findViewById(R.id.store_address);
            distance_store = itemview.findViewById(R.id.distance_store);

        }
    }
}
