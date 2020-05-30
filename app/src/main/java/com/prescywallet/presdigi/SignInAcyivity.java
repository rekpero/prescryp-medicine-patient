package com.prescywallet.presdigi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.prescywallet.presdigi.Model.User;
import com.prescywallet.presdigi.Session.MobileNumberSessionManager;
import com.prescywallet.presdigi.Session.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SignInAcyivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    private static final int RC_SIGN_IN = 9001;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    ImageButton fb_card;
    ImageButton google_card;
    LoginButton loginButton;
    GoogleApiClient googleApiClient;
    GoogleSignInClient mGoogleSignInClient;
    String emailFB, imageUrlFB, profileId;
    ProgressDialog mDialog;
    UserSessionManager session;
    private TextView termCondition;


    private static final String EMAIL = "email";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login_id);

        termCondition = findViewById(R.id.terms_and_condition);
        termCondition.setText(Html.fromHtml("By signing up, you agree to our <u>Terms and Conditions</u>"));



        session = new UserSessionManager(getApplicationContext());
        if(session.isLoggedIn()){
            HashMap<String, String> user = session.getUserDetails();
            String session_name = user.get(UserSessionManager.KEY_NAME);
            String session_mob = user.get(UserSessionManager.KEY_MOB);
            Intent custom = new Intent(SignInAcyivity.this, MainActivity.class);
            custom.putExtra("SENDERS_KEY", "CUSTOM_LOGIN");
            custom.putExtra("name", session_name);
            custom.putExtra("mobilenumber", session_mob);
            custom.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(custom);
            finish();

        }

        fb_card = findViewById(R.id.fb_card);
        loginButton = findViewById(R.id.fb_login_button);
        fb_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == fb_card) {
                    loginButton.performClick();
                }
            }
        });

        //Facebook login id
        callbackManager = CallbackManager.Factory.create();




        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                nextActivity(currentProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();


        // Callback registration
        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mDialog = new ProgressDialog(SignInAcyivity.this);
                mDialog.setMessage("Retrieving data...");
                mDialog.show();

                // App code
                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);

                // Facebook Email address
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.v("LoginActivity Response ", response.toString());

                                try {
                                    String name = object.getString("name");
                                    String email = object.getString("email");
                                    String id = object.getString("id");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(SignInAcyivity.this,"Login Cancelled",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(SignInAcyivity.this,"Login Error",Toast.LENGTH_LONG).show();
            }
        };
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        loginButton.registerCallback(callbackManager, callback);


        google_card = findViewById(R.id.google_card);

        findViewById(R.id.google_card).setOnClickListener(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        Intent intent = this.getIntent();
        if(intent != null) {
            String logout = intent.getStringExtra("LogOut");
            if (logout != null && logout.equalsIgnoreCase("logout")){
                signOut();
            }
        }


        TextView create_acc = findViewById(R.id.new_acc_link);
        create_acc.setText(Html.fromHtml("New to PresDigi? <b>Create an account</b>"));
        create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInAcyivity.this, CreateAccountActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        });

        CardView signin = findViewById(R.id.signin_card);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInAcyivity.this, SigninActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        });






    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_card:
                signIn();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
       mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Spalsh Activity", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            sendProfile(acct);
        }
    }


    private void sendProfile(GoogleSignInAccount account){
        String id = account.getId();
        String name = account.getDisplayName();
        String email =  account.getEmail();
        String img_url = account.getPhotoUrl().toString();
        sendDirectlyToMain(id, name, email, img_url, "GOOGLE_LOGIN_RESULT");

    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            sendProfile(account);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void nextActivity(Profile profile){
        if(profile != null){

            String email = "";
            getProfileImgurl(profile.getProfilePictureUri(200,200).toString());

            sendDirectlyToMain("", profile.getName(), email, profile.getProfilePictureUri(200,200).toString(), "FACEBOOK_LOGIN_RESULT");

        }
    }

    MobileNumberSessionManager mobileNumberSessionManager;
    private User user;

    private void sendDirectlyToMain(final String id, final String name, final String email, final String img_url, final String intent_sender){
        mobileNumberSessionManager = new MobileNumberSessionManager(getApplicationContext());
        if (!mobileNumberSessionManager.isMobLoggedIn()) {
            sendDatabase(id, name, email, img_url, intent_sender);
        }else {
            Intent main = new Intent(SignInAcyivity.this, MainActivity.class);
            main.putExtra("SENDERS_KEY", intent_sender);
            main.putExtra("name", name);
            main.putExtra("email", email);
            main.putExtra("id", id);
            main.putExtra("imageUrl", img_url);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            finish();
        }
    }

    private void sendDatabase(final String id, final String name, final String email, final String img_url, final String intent_sender){
        user = new User(id, name, null, "", email, img_url,"", "");
        String url = "http://prescryp.com/prescriptionUpload/createAccount.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String custId = jsonObject.getString("result");
                    String mobile_number = jsonObject.getString("mobnum");

                    if (success.equalsIgnoreCase("1") || success.equalsIgnoreCase("2")) {
                        //Toast.makeText(getActivity(), mobile_number, Toast.LENGTH_SHORT).show();
                        if (mobile_number.equalsIgnoreCase("null")){
                            //Toast.makeText(getActivity(), "Custom Number Dialog", Toast.LENGTH_SHORT).show();
                            //customNumberDialog("PHONE_NUMBER", "Enter Your Mobile Number", "Get OTP", numPromptsView, name, custId);
                            Intent intent1 = new Intent(SignInAcyivity.this, PhoneNumberActivity.class);
                            intent1.putExtra("SENDERS_KEY", intent_sender);
                            intent1.putExtra("CUST_ID", custId);
                            intent1.putExtra("NAME", name);
                            intent1.putExtra("email", email);
                            intent1.putExtra("id", id);
                            intent1.putExtra("imageUrl", img_url);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent1);
                            finish();
                        }else{
                            //Toast.makeText(getActivity(), "OTP Dialog", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(SignInAcyivity.this, ValidateOTPActivity.class);
                            intent1.putExtra("SENDERS_KEY", intent_sender);
                            intent1.putExtra("SENDER", "FROM SIGNING");
                            intent1.putExtra("MOBILE_NUMBER", mobile_number);
                            intent1.putExtra("NAME", name);
                            intent1.putExtra("email", email);
                            intent1.putExtra("id", id);
                            intent1.putExtra("imageUrl", img_url);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent1);
                            finish();
                        }

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




    private void getProfileImgurl(String s) {
        this.imageUrlFB = s;
    }


}
