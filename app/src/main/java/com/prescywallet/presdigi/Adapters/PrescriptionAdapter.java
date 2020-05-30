package com.prescywallet.presdigi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.prescywallet.presdigi.Model.ListItem;
import com.prescywallet.presdigi.PrescriptionImageviewActivity;
import com.prescywallet.presdigi.R;

import java.util.List;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.ViewHolder>{

    private List<ListItem> listItems;
    private Context context;
    private String key;

    public PrescriptionAdapter(List<ListItem> listItems, Context context, String key){
        this.listItems = listItems;
        this.context = context;
        this.key = key;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);

        holder.textViewPresId.setText(listItem.getPrescriptionId());
        holder.textViewDate.setText(listItem.getDate());
        holder.textViewStatus.setText(listItem.getStatus());
        holder.presLinearview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PrescriptionImageviewActivity.class);
                i.putExtra("PrescriptionId", listItem.getPrescriptionId());
                i.putExtra("Date", listItem.getDate());
                i.putExtra("ImagePathUrl", listItem.getImagePath());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewPresId;
        public TextView textViewDate;
        public TextView textViewStatus;
        public CardView cardView;
        public LinearLayout presLinearview;

        public ViewHolder(View itemview) {
            super(itemview);
            cardView = itemview.findViewById(R.id.presCard);
            textViewPresId = itemview.findViewById(R.id.presId);
            textViewDate = itemview.findViewById(R.id.presdate);
            textViewStatus = itemview.findViewById(R.id.presstatus);
            presLinearview = itemview.findViewById(R.id.presLinearView);

        }
    }
}
