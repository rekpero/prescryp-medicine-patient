package com.prescywallet.presdigi;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PrescriptionImageviewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    ImageView presImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescription_imageview_bar_main);

        toolbar = findViewById(R.id.prescription_image_view_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        presImageView = findViewById(R.id.prescriptionView);

        if(getIntent().getStringExtra("PrescriptionId") != null && getIntent().getStringExtra("Date") != null
                && getIntent().getStringExtra("ImagePathUrl") != null){
            getSupportActionBar().setTitle(getIntent().getStringExtra("PrescriptionId"));
            String date = getIntent().getStringExtra("Date");
            String[] date_split = date.split(" ");
            String date_changed = getDateFormatChangeSubtitle(date_split[2].trim());
            String date_shown = date_split[0].trim() + " : " + date_changed.trim();
            toolbar.setSubtitle(date_shown);
            Picasso.get()
                    .load(getIntent().getStringExtra("ImagePathUrl"))
                    .fit()
                    .into(presImageView);

        }


    }

    private String getDateFormatChangeSubtitle(String date){
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        String outputDateStr = "";
        try {
            Date new_date = inputFormat.parse(date);
            outputDateStr = outputFormat.format(new_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateStr;
    }


}
