package com.empti.app.utilities;

import android.app.Activity;
import android.os.Handler;
import com.tapadoo.alerter.Alerter;

public class AlerterMessage {

  public static void message(Activity context, String title,String errorMessage,int bgColor,int drawerIcon){
        Alerter.create(context)
                .setTitle(title)
                .setText(errorMessage)
                .setIcon(drawerIcon)
                .setBackgroundColorRes(bgColor)
                .show();
      Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
          @Override
          public void run() {
              Alerter.hide();
          }
      }, 2000);
    }
}
