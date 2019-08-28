package com.prescywallet.presdigi.reminder;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.prescywallet.presdigi.database.AlarmReminderContract;

import static android.content.Context.NOTIFICATION_SERVICE;

public class TakeMedicineReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Uri uri = intent.getData();
        String NOTIFICATION_ID_STRING = intent.getStringExtra("NOTIFICATION_ID");
        int NOTIFICATION_ID = Integer.valueOf(NOTIFICATION_ID_STRING);
        //Grab the task description
        assert uri != null;
        //Grab the task description
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {

                String medicine = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_MEDICINE);
                String dosage = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_ROUTINE_DOSAGE);
                String date = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_DATE);
                String time = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_TIME);
                String repeat = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT);
                String mRepeatType = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE);
                String mRepeatNo = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO);
                String mActive = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE);

                ContentValues values = new ContentValues();
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_MEDICINE, medicine);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_DATE, date);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TIME, time);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT, repeat);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO, mRepeatNo);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE, mRepeatType);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_ROUTINE_DOSAGE, dosage);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REMINDER_STATUS, "Medicine Taken");

                int rowsAffected = context.getContentResolver().update(uri, values, null, null);
                if (rowsAffected != 0){
                    NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                    assert manager != null;
                    manager.cancel(NOTIFICATION_ID);
                }

            }
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }



    }
}
