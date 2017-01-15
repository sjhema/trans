package com.primovision.lutransport.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.Months;

public class PaymentUtil {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	
	public static void main(String[] args) {
	}
	
	public static int calculateNoOfPayments(String startDate, String endDate) {
		Date startDateDt = null;
		Date endDateDt = null;
		try {
		   startDateDt = dateFormat.parse(startDate);
			endDateDt = dateFormat.parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Calendar startCal = new GregorianCalendar();
		startCal.setTime(startDateDt);
		Calendar endCal = new GregorianCalendar();
		endCal.setTime(endDateDt);
		
		DateTime startDateTime = new DateTime().
				withDate(startCal.get(Calendar.YEAR), startCal.get(Calendar.MONTH)+1, 1);
		DateTime endDateTime = new DateTime().
				withDate(endCal.get(Calendar.YEAR), endCal.get(Calendar.MONTH)+1, 1);
		
		Months months = Months.monthsBetween(startDateTime, endDateTime);
		return months.getMonths() + 1;
	}
	
	public static int calculateNoOfPaymentsLeft(int noOfPayments, String endDate, String paymentDueDomStr) {
		Date endDateDt = null;
		try {
			endDateDt = dateFormat.parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return calculateNoOfPaymentsLeft(noOfPayments, endDateDt, paymentDueDomStr);
	}
	
	public static int calculateNoOfPaymentsLeft(int noOfPayments, Date endDate, String paymentDueDomStr) {
		Date today = new Date();
		if (today.after(endDate)) {
			return 0;
		}
		
		String todayStr = dateFormat.format(today);
		String endDateStr = dateFormat.format(endDate);
		int noOfMonthsFromToday = calculateNoOfPayments(todayStr, endDateStr);
		
		int paymentDueDom = Integer.parseInt(paymentDueDomStr.replaceAll("[^0-9]", StringUtils.EMPTY));
		
		Calendar todayCal = new GregorianCalendar();
		todayCal.setTime(today);
		if (todayCal.get(Calendar.DATE) > paymentDueDom) {
			noOfMonthsFromToday--;
		}
		
		return noOfMonthsFromToday;
	}
}
