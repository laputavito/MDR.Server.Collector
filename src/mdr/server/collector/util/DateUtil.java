package mdr.server.collector.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {
	/**
	 * 날짜 셋팅 (더하기)
	 * @param date
	 * @param calendarType
	 * @param addDte
	 * @return
	 */
	public static String getDateAdd(int calendarType, int addDte){
		Calendar cal = new GregorianCalendar(Locale.KOREA);
		cal.setTime(new Date());
		cal.add(calendarType, addDte);
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		return fm.format(cal.getTime());
	}
	/**
	 * 해당월 마지막 날짜 조회
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static String getLastDayOfMonth(String strDate) throws ParseException{
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		Date tdate = fm.parse(strDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(tdate);
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		//int firstDay = cal.getActualMinimum(Calendar.DATE);
		return lastDay < 10 ? "0" + String.valueOf(lastDay) : String.valueOf(lastDay);
	}
}
