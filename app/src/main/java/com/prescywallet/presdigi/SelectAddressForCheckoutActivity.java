package com.prescywallet.presdigi;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.prescywallet.presdigi.Adapters.SavedAddressListAdapter;
import com.prescywallet.presdigi.Model.DeliveryAddressItem;
import com.prescywallet.presdigi.database.PatientAddressDBHelper;

import java.util.List;

public class SelectAddressForCheckoutActivity extends AppCompatActivity {

    private ConstraintLayout add_address_layout;
    private RecyclerView saved_address_checkout_recycler_view;
    private SavedAddressListAdapter addressListAdapter;
    private List<DeliveryAddressItem> addressItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address_for_checkout);
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

        add_address_layout = findViewById(R.id.add_address_layout);
        saved_address_checkout_recycler_view = findViewById(R.id.saved_address_checkout_recycler_view);

        add_address_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectAddressForCheckoutActivity.this, AddNewAddressActivity.class);
                intent.putExtra("SENDER_KEY", "for_checkout");
                startActivity(intent);
            }
        });

        addressItems = new PatientAddressDBHelper(SelectAddressForCheckoutActivity.this).getAllDeliveryAddress();
        if (addressItems.size() == 0){
            Intent intent = new Intent(SelectAddressForCheckoutActivity.this, AddNewAddressActivity.class);
            intent.putExtra("SENDER_KEY", "for_checkout");
            startActivity(intent);
        }

        addressListAdapter = new SavedAddressListAdapter(addressItems, SelectAddressForCheckoutActivity.this, "Checkout");
        saved_address_checkout_recycler_view.setAdapter(addressListAdapter);
        saved_address_checkout_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


    }

}
