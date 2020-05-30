package com.prescywallet.presdigi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prescywallet.presdigi.MedicineActivity;
import com.prescywallet.presdigi.Model.DeliveryAddressItem;
import com.prescywallet.presdigi.OrderSummaryActivity;
import com.prescywallet.presdigi.R;
import com.prescywallet.presdigi.Session.LocationSessionManager;

import java.util.List;

public class SavedAddressListAdapter extends RecyclerView.Adapter<SavedAddressListAdapter.ViewHolder>{

    private List<DeliveryAddressItem> listItems;
    private Context context;
    private String key;
    private LocationSessionManager sessionManager;

    public SavedAddressListAdapter(List<DeliveryAddressItem> listItems, Context context, String key){
        this.listItems = listItems;
        this.context = context;
        this.key = key;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DeliveryAddressItem listItem = listItems.get(position);
        sessionManager = new LocationSessionManager(context);

        holder.addressNick.setText(listItem.getDeliveryNickname());
        holder.complete_address_text_view.setText(listItem.getCompleteAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key.equalsIgnoreCase("Medicine")){
                    String deliver_to = listItem.getDeliveryNickname() + " (" + listItem.getLocality() + ")";
                    sessionManager.createLocationSession(deliver_to, listItem.getDeliveryLatitude(), listItem.getDeliveryLongitude());
                    Intent intent = new Intent(context, MedicineActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }else if (key.equalsIgnoreCase("Checkout")){
                    Intent intent = new Intent(context, OrderSummaryActivity.class);
                    intent.putExtra("DELIVERY_NICKNAME", listItem.getDeliveryNickname());
                    intent.putExtra("DELIVERY_LOCALITY", listItem.getLocality());
                    intent.putExtra("COMPLETE_DELIVERY_ADDRESS", listItem.getCompleteAddress());
                    intent.putExtra("DELIVERY_INSTRUCTION", listItem.getDeliveryInstruction());
                    intent.putExtra("DELIVERY_LATITUDE", listItem.getDeliveryLatitude());
                    intent.putExtra("DELIVERY_LONGITUDE", listItem.getDeliveryLongitude());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView addressNick;
        public TextView complete_address_text_view;

        public ViewHolder(View itemview){
            super(itemview);
            addressNick = itemview.findViewById(R.id.addressNick);
            complete_address_text_view = itemview.findViewById(R.id.complete_address_text_view);

        }
    }
}
