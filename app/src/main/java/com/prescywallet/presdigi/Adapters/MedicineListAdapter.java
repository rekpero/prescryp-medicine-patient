package com.prescywallet.presdigi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prescywallet.presdigi.Model.MedicineItem;
import com.prescywallet.presdigi.R;
import com.prescywallet.presdigi.SetReminderActivity;

import java.util.List;

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.ViewHolder>{

    private List<MedicineItem> listItems;
    private Context context;

    public MedicineListAdapter(List<MedicineItem> listItems, Context context){
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicine_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MedicineItem listItem = listItems.get(position);

        holder.medicineName.setText(listItem.getMedicineName());

        holder.medicineLinearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, SetReminderActivity.class);
                i.putExtra("MedicineName", listItem.getMedicineName());
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView medicineName;
        public LinearLayout medicineLinearView;

        public ViewHolder(View itemview){
            super(itemview);
            medicineName = itemview.findViewById(R.id.medicinesName);
            medicineLinearView = itemview.findViewById(R.id.medicineLinearView);

        }
    }
}
