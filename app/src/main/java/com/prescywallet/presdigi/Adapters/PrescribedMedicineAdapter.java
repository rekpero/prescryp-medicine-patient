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

import com.prescywallet.presdigi.MedicineActivity.MedicineDisplayItem;
import com.prescywallet.presdigi.R;
import com.prescywallet.presdigi.ShowMedicineDetailActivity;

import java.util.List;

public class PrescribedMedicineAdapter extends RecyclerView.Adapter<PrescribedMedicineAdapter.ViewHolder>{

    private List<MedicineDisplayItem> listItems;
    private Context context;

    public PrescribedMedicineAdapter(List<MedicineDisplayItem> listItems, Context context){
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicne_company_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MedicineDisplayItem listItem = listItems.get(position);

        holder.prescribed_medicine_name.setText(listItem.getMedicineName());
        holder.prescribed_company_name.setText(listItem.getCompanyName());
        holder.medicineCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowMedicineDetailActivity.class);
                intent.putExtra("medicine_name", listItem.getMedicineName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView prescribed_medicine_name, prescribed_company_name;
        CardView medicineCardView;

        public ViewHolder(View itemview){
            super(itemview);
            prescribed_medicine_name = itemview.findViewById(R.id.prescribed_medicine_name);
            prescribed_company_name = itemview.findViewById(R.id.prescribed_company_name);
            medicineCardView = itemView.findViewById(R.id.medicineCardView);
        }
    }
}
