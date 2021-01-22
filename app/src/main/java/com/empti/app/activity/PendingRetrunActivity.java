package com.empti.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.empti.app.R;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.ImageUtils;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingRetrunActivity extends Activity {
    ImageView container_img;
    TextView message;
    Button add_more_btn;
    CustomProgressDialog pDialog;
    String retrun = "Returned successfully";
    TextView price_message;
    String price = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_product_layout);
        init();
        setListners();
    }

    public void init() {
        price_message = findViewById(R.id.price_message);
        container_img = findViewById(R.id.container_img);
        message = findViewById(R.id.message);
        add_more_btn = findViewById(R.id.add_more_btn);
        pDialog = new CustomProgressDialog(this);
        message.setText(getIntent().getExtras().getString(Constant.name) + " Returned Successfully");
        ImageUtils.displayImageFromUrl(this, Constant.CONTAINER_IMAGE + getIntent().getExtras().getString(Constant.photo), container_img, null);
        price = getIntent().getExtras().getString(Constant.fee);
        price_message.setText("Deposit of " + price + " will be credit to your account in next 24 hours");
    }

    public void setListners() {
        add_more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.setUpDialog();
                hireReturnApi(getIntent().getExtras().getString(Constant.qrcode));
            }
        });
    }

    private void hireReturnApi(String qrcode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.qrcode, qrcode);
            Log.e("json object", jsonObject.toString());
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().hireReturnApi("Bearer" + " " + SharedPrefferenceHandler.retriveData(PendingRetrunActivity.this,
                    Constant.BEARER_TOKEN), requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    pDialog.dismissDialog();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            Log.e("detail data", object.toString());
                            if (object.getString("status").equalsIgnoreCase("1")) {
                                Toast.makeText(PendingRetrunActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent();
                                setResult(Activity.RESULT_OK, i);
                                finish();
                            } else {
                                Toast.makeText(PendingRetrunActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pDialog.dismissDialog();
                    //Toast.makeText(ContainerAllActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            pDialog.dismissDialog();
            e.printStackTrace();
        }
    }
}
