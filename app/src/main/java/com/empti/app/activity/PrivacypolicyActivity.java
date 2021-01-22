package com.empti.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.empti.app.R;

public class PrivacypolicyActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy_layout);
        WebView mywebview = (WebView) findViewById(R.id.webView);
        mywebview.getSettings().setJavaScriptEnabled(true); // enable javascript
        mywebview.getSettings().setDisplayZoomControls(false);
        mywebview.getSettings().setBuiltInZoomControls(false);
        mywebview.setWebChromeClient(new WebChromeClient());
        // mywebview.loadUrl("https://www.javatpoint.com/");
        /*String data = "<html><body><h1>Hello, Javatpoint!</h1></body></html>";
        mywebview.loadData(data, "text/html", "UTF-8"); */
   //     mywebview.loadUrl("http://ec2-18-219-13-255.us-east-2.compute.amazonaws.com/takai/public/privacy_policy");
        mywebview.loadUrl("https://empti.me/empti_policy/");
    }
}
