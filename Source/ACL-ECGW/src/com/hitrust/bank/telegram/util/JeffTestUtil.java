
package com.hitrust.bank.telegram.util;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.hitrust.acl.util.MAC;
import com.hitrust.bank.common.CommonUtil;
import com.hitrust.framework.util.DateUtil;


public class JeffTestUtil {
	public static void main(String[] args) {
		try {
			System.out.println(DateUtil.getCurrentTime("DT","AD"));

			System.out.println(new Random().nextInt());
			
			String[] a = {"20","21","50","51","52","53","54","55","56","57","68","69"};
			System.out.println(Arrays.asList(a).contains("22"));
			
			
			System.out.println(MAC.encryptePswd("zaq12wsx"));
			System.out.println(MAC.decryptePswd("2A87D7015B014B87A2D83D39A45CF4AF"));
			
			System.out.println("123456789".substring(3, 5));
			
			
			String e = "中文字";
		    
		    
		    String e5 = new String(e.getBytes("BIG5"),"UTF-8");
		    String e8 = new String(e.getBytes("UTF-8"),"BIG5");
		    
		    System.out.println(e5 + e8);
		    
		    
		    String today = DateUtil.getCurrentTime("DT", "AD");
			String yesterday = DateUtil.countDate(today, -1);
		    System.out.println(today);
		    System.out.println(yesterday);
		    
		    System.out.println(URLDecoder.decode("中文字"));
		} catch (Exception e) {

		}
	}
}