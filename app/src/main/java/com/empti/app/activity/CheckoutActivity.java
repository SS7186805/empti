package com.empti.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.empti.app.R;
import com.empti.app.model.CardModel;
import com.empti.app.model.NewBadgeModel;
import com.empti.app.utilities.Constant;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.NotifyService;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;
import com.google.gson.Gson;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.StripeModel;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputListener;
import com.stripe.android.view.CardInputWidget;
import com.tenbis.support.consts.CardType;
import com.tenbis.support.listeners.OnCreditCardStateChanged;
import com.tenbis.support.models.CreditCard;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import kotlin.jvm.JvmStatic;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.empti.app.utilities.Constant.qrcode;

public class CheckoutActivity extends Activity {

    private String totalAmount, qrCodeNumber, shopname, shopLogo;
    private TextView tvAmount;
    private TextView tvTitle;
    com.tenbis.support.views.CompactCreditInput debitCard;
    CardInputWidget stripeCard;
    Stripe stripe;
    Button btNext;
    Intent intent;
    private ImageView ivBackArrow;
    CustomProgressDialog pDialog;
    ArrayList<NewBadgeModel.NewBadge> newBadgeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        intent = getIntent();
        if (intent != null) {
            totalAmount = intent.getExtras().getString("amount");
            qrCodeNumber = intent.getExtras().getString("qrCode");
            shopname = intent.getExtras().getString("shopName");
            shopLogo = intent.getExtras().getString("shopLogo");

        }
        init();
        stripe = new Stripe(this, "pk_test_Lel31B0nl8KVzWchAMW2MMcW00YDT78Lbw");

        //  stripeCard.setPostalCodeRequired(false);

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValids();
            }
        });

        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void checkValids() {

        Map<String, Object> card2 = new HashMap<>();
        card2.put("number", "4242424242424242");
        card2.put("exp_month", 6);
        card2.put("exp_year", 2021);
        card2.put("cvc", "314");
        Map<String, Object> params = new HashMap<>();
        params.put("card", card2);


        Set<String> a = new HashSet<String>();
        a.add("d");
        a.add("de");
        a.add("def");
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "test");
        map.put("cc", "default");


        if (Objects.requireNonNull(debitCard.getCardNumberInput().getText()).toString().isEmpty() || debitCard.getCardNumberInput().getText() == null) {
            Toast.makeText(this, "Enter Card Number", Toast.LENGTH_SHORT).show();
            return;
        } else if (Objects.requireNonNull(debitCard.getCardNumberInput().getText()).toString().length() != 16) {
            Toast.makeText(this, "Enter valid card number", Toast.LENGTH_SHORT).show();
            return;
        } else if (Objects.requireNonNull(debitCard.getCardExpirationDateInput().getText()).toString().isEmpty() || debitCard.getCardExpirationDateInput().getText() == null) {
            Toast.makeText(this, "Enter Exp. Date", Toast.LENGTH_SHORT).show();
            return;
        } else if (Objects.requireNonNull(debitCard.getCardCvvNumberInput().getText()).toString().isEmpty() || debitCard.getCardCvvNumberInput().getText() == null) {
            Toast.makeText(this, "Enter Cvv", Toast.LENGTH_SHORT).show();
            return;
        } else if (Objects.requireNonNull(debitCard.getCardCvvNumberInput().getText()).toString().length() != 3) {
            Toast.makeText(this, "Enter valid cvv number", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String cardNumber = debitCard.getCardNumberInput().getText().toString();
            String cardExpDate = debitCard.getCardExpirationDateInput().getText().toString();
            String cardCvv = debitCard.getCardCvvNumberInput().getText().toString();

            int s = Integer.parseInt(cardExpDate);

            String str = String.valueOf(s);
            String mnth = "";
            String expYear = "";

            if (str.length() < 3) {
                Toast.makeText(this, "Enter valid Exp Date/Month", Toast.LENGTH_SHORT).show();
                return;
            } else if (str.length() == 3) {
                mnth = str.substring(0, 1);
                expYear = str.substring(1, 3);
            } else if (str.length() == 4) {
                mnth = str.substring(0, 2);
                expYear = str.substring(1, 3);
            }
            pDialog.setUpDialog();
            stripe.createToken(create(cardNumber, Integer.parseInt(mnth), Integer.parseInt(expYear), cardCvv), new ApiResultCallback<Token>() {
                @Override
                public void onSuccess(Token token) {
                    Log.e("tokenSuccess", token.toString());
                    String stripeToken = token.getId().toString();
                    paymentApi(stripeToken);
                }

                @Override
                public void onError(@NotNull Exception e) {
                    pDialog.dismissDialog();
                    Log.e("tokenFailure", e.toString());
                    Toast.makeText(CheckoutActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            });
            //   }

        }
    }

    private void paymentApi(String stripeToken) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("amount", intent.getExtras().getString("amount"));
            jsonObject.put("qr_code", qrCodeNumber);
            jsonObject.put("stripeToken", stripeToken);
            Log.e("json object", jsonObject.toString());
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<ResponseBody> call = RetrofitClient.getRetrofitClient().paymentApi("Bearer" + " " + SharedPrefferenceHandler.retriveData(CheckoutActivity.this,
                    Constant.BEARER_TOKEN), requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    pDialog.dismissDialog();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            Log.e("detail data", object.toString());
                            if (object.getString("status").equalsIgnoreCase("1")) {

                                JSONObject dataObj = object.getJSONObject("data");
                                String chargeId = dataObj.getString("charge_id");
                                String receiptUrl = dataObj.getString("receipt_url");

                                hireReturnApi(chargeId, receiptUrl);
                            } else {
                                Toast.makeText(CheckoutActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            pDialog.dismissDialog();
                            e.printStackTrace();
                        } catch (IOException e) {
                            pDialog.dismissDialog();
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pDialog.dismissDialog();
                    Toast.makeText(CheckoutActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            pDialog.dismissDialog();
        }
    }

    public final Card create(String number, int expMonth, int expYear, String cvc) {
        return (new Card.Builder(number, expMonth, expYear, cvc)).build();
    }

    private void hireReturnApi(String chargeId, String receiptUrl) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.qrcode, qrCodeNumber);
            jsonObject.put("charge_id", "");

            Log.e("json object", jsonObject.toString());
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
            Call<NewBadgeModel> call = RetrofitClient.getRetrofitClient().hireReturnApiModel("Bearer" + " " + SharedPrefferenceHandler.retriveData(CheckoutActivity.this,
                    Constant.BEARER_TOKEN), requestBody);
            call.enqueue(new Callback<NewBadgeModel>() {
                @Override
                public void onResponse(Call<NewBadgeModel> call, Response<NewBadgeModel> response) {
                    if (response.isSuccessful()) {
                        try {
                            NewBadgeModel newBadgeModel = response.body();
                            newBadgeList = new ArrayList<>();

                            if (newBadgeModel.getNewBadge().size() > 0) {
                                newBadgeList.addAll(newBadgeModel.getNewBadge());
                            }

                            assert newBadgeModel != null;
                            if (newBadgeModel.getStatus() == 1) {
                                pDialog.dismissDialog();
                                createNotification();
                                Bundle bundle = new Bundle();
                                Intent i = new Intent(CheckoutActivity.this, ReadyToUseSuccessActivity.class);
                                i.putExtra("receiptUrl", receiptUrl);
                                i.putExtra("shopName", shopname);
                                i.putExtra("shopLogo", shopLogo);
                                bundle.putSerializable("badgeListBundle", newBadgeList);
                                i.putExtras(bundle);
                                startActivityForResult(i, 101);
                            } else {
                                Toast.makeText(CheckoutActivity.this, newBadgeModel.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                pDialog.dismissDialog();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            pDialog.dismissDialog();
                        }
                    }
                }

                @Override
                public void onFailure(Call<NewBadgeModel> call, Throwable t) {
                    pDialog.dismissDialog();
                    Toast.makeText(CheckoutActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            pDialog.dismissDialog();
        }
    }

    public void createNotification() {
        Intent myIntent = new Intent(getApplicationContext(), NotifyService.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 18, pendingIntent);
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        pDialog = new CustomProgressDialog(this);
        ivBackArrow = findViewById(R.id.ivBackArrow);
        tvAmount = findViewById(R.id.tvAmount);
        tvAmount.setText("Amount to Pay:" + " " + " CHF " + totalAmount);
        tvAmount.setTextSize(18);
        tvAmount.setTypeface(null, Typeface.BOLD);
        tvAmount.setTextColor(Color.parseColor("#000000"));
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Checkout");
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setTextSize(18);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setTextColor(Color.parseColor("#000000"));
        debitCard = findViewById(R.id.debitCard);
        stripeCard = findViewById(R.id.stripeCard);
        btNext = findViewById(R.id.btNext);
    }


}

