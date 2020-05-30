package com.prescywallet.presdigi;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class YourOrderListActivity extends AppCompatActivity {
    private RecyclerView order_history;
    private List<OrderHistoryItem> orderHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_order_list);
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

        order_history = findViewById(R.id.order_history_list_recycler);
        orderHistoryList = new ArrayList<>();
        loadOrderHistory();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        orderHistoryList = new ArrayList<>();
        loadOrderHistory();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadOrderHistory() {

        MobileNumberSessionManager mobileNumberSessionManager = new MobileNumberSessionManager(getApplicationContext());
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
                        for (int i = 0; i < jsonArray.length(); i++) {

                            //getting product object from json array
                            JSONObject order_history_item = jsonArray.getJSONObject(i);

                            orderHistoryList.add(new OrderHistoryItem(order_history_item.getString("order_number"),
                                    order_history_item.getString("date_of_order"),
                                    order_history_item.getString("time_of_order"),
                                    order_history_item.getString("order_status"),
                                    order_history_item.getString("grand_total")));


                        }

                        OrderHistoryAdapter adapter = new OrderHistoryAdapter(orderHistoryList, getApplicationContext());
                        order_history.setAdapter(adapter);
                        order_history.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

}
