package com.empti.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;

import com.empti.app.R;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends Activity {
    LinearLayout change_password, privacy_policy_layout, termsandconditions;
    ImageView back_img;
    String version = "";
    TextView version_txt, tvDeleteAccount;
    CustomProgressDialog pDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        init();
        setListners();
        pDialog = new CustomProgressDialog(this);
    }

    public void init() {
        tvDeleteAccount = findViewById(R.id.tvDeleteAccount);
        version_txt = findViewById(R.id.version_txt);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version_txt.setText("version:- " + version);
        privacy_policy_layout = findViewById(R.id.privacy_policy_layout);
        back_img = findViewById(R.id.back_img);
        change_password = findViewById(R.id.change_password);
        termsandconditions = findViewById(R.id.termsandconditions);
    }

    public void setListners() {
        termsandconditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, PrivacypolicyActivity.class);
                startActivity(i);
            }
        });
        privacy_policy_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, PrivacypolicyActivity.class);
                startActivity(i);
            }
        });
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(i);
            }
        });

        tvDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccountDialog();
            }
        });

    }

    private void deleteAccountDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle(Html.fromHtml("<font color='#FF888888'>Delete Account!</font>"));
        // outside cancel false
        alertDialog.setCancelable(false);

        // Setting Dialog Message
        alertDialog.setMessage("This will delete all of your information from Empti platform. Click Delete to continue.");
        // On pressing Settings button
        alertDialog.setPositiveButton(Html.fromHtml("<font color='#FF0000'>Delete</font>"), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                pDialog.setUpDialog();
                deleteAccount();
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

    private void deleteAccount() {
        try {
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().deleteAccount("Bearer" + " " + SharedPrefferenceHandler.retriveData(SettingActivity.this, Constant.BEARER_TOKEN));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    pDialog.dismissDialog();
                    if (response.isSuccessful()) {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response.body().string());
//                            Toast.makeText(SettingActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            if (object.getInt("status") == 1) {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingActivity.this);
                                // Setting Dialog Title
                                alertDialog.setTitle(Html.fromHtml("<font color='#FF888888'>Account Deleted!</font>"));
                                // outside cancel false
                                alertDialog.setCancelable(false);
                                // Setting Dialog Message
                                alertDialog.setMessage("Your account has been deleted from Empti platform.");
                                // On pressing Settings button
                                alertDialog.setPositiveButton(Html.fromHtml("<font color='#319839'>ok</font>"), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPrefferenceHandler.removeAll(SettingActivity.this);
                                        Intent i = new Intent(SettingActivity.this, SignUp.class);
                                        startActivity(i);
                                        finish();
                                    }
                                });
                                alertDialog.show();
                            } else if (object.getInt("status") == 0) {
                                Toast.makeText(SettingActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        pDialog.dismissDialog();
                        Toast.makeText(SettingActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pDialog.dismissDialog();
                }
            });
        } catch (Exception e) {
            pDialog.dismissDialog();
            e.printStackTrace();
        }
    }
}
