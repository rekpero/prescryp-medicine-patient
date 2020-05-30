package com.prescywallet.presdigi;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.prescywallet.presdigi.Adapters.PrescriptionGroupingAdapter;
import com.prescywallet.presdigi.Model.DigitizedPrescriptionItem;
import com.prescywallet.presdigi.Model.PrescriptionGroupItem;
import com.prescywallet.presdigi.Session.MobileNumberSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PrescriptionGroupingActivity extends AppCompatActivity {

    private List<DigitizedPrescriptionItem> digitizedPrescriptionItems;
    private List<PrescriptionGroupItem> prescriptionGroupItems;
    private RecyclerView listOfGroup;
    private TextView numOfPrescription;
    private String currentGroupBy = "DOCTOR NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescription_grouping_bar_main);
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

        digitizedPrescriptionItems = new ArrayList<>();
        prescriptionGroupItems = new ArrayList<>();

        listOfGroup = findViewById(R.id.list_of_group);

        getPrescription();

        numOfPrescription = findViewById(R.id.num_of_prescription);
        TextView groupBy = findViewById(R.id.group_by_btn);
        groupBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGroupByPopup();
            }
        });


    }

    private void showGroupByPopup() {
        ConstraintLayout doctorLayout, patientLayout, dateOfConsultationLayout;
        TextView doctorTextView, patientTextView, dateOfConsultationTextView, resetTextView;
        ImageView closeGroupBtn;
        final Dialog dialog = new Dialog(PrescriptionGroupingActivity.this);
        dialog.setContentView(R.layout.group_by_popup);
        doctorLayout = dialog.findViewById(R.id.doctor_layout);
        patientLayout = dialog.findViewById(R.id.patient_layout);
        dateOfConsultationLayout = dialog.findViewById(R.id.date_of_consultation_layout);

        doctorTextView = dialog.findViewById(R.id.doctor_text_view);
        patientTextView = dialog.findViewById(R.id.patient_text_view);
        dateOfConsultationTextView = dialog.findViewById(R.id.date_of_consultation_text_view);
        resetTextView = dialog.findViewById(R.id.reset_text_view);

        closeGroupBtn = dialog.findViewById(R.id.closeGroupBtn);

        closeGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (currentGroupBy.equalsIgnoreCase("DOCTOR NAME")){
            resetTextView.setVisibility(View.GONE);
        }else {
            resetTextView.setVisibility(View.VISIBLE);
        }

        resetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentGroupBy = "DOCTOR NAME";
                groupByDoctorName();
                dialog.dismiss();
            }
        });

        if (currentGroupBy.equalsIgnoreCase("DOCTOR NAME")){
            doctorTextView.setTextColor(getResources().getColor(R.color.themeColor));
            patientTextView.setTextColor(Color.BLACK);
            dateOfConsultationTextView.setTextColor(Color.BLACK);
        }else if (currentGroupBy.equalsIgnoreCase("PATIENT NAME")){
            patientTextView.setTextColor(getResources().getColor(R.color.themeColor));
            doctorTextView.setTextColor(Color.BLACK);
            dateOfConsultationTextView.setTextColor(Color.BLACK);
        }else if (currentGroupBy.equalsIgnoreCase("DATE OF CONSULTATION")){
            dateOfConsultationTextView.setTextColor(getResources().getColor(R.color.themeColor));
            doctorTextView.setTextColor(Color.BLACK);
            patientTextView.setTextColor(Color.BLACK);
        }

        doctorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupByDoctorName();
                currentGroupBy = "DOCTOR NAME";
                dialog.dismiss();
            }
        });
        patientLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupByPatientName();
                currentGroupBy = "PATIENT NAME";
                dialog.dismiss();
            }
        });
        dateOfConsultationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupByDateOfConsultation();
                currentGroupBy = "DATE OF CONSULTATION";
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    private void groupByDoctorName() {
        prescriptionGroupItems.clear();
        for (DigitizedPrescriptionItem item : digitizedPrescriptionItems){
            Boolean contain = false;
            for (PrescriptionGroupItem groupItem : prescriptionGroupItems){
                if (item.getDoctorName().equalsIgnoreCase(groupItem.getItemTitle())){
                    groupItem.getListItem().add(item);
                    contain = true;
                }
            }
            if (!contain){
                List<DigitizedPrescriptionItem> list = new ArrayList<>();
                list.add(item);
                prescriptionGroupItems.add(new PrescriptionGroupItem(item.getDoctorName(), list));
            }
        }

        PrescriptionGroupingAdapter prescriptionGroupAdapter = new PrescriptionGroupingAdapter(prescriptionGroupItems, PrescriptionGroupingActivity.this);
        listOfGroup.setAdapter(prescriptionGroupAdapter);
        listOfGroup.setLayoutManager(new LinearLayoutManager(PrescriptionGroupingActivity.this));

    }

    private void groupByPatientName() {
        prescriptionGroupItems.clear();
        for (DigitizedPrescriptionItem item : digitizedPrescriptionItems){
            Boolean contain = false;
            for (PrescriptionGroupItem groupItem : prescriptionGroupItems){
                if (item.getPatientName().equalsIgnoreCase(groupItem.getItemTitle())){
                    groupItem.getListItem().add(item);
                    contain = true;
                }
            }
            if (!contain){
                List<DigitizedPrescriptionItem> list = new ArrayList<>();
                list.add(item);
                prescriptionGroupItems.add(new PrescriptionGroupItem(item.getPatientName(), list));
            }
        }

        PrescriptionGroupingAdapter prescriptionGroupAdapter = new PrescriptionGroupingAdapter(prescriptionGroupItems, PrescriptionGroupingActivity.this);
        listOfGroup.setAdapter(prescriptionGroupAdapter);
        listOfGroup.setLayoutManager(new LinearLayoutManager(PrescriptionGroupingActivity.this));

    }

    private void groupByDateOfConsultation() {
        prescriptionGroupItems.clear();
        for (DigitizedPrescriptionItem item : digitizedPrescriptionItems){
            Boolean contain = false;
            for (PrescriptionGroupItem groupItem : prescriptionGroupItems){
                if (item.getDateOfConsultation().equalsIgnoreCase(groupItem.getItemTitle())){
                    groupItem.getListItem().add(item);
                    contain = true;
                }
            }
            if (!contain){
                List<DigitizedPrescriptionItem> list = new ArrayList<>();
                list.add(item);
                prescriptionGroupItems.add(new PrescriptionGroupItem(item.getDateOfConsultation(), list));
            }
        }

        PrescriptionGroupingAdapter prescriptionGroupAdapter = new PrescriptionGroupingAdapter(prescriptionGroupItems, PrescriptionGroupingActivity.this);
        listOfGroup.setAdapter(prescriptionGroupAdapter);
        listOfGroup.setLayoutManager(new LinearLayoutManager(PrescriptionGroupingActivity.this));

    }

    private void getPrescription(){
        MobileNumberSessionManager mobileNumberSessionManager = new MobileNumberSessionManager(getApplicationContext());
        final HashMap<String, String> detail = mobileNumberSessionManager.geMobileDetails();
        String url =  "http://prescryp.com/prescriptionUpload/getDigitalizedPrescriptionDetails.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting the string to json array object
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("prescriptions");
                            //Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();

                            //traversing through all the object
                            if (success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    //getting product object from json array
                                    JSONObject product = jsonArray.getJSONObject(i);

                                    //adding the product to product list
                                    String prescriptionId = product.getString("prescription_id");
                                    String dateOfCreation = product.getString("date_of_creation");
                                    String doctorName = product.getString("doctor_name_qualification").split(",")[0];
                                    String patientName = product.getString("patient_name");
                                    String dateOfConsultation = product.getString("patient_date_of_consultation");
                                    String dateOfConsultationFormatChanged = getDateFormatChange(dateOfConsultation);

                                    boolean checkingPrescription = false;
                                    for (DigitizedPrescriptionItem item : digitizedPrescriptionItems){
                                        if (item.getPrescriptionId().equalsIgnoreCase(prescriptionId) && item.getDoctorName().equalsIgnoreCase(doctorName)
                                                && item.getDateOfCreation().equalsIgnoreCase(dateOfCreation) && item.getPatientName().equalsIgnoreCase(patientName)
                                                && item.getDateOfConsultation().equalsIgnoreCase(dateOfConsultationFormatChanged)){
                                            checkingPrescription = true;
                                        }
                                    }
                                    if (!checkingPrescription){
                                        digitizedPrescriptionItems.add(new DigitizedPrescriptionItem(prescriptionId, dateOfCreation,
                                                doctorName, patientName, dateOfConsultationFormatChanged));
                                    }

                                }
                                groupByDoctorName();
                                String number_of_pres = digitizedPrescriptionItems.size() + " PRESCRIPTIONS";
                                numOfPrescription.setText(number_of_pres);

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
                HashMap<String, String> params = new HashMap<>();
                params.put("mobilenumber", detail.get(MobileNumberSessionManager.KEY_MOB));
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


    }

    private String getDateFormatChange(String date){
        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
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
