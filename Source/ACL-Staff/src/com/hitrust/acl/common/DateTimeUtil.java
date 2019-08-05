/**
 * @(#)DateTimeUtil.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 處理日期的格式化，轉換等操作。
 * 
 * Modify History:
 *  v1.00, 2018/04/17, Darren Tsai
 *   1) First release
 *  
 */
package com.hitrust.acl.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

public class DateTimeUtil {
	private static Logger LOG = Logger.getLogger(DateTimeUtil.class);

	/**
	 * 功能：把字串轉換成Date
	 *
	 * @param str
	 * @return
	 */
	public static synchronized Date formatStr(final String str, final String pattern) {
		Date date = null;
		try {
			date = new SimpleDateFormat(pattern).parse(str);
		}
		catch (Exception e) {
			LOG.error("formatStr error" + e.toString());
		}
		return date;
	}
	
	/**
  	 * Convert string in some specific format to a Date object.
  	 *
  	 * @param dateString
  	 * @param format
  	 * @return
  	 * @throws BmsException
  	 */
  	public static Date getDateObject(String dateString, String format) throws ParseException {
  		if ((dateString == null) || (dateString.length() == 0)) {
  			return null;
  		}
  		String parrten[] = new String[1];
  		parrten[0]=format;
  		return DateUtils.parseDateStrictly(dateString, parrten );
  	}
  	
  	/**
	 * java Date轉換為字串
	 * 
	 * @param currDate
	 *            java.util.Date
	 * @param format
	 *            字串格式
	 * @return
	 */
	public static String converToString(java.util.Date date, String format) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dtFormatdB = null;
		try {
			dtFormatdB = new SimpleDateFormat(format);
			return dtFormatdB.format(date);
		} catch (Exception e) {
			dtFormatdB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return dtFormatdB.format(date);
			} catch (Exception ex) {
			}
		}
		return null;
	}
  	
  	

}
