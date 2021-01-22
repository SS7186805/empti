package com.empti.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.empti.app.R;
import com.empti.app.adapter.LeaderboardAdapter;
import com.empti.app.adapter.ReturnsAdapter;
import com.empti.app.adapter.ScansAdapter;
import com.empti.app.adapter.StreaksAdapter;
import com.empti.app.model.BatchesModel;
import com.empti.app.utilities.AlerterMessage;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.ImageUtils;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AchievementsActivity extends Activity {
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView tvTitle, tvTotalItemsCount, tvPendingContainersCount;
    private ImageView ivBackArrow, ivReturned;
    private Handler handler = new Handler();
    CustomProgressDialog pDialog;
    ArrayList<BatchesModel.Return> returnsList;
    ArrayList<BatchesModel.Scan> scansList;
    ArrayList<BatchesModel.Streak> streaksList;
    LinearLayout scansLayout, returnsLayout, streaksLayout;
    GridView scansGridView, returnsGridView, streaksGridView;
    private String userId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            userId = intent.getExtras().getString("userId");
        }
        init();
        pDialog.setUpDialog();
        //       returnedItemsApi();
        allBatches(userId);
        //  getOtherUserBatches();
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#319839"), android.graphics.PorterDuff.Mode.SRC_IN);
        tvTotalItemsCount = (TextView) findViewById(R.id.tvTotalItemsCount);
        tvPendingContainersCount = findViewById(R.id.tvPendingContainersCount);
        ivReturned = findViewById(R.id.ivReturned);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("Achievements");
        tvTitle.setTextSize(18);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setTextColor(Color.parseColor("#000000"));
        ivBackArrow = findViewById(R.id.ivBackArrow);

        scansLayout = findViewById(R.id.scansLayout);
        returnsLayout = findViewById(R.id.returnsLayout);
        streaksLayout = findViewById(R.id.streaksLayout);
        scansGridView = findViewById(R.id.scansGridView);
        returnsGridView = findViewById(R.id.returnsGridView);
        streaksGridView = findViewById(R.id.streaksGridView);
        pDialog = new CustomProgressDialog(this);
    }

    private void returnedItemsApi() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.status, "Returned");
            jsonObject.put(Constant.page_no, "");
            Log.e("json object", jsonObject.toString());
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().myContainerData("Bearer" + " " + SharedPrefferenceHandler.retriveData(AchievementsActivity.this, Constant.BEARER_TOKEN), requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    pDialog.dismissDialog();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            Log.e("mycontainer data", object.toString());
                            if (object.getString("status").equalsIgnoreCase("1")) {
                                String itemCount = object.getString("total_count");
                                int returnedItemCount = Integer.parseInt(itemCount);
                                tvTotalItemsCount.setText(String.valueOf(returnedItemCount));
                                new Thread(new Runnable() {
                                    @SuppressLint("SetTextI18n")
                                    public void run() {
                                        progressBar.setProgress(returnedItemCount);
                                    }
                                }).start();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismissDialog();
                        } catch (IOException e) {
                            e.printStackTrace();
                            pDialog.dismissDialog();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    AlerterMessage.message(AchievementsActivity.this, getResources().getString(R.string.error), getString(R.string.something_went_wrong), (R.color.errorColor), (R.drawable.ic_error_outline_white_24dp));
//                    Toast.makeText(AchievementsActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                    pDialog.dismissDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            pDialog.dismissDialog();
        }
    }

    private void getOtherUserBatches() {
        try {
            Call<BatchesModel> call = RetrofitClient.getRetrofitClient().getOtherBatchUser("Bearer" + " " + SharedPrefferenceHandler.retriveData(AchievementsActivity.this, Constant.BEARER_TOKEN), SharedPrefferenceHandler.retriveData(AchievementsActivity.this, Constant.USER_ID));
            call.enqueue(new Callback<BatchesModel>() {
                @Override
                public void onResponse(Call<BatchesModel> call, Response<BatchesModel> response) {
                    if (response.isSuccessful()) {
                        try {
                            pDialog.dismissDialog();
                            BatchesModel batchesModel = response.body();
                            assert response.body() != null;
                            Log.e("other user batch data", response.body().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BatchesModel> call, Throwable t) {
//                    Toast.makeText(AchievementsActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    AlerterMessage.message(AchievementsActivity.this, getString(R.string.error), getString(R.string.something_went_wrong), (R.color.errorColor), (R.drawable.ic_error_outline_white_24dp));
                    pDialog.dismissDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void allBatches(String userId) {
        try {
            Call<BatchesModel> call = RetrofitClient.getRetrofitClient().getBatches("Bearer" + " " + SharedPrefferenceHandler.retriveData(AchievementsActivity.this, Constant.BEARER_TOKEN), userId);
            call.enqueue(new Callback<BatchesModel>() {
                @Override
                public void onResponse(Call<BatchesModel> call, Response<BatchesModel> response) {
                    pDialog.dismissDialog();
                    if (response.isSuccessful()) {
                        try {
                            assert response.body() != null;
                            Log.e("response body ", response.body().toString());
                            BatchesModel batchesModel = response.body();

                            ImageUtils.displayImageFromUrl(AchievementsActivity.this, Constant.BATCHES_IMAGE_BASE_URL + batchesModel.getData().getNextReturnBatch().getImage(), ivReturned, null);
                            int returnedItemCount = batchesModel.getData().getTotalReturns();
                            int leftContainers = batchesModel.getData().getNextReturnBatch().getValue() - batchesModel.getData().getTotalReturns();
                            tvTotalItemsCount.setText(String.valueOf(returnedItemCount));
                            tvPendingContainersCount.setText(String.valueOf(leftContainers + " "));
                            progressBar.setMax(batchesModel.getData().getNextReturnBatch().getValue());
                            new Thread(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                public void run() {
                                    progressBar.setProgress(batchesModel.getData().getTotalReturns());
                                }
                            }).start();
                            returnsList = new ArrayList<>();
                            scansList = new ArrayList<>();
                            streaksList = new ArrayList<>();
                            if (batchesModel.getData() != null && batchesModel.getData().getScans() != null) {
                                for (int i = 0; i < batchesModel.getData().getScans().size(); i++) {
                                    scansList.add(batchesModel.getData().getScans().get(i));
                                }
                                setScansGridView(scansList);
                            }
                            if (batchesModel.getData() != null && batchesModel.getData().getReturns() != null) {
                                for (int i = 0; i < batchesModel.getData().getReturns().size(); i++) {
                                    returnsList.add(batchesModel.getData().getReturns().get(i));
                                }
                                Log.e("Return List", returnsList.toString());
                                setReturnGridView(returnsList);
                            }
                            if (batchesModel.getData() != null && batchesModel.getData().getStreaks() != null) {
                                for (int i = 0; i < batchesModel.getData().getStreaks().size(); i++) {
                                    streaksList.add(batchesModel.getData().getStreaks().get(i));
                                }
                                setStreaksGridView(streaksList);
                            }
                            pDialog.dismissDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BatchesModel> call, Throwable t) {
                    AlerterMessage.message(AchievementsActivity.this, getString(R.string.error), getString(R.string.something_went_wrong), (R.color.errorColor), (R.drawable.ic_error_outline_white_24dp));
//                    Toast.makeText(AchievementsActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                    pDialog.dismissDialog();
                }
            });
        } catch (Exception e) {
            pDialog.dismissDialog();
            e.printStackTrace();
        }
    }

    private void setStreaksGridView(ArrayList<BatchesModel.Streak> streakList) {
        streaksLayout.setVisibility(View.VISIBLE);
        StreaksAdapter streaksAdapter = new StreaksAdapter(this, streakList);
        streaksGridView.setAdapter(streaksAdapter);
    }

    private void setScansGridView(ArrayList<BatchesModel.Scan> scanList) {
        scansLayout.setVisibility(View.VISIBLE);
        ScansAdapter scansAdapter = new ScansAdapter(this, scanList);
        scansGridView.setAdapter(scansAdapter);
    }

    private void setReturnGridView(ArrayList<BatchesModel.Return> returnsList) {
        returnsLayout.setVisibility(View.VISIBLE);
        ReturnsAdapter returnsAdapter = new ReturnsAdapter(this, returnsList);
        returnsGridView.setAdapter(returnsAdapter);
    }
}
