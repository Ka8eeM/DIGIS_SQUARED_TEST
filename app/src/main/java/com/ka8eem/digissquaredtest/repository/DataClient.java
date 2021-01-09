package com.ka8eem.digissquaredtest.repository;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ka8eem.digissquaredtest.interfaces.DataInterface;
import com.ka8eem.digissquaredtest.models.DataModel;
import com.ka8eem.digissquaredtest.util.Constants;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataClient {

    private DataInterface dataInterface;
    private static DataClient INSTANCE;

    private DataClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        dataInterface = retrofit.create(DataInterface.class);
    }

    public static synchronized DataClient getInstance() {
        if (INSTANCE == null)
            INSTANCE = new DataClient();
        return INSTANCE;
    }

    public Observable<DataModel> getData() {
        Observable<DataModel> observable = dataInterface.getData();
        return observable;
    }
}
