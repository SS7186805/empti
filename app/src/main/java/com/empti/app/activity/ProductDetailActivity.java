package com.empti.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.text.DecimalFormat;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends Activity {
    ImageView back_img, container_img;
    TextView container_txt;
    EditText enter_deposit, enter_fee;
    TextView cost_view;
    Button btConfirm;
    float d = 0f, f = 0f, t = 0f;
    CustomProgressDialog pDialog;
    String container_id = "", outlet_container_id, qrcode = "";
    boolean isFromScan = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_layout);
        init();
        setListners();
    }

    public void init() {
        pDialog = new CustomProgressDialog(this);
        enter_deposit = findViewById(R.id.enter_deposit);
        enter_fee = findViewById(R.id.fee_deposit);
        back_img = findViewById(R.id.back_img);
        container_img = findViewById(R.id.container_img);
        container_txt = findViewById(R.id.container_name);
        cost_view = findViewById(R.id.cost_view);
        btConfirm = findViewById(R.id.btConfirm);
        getData();
    }

    public void getData() {
        container_txt.setText(getIntent().getExtras().getString(Constant.name));
        ImageUtils.displayImageFromUrl(this, Constant.CONTAINER_IMAGE + getIntent().getExtras().getString(Constant.photo), container_img, null);
        if (getIntent().getExtras().getString(Constant.deposit) != null) {
            enter_deposit.setText(getIntent().getExtras().getString(Constant.deposit));
            enter_fee.setText(getIntent().getExtras().getString(Constant.fee));
            container_id = getIntent().getExtras().getString(Constant.container_id);
            outlet_container_id = getIntent().getExtras().getString(Constant.outlet_container_id);
            qrcode = getIntent().getExtras().getString(Constant.qrcode);
            isFromScan = false;
        }
        setTotalCost();
    }

    public void setListners() {
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (enter_deposit.getText().toString().equalsIgnoreCase("")) {
//                    Toast.makeText(ProductDetailActivity.this, "Please enter deposit", Toast.LENGTH_SHORT).show();
//                } else if (enter_fee.getText().toString().equalsIgnoreCase("")) {
//                    Toast.makeText(ProductDetailActivity.this, "Please enter fee", Toast.LENGTH_SHORT).show();
//                } else {
                    if (isFromScan) {
                        pDialog.setUpDialog();
                        addContaineData(getIntent().getExtras().getString(Constant.id), getIntent().getExtras().getString(Constant.qrcode),"5", "5");
                    } else {
                        pDialog.setUpDialog();
                        editData(container_id, outlet_container_id, qrcode, "5", "5");
//                    }
                }
            }
        });
        enter_fee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setTotalCost();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        enter_deposit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setTotalCost();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void setTotalCost() {
        if (enter_deposit.getText().toString().equalsIgnoreCase("")) {
            d = 0;
        } else {
            d = Float.parseFloat(enter_deposit.getText().toString());
        }
        if (enter_fee.getText().toString().equalsIgnoreCase("")) {
            f = 0;
        } else {
            f = Float.parseFloat(enter_fee.getText().toString());
        }
        t = d + f;
        DecimalFormat df = new DecimalFormat("0.00");
        if (t == 0) {
            cost_view.setText("");
        } else {
            cost_view.setText("CHF " + df.format(t));
        }
    }

    private void addContaineData(String id, String qrcode, String deposit, String fee) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.container_id, id);
            jsonObject.put(Constant.qrcode, qrcode);
            jsonObject.put(Constant.deposit, deposit);
            jsonObject.put(Constant.fee, fee);
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().addContainers("Bearer" + " " + SharedPrefferenceHandler.retriveData(ProductDetailActivity.this, Constant.BEARER_TOKEN), requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    pDialog.dismissDialog();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            Log.e("result", object.toString());
                            if (object.getString("status").equalsIgnoreCase("1")) {
                                Intent i = new Intent(ProductDetailActivity.this, SuccessProductActivity.class);
                                i.putExtra(Constant.photo, getIntent().getExtras().getString(Constant.photo));
                                i.putExtra(Constant.name, getIntent().getExtras().getString(Constant.name));
                                startActivityForResult(i, 101);
                            } else {
                                Toast.makeText(ProductDetailActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ProductDetailActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            pDialog.dismissDialog();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Intent i = new Intent();
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        }
    }

    private void editData(String container_id, String outlet_container_id, String qrcode, String deposit, String fee) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.container_id, container_id);
            jsonObject.put(Constant.outlet_container_id, outlet_container_id);
            jsonObject.put(Constant.qrcode, qrcode);
            jsonObject.put(Constant.deposit, deposit);
            jsonObject.put(Constant.fee, fee);
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().editContainers("Bearer" + " " + SharedPrefferenceHandler.retriveData(ProductDetailActivity.this, Constant.BEARER_TOKEN), requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    pDialog.dismissDialog();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            Log.e("result", object.toString());
                            if (object.getString("status").equalsIgnoreCase("1")) {
                                Toast.makeText(ProductDetailActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent();
                                setResult(Activity.RESULT_OK, i);
                                finish();
                            } else {
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
                    Toast.makeText(ProductDetailActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            pDialog.dismissDialog();
            e.printStackTrace();
        }
    }
}
