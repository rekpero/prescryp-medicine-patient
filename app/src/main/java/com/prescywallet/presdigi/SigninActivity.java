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
import com.prescywallet.presdigi.Session.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SigninActivity extends AppCompatActivity {



    TextInputLayout mob_num, pass_word;
    ProgressBar progressBar;
    CardView signin;
    RelativeLayout create_acc;

    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_bar_main);
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

        mob_num = findViewById(R.id.mobnumSignin);
        pass_word = findViewById(R.id.passwordSignin);

        signin = findViewById(R.id.signinCardview);
        create_acc = findViewById(R.id.create_new_acc_button);
        progressBar = (ProgressBar) findViewById(R.id.loading);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(mob_num.getEditText().getText().toString().trim())) {
                    mob_num.setErrorEnabled(false);
                    mob_num.setError("Cant be blank");
                    progressBar.setVisibility(View.GONE);
                }else if (TextUtils.isEmpty(pass_word.getEditText().getText().toString().trim())){
                    pass_word.setErrorEnabled(false);
                    pass_word.setError("Cant be blank");
                    progressBar.setVisibility(View.GONE);
                }else {

                    signingIn(mob_num.getEditText().getText().toString().trim(), pass_word.getEditText().getText().toString().trim());
                }

            }
        });

        create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendAcc = new Intent(SigninActivity.this, CreateAccountActivity.class);
                startActivity(sendAcc);
                finish();
            }
        });


    }

    private void signingIn(final String mobnum, final String password) {

        String url = "http://prescryp.com/prescriptionUpload/login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");

                    if (success.equals("1")){
                        JSONObject object = jsonArray.getJSONObject(0);

                        String name = object.getString("name").trim();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        session = new UserSessionManager(getApplicationContext());
                        session.createUserLoginSession(name, mobnum, "", password);
                        Intent main = new Intent(SigninActivity.this, ValidateOTPActivity.class);
                        main.putExtra("SENDERS_KEY", "FROM LOGIN");
                        main.putExtra("NAME", name);
                        main.putExtra("MOBILE_NUMBER", mobnum);
                        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(main);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        signin.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        mob_num.getEditText().getText().clear();
                        pass_word.getEditText().getText().clear();
                        RelativeLayout layout = (RelativeLayout) findViewById(R.id.signin);
                        layout.clearFocus();
                        mob_num.requestFocus();
                    }
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
                params.put("password", password);
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SigninActivity.this, SignInAcyivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
