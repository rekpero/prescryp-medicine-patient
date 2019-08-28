package com.prescywallet.presdigi;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.prescywallet.presdigi.Session.MobileNumberSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SetPasswordActivity extends AppCompatActivity {
    TextInputLayout newPassword, repeatPassword;
    CardView setPasswordCard;
    MobileNumberSessionManager mobileNumberSessionManager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_password_bar_main);
        toolbar = (Toolbar) findViewById(R.id.passwordToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        newPassword = (TextInputLayout) findViewById(R.id.new_password);
        repeatPassword = (TextInputLayout) findViewById(R.id.repeat_password);
        setPasswordCard = (CardView) findViewById(R.id.setPasswordCard);

        setPasswordCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newPassword.getEditText().getText().toString().equalsIgnoreCase(repeatPassword.getEditText().getText().toString())){
                    setPassword(newPassword.getEditText().getText().toString());
                }

            }
        });


    }

    private void setPassword(final String password) {
        mobileNumberSessionManager = new MobileNumberSessionManager(getApplicationContext());
        String url =  "http://prescryp.com/prescriptionUpload/updatePassword.php";
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
                                onBackPressed();
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
                params.put("mobile_number", details.get(MobileNumberSessionManager.KEY_MOB));
                params.put("password", password);
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }
}
