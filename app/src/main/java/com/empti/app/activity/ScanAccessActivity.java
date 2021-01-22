package com.empti.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.empti.app.R;

public class ScanAccessActivity extends Activity {
    Button allow_btn, cancel_btn;
    ImageView back_img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_access_layout);
        init();
        setListners();
    }

    public void init() {
        back_img = findViewById(R.id.back_img);
        allow_btn = findViewById(R.id.allow_btn);
        cancel_btn = findViewById(R.id.cancel_btn);
    }

    public void setListners() {
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        allow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScanAccessActivity.this, FullScannerActivity.class);
                i.putExtra("type", "true");
                startActivityForResult(i, 101);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Intent i = new Intent();
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        }
    }
}
