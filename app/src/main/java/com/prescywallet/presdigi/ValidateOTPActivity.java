package com.prescywallet.presdigi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.prescywallet.presdigi.Session.MobileNumberSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ValidateOTPActivity extends AppCompatActivity {

    private PinView otp_pinview;
    private String img_url, custId, name, email, id, mobile_number, senders_key;
    MobileNumberSessionManager mobileNumberSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_otp);

        TextView mobile_text = findViewById(R.id.mobile_number_text);
        otp_pinview = findViewById(R.id.otp_value);
        ImageView next_activity = findViewById(R.id.nextActivity);
        ImageView back_activity = findViewById(R.id.backActivity);

        otp_pinview.setAnimationEnable(true);

        if (getIntent() != null){
            img_url = getIntent().getStringExtra("imageUrl");
            name = getIntent().getStringExtra("NAME");
            mobile_number = getIntent().getStringExtra("MOBILE_NUMBER");
            email = getIntent().getStringExtra("email");
            id = getIntent().getStringExtra("id");
            senders_key = getIntent().getStringExtra("SENDERS_KEY");
            if (getIntent().getStringExtra("CUST_ID") != null){
                custId = getIntent().getStringExtra("CUST_ID");
            }
            if (getIntent().getStringExtra("SENDER").equalsIgnoreCase("FROM MOBILE NUMBER") || getIntent().getStringExtra("SENDER").equalsIgnoreCase("FROM LOGIN")){
                back_activity.setVisibility(View.VISIBLE);
            }if(getIntent().getStringExtra("SENDER").equalsIgnoreCase("FOR CHANGE NUMBER")){
                back_activity.setVisibility(View.VISIBLE);
            }
        }

        String mobile_num_text = "(+91) " + mobile_number;
        mobile_text.setText(mobile_num_text);

        back_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getStringExtra("SENDER").equalsIgnoreCase("FROM MOBILE NUMBER")){
                    Intent intent = new Intent(getApplication(), PhoneNumberActivity.class);
                    intent.putExtra("SENDERS_KEY", senders_key);
                    intent.putExtra("CUST_ID", custId);
                    intent.putExtra("NAME", name);
                    intent.putExtra("email", email);
                    intent.putExtra("id", id);
                    intent.putExtra("imageUrl", img_url);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else if(getIntent().getStringExtra("SENDER").equalsIgnoreCase("FOR CHANGE NUMBER")){
                    onBackPressed();
                }else if (getIntent().getStringExtra("SENDER").equalsIgnoreCase("FROM LOGIN")){
                    onBackPressed();
                }

            }
        });

        mobileNumberSessionManager = new MobileNumberSessionManager(getApplicationContext());

        next_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = otp_pinview.getText().toString();
                //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                if (text.length() == 6){

                    if(getIntent().getStringExtra("SENDER").equalsIgnoreCase("FOR CHANGE NUMBER")){
                        changeMobileNumber(mobile_number);
                    }else {
                        mobileNumberSessionManager.createMobileNumberSession(name, mobile_number);
                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( ValidateOTPActivity.this,  new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                String newToken = instanceIdResult.getToken();
                                sendRegistrationToServer(newToken);

                            }
                        });

                    }
                }

            }
        });


    }
    private void sendRegistrationToServer(final String token) {
        String url =  "http://prescryp.com/prescriptionUpload/insertToken.php";
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

                            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            //int length = jsonArray.length();
                            //traversing through all the object
                            if (success.equals("1") || success.equals("3")){
                                Intent intent = new Intent(ValidateOTPActivity.this, MainActivity.class);
                                intent.putExtra("SENDERS_KEY", senders_key);
                                intent.putExtra("imageUrl", img_url);
                                intent.putExtra("name", name);
                                intent.putExtra("email", email);
                                intent.putExtra("id", id);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();


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
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("mobile_number", mobile_number);
                params.put("token", token);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);


    }

    public void changeMobileNumber(final String newMobileNumber) {
        mobileNumberSessionManager = new MobileNumberSessionManager(getApplicationContext());
        String url =  "http://annxions.com/prescriptionUpload/updateNewMobNum.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            if (success.equalsIgnoreCase("1")){
                                HashMap<String, String> details = mobileNumberSessionManager.geMobileDetails();
                                mobileNumberSessionManager.createMobileNumberSession(details.get(MobileNumberSessionManager.KEY_NAME), newMobileNumber);
                                Intent intent = new Intent(ValidateOTPActivity.this, AccountSettingsActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
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
                HashMap<String, String> details = mobileNumberSessionManager.geMobileDetails();
                HashMap<String, String> params = new HashMap<>();
                params.put("prev_mobile_number", details.get(MobileNumberSessionManager.KEY_MOB));
                params.put("new_mobile_number", newMobileNumber);
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }
}
