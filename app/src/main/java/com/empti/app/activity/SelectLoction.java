package com.empti.app.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.empti.app.R;

import java.util.List;

public class SelectLoction extends Activity {
    private RelativeLayout rlUseLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_location);
        //call initialize method
        init();
        listener();
    }

    public void init() {
        rlUseLocation = findViewById(R.id.rlUseLocation);
    }

    public void callPermision() {
        TedPermission.with(SelectLoction.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Intent i = new Intent(SelectLoction.this, MapsActivity.class);
            startActivity(i);
            // Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(SelectLoction.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    public void listener() {
        rlUseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPermision();
            }
        });
    }
}
