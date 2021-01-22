package com.empti.app.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.empti.app.R;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.webservices.RetrofitClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class RecoverPassword extends AppCompatActivity {

    private Button btRecoverPassword;
    private ImageView ivBackArrow;
    private TextView tvTitle;
    private EditText etEmailRecover;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    CustomProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recover_password);
        //call initialize method
        init();
        listner();
    }

    //initialize
    public void init() {
        btRecoverPassword = findViewById(R.id.btRecoverPassword);
        ivBackArrow = findViewById(R.id.ivBackArrow);
        etEmailRecover = findViewById(R.id.etEmailRecover);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Forgot Password");
        pDialog = new CustomProgressDialog(this);
    }

    public void listner() {
        btRecoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkvalidation();
            }
        });
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void checkvalidation() {
        if (etEmailRecover.getText().toString().isEmpty()) {
            etEmailRecover.setError("please enter email address");
        } else if (!etEmailRecover.getText().toString().matches(emailPattern)) {
            etEmailRecover.setError("please enter valid email address");
        } else {
            sendPasswordDetail();
        }
    }

    public void sendPasswordDetail() {
        Map<String, String> object = new HashMap<>();
        try {
            object.put("email", etEmailRecover.getText().toString());
            pDialog.setUpDialog();
            recoverPassword(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recoverPassword(Map<String, String> object) {
        // RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/x-www-form-urlencoded"), object.toString());
        Call<ResponseBody> call = RetrofitClient.getRetrofitClient().recoverPassword(object);
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
//                            Toast.makeText(RecoverPassword.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RecoverPassword.this,
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
        TextView title = (TextView) dialog.findViewById(R.id.tvTitle);
        TextView message = (TextView) dialog.findViewById(R.id.tvMessage);
        title.setText("New password sent");
        message.setText("Please check the entered email for new password.");
        Button dialogButton = (Button) dialog.findViewById(R.id.okayBtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecoverPassword.this, ResetPasswordActivity.class);
                startActivity(i);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
