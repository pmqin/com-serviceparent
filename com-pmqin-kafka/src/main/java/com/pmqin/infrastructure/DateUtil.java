package com.pmqin.infrastructure;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class DateUtil {

	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

	/**
	 * getDateStr get a string with format YYYY-MM-DD from a Date object
	 * 
	 * @param date date
	 * @return String
	 */
	static public String getDateStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	static public String getDateTimeStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	static public String getTimestamp(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(date);
	}

	static public String getTimestamp2(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return format.format(date);
	}

	static public String getYear(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		return format.format(date);
	}

	static public String getDateStrC(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		return format.format(date);
	}

	static public String getDateStrCompact(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String str = format.format(date);
		return str;
	}

	static public String getDateStr(Date date, String formatString) {
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		return format.format(date);
	}

	public static Calendar toCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	/**
	 * @throws ParseException
	 * 
	 */
	public static String nextDayString(String date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date old = format.parse(date);
		Date newDate = new Date(old.getTime() + 24 * 60 * 60 * 1000);
		return getDateStr(newDate);
	}

	public static Date addDays(int days, Date date) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.DAY_OF_YEAR, days);
		return calender.getTime();
	}

	public static Date addMonths(int months, Date date) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.MONTH, months);
		return calender.getTime();
	}

	/**
	 * @throws ParseException
	 * 
	 */
	public static String nextDayTime(String date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date old = format.parse(date);
		Date newDate = new Date(old.getTime() + 24 * 60 * 60 * 1000);
		return getDateTimeStr(newDate);
	}

	/**
	 * @throws ParseException
	 * 
	 */
	public static String getPreviousTime(String date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date old = format.parse(date);
		Date newDate = new Date(old.getTime() - 24 * 60 * 60 * 1000);
		return getDateTimeStr(newDate);
	}

	/**
	 * getDateStr get a string with format YYYY-MM-DD HH:mm:ss from a Date object
	 * 
	 * @param date date
	 * @return String
	 */
	static public String getShortDateTimeStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("MMdd-HHmm");
		return format.format(date);
	}

	static public String getDateTimeStrC(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		return format.format(date);
	}

	public static String getCurDateStr(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	/**
	 * Parses text in 'YYYY-MM-DD' format to produce a date.
	 * 
	 * @param s the text
	 * @return Date
	 * @throws ParseException
	 */
	static public Date parseDate(String s) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return format.parse(s);
		} catch (ParseException e) {

			logger.error(e.getMessage(), e);
		}
		return null;
	}

	static public Date parseDateC(String s) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		return format.parse(s);
	}

	/**
	 * Parses text in 'YYYY-MM-DD' format to produce a date.
	 * 
	 * @param s the text
	 * @return Date
	 * @throws ParseException
	 */
	static public Date parseDateTime(String s) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.parse(s);
	}

	static public Date parseDateTimeC(String s) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		return format.parse(s);
	}

	/**
	 * Parses text in 'HH:mm:ss' format to produce a time.
	 * 
	 * @param s the text
	 * @return Date
	 * @throws ParseException
	 */
	static public Date parseTime(String s) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.parse(s);
	}

	static public Date parseString(String dateString, String formatString) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		return format.parse(dateString);
	}

	static public Date parseTimeC(String s) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("HH时mm分ss秒");
		return format.parse(s);
	}

	static public int yearOfDate(Date s) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String d = format.format(s);
		return Integer.parseInt(d.substring(0, 4));
	}

	static public int monthOfDate(Date s) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String d = format.format(s);
		return Integer.parseInt(d.substring(5, 7));
	}

	static public int dayOfDate(Date s) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String d = format.format(s);
		return Integer.parseInt(d.substring(8, 10));
	}

	static public String getDateTimeStr(java.sql.Date date, double time) {
		int year = date.getYear() + 1900;
		int month = date.getMonth() + 1;
		int day = date.getDate();
		String dateStr = year + "-" + month + "-" + day;
		Double d = new Double(time);
		String timeStr = String.valueOf(d.intValue()) + ":00:00";

		return dateStr + " " + timeStr;
	}

	/**
	 * Get the total month from two date.
	 * 
	 * @param sd the start date
	 * @param ed the end date
	 * @return int month form the start to end date
	 * @throws ParseException
	 */
	static public int diffDateM(Date sd, Date ed) throws ParseException {
		return (ed.getYear() - sd.getYear()) * 12 + ed.getMonth() - sd.getMonth() + 1;
	}

	static public java.sql.Date getNextMonthFirstDate(java.sql.Date date) throws ParseException {
		Calendar scalendar = new GregorianCalendar();
		scalendar.setTime(date);
		scalendar.add(Calendar.MONTH, 1);
		scalendar.set(Calendar.DATE, 1);
		return new java.sql.Date(scalendar.getTime().getTime());
	}

	static public java.sql.Date getFrontDateByDayCount(java.sql.Date date, int dayCount) throws ParseException {
		Calendar scalendar = new GregorianCalendar();
		scalendar.setTime(date);
		scalendar.add(Calendar.DATE, -dayCount);
		return new java.sql.Date(scalendar.getTime().getTime());
	}

	/**
	 * Get first day of the month.
	 * 
	 * @param year the year
	 * @param month the month
	 * @return Date first day of the month.
	 * @throws ParseException
	 */
	static public Date getFirstDay(String year, String month) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.parse(year + "-" + month + "-1");
	}

	static public Date getFirstDay(int year, int month) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.parse(year + "-" + month + "-1");
	}

	static public Date getLastDay(String year, String month) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse(year + "-" + month + "-1");

		Calendar scalendar = new GregorianCalendar();
		scalendar.setTime(date);
		scalendar.add(Calendar.MONTH, 1);
		scalendar.add(Calendar.DATE, -1);
		date = scalendar.getTime();
		return date;
	}

	static public Date getLastDay(int year, int month) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse(year + "-" + month + "-1");

		Calendar scalendar = new GregorianCalendar();
		scalendar.setTime(date);
		scalendar.add(Calendar.MONTH, 1);
		scalendar.add(Calendar.DATE, -1);
		date = scalendar.getTime();
		return date;
	}

	/**
	 * getToday get todat string with format YYYY-MM-DD from a Date object
	 * 
	 * @param date date
	 * @return String
	 */

	static public String getTodayStr() throws ParseException {
		Calendar calendar = Calendar.getInstance();
		return getDateStr(calendar.getTime());
	}

	static public Date getToday() throws ParseException {
		return new Date(System.currentTimeMillis());
	}

	static public String getTodayAndTime() {
		return new Timestamp(System.currentTimeMillis()).toString();
	}

	static public String getTodayC() throws ParseException {
		Calendar calendar = Calendar.getInstance();
		return getDateStrC(calendar.getTime());
	}

	static public int getThisYearMonth() throws ParseException {
		Date today = Calendar.getInstance().getTime();
		return (today.getYear() + 1900) * 100 + today.getMonth() + 1;
	}

	static public int getYearMonth(Date date) throws ParseException {
		return (date.getYear() + 1900) * 100 + date.getMonth() + 1;
	}

	// 获取相隔月数
	static public long getDistinceMonth(String beforedate, String afterdate) throws ParseException {
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
		long monthCount = 0;
		try {
			java.util.Date d1 = d.parse(beforedate);
			java.util.Date d2 = d.parse(afterdate);

			monthCount = (d2.getYear() - d1.getYear()) * 12 + d2.getMonth() - d1.getMonth();
			// dayCount = (d2.getTime()-d1.getTime())/(30*24*60*60*1000);

		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return monthCount;
	}

	// 获取相隔天数
	static public long getDistinceDay(String beforedate, String afterdate) throws ParseException {
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
		long dayCount = 0;
		try {
			java.util.Date d1 = d.parse(beforedate);
			java.util.Date d2 = d.parse(afterdate);

			dayCount = (d2.getTime() - d1.getTime()) / (24 * 60 * 60 * 1000);

		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return dayCount;
	}

	public static void main(String[] args) throws ParseException {
		DateUtil.getDates("2014-05-26", "2014-06-26", "1");
		System.out.println(DateUtil.addDays(new Date(), 1));
	}

	// 获取相隔天数
	static public long getDistinceDay(Date beforedate, Date afterdate) {
		long dayCount = 0;

		try {
			dayCount = (afterdate.getTime() - beforedate.getTime()) / (24 * 60 * 60 * 1000);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return dayCount;
	}

	static public long getDistinceDay(java.sql.Date beforedate, java.sql.Date afterdate) throws ParseException {
		long dayCount = 0;

		try {
			dayCount = (afterdate.getTime() - beforedate.getTime()) / (24 * 60 * 60 * 1000);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return dayCount;
	}

	// 获取相隔天数
	static public long getDistinceDay(String beforedate) throws ParseException {
		return getDistinceDay(beforedate, getTodayStr());
	}

	// 获取相隔时间数
	static public long getDistinceTime(String beforeDateTime, String afterDateTime) throws ParseException {
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		long timeCount = 0;
		try {
			java.util.Date d1 = d.parse(beforeDateTime);
			java.util.Date d2 = d.parse(afterDateTime);

			timeCount = (d2.getTime() - d1.getTime()) / 1000;

		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return timeCount;
	}

	// 获取相隔时间数
	static public long getDistinceTime(Date begin, Date end) {
		long timeCount = 0;
		if (begin != null && end != null) {
			timeCount = (end.getTime() - begin.getTime()) / 1000;
		}
		return timeCount;
	}

	// 获取相隔时间数
	static public long getDistinceTime(String beforeDateTime) throws ParseException {
		return getDistinceTime(beforeDateTime, new Timestamp(System.currentTimeMillis()).toLocaleString());
	}

	/**
	 * 根据传入的开始日期点和结束日期点,获取hh:mm:ss
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	static public String getDistinceTimeString(Date begin, Date end) {
		long timeCount = 0;
		if (begin != null && end != null) {
			timeCount = (end.getTime() - begin.getTime());
		}
		return getHHmmssStr(timeCount);
	}

	/**
	 * 将毫秒转化为时分秒，并使用XX：XX：xx输出
	 * 
	 * @param ms 时间的毫秒数
	 * @return
	 */
	static public String getHHmmssStr(Long ms) {
		if (null == ms) {
			return null;
		}
		if (ms == 0) {
			return "00:00:00";
		}
		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;

		long hour = ms / hh;
		long minute = (ms - hour * hh) / mi;
		long second = (ms - hour * hh - minute * mi) / ss;
		long milliSecond = ms - hour * hh - minute * mi - second * ss;

		String strHour = hour < 10 ? "0" + hour : "" + hour;
		String strMinute = minute < 10 ? "0" + minute : "" + minute;
		String strSecond = second < 10 ? "0" + second : "" + second;
		String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;
		strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
		return strHour + ":" + strMinute + ":" + strSecond;
	}

	/**
	 * 获取某一时间段特定星期几的日期
	 * 
	 * @param dateFrom 开始时间
	 * @param dateEnd 结束时间
	 * @param weekDays 星期
	 * @return 返回时间数组
	 */
	public static Date[] getDates(String dateFrom, String dateEnd, String weekDays) {
		long time = 1l;
		long perDayMilSec = 24 * 60 * 60 * 1000;
		List<Date> dateList = new ArrayList<Date>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 需要查询的星期系数
		String strWeekNumber = weekDays;
		try {
			dateFrom = sdf.format(sdf.parse(dateFrom).getTime() - perDayMilSec);
			while (true) {
				time = sdf.parse(dateFrom).getTime();
				time = time + perDayMilSec;
				Date date = new Date(time);
				dateFrom = sdf.format(date);
				if (dateFrom.compareTo(dateEnd) <= 0) {
					// 查询的某一时间的星期系数
					Integer weekDay = dayForWeek(date);
					// 判断当期日期的星期系数是否是需要查询的
					if (weekDay.toString().indexOf(strWeekNumber) != -1) {
						dateList.add(date);
					}
				} else {
					break;
				}
			}
		} catch (ParseException e1) {
			logger.error(e1.getMessage(), e1);
		}
		return dateList.toArray(new Date[dateList.size()]);
	}

	/**
	 * 获取某一时间段特定星期几的日期
	 * 
	 * @param dateFrom 开始时间
	 * @param dateEnd 结束时间
	 * @param weekDays 星期
	 * @return 返回时间数组
	 */
	public static List<Date> getDates(Date dateFrom, Date dateEnd, String weekDays) {
		long time = 1l;
		long perDayMilSec = 24 * 60 * 60 * 1000;
		List<Date> dateList = new ArrayList<Date>();
		// 需要查询的星期系数
		String strWeekNumber = weekDays;

		long from = dateFrom.getTime() - perDayMilSec;
		time = from;
		while (true) {

			time = time + perDayMilSec;
			if (time <= dateEnd.getTime()) {
				Date date = new Date(time);
				// 查询的某一时间的星期系数
				Integer weekDay = dayForWeek(date);
				// 判断当期日期的星期系数是否是需要查询的
				if (strWeekNumber.indexOf(weekDay.toString()) != -1) {
					dateList.add(date);
				}
			} else {
				break;
			}
		}

		return dateList;
	}

	public static Integer dayForWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (week == 0) {
			week = 7;
		}
		return week;
	}

	public static String chineseDayForWeek(Date date) {
		String str = "";
		switch (dayForWeek(date)) {
		case 1:
			str = "周一";
			break;
		case 2:
			str = "周二";
			break;
		case 3:
			str = "周三";
			break;
		case 4:
			str = "周四";
			break;
		case 5:
			str = "周五";
			break;
		case 6:
			str = "周六";
			break;
		case 7:
			str = "周日";
			break;
		}
		return str;
	}

	/**
	 * 获取当天开始时间
	 * 
	 * @param date 日期字符串
	 * @return 返回时间
	 */
	public static Date getStartTime4Day(String date) {
		Date datetime = parseDate(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(datetime);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	public static Date addDays(Date date, int days) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		// calendar.add(calendar.DAY_OF_MONTH,1);
		date = calendar.getTime();
		calendar.add(calendar.DATE, days);
		return calendar.getTime();
	}

	public static Date addDateField(Date now, int field, int amount) {
		if (now == null) {
			return null;
		}
		if (amount == 0) {
			return now;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(field, amount);
		return c.getTime();
	}

	/**
	 * 获取当天结束时间
	 * 
	 * @param date 日期字符串
	 * @return 返回时间
	 */
	public static Date getEndTime4Day(String date) {
		Date datetime = parseDate(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(datetime);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	/**
	 * 获取当天开始时间
	 * 
	 * @param date 日期
	 * @return 返回时间
	 */
	public static Date getStartTime4Day(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取当天结束时间
	 * 
	 * @param date 日期
	 * @return 返回时间
	 */
	public static Date getEndTime4Day(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	/**
	 * teeTime int转字符串
	 * 
	 * @param Integer 日期
	 * @return teeTime
	 */
	public static String int2TeeTime(Integer time) {
		String teeTime = null;
		if (time % 60 == 0) {
			teeTime = time / 60 + ":00";
		} else {
			teeTime = time / 60 + ":" + time % 60;
		}
		return teeTime;
	}

	/**
	 * teeTime 字符串转int
	 * 
	 * @param String 日期
	 * @return teeTime
	 */
	public static Integer teeTime2Int(String teeTime) {
		String[] tees = teeTime.split(":");
		int hour = Integer.parseInt(tees[0]) * 60;
		int minute = Integer.parseInt(tees[1]);
		return hour + minute;
	}

	public static Timestamp nowTimeStamp() {
		return new Timestamp(new Date().getTime());
	}

	public static Date toSimpleDate(Calendar cal) {

		return deepCopyForSimple(cal).getTime();

	}

	/**
	 * Calendar深拷贝(日期不带时分秒)
	 * 
	 * @param a
	 * @return
	 */
	public static Date toOnlyDate(Calendar cal) {

		return getStartTime4Day(deepCopyForSimple(cal).getTime());

	}

	public static List<Date> toSimpleDates(List<Calendar> cals) {
		if (cals == null) {
			return null;
		}

		List<Date> dates = new ArrayList<Date>();

		for (Calendar calendar : cals) {
			dates.add(toSimpleDate(calendar));
		}
		return dates;

	}

	/**
	 * Calendar深拷贝(日期不带时分秒)
	 * 
	 * @param a
	 * @return
	 */
	public static Calendar deepCopyForSimple(Calendar a) {
		Calendar date = Calendar.getInstance();
		date.set(a.get(Calendar.YEAR), a.get(Calendar.MONTH), a.get(Calendar.DATE), 0, 0, 0);
		return date;
	}

	/**
	 * Date深拷贝
	 * 
	 * @param a
	 * @return
	 */
	public static Date deepCopyDate(Date a) {
		Date date = new Date();
		date.setTime(a.getTime());
		return date;
	}
}