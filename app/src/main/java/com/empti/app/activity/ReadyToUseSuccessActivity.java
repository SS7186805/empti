package com.empti.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.empti.app.R;
import com.empti.app.model.NewBadgeModel;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.ImageUtils;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadyToUseSuccessActivity extends Activity {
    private Button done_btn, clostBtn;
    private TextView paymentReceipt, message, tvInfo,tvRemember;
    private ImageView container_img, badgeOne, badgeTwo, badgeThree;
    private String receiptUrl, shopname, shopLogo, bList,ContainerName;
    private RelativeLayout parent, confettiView;
    private NewBadgeModel.NewBadge badgeArray;
    private KonfettiView konfettiView;
    CustomProgressDialog pDialog;
    ArrayList<NewBadgeModel.NewBadge> newBadgeList;
    Handler handler = new Handler();
    String Return;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ready_to_use_success);
        initViews();
        initConfettiView();
        Intent intent = getIntent();
        // Bundle bundle = intent.getExtras();
        if (intent != null) {
            //  badgeArray = new ArrayList();
            newBadgeList = new ArrayList<>();
            Bundle bundle = intent.getExtras();
            receiptUrl = intent.getExtras().getString("receiptUrl");
            shopname = getIntent().getExtras().getString("shopName");
            ContainerName = getIntent().getExtras().getString("containerName");
            shopLogo = getIntent().getExtras().getString("shopLogo");
            Return = getIntent().getExtras().getString("return")!=null?"return":"null";
//            Log.i("shopLogo",shopLogo);

            assert bundle != null;
            newBadgeList = (ArrayList<NewBadgeModel.NewBadge>) bundle.getSerializable("badgeListBundle");
            if (newBadgeList != null) {
                if (newBadgeList.size() == 1) {
                    confettiView.setVisibility(View.VISIBLE);
                    ImageUtils.displayImageFromUrl(this, Constant.BATCHES_IMAGE_BASE_URL + newBadgeList.get(0).getImage(), badgeOne, null);
                    badgeOne.setVisibility(View.VISIBLE);
                    badgeTwo.setVisibility(View.GONE);
                    badgeThree.setVisibility(View.GONE);
                } else if (newBadgeList.size() == 2) {
                    confettiView.setVisibility(View.VISIBLE);
                    badgeOne.setVisibility(View.VISIBLE);
                    ImageUtils.displayImageFromUrl(this, Constant.BATCHES_IMAGE_BASE_URL + newBadgeList.get(0).getImage(), badgeOne, null);
                    badgeTwo.setVisibility(View.VISIBLE);
                    ImageUtils.displayImageFromUrl(this, Constant.BATCHES_IMAGE_BASE_URL + newBadgeList.get(1).getImage(), badgeTwo, null);
                    badgeThree.setVisibility(View.GONE);
                } else if (newBadgeList.size() == 3) {
                    confettiView.setVisibility(View.VISIBLE);
                    badgeOne.setVisibility(View.VISIBLE);
                    ImageUtils.displayImageFromUrl(this, Constant.BATCHES_IMAGE_BASE_URL + newBadgeList.get(0).getImage(), badgeOne, null);
                    badgeTwo.setVisibility(View.VISIBLE);
                    ImageUtils.displayImageFromUrl(this, Constant.BATCHES_IMAGE_BASE_URL + newBadgeList.get(1).getImage(), badgeTwo, null);
                    badgeThree.setVisibility(View.VISIBLE);
                    ImageUtils.displayImageFromUrl(this, Constant.BATCHES_IMAGE_BASE_URL + newBadgeList.get(2).getImage(), badgeThree, null);
                }
            }
            assert newBadgeList != null;
            tvInfo.setText("You have unlocked " + newBadgeList.size() + " badge.");
        }


//        Log.i("shoplogo",shopLogo);
        ImageUtils.displayRoundImageFromUrl(this, shopLogo, container_img);
        message.setText(ContainerName +" Picked Up Successfully.");
        message.setTextSize(18);
        message.setTypeface(null, Typeface.BOLD);
        message.setTextColor(Color.parseColor("#000000"));
        done_btn = findViewById(R.id.done_btn);
        paymentReceipt = findViewById(R.id.paymentReceipt);
        paymentReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(receiptUrl));
                startActivity(intent);
            }
        });

        if (Return.equals("return")){
           tvRemember.setVisibility(View.GONE);
           message.setText(ContainerName+" Returned Successfully.");
        }
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                Intent intent1 = new Intent(ReadyToUseSuccessActivity.this, MapsActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                //onBackPressed();
            }
        });
        handle();
    }

    public void handle() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(ReadyToUseSuccessActivity.this, MapsActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            }
        }, 3000);
    }

    private void initConfettiView() {
        float maxHeight = konfettiView.getHeight();
        float maxWidth = konfettiView.getWidth();



        konfettiView.build()
                .addColors(Color.parseColor("#8ED2E2"), Color.parseColor("#E8899E"), Color.parseColor("#89D47F"), Color.parseColor("#EFD176"))
                .setFadeOutEnabled(true)
                .setTimeToLive(5000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, 2000 + 50f, -50f, -50f)
                .stream(30, 5000L);

    }

    private void initViews() {
        container_img = findViewById(R.id.container_img);
        pDialog = new CustomProgressDialog(this);
        message = findViewById(R.id.message);
        parent = findViewById(R.id.parent);
        tvInfo = findViewById(R.id.tvInfo);
        tvRemember = findViewById(R.id.tvRemember);
        badgeOne = findViewById(R.id.badgeOne);
        badgeTwo = findViewById(R.id.badgeTwo);
        badgeThree = findViewById(R.id.badgeThree);
        badgeOne = findViewById(R.id.badgeOne);
        confettiView = findViewById(R.id.confettiView);
        konfettiView = findViewById(R.id.viewKonfetti);
        clostBtn = findViewById(R.id.closeBtn);
        clostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confettiView.setVisibility(View.GONE);
                konfettiView.stopGracefully();
            }
        });
    }


    @Override
    public void onBackPressed() {

        Intent intent1 = new Intent(ReadyToUseSuccessActivity.this, MapsActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent1);

//        Intent t = new Intent();
//        setResult(Activity.RESULT_OK, t);
//        finish();
    }
}
