package com.hupu.ad.util;

import java.util.Calendar;

/**
 * unix时间工具类
 * @author donghui
 *
 */
public class UnixTimeUtils {
	
	/**
	 * 得到精确到天的UnixTime
	 */
	public static int getCurrentUnixTimeDD(){
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(year, month, day, 0, 0, 0);
		return getIntTimeByCalendar(calendar);
	}
	
	/**
	 * 得到精确到月的UnixTime
	 */
	public static int getCurrentUnixTimeMM(){
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		calendar.set(year, month, 2, 0, 0, 0);
		calendar.add(Calendar.DATE, -1);
		return getIntTimeByCalendar(calendar);
	}
	
	/**
	 * 得到精确到秒的UnixTime
	 */
	public static int getCurrentUnixTimeSS() {
		return getIntTimeByCalendar(Calendar.getInstance());
	}
	
	/**
	 * 得到今天的开始时间
	 * @return
	 */
	public static int getCurrentStartTime(){
		return UnixTimeUtils.getCurrentUnixTimeDD();
	}
	
	/**
	 * 得到今天的结束时间
	 * @return
	 */
	public static int getCurrentEndTime(){
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(year, month, day, 23, 59, 59);
		return getIntTimeByCalendar(calendar);
	}
	
	private static int getIntTimeByCalendar(Calendar calendar){
		return new Integer((calendar.getTimeInMillis() / 1000) + "");
	}
	
	public static void main(String[] args) {
		System.out.println(UnixTimeUtils.getCurrentUnixTimeSS());
		System.out.println(UnixTimeUtils.getCurrentUnixTimeDD());
		System.out.println(UnixTimeUtils.getCurrentUnixTimeMM());
		System.out.println(UnixTimeUtils.getCurrentStartTime());
		System.out.println(UnixTimeUtils.getCurrentEndTime());
	}
}
