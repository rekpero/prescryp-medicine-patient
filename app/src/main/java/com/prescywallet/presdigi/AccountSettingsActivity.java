package com.prescywallet.presdigi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.prescywallet.presdigi.Session.MobileNumberSessionManager;
import com.prescywallet.presdigi.Session.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountSettingsActivity extends AppCompatActivity {
    TextView change_mobile_number, set_password, deactivate_account;
    private Toolbar toolbar;
    MobileNumberSessionManager mobileNumberSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting_bar_main);

        toolbar = (Toolbar) findViewById(R.id.accountSettingsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        change_mobile_number =  findViewById(R.id.change_mobile_no);
        set_password =  findViewById(R.id.set_password);
        deactivate_account =  findViewById(R.id.deactivate_account);

        change_mobile_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSettingsActivity.this, ChangeMobileNumberActivity.class));
            }
        });

        set_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSettingsActivity.this, SetPasswordActivity.class));
            }
        });

        deactivate_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog();
            }
        });
    }

    public void logoutDialog(){


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Deactivate Account");
        builder.setMessage("Do you want to deactivate your account?");

        builder.setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                changeMobileNumber();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
        builder.show();
    }


    public void changeMobileNumber() {
        mobileNumberSessionManager = new MobileNumberSessionManager(getApplicationContext());
        String url =  "http://prescryp.com/prescriptionUpload/deactivateAccount.php";
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
                                logOutFunc(getIntent());
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
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

    private void logOutFunc(Intent intent){
        if(intent != null) {
            final String sender = intent.getStringExtra("SENDERS_KEY");
            //IF ITS THE FRAGMENT THEN RECEIVE DATA
            if (sender != null) {
                if (sender.equalsIgnoreCase("FACEBOOK_LOGIN_RESULT")) {
                    LoginManager.getInstance().logOut();
                    mobileNumberSessionManager.logoutUser();
                    Intent logout = new Intent(AccountSettingsActivity.this, SignInAcyivity.class);
                    logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(logout);
                    finish();
                } else if (sender.equalsIgnoreCase("GOOGLE_LOGIN_RESULT")) {
                    mobileNumberSessionManager.logoutUser();
                    Intent logout = new Intent(AccountSettingsActivity.this, SignInAcyivity.class);
                    logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    logout.putExtra("LogOut", "logout");
                    startActivity(logout);
                    finish();
                }  else if (sender.equalsIgnoreCase("CUSTOM_LOGIN")){
                    mobileNumberSessionManager.logoutUser();
                    UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
                    sessionManager.logoutUser();
                }
            }
        }
    }
}
