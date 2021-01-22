package com.empti.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.empti.app.R;
import com.empti.app.model.BatchesModel;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.ImageUtils;

import java.util.ArrayList;

public class StreaksAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflter;
    ArrayList<BatchesModel.Streak> streakArrayList;


    public StreaksAdapter(Context applicationContext, ArrayList<BatchesModel.Streak> streakArrayList) {
        this.context = applicationContext;
        this.streakArrayList = streakArrayList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return streakArrayList.size();
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
        ImageView lock = view.findViewById(R.id.lock);
        TextView scanLebel = view.findViewById(R.id.scanLabel);
        scanLebel.setText(streakArrayList.get(i).getValue().toString() + " STREAKS");
        ImageUtils.displayImageFromUrl(context, Constant.BATCHES_IMAGE_BASE_URL + streakArrayList.get(i).getImage(), oneScan, null);
        if (streakArrayList.get(i).getIsOccupied() == 0) {
            fade.setVisibility(View.VISIBLE);
            lock.setVisibility(View.VISIBLE);
        } else {
            fade.setVisibility(View.GONE);
            lock.setVisibility(View.GONE);
        }
        return view;
    }
}
