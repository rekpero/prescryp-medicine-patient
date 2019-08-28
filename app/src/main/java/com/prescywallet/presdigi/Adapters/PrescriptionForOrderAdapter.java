package com.prescywallet.presdigi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prescywallet.presdigi.DigitalPrescriptionActivity;
import com.prescywallet.presdigi.Model.ListItem;
import com.prescywallet.presdigi.PrescriptionImageviewActivity;
import com.prescywallet.presdigi.R;

import java.util.List;

public class PrescriptionForOrderAdapter extends RecyclerView.Adapter<PrescriptionForOrderAdapter.ViewHolder>{

    private List<ListItem> listItems;
    private Context context;

    public PrescriptionForOrderAdapter(List<ListItem> listItems, Context context){
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attached_prescription_order_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);

        holder.textViewPresId.setText(listItem.getPrescriptionId());
        String status = "Status : " + listItem.getStatus();
        final String date = "Date : " + listItem.getDate();
        holder.textViewStatus.setText(status);
        holder.presLinearview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listItem.getStatus().equalsIgnoreCase("Processing")){
                    Intent i = new Intent(context, PrescriptionImageviewActivity.class);
                    i.putExtra("PrescriptionId", listItem.getPrescriptionId());
                    i.putExtra("Date", date);
                    i.putExtra("ImagePathUrl", listItem.getImagePath());
                    context.startActivity(i);
                }else {
                    Intent intent = new Intent(context, DigitalPrescriptionActivity.class);
                    intent.putExtra("PrescriptionId", listItem.getPrescriptionId());
                    intent.putExtra("Date", date);
                    intent.putExtra("Sender", "See Prescription");
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

        public TextView textViewPresId;
        public TextView textViewStatus;
        public LinearLayout presLinearview;

        public ViewHolder(View itemview){
            super(itemview);
            textViewPresId = (TextView) itemview.findViewById(R.id.presId);
            textViewStatus = (TextView) itemview.findViewById(R.id.presstatus);
            presLinearview = (LinearLayout) itemview.findViewById(R.id.presLinearView);

        }
    }
}
