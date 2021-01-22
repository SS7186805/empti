package com.empti.app.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class SharedPrefferenceHandler {
    private static SharedPreferences sharedPreference;
    private static SharedPreferences.Editor editor;

    public static void storeData(Context context, String key, String value) {
        sharedPreference = context.getSharedPreferences("Info", Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String retriveData(Context context, String Key) {
        sharedPreference = context.getSharedPreferences("Info", Context.MODE_PRIVATE);
        return sharedPreference.getString(Key, null);
    }

    public static void removeAll(Context context) {
        sharedPreference = context.getSharedPreferences("Info", Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        editor.clear();
        editor.commit();
    }

    public static void removeKey(Context context, String Key) {
        sharedPreference = context.getSharedPreferences("Info", Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        editor.remove(Key);
        editor.commit();
    }

    public static void storeAndParseJsonData(Context context, JSONObject object) {
        sharedPreference = context.getSharedPreferences("Info", Context.MODE_PRIVATE);
        Log.i("jsonobject", "storeAndParseJsonData: " + object.toString());
        editor = sharedPreference.edit();
        try {
            editor.putString(Constant.USER_ID, object.getString("id"));
            editor.putString(Constant.EMAIL_ID, object.getString("email"));
            editor.putString(Constant.BEARER_TOKEN, object.getString("bearer_token"));
            editor.putString(Constant.FIRST_NAME, object.getString("first_name"));
            editor.putString(Constant.LAST_NAME, object.getString("last_name"));
            editor.putString(Constant.COUNTRY_CODE, object.getString("country_code"));
            editor.putString(Constant.PHONE_NUMBER, object.getString("phone_no"));
            editor.putString(Constant.DATE_OF_BIRTH, object.getString("date_of_birth"));
            editor.putString(Constant.CITY, object.getString("city"));
            editor.putString(Constant.GENDER, object.getString("gender"));
            editor.putString(Constant.IMAGE, object.getString("image"));
            editor.putString(Constant.ROLE, object.getString("role"));
            editor.putString(Constant.SHOP_NAME, object.getString("shop_name"));
            editor.putString(Constant.SHOP_LOGO, object.getString("shop_logo"));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("storeAndParseJsonData: ", e.getMessage());
        }
    }

    public static void editprofile(Context context, JSONObject object) {
        sharedPreference = context.getSharedPreferences("Info", Context.MODE_PRIVATE);
        Log.e("jsonobject", "storeAndParseJsonData: " + object.toString());
        editor = sharedPreference.edit();
        try {
            editor.putString(Constant.USER_ID, object.getString("id"));
            editor.putString(Constant.EMAIL_ID, object.getString("email"));
            editor.putString(Constant.FIRST_NAME, object.getString("first_name"));
            editor.putString(Constant.LAST_NAME, object.getString("last_name"));
            editor.putString(Constant.COUNTRY_CODE, object.getString("country_code"));
            editor.putString(Constant.PHONE_NUMBER, object.getString("phone_no"));
            editor.putString(Constant.DATE_OF_BIRTH, object.getString("date_of_birth"));
            editor.putString(Constant.CITY, object.getString("city"));
            editor.putString(Constant.GENDER, object.getString("gender"));
            editor.putString(Constant.IMAGE, object.getString("image"));
            editor.putString(Constant.ROLE, object.getString("role"));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("storeAndParseJsonData: ", e.getMessage());
        }
    }

    /*To Store Fcm Device ID*/
    public static void storeFcmDeviceId(Context context, String val) {
        sharedPreference = context.getSharedPreferences("FCM", Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        editor.putString("DEVICE", val);
        editor.commit();
    }

    public static String retrieveFcmDeviceId(Context context) {
        sharedPreference = context.getSharedPreferences("FCM", Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        return sharedPreference.getString("DEVICE", null);
    }

    public static void storeMessageCount(Context context, String key, int value) {
        sharedPreference = context.getSharedPreferences("Info", Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int retrieveMessageCount(Context context, String key) {
        sharedPreference = context.getSharedPreferences("Info", Context.MODE_PRIVATE);
        return sharedPreference.getInt(key, 0);
    }

    public static void removeMessageCount(Context context, String key) {
        sharedPreference = context.getSharedPreferences("Info", Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void storeDatalanguage(Context context, String key, String value) {
        sharedPreference = context.getSharedPreferences(Constant.KEY, Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String retrievedatalanguage(Context context, String Key) {
        sharedPreference = context.getSharedPreferences(Constant.KEY, Context.MODE_PRIVATE);
        return sharedPreference.getString(Key, null);
    }

    public static void storeScanCount(Context context, String key, int value) {
        sharedPreference = context.getSharedPreferences("Info", Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int retrieveScanCount(Context context, String key) {
        sharedPreference = context.getSharedPreferences("Info", Context.MODE_PRIVATE);
        return sharedPreference.getInt(key, 0);
    }
}