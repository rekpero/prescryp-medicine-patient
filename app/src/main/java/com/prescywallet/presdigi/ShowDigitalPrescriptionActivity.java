package com.prescywallet.presdigi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.prescywallet.presdigi.Adapters.PresGridAdapter;
import com.prescywallet.presdigi.Model.ListItem;
import com.prescywallet.presdigi.Session.MobileNumberSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowDigitalPrescriptionActivity extends AppCompatActivity {
    GridView predGrid;
    private PresGridAdapter adapter_completed;
    private List<ListItem> listItem_completed;
    MobileNumberSessionManager mobileNumberSessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_digital_prescription);
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
        predGrid = findViewById(R.id.presGrid);
        listItem_completed = new ArrayList<>();
        getPrescription();

    }

    private void getPrescription(){
        mobileNumberSessionManager = new MobileNumberSessionManager(ShowDigitalPrescriptionActivity.this);
        final HashMap<String, String> detail = mobileNumberSessionManager.geMobileDetails();
        String url =  "http://prescryp.com/prescriptionUpload/getPrescriptionDetail.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting the string to json array object
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            //traversing through all the object
                            if (success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    //getting product object from json array
                                    JSONObject product = jsonArray.getJSONObject(i);

                                    //adding the product to product list
                                    if (product.getString("status").equalsIgnoreCase("Completed")){
                                        listItem_completed.add(new ListItem(
                                                product.getString("prescription_id"),
                                                "Date : " + product.getString("date"),
                                                "Status : " + product.getString("status"),
                                                product.getString("image")
                                        ));
                                    }


                                }
                                if (getApplicationContext() != null){
                                    adapter_completed = new PresGridAdapter(getApplicationContext(), R.layout.grid_item_list, listItem_completed, "Attach Prescription");

                                    predGrid.setAdapter(adapter_completed);
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
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("mobilenumber", detail.get(MobileNumberSessionManager.KEY_MOB));
                params.put("name", detail.get(MobileNumberSessionManager.KEY_NAME));
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


    }


}
