package com.ka8eem.digissquaredtest.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.ka8eem.digissquaredtest.R;
import com.ka8eem.digissquaredtest.databinding.ActivityMainBinding;
import com.ka8eem.digissquaredtest.repository.Colored;
import com.ka8eem.digissquaredtest.models.DataModel;
import com.ka8eem.digissquaredtest.repository.DataClient;
import com.ka8eem.digissquaredtest.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (disposable.isDisposed()) {
            disposable = Observable.interval(Constants.INITIAL_DELAY, Constants.TIME_TO_REPEAT,
                    TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::getData, this::onError);
        }
    }


    private ActivityMainBinding binding;
    private Observable<DataModel> dataModelObservable;
    private Disposable disposable;

    private List<Entry> RSRPXY = new ArrayList<>();
    private List<Entry> RSRQXY = new ArrayList<>();
    private List<Entry> SINRXY = new ArrayList<>();
    private LineDataSet RSRPDataSet;
    private LineDataSet RSRQDataSet;
    private LineDataSet SINRDateSet;
    private List<ILineDataSet> dataSetRSRP = new ArrayList<>();
    private List<ILineDataSet> dataSetRSRQ = new ArrayList<>();
    private List<ILineDataSet> dataSetSINR = new ArrayList<>();

    private int timeInterval = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
    }

    private void getData(Long aLong) {
        dataModelObservable = DataClient.getInstance().getData();
        dataModelObservable.subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::handleResults, this::throwError);
        timeInterval += 2;
    }

    private void init() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding.rsrpGraph.setDragEnabled(true);
        binding.rsrpGraph.setScaleEnabled(false);

        binding.rsrqGraph.setDragEnabled(true);
        binding.rsrqGraph.setScaleEnabled(false);

        binding.sinrGraph.setDragEnabled(true);
        binding.sinrGraph.setScaleEnabled(false);


        disposable = Observable.interval(Constants.INITIAL_DELAY, Constants.TIME_TO_REPEAT,
                TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getData, this::onError);
    }


    private void onError(Throwable throwable) {
        Constants.showSnackbar(binding.scrollView, "Error in Observable");
    }

    /*
     * This function bind the result into data table
     * and the graphs.
     * */
    private void handleResults(DataModel dataModel) {

        updateTable(dataModel);
        updateGraph(dataModel, 1);
        updateGraph(dataModel, 2);
        updateGraph(dataModel, 3);
    }

    private void throwError(Throwable t) {
        //Constants.showSnackbar(binding.scrollView, t.getMessage());
    }

    /*
     * This function creates single data row
     * every 2 seconds after calling the API
     * and append this row to the table layout.
     * */
    private void updateTable(DataModel dataModel) {

        String color = null;
        double value = 0;
        final TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.data_row, null);

        /*
         * RSRP
         */
        ProgressBar progressBar = tableRow.findViewById(R.id.rsrp_progress_bar);
        TextView textView = tableRow.findViewById(R.id.text_progress_rsrp);
        value = dataModel.getRSRP();
        color = Colored.getInstance().getColor(value, "RSRP");
        int val = (int) value;
        val %= 100;
        if (value < 0)
            val *= -1;
        textView.setText(value + "");
        progressBar.setMax(100);
        progressBar.setProgress(val);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor(color), android.graphics.PorterDuff.Mode.SRC_IN);

        /*
         * RSRQ
         */
        progressBar = tableRow.findViewById(R.id.rsrq_progress_bar);
        textView = tableRow.findViewById(R.id.text_progress_rsrq);
        value = dataModel.getRSRQ();
        color = Colored.getInstance().getColor(value, "RSRQ");
        val = (int) value;
        val %= 100;
        if (value < 0)
            val *= -1;
        textView.setText(value + "");
        progressBar.setMax(100);
        progressBar.setProgress(val);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor(color), android.graphics.PorterDuff.Mode.SRC_IN);

        /*
         * SINR
         */
        progressBar = tableRow.findViewById(R.id.sinr_progress_bar);
        textView = tableRow.findViewById(R.id.text_progress_sinr);
        value = dataModel.getSINR();
        color = Colored.getInstance().getColor(value, "SINR");
        val = (int) value;
        val %= 100;
        if (value < 0)
            val *= -1;
        textView.setText(value + "");
        progressBar.setMax(100);
        progressBar.setProgress(val);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor(color), android.graphics.PorterDuff.Mode.SRC_IN);

        binding.tableLayout.addView(tableRow);
        binding.tableLayout.refreshDrawableState();
    }


    /*
     * This function update all the 3 graphs
     * every 2 seconds after calling the API.
     * */
    private void updateGraph(DataModel dataModel, int type) {

        switch (type) {
            case 1: {
                double val = dataModel.getRSRP();
                RSRPXY.add(new Entry(timeInterval, (float) val));
                RSRPDataSet = new LineDataSet(RSRPXY, "RSRP");
                RSRPDataSet.setFillAlpha(110);
                RSRPDataSet.setColor(Color.RED);
                RSRPDataSet.setCircleColor(Color.BLACK);
                RSRPDataSet.setLineWidth(2f);
                RSRPDataSet.setValueTextSize(10f);
                dataSetRSRP.add(RSRPDataSet);
                break;
            }
            case 2: {
                double val = dataModel.getRSRQ();
                RSRQXY.add(new Entry(timeInterval, (float) val));
                RSRQDataSet = new LineDataSet(RSRQXY, "RSRQ");
                RSRQDataSet.setFillAlpha(110);
                RSRQDataSet.setColor(Color.GREEN);
                RSRQDataSet.setCircleColor(Color.BLACK);
                RSRQDataSet.setLineWidth(2f);
                RSRQDataSet.setValueTextSize(10f);
                dataSetRSRQ.add(RSRQDataSet);
                break;
            }
            case 3: {
                double val = dataModel.getSINR();
                SINRXY.add(new Entry(timeInterval, (float) val));
                SINRDateSet = new LineDataSet(SINRXY, "SINR");
                SINRDateSet.setFillAlpha(110);
                SINRDateSet.setColor(Color.MAGENTA);
                SINRDateSet.setCircleColor(Color.BLACK);
                SINRDateSet.setLineWidth(2f);
                SINRDateSet.setValueTextSize(10f);
                dataSetSINR.add(SINRDateSet);
                break;
            }
        }
        LineData RSRPLineData = new LineData(dataSetRSRP);
        LineData RSRQLineData = new LineData(dataSetRSRQ);
        LineData SINRLineData = new LineData(dataSetSINR);

        binding.rsrpGraph.setData(RSRPLineData);
        binding.rsrqGraph.setData(RSRQLineData);
        binding.sinrGraph.setData(SINRLineData);
    }

    public void toggleDataTable(View view) {
        TextView textView = (TextView) view;
        int visible = binding.tableLayout.getVisibility();
        switch (visible) {
            case View.VISIBLE:
                binding.tableLayout.setVisibility(View.GONE);
                textView.setText(getString(R.string.show_data_table));
                break;
            case View.GONE:
                binding.tableLayout.setVisibility(View.VISIBLE);
                textView.setText(getString(R.string.hide_data_table));
                break;
        }
    }

    public void toggleGraph(View view) {

        TextView textView = (TextView) view;
        switch (textView.getId()) {
            case R.id.show_hide_rsrp: {
                int vis = binding.rsrpGraph.getVisibility();
                if (vis == View.VISIBLE) {
                    binding.rsrpGraph.setVisibility(View.GONE);
                    textView.setText(getString(R.string.show_rsrp));
                } else {
                    binding.rsrpGraph.setVisibility(View.VISIBLE);
                    textView.setText(getString(R.string.hide_rsrp));
                }
                break;
            }
            case R.id.show_hide_rsrq: {
                int vis = binding.rsrqGraph.getVisibility();
                if (vis == View.VISIBLE) {
                    binding.rsrqGraph.setVisibility(View.GONE);
                    textView.setText(getString(R.string.show_rsrq));
                } else {
                    binding.rsrqGraph.setVisibility(View.VISIBLE);
                    textView.setText(getString(R.string.hide_rsrq));
                }
                break;
            }
            case R.id.show_hide_sinr: {
                int vis = binding.sinrGraph.getVisibility();
                if (vis == View.VISIBLE) {
                    binding.sinrGraph.setVisibility(View.GONE);
                    textView.setText(getString(R.string.show_sinr));
                } else {
                    binding.sinrGraph.setVisibility(View.VISIBLE);
                    textView.setText(getString(R.string.hide_sinr));
                }
                break;
            }
        }
    }
}