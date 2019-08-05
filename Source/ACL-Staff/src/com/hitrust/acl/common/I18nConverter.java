/**
 * @(#) ConverMessageUtil.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/02/01, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.acl.common;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import com.hitrust.acl.util.StringUtil;
import com.hitrust.framework.APSystem;

public class I18nConverter {
	
	/**
	 * i18n Mapping
	 * @param key	 i18n key 值
	 * @param locale Locale 物件
	 * @return  回傳 Mapping 後的訊息
	 * @throws NoSuchMessageException
	 */
	public static String i18nMapping(String key, Locale locale) throws NoSuchMessageException {
		
		MessageSource source = null;
		String msg = ""; // 回傳 Mapping 後的訊息
		
		String i18nNoMap = AppEnv.getString("I18N_NOMAP");		// 預設訊息
		String showNoMap = AppEnv.getString("I18N_NOMAP_SHOW"); // 是否顯示錯誤代碼
		
		try {
			// 從 spring pool 中取得 messageSource
			source = (MessageSource) APSystem.getApplicationContext().getBean("messageSource");
		
			// ====================
			// = 訊息未定義是否顯示 errorCode
			// = 0: 不顯示	eg 訊息未定義
			// = 1: 顯示	eg 訊息未定義(xxx.xxx)
			// ====================
			if ("0".equals(showNoMap)) {
				
				try {
					
					msg = source.getMessage(key, null, locale);
					
				} catch (NoSuchMessageException e) {
					// 是否顯示預設訊息
					if (!StringUtil.isBlank(i18nNoMap)) {
						msg = source.getMessage(i18nNoMap, null, locale);
					} else {
						throw e;
					}
				}
				
			} else {
				try {
					
					msg = source.getMessage(key, null, locale);
					
				} catch (NoSuchMessageException e) {
					// 是否顯示預設訊息
					if (!StringUtil.isBlank(i18nNoMap)) {
						msg = source.getMessage(i18nNoMap, null, locale);
					} else {
						throw e;
					}
					// 組合錯誤代碼
					msg.concat("(");
					msg.concat(key);
					msg.concat(")");
				}
				
			}
			
		} catch (NoSuchMessageException e) {
			throw e;
		}
		
		return msg;
	}

}
