/**
 * @(#)StringUtil.java
 *
 * Copyright (c) 2004 HiTRUST Incorporated. All rights reserved.
 * Description : 字串轉換公用程式
 * Modify History:
 *  v1.00, 2004/08/26, Bruce Zhai
 *   1) First release
 *  v1.10, 2004/09/06, Bruce Zhai
 *   2) Update
 *  v1.20, 2004/09/09, Bruce Zhai
 *   3) Update
 *  v1.20, 2004/09/15, Bruce Zhai
 *   4) Update
 *  v1.20, 2004/09/22, Bruce Zhai
 *   4) Update
 * 	v1.20, 2004/09/24, Bruce Zhai
 *   4) Update
 * 	v1.22, 2007/01/16, Chris
 *   1) 增加註解
 * 	v1.23, 2007/07/05, Brent Zhang, [2007/07/11 提交過版]
 *   1)FUBONCR-1 ,　調整後端限制特殊字元method, 並拆為非外幣使用及外幣使用
 *  v1.24, 2007/08/14, Ada Chen
 *   1)FUBONCR-156, 增加Cross-Site Scripting偵測method
 *  v1.25, 2008/01/15, Phoebe Wang
 *   1)FUBONCR-287, 調整外幣外匯資料字元檢核規則. modify validateAllFrgn(), validateOne()
 *  v1.26, 2008/02/15, Ada Chen
 *   1)FUBONCR-304, 調整外幣受款人及外幣付款交易資料不可為全型字(含檔案上傳 及 線上編輯) 
 *   	1. modify method: validateAllFrgn(), validateOne()
 *  v3.00, 2008/08/08, Ada Chen
 *   1) FUBONCR-417, (越南地區)多語系、多地區別 - 整合同步Utiltiy Package
 *  v4.00, 2009/06/02, Ada Chen
 *   1) FUBONCR-737, (EOI)增加去除字串中的半形空白
 *   	1. Add Method: trimEmptyChar()
 *  v4.50, 2009/08/18, Ada Chen
 *   1) FUBONCR-953, 增加中文難字轉成指定字元, 修正半形轉全形功能
 *   	1. Add Method: trimEmptyChar()
 *   	2. Modify Method: toChanisesFullChar()
 *  v4.51, 2010/01/27, Karen
 *   1) FUBONCR-1088, 薪轉檔案上傳,檢核不可有全型字
 *   2) Add Method: validateFrameWorkCharSlry(),validateAllHalfCharSlry()
 *  v4.52, 2010/02/03, Phoebe
 *   1) FUBONCR-1089, 增加外幣受款帳號字元檢核規則, 允許輸入「英數字」、「.」、「-」、「/」
 *   2) Add Method: validateFrgnApntAcnt()
 *  v4.53, 2011/12/07, James
 *   1) FUBONCR-1571, 調整電子郵件格式, 可接受「--」
 *  v4.54, 2012/07/13, James
 *   1) FUBONCR-1705, 增加台幣/外幣/薪轉:檢核字串是否含Framework限制字元TAB(\t)
 *  v4.55, 2012/08/30, James Sun
 *   1) FUBONCR-1733, FBO 滲透測試報告 (2012/08/19)
 *   	1. Modify Method: validateInputForXSS()
 *  v4.56, 2012/09/19, James Sun
 *   1) FUBONCR-1751, [2012第3Q]部分主機回覆訊息會被判定為 XSS 攻擊(2012/09/17)
 *   	1. Modify Method: validateInputForXSS()
 *  v4.57, 2012/12/26, Emy
 *   1) FUBONCR-1818,外幣受款人名稱地址多開放可輸入全形字
 *   2) 多型，多加一個參數boolean: validateAllFrgn(),validateOne()
 *  v6.00, 2013/05/14, Casey
 *   1) FUBONCR-1922,外幣受款人/受款銀行/附言限制字元規則調整
 *   2) 包含: ~=!"%&*<>;@#$[]\_^`|
 *   3) 第一碼不得為: /-:
 *  v6.01, 2013/06/06, Casey
 *   1) FUBONCR-1923,CM統計報表需因應個資法進行資料遮蔽處理 
 *   2) 增加 toMask()
 *  v6.02, 2013/06/10, Ada Chen
 *   1) FUBONCR-1922, 外幣受款人/受款銀行/附言限制字元規則調整, 調整第一碼可接受「/」
 *  v6.03, 2013/08/22, Phoebe
 *   1) FUBONCR-1955,外幣受款人帳號檢核檢核調整  , 帳號允許輸入空白  , 但帳號前面與後面不可輸入空白
 *   2) FUBONCR-1955,外幣自行受款人帳號自動去符號[.]、[-]、[/]、[空白]
 *   3) modify method: validateFrgnApntAcnt(), add method: replaceFrgnApntAcnt()
 */
package com.hitrust.acl.common;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * 字串轉換公用程式
 */
public class StringUtil {
    //LOG4J
	static Logger LOG = Logger.getLogger(StringUtil.class);
	
	/**
	 * 數字格式化函數加上百分位
	 * @param  number - 格式化前的數字;
	 * @param  decimalDigits - 小數位數;
	 * @return String - 三位一組以逗號分割的字符串;
	 */
	public static String format(double number, int decimalDigits) {
		if (number == 0d)
			number = 0d;

		boolean flag = false;
		if (decimalDigits < 0) {
			LOG.debug("小數位數不能小於0.");
			return "";
		}

		String pattern = "###,###,###,###,###,###";
		if (decimalDigits > 0) {
			flag = true;
			pattern += ".";
			for (int i = 0; i < decimalDigits; i++) {
				pattern += "0";
			}
		}

		DecimalFormat df = new DecimalFormat(pattern);
		if (number <= -1d) {
			return df.format(number);
		} else if (number > -1d && number < 0d) {
			return "-0" + df.format(number).substring(1);
		} else if (number >= 0d && number < 1d) {
			if (flag == true) {
				return "0" + df.format(number);
			} else {
				return df.format(number);
			}
		} else {
			return df.format(number);
		}
	}

	/**
	 * 數字格式化函數不加上百分位
	 * @param  number - 格式化前的數字;
	 * @param  decimalDigits - 小數位數;
	 * @return String - 不增加百分位的數值字串
	 */
	public static String formatNoDot(double number, int decimalDigits) {
		if (number == 0d)
			number = 0d;

		boolean flag = false;
		if (decimalDigits < 0) {
			LOG.debug("小數位數不能小於0.");
			return "";
		}

		String pattern = "##################";
		if (decimalDigits > 0) {
			flag = true;
			pattern += ".";
			for (int i = 0; i < decimalDigits; i++) {
				pattern += "0";
			}
		}

		DecimalFormat df = new DecimalFormat(pattern);
		if (number <= -1d) {
			return df.format(number);
		} else if (number > -1d && number < 0d) {
			return "-0" + df.format(number).substring(1);
		} else if (number >= 0d && number < 1d) {
			if (flag == true) {
				return "0" + df.format(number);
			} else {
				return df.format(number);
			}
		} else {
			return df.format(number);
		}
	}

	/**
	 * 數字格式化函數
	 * @param  s - 格式化前的字符串(是一個數字);
	 * @param  decimalDigits - 小數位數;
	 * @return String - 三位一組以逗號分割的字符串,如果為null,或空串或只有空格的字符串,返回空串;
	 */
	public static String format(String s, int decimalDigits) {
		if (s == null)
			return "";
		s = s.trim();
		if (s.equals("&nbsp;"))
			return "";
		if (s.length() == 0)
			return "";
		double number = Double.parseDouble(s);
		return format(number, decimalDigits);
	}

	/**
	 * 日期格式化函數
	 * @param date - 格式化前的字符串,長度必須為8,且是YYYYMMDD格式;如果為null,或空串或只有空格的字符串,返回空串;如果長度是不為8的字符串,返回空串;
	 * @return String - 格式為 YYYY/MM/DD 的字符串
	 */
	public static String formatDate(String date) {
		if (date == null)
			return "";
		date = date.trim();
		if (date.equals("&nbsp;"))
			return "";
		if (date.length() == 0 || date.length() != 8)
			return "";
		date = date.substring(0, 4) + "/" + date.substring(4, 6) + "/"
				+ date.substring(6, 8);
		return date;
	}

	/**
	 * 數字字串轉成DOUBLE型態
	 * @param str - 數字字串
	 * @return double - 數值
	 */
	public static double parseDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			return 0d;
		}
	}

	/**
	 * 判斷是否為整數Integer
	 * @param data - 欲判斷的字串
	 * @return boolean - true:為整數, false:不為整數
	 */
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

	/**
	 * 數值字串轉型成電文格式數值字串
	 * @param s - 數值字串
	 * @return Stirng - 電文格式數值字串
	 */
	public static String parseAmntFromTelegram(String s) {
		if (s == null) return "";
		s = s.trim();
		if (s.length() == 0) return "";
		double temp = 0;
		try {
			temp = Double.parseDouble(s);
		} catch (NumberFormatException nfe) {
			return "";
		}
		return new DecimalFormat("############").format(temp);
	}

	/**
	 * 數值字串轉型成電文格式數值字串
	 * @param s - 數值字串
	 * @param decimalDigits - 小數位數
	 * @return Stirng - 電文格式數值字串
	 */
	public static String formatForTelegram(String s, int decimalDigits) {
		if (s == null)
			return "";
		s = s.trim();
		if (s.length() == 0)
			return "";
		if (s.indexOf(",") > 0 || s.indexOf(".") > 0)
			return s;
		// remove left 0
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != '0') {
				s = s.substring(i);
				break;
			}
			if (s.length() - i <= decimalDigits) {
				break;
			}
		}
		// add digit
		s = s.substring(0, s.length() - decimalDigits) + "."
				+ s.substring(s.length() - decimalDigits);

		return format(Double.parseDouble(s), decimalDigits);
	}

	/**
	 * check Telegram Date is Empty
	 * @param date - 日期字串
	 * @return boolean - true:日期為零, false:日期不為零
	 */
	public static boolean checkTelegramDate(String date) {
		for (int i = 0; i < date.length(); i++) {
			if (date.charAt(i) != '0') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判斷字符串是否為空
	 * @param str - 欲判斷的字串
	 * @return boolean - ture:為空字串, false:不為空字串
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().equals("");
	}

	/**
	 * 判斷物件是否為空值
	 * @param object - 欲判斷的物件
	 * @return boolean - ture:為空, false:不為空
	 */
	public static boolean isBlank(Object object) {
		if (object == null)
			return true;
		return isBlank(object.toString());
	}

	/**
	 * 格式化數值(小數二位,加上小數點)
	 * @param amount - 數值(########)
	 * @return String - 格式化後的數值(######.##)
	 */
	public static String fomateAMT(String amount) {
		StringBuffer buffer = new StringBuffer(amount);
		buffer.insert(buffer.length() - 2, ".");
		return buffer.toString();
	}

	/**
	 * 去除中文字間的空白
	 * @param before_trim - 欲去除空白字串
	 * @return String - 已去除空白字串
	 */
	public static String trim4Chinese(String before_trim) {
		before_trim = before_trim.trim();
		char[] temp = before_trim.toCharArray();
		int pos = 0;
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] == '　')
				pos = i;
			else
				break;
		}
		before_trim = before_trim.substring(pos);
		pos = before_trim.length();
		for (int i = temp.length; i-- > 0;) {
			if (temp[i] == '　')
				pos = i;
			else
				break;
		}
		before_trim = before_trim.substring(0, pos);
		return before_trim;
	}

	/**
	 * 去除字串的間的半形空白, 含字串前後空白
	 * @param beforeStr - 欲去除空白字串
	 * @return String - 已去除空白字串
	 * @since v4.00
	 */
	public static String trimEmptyChar(String beforeStr) {
		
		beforeStr = beforeStr.trim();
		
		return beforeStr.replaceAll(" ", "");
	}
	
	/**
	 * 判斷E_MAIL格式
	 * @param email - 欲判斷的字串
	 * @return boolean - true:符合MAIL格式, false:不符合MAIL格式
	 */
	public static boolean checkEmail(String email) {
		//v4.53,調整正規表示法允許「--」
		String regx = ("^[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*\\@[A-Za-z0-9-]+((\\.)[A-Za-z0-9-]+)*\\.[A-Za-z0-9-]+$");
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(email);
		if (!m.find()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 檢查多個MAIL格式的正確
	 * checkMutlEmail split:',' or ';'
	 * @param email - MAIL字串
	 * @return boolean - ture:MAIL格式正確, false:MAIL格式不正確
	 */
	public static boolean checkMutlEmail(String email) {
		if (!StringUtil.isBlank(email)) {
			String[] mails;
			if (email.indexOf(";") > 0) {
				mails = StringUtil.getMutilMailList(email, ';');
			} else if (email.indexOf(",") > 0) {
				mails = StringUtil.getMutilMailList(email, ',');
			} else {
				mails = new String[1];
				mails[0] = email;
			}
			for (int h = 0; h < mails.length; h++) {
				if (!StringUtil.checkEmail(mails[h])) {
					return false;
				}// end if
			}// end for
		}// end if
		return true;
	}

	/**
	 * 將多MAIL字串(以',' or ';'分割)分割成MAIL字串陣列
	 * @param themail - 多MAIL字串
	 * @return String[] - MAIL字串陣列
	 */
	public static String[] getMutilMailList(String themail, char split) {
		ArrayList matchList = new ArrayList();
		int startPos = 0;
		int endPos = 0;
		byte[] temp2 = null;
		byte[] mailchars = themail.getBytes();
		for (int k = 0; k < mailchars.length; k++) {
			if (mailchars[k] == split) {
				endPos = k;
				temp2 = new byte[endPos - startPos];
				int p = 0;
				for (int h = startPos; h < endPos; h++) {
					temp2[p] = mailchars[h];
					p++;
				}
				matchList.add(new String(temp2));
				startPos = endPos + 1;
			}
		}
		endPos = mailchars.length;
		temp2 = new byte[endPos - startPos];
		int p = 0;
		for (int h = startPos; h < endPos; h++) {
			temp2[p] = mailchars[h];
			p++;
		}
		matchList.add(new String(temp2));
		String[] mails = new String[0];
		mails = (String[]) matchList.toArray(mails);
		return mails;
	}

	/**
	 * 檢查是否為中文字串
	 * @param datavalue - 欲判斷是否為中文的字串
	 * @return boolean - true:為中文, false:非中文
	 */
	public static boolean isAllChinese(String datavalue) {
		boolean result = true;
		String data = datavalue;
		int len = data.length();
		String temp = "";
		int bytelen = 0;
		for (int i = 1; i < len + 1; i++) {
			temp = data.substring(i - 1, i);
			try {
				bytelen = temp.getBytes("UTF-8").length;
			} catch (UnsupportedEncodingException e) {
				LOG.error(e.getMessage());
			}// end try
			if (bytelen >= 2) {
				result = true;
			} else {
				result = false;
				break;
			}// end else
		}// end for i
		return result;
	}
	
	/**
	 * 半形字轉成全形字
	 * 轉換規則:
	 *   1. 中文字不做轉換; 若為難字, 則會轉成半形問號(?)
	 *   2. 半形空白轉成全形空白
	 *   3. 英文字母, 數字及符號(!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~)由半形轉為全形, 範圍:ASCII碼33~126
	 *   4. 其餘字元(ex.控制字元), 一律轉成全形空白 
	 * @param s - 半形字
	 * @return String - 全形字
	 */
	public static String toChanisesFullChar(String s) {
	    String[] asciiTable = { "!", "\"", "#", "$", "%", "&", "\'", "(", ")",
	            "*", "+", ",", "-", ".", "/", "0", "1", "2", "3", "4", "5",
	            "6", "7", "8", "9", ":", ";", "<", "=", ">", "?", "@", "A",
	            "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
	            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
	            "Z", "[", "\\", "]", "^", "_", "`", "a", "b", "c", "d", "e",
	            "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
	            "r", "s", "t", "u", "v", "w", "x", "y", "z", "{", "|", "}", "~" };
	    String[] big5Table = { "！", "”", "＃", "＄", "％", "＆", "’", "（", "）",
	            "＊", "＋", "，", "－", "。", "／", "０", "１", "２", "３", "４", "５",
	            "６", "７", "８", "９", "：", "；", "＜", "＝", "＞", "？", "＠", "Ａ",
	            "Ｂ", "Ｃ", "Ｄ", "Ｅ", "Ｆ", "Ｇ", "Ｈ", "Ｉ", "Ｊ", "Ｋ", "Ｌ", "Ｍ",
	            "Ｎ", "Ｏ", "Ｐ", "Ｑ", "Ｒ", "Ｓ", "Ｔ", "Ｕ", "Ｖ", "Ｗ", "Ｘ", "Ｙ",
	            "Ｚ", "〔", "＼", "〕", "︿", "﹍", "‵", "ａ", "ｂ", "ｃ", "ｄ", "ｅ",
	            "ｆ", "ｇ", "ｈ", "ｉ", "ｊ", "ｋ", "ｌ", "ｍ", "ｎ", "ｏ", "ｐ", "ｑ",
	            "ｒ", "ｓ", "ｔ", "ｕ", "ｖ", "ｗ", "ｘ", "ｙ", "ｚ", "｛", "｜", "｝", "～" };
	 
        if (s == null || "".equalsIgnoreCase(s)) {
            return "";
        }
 
        char[] ca = s.toCharArray();

		for (int i = 0; i < ca.length; i++) {
			
			if (ca[i] > '\200') { // 中文字
				continue;
			} else if (ca[i] == 32) { // 半形空白轉成全型空白
				ca[i] = (char) 12288;
				continue;
			} else if (ca[i] >= 33 || ca[i] <= 126){ // 英文字母, 數字及符號
			
	            String caStr = String.valueOf(ca[i]);
	            for (int j = 0; j < asciiTable.length; j++) {
	                if (caStr.equals(asciiTable[j])) {
	                    ca[i] = big5Table[j].charAt(0);
	                    break;
	                }
	            }
			}else{
	            //其餘轉成全型空白
	            ca[i] = (char) 12288;
			}
        }
  
		return String.valueOf(ca);
 
//		if (s == null || s.equals("")) {
//			return "";
//		}
//		
//		char[] ca = s.toCharArray();
//		
//		for (int i = 0; i < ca.length; i++) {
//			if (ca[i] > '\200') {
//				continue;
//			} // 超過這個應該都是中文字了…
//			if (ca[i] == 32) {
//				ca[i] = (char) 12288;
//				continue;
//			} // 半型空白轉成全型空白
//			if (Character.isLetterOrDigit(ca[i])) {
//				ca[i] = (char) (ca[i] + 65248);
//				continue;
//			} // 是有定義的字、數字及符號
//			if (ca[i] == 63) {
//				ca[i] = (char) (ca[i] + 65248);
//				continue;
//			}
//			
//			ca[i] = (char) 12288; // 其它不合要求的，全部轉成全型空白。
//		}
//
//		return String.valueOf(ca);
	}

	/**
	 * 小寫轉大寫
	 * @param s - 欲轉換字串
	 * @return String - 大寫字串
	 */
	public static String toAllEnUpperCase(String s) {
		String result=null;
	    StringBuffer buffer = new StringBuffer();
			if (s != null){
			for (int i = 0; i < s.length(); i++) {
				char ch = s.charAt(i);

				if ((ch >= 'a' && ch <= 'z')||(ch >= 'A' && ch <= 'Z')){
					//System.out.print(ch);
					buffer.append(ch);
				}
			 }
			result=buffer.toString().toUpperCase();
		}
		return result;
	}
	
	/**
	 * 檢核字串是否包含Framework限制字元
	 * 1. Framework 限制字元: #$%^*'"\, TAB(\t)
	 * 2. 非外幣/薪轉使用
	 * @param input - 檢核字串
	 * @return boolean - true:不含Framework限制字元, false:含Framework限制字元
	 */
	public static boolean validateFrameWorkChar(String input){//v1.23
		//v4.54,增加台幣/外幣/薪轉:檢核字串是否含Framework限制字元TAB(\t)
		String REGEX = "[^#$%^*\'\"\\\\\t]*";//FrameWork限制的字符(臺幣)
		if (input == null || input.length() == 0) return true;
		Pattern mask = Pattern.compile(REGEX);
		Matcher matcher = mask.matcher(input);
		if(!matcher.matches()){
			return false;
		}
		return true;
	}
	
	/**
	 * 檢核字串是否包含Framework限制字元
	 * 1. Framework 限制字元: #$%^*"\, TAB(\t)
	 * 2. 外幣專用
	 * @param input - 檢核字串
	 * @return boolean - true:不含Framework限制字元, false:含Framework限制字元
	 */
	public static boolean validateFrameWorkCharFrgn(String input){//v1.23
		//v4.54,增加台幣/外幣/薪轉:檢核字串是否含Framework限制字元TAB(\t)
		String REGEX = "[^#$%^*\"\\\\\t]*";//FrameWork限制的字符(外幣)
		if (input == null || input.length() == 0) return true;
		Pattern mask = Pattern.compile(REGEX);
		Matcher matcher = mask.matcher(input);
		if(!matcher.matches()){
			return false;
		}
		return true;
	}
	
	/**
	 * 檢核字串是否含Framework限制字元
	 * 1. Framework 限制字元: #$%^'"\, TAB(\t)
	 * 2. 薪轉專用
	 * @param input - 檢核字串
	 * @return boolean - true:不含Framework限制字元, false:含Framework限制字元
	 * @since v4.51
	 */
	public static boolean validateFrameWorkCharSlry(String input){
		//v4.54,增加台幣/外幣/薪轉:檢核字串是否含Framework限制字元TAB(\t)
		String REGEX = "[^#$%^\'\"\\\\\t]*";//FrameWork限制的字符(薪轉)
		if (input == null || input.length() == 0) return true;
		Pattern mask = Pattern.compile(REGEX);
		Matcher matcher = mask.matcher(input);
		if(!matcher.matches()){
			return false;
		}
		return true;
	}
	
	/**
	 * 外幣受款帳號檢核
	 * 1. 檢核規則: 僅可輸入英數字、「.」、「-」、「/」、「中間可空白」
	 * 2. 外幣專用
	 * @param input - 檢核字串
	 * @return boolean - true:無不合法字元, false:含不合法字元
	 * @since v4.52
	 * v6.03 增加允許輸入空白, 但前後不可以為空白
	 */
	public static boolean validateFrgnApntAcnt(String input) throws Exception{
		//String REGEX = "[A-Za-z0-9/.-]*"; //v6.03
		
		//v6.03, 允許輸入空白(\\s放最後面錯出現exception), 但前後不可以為空白
		//說明：因最前和最後都不可以是空白，因此加上第一位和最後一位，一定會符合英數字、「.」、「-」、「/」至少有一次
		//^[A-Za-z0-9/.-]+   : 符合英數字、「.」、「-」、「/」，至少有一次(一次至多次)
		//[\\sA-Za-z0-9/.-]* : 符合英數字、「.」、「-」、「/」、「中間可空白」，可零次至多次
		//[A-Za-z0-9/.-]+$   : 符合英數字、「.」、「-」、「/」，至少有一次(一次至多次)
		String REGEX = "^[A-Za-z0-9/.-]+[\\sA-Za-z0-9/.-]*[A-Za-z0-9/.-]+$";
		if (input == null || input.length() == 0) return true;
		Pattern mask = Pattern.compile(REGEX);
		Matcher matcher = mask.matcher(input);
		if(!matcher.matches()){
			return false;
		}
		return true;
	}
	
	/**
	 * 檢核字串是否含Framework限制字元, 且需為半型字串
	 * 1. Framework 限制字元: #$%^*'"\, TAB(\t)
	 * 2. 非外幣/薪轉使用
	 * @param input - 檢核字串
	 * @return boolean - true:不含Framework限制字元且為半型字串, false:含Framework限制字元或非半型字串
	 */
	public static boolean validateAllHalfChar(String input){//v1.23
		return validateFrameWorkChar(input) && validateHalfChar(input);
	}
	/**
	 * 檢核字串是否含Framework限制字元, 且需為半型字串
	 * 1. Framework 限制字元: #$%^*"\, TAB(\t)
	 * 2. 外幣專用
	 * @param input - 檢核字串
	 * @return boolean - true:不含Framework限制字元且為半型字串, false:含Framework限制字元或非半型字串
	 */
	public static boolean validateAllHalfCharFrgn(String input){//v1.23
		return validateFrameWorkCharFrgn(input) && validateHalfChar(input);
	}	
	
	/**
	 * 檢核字串是否含Framework限制字元, 且需為半型字串
	 * 1. Framework 限制字元: #$%^'"\, TAB(\t)
	 * 2. 薪轉專用
	 * @param input - 檢核字串
	 * @return boolean - true:不含Framework限制字元且為半型字串, false:含Framework限制字元或非半型字串
	 * @since v4.51
	 */
	public static boolean validateAllHalfCharSlry(String input){
		return validateFrameWorkCharSlry(input) && validateHalfChar(input);
	}	
	
	/**
	 * 檢核字串是否為半型字串
	 * @param input - 檢核字串
	 * @return boolean - true:半型字串, false:非半型字串
	 */
	private static boolean validateHalfChar(String input){//v1.23
		if (input == null || input.length() == 0) return true;
		int len = input.length();
		String temp = "";
		int bytelen = 0;
		for (int i = 1; i < len + 1; i++) {
			temp = input.substring(i - 1, i);
			try {
				bytelen = temp.getBytes("UTF-8").length;
			} catch (UnsupportedEncodingException e) {
				LOG.error(e.getMessage());
			}// end try
			if (bytelen >= 2) {
				return false;
			} 
		}// end for i
		return true;
	}
	
	/**
	 * 檢核字串是否包含不合法字元
	 * 1. 合法字元: A-Z, a-z, 0-9, /?:().,+{}-
	 * @param input - 檢核字串
	 * @return boolean - true:無不合法字元, false:含不合法字元
	 */
	public static boolean validateAll(String input){
		String REGEX = "[A-Za-z0-9 /?:().,+{}-]*";
		if (input == null || input.length() == 0) return true;
		Pattern mask = Pattern.compile(REGEX);
		Matcher matcher = mask.matcher(input);
		if(!matcher.matches()){
			return false;
		}
		return true;
	}
	/**
	 * 檢核字串是否含外幣限制字元, 且需為半型字串
	 * 1. 外幣限制字元: ~=!"%&*<>;@#$[]\_^`|
	 * 2. 外幣專用
	 * @param input - 檢核字串
	 * @return boolean - true:不含限制字元且為半型字串, false:含限制字元或非為半型字串
	 * v4.57 自行受款人戶名地址開放可輸入全形，故多加控制檢核半形
	 */
	public static boolean validateAllFrgn(String input){
		return validateAllFrgn(input,true);
	}	
	
	/**
	 * 檢核字串是否含外幣限制字元
	 * 1. 外幣限制字元: ~=!"%&*<>;@#$[]\_^`|
	 * 2. 外幣專用
	 * @param input - 檢核字串
	 * @param isHalf - 是否檢核半型,true:檢核須為半形, false:不檢核半形，表示可為全形
	 * @return boolean - true:不含限制字元, false:含限制字元
	 * v4.57 自行受款人戶名地址開放可輸入全形，故多加控制檢核半形
	 * v6.00 調整限制字元: ~=!"%&*<>;@#$[]\_^`|
	 */
	public static boolean validateAllFrgn(String input,boolean isHalf){
		//v1.26 調整檢核規則, 僅允許輸入半形字,v4.57 多加控制檢核半形
		if(isHalf && !validateHalfChar(input))
			return false;
		
		//String REGEX = "[A-Za-z0-9 /?:().,+{}-[']]*";	//v1.25 調整檢核規則(mark)
		//String REGEX = "[&><\"]+";	//v1.25 調整檢核規則(add)
		String REGEX = "[~=!\"%&*<>;@#$\\[\\]\\\\_^`|]+";    //6.00 調整限制字元規則

		if (input == null || input.length() == 0) return true;
		Pattern mask = Pattern.compile(REGEX);
		Matcher matcher = mask.matcher(input);
		//if(!matcher.matches()){	//v1.25 調整檢核規則(mark)
		if(matcher.find()){	//v1.25 調整檢核規則(add)
			return false;
		}
		return true;
	}
	
	/**
	 * 檢核字串第一碼是否含限制字元, 且需為半型字串
	 * 1. 第一碼限制字元: -/:~=!"%&*<>;@#$[]\_^`|
	 * 2. 外幣專用
	 * @param input - 檢核字串
	 * @return boolean - true:不含限制字元開頭且為半型字串, false:含限制字元開頭或非為半型字串
	 * v4.57 自行受款人戶名地址開放可輸入全形，故多加控制檢核半形
	 * v6.02 調整第一碼可接受「/」
	 */
	public static boolean validateOne(String input){
		return validateOne(input,true);
	}

	/**
	 * 檢核字串第一碼是否含限制字元
	 * 1. 第一碼限制字元: -/:~=!"%&*<>;@#$[]\_^`|
	 * 2. 外幣專用
	 * @param input - 檢核字串
	 * @param isHalf - 是否檢核半型,true:檢核須為半形, false:不檢核半形，表示可為全形
	 * @return boolean - true:不含限制字元開頭, false:含限制字元開頭
	 * v4.57 自行受款人戶名地址開放可輸入全形，故多加控制檢核半形
	 * v6.02 調整第一碼可接受「/」
	 */
	public static boolean validateOne(String input,boolean isHalf){
		// v1.25 調整檢核規則(mark)
		/*
		String REGEX = "[A-Za-z0-9 ]{1}"; 
		Pattern mask = Pattern.compile(REGEX);
		String temp = null; 
		if (input==null || input.length() == 0) return true;
		temp = input.substring(0 , 1); 
		Matcher matcher = mask.matcher(temp); 
		if(!matcher.matches()){ 
			return false;
		}
		*/
		//v1.26 調整檢核規則, 僅允許輸入半形字,v4.57 多加控制檢核半形
		if(isHalf && !validateHalfChar(input))
			return false;

		// v1.25 調整檢核規則(add)
		//String REGEX = "^:|^-|^>|^&|^<|^\"+";
		String REGEX = "^[-:~=!\"%&*<>;@#$\\[\\]\\\\_^`|]+";  //v6.00 調整限制字元規則
		
		Pattern mask = Pattern.compile(REGEX);
		if (input==null || input.length() == 0) return true;
		Matcher matcher = mask.matcher(input); 
		if(matcher.find()){ 
			return false;
		}
		// (end)
		
		return true;
	}
	
	/**
	 * 檢核字串是否為英數字(A-Z,a-z,0-9)
	 * @param input - 檢核字串
	 * @return boolean - true:英數字, false:非英數字
	 */
	public static boolean validateAlpha(String input){
		String REGEX = "[A-Za-z0-9]*";
		if (input == null || input.length() == 0) return true;
		Pattern mask = Pattern.compile(REGEX);
		Matcher matcher = mask.matcher(input);
		if(!matcher.matches()){
			return false;
		}
		return true;
	}
	
	/**
	 * 檢核字串是否為純數字(A-Z,a-z,0-9)
	 * @param input - 檢核字串
	 * @return boolean - true:純數字, false:非純數字
	 */
	public static boolean isNumber(String input) {
		String reg = "0123456789";
		if (input!=null && input.length() > 0) {
			char[] inputchars=input.toCharArray();			
			int len = inputchars.length;
			for (int i = 0; i < len ; i++) {
				if (reg.indexOf(inputchars[i]) <= -1) {
					return false;
				}
			}
		}
		return true;	
	}
	
    /**
     * 依據指定Token, 將字串拆解為字串陣列
     * @param input - 欲拆解字串
     * @param token - 指定Token
     * @return String[] - 拆解後的字串陣列
     * @throws Exception
     */
    static public String[] ParseStr(String input,String token) throws Exception{
    	int num = 0;
    	String[] strs = null;
    	try{
	    	StringTokenizer st = new StringTokenizer(input, token);
	    	 
	    	//check last character
	    	if(input.lastIndexOf(token) == input.length()-1) 
	    		num = st.countTokens();
	    	else
	    		num = st.countTokens() - 1;
	    	
	        strs = new String[num+1];
	    	int flag = 0;
	    	while(st.hasMoreTokens()){
	    		strs[flag] = st.nextToken();
	    		flag++;
	    	}
	 	}catch(Exception ex){
	 		throw ex;	
	 	}
		return strs;
    }

    /**
     * 取得字串前12Bytes
     * @param input - 原始字串
     * @return String - 原始字串的前12Bytes
     */
    public static String getStringH12Bytes(String input) {
    	if (input==null) {
    		return "";
    	}
        byte[] temp = input.getBytes();
        if(temp.length > 12) { //超過12Bytes, 取前12Bytes
            return new String(temp,0,12);
        }
        //未超過12Bytes, 則直接回傳原始字串
        return input;
    }

    /**
     * 計算原始字串中含指定token的數量
     * @param input - 原始字串
     * @param token - 指定token
     * @return int - 含指定token的數量
     */
    public static int countCharacter(String input,String token) {
        int result = 0;
        int trace = -1;
        while((trace=input.indexOf(token,trace + 1)) != -1) {
            result++;
        }
        
        return result;
    }
    
    /**
     * 資料遮罩處理(for個資條例)
     * @method toMask()
     * @param strring data 原始資料
     * @param string direction 起迄位置方向(L:left左邊起算, R:right右邊起算(倒數))
     * @param int start 起始位置
     * @param int end 結束位置
     * @param String 替換符號 symbol
     * @return String
     * @since v6.01
     */ 
     public static String toMask(String data, String direction, int start, int end, String symbol) {
         String sResult = data;
         int iLen = sResult.length();
         int iStart = start;
         int iEnd = end;
         //檢核原始資料長度 及 起迄位置方向是否合理
         if(iLen==0 || direction.length()!=1) return sResult;
         //檢核起始位置必須<=結束位置
         if(iStart>iEnd) return sResult;
         //檢核替換符號不得為空值
         if(symbol.length()==0) return sResult;
         //若起迄位置方向為R:右邊起算(倒數), 則重新定義起迄位置
         if(direction.equalsIgnoreCase("R")){ 
             iStart = iLen - end;
             iEnd = iLen - start + 1;
         }else if(direction.equalsIgnoreCase("L")) {
             iStart = start - 1;
         }else{
        	 return sResult;
         }
         
         sResult=data.substring(0, iStart);  
         for(int i=iStart; i<iEnd; i++){
             //若為全型字, 則轉換為全型替換符號
             if(data.substring(i,i+1).getBytes().length==2){
                 sResult+=StringUtil.toChanisesFullChar(symbol);
             }else{
                 sResult+=symbol;
             }
         }
         sResult+=data.substring(iEnd);

         return sResult;
     }   

	/**
	 * 外幣受款人帳號帳號，去除「.」、「-」、「/」、[空白] 
	 * 1. 處理規則: 去除「.」、「-」、「/」、[空白] 
	 * 2. 外幣專用
	 * @param input - 檢核字串
	 * @return String - 置換後的帳號
	 * @since v6.03
	 */
	public static String replaceFrgnApntAcnt(String input) throws Exception{
		String REGEX = "[\\s/.-]";
		if (input == null || input.length() == 0) return "";
		Pattern mask = Pattern.compile(REGEX);
		Matcher matcher = mask.matcher(input);
		return matcher.replaceAll("");
	}
 	
}