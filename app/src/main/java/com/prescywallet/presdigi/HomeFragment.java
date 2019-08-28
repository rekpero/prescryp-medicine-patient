package com.prescywallet.presdigi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.prescywallet.presdigi.Adapters.OrderHistoryAdapter;
import com.prescywallet.presdigi.Model.OrderHistoryItem;
import com.prescywallet.presdigi.Session.MobileNumberSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class HomeFragment extends Fragment {


    private RelativeLayout alarm, medicine, p_folder;
    private RecyclerView past_order;
    private List<OrderHistoryItem> pastOrderList;
    private TextView past_order_heading, show_more_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.app_bar_main, container, false);
        if (container != null) {
            container.removeAllViews();
        }
        HomePage(view);
        return view;
    }

    private void HomePage(View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        alarm = view.findViewById(R.id.alarm);
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null){
                    Intent intent = new Intent(getActivity(), ReminderActivity.class);
                    startActivity(intent);
                }
            }
        });

        medicine = view.findViewById(R.id.medicineSearch);
        medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null){
                    Intent intent = new Intent(getActivity(), MedicineActivity.class);
                    startActivity(intent);
                }
            }
        });

        p_folder = view.findViewById(R.id.folder);
        p_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null){
                    startActivity(new Intent(getActivity(), PrescriptionGroupingActivity.class));
                }
            }
        });

        past_order = view.findViewById(R.id.past_order_recycler);
        past_order_heading = view.findViewById(R.id.past_order_heading);
        show_more_text = view.findViewById(R.id.show_more_text);
        pastOrderList = new ArrayList<>();
        loadOrderHistory();

        show_more_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null){
                    startActivity(new Intent(getActivity(), YourOrderListActivity.class));
                }
            }
        });


    }

    private void loadOrderHistory() {

        MobileNumberSessionManager mobileNumberSessionManager = new MobileNumberSessionManager(getContext());
        HashMap<String, String> user = mobileNumberSessionManager.geMobileDetails();
        final String session_mob = user.get(MobileNumberSessionManager.KEY_MOB);
        String url = "http://prescryp.com/prescriptionUpload/getAllOrders.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("order_history");
                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    if (success.equalsIgnoreCase("1")){
                        int k = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {

                            //getting product object from json array
                            JSONObject order_history_item = jsonArray.getJSONObject(i);

                            if (k < 3){
                                if (order_history_item.getString("order_status").equalsIgnoreCase("Delivered")){
                                    pastOrderList.add(new OrderHistoryItem(order_history_item.getString("order_number"),
                                            order_history_item.getString("date_of_order"),
                                            order_history_item.getString("time_of_order"),
                                            order_history_item.getString("order_status"),
                                            order_history_item.getString("grand_total")));

                                }
                            }


                            k++;

                        }

                        if (pastOrderList.size() == 0){
                            past_order_heading.setVisibility(View.GONE);
                            past_order.setVisibility(View.GONE);
                            show_more_text.setVisibility(View.GONE);
                        }else {
                            past_order_heading.setVisibility(View.VISIBLE);
                            past_order.setVisibility(View.VISIBLE);
                            show_more_text.setVisibility(View.VISIBLE);
                        }
                        OrderHistoryAdapter adapter = new OrderHistoryAdapter(pastOrderList, getContext());
                        past_order.setAdapter(adapter);
                        past_order.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("mobile_number", session_mob);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }




}
