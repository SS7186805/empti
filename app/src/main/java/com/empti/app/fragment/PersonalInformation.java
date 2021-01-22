package com.empti.app.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.empti.app.activity.MapsActivity;
import com.empti.app.activity.ReadyToUseSuccessActivity;
import com.empti.app.activity.TermsAndConditionsActivity;
import com.empti.app.utilities.AlerterMessage;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.hbb20.CountryCodePicker;
import com.empti.app.R;
import com.empti.app.activity.CompleteProfile;
import com.empti.app.activity.Information;
import com.empti.app.activity.SelectGender;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.FileUtils;
import com.empti.app.utilities.ImageUtils;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;
import com.tapadoo.alerter.Alerter;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
/*
 * Edit personal information for simple signup and submit detail
 * */

public class PersonalInformation extends Fragment {
    private int mYear, mMonth, mDay;
    private TextView tvDate;
    private EditText etGender;
    private Button btNextPersonalInfo;
    private ImageView ivPlaceHolder;
    private String userId, email;
    private RelativeLayout rlSelectImage;
    private File file;
    private String image;
    // private Uri file;
    private CountryCodePicker cpCountryCode;
    private String socialToken = "";
    CustomProgressDialog pDialog;
    private EditText etFirstName, etLastName, etSignupMobileNum, etCitySignup;
    private String getEmail = "", getPassword = "", countryCode = "", chngeDateFormat = "", getfbFirstName = "", getfbLastName = "", getemail = "";
    private boolean isSocial;
    private DatePickerDialog datePickerDialog;
    private LinearLayout llDate;
    private static int PICK_IMAGE_FROM_CAMERA = 100;
    private String profile_image_path;
    private Call<ResponseBody> call;
    private View viewCountrycode;
    public static PersonalInformation instance;
    ColorStateList colorStateList;
    BottomSheetDialog camgllry;
    String selectedImagePath = "";
    Handler handler = new Handler();


    public static PersonalInformation getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.personal_information, parentViewGroup, false);
        instance = this;
        // //call initialize method
        pDialog = new CustomProgressDialog(getActivity());
        init(rootView);
        if (getArguments() != null) {
            isSocial = getArguments().getBoolean("isSocial");
            if (!isSocial) {
                getEmail = getArguments().getString("emailSignup");
                getPassword = getArguments().getString("passwordSignup");
            } else {
                getfbFirstName = getArguments().getString("firstName");
                getfbLastName = getArguments().getString("lastname");
                getemail = getArguments().getString("email");
                etFirstName.setText(getfbFirstName);
                etLastName.setText(getfbLastName);
            }
        }
        //  callPermision();
        //call on clicklisner method
        Listener();
        phonenoAddTextChange();
        return rootView;
    }

    public void callPermision() {
        TedPermission.with(getActivity())
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
            Toast.makeText(getActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    public void setImage(File file, Bitmap bitmap) {
        ivPlaceHolder.setImageBitmap(bitmap);
        this.file = file;
    }

    //initialize
    public void init(View view) {
        colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorAccent));
        tvDate = view.findViewById(R.id.tvDate);
        btNextPersonalInfo = view.findViewById(R.id.btNextPersonalInfo);
        ivPlaceHolder = view.findViewById(R.id.ivPlaceHolder);
        cpCountryCode = view.findViewById(R.id.cpCountryCode);
        etFirstName = view.findViewById(R.id.etFirstName);
        rlSelectImage = view.findViewById(R.id.rlSelectImage);
        etLastName = view.findViewById(R.id.etLastName);
        etSignupMobileNum = view.findViewById(R.id.etSignupMobileNum);
        etCitySignup = view.findViewById(R.id.etCitySignup);
        etGender = view.findViewById(R.id.etGender);
        viewCountrycode = view.findViewById(R.id.viewCountrycode);
        //call camera permission
        //   permssion();


    }

    // Pick or Capture Image
    private void PickImage() {
        CropImage.activity()
                // .setGuidelines(CropImageView.Guidelines.ON)
                .start(getActivity());
    }

    //on click listner
    public void Listener() {
        tvDate.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    datePicker();
                }
                return true;
            }
        });
        btNextPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
                ((Information) getActivity()).setProgressStatus(100);
            }
        });
        ivPlaceHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPermision();
                //  PickImage();
            }
        });
        cpCountryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                // Toast.makeText(getContext(), "Updated " + cpCountryCode.getSelectedCountryName(), Toast.LENGTH_SHORT).show();
                countryCode = cpCountryCode.getSelectedCountryCode();
                Log.e("cotcode", "onCountrySelected: " + countryCode);
            }
        });
        etGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SelectGender.class);
                startActivityForResult(i, 2);
            }
        });

    }

    //validations to check the feild
    private void checkValidation() {
      /*  if (etFirstName.getText().toString().isEmpty()) {
            etFirstName.setError("please enter first name");
        } *//*else if (etSignupMobileNum.getText().toString().length() < 10) {
            etSignupMobileNum.setError("Please enter valid 10 digit phone number");
        }*//* else {
            sendSignupDetail();
        }*/
        sendSignupDetail();

    }

    //Date picker to select particular date
    private void datePicker() {
        final Calendar c = Calendar.getInstance();
        mYear = 2000;
//        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePicker mdatepicker;
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        //  datePickerDialog.updateDate(2000, Calendar.JANUARY, 1);
        datePickerDialog.show();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        chngeDateFormat = dateFormat.format(c.getTime());
    }

    //camera permissions
    public void permssion() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        profile_image_path = image.getAbsolutePath();
        return image;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            Log.e("resultcode", "onActivityResult: " + resultCode);
            if (data != null) {
                String gender = data.getStringExtra("gender");
                Log.e("male", "onActivityResult: " + gender);
                etGender.setText(gender);
                etGender.setBackgroundTintList(colorStateList);
            }
        } else if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri uri = FileUtils.onSelectFromGalleryResult(data, getActivity());
                    Log.e("uri", uri.toString());
                    try {
                        selectedImagePath = FileUtils.getFilePath(getActivity(), uri);
                        file = new File(selectedImagePath);
                        ImageUtils.displayRoundImageFromUrl(getActivity(), selectedImagePath, ivPlaceHolder);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    camgllry.dismiss();
                }
            }
        } else if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri uri = FileUtils.onCaptureImageResult(data, getActivity());
                    // image_user.setImageBitmap(bitmap);
                    Log.e("uri", uri.toString());
                    try {
                        file = new File(selectedImagePath);
                        selectedImagePath = FileUtils.getFilePath(getActivity(), uri);
                        ImageUtils.displayRoundImageFromUrl(getActivity(), selectedImagePath, ivPlaceHolder);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    camgllry.dismiss();
                }
            }
        }
    }

    // send signup api param
    private void sendSignupDetail() {
        JSONObject object = new JSONObject();
        HashMap<String, RequestBody> map = new HashMap<>();
        try {
            if (!isSocial) {
                object.put("first_name", etFirstName.getText().toString());
                object.put("last_name", etLastName.getText().toString());
                object.put("phone_no", etSignupMobileNum.getText().toString());
                object.put("email", getEmail);
                object.put("password", getPassword);
                object.put("country_code", cpCountryCode.getSelectedCountryCode());
                object.put("city", etCitySignup.getText().toString());
                object.put("gender", etGender.getText().toString());
                object.put("date_of_birth", tvDate.getText().toString());
                object.put("gender", etGender.getText().toString());

                Log.i("object123", object.toString());

                map.put("first_name", RequestBody.create(MediaType.parse("multipart/form-data"), etFirstName.getText().toString()));
                map.put("last_name", RequestBody.create(MediaType.parse("multipart/form-data"), etLastName.getText().toString()));
                map.put("email", RequestBody.create(MediaType.parse("multipart/form-data"), getEmail));
                map.put("phone_no", RequestBody.create(MediaType.parse("multipart/form-data"), etSignupMobileNum.getText().toString()));
                map.put("password", RequestBody.create(MediaType.parse("multipart/form-data"), getPassword));
                map.put("country_code", RequestBody.create(MediaType.parse("multipart/form-data"), cpCountryCode.getSelectedCountryCode()));
                map.put("city", RequestBody.create(MediaType.parse("multipart/form-data"), etCitySignup.getText().toString()));
                map.put("date_of_birth", RequestBody.create(MediaType.parse("multipart/form-data"), tvDate.getText().toString()));
                map.put("gender", RequestBody.create(MediaType.parse("multipart/form-data"), etGender.getText().toString()));

                Log.i("object123", map.toString());

                if(etSignupMobileNum.getText().toString().isEmpty()){
                    etSignupMobileNum.setError("Please enter phone number");
                    etSignupMobileNum.requestFocus();
                    return;
                }else {
                    pDialog.setUpDialog();
                    signup(object, map);
                }
            } else {
                object.put("first_name", etFirstName.getText().toString());
                object.put("last_name", etLastName.getText().toString());
                object.put("email", getemail);
                object.put("country_code", cpCountryCode.getSelectedCountryCode());
                object.put("phone_no", etSignupMobileNum.getText().toString());
                object.put("city", etCitySignup.getText().toString());
                object.put("gender", etGender.getText().toString());
                object.put("date_of_birth", tvDate.getText().toString());

                Log.i("object123Social", object.toString());

                map.put("first_name", RequestBody.create(MediaType.parse("multipart/form-data"), etFirstName.getText().toString()));
                map.put("last_name", RequestBody.create(MediaType.parse("multipart/form-data"), etLastName.getText().toString()));
                map.put("email", RequestBody.create(MediaType.parse("multipart/form-data"), getemail));
                map.put("phone_no", RequestBody.create(MediaType.parse("multipart/form-data"), etSignupMobileNum.getText().toString()));
                map.put("country_code", RequestBody.create(MediaType.parse("multipart/form-data"), cpCountryCode.getSelectedCountryCode()));
                map.put("city", RequestBody.create(MediaType.parse("multipart/form-data"), etCitySignup.getText().toString()));
                map.put("date_of_birth", RequestBody.create(MediaType.parse("multipart/form-data"), tvDate.getText().toString()));

                Log.i("object123Social",  map.toString());

                if (getArguments().getString("type").equalsIgnoreCase("google")) {
                    object.put("google_token", getArguments().getString("token"));
                    map.put("google_token", RequestBody.create(MediaType.parse("multipart/form-data"), getArguments().getString("token")));
                } else {
                    object.put("facebook_token", getArguments().getString("token"));
                    map.put("facebook_token", RequestBody.create(MediaType.parse("multipart/form-data"), getArguments().getString("token")));
                }
                signup(object, map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //api of sign up
    private void signup(JSONObject jsonObject, HashMap<String, RequestBody> map) {
        if (file != null) {
            Log.i("PersonalInfo", "signup: " + "SIGN" + file.getName() + file.getPath() + file.length());
            RequestBody requestBody = RequestBody.create(MediaType.parse("image"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getPath(), requestBody);
            call = RetrofitClient.getRetrofitClient().signup(map, part);
        } else {
            Log.i("PersonalInfo", "signup: " + "SIGN notExit");
            final RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            call = RetrofitClient.getRetrofitClient().signup(requestBody);
        }
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pDialog.dismissDialog();
                if (response.isSuccessful()) {
                    JSONObject object = null;
                    Log.i("mResponse", "onResponse: " + response.body().toString());
                    try {
                        object = new JSONObject(response.body().string());
                        if (object.getInt("status") == 1) {
                            Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                            JSONObject object1 = object.getJSONObject("data");
                            SharedPrefferenceHandler.storeAndParseJsonData(getActivity(), object1);
                            Intent i = new Intent(getActivity(), CompleteProfile.class);
                            startActivity(i);
                            getActivity().finish();
                        } else if (object.getInt("status") == 0) {
                            Alerter.create(requireActivity())
                                    .setTitle(getResources().getString(R.string.error))
                                    .setText(object.getString("message")).setIcon(R.drawable.ic_error_outline_white_24dp)
                                    .setBackgroundColorRes(R.color.errorColor)
                                    .show();
                            DismisAlert();
                          //  Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        pDialog.dismissDialog();
//                        e.printStackTrace();
                        Log.i("responseError", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismissDialog();
                Log.i("failur", "onFailure: " + t.getMessage());
            }
        });
    }

    private void DismisAlert(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Alerter.hide();
            }
        }, 2000);
    }

    private void phonenoAddTextChange() {
        etSignupMobileNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (etSignupMobileNum.getText().toString().isEmpty()) {
                    viewCountrycode.setBackgroundColor(getResources().getColor(R.color.colorPasswordTitle));
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewCountrycode.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etSignupMobileNum.getText().toString().isEmpty()) {
                    viewCountrycode.setBackgroundColor(getResources().getColor(R.color.colorPasswordTitle));
                }
            }
        });
    }

    //---------------------------Camera----upload----------------------
    public void dailog() {
        camgllry = new BottomSheetDialog(getActivity());
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
