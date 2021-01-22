package com.empti.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.empti.app.R;
import com.empti.app.adapter.ContainerList;
import com.empti.app.allinterface.SelectContainerInterface;
import com.empti.app.model.ContainerListModel;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class    SelectContainers extends Activity implements SelectContainerInterface {
    RecyclerView container_lsitView;
    ImageView back_img;
    CustomProgressDialog pDialog;
    ArrayList<ContainerListModel> modelArr;
    ContainerList adapter;
    Dialog mDialog;
    ImageView close_view;
    int scanCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_product_layout);
        scanCount = SharedPrefferenceHandler.retrieveScanCount(SelectContainers.this, "more");
        init();
        setListners();
        pDialog.setUpDialog();
        containerAll();
    }

    public void init() {
        pDialog = new CustomProgressDialog(this);
        container_lsitView = findViewById(R.id.container_lsitView);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        container_lsitView.setLayoutManager(manager);
        back_img = findViewById(R.id.back_img);
    }

    public void setListners() {
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setAdapter() {
        adapter = new ContainerList(this, modelArr, this);
        container_lsitView.setAdapter(adapter);
    }

    private void containerAll() {
        modelArr = new ArrayList<>();
        try {
            //RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().outletContainerInfo("Bearer" + " " + SharedPrefferenceHandler.retriveData(SelectContainers.this, Constant.BEARER_TOKEN));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    pDialog.dismissDialog();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            Log.e("containers", object.toString());
                            if (object.getString("status").equalsIgnoreCase("1")) {
                                JSONArray data = object.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject obj = data.getJSONObject(i);
                                    ContainerListModel modle = new ContainerListModel(obj.getString(Constant.name),
                                            obj.getString(Constant.photo), obj.getString(Constant.id),
                                            false);
                                    modelArr.add(modle);
                                }
                                setAdapter();
                                if (scanCount == 10) {
                                    congrtsDialog();
                                } else {
                                    scanCount = scanCount + 1;
                                    SharedPrefferenceHandler.storeScanCount(SelectContainers.this, "more", scanCount);
                                }
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
                    Toast.makeText(SelectContainers.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            pDialog.dismissDialog();
            e.printStackTrace();
        }
    }

    @Override
    public void clickView(int pos) {
        for (int i = 0; i < modelArr.size(); i++) {
            if (pos == i) {
                modelArr.get(i).setSelected(true);
            } else {
                modelArr.get(i).setSelected(false);
            }
        }
        adapter.notify(modelArr);
        Intent i = new Intent(SelectContainers.this, ProductDetailActivity.class);
        i.putExtra(Constant.name, modelArr.get(pos).getName());
        i.putExtra(Constant.photo, modelArr.get(pos).getPhoto());
        i.putExtra(Constant.id, modelArr.get(pos).getId());
        i.putExtra(Constant.qrcode, getIntent().getExtras().getString(Constant.qrcode));
        startActivityForResult(i, 101);
    }

    @Override
    public void clickInfoView(int pos, String name, boolean image) {
    }

    @Override
    public void deltetView(String containerId, int pos) {
    }

    @Override
    public void editView(int pos) {
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

            }
        }, 300);
    }
}
