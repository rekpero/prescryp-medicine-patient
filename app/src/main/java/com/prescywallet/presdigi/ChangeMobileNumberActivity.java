package com.prescywallet.presdigi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputLayout;
import com.prescywallet.presdigi.Session.MobileNumberSessionManager;

public class ChangeMobileNumberActivity extends AppCompatActivity {

    TextInputLayout new_mobile_number;
    CardView change_mobile_card;
    MobileNumberSessionManager mobileNumberSessionManager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_mobile_number_bar_main);
        toolbar = findViewById(R.id.changeMobNumToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        new_mobile_number = findViewById(R.id.input_new_mobile_number);
        change_mobile_card = findViewById(R.id.change_mob_card);
        change_mobile_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ChangeMobileNumberActivity.this, ValidateOTPActivity.class);
                intent.putExtra("SENDER", "FOR CHANGE NUMBER");
                intent.putExtra("MOBILE_NUMBER", new_mobile_number.getEditText().getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }



}
