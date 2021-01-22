package com.empti.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.empti.app.R;
import com.empti.app.allinterface.OnLeaderClick;
import com.empti.app.model.LeaderboardModel;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.ImageUtils;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private View view;
    private Context context;
    LayoutInflater layoutInflater;
    List<LeaderboardModel.AllLeader> arrayList;
    List<LeaderboardModel.LoggedInUser> leaderboardModelFull;
    LeaderboardModel.LoggedInUser leaderboardLoggedIn;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    String fName;
    private OnLeaderClick onLeaderClick;

    public LeaderboardAdapter(Context context, List<LeaderboardModel.AllLeader> arrayList, LeaderboardModel.LoggedInUser leaderboardLoggedIn,OnLeaderClick onLeaderClick) {
        this.context = context;
        this.arrayList = arrayList;
        this.leaderboardLoggedIn = leaderboardLoggedIn;
        this.onLeaderClick = onLeaderClick;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            view = layoutInflater.inflate(R.layout.leaderboard_view_item, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof MyViewHolder) {
            if (arrayList.get(position).getFirstName() != null) {
                ((MyViewHolder) viewHolder).userName.setText(arrayList.get(position).getFirstName().toString() + " " + arrayList.get(position).getLastName().toString());
            }
            ((MyViewHolder) viewHolder).count.setText(arrayList.get(position).getTotalPickups().toString());
            ImageUtils.displayImageFromUrl(context, Constant.USER_IMAGE + arrayList.get(position).getImage(), ((MyViewHolder) viewHolder).userPic, ((MyViewHolder) viewHolder).userName.getResources().getDrawable(R.drawable.test));
            if (arrayList.get(position).getLastBadge() != null) {
                ((MyViewHolder) viewHolder).badgePic.setVisibility(View.VISIBLE);
                ImageUtils.displayImageFromUrl(context, Constant.BATCHES_IMAGE_BASE_URL + arrayList.get(position).getLastBadge().getImage(), ((MyViewHolder) viewHolder).badgePic, null);
            } else {
                ((MyViewHolder) viewHolder).badgePic.setVisibility(View.GONE);
            }

            ((MyViewHolder) viewHolder).itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLeaderClick.leaderClick(arrayList.get(position).getId());
                }
            });
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return arrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView userName, count;
        private ImageView userPic, badgePic;
        private RelativeLayout itemLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            count = itemView.findViewById(R.id.count);
            userPic = itemView.findViewById(R.id.userPic);
            badgePic = itemView.findViewById(R.id.badgePic);
            itemLayout = itemView.findViewById(R.id.itemLayout);
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
}

