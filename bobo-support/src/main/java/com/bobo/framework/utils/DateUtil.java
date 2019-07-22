package com.bobo.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

/**
 * 日期工具类
 * 
 * @author
 * 
 */
public class DateUtil {

	@SuppressWarnings("unused")
	private static long milliSecond = 1413424927;

	/**
	 * 将Date类型转换为字符串
	 * 
	 * @param date
	 *            日期类型
	 * @return 日期字符串
	 */
	public static String format(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 将Date类型转换为字符串
	 * 
	 * @param date
	 *            日期类型
	 * @param pattern
	 *            字符串格式
	 * @return 日期字符串
	 */
	public static String format(Date date, String pattern) {
		if (date == null) {
			return "null";
		}
		if (pattern == null || pattern.equals("") || pattern.equals("null")) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		return new java.text.SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 将字符串转换为Date类型
	 * 
	 * @param date
	 *            字符串类型
	 * @return 日期类型
	 */
	public static Date format(String date) {
		return format(date, null);
	}

	/**
	 * 将字符串转换为Date类型
	 * 
	 * @param date
	 *            字符串类型
	 * @param pattern
	 *            格式
	 * @return 日期类型
	 */
	public static Date format(String date, String pattern) {
		if (pattern == null || pattern.equals("") || pattern.equals("null")) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		if (date == null || date.equals("") || date.equals("null")) {
			pattern = "mm:ss";
		}
		Date d = new Date();
		try {
			d = new java.text.SimpleDateFormat(pattern).parse(date);
		} catch (ParseException pe) {
		}
		return d;
	}

	/**
	 * 
	 * Description:格式化日期，date转换为string
	 * 
	 * @param date
	 * @param dtFormat
	 *            例如:yyyy-MM-dd HH:mm:ss yyyyMMdd
	 * @return
	 */
	public static String fmtDateToStr(Date date, String dtFormat) {
		if (date == null)
			return "";
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(dtFormat);
			return dateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static Integer getToday() {
		Long currentTime = System.currentTimeMillis();
		currentTime = currentTime / 1000;

		return currentTime.intValue();
	}

	public static String getDateByTime(Integer time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sd = sdf.format(new Date(Long.parseLong(time.toString()) * 1000));
		return sd;
	}

	public static String getDateByTime(Integer time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String sd = sdf.format(new Date(Long.parseLong(time.toString()) * 1000));
		return sd;
	}

	public static Integer getThatDate(Date d) {
		Long thatDateTime = d.getTime() / 1000;
		return thatDateTime.intValue();
	}

	public static int getThatDateForTimeStamp(String string_u) {
		int currentTime = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			currentTime = Integer.parseInt(String.valueOf(sdf.parse(string_u).getTime() / 1000));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return currentTime;
	}

	public static Date fmtStrToDate(String dtFormat) {
		if (dtFormat == null || dtFormat.trim().equals("")) {
			return null;
		}
		try {
			if (dtFormat.length() == 9 || dtFormat.length() == 8) {
				String[] dateStr = dtFormat.split("-");
				dtFormat = dateStr[0] + (dateStr[1].length() == 1 ? "-0" : "-") + dateStr[1]
						+ (dateStr[2].length() == 1 ? "-0" : "-") + dateStr[2];
			}
			if (dtFormat.length() != 10 & dtFormat.length() != 19)
				return null;
			if (dtFormat.length() == 10)
				dtFormat = dtFormat + " 00:00:00";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dateFormat.parse(dtFormat);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * Description:只保留日期中的年月日
	 * 
	 * @param date
	 * @return
	 * @author 孙钰佳 @since：2007-12-10 上午11:25:50
	 */
	public static Date toShortDate(Date date) {
		String strD = fmtDateToStr(date, "yyyy-MM-dd");
		return fmtStrToDate(strD);
	}

	public static Date getTodayStr() {

		return toShortDate(new Date());
	}

	public static Date getAddMinute(String minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, new Integer(minute));
		return calendar.getTime();
	}

	public static Date getAddDate(String date) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, new Integer(date));
		return calendar.getTime();
	}

	/**
	 * 得到增加天数后的时间
	 * 
	 * @Title: addDays
	 * @Description: TODO
	 * @param date
	 *            时间
	 * @param days
	 *            需要增加的天数
	 * @return
	 * @return: Date
	 */
	public static Date addDays(Date date, String days) {
		if (date == null)
			return null;
		try {

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, new Integer(days));
			return calendar.getTime();

		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 获取当前月的第一天
	 * 
	 * @Title: getFirstDayOfCunMonth
	 * @Description: TODO
	 * @return
	 * @return: Date
	 */
	public static Date getFirstDayOfCunMonth() {
		try {

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			return calendar.getTime();

		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 获取下个月的最后一天
	 * 
	 * @Title: getLastDayOfCunMonth
	 * @Description: TODO
	 * @return
	 * @return: Date
	 */
	public static Date getLastDayOfCunMonth() {
		try {

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			return calendar.getTime();

		} catch (Exception e) {
			return null;
		}

	}

	public static Integer getAddMinuteInteger(String minute) {
		return new Long(getAddMinute(minute).getTime() / 1000).intValue();
	}

	public static String timeStampToDate(String timeStamp, String dtFormat) {

		SimpleDateFormat sdf = new SimpleDateFormat(dtFormat);
		return sdf.format(new Date(Long.parseLong(timeStamp)));
	}

	// 获取指定天数的日期 时 分 秒都为0
	public static Date getDateBefore(int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(toShortDate(new Date()));
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	// 获取时间戳（精确到秒，i进制）
	@SuppressWarnings("static-access")
	public static String getTimestamp(int i) {
		Long l = new Long(10);
		long mic = System.currentTimeMillis() / 1000;
		return l.toString(mic, i).toUpperCase();
	}

	public static Long getDelayBetweenTwoShot(String utcTimeStr) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date = new Date();
		try {
			date = format.parse(utcTimeStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Long endTime = date.getTime();
		Long startTime = new Date().getTime();
		Long offset = (endTime - startTime) / 1000 - 120;
		return offset;
	}

	/**
	 * 
	 * @Title: getDateBy24Time
	 * @Description: 获取 格式 为 20151116103012025 的时间串，到毫秒
	 * @return
	 * @return: String
	 */
	public static String getDateBy24Time() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String sd = sdf.format(new Date());
		return sd;
	}

	/**
	 * 为当前日期增加指定的秒
	 * 
	 * @param baseDate
	 *            基础日期
	 * @param addNum
	 *            增加的秒
	 * @return String 增加秒后yyyyMMddHHmmss格式的日期
	 * @throws ParseException
	 *             日期格式异常
	 */
	public static String addDate(String baseDate, int addNum, String format) throws ParseException {
		SimpleDateFormat df = null;

		if (StringUtils.isBlank(format)) {
			df = new SimpleDateFormat("yyyyMMddHHmmss");
		} else {
			df = new SimpleDateFormat(format);
		}
		Date date = df.parse(baseDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, addNum);
		return df.format(cal.getTime());
	}

	/**
	 * 日期时间加减操作 Example: addDate(date, Calendar.MONTH, -1);
	 * 
	 * @param date
	 * @return date
	 */
	public static Date addDate(Date date, int calendarField, int amount) {
		if (null == date) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(calendarField, amount);
		return calendar.getTime();
	}

	/*
	 * 取得当前时间(格式:20080109151259)
	 * 
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar theday = Calendar.getInstance();
		String currTime = df.format(theday.getTime());

		return currTime;
	}

	/**
	 * 取得当前月份(格式:04)
	 * 
	 * 
	 * @return
	 */
	public static String getCurrentMonth() {
		SimpleDateFormat df = new SimpleDateFormat("MM");
		Calendar theday = Calendar.getInstance();
		String currTime = df.format(theday.getTime());

		return currTime;
	}

	/**
	 * @return
	 */
	public static String getCurrentMonth(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		Calendar theday = Calendar.getInstance();
		String currTime = df.format(theday.getTime());
		return currTime;
	}

	/**
	 * 取得下个月第一天时间
	 * 
	 * @return 2012-04-01 00:00:00
	 */
	public static String getNextMonthFirstDay() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.MONTH, 1);
		lastDate.set(Calendar.DATE, 1);
		str = sdf.format(lastDate.getTime()) + " 00:00:00";
		return str;
	}

	/**
	 * 取得下周第n天时间
	 * 
	 * @return 2012-04-01
	 */
	public static String getNextWeekFirstDay(int day) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.WEEK_OF_MONTH, 1);
		lastDate.set(Calendar.DAY_OF_WEEK, day);
		str = sdf.format(lastDate.getTime());
		return str;
	}

	/**
	 * 取得当前时间
	 * 
	 * @param pattern
	 *            当前时间的格式 'yyyy-MM-dd HH:mm:ss' 'yyyy-MM-dd' 'yyyyMMddHHmmss'
	 * @return
	 */
	public static String getCurrentTime(String pattern) {
		return formatDate(pattern, new Date());
	}

	/**
	 * 取得当前时间前days天的时间
	 * 
	 * @param pattern
	 *            时间格式
	 * @param days
	 * @return
	 */
	public static String getBeforeCurrentTime(String pattern, int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1 * days);
		Date date = cal.getTime();
		return formatDate(pattern, date);
	}

	// /**
	// * 判断时间是否过期，过期返回true
	// *
	// * @param startTime
	// * 起始时间
	// * @param timeLimit
	// * 时间限定长度
	// * @return
	// */
	// public static boolean isOverTime(String startTime, int timeLimit) {
	// try {
	// String limitTime = DateUtil.addDate(startTime, timeLimit, null);
	// String currTime = DateUtil.getCurrentTime();// 取得当前时间
	// if (new Long(currTime).compareTo(new Long(limitTime)) > 0) {
	// return true;
	// } else {
	// return false;
	// }
	// } catch (ParseException e) {
	// return true;
	// }
	// }

	/**
	 * 格式化日期 yyyy-MM-dd HH:mm:ss
	 */
	public static String dateToyyyyMMddHHmmss(Date date) {
		return formatDate("yyyy-MM-dd HH:mm:ss", date);
	}

	/**
	 * 格式化日期
	 * 
	 * @param pattern
	 * @param date
	 * @return
	 */
	public static String formatDate(String pattern, Date date) {
		String s = "";

		if (null != date) {
			s = new SimpleDateFormat(pattern).format(date);
		}
		// else 缺省值 ""

		return s;
	}

	/**
	 * 格式化日期
	 * 
	 * @param pattern
	 * @param date
	 * @return
	 */
	public static Date formatDate(String pattern, String date) {
		Date s = new Date();

		try {
			if (null != date) {
				s = new SimpleDateFormat(pattern).parse(date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return s;
	}

	/**
	 * 验证结束开始日期、日期格式是否正确 验证结束日期是否小于开始日期 验证结束日期是否大于当前日期 日期格式必须为：yyyy-mm-dd
	 * 
	 * @param startDate
	 * @param endDate
	 * @param nowDate
	 * @return String[] 如果验证通过，返回空字符串，否则返回错误信息
	 */
	public String[] validDate(String startDate, String endDate, String nowDate) {
		// 初始化数组，共6个元素，默认为空字符串
		String[] returnKey = { "", "", "", "", "", "" };

		// 验证开始日期是否为空和是否符合格式
		if (ValiUtil.isEmpty(startDate)) {
			returnKey[0] = "请选择起始日期！";
		} else if (!ValiUtil.isDate(startDate)) {
			returnKey[1] = "起始日期格式有误，请重新填写！";
		}

		// 验证结束日期是否为空和是否符合格式
		if (ValiUtil.isEmpty(endDate)) {
			returnKey[2] = "请选择结束日期！";
		} else if (!ValiUtil.isDate(endDate)) {
			returnKey[3] = "截止日期格式有误，请重新填写！";
		}

		// 验证结束日期是否小于开始日期和结束日期是否大于当前日期
		if (ValiUtil.isDate(startDate) && ValiUtil.isDate(endDate)) {
			int startdate = Integer.parseInt(startDate.replaceAll("-", ""));
			int enddate = Integer.parseInt(endDate.replaceAll("-", ""));
			int nowdate = Integer.parseInt(nowDate.replaceAll("-", ""));
			if (startdate > enddate) {
				returnKey[4] = "起始日期大于截止日期，请重新填写！";
			}
			if (enddate > nowdate) {
				returnKey[5] = "截止日期必须小于等于今天，请重新填写！";
			}
		}

		return returnKey;
	}

	/**
	 * 截取字符串,将精确到时分秒的数据截取到年月日 例：将“2005-12-06 09:51:57”截取为“2005-12-06”
	 * 
	 * @param str
	 * @return
	 */
	public static String interDateString(String str) {
		String rtnStr = "";
		if (null != str) {
			// 截取时间年月日格式
			rtnStr = str.indexOf(" ") != -1 ? str.substring(0, str.indexOf(" ")) : "";
		} else {
			rtnStr = "";
		}
		return rtnStr;
	}

	/**
	 * 按默认输入日期格式为yyyyMMdd进行时间月份日期处理
	 * 
	 * @param inDate
	 *            默认格式 yyyyMMdd
	 * @param pattern
	 *            返回日期的格式
	 * @param num
	 *            减去的月份数
	 * @return 处理后的时间
	 */
	public static String getLastMonth(String inDate, String pattern, int num) {
		return getLastMonth(inDate, "yyyyMMdd", pattern, num);
	}

	/**
	 * 按当前日期进行时间月份日期处理
	 * 
	 * @param pattern
	 *            返回日期的格式
	 * @param num
	 *            减去的月份数
	 * @return 处理后的时间
	 */
	public static String getLastMonth(String pattern, int num) {
		return getLastMonth(DateUtil.formatDate("yyyyMMdd", new Date()), "yyyyMMdd", pattern, num);
	}

	/**
	 * 时间月份日期处理
	 * 
	 * @param inDate
	 *            输入日期
	 * @param intPattern
	 *            输入日期的格式
	 * @param outPattern
	 *            返回日期的格式
	 * @param num
	 *            减去的月份数
	 * @return 处理后的时间
	 */
	public static String getLastMonth(String inDate, String inPattern, String outPattern, int num) {
		Date dates = DateUtil.formatDate(inPattern, inDate);
		Calendar cal = Calendar.getInstance();
		if (dates != null)
			cal.setTime(dates);
		cal.add(Calendar.MONTH, -num);

		return DateUtil.formatDate(outPattern, cal.getTime());
	}

	/**
	 * 获得某一月的最后一天
	 * 
	 * @param monthPattern
	 * @return
	 */
	public static int getActualMaximum(String theMonth) {
		Calendar c = Calendar.getInstance();

		Date theDate = null;
		SimpleDateFormat s = new SimpleDateFormat("yyyyMM");
		try {
			theDate = s.parse(theMonth);
			c.setTime(theDate);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// 最后一天
		int endDate = c.getActualMaximum(Calendar.DATE);
		return endDate;
	}

	/**
	 * 
	 * @param startTime
	 *            开始日期，格式为2011-11-20 “-”可替换为任意分隔符
	 * @param endTime
	 * @return 日期的中文表示格式 如2011年11月20日
	 */
	public static String getChineseTime(String startTime, String endTime) {
		StringBuffer sb = new StringBuffer();
		if (endTime.equals(startTime)) {
			sb.append(startTime.substring(0, 4)).append("年").append(startTime.substring(5, 7)).append("月")
					.append(startTime.substring(8, 10)).append("日");
		} else {
			sb.append(startTime.substring(0, 4)).append("年").append(startTime.substring(5, 7)).append("月")
					.append(startTime.substring(8, 10)).append("日至").append(endTime.substring(0, 4)).append("年")
					.append(endTime.substring(5, 7)).append("月").append(endTime.substring(8, 10)).append("日");
		}
		return sb.toString();
	}

	/**
	 * 获取下一天
	 * 
	 * @param strTime
	 * @param format
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getNextDay(String strTime, String format) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String reTime = "";
		try {
			Date date = sdf.parse(strTime);
			cal.setTime(date);
			cal.add(Calendar.DATE, 1);
			reTime = sdf.format(cal.getTime());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return reTime;
	}

	/**
	 * 获取后一个月的第一天.
	 * <P>
	 * 
	 * @param fmt
	 *            日期
	 * @return String 下个月第一天
	 */
	public static String getNextMonthFirstDay(String fmt) {
		Date date = null;

		Calendar cl = Calendar.getInstance();
		cl.set(Calendar.MONTH, cl.get(Calendar.MONTH) + 1);
		cl.set(Calendar.DAY_OF_MONTH, 1);

		date = cl.getTime();

		return formatDate(fmt, date);
	}

	/**
	 * 获取后一个月的第一天.
	 * <P>
	 * 
	 * @param nowdate
	 *            现在日期
	 * @param inFormat
	 *            输入格式
	 * @param outFormat
	 *            输出格式
	 * @return String 后一个月的第一天日期
	 * @throws ParseException
	 *             解析日期异常
	 */
	public static String getNextMonthFistDay(String nowdate, String inFormat, String outFormat) {

		Date date = formatDate(inFormat, nowdate);

		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.set(Calendar.MONTH, cl.get(Calendar.MONTH) + 1);
		cl.set(Calendar.DAY_OF_MONTH, 1);

		date = cl.getTime();
		return formatDate(outFormat, date);
	}

	/**
	 * 所属月第一天.
	 * 
	 * @author chylg
	 * @param date
	 *            输入日期
	 * @param format
	 *            输出日期格式
	 * @return 日期字符串
	 */
	public static String firstDay(Date date, String format) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return formatDate(format, cal.getTime());
	}

	/**
	 * 当月第一天.
	 * 
	 * @author chylg
	 * @param format
	 *            输出日期格式
	 * @return 日期字符串
	 */
	public static String firstDay(String format) {
		return firstDay(new Date(), format);
	}

	/**
	 * 所属月最后一天.
	 * 
	 * @author chylg
	 * @param date
	 *            日期
	 * @param format
	 *            日期格式
	 * @return 日期字符串
	 */
	public static String lastDay(Date date, String format) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);
		return formatDate(format, cal.getTime());
	}

	/**
	 * 当前月最后一天.
	 * 
	 * @author chylg
	 * @param format
	 *            日期格式
	 * @return 日期字符串
	 */
	public static String lastDay(String format) {
		return lastDay(new Date(), format);
	}

	/**
	 * 近月（n月前/后），n为正数为n月后，n为负数为n月前.
	 * 
	 * @author chylg
	 * @param format
	 *            日期格式
	 * @param num
	 *            相邻月数
	 * @return 日期
	 */
	public static String nearMonth(String format, int num) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + num);
		return formatDate(format, cal.getTime());
	}

	/**
	 * 获取指定日期的进几天(n天前/后) n为正数为n天后，n为负数为n天前
	 * 
	 * @param date
	 * @param format
	 * @param num
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String nearDay(Date date, String format, int num) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, num);
		return formatDate(format, cal.getTime());
	}

	/**
	 * 获取某个月某一天 <功能详细描述>
	 * 
	 * @param month
	 * @param date
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getMonthDay(int month, int date, String pattern) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, date);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, month);// 减一个月，变为下月的1号

		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得当天0点时间
	public static Date getTimesmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();

	}

	// 获得昨天0点时间
	public static Date getYesterdaymorning() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(getTimesmorning().getTime() - 3600 * 24 * 1000);
		return cal.getTime();
	}

	// 获得当天近7天时间
	public static Date getWeekFromNow() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(getTimesmorning().getTime() - 3600 * 24 * 1000 * 7);
		return cal.getTime();
	}

	// 获得当天24点时间
	public static Date getTimesnight() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	// 获得本周一0点时间
	public static Date getTimesWeekmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal.getTime();
	}

	// 获得本周日24点时间
	public static Date getTimesWeeknight() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getTimesWeekmorning());
		cal.add(Calendar.DAY_OF_WEEK, 7);
		return cal.getTime();
	}

	// 获得本月第一天0点时间
	public static Date getTimesMonthmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	// 获得本月最后一天24点时间
	public static Date getTimesMonthnight() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 24);
		return cal.getTime();
	}

	public static Date getLastMonthStartMorning() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getTimesMonthmorning());
		cal.add(Calendar.MONTH, -1);
		return cal.getTime();
	}

	public static Date getCurrentQuarterStartTime() {
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 3)
				c.set(Calendar.MONTH, 0);
			else if (currentMonth >= 4 && currentMonth <= 6)
				c.set(Calendar.MONTH, 3);
			else if (currentMonth >= 7 && currentMonth <= 9)
				c.set(Calendar.MONTH, 4);
			else if (currentMonth >= 10 && currentMonth <= 12)
				c.set(Calendar.MONTH, 9);
			c.set(Calendar.DATE, 1);
			now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 当前季度的结束时间。即2012-03-31 23:59:59
	 *
	 * @return
	 */
	public static Date getCurrentQuarterEndTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCurrentQuarterStartTime());
		cal.add(Calendar.MONTH, 3);
		return cal.getTime();
	}

	public static Date getCurrentYearStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.YEAR));
		return cal.getTime();
	}

	public static Date getCurrentYearEndTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCurrentYearStartTime());
		cal.add(Calendar.YEAR, 1);
		return cal.getTime();
	}

	public static Date getLastYearStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCurrentYearStartTime());
		cal.add(Calendar.YEAR, -1);
		return cal.getTime();
	}

	public static Date getTimesmorningByTimestamp(long timestamp) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(timestamp));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		return cal.getTime();
	}

	public static int getTodayZero() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return (int) (calendar.getTimeInMillis() / 1000);
	}

	public static int getCheckEndTime(int days) {
		return getTodayZero() - 1 + 24 * 60 * 60 * (days + 1);
	}

	/**
	 * 获取上月第一天时间戳
	 * 
	 * @return
	 */
	public static int getFirstDateOfLastMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return (int) (calendar.getTimeInMillis() / 1000);
	}

	/**
	 * 获取上月最后一天时间戳
	 * 
	 * @return
	 */
	public static int getLastDateOfLastMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return (int) (calendar.getTimeInMillis() / 1000);
	}

	/**
	 * 获取前2个月第一天
	 * 
	 * @return
	 */
	public static int getFirstDateOfLastTwoMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -2);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return (int) (calendar.getTimeInMillis() / 1000);
	}

	/**
	 * 获取前2个月最后一天时间戳
	 * 
	 * @return
	 */
	public static int getLastDateOfLastTwoMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return (int) (calendar.getTimeInMillis() / 1000);
	}

	/**
	 * 获取前n个月第一天
	 * 
	 * @return
	 */
	public static int getFirstDateOfLastNMonth(int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -n);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return (int) (calendar.getTimeInMillis() / 1000);
	}

	/**
	 * 获取前n个月最后一天时间戳
	 * 
	 * @return
	 */
	public static int getLastDateOfLastNMonth(int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -(n - 1));
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return (int) (calendar.getTimeInMillis() / 1000);
	}

	/**
	 * 获取前n天时间戳
	 * 
	 * @param days
	 * @return
	 */
	public static int getDaysBeforeTime(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1 * days);
		return (int) (calendar.getTimeInMillis() / 1000);
	}
	
public static String secondsToCN(long seconds) {
		
		if(seconds <3600) {
			return "1小时";
		}
		
        int d = (int)seconds / (60 * 60 * 24);
        int h = (int)(seconds - (60 * 60 * 24 * d)) / 3600;
        StringBuilder build = new StringBuilder();
        if(d>0)
        	build = build.append(d+"天");
        if(h>0)
        	build = build.append(h+"小时");
        return build.toString();
 
    }

	public static void main(String[] args) {
		System.out.println(getCurrentMonth("YYYYMM"));
		/*
		 * System.out.println("当前验货截止时间：" + getCheckEndTime(300));
		 * System.out.println("当天24点时间：" + getTimesnight());
		 * System.out.println("当前时间：" + new Date());
		 * System.out.println("当天0点时间：" + getTimesmorning());
		 * System.out.println("昨天0点时间：" + getYesterdaymorning());
		 * System.out.println("近7天时间：" + getWeekFromNow());
		 * System.out.println("本周周一0点时间：" + getTimesWeekmorning());
		 * System.out.println("本周周日24点时间：" + getTimesWeeknight());
		 * System.out.println("本月初0点时间：" + getTimesMonthmorning());
		 * System.out.println("本月未24点时间：" + getTimesMonthnight());
		 * System.out.println("上月初0点时间：" + getLastMonthStartMorning());
		 * System.out.println("本季度開始点时间：" + getCurrentQuarterStartTime());
		 * System.out.println("本季度结束点时间：" + getCurrentQuarterEndTime());
		 * System.out.println("本年開始点时间：" + getCurrentYearStartTime());
		 * System.out.println("本年结束点时间：" + getCurrentYearEndTime());
		 * System.out.println("上年開始点时间：" + getLastYearStartTime());
		 */

	}

}
