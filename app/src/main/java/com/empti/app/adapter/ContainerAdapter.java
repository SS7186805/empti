package com.empti.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.empti.app.R;
import com.empti.app.model.ContainerDetail;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.ImageUtils;

import java.util.ArrayList;

public class ContainerAdapter extends RecyclerView.Adapter<ContainerAdapter.MyViewHolder> {
    private View view;
    LayoutInflater layoutInflater;
    ArrayList<ContainerDetail> modelArr = new ArrayList<>();
    Context context;

    public ContainerAdapter(Context context, ArrayList<ContainerDetail> modelArr) {
        this.context = context;
        this.modelArr = modelArr;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ContainerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.container_adap, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContainerAdapter.MyViewHolder holder, int position) {
        Log.e("image url", Constant.CONTAINER_IMAGE + modelArr.get(position).getPhoto());
        ImageUtils.displayImageFromUrl(context, Constant.CONTAINER_IMAGE + modelArr.get(position).getPhoto(), holder.container_img, null);
        holder.avaliable_container.setText(modelArr.get(position).getAvailable_container() + " Left");
    }

    @Override
    public int getItemCount() {
        return modelArr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView container_img;
        TextView avaliable_container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            container_img = itemView.findViewById(R.id.container_img);
            avaliable_container = itemView.findViewById(R.id.avaliable_container);
        }
    }
}
