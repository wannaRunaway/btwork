package com.botann.charing.pad.utils;

/**
 * Created by mengchenyun on 2017/2/16.
 */

public class FormatUtil {

    public static String format(String origin, String defaultValue) {
        if(origin == null) {
            return defaultValue;
        } else {
            return origin;
        }
    }

    public static String format(Integer origin, String defaultValue) {
        if(origin == null) {
            return defaultValue;
        } else {
            return Integer.toString(origin);
        }
    }

    public static String format(double value) {
        return String.format("%.2f", value);
    }

    public static String format(String value) {
        String res = "";
        Double d = null;
        try {
            String parse = format(value, "");
            d = Double.parseDouble(parse);
            res = format(d);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String floor(String value) {
        String res = "";
        double x = 0;
        try {
            Double d = Double.parseDouble(value);
            x = Math.floor(d);
            res = ((int)x) + "";
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String centToNormal(String cent) {
        return Double.toString(Double.parseDouble(cent) / 100);
    }

    public static String normalToCent(String normal) {
        return Double.toString(Double.parseDouble(normal) * 100);
    }
}
