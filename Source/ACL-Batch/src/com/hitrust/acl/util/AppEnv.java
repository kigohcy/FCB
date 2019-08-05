/**
 * @(#)AppEnv.java
 *
 * Copyright(c)2007 HiTRUST Incorporated.All rights reserved.
 * 
 * Description :讀取properties文件工具類
 *
 * Modify History:
 *  v1.00, 2013/08/28, Woodman Fan
 *   1) First release
 */
package com.hitrust.acl.util;

import java.io.File;
import java.util.ResourceBundle;

import com.hitrust.framework.util.StringUtil;


public class AppEnv {
	private static ResourceBundle rb;
	
	static{
		try {
			 rb = ResourceBundle.getBundle("batch");
		} catch (Exception e) {
			System.out.println("Not load config file");
			throw new RuntimeException("Not load config file",e);

		}
	}
	
	public static String getConfig(String key){
		return rb.getString(key);
	}
	public static String getString(String key){
		return rb.getString(key);
	}
	
	public static int getInt(String key){
		String value=getString(key);
		if(StringUtil.isBlank(value)){
			return 0;
		}else{
			return Integer.parseInt(value);
		}
	}
	
	public static String getConfigDir(){
		String result=getString("config.directory");
		if(!result.endsWith(File.separator)) result+=File.separator;
		return result;
	}

}
