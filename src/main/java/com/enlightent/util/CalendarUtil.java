package com.enlightent.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class CalendarUtil {
	private static  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static long getRangeDate(String days) {
		if (days == null || "all".equals(days)) {
			return 0;
		}
		if (days.length() == 10) {
			return Long.parseLong(days + "000");
		}
		
		Calendar calendar = Calendar.getInstance();
		
		switch (days) {
		case "now":
			break;
		case "yesterday":
			calendar.add(Calendar.DATE, -1);
			break;
		case "7":
			calendar.add(Calendar.DATE, -7);
			break;
		case "8":
			calendar.add(Calendar.DATE, -8);
			break;

		case "30":
			calendar.add(Calendar.DATE, -30);
			break;
		case "60":
			calendar.add(Calendar.DATE, -60);
			break;
		case "90":
			calendar.add(Calendar.DATE, -90);
			break;
		case "half":
			calendar.add(Calendar.DATE, -182);
			break;
		}
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}
	
	public static long getSevenDays() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -6);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTimeInMillis();
	}
	
	public static long getEightDays() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -7);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTimeInMillis();
	}
	
	public static long getTodayStart() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}
	
	public static long getYesterday() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		return c.getTimeInMillis();
	}
	
	public static long getYesterdayStart() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}
	
	public static long getDayTime(int date) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}
	
	public static long getDayTime(String date) {
		String[] split = date.split("/");
		int day = 0;
		Integer hour = null;
		Integer minute = null;
		for (int i = 0, len = split.length; i < len; i++) {
			String s = split[i];
			if (i == 0) {
				day = Integer.parseInt(s);
			} else if (i == 1) {
				if (StringUtils.isNotBlank(s)) {
					hour = Integer.parseInt(s);
				}
			} else if (i == 2) {
				if (StringUtils.isNotBlank(s)) {
					minute = Integer.parseInt(s);
				}
			}
		}
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, day);
		if (hour != null) {
			c.add(Calendar.HOUR_OF_DAY, hour);
		} else {
			c.set(Calendar.HOUR_OF_DAY, 0);
		}
		
		if (minute != null) {
			c.add(Calendar.MINUTE, minute);
		} else {
			c.set(Calendar.MINUTE, 0);
		}
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}
	
	public static void main(String[] args) {
		System.out.println(new Date(getDayTime("-1/1")));
	}
	
	public static long getBefore2Start() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -2);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}
	
	public static long getNextdayStart(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTimeInMillis();
	}
	
	public static long getDateStart(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTimeInMillis();
	}
	public static long getBeforeDateStart(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTimeInMillis();
	}
	
	public static String changeDateToStr(Date date){
		return formatter.format(date);
	}
	
	public static Date changeStrToDate(String date){
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Integer getYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}

	public static Integer getMonth() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MONTH) + 1;
	}
}
