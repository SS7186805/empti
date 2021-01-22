package com.empti.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.empti.app.R;
import com.empti.app.adapter.AllContainerAdapter;
import com.empti.app.adapter.TypeViewAdapter;
import com.empti.app.allinterface.SelectContainerInterface;
import com.empti.app.model.DetailModel;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.DateUtils;
import com.empti.app.utilities.GpsTracker;
import com.empti.app.utilities.ImageUtils;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyContainerActivity extends Activity implements SelectContainerInterface {
    TextView outlet_name, type_txt, status_txt;
    ImageView outlet_img, back_img;
    LinearLayout type_view, status_view;
    ImageView add_more;
    RecyclerView containerList;
    Dialog mDialog;
    Global global;
    AllContainerAdapter adapter;
    RoundedImageView container_img;
    TextView container_name, borrow_date, return_txt, deposit_txt, fee_txt, return_date;
    CustomProgressDialog pDialog;
    int page = 0;
    ArrayList<DetailModel> modelArr;
    boolean isLoading = false;
    BottomSheetDialog bottomSheetDialog, bottomSheetDialog1;
    String statusMstring = "", typemstring = "";
    Button direction_btn, return_btn;
    GpsTracker gpsTracker;
    double latitude, longitude;
    LinearLayout view_info_btn;
    TextView no_data_txt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_container_layout);
        init();
        setListgners();
    }

    public void init() {
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
        add_more.setVisibility(View.GONE);
        modelArr = new ArrayList<>();
        initScrollListener();
        type_view.setVisibility(View.GONE);
        getLocation();
        callApi(statusMstring);
    }

    public void setAdapter(ArrayList<DetailModel> finalArr) {
        adapter = new AllContainerAdapter(this, finalArr, this, false);
        containerList.setAdapter(adapter);
        if (finalArr.size() > 0) {
            no_data_txt.setVisibility(View.GONE);
            containerList.setVisibility(View.VISIBLE);
        } else {
            no_data_txt.setVisibility(View.VISIBLE);
            containerList.setVisibility(View.GONE);
        }
    }

    public void setListgners() {
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyContainerActivity.this, ScanAccessActivity.class);
                startActivityForResult(i, 101);
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

    public void getLocation() {
        gpsTracker = new GpsTracker(MyContainerActivity.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Log.e("latitue", latitude + " " + longitude);
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    public void openGoogleMap(double point1_lat, double point1_lng, double point2_latitude, double point2_longitude) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + point1_lat + "," + point1_lng + "&daddr=" + point2_latitude + "," + point2_longitude + ""));
        startActivity(intent);
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
        } else if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                isLoading = false;
                modelArr.clear();
                callApi(statusMstring);
            }
        }
    }

    public void infoConatainer() {
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.my_container_detail_info);
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
        return_date = mDialog.findViewById(R.id.return_date);
        container_name = mDialog.findViewById(R.id.container_name);
        borrow_date = mDialog.findViewById(R.id.borrow_date);
        return_txt = mDialog.findViewById(R.id.return_txt);
        deposit_txt = mDialog.findViewById(R.id.deposit_txt);
        fee_txt = mDialog.findViewById(R.id.fee_txt);
        direction_btn = mDialog.findViewById(R.id.direction_btn);
        return_btn = mDialog.findViewById(R.id.return_btn);
        view_info_btn = mDialog.findViewById(R.id.view_info_btn);
        mDialog.show();
    }

    @Override
    public void clickView(int pos) {
        infoConatainer();
        ImageUtils.displayImageFromUrl(MyContainerActivity.this, Constant.OUTLET_IMAGE + modelArr.get(pos).getShop_image(), container_img, getResources().getDrawable(R.drawable.dummy_outlet));
        container_name.setText(modelArr.get(pos).getShop_name());
        return_txt.setText(modelArr.get(pos).getStatus());
        if (modelArr.get(pos).getStatus().equalsIgnoreCase("Ready to Use")) {
            view_info_btn.setVisibility(View.GONE);
            return_txt.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
        } else if (modelArr.get(pos).getStatus().equalsIgnoreCase("In use")) {
            view_info_btn.setVisibility(View.VISIBLE);
            return_txt.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            view_info_btn.setVisibility(View.GONE);
            return_txt.setTextColor(getResources().getColor(R.color.colorToolbarTitle));
        }
        deposit_txt.setText(modelArr.get(pos).getDeposit() + " CHF");
        fee_txt.setText(modelArr.get(pos).getFee() + " CHF");
        borrow_date.setText(DateUtils.formatDate(Long.parseLong(modelArr.get(pos).getPicked_date())));
        if (modelArr.get(pos).getReturned_date().equalsIgnoreCase("0")) {
            return_date.setText("-");
        } else {
            return_date.setText(DateUtils.formatDate(Long.parseLong(modelArr.get(pos).getReturned_date())));
        }
        direction_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMap(latitude, longitude, Double.parseDouble(modelArr.get(pos).getShop_lat()), Double.parseDouble(modelArr.get(pos).getShop_lng()));
                mDialog.dismiss();
            }
        });
        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyContainerActivity.this, PendingRetrunActivity.class);
                i.putExtra(Constant.photo, modelArr.get(pos).getPhoto());
                i.putExtra(Constant.qrcode, modelArr.get(pos).getQrcode());
                i.putExtra(Constant.name, modelArr.get(pos).getName());
                i.putExtra(Constant.fee, modelArr.get(pos).getFee());
                startActivityForResult(i, 102);
               /* Intent i = new Intent(MyContainerActivity.this, FullScannerActivity.class);
                i.putExtra("type","false");
                startActivityForResult(i,102);*/
                mDialog.dismiss();
            }
        });
    }
    @Override
    public void clickInfoView(int pos, String name, boolean image) {
        type_txt.setText(name);
        bottomSheetDialog.dismiss();
        typemstring = global.getModelArr().get(pos).getContainer_id();
        callApi(name);
    }

    @Override
    public void deltetView(String containerId, int pos) {
    }
    @Override
    public void editView(int pos) {
    }

    public void callApi(String status) {
        if (modelArr.size() > 0) {
            modelArr.clear();
        }
        page = 0;
        isLoading = false;
        pDialog.setUpDialog();
        filterDetailApi(status, String.valueOf(page));
    }
    private void filterDetailApi(String status, String page_no) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.status, status);
            jsonObject.put(Constant.page_no, page_no);
            Log.e("json object", jsonObject.toString());
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().myContainerData("Bearer" + " " + SharedPrefferenceHandler.retriveData(MyContainerActivity.this, Constant.BEARER_TOKEN), requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        if (!isLoading) {
                            pDialog.dismissDialog();
                        }
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            Log.e("mycontainer data", object.toString());
                            if (object.getString("status").equalsIgnoreCase("1")) {
                                JSONArray data = object.getJSONArray("data");
                                if (isLoading) {
                                    modelArr.remove(modelArr.size() - 1);
                                    int scrollPosition = modelArr.size();
                                    adapter.notifyItemRemoved(scrollPosition);
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject dataObj = data.getJSONObject(i);
                                        JSONObject shop_detail = dataObj.getJSONObject("shop_detail");
                                        DetailModel model = new DetailModel(dataObj.getString(Constant.outlet_containers_id),
                                                dataObj.getString(Constant.deposit), dataObj.getString(Constant.fee),
                                                dataObj.getString(Constant.status), dataObj.getString(Constant.status),
                                                dataObj.getString(Constant.picked_date), dataObj.getString(Constant.returned_date),
                                                dataObj.getString(Constant.name), dataObj.getString(Constant.photo),
                                                shop_detail.getString(Constant.shop_name), shop_detail.getString(Constant.shop_logo),
                                                shop_detail.getString(Constant.latitude), shop_detail.getString(Constant.longitude), dataObj.getString(Constant.qrcode),
                                                dataObj.getString(Constant.container_id));
                                        modelArr.add(model);
                                    }
                                    adapter.notifyDataSetChanged();
                                    isLoading = false;
                                } else {
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject dataObj = data.getJSONObject(i);
                                        JSONObject shop_detail = dataObj.getJSONObject("shop_detail");
                                        DetailModel model = new DetailModel(dataObj.getString(Constant.outlet_containers_id),
                                                dataObj.getString(Constant.deposit), dataObj.getString(Constant.fee),
                                                dataObj.getString(Constant.status), dataObj.getString(Constant.status),
                                                dataObj.getString(Constant.picked_date), dataObj.getString(Constant.returned_date),
                                                dataObj.getString(Constant.name), dataObj.getString(Constant.photo),
                                                shop_detail.getString(Constant.shop_name), shop_detail.getString(Constant.shop_logo),
                                                shop_detail.getString(Constant.latitude), shop_detail.getString(Constant.longitude), dataObj.getString(Constant.qrcode),
                                                dataObj.getString(Constant.container_id));
                                        modelArr.add(model);
                                    }
                                    setAdapter(modelArr);
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
                    Toast.makeText(MyContainerActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            pDialog.dismissDialog();
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
        filterDetailApi(statusMstring, String.valueOf(page));
    }
    public void setUpTypeViewDialog() {
        bottomSheetDialog = new BottomSheetDialog(MyContainerActivity.this);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.type_view_layout);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView all_txt = bottomSheetDialog.findViewById(R.id.all_txt);
        RecyclerView type_list = bottomSheetDialog.findViewById(R.id.type_list);
        type_list.setLayoutManager(new LinearLayoutManager(MyContainerActivity.this));
        type_list.setAdapter(new TypeViewAdapter(MyContainerActivity.this, global.getModelArr(), MyContainerActivity.this));
        all_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                type_txt.setText("all");
                typemstring = "all";
                callApi(statusMstring);
            }
        });
        bottomSheetDialog.show();
    }

    public void setUpstatusViewDialog() {
        bottomSheetDialog1 = new BottomSheetDialog(MyContainerActivity.this);
        bottomSheetDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog1.setContentView(R.layout.status_view_layout);
        bottomSheetDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView all_txt = bottomSheetDialog1.findViewById(R.id.all_txt);
        all_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog1.dismiss();
                statusMstring = "all";
                String upperString = statusMstring.substring(0, 1).toUpperCase() + statusMstring.substring(1).toLowerCase();
                status_txt.setText(upperString);
                callApi(statusMstring);
            }
        });
        TextView pending_txt = bottomSheetDialog1.findViewById(R.id.pending_txt);
        pending_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog1.dismiss();
                statusMstring = pending_txt.getText().toString();
                status_txt.setText(statusMstring);
                callApi(statusMstring);
            }
        });
        TextView returned_txt = bottomSheetDialog1.findViewById(R.id.returned_txt);
        returned_txt.setVisibility(View.VISIBLE);
        returned_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog1.dismiss();
                statusMstring = returned_txt.getText().toString();
                status_txt.setText(statusMstring);
                callApi(statusMstring);
            }
        });
        TextView read_txt = bottomSheetDialog1.findViewById(R.id.ready_txt);
        View view = bottomSheetDialog1.findViewById(R.id.view2);
        view.setVisibility(View.VISIBLE);
        read_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog1.dismiss();
                statusMstring = read_txt.getText().toString();
                status_txt.setText(statusMstring);
                callApi(statusMstring);
            }
        });
        read_txt.setVisibility(View.GONE);
        bottomSheetDialog1.show();
    }
}
