package com.empti.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.empti.app.R;
import com.empti.app.utilities.CustomProgressDialog;

public class TermsAndConditionsActivity extends Activity {

    CustomProgressDialog pDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy_layout);
        pDialog = new CustomProgressDialog(this);
        pDialog.setUpDialog();
        WebView mywebview = (WebView) findViewById(R.id.webView);
        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.loadUrl("https://empti.me/terms-conditions/");
        mywebview.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                pDialog.dismissDialog();
            }
        });

    }

}