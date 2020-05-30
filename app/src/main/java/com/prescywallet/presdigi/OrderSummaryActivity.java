package com.prescywallet.presdigi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.prescywallet.presdigi.Adapters.CheckoutCartListAdapter;
import com.prescywallet.presdigi.Interface.OnDataChangeListener;
import com.prescywallet.presdigi.Model.AttachedDigitalPrescriptionItem;
import com.prescywallet.presdigi.Model.CartItem;
import com.prescywallet.presdigi.Model.ChemistLatLngItem;
import com.prescywallet.presdigi.Model.PrescriptionImagePathItem;
import com.prescywallet.presdigi.Session.LocationSessionManager;
import com.prescywallet.presdigi.Session.MobileNumberSessionManager;
import com.prescywallet.presdigi.database.AttachedDigitalPrescriptionDBHelper;
import com.prescywallet.presdigi.database.AttachedPrescriptionDBHelper;
import com.prescywallet.presdigi.database.CartDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class OrderSummaryActivity extends AppCompatActivity {

    MobileNumberSessionManager mobileNumberSessionManager;
    private TextView personal_detail_text_view, delivering_detail_text_view, attach_prescription_text_view, change_delivery, store_not_available;
    private String person_detail, delivery_nickname, delivery_locality, complete_delivery_address, delivery_instruction, delivery_latitude,
            delivery_longitude;
    private ImageView checked_icon, cancel_icon;
    private List<PrescriptionImagePathItem> imagePathItems;
    private List<AttachedDigitalPrescriptionItem> digitalPrescriptionItems;
    private List<String> uniqueChemistMobNum;
    private List<CartItem> cartItems;
    private RecyclerView cart_list_checkout;
    private CheckoutCartListAdapter cartListAdapter;
    private CardView require_prescription_card;
    private TextView subtotal_amount, promo_discount_amount, grand_total, view_different_address, confirm_text_view;
    private List<ChemistLatLngItem> chemistLatLngItems;
    LocationSessionManager sessionManager;
    private ConstraintLayout confirm_btn;
    ProgressDialog mDialog;
    private String session_name, session_mob;
    private int subtotal;
    private float grand_tot;
    private String order_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
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

        personal_detail_text_view = findViewById(R.id.personal_detail_text_view);
        delivering_detail_text_view = findViewById(R.id.delivering_detail_text_view);
        cart_list_checkout = findViewById(R.id.cart_list_checkout);
        attach_prescription_text_view = findViewById(R.id.attach_prescription_text_view);
        require_prescription_card = findViewById(R.id.require_prescription_card);
        subtotal_amount = findViewById(R.id.subtotal_amount);
        promo_discount_amount = findViewById(R.id.promo_discount_amount);
        grand_total = findViewById(R.id.grand_total);
        change_delivery = findViewById(R.id.change_delivery);
        store_not_available = findViewById(R.id.store_not_available);
        checked_icon = findViewById(R.id.checked_icon);
        cancel_icon = findViewById(R.id.cancel_icon);
        view_different_address = findViewById(R.id.view_different_address);
        confirm_text_view = findViewById(R.id.confirm_text_view);
        confirm_btn = findViewById(R.id.confirm_btn);


        sessionManager = new LocationSessionManager(getApplicationContext());

        mobileNumberSessionManager = new MobileNumberSessionManager(getApplicationContext());
        HashMap<String, String> user = mobileNumberSessionManager.geMobileDetails();
        session_name = user.get(MobileNumberSessionManager.KEY_NAME);
        session_mob = user.get(MobileNumberSessionManager.KEY_MOB);

        person_detail = session_name + ", " + session_mob;
        personal_detail_text_view.setText(person_detail);
        imagePathItems = new ArrayList<>();
        digitalPrescriptionItems = new ArrayList<>();

        if (getIntent() != null){
            delivery_nickname = getIntent().getStringExtra("DELIVERY_NICKNAME");
            delivery_locality = getIntent().getStringExtra("DELIVERY_LOCALITY");
            complete_delivery_address = getIntent().getStringExtra("COMPLETE_DELIVERY_ADDRESS");
            delivery_instruction = getIntent().getStringExtra("DELIVERY_INSTRUCTION");
            delivery_latitude = getIntent().getStringExtra("DELIVERY_LATITUDE");
            delivery_longitude = getIntent().getStringExtra("DELIVERY_LONGITUDE");
        }


        cartItems = new ArrayList<>();
        chemistLatLngItems =new ArrayList<>();
        loadCart();

        change_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderSummaryActivity.this, SelectAddressForCheckoutActivity.class));
            }
        });


    }

    private void loadCart() {

        cartItems = new CartDbHelper(this).getCart();
        getChemistOnSelectedAddress();

        cartListAdapter = new CheckoutCartListAdapter(cartItems, this);
        cart_list_checkout.setAdapter(cartListAdapter);
        cart_list_checkout.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        cartListAdapter.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onDataChanged(int size) {
                cartItems = new CartDbHelper(getApplicationContext()).getCart();
                subtotal = 0;
                for (CartItem cartItem : cartItems){
                    subtotal += Float.valueOf(cartItem.getPrice())*Integer.valueOf(cartItem.getQuantity());
                }
                Locale locale = new Locale("hi", "IN");
                NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

                subtotal_amount.setText(nf.format(subtotal));
                float promo = (float) (subtotal*0.1);
                String prome_dis = "-"+ nf.format(promo);
                promo_discount_amount.setText(prome_dis);

                grand_tot = subtotal-promo;
                grand_total.setText(nf.format(grand_tot));

                String confirm_text = "Confirm (" + nf.format(grand_tot) + ")";
                confirm_text_view.setText(confirm_text);

            }
        });
        subtotal = 0;
        boolean checkPresReq = false;
        for (CartItem cartItem : cartItems){
            subtotal += Float.valueOf(cartItem.getPrice())*Integer.valueOf(cartItem.getQuantity());
            if (cartItem.getRequirePrescription().equalsIgnoreCase("Yes")){
                checkPresReq = true;
            }
        }

        if (checkPresReq){
            imagePathItems = new AttachedPrescriptionDBHelper(getApplicationContext()).getImagePath();
            digitalPrescriptionItems = new AttachedDigitalPrescriptionDBHelper(getApplicationContext()).getDigitalPrescription();

            int total_attachment = imagePathItems.size() + digitalPrescriptionItems.size();

            String attach = "Attached Prescription (" + total_attachment + ")";
            attach_prescription_text_view.setText(attach);
            attach_prescription_text_view.setVisibility(View.VISIBLE);
            require_prescription_card.setVisibility(View.VISIBLE);
        }else {
            attach_prescription_text_view.setVisibility(View.GONE);
            require_prescription_card.setVisibility(View.GONE);
        }
        Locale locale = new Locale("hi", "IN");
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

        subtotal_amount.setText(nf.format(subtotal));
        float promo = (float) (subtotal*0.1);
        String prome_dis = "-"+ nf.format(promo);
        promo_discount_amount.setText(prome_dis);

        grand_tot = subtotal-promo;
        grand_total.setText(nf.format(grand_tot));

        String confirm_text = "Confirm (" + nf.format(grand_tot) + ")";
        confirm_text_view.setText(confirm_text);

    }
    boolean not_near = false;
    CountDownLatch latch;
    private void getChemistOnSelectedAddress() {
        List<String> sellerMobNumber = new ArrayList<>();
        for (CartItem cartItem : cartItems){
            if (!sellerMobNumber.contains(cartItem.getSellerMobileNumber())){
                sellerMobNumber.add(cartItem.getSellerMobileNumber());
            }
        }
        latch = new CountDownLatch(sellerMobNumber.size());
        for (String mobNum : sellerMobNumber){
            getSellerLatLng(mobNum);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                    OrderSummaryActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (not_near){
                                checked_icon.setVisibility(View.GONE);
                                cancel_icon.setVisibility(View.VISIBLE);
                                store_not_available.setVisibility(View.VISIBLE);
                                view_different_address.setVisibility(View.VISIBLE);
                            }else {
                                cancel_icon.setVisibility(View.GONE);
                                store_not_available.setVisibility(View.GONE);
                                view_different_address.setVisibility(View.GONE);
                                checked_icon.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();




        final String deliver_to = delivery_nickname + " (" + delivery_locality + ")";
        delivering_detail_text_view.setText(deliver_to);

        view_different_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CartDbHelper(getApplicationContext()).deleteAllCart();
                sessionManager.createLocationSession(deliver_to, delivery_latitude, delivery_longitude);
                Intent intent = new Intent(OrderSummaryActivity.this, MedicineActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!not_near){
                    mDialog = new ProgressDialog(OrderSummaryActivity.this);
                    mDialog.setMessage("Sending Order Details...");
                    mDialog.show();
                    getDeliveryPerson(mDialog);

                }else {
                    Toast.makeText(getApplicationContext(), "Select Correct Delivery Address", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void getDeliveryPerson(final ProgressDialog mDialog) {
        final List<String> delivery = new ArrayList<>();
        String url = "http://prescryp.com/prescriptionUpload/getDeliveryPerson.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("delivery_person");
                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    if (success.equalsIgnoreCase("1")){
                        for (int i = 0; i < jsonArray.length(); i++) {

                            //getting product object from json array
                            JSONObject delivery_person = jsonArray.getJSONObject(i);


                            delivery.add(delivery_person.getString("mobile_number"));

                        }

                        Random rand = new Random();

                        int  n = rand.nextInt(delivery.size());

                        String selected_delivery_person = delivery.get(n);
                        insertOrderDetail(selected_delivery_person, mDialog);

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

    private void insertOrderDetail(final String selected_delivery_person, final ProgressDialog mDialog) {
        String url = "http://prescryp.com/prescriptionUpload/insertOrder.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    if (success.equalsIgnoreCase("1")){
                        order_number = jsonObject.getString("order_number");
                        if (imagePathItems.size() == 0 && digitalPrescriptionItems.size() != 0){
                            insertAttachedPrescription(mDialog, order_number);
                        }else if (imagePathItems.size() != 0 && digitalPrescriptionItems.size() == 0){
                            digitalPrescriptionItems = new ArrayList<>();
                            uploadPrescription(mDialog, order_number);
                        }else if (imagePathItems.size() != 0){
                            uploadPrescription(mDialog, order_number);
                        }else {
                            insertOrderItems(mDialog, order_number);
                        }

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
                params.put("patient_mobile_number", session_mob);
                params.put("patient_name", session_name);
                params.put("delivery_locality", delivery_locality);
                params.put("delivery_complete_address", complete_delivery_address);
                params.put("delivery_instruction", delivery_instruction);
                params.put("delivery_latitude", delivery_latitude);
                params.put("delivery_longitude", delivery_longitude);
                params.put("subtotal", String.valueOf(subtotal));
                params.put("grand_total", String.valueOf(grand_tot));
                params.put("payment_type", "Cash on Delivery");
                params.put("paid_or_not", "Not Paid");
                params.put("delivery_person_mobile_number", selected_delivery_person);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }
    CountDownLatch latch1, latch2, latch3, latch4;
    private void insertOrderItems(final ProgressDialog mDialog, final String order_number) {
        mDialog.setMessage("Placing your Medicines...");
        uniqueChemistMobNum = new ArrayList<>();
        latch3 = new CountDownLatch(cartItems.size());
        for (CartItem cartItem : cartItems){
            insertOrderItemsDB(order_number, cartItem.getMedicineName(), cartItem.getPackageContain(), cartItem.getQuantity(), cartItem.getPrice(), cartItem.getSellerMobileNumber(), cartItem.getSellerName());
            boolean mobile_num_exist = false;
            for (String mob_num : uniqueChemistMobNum){
                if (mob_num.equalsIgnoreCase(cartItem.getSellerMobileNumber())){
                    mobile_num_exist = true;
                }
            }

            if (!mobile_num_exist){
                uniqueChemistMobNum.add(cartItem.getSellerMobileNumber());
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch3.await();
                    OrderSummaryActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sendOrderToChemist(mDialog, order_number);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendOrderToChemist(final ProgressDialog mDialog, final String order_number) {
        latch4 = new CountDownLatch(uniqueChemistMobNum.size());
        for (String mobile_number : uniqueChemistMobNum){
            sendOrderToChemistFCM(mobile_number, order_number);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch4.await();
                    OrderSummaryActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            new CartDbHelper(OrderSummaryActivity.this).deleteAllCart();
                            new AttachedPrescriptionDBHelper(getApplicationContext()).deleteAllDigitalPrescription();
                            new AttachedDigitalPrescriptionDBHelper(getApplicationContext()).deleteAllDigitalPrescription();
                            Intent intent = new Intent(OrderSummaryActivity.this, TrackOrderActivity.class);
                            intent.putExtra("Sender_Key", "From_Order_Summary");
                            intent.putExtra("Order_Number", order_number);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendOrderToChemistFCM(final String mobile_number, final String order_number) {
        String url = "http://prescryp.com/prescriptionUpload/sendOrderToChemist.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equalsIgnoreCase("1")){
                        Toast.makeText(getApplicationContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                latch4.countDown();


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
                params.put("chemist_mobile_number", mobile_number);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void insertOrderItemsDB(final String order_number, final String medicineName, final String packageContain, final String quantity, final String price, final String sellerMobileNumber, final String sellerName) {
        String url = "http://prescryp.com/prescriptionUpload/insertOrderItems.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    latch3.countDown();

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
                params.put("medicine_name", medicineName);
                params.put("package_contain", packageContain);
                params.put("quantity", quantity);
                params.put("price", price);
                params.put("seller_mobile_number", sellerMobileNumber);
                params.put("seller_name", sellerName);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }


    private void insertAttachedPrescription(final ProgressDialog mDialog, final String order_number) {
        mDialog.setMessage("Attaching your Prescriptions...");
        latch1 = new CountDownLatch(digitalPrescriptionItems.size());
        for (AttachedDigitalPrescriptionItem prescriptionItem : digitalPrescriptionItems){
            insertAttachedPrescriptionDB(order_number, prescriptionItem.getPrescriptionId());
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch1.await();
                    OrderSummaryActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            insertOrderItems(mDialog, order_number);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void insertAttachedPrescriptionDB(final String order_number, final String prescriptionId) {
        String url = "http://prescryp.com/prescriptionUpload/insertAttachedPrescription.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    latch1.countDown();

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
                mobileNumberSessionManager = new MobileNumberSessionManager(getApplicationContext());
                final HashMap<String, String> detail = mobileNumberSessionManager.geMobileDetails();
                HashMap<String, String> params = new HashMap<>();
                params.put("order_number", order_number);
                params.put("prescription_id", prescriptionId);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void uploadPrescription(final ProgressDialog mDialog, final String order_number) {
        mDialog.setMessage("Uploading your Prescriptions...");
        latch2 = new CountDownLatch(imagePathItems.size());
        for (PrescriptionImagePathItem pathItem : imagePathItems){
            uploadThisPrescription(pathItem.getPrescriptionImagePath());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch2.await();
                    OrderSummaryActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            insertAttachedPrescription(mDialog, order_number);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void uploadThisPrescription(String prescriptionImagePath) {
        try {
            Bitmap bitmap = ImageLoader.init().from(prescriptionImagePath).requestSize(256, 512).getBitmap();
            final String encodedImage = ImageBase64.encode(bitmap);

            String url = "http://prescryp.com/prescriptionUpload/uploadImage.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        String message = jsonObject.getString("message");
                        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        if (success.equalsIgnoreCase("1")){
                            String presId = jsonObject.getString("prescription_id");
                            String date = jsonObject.getString("date_pres");
                            digitalPrescriptionItems.add(new AttachedDigitalPrescriptionItem(presId, date));

                        }
                        latch2.countDown();

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
                    mobileNumberSessionManager = new MobileNumberSessionManager(getApplicationContext());
                    final HashMap<String, String> detail = mobileNumberSessionManager.geMobileDetails();
                    HashMap<String, String> params = new HashMap<>();
                    params.put("image", encodedImage);
                    params.put("mobilenumber", detail.get(MobileNumberSessionManager.KEY_MOB));
                    params.put("name", detail.get(MobileNumberSessionManager.KEY_NAME));
                    return params;
                }
            };
            Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void getSellerLatLng(final String mobNum) {
        String url = "http://prescryp.com/prescriptionUpload/getChemistLatLong.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("chemist");
                            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            if (success.equalsIgnoreCase("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    //getting product object from json array
                                    JSONObject product = jsonArray.getJSONObject(i);

                                    chemistLatLngItems.add(new ChemistLatLngItem(product.getString("chemist_mobile_number"),
                                            product.getString("chemist_store_name"),
                                            new LatLng(Double.valueOf(product.getString("latitude")),
                                                    Double.valueOf(product.getString("longitude")))));

                                }

                                for (ChemistLatLngItem item : chemistLatLngItems){
                                    Location locationA = new Location("point A");
                                    locationA.setLatitude(Double.valueOf(delivery_latitude));
                                    locationA.setLongitude(Double.valueOf(delivery_longitude));

                                    Location locationB = new Location("point B");
                                    locationB.setLatitude(item.getLatLng().latitude);
                                    locationB.setLongitude(item.getLatLng().longitude);

                                    double distance = locationA.distanceTo(locationB);

                                    if (distance > 5000){
                                        not_near = true;
                                    }
                                }



                            }
                            latch.countDown();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
                params.put("mobilenumber", mobNum);
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

}
