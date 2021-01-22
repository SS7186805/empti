package com.empti.app.onesignal;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class MyNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
    @Override
    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;
        String customKey;

        if (data != null) {
            //While sending a Push notification from OneSignal dashboard
            // you can send an addtional data named "customkey" and retrieve the value of it and do necessary operation
       /*     try {
                String id = data.getString("id");
                Log.e("notification data",data.toString());
                Intent intent = new Intent(MyApplication.getContext(), MainActivity.class);
                intent.putExtra("isNotification",true);
                intent.putExtra("id",id);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getContext().startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }
    }
}
