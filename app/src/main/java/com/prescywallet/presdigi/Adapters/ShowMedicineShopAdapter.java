package com.prescywallet.presdigi.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.snackbar.Snackbar;
import com.prescywallet.presdigi.Interface.OnDataChangeListener;
import com.prescywallet.presdigi.Interface.OnDataClickListener;
import com.prescywallet.presdigi.Model.CartItem;
import com.prescywallet.presdigi.Model.MedicinePackagePriorityItem;
import com.prescywallet.presdigi.Model.StorePriorityItem;
import com.prescywallet.presdigi.R;
import com.prescywallet.presdigi.database.CartDbHelper;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ShowMedicineShopAdapter extends RecyclerView.Adapter<ShowMedicineShopAdapter.ViewHolder>{

    private List<MedicinePackagePriorityItem> listItems;
    private Context context;
    private String medicineName, requirePres;
    private String max_priority_chemist, max_priority_chemist_store_name, max_priority_chemist_quantity;
    OnDataChangeListener mOnDataChangeListener;

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        mOnDataChangeListener = onDataChangeListener;
    }

    public ShowMedicineShopAdapter(List<MedicinePackagePriorityItem> listItems, Context context, String medicineName, String requirePres){
        this.listItems = listItems;
        this.context = context;
        this.medicineName = medicineName;
        this.requirePres = requirePres;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicine_show_add_cart_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final MedicinePackagePriorityItem listItem = listItems.get(position);
        final String mrp, packaging;

        if (listItem.getPackage().contains("INR")){
            String mrp_temp = listItem.getPackage().split("\\(")[1];
            mrp = mrp_temp.substring(0, mrp_temp.length()-5);


            Locale locale = new Locale("hi", "IN");
            final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
            holder.medicine_mrp.setText(nf.format(Float.valueOf(mrp.trim())));

            packaging = listItem.getPackage().split("\\(")[0];
            holder.package_contain.setText(packaging.trim());
        }else {
            mrp = "Price to be updated";
            packaging = listItem.getPackage();
            holder.medicine_mrp.setText("Price to be updated");
            holder.medicine_mrp.setTextSize(14);
            holder.package_contain.setText(listItem.getPackage());
        }



        if (listItem.getStorePriorityItems().size() == 0){
            holder.plus_minus_cart.setVisibility(View.GONE);
            holder.add_to_cart_btn.setVisibility(View.GONE);
            holder.change_seller.setVisibility(View.GONE);
            holder.seller_name.setVisibility(View.GONE);
            holder.seller_text_view.setVisibility(View.GONE);
            holder.not_available.setVisibility(View.VISIBLE);
        }else {


            Collections.sort(listItem.getStorePriorityItems(), new SortByPriority());

            max_priority_chemist = listItem.getStorePriorityItems().get(0).getChemistMobile();
            max_priority_chemist_store_name = listItem.getStorePriorityItems().get(0).getChemistName();
            max_priority_chemist_quantity = listItem.getStorePriorityItems().get(0).getQuantity();

            holder.seller_name.setText(max_priority_chemist_store_name);


            holder.change_seller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showSellerPopup(listItem.getStorePriorityItems());
                }
            });
            List<CartItem> cartItems = new CartDbHelper(context).getCart();
            for (CartItem cartItem : cartItems){
                if (cartItem.getMedicineName().equalsIgnoreCase(medicineName) && cartItem.getPackageContain().equalsIgnoreCase(packaging)){
                    holder.add_to_cart_btn.setVisibility(View.INVISIBLE);
                    holder.plus_minus_cart.setVisibility(View.VISIBLE);
                    holder.plus_minus_cart.setNumber(cartItem.getQuantity());
                }
            }
            holder.add_to_cart_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.add_to_cart_btn.setVisibility(View.INVISIBLE);
                    holder.plus_minus_cart.setVisibility(View.VISIBLE);
                    new CartDbHelper(context).addToCart(new CartItem(medicineName,
                            String.valueOf(1),
                            mrp, max_priority_chemist_store_name, max_priority_chemist,
                            requirePres, packaging));
                    Snackbar.make(v, "Added to Cart", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    if(mOnDataChangeListener != null){
                        mOnDataChangeListener.onDataChanged(listItems.size());
                    }

                }
            });
            holder.plus_minus_cart.setRange(1, Integer.valueOf(max_priority_chemist_quantity));

            holder.plus_minus_cart.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                    new CartDbHelper(context).updateCart(medicineName, String.valueOf(newValue), packaging);
                }
            });



            holder.change_seller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*showChangeSellerPopup()*/
                    ImageView closeSellerBtn;
                    RecyclerView seller_list;
                    final Dialog mDialog = new Dialog(context);
                    mDialog.setContentView(R.layout.change_seller_popup);
                    closeSellerBtn = mDialog.findViewById(R.id.closeSellerBtn);
                    seller_list = mDialog.findViewById(R.id.seller_list);
                    closeSellerBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    });
                    ChangeSellerAdapter adapter = new ChangeSellerAdapter(listItem.getStorePriorityItems(), context);
                    seller_list.setAdapter(adapter);
                    seller_list.setLayoutManager(new LinearLayoutManager(context));
                    seller_list.setHasFixedSize(true);

                    adapter.setOnDataClickListener(new OnDataClickListener() {
                        @Override
                        public void onDataClick(int position) {

                            max_priority_chemist = listItem.getStorePriorityItems().get(position).getChemistMobile();
                            max_priority_chemist_store_name = listItem.getStorePriorityItems().get(position).getChemistName();
                            max_priority_chemist_quantity = listItem.getStorePriorityItems().get(position).getQuantity();
                            holder.seller_name.setText(max_priority_chemist_store_name);
                            holder.plus_minus_cart.setRange(1, Integer.valueOf(max_priority_chemist_quantity));
                            notifyDataSetChanged();
                            mDialog.dismiss();

                        }
                    });

                    mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    mDialog.show();

                }
            });

        }




    }

    class SortByPriority implements Comparator<StorePriorityItem>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(StorePriorityItem a, StorePriorityItem b)
        {
            return b.getPriority() - a.getPriority();
        }
    }




    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView medicine_mrp, package_contain, seller_name, change_seller, seller_text_view;
        public ConstraintLayout add_to_cart_btn, not_available;
        public ElegantNumberButton plus_minus_cart;


        public ViewHolder(View itemview){
            super(itemview);
            medicine_mrp = itemview.findViewById(R.id.medicine_mrp);
            package_contain = itemview.findViewById(R.id.package_contain);
            seller_name = itemview.findViewById(R.id.seller_name);
            change_seller = itemview.findViewById(R.id.changeSellerBtn);
            add_to_cart_btn = itemview.findViewById(R.id.add_to_cart_btn);
            plus_minus_cart = itemview.findViewById(R.id.plus_minus_cart);
            seller_text_view = itemView.findViewById(R.id.seller_text_view);
            not_available = itemview.findViewById(R.id.not_available);
        }
    }
}
