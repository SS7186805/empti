package com.empti.app.stripePG;

import android.app.Application;

import com.stripe.android.PaymentConfiguration;

public class PaymentGateway extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PaymentConfiguration.init(getApplicationContext(), "pk_test_TYooMQauvdEDq54NiTphI7jx");
    }
}
