package com.prescywallet.presdigi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.prescywallet.presdigi.Adapters.AllMedicineDbAdapter;
import com.prescywallet.presdigi.Model.MedicineObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SearchMedicineActivity extends AppCompatActivity {
    private AllMedicineDbAdapter medAdapter;
    private RecyclerView medListShow;
    private ProgressBar loadingMed;
    private ArrayList<MedicineObject> medListItems;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_medicine);


        medListShow = findViewById(R.id.med_list_show);
        EditText medSearchText = findViewById(R.id.med_search_input);
        loadingMed = findViewById(R.id.loading_medicines);
        ImageView backBtn = findViewById(R.id.backIcon);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        medListItems = new ArrayList<>();
        loadingMed.setVisibility(View.GONE);


        medSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.toString().length() > 0){
                    //Toast.makeText(getApplicationContext(), s.toString(), Toast.LENGTH_SHORT).show();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // do your actual work here
                            SearchMedicineActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    medListItems.clear();
                                    getMedView(s.toString().toUpperCase());
                                    loadingMed.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                    }, 600);

                }else if (s.toString().matches("")){
                    //Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            SearchMedicineActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    medListItems.clear();
                                    medAdapter = new AllMedicineDbAdapter(medListItems, SearchMedicineActivity.this);
                                    medListShow.setAdapter(medAdapter);
                                    medListShow.setLayoutManager(new LinearLayoutManager(SearchMedicineActivity.this));
                                }
                            });

                        }
                    }, 600);

                }

            }
        });

    }


    public void getMedView(final String medicineName){
        String url =  "http://prescryp.com/prescriptionUpload/getAllMedDb.php";
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
                            JSONArray jsonArray = jsonObject.getJSONArray("medicines");

                            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            //traversing through all the object
                            if (success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    //getting product object from json array
                                    JSONObject product = jsonArray.getJSONObject(i);

                                    //adding the product to product list
                                    medListItems.add(new MedicineObject(product.get("medicine_name").toString()));



                                }
                                medAdapter = new AllMedicineDbAdapter(medListItems, SearchMedicineActivity.this);
                                medListShow.setAdapter(medAdapter);
                                medListShow.setLayoutManager(new LinearLayoutManager(SearchMedicineActivity.this));


                            }else if (success.equals("2")){
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                            loadingMed.setVisibility(View.GONE);


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
                params.put("medicine_name", medicineName);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);


    }

}
