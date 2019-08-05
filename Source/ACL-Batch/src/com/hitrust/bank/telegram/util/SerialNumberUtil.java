package com.hitrust.bank.telegram.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.TimeZone;

import com.hitrust.bank.telegram.util.StringUtil;

public class SerialNumberUtil {
	
	public static String getCorrID(String str){
		if (str.length()>7){
			str = str.substring(0,7);
		}else{
			str = StringUtil.padString(str, 7, "0", "LEFT");
		}
		return str + currDateTimeM();
	}
	
	public static String getCorrIDTest(){
		//2012-12-07_JerryChien_調整CorrID有年月日時分秒+1豪+4亂數
		//SimpleDateFormat s = new SimpleDateFormat("yyyyMMHHmmssS");
		SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmssS");
		StringBuffer sb = new StringBuffer("FCB11");
		sb.append(s.format(new Date()));
		Random r = new Random();
		sb.append((String.valueOf(r.nextInt(100000))+"0000000").substring(0,7));
		return sb.toString().substring(0,24); 
	}
	
	public static String currDateTimeM() {
		//GregorianCalendar now = new GregorianCalendar();
		TimeZone tz = TimeZone.getTimeZone("GMT+08:00");
		//use GMT timezone
		GregorianCalendar now = new GregorianCalendar(tz);

		String sCurrDateTime = "";
		String year = "0000" + String.valueOf(now.get(Calendar.YEAR));
		String month = "0" + String.valueOf(now.get(Calendar.MONTH) + 1);
		String date = "0" + String.valueOf(now.get(Calendar.DATE));
		String hours = "0" + String.valueOf(now.get(Calendar.HOUR_OF_DAY));
		String minutes = "0" + String.valueOf(now.get(Calendar.MINUTE));
		String seconds = "0" + String.valueOf(now.get(Calendar.SECOND));
		String milliSec = "00"+String.valueOf(now.get(Calendar.MILLISECOND));
		sCurrDateTime = year.substring(year.length() - 4) + //"/" + 
						month.substring(month.length() - 2) + //"/" + 
						date.substring(date.length() - 2) + //"T" + 
		                hours.substring(hours.length() - 2) + //":" + 
		                minutes.substring(minutes.length() - 2) + //":" + 
		                seconds.substring(seconds.length() - 2)+ //":" +
		                milliSec.substring(milliSec.length() - 3);
												
		return sCurrDateTime;
	}

	//temp method add by testing 
	public static String getCorrID(){
		//2012-12-07_JerryChien_調整CorrID有年月日時分秒+1豪+4亂數
		//SimpleDateFormat s = new SimpleDateFormat("yyyyMMHHmmssS");
		SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmssS");
		StringBuffer sb = new StringBuffer("FCB11");
		sb.append(s.format(new Date()));
		Random r = new Random();
		sb.append((String.valueOf(r.nextInt(100000))+"0000000").substring(0,7));
		return sb.toString().substring(0,24); 
	}
	
	
	//temp method add by testing 
	public static String getEmailTxKey(String ntcType){
		StringBuffer sb = new StringBuffer("CNB"+ntcType);
		try {
              Thread.sleep(100);
         } catch (InterruptedException e1) {
        }
		sb.append(String.valueOf(System.currentTimeMillis()));
		Random r = new Random();
		sb.append((String.valueOf(r.nextInt(100000))+"0000000").substring(0,7));
		return sb.toString(); 
	}
}
