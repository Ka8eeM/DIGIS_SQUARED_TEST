package com.ka8eem.digissquaredtest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.Toast;

import com.ka8eem.digissquaredtest.R;
import com.ka8eem.digissquaredtest.databinding.ActivityMainBinding;
import com.ka8eem.digissquaredtest.models.Colored;
import com.ka8eem.digissquaredtest.models.DataModel;
import com.ka8eem.digissquaredtest.viewmodel.DataViewModel;
import com.ka8eem.digissquaredtest.workmanager.MyAlarm;
import com.ka8eem.digissquaredtest.workmanager.MyWorkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    DataViewModel dataViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
    }


    private void init() {


        /*
         * initialize the view model
         * */
        dataViewModel = ViewModelProviders.of(this, new DataViewModel(getApplication())).get(DataViewModel.class);
        /*
         * alarm model to schedule calling the API every 2 seconds
         * */
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC, 2000/*trigger every 2 seconds*/, AlarmManager.INTERVAL_DAY, pendingIntent);

        dataViewModel.getOutputWorkInfo().observe(this, new Observer<List<WorkInfo>>() {
            @Override
            public void onChanged(List<WorkInfo> workInfos) {
                if (workInfos == null || workInfos.isEmpty())
                    return;
                WorkInfo workInfo = workInfos.get(0);
                boolean finished = workInfo.getState().isFinished();
                if (finished) {
                    Data data = workInfo.getOutputData();
                    double rsrp = data.getDouble("RSRP", 0.0);
                    double rsrq = data.getDouble("RSRQ", 0.0);
                    double sinr = data.getDouble("SINR", 0.0);
                    updateTable(rsrp, R.id.rsrp_progress_bar);
                    updateTable(rsrq, R.id.rsrq_progress_bar);
                    updateTable(sinr, R.id.sinr_progress_bar);
                    // updateGrid(dataModel);
                }
            }
        });
    }

    private void updateTable(double value, int progressBarId) {

        String color = null;
        switch (progressBarId) {
            case R.id.rsrp_progress_bar:
                color = Colored.getInstance().getColor(value, "RSRP");
                break;
            case R.id.rsrq_progress_bar:
                color = color = Colored.getInstance().getColor(value, "RSRQ");
                break;
            case R.id.sinr_progress_bar:
                color = Colored.getInstance().getColor(value, "SINR");
                break;
        }
        Log.e("color", color);
        final TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.data_row, null);
        ProgressBar progressBar = tableRow.findViewById(progressBarId);
        if (value >= 0)
            progressBar.setScaleX(1);
        else
            progressBar.setScaleX(-1);
        progressBar.setProgress((int) value);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor(color), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.tableLayout.addView(tableRow);
    }

    private void updateGrid(DataModel dataModel) {

    }

}