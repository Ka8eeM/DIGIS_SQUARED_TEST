package com.ka8eem.digissquaredtest.util;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.ka8eem.digissquaredtest.R;

public class Constants {

    public static final String BASE_URL = "http://51.195.89.92:6000/";

    public static final int INITIAL_DELAY = 0;

    public static final int TIME_TO_REPEAT = 2000;

    public static void showSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackView = snackbar.getView();
        TextView textView = snackView.findViewById(R.id.snackbar_text);
        textView.setTextColor(view.getContext().getResources().getColor(R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        snackbar.show();
    }
}