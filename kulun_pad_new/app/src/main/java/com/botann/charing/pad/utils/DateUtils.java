package com.botann.charing.pad.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * created by xuedi on 2018/11/6
 */
public class DateUtils {
    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long s){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(s);
        return simpleDateFormat.format(date);
    }

    /*
     * 将时间戳转换为日期
     */
    public static String stampToYear(long s){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(s);
        return simpleDateFormat.format(date);
    }

    /**
     * 时间戳转换为年
     */
    public static int stampToYears(long s){
        String ss = stampToYear(s);
        if (ss.indexOf("-") != -1){
            return Integer.parseInt(ss.split("-")[0]);
        }
        return 2019;
    }

    /**
     * 时间戳转换为月
     */
    public static int stampToMonth(long s){
        String ss = stampToDate(s);
        if (ss.indexOf("-") != -1){
            return Integer.parseInt(ss.split("-")[1]);
        }
        return 01;
    }

    /**
     * 时间戳转换为日
     */
    public static int stampToDay(long s){
        String ss = stampToDate(s);
        if (ss.indexOf("-") != -1){
            return Integer.parseInt(ss.split("-")[2]);
        }
        return 0;
    }

    /**
     * 时间戳转换为时
     */
    public static int stampToHour(long s){
        String ss = stampToDate(s);
        if (ss.indexOf("-") != -1){
            return Integer.parseInt(ss.split("-")[1]);
        }
        return 0;
    }

    /**
     * 时间戳转换为分
     */
    public static int stampToMinute(long s){
        String ss = stampToDate(s);
        if (ss.indexOf("-") != -1){
            return Integer.parseInt(ss.split("-")[1]);
        }
        return 0;
    }

    /**
     * 获取某年某月的第一天
     */
    public static String getFirstDayOfMonth(int year, int month){
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfMonth = sdf.format(cal.getTime());
        return firstDayOfMonth;
    }

    /**
     * 获取某月的最后一天
     *
     */
    public static String getLastDayOfMonth(int year,int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }
}
