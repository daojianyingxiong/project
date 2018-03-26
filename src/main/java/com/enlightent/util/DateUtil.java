package com.enlightent.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

	public static final SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy/MM/dd");
	public static final SimpleDateFormat HH_mm = new SimpleDateFormat("HH:mm");
	public static final SimpleDateFormat YYYY_MM_DD_HH_MM = new SimpleDateFormat("yyyy/MM/dd HH:mm");

	public static SimpleDateFormat getYyyyMmDd() {
		return new SimpleDateFormat("yyyy/MM/dd");
	}

	public static SimpleDateFormat getYyyy_MM_dd(){
		return new SimpleDateFormat("yyyy-MM-dd");
	}
	
	private static String parseDateReg(String reg) {
		String[] split = reg.split(",");
		
    	String date = split[0].trim();
    	String type = split[1].trim().toLowerCase();
    	SimpleDateFormat format = null;
    	if (split.length > 2) {
    		format = new SimpleDateFormat(split[2].trim());
		}
    	
    	long dayTime = CalendarUtil.getDayTime(date);
//    	long dayTime = CalendarUtil.getDayTime(Integer.parseInt(date));
    	String parseDate = null;
    	switch (type) {
		case "i":
			parseDate = dayTime / 1000 + "";
			break;
		case "l":
			parseDate = dayTime + "";
			break;
		case "f":
			parseDate = format.format(new Date(dayTime));
			break;
		default:
			break;
		}
		return parseDate;
	}
	
	public static String replaceDateReg(String sql) {
		Pattern p = Pattern.compile("R_DATE\\(.+?\\)"); 
    	Matcher m = p.matcher(sql); 
    	Map<String, String> dateMap = new HashMap<>();
    	while (m.find()) {
			String group = m.group(0);
			String reg = group.substring(group.indexOf("(") + 1, group.lastIndexOf(")"));
			dateMap.put(group, parseDateReg(reg));
		}
    	if (!dateMap.isEmpty()) {
    		Set<Entry<String,String>> entrySet = dateMap.entrySet();
    		for (Entry<String, String> entry : entrySet) {
				sql = sql.replace(entry.getKey(), entry.getValue());
			}
		}
		return sql;
	}
	
	public static void main(String[] args) {
		String sql = "(select DISTINCT(v.name) from enlightent_daily.video_ranking_list v where v.channelType=\"tv\" and v.channel=\"total\" and v.dataType=\"R_DATE(-1, f, yyyy-MM-dd HH:mm)\" and v.`day`= R_DATE(-7, i) and v.date=DATE_SUB(curdate(),INTERVAL 1 DAY) ORDER BY v.playTimesPredicted desc limit 100)  union (select DISTINCT(videoName) name from management.monitor where`status`=1 and curDate() >=startDate and curDate()<= toDate and channelType=0 and DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(createTime))";
//		String sql = "R_DATE(-1/0/5, l, yyyy-hh-dd)";
		System.out.println(sql);
		String replaceDateReg = replaceDateReg(sql);
		System.out.println(replaceDateReg);
//		System.out.println(new Date(Long.parseLong(replaceDateReg)));
	}
	
	public static DateRange parseDate(String fromDate, String toDate) {
		Long startL = 0L;
		Long endL = 0L;
		if (fromDate.length() == 13) {
			startL = Long.parseLong(fromDate);
			endL = Long.parseLong(toDate);
			return new DateRange(startL, endL);
		}
		
		if (fromDate.length() == 10) {
			startL = Long.parseLong(fromDate) * 1000;
			endL = Long.parseLong(toDate) * 1000;
			long current = System.currentTimeMillis();
			if (endL > current) {
				endL = current;
			}
		} else {
			startL = getRangeDate(fromDate) * 1000;
			if ("now".equals(fromDate) || "all".equals(fromDate)) {
				endL = System.currentTimeMillis();
			} else {
				endL = parseEndDate(fromDate) * 1000;
			}
		}
		return new DateRange(startL, endL);
	}

	public static class DateRange {
		private Long fromDate;
		private Long toDate;

		public DateRange() {
		}
		public DateRange(Long fromDate, Long toDate) {
			super();
			this.fromDate = fromDate;
			this.toDate = toDate;
		}

		public Long getFromDate() {
			return fromDate;
		}
		
		public Date getFrom() {
			return new Date(fromDate);
		}

		public void setFromDate(Long fromDate) {
			this.fromDate = fromDate;
		}

		public Long getToDate() {
			return toDate;
		}
		
		public Date getTo() {
			return new Date(toDate);
		}

		public void setToDate(Long toDate) {
			this.toDate = toDate;
		}
		@Override
		public String toString() {
			return "DateRange [fromDate=" + fromDate + ", toDate=" + toDate + "]";
		}
		
	}

	public static long getRangeDate(String days) {
		if (days == null || "all".equals(days)) {
			return 0;
		} else if (days.length() == 10) {
			return Long.parseLong(days);
		}

		Calendar c = Calendar.getInstance();
		c.set(java.util.Calendar.HOUR_OF_DAY, 0);
		c.set(java.util.Calendar.MINUTE, 0);
		c.set(java.util.Calendar.SECOND, 0);
		switch (days) {
		case "now":
			break;
		case "yesterday":
			c.add(Calendar.DATE, -1);
			break;
		case "7":
			c.add(Calendar.DATE, -7);
			break;
		}
		return c.getTimeInMillis() / 1000;
	}

	public static long parseEndDate(String end) {
		if (end.length() == 10) {
			return Long.parseLong(end);
		}

		Calendar c = Calendar.getInstance();
		c.set(java.util.Calendar.HOUR_OF_DAY, 0);
		c.set(java.util.Calendar.MINUTE, 0);
		c.set(java.util.Calendar.SECOND, 0);
		switch (end) {
		case "7":
			c.add(Calendar.DATE, -6);
			break;
		}
		return c.getTimeInMillis() / 1000 - 1;
	}

	public static String addThreeZero(String str) {
		if (str == null) {
			throw new NullPointerException("the 'str' must not be null");
		}
		if (str.length() == 10) {
			str += "000";
		}
		return str;
	}

}
