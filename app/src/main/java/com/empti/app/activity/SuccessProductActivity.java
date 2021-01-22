package com.empti.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.empti.app.R;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.ImageUtils;

public class SuccessProductActivity extends Activity {
    ImageView container_img;
    TextView message;
    Button add_more_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_product_layout);
        init();
        setListners();
    }

    public void init() {
        container_img = findViewById(R.id.container_img);
        message = findViewById(R.id.message);
        add_more_btn = findViewById(R.id.add_more_btn);
        message.setText(getIntent().getExtras().getString(Constant.name) + " Added Successfully");
        ImageUtils.displayImageFromUrl(this, Constant.CONTAINER_IMAGE + getIntent().getExtras().getString(Constant.photo), container_img, null);
    }

    public void setListners() {
        add_more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
