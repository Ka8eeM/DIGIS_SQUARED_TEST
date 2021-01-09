package com.ka8eem.digissquaredtest.interfaces;

import com.ka8eem.digissquaredtest.models.DataModel;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DataInterface {

    @GET("random")
    Observable<DataModel> getData();
}
