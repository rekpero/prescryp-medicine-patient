package com.prescywallet.presdigi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prescywallet.presdigi.Model.PrescriptionGroupItem;
import com.prescywallet.presdigi.R;

import java.util.List;

public class PrescriptionGroupingAdapter extends RecyclerView.Adapter<PrescriptionGroupingAdapter.ViewHolder>{

    private List<PrescriptionGroupItem> listItems;
    private Context context;

    public PrescriptionGroupingAdapter(List<PrescriptionGroupItem> listItems, Context context){
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prescription_group_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PrescriptionGroupItem listItem = listItems.get(position);

        holder.groupTitleText.setText(listItem.getItemTitle());
        PrescriptionGroupListAdapter prescriptionGroupAdapter = new PrescriptionGroupListAdapter(listItem.getListItem(), context);
        holder.groupList.setAdapter(prescriptionGroupAdapter);
        holder.groupList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));


    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView groupTitleText;
        public RecyclerView groupList;

        public ViewHolder(View itemview){
            super(itemview);
            groupTitleText = itemview.findViewById(R.id.group_title_text);
            groupList = itemview.findViewById(R.id.group_list);

        }
    }
}
