package com.prescywallet.presdigi.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.prescywallet.presdigi.Interface.OnDataChangeListener;
import com.prescywallet.presdigi.Model.PrescriptionImagePathItem;
import com.prescywallet.presdigi.R;
import com.prescywallet.presdigi.database.AttachedPrescriptionDBHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class AttachedPrescriptionBitmapAdapter extends RecyclerView.Adapter<AttachedPrescriptionBitmapAdapter.ViewHolder>{

    private List<PrescriptionImagePathItem> listItems;
    private Context context;
    private OnDataChangeListener mOnDataChangeListener;

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        mOnDataChangeListener = onDataChangeListener;
    }

    public AttachedPrescriptionBitmapAdapter(List<PrescriptionImagePathItem> listItems, Context context){
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attached_prescription_bitmap_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final PrescriptionImagePathItem listItem = listItems.get(position);

        File image = new File(listItem.getPrescriptionImagePath());

        Picasso.get()
                .load(image)
                .fit()
                .into(holder.attachedPrescriptionView);

        holder.deleteAttachedPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemoveConfirmationDialog(listItem, position);

            }
        });

    }

    private void showRemoveConfirmationDialog(final PrescriptionImagePathItem listItem, final int position) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to remove this prescription?");
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the reminder.
                new AttachedPrescriptionDBHelper(context).deletePrescriptionImagePath(listItem.getPrescriptionImagePath());
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

        public ImageView attachedPrescriptionView, deleteAttachedPrescription;


        public ViewHolder(View itemview){
            super(itemview);
            attachedPrescriptionView = itemView.findViewById(R.id.attachedPrescriptionView);
            deleteAttachedPrescription = itemview.findViewById(R.id.deleteAttachedPrescription);
        }
    }
}
