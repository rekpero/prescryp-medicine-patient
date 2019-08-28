package com.prescywallet.presdigi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PhoneNumberActivity extends AppCompatActivity {
    String name, custId, email, id, img_url, senders_key;


    private TextInputLayout mobileTextInput;
    private CardView continueCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        mobileTextInput = findViewById(R.id.mobile_number_input);
        continueCard = findViewById(R.id.continueCardView);

        Intent intent = getIntent();

        if(intent != null) {
                name = intent.getStringExtra("NAME");
                custId = intent.getStringExtra("CUST_ID");
                email = intent.getStringExtra("email");
                id = intent.getStringExtra("id");
                img_url = intent.getStringExtra("imageUrl");
                senders_key = intent.getStringExtra("SENDERS_KEY");

        }

        continueCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMobileNumber(custId, mobileTextInput.getEditText().getText().toString());
                Intent intent1 = new Intent(PhoneNumberActivity.this, ValidateOTPActivity.class);
                intent1.putExtra("SENDERS_KEY", senders_key);
                intent1.putExtra("SENDER", "FROM MOBILE NUMBER");
                intent1.putExtra("MOBILE_NUMBER", mobileTextInput.getEditText().getText().toString());
                intent1.putExtra("NAME", name);
                intent1.putExtra("CUST_ID", custId);
                intent1.putExtra("email", email);
                intent1.putExtra("id", id);
                intent1.putExtra("imageUrl", img_url);
                startActivity(intent1);
                finish();

            }
        });
    }

    private void updateMobileNumber(final String custId, final String mobnum) {
        String url = "http://prescryp.com/prescriptionUpload/updateMobNum.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("mobilenumber", mobnum);
                params.put("custid", custId);
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }


}
