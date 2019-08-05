/**
 * @(#) AppEnv.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/01/26, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.acl.common;

import java.io.File;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class AppEnv {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(AppEnv.class);
	
	// Global public variables
	public static final String PROPERTY_FILE = "acl"; 			   // Application default property file 
	public static final String CONFIG_DIR 	 = "config.directory"; // 設定檔目錄
	
	private static ResourceBundle bundle;
	
	static {
		
		try {
			// 取得預設 Property file
			bundle = ResourceBundle.getBundle(PROPERTY_FILE);
			
		} catch (Exception e) {
			LOG.error("初始 AppEnv 失敗 ", e);
			throw new RuntimeException("Not load config file!! ");
		}
	}
	
	/**
	 * 依據 Property key 取得對應參數值
	 * @param key	參數名稱
	 * @return 回傳 key 對應的值
	 */
	public static String getString(String key) {
		return bundle.getString(key);
	}
	
	/**
	 * 取得系統設定檔路徑
	 * @return
	 */
	public static String getConfigDir() {
		
		String value = getString(CONFIG_DIR);
		
		if (!value.endsWith(File.separator)) {
			value.concat(File.separator);
		}
		
		return value;
	}
}
