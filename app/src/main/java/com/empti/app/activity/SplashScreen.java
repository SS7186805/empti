package com.empti.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.empti.app.R;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.SharedPrefferenceHandler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * splash screen handle and display
 * */

public class SplashScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        //call handle method
        handle();
        // Add code to print out the key hash
        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.empti.app",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {}
         catch (NoSuchAlgorithmException e) {}*/
    }

    //splash screen handle
    public void handle() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPrefferenceHandler.retriveData(SplashScreen.this, Constant.BEARER_TOKEN) == null) {
                    startActivity(new Intent(SplashScreen.this, WelcomeScreen.class));
                } else {
                    startActivity(new Intent(SplashScreen.this, MapsActivity.class));
                }
                finish();
            }
        }, 3000);
    }
}
