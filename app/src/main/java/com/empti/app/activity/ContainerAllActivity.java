package com.empti.app.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.empti.app.R;
import com.empti.app.adapter.AllContainerAdapterForFilter;
import com.empti.app.adapter.TypeViewAdapter;
import com.empti.app.allinterface.SelectContainerInterface;
import com.empti.app.model.DetailModel;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.DateUtils;
import com.empti.app.utilities.ImageUtils;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContainerAllActivity extends Activity implements SelectContainerInterface {
    TextView outlet_name, type_txt, status_txt;
    RoundedImageView outlet_img;
    ImageView back_img;
    LinearLayout type_view, status_view;
    ImageView add_more;
    RecyclerView containerList;
    Dialog mDialog;
    Global global;
    AllContainerAdapterForFilter adapter;
    ImageView container_img;
    TextView container_name, borrow_date, return_txt, deposit_txt, fee_txt;
    CustomProgressDialog pDialog;
    int page = 0;
    ArrayList<DetailModel> modelArr;
    boolean isLoading = false;
    BottomSheetDialog bottomSheetDialog, bottomSheetDialog1;
    String statusMstring = "", typemstring = "";
    Button reminder_btn, ready_to_use_btn;
    String container_id = "";
    TextView all_txt, no_data_txt;
    int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_container_layout);
        init();
        setListgners();
    }
    public void init() {
        all_txt = findViewById(R.id.all_txt);
        ready_to_use_btn = findViewById(R.id.ready_to_use_btn);
        global = (Global) getApplicationContext();
        pDialog = new CustomProgressDialog(this);
        no_data_txt = findViewById(R.id.no_data_txt);
        outlet_name = findViewById(R.id.outlet_name);
        outlet_img = findViewById(R.id.outlet_image);
        back_img = findViewById(R.id.back_img);
        type_view = findViewById(R.id.type_view);
        status_view = findViewById(R.id.status_view);
        type_txt = findViewById(R.id.type_txt);
        status_txt = findViewById(R.id.status_txt);
        add_more = findViewById(R.id.add_more);
        containerList = findViewById(R.id.containerList);
        containerList.setLayoutManager(new LinearLayoutManager(this));
        ImageUtils.displayImageFromUrl(ContainerAllActivity.this, Constant.OUTLET_IMAGE + getIntent().getExtras().getString(Constant.photo), outlet_img, getResources().getDrawable(R.drawable.dummy_outlet));
        outlet_name.setText(getIntent().getExtras().getString(Constant.shop_name));
        type_txt.setText(getIntent().getExtras().getString(Constant.name));
        //setAdapter();
        modelArr = new ArrayList<>();
        initScrollListener();
        statusMstring = getIntent().getExtras().getString(Constant.status);
        typemstring = getIntent().getExtras().getString(Constant.type);
        status_txt.setText(statusMstring);
        callApi(typemstring, statusMstring);
    }
    public void setAdapter(ArrayList<DetailModel> finalArr) {
        adapter = new AllContainerAdapterForFilter(this, finalArr, this, true);
        containerList.setAdapter(adapter);
        if (adapter.getItemCount() > 0) {
            no_data_txt.setVisibility(View.GONE);
            containerList.setVisibility(View.VISIBLE);
        } else {
            no_data_txt.setVisibility(View.VISIBLE);
            containerList.setVisibility(View.GONE);
        }
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        setResult(Activity.RESULT_OK, i);
        finish();
    }
    public void setListgners() {
        all_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(all_txt.getText().toString() == "Cancel") {
                    for (int i = 0; i < modelArr.size(); i++) {
                        if (modelArr.get(i).getStatus().equalsIgnoreCase("In Use")) {
                            modelArr.get(i).setReady(false);
                            count = 0;
                        }
                    }
                    setAdapter(modelArr);
                    ready_to_use_btn.setVisibility(View.GONE);
                    add_more.setVisibility(View.VISIBLE);
                    all_txt.setVisibility(View.GONE);
                }else{
                    all_txt.setText("Cancel");
                    count = adapter.selectedAll();
                    ready_to_use_btn.setText("Mark as ready to use (" + count + ")");
                }
            }
        });
        ready_to_use_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < modelArr.size(); i++) {
                    if (modelArr.get(i).isReady) {
                        if (container_id.equalsIgnoreCase("")) {
                            container_id = modelArr.get(i).getOutlet_containers_id();
                        } else {
                            container_id = container_id + "," + modelArr.get(i).getOutlet_containers_id();
                        }
                    }
                }
                pDialog.setUpDialog();
                changeStatusContainer(container_id);
            }
        });
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPermision();
            }
        });
        type_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTypeViewDialog();
            }
        });
        status_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpstatusViewDialog();
            }
        });
    }
    public void callPermision() {
        TedPermission.with(ContainerAllActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Intent i = new Intent(ContainerAllActivity.this, ScanAccessActivity.class);
            startActivityForResult(i, 101);        // Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(ContainerAllActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Intent i = new Intent();
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        } else if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                callApi(typemstring, statusMstring);
            }
        }
    }
    public void infoConatainer() {
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.detail_info_dialog);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCanceledOnTouchOutside(false);
        ImageView cancelView = mDialog.findViewById(R.id.cancel_view);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        container_img = mDialog.findViewById(R.id.container_img);
        container_name = mDialog.findViewById(R.id.container_name);
        borrow_date = mDialog.findViewById(R.id.borrow_date);
        return_txt = mDialog.findViewById(R.id.return_txt);
        deposit_txt = mDialog.findViewById(R.id.deposit_txt);
        fee_txt = mDialog.findViewById(R.id.fee_txt);
        reminder_btn = mDialog.findViewById(R.id.reminder_btn);
        mDialog.show();
    }
    @Override
    public void clickView(int pos) {
        infoConatainer();
        ImageUtils.displayImageFromUrl(ContainerAllActivity.this, Constant.CONTAINER_IMAGE + modelArr.get(pos).getPhoto(), container_img, null);
        container_name.setText(modelArr.get(pos).getName());
        return_txt.setText(modelArr.get(pos).getStatus());
        if (modelArr.get(pos).getStatus().equalsIgnoreCase("Ready to Use")) {
            return_txt.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
            reminder_btn.setVisibility(View.GONE);
        } else if (modelArr.get(pos).getStatus().equalsIgnoreCase("In use")) {
            return_txt.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            reminder_btn.setVisibility(View.VISIBLE);
        } else {
            return_txt.setTextColor(getResources().getColor(R.color.colorToolbarTitle));
            reminder_btn.setVisibility(View.GONE);
        }
        deposit_txt.setText(modelArr.get(pos).getDeposit() + " CHF");
        fee_txt.setText(modelArr.get(pos).getFee() + " CHF");
        borrow_date.setText(DateUtils.formatDate(Long.parseLong(modelArr.get(pos).getPicked_date())));
        reminder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                pDialog.setUpDialog();
                notificationsApi(modelArr.get(pos).getQrcode());
            }
        });
    }
    private void notificationsApi(String qrcode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.qrcode, qrcode);
            Log.e("json object", jsonObject.toString());
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().remindNotifications("Bearer" + " " + SharedPrefferenceHandler.retriveData(ContainerAllActivity.this, Constant.BEARER_TOKEN), requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    pDialog.dismissDialog();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            Log.e("detail data", object.toString());
                            if (object.getString("status").equalsIgnoreCase("1")) {
                                Toast.makeText(ContainerAllActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ContainerAllActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
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
    @Override
    public void clickInfoView(int pos, String name, boolean isReady) {
        if (name.equalsIgnoreCase("")) {
            modelArr.get(pos).setReady(isReady);
            if (isReady) {
                count = count + 1;
            } else {
                count = count - 1;
            }
            boolean isTrue = false;
            for (int i = 0; i < modelArr.size(); i++) {
                if (modelArr.get(i).isReady()) {
                    isTrue = true;
                    break;
                }
            }
            if (isTrue) {
                ready_to_use_btn.setVisibility(View.VISIBLE);
                add_more.setVisibility(View.GONE);
                all_txt.setVisibility(View.VISIBLE);
                ready_to_use_btn.setText("Mark as ready to use (" + count + ")");
                int size = adapter.CountAllInUse();
                if (size>1) {
                    all_txt.setText("Select All");
                }else{
                    ready_to_use_btn.setText("Mark as ready to use (" + 1 + ")");
                }
            } else {
                ready_to_use_btn.setVisibility(View.GONE);
                add_more.setVisibility(View.VISIBLE);
                if (count == 0) {
                    all_txt.setVisibility(View.GONE);
                }
            }
        } else {
            type_txt.setText(name);
            bottomSheetDialog.dismiss();
            typemstring = global.getModelArr().get(pos).getContainer_id();
            callApi(typemstring, statusMstring);
        }
    }
    @Override
    public void deltetView(String containerId, int pos) {
        pDialog.setUpDialog();
        deleteConatinersApi(containerId, pos);
    }
    @Override
    public void editView(int pos) {
        Intent i = new Intent(ContainerAllActivity.this, ProductDetailActivity.class);
        i.putExtra(Constant.deposit, modelArr.get(pos).getDeposit());
        i.putExtra(Constant.fee, modelArr.get(pos).getFee());
        i.putExtra(Constant.name, modelArr.get(pos).getName());
        i.putExtra(Constant.photo, modelArr.get(pos).getPhoto());
        i.putExtra(Constant.container_id, modelArr.get(pos).getContainers_id());
        i.putExtra(Constant.outlet_container_id, modelArr.get(pos).getOutlet_containers_id());
        i.putExtra(Constant.qrcode, modelArr.get(pos).getQrcode());
        startActivityForResult(i, 102);
    }
    private void filterDetailApi(String type, String status, String page_no) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.type, type);
            jsonObject.put(Constant.status, status);
            jsonObject.put(Constant.page_no, page_no);
            Log.e("json object", jsonObject.toString());
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().filterContainers("Bearer" + " " + SharedPrefferenceHandler.retriveData(ContainerAllActivity.this, Constant.BEARER_TOKEN), requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        if (!isLoading) {
                            pDialog.dismissDialog();
                        }
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            Log.e("filter data", object.toString());
                            if (object.getString("status").equalsIgnoreCase("1")) {
                                JSONArray data = object.getJSONArray("data");
                                if (isLoading) {
                                    modelArr.remove(modelArr.size() - 1);
                                    int scrollPosition = modelArr.size();
                                    adapter.notifyItemRemoved(scrollPosition);
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject dataObj = data.getJSONObject(i);
                                        Log.i("data",dataObj.toString());
                                        DetailModel model = new DetailModel(dataObj.getString(Constant.outlet_containers_id),
                                                dataObj.getString(Constant.deposit), dataObj.getString(Constant.fee),
                                                dataObj.getString(Constant.status), dataObj.getString(Constant.modified_at),
                                                dataObj.getString(Constant.picked_date), dataObj.getString(Constant.returned_date),
                                                dataObj.getString(Constant.name), dataObj.getString(Constant.photo), false,
                                                dataObj.getString(Constant.qrcode),
                                                dataObj.getString(Constant.container_id));
                                        modelArr.add(model);
                                    }
                                    adapter.notifyDataSetChanged();
                                    isLoading = false;
                                    boolean isShowAll = false;
                                    for (int i = 0; i < modelArr.size(); i++) {
                                        if (modelArr.get(i).getStatus().equalsIgnoreCase("Returned")) {
                                            isShowAll = true;
                                        }
                                    }
                                    /*if(isShowAll){
                                        all_txt.setVisibility(View.VISIBLE);
                                    }else{
                                        all_txt.setVisibility(View.GONE);
                                    }*/
                                } else {
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject dataObj = data.getJSONObject(i);
                                        Log.i("data",dataObj.toString());
                                        DetailModel model = new DetailModel(dataObj.getString(Constant.outlet_containers_id),
                                                dataObj.getString(Constant.deposit), dataObj.getString(Constant.fee),
                                                dataObj.getString(Constant.status), dataObj.getString(Constant.modified_at),
                                                dataObj.getString(Constant.picked_date), dataObj.getString(Constant.returned_date),
                                                dataObj.getString(Constant.name), dataObj.getString(Constant.photo), false,
                                                dataObj.getString(Constant.qrcode),
                                                dataObj.getString(Constant.container_id));
                                        modelArr.add(model);
                                    }
                                    setAdapter(modelArr);
                                    boolean isShowAll = false;
                                    for (int i = 0; i < modelArr.size(); i++) {
                                        if (modelArr.get(i).getStatus().equalsIgnoreCase("Returned")) {
                                            isShowAll = true;
                                        }
                                    }
                                    ready_to_use_btn.setVisibility(View.GONE);
                                   /* if(isShowAll){
                                        all_txt.setVisibility(View.VISIBLE);
                                    }else{
                                        all_txt.setVisibility(View.GONE);
                                    }*/
                                }
                                page = page + 1;
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
                    if (!isLoading) {
                        pDialog.dismissDialog();
                    }
                    Toast.makeText(ContainerAllActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initScrollListener() {
        containerList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == modelArr.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }
    private void loadMore() {
        modelArr.add(null);
        adapter.notifyItemInserted(modelArr.size() - 1);
        filterDetailApi(typemstring, statusMstring, String.valueOf(page));
    }
    public void setUpTypeViewDialog() {
        bottomSheetDialog = new BottomSheetDialog(ContainerAllActivity.this);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.type_view_layout);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView all_txt = bottomSheetDialog.findViewById(R.id.all_txt);
        RecyclerView type_list = bottomSheetDialog.findViewById(R.id.type_list);
        type_list.setLayoutManager(new LinearLayoutManager(ContainerAllActivity.this));
        type_list.setAdapter(new TypeViewAdapter(ContainerAllActivity.this, global.getModelArr(), ContainerAllActivity.this));
        all_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                type_txt.setText("All");
                typemstring = "all";
                callApi(typemstring, statusMstring);
            }
        });
        bottomSheetDialog.show();
    }
    public void callApi(String type, String status) {
        if (modelArr.size() > 0) {
            modelArr.clear();
        }
        page = 0;
        isLoading = false;
        pDialog.setUpDialog();
        filterDetailApi(type, status, String.valueOf(page));
    }
    public void setUpstatusViewDialog() {
        bottomSheetDialog1 = new BottomSheetDialog(ContainerAllActivity.this);
        bottomSheetDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog1.setContentView(R.layout.status_view_layout);
        bottomSheetDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView all_txt = bottomSheetDialog1.findViewById(R.id.all_txt);
        all_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(all_txt.getText().toString() =="Cancel") {
                    bottomSheetDialog1.dismiss();
                    statusMstring = "all";
                    status_txt.setText(statusMstring);
                    callApi(typemstring, statusMstring);
                }else{

                }
            }
        });
        TextView pending_txt = bottomSheetDialog1.findViewById(R.id.pending_txt);
        pending_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog1.dismiss();
                statusMstring = pending_txt.getText().toString();
                status_txt.setText(statusMstring);
                callApi(typemstring, statusMstring);
            }
        });
        TextView returned_txt = bottomSheetDialog1.findViewById(R.id.returned_txt);
        returned_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog1.dismiss();
                statusMstring = returned_txt.getText().toString();
                status_txt.setText(statusMstring);
                callApi(typemstring, statusMstring);
            }
        });
        TextView read_txt = bottomSheetDialog1.findViewById(R.id.ready_txt);
        read_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog1.dismiss();
                statusMstring = read_txt.getText().toString();
                status_txt.setText(statusMstring);
                callApi(typemstring, statusMstring);
            }
        });
        bottomSheetDialog1.show();
    }
    //-------------------------delete containers----------------
    private void deleteConatinersApi(String id, int pos) {
        JSONObject jsonObject = new JSONObject();
        String url = "api/outletcontainer/delete?outlet_container_id=" + id;
        Log.e("json object", jsonObject.toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
        Call<ResponseBody> call = RetrofitClient.getRetrofitClient().deleteContainers("Bearer" + " " + SharedPrefferenceHandler.retriveData(ContainerAllActivity.this,
                Constant.BEARER_TOKEN), url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    pDialog.dismissDialog();
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        Log.e("filter", object.toString());
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            modelArr.remove(pos);
                            setAdapter(modelArr);
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
                Toast.makeText(ContainerAllActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //---------------------change status container---------------------------
    private void changeStatusContainer(String container_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.outlet_container_id, container_id);
            Log.e("json object", jsonObject.toString());
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().changeStatusContainer("Bearer" + " " + SharedPrefferenceHandler.retriveData(ContainerAllActivity.this,
                    Constant.BEARER_TOKEN), requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    pDialog.dismissDialog();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            Log.e("filter data", object.toString());
                            if (object.getString("status").equalsIgnoreCase("1")) {
                                ready_to_use_btn.setVisibility(View.GONE);
                                add_more.setVisibility(View.VISIBLE);
                                for (int i = 0; i < modelArr.size(); i++) {
                                    if (modelArr.get(i).isReady()) {
                                        modelArr.get(i).setStatus("Ready to Use");
                                        modelArr.get(i).setReady(false);
                                        all_txt.setVisibility(View.GONE);
                                    }
                                }
                                setAdapter(modelArr);
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
                    Toast.makeText(ContainerAllActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            pDialog.dismissDialog();
            e.printStackTrace();
        }
    }
}