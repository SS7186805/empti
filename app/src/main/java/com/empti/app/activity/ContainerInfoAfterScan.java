package com.empti.app.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.empti.app.R;
import com.empti.app.model.NewBadgeModel;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.ImageUtils;
import com.empti.app.utilities.NotifyService;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContainerInfoAfterScan extends Activity {
    Dialog mDialog;
    int scanCount = 0;
    ImageView close_view, container_img, shop_image, back_img;
    TextView container_name, enter_deposit, fee_deposit, cost_view, shop_name;
    Button btConfirm;
    CustomProgressDialog pDialog;
    String qrCodeNumber, shopname, shopLogo , Return;
    ArrayList<NewBadgeModel.NewBadge> newBadgeList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_info_after_scan);

        if (getIntent() != null) {
            shopname = getIntent().getExtras().getString(Constant.shop_name);
            Return = getIntent().getExtras().getString("return")!=null?"return":"null";
            shopLogo =Constant.CONTAINER_IMAGE + getIntent().getExtras().getString(Constant.photo);
            Log.i("shopLogo==>>",shopLogo);
            qrCodeNumber = getIntent().getExtras().getString(Constant.qrcode);
        }
       /* scanCount = SharedPrefferenceHandler.retrieveScanCount(ContainerInfoAfterScan.this, "scan");
        if (scanCount == 10) {
            handle();
        } else {
            scanCount = scanCount + 1;
            SharedPrefferenceHandler.storeScanCount(ContainerInfoAfterScan.this, "scan", scanCount);
        }*/
        init();
        setListners();
        accessValue();

        if (Return.equals("return")){
            btConfirm.setText("Return Container");
        }
    }

    public void init() {
        container_img = findViewById(R.id.container_img);
        shop_image = findViewById(R.id.shop_image);
        back_img = findViewById(R.id.back_img);
        shop_name = findViewById(R.id.shop_name);
        container_name = findViewById(R.id.container_name);
        enter_deposit = findViewById(R.id.enter_deposit);
        fee_deposit = findViewById(R.id.fee_deposit);
        cost_view = findViewById(R.id.cost_view);
        btConfirm = findViewById(R.id.btConfirm);
        pDialog = new CustomProgressDialog(this);
    }

    public void accessValue() {
        shop_name.setText(getIntent().getExtras().getString(Constant.shop_name));
        container_name.setText(getIntent().getExtras().getString(Constant.name));
        enter_deposit.setText(getIntent().getExtras().getString(Constant.deposit));
        fee_deposit.setText(getIntent().getExtras().getString(Constant.fee));
        cost_view.setText("CHF " + getIntent().getExtras().getString(Constant.total));
        ImageUtils.displayImageFromUrl(ContainerInfoAfterScan.this, Constant.CONTAINER_IMAGE + getIntent().getExtras().getString(Constant.photo), container_img, null);
        ImageUtils.displayImageFromUrl(ContainerInfoAfterScan.this, Constant.OUTLET_IMAGE + getIntent().getExtras().getString(Constant.shop_logo), shop_image, null);
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
                pDialog.setUpDialog();
//                Intent intent = new Intent(ContainerInfoAfterScan.this, CheckoutActivity.class);
//                String amt = String.valueOf(getIntent().getExtras().getString(Constant.total));
//                intent.putExtra("amount", amt);
//                intent.putExtra("qrCode", qrCodeNumber);
//                intent.putExtra("shopName",getIntent().getExtras().getString("shop_name"));
//                intent.putExtra("shopLogo",getIntent().getExtras().getString("shop_logo"));
//
//                startActivity(intent);
//                pDialog.dismissDialog();
////                hireReturnApi(getIntent().getExtras().getString(Constant.qrcode));

                hireReturnApi("", "qqqqqq");

            }
        });
    }

    public void congrtsDialog() {
        mDialog = new Dialog(this, R.style.AppTheme_Slide);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        mDialog.getWindow().setLayout(width, height);
        mDialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        mDialog.setContentView(R.layout.confiti_layout);
        close_view = mDialog.findViewById(R.id.close_view);
        close_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public void handle() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                congrtsDialog();
            }
        }, 300);
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

    private void hireReturnApi(String chargeId, String receiptUrl) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.qrcode, qrCodeNumber);
            jsonObject.put("charge_id", "");
            Log.e("json object", jsonObject.toString());
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<NewBadgeModel> call = RetrofitClient.getRetrofitClient().hireReturnApiModel("Bearer" + " " + SharedPrefferenceHandler.retriveData(ContainerInfoAfterScan.this,
                    Constant.BEARER_TOKEN), requestBody);
            call.enqueue(new Callback<NewBadgeModel>() {
                @Override
                public void onResponse(Call<NewBadgeModel> call, Response<NewBadgeModel> response) {
                    pDialog.dismissDialog();
                    if (response.isSuccessful()) {
                        try {
                            NewBadgeModel newBadgeModel = response.body();
                            newBadgeList = new ArrayList<>();
                            if (newBadgeModel.getNewBadge().size() > 0) {
                                newBadgeList.addAll(newBadgeModel.getNewBadge());
                            }
                            assert newBadgeModel != null;
                            if (newBadgeModel.getStatus() == 1) {
                                pDialog.dismissDialog();
                                createNotification();
                                Bundle bundle = new Bundle();
                                Intent i = new Intent(ContainerInfoAfterScan.this, ReadyToUseSuccessActivity.class);
                                i.putExtra("receiptUrl", receiptUrl);
                                i.putExtra("shopName", shopname);
                                i.putExtra("shopLogo", shopLogo);
                                i.putExtra("containerName",getIntent().getExtras().getString(Constant.name));
                                if (Return.equals("return")){
                                    i.putExtra("return","return");
                                }
                                Log.i("shopLogo",shopLogo);
                                bundle.putSerializable("badgeListBundle", newBadgeList);
                                i.putExtras(bundle);
                                startActivityForResult(i, 101);
                            } else {
                                Toast.makeText(ContainerInfoAfterScan.this, newBadgeModel.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                pDialog.dismissDialog();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            pDialog.dismissDialog();
                        }
                    }
                }

                @Override
                public void onFailure(Call<NewBadgeModel> call, Throwable t) {
                    pDialog.dismissDialog();
                    Toast.makeText(ContainerInfoAfterScan.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            pDialog.dismissDialog();
        }
    }

    private void hireReturnApi(String qrcode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.qrcode, qrcode);
            Log.e("json object", jsonObject.toString());
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().hireReturnApi("Bearer" + " " + SharedPrefferenceHandler.retriveData(ContainerInfoAfterScan.this,
                    Constant.BEARER_TOKEN), requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        pDialog.dismissDialog();
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            Log.e("detail data", object.toString());
                            if (object.getString("status").equalsIgnoreCase("1")) {
                                createNotification();
                                Intent i = new Intent(ContainerInfoAfterScan.this, ReadyToUseSuccessActivity.class);

                                startActivityForResult(i, 101);
                            } else {
                                Toast.makeText(ContainerInfoAfterScan.this, object.getString("message"), Toast.LENGTH_SHORT).show();
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
            e.printStackTrace();
        }
    }

    public void createNotification() {
        Intent myIntent = new Intent(getApplicationContext(), NotifyService.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 18, pendingIntent);
    }
}
