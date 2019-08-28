package com.prescywallet.presdigi.reminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
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

import java.util.Calendar;
import java.util.Random;

public class ReminderAlarmService extends IntentService {
    private static final String TAG = ReminderAlarmService.class.getSimpleName();

    private static final String NOTIFICATION_CHANNEL = "Medicine_Reminder";
    //This is a deep link intent, and needs the task stack
    public static PendingIntent getReminderPendingIntent(Context context, Uri uri) {
        Intent action = new Intent(context, ReminderAlarmService.class);
        action.setData(uri);
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public ReminderAlarmService() {
        super(TAG);
    }

    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    long mRepeatTime;


    @Override
    protected void onHandleIntent(Intent intent) {
        Random random = new Random();
        int NOTIFICATION_ID = random.nextInt(1000);

        System.out.print("NOTIFICATION_ID = " + NOTIFICATION_ID);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri uri = intent.getData();

        //Display a notification to view the task details
        Intent action = new Intent(this, ReminderActivity.class);
        action.setData(uri);
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent takeIntent = new Intent(this, TakeMedicineReceiver.class);
        takeIntent.setData(uri);
        takeIntent.putExtra("NOTIFICATION_ID", String.valueOf(NOTIFICATION_ID));
        PendingIntent pendingTakeIntent = PendingIntent.getBroadcast(this, 0, takeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent skipIntent = new Intent(this, SkipMedicineReceiver.class);
        skipIntent.setData(uri);
        skipIntent.putExtra("NOTIFICATION_ID", String.valueOf(NOTIFICATION_ID));
        PendingIntent pendingSkipIntent = PendingIntent.getBroadcast(this, 0, skipIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Grab the task description
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);


        Calendar tCalendar = Calendar.getInstance();
        Calendar mCalendar = Calendar.getInstance();
        int miMinute = tCalendar.get(Calendar.MINUTE);
        int mYear = tCalendar.get(Calendar.YEAR);
        int mMonth = tCalendar.get(Calendar.MONTH) + 1;
        int mDay = tCalendar.get(Calendar.DATE) + 1;
        tCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.MINUTE, miMinute+2);
        long selectedTimestamp =  tCalendar.getTimeInMillis();
        long missedSelectedTime = mCalendar.getTimeInMillis();
        String mDate = mDay + "/" + mMonth + "/" + mYear;




        String description = "";
        String title = "";
        String reminderStatus = null;
        String time = null, repeat = null, mRepeatType = null, mRepeatNo = null, mActive = null, medicine = null, dosage = null;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                medicine = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_MEDICINE);
                dosage = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_ROUTINE_DOSAGE);
                title = getString(R.string.take_medicines);
                if (dosage.contains("Breakfast")){
                    description = "Enjoying your breakfast! Now doctor have asked you to take " + dosage.substring(0, 8) + " of " + medicine + " " + dosage.substring(9);
                }else if (dosage.contains("Lunch")){
                    description = "Enjoying your lunch! Now doctor have asked you to take " + dosage.substring(0, 8) + " of " + medicine + " " + dosage.substring(9);
                }else if (dosage.contains("Dinner")){
                    description = "Enjoying your dinner! Now doctor have asked you to take " + dosage.substring(0, 8) + " of " + medicine + " " + dosage.substring(9);
                }


                time = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_TIME);
                repeat = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT);
                mRepeatType = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE);
                mRepeatNo = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO);
                mActive = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE);




            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Notification note = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(description))
                .addAction(R.drawable.hospital, "TAKE", pendingTakeIntent)
                .addAction(R.drawable.skip, "SKIP", pendingSkipIntent)
                .setContentText(description)
                .setSmallIcon(R.drawable.logo_24px)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_256px))
                .setContentIntent(operation)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setSound(Settings.System.DEFAULT_ALARM_ALERT_URI)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .build();

        assert manager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL,
                    "Your Reminder",
                    NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setSound(Settings.System.DEFAULT_ALARM_ALERT_URI, attributes);
        }
        manager.notify(NOTIFICATION_ID, note);


        ContentValues values = new ContentValues();

        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_MEDICINE, medicine);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_DATE, mDate);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TIME, time);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT, repeat);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO, mRepeatNo);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE, mRepeatType);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_ROUTINE_DOSAGE, dosage);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REMINDER_STATUS, "Missed Reminder");

        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE, mActive);

        int rowsAffected = getContentResolver().update(uri, values, null, null);

        if (rowsAffected != 0){
            assert mRepeatType != null;
            switch (mRepeatType) {
                case "Minute":
                    mRepeatTime = Integer.parseInt(mRepeatNo) * milMinute;
                    break;
                case "Hour":
                    mRepeatTime = Integer.parseInt(mRepeatNo) * milHour;
                    break;
                case "Day":
                    mRepeatTime = Integer.parseInt(mRepeatNo) * milDay;
                    break;
                case "Week":
                    mRepeatTime = Integer.parseInt(mRepeatNo) * milWeek;
                    break;
                case "Month":
                    mRepeatTime = Integer.parseInt(mRepeatNo) * milMonth;
                    break;
            }
            if (repeat.equalsIgnoreCase("Until I stop")){
                System.out.print(repeat);
                new AlarmScheduler().setRepeatAlarm(getApplicationContext(), selectedTimestamp , uri, mRepeatTime);
            }
            new MissedAlarmScheduler().setAlarm(getApplicationContext(), missedSelectedTime , uri, NOTIFICATION_ID);
        }


    }
}