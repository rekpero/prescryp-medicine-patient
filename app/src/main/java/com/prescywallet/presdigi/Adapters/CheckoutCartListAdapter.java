package com.prescywallet.presdigi.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.prescywallet.presdigi.Interface.OnDataChangeListener;
import com.prescywallet.presdigi.Model.CartItem;
import com.prescywallet.presdigi.R;
import com.prescywallet.presdigi.database.CartDbHelper;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CheckoutCartListAdapter extends RecyclerView.Adapter<CheckoutCartListAdapter.ViewHolder>{

    private List<CartItem> listItems;
    private Context context;
    private OnDataChangeListener mOnDataChangeListener;

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        mOnDataChangeListener = onDataChangeListener;
    }

    public CheckoutCartListAdapter(List<CartItem> listItems, Context context){
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checkout_cart_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final CartItem listItem = listItems.get(position);

        holder.medicineName.setText(listItem.getMedicineName());
        holder.package_contain.setText(listItem.getPackageContain());
        holder.seller_name.setText(listItem.getSellerName());
        Locale locale = new Locale("hi", "IN");
        final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        String totalMrp = nf.format(Integer.valueOf(listItem.getQuantity())*Float.valueOf(listItem.getPrice()));
        holder.medicine_mrp.setText(totalMrp);
        holder.plus_minus_cart.setNumber(listItem.getQuantity());
        holder.plus_minus_cart.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                new CartDbHelper(context).updateCart(listItem.getMedicineName(), String.valueOf(newValue), listItem.getPackageContain());
                String totalMrp = nf.format(newValue *Float.valueOf(listItem.getPrice()));
                holder.medicine_mrp.setText(totalMrp);
                if(mOnDataChangeListener != null){
                    mOnDataChangeListener.onDataChanged(listItems.size());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView medicineName, package_contain, seller_name, medicine_mrp;
        public ElegantNumberButton plus_minus_cart;

        public ViewHolder(View itemview){
            super(itemview);
            medicineName = itemview.findViewById(R.id.medicine_name_cart);
            package_contain = itemview.findViewById(R.id.package_contain);
            seller_name = itemview.findViewById(R.id.seller_name);
            medicine_mrp = itemview.findViewById(R.id.medicine_mrp);
            plus_minus_cart = itemview.findViewById(R.id.plus_minus_cart);
        }
    }
}
