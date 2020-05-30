package com.prescywallet.presdigi.Adapters;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.prescywallet.presdigi.Abstract.RecyclerViewCursorAdapter;
import com.prescywallet.presdigi.R;
import com.prescywallet.presdigi.database.AlarmReminderContract;
import com.prescywallet.presdigi.reminder.AlarmScheduler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmListAdapter extends RecyclerViewCursorAdapter<AlarmListAdapter.ViewHolder> {
    private Context context;

    public AlarmListAdapter(Context context, Cursor c) {
        super(c);
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(final ViewHolder holder, final Cursor cursor) {
        int medicineColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_MEDICINE);
        int dateColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_DATE);
        final int timeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TIME);
        int dosageColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_ROUTINE_DOSAGE);
        final int reminderStatusColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REMINDER_STATUS);


        String date = cursor.getString(dateColumnIndex);
        final String routine_date = getRoutineDate(date);
        String dosage = cursor.getString(dosageColumnIndex);
        String medicine = cursor.getString(medicineColumnIndex);
        final String description = "Take " + dosage.substring(0, 8) + " of " + medicine + " " + dosage.substring(9);

        holder.reminderDesc.setText(description);
        holder.routineDate.setText(routine_date);
        holder.routineTime.setText(cursor.getString(timeColumnIndex));
        holder.reminderStatus.setText(cursor.getString(reminderStatusColumnIndex));
        holder.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri currentVehicleUri = ContentUris.withAppendedId(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, holder.getItemId());

                showDeleteConfirmationDialog(currentVehicleUri);
            }
        });

        holder.aCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView timeImage, closeBtn;
                TextView timeText, dayText, medicineDesc, medicineStatus;
                ConstraintLayout popupWindow;
                final Switch deleteSwitch;
                final Dialog mDialog = new Dialog(context);
                mDialog.setContentView(R.layout.reminder_item_popup);
                timeImage = mDialog.findViewById(R.id.timeImage);
                timeText = mDialog.findViewById(R.id.timeText);
                dayText = mDialog.findViewById(R.id.dayText);
                medicineDesc = mDialog.findViewById(R.id.medicineDesc);
                medicineStatus = mDialog.findViewById(R.id.medicineStatus);
                deleteSwitch = mDialog.findViewById(R.id.deleteSwitch);
                closeBtn = mDialog.findViewById(R.id.closeBtn);

                if (description.contains("Dinner")){
                    timeImage.setImageResource(R.drawable.night_colour);
                }else if (description.contains("Lunch")){
                    timeImage.setImageResource(R.drawable.sun);
                }else if (description.contains("Breakfast")){
                    timeImage.setImageResource(R.drawable.sunset_colour);
                }

                timeText.setText(cursor.getString(timeColumnIndex));
                dayText.setText(routine_date);
                medicineDesc.setText(description);
                medicineStatus.setText(cursor.getString(reminderStatusColumnIndex));

                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
                deleteSwitch.setChecked(true);
                deleteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked){
                            Uri currentVehicleUri = ContentUris.withAppendedId(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, holder.getItemId());

                            showDeleteConfirmationDialog(currentVehicleUri, deleteSwitch, mDialog);
                        }
                    }
                });

                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.show();

            }
        });


    }

    private void showDeleteConfirmationDialog(final Uri currentUri, final Switch deleteSwitch, final Dialog mDialog) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the reminder.
                mDialog.dismiss();
                deleteReminder(currentUri);

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the reminder.
                if (dialog != null) {
                    dialog.dismiss();
                    deleteSwitch.setChecked(true);
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showDeleteConfirmationDialog(final Uri currentUri) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the reminder.
                deleteReminder(currentUri);

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
    private void deleteReminder(Uri mCurrentReminderUri) {
        // Only perform the delete if this is an existing reminder.
        if (mCurrentReminderUri != null) {
            // Call the ContentResolver to delete the reminder at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentreminderUri
            // content URI already identifies the reminder that we want.
            int rowsDeleted = context.getContentResolver().delete(mCurrentReminderUri, null, null);
            new AlarmScheduler().cancelAlarm(context, mCurrentReminderUri);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(context, context.getString(R.string.editor_delete_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(context, context.getString(R.string.editor_delete_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

    }


    private String getRoutineDate(String date){
        String r_date = "";
        Calendar mCalendar = Calendar.getInstance();
        int mYear = mCalendar.get(Calendar.YEAR);
        int mMonth = mCalendar.get(Calendar.MONTH) + 1;
        int mDay = mCalendar.get(Calendar.DATE);
        String tDate = mDay + "/" + mMonth + "/" + mYear;
        String toDate = (mDay + 1) + "/" + mMonth + "/" + mYear;
        if (tDate.equalsIgnoreCase(date)){
            r_date = "Today";
        }else if (toDate.equalsIgnoreCase(date)){
            r_date = "Tomorrow";
        }else{
            r_date = getDateFormatChange(date);
        }
        return r_date;
    }

    private String getDateFormatChange(String date){
        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String outputDateStr = "";
        try {
            Date new_date = inputFormat.parse(date);
            outputDateStr = outputFormat.format(new_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateStr;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView reminderDesc;
        public TextView routineDate;
        public TextView routineTime;
        public TextView reminderStatus;
        public CardView aCard;
        public LinearLayout deleteLayout;


        public ViewHolder(View itemview){
            super(itemview);
            reminderDesc = itemview.findViewById(R.id.reminder_desc);
            routineDate = itemview.findViewById(R.id.reminder_date);
            routineTime = itemview.findViewById(R.id.reminder_time);
            reminderStatus = itemview.findViewById(R.id.reminder_status);
            aCard = itemview.findViewById(R.id.alarm_card);
            deleteLayout = itemview.findViewById(R.id.deleteLayout);

        }
    }
}
