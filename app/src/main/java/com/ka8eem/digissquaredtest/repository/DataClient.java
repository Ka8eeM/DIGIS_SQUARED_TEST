package com.ka8eem.digissquaredtest.repository;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ka8eem.digissquaredtest.interfaces.DataInterface;
import com.ka8eem.digissquaredtest.models.DataModel;
import com.ka8eem.digissquaredtest.util.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataClient {

    private DataInterface dataInterface;
    private static DataClient INSTANCE;

    private DataClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        dataInterface = retrofit.create(DataInterface.class);
    }

    public static synchronized DataClient getInstance() {
        if (INSTANCE == null)
            INSTANCE = new DataClient();
        return INSTANCE;
    }

    public Call<DataModel> getData() {
        return dataInterface.getData();
    }
}
