package com.botann.charing.pad.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @Title DateUtil.java
 * @package cn.roboca.ppap.utils
 * @Description 时间帮助类
 * @author PW
 * @date 2014-3
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil
{
	private Calendar calendar = Calendar.getInstance();

	/**
	 * 获得从1970年开始的当前时间 ms
	 * 
	 * @return
	 */
	public static long getTimeMillis()
	{
		return System.currentTimeMillis();
	}

	/**
	 * 得到花费的时间
	 * 
	 * @param timeStr
	 *            需要计算的时间 单位s
	 * @return 以x小时x分钟x秒格式输出
	 */
	public static String getTime(String timeStr)
	{
		if (timeStr == null || timeStr.equals(""))
			return "";
		try
		{
			float timeFloat = Float.parseFloat(timeStr);
			int time = (int) timeFloat;
			String timeDisp = "";
			if (time / 3600 != 0)
			{
				timeDisp += (time / 3600) + "小时";
			}
			time %= 3600;
			if (time / 60 != 0)
			{
				timeDisp += (time / 60) + "分钟";
			}
			time %= 60;
			timeDisp += time + "秒";
			return timeDisp;
		}
		catch (Exception e)
		{
			return "";
		}

	}

	/**
	 * 得到花费的时间，时间格式 xx小时xx分钟
	 * 
	 * @param timeStr
	 *            需要计算的时间 单位s
	 * @return 以x小时x分钟格式输出
	 */
	public static String getTimeMin(String timeStr)
	{
		try
		{
			float timeFloat = Float.parseFloat(timeStr);
			int time = (int) timeFloat;
			String timeDisp = "";
			if (time / 3600 != 0)
			{
				timeDisp += (time / 3600) + "小时";
			}
			time %= 3600;
			if (time / 60 != 0)
			{
				timeDisp += (time / 60) + "分钟";
			}
			return timeDisp;
		}
		catch (Exception e)
		{
			return "";
		}

	}

	/**
	 * 得到 从1970 1 1开始的时间，时间格式yyyy-MM-dd
	 *
	 * @param timeStamp Long 需要计算的时间ms
	 * @return yyyy-MM-dd
	 */
	public static String getAppointDate(Long timeStamp){
		return getAppointDate(timeStamp,"yyyy-MM-dd");
	}
	public static String getAppointDate(Long timeStamp, String format)
	{
		return new java.text.SimpleDateFormat(format)
				.format(new java.util.Date(timeStamp));
	}

	/**
	 * 得到 从1970 1 1开始的时间，时间格式yyyy年MM月dd日
	 * 
	 * @param  time String 时间戳
	 *            需要计算的时间ms
	 * @return yyyy年MM月dd日
	 */
	public static String getAppointDate(String time)
	{
		if (time == null || time.equals("") || time.equals("0"))
		{
			return "无";
		}
		long t = 0;
		try
		{
			t = Long.parseLong(time);
		}
		catch (Exception e)
		{
			t = 0;
		}
		return getAppointDate(t);
	}

	/**
	 * 得到 从1970 1 1开始的时间，时间格式MM月dd日
	 *
	 * @param time
	 *            需要计算的时间ms
	 * @return yyyy年MM月dd日
	 */
	public static String getAppointDateNoYear(String time)
	{
		if (time == null || time.equals("") || time.equals("0"))
		{
			return "无";
		}
		long t = 0;
		try
		{
			t = Long.parseLong(time);
		}
		catch (Exception e)
		{
			t = 0;
		}
		return new java.text.SimpleDateFormat("MM月dd日")
				.format(new java.util.Date(t));
	}

	/**
	 * 得到 从1970 1 1开始的时间，时间格式yyyy-MM-dd kk:mm:ss(24小时制)
	 *
	 * @param time
	 *            需要计算的时间ms
	 * @return yyyy-MM-dd kk:mm:ss
	 */
	public static String getTimeLine(String time)
	{
		if (time == null || time.equals("") || time.equals("0"))
		{
			return "无";
		}
		long t = 0;
		try
		{
			t = Long.parseLong(time);
		}
		catch (Exception e)
		{
			t = 0;
		}
		return new SimpleDateFormat("yyyy-MM-dd kk:mm:ss")
				.format(new java.util.Date(t));
	}

	/**
	 * 得到当前的时间，时间格式yyyy-MM-dd
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getCurrentDate()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");// 12小时制
		return sdf.format(new Date());
	}

	/**
	 * 得到当前的时间,自定义时间格式 y 年 M 月 d 日 H 时 m 分 s 秒
	 * 
	 * @param dateFormat
	 *            输出显示的时间格式
	 * @return 时间
	 */
	public String getCurrentDate(String dateFormat)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(new Date());
	}

	/**
	 * 日期格式化，默认日期格式yyyy-MM-dd
	 * 
	 * @param date
	 * @return yyyy-MM-dd
	 */
	public static String getFormatDate(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	/**
	 * 日期格式化，自定义输出日期格式
	 * 
	 * @param date
	 *            日期
	 * @param dateFormat
	 *            格式
	 * @return 日期
	 */
	public String getFormatDate(Date date, String dateFormat)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}

	/**
	 * 返回当前日期的前一个时间日期，amount为正数 当前时间后的时间 为负数 当前时间前的时间 默认日期格式yyyy-MM-dd
	 * 
	 * @param field
	 *            日历字段 y 年 M 月 d 日 H 时 m 分 s 秒
	 * @param amount
	 *            数量
	 * @return 一个日期
	 */
	public String getPreDate(String field, int amount)
	{
		calendar.setTime(new Date());
		if (field != null && !field.equals(""))
		{
			if (field.equals("y"))
			{
				calendar.add(Calendar.YEAR, amount);
			}
			else if (field.equals("M"))
			{
				calendar.add(Calendar.MONTH, amount);
			}
			else if (field.equals("d"))
			{
				calendar.add(Calendar.DAY_OF_MONTH, amount);
			}
			else if (field.equals("H"))
			{
				calendar.add(Calendar.HOUR, amount);
			}
		}
		else
		{
			return null;
		}
		return getFormatDate(calendar.getTime());
	}

	/**
	 * 某一个日期的前一个日期
	 * 
	 * @param d
	 *            ,某一个日期
	 * @param field
	 *            日历字段 y 年 M 月 d 日 H 时 m 分 s 秒
	 * @param amount
	 *            数量
	 * @return 一个日期
	 */
	public String getPreDate(Date d, String field, int amount)
	{
		calendar.setTime(d);
		if (field != null && !field.equals(""))
		{
			if (field.equals("y"))
			{
				calendar.add(Calendar.YEAR, amount);
			}
			else if (field.equals("M"))
			{
				calendar.add(Calendar.MONTH, amount);
			}
			else if (field.equals("d"))
			{
				calendar.add(Calendar.DAY_OF_MONTH, amount);
			}
			else if (field.equals("H"))
			{
				calendar.add(Calendar.HOUR, amount);
			}
		}
		else
		{
			return null;
		}
		return getFormatDate(calendar.getTime());
	}

	/**
	 * 某一个时间的前一个时间
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public String getPreDate(String date) throws ParseException
	{
		Date d = new SimpleDateFormat().parse(date);
		String preD = getPreDate(d, "d", 1);
		Date preDate = new SimpleDateFormat().parse(preD);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(preDate);
	}



	public static String getNextDay(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); //这个时间就是日期往后推一天的结果
		return getFormatDate(date);
	}

	public static String getNextDay(String prev) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(prev);
		return getNextDay(date);
	}

	public static Date parseDate(String day) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(day);
		return date;
	}


	public static void main(String[] args) {
		try {
			Date endDate = parseDate("2017-02-25");
			System.out.println(endDate.after(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
