/*
 * @(#)StringUtil.java
 * Copyright (c) 2005 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 * v1.00, 2005-11-17, Hang Zhou SDC
 *  1) First release
 *  v1.01,2005/12/20, Fong Yu
 *  1)add method:formatEDouble()
 *  v1.02,2006/02/06, Fong Yu
 *  1)add method:formatEDoubleWithDot();
 *  v1.03,2006/08/15, Zene Xu
 *  1) Add method:cropping().
 *  v1.04,2006/08/27 ,Royal Shen
 *  1) add method,formatAmtForPageView()
 *  v1.04,2008/09/19, Royal Shen
 *  1) modify method,formatAmt4TelegramView() ,RATE 去掉%
 *   
 */ 
package com.hitrust.bank.telegram.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class StringUtil {
	static Logger LOG = Logger.getLogger(StringUtil.class);
	static Map CARRY66_MAP = new HashMap();
	
	static{
		CARRY66_MAP.put("0", "0");
		CARRY66_MAP.put("1", "1");
		CARRY66_MAP.put("2", "2");
		CARRY66_MAP.put("3", "3");
		CARRY66_MAP.put("4", "4");
		CARRY66_MAP.put("5", "5");
		CARRY66_MAP.put("6", "6");
		CARRY66_MAP.put("7", "7");
		CARRY66_MAP.put("8", "8");
		CARRY66_MAP.put("9", "9");
		
		CARRY66_MAP.put("10", "A");
		CARRY66_MAP.put("11", "B");
		CARRY66_MAP.put("12", "C");
		CARRY66_MAP.put("13", "D");
		CARRY66_MAP.put("14", "E");
		CARRY66_MAP.put("15", "F");
		CARRY66_MAP.put("16", "G");
		CARRY66_MAP.put("17", "H");
		CARRY66_MAP.put("18", "I");
		CARRY66_MAP.put("19", "J");
		
		CARRY66_MAP.put("20", "K");
		CARRY66_MAP.put("21", "L");
		CARRY66_MAP.put("22", "M");
		CARRY66_MAP.put("23", "N");
		CARRY66_MAP.put("24", "O");
		CARRY66_MAP.put("25", "P");
		CARRY66_MAP.put("26", "Q");
		CARRY66_MAP.put("27", "R");
		CARRY66_MAP.put("28", "S");
		CARRY66_MAP.put("29", "T");
		
		CARRY66_MAP.put("30", "U");
		CARRY66_MAP.put("31", "V");
		CARRY66_MAP.put("32", "W");
		CARRY66_MAP.put("33", "X");
		CARRY66_MAP.put("34", "Y");
		CARRY66_MAP.put("35", "Z");
		CARRY66_MAP.put("36", ",");
		CARRY66_MAP.put("37", ".");
		CARRY66_MAP.put("38", ";");
		CARRY66_MAP.put("39", "'");
		
		CARRY66_MAP.put("40", "a");
		CARRY66_MAP.put("41", "b");
		CARRY66_MAP.put("42", "c");
		CARRY66_MAP.put("43", "d");
		CARRY66_MAP.put("44", "e");
		CARRY66_MAP.put("45", "f");
		CARRY66_MAP.put("46", "g");
		CARRY66_MAP.put("47", "h");
		CARRY66_MAP.put("48", "i");
		CARRY66_MAP.put("49", "j");
		
		CARRY66_MAP.put("50", "k");
		CARRY66_MAP.put("51", "l");
		CARRY66_MAP.put("52", "m");
		CARRY66_MAP.put("53", "n");
		CARRY66_MAP.put("54", "o");
		CARRY66_MAP.put("55", "p");
		CARRY66_MAP.put("56", "q");
		CARRY66_MAP.put("57", "r");
		CARRY66_MAP.put("58", "s");
		CARRY66_MAP.put("59", "t");
		
		CARRY66_MAP.put("60", "u");
		CARRY66_MAP.put("61", "v");
		CARRY66_MAP.put("62", "w");
		CARRY66_MAP.put("63", "x");
		CARRY66_MAP.put("64", "y");
		CARRY66_MAP.put("65", "z");
		
			
	}
	/**
	 * Double類型的參數格式化成14位
	 * ==> *****1,234,567[展示給pdf使用]
	 * @param amt
	 * @return
	 */
	public static String formatAmtForPdf(Double amt){
	   if (amt == null||amt==0){
			return "0";
	   }
		int temp=amt.intValue();
		String rtn=formatStrSplite(String.valueOf(temp));
		String str=paddingMark(rtn, 14, "L");
		return str;
	}
	/**
	 * 將srcString(左靠右補或右靠左補）補*至長度len為止
	 * 
	 * @param srcString
	 * @param len
	 * @param flag
	 *            "L" - 左補空格 其他 - 右補
	 * @return
	 */
	public synchronized static String paddingMark(String srcString, int len,
			String flag) {
		String desString = null;
		// 計算srcString之長度
		int srcLen = 0;
		if (srcString != null) {
			srcLen = srcString.getBytes().length;
		}
		// 將回傳之字串補上不足之空白
		desString = srcString;
		if (srcString == null) {
			desString = "";
		}
		for (int i = 0; i < (len - srcLen); i++) {
			if ("L".equals(flag)) {
				desString = "*" + desString;
			} else {
				desString = desString + "*";
			}
		}
		return desString;
	}
	
	/**
	 * 數字格式化函數
	 * @param  number: 格式化前的數字;
	 * @param  decimalDigits: 小數位數;
	 * @return: 三位一組以逗號分割的字符串;
	 */
	public static String format(double number, int decimalDigits) {
		if (number == 0d) number = 0d;
		
		boolean flag=false;
		if(decimalDigits < 0) {
			LOG.debug("小數位數不能小於0.");
			return "";
		}
		
		String pattern = "###,###,###,###,###,###";
		if(decimalDigits > 0) {
			flag=true;
			pattern += ".";
			for(int i=0;i<decimalDigits;i++) {
				pattern += "0";
			}
		}
		
		DecimalFormat df = new DecimalFormat(pattern);
		if (number <= -1d){
			return df.format(number);
		}else if (number > -1d && number < 0d){
			return "-0"+df.format(number).substring(1);
		}else if (number >= 0d && number < 1d){
			if(flag==true){
				return "0"+df.format(number);
			}else{
				return df.format(number);
			}
		}else{
			return df.format(number);
		}
	}
	
	/**
	 * 去掉左邊的0
	 * 
	 * @param String
	 * @return: 
	 */
	public static String removeLeftZero(String s) {
		if (s == null)
			return "";
		s = s.trim();
		if (s.equals("&nbsp;"))
			return "";
		if (s.length() == 0)
			return "";
		
		char[] arr = s.toCharArray();
		StringBuffer sb = new StringBuffer(s);
		int index=0;
		for(int i=0;i<arr.length;i++){
			char ch = arr[i];
			if(ch == '0'){
				index++;
				continue;
			}
			break;
		}
		return sb.substring(index,s.length());
		
	}
	
	public static String formatBigNumber(BigDecimal bd, int decimalDigits) {
		if(bd.intValue()<=0){
			return "&nbsp;";
		}
		double number=0;
		number=bd.doubleValue();
		if (number == 0d) number = 0d;
		
		boolean flag=false;
		if(decimalDigits < 0) {
			LOG.debug("小數位數不能小於0.");
			return "";
		}
		
		String pattern = "###,###,###,###,###,###";
		if(decimalDigits > 0) {
			flag=true;
			pattern += ".";
			for(int i=0;i<decimalDigits;i++) {
				pattern += "0";
			}
		}
		
		DecimalFormat df = new DecimalFormat(pattern);
		if (number <= -1d){
			return df.format(number);
		}else if (number > -1d && number < 0d){
			return "-0"+df.format(number).substring(1);
		}else if (number >= 0d && number < 1d){
			if(flag==true){
				return "0"+df.format(number);
			}else{
				return df.format(number);
			}
		}else{
			return df.format(number);
		}
	}
	
	/**
	 * 數字格式化函數
	 * @param  number: 格式化前的字符串(是一個數字);
	 * @param  decimalDigits: 小數位數;
	 * @return: 三位一組以逗號分割的字符串,如果為null,或空串或只有空格的字符串,返回空串;
	 */
	public static String format(String s, int decimalDigits) {
		if(s == null) return "";
		s = s.trim();
		if(s.equals("&nbsp;")) return "";
		if(s.length() == 0) return "";
		double number = Double.parseDouble(s);
		return format(number,decimalDigits);
	}
	
	/**
	 * 日期格式化函數
	 * @param  date: 格式化前的字符串,長度必須為8,且是YYYYMMDD格式;
	 *         ??    如果為null,或空串或只有空格的字符串,返回空串;
	 *         ??    如果長度是不為8的字符串,返回空串;
	 * @return 格式為: YYYY/MM/DD 的字符串;
	 */
	public static String formatDate(String date) {
		if(date == null) return "";
		date = date.trim();
		if(date.equals("&nbsp;")) return "";
		if (date.length() == 0 || date.length() != 8) return "";
		date = date.substring(0,4) + "/" + date.substring(4,6) + "/" + date.substring(6,8);
		return date;
	}
	
	/**
	 * 日期格式化函數
	 * @param  date: 格式化前的字符串,長度必須為10,且是YYYY/MM/DD格式;
	 *         ??    如果為null,或空串或只有空格的字符串,返回空串;
	 *         ??    如果長度是不為8的字符串,返回空串;
	 * @return 格式為: YYYYMMDD 的字符串;
	 */
	public static String formatDateToStr(String date) {
		if(date == null) return "";
		date = date.trim();
		if(date.equals("&nbsp;")) return "";
		if (date.length() == 0 || date.length() != 10) return "";
		date = date.substring(0,4) + date.substring(5,7) + date.substring(8,10);
		return date;
	}

	public static double parseDouble(String str){
		try{
			return Double.parseDouble(str);
		}catch(Exception e){
			return 0d;
		}
	}
	
	 /**
     * @method name: formatEDouble()
     * @author added by  Fong Yu 2005-12-19    
     * @param number
     * @param decimalDigits
     * @return
     */
    public static String formatEDouble(double number, int decimalDigits) {
		if (number == 0d) number = 0d;
		
		boolean flag=false;
		if(decimalDigits < 0) {
			LOG.debug("小數位數不能小於0.");
			return "";
		}
		
		String pattern = "##################";
		if(decimalDigits > 0) {
			flag=true;
			pattern += ".";
			for(int i=0;i<decimalDigits;i++) {
				pattern += "0";
			}
		}
		
		DecimalFormat df = new DecimalFormat(pattern);
		if (number <= -1d){
			return df.format(number);
		}else if (number > -1d && number < 0d){
			return "-0"+df.format(number).substring(1);
		}else if (number >= 0d && number < 1d){
			if(flag==true){
				return "0"+df.format(number);
			}else{
				return df.format(number);
			}
		}else{
			return df.format(number);
		}
	}
    
	 /**
     * @method name: formatEDouble()
     * @author added by  Fong Yu 2005-12-19    
     * @param number
     * @param decimalDigits
     * @return
     */
    public static String formatEDouble(String snumber, int decimalDigits) {
		double number = 0d;
		if (snumber == null) 
			return "";
		boolean flag=false;
		if(decimalDigits < 0) {
			LOG.debug("小數位數不能小於0.");
			return "";
		}
		
		String pattern = "##################";
		if(decimalDigits > 0) {
			flag=true;
			pattern += ".";
			for(int i=0;i<decimalDigits;i++) {
				pattern += "0";
			}
		}
		
		DecimalFormat df = new DecimalFormat(pattern);
		if (number <= -1d){
			return df.format(number);
		}else if (number > -1d && number < 0d){
			return "-0"+df.format(number).substring(1);
		}else if (number >= 0d && number < 1d){
			if(flag==true){
				return "0"+df.format(number);
			}else{
				return df.format(number);
			}
		}else{
			return df.format(number);
		}
	}
	
    /**
     * @method name: formatEDoubleWithDot()
     * @author added by  Fong Yu 2006-2-7  
     * @param number
     * @param decimalDigits
     * @return
     */
    public static String formatEDoubleWithDot(double number, int decimalDigits) {
		if (number == 0d) number = 0d;
		
		boolean flag=false;
		if(decimalDigits < 0) {
			LOG.debug("小數位數不能小於0.");
			return "";
		}
		
		String pattern = "###,###,###,###,###,###";
		if(decimalDigits > 0) {
			flag=true;
			pattern += ".";
			for(int i=0;i<decimalDigits;i++) {
				pattern += "0";
			}
		}
		
		DecimalFormat df = new DecimalFormat(pattern);
		if (number <= -1d){
			return df.format(number);
		}else if (number > -1d && number < 0d){
			return "-0"+df.format(number).substring(1);
		}else if (number >= 0d && number < 1d){
			if(flag==true){
				return "0"+df.format(number);
			}else{
				return df.format(number);
			}
		}else{
			return df.format(number);
		}
	}
    
	/**
	 * null or "" =====>&nbsp;
	 * @param str
	 * @return
	 */
	public static String fomatStrForJsp(String str){		
		if( str==null || str.trim().equals("")) 
			return "&nbsp;";
		return str;
	}
	
	public static String fomatStrForJspNoNull(String str) {
		if (str == null || str.trim().equals(""))
			return "";
		return str;
	}
	
	public static String formatStrForJspTrim(String str) {
		if (str == null || str.trim().equals(""))
			return "";
		return str.trim();
	}
	
	//added by Brent Zhang 2005/10/10
    public static String trimSpace(String temp) {
        String result = "";
    	if (temp!=null && !"".equalsIgnoreCase(temp)){
	    	int length = temp.length();
	        int len = length;
	    	int st = 0;
	    	char[] val = temp.toCharArray();
	    	while ((st < len) && ((val[st] == ' ') || (val[st] == '　'))) {//前面的半型空白和全型空白
	    	    st++;
	    	}
	    	while ((st < len) && ((val[len-1] == ' ') || (val[len-1] == '　'))) {//後面的半型空白和全型空白
	    	    len--;
	    	}
	    	result = ((st > 0) || (len < length)) ? temp.substring(st, len) : temp;
    	}
    	return result;
    }	
	
	/**
	 * 字符串格式化函數
	 * 
	 * @param String
	 * @return: 三位一組以逗號分割的字符串,如果為null,或空串或只有空格的字符串,返回空串;
	 */
	public static String formatStrSplite(String s) {
		if (s == null)
			return "";
		s = s.trim();
		if (s.equals("&nbsp;"))
			return "";
		if (s.length() == 0)
			return "";
		
		char[] arr = s.toCharArray();
		StringBuffer sb = new StringBuffer();
		
		for(int i=0;i<arr.length;i++){
			char ch = arr[arr.length-i-1];
			if(i!=0&& i%3==0) 
				sb.append(',');
			sb.append(ch);
		}
		return sb.reverse().toString();
		
	}
	
    // ?srcString截取至最大maxLen(中文需一次取2 bytes)
    public synchronized static String cropping(String srcString,
        int maxLen) throws Exception {
        if(srcString == null) {
            throw new Exception("cropping():?入???null!");
        }
        String desString = null;
        //??String?byte?料
        byte[] desBytes = srcString.getBytes();
        //比?desBytes之?度
        if(desBytes.length > maxLen) {
            //呼叫cropping函式,??果assign回去
            byte[] tmpBytes = cropping(desBytes, maxLen);
            desBytes = tmpBytes;
        }
        //??srcBytes?Sting,?成回?之desString
        desString = new String(desBytes);
        return desString;
    }
    
    //?srcByte截取至最大maxLen(中文需一次取2 bytes)
    public synchronized static byte[] cropping(byte[] srcBytes,
        int maxLen) throws Exception {
        if(srcBytes == null) {
            throw new Exception("cropping():?入???null!");
        }
        byte[] desBytes = srcBytes;
        //比?srcBytes 之?度
        if(srcBytes.length > maxLen) {
            //?理中文字, 重新?算maxLen
            for(int i = 0; i < maxLen; i++) {
                //若?中文?一次跳2 byte
                if(srcBytes[i] < 0) {
                    i++;
                }
                if(i == maxLen) {
                    maxLen = maxLen - 1;
                }
            }
            //以maxLen??,?srcBytes中超出maxLen之部份移除
            byte[] tmpBytes = new byte[maxLen];
            System.arraycopy(srcBytes, 0, tmpBytes, 0, maxLen);
            desBytes = tmpBytes;
        }
        return desBytes;
    }
	
	public static void main(String[] args) {
		System.out.println(formatAmtForPdf(1234567.0));
		//IDNO檢核==============================================		
//		String idno = "";
//		System.out.println("Idno check result:"+checkIdno(idno));
		//=====================================================
//		String date ="10";
//		System.out.println(StringUtil.formatToAplyBook(date));
//		System.out.println(formatDecimalDigits(date, 1));
		/*
	    int decimalDigits = 2;
		String s = "-1.1234";
		System.out.println(format(s,decimalDigits));
		s = "-0.5781";
		System.out.println(format(s,decimalDigits));
		s = "000.0000";
		System.out.println(format(s,decimalDigits));
		s = "000.1479";
		System.out.println(format(s,decimalDigits));
		s = "123.5711";
		System.out.println(format(s,decimalDigits));
		double d = 9999999.55;
		System.out.println(format(d,decimalDigits));
		System.out.println(format("0.00",0));
		System.out.println(format("-0.00",0));
		System.out.println(format("0",0));
		System.out.println(format("-0",0));
		System.out.println(format(0,0));
		System.out.println(format(0.00,0));
		System.out.println(format(-0,0));
		System.out.println(format(-0.00,0));
		System.out.println("--------------");
		System.out.println(format("0.000",2));
		System.out.println(format("-0.000",2));
		System.out.println(format("0",2));
		System.out.println(format("-0",2));
		System.out.println(format(0,2));
		System.out.println(format(0.000,2));
		System.out.println(format(-0,2));
		System.out.println(format(-0.000,2));
		System.out.println(format("-1.1234",0));
		System.out.println(format("-1.56",0));
		System.out.println(format("-999999",0));
		System.out.println(format("-999999",2));
		System.out.println("*****************");
		//System.out.println(format("asd",2));
		//System.out.println(format("-asd",0));
		
		
		long  l = 9999999;
		System.out.println(format(l,0));
		int i = 9999999;
		System.out.println(format(i,decimalDigits));
		
		String date = "20040505";
		System.out.println(formatDate(date));
		
		System.out.println("111: " + parseDouble(""));
		System.out.println("222: " + parseDouble(null));
		System.out.println("333: " + parseDouble("awaw"));
		System.out.println("444: " + parseDouble("123"));
		*/
//	    String temp = "";
//	    String temp1 = "";
//	    temp = " ";
//	    temp1=trimSpace(temp);
//        System.out.println("org:"+temp+";cur:"+temp1);
//	    temp = "中國 ";
//	    temp1=trimSpace(temp);
//    	System.out.println("len:"+temp.length());
//    	System.out.println("len1:"+temp1.length());
//        System.out.println("org:"+temp+";cur:"+temp1+".");
//	    temp = "中國　";
//	    temp1=trimSpace(temp);
//    	System.out.println("len:"+temp.length());
//    	System.out.println("len1:"+temp1.length());
//        System.out.println("org:"+temp+";cur:"+temp1+".");        
//	    temp = " 中國　";
//	    temp1=trimSpace(temp);
//    	System.out.println("len:"+temp.length());
//    	System.out.println("len1:"+temp1.length());	    
//        System.out.println("org:"+temp+";cur:"+temp1+".");
//	    temp = "中國　 　";
//	    temp1=trimSpace(temp);
//    	System.out.println("len:"+temp.length());
//    	System.out.println("len1:"+temp1.length());	    
//        System.out.println("org:"+temp+";cur:"+temp1+".");
//	    temp = "　中國v　 　";
//	    temp1=trimSpace(temp);
//    	System.out.println("len:"+temp.length());
//    	System.out.println("len1:"+temp1.length());	    
//        System.out.println("org:"+temp+";cur:"+temp1+".");
//	    temp = "　 　中a國　 　";
//	    temp1=trimSpace(temp);
//        System.out.println("org:"+temp+";cur:"+temp1+".");        
//	    temp = "中國　 　人民    ";
//	    temp1=trimSpace(temp);
//    	System.out.println("len:"+temp.length());
//    	System.out.println("len1:"+temp1.length());	    
//        System.out.println("org:"+temp+";cur:"+temp1+".");        
	   System.out.println(StringUtil.removeLeftZero("0011")) ;
	}
	
	/**
	 * 判斷字符串是否為空
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str){
		return str==null||str.trim().equals("");
	}
	
	/**
	 * 判斷統編
	 * @param idno
	 * @return
	 */
	public static boolean isIdno(String inputidno){
		boolean result=false;
		if(StringUtil.isBlank(inputidno)) return false;
		String idno=inputidno.trim();
		if(idno.startsWith("&")){
			if(idno.length()<8 || idno.length()>10) return false;
			String temp="&&&&&&&&&&";
			String sub = idno.substring(0,idno.lastIndexOf("&")+1);
			if(!sub.equals(temp.substring(0,idno.lastIndexOf("&")+1))){
				return false;
			}else{
				result=isAlpha(idno.substring(idno.lastIndexOf("&")+1,idno.length()));
			}
		}else{
			if(idno.length()!=8) return false;
			result=isAlpha(idno);
		}
	
		return result;
	}
	
	
	/**
	 * 
	 * @param inputediid
	 * @return
	 */
	public static boolean isEDIid(String inputediid){
		boolean result = false;
		if(StringUtil.isBlank(inputediid)) return false;
		String ediid = inputediid.trim();
		if(ediid.length()!=12) return false;
		if(ediid.startsWith("&")){
			String temp="&&&&&&&&&&&&";
			String sub = ediid.substring(0,ediid.lastIndexOf("&")+1);
			if(!sub.equals(temp.substring(0,ediid.lastIndexOf("&")+1))){
				return false;
			}else{
				result=isAlpha(ediid.substring(ediid.lastIndexOf("&")+1,ediid.length()));
			}
		}else{
			result=isAlpha(ediid);
		}
		
		return result;
	}
	
	/**
	 * 判斷英數字
	 * @param val
	 * @return
	 */
    public static boolean isAlpha(String val){
    	if(val == null) return false;
    	for(int i=0; i < val.length();i++) {
    		char ch = val.charAt(i);
    		
    		if((ch < 'a' || ch > 'z' ) && (ch < 'A' || ch > 'Z' ) && (ch < '0' || ch > '9' )) 
    			return false;
    	}
    	return true;
    }
    
    /**
     * 判斷是否為大寫英文字母
     * @param val
     * @return
     */
    public static boolean isCapsEnglish(String val){
    	if(val == null) return false;
    	for(int i=0; i < val.length();i++) {
    		char ch = val.charAt(i);
    		
    		if(ch < 'A' || ch > 'Z' ) 
    			return false;
    	}
    	return true;
    }
    
    /**
     * 
     * @param val
     * @return
     */
    public static boolean isNumber(String val){
    	if(val == null) return false;
    	for(int i = 0 ; i < val.length() ; i++){
    		char ch = val.charAt(i);
    		if(ch<'0'||ch>'9'){
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * 
     * @param val
     * @return
     */
    public static boolean isNumberOrCapsEnglish(String val){
    	if(val == null) return false;
    	for(int i=0; i < val.length();i++) {
    		char ch = val.charAt(i);
    		
    		if((ch < 'A' || ch > 'Z' ) && (ch < '0' || ch > '9' )) 
    			return false;
    	}
    	return true;
    }
    
    /**
     * 
     * @param val
     * @return
     */
    public static boolean isAmount(String amount){
    	if(amount == null) return false;
    	
    	return amount.matches("^(((0-9)+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$");    	
    }

	/**
	 * @param object
	 * @return
	 */
	public static boolean isBlank(Object object) {
		if(object==null) return true;
		return isBlank(object.toString());
	}
	
	/**
	 * 取得字符串指定位置的字符
	 * @param str
	 * @param num
	 * @return
	 */
	public static String getStrChar(String str,int num){
		if(str==null) return ""; //add by royal shen 2007-03-21
		if(num>str.length()) return "";
		return str.substring(num-1,num);
	}
	
	/**
	 * 根據幣別格式化頁面金額顯示（TWD、NTD、JPY、THB無小數位，其他2位小數）
	 * 2000.225--->2,000.23
	 * @param amt
	 * @param crnc
	 * @return
	 */
	public static String formatAmtForPageView(String amt,String crnc){
		//if("TWD".equals(crnc)||"NTD".equals(crnc)||"JPY".equals(crnc)||"THB".equals(crnc)){
		if("TWD".equals(crnc)||"NTD".equals(crnc)||"JPY".equals(crnc)){
			return format(formatNumber(amt,0),0);
		}else{
			return format(formatNumber(amt,2),2);
		}
	}
	
	/**
	 * 根據幣別格式化頁面金額顯示（TWD、NTD、JPY、THB無小數位，其他2位小數）
	 * 2000.225--->2000.23
	 * @param amt
	 * @param crnc
	 * @return
	 */
	public static String formatEDoubleForPageView(double amt,String crnc){
		//if("TWD".equals(crnc)||"NTD".equals(crnc)||"JPY".equals(crnc)||"THB".equals(crnc)){
		if("TWD".equals(crnc)||"NTD".equals(crnc)||"JPY".equals(crnc)){
			return formatNumber(amt+"",0);
		}else{
			return formatNumber(amt+"",2);
		}
	}
	
	/**
	 * 格式化頁面汇利率显示
	 * @param rate
	 * @param pos
	 * @return
	 */
	public static String formatRate(String rate , int pos){
		if(rate==null||"".equals(rate)||"&nbsp".equals(rate)){
			return "";
		}else{
			String temp = StringUtil.formatNumber(rate,pos);
//			if(Double.parseDouble(temp)==0){
//				return "0.0";
//			}else{
//				while(temp.endsWith("0")){
//					temp = temp.substring(0,temp.length()-1);
//				}
//				if(temp.endsWith(".")){
//					temp = temp +"0";
//				}
//			}
			return temp;
		}
	}
	
	/**
	 * 四捨五入 格式化數字 eg.20000.335--->20000.34
	 * @param s
	 * @param decimalDigits
	 * @return
	 */
//	public static String formatNumber(String  s ,int decimalDigits){
//		try{
//			if(s == null) return "";
//			s = s.trim();
//			if(s.equals("&nbsp;")) return "";
//			if(s.length() == 0) return "";
//			String patten = "0";
//			StringBuffer sb =new StringBuffer(patten);
//			if(decimalDigits>0){
//				sb.append(".");
//				for(int i=0 ; i < decimalDigits ; i++){
//					sb.append("0");
//				}
//			}else{
//				return String.valueOf(Math.round(Double.parseDouble(s)));
//			}
//			double amt = Double.parseDouble(s);
//			return new java.text.DecimalFormat(sb.toString()).format(amt);
//		}catch(Exception e){
//			//format error;
//			LOG.debug(" number format error : " +e.toString());
//			return "";
//		}
//	}
	/**
	 * 四捨五入 格式化數字 eg.20000.335--->20000.34
	 * @param s
	 * @param decimalDigits
	 * @return
	 */
	public static String formatNumber(String  s ,int decimalDigits){
		if(s == null) return "";
		s = s.trim();
		if(s.equals("&nbsp;")) return "";
		if(s.length() == 0) return "";
        BigDecimal bd = new BigDecimal(s);
        BigDecimal db1 = bd.setScale(decimalDigits,BigDecimal.ROUND_HALF_UP);
        return db1.toString();
        
	}
	
	
	/**
	 * 四捨五入 格式化數字 eg.20000.335--->20,000.34
	 * @param s
	 * @param decimalDigits
	 * @return
	 */
	public static String formatNumberWithD(String  s ,int decimalDigits){
		return format(formatNumber(s,decimalDigits),decimalDigits);
	}
	
	/**
	   * 半形字元轉全形字元
	   * @param char halfwidthchar
	   * @return char fullwidthchar
	   */
	  public static char half2FullWidth(char halfwidthchar) {
	    // 全形文字直接return
		//20101230 modified by AJ - 預設使用UTF-8 getBytes,因為R6上ISO8859-1對不出中文字,所以getBytes後的長度always等於1 - start
		int intCompare = 0;
		try {
			intCompare = String.valueOf(halfwidthchar).getBytes("UTF-8").length;
		} catch (UnsupportedEncodingException e) {
			LOG.error("StringUtil:half2FullWidth:"+e.toString());
		}
//	    if (String.valueOf(halfwidthchar).getBytes().length != 1){
		if (intCompare!=1)
	      return halfwidthchar;
		//20101230 modified by AJ - 預設使用UTF-8 getBytes,因為R6上ISO8859-1對不出中文字,所以getBytes後的長度always等於1 - end
		// 半形空白無法mapping
		if (halfwidthchar == ' ')
		  return '　';
		
		return (char) ( (int) halfwidthchar + 65248);
	  }
	  
	  /**
	   * 半形字元轉全形字元

	   * @param char halfwidthchar
	   * @return char fullwidthchar
	   */
	  public static String half2FullWidthReplace(String str) {
		  String b1 =""; 
		  b1 = str; 
		  
		  String q1[]={"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"," ","-","+","(",")","."}; 
		  String q2[]={"０","１","２","３","４","５","６","７","８","９","ａ","ｂ","ｃ","ｄ","ｅ","ｆ","ｇ","ｈ","ｉ","ｊ","ｋ","ｌ","ｍ","ｎ","ｏ","ｐ","ｑ","ｒ","ｓ","ｔ","ｕ","ｖ","ｗ","ｘ","ｙ","ｚ","Ａ","Ｂ","Ｃ","Ｄ","Ｅ","Ｆ","Ｇ","Ｈ","Ｉ","Ｊ","Ｋ","Ｌ","Ｍ","Ｎ","Ｏ","Ｐ","Ｑ","Ｒ","Ｓ","Ｔ","Ｕ","Ｖ","Ｗ","Ｘ","Ｙ","Ｚ","　","－","＋","（","）","．"}; 
		  //這邊用68是因為小寫26個+大寫26個+數字10個 +6
		  for (int i=0;i<68;i++){ 
			  while(b1.indexOf(q1[i])!=-1){
				  b1= b1.replace(q1[i],q2[i]); 
			  }
		  }   

		  return b1;
	  }

	  /**
	   * 半形字元轉全形字元
	   * @param str
	   * @return String
	   */
	  public static String half2FullWidth(String str) {
	    char[] charArray = str.toCharArray();
	    for (int i = 0; i < charArray.length; i++) {
	      charArray[i] = StringUtil.half2FullWidth(charArray[i]);
	    }
	    String result=new String(charArray);
	    try {
			return new String(result.getBytes(System.getProperty("file.encoding")));
		} catch (UnsupportedEncodingException e) {
			LOG.warn("half2FullWidth ERR!"+e.toString());
			return str;
		}
	  }
	  
	  /**
	   * Description：半形字元轉全形字元使用預設編碼
	   * Create by AJax, 2011/7/1
	   * @param str
	   * @return
	   */
	  public static String half2FullByDftEncode(String str){
		    char[] charArray = str.toCharArray();
		    for (int i = 0; i < charArray.length; i++) {
		    	charArray[i] = StringUtil.half2FullWidth(charArray[i]);
		    }
		    return new String(charArray);
	  }
	  
	  /**
		 * 根據幣別格式化頁面金額顯示（TWD、NTD、JPY、THB無小數位，其他2位小數）4電文模組
		 * 200000--->2000(TWD&NULL&"")或者2000022---->2000.22(外幣)
		 * @param amt
		 * @param crnc
		 * @param i
		 * @return
		 */
		public static String formatAmtForTelegramView(String amt,String crnc,int i){
			//20100624 modified by AJ - Start
			//1.修正美金金額0000000000001,小數位兩位回傳1的錯誤,應該為0.01
			String result = "0";
			if(StringUtil.isBlank(amt)){
				amt = "0";
			}
	        //10的幾次方
	        double f = Math.pow(10, i);
	        BigDecimal b1 = new BigDecimal(Double.toString(f));			        

	        //purAmount除以位數代表實際金額amount
	        BigDecimal b2 = new BigDecimal(amt);
	        
	        //if(StringUtil.isBlank(crnc)||"".equals(crnc)||"TWD".equals(crnc)||"NTD".equals(crnc)||"JPY".equals(crnc)||"THB".equals(crnc)){
	        if(StringUtil.isBlank(crnc)||"".equals(crnc)||"TWD".equals(crnc)||"NTD".equals(crnc)||"JPY".equals(crnc)){
	        	
	        	return (b2.divide(b1, 0, BigDecimal.ROUND_DOWN)).toPlainString();
	        }else{
	        	return (b2.divide(b1, i, BigDecimal.ROUND_DOWN)).toPlainString();
	        }
//			if(StringUtil.isBlank(amt)){
//				return "";
//			}
//			if(amt.length()<i)
//				return amt;
//			if(StringUtil.isBlank(crnc)||"".equals(crnc)||"TWD".equals(crnc)||"NTD".equals(crnc)||"JPY".equals(crnc)||"THB".equals(crnc)){
//				if(!StringUtil.isBlank(amt)){
//					return amt.substring(0,amt.length()-i);
//				}else{
//					return "0";
//				}
//			}else{
//				if(!StringUtil.isBlank(amt)){
//					return amt.substring(0,amt.length()-i)+"."+amt.substring(amt.length()-i,amt.length());
//				}else{
//					return "0.00";
//				}
//			}
			//20100624 modified by AJ - End
		}
		
		/**
		 * 根據幣別格式化頁面金額顯示（TWD、NTD、JPY、THB無小數位，其他2位小數）4電文模組(外币)
		 * 200000--->2000(TWD&NULL&"")或者2000022---->2000.22(外幣)
		 * @param amt
		 * @param crnc
		 * @param i 小數位
		 * @return
		 */
		public static String formatAmt4TelegramView(String amt,String crnc,int i){
			//20100624 modified by AJ - Start
			//1.修正美金金額0000000000001,小數位兩位回傳1的錯誤,應該為0.01
			//2.與method[formatAmtForTelegramView]同步
			return formatAmtForTelegramView(amt, crnc, i);
//			String prefix="";
//			String resultAmt="";
//			if(StringUtil.isBlank(amt)){
//				return resultAmt;
//			}else if(amt.length()<i){
//				return amt;
//			}else if(!"RATE".equalsIgnoreCase(crnc)){
//				prefix=amt.substring(0,1);
//				amt=amt.substring(1,amt.length());
//				String preAmt=amt.substring(0,amt.length()-i);
//				LOG.debug("The preAmt="+preAmt);
//				if(StringUtil.isBlank(StringUtil.trimSpace(preAmt))){
//					preAmt="0";
//				}else{
//					preAmt=""+Long.parseLong(preAmt);
//				}
//				LOG.debug("The preAmt1="+preAmt);
//				if(StringUtil.isBlank(crnc)||"".equals(crnc)||"TWD".equals(crnc)||"NTD".equals(crnc)||"JPY".equals(crnc)||"THB".equals(crnc)){
//					if(!StringUtil.isBlank(amt)){
//						resultAmt= preAmt;
//					}else{
//						resultAmt= "0";
//					}
//				}else{
//					if(!StringUtil.isBlank(amt)){
//						resultAmt= preAmt+"."+amt.substring(amt.length()-i,amt.length());
//					}else{
//						resultAmt= "0.00";
//					}
//				}
//				if("-".equals(prefix)){
//					return prefix+resultAmt;
//				}else{
//					return resultAmt;
//				}
//			}else{
//				if(!StringUtil.isBlank(amt)){
//					String preRate=amt.substring(0,amt.length()-i);
//					if(StringUtil.isBlank(StringUtil.trimSpace(preRate))){
//						preRate="0";
//					}else{
//						preRate=""+Long.parseLong(preRate);
//					}
//					return preRate+"."+amt.substring(amt.length()-i,amt.length());
//				}else{
//					return "0.00";
//				}
//			}
			//20100624 modified by AJ - End
		}
		/**
		 * 
		 * 功能：输入 20070101 ->  2007年01月01日
		 * @author： Ewin zhou
		 * @param date
		 * @return
		 * String
		 */
		public static String formatToAplyBook(String date){
			String returnDate="";
			if(!StringUtil.isBlank(date))
				returnDate=(String)date.substring(0, 4)+"年"+date.substring(4, 6)+"月"+date.substring(6, 8)+"日";
			return returnDate;
			
		}
		
		/**
		 * 去掉 '%':  344.33%-->3.44330
		 * @param rateWithPercent
		 * @return
		 */
//		public static BigDecimal rate2BigDecimal(String rateWithPercent){
//			String rate = rateWithPercent .substring(0,rateWithPercent.length()-1);
//			BigDecimal exchange = new BigDecimal(rate).divide(new BigDecimal(100),5,BigDecimal.ROUND_HALF_UP);
//			return exchange;
//		}
		/**
		 * 
		 * 功能：添加小數點 例如：字符12342 --> 123.42 eg:formatDecimalDigits(12342,2)
		 * @author： Ewin zhou
		 * @param doub 需要轉換的字符
		 * @param decimalDigits  小數點位數
		 * @return
		 * String
		 */
		public static String formatDecimalDigits(String doub,int decimalDigits){
			String formated="";
			if(decimalDigits<=0){
				return doub;
			}else{
				formated = formatEDouble(new Double(doub).doubleValue(),0);
				if(formated.length()<=decimalDigits){
					int len = decimalDigits-formated.length();
					for(int i=0;i<len;i++)
						formated = "0"+formated;
					return "0."+formated;
				}else{
					formated = formated.substring(0, formated.length()-decimalDigits)+"."+formated.substring(formated.length()-decimalDigits);
					return formated;
				
				}
			}
		}
		
        /**
         * String --> InputStream
         * @throws IOException 
         * */
		public static InputStream convertToInputStream(String str,String encoding) throws IOException{
		        ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes(encoding)); 
		        return is;
		    }

		
		/**
         * InputStream --> String
         * */      
		public static String convertToString(InputStream is) throws IOException{
		        BufferedReader in = new BufferedReader(new InputStreamReader(is));
		        StringBuffer buffer = new StringBuffer();
		        String line = "";
		        while ((line = in.readLine()) != null)
		              {
		               buffer.append(line);
		           }
		         return buffer.toString();
		 }
		
		public static byte[] InputStreamToByte(InputStream iStrm) throws IOException {
			iStrm.reset();
			//System.out.println(iStrm.available());
			byte[] data = new byte[iStrm.available()];
			iStrm.read(data); 
			return data;
			/*
		    ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		    int ch;
		    byte[] data = new byte[1024];
		    while ((ch = iStrm.read(data)) != -1)
		    {
		       bytestream.write(data);
		    }
		    byte imgdata[]=bytestream.toByteArray();
		    bytestream.close();
		    return imgdata;    
		    */
		  } 
		
		 /**
		  *  HONDA
		   * EX. padString("1",2,"0","LEFT")  "1"==>傳入的字串 2==>兩位 "0"==>補什麼 "LEFT"==>靠左補
		   * pad two string from specific index
		   * 建立日期： 
		   * @return java.lang.String
		   */
		  public static String padString(String org, int len, String pad,
		                                 String left) {
		    String result = org;

		    if (pad.length() > 1)
		      return org;

		    if (org.length() > len)
		      return org.substring(0, len);

		    int count = 0;
		    count = len - result.length();

		    for (int i = 0; i < count; i++) {
		      if (left.equals("LEFT"))
		        result = pad + result;
		      else
		        result = result + pad;
		    }
		    return result;
		  }
		  
		  /*
		   * 檢查是否包含全形字元
		   */
		  public static boolean isIncludedFullChar(String text){
				if (StringUtil.isBlank(text)) return false;
				
				text = text.trim();
				
				int org_len = text.length();
				try {
					byte[] utf_data = text.getBytes("UTF-8");
					if (utf_data.length != org_len){
						return true;
					}
					return false;
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}
		  
		  /*
		   * 取得最大指定長度的字串內容。
		   * 如果傳入的字串長度<=指定最大長度，
		   * 將回傳原字串內容；否則傳回最大長度的字串內容
		   * 
		   */
		  
		  public static String cutToMaxLenStr(String content, int maxLen){
			  if (isBlank(content)) return content;
			  
			  int available_len = 0;
			  available_len = content.length();
				if (available_len > maxLen){
					available_len = maxLen;
				}
				
				return content.substring(0, available_len);
		  }
		  
		  /**
			 * 根據幣別格式化頁面金額顯示（TWD、NTD、JPY、THB無小數位，其他2位小數）4電文模組
			 * 200000--->2000(TWD&NULL&"")或者2000022---->2000.22(外幣)
			 * @param amt
			 * @param crnc
			 * @param i
			 * @return
			 */
		  public static String formatDoubleAmntForTelegramView(double amnt, String curr, int i){
			  return StringUtil.formatAmtForTelegramView(StringUtil.formatEDouble(amnt, i), curr, i);
		  }
		  
		  /**
		   * 將最大13位數的十進位數值轉成66進位的文字
		   * @return 回傳0~9, A~Z, a~z等36位數所組成的66進位文字
		   */
		  public static String decimalToCARRY66String(long decimal){
			  	int carry_count = CARRY66_MAP.size();
			  	StringBuffer sb = new StringBuffer();
				long divide_result = decimal;//商
				long remainder = 0;//餘數
				String carry = null;
				while(divide_result > 0){
					remainder = divide_result % carry_count;
					carry = (String) CARRY66_MAP.get(String.valueOf(remainder));
					sb.append(carry);					
					divide_result = divide_result / carry_count;					
				}
				
				sb.reverse();
				return sb.toString();
		  }
		  
		  /**
		   * 回傳一段至少7位文字長度的UUID（也就是7位已經保證唯一性了）。
		   * 本UUID只在該台機器上有效，UUID是以系統時間用66進位所換算出的唯一性質的字串
		   * @return
		   */
		  public synchronized static  String getLocalUUID(){
			  try {
				Thread.sleep(1);//delay 1 millis可保證下面取得的系統時間絕不會與上次呼叫本方法時取得的時間重複
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
			
			long mis = System.currentTimeMillis();
			return decimalToCARRY66String(mis);  						  
		  }
		  
		  
		  /*
		   * 檢查傳入的字串是否含有任一半形字元
		   */
		  public static boolean isIncludedHalfChar(String text){
			  if (StringUtil.isBlank(text)) return true;
			  		  
			  String patternStr = "[\u0020-\u007e].*";
			  Pattern pattern = Pattern.compile(patternStr);
			  Matcher matcher = pattern.matcher(text);
			  return matcher.find();			  
		  }
		  
		  /**
		   * 檢查是否全/半形夾雜
		   * @param text
		   * @return
		   */
		  public static boolean isComposedOfHalfAndFullChars(String text){
			  if (StringUtil.isBlank(text)) return false;
			  
			  if (isIncludedHalfChar(text) && isIncludedFullChar(text))
				  return true;
			  else
				  return false;
		  }
		  
	  /**
	   * Description：身份字號,統一編號,居留證號正確性檢核
	   * Create by AJax, 2011/7/28
	   * @param idno
	   * @return true:正確 false:失敗
	   */
	  public static boolean checkIdno(String idno){
		  boolean result = true;
		  if (StringUtil.isBlank(idno)) return result;
		  
		  idno = idno.trim();
		  String regex = "[a-zA-Z]\\d{9}";//身份字號
		  result = idno.matches(regex);
		  
		  regex = "\\d{8}";//統一編號
		  if(!result) result = idno.matches(regex);
		  
		  regex = "[a-zA-Z]{2}\\d{8}";//居留證號
		  if(!result) result = idno.matches(regex);
		  regex = "\\d{8}[a-zA-Z]{2}";//居留證號II
		  if(!result) result = idno.matches(regex);
		  
		  return result;
	  }
	  /**
	   * @author hangzhouSDC fong  2007-1-12
	   * @method replaceAll (source, target, replace )
	   * @param source
	   * @param target
	   * @param replace
	   * @return
	   */
	public static String replaceAll(String source, String target, String replace) {
		if(isBlank(source)) return source;
		while (source.indexOf(target) >= 0) {
		    source = replaceFirst(source, target, replace);
		}
		return source;
	}
	
	/**
     * @author hangzhouSDC fong  2007-1-12
     * @method replaceFirst (source, target, replace )
     * @param source
     * @param target
     * @param replace
     * @return
     */
    public static String replaceFirst(String source, String target, String replace) {
    	if(isBlank(source)) return source;
	    if (source.indexOf(target) < 0) {
	        return source;
	    }
	    String result = null;
	    result = source.substring(0, source.indexOf(target));
	    result = result + replace;
	    result = result + source.substring(source.indexOf(target)+target.length(), source.length());
	    return result;
	}
    
    public static boolean isInteger(String data) {
		if (data == null)
			return false;
		for (int i = 0; i < data.length(); i++) {
			char ch = data.charAt(i);
			if (i == 0 && ch == '-')
				continue;
			if (ch < '0' || ch > '9')
				return false;
		}
		return true;
	}
    
    public static String[] split(String s,String flag){
		String[] results=null;
		if(s==null||s.equalsIgnoreCase("")){
			return null;
		}
		if(s.indexOf(flag)<0){
			return new String[]{s};
		}else{
			List partList = new ArrayList();
			while (s.indexOf(flag) >= 0) {
				partList.add(s.substring(0, s.indexOf(flag)));
				s = s.substring(s.indexOf(flag)+flag.length(), s.length());
			}
			partList.add(s);
			results = (String[]) partList.toArray(new String[]{});
		}
		return results;
	}
    
    /**
     * 組custid
     * @param custId1
     * @param custId2
     * @return
     */
    public static String compositeCustId(String custId1,String custId2){
    	if(isBlank(custId1)&&isBlank(custId2)){
    		return "";
    	}
    	if(custId1.trim().length() >8)
    	return custId1+custId2;	
    	else	
    	return custId1.trim()+"  "+custId2;
    }
	/**
     * 将Sql判断条件添加到SQL语句总
     * rock ding 2007/8/20
     * @param SqlString1 需要添加在原始SQL後的SQL判断条件
     * @param SqlString 原始SQL
     * @return
     */
    public static String AddSqlSting1ToSqlString(String SqlString1,String SqlString){
    	StringBuffer resultSqlString=new StringBuffer(SqlString);
    	int whereIndex=SqlString.toUpperCase().indexOf("WHERE");
    	if(whereIndex<=0){    		
    		throw new RuntimeException("the sql string not contain  'where' please insert the 'where'");
    	}else{
    		resultSqlString.insert(whereIndex+5,SqlString1+" and ");
    	}
    	return resultSqlString.toString();
    	
    }
    
    /**
     * 功能：从SQL语句中获取第一个表的别名
     * rock ding 2007/8/20
     * @param sqlName
     * @return
     */
    public static String getTableNameFromSql(String queryString){
    	String copyOfSql=new String(queryString);
    	String tableName="";
    	int fromIndex=copyOfSql.toUpperCase().indexOf("FROM");
    	int whereIndex=copyOfSql.toUpperCase().indexOf("WHERE");
    	String tableString=null;
    	if(whereIndex<=0){    		
    		throw new RuntimeException("the sql string not contain  'where' please insert the 'where'");
    	}else{
    		tableString=copyOfSql.substring(fromIndex+4,whereIndex);
    	}    	
    	int firstAsIndex=tableString.toUpperCase().indexOf("AS");
    	int firstTableIndex=tableString.indexOf(",");
    	if(firstAsIndex>0&&(firstAsIndex<firstTableIndex||(firstTableIndex==-1&&firstAsIndex>0))){
    		tableName=tableString.substring(firstAsIndex+2,(firstTableIndex==-1?tableString.length():firstTableIndex)).trim();
    	}
    	return tableName;
    }

    /**
	 * 將srcString左靠右補空白至長度len為止
	 * 
	 * @param srcString
	 * @param len
	 * @return
	 */
	public synchronized static String padding(String srcString, int len) {
		if (srcString == null)
			srcString = "";
		String desString = null;
		// 計算srcString之長度
		int srcLen = srcString.getBytes().length;

		// 將回傳之字串補上不足之空白
		desString = srcString;
		if (srcString == null) {
			desString = "";
		}
		if (desString.length() > len) {
			LOG.warn("資料長度大於定義長度! 資料內容:[" + desString + "],資料長度不得大於:[" + len
					+ "]");
			return desString.substring(0, len);
		}
		for (int i = 0; i < (len - srcLen); i++) {
			desString = desString + " ";
		}
		return desString;
	}

	/**
	 * 將srcString(左靠右補或右靠左補）空白至長度len為止
	 * 
	 * @param srcString
	 * @param len
	 * @param flag
	 *            "L" - 左補空格 其他 - 右補
	 * @return
	 */
	public synchronized static String padding(String srcString, int len,
			String flag) {
		String desString = null;
		// 計算srcString之長度
		int srcLen = 0;
		if (srcString != null) {
			srcLen = srcString.getBytes().length;
		}
		// 將回傳之字串補上不足之空白
		desString = srcString;
		if (srcString == null) {
			desString = "";
		}
		for (int i = 0; i < (len - srcLen); i++) {
			if ("L".equals(flag)) {
				desString = " " + desString;
			} else {
				desString = desString + " ";
			}
		}
		return desString;
	}
	
	/**
	 *  將srcString(左靠右補或右靠左補）"0"至長度len為止
	 * @param srcString
	 * @param len
	 * @param flag "L" - 左補0  其他  - 右補
	 * @return
	 */
	public synchronized static String paddingaddzero(String srcString, int len,String flag) {
		String desString = null;
		// 計算srcString之長度
		int srcLen=0;
		if(srcString!=null){
			try{
				srcLen = srcString.getBytes("GBK").length;
			} catch (Exception ex) {
				LOG.error("srcString取BYTE长度错误!", ex);
			}
		}
		// 將回傳之字串補上不足之空白
		desString = srcString;
		if(srcString==null){
			desString="";
		}
		for (int i = 0; i < (len - srcLen); i++) {
			if("L".equals(flag)){
				desString = "0" + desString;
			}else{
				desString = desString + "0";
			}
		}
		return desString;
	}
	
	//add by Arf 2012/08/07 begin	
	/**
	 * add by Arf 2012/08/07
	 * @param curno
	 * @return true:正確 false:失敗
	 */
	public static boolean checkCurno(String curno){
		boolean result = true;
		if (StringUtil.isBlank(curno)) return false;
  
		curno = curno.trim();
		
		if (curno.length()>2) return false;
		
		//2013-04-01_Jerrychien_主機幣別代碼可能一位至二位數字
		String regex = "\\d{2}";
		if(curno.length() == 1){
			regex = "\\d{1}";			
		}
		result = curno.matches(regex);
  
		return result;
	}
	
	/**
	 * add by Arf 2012/08/07  
	 * @param curno
	 * @return true:正確 false:失敗
	 */
	public static boolean checkSysid(String sysid){
		boolean result = true;
		if (StringUtil.isBlank(sysid)) return false;
  
		sysid = sysid.trim();
		
		if (sysid.length()< 3) return false;
		if (sysid.indexOf("..") != -1) return false;
		if (sysid.indexOf("/") != -1) return false;
  
		return result;
	}
	
	/**
	 * add by Arf 2012/08/07  
	 * @param curno
	 * @return true:正確 false:失敗
	 */
	public static boolean checkFilename(String finename){
		boolean result = true;
		if (StringUtil.isBlank(finename)) return false;
  
		finename = finename.trim();
		
		if (finename.indexOf("..") != -1) return false;
		
		if (finename.indexOf("/") != -1) return false;
		
		if (finename.length()<3) return false;
		
		//2013-12-26_JerryChien_Header Manipulation_fortify_增加檢核檔名不能有\r\n=:
		if (finename.indexOf("\r") != -1) return false;
		if (finename.indexOf("\n") != -1) return false;
		if (finename.indexOf("=") != -1) return false;
		if (finename.indexOf(":") != -1) return false;
		return result;
	}
	
	//add by Arf 2012/08/07 end
	
	/**
	 * 將Exception轉成String
	 * @param Throwable th
	 * @return
	 */
	public static String exceptionToString(Throwable th)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		th.printStackTrace(pw);
		return sw.toString();
	}
}
