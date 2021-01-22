package com.empti.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.empti.app.R;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.webservices.RetrofitClient;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ResetPasswordActivity extends Activity {
    private Button btRecoverPassword;
    private ImageView ivBackArrow;
    private TextView tvTitle;
    private EditText etNewPassword, etOtp;
    private TextInputLayout textInputOtp, textInputNewPassword;
    CustomProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        init();
        listener();
        closeKeyBoared();
    }

    public void closeKeyBoared() {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(etOtp.getWindowToken(), 0);
    }

    public void init() {
        etNewPassword = findViewById(R.id.etNewPassword);
        etOtp = findViewById(R.id.etOtp);
        ivBackArrow = findViewById(R.id.ivBackArrow);
        textInputNewPassword = findViewById(R.id.textInputNewPassword);
        textInputOtp = findViewById(R.id.textInputOtp);
        btRecoverPassword = findViewById(R.id.btRecoverPassword);
        pDialog = new CustomProgressDialog(this);
    }

    public void listener() {
        etOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= 3) {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorRectangleBackground));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        etOtp.setBackgroundTintList(colorStateList);
                    }
                    textInputOtp.setDefaultHintTextColor(colorStateList);
                } else {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorAccent));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        etOtp.setBackgroundTintList(colorStateList);
                    }
                    textInputOtp.setDefaultHintTextColor(colorStateList);
                }
            }


            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= 6) {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorRectangleBackground));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        etNewPassword.setBackgroundTintList(colorStateList);
                    }
                    textInputNewPassword.setDefaultHintTextColor(colorStateList);
                    if (etNewPassword.getText().toString().length() >= 6) {
                        btRecoverPassword.setBackgroundColor(getResources().getColor(R.color.colorRectangleBackground));
                    }
                } else {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorAccent));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        etNewPassword.setBackgroundTintList(colorStateList);
                    }
                    textInputNewPassword.setDefaultHintTextColor(colorStateList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btRecoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidations();
            }
        });

        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void checkValidations() {
        if (etOtp.getText().toString().trim().isEmpty()) {
            etOtp.setError("Enter Otp");
        } else if (etNewPassword.getText().toString().trim().isEmpty()) {
            etNewPassword.setError("Enter Password");
        } else {
            resetPasswordCall();
        }
    }

    private void resetPasswordCall() {
        Map<String, String> object = new HashMap<>();
        try {
            object.put("otp", etOtp.getText().toString().trim());
            object.put("new_password", etNewPassword.getText().toString().trim());
            pDialog.setUpDialog();
            resetPasswordApi(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetPasswordApi(Map<String, String> object) {
        Call<ResponseBody> call = RetrofitClient.getRetrofitClient().resetPassword(object);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pDialog.dismissDialog();
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    Log.e("mResponse", "onResponse: " + response.body().toString());
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.getInt("status") == 1) {
                            showCustomDialog();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this,
                                    jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        pDialog.dismissDialog();
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismissDialog();
                t.printStackTrace();
            }
        });
    }

    private void showCustomDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);
        Button dialogButton = (Button) dialog.findViewById(R.id.okayBtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPasswordActivity.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
