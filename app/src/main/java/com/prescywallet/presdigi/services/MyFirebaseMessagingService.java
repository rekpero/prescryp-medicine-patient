package com.prescywallet.presdigi.services;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.prescywallet.presdigi.Misc.MyNotificationManager;
import com.prescywallet.presdigi.TrackOrderActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }


    private void sendPushNotification(JSONObject json) {

        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            String order_number = data.getString("order_number");

            String notify_message = message + " The order number is " + order_number;

            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            //creating an intent for the notification
            Intent intent = new Intent(getApplicationContext(), TrackOrderActivity.class);
            intent.putExtra("Order_Number", order_number);
            intent.putExtra("Sender_Key", "From_Order_Summary");

            mNotificationManager.showSmallNotification(title, notify_message, intent);

            Intent i = new Intent("android.intent.action.MAIN");
            i.putExtra("Order_Number", order_number);
            if (title.contains("Dispatched")){
                i.putExtra("Order_Status", "Dispatched");
            }else if (title.contains("Delivered")){
                i.putExtra("Order_Status", "Delivered");
            }
            this.sendBroadcast(i);


        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


}
