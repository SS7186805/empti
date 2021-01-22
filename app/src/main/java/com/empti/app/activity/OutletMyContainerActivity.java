package com.empti.app.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.empti.app.R;
import com.empti.app.adapter.OutletContainerAdapter;
import com.empti.app.allinterface.OutletContainerInterface;
import com.empti.app.model.DetailModel;
import com.empti.app.model.OutletData;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.ImageUtils;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutletMyContainerActivity extends Activity implements OutletContainerInterface {
    RoundedImageView outlet_image;
    ImageView add_more;
    TextView outlet_name, view_all_txt;
    RecyclerView containerList;
    CustomProgressDialog pDialog;
    ArrayList<OutletData> modelArr;
    ArrayList<DetailModel> detailmodelArr;
    Global global;
    TextView no_data_txt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_container_layout);
        init();
        setListners();
    }

    public void init() {
        global = (Global) getApplicationContext();
        pDialog = new CustomProgressDialog(this);
        no_data_txt = findViewById(R.id.no_data_txt);
        containerList = findViewById(R.id.containerList);
        containerList.setLayoutManager(new LinearLayoutManager(this));
        outlet_image = findViewById(R.id.outlet_image);
        outlet_name = findViewById(R.id.outlet_name);
        add_more = findViewById(R.id.add_more);
        ImageUtils.displayImageFromUrl(OutletMyContainerActivity.this, Constant.OUTLET_IMAGE + getIntent().getExtras().getString("image_name"), outlet_image, getResources().getDrawable(R.drawable.dummy_outlet));
        outlet_name.setText(getIntent().getExtras().getString("name"));
        view_all_txt = findViewById(R.id.view_all_txt);
        pDialog.setUpDialog();
        outLetAll();
    }

    public void callPermision() {
        TedPermission.with(OutletMyContainerActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Intent i = new Intent(OutletMyContainerActivity.this, ScanAccessActivity.class);
            startActivityForResult(i, 101);        // Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(OutletMyContainerActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    public void setListners() {
        add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPermision();
            }
        });
        view_all_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* detailmodelArr=new ArrayList<>();
                for (int k=0;k<modelArr.size();k++){
                    for(int f=0;f<modelArr.get(k).getModelArr().size();f++){
                        DetailModel dModel=modelArr.get(k).getModelArr().get(f);
                        detailmodelArr.add(dModel);
                    }
                }
                global.setModelArr(detailmodelArr);*/
                Intent i = new Intent(OutletMyContainerActivity.this, ContainerAllActivity.class);
                i.putExtra(Constant.shop_name, getIntent().getExtras().getString("name"));
                i.putExtra(Constant.photo, getIntent().getExtras().getString("image_name"));
                i.putExtra(Constant.name, "all");
                i.putExtra(Constant.type, "all");
                i.putExtra(Constant.status, "all");
                //i.putExtra("image","");
                startActivityForResult(i, 101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                pDialog.setUpDialog();
                outLetAll();
            }
        }
    }

    public void setAdapter() {
        containerList.setAdapter(new OutletContainerAdapter(this, modelArr, this));
        if (modelArr.size() > 0) {
            no_data_txt.setVisibility(View.GONE);
            containerList.setVisibility(View.VISIBLE);
        } else {
            no_data_txt.setVisibility(View.VISIBLE);
            containerList.setVisibility(View.GONE);
        }
    }

    private void outLetAll() {
        modelArr = new ArrayList<>();
        try {
            //RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().outletInfo("Bearer" + " " + SharedPrefferenceHandler.retriveData(OutletMyContainerActivity.this, Constant.BEARER_TOKEN));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    pDialog.dismissDialog();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            Log.e("outlet", object.toString());
                            if (object.getString("status").equalsIgnoreCase("1")) {
                                JSONArray data = object.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject obj = data.getJSONObject(i);
                                    JSONArray detail = obj.getJSONArray("details");
                                    detailmodelArr = new ArrayList<>();
                                    for (int f = 0; f < detail.length(); f++) {
                                        JSONObject objDetail = detail.getJSONObject(f);
                                        DetailModel dModel = new DetailModel(objDetail.getString(Constant.outlet_containers_id),
                                                objDetail.getString(Constant.deposit), objDetail.getString(Constant.fee),
                                                objDetail.getString(Constant.status), objDetail.getString(Constant.modified_at),
                                                objDetail.getString(Constant.picked_date), objDetail.getString(Constant.returned_date), obj.getString(Constant.name),
                                                obj.getString(Constant.photo));
                                        detailmodelArr.add(dModel);
                                    }
                                    OutletData modle = new OutletData(obj.getString(Constant.name),
                                            obj.getString(Constant.photo), obj.getString(Constant.container_id),
                                            obj.getString(Constant.total_container), detailmodelArr);
                                    modelArr.add(modle);
                                }
                                global.setModelArr(modelArr);
                                setAdapter();
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
                    Toast.makeText(OutletMyContainerActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            pDialog.dismissDialog();
            e.printStackTrace();
        }
    }

    @Override
    public void clickView(int p) {
        //global.setModelArr(modelArr.get(p).getModelArr());
        Intent i = new Intent(OutletMyContainerActivity.this, ContainerAllActivity.class);
        i.putExtra(Constant.shop_name, getIntent().getExtras().getString("name"));
        i.putExtra(Constant.photo, getIntent().getExtras().getString("image_name"));
        i.putExtra(Constant.name, modelArr.get(p).getName());
        i.putExtra(Constant.type, modelArr.get(p).getContainer_id());
        i.putExtra(Constant.status, "all");
        startActivityForResult(i, 101);
    }
}
