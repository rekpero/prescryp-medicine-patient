package com.prescywallet.presdigi;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.prescywallet.presdigi.Adapters.AlarmListAdapter;
import com.prescywallet.presdigi.Adapters.MedicineListAdapter;
import com.prescywallet.presdigi.Model.MedicineItem;
import com.prescywallet.presdigi.Session.MobileNumberSessionManager;
import com.prescywallet.presdigi.database.AlarmReminderContract;
import com.prescywallet.presdigi.database.AlarmReminderDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReminderActivity extends AppCompatActivity {

    /**
     * The {@link androidx.viewpager.widget.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link androidx.fragment.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remider);

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
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), 2);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.setCurrentItem(1);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), SetReminderActivity.class));
            }
        });

        if (getIntent() != null){
            if (getIntent().getStringExtra("WINDOW_OPEN") != null){
                if (getIntent().getStringExtra("WINDOW_OPEN").equalsIgnoreCase("Timeline")){
                    mViewPager.setCurrentItem(0);
                }else {
                    mViewPager.setCurrentItem(1);
                }
            }
        }

    }


    public static class AlarmFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        public AlarmFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        private AlarmListAdapter mAlarmAdapter;
        private AlarmReminderDbHelper alarmReminderDbHelper = new AlarmReminderDbHelper(getContext());
        private TextView yourReminder;
        private static final int VEHICLE_LOADER = 0;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_alarm, container, false);
            yourReminder = rootView.findViewById(R.id.your_reminder_text);
            RecyclerView alarmRecycler = rootView.findViewById(R.id.alarm_list);
            alarmRecycler.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            alarmRecycler.setLayoutManager(layoutManager);

            mAlarmAdapter = new AlarmListAdapter(getContext(), null);
            alarmRecycler.setAdapter(mAlarmAdapter);

            getActivity().getSupportLoaderManager().initLoader(VEHICLE_LOADER, null, this);
            return rootView;
        }
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            String[] projection = {
                    AlarmReminderContract.AlarmReminderEntry._ID,
                    AlarmReminderContract.AlarmReminderEntry.KEY_MEDICINE,
                    AlarmReminderContract.AlarmReminderEntry.KEY_DATE,
                    AlarmReminderContract.AlarmReminderEntry.KEY_TIME,
                    AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                    AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                    AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                    AlarmReminderContract.AlarmReminderEntry.KEY_ROUTINE_DOSAGE,
                    AlarmReminderContract.AlarmReminderEntry.KEY_REMINDER_STATUS,
                    AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE

            };

            return new CursorLoader(getContext(),   // Parent activity context
                    AlarmReminderContract.AlarmReminderEntry.CONTENT_URI,   // Provider content URI to query
                    projection,             // Columns to include in the resulting Cursor
                    null,                   // No selection clause
                    null,                   // No selection arguments
                    "_ID DESC");                  // Default sort order

        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            mAlarmAdapter.swapCursor(data);
            if (data.getCount() > 0){
                yourReminder.setVisibility(View.VISIBLE);
            }else{
                yourReminder.setVisibility(View.GONE);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            mAlarmAdapter.swapCursor(null);
        }



    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class SuggestionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        public SuggestionFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        private RecyclerView recyclerView;
        private List<MedicineItem> medicineItems;
        private RecyclerView.Adapter adapter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_suggestion, container, false);
            recyclerView = rootView.findViewById(R.id.medicines_list);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            medicineItems = new ArrayList<>();

            getMedicines();

            return rootView;
        }
        MobileNumberSessionManager mobileNumberSessionManager;

        private void getMedicines(){
            mobileNumberSessionManager = new MobileNumberSessionManager(getActivity());
            final HashMap<String, String> detail = mobileNumberSessionManager.geMobileDetails();
            String url =  "http://prescryp.com/prescriptionUpload/getMedicinesNames.php";
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
                                JSONArray jsonArray = jsonObject.getJSONArray("medicines");

                                //traversing through all the object
                                if (success.equals("1")){
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        //getting product object from json array
                                        JSONObject product = jsonArray.getJSONObject(i);

                                        //adding the product to product list
                                        medicineItems.add(new MedicineItem(
                                                product.getString("medicine_name")
                                        ));

                                    }

                                    adapter = new MedicineListAdapter(medicineItems, getActivity());

                                    recyclerView.setAdapter(adapter);


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
            assert getActivity() != null;
            Volley.newRequestQueue(getActivity()).add(stringRequest);


        }

    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        int tabCount;

        public SectionsPagerAdapter(FragmentManager fm, int tabCount){
            super(fm);
            this.tabCount=tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0 :
                    return new AlarmFragment();
                case 1 :
                    return new SuggestionFragment();

                default: return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
