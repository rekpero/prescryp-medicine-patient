package com.prescywallet.presdigi.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

public class MissedAlarmScheduler {
    public void setAlarm(Context context, long alarmTime, Uri reminderTask, int NOTIFICATION_ID) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                MissedReminderAlarmService.getReminderPendingIntent(context, reminderTask, NOTIFICATION_ID);


        if (Build.VERSION.SDK_INT >= 23) {

            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        } else if (Build.VERSION.SDK_INT >= 19) {

            manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        } else {

            manager.set(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        }
    }

    public void setRepeatAlarm(Context context, long alarmTime, Uri reminderTask, long RepeatTime, int NOTIFICATION_ID) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                MissedReminderAlarmService.getReminderPendingIntent(context, reminderTask, NOTIFICATION_ID);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, RepeatTime, operation);


    }

    public void cancelAlarm(Context context, Uri reminderTask, int NOTIFICATION_ID) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                MissedReminderAlarmService.getReminderPendingIntent(context, reminderTask, NOTIFICATION_ID);

        manager.cancel(operation);

    }
}
