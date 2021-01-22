package com.empti.app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.empti.app.R;
import com.empti.app.fragment.BasicInfoFragment;
import com.empti.app.fragment.PersonalInformation;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.List;

public class Information extends FragmentActivity {
    private FrameLayout flInfo;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private boolean isSocial;
    private String fbfrstName, fblastName, email;
    private ImageView ivBack;
    private String socialToken = "", type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_signup);
        //call initialize method
        init();
        isSocial = getIntent().getExtras().getBoolean("isSocial");
        Log.e("soooo", "onCreate: " + isSocial);
        if (!isSocial) {
            loadFragment(new BasicInfoFragment());
        } else {
            fbfrstName = getIntent().getExtras().getString("fbFirstName");
            fblastName = getIntent().getExtras().getString("fbLastName");
            email = getIntent().getExtras().getString("fbEmail");
            type = getIntent().getExtras().getString("type");
            if (getIntent().getExtras().getString("type").equalsIgnoreCase("google")) {
                socialToken = getIntent().getExtras().getString("google_token");
            } else {
                socialToken = getIntent().getExtras().getString("facebook_token");
            }
            loadFragment(new PersonalInformation());
        }
    }
    // initialize method
    public void init() {
        flInfo = findViewById(R.id.flInfo);
        progressBar = findViewById(R.id.my_progressBar);
        ivBack = findViewById(R.id.ivBack);
        setProgressStatus(50);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalInformation f = (PersonalInformation) getSupportFragmentManager().findFragmentByTag(PersonalInformation.class.getName());
                if (getVisibleFragment() instanceof PersonalInformation) {
                    Log.e("frag1", "onClick: ");
                    setProgressStatus(50);
//                    loadFragment(new BasicInfoFragment());
                }
                onBackPressed();

            }
        });
    }
    public void setProgressStatus(int value) {
        progressBar.setProgress(value);
    }
    private Fragment getVisibleFragment() {
        FragmentManager fragmentManager = Information.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }
    //fragment transaction
    private void loadFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("firstName", fbfrstName);
        bundle.putString("lastname", fblastName);
        bundle.putString("email", email);
        bundle.putString("token", socialToken);
        bundle.putString("type", type);
        bundle.putBoolean("isSocial", isSocial);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(R.id.flInfo, fragment);
        transaction.commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                File file = new File(resultUri.getPath());
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                PersonalInformation.getInstance().setImage(file, bitmap);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
