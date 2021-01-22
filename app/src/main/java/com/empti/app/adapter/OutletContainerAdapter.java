package com.empti.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.empti.app.R;
import com.empti.app.allinterface.OutletContainerInterface;
import com.empti.app.model.OutletData;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.ImageUtils;

import java.util.ArrayList;

public class OutletContainerAdapter extends RecyclerView.Adapter<OutletContainerAdapter.MyViewHolder> {

    private View view;
    LayoutInflater layoutInflater;
    ArrayList<OutletData> modelArr = new ArrayList<>();
    OutletContainerInterface callBack;
    Context context;

    public OutletContainerAdapter(Context context, ArrayList<OutletData> modelArr, OutletContainerInterface callBack) {
        this.modelArr = modelArr;
        this.context = context;
        this.callBack = callBack;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.outlet_container_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.container_name.setText(modelArr.get(position).getName());
        holder.item_count.setText(modelArr.get(position).getTotal_container());
        ImageUtils.displayImageFromUrl(context, Constant.CONTAINER_IMAGE + modelArr.get(position).getPhoto(), holder.container_img, null);
        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.clickView(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelArr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView container_img;
        TextView container_name, item_count;
        RelativeLayout main_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            container_img = itemView.findViewById(R.id.container_img);
            container_name = itemView.findViewById(R.id.container_name);
            item_count = itemView.findViewById(R.id.item_count);
            main_layout = itemView.findViewById(R.id.main_layout);
        }
    }
}
