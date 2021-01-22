package com.empti.app.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.hbb20.CountryCodePicker;
import com.empti.app.R;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.FileUtils;
import com.empti.app.utilities.ImageUtils;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class EditProfile extends Activity {
    private TextView tvTitle, tvEditDate;
    private int mYear, mMonth, mDay;
    TextView etEditGender;
    private EditText etEditFirstName, etEditLastName, etEditEmail, etEditMobileNum, etEditCity;
    private static int PICK_IMAGE_FROM_CAMERA = 100;
    private String profile_image_path;
    private ImageView ivEditPlaceHolder;
    private DatePickerDialog datePickerDialog;
    private String chngeDateFormat;
    private Button btUpgrade;
    private ImageView ivBackArrow;
    private Call<ResponseBody> call;
    CustomProgressDialog pDialog;
    BottomSheetDialog camgllry;
    String selectedImagePath = "";
    File file;
    CountryCodePicker cpCountryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        //call initialize method
        pDialog = new CustomProgressDialog(this);
        init();
        setData();
        Listener();
    }
    public void init() {
        cpCountryCode = findViewById(R.id.cpCountryCode);
        etEditFirstName = findViewById(R.id.etEditFirstName);
        etEditLastName = findViewById(R.id.etEditLastName);
        etEditEmail = findViewById(R.id.etEditEmail);
        tvTitle = findViewById(R.id.tvTitle);
        ivBackArrow = findViewById(R.id.ivBackArrow);
        etEditMobileNum = findViewById(R.id.etEditMobileNum);
        btUpgrade = findViewById(R.id.btUpgrade);
        etEditCity = findViewById(R.id.etEditCity);
        tvEditDate = findViewById(R.id.tvEditDate);
        ivEditPlaceHolder = findViewById(R.id.ivEditPlaceHolder);
        etEditGender = findViewById(R.id.etEditGender);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("Your profile");
    }
    private void Listener() {
        ivEditPlaceHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPermision();
            }
        });
        tvEditDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    datePicker();
                }
                return true;
            }
        });
        btUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDetailEditProfile();
            }
        });
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        etEditGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditProfile.this, SelectGender.class);
                startActivityForResult(i, 2);
            }
        });
    }
    public void callPermision() {
        TedPermission.with(EditProfile.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            dailog();
            // Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(EditProfile.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };
    private void setData() {
        tvEditDate.setText(SharedPrefferenceHandler.retriveData(this, Constant.DATE_OF_BIRTH));
        // Log.e("", "setData: ", );
        etEditGender.setText(SharedPrefferenceHandler.retriveData(this, Constant.GENDER));
        cpCountryCode.setCountryForPhoneCode(Integer.valueOf(SharedPrefferenceHandler.retriveData(this,Constant.COUNTRY_CODE)));
        Log.e("gender", "setData: " + SharedPrefferenceHandler.retriveData(this, Constant.GENDER + "" + SharedPrefferenceHandler.retriveData(this, Constant.FIRST_NAME)));
        etEditFirstName.setText(SharedPrefferenceHandler.retriveData(this, Constant.FIRST_NAME));
        etEditLastName.setText(SharedPrefferenceHandler.retriveData(this, Constant.LAST_NAME));
        etEditEmail.setText(SharedPrefferenceHandler.retriveData(this, Constant.EMAIL_ID));
        etEditMobileNum.setText(SharedPrefferenceHandler.retriveData(this, Constant.PHONE_NUMBER));
        etEditCity.setText(SharedPrefferenceHandler.retriveData(this, Constant.CITY));
        String image = SharedPrefferenceHandler.retriveData(this, Constant.IMAGE);
        // Picasso.with(this).load(Constant.FRONT_IMAGE + image).placeholder(R.drawable.circle).into(ivEditPlaceHolder);
        ImageUtils.displayRoundImageFromUrl(EditProfile.this, Constant.FRONT_IMAGE + image, ivEditPlaceHolder);
    }
    private void datePicker() {
        final Calendar c = Calendar.getInstance();
        mYear = 2000;
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePicker mdatepicker;
        datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tvEditDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        //  datePickerDialog.updateDate(2000, Calendar.JANUARY, 1);
        datePickerDialog.show();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        chngeDateFormat = dateFormat.format(c.getTime());
    }
    public void sendDetailEditProfile() {
        JSONObject object = new JSONObject();
        HashMap<String, RequestBody> map = new HashMap<>();
        try {
            object.put("first_name", etEditFirstName.getText().toString());
            object.put("last_name", etEditLastName.getText().toString());
            object.put("phone_no", etEditMobileNum.getText().toString());
            object.put("city", etEditCity.getText().toString());
            object.put("date_of_birth", tvEditDate.getText().toString());
            object.put("gender", etEditGender.getText().toString());
            object.put("country_code", cpCountryCode.getSelectedCountryCode());
            map.put("first_name", RequestBody.create(MediaType.parse("application[form-data"), etEditFirstName.getText().toString()));
            map.put("last_name", RequestBody.create(MediaType.parse("application[form-data"), etEditLastName.getText().toString()));
            // map.put("email", RequestBody.create(MediaType.parse("application[form-data"), getEmail));
            map.put("phone_no", RequestBody.create(MediaType.parse("application[form-data"), etEditMobileNum.getText().toString()));
            //  map.put("password", RequestBody.create(MediaType.parse("application[form-data"), getPassword));
            map.put("country_code", RequestBody.create(MediaType.parse("application[form-data"), cpCountryCode.getSelectedCountryCode()));
            map.put("city", RequestBody.create(MediaType.parse("application[form-data"), etEditCity.getText().toString()));
            map.put("date_of_birth", RequestBody.create(MediaType.parse("application[form-data"), tvEditDate.getText().toString()));
            map.put("gender", RequestBody.create(MediaType.parse("application[form-data"), etEditGender.getText().toString()));
            pDialog.setUpDialog();
            Log.i("send_Data",object.toString());
            editProfile(object, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void PickImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }
    //camera open
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                file = new File(resultUri.getPath());
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                ivEditPlaceHolder.setImageBitmap(bitmap);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if (requestCode == 2) {
            Log.e("resultcode", "onActivityResult: " + resultCode);
            if (data != null) {
                String gender = data.getStringExtra("gender");
                Log.e("male", "onActivityResult: " + gender);
                etEditGender.setText(gender);
            }
        } else if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri uri = FileUtils.onSelectFromGalleryResult(data, EditProfile.this);
                    Log.e("uri", uri.toString());
                    try {
                        selectedImagePath = FileUtils.getFilePath(EditProfile.this, uri);
                        file = new File(selectedImagePath);
                        ImageUtils.displayRoundImageFromUrl(EditProfile.this, selectedImagePath, ivEditPlaceHolder);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    camgllry.dismiss();
                }
            }
        } else if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri uri = FileUtils.onCaptureImageResult(data, EditProfile.this);
                    // image_user.setImageBitmap(bitmap);
                    Log.e("uri", uri.toString());
                    try {
                        selectedImagePath = FileUtils.getFilePath(EditProfile.this, uri);
                        file = new File(selectedImagePath);
                        ImageUtils.displayRoundImageFromUrl(EditProfile.this, selectedImagePath, ivEditPlaceHolder);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    camgllry.dismiss();
                }
            }
        }
    }
    private void editProfile(JSONObject object, HashMap<String, RequestBody> map) {
        if (file != null) {
            Log.e("PersonalInfo", "signup: " + "SIGN" + file.getName() + file.getPath() + file.length());
            RequestBody requestBody = RequestBody.create(MediaType.parse("image"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getPath(), requestBody);
            call = RetrofitClient.getRetrofitClient().editProfile("Bearer" + " " + SharedPrefferenceHandler.retriveData(this, Constant.BEARER_TOKEN), map, part);
        } else {
            Log.e("PersonalInfo", "signup: " + "SIGN notExit");
            final RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), object.toString());
            call = RetrofitClient.getRetrofitClient().editProfile("Bearer" + " " + SharedPrefferenceHandler.retriveData(this,
                    Constant.BEARER_TOKEN), requestBody);
        }
         /* RequestBody requestBody= RequestBody.create(MediaType.parse("Content-Type: application/json"),object.toString());
          Call<ResponseBody> call= RetrofitClient.getRetrofitClient().editProfile("Bearer"+" "+SharedPrefferenceHandler.retriveData(this,Constant.BEARER_TOKEN),requestBody);
*/
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pDialog.dismissDialog();
                if (response.isSuccessful()) {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(response.body().string());
                        if (object.getInt("status") == 1) {
                            Toast.makeText(EditProfile.this, ("Profile update sucessful"), Toast.LENGTH_SHORT).show();
                            JSONObject object1 = object.getJSONObject("data");
                            Log.i("country_Code",object1.toString());
                            SharedPrefferenceHandler.editprofile(EditProfile.this, object1);
                            Intent i = new Intent();
                            setResult(Activity.RESULT_OK, i);
                            finish();
                        }
                    } catch (JSONException e) {
                        pDialog.dismissDialog();
                        e.printStackTrace();
                    } catch (IOException e) {
                        pDialog.dismissDialog();
                        e.printStackTrace();
                    }
                    try {
                        Log.e("mResponse", "onResponse: " + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismissDialog();
            }
        });
    }
    //---------------------------Camera----upload----------------------
    public void dailog() {
        camgllry = new BottomSheetDialog(EditProfile.this);
        camgllry.requestWindowFeature(Window.FEATURE_NO_TITLE);
        camgllry.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        camgllry.setContentView(R.layout.camera_dialog);
        camgllry.show();
        onclick();
    }
    public void onclick() {
        LinearLayout camera, gallery, cancel_layout;
        camera = (LinearLayout) camgllry.findViewById(R.id.camera_layout);
        gallery = (LinearLayout) camgllry.findViewById(R.id.gallery_layout);
        cancel_layout = (LinearLayout) camgllry.findViewById(R.id.cancel_layout);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 1);
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
            }
        });
        cancel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camgllry.dismiss();
            }
        });
    }
}




