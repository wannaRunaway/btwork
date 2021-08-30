package com.btkj.chongdianbao.utils;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/8/28.
 */
public class FormatUtil {
    private static String s;
    public static String format(double d){
        int a= (int) d;
        DecimalFormat format = new DecimalFormat("#.00");
        s=format.format(d);
        if(a==0){
            return "0"+s;
        }else{
            return s;
        }
    }
    public static String format(float f){
        int a= (int) f;
        DecimalFormat format = new DecimalFormat("#.00");
        s=format.format(f);
        if(a==0){
            return "0"+s;
        }else{
            return s;
        }
    }
    public static  String socformat( float f){
        DecimalFormat df = new DecimalFormat("###.####");
        return df.format(f);
    }
}
