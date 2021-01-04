package com.ka8eem.digissquaredtest.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.ka8eem.digissquaredtest.models.DataModel;
import com.ka8eem.digissquaredtest.repository.DataClient;
import com.ka8eem.digissquaredtest.util.Constants;
import com.ka8eem.digissquaredtest.workmanager.MyWorkManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataViewModel extends ViewModel implements ViewModelProvider.Factory {

    private Application application;
    private WorkManager workManager;
    private OneTimeWorkRequest workRequest = null;
    private LiveData<List<WorkInfo>> savedWorkInfo;

    public DataViewModel(Application application) {
        this.application = application;
        workManager = WorkManager.getInstance(application);
        fun();
        savedWorkInfo = workManager.getWorkInfosByTagLiveData(Constants.TAG);
    }

    public LiveData<List<WorkInfo>> getOutputWorkInfo() {
        return savedWorkInfo;
    }

    private void fun() {
        workRequest = new OneTimeWorkRequest
                .Builder(MyWorkManager.class)
                .addTag(Constants.TAG)
                .build();
        workManager.enqueue(workRequest);
    }

    @SuppressWarnings("unchecked cast")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DataViewModel.class)) {
            return (T) new DataViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
