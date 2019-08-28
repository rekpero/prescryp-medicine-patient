package com.prescywallet.presdigi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.badoualy.stepperindicator.StepperIndicator;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.prescywallet.presdigi.Adapters.OrderListAdapter;
import com.prescywallet.presdigi.Adapters.PrescriptionForOrderAdapter;
import com.prescywallet.presdigi.Model.CartItem;
import com.prescywallet.presdigi.Model.ListItem;
import com.prescywallet.presdigi.Model.TrackOrderItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class TrackOrderActivity extends AppCompatActivity {
    private String order_number, sender_key, order_rating, order_status;
    private TextView date_of_order_text_view, amount_to_pay_or_paid, grand_total_text_view, order_size, order_status_text_view;
    private TextView subtotal_amount, promo_discount_amount, grand_total_text_view_list, paid_via_text, phone_number_text, complete_delivery_address_text;
    private TextView attached_prescription_number, done_rating, expected_or_delivered, delivery_date_text, executive_text_view, verification_otp;
    private StepperIndicator stepper_indicator_status;
    private RecyclerView order_list_recycler, attached_prescription_list_recycler;
    private ProgressBar loading_order_tracking, updateRating;
    private ScrollView track_order_view;
    private List<TrackOrderItem> order_list;
    private List<ListItem> attached_prescription_list;
    private ImageView down_arrow, up_arrow;
    private CardView attachedPrescriptionCard, ratingCard, deliveryGuyCard;
    private CountDownLatch latch;
    private ConstraintLayout refill_medicines;
    private BroadcastReceiver mReceiver;
    private SmileRating smile_rating;
    private TextView delivery_person_name_text, delivery_phone_number_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent() != null){
            sender_key = getIntent().getStringExtra("Sender_Key");
            order_number = getIntent().getStringExtra("Order_Number");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
        String subtitle = "Order #" + order_number;
        toolbar.setSubtitle(subtitle);

        initUi();


        ratingCard.setVisibility(View.GONE);

        latch = new CountDownLatch(3);

        getOrderForTracking(order_number);

        order_list = new ArrayList<>();

        attached_prescription_list = new ArrayList<>();
        getOrderItem(order_number);

        getAttachPrescription(order_number);


        down_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down_arrow.setVisibility(View.GONE);
                up_arrow.setVisibility(View.VISIBLE);
                attached_prescription_list_recycler.setVisibility(View.VISIBLE);
            }
        });

        up_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up_arrow.setVisibility(View.GONE);
                attached_prescription_list_recycler.setVisibility(View.GONE);
                down_arrow.setVisibility(View.VISIBLE);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                    TrackOrderActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading_order_tracking.setVisibility(View.GONE);
                            track_order_view.setVisibility(View.VISIBLE);
                            changeOrderStatus();

                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        done_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level = smile_rating.getRating();
                done_rating.setVisibility(View.GONE);
                updateRating.setVisibility(View.VISIBLE);
                updateRating(level, v);
            }
        });



    }


    private void initUi() {
        date_of_order_text_view = findViewById(R.id.date_of_order);
        amount_to_pay_or_paid = findViewById(R.id.amount_to_pay_or_paid);
        grand_total_text_view = findViewById(R.id.grand_total);
        order_size = findViewById(R.id.order_size_text);
        order_list_recycler = findViewById(R.id.order_list_recycler);
        loading_order_tracking = findViewById(R.id.loading_order_tracking);
        track_order_view = findViewById(R.id.track_order_view);
        stepper_indicator_status = findViewById(R.id.stepper_indicator_status);
        order_status_text_view = findViewById(R.id.order_status_text_view);
        subtotal_amount = findViewById(R.id.subtotal_amount);
        promo_discount_amount = findViewById(R.id.promo_discount_amount);
        grand_total_text_view_list = findViewById(R.id.grand_total_text_view);
        paid_via_text = findViewById(R.id.paid_via_text);
        phone_number_text = findViewById(R.id.phone_number_text);
        complete_delivery_address_text = findViewById(R.id.complete_delivery_address_text);
        attached_prescription_list_recycler = findViewById(R.id.attached_prescription_list);
        down_arrow = findViewById(R.id.down_arrow);
        up_arrow = findViewById(R.id.up_arrow);
        attached_prescription_number = findViewById(R.id.attached_prescription_number);
        attachedPrescriptionCard = findViewById(R.id.attachedPrescriptionCard);
        refill_medicines = findViewById(R.id.refill_medicines);
        ratingCard = findViewById(R.id.ratingCard);
        done_rating = findViewById(R.id.done_rating);
        smile_rating = findViewById(R.id.smile_rating);
        updateRating = findViewById(R.id.updateRating);
        expected_or_delivered = findViewById(R.id.expected_or_delivered);
        delivery_date_text = findViewById(R.id.delivery_date);
        executive_text_view = findViewById(R.id.executive_text_view);
        deliveryGuyCard = findViewById(R.id.deliveryGuyCard);
        verification_otp = findViewById(R.id.verification_otp);
        delivery_person_name_text = findViewById(R.id.delivery_person_name_text);
        delivery_phone_number_text = findViewById(R.id.delivery_phone_number_text);

    }

    private void getVerificationOTP() {
        String url = "http://prescryp.com/prescriptionUpload/getPatientOTP.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    if (success.equalsIgnoreCase("1") || success.equalsIgnoreCase("3")){
                        String patient_verification_code = jsonObject.getString("patient_verification_code");

                        verification_otp.setText(patient_verification_code);

                        deliveryGuyCard.setVisibility(View.VISIBLE);

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
                params.put("order_number", order_number);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void changeOrderStatus() {
        if (order_status.equalsIgnoreCase("Delivered")){
            refill_medicines.setVisibility(View.VISIBLE);
        }
        if (order_rating.equalsIgnoreCase("")){
            done_rating.setVisibility(View.VISIBLE);
        }else if (order_rating.equalsIgnoreCase("1")){
            done_rating.setVisibility(View.GONE);
            smile_rating.post(new Runnable() {
                @Override
                public void run() {
                    smile_rating.setSelectedSmile(BaseRating.TERRIBLE,true);
                    smile_rating.setClickable(false);
                    smile_rating.setIndicator(true);
                }
            });
        }else if (order_rating.equalsIgnoreCase("2")){
            done_rating.setVisibility(View.GONE);
            smile_rating.post(new Runnable() {
                @Override
                public void run() {
                    smile_rating.setSelectedSmile(BaseRating.BAD,true);
                    smile_rating.setClickable(false);
                    smile_rating.setIndicator(true);
                }
            });
        }else if (order_rating.equalsIgnoreCase("3")){
            done_rating.setVisibility(View.GONE);
            smile_rating.post(new Runnable() {
                @Override
                public void run() {
                    smile_rating.setSelectedSmile(BaseRating.OKAY,true);
                    smile_rating.setClickable(false);
                    smile_rating.setIndicator(true);
                }
            });
        }else if (order_rating.equalsIgnoreCase("4")){
            done_rating.setVisibility(View.GONE);
            smile_rating.post(new Runnable() {
                @Override
                public void run() {
                    smile_rating.setSelectedSmile(BaseRating.GOOD,true);
                    smile_rating.setClickable(false);
                    smile_rating.setIndicator(true);
                }
            });
        }else if (order_rating.equalsIgnoreCase("5")){
            done_rating.setVisibility(View.GONE);
            smile_rating.post(new Runnable() {
                @Override
                public void run() {
                    smile_rating.setSelectedSmile(BaseRating.GREAT,true);
                    smile_rating.setClickable(false);
                    smile_rating.setIndicator(true);
                }
            });
        }
    }

    private void updateRating(final int level, final View v) {
        String url = "http://prescryp.com/prescriptionUpload/updateUserRating.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    if (success.equalsIgnoreCase("1")){
                        Snackbar.make(v, "Thank you for sharing your experience", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        updateRating.setVisibility(View.GONE);
                        smile_rating.setIndicator(true);
                    }

                    latch.countDown();

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
                params.put("order_number", order_number);
                params.put("order_rating", String.valueOf(level));
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                String order_number_received = intent.getStringExtra("Order_Number");
                String order_status_received = intent.getStringExtra("Order_Status");
                //log our message value
                Log.i("ReceivedStatus", order_status_received);

                if (order_number_received.equalsIgnoreCase(order_number) && order_status_received.equalsIgnoreCase("Dispatched")){
                    stepper_indicator_status.setCurrentStep(2);
                    order_status_text_view.setText("Your order is on the way");
                    getVerificationOTP();
                }else if (order_number_received.equalsIgnoreCase(order_number) && order_status_received.equalsIgnoreCase("Delivered")){
                    stepper_indicator_status.setCurrentStep(3);
                    order_status_text_view.setText("Your order has been delivered");
                    deliveryGuyCard.setVisibility(View.GONE);
                    refill_medicines.setVisibility(View.VISIBLE);
                    ratingCard.setVisibility(View.VISIBLE);
                    getOrderForTracking(order_number);
                }

            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);

    }

    private String getDateFormatChange(String date){
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        String outputDateStr = "";
        try {
            Date new_date = inputFormat.parse(date);
            outputDateStr = outputFormat.format(new_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateStr;
    }


    private void getOrderForTracking(final String order_number) {
        String url = "http://prescryp.com/prescriptionUpload/getOrderForTracking.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    String date_of_order = jsonObject.getString("date_of_order");
                    String time_of_order = jsonObject.getString("time_of_order");
                    String subtotal = jsonObject.getString("subtotal");
                    String grand_total = jsonObject.getString("grand_total");
                    String paid_or_not = jsonObject.getString("paid_or_not");
                    order_status = jsonObject.getString("order_status");
                    String payment_type = jsonObject.getString("payment_type");
                    String patient_mobile_number = jsonObject.getString("patient_mobile_number");
                    String delivery_complete_address = jsonObject.getString("delivery_complete_address");
                    order_rating = jsonObject.getString("order_rating");
                    String delivery_date = jsonObject.getString("delivery_date");
                    String delivery_time = jsonObject.getString("delivery_time");
                    String delivery_person_mobile_number = jsonObject.getString("delivery_person_mobile_number");
                    String delivery_person_name = jsonObject.getString("name");

                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    if (success.equalsIgnoreCase("1")){
                        String date_change_format = getDateFormatChange(date_of_order);
                        String[] time_list = time_of_order.split(" ");
                        String time_changed = time_list[0] + " " + time_list[1].toUpperCase();

                        String date_shown = date_change_format.trim() + ", " + time_changed.trim();

                        date_of_order_text_view.setText(date_shown);

                        if (paid_or_not.equalsIgnoreCase("Not Paid")){
                            amount_to_pay_or_paid.setText("AMOUNT TO PAY");
                        }else {
                            amount_to_pay_or_paid.setText("AMOUNT PAID");
                        }

                        Locale locale = new Locale("hi", "IN");
                        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

                        grand_total_text_view.setText(nf.format(Float.valueOf(grand_total)));

                        if (order_status.equalsIgnoreCase("Confirmed")){
                            stepper_indicator_status.setCurrentStep(1);
                            order_status_text_view.setText("Your order has been confirmed");
                        }else if (order_status.equalsIgnoreCase("Dispatched")){
                            stepper_indicator_status.setCurrentStep(2);
                            order_status_text_view.setText("Your order is on the way");
                            delivery_phone_number_text.setText(delivery_person_mobile_number);
                            delivery_person_name_text.setText(delivery_person_name);
                            getVerificationOTP();
                        }else if (order_status.equalsIgnoreCase("Delivered")){
                            stepper_indicator_status.setCurrentStep(3);
                            order_status_text_view.setText("Your order has been delivered");
                            expected_or_delivered.setText("DELIVERED ON");
                            String delivery_date_change_format = getDateFormatChange(delivery_date);
                            String[] delivery_time_list = delivery_time.split(" ");
                            String delivery_time_changed = delivery_time_list[0] + " " + delivery_time_list[1].toUpperCase();

                            String delivery_date_shown = delivery_date_change_format.trim() + ", " + delivery_time_changed.trim();

                            delivery_date_text.setText(delivery_date_shown);
                            executive_text_view.setText("Some problem in order items! Feel free to call us");
                            ratingCard.setVisibility(View.VISIBLE);
                            deliveryGuyCard.setVisibility(View.GONE);
                        }

                        subtotal_amount.setText(nf.format(Float.valueOf(subtotal)));
                        grand_total_text_view_list.setText(nf.format(Float.valueOf(grand_total)));
                        float promo_discount = Float.valueOf(grand_total) - Float.valueOf(subtotal);
                        String promo_dis_text = "-" + nf.format(promo_discount);
                        promo_discount_amount.setText(promo_dis_text);

                        String paid_via = "Paid : " + payment_type;
                        paid_via_text.setText(paid_via);

                        phone_number_text.setText(patient_mobile_number);

                        complete_delivery_address_text.setText(delivery_complete_address);




                    }

                    latch.countDown();

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
                params.put("order_number", order_number);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void getOrderItem(final String order_number) {
        String url = "http://prescryp.com/prescriptionUpload/getOrderItem.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("order_items");
                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    if (success.equalsIgnoreCase("1")){
                        for (int i = 0; i < jsonArray.length(); i++) {

                            //getting product object from json array
                            JSONObject order_item = jsonArray.getJSONObject(i);

                            String medicine_name = order_item.getString("medicine_name");
                            String package_contain = order_item.getString("package_contain");
                            String quantity = order_item.getString("quantity");
                            String price = order_item.getString("price");
                            String seller_mobile_number = order_item.getString("seller_mobile_number");
                            String seller_name = order_item.getString("seller_name");
                            String order_status = order_item.getString("order_status");


                            order_list.add(new TrackOrderItem(medicine_name, quantity, price, seller_name, seller_mobile_number, package_contain, order_status));


                        }
                        String order_size_shown = "Your Orders (" + order_list.size() + ")";
                        order_size.setText(order_size_shown);
                        OrderListAdapter adapter = new OrderListAdapter(order_list, getApplicationContext(), TrackOrderActivity.this);
                        order_list_recycler.setAdapter(adapter);
                        order_list_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        order_list_recycler.setHasFixedSize(true);
                    }

                    latch.countDown();

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
                params.put("order_number", order_number);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    @Override
    public void onBackPressed() {

        if (sender_key.equalsIgnoreCase("From_Order_Summary")){
            startActivity(new Intent(TrackOrderActivity.this, MedicineActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        }else {
            super.onBackPressed();
        }

    }

    private void getAttachPrescription(final String order_number) {
        String url = "http://prescryp.com/prescriptionUpload/getAttachedPrescription.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("attached_prescription");
                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    if (success.equalsIgnoreCase("1")){
                        for (int i = 0; i < jsonArray.length(); i++) {

                            //getting product object from json array
                            JSONObject attached_prescription = jsonArray.getJSONObject(i);
                            attached_prescription_list.add(new ListItem(attached_prescription.getString("prescription_id"),
                                    attached_prescription.getString("dateOfCreation"), attached_prescription.getString("status"), attached_prescription.getString("imagePath")));


                        }
                            String attached_pres_size = "Attached Prescription (" + attached_prescription_list.size() + ")";
                            attached_prescription_number.setText(attached_pres_size);
                            PrescriptionForOrderAdapter adapter = new PrescriptionForOrderAdapter(attached_prescription_list, getApplicationContext());
                            attached_prescription_list_recycler.setAdapter(adapter);
                            attached_prescription_list_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            attached_prescription_list_recycler.setHasFixedSize(true);



                    }else if (success.equalsIgnoreCase("2")){
                        attachedPrescriptionCard.setVisibility(View.GONE);
                    }

                    latch.countDown();

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
                params.put("order_number", order_number);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

}
