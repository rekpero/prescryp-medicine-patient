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

import com.prescywallet.presdigi.Model.MedicineObject;
import com.prescywallet.presdigi.R;
import com.prescywallet.presdigi.ShowMedicineDetailActivity;

import java.util.List;

public class AllMedicineDbAdapter extends RecyclerView.Adapter<AllMedicineDbAdapter.ViewHolder>{

    private List<MedicineObject> listItems;
    private Context context;

    public AllMedicineDbAdapter(List<MedicineObject> listItems, Context context){
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicine_search_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MedicineObject listItem = listItems.get(position);

        holder.testName.setText(listItem.getMedicineName());

        holder.medicineLayoutSearch.setOnClickListener(new View.OnClickListener() {
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

        public TextView testName;
        public LinearLayout medicineLayoutSearch;

        public ViewHolder(View itemview){
            super(itemview);
            testName = itemview.findViewById(R.id.testName);
            medicineLayoutSearch = itemview.findViewById(R.id.medicineLayoutSearch);
        }
    }
}
