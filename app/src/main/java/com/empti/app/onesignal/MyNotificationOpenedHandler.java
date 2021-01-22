package com.empti.app.onesignal;

import android.content.Intent;
import android.util.Log;

import com.empti.app.activity.Global;
import com.empti.app.activity.MapsActivity;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class MyNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    // This fires when a notification is opened by tapping on it.
    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String activityToBeOpened;
        Log.e("NotificationHandler", "" + actionType.toString() + ">>" + result.toJSONObject().toString());
        Intent intent = new Intent(Global.getContext(), MapsActivity.class);
        intent.putExtra("notification", "true");
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        Global.getContext().startActivity(intent);

        if (data != null) {
           /* activityToBeOpened = data.optString("activityToBeOpened", null);
            if (activityToBeOpened != null && activityToBeOpened.equals("AnotherActivity")) {
                Log.i("OneSignalExample", "customkey set with value: " + activityToBeOpened);
                Intent intent = new Intent(MyApplication.getContext(), AnotherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getContext().startActivity(intent);
            } else if (activityToBeOpened != null && activityToBeOpened.equals("MainActivity")) {
                Log.i("OneSignalExample", "customkey set with value: " + activityToBeOpened);
                Intent intent = new Intent(MyApplication.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getContext().startActivity(intent);
            } else {*/
           /* try {
                String id = data.getString("id");
                String type = data.getString("type");
                Log.e("notification data",data.toString());
                if(SharedPreferencesHandler.getBooleanValues(MyApplication.getContext(), Constants.LOGIN_ENTRY)){
                    if(type.equalsIgnoreCase("admin_deck")){
                        Intent intent = new Intent(MyApplication.getContext(), MainActivity.class);
                        intent.putExtra("isNotification",true);
                        intent.putExtra("type",type);
                        intent.putExtra("id",id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        MyApplication.getContext().startActivity(intent);
                    }
                    else if(type.equalsIgnoreCase("web_notification")){
                        Intent intent = new Intent(MyApplication.getContext(), MainActivity.class);
                        intent.putExtra("isNotification",true);
                        intent.putExtra("type",type);
                        intent.putExtra("id",id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        MyApplication.getContext().startActivity(intent);
                    }

                }
                else{
                    Intent intent = new Intent(MyApplication.getContext(), Login_Signup.class);
                    intent.putExtra("isNotification",true);
                    intent.putExtra("id",id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getContext().startActivity(intent);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }*/

            // }

        }

        //If we send notification with action buttons we need to specidy the button id's and retrieve it to
        //do the necessary operation.
      /*  if (actionType == OSNotificationAction.ActionType.ActionTaken) {
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
            if (result.action.actionID.equals("ActionOne")) {
                Toast.makeText(MyApplication.getContext(), "ActionOne Button was pressed", Toast.LENGTH_LONG).show();
            } else if (result.action.actionID.equals("ActionTwo")) {
                Toast.makeText(MyApplication.getContext(), "ActionTwo Button was pressed", Toast.LENGTH_LONG).show();
            }
        }*/
    }

}
