package com.prescywallet.presdigi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ReferAndEarnActivity extends AppCompatActivity {
    ImageView whatsAppBtn, messengerBtn, smsBtn, gmailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn);
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


        whatsAppBtn = findViewById(R.id.whatsappBtn);
        messengerBtn = findViewById(R.id.messengerBtn);
        smsBtn = findViewById(R.id.smsBtn);
        gmailBtn = findViewById(R.id.gmailBtn);

        whatsAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String text = " message you want to share..";
                // change with required  application package

                intent.setPackage("com.whatsapp");
                if (intent != null) {
                    intent.putExtra(Intent.EXTRA_TEXT, text);//
                    startActivity(Intent.createChooser(intent, text));
                } else {

                    Toast.makeText(getApplication(), "App not found", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        messengerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String text = " message you want to share..";
                // change with required  application package

                intent.setPackage("com.facebook.orca");
                if (intent != null) {
                    intent.putExtra(Intent.EXTRA_TEXT, text);//
                    startActivity(Intent.createChooser(intent, text));
                } else {

                    Toast.makeText(getApplication(), "App not found", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                String x = "Please download Prescyber";
                sendIntent.putExtra("sms_body", x);
                startActivity(sendIntent);
            }
        });

        gmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Prescyber App Download invitation");

                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Please install Prescyber app...");

                startActivity(emailIntent);
            }
        });

    }


}
