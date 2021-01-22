package com.empti.app.activity;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.empti.app.model.OutletData;
import com.empti.app.onesignal.MyNotificationOpenedHandler;
import com.empti.app.onesignal.MyNotificationReceivedHandler;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.onesignal.OneSignal;

import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;

public class Global extends Application {
    public ArrayList<OutletData> modelArr = new ArrayList<>();
    public ArrayList<OutletData> getModelArr() {
        return modelArr;
    }
    public void setModelArr(ArrayList<OutletData> modelArr) {
        this.modelArr = modelArr;
    }
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        context = getApplicationContext();
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler())
                .setNotificationReceivedHandler(new MyNotificationReceivedHandler())
                .init();
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.d("debug", "User:" + userId);
                if (registrationId != null)
                    SharedPrefferenceHandler.storeData(context, Constant.UPDATE_TOKEN, userId);
                Log.d("debug", "registrationId:" + registrationId);
            }
        });
    }
    public static Context getContext() {
        return context;
    }
}