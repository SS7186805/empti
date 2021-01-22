package com.empti.app.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.empti.app.activity.Login;
import com.empti.app.activity.SettingActivity;
import com.empti.app.activity.SignUp;
import com.empti.app.activity.TermsAndConditionsActivity;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.graphics.drawable.DrawableCompat;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.empti.app.R;
import com.empti.app.activity.Information;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/*
 * create email or password for simple signup
 * */

public class BasicInfoFragment extends Fragment {
    private Button btNextBasiclInfo;
    private String email, password;
    private TextView termsAndConditions;
    CustomProgressDialog pDialog;
    private Call<ResponseBody> call;
    private EditText etEmailSignUp, etPasswordSignup, etConfirmPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    TextInputLayout text_input_email, text_input_password, text_input_confirmpass;
    private boolean emailmatch, confirmpass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.basic_information, parentViewGroup, false);
        init(rootView);
        listener();
        pDialog = new CustomProgressDialog(getActivity());
        return rootView;
    }
    // initialize
    public void init(View view) {
        termsAndConditions = view.findViewById(R.id.tvTerms);
        text_input_email = view.findViewById(R.id.text_input_email);
        btNextBasiclInfo = view.findViewById(R.id.btNextBasiclInfo);
        etEmailSignUp = view.findViewById(R.id.etEmailSignUp);
        etPasswordSignup = view.findViewById(R.id.etPasswordSignup);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        text_input_password = view.findViewById(R.id.text_input_password);
        text_input_confirmpass = view.findViewById(R.id.text_input_confirmpass);
        btNextBasiclInfo.setBackground(getResources().getDrawable(R.drawable.blackbutton));
    }
    private void setStyleForTextForAutoComplete(int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(text_input_email.getBackground());
        DrawableCompat.setTint(wrappedDrawable, color);
        text_input_email.setBackgroundDrawable(wrappedDrawable);
    }
    //on click listner
    public void listener() {
        if (emailmatch) {}
        btNextBasiclInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkvalidations();
            }
        });
        etEmailSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void
            onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches(emailPattern)) {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorRectangleBackground));
                    etEmailSignUp.setBackgroundTintList(colorStateList);
                    text_input_email.setDefaultHintTextColor(colorStateList);
                    emailmatch = true;
                } else {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorAccent));
                    etEmailSignUp.setBackgroundTintList(colorStateList);
                    text_input_email.setDefaultHintTextColor(colorStateList);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        etPasswordSignup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= 8) {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorRectangleBackground));
                    etPasswordSignup.setBackgroundTintList(colorStateList);
                    text_input_password.setDefaultHintTextColor(colorStateList);
                    if (etConfirmPassword.getText().toString().matches(etPasswordSignup.getText().toString())) {
                        btNextBasiclInfo.setBackgroundColor(getResources().getColor(R.color.colorRectangleBackground));
                    }
                } else {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorAccent));
                    etPasswordSignup.setBackgroundTintList(colorStateList);
                    text_input_password.setDefaultHintTextColor(colorStateList);
                    if (!etConfirmPassword.getText().toString().matches(etPasswordSignup.getText().toString())) {
                        btNextBasiclInfo.setBackgroundColor(getResources().getColor(R.color.colorSignupText));
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches(etPasswordSignup.getText().toString())) {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorRectangleBackground));
                    etConfirmPassword.setBackgroundTintList(colorStateList);
                    text_input_confirmpass.setDefaultHintTextColor(colorStateList);
                    confirmpass = true;
                    if (etConfirmPassword.getText().toString().matches(etPasswordSignup.getText().toString())) {
                        btNextBasiclInfo.setBackground(getResources().getDrawable(R.drawable.rectangal));
                    }
                } else {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorAccent));
                    etConfirmPassword.setBackgroundTintList(colorStateList);
                    text_input_confirmpass.setDefaultHintTextColor(colorStateList);
                    if (!etConfirmPassword.getText().toString().matches(etPasswordSignup.getText().toString())) {
                        btNextBasiclInfo.setBackground(getResources().getDrawable(R.drawable.blackbutton));
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        termsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TermsAndConditionsActivity.class);
                startActivity(intent);
            }
        });
    }
    //validations to check the feild
    private void checkvalidations() {
        if (etEmailSignUp.getText().toString().isEmpty()) {
            etEmailSignUp.setError("please enter email");
            etEmailSignUp.requestFocus();
            return;
        } else if (!etEmailSignUp.getText().toString().matches(emailPattern)) {
            etEmailSignUp.setError("Invalid email");
            etEmailSignUp.requestFocus();
            return;
        } else if (etPasswordSignup.getText().toString().isEmpty()) {
            etPasswordSignup.setError("please enter password");
            etPasswordSignup.requestFocus();
            return;
        } else if (etPasswordSignup.getText().toString().length() < 6) {
            etPasswordSignup.requestFocus();
            etPasswordSignup.setError("password should be atleast 6 characters");
            return;
        } else if (etConfirmPassword.getText().toString().isEmpty()) {
            etConfirmPassword.setError("please enter confirm password");
            etConfirmPassword.requestFocus();
            return;
        } else if (!etPasswordSignup.getText().toString().equals(etConfirmPassword.getText().toString())) {
            etConfirmPassword.setError("confirm password does not match");
            etConfirmPassword.requestFocus();
            return;
        } else {
            ((Information) getActivity()).setProgressStatus(50);
           // loadfragment();
            String email = etEmailSignUp.getText().toString();
            checkEmail(email);
        }
    }

    private  void checkEmail(String email){
        JSONObject object = new JSONObject();
//        HashMap<String , RequestBody> map = new HashMap<>();
        try {
            pDialog.setUpDialog();
            object.put("email",etEmailSignUp.getText().toString());
          //  map.put("email",RequestBody.create(MediaType.parse("multipart/form-data"),etEmailSignUp.getText().toString()));

            final RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), object.toString());
            call = RetrofitClient.getRetrofitClient().checkEmail(requestBody);

            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    pDialog.dismissDialog();
                    if (response.isSuccessful()){
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response.body().string());
                            if (object.getInt("status")== 1){
//                                Toast.makeText(getActivity(),"Success message", Toast.LENGTH_SHORT).show();
                                loadfragment();
                            }else if(object.getInt("status")== 0){
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                // Setting Dialog Title
                                alertDialog.setTitle(Html.fromHtml("<font color='#FF888888'>Email in Use!</font>"));
                                // outside cancel false
                                alertDialog.setCancelable(false);

                                // Setting Dialog Message
                                alertDialog.setMessage("This email is already linked to an account. Please login or enter a different email.");
                                // On pressing Settings button
                                alertDialog.setPositiveButton(Html.fromHtml("<font color='#319839'>Go to login</font>"), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(getContext(), Login.class);
                                        i.putExtra("email",etEmailSignUp.getText().toString());
                                        startActivity(i);
                                    }
                                });
                                // on pressing cancel button
                                alertDialog.setNegativeButton(Html.fromHtml("<font color='#319839'>Cancel</font>"), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alertDialog.show();
//                                Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e){
                            pDialog.dismissDialog();
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pDialog.dismissDialog();
                }
            });

        }catch (Exception e){
            pDialog.dismissDialog();
            e.printStackTrace();
        }
    }
    //fragment transaction from this to another
    private void loadfragment() {
        email = etEmailSignUp.getText().toString();
        password = etPasswordSignup.getText().toString();
        Fragment fr = new PersonalInformation();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
        Bundle arg = new Bundle();
        arg.putString("emailSignup", email);
        arg.putString("passwordSignup", password);
        fr.setArguments(arg);
        ft.replace(R.id.flInfo, fr);
        ft.commit();
    }
}
