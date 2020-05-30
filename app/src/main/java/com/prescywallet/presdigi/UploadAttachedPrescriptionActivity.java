package com.prescywallet.presdigi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prescywallet.presdigi.Model.PrescriptionImagePathItem;
import com.prescywallet.presdigi.Session.MobileNumberSessionManager;
import com.prescywallet.presdigi.database.AttachedPrescriptionDBHelper;
import com.squareup.picasso.Picasso;

import java.io.File;

public class UploadAttachedPrescriptionActivity extends AppCompatActivity {


    ImageView imgShow;
    ProgressBar progressBar;
    String pathImage;
    MobileNumberSessionManager mobileNumberSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_attached_prescription);
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


        uploadImageView();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                new AttachedPrescriptionDBHelper(getApplicationContext()).addPrescriptionImagePath(new PrescriptionImagePathItem(pathImage));
                Intent intent = new Intent(getApplicationContext(), AttachPrescriptionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void uploadImageView() {

        imgShow = findViewById(R.id.imgShow);
        progressBar = findViewById(R.id.loading);

        progressBar.setVisibility(View.GONE);

        if (getIntent() != null) {
            pathImage = getIntent().getStringExtra("pathImage");
        }

        init();

    }




    private void init() {




        imgShow.setVisibility(View.VISIBLE);
        File image = new File(pathImage);

        Picasso.get()
                .load(image)
                .fit()
                .into(imgShow);






    }


}
