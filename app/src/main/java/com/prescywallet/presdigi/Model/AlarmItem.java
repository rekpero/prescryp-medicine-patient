package com.prescywallet.presdigi.Model;

import android.net.Uri;

public class AlarmItem {
    Uri alarmUri;
    long timestamp;

    public AlarmItem(Uri alarmUri, long timestamp) {
        this.alarmUri = alarmUri;
        this.timestamp = timestamp;
    }

    public Uri getAlarmUri() {
        return alarmUri;
    }

    public void setAlarmUri(Uri alarmUri) {
        this.alarmUri = alarmUri;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
