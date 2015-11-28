package com.primovision.lutransport.core.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

public class ReportDateUtil {
	public static SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"MM-dd-yyyy");

	public static SimpleDateFormat oracleFormatter = new SimpleDateFormat(
			"yyyy-MM-dd");

	private static Logger log = Logger.getLogger(ReportDateUtil.class);

	public static String getFromDate(String fromDateStr) {
		Date fromDate = null;
		try {
			if (!StringUtils.isEmpty(fromDateStr)) {
				if (fromDateStr.indexOf(":") == -1)
					fromDateStr += " 00:00:00";
				fromDate = dateFormatter.parse(fromDateStr);
			} else {
				/*fromDate = dateFormatter.parse("01-01-1900 00:00:00");*/
				fromDate = null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.warn("Error in date formating :" + ex);
			fromDate = Calendar.getInstance().getTime();
		}
		
		 if(!StringUtils.isEmpty(fromDateStr)){
	    	  fromDateStr = oracleFormatter.format(fromDate);
	      }
	      return fromDateStr;
	      /*return fromDateStr = oracleFormatter.format(fromDate);*/
	}

	public static String getToDate(String toDateStr) {
		Date toDate = null;
		try {
			if (!StringUtils.isEmpty(toDateStr)) {
				if (toDateStr.indexOf(":") == -1)
					toDateStr += " 23:59:59";
				toDate = dateFormatter.parse(toDateStr);
				/*toDate = DateUtils.addDays(toDate, 1);*/
			} else {
				/*toDate = dateFormatter.parse("01-01-2050 00:00:00");*/
				toDate = null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.warn("Error in date formating :" + ex);
			toDate = Calendar.getInstance().getTime();
		}
		 if(!StringUtils.isEmpty(toDateStr)){
			 toDateStr = oracleFormatter.format(toDate);
	      }
	      return toDateStr;
		/*return oracleFormatter.format(toDate);*/
	}

	public static List<String> getDateListForMonth(Date startDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		int currentMonth = cal.get(Calendar.MONTH);
		List<String> dayList = new ArrayList<String>();
		while (currentMonth == cal.get(Calendar.MONTH)) {
			dayList.add(dateFormatter.format(cal.getTime()));
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		return dayList;
	}
}
