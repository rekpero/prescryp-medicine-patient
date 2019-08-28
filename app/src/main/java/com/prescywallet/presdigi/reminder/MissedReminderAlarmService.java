package com.prescywallet.presdigi.reminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.prescywallet.presdigi.R;
import com.prescywallet.presdigi.ReminderActivity;
import com.prescywallet.presdigi.database.AlarmReminderContract;

public class MissedReminderAlarmService extends IntentService {
    private static final String TAG = MissedReminderAlarmService.class.getSimpleName();

    private static final String CHANNEL_ID = "MEDIC";
    String GROUP_KEY_REMINDER_MEDICINE = "com.prescywallet.presdigi.REMINDER_MEDICINE";

    //This is a deep link intent, and needs the task stack
    public static PendingIntent getReminderPendingIntent(Context context, Uri uri, int NOTIFICATION_ID) {
        Intent action = new Intent(context, MissedReminderAlarmService.class);
        action.setData(uri);
        action.putExtra("NOTIFICATION_ID", String.valueOf(NOTIFICATION_ID));
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public MissedReminderAlarmService() {
        super(TAG);
    }



    @Override
    protected void onHandleIntent(Intent intent) {

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Uri uri = intent.getData();
        String NOTIFICATION_ID = intent.getStringExtra("NOTIFICATION_ID");
        //Display a notification to view the task details
        Intent action = new Intent(this, ReminderActivity.class);
        action.setData(uri);
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent takeIntent = new Intent(this, TakeMedicineReceiver.class);
        takeIntent.setData(uri);
        takeIntent.putExtra("NOTIFICATION_ID", NOTIFICATION_ID);
        PendingIntent pendingTakeIntent = PendingIntent.getBroadcast(this, 0, takeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent skipIntent = new Intent(this, SkipMedicineReceiver.class);
        skipIntent.setData(uri);
        skipIntent.putExtra("NOTIFICATION_ID", NOTIFICATION_ID);
        PendingIntent pendingSkipIntent = PendingIntent.getBroadcast(this, 0, skipIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Grab the task description
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);





        String description = "";
        String title = "";
        String reminderStatus = null;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                String medicine = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_MEDICINE);
                reminderStatus = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_REMINDER_STATUS);
                if (reminderStatus.equalsIgnoreCase("Missed Reminder")){
                    title = "Missed Reminder";
                    description = "Did you missed to take " + medicine + " ?";
                }



            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        assert reminderStatus != null;
        if (reminderStatus.equalsIgnoreCase("Missed Reminder")){
            Notification note = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(description))
                    .addAction(R.drawable.hospital, "TAKE", pendingTakeIntent) // #0
                    .addAction(R.drawable.skip, "SKIP", pendingSkipIntent)  // #1
                    // Apply the media style template
                    .setContentText(description)
                    .setSmallIcon(R.drawable.logo_32px)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_256px))
                    .setContentIntent(operation)
                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setAutoCancel(true)
                    .build();

            assert manager != null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AudioAttributes attributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        "Missed Reminder",
                        NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
                channel.enableVibration(true);
                channel.enableLights(true);
                channel.setSound(Settings.System.DEFAULT_ALARM_ALERT_URI, attributes);
            }
            manager.notify(Integer.valueOf(NOTIFICATION_ID), note);
        }


    }
}