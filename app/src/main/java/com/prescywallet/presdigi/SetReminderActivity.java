package com.prescywallet.presdigi;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.prescywallet.presdigi.Model.AlarmItem;
import com.prescywallet.presdigi.Model.DateItem;
import com.prescywallet.presdigi.database.AlarmReminderContract;
import com.prescywallet.presdigi.reminder.AlarmScheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SetReminderActivity extends AppCompatActivity {
    private TextView repeatText, routine_time1, routine_time2, routine_time3, repeat_type;
    private TextView durationText, routine_dosage1, routine_dosage2, routine_dosage3, edit_breakfast_time, edit_lunch_time, edit_dinner_time;
    private RelativeLayout scheduleBefore, scheduleAfter, breakfastView, lunchView, dinnerView;
    private Spinner duration_spinner;
    private RadioGroup radio_duration, radio_repeat, before_after_radio1, before_after_radio2, before_after_radio3;
    private LinearLayout weeklyView, monthlyView, beforeAfterLinear1, beforeAfterLinear2, beforeAfterLinear3;
    private CheckBox sun_chk, mon_chk, tue_chk, wed_chk, thurs_chk, fri_chk, sat_chk;
    private boolean week_al, date_err, check_breakfast_open = false, check_lunch_open = false, check_dinner_open = false;
    private EditText month_days;
    private ImageView expandImg1, expandImg2, expandImg3, checkImg1, checkImg2, checkImg3;
    private Spinner dosage_num_spinner1, dosage_num_spinner2, dosage_num_spinner3, dosage_type_spinner1, dosage_type_spinner2, dosage_type_spinner3;
    private TextView medicineName;
    private EditText editMedicineName;
    private CardView scheduleCard, saveReminder;
    private ImageView saveAll;
    private Switch routine_chk1, routine_chk2, routine_chk3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);
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

        init();

        radio_duration.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.num_radio){
                    duration_spinner.setClickable(true);
                    duration_spinner.setEnabled(true);
                }else {
                    duration_spinner.setClickable(false);
                    duration_spinner.setEnabled(false);
                }
            }
        });
        radio_repeat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.weekly_radio){
                    monthlyView.setVisibility(View.GONE);
                    weeklyView.setVisibility(View.VISIBLE);
                    repeat_type.setText(R.string.weeks);

                }else if (checkedId == R.id.monthly_radio){
                    weeklyView.setVisibility(View.GONE);
                    monthlyView.setVisibility(View.VISIBLE);
                    repeat_type.setText(R.string.months);

                }else {
                    weeklyView.setVisibility(View.GONE);
                    monthlyView.setVisibility(View.GONE);
                    repeat_type.setText(R.string.days);
                }
            }
        });


        saveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSaveAll();
            }
        });

        breakfastView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routine_dosage1.setVisibility(View.GONE);
                expandImg1.setVisibility(View.GONE);
                routine_time1.setTextColor(getResources().getColor(R.color.themeColor));
                edit_breakfast_time.setTextColor(getResources().getColor(R.color.themeColor));
                edit_breakfast_time.setVisibility(View.VISIBLE);
                checkImg1.setVisibility(View.VISIBLE);
                beforeAfterLinear1.setVisibility(View.VISIBLE);
                if (check_breakfast_open) {
                    Intent intent = new Intent(getApplication(), SetReminderTimeActivity.class);
                    intent.putExtra("ROUTINE_TYPE", "BREAKFAST");
                    intent.putExtra("ROUTINE_TIME", routine_time1.getText().toString());
                    if (getIntent().getStringExtra("MedicineName") != null){
                        intent.putExtra("MEDICINE_NAME", medicineName.getText().toString());
                    }else {
                        intent.putExtra("MEDICINE_NAME", editMedicineName.getText().toString());
                    }
                    intent.putExtra("REPEAT_TYPE", repeatText.getText().toString());
                    intent.putExtra("REPEAT_TIMES", durationText.getText().toString());
                    intent.putExtra("DOSAGE", routine_dosage1.getText().toString());
                    startActivity(intent);
                }
                check_breakfast_open = true;
            }
        });



        lunchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routine_dosage2.setVisibility(View.GONE);
                expandImg2.setVisibility(View.GONE);
                routine_time2.setTextColor(getResources().getColor(R.color.themeColor));
                edit_lunch_time.setTextColor(getResources().getColor(R.color.themeColor));
                edit_lunch_time.setVisibility(View.VISIBLE);
                checkImg2.setVisibility(View.VISIBLE);
                beforeAfterLinear2.setVisibility(View.VISIBLE);
                if (check_lunch_open) {
                    Intent intent = new Intent(getApplication(), SetReminderTimeActivity.class);
                    intent.putExtra("ROUTINE_TYPE", "LUNCH");
                    intent.putExtra("ROUTINE_TIME", routine_time2.getText().toString());
                    if (getIntent().getStringExtra("MedicineName") != null){
                        intent.putExtra("MEDICINE_NAME", medicineName.getText().toString());
                    }else {
                        intent.putExtra("MEDICINE_NAME", editMedicineName.getText().toString());
                    }
                    intent.putExtra("REPEAT_TYPE", repeatText.getText().toString());
                    intent.putExtra("REPEAT_TIMES", durationText.getText().toString());
                    intent.putExtra("DOSAGE", routine_dosage2.getText().toString());
                    startActivity(intent);
                }
                check_lunch_open = true;
            }
        });




        dinnerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routine_dosage3.setVisibility(View.GONE);
                expandImg3.setVisibility(View.GONE);
                routine_time3.setTextColor(getResources().getColor(R.color.themeColor));
                edit_dinner_time.setTextColor(getResources().getColor(R.color.themeColor));
                edit_dinner_time.setVisibility(View.VISIBLE);
                checkImg3.setVisibility(View.VISIBLE);
                beforeAfterLinear3.setVisibility(View.VISIBLE);
                if (check_dinner_open){
                    Intent intent = new Intent(getApplication(), SetReminderTimeActivity.class);
                    intent.putExtra("ROUTINE_TYPE", "DINNER");
                    intent.putExtra("ROUTINE_TIME", routine_time3.getText().toString());
                    if (getIntent().getStringExtra("MedicineName") != null){
                        intent.putExtra("MEDICINE_NAME", medicineName.getText().toString());
                    }else {
                        intent.putExtra("MEDICINE_NAME", editMedicineName.getText().toString());
                    }
                    intent.putExtra("REPEAT_TYPE", repeatText.getText().toString());
                    intent.putExtra("REPEAT_TIMES", durationText.getText().toString());
                    intent.putExtra("DOSAGE", routine_dosage3.getText().toString());
                    startActivity(intent);

                }
                check_dinner_open = true;
            }
        });




        checkImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routine_time1.setTextColor(Color.BLACK);
                edit_breakfast_time.setTextColor(Color.GRAY);
                edit_breakfast_time.setVisibility(View.GONE);
                routine_dosage1.setVisibility(View.VISIBLE);
                String dosage_text = dosage_num_spinner1.getSelectedItem().toString() + " " + dosage_type_spinner1.getSelectedItem().toString() + " ";
                if (before_after_radio1.getCheckedRadioButtonId() == R.id.before_radio1){
                    dosage_text += "before Breakfast";
                }else if (before_after_radio1.getCheckedRadioButtonId() == R.id.after_radio1){
                    dosage_text += "after Breakfast";
                }else if (before_after_radio1.getCheckedRadioButtonId() == R.id.with_radio1){
                    dosage_text += "with Breakfast";
                }
                routine_dosage1.setText(dosage_text);

                checkImg1.setVisibility(View.GONE);
                beforeAfterLinear1.setVisibility(View.GONE);
                expandImg1.setVisibility(View.VISIBLE);
                check_breakfast_open = false;
            }
        });


        checkImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routine_time2.setTextColor(Color.BLACK);
                edit_lunch_time.setTextColor(Color.GRAY);
                edit_lunch_time.setVisibility(View.GONE);
                routine_dosage2.setVisibility(View.VISIBLE);
                String dosage_text = dosage_num_spinner2.getSelectedItem().toString() + " " + dosage_type_spinner2.getSelectedItem().toString() + " ";
                if (before_after_radio2.getCheckedRadioButtonId() == R.id.before_radio2){
                    dosage_text += "before Lunch";
                }else if (before_after_radio2.getCheckedRadioButtonId() == R.id.after_radio2){
                    dosage_text += "after Lunch";
                }else if (before_after_radio2.getCheckedRadioButtonId() == R.id.with_radio2){
                    dosage_text += "with Lunch";
                }
                routine_dosage2.setText(dosage_text);
                expandImg2.setVisibility(View.VISIBLE);
                checkImg2.setVisibility(View.GONE);
                beforeAfterLinear2.setVisibility(View.GONE);
                check_lunch_open = false;
            }
        });


        checkImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routine_time3.setTextColor(Color.BLACK);
                edit_dinner_time.setTextColor(Color.GRAY);
                edit_dinner_time.setVisibility(View.GONE);
                routine_dosage3.setVisibility(View.VISIBLE);
                String dosage_text = dosage_num_spinner3.getSelectedItem().toString() + " " + dosage_type_spinner3.getSelectedItem().toString() + " ";
                if (before_after_radio3.getCheckedRadioButtonId() == R.id.before_radio3){
                    dosage_text += "before Dinner";
                }else if (before_after_radio3.getCheckedRadioButtonId() == R.id.after_radio3){
                    dosage_text += "after Dinner";
                }else if (before_after_radio3.getCheckedRadioButtonId() == R.id.with_radio3){
                    dosage_text += "with Dinner";
                }
                routine_dosage3.setText(dosage_text);
                expandImg3.setVisibility(View.VISIBLE);
                checkImg3.setVisibility(View.GONE);
                beforeAfterLinear3.setVisibility(View.GONE);
                check_dinner_open = false;
            }
        });


        scheduleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleBefore.setVisibility(View.GONE);
                scheduleAfter.setVisibility(View.VISIBLE);
            }
        });

        saveReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReminder();
            }
        });

        if (getIntent() != null){
            if (getIntent().getStringExtra("MedicineName") != null){
                editMedicineName.setVisibility(View.GONE);
                medicineName.setVisibility(View.VISIBLE);
                String medicine_name = getIntent().getStringExtra("MedicineName");
                String first_cap = medicine_name.substring(0, 1).toUpperCase() + medicine_name.substring(1).toLowerCase();
                medicineName.setText(first_cap);
            }else {
                medicineName.setVisibility(View.GONE);
                editMedicineName.setVisibility(View.VISIBLE);
            }

            if (getIntent().getStringExtra("ROUTINE_TYPE") != null){
                if (getIntent().getStringExtra("SELECTED_ROUTINE_TIME") != null){
                    if (getIntent().getStringExtra("ROUTINE_TYPE").equalsIgnoreCase("BREAKFAST")){
                        routine_time1.setText(getIntent().getStringExtra("SELECTED_ROUTINE_TIME").toUpperCase());
                    }else if (getIntent().getStringExtra("ROUTINE_TYPE").equalsIgnoreCase("LUNCH")){
                        routine_time2.setText(getIntent().getStringExtra("SELECTED_ROUTINE_TIME").toUpperCase());
                    }else if (getIntent().getStringExtra("ROUTINE_TYPE").equalsIgnoreCase("DINNER")){
                        routine_time3.setText(getIntent().getStringExtra("SELECTED_ROUTINE_TIME").toUpperCase());
                    }
                }if (getIntent().getStringExtra("REPEAT_TYPE") != null){
                    repeatText.setText(getIntent().getStringExtra("REPEAT_TYPE"));
                }if (getIntent().getStringExtra("REPEAT_TIMES") != null){
                    durationText.setText(getIntent().getStringExtra("REPEAT_TIMES"));
                }if (getIntent().getStringExtra("DOSAGE") != null){
                    if (getIntent().getStringExtra("ROUTINE_TYPE").equalsIgnoreCase("BREAKFAST")){
                        routine_dosage1.setText(getIntent().getStringExtra("DOSAGE"));
                    }else if (getIntent().getStringExtra("ROUTINE_TYPE").equalsIgnoreCase("LUNCH")){
                        routine_dosage2.setText(getIntent().getStringExtra("DOSAGE"));
                    }else if (getIntent().getStringExtra("ROUTINE_TYPE").equalsIgnoreCase("DINNER")){
                        routine_dosage3.setText(getIntent().getStringExtra("DOSAGE"));
                    }
                }

            }

        }

        initReminderSettings();
    }

    private void addItemsOnDurationSpinner() {

        duration_spinner =  findViewById(R.id.duration_spinner);
        List<String> list = new ArrayList<String>();
        for (int i = 1; i<=31; i++){
            list.add(String.valueOf(i));
        }

        String default_duration = "3";


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);


        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        duration_spinner.setEnabled(false);
        duration_spinner.setClickable(false);
        duration_spinner.setAdapter(dataAdapter);
        int default_pos = dataAdapter.getPosition(default_duration);
        duration_spinner.setSelection(default_pos);
    }

    private void addItemsOnDosageSpinner1() {

        dosage_num_spinner1 = findViewById(R.id.dosage_num_spinner1);
        List<String> list = new ArrayList<String>();
        for (int i = 1; i<=5; i++){
            list.add(String.valueOf(i));
        }

        String default_duration = "1";


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);


        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dosage_num_spinner1.setAdapter(dataAdapter);
        int default_pos = dataAdapter.getPosition(default_duration);
        dosage_num_spinner1.setSelection(default_pos);
    }

    private void addItemsOnDosageSpinner2() {

        dosage_num_spinner2 =  findViewById(R.id.dosage_num_spinner2);
        List<String> list = new ArrayList<String>();
        for (int i = 1; i<=5; i++){
            list.add(String.valueOf(i));
        }

        String default_duration = "1";


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);


        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dosage_num_spinner2.setAdapter(dataAdapter);
        int default_pos = dataAdapter.getPosition(default_duration);
        dosage_num_spinner2.setSelection(default_pos);
    }

    private void addItemsOnDosageSpinner3() {

        dosage_num_spinner3 =  findViewById(R.id.dosage_num_spinner3);
        List<String> list = new ArrayList<String>();
        for (int i = 1; i<=5; i++){
            list.add(String.valueOf(i));
        }

        String default_duration = "1";


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);


        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dosage_num_spinner3.setAdapter(dataAdapter);
        int default_pos = dataAdapter.getPosition(default_duration);
        dosage_num_spinner3.setSelection(default_pos);
    }

    private void addItemsOnDosageTypeSpinner1() {

        dosage_type_spinner1 =  findViewById(R.id.dosage_type_spinner1);
        List<String> list = new ArrayList<String>();
        list.add("Tablet");
        list.add("Capsule");
        list.add("Injection");
        list.add("Tea Spoon");
        list.add("Drop");
        list.add("Lotion");
        list.add("Gel");
        list.add("Spray");
        list.add("Cream");
        list.add("Powder");
        list.add("Infusion");
        list.add("Solution");
        list.add("Inhaler");
        list.add("Suppository");
        list.add("Patch");
        list.add("Buccal");
        list.add("Suspension");

        String default_duration = "Tablet";


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);


        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dosage_type_spinner1.setAdapter(dataAdapter);
        int default_pos = dataAdapter.getPosition(default_duration);
        dosage_type_spinner1.setSelection(default_pos);
    }

    private void addItemsOnDosageTypeSpinner2() {

        dosage_type_spinner2 =  findViewById(R.id.dosage_type_spinner2);
        List<String> list = new ArrayList<String>();
        list.add("Tablet");
        list.add("Capsule");
        list.add("Injection");
        list.add("Tea Spoon");
        list.add("Drop");
        list.add("Lotion");
        list.add("Gel");
        list.add("Spray");
        list.add("Cream");
        list.add("Powder");
        list.add("Infusion");
        list.add("Solution");
        list.add("Inhaler");
        list.add("Suppository");
        list.add("Patch");
        list.add("Buccal");
        list.add("Suspension");

        String default_duration = "Tablet";


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);


        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dosage_type_spinner2.setAdapter(dataAdapter);
        int default_pos = dataAdapter.getPosition(default_duration);
        dosage_type_spinner2.setSelection(default_pos);
    }

    private void addItemsOnDosageTypeSpinner3() {

        dosage_type_spinner3 =  findViewById(R.id.dosage_type_spinner3);
        List<String> list = new ArrayList<String>();
        list.add("Tablet");
        list.add("Capsule");
        list.add("Injection");
        list.add("Tea Spoon");
        list.add("Drop");
        list.add("Lotion");
        list.add("Gel");
        list.add("Spray");
        list.add("Cream");
        list.add("Powder");
        list.add("Infusion");
        list.add("Solution");
        list.add("Inhaler");
        list.add("Suppository");
        list.add("Patch");
        list.add("Buccal");
        list.add("Suspension");

        String default_duration = "Tablet";


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);


        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dosage_type_spinner3.setAdapter(dataAdapter);
        int default_pos = dataAdapter.getPosition(default_duration);
        dosage_type_spinner3.setSelection(default_pos);
    }

    private void clickSaveAll(){
        scheduleBefore.setVisibility(View.INVISIBLE);
        if (radio_repeat.getCheckedRadioButtonId() == R.id.daily_radio){
            repeatText.setText(R.string.daily);
        }else if (radio_repeat.getCheckedRadioButtonId() == R.id.weekly_radio){
            String week_str = "Weekly on ";
            week_al = false;
            if (sun_chk.isChecked()){
                if (week_al){
                    week_str += ", ";
                }
                week_str += "Sun";
                week_al = true;
            }if (mon_chk.isChecked()){
                if (week_al){
                    week_str += ", ";
                }
                week_str += "Mon";
                week_al = true;
            }if (tue_chk.isChecked()){
                if (week_al){
                    week_str += ", ";
                }
                week_str += "Tue";
                week_al = true;
            }if (wed_chk.isChecked()){
                if (week_al){
                    week_str += ", ";
                }
                week_str += "Wed";
                week_al = true;
            }if (thurs_chk.isChecked()){
                if (week_al){
                    week_str += ", ";
                }
                week_str += "Thu";
                week_al = true;
            }if (fri_chk.isChecked()){
                if (week_al){
                    week_str += ", ";
                }
                week_str += "Fri";
                week_al = true;
            }if (sat_chk.isChecked()){
                if (week_al){
                    week_str += ", ";
                }
                week_str += "Sat";
                week_al = true;
            }
            repeatText.setText(week_str);
        }else if (radio_repeat.getCheckedRadioButtonId() == R.id.monthly_radio){
            date_err = false;
            if ( !month_days.getText().toString().matches("")){
                StringBuilder final_dates = new StringBuilder();
                String[] dates = month_days.getText().toString().split(",");
                for (int i = 0; i<dates.length; i++){
                    dates[i] = dates[i].trim();
                    if (Integer.valueOf(dates[i]) < 1 || Integer.valueOf(dates[i]) > 31){
                        date_err = true;
                    }else {
                        if (Integer.valueOf(dates[i]) == 1 || Integer.valueOf(dates[i]) == 21 || Integer.valueOf(dates[i]) == 31){
                            dates[i] += "st";
                        }else if (Integer.valueOf(dates[i]) == 2 || Integer.valueOf(dates[i]) == 22){
                            dates[i] += "nd";
                        }else if (Integer.valueOf(dates[i]) == 3 || Integer.valueOf(dates[i]) == 23){
                            dates[i] += "rd";
                        }else {
                            dates[i] += "th";
                        }
                    }
                    if (i == dates.length - 1){
                        final_dates.append(dates[i]);
                    }else {
                        final_dates.append(dates[i]).append(", ");
                    }

                }
                String month_str = "Monthly on " + final_dates;
                repeatText.setText(month_str);
            }


        }

        if (radio_duration.getCheckedRadioButtonId() == R.id.num_radio){
            String dur_tex = "For " + duration_spinner.getSelectedItem().toString() + " days";
            durationText.setText(dur_tex);
        }else if (radio_duration.getCheckedRadioButtonId() == R.id.until_radio){
            durationText.setText(R.string.until_i_stop);
        }
        if (radio_repeat.getCheckedRadioButtonId() == R.id.weekly_radio && !week_al){
            Toast.makeText(getApplicationContext(), "Please select a day", Toast.LENGTH_SHORT).show();
        }else if (radio_repeat.getCheckedRadioButtonId() == R.id.monthly_radio && month_days.getText().toString().matches("")){
            Toast.makeText(getApplicationContext(), "Please select a date", Toast.LENGTH_SHORT).show();
        }else if (radio_repeat.getCheckedRadioButtonId() == R.id.monthly_radio && date_err){
            Toast.makeText(getApplicationContext(), "Please select a correct date", Toast.LENGTH_SHORT).show();
        }else {
            scheduleAfter.setVisibility(View.GONE);
            scheduleBefore.setVisibility(View.VISIBLE);
        }


    }

    private void init(){
        medicineName = findViewById(R.id.medicine_name);
        editMedicineName = findViewById(R.id.editMedicineName);
        repeatText = findViewById(R.id.repeat_text);
        durationText = findViewById(R.id.duration_text);
        scheduleCard = findViewById(R.id.schedule_card);
        scheduleBefore = findViewById(R.id.schedule_before);
        scheduleAfter = findViewById(R.id.schedule_after);
        saveAll = findViewById(R.id.saveAll);
        weeklyView = findViewById(R.id.weekly_view);
        monthlyView = findViewById(R.id.monthly_view);
        sun_chk = findViewById(R.id.sunday_chk);
        mon_chk = findViewById(R.id.monday_chk);
        tue_chk = findViewById(R.id.tuesday_chk);
        wed_chk = findViewById(R.id.wednesday_chk);
        thurs_chk = findViewById(R.id.thursday_chk);
        fri_chk = findViewById(R.id.friday_chk);
        sat_chk = findViewById(R.id.saturday_chk);
        month_days = findViewById(R.id.monthly_days);
        breakfastView = findViewById(R.id.breakfast_view);
        lunchView = findViewById(R.id.lunch_view);
        dinnerView = findViewById(R.id.dinner_view);
        beforeAfterLinear1 = findViewById(R.id.before_after_linear);
        beforeAfterLinear2 = findViewById(R.id.before_after_linear1);
        beforeAfterLinear3 = findViewById(R.id.before_after_linear2);
        expandImg1 = findViewById(R.id.expand_img1);
        expandImg2 = findViewById(R.id.expand_img2);
        expandImg3 = findViewById(R.id.expand_img3);
        checkImg1 = findViewById(R.id.check_save_img1);
        checkImg2 = findViewById(R.id.check_save_img2);
        checkImg3 = findViewById(R.id.check_save_img3);
        dosage_num_spinner1 = findViewById(R.id.dosage_num_spinner1);
        dosage_num_spinner2 = findViewById(R.id.dosage_num_spinner2);
        dosage_num_spinner3 = findViewById(R.id.dosage_num_spinner3);
        dosage_type_spinner1 = findViewById(R.id.dosage_type_spinner1);
        dosage_type_spinner2 = findViewById(R.id.dosage_type_spinner2);
        dosage_type_spinner3 = findViewById(R.id.dosage_type_spinner3);
        routine_dosage1 = findViewById(R.id.routine_dosage1);
        routine_dosage2 = findViewById(R.id.routine_dosage2);
        routine_dosage3 = findViewById(R.id.routine_dosage3);
        edit_breakfast_time = findViewById(R.id.edit_breakfast_time);
        edit_lunch_time = findViewById(R.id.edit_lunch_time);
        edit_dinner_time = findViewById(R.id.edit_dinner_time);
        routine_time1 = findViewById(R.id.routine_time1);
        routine_time2 = findViewById(R.id.routine_time2);
        routine_time3 = findViewById(R.id.routine_time3);


        radio_duration = findViewById(R.id.radio_group_duration);
        radio_repeat = findViewById(R.id.radio_btn_repeat);
        before_after_radio1 = findViewById(R.id.before_after_radio1);
        before_after_radio2 = findViewById(R.id.before_after_radio2);
        before_after_radio3 = findViewById(R.id.before_after_radio3);
        repeat_type = findViewById(R.id.repeat_type);

        routine_chk1 = findViewById(R.id.routine_check_box1);
        routine_chk2 = findViewById(R.id.routine_check_box2);
        routine_chk3 = findViewById(R.id.routine_check_box3);

        saveReminder = findViewById(R.id.saveReminder);

        addItemsOnDurationSpinner();
        addItemsOnDosageSpinner1();
        addItemsOnDosageSpinner2();
        addItemsOnDosageSpinner3();
        addItemsOnDosageTypeSpinner1();
        addItemsOnDosageTypeSpinner2();
        addItemsOnDosageTypeSpinner3();
    }

    //Setting Reminder system...........................................................................

    // Constant values in milliseconds
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    private String mTitle;
    private String mTime;
    private String tDate, mDate;
    private String mDosage;
    private String mRepeat;
    private String mRepeatNo;
    private String mRepeatType;
    private String mActive = "true";
    private Calendar tCalendar;
    private int tYear, tMonth, tHour, tMinute, tDay, mDay, mMonth, mYear;
    private long mRepeatTime;
    private ArrayList<AlarmItem> alarm_list = new ArrayList<>();
    private ArrayList<DateItem> date_list = new ArrayList<>();
    private String mReminderStatus = "Take Medicine";



    private void initReminderSettings(){

        mRepeat = durationText.getText().toString();
        if (durationText.getText().toString().equalsIgnoreCase("Until I stop")){
            mRepeatNo = "1";
        }else {
            mRepeatNo = durationText.getText().toString().substring(4, 5);
        }


    }
    private int getWeekNumber(String weekDay){
        int res = 0;
        if (weekDay.equalsIgnoreCase("Mon")){
            res = 1;
        }else if (weekDay.equalsIgnoreCase("Tue")){
            res = 2;
        }else if (weekDay.equalsIgnoreCase("Wed")){
            res = 3;
        }else if (weekDay.equalsIgnoreCase("Thu")){
            res = 4;
        }else if (weekDay.equalsIgnoreCase("Fri")){
            res = 5;
        }else if (weekDay.equalsIgnoreCase("Sat")){
            res = 6;
        }else if (weekDay.equalsIgnoreCase("Sun")){
            res = 7;
        }
        return res;
    }

    private void saveReminder(){
        if (repeatText.getText().toString().contains("Daily")){
            mRepeatType = "Day";

            tCalendar = Calendar.getInstance();
            tDay = tCalendar.get(Calendar.DATE);
            tMonth = tCalendar.get(Calendar.MONTH) + 1;
            tYear = tCalendar.get(Calendar.YEAR);

            mDay = tDay;
            mMonth = tMonth;
            mYear = tYear;

            date_list.add(new DateItem(mDay, mMonth, mYear));

        }else if (repeatText.getText().toString().contains("Weekly")){
            mRepeatType = "Week";

            tCalendar = Calendar.getInstance();
            tDay = tCalendar.get(Calendar.DATE);
            tMonth = tCalendar.get(Calendar.MONTH) + 1;
            tYear = tCalendar.get(Calendar.YEAR);
            int monthMaxDays = tCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.US);
            Date d = new Date();
            String dayOfTheWeek = sdf.format(d);
            int currWeekNum = getWeekNumber(dayOfTheWeek);
            String weekDaysListStr = repeatText.getText().toString().substring(10);
            String[] weekDaysList = weekDaysListStr.split(",");
            for (String weekDay : weekDaysList){
                int week_day_num = getWeekNumber(weekDay.trim());
                int get_week_day_diff = week_day_num - currWeekNum;
                if (get_week_day_diff < 0){
                    mDay = tDay + get_week_day_diff + 7;
                }else {
                    mDay = tDay + get_week_day_diff;
                }
                if (mDay > monthMaxDays){
                    mDay -= monthMaxDays;
                    mMonth = tMonth + 1;
                }else {
                    mMonth = tMonth;
                }
                mYear = tYear;

                date_list.add(new DateItem(mDay, mMonth, mYear));
            }
        }else if (repeatText.getText().toString().contains("Monthly")){
            mRepeatType = "Month";

            tCalendar = Calendar.getInstance();
            tDay = tCalendar.get(Calendar.DATE);
            tMonth = tCalendar.get(Calendar.MONTH) + 1;
            tYear = tCalendar.get(Calendar.YEAR);

            String monthDaysListStr = repeatText.getText().toString().substring(11);
            String[] monthDaysList = monthDaysListStr.split(",");
            for (String monthDays : monthDaysList){
                int month_day_trim = Integer.valueOf(monthDays.trim().substring(0, 2));
                if (month_day_trim < tDay){
                    mMonth = tMonth + 1;
                }else {
                    mMonth = tMonth;
                }
                mDay = month_day_trim;
                mYear = tYear;
                if (mMonth > 12){
                    mMonth -= 12;
                    mYear -= 1;
                }
                date_list.add(new DateItem(mDay, mMonth, mYear));
            }
        }
        for (DateItem dateItem : date_list){
            if (routine_chk1.isChecked()){
                tCalendar = Calendar.getInstance();
                tHour = tCalendar.get(Calendar.HOUR_OF_DAY);
                tYear = tCalendar.get(Calendar.YEAR);
                tMonth = tCalendar.get(Calendar.MONTH) + 1;
                tDay = tCalendar.get(Calendar.DATE);
                tDate = tDay + "/" + tMonth + "/" + tYear;
                String time = routine_time1.getText().toString();

                if (time.contains("AM")){
                    String hour = time.substring(0, 2);
                    String minute = time.substring(3, 5);
                    if (dateItem.getDate().equalsIgnoreCase(tDate)) {
                        if (tHour > Integer.valueOf(hour)) {
                            tDay += 1;
                        } else if (tHour == Integer.valueOf(hour)) {
                            if (tMinute >= Integer.valueOf(minute)) {
                                tDay += 1;
                            }
                        }
                        dateItem.setDay(tDay);
                    }
                    tHour = Integer.valueOf(hour);
                    tMinute = Integer.valueOf(minute);
                }



                mDosage = routine_dosage1.getText().toString();
                mTime = time;

                mDate = dateItem.getDate();
                if (getIntent().getStringExtra("MedicineName") != null){
                    mTitle = medicineName.getText().toString();
                }else {
                    mTitle = editMedicineName.getText().toString();
                }

                ContentValues values = new ContentValues();
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_MEDICINE, mTitle);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_DATE, mDate);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TIME, mTime);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT, mRepeat);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO, mRepeatNo);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE, mRepeatType);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_ROUTINE_DOSAGE, mDosage);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REMINDER_STATUS, mReminderStatus);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE, mActive);


                // Set up calender for creating the notification
                tCalendar.set(Calendar.MONTH, dateItem.getMonth() - 1);
                tCalendar.set(Calendar.YEAR, dateItem.getYear());
                tCalendar.set(Calendar.DAY_OF_MONTH, dateItem.getDay());
                tCalendar.set(Calendar.HOUR_OF_DAY, tHour);
                tCalendar.set(Calendar.MINUTE, tMinute);
                tCalendar.set(Calendar.SECOND, 0);

                long selectedTimestamp =  tCalendar.getTimeInMillis();

                Uri newUri = getContentResolver().insert(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, values);

                alarm_list.add(new AlarmItem(newUri, selectedTimestamp));

            }if (routine_chk2.isChecked()){
                tCalendar = Calendar.getInstance();
                tHour = tCalendar.get(Calendar.HOUR_OF_DAY);
                tYear = tCalendar.get(Calendar.YEAR);
                tMonth = tCalendar.get(Calendar.MONTH) + 1;
                tDay = tCalendar.get(Calendar.DATE);
                tDate = tDay + "/" + tMonth + "/" + tYear;
                String time = routine_time2.getText().toString();

                if (time.contains("PM")){
                    String hour = time.substring(0, 2);
                    String minute = time.substring(3,5);
                    if (dateItem.getDate().equalsIgnoreCase(tDate)) {
                        if (tHour > Integer.valueOf(hour) + 12) {
                            tDay += 1;
                        } else if (tHour == Integer.valueOf(hour) + 12) {
                            if (tMinute >= Integer.valueOf(minute)) {
                                tDay += 1;
                            }
                        }
                        dateItem.setDay(tDay);
                    }
                    tHour = Integer.valueOf(hour) + 12;
                    tMinute = Integer.valueOf(minute);
                }

                mDosage = routine_dosage2.getText().toString();
                mTime = time;

                mDate = dateItem.getDate();
                if (getIntent().getStringExtra("MedicineName") != null){
                    mTitle = medicineName.getText().toString();
                }else {
                    mTitle = editMedicineName.getText().toString();
                }

                ContentValues values = new ContentValues();

                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_MEDICINE, mTitle);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_DATE, mDate);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TIME, mTime);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT, mRepeat);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO, mRepeatNo);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE, mRepeatType);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_ROUTINE_DOSAGE, mDosage);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REMINDER_STATUS, mReminderStatus);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE, mActive);


                // Set up calender for creating the notification
                tCalendar.set(Calendar.MONTH, dateItem.getMonth() - 1);
                tCalendar.set(Calendar.YEAR, dateItem.getYear());
                tCalendar.set(Calendar.DAY_OF_MONTH, dateItem.getDay());
                tCalendar.set(Calendar.HOUR_OF_DAY, tHour);
                tCalendar.set(Calendar.MINUTE, tMinute);
                tCalendar.set(Calendar.SECOND, 0);

                long selectedTimestamp =  tCalendar.getTimeInMillis();

                Uri newUri = getContentResolver().insert(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, values);

                alarm_list.add(new AlarmItem(newUri, selectedTimestamp));
            }if (routine_chk3.isChecked()){
                tCalendar = Calendar.getInstance();
                tHour = tCalendar.get(Calendar.HOUR_OF_DAY);
                tYear = tCalendar.get(Calendar.YEAR);
                tMonth = tCalendar.get(Calendar.MONTH) + 1;
                tDay = tCalendar.get(Calendar.DATE);
                tDate = tDay + "/" + tMonth + "/" + tYear;
                String time = routine_time3.getText().toString();

                if (time.contains("PM")){
                    String hour = time.substring(0, 2);
                    String minute = time.substring(3, 5);
                    if (dateItem.getDate().equalsIgnoreCase(tDate)){
                        if (tHour > Integer.valueOf(hour) + 12){
                            tDay += 1;
                        }else if (tHour == Integer.valueOf(hour) + 12){
                            if (tMinute >= Integer.valueOf(minute)){
                                tDay += 1;
                            }
                        }
                        dateItem.setDay(tDay);
                    }
                    tHour = Integer.valueOf(hour) + 12;
                    tMinute = Integer.valueOf(minute);
                }

                mDosage = routine_dosage3.getText().toString();
                mTime = time;

                mDate = dateItem.getDate();
                if (getIntent().getStringExtra("MedicineName") != null){
                    mTitle = medicineName.getText().toString();
                }else {
                    mTitle = editMedicineName.getText().toString();
                }

                ContentValues values = new ContentValues();

                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_MEDICINE, mTitle);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_DATE, mDate);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TIME, mTime);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT, mRepeat);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO, mRepeatNo);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE, mRepeatType);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_ROUTINE_DOSAGE, mDosage);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REMINDER_STATUS, mReminderStatus);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE, mActive);


                // Set up calender for creating the notification
                tCalendar.set(Calendar.MONTH, dateItem.getMonth() - 1);
                tCalendar.set(Calendar.YEAR, dateItem.getYear());
                tCalendar.set(Calendar.DAY_OF_MONTH, dateItem.getDay());
                tCalendar.set(Calendar.HOUR_OF_DAY, tHour);
                tCalendar.set(Calendar.MINUTE, tMinute);
                tCalendar.set(Calendar.SECOND, 0);

                long selectedTimestamp =  tCalendar.getTimeInMillis();

                Uri newUri = getContentResolver().insert(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, values);

                alarm_list.add(new AlarmItem(newUri, selectedTimestamp));
            }

        }


        // Check repeat type
        switch (mRepeatType) {
            case "Day":
                mRepeatTime = Integer.parseInt(mRepeatNo) * milDay;
                break;
            case "Week":
                mRepeatTime = Integer.parseInt(mRepeatNo) * milWeek;
                break;
            case "Month":
                mRepeatTime = Integer.parseInt(mRepeatNo) * milMonth;
                break;
        }


        // Create a new notification
        for (AlarmItem alarmItem : alarm_list){
            new AlarmScheduler().setRepeatAlarm(getApplicationContext(), alarmItem.getTimestamp(), alarmItem.getAlarmUri(), mRepeatTime);
            Toast.makeText(getApplicationContext(), "Your Reminders has been set", Toast.LENGTH_SHORT).show();
        }



        Intent intent = new Intent(getApplicationContext(), ReminderActivity.class);
        intent.putExtra("WINDOW_OPEN", "Timeline");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


}