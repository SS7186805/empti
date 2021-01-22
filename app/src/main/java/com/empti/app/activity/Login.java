package com.empti.app.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.empti.app.R;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/*
 * Login
 */
public class Login extends AppCompatActivity {

    private TextView tvForgotPasswordLogin, tvError;
    private EditText etLoginPassword, etLoginEmail;
    private Button btLogin;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private TextInputLayout text_input_email_login, text_input_password_login;
    private ImageView ivBackArrow;
    private String userId, email, bearerToken;
    CustomProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //call initialize method
        init();
        listener();

        if (getIntent()!= null) {
            String email = getIntent().getStringExtra("email");
            etLoginEmail.setText(email);
        }
    }
    //initialize
    public void init() {
        tvForgotPasswordLogin = findViewById(R.id.tvForgotPasswordLogin);
        tvError = findViewById(R.id.tvError);
        btLogin = findViewById(R.id.btLogin);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        etLoginEmail = findViewById(R.id.etLoginEmail);
        text_input_email_login = findViewById(R.id.text_input_email_login);
        text_input_password_login = findViewById(R.id.text_input_password_login);
        ivBackArrow = findViewById(R.id.ivBackArrow);
        btLogin.setBackgroundColor(getResources().getColor(R.color.colorSignupText));
        pDialog = new CustomProgressDialog(this);
    }
    //validations
    public void checkValidations() {
        if (etLoginEmail.getText().toString().isEmpty()) {
            // tvError.setVisibility(View.VISIBLE);
            // tvError.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            sendLoginDetail();
        }
    }
    public void listener() {
        tvForgotPasswordLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, RecoverPassword.class);
                startActivity(i);
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidations();
            }
        });
        etLoginEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches(emailPattern)) {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorRectangleBackground));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        etLoginEmail.setBackgroundTintList(colorStateList);
                    }
                    text_input_email_login.setDefaultHintTextColor(colorStateList);
                } else {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorAccent));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        etLoginEmail.setBackgroundTintList(colorStateList);
                    }
                    text_input_email_login.setDefaultHintTextColor(colorStateList);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        etLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= 8) {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorRectangleBackground));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        etLoginPassword.setBackgroundTintList(colorStateList);
                    }
                    text_input_password_login.setDefaultHintTextColor(colorStateList);
                    if (etLoginPassword.getText().toString().length() >= 8) {
                        btLogin.setBackgroundColor(getResources().getColor(R.color.colorRectangleBackground));
                    }
                } else {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorAccent));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        etLoginPassword.setBackgroundTintList(colorStateList);
                    }
                    text_input_password_login.setDefaultHintTextColor(colorStateList);
                    if (etLoginPassword.getText().toString().length() >= 8) {
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    //login Api param send
    private void sendLoginDetail() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", etLoginEmail.getText().toString());
            jsonObject.put("password", etLoginPassword.getText().toString());
            pDialog.setUpDialog();
            login(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Api of login
    private void login(JSONObject jsonObject) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
        Call<ResponseBody> call = RetrofitClient.getRetrofitClient().login(requestBody);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pDialog.dismissDialog();
                if (response.isSuccessful()) {
                    JSONObject object = null;
                    Log.e("mResponse", "onResponse: " + response.body().toString());
                    try {
                        object = new JSONObject(response.body().string());
                        if (object.getInt("status") == 1) {
                            Toast.makeText(Login.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            JSONObject object1 = object.getJSONObject("data");
                            SharedPrefferenceHandler.storeAndParseJsonData(Login.this, object1);
                            Intent i = new Intent(Login.this, MapsActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            // Toast.makeText(Login.this,object.getString("message"),Toast.LENGTH_SHORT).show();
                            tvError.setVisibility(View.VISIBLE);
                            tvError.setText(object.getString("message"));
                            tvError.setTextColor(getResources().getColor(R.color.colorRed));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        pDialog.dismissDialog();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismissDialog();
                Toast.makeText(Login.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
