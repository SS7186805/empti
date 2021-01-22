package com.empti.app.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.empti.app.model.NewBadgeModel;
import com.empti.app.utilities.NotifyService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.empti.app.R;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.GpsTracker;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
    private ZXingScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1;
    ImageView cancel_view, cancel_code_view, dummy_image;
    LinearLayout enter_id_view;
    RelativeLayout qr_code_view, enter_txt_view;
    EditText enter_code_ed;
    LinearLayout scan_qr_code_view, flash_view;
    String type = "";
    int scanCount = 0;
    CustomProgressDialog pDialog;
    GpsTracker gpsTracker;
    double latitude, longitude;
    ArrayList<NewBadgeModel.NewBadge> newBadgeList;
    JSONObject shop_detail;
    JSONObject data;


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        if (state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
            mCameraId = state.getInt(CAMERA_ID, -1);
        } else {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
            mCameraId = -1;
        }
        setContentView(R.layout.activity_full_screen_layout);

        Log.i("token", SharedPrefferenceHandler.retriveData(this, Constant.BEARER_TOKEN));

        scanCount = SharedPrefferenceHandler.retrieveScanCount(FullScannerActivity.this, "Info");
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        setupFormats();
        contentFrame.addView(mScannerView);
        pDialog = new CustomProgressDialog(this);
        cancel_view = findViewById(R.id.cancel_view);
        dummy_image = findViewById(R.id.dummy_image);
        cancel_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        qr_code_view = findViewById(R.id.qr_code_view);
        enter_txt_view = findViewById(R.id.enter_txt_view);
        enter_id_view = findViewById(R.id.enter_id_view);
        enter_id_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScannerView.setAutoFocus(false);
                enter_code_ed.requestFocus();
                enter_code_ed.setFocusable(true);
                openKeyBoared();
                enter_txt_view.setVisibility(View.VISIBLE);
                qr_code_view.setVisibility(View.GONE);
                enter_code_ed.setText("");
                dummy_image.setAlpha(0.8f);
            }
        });
        enter_code_ed = findViewById(R.id.enter_code_ed);

        //  enter_code_ed.setCursorVisible(false);
        cancel_code_view = findViewById(R.id.cancel_code_view);
        cancel_code_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
                closeKeyBoared();
                enter_txt_view.setVisibility(View.GONE);
                qr_code_view.setVisibility(View.VISIBLE);
            }
        });
        scan_qr_code_view = findViewById(R.id.scan_qr_code_view);
        scan_qr_code_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
                closeKeyBoared();
                enter_txt_view.setVisibility(View.GONE);
                qr_code_view.setVisibility(View.VISIBLE);
            }
        });

        enter_code_ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (enter_code_ed.getText().toString().length() > 0) {
                        if (type.equalsIgnoreCase("true")) {
                            Intent i = new Intent(FullScannerActivity.this, SelectContainers.class);
                            i.putExtra(Constant.qrcode, enter_code_ed.getText().toString());
                            startActivityForResult(i, 101);
                        } else {
                            pDialog.setUpDialog();
                            qrcodeScanApi(enter_code_ed.getText().toString());
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        type = getIntent().getExtras().getString("type");
        flash_view = findViewById(R.id.flash_view);
        flash_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFlash) {
                    mFlash = false;
                    mScannerView.setFlash(mFlash);
                } else {
                    mFlash = true;
                    mScannerView.setFlash(mFlash);
                }
            }
        });
    }

    public void getLocation() {
        gpsTracker = new GpsTracker(FullScannerActivity.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Log.e("latitue", latitude + " " + longitude);
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    public void closeKeyBoared() {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(enter_code_ed.getWindowToken(), 0);
    }

    public void openKeyBoared() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
        closeKeyBoared();
        enter_txt_view.setVisibility(View.GONE);
        qr_code_view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
        outState.putInt(CAMERA_ID, mCameraId);
    }

    @Override
    public void handleResult(Result rawResult) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
        }
        if (type.equalsIgnoreCase("true")) {
            Intent i = new Intent(FullScannerActivity.this, SelectContainers.class);
            i.putExtra(Constant.qrcode, rawResult.getText());
            startActivityForResult(i, 101);
        } else {
            pDialog.setUpDialog();
            qrcodeScanApi(rawResult.getText());
        }
        mFlash = false;
        onResume();
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

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if (mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for (int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }
        for (int index : mSelectedIndices) {
            formats.add(ZXingScannerView.ALL_FORMATS.get(index));
        }
        if (mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        closeKeyBoared();
    }

    @Override
    public void onBackPressed() {
        mScannerView.setAutoFocus(true);
        if (enter_txt_view.getVisibility() == View.VISIBLE) {
            enter_txt_view.setVisibility(View.GONE);
            qr_code_view.setVisibility(View.VISIBLE);

            onResume();
        } else {
            super.onBackPressed();
        }
    }

    private void qrcodeScanApi(String qrcode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.qrcode, qrcode);
            Log.e("json object", jsonObject.toString());
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().containerDetails("Bearer" + " " + SharedPrefferenceHandler.retriveData(FullScannerActivity.this,
                    Constant.BEARER_TOKEN), requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            Log.e("detail_data", object.toString());
                            if (object.getString("status").equalsIgnoreCase("1")) {
                                pDialog.dismissDialog();

                                data = object.getJSONObject("data");
                                shop_detail = data.getJSONObject("shop_detail");
                                if (data.getString(Constant.status).equalsIgnoreCase("Ready to Use")) {
                                    closeKeyBoared();
                                    Intent i = new Intent(FullScannerActivity.this, ContainerInfoAfterScan.class);
                                    i.putExtra(Constant.qrcode, qrcode);
                                    i.putExtra(Constant.shop_name, shop_detail.getString(Constant.shop_name));
                                    i.putExtra(Constant.shop_logo, shop_detail.getString(Constant.shop_logo));
                                    i.putExtra(Constant.photo, data.getString(Constant.photo));
                                    i.putExtra(Constant.name, data.getString(Constant.name));
                                    i.putExtra(Constant.deposit, data.getString(Constant.deposit));
                                    i.putExtra(Constant.fee, data.getString(Constant.fee));
                                    i.putExtra(Constant.total, data.getString(Constant.total));
                                    startActivityForResult(i, 101);
                                    Log.i("responseConfirm", "Confirm");
                                } else if (data.getString(Constant.status).equalsIgnoreCase("In Use")) {
                                    closeKeyBoared();
//                                    pDialog.setUpDialog();
                                    hireReturnApi("", "");
//                                    Intent i = new Intent(FullScannerActivity.this, ContainerInfoAfterScan.class);
//                                    i.putExtra(Constant.qrcode, qrcode);
//                                    i.putExtra(Constant.shop_name, shop_detail.getString(Constant.shop_name));
//                                    i.putExtra(Constant.shop_logo, shop_detail.getString(Constant.shop_logo));
//                                    i.putExtra(Constant.photo, data.getString(Constant.photo));
//                                    i.putExtra(Constant.name, data.getString(Constant.name));
//                                    i.putExtra(Constant.deposit, data.getString(Constant.deposit));
//                                    i.putExtra(Constant.fee, data.getString(Constant.fee));
//                                    i.putExtra("return", "return");
//                                    i.putExtra(Constant.total, data.getString(Constant.total));
//                                    startActivityForResult(i, 101);
                                    Log.i("responsereturn", "return");
                                } else {
                                    pDialog.dismissDialog();
                                    closeKeyBoared();
                                    if (Constant.distance(latitude, longitude, Double.parseDouble(shop_detail.getString(Constant.latitude)),
                                            Double.parseDouble(shop_detail.getString(Constant.longitude))) < 1000) {
                                    }
                                    Intent i = new Intent(FullScannerActivity.this, PendingRetrunActivity.class);
                                    i.putExtra(Constant.photo, data.getString(Constant.photo));
                                    i.putExtra(Constant.qrcode, qrcode);
                                    i.putExtra(Constant.name, data.getString(Constant.name));
                                    startActivityForResult(i, 101);
                                }
                            } else {
                                pDialog.dismissDialog();
                                Toast.makeText(FullScannerActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismissDialog();
                        } catch (IOException e) {
                            e.printStackTrace();
                            pDialog.dismissDialog();
                        }
                    }else{
                        pDialog.dismissDialog();
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

    private void hireReturnApi(String chargeId, String receiptUrl) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.qrcode, enter_code_ed.getText().toString());
            jsonObject.put("charge_id", "");

            Log.e("json object", jsonObject.toString());
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<NewBadgeModel> call = RetrofitClient.getRetrofitClient().hireReturnApiModel("Bearer" + " " + SharedPrefferenceHandler.retriveData(FullScannerActivity.this,
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
                                Intent i = new Intent(FullScannerActivity.this, ReadyToUseSuccessActivity.class);
                                i.putExtra("receiptUrl", receiptUrl);
                                i.putExtra("shopName", shop_detail.getString(Constant.shop_name));
                                i.putExtra("shopLogo",Constant.CONTAINER_IMAGE + data.getString(Constant.photo));
                                i.putExtra("containerName", data.getString(Constant.name));
                                i.putExtra("return", "return");

//                                Log.i("shopLogo", shopLogo);
                                bundle.putSerializable("badgeListBundle", newBadgeList);
                                i.putExtras(bundle);
                                startActivityForResult(i, 101);
                            } else {
                                Toast.makeText(FullScannerActivity.this, newBadgeModel.getMessage().toString(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(FullScannerActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            pDialog.dismissDialog();
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