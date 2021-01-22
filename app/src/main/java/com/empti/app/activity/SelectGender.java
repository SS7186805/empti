package com.empti.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.empti.app.R;

public class SelectGender extends Activity implements View.OnClickListener {
    private RelativeLayout rlMale, rlFemale, rlOther;
    private ImageView ivBackGender, ivMale, ivFemale, ivOther;
    private TextView tvMale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_gender);
        //call initialize method
        init();
        Listener();
    }

    //initialize method
    public void init() {
        ivBackGender = findViewById(R.id.ivBackGender);
        rlMale = findViewById(R.id.rlMale);
        rlFemale = findViewById(R.id.rlFemale);
        rlOther = findViewById(R.id.rlOther);
        ivMale = findViewById(R.id.ivMale);
        ivFemale = findViewById(R.id.ivFemale);
        ivOther = findViewById(R.id.ivOther);
        tvMale = findViewById(R.id.tvMale);
    }

    public void Listener() {
        rlMale.setOnClickListener(this);
        rlFemale.setOnClickListener(this);
        rlOther.setOnClickListener(this);
        ivBackGender.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlMale:
                ivMale.setVisibility(View.VISIBLE);
                Intent i = new Intent();
                i.putExtra("gender", "Male");
                setResult(2, i);
                finish();
                ivFemale.setVisibility(View.INVISIBLE);
                ivOther.setVisibility(View.INVISIBLE);
                break;
            case R.id.rlFemale:
                ivFemale.setVisibility(View.VISIBLE);
                Intent i1 = new Intent();
                i1.putExtra("gender", "Female");
                setResult(2, i1);
                finish();
                ivMale.setVisibility(View.INVISIBLE);
                ivOther.setVisibility(View.INVISIBLE);
                break;
            case R.id.rlOther:
                ivOther.setVisibility(View.VISIBLE);
                Intent i2 = new Intent();
                i2.putExtra("gender", "Other");
                setResult(2, i2);
                finish();
                ivFemale.setVisibility(View.INVISIBLE);
                ivMale.setVisibility(View.INVISIBLE);
                break;
            case R.id.ivBackGender:
                onBackPressed();
        }
    }
}