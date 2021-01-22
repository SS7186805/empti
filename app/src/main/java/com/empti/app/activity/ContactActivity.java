package com.empti.app.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.empti.app.R;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactActivity extends Activity {

    ImageView back_img;
    Button mail_send;
    EditText etName, etEmail, etMessage;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    CustomProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_layout);
        init();
        setListners();
    }

    public void init() {
        pDialog = new CustomProgressDialog(this);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etMessage = findViewById(R.id.etMessage);
        mail_send = findViewById(R.id.mail_send);
        back_img = findViewById(R.id.back_img);
    }

    public void setListners() {
        mail_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().length() == 0) {
                    etName.setError("please enter name");
                } else if (etEmail.getText().length() == 0) {
                    etEmail.setError("please enter email");
                } else if (!etEmail.getText().toString().matches(emailPattern)) {
                    etEmail.setError("please enter valid email");
                } else if (etMessage.getText().length() == 0) {
                    etMessage.setError("please enter message");
                } else {
                    pDialog.setUpDialog();
                    contactusApi(etName.getText().toString(), etEmail.getText().toString(), etMessage.getText().toString());
                    // String mail[] = {"Hello@takai.com"};
                    //composeEmail(mail, "Takai help");
                }
            }
        });
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    public void callPermision() {
        TedPermission.with(ContactActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CALL_PHONE)
                .check();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: call_txt.getText().toString()"));
            startActivity(intent);
            // Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(ContactActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    private void contactusApi(String name, String email, String message) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.name, name);
            jsonObject.put("email", email);
            jsonObject.put("message", message);
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().contactUs("Bearer" + " " + SharedPrefferenceHandler.retriveData(ContactActivity.this, Constant.BEARER_TOKEN), requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    pDialog.dismissDialog();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            if (object.getString("status").equalsIgnoreCase("1")) {
                                Toast.makeText(ContactActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ContactActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        pDialog.dismissDialog();
                        Toast.makeText(ContactActivity.this, "Server Issue.PleaseTry again!!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pDialog.dismissDialog();
                    Toast.makeText(ContactActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            pDialog.dismissDialog();
            e.printStackTrace();
        }
    }
}
