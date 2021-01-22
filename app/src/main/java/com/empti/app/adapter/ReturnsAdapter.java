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

public class ReturnsAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflter;
    ArrayList<BatchesModel.Return> returnArrayList;

    public ReturnsAdapter(Context applicationContext, ArrayList<BatchesModel.Return> returnArrayList) {
        this.context = applicationContext;
        this.returnArrayList = returnArrayList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return returnArrayList.size();
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
        TextView scanLebel = view.findViewById(R.id.scanLabel);
        ImageView fade = view.findViewById(R.id.fade);
        ImageView lock = view.findViewById(R.id.lock);
        scanLebel.setText(returnArrayList.get(i).getValue().toString() + " RETURNS");
        ImageUtils.displayImageFromUrl(context, Constant.BATCHES_IMAGE_BASE_URL + returnArrayList.get(i).getImage(), oneScan, null);
        if (returnArrayList.get(i).getIsOccupied() == 0) {
            fade.setVisibility(View.VISIBLE);
            lock.setVisibility(View.VISIBLE);
        } else {
            fade.setVisibility(View.GONE);
            lock.setVisibility(View.GONE);
        }
        return view;
    }
}
