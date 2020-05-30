package com.prescywallet.presdigi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.prescywallet.presdigi.Adapters.PresGridAdapter;
import com.prescywallet.presdigi.Adapters.PrescriptionAdapter;
import com.prescywallet.presdigi.Model.ListItem;
import com.prescywallet.presdigi.Session.MobileNumberSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class PrescriptionStatusFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.prescription_bar_main, container, false);
        if (container != null) {
            container.removeAllViews();
        }

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), 2);
        // Set up the ViewPager with the sections adapter.
        final ViewPager mViewPager = view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = view.findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.setCurrentItem(0);

        return view;
    }




    public static class ProcessingFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        public ProcessingFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        private RecyclerView recyclerView;
        private RecyclerView.Adapter adapter_history;
        private List<ListItem> listItems;
        MobileNumberSessionManager mobileNumberSessionManager;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_processing, container, false);
            recyclerView = rootView.findViewById(R.id.presList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            listItems = new ArrayList<>();

            getPrescription();
            return rootView;
        }

        private void getPrescription(){
            mobileNumberSessionManager = new MobileNumberSessionManager(getContext());
            final HashMap<String, String> detail = mobileNumberSessionManager.geMobileDetails();
            String url =  "http://prescryp.com/prescriptionUpload/getPrescriptionDetail.php";
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
                                JSONArray jsonArray = jsonObject.getJSONArray("login");

                                //traversing through all the object
                                if (success.equals("1")){
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        //getting product object from json array
                                        JSONObject product = jsonArray.getJSONObject(i);

                                        //adding the product to product list
                                        listItems.add(new ListItem(
                                                product.getString("prescription_id"),
                                                "Date : " + product.getString("date"),
                                                "Status : " + product.getString("status"),
                                                product.getString("image")
                                        ));


                                    }
                                    if (getActivity() != null){
                                        adapter_history = new PrescriptionAdapter(listItems, getActivity(), "History");
                                        recyclerView.setAdapter(adapter_history);
                                    }

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
                    params.put("name", detail.get(MobileNumberSessionManager.KEY_NAME));
                    return params;
                }
            };

            Volley.newRequestQueue(getContext()).add(stringRequest);


        }

    }

    public static class CompletedFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        public CompletedFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        GridView predGrid;
        private PresGridAdapter adapter_completed;
        private List<ListItem> listItem_completed;
        MobileNumberSessionManager mobileNumberSessionManager;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_completed, container, false);

            predGrid = rootView.findViewById(R.id.presGrid);
            listItem_completed = new ArrayList<>();
            getPrescription();

            return rootView;
        }

        private void getPrescription(){
            mobileNumberSessionManager = new MobileNumberSessionManager(getContext());
            final HashMap<String, String> detail = mobileNumberSessionManager.geMobileDetails();
            String url =  "http://prescryp.com/prescriptionUpload/getPrescriptionDetail.php";
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
                                JSONArray jsonArray = jsonObject.getJSONArray("login");

                                //traversing through all the object
                                if (success.equals("1")){
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        //getting product object from json array
                                        JSONObject product = jsonArray.getJSONObject(i);

                                        //adding the product to product list
                                        if (product.getString("status").equalsIgnoreCase("Completed")){
                                            listItem_completed.add(new ListItem(
                                                    product.getString("prescription_id"),
                                                    "Date : " + product.getString("date"),
                                                    "Status : " + product.getString("status"),
                                                    product.getString("image")
                                            ));
                                        }


                                    }
                                    if (getActivity() != null){
                                        adapter_completed = new PresGridAdapter(getActivity(), R.layout.grid_item_list, listItem_completed, "See Prescription");

                                        predGrid.setAdapter(adapter_completed);
                                    }

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
                    params.put("name", detail.get(MobileNumberSessionManager.KEY_NAME));
                    return params;
                }
            };

            Volley.newRequestQueue(getContext()).add(stringRequest);


        }

    }


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
                    return new PrescriptionStatusFragment.ProcessingFragment();
                case 1 :
                    return new PrescriptionStatusFragment.CompletedFragment();

                default: return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

}
