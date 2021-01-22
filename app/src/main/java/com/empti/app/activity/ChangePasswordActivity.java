package com.empti.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.empti.app.R;
import com.empti.app.utilities.AlerterMessage;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends Activity {
    ImageView back_img;
    EditText etCurrentPassword, etNewPassword, etRepeatPassword;
    Button btupdatePassword;
    CustomProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_layout);
        init();
        setListners();
    }
    public void init() {
        back_img = findViewById(R.id.back_img);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etRepeatPassword = findViewById(R.id.etRepeatPassword);
        btupdatePassword = findViewById(R.id.btupdatePassword);
        pDialog = new CustomProgressDialog(this);
    }
    public void setListners() {
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btupdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCurrentPassword.getText().toString().length() == 0) {
                    etCurrentPassword.setError("please enter current password");
                } else if (etCurrentPassword.getText().toString().length() < 6) {
                    etCurrentPassword.setError("password should be atleast 6 characters");
                } else if (etNewPassword.getText().toString().length() == 0) {
                    etNewPassword.setError("please enter new password");
                } else if (etNewPassword.getText().toString().length() < 6) {
                    etNewPassword.setError("password should be atleast 6 characters");
                } else if (etRepeatPassword.getText().toString().length() == 0) {
                    etRepeatPassword.setError("please repeat your password");
                } else if (!etRepeatPassword.getText().toString().equalsIgnoreCase(etNewPassword.getText().toString())) {
                    AlerterMessage.message(ChangePasswordActivity.this,getString(R.string.error),getString(R.string.something_went_wrong),(R.color.errorColor),(R.drawable.ic_error_outline_white_24dp));
//                   Toast.makeText(ChangePasswordActivity.this, (R.string.password_does_not_match), Toast.LENGTH_SHORT).show();
                } else {
                    pDialog.setUpDialog();
                    changePasswordApi(etCurrentPassword.getText().toString(), etNewPassword.getText().toString());
                }
            }
        });
    }
    private void changePasswordApi(String old_password, String new_password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.old_password, old_password);
            jsonObject.put(Constant.new_password, new_password);
            Log.e("json object", jsonObject.toString());
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().changePassword("Bearer" + " " + SharedPrefferenceHandler.retriveData(ChangePasswordActivity.this,
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
                                Toast.makeText(ChangePasswordActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
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
                    AlerterMessage.message(ChangePasswordActivity.this,getString(R.string.error),getString(R.string.something_went_wrong),(R.color.errorColor),(R.drawable.ic_error_outline_white_24dp));
                    //Toast.makeText(ContainerAllActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            pDialog.dismissDialog();
            e.printStackTrace();
        }
    }
}
