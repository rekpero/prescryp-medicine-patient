package com.prescywallet.presdigi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prescywallet.presdigi.DigitalPrescriptionActivity;
import com.prescywallet.presdigi.Model.DigitizedPrescriptionItem;
import com.prescywallet.presdigi.R;

import java.util.List;

public class PrescriptionGroupListAdapter extends RecyclerView.Adapter<PrescriptionGroupListAdapter.ViewHolder>{

    private List<DigitizedPrescriptionItem> listItems;
    private Context context;

    public PrescriptionGroupListAdapter(List<DigitizedPrescriptionItem> listItems, Context context){
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pres_group_grid_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DigitizedPrescriptionItem listItem = listItems.get(position);

        holder.gridPresId.setText(listItem.getPrescriptionId());
        holder.gridPresDate.setText(listItem.getDateOfCreation());
        holder.presCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DigitalPrescriptionActivity.class);
                intent.putExtra("PrescriptionId", listItem.getPrescriptionId());
                intent.putExtra("Date", listItem.getDateOfCreation());
                intent.putExtra("Sender", "See Prescription");
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView gridPresId;
        public TextView gridPresDate;
        public CardView presCard;

        public ViewHolder(View itemview){
            super(itemview);
            gridPresId = itemview.findViewById(R.id.gridPresId);
            gridPresDate = itemview.findViewById(R.id.gridPresDate);
            presCard = itemview.findViewById(R.id.presCard);

        }
    }
}
