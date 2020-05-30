package com.prescywallet.presdigi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SetReminderTimeActivity extends AppCompatActivity {

    private TextView time_set, medicine_name, repeat, repeat_times, dosage;
    private TextView time_0600am, time_0630am, time_0700am, time_0730am, time_0800am, time_0830am, time_0900am, time_0930am;
    private TextView time_1000am, time_1030am, time_1100am, time_1130am, time_1200pm, time_1230pm, time_0100pm, time_0130pm;
    private TextView time_0200pm, time_0230pm, time_0300pm, time_0330pm, time_0400pm, time_0430pm, time_0500pm, time_0530pm;
    private TextView time_0600pm, time_0630pm, time_0700pm, time_0730pm, time_0800pm, time_0830pm, time_0900pm, time_0930pm;
    private TextView time_1000pm, time_1030pm, time_1100pm, time_1130pm;
    private ConstraintLayout breakfastView, lunchView, dinnerView;
    private ImageView breakfastImg, lunchImg, dinnerImg;
    private CardView ok_card1, ok_card2, ok_card3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_reminder_time_bar_main);
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

        init();

        if (getIntent() != null){
            if (getIntent().getStringExtra("ROUTINE_TYPE") != null){
                String type = getIntent().getStringExtra("ROUTINE_TYPE");
                if (type.equalsIgnoreCase("BREAKFAST")){
                    lunchView.setVisibility(View.GONE);
                    dinnerView.setVisibility(View.GONE);
                    lunchImg.setVisibility(View.GONE);
                    dinnerImg.setVisibility(View.GONE);
                    breakfastView.setVisibility(View.VISIBLE);
                    breakfastImg.setVisibility(View.VISIBLE);
                }else if (type.equalsIgnoreCase("LUNCH")){
                    breakfastView.setVisibility(View.GONE);
                    breakfastImg.setVisibility(View.GONE);
                    dinnerView.setVisibility(View.GONE);
                    dinnerImg.setVisibility(View.GONE);
                    lunchView.setVisibility(View.VISIBLE);
                    lunchImg.setVisibility(View.VISIBLE);
                }else if (type.equalsIgnoreCase("DINNER")){
                    breakfastView.setVisibility(View.GONE);
                    breakfastImg.setVisibility(View.GONE);
                    lunchView.setVisibility(View.GONE);
                    lunchImg.setVisibility(View.GONE);
                    dinnerView.setVisibility(View.VISIBLE);
                    dinnerImg.setVisibility(View.VISIBLE);
                }
            }
            if (getIntent().getStringExtra("ROUTINE_TIME") != null){
                setTimeBackground(getIntent().getStringExtra("ROUTINE_TIME"));
                time_set.setText(getIntent().getStringExtra("ROUTINE_TIME").toUpperCase());
            }
            if (getIntent().getStringExtra("MEDICINE_NAME") != null){
                medicine_name.setText(getIntent().getStringExtra("MEDICINE_NAME").toUpperCase());
            }
            if (getIntent().getStringExtra("REPEAT_TYPE") != null){
                repeat.setText(getIntent().getStringExtra("REPEAT_TYPE").toUpperCase());
            }
            if (getIntent().getStringExtra("REPEAT_TIMES") != null){
                repeat_times.setText(getIntent().getStringExtra("REPEAT_TIMES").toUpperCase());
            }
            if (getIntent().getStringExtra("DOSAGE") != null){
                dosage.setText(getIntent().getStringExtra("DOSAGE").toUpperCase());
            }



        }
        onClickTime();

        ok_card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SetReminderActivity.class);
                intent.putExtra("ROUTINE_TYPE", getIntent().getStringExtra("ROUTINE_TYPE"));
                intent.putExtra("MedicineName", medicine_name.getText().toString());
                intent.putExtra("SELECTED_ROUTINE_TIME", time_set.getText().toString());
                intent.putExtra("REPEAT_TYPE", getIntent().getStringExtra("REPEAT_TYPE"));
                intent.putExtra("REPEAT_TIMES", getIntent().getStringExtra("REPEAT_TIMES"));
                intent.putExtra("DOSAGE", getIntent().getStringExtra("DOSAGE"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        ok_card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SetReminderActivity.class);
                intent.putExtra("ROUTINE_TYPE", getIntent().getStringExtra("ROUTINE_TYPE"));
                intent.putExtra("MedicineName", medicine_name.getText().toString());
                intent.putExtra("SELECTED_ROUTINE_TIME", time_set.getText().toString());
                intent.putExtra("REPEAT_TYPE", getIntent().getStringExtra("REPEAT_TYPE"));
                intent.putExtra("REPEAT_TIMES", getIntent().getStringExtra("REPEAT_TIMES"));
                intent.putExtra("DOSAGE", getIntent().getStringExtra("DOSAGE"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        ok_card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SetReminderActivity.class);
                intent.putExtra("ROUTINE_TYPE", getIntent().getStringExtra("ROUTINE_TYPE"));
                intent.putExtra("MedicineName", medicine_name.getText().toString());
                intent.putExtra("SELECTED_ROUTINE_TIME", time_set.getText().toString());
                intent.putExtra("REPEAT_TYPE", getIntent().getStringExtra("REPEAT_TYPE"));
                intent.putExtra("REPEAT_TIMES", getIntent().getStringExtra("REPEAT_TIMES"));
                intent.putExtra("DOSAGE", getIntent().getStringExtra("DOSAGE"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });



    }

    private void init(){
        time_set = findViewById(R.id.time_set);
        medicine_name = findViewById(R.id.medicine_name);
        repeat = findViewById(R.id.repeat);
        repeat_times = findViewById(R.id.repeat_times);
        dosage = findViewById(R.id.dosage);
        time_0600am = findViewById(R.id.time_0600am);
        time_0630am = findViewById(R.id.time_0630am);
        time_0700am = findViewById(R.id.time_0700am);
        time_0730am = findViewById(R.id.time_0730am);
        time_0800am = findViewById(R.id.time_0800am);
        time_0830am = findViewById(R.id.time_0830am);
        time_0900am = findViewById(R.id.time_0900am);
        time_0930am = findViewById(R.id.time_0930am);
        time_1000am = findViewById(R.id.time_1000am);
        time_1030am = findViewById(R.id.time_1030am);
        time_1100am = findViewById(R.id.time_1100am);
        time_1130am = findViewById(R.id.time_1130am);
        time_1200pm = findViewById(R.id.time_1200pm);
        time_1230pm = findViewById(R.id.time_1230pm);
        time_0100pm = findViewById(R.id.time_0100pm);
        time_0130pm = findViewById(R.id.time_0130pm);
        time_0200pm = findViewById(R.id.time_0200pm);
        time_0230pm = findViewById(R.id.time_0230pm);
        time_0300pm = findViewById(R.id.time_0300pm);
        time_0330pm = findViewById(R.id.time_0330pm);
        time_0400pm = findViewById(R.id.time_0400pm);
        time_0430pm = findViewById(R.id.time_0430pm);
        time_0500pm = findViewById(R.id.time_0500pm);
        time_0530pm = findViewById(R.id.time_0530pm);
        time_0600pm = findViewById(R.id.time_0600pm);
        time_0630pm = findViewById(R.id.time_0630pm);
        time_0700pm = findViewById(R.id.time_0700pm);
        time_0730pm = findViewById(R.id.time_0730pm);
        time_0800pm = findViewById(R.id.time_0800pm);
        time_0830pm = findViewById(R.id.time_0830pm);
        time_0900pm = findViewById(R.id.time_0900pm);
        time_0930pm = findViewById(R.id.time_0930pm);
        time_1000pm = findViewById(R.id.time_1000pm);
        time_1030pm = findViewById(R.id.time_1030pm);
        time_1100pm = findViewById(R.id.time_1100pm);
        time_1130pm = findViewById(R.id.time_1130pm);

        breakfastView = findViewById(R.id.breakfast_view);
        lunchView = findViewById(R.id.lunch_view);
        dinnerView = findViewById(R.id.dinner_view);

        breakfastImg = findViewById(R.id.breakfast_img);
        lunchImg = findViewById(R.id.lunch_img);
        dinnerImg = findViewById(R.id.dinner_img);

        ok_card1 = findViewById(R.id.ok_card1);
        ok_card2 = findViewById(R.id.ok_card2);
        ok_card3 = findViewById(R.id.ok_card3);
    }

    private void setTimeBackground(String time){
        if (time.toLowerCase().equals("06:00am")){
            time_0600am.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("06:30am")){
            time_0630am.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("07:00am")){
            time_0700am.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("07:30am")){
            time_0730am.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("08:00am")){
            time_0800am.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("08:30am")){
            time_0830am.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("09:00am")){
            time_0900am.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("09:30am")){
            time_0930am.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("10:00am")){
            time_1000am.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("10:30am")){
            time_1030am.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("11:00am")){
            time_1100am.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("11:30am")){
            time_1130am.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("12:00pm")){
            time_1200pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("12:30pm")){
            time_1230pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("01:00pm")){
            time_0100pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("01:30pm")){
            time_0130pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("02:00pm")){
            time_0200pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("02:30pm")){
            time_0230pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("03:00pm")){
            time_0300pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("03:30pm")){
            time_0330pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("04:00pm")){
            time_0400pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("04:30pm")){
            time_0430pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("05:00pm")){
            time_0500pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("05:30pm")){
            time_0530pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("06:00pm")){
            time_0600pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("06:30pm")){
            time_0630pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("07:00pm")){
            time_0700pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("07:30pm")){
            time_0730pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("08:00pm")){
            time_0800pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("08:30pm")){
            time_0830pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("09:00pm")){
            time_0900pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("09:30pm")){
            time_0930pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("10:00pm")){
            time_1000pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("10:30pm")){
            time_1030pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("11:00pm")){
            time_1100pm.setBackgroundResource(R.drawable.time_set_color);
        }else if (time.toLowerCase().equals("11:30pm")){
            time_1130pm.setBackgroundResource(R.drawable.time_set_color);
        }
    }

    private void onClickTime(){
        time_0600am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._06_00am);
                time_0600am.setBackgroundResource(R.drawable.time_set_color);
                time_0630am.setBackgroundResource(0);
                time_0700am.setBackgroundResource(0);
                time_0730am.setBackgroundResource(0);
                time_0800am.setBackgroundResource(0);
                time_0830am.setBackgroundResource(0);
                time_0900am.setBackgroundResource(0);
                time_0930am.setBackgroundResource(0);
                time_1000am.setBackgroundResource(0);
                time_1030am.setBackgroundResource(0);
                time_1100am.setBackgroundResource(0);
                time_1130am.setBackgroundResource(0);

            }
        });
        time_0630am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._06_30am);
                time_0630am.setBackgroundResource(R.drawable.time_set_color);
                time_0600am.setBackgroundResource(0);
                time_0700am.setBackgroundResource(0);
                time_0730am.setBackgroundResource(0);
                time_0800am.setBackgroundResource(0);
                time_0830am.setBackgroundResource(0);
                time_0900am.setBackgroundResource(0);
                time_0930am.setBackgroundResource(0);
                time_1000am.setBackgroundResource(0);
                time_1030am.setBackgroundResource(0);
                time_1100am.setBackgroundResource(0);
                time_1130am.setBackgroundResource(0);
            }
        });
        time_0700am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._07_00am);
                time_0700am.setBackgroundResource(R.drawable.time_set_color);
                time_0630am.setBackgroundResource(0);
                time_0600am.setBackgroundResource(0);
                time_0730am.setBackgroundResource(0);
                time_0800am.setBackgroundResource(0);
                time_0830am.setBackgroundResource(0);
                time_0900am.setBackgroundResource(0);
                time_0930am.setBackgroundResource(0);
                time_1000am.setBackgroundResource(0);
                time_1030am.setBackgroundResource(0);
                time_1100am.setBackgroundResource(0);
                time_1130am.setBackgroundResource(0);
            }
        });
        time_0730am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._07_30am);
                time_0730am.setBackgroundResource(R.drawable.time_set_color);
                time_0630am.setBackgroundResource(0);
                time_0700am.setBackgroundResource(0);
                time_0600am.setBackgroundResource(0);
                time_0800am.setBackgroundResource(0);
                time_0830am.setBackgroundResource(0);
                time_0900am.setBackgroundResource(0);
                time_0930am.setBackgroundResource(0);
                time_1000am.setBackgroundResource(0);
                time_1030am.setBackgroundResource(0);
                time_1100am.setBackgroundResource(0);
                time_1130am.setBackgroundResource(0);
            }
        });
        time_0800am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._08_00am);
                time_0800am.setBackgroundResource(R.drawable.time_set_color);
                time_0630am.setBackgroundResource(0);
                time_0700am.setBackgroundResource(0);
                time_0730am.setBackgroundResource(0);
                time_0600am.setBackgroundResource(0);
                time_0830am.setBackgroundResource(0);
                time_0900am.setBackgroundResource(0);
                time_0930am.setBackgroundResource(0);
                time_1000am.setBackgroundResource(0);
                time_1030am.setBackgroundResource(0);
                time_1100am.setBackgroundResource(0);
                time_1130am.setBackgroundResource(0);
            }
        });
        time_0830am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._08_30am);
                time_0830am.setBackgroundResource(R.drawable.time_set_color);
                time_0630am.setBackgroundResource(0);
                time_0700am.setBackgroundResource(0);
                time_0730am.setBackgroundResource(0);
                time_0800am.setBackgroundResource(0);
                time_0600am.setBackgroundResource(0);
                time_0900am.setBackgroundResource(0);
                time_0930am.setBackgroundResource(0);
                time_1000am.setBackgroundResource(0);
                time_1030am.setBackgroundResource(0);
                time_1100am.setBackgroundResource(0);
                time_1130am.setBackgroundResource(0);
            }
        });
        time_0900am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._09_00am);
                time_0900am.setBackgroundResource(R.drawable.time_set_color);
                time_0630am.setBackgroundResource(0);
                time_0700am.setBackgroundResource(0);
                time_0730am.setBackgroundResource(0);
                time_0800am.setBackgroundResource(0);
                time_0830am.setBackgroundResource(0);
                time_0600am.setBackgroundResource(0);
                time_0930am.setBackgroundResource(0);
                time_1000am.setBackgroundResource(0);
                time_1030am.setBackgroundResource(0);
                time_1100am.setBackgroundResource(0);
                time_1130am.setBackgroundResource(0);
            }
        });
        time_0930am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._09_30am);
                time_0930am.setBackgroundResource(R.drawable.time_set_color);
                time_0630am.setBackgroundResource(0);
                time_0700am.setBackgroundResource(0);
                time_0730am.setBackgroundResource(0);
                time_0800am.setBackgroundResource(0);
                time_0830am.setBackgroundResource(0);
                time_0900am.setBackgroundResource(0);
                time_0600am.setBackgroundResource(0);
                time_1000am.setBackgroundResource(0);
                time_1030am.setBackgroundResource(0);
                time_1100am.setBackgroundResource(0);
                time_1130am.setBackgroundResource(0);
            }
        });
        time_1000am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._10_00am);
                time_1000am.setBackgroundResource(R.drawable.time_set_color);
                time_0630am.setBackgroundResource(0);
                time_0700am.setBackgroundResource(0);
                time_0730am.setBackgroundResource(0);
                time_0800am.setBackgroundResource(0);
                time_0830am.setBackgroundResource(0);
                time_0900am.setBackgroundResource(0);
                time_0930am.setBackgroundResource(0);
                time_0600am.setBackgroundResource(0);
                time_1030am.setBackgroundResource(0);
                time_1100am.setBackgroundResource(0);
                time_1130am.setBackgroundResource(0);
            }
        });
        time_1030am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._10_30am);
                time_1030am.setBackgroundResource(R.drawable.time_set_color);
                time_0630am.setBackgroundResource(0);
                time_0700am.setBackgroundResource(0);
                time_0730am.setBackgroundResource(0);
                time_0800am.setBackgroundResource(0);
                time_0830am.setBackgroundResource(0);
                time_0900am.setBackgroundResource(0);
                time_0930am.setBackgroundResource(0);
                time_1000am.setBackgroundResource(0);
                time_0600am.setBackgroundResource(0);
                time_1100am.setBackgroundResource(0);
                time_1130am.setBackgroundResource(0);
            }
        });
        time_1100am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._11_00am);
                time_1100am.setBackgroundResource(R.drawable.time_set_color);
                time_0630am.setBackgroundResource(0);
                time_0700am.setBackgroundResource(0);
                time_0730am.setBackgroundResource(0);
                time_0800am.setBackgroundResource(0);
                time_0830am.setBackgroundResource(0);
                time_0900am.setBackgroundResource(0);
                time_0930am.setBackgroundResource(0);
                time_1000am.setBackgroundResource(0);
                time_1030am.setBackgroundResource(0);
                time_0600am.setBackgroundResource(0);
                time_1130am.setBackgroundResource(0);
            }
        });
        time_1130am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._11_30am);
                time_1130am.setBackgroundResource(R.drawable.time_set_color);
                time_0630am.setBackgroundResource(0);
                time_0700am.setBackgroundResource(0);
                time_0730am.setBackgroundResource(0);
                time_0800am.setBackgroundResource(0);
                time_0830am.setBackgroundResource(0);
                time_0900am.setBackgroundResource(0);
                time_0930am.setBackgroundResource(0);
                time_1000am.setBackgroundResource(0);
                time_1030am.setBackgroundResource(0);
                time_1100am.setBackgroundResource(0);
                time_0600am.setBackgroundResource(0);
            }
        });

        time_1200pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._12_00pm);
                time_1200pm.setBackgroundResource(R.drawable.time_set_color);
                time_1230pm.setBackgroundResource(0);
                time_0100pm.setBackgroundResource(0);
                time_0130pm.setBackgroundResource(0);
                time_0200pm.setBackgroundResource(0);
                time_0230pm.setBackgroundResource(0);
                time_0300pm.setBackgroundResource(0);
                time_0330pm.setBackgroundResource(0);
                time_0400pm.setBackgroundResource(0);
                time_0430pm.setBackgroundResource(0);
                time_0500pm.setBackgroundResource(0);
                time_0530pm.setBackgroundResource(0);
            }
        });

        time_1230pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._12_30pm);
                time_1230pm.setBackgroundResource(R.drawable.time_set_color);
                time_1200pm.setBackgroundResource(0);
                time_0100pm.setBackgroundResource(0);
                time_0130pm.setBackgroundResource(0);
                time_0200pm.setBackgroundResource(0);
                time_0230pm.setBackgroundResource(0);
                time_0300pm.setBackgroundResource(0);
                time_0330pm.setBackgroundResource(0);
                time_0400pm.setBackgroundResource(0);
                time_0430pm.setBackgroundResource(0);
                time_0500pm.setBackgroundResource(0);
                time_0530pm.setBackgroundResource(0);
            }
        });

        time_0100pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._01_00pm);
                time_0100pm.setBackgroundResource(R.drawable.time_set_color);
                time_1230pm.setBackgroundResource(0);
                time_1200pm.setBackgroundResource(0);
                time_0130pm.setBackgroundResource(0);
                time_0200pm.setBackgroundResource(0);
                time_0230pm.setBackgroundResource(0);
                time_0300pm.setBackgroundResource(0);
                time_0330pm.setBackgroundResource(0);
                time_0400pm.setBackgroundResource(0);
                time_0430pm.setBackgroundResource(0);
                time_0500pm.setBackgroundResource(0);
                time_0530pm.setBackgroundResource(0);
            }
        });

        time_0130pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._01_30pm);
                time_0130pm.setBackgroundResource(R.drawable.time_set_color);
                time_1230pm.setBackgroundResource(0);
                time_0100pm.setBackgroundResource(0);
                time_1200pm.setBackgroundResource(0);
                time_0200pm.setBackgroundResource(0);
                time_0230pm.setBackgroundResource(0);
                time_0300pm.setBackgroundResource(0);
                time_0330pm.setBackgroundResource(0);
                time_0400pm.setBackgroundResource(0);
                time_0430pm.setBackgroundResource(0);
                time_0500pm.setBackgroundResource(0);
                time_0530pm.setBackgroundResource(0);
            }
        });

        time_0200pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._02_00pm);
                time_0200pm.setBackgroundResource(R.drawable.time_set_color);
                time_1230pm.setBackgroundResource(0);
                time_0100pm.setBackgroundResource(0);
                time_1200pm.setBackgroundResource(0);
                time_0130pm.setBackgroundResource(0);
                time_0230pm.setBackgroundResource(0);
                time_0300pm.setBackgroundResource(0);
                time_0330pm.setBackgroundResource(0);
                time_0400pm.setBackgroundResource(0);
                time_0430pm.setBackgroundResource(0);
                time_0500pm.setBackgroundResource(0);
                time_0530pm.setBackgroundResource(0);
            }
        });

        time_0230pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._02_30pm);
                time_0230pm.setBackgroundResource(R.drawable.time_set_color);
                time_1230pm.setBackgroundResource(0);
                time_0100pm.setBackgroundResource(0);
                time_1200pm.setBackgroundResource(0);
                time_0130pm.setBackgroundResource(0);
                time_0200pm.setBackgroundResource(0);
                time_0300pm.setBackgroundResource(0);
                time_0330pm.setBackgroundResource(0);
                time_0400pm.setBackgroundResource(0);
                time_0430pm.setBackgroundResource(0);
                time_0500pm.setBackgroundResource(0);
                time_0530pm.setBackgroundResource(0);
            }
        });

        time_0300pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._03_00pm);
                time_0300pm.setBackgroundResource(R.drawable.time_set_color);
                time_1230pm.setBackgroundResource(0);
                time_0100pm.setBackgroundResource(0);
                time_1200pm.setBackgroundResource(0);
                time_0130pm.setBackgroundResource(0);
                time_0230pm.setBackgroundResource(0);
                time_0200pm.setBackgroundResource(0);
                time_0330pm.setBackgroundResource(0);
                time_0400pm.setBackgroundResource(0);
                time_0430pm.setBackgroundResource(0);
                time_0500pm.setBackgroundResource(0);
                time_0530pm.setBackgroundResource(0);
            }
        });

        time_0330pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._03_30pm);
                time_0330pm.setBackgroundResource(R.drawable.time_set_color);
                time_1230pm.setBackgroundResource(0);
                time_0100pm.setBackgroundResource(0);
                time_1200pm.setBackgroundResource(0);
                time_0130pm.setBackgroundResource(0);
                time_0230pm.setBackgroundResource(0);
                time_0300pm.setBackgroundResource(0);
                time_0200pm.setBackgroundResource(0);
                time_0400pm.setBackgroundResource(0);
                time_0430pm.setBackgroundResource(0);
                time_0500pm.setBackgroundResource(0);
                time_0530pm.setBackgroundResource(0);
            }
        });

        time_0400pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._04_00pm);
                time_0400pm.setBackgroundResource(R.drawable.time_set_color);
                time_1230pm.setBackgroundResource(0);
                time_0100pm.setBackgroundResource(0);
                time_1200pm.setBackgroundResource(0);
                time_0130pm.setBackgroundResource(0);
                time_0230pm.setBackgroundResource(0);
                time_0300pm.setBackgroundResource(0);
                time_0330pm.setBackgroundResource(0);
                time_0200pm.setBackgroundResource(0);
                time_0430pm.setBackgroundResource(0);
                time_0500pm.setBackgroundResource(0);
                time_0530pm.setBackgroundResource(0);
            }
        });

        time_0430pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._04_30pm);
                time_0430pm.setBackgroundResource(R.drawable.time_set_color);
                time_1230pm.setBackgroundResource(0);
                time_0100pm.setBackgroundResource(0);
                time_1200pm.setBackgroundResource(0);
                time_0130pm.setBackgroundResource(0);
                time_0230pm.setBackgroundResource(0);
                time_0300pm.setBackgroundResource(0);
                time_0330pm.setBackgroundResource(0);
                time_0400pm.setBackgroundResource(0);
                time_0200pm.setBackgroundResource(0);
                time_0500pm.setBackgroundResource(0);
                time_0530pm.setBackgroundResource(0);
            }
        });

        time_0500pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._05_00pm);
                time_0500pm.setBackgroundResource(R.drawable.time_set_color);
                time_1230pm.setBackgroundResource(0);
                time_0100pm.setBackgroundResource(0);
                time_1200pm.setBackgroundResource(0);
                time_0130pm.setBackgroundResource(0);
                time_0230pm.setBackgroundResource(0);
                time_0300pm.setBackgroundResource(0);
                time_0330pm.setBackgroundResource(0);
                time_0400pm.setBackgroundResource(0);
                time_0430pm.setBackgroundResource(0);
                time_0200pm.setBackgroundResource(0);
                time_0530pm.setBackgroundResource(0);
            }
        });

        time_0530pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._05_30pm);
                time_0530pm.setBackgroundResource(R.drawable.time_set_color);
                time_1230pm.setBackgroundResource(0);
                time_0100pm.setBackgroundResource(0);
                time_1200pm.setBackgroundResource(0);
                time_0130pm.setBackgroundResource(0);
                time_0230pm.setBackgroundResource(0);
                time_0300pm.setBackgroundResource(0);
                time_0330pm.setBackgroundResource(0);
                time_0400pm.setBackgroundResource(0);
                time_0430pm.setBackgroundResource(0);
                time_0500pm.setBackgroundResource(0);
                time_0200pm.setBackgroundResource(0);
            }
        });

        time_0600pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._06_00pm);
                time_0600pm.setBackgroundResource(R.drawable.time_set_color);
                time_0630pm.setBackgroundResource(0);
                time_0700pm.setBackgroundResource(0);
                time_0730pm.setBackgroundResource(0);
                time_0800pm.setBackgroundResource(0);
                time_0830pm.setBackgroundResource(0);
                time_0900pm.setBackgroundResource(0);
                time_0930pm.setBackgroundResource(0);
                time_1000pm.setBackgroundResource(0);
                time_1030pm.setBackgroundResource(0);
                time_1100pm.setBackgroundResource(0);
                time_1130pm.setBackgroundResource(0);
            }
        });

        time_0630pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._06_30pm);
                time_0630pm.setBackgroundResource(R.drawable.time_set_color);
                time_0600pm.setBackgroundResource(0);
                time_0700pm.setBackgroundResource(0);
                time_0730pm.setBackgroundResource(0);
                time_0800pm.setBackgroundResource(0);
                time_0830pm.setBackgroundResource(0);
                time_0900pm.setBackgroundResource(0);
                time_0930pm.setBackgroundResource(0);
                time_1000pm.setBackgroundResource(0);
                time_1030pm.setBackgroundResource(0);
                time_1100pm.setBackgroundResource(0);
                time_1130pm.setBackgroundResource(0);
            }
        });


        time_0700pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._07_00pm);
                time_0700pm.setBackgroundResource(R.drawable.time_set_color);
                time_0630pm.setBackgroundResource(0);
                time_0600pm.setBackgroundResource(0);
                time_0730pm.setBackgroundResource(0);
                time_0800pm.setBackgroundResource(0);
                time_0830pm.setBackgroundResource(0);
                time_0900pm.setBackgroundResource(0);
                time_0930pm.setBackgroundResource(0);
                time_1000pm.setBackgroundResource(0);
                time_1030pm.setBackgroundResource(0);
                time_1100pm.setBackgroundResource(0);
                time_1130pm.setBackgroundResource(0);
            }
        });


        time_0730pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._07_30pm);
                time_0730pm.setBackgroundResource(R.drawable.time_set_color);
                time_0630pm.setBackgroundResource(0);
                time_0700pm.setBackgroundResource(0);
                time_0600pm.setBackgroundResource(0);
                time_0800pm.setBackgroundResource(0);
                time_0830pm.setBackgroundResource(0);
                time_0900pm.setBackgroundResource(0);
                time_0930pm.setBackgroundResource(0);
                time_1000pm.setBackgroundResource(0);
                time_1030pm.setBackgroundResource(0);
                time_1100pm.setBackgroundResource(0);
                time_1130pm.setBackgroundResource(0);
            }
        });


        time_0800pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._08_00pm);
                time_0800pm.setBackgroundResource(R.drawable.time_set_color);
                time_0630pm.setBackgroundResource(0);
                time_0700pm.setBackgroundResource(0);
                time_0730pm.setBackgroundResource(0);
                time_0600pm.setBackgroundResource(0);
                time_0830pm.setBackgroundResource(0);
                time_0900pm.setBackgroundResource(0);
                time_0930pm.setBackgroundResource(0);
                time_1000pm.setBackgroundResource(0);
                time_1030pm.setBackgroundResource(0);
                time_1100pm.setBackgroundResource(0);
                time_1130pm.setBackgroundResource(0);
            }
        });


        time_0830pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._08_30pm);
                time_0830pm.setBackgroundResource(R.drawable.time_set_color);
                time_0630pm.setBackgroundResource(0);
                time_0700pm.setBackgroundResource(0);
                time_0730pm.setBackgroundResource(0);
                time_0800pm.setBackgroundResource(0);
                time_0600pm.setBackgroundResource(0);
                time_0900pm.setBackgroundResource(0);
                time_0930pm.setBackgroundResource(0);
                time_1000pm.setBackgroundResource(0);
                time_1030pm.setBackgroundResource(0);
                time_1100pm.setBackgroundResource(0);
                time_1130pm.setBackgroundResource(0);
            }
        });

        time_0900pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._09_00pm);
                time_0900pm.setBackgroundResource(R.drawable.time_set_color);
                time_0630pm.setBackgroundResource(0);
                time_0700pm.setBackgroundResource(0);
                time_0730pm.setBackgroundResource(0);
                time_0800pm.setBackgroundResource(0);
                time_0830pm.setBackgroundResource(0);
                time_0600pm.setBackgroundResource(0);
                time_0930pm.setBackgroundResource(0);
                time_1000pm.setBackgroundResource(0);
                time_1030pm.setBackgroundResource(0);
                time_1100pm.setBackgroundResource(0);
                time_1130pm.setBackgroundResource(0);
            }
        });

        time_0930pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._09_30pm);
                time_0930pm.setBackgroundResource(R.drawable.time_set_color);
                time_0630pm.setBackgroundResource(0);
                time_0700pm.setBackgroundResource(0);
                time_0730pm.setBackgroundResource(0);
                time_0800pm.setBackgroundResource(0);
                time_0830pm.setBackgroundResource(0);
                time_0900pm.setBackgroundResource(0);
                time_0600pm.setBackgroundResource(0);
                time_1000pm.setBackgroundResource(0);
                time_1030pm.setBackgroundResource(0);
                time_1100pm.setBackgroundResource(0);
                time_1130pm.setBackgroundResource(0);
            }
        });

        time_1000pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._10_00pm);
                time_1000pm.setBackgroundResource(R.drawable.time_set_color);
                time_0630pm.setBackgroundResource(0);
                time_0700pm.setBackgroundResource(0);
                time_0730pm.setBackgroundResource(0);
                time_0800pm.setBackgroundResource(0);
                time_0830pm.setBackgroundResource(0);
                time_0900pm.setBackgroundResource(0);
                time_0930pm.setBackgroundResource(0);
                time_0600pm.setBackgroundResource(0);
                time_1030pm.setBackgroundResource(0);
                time_1100pm.setBackgroundResource(0);
                time_1130pm.setBackgroundResource(0);
            }
        });

        time_1030pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._10_30pm);
                time_1030pm.setBackgroundResource(R.drawable.time_set_color);
                time_0630pm.setBackgroundResource(0);
                time_0700pm.setBackgroundResource(0);
                time_0730pm.setBackgroundResource(0);
                time_0800pm.setBackgroundResource(0);
                time_0830pm.setBackgroundResource(0);
                time_0900pm.setBackgroundResource(0);
                time_0930pm.setBackgroundResource(0);
                time_1000pm.setBackgroundResource(0);
                time_0600pm.setBackgroundResource(0);
                time_1100pm.setBackgroundResource(0);
                time_1130pm.setBackgroundResource(0);
            }
        });

        time_1100pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._11_00pm);
                time_1100pm.setBackgroundResource(R.drawable.time_set_color);
                time_0630pm.setBackgroundResource(0);
                time_0700pm.setBackgroundResource(0);
                time_0730pm.setBackgroundResource(0);
                time_0800pm.setBackgroundResource(0);
                time_0830pm.setBackgroundResource(0);
                time_0900pm.setBackgroundResource(0);
                time_0930pm.setBackgroundResource(0);
                time_1000pm.setBackgroundResource(0);
                time_1030pm.setBackgroundResource(0);
                time_0600pm.setBackgroundResource(0);
                time_1130pm.setBackgroundResource(0);
            }
        });

        time_1130pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set.setText(R.string._11_30pm);
                time_1130pm.setBackgroundResource(R.drawable.time_set_color);
                time_0630pm.setBackgroundResource(0);
                time_0700pm.setBackgroundResource(0);
                time_0730pm.setBackgroundResource(0);
                time_0800pm.setBackgroundResource(0);
                time_0830pm.setBackgroundResource(0);
                time_0900pm.setBackgroundResource(0);
                time_0930pm.setBackgroundResource(0);
                time_1000pm.setBackgroundResource(0);
                time_1030pm.setBackgroundResource(0);
                time_1100pm.setBackgroundResource(0);
                time_0600pm.setBackgroundResource(0);
            }
        });




    }
}
