package com.empti.app.utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import com.empti.app.R;

public class CustomProgressDialog {
    Context c;
    Dialog mDialog;

    public CustomProgressDialog(Context c) {
        this.c = c;
    }

    public void setUpDialog() {
        mDialog = new Dialog(c);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.progress_dialog);
        mDialog.findViewById(R.id.avi).setVisibility(View.VISIBLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    public void dismissDialog() {
        mDialog.dismiss();
    }
}
