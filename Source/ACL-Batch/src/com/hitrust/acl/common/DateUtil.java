/**
 * 日期處理類
 * <p/>
 * Package: com.hitrust.mnb.utils <br>
 * File Name: DateUtil.java <br>
 * <p/>
 * Purpose: <br>
 * 處理日期的格式化，轉換等操作 <br>
 *
 * @author JmiuHan
 * @version 1.0   2010/12/02
 *  <ul>
 *        <li> Created </li>
 *   </ul>
 */
package com.hitrust.acl.common;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;

public class DateUtil {

	private static Logger  LOG = Logger.getLogger(DateUtil.class);// Log定義

	public static final String TYPE_DATE = "D"; // 只取日期

	public static final String TYPE_TIME = "T"; // 只取時間

	public static final String TYPE_DATETIME = "DT"; // 取得日期及時間

	public static final String STYLE_XML = "X"; // XML格式
	// CCYY-MM-DDThh:mm:ss+hh:ss

	public static final String STYLE_AD = "AD"; // 西元年 CCYYMMDDhhmmss

	public static final String STYLE_ROC = "R"; // 民國年 YYYMMDDhhmmss

	public static final String STYLE_FORMAT = "F"; // 格式化 CCYY-MM-DD hh:mm:ss

	public static final String STYLE_FORMAT_FOR_USER = "FU"; // 格式化

	// CCYY/MM/DD
	// hh:mm:ss

	public DateUtil() {
	}

	/**
	 * 功能：將srcString右靠左補空白至長度len為止
	 *
	 * @param srcString
	 * @param len
	 * @return
	 * @throws Exception
	 */
	public synchronized static String padding(String srcString, int len)
			throws Exception {
		if (srcString == null) {
			throw new Exception("padding():傳入參數為null!");
		}
		String desString = null;
		// 呼叫cropping函式,避免srcString長度大於len
		srcString = cropping(srcString, len);
		// 計算srcString之長度
		int srcLen = srcString.getBytes().length;
		// 將回傳之字串補上不足之空白
		desString = srcString;
		for (int i = 0; i < (len - srcLen); i++) {
			desString = " " + desString;
		}
		return desString;
	}

	/**
	 * 功能：將srcString右靠左補空白至長度len為止
	 *
	 * @param srcString
	 * @param len
	 * @return
	 * @throws Exception
	 */
	public synchronized static String strpadding(String srcString, int len)
			throws Exception {
		if (srcString == null) {
			throw new Exception("padding():傳入參數為null!");
		}
		String desString = null;
		// 呼叫cropping函式,避免srcString長度大於len
		srcString = cropping(srcString, len);
		// 計算srcString之長度
		int srcLen = srcString.getBytes().length;
		// 將回傳之字串補上不足之空白
		desString = srcString;
		for (int i = 0; i < (len - srcLen); i++) {
			desString = "0" + desString;
		}
		return desString;
	}

	/**
	 * 功能：將srcInt右靠左補0至長度len為止
	 *
	 * @param srcInteger
	 * @param len
	 * @return
	 * @throws Exception
	 */
	public synchronized static String padding(int srcInteger, int len)
			throws Exception {
		String desString = null;
		String srcString = String.valueOf(srcInteger);
		// 呼叫cropping函式,避免srcString長度大於len
		srcString = cropping(srcString, len);
		// 計算srcString之長度
		int srcLen = srcString.length();
		// 將回傳之字串補上不足之空白
		desString = srcString;
		for (int i = 0; i < (len - srcLen); i++) {
			desString = "0" + desString;
		}
		return desString;
	}

	/**
	 * 功能：將srcLong右靠左補0至長度len為止
	 *
	 * @param srcLong
	 * @param len
	 * @return
	 * @throws Exception
	 */
	public synchronized static String padding(long srcLong, int len)
			throws Exception {
		String desString = null;
		String srcString = String.valueOf(srcLong);
		// 呼叫cropping函式,避免srcString長度大於len
		srcString = cropping(srcString, len);
		// 計算srcString之長度
		int srcLen = srcString.length();
		// 將回傳之字串補上不足之空白
		desString = srcString;
		for (int i = 0; i < (len - srcLen); i++) {
			desString = "0" + desString;
		}
		return desString;
	}

	/**
	 * 功能：將srcString截取至最大maxLen(中文需一次取2 bytes)
	 *
	 * @param srcString
	 * @param maxLen
	 * @return
	 * @throws Exception
	 */
	public synchronized static String cropping(String srcString, int maxLen)
			throws Exception {
		if (srcString == null) {
			throw new Exception("cropping():傳入參數為null!");
		}
		String desString = null;
		// 轉換String為byte資料
		byte[] desBytes = srcString.getBytes();
		// 比較desBytes之長度
		if (desBytes.length > maxLen) {
			// 呼叫cropping函式,將結果assign回去
			byte[] tmpBytes = cropping(desBytes, maxLen);
			desBytes = tmpBytes;
		}
		// 轉換srcBytes為Sting,組成回傳之desString
		desString = new String(desBytes);
		return desString;
	}

	/**
	 * 功能：將srcByte截取至最大maxLen(中文需一次取2 bytes)
	 *
	 * @param srcBytes
	 * @param maxLen
	 * @return
	 * @throws Exception
	 */
	public synchronized static byte[] cropping(byte[] srcBytes, int maxLen)
			throws Exception {
		if (srcBytes == null) {
			throw new Exception("cropping():傳入參數為null!");
		}
		byte[] desBytes = srcBytes;
		// 比較srcBytes 之長度
		if (srcBytes.length > maxLen) {
			// 處理中文字, 重新計算maxLen
			for (int i = 0; i < maxLen; i++) {
				// 若為中文則一次跳2 byte
				if (srcBytes[i] < 0) {
					i++;
				}
				if (i == maxLen) {
					maxLen = maxLen - 1;
				}
			}
			// 以maxLen為準,將srcBytes中超出maxLen之部份移除
			byte[] tmpBytes = new byte[maxLen];
			System.arraycopy(srcBytes, 0, tmpBytes, 0, maxLen);
			desBytes = tmpBytes;
		}
		return desBytes;
	}

	/**
	 * 功能：取得目前日期時間type:"D","T","DT" style:"X","AD","R","F"
	 *
	 * @param type
	 * @param style
	 * @return
	 */
	public synchronized static String getCurrentTime(String type, String style) {
		Calendar calendar = Calendar.getInstance();
		try {
			return getDateTime(calendar, type, style);
		} catch (Exception e) {
			LOG.debug("getCurrentTime Exception :" + e.toString());
			return "";
		}
	}

	/**
	 * 功能：取得某個calendar之時間日期 依據type及style取得字串 type: "D" ==> 取日期, "T" ==> 取時間,
	 * "DT" ==> 取日期時間 style: "X" ==> XML格式, "AD" ==> 西元年, "R" ==> 民國年, "F" ==>
	 * 類似xml格式,但沒有zonetime
	 *
	 * @param calendar
	 * @param type
	 * @param style
	 * @return
	 * @throws Exception
	 */
	public synchronized static String getDateTime(Calendar calendar,
			String type, String style) throws Exception {
		String myDateTime = "";
		if (type == null || type.equals("")) {
			type = TYPE_DATETIME;
		}
		if (style == null || style.equals("")) {
			style = STYLE_AD;
		}
		String year, month, day, hour, min, sec;
		if (style.equals(STYLE_ROC)) {
			//year = padding(calendar.get(Calendar.YEAR) - 1911, 2);
			//民國百年調整
			year = new DecimalFormat("###0").format(calendar.get(Calendar.YEAR) - 1911);
		} else {
			year = padding(calendar.get(Calendar.YEAR), 4);
		}
		month = padding(calendar.get(Calendar.MONTH) + 1, 2);
		day = padding(calendar.get(Calendar.DATE), 2);
		hour = padding(calendar.get(Calendar.HOUR_OF_DAY), 2);
		min = padding(calendar.get(Calendar.MINUTE), 2);
		sec = padding(calendar.get(Calendar.SECOND), 2);

		if (type.equals(TYPE_DATE) || type.equals(TYPE_DATETIME)) {
			myDateTime = year + month + day;
		}
		if (type.equals(TYPE_TIME) || type.equals(TYPE_DATETIME)) {
			myDateTime = myDateTime + hour + min + sec;
			// }
		}
		if (style.equals(STYLE_FORMAT)) {
			myDateTime = formateDateTime(myDateTime);
		} else if (style.equals(STYLE_XML)) {
			myDateTime = strTime2XMLTime(myDateTime);
		} else if (style.equals(STYLE_FORMAT_FOR_USER)) {
			myDateTime = formateDateTimeForUser(myDateTime);
		}

		return myDateTime;
	}

	/**
	 * 功能：將字串時間做格式化給USER看的格式 ex. 20011217 ==> 2001/12/17 ex. 151617 ==> 15:16:17
	 * ex. 20011217151617 ==> 2001/12/17 15:16:17
	 *
	 * @param myDateTime
	 * @return
	 */
	public synchronized static String formateDateTimeForUser(String myDateTime) {
		String rtnDateTime = "";
		if (myDateTime == null) {
			return "";
		}
		if (myDateTime.length() == 17) {
			myDateTime = myDateTime.substring(0, 14);
		}
		if (myDateTime.indexOf("/") != -1)
			return myDateTime;
		if (myDateTime.length() == 8 || myDateTime.length() == 14
				|| myDateTime.length() == 10) {
			rtnDateTime = myDateTime.substring(0, 4) + "/"
					+ myDateTime.substring(4, 6) + "/"
					+ myDateTime.substring(6, 8);
			if (myDateTime.length() == 14) {
				rtnDateTime = rtnDateTime + " ";
				myDateTime = myDateTime.substring(8);
			}
			// added by Glenn Tung for some date formate length is 10
			if (myDateTime.length() == 10) {
				rtnDateTime = rtnDateTime + " ";
				myDateTime = myDateTime.substring(8);
			}
		}
		if (myDateTime.length() == 6) {
			rtnDateTime = rtnDateTime + myDateTime.substring(0, 2) + ":"
					+ myDateTime.substring(2, 4) + ":"
					+ myDateTime.substring(4, 6);
		}
		if (myDateTime.length() == 9) {
			rtnDateTime = rtnDateTime + myDateTime.substring(0, 2) + ":"
					+ myDateTime.substring(2, 4) + ":"
					+ myDateTime.substring(4, 6);
		}
		if (myDateTime.length() == 2) {
			rtnDateTime = rtnDateTime + myDateTime.substring(0, 2);
		}

		if (myDateTime.length() == 4) {
			rtnDateTime = rtnDateTime + myDateTime.substring(0, 2) + ":"
					+ myDateTime.substring(2, 4);
		}

		return rtnDateTime;
	}

	/**
	 * 功能：將字串時間做格式化給USER看的格式 ex. 151617 ==> 15:16:17
	 *
	 * @param myDateTime
	 * @return
	 */
	public synchronized static String formateTimeForUser(String myDateTime) {
		String rtnDateTime = "";
		if (myDateTime == null)
			return "";

		if (myDateTime.length() >= 6) {
			rtnDateTime = rtnDateTime + myDateTime.substring(0, 2) + ":"
					+ myDateTime.substring(2, 4) + ":"
					+ myDateTime.substring(4, 6);
		}
		return rtnDateTime;
	}

	/**
	 * 功能：將字串時間做格式化給USER看的格式 ex. 1516 ==> 15:16
	 *
	 * @param myDateTime
	 * @return
	 */
	public synchronized static String formateTime(String myDateTime) {
		String rtnDateTime = "";
		if (myDateTime == null)
			return "";
		if (myDateTime.length() >= 4) {
			rtnDateTime = rtnDateTime + myDateTime.substring(0, 2) + ":"
					+ myDateTime.substring(2, 4);
		}
		return rtnDateTime;
	}

	/**
	 * 功能：將字串時間做格式化 ex. 20011217 ==> 2001-12-17 ex. 151617 ==> 15:16:17 ex.
	 * 20011217151617 ==> 2001-12-17 15:16:17
	 *
	 * @param myDateTime
	 * @return
	 */
	public synchronized static String formateDateTime(String myDateTime) {
		String rtnDateTime = "";
		if (myDateTime == null)
			return "";
		if (myDateTime.length() == 8 || myDateTime.length() == 14) {
			rtnDateTime = myDateTime.substring(0, 4) + "-"
					+ myDateTime.substring(4, 6) + "-"
					+ myDateTime.substring(6, 8);
			if (myDateTime.length() == 14) {
				rtnDateTime = rtnDateTime + " ";
				myDateTime = myDateTime.substring(8);
			}
		}
		if (myDateTime.length() == 6) {
			rtnDateTime = rtnDateTime + myDateTime.substring(0, 2) + ":"
					+ myDateTime.substring(2, 4) + ":"
					+ myDateTime.substring(4, 6);
		}
		return rtnDateTime;
	}

	/**
	 * 功能：還原格式化之DateTime(適用西元str) ex. 2001/12/17 ==> 20011217 ex. 2001-12-17 ==>
	 * 20011217 ex. 16:15:17 ==> 161517 ex. 2001-11-12T15:16:17+08:00 ==>
	 * 20011112151617
	 *
	 * @param myDateTime
	 * @return
	 */
	public synchronized static String revertDateTime(String myDateTime) {
		String rtnDateTime = "";
		if (myDateTime == null)
			return "";
		if (myDateTime.length() == 10 || myDateTime.length() == 19) {
			rtnDateTime = myDateTime.substring(0, 4)
					+ myDateTime.substring(5, 7) + myDateTime.substring(8, 10);
			if (myDateTime.length() == 19) {
				myDateTime = myDateTime.substring(11);
			}
		}
		if (myDateTime.length() == 8) {
			rtnDateTime = rtnDateTime + myDateTime.substring(0, 2)
					+ myDateTime.substring(3, 5) + myDateTime.substring(6, 8);
		}
		return rtnDateTime;
	}

	/**
	 * 功能：還原格式化之Time(適用西元str) ex. 16:15 ==> 1615 ex. 16:15:17 ==> 161517
	 *
	 * @param myDateTime
	 * @return
	 */
	public synchronized static String revertTime(String myDateTime) {
		String rtnDateTime = "";
		if (myDateTime == null)
			return "";
		if (myDateTime.length() == 5) {
			rtnDateTime = myDateTime.substring(0, 2)
					+ myDateTime.substring(3, 5);
		}
		if (myDateTime.length() == 8) {
			rtnDateTime = myDateTime.substring(0, 2)
					+ myDateTime.substring(3, 5) + myDateTime.substring(6, 8);
		}
		return rtnDateTime;
	}

	/**
	 * 功能：轉換字串格式日期時間為XML格式日期時間(timezone=+08:00) ex. 20011112 ==> 2001-11-12 ex.
	 * 20011112151617 ==> 2001-11-12T15:16:17+08:00
	 *
	 * @param xmlTime
	 * @return
	 * @throws Exception
	 */
	public synchronized static String strTime2XMLTime(String xmlTime)
			throws Exception {
		String rtnDateTime = "";
		String timezone = "+08:00";
		if (xmlTime == null
				|| (xmlTime.length() != 14 && xmlTime.length() != 6 && xmlTime
						.length() != 8)) {
			throw new Exception("strTime2XMLTime():傳入日期時間不合法,無法轉換!");
		}
		if (xmlTime.length() == 6 || xmlTime.length() == 8) {
			rtnDateTime = formateDateTime(xmlTime);
		} else if (xmlTime.length() == 14) {
			rtnDateTime = formateDateTime(xmlTime.substring(0, 8)) + "T"
					+ formateDateTime(xmlTime.substring(8, 14)) + timezone;
		}
		return rtnDateTime;
	}

	/**
	 * 功能：轉換XML格式日期時間為字串格式日期時間 ex. 2001-12-21 or 16:35:45 or 25碼xml格式日期
	 *
	 * @param strTime
	 * @return
	 * @throws Exception
	 */
	public synchronized static String xmlTime2StrTime(String strTime)
			throws Exception {
		String rtnDateTime = "";
		if (strTime == null
				|| (strTime.length() != 25 && strTime.length() != 10 && strTime
						.length() != 8)) {
			throw new Exception("xmlTime2StrTime():傳入日期時間不合法,無法轉換!");
		}
		if (strTime.length() == 10 || strTime.length() == 8) {
			rtnDateTime = revertDateTime(strTime);
		} else if (strTime.length() == 25) {
			rtnDateTime = revertDateTime(strTime.substring(0, 19));
		}
		return rtnDateTime;
	}

	/**
	 * 功能：檢查是否為數字
	 *
	 * @param intString
	 * @return
	 */
	public synchronized static boolean checkIntIsValid(String intString) {
		boolean isValid = false;
		try {
			Long.parseLong(intString);
			isValid = true;
		} catch (Exception e) {
			// e.printStackTrace();
			LOG.error("checkIntIsValid() error is :" + e.toString());
		}
		return isValid;
	}

	/**
	 * 功能：檢查傳入時間是否合法(可傳入xmlTime及strTime) ex. 2001-11-12 or 15:16:17 or
	 * 2001-12-17T15:16:17+08:00 ex. 20011112 or 151617 or 20011112151617 ex.
	 * 2001-11-12 15:16:17
	 *
	 * @param dateString
	 * @return
	 */
	public synchronized static boolean checkDateIsValid(String dateString) {
		boolean isValid = false;
		try {
			if (dateString.length() == 25
					|| dateString.length() == 10
					|| (dateString.length() == 8 && dateString.indexOf(":") == 2)) {
				dateString = xmlTime2StrTime(dateString);
			} else if (dateString.length() == 19) {
				dateString = revertDateTime(dateString);
			}
			string2Calendar(dateString);
			isValid = true;
		} catch (Exception e) {
			LOG.debug(" checkDateIsValid Exception :" + e.toString());
		}
		return isValid;
	}

	/**
	 * 功能：取得某日期幾天前或幾天後之日期(須為標準字串格式) ex. 20011112 ==> 20011115(加3天) ex. 20011112
	 * ==> 20011109(減3天)
	 *
	 * @param orgDate
	 * @param dayCnt
	 * @return
	 * @throws Exception
	 */
	public synchronized static String countDate(String orgDate, int dayCnt)
			throws Exception {
		String myDateLine = null;
		String myType = null;
		Calendar calendar = string2Calendar(orgDate);
		calendar.add(Calendar.DATE, dayCnt);
		if (orgDate.length() == 8) {
			myType = TYPE_DATE;
		} else if (orgDate.length() == 14) {
			myType = TYPE_DATETIME;
		}
		myDateLine = getDateTime(calendar, myType, STYLE_AD);
		return myDateLine;
	}

	/**
	 * 取得某日期幾月前或幾月後之日期(須為標準字串格式) ex. 20040112 ==> 20040412(加3月) ex. 20040112 ==>
	 * 20040412(減3月)
	 *
	 * @param orgDate
	 * @param monthCnt
	 * @return
	 */
	public synchronized static String countMonth(String orgDate, int monthCnt) {
		String myDateLine = null;
		String myType = null;
		try {

			Calendar calendar = string2Calendar(orgDate);
			calendar.add(Calendar.MONTH, monthCnt);
			if (orgDate.length() == 8) {
				myType = TYPE_DATE;
			} else if (orgDate.length() == 14) {
				myType = TYPE_DATETIME;
			}
			myDateLine = getDateTime(calendar, myType, STYLE_AD);
		} catch (Exception e) {
			LOG.debug("countMonth error:" + e.toString());
		}
		return myDateLine;
	}

	/**
	 * 取得某日期幾年前或幾年後之日期(須為標準字串格式) ex. 20041112 ==> 20071112(加3年) ex. 20041112 ==>
	 * 20011112(減3年)
	 *
	 * @param orgDate
	 * @param yearCnt
	 * @return
	 * @throws Exception
	 */
	public synchronized static String countYear(String orgDate, int yearCnt)
			throws Exception {
		String myDateLine = null;
		String myType = null;
		Calendar calendar = string2Calendar(orgDate);
		calendar.add(Calendar.YEAR, yearCnt);
		if (orgDate.length() == 8) {
			myType = TYPE_DATE;
		} else if (orgDate.length() == 14) {
			myType = TYPE_DATETIME;
		}
		myDateLine = getDateTime(calendar, myType, STYLE_AD);
		return myDateLine;
	}

	/**
	 * 將傳入之字串日期格式轉為calendar,若不合法則throw Exception ex. 20011112 or 20011112151617
	 * or 151617
	 *
	 * @param dateString
	 * @return
	 * @throws Exception
	 */
	public synchronized static Calendar string2Calendar(String dateString)
			throws Exception {
		int year = 0, month = 0, date = 0, hour = 0, min = 0, sec = 0, myLen = 0;
		if (dateString == null) {
			throw new Exception("string2Calendar():傳入日期時間為null!");
		}
		myLen = dateString.length();
		if (myLen == 8 || myLen == 14) {
			year = Integer.parseInt(dateString.substring(0, 4));
			month = Integer.parseInt(dateString.substring(4, 6)) - 1;
			date = Integer.parseInt(dateString.substring(6, 8));
			if (myLen == 14) {
				dateString = dateString.substring(8);
			}
		}

		if (dateString.length() == 6) {
			hour = Integer.parseInt(dateString.substring(0, 2));
			min = Integer.parseInt(dateString.substring(2, 4));
			sec = Integer.parseInt(dateString.substring(4, 6));
		}

		Calendar calendarObj = Calendar.getInstance();
		try {
			if (myLen == 8) {
				calendarObj.set(year, month, date);
				if (year != calendarObj.get(Calendar.YEAR)
						|| month != (calendarObj.get(Calendar.MONTH))
						|| date != calendarObj.get(Calendar.DATE)) {
					throw new Exception("日期錯誤!");
				}
			} else if (myLen == 6) {
				if (hour < 0 || hour >= 24 || min < 0 || min >= 60 || sec < 0
						|| sec >= 60) {
					throw new Exception("時間錯誤!");
				}
			} else if (myLen == 14) {
				calendarObj.set(year, month, date, hour, min, sec);
				if (year != calendarObj.get(Calendar.YEAR)
						|| month != (calendarObj.get(Calendar.MONTH))
						|| date != calendarObj.get(Calendar.DATE)
						|| hour != calendarObj.get(Calendar.HOUR_OF_DAY)
						|| min != calendarObj.get(Calendar.MINUTE)
						|| sec != calendarObj.get(Calendar.SECOND)) {
					throw new Exception("日期或時間錯誤!");
				}
			} else {
				throw new Exception("傳入長度錯誤!");
			}
		} catch (Exception e) {
			throw new Exception("string2Calendar():傳入日期時間不合法,無法轉換Calendar!"
					+ e.getMessage());
		}
		return calendarObj;
	}

	/**
	 * 功能：轉換Null String為空字串,並將多餘之空白去掉
	 *
	 * @param inStr
	 * @return
	 */
	public synchronized static String cnvNullStr(String inStr) {
		if (inStr == null) {
			inStr = "";
		} else {
			inStr = inStr.trim();
		}
		return inStr;
	}

	/**
	 * 功能：獲取諾干分鐘后的系統時間
	 *
	 * @param minute
	 *            附加分鐘數
	 * @return 格式： 2010-11-23T05:54:08
	 */
	public static synchronized String exptTime(int minute) {
		Calendar curTime = Calendar.getInstance();
		curTime.add(Calendar.MINUTE, minute);
		String sExptTime = null;

		try {
			sExptTime = getDateTime(curTime, DateUtil.TYPE_DATETIME,
					DateUtil.STYLE_XML).substring(0, 19);
		} catch (Exception e) {
		}
		return sExptTime;
	}

	/**
	 * 功能：轉換ASCII ==> EBCDIC
	 *
	 * @param barrAscii
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static synchronized byte[] cnvASCII2EBCDIC(byte[] barrAscii)
			throws UnsupportedEncodingException {
		String asciiStr = new String(barrAscii);
		return asciiStr.getBytes("IBM-937");
	}

	/**
	 * 功能：轉換ASCII ==> EBCDIC
	 *
	 * @param asciiStr
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static synchronized byte[] cnvASCII2EBCDIC(String asciiStr)
			throws UnsupportedEncodingException {
		return asciiStr.getBytes("IBM-937");
	}

	/**
	 * 功能：轉換EBCDIC ==> ASCII
	 *
	 * @param barrEBCDIC
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static synchronized byte[] cnvEBCDIC2ASCII(byte[] barrEBCDIC)
			throws UnsupportedEncodingException {
		String asciiStr = new String(barrEBCDIC, "IBM-937");
		return asciiStr.getBytes();
	}

	/**
	 * 功能：轉換byte ==> HEX值
	 *
	 * @param bdata
	 * @return
	 */
	public static synchronized String bytetoString(byte[] bdata) {
		String tmp = "";
		String str = "";
		if (bdata == null) {
			return "00";
		}
		for (int i = 0; i < bdata.length; i++) {
			int x = bdata[i];
			Byte bb = null;
			if (x < 0) {
				x = x + 256;
				tmp = Integer.toHexString(x);
			} else {
				bb = new Byte(bdata[i]);
				tmp = Integer.toHexString(bb.intValue());
			}
			if (tmp.length() < 2) {
				tmp = "0" + tmp;
			} else if (tmp.length() > 2) {
				tmp = tmp.substring(tmp.length() - 2);
			}
			str = str + tmp;
		}
		return str.toUpperCase();
	}

	/**
	 * 功能：轉換byte ==> Binary值
	 *
	 * @param bdata
	 * @return
	 */
	public static synchronized String bytetoBinString(byte[] bdata) {
		String tmp = "";
		String str = "";
		if (bdata == null) {
			return "00";
		}
		for (int i = 0; i < bdata.length; i++) {
			int x = bdata[i];
			Byte bb = null;
			if (x < 0) {
				x = x + 256;
				tmp = Integer.toBinaryString(x);
			} else {
				bb = new Byte(bdata[i]);
				tmp = Integer.toBinaryString(bb.intValue());
			}
			if (tmp.length() < 8) {
				int len = tmp.length();
				for (int j = 0; j < (8 - len); j++) {
					tmp = "0" + tmp;
				}
			} else if (tmp.length() > 8) {
				tmp = tmp.substring(tmp.length() - 8);
			}
			str = str + tmp;
		}
		return str;
	}

	/**
	 * 功能：Pack資料
	 *
	 * @param in
	 * @return
	 */
	public static synchronized byte[] pack(String in) {
		byte[] bdata = in.getBytes();
		byte[] hexbyte = new byte[bdata.length / 2];
		int index = 0;
		int digit2 = 0;
		int digit = 0;
		int tmp = 0;

		for (int i = 0; i < bdata.length; i = i + 2) {
			if (bdata[i] > 64) {
				digit2 = (bdata[i] - 55) << 4; // 因為要讓該4bit為A-F
			} else {
				digit2 = (bdata[i] - 48) << 4;
			}
			if (bdata[i + 1] > 64) {
				digit = (bdata[i + 1] - 55);
			} else {
				digit = (bdata[i + 1] - 48);
			}
			tmp = digit + digit2;
			if (tmp > 127) {
				tmp = tmp - 256;

			}
			hexbyte[index] = (byte) tmp;
			index++;
		}
		return hexbyte;
	}

	/**
	 * 功能：check isBusinessDay
	 *
	 * @return
	 */
	public static boolean isBusinessDay() {
		boolean isBusinessDay = true;
		int iToday = 0;
		Calendar calendar = Calendar.getInstance();
		iToday = calendar.get(Calendar.DAY_OF_WEEK);
		if (iToday == Calendar.SUNDAY || iToday == Calendar.SATURDAY) {
			isBusinessDay = false;
		}
		return isBusinessDay;
	}

	/**
	 * 功能：取得今日
	 *
	 * @param none
	 * @return String 今日日期 (格式: yyyyMMdd)
	 * @exception none
	 */
	static public String getToday() {
		Calendar calendar = Calendar.getInstance();
		int tYear = calendar.get(Calendar.YEAR);
		int tMonth = calendar.get(Calendar.MONTH) + 1;
		int tDate = calendar.get(Calendar.DATE);
		return String.valueOf(tYear * 10000 + tMonth * 100 + tDate);
	}

	/**
	 * 功能：取得当前月
	 *
	 * @return
	 */
	public static String getThisMonth() {
		Calendar calendar = Calendar.getInstance();
		int tYear = calendar.get(Calendar.YEAR);
		int tMonth = calendar.get(Calendar.MONTH) + 1;
		return String.valueOf(tYear * 10000 + tMonth * 100);
	}

	/**
	 * 功能：取得當月的最大天數
	 *
	 * @param today
	 *            日期
	 * @return int 該日期所在月份的最大天數
	 * @exception none
	 */
	static public int getMaxMonthDay(String today) {
		if (today == null || today.length() < 6) {
			LOG.debug("getMaxMonthDay error for input today is :" + today);
			return -1;
		}
		int Now_yyyy = Integer.parseInt(today.substring(0, 4));
		int Now_mm = Integer.parseInt(today.substring(4, 6)) - 1;

		Calendar dd = Calendar.getInstance();
		dd.clear();
		dd.set(Calendar.YEAR, Now_yyyy);
		dd.set(Calendar.MONTH, Now_mm);

		return dd.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 功能：是否當月的最後一天
	 *
	 * @param currDate
	 *            檢查日期
	 * @return boolean true: 當月的最後一天, false: 非當月的最後一天
	 */
	static public boolean isMaxMonthDay(String currDate) {
		int day = 0;

		// Get day
		day = Integer.parseInt(currDate.substring(6, 8));

		// Processing
		if (day == getMaxMonthDay(currDate)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 功能：取得當日星期
	 *
	 * @param today
	 *            日期
	 * @return int 星期日 :1 星期一 :2 星期二 :3 星期三 :4 星期四 :5 星期五 :6 星期六 :7
	 *
	 * @exception none
	 */
	static public int getDayOfWeek(String today) {
		int Now_yyyy = Integer.parseInt(today.substring(0, 4));
		int Now_mm = Integer.parseInt(today.substring(4, 6)) - 1;
		int date = Integer.parseInt(today.substring(6, 8));

		Calendar dd = Calendar.getInstance();
		dd.clear();
		dd.set(Calendar.YEAR, Now_yyyy);
		dd.set(Calendar.MONTH, Now_mm);
		dd.set(Calendar.DATE, date);

		return dd.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 功能：取得當日星期
	 *
	 * @param String
	 *            today 日期
	 * @return String 星期日 :日 星期一 :一 星期二 :二 星期三 :三 星期四 :四 星期五 :五 星期六 :六
	 *
	 * @exception none
	 */
	static public String getDayStrOfWeek(String today) {
		int week = getDayOfWeek(today);
		if (week == 1)
			return "日";
		if (week == 2)
			return "一";
		if (week == 3)
			return "二";
		if (week == 4)
			return "三";
		if (week == 5)
			return "四";
		if (week == 6)
			return "五";
		if (week == 7)
			return "六";
		return week + "";
	}

	/**
	 * 功能：轉民國年
	 *
	 * @param today
	 *            日期 YYYYMMDD
	 * @return int \uFFFD
	 * @exception none
	 */
	static public String getDateOfROC(String date) {
		if (date == null || "".equals(date.trim()))
			return "";
		if (date.length() != 8)
			return date;
		int year = Integer.parseInt(date.substring(0, 4));
		year = year - 1911;
		String tmp = ("0" + Integer.toString(year));
		if (tmp.length() > 3) {
			date = tmp.substring(tmp.length() - 3, tmp.length())
					+ date.substring(4);
		} else {
			date = tmp + date.substring(4);
		}
		return date;
	}

	/**
	 * 功能：民國年轉西元年
	 *
	 * @param today
	 *            日期 YYYYMMDD
	 * @return int \uFFFD
	 * @exception none
	 */
	static public String ROC2Date(String date) {
		if (date == null || date.length() < 7)
			return date;
		// 民國年轉西元年
		int year = Integer.parseInt(date.substring(0, 3));
		year = year + 1911;
		String tmp = Integer.toString(year);
		String rt = tmp + date.substring(3);
		return rt;
	}

	/**
	 * 功能：民國年月轉西元年月 如： 09801=>200901 098/01=>2009/01
	 *
	 * @param yearMonth
	 * @return
	 */
	static public String ROC2DateForYM(String yearMonth) {
		if (yearMonth == null || yearMonth.length() < 5)
			return yearMonth;
		// 民國年月轉西元年月
		int year = Integer.parseInt(yearMonth.substring(0, 3));
		year = year + 1911;
		String tmp = Integer.toString(year);
		String rt = tmp + yearMonth.substring(3);
		return rt;
	}

	/**
	 * 功能：西元年轉Unix時間格式
	 *
	 * @param today
	 *            日期 YYYYMMDD
	 * @return int \uFFFD
	 * @exception none
	 */
	/*
	 * static public long dateToUnix(String s) throws Exception { if (s == null ||
	 * "".equals(s) || s.length() != 14) { return 0; } Calendar cl =
	 * Calendar.getInstance(); int year = Integer.parseInt(s.substring(0, 4));
	 * int month = Integer.parseInt(s.substring(4, 6)); int date =
	 * Integer.parseInt(s.substring(6, 8)); int hh =
	 * Integer.parseInt(s.substring(8, 10)); int mm =
	 * Integer.parseInt(s.substring(10, 12)); int ss =
	 * Integer.parseInt(s.substring(12, 14)); cl.set(year, month - 1, date, hh,
	 * mm, ss); long end = cl.getTimeInMillis(); cl.set(1970, 0, 1, 0, 0, 0);
	 * long start = cl.getTimeInMillis(); long seconds = (end - start) / 1000;
	 * return seconds; }
	 */

	/**
	 * 功能：Unix時間格式轉西元年時分秒
	 *
	 * @param today
	 *            日期 YYYYMMDD
	 * @return int \uFFFD
	 * @exception none
	 */
	static public String getUnixDate(long second) throws Exception {
		Calendar c = DateUtil.string2Calendar("19700101000000");
		c.add(Calendar.SECOND, (int) second);
		return new SimpleDateFormat("yyyyMMddHHmmss").format(c.getTime());
	}

	/**
	 * 功能：取得某日期幾秒前或幾秒後之日期時間(須為標準字串格式) ex. 20011112121231 ==> 20011115121232(加1秒)
	 * ex. 20011112121231 ==> 20011112121228(減3秒)
	 *
	 * @param orgDttm
	 * @param secondCnt
	 * @return
	 * @throws Exception
	 */
	public synchronized static String countSecond(String orgDttm, int secondCnt)
			throws Exception {
		String myDateLine = null;
		String myType = null;
		Calendar calendar = string2Calendar(orgDttm);
		calendar.add(Calendar.SECOND, secondCnt);
		myType = TYPE_DATETIME;
		myDateLine = getDateTime(calendar, myType, STYLE_AD);
		return myDateLine;
	}

	/**
	 * 功能：返回指定日期的月份;date Formate yyyymmdd
	 *
	 * @author JmiuHan,2007/03/21
	 * @param date
	 * @return -1 日期不合法
	 */
	public static int getMonthOfDay(String date) {
		if (StringUtil.isBlank(date) || date.length() != 8) {
			return -1;
		}
		String month = date.substring(4, 6);
		return Integer.parseInt(month);
	}

	/**
	 * 功能：返回指定年,月,日 日期
	 *
	 * @param String
	 *            year
	 * @param int
	 *            month
	 * @param int
	 *            day
	 * @return String
	 */
	public static String getDate(String year, int month, int day) {
		return year + ((100 + month) + "").substring(1)
				+ ((100 + day) + "").substring(1);
	}

	/***************************************************************************
	 * Parse GMT time to Date
	 *
	 * @param long
	 * @return String - local current DateTime Formate:yyyy/mm/dd
	 * @exception none
	 **************************************************************************/
	public static String parseLong2Date(long date) {

		String sdate, year, month, day;// , hours, mins, secs;

		// get Date instance for long Integer
		date = date * 1000L; // -- set Date with milliseconds
		Date dt = new Date(date);

		// transfer Date to String
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		year = String.valueOf(cal.get(Calendar.YEAR));
		month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

		if (day.length() == 1)
			day = "0" + day;
		if (month.length() == 1)
			month = "0" + month;
		sdate = year + "/" + month + "/" + day;
		// return Localtime
		return sdate;
	}

	/**
	 * 功能：取得當前系統時間
	 *
	 * @return
	 */
	public static Date getCurrentDateTime() {
		return new Date();
	}

	/**
	 * 功能：取得當前系統時間 yyyymmddHHMMSS
	 *
	 * @return
	 */
	public static String getSysDateTime() {
		return getCurrentTime("DT", "AD");
	}

	/**
	 * 功能：取得昨天
	 *
	 * @return
	 */
	public static String getYestoday() {
		try {
			return DateUtil.countDate(getToday(), -1);
		} catch (Exception e) {
			LOG.error("getYestoday !", e);
		}
		return "";
	}

	/**
	 * 功能：取得明天
	 *
	 * @return
	 */
	public static String getTomorrow() {
		try {
			return DateUtil.countDate(getToday(), 1);
		} catch (Exception e) {
			LOG.error("getYestoday !", e);
		}
		return "";
	}

	/**
	 * 功能：將時間做格式化給USER看的格式
	 *
	 * @param myDateTime
	 * @return
	 */
	public synchronized static String formateDBDateTimeForUser(Date myDateTime) {
		if (myDateTime == null)
			return "";
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(myDateTime);
		try {
			return getDateTime(calendar, "DT", "FU");
		} catch (Exception e) {
			LOG.error("formate Date Error !", e);
		}
		return "";
	}

	/**
	 * 功能：把格式为yyyy/MM/DD hh:mm:ss的字符串转换为Date类型
	 *
	 * @param str
	 * @return
	 */
	public static synchronized Date formatStr(String str) {
		try {
			Date date = new SimpleDateFormat("yyyy/MM/DD hh:mm:ss").parse(str);
			return date;
		} catch (Exception e) {
			LOG.error("formatStr error" + e.toString());
		}
		return null;
	}

	/**
	 * 功能： 時間值比較
	 * @param datetime1
	 * @param datetime2
	 * @return
	 */
	public static int compareDateTime(String datetime1, String datetime2) {
		if (datetime1 == null && datetime2 == null) {
			return 0;
		} else if (datetime1 == null) {
			return -1;
		} else if (datetime2 == null) {
			return 2;
		} else {
			return datetime1.compareTo(datetime2);
		}
	}

	/**
	 * 功能：取得二日期間隔天數
	 *
	 * @param beginDate yyyyMMdd
	 * @param endDate yyyyMMdd
	 * @return
	 * @throws Exception
	 */
	public static int getDay(String beginDate, String endDate) throws Exception {
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMdd");
		Date d1 = sim.parse(beginDate);
		Date d2 = sim.parse(endDate);
		return (int) ((d2.getTime() - d1.getTime()) / (3600L * 1000 * 24));
	}
	
	/**
	 * 格式化民国年  0980101 ==> 098/01/01  0980101000000 ==> 098/01/01 00:00:00
	 * @param str
	 * @return 
	 */
	public static String formatMGDate1(String str) {
		if(str==null || (str.length() != 13 && str.length() != 7 ) ){
			LOG.info("傳入參數長度不符合民國年長度！"+str);
			return "";
		}
		String returnStr = formateDateTimeForUser("0"+str);
		return returnStr.substring(1);
	}
	
	/**
	 * 
	 * 將用字元隔開的日期拆解成民國日期，支援民國/西元之輸入
	 * 99/10/3    >>  0991003
	 * 2011/2/10  >>  1000210
	 * 
	 * @param date 日期
	 * @param regexNumberGap 數字的間隔字元(使用regex)
	 * @param inputIsMGYear 指示輸入的是否為民國年
	 * @return 民國年yyyMMdd
	 */
	public static String getMGDateFormFormatedDate(String date,
			String regexNumberGap, boolean inputIsMGYear){
		String[] se = date.split(regexNumberGap);
		
		if(se==null||se.length!=3)
			return "";
		
		int year = Integer.parseInt(se[0]);
		int month = Integer.parseInt(se[1]);
		int day = Integer.parseInt(se[2]);
		
		if(!inputIsMGYear)
			year -= 1911;
		
		StringBuffer sb = new StringBuffer();
		if(year<100){
			sb.append("0");
			if(year<10)
				sb.append("0");
		}
		sb.append(""+year);
		if(month<10)
			sb.append("0");
		sb.append(""+month);
		if(day<10)
			sb.append("0");
		sb.append(""+day);
		
		return sb.toString();
	}
	
	/**
	 * 取当前 民国年日期   0980101
	 * @return
	 */
	public static String getMGDate(){
		return getDateOfROC(getToday());
	}
	
	/**
	 * 依據指定日期格式字串, 產生相同日期的 Date 物件
	 * @param dateStr, e.g: yyyyMMdd or yyyy/MM/dd or yyyy-MM-dd
	 * @param timeStr, e.g: hhmmss or hh:mm:ss
	 * @return
	 */
	public static synchronized Date formatStrToDate(String dateStr, String timeStr) {
		// 檢核 dateStr 日期格式是否正確
        if(StringUtil.isBlank(dateStr) || (dateStr.length() != 8 && dateStr.length() != 10)){
            return null;
        }
        
        Date date = null;
        
        try {
        	
        	// 去除日期格式中的符號 e.g: 「 / 」 or 「 - 」
            if(dateStr.length() == 10){
                 dateStr = DateUtil.revertDateTime(dateStr);
            }
            
            // timeStr 若為 null or 空白, 預設值為 000000
            if(StringUtil.isBlank(timeStr)) {
            	timeStr = "000000";
            	
            } else {
            	timeStr = timeStr.replaceAll(":", "");
            	timeStr = timeStr + "000000";
            	timeStr = timeStr.substring(0, 6);
            }
            
            date = new SimpleDateFormat("yyyyMMddhhmmss").parse(dateStr + timeStr);
            
            return date;
            
        } catch (Exception e) {
            LOG.error("formatStrToDate error: " + e.toString(), e);
        }
        
        return null;
    }
	
	/**
	 * 傳入datetime格式字串 eg:2016-03-10 14:00:00.0000000 轉換為 2016/03/10 14:00:00
	 * @param dateStr: datetime格式字串
	 * @return String YYYY/MM/DD hh:mm:ss
	 */
	public static synchronized String formatDatetimeToDateStr(String dateStr) {
		String str = "";
		try{
			Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateStr);
			Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        str = formateDateTimeForUser(getDateTime(calendar, "DT", "AD"));
		}catch(Exception e){
			LOG.error("formatDateToDateStr error: " + e.toString(), e);
		}
		
        return str;
	}
	
    /**
     * 返回指定日期的月的第一天
     *
     * @param dateStr(YYYYMMDD)
     * @return String(YYYYMMDD)
     */
    public static synchronized String getFirstDayOfMonth(String dateStr) {
    	String str = "";
    	try{
    		Date date = formatStrToDate(dateStr, "");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(calendar.get(Calendar.YEAR),
                         calendar.get(Calendar.MONTH), 1);
            
            str = getDateTime(calendar, "D", "AD");
            //return calendar.getTime();
    	}catch(Exception e){
			LOG.error("formatDateToDateStr error: " + e.toString(), e);
		}
    	
    	return str;
    }
    
    /**
     * 返回指定日期的月的最後一天
     *
     * @param dateStr(YYYYMMDD)
     * @return String(YYYYMMDD)
     */
    public static synchronized String getLastDayOfMonth(String dateStr) {
    	String str = "";
    	try{
    		Date date = formatStrToDate(dateStr, "");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(calendar.get(Calendar.YEAR),
                         calendar.get(Calendar.MONTH), 1);
            calendar.roll(Calendar.DATE, -1);
            
            str = getDateTime(calendar, "D", "AD");
            //return calendar.getTime();
    	}catch(Exception e){
			LOG.error("formatDateToDateStr error: " + e.toString(), e);
		}
    	
    	return str;
    }
    
    /**
     *  
     * @param date
     * @return String yyyyMMddHHmmss
     */
    public static String formatDate2Str(Date date) {
	    	DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	    	return df.format(date);
	}
    
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
