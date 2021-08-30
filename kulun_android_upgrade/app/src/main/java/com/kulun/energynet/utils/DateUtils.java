package com.kulun.energynet.utils;

import android.text.TextUtils;

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
    public static String stampToDate(long s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(s);
        return simpleDateFormat.format(date);
    }

    public static boolean isBelong(String beiginTime, String endtoTime){

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        Date now =null;
        Date beginTime = null;
        Date endTime = null;
        try {
            now = df.parse(df.format(new Date()));
            beginTime = df.parse(beiginTime);
            endTime = df.parse(endtoTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Boolean flag = belongCalendar(now, beginTime, endTime);
        return flag;
    }


    /**
     * 判断时间是否在时间段内
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 22      * 日期格式字符串转换成时间戳
     * 23      * @param date 字符串日期
     * 24      * @param format 如：yyyy-MM-dd HH:mm:ss
     * 25      * @return
     * 26
     */
    public static long date2TimeStamp(String date_str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
            return sdf.parse(date_str).getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 22      * 日期格式字符串转换日期
     * 23      * @param date 字符串日期
     * 24      * @param format 如：yyyy-MM-dd HH:mm:ss
     * 25      * @return
     * 26
     */
    public static String timeToDate(String date_str) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ").parse(date_str));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 22      * 日期格式字符串转换当天时间
     * 23      * @param date 字符串日期
     * 24      * @param format 如：yyyy-MM-dd HH:mm:ss
     * 25      * @return
     * 26
     */
    public static String timeTotime(String date_str) {
        try {
            return new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ").parse(date_str));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 22      * 日期格式字符串转换成时间戳
     * 23      * @param date 字符串日期
     * 24      * @param format 如：yyyy-MM-dd HH:mm:ss
     * 25      * @return
     * 26
     */
    public static String datetoTime(String date_str) {
        try {
            long time = date2TimeStamp(date_str);
            return stampToTime(time * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     *将时间戳转换为年月日
     */
    public static String stampToYear(long s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(s);
        return simpleDateFormat.format(date);
    }

    public static String stampToTime(long s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(s);
        return simpleDateFormat.format(date);
    }

    /**
     * 时间戳转换为年
     */
    public static int stampToYears(long s) {
        String ss = stampToYear(s);
        if (ss.indexOf("-") != -1) {
            return Integer.parseInt(ss.split("-")[0]);
        }
        return 0;
    }

    /**
     * 时间戳转换为月
     */
    public static int stampToMonth(long s) {
        String ss = stampToDate(s);
        if (ss.indexOf("-") != -1) {
            return Integer.parseInt(ss.split("-")[1]);
        }
        return 0;
    }

    /**
     * 获取某年某月的第一天
     */
    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfMonth = sdf.format(cal.getTime());
        return firstDayOfMonth;
    }

//    public static String getDay(String date){//把日历模式转换为年月份
//        if (TextUtils.isEmpty(date)){
//            return "";
//        }
//        String splite[] = date.split("-");
//        if (splite.length >= 2){
//            return splite[0]+"年"+splite[1]+"月份";
//        }
//        return "";
//    }

    /**
     * 获取某月的最后一天
     */
//    public static String getLastDayOfMonth(int year, int month) {
//        Calendar cal = Calendar.getInstance();
//        //设置年份
//        cal.set(Calendar.YEAR, year);
//        //设置月份
//        cal.set(Calendar.MONTH, month - 1);
//        //获取某月最大天数
//        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//        //设置日历中月份的最大天数
//        cal.set(Calendar.DAY_OF_MONTH, lastDay);
//        //格式化日期
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String lastDayOfMonth = sdf.format(cal.getTime());
//        return lastDayOfMonth;
//    }

    /**
     * 获取年
     *
     * @return
     */
    public static int getYear() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.YEAR);
    }

    /**
     * 获取月
     *
     * @return
     */
    public static int getMonth() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.MONTH) + 1;
    }


    /**
     * 把时间转换为：时分秒格式。
     *
     * @param second ：秒，传入单位为秒
     * @return
     */
    /**
     * 把时间转换为：时分秒格式。
     *
     * @param time
     * @return
     */
    public static String getTimeString(int time) {
        int miao = time % 60;
        int fen = time / 60;
        int hour = 0;
        if (fen >= 60) {
            hour = fen / 60;
            fen = fen % 60;
        }
        String timeString = "";
        String miaoString = "";
        String fenString = "";
        String hourString = "";
        if (miao < 10) {
            miaoString = "0" + miao;
        } else {
            miaoString = miao + "";
        }
        if (fen < 10) {
            fenString = "0" + fen;
        } else {
            fenString = fen + "";
        }
        if (hour < 10) {
            hourString = "0" + hour;
        } else {
            hourString = hour + "";
        }
        if (hour != 0) {
            timeString = hourString + ":" + fenString + ":" + miaoString;
        } else {
            timeString = fenString + ":" + miaoString;
        }
        return timeString;
    }
}
