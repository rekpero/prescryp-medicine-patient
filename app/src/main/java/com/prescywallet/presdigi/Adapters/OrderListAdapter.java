package com.prescywallet.presdigi.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prescywallet.presdigi.Model.CartItem;
import com.prescywallet.presdigi.Model.TrackOrderItem;
import com.prescywallet.presdigi.R;
import com.prescywallet.presdigi.Misc.RunTimePermission;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private List<TrackOrderItem> listItems;
    private Context context;
    private Activity activity;
    RunTimePermission photoRunTimePermission;

    public OrderListAdapter(List<TrackOrderItem> listItems, Context context, Activity activity) {
        this.listItems = listItems;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_status_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final TrackOrderItem listItem = listItems.get(position);

        holder.medicineName.setText(listItem.getMedicineName());
        holder.quantity.setText(listItem.getQuantity());
        holder.package_contain.setText(listItem.getPackageContain());
        holder.seller_name.setText(listItem.getSellerName());
        Locale locale = new Locale("hi", "IN");
        final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        String totalMrp = nf.format(Integer.valueOf(listItem.getQuantity()) * Float.valueOf(listItem.getPrice()));
        holder.medicine_mrp.setText(totalMrp);

        /*holder.call_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + listItem.getSellerMobileNumber()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    context.startActivity(intent);
                } else {
                    photoRunTimePermission = new RunTimePermission(activity);
                    photoRunTimePermission.requestPermission(new String[]{
                            Manifest.permission.CALL_PHONE
                    }, new RunTimePermission.RunTimePermissionListener() {

                        @Override
                        public void permissionGranted() {
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                context.startActivity(intent);
                            }

                        }

                        @Override
                        public void permissionDenied() {
                        }
                    });
                }

            }
        });*/

        if (listItem.getOrderStatus().equalsIgnoreCase("Dispatched")){
            holder.dispatched_order.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView medicineName, package_contain, seller_name, medicine_mrp, quantity;
        public ConstraintLayout dispatched_order;


        public ViewHolder(View itemview){
            super(itemview);
            medicineName = itemview.findViewById(R.id.medicine_name_order);
            package_contain = itemview.findViewById(R.id.package_contain);
            seller_name = itemview.findViewById(R.id.seller_name);
            medicine_mrp = itemview.findViewById(R.id.medicine_mrp);
            dispatched_order = itemview.findViewById(R.id.dispatched_order);
            quantity = itemview.findViewById(R.id.quantity);
        }
    }
}
