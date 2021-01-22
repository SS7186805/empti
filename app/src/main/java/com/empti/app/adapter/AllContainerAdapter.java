package com.empti.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.empti.app.R;
import com.empti.app.allinterface.SelectContainerInterface;
import com.empti.app.model.DetailModel;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.ImageUtils;
import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.ArrayList;

public class AllContainerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

    public AllContainerAdapter(Context context, ArrayList<DetailModel> modelArr, SelectContainerInterface callBack, boolean isSwipe) {
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
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof MyViewHolder) {
            populateItemRows((MyViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
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
        RoundedImageView store_img;
        RelativeLayout containerLayout;
        TextView container_name, container_status, store_name_txt;
        LinearLayout delete_layout, edit_txt;
        SwipeLayout swipe;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            store_img = itemView.findViewById(R.id.store_img);
            containerLayout = itemView.findViewById(R.id.containerLayout);
            store_name_txt = itemView.findViewById(R.id.store_name_txt);
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

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed
    }

    private void populateItemRows(MyViewHolder holder, int position) {
        holder.container_name.setText(modelArr.get(position).getName());
        holder.container_status.setText(modelArr.get(position).getStatus());
        if (isSwipe) {
            holder.swipe.setRightSwipeEnabled(true);
            if (modelArr.get(position).getStatus().equalsIgnoreCase("Returned")) {
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
                            modelArr.get(position).setReady(true);
                            callBack.clickInfoView(position, "", true);
                        }
                    });
                }
            } else {
                holder.ready_checked_img.setVisibility(View.GONE);
                holder.ready_unchecked_img.setVisibility(View.GONE);
            }
        } else {
            holder.swipe.setRightSwipeEnabled(false);
        }
        if (modelArr.get(position).getStatus().equalsIgnoreCase("Ready to Use")) {
            holder.container_status.setTextColor(context.getResources().getColor(android.R.color.holo_orange_light));
        } else if (modelArr.get(position).getStatus().equalsIgnoreCase("In use")) {
            holder.container_status.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.container_status.setTextColor(context.getResources().getColor(R.color.colorToolbarTitle));
        }
        ImageUtils.displayImageFromUrl(context, Constant.OUTLET_IMAGE + modelArr.get(position).getShop_image(), holder.store_img, context.getResources().getDrawable(R.drawable.dummy_outlet));
        holder.store_name_txt.setText(modelArr.get(position).getShop_name());
        ImageUtils.displayImageFromUrl(context, Constant.CONTAINER_IMAGE + modelArr.get(position).getPhoto(), holder.container_img, null);
        holder.containerLayout.setOnClickListener(new View.OnClickListener() {
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
