package com.empti.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.empti.app.R;
import com.empti.app.allinterface.SelectContainerInterface;
import com.empti.app.model.DetailModel;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.ImageUtils;

import java.util.ArrayList;

public class AllContainerAdapterForFilter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private View view;
    LayoutInflater layoutInflater;
    ArrayList<DetailModel> modelArr = new ArrayList<>();
    Context context;
    String containerName;
    String containerImg;
    SelectContainerInterface callBack;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    boolean isSwipe;

    public AllContainerAdapterForFilter(Context context, ArrayList<DetailModel> modelArr, SelectContainerInterface callBack, boolean isSwipe) {
        this.modelArr = modelArr;
        this.context = context;
        this.callBack = callBack;
        this.isSwipe = isSwipe;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            view = layoutInflater.inflate(R.layout.all_container_layout_item, parent, false);
            return new AllContainerAdapterForFilter.MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new AllContainerAdapterForFilter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof AllContainerAdapterForFilter.MyViewHolder) {
            populateItemRows((AllContainerAdapterForFilter.MyViewHolder) viewHolder, position);
        } else if (viewHolder instanceof AllContainerAdapterForFilter.LoadingViewHolder) {
            showLoadingView((AllContainerAdapterForFilter.LoadingViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return modelArr.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return modelArr == null ? 0 : modelArr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView container_img, info_img, ready_unchecked_img, ready_checked_img;
        TextView container_name, container_status;
        LinearLayout delete_layout, edit_txt;
        SwipeLayout swipe;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            container_img = itemView.findViewById(R.id.container_img);
            container_name = itemView.findViewById(R.id.container_name);
            container_status = itemView.findViewById(R.id.container_status);
            info_img = itemView.findViewById(R.id.info_img);
            delete_layout = itemView.findViewById(R.id.delete_layout);
            edit_txt = itemView.findViewById(R.id.edit_txt);
            swipe = itemView.findViewById(R.id.swipe);
            ready_unchecked_img = itemView.findViewById(R.id.ready_unchecked_img);
            ready_checked_img = itemView.findViewById(R.id.ready_checked_img);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(AllContainerAdapterForFilter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed
    }

  public int selectedAll(){
        int count = 0;
        for (int i =0 ; i<modelArr.size();i++){
            if (modelArr.get(i).getStatus().equalsIgnoreCase("In Use")){
                modelArr.get(i).setReady(true);
                count++;
            }
        }
        notifyDataSetChanged();
        return count;
    }

    public int CountAllInUse(){
        int count = 0;
        for (int i =0 ; i<modelArr.size();i++) {
            if (modelArr.get(i).getStatus() != null) {
                if (modelArr.get(i).getStatus().equalsIgnoreCase("In Use")) {
                    count++;
                }
            }
        }
        return count;
    }


    private void populateItemRows(AllContainerAdapterForFilter.MyViewHolder holder, int position) {
        holder.container_name.setText(modelArr.get(position).getName());
        holder.container_status.setText(modelArr.get(position).getStatus());
        holder.swipe.setRightSwipeEnabled(true);
        if (modelArr.get(position).getStatus().equalsIgnoreCase("In Use")) {
            if (modelArr.get(position).isReady()) {
                holder.swipe.setRightSwipeEnabled(false);
                holder.ready_checked_img.setVisibility(View.VISIBLE);
                holder.ready_unchecked_img.setVisibility(View.GONE);
                holder.ready_checked_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.swipe.setRightSwipeEnabled(true);
                        holder.ready_checked_img.setVisibility(View.GONE);
                        holder.ready_unchecked_img.setVisibility(View.VISIBLE);
                        modelArr.get(position).setReady(false);
                        callBack.clickInfoView(position, "", false);
                        notifyItemChanged(position);
                    }
                });
            } else {
                holder.swipe.setRightSwipeEnabled(true);
                holder.ready_checked_img.setVisibility(View.GONE);
                holder.ready_unchecked_img.setVisibility(View.VISIBLE);
                holder.ready_unchecked_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.swipe.setRightSwipeEnabled(false);
                        holder.ready_checked_img.setVisibility(View.VISIBLE);
                        holder.ready_unchecked_img.setVisibility(View.GONE);
                        modelArr.get(position).setReady(true);
                        callBack.clickInfoView(position, "", true);
                        notifyItemChanged(position);
                    }
                });
            }
        } else {
            holder.ready_checked_img.setVisibility(View.GONE);
            holder.ready_unchecked_img.setVisibility(View.GONE);
        }
        if (modelArr.get(position).getStatus().equalsIgnoreCase("Ready to Use")) {
            holder.swipe.setRightSwipeEnabled(true);
            holder.container_status.setTextColor(context.getResources().getColor(android.R.color.holo_orange_light));
        } else if (modelArr.get(position).getStatus().equalsIgnoreCase("In Use")) {
            holder.swipe.setRightSwipeEnabled(false);
            holder.container_status.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.container_status.setTextColor(context.getResources().getColor(R.color.colorToolbarTitle));
        }
        ImageUtils.displayImageFromUrl(context, Constant.CONTAINER_IMAGE + modelArr.get(position).getPhoto(), holder.container_img, null);
        holder.info_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.clickView(position);
            }
        });
        holder.delete_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.deltetView(modelArr.get(position).getOutlet_containers_id(), position);
            }
        });
        holder.edit_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.editView(position);
                holder.swipe.close();
            }
        });
    }
}
