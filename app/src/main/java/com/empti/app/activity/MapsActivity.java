package com.empti.app.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.empti.app.R;
import com.empti.app.adapter.ContainerAdapter;
import com.empti.app.model.ContainerDetail;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.GpsTracker;
import com.empti.app.utilities.ImageUtils;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DrawerLayout dvDrawableLayout;
    Marker mCurrLocationMarker;
    LinearLayout llContainer;
    private ImageView ivOpenDrawer;
    private String outletImage;
    List<MapModel.DataBean> list = new ArrayList<>();
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView rvContainers;
    private ContainerAdapter containerAdapter;
    private LinearLayout llAccountSetting, llProfile, llSignout;
    private ImageView cvPofileNavigation;
    private TextView tvNameNavigation, tvShopName, tvSheetShopName;
    private RoundedImageView ivMigros;
    private RoundedImageView ivSheetShopLogo;
    private RelativeLayout rlOutlet;
    private NavigationView nav_view;
    private String shopName, distance;
    CustomProgressDialog pDialog;
    GpsTracker gpsTracker;
    double latitude, longitude;
    HashMap<String, String> positionList = new HashMap<>();
    String destinationLat = "", destinationLng = "";
    Button btLogin;
    ArrayList<ContainerDetail> modelArr;
    TextView no_data_txt;
    LinearLayout btdirection;
    Marker idMarker;
    LinearLayout contact_us, faq_layout, home_layout, leaderboard;
    RelativeLayout myConatiners, achievements;
    String version = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        //call initialize method
        pDialog = new CustomProgressDialog(this);
        init();
        //Call click method
        Listener();
        //bottom sheet layout invisible
        llContainer.setVisibility(View.INVISIBLE);
        //  setAdapter();
        setNavigationMenu();
        if (getIntent().getExtras() != null) {
            Intent i = new Intent(MapsActivity.this, MyContainerActivity.class);
            startActivity(i);
        }
    }

    //initialize
    public void init() {
        home_layout = findViewById(R.id.home_layout);
        home_layout.setVisibility(View.GONE);
        myConatiners = findViewById(R.id.myConatiners);
        achievements = findViewById(R.id.achievements);
        contact_us = findViewById(R.id.contact_us);
        btdirection = findViewById(R.id.btdirection);
        no_data_txt = findViewById(R.id.no_data_txt);
        btLogin = findViewById(R.id.btLogin);
        llContainer = findViewById(R.id.llContainer);
        rlOutlet = findViewById(R.id.rlOutlet);
        ivOpenDrawer = findViewById(R.id.ivOpenDrawer);
        llAccountSetting = findViewById(R.id.llAccountSetting);
        rvContainers = findViewById(R.id.rvContainers);
        llProfile = findViewById(R.id.llProfile);
        tvSheetShopName = findViewById(R.id.tvSheetShopName);
        tvSheetShopName = findViewById(R.id.tvSheetShopName);
        ivSheetShopLogo = findViewById(R.id.ivSheetShopLogo);
        llSignout = findViewById(R.id.llSignout);
        tvNameNavigation = findViewById(R.id.tvNameNavigation);
        cvPofileNavigation = findViewById(R.id.cvPofileNavigation);
        ivMigros = findViewById(R.id.ivMigros);
        nav_view = findViewById(R.id.nav_view);
        dvDrawableLayout = findViewById(R.id.dvDrawableLayout);
        tvShopName = findViewById(R.id.tvShopName);
        bottomSheetBehavior = BottomSheetBehavior.from(llContainer);
        faq_layout = findViewById(R.id.faq_layout);
        leaderboard = findViewById(R.id.leaderboard);
        getLocation();
        pDialog.setUpDialog();
        callApi(latitude, longitude);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    if (idMarker != null) {
                        idMarker.hideInfoWindow();
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });
        updateToken();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tokenRefershApi();
    }

    public void getLocation() {
        gpsTracker = new GpsTracker(MapsActivity.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Log.e("latitue", latitude + " " + longitude);
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    //set on click listener
    public void Listener() {
        home_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dvDrawableLayout.closeDrawer(GravityCompat.START);
            }
        });
        myConatiners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, MyContainerActivity.class);
                startActivity(i);
                dvDrawableLayout.closeDrawer(GravityCompat.START);
            }
        });
        achievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MapsActivity.this, AchievementsActivity.class);
                startActivity(i);
                dvDrawableLayout.closeDrawer(GravityCompat.START);
            }
        });
        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MapsActivity.this, LeaderboardActivity.class);
                startActivity(i);
                dvDrawableLayout.closeDrawer(GravityCompat.START);
            }
        });
        llAccountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, SettingActivity.class);
                startActivity(i);
                dvDrawableLayout.closeDrawer(GravityCompat.START);
            }
        });
        faq_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, FAQActivity.class);
                startActivity(i);
                dvDrawableLayout.closeDrawer(GravityCompat.START);
            }
        });

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, ContactActivity.class);
                startActivity(i);
                dvDrawableLayout.closeDrawer(GravityCompat.START);
            }
        });

        ivOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dvDrawableLayout.openDrawer(GravityCompat.START);
                   /* llContainer.setVisibility(View.VISIBLE);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    //set the peek height
                    bottomSheetBehavior.setPeekHeight(0);
                    // set hideable or not
                    bottomSheetBehavior.setHideable(false);  */
            }
        });
        llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, EditProfile.class);
                startActivityForResult(i, 3);
                dvDrawableLayout.closeDrawer(GravityCompat.START);
            }
        });
        llSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
                // Setting Dialog Title
                alertDialog.setTitle(Html.fromHtml("<font color='#FF888888'>Sign out</font>"));
                alertDialog.setCancelable(false);
                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want to go to Sign out?");
                // On pressing Settings button
                alertDialog.setPositiveButton(Html.fromHtml("<font color='#319839'>Sign out</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPrefferenceHandler.removeAll(MapsActivity.this);
                        Intent i = new Intent(MapsActivity.this, SignUp.class);
                        startActivity(i);
                        finish();
                        dvDrawableLayout.closeDrawer(GravityCompat.START);
                    }
                });
                // on pressing cancel button
                alertDialog.setNegativeButton(Html.fromHtml("<font color='#319839'>Cancel</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }


        });
        rlOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, OutletMyContainerActivity.class);
                i.putExtra("name", SharedPrefferenceHandler.retriveData(MapsActivity.this, Constant.SHOP_NAME));
                i.putExtra("image_name", SharedPrefferenceHandler.retriveData(MapsActivity.this, Constant.SHOP_LOGO));
                startActivity(i);
                dvDrawableLayout.closeDrawer(GravityCompat.START);
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPermision();
            }
        });
    }

    public void callPermision() {
        TedPermission.with(MapsActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Intent i = new Intent(MapsActivity.this, FullScannerActivity.class);
            i.putExtra("type", "false");
            startActivity(i);            // Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(MapsActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    //navigation menu items set
    public void setNavigationMenu() {
        String image = SharedPrefferenceHandler.retriveData(this, Constant.IMAGE);
        String name = SharedPrefferenceHandler.retriveData(this, Constant.FIRST_NAME);
        String lastName = SharedPrefferenceHandler.retriveData(this, Constant.LAST_NAME);
        Log.e("naviName", "setNavigationMenu: " + Constant.FIRST_NAME);
        tvNameNavigation.setText(name + " " + lastName);
        ImageUtils.displayRoundImageFromUrl(MapsActivity.this, Constant.FRONT_IMAGE + image, cvPofileNavigation);
        Log.e("pic", "setNavigationMenu: " + Constant.FRONT_IMAGE + image);
        if (SharedPrefferenceHandler.retriveData(this, Constant.ROLE).equalsIgnoreCase("Outlet")) {
            rlOutlet.setVisibility(View.VISIBLE);
            ImageUtils.displayImageFromUrl(MapsActivity.this, Constant.OUTLET_IMAGE + SharedPrefferenceHandler.retriveData(this, Constant.SHOP_LOGO), ivMigros, getResources().getDrawable(R.drawable.dummy_outlet));
            //Picasso.with(MapsActivity.this).load(Constant.OUTLET_IMAGE + SharedPrefferenceHandler.retriveData(this,Constant.SHOP_LOGO)).placeholder(R.drawable.calendar).into(ivMigros);
            tvShopName.setText(SharedPrefferenceHandler.retriveData(this, Constant.SHOP_NAME));
        } else {
            rlOutlet.setVisibility(View.GONE);
        }
    }

    //container adapter set
    public void setAdapter() {
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rvContainers.setLayoutManager(manager);
        Log.i("model123",modelArr.toString());
        containerAdapter = new ContainerAdapter(this, modelArr);
        rvContainers.setAdapter(containerAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 11);
        mMap.animateCamera(update);
    }

    protected Marker createMarker(final int pos) {
        // Bitmap image = MarkerImage.customImage(ExplorehelpRequest.this, Constants.Image_URL, list.get(pos).getImage());
        LatLng latLng = new LatLng(Double.valueOf(list.get(pos).getLatitude()), Double.valueOf(list.get(pos).getLongitude()));
        Marker marker;
        marker = mMap.addMarker(new MarkerOptions()
                //  .position(latLng).title(list.get(pos).getShop_address())
                //.icon(BitmapDescriptorFactory.fromBitmap(image))); to show image on marl/ker
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.whitepin)).title(list.get(pos).getShop_name()));
        //move map camera
        positionList.put(marker.getId(), String.valueOf(pos));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.e("marker", marker.getId());
                String markerPost = marker.getId();
                if (markerPost.equalsIgnoreCase("m0")) {
                } else {
                    markerPost = markerPost.replace("m", "");
                    int i = Integer.parseInt(markerPost);
                    i = i - 1;

                    pDialog.setUpDialog();
                    idMarker = marker;
                    outletDetailApi(list.get(i).getId());
                }
                return false;
            }
        });
        return marker;
    }

    private void callApi(double lat, double lng) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latitude", lat);
            jsonObject.put("longitude", lng);
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<MapModel> call = RetrofitClient.getRetrofitClient().data("Bearer" + " " + SharedPrefferenceHandler.retriveData(MapsActivity.this, Constant.BEARER_TOKEN), requestBody);
            Log.e("accesssss", "callApi: " + SharedPrefferenceHandler.retriveData(this, Constant.BEARER_TOKEN));
            call.enqueue(new retrofit2.Callback<MapModel>() {
                @Override
                public void onResponse(Call<MapModel> call, Response<MapModel> response) {
                    pDialog.dismissDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            list.clear();
                            list = response.body().getData();
                            Log.e("size", "onResponse: " + list.size());
                            for (int i = 0; i < list.size(); i++) {
                                String Role = list.get(i).getRole();
                                createMarker(i);
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<MapModel> call, Throwable t) {
                    pDialog.dismissDialog();
                    Toast.makeText(MapsActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void outletDetailApi(int id) {
        modelArr = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.outlet_id, id);
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().outletdata("Bearer" + " " + SharedPrefferenceHandler.retriveData(MapsActivity.this, Constant.BEARER_TOKEN), requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    pDialog.dismissDialog();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            Log.e("objevct", object.toString());
                            if (object.getString("status").equalsIgnoreCase("1")) {
                                JSONObject data = object.getJSONObject("data");
                                ImageUtils.displayImageFromUrl(MapsActivity.this, Constant.OUTLET_IMAGE + data.getString(Constant.shop_logo), ivSheetShopLogo, getResources().getDrawable(R.drawable.dummy_outlet));
                                // Picasso.with(MapsActivity.this).load(Constant.OUTLET_IMAGE + SharedPrefferenceHandler.retriveData(MapsActivity.this,Constant.SHOP_LOGO)).placeholder(R.drawable.calendar).into(ivSheetShopLogo);
                                tvSheetShopName.setText(data.getString(Constant.shop_name));
                                destinationLat = data.getString(Constant.latitude);
                                destinationLng = data.getString(Constant.longitude);
                                JSONArray containerDetail = data.getJSONArray("containerDetail");
                                Log.i("data123Container",containerDetail.toString());
                                if (containerDetail.length() > 0) {
                                    for (int i = 0; i < containerDetail.length(); i++) {
                                        JSONObject obj = containerDetail.getJSONObject(i);
                                        if (obj.getInt(Constant.available_container)==0){
                                            continue;
                                        }else {
                                            ContainerDetail model = new ContainerDetail(obj.getString(Constant.name),
                                                    obj.getString(Constant.photo), obj.getString(Constant.container_id),
                                                    obj.getString(Constant.total_container), obj.getString(Constant.available_container));
                                            modelArr.add(model);

                                        }
                                    }
                                    setAdapter();
                                    rvContainers.setVisibility(View.VISIBLE);
                                    no_data_txt.setVisibility(View.GONE);
                                } else {
                                    no_data_txt.setVisibility(View.VISIBLE);
                                    rvContainers.setVisibility(View.GONE);
                                }
                                llContainer.setVisibility(View.VISIBLE);
                                btdirection.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openGoogleMap(latitude, longitude, Double.parseDouble(destinationLat), Double.parseDouble(destinationLng));
                                    }
                                });
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                //set the peek height
                                bottomSheetBehavior.setPeekHeight(0);
                                // set hideable or not
                                bottomSheetBehavior.setHideable(false);
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
                    Toast.makeText(MapsActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            pDialog.dismissDialog();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            super.onBackPressed();
            finishAffinity();
            finish();
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                // Toast.makeText(this,"resultokk",Toast.LENGTH_SHORT).show();
                String image = SharedPrefferenceHandler.retriveData(this, Constant.IMAGE);
                String name = SharedPrefferenceHandler.retriveData(this, Constant.FIRST_NAME);
                String lastName = SharedPrefferenceHandler.retriveData(this, Constant.LAST_NAME);
                Log.e("naviName", "setNavigationMenu: " + Constant.FIRST_NAME);
                tvNameNavigation.setText(name + " " + lastName);
                ImageUtils.displayRoundImageFromUrl(MapsActivity.this, Constant.FRONT_IMAGE + image, cvPofileNavigation);
                //Picasso.with(this).load(Constant.FRONT_IMAGE + image).placeholder(R.drawable.profile_user).into(cvPofileNavigation);
                Log.e("pic", "setNavigationMenu: " + Constant.FRONT_IMAGE + image);
            }
        }
    }

    public void openGoogleMap(double point1_lat, double point1_lng, double point2_latitude,
                              double point2_longitude) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + point1_lat + ","
                        + point1_lng + "&daddr=" + point2_latitude + "," + point2_longitude + ""));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void tokenRefershApi() {
        try {
            //RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().getTokenRefersh("Bearer" + " " + SharedPrefferenceHandler.retriveData(MapsActivity.this, Constant.BEARER_TOKEN));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        pDialog.dismissDialog();
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            Log.e("outlet", object.toString());
                            if (object.getString("status").equalsIgnoreCase("1")) {
                                JSONObject data = object.getJSONObject("data");
                                Log.e("token", data.getString("bearer_token"));
                                SharedPrefferenceHandler.storeData(MapsActivity.this, Constant.BEARER_TOKEN, data.getString("bearer_token"));
                                Log.e("token", SharedPrefferenceHandler.retriveData(MapsActivity.this, Constant.BEARER_TOKEN));
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
                    Toast.makeText(MapsActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateToken() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.UPDATE_TOKEN, SharedPrefferenceHandler.retriveData(MapsActivity.this, Constant.UPDATE_TOKEN));
            Log.e("notify", jsonObject.toString());
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().updateToken("Bearer" + " " + SharedPrefferenceHandler.retriveData(MapsActivity.this, Constant.BEARER_TOKEN), requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.e("NotifyRes", "" + response.body());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //Toast.makeText(ContainerAllActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}