package com.empti.app.webservices;

import com.empti.app.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Retrofit connectivity
public class RetrofitClient {
    private static Retrofit mRetrofit;
    static String basrUrl = "https://empti.org/empti/public/";
    //static String basrUrl="http://ec2-18-219-13-255.us-east-2.compute.amazonaws.com/takai/public/";
    private RetrofitClient() {}

    private static OkHttpClient getClient() {
        OkHttpClient client;
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        } else {
            client = new OkHttpClient.Builder().build();
        }
        OkHttpClient.Builder builder = client.newBuilder().readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS).connectTimeout(300, TimeUnit.SECONDS);
        return builder.build();
    }
    public static RetrofitApis getRetrofitClient() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(basrUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getClient())
                    .build();
        }
        return mRetrofit.create(RetrofitApis.class);
    }
}
