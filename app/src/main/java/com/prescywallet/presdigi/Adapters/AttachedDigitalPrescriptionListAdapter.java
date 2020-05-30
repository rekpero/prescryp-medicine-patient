package com.prescywallet.presdigi.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.prescywallet.presdigi.Interface.OnDataChangeListener;
import com.prescywallet.presdigi.Model.AttachedDigitalPrescriptionItem;
import com.prescywallet.presdigi.R;
import com.prescywallet.presdigi.database.AttachedDigitalPrescriptionDBHelper;

import java.util.List;

public class AttachedDigitalPrescriptionListAdapter extends RecyclerView.Adapter<AttachedDigitalPrescriptionListAdapter.ViewHolder>{

    private List<AttachedDigitalPrescriptionItem> listItems;
    private Context context;
    private OnDataChangeListener mOnDataChangeListener;

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        mOnDataChangeListener = onDataChangeListener;
    }

    public AttachedDigitalPrescriptionListAdapter(List<AttachedDigitalPrescriptionItem> listItems, Context context){
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attached_digital_prescription_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final AttachedDigitalPrescriptionItem listItem = listItems.get(position);

        holder.gridPresId.setText(listItem.getPrescriptionId());
        holder.gridPresDate.setText(listItem.getDate());

        holder.deletePres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemoveConfirmationDialog(listItem, position);

            }
        });

    }
    private void showRemoveConfirmationDialog(final AttachedDigitalPrescriptionItem listItem, final int position) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to remove this prescription?");
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the reminder.
                new AttachedDigitalPrescriptionDBHelper(context).deleteDigitalPrescription(listItem.getPrescriptionId());
                listItems.remove(position);
                notifyDataSetChanged();
                if(mOnDataChangeListener != null){
                    mOnDataChangeListener.onDataChanged(listItems.size());
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the reminder.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView gridPresId;
        public TextView gridPresDate;
        public ImageView deletePres;

        public ViewHolder(View itemview){
            super(itemview);
            gridPresId = itemview.findViewById(R.id.gridPresIdA);
            gridPresDate = itemview.findViewById(R.id.gridPresDateA);
            deletePres = itemview.findViewById(R.id.deletePres);

        }
    }
}
