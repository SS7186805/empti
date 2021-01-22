package com.empti.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.empti.app.R;
import com.empti.app.model.BatchesModel;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.ImageUtils;

import java.util.ArrayList;

public class ScansAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflter;
    ArrayList<BatchesModel.Scan> scanArrayList;

    public ScansAdapter(Context applicationContext, ArrayList<BatchesModel.Scan> scanArrayList) {
        this.context = applicationContext;
        this.scanArrayList = scanArrayList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return scanArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.batch_view_item, null); // inflate the layout
        ImageView oneScan = view.findViewById(R.id.oneScan);
        ImageView fade = view.findViewById(R.id.fade);
        TextView scanLebel = view.findViewById(R.id.scanLabel);
        ImageView lock = view.findViewById(R.id.lock);
        scanLebel.setText(scanArrayList.get(i).getValue().toString() + " SCANS");
        ImageUtils.displayImageFromUrl(context, Constant.BATCHES_IMAGE_BASE_URL + scanArrayList.get(i).getImage(), oneScan, null);
        if (scanArrayList.get(i).getIsOccupied() == 0) {
            fade.setVisibility(View.VISIBLE);
            lock.setVisibility(View.VISIBLE);
        } else {
            fade.setVisibility(View.GONE);
            lock.setVisibility(View.GONE);
        }
        return view;
    }
}
