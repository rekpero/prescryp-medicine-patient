package com.prescywallet.presdigi;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.prescywallet.presdigi.Session.MobileNumberSessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;


public class PrescriptionUploadFragment extends Fragment {

    ImageView imgShow;
    ProgressBar progressBar;
    Toolbar uploadPresToolbar;
    String pathImage;
    MobileNumberSessionManager mobileNumberSessionManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            pathImage = getArguments().getString("pathImage");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.upload_bar_main, container, false);

        if (container != null) {
            container.removeAllViews();
        }

        uploadImageView(view);
        return view;
    }

    private void uploadImageView(View view){

        imgShow = (ImageView) view.findViewById(R.id.imgShow);
        progressBar = (ProgressBar) view.findViewById(R.id.loading);
        uploadPresToolbar = (Toolbar) view.findViewById(R.id.uploadPresToolbar);
        uploadPresToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        uploadPresToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgShow.setImageResource(R.drawable.img_show_upload);
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });

        init();


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.nextBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), name + " " + mobnum, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.VISIBLE);
                uploadImage();
            }
        });
    }




    private void uploadImage() {
        try {
            Bitmap bitmap = ImageLoader.init().from(pathImage).requestSize(256, 512).getBitmap();
            final String encodedImage = ImageBase64.encode(bitmap);
            //Toast.makeText(getApplicationContext(), mobileNum + " " + name + " " + pathImage, Toast.LENGTH_SHORT).show();




            String url = "http://prescryp.com/prescriptionUpload/uploadImage.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        String message = jsonObject.getString("message");
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        if (success.equalsIgnoreCase("1")){
                            imgShow.setImageResource(R.drawable.img_show_upload);
                            if (((MainActivity)getActivity()) != null){
                            ((MainActivity)getActivity()).navigation.setSelectedItemId(R.id.navigation_prescription);
                            }

                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    imgShow.setImageResource(R.drawable.img_show_upload);
                    if (((MainActivity)getActivity()) != null){
                        ((MainActivity)getActivity()).navigation.setSelectedItemId(R.id.navigation_prescription);
                    }
                    Volley.newRequestQueue(getApplicationContext()).getCache().clear();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    mobileNumberSessionManager = new MobileNumberSessionManager(getApplicationContext());
                    final HashMap<String, String> detail = mobileNumberSessionManager.geMobileDetails();
                    HashMap<String, String> params = new HashMap<>();
                    params.put("image", encodedImage);
                    params.put("mobilenumber", detail.get(MobileNumberSessionManager.KEY_MOB));
                    params.put("name", detail.get(MobileNumberSessionManager.KEY_NAME));
                    return params;
                }
            };
            Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
