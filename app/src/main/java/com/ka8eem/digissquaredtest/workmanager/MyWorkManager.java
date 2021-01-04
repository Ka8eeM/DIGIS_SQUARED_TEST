package com.ka8eem.digissquaredtest.workmanager;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.ka8eem.digissquaredtest.models.DataModel;
import com.ka8eem.digissquaredtest.repository.DataClient;
import com.ka8eem.digissquaredtest.ui.MainActivity;
import com.ka8eem.digissquaredtest.viewmodel.DataViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyWorkManager extends Worker {


    Data data = null;

    public MyWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        DataClient.getInstance().getData().enqueue(new Callback<DataModel>() {
            @Override
            public void onResponse(Call<DataModel> call, Response<DataModel> response) {
                data = createData(response.body());

            }

            @Override
            public void onFailure(Call<DataModel> call, Throwable t) {
                data = null;
            }
        });

        if (data == null)
            return Result.failure(data);
        return Result.success(data);
    }

    private Data createData(DataModel dataModel) {
        Data.Builder builder = new Data.Builder();
        builder.putDouble("RSRP", dataModel.getRSRP());
        builder.putDouble("RSRQ", dataModel.getRSRQ());
        builder.putDouble("SINR", dataModel.getSINR());
        return builder.build();
    }
}