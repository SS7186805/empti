package com.empti.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.empti.app.R;
import com.empti.app.allinterface.SelectContainerInterface;
import com.empti.app.model.ContainerListModel;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.ImageUtils;

import java.util.ArrayList;

public class ContainerList extends RecyclerView.Adapter<ContainerList.MyViewHolder> {

    private View view;
    LayoutInflater layoutInflater;
    ArrayList<ContainerListModel> modelArr = new ArrayList<>();
    Context context;
    SelectContainerInterface callBack;

    public ContainerList(Context context, ArrayList<ContainerListModel> modelArr, SelectContainerInterface callBack) {
        this.context = context;
        this.modelArr = modelArr;
        this.callBack = callBack;
        layoutInflater = LayoutInflater.from(context);
    }

    public void notify(ArrayList<ContainerListModel> modelArr) {
        this.modelArr = modelArr;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.container_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (modelArr.get(position).isSelected()) {
            holder.container_img.setBackground(context.getResources().getDrawable(R.drawable.shape_circle_green));
            holder.container_name.setTextColor(context.getResources().getColor(R.color.appColor));
        } else {
            holder.container_img.setBackground(context.getResources().getDrawable(R.drawable.shape_circle));
            holder.container_name.setTextColor(context.getResources().getColor(R.color.colorToolbarTitle));
        }
        holder.container_name.setText(modelArr.get(position).getName());
        ImageUtils.displayImageFromUrl(context, Constant.CONTAINER_IMAGE + modelArr.get(position).getPhoto(), holder.container_img, null);
        holder.container_img.setOnClickListener(new View.OnClickListener() {
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
        TextView container_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            container_img = itemView.findViewById(R.id.container_img);
            container_name = itemView.findViewById(R.id.container_name);
        }
    }
}

