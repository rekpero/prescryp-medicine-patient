package com.prescywallet.presdigi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.prescywallet.presdigi.Model.User;
import com.prescywallet.presdigi.Session.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {



    TextInputLayout name, mob_num, email, password;
    ProgressBar progressBar;
    CardView continue_btn;
    RelativeLayout signin_now;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_bar_main);
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



        session = new UserSessionManager(getApplicationContext());


        name = (TextInputLayout) findViewById(R.id.full_name);
        mob_num = (TextInputLayout) findViewById(R.id.mobile_number);
        email = (TextInputLayout) findViewById(R.id.email);
        password = (TextInputLayout) findViewById(R.id.password);

        continue_btn = (CardView) findViewById(R.id.continue_btn);

        signin_now = findViewById(R.id.signin_now);
        signin_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent si = new Intent(CreateAccountActivity.this, SigninActivity.class);
                startActivity(si);
                finish();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.loading);
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continue_btn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                if (!email.getEditText().getText().toString().trim().contains("@")){
                    email.setErrorEnabled(false);
                    email.setError("Invalid E-mail");
                    progressBar.setVisibility(View.GONE);
                }else if (TextUtils.isEmpty(name.getEditText().getText().toString().trim())){
                    name.setErrorEnabled(false);
                    name.setError("Cant be blank");
                    progressBar.setVisibility(View.GONE);
                }else if (TextUtils.isEmpty(mob_num.getEditText().getText().toString().trim())){
                    mob_num.setErrorEnabled(false);
                    mob_num.setError("Cant be blank");
                    progressBar.setVisibility(View.GONE);
                }else if (TextUtils.isEmpty(password.getEditText().getText().toString().trim())){
                    password.setErrorEnabled(false);
                    password.setError("Cant be blank");
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    final User user = new User(null, name.getEditText().getText().toString().trim(), null,
                            mob_num.getEditText().getText().toString().trim(),
                            email.getEditText().getText().toString().trim(), "", "",
                            password.getEditText().getText().toString().trim());

                    final String url = "http://prescryp.com/prescriptionUpload/createAccount.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                String message = jsonObject.getString("message");
                                String custId = jsonObject.getString("result");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                if (success.equalsIgnoreCase("1")){
                                    session = new UserSessionManager(getApplicationContext());
                                    session.createUserLoginSession(user.getName(), user.getMobile_num(), user.getEmail(), user.getPassword());
                                    Intent main = new Intent(CreateAccountActivity.this, MainActivity.class);
                                    main.putExtra("SENDERS_KEY", "CUSTOM_LOGIN");
                                    main.putExtra("name", user.getName());
                                    main.putExtra("customer_id", custId);
                                    main.putExtra("mobilenumber", user.getMobile_num());
                                    main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(main);
                                    finish();
                                }else if (success.equalsIgnoreCase("2")){
                                    name.getEditText().getText().clear();
                                    mob_num.getEditText().getText().clear();
                                    email.getEditText().getText().clear();
                                    password.getEditText().getText().clear();
                                    RelativeLayout layout = (RelativeLayout) findViewById(R.id.create_new_acc);
                                    layout.clearFocus();
                                    name.requestFocus();

                                    continue_btn.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("mobilenumber", user.getMobile_num());
                            params.put("name", user.getName());
                            params.put("email", user.getEmail());
                            params.put("imageurl", user.getImageUrl());
                            params.put("password", user.getPassword());
                            return params;
                        }
                    };

                    Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CreateAccountActivity.this, SignInAcyivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
