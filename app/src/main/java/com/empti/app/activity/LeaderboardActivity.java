package com.empti.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.empti.app.R;
import com.empti.app.adapter.LeaderboardAdapter;
import com.empti.app.allinterface.OnLeaderClick;
import com.empti.app.model.LeaderboardModel;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.ImageUtils;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardActivity extends Activity implements OnLeaderClick {

    private TextView tvTitle, userName, totalpickups;
    private ImageView ivBackArrow, myBadgePic, userPic;
    private RecyclerView leaderboardView;
    CustomProgressDialog pDialog;
    boolean isLoading = false;
    int page = 1;
    LeaderboardAdapter leaderboardAdapter;
    List<LeaderboardModel.AllLeader> arrayList;
    List<LeaderboardModel.LoggedInUser> leaderboardModelsFull;
    LeaderboardModel.LoggedInUser leaderboardLoggedIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        initViews();
        initScrollListener();
        callApi();

        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setLeadersView(List<LeaderboardModel.AllLeader> leaderboardModel, LeaderboardModel.LoggedInUser leaderboardLoggedIn) {
        leaderboardAdapter = new LeaderboardAdapter(this, leaderboardModel, leaderboardLoggedIn,this);
        leaderboardView.setAdapter(leaderboardAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        leaderboardView.setLayoutManager(linearLayoutManager);
    }



    private void initViews() {
        pDialog = new CustomProgressDialog(this);
        pDialog.setUpDialog();
        tvTitle = findViewById(R.id.tvTitle);
        leaderboardView = findViewById(R.id.leaderboardView);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("Leaderboard");
        tvTitle.setTextSize(18);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setTextColor(Color.parseColor("#000000"));
        ivBackArrow = findViewById(R.id.ivBackArrow);

        userPic = findViewById(R.id.myPic);
        myBadgePic = findViewById(R.id.myBadgePic);
        userName = findViewById(R.id.myName);
        totalpickups = findViewById(R.id.pickups);
        arrayList = new ArrayList<>();
        leaderboardModelsFull = new ArrayList<>();


    }

    public void initScrollListener() {
        leaderboardView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == arrayList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    public void callApi() {
        if (arrayList.size() > 0) {
            arrayList.clear();
        }
        isLoading = false;
        callLeaderboardApi();
    }

    private void loadMore() {
        arrayList.add(null);
        leaderboardAdapter.notifyItemInserted(arrayList.size() - 1);
        callLeaderboardApi();
    }

    private void callLeaderboardApi() {
        try {
            Call<LeaderboardModel> call = RetrofitClient.getRetrofitClient().getLeadersList("Bearer" + " " + SharedPrefferenceHandler.retriveData(LeaderboardActivity.this, Constant.BEARER_TOKEN)
                    , String.valueOf(page));
            call.enqueue(new Callback<LeaderboardModel>() {
                @SuppressWarnings("UseBulkOperation")
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<LeaderboardModel> call, Response<LeaderboardModel> response) {
                    if (response.isSuccessful()) {
                        if (!isLoading) {
                            pDialog.dismissDialog();
                        }
                        pDialog.dismissDialog();
                        LeaderboardModel leaderboardModel = response.body();
                        leaderboardLoggedIn = leaderboardModel.getData().getLoggedInUser();

                        userName.setText(leaderboardModel.getData().getLoggedInUser().getFirstName() + " " + leaderboardModel.getData().getLoggedInUser().getLastName());
                        totalpickups.setText(leaderboardModel.getData().getLoggedInUser().getTotalPickups().toString());
                        ImageUtils.displayImageFromUrl(LeaderboardActivity.this, "https://empti.org/empti/public/images/users/" + leaderboardModel.getData().getLoggedInUser().getImage(), userPic, getResources().getDrawable(R.drawable.test));
                        if (leaderboardModel.getData().getLoggedInUser().getLastBadge() != null) {
                            ImageUtils.displayImageFromUrl(LeaderboardActivity.this, Constant.BATCHES_IMAGE_BASE_URL + leaderboardModel.getData().getLoggedInUser().getLastBadge().getImage(), myBadgePic,null);
                        }
                        if (isLoading) {
                            arrayList.remove(arrayList.size() - 1);
                            int scrollPosition = arrayList.size();
                            leaderboardAdapter.notifyItemRemoved(scrollPosition);
                            for (int i = 0; i < leaderboardModel.getData().getAllLeaders().size(); i++) {
                                arrayList.add(leaderboardModel.getData().getAllLeaders().get(i));

                            }
                            leaderboardAdapter.notifyDataSetChanged();
                            isLoading = false;
                        } else {
                            for (int i = 0; i < leaderboardModel.getData().getAllLeaders().size(); i++) {
                                arrayList.add(leaderboardModel.getData().getAllLeaders().get(i));
                            }
                            setLeadersView(arrayList, leaderboardLoggedIn);
                        }
                        page = page + 1;
                    }
                }

                @Override
                public void onFailure(Call<LeaderboardModel> call, Throwable t) {
                    if (!isLoading) {
                        pDialog.dismissDialog();
                    }
                    Toast.makeText(LeaderboardActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            pDialog.dismissDialog();
            e.printStackTrace();
        }
    }

    @Override
    public void leaderClick(int userId) {

        Intent intent = new Intent(LeaderboardActivity.this,AchievementsActivity.class);
        intent.putExtra("userId",String.valueOf(userId));
        startActivity(intent);
    }
}
