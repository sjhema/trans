package com.primovision.lutransport.core.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
/**
 * @author kishor
 *
 */
public class DateUtil {
	public static int daysBetween(Date startday,Date endday){
		Calendar cal1 = new GregorianCalendar();
		 Calendar cal2 = new GregorianCalendar();
		 if(startday!=null&&endday!=null){
			 cal1.set(startday.getYear(),startday.getMonth(),startday.getDate());
			 cal2.set(endday.getYear(),endday.getMonth(),endday.getDate());
			 long d=(cal2.getTime().getTime()-cal1.getTime().getTime())/(1000 * 60 * 60 * 24);
			 
			 return (int)d+1;
		 }
		
		return 0;
		
	}
	public static void main(String[] args){
		
	}
	 public static Date get30day(){  
	        Calendar c = Calendar.getInstance();  
	        c.add(Calendar.DATE, 30);  
	        Date d = c.getTime();  
	         return d;
	    }  
}
