package com.empti.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.empti.app.R;
import com.empti.app.adapter.ExpandableListViewAdapter;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FAQActivity extends Activity {
    ExpandableListView question_view;
    CustomProgressDialog pDialog;
    ArrayList<HashMap<String, String>> faqData;
    ImageView back_img;
    WebView faqWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_layout);
      //  init();

        faqWebView = findViewById(R.id.FAQwebView);
        back_img = findViewById(R.id.back_img);
        faqWebView.getSettings().setJavaScriptEnabled(true); // enable javascript
        faqWebView.getSettings().setDisplayZoomControls(false);
        faqWebView.getSettings().setBuiltInZoomControls(false);
        faqWebView.setWebChromeClient(new WebChromeClient());
        faqWebView.loadUrl("https://empti.me/faqs/");
        Log.i("webView","https://empti.me/faqs/");

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void init() {
        faqData = new ArrayList<>();
        pDialog = new CustomProgressDialog(this);
        question_view = findViewById(R.id.question_view);
        back_img = findViewById(R.id.back_img);
   //     pDialog.setUpDialog();
    //    getFAQ();
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void addAdapter(ArrayList<HashMap<String, String>> data) {
        question_view.setAdapter(new ExpandableListViewAdapter(FAQActivity.this, question_view, data));
    }
    private void getFAQ() {
        Call<ResponseBody> call = RetrofitClient.getRetrofitClient().getFAQ("Bearer" + " " + SharedPrefferenceHandler.retriveData(FAQActivity.this, Constant.BEARER_TOKEN));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pDialog.dismissDialog();
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        Log.e("FAQ", object.toString());
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            JSONArray data = object.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject dataArr = data.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("question", dataArr.getString("question"));
                                map.put("answer", dataArr.getString("answer"));
                                faqData.add(map);
                            }
                            addAdapter(faqData);
                        } else {
                        }
                    } catch (JSONException e) {
                        pDialog.dismissDialog();
                        e.printStackTrace();
                    } catch (IOException e) {
                        pDialog.dismissDialog();
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismissDialog();
                Toast.makeText(FAQActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }
}