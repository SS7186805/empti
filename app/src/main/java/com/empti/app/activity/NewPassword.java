package com.empti.app.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.empti.app.R;

import static android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest.newInstance;

public class NewPassword extends AppCompatActivity {
    private Button btSetPassword;
    private ImageView ivBackArrow;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_pasword);
        // call initialize method
        init();
        listener();
    }

    //initialize
    public void init() {
        btSetPassword = findViewById(R.id.btSetPassword);
        ivBackArrow = findViewById(R.id.ivBackArrow);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Reset Password");
    }

    public void listener() {
        btSetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
