package com.prescywallet.presdigi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.facebook.login.LoginManager;
import com.prescywallet.presdigi.Session.MobileNumberSessionManager;
import com.prescywallet.presdigi.Session.UserSessionManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccountSettingsFragment extends Fragment {

    ConstraintLayout editProfile, notificationSettings, accountSettings, logOutAction, referAndEarn, legal, myOrder;
    MobileNumberSessionManager mobileNumberSessionManager;
    String imgUrl;
    CircleImageView profile_pic;
    TextView profile_name, phone_num;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            imgUrl = getArguments().getString("profileImageUrl");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.my_account_bar_main, container, false);
        mobileNumberSessionManager = new MobileNumberSessionManager(getContext());
        if (container != null) {
            container.removeAllViews();
        }

        loadAccountSetting(view);
        return view;
    }


    private void loadAccountSetting(View view) {
        profile_pic = view.findViewById(R.id.profile_pic_front);
        Picasso.get().load(imgUrl).into(profile_pic);

        final HashMap<String, String> detail = mobileNumberSessionManager.geMobileDetails();
        phone_num = view.findViewById(R.id.phone_number_text);
        String phone_num_full = "+91 " + detail.get(MobileNumberSessionManager.KEY_MOB);
        phone_num.setText(phone_num_full);
        profile_name = view.findViewById(R.id.profile_name_text);
        profile_name.setText(detail.get(MobileNumberSessionManager.KEY_NAME));


        editProfile = view.findViewById(R.id.editProfile);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                final HashMap<String, String> detail = mobileNumberSessionManager.geMobileDetails();
                Intent i = new Intent(getActivity(), EditProfileActivity.class);
                i.putExtra("MOBILE_NUMBER", detail.get(MobileNumberSessionManager.KEY_MOB));
                i.putExtra("NAME", detail.get(MobileNumberSessionManager.KEY_NAME));
                i.putExtra("PROFILE_PIC_URL", imgUrl);
                startActivity(i);
            }
        });

        notificationSettings = view.findViewById(R.id.notificationSettings);
        notificationSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NotificationActivity.class));
            }
        });

        accountSettings =  view.findViewById(R.id.accountSettings);
        accountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                if (getActivity().getIntent().getStringExtra("SENDERS_KEY") != null){
                    intent.putExtra("SENDERS_KEY", getActivity().getIntent().getStringExtra("SENDERS_KEY"));
                }
                startActivity(intent);
            }
        });

        logOutAction =  view.findViewById(R.id.logOutAction);
        logOutAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog();
            }
        });

        referAndEarn = view.findViewById(R.id.referAndEarnBtn);
        referAndEarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ReferAndEarnActivity.class));
            }
        });

        myOrder = view.findViewById(R.id.myOrder);
        myOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), YourOrderListActivity.class));
            }
        });
    }

    public void logoutDialog(){


        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Log Out");
        builder.setMessage("Do you want to log out?");

        builder.setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                LogOut(getActivity().getIntent());
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

    private void LogOut(Intent intent){
        if(intent != null) {
            final String sender = intent.getStringExtra("SENDERS_KEY");
            //IF ITS THE FRAGMENT THEN RECEIVE DATA
            if (sender != null) {
                if (sender.equalsIgnoreCase("FACEBOOK_LOGIN_RESULT")) {

                    LoginManager.getInstance().logOut();
                    mobileNumberSessionManager.logoutUser();
                    Intent logout = new Intent(getActivity(), SignInAcyivity.class);
                    logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(logout);
                    getActivity().finish();
                } else if (sender.equalsIgnoreCase("GOOGLE_LOGIN_RESULT")) {
                    mobileNumberSessionManager.logoutUser();
                    Intent logout = new Intent(getActivity(), SignInAcyivity.class);
                    logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    logout.putExtra("LogOut", "logout");
                    startActivity(logout);
                    getActivity().finish();
                }  else if (sender.equalsIgnoreCase("CUSTOM_LOGIN")){
                    mobileNumberSessionManager.logoutUser();
                    UserSessionManager sessionManager = new UserSessionManager(getContext());
                    sessionManager.logoutUser();
                }
            }
        }
    }
}
