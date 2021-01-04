package com.ka8eem.digissquaredtest.models;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Colored {

    private static Colored INSTANCE;

    private static List<Pair<Pair<Double, Double>, String>> RSRPColorValues;

    private static List<Pair<Pair<Double, Double>, String>> RSRQColorValues;

    private static List<Pair<Pair<Double, Double>, String>> SINRColorValues;

    private Colored() {

        // initialize data
        RSRPColorValues = new ArrayList<>();
        RSRQColorValues = new ArrayList<>();
        SINRColorValues = new ArrayList<>();

        // RSRP
        RSRPColorValues.add(makePair(Double.MIN_VALUE, -110, "#000A00"));
        RSRPColorValues.add(makePair(-110, -100, "#E51304"));
        RSRPColorValues.add(makePair(-100, -90, "#FAFD0C"));
        RSRPColorValues.add(makePair(-90, -80, "#02FA0E"));
        RSRPColorValues.add(makePair(-80, -70, "#0B440D"));
        RSRPColorValues.add(makePair(-70, -60, "#0EFFF8"));
        RSRPColorValues.add(makePair(-60, Double.MAX_VALUE, "#0007FF"));

        // RSRQ
        RSRQColorValues.add(makePair(Double.MIN_VALUE, -19.5, "#000000"));
        RSRQColorValues.add(makePair(-19.5, -14, "#ff0000"));
        RSRQColorValues.add(makePair(-14, -9, "#ffee00"));
        RSRQColorValues.add(makePair(-9, -3, "#80ff00"));
        RSRQColorValues.add(makePair(-3, Double.MAX_VALUE, "#3f7806"));

        // SINR
        SINRColorValues.add(makePair(Double.MIN_VALUE, 0, "#000000"));
        SINRColorValues.add(makePair(0, 5, "#F90500"));
        SINRColorValues.add(makePair(5, 10, "#FD7632"));
        SINRColorValues.add(makePair(10, 15, "#FBFD00"));
        SINRColorValues.add(makePair(15, 20, "#00FF06"));
        SINRColorValues.add(makePair(20, 25, "#027500"));
        SINRColorValues.add(makePair(25, 30, "#0EFFF8"));
        SINRColorValues.add(makePair(30, Double.MAX_VALUE, "#0000F0"));

    }

    private Pair<Pair<Double, Double>, String> makePair(double from, double to, String color) {
        Pair<Pair<Double, Double>, String> ret =
                new Pair<>(new Pair<>(from, to), color);
        return ret;
    }

    public static synchronized Colored getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Colored();
        return INSTANCE;
    }

    public static String getColor(double value, String type) {
        String color = "";
        if ("RSRP".equals(type)) {

            for (Pair<Pair<Double, Double>, String> it : RSRPColorValues) {
                if (value >= it.first.first && value <= it.first.second) {
                    color = it.second;
                    break;
                }
            }
        } else if ("RSRQ".equals(type)) {

            for (Pair<Pair<Double, Double>, String> it : RSRQColorValues) {
                if (value >= it.first.first && value <= it.first.second) {
                    color = it.second;
                    break;
                }
            }
        } else {

            for (Pair<Pair<Double, Double>, String> it : SINRColorValues) {
                if (value >= it.first.first && value <= it.first.second) {
                    color = it.second;
                    break;
                }
            }
        }
        return color;
    }
}
