package com.prescywallet.presdigi;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.prescywallet.presdigi.Adapters.CartListAdapter;
import com.prescywallet.presdigi.Interface.OnDataChangeListener;
import com.prescywallet.presdigi.Model.CartItem;
import com.prescywallet.presdigi.database.AttachedDigitalPrescriptionDBHelper;
import com.prescywallet.presdigi.database.AttachedPrescriptionDBHelper;
import com.prescywallet.presdigi.database.CartDbHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private RecyclerView cartList;
    private List<CartItem> cartItems;
    private CartListAdapter adapter;
    private CardView require_prescription_card;
    private TextView subtotal_amount, promo_discount_amount, grand_total, continue_text_view;
    private ConstraintLayout continue_btn, no_cart_layout_view;
    private ScrollView cart_layout_view;
    private Button add_medicine_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_bar_main);
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

        cartList = findViewById(R.id.cart_list);
        subtotal_amount = findViewById(R.id.subtotal_amount);
        promo_discount_amount = findViewById(R.id.promo_discount_amount);
        grand_total = findViewById(R.id.grand_total);
        require_prescription_card = findViewById(R.id.require_prescription_card);
        require_prescription_card.setVisibility(View.GONE);
        continue_btn = findViewById(R.id.continue_btn);

        no_cart_layout_view = findViewById(R.id.no_cart_layout_view);
        cart_layout_view = findViewById(R.id.cart_layout_view);
        add_medicine_btn = findViewById(R.id.add_medicine_btn);
        continue_text_view = findViewById(R.id.continue_text_view);

        cartItems = new ArrayList<>();

        loadCart();
    }



    private void loadCart() {
        cartItems = new CartDbHelper(this).getCart();
        if (cartItems.size() == 0){
            new AttachedDigitalPrescriptionDBHelper(getApplicationContext()).deleteAllDigitalPrescription();
            new AttachedPrescriptionDBHelper(getApplicationContext()).deleteAllDigitalPrescription();

            cart_layout_view.setVisibility(View.GONE);
            continue_btn.setVisibility(View.GONE);
            no_cart_layout_view.setVisibility(View.VISIBLE);
        }else {
            no_cart_layout_view.setVisibility(View.GONE);
            cart_layout_view.setVisibility(View.VISIBLE);
            continue_btn.setVisibility(View.VISIBLE);

        }

        add_medicine_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, SearchMedicineActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        adapter = new CartListAdapter(cartItems, this);
        cartList.setAdapter(adapter);
        cartList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onDataChanged(int size) {
                cartItems = new CartDbHelper(getApplicationContext()).getCart();
                if (cartItems.size() == 0){
                    new AttachedDigitalPrescriptionDBHelper(getApplicationContext()).deleteAllDigitalPrescription();
                    new AttachedPrescriptionDBHelper(getApplicationContext()).deleteAllDigitalPrescription();

                    cart_layout_view.setVisibility(View.GONE);
                    continue_btn.setVisibility(View.GONE);
                    no_cart_layout_view.setVisibility(View.VISIBLE);
                }
                int subtotal = 0;
                boolean checkPresReq = false;
                for (CartItem cartItem : cartItems){
                    subtotal += Float.valueOf(cartItem.getPrice())*Integer.valueOf(cartItem.getQuantity());
                    if (cartItem.getRequirePrescription().equalsIgnoreCase("Yes")){
                        checkPresReq = true;
                    }
                }
                if (checkPresReq){
                    require_prescription_card.setVisibility(View.VISIBLE);
                }else {

                    require_prescription_card.setVisibility(View.GONE);
                }
                Locale locale = new Locale("hi", "IN");
                NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

                subtotal_amount.setText(nf.format(subtotal));
                float promo = (float) (subtotal*0.1);
                String prome_dis = "-"+ nf.format(promo);
                promo_discount_amount.setText(prome_dis);

                float grand_tot = subtotal-promo;
                grand_total.setText(nf.format(grand_tot));

                String continue_text = "Continue (" + nf.format(grand_tot) + ")";
                continue_text_view.setText(continue_text);


            }
        });
        int subtotal = 0;
        boolean checkPresReq = false;
        for (CartItem cartItem : cartItems){
            subtotal += Float.valueOf(cartItem.getPrice())*Integer.valueOf(cartItem.getQuantity());
            if (cartItem.getRequirePrescription().equalsIgnoreCase("Yes")){
                checkPresReq = true;
            }
        }
        if (checkPresReq){
            require_prescription_card.setVisibility(View.VISIBLE);
        }else {

            require_prescription_card.setVisibility(View.GONE);
        }


        Locale locale = new Locale("hi", "IN");
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

        subtotal_amount.setText(nf.format(subtotal));
        float promo = (float) (subtotal*0.1);
        String prome_dis = "-"+ nf.format(promo);
        promo_discount_amount.setText(prome_dis);

        final float grand_tot = subtotal-promo;
        grand_total.setText(nf.format(grand_tot));

        String continue_text = "Continue (" + nf.format(grand_tot) + ")";
        continue_text_view.setText(continue_text);

        final boolean finalCheckPresReq = checkPresReq;
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalCheckPresReq){
                    Intent intent = new Intent(CartActivity.this, AttachPrescriptionActivity.class);
                    intent.putExtra("Grand_total", String.valueOf(grand_tot));
                    startActivity(intent);
                }else {
                    startActivity(new Intent(CartActivity.this, SelectAddressForCheckoutActivity.class));
                }

            }
        });


    }
}
