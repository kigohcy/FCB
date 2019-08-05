/**
 * @(#) Validation.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/04/01, Eason Hsu
 *    1) JIRA-Number, First release
 *   v1.01, 2017/01/04, Yann
 *    1) TSBACL-145, validation.xml 增加 mask參數
 *   
 */
package com.hitrust.acl.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import com.hitrust.acl.APSystem;
import com.hitrust.acl.util.StringUtil;

public class Validation {

	// Log4j
	private static Logger LOG = Logger.getLogger(Validation.class);
	
	/**
	 * 
	 * @param url
	 * @param validateFieldMap
	 * @param req
	 * @return
	 */
	public static String validationReqField(String url, Map<String, Map<String, String>> validateFieldMap, HttpServletRequest req) {
		
		LOG.info("========== Vaildate Request Field Begin ==========");
		String rtnMsg = ""; // 回傳訊息
		
		String enCoding = APSystem.REQUEST_ENCODING; // 參數值編碼
		String paramName = "";    // 參數名稱
		String paramValue = "";   // 參數值
		String type = "";		  // 檢核類別
		String length = ""; 	  // 檢核參數值長度
		String isNull = "";		  // 參數值是否允許 null N: 不允許, Y: 允許
		String mask   = "";       // v1.01, 印出之Log是否需遮蔽, Y:印出之Log固定為****
		boolean maskFlag = false; //v1.01
		String decode="";
		
		Enumeration<String> names = req.getParameterNames();
		
		// 無請求參數
		if (StringUtil.isBlank(names)) {
			LOG.error(StringEscapeUtils.unescapeHtml(StringEscapeUtils.escapeHtml("URL no parameter:" + " [URL] " + url)));
			return APSystem.SERVICE_UNAVAILABLE;
		}
		
		// TODO 測試用(Eason Hsu)
//		LOG.info("[URL:" + url + "]");
//		if("/IBankLoginCheck".equals(url)) {
//			LOG.info("========== Vaildate Request Field End URL = "+url+"==========");
//			return rtnMsg;
//		}
		
		while (names.hasMoreElements()) {
			paramName = names.nextElement();
			// 依據欄位名稱取得對應檢核規則
			Map<String, String> checkElements = validateFieldMap.get(paramName);
			if (!StringUtil.isBlank(checkElements)) {
				decode = checkElements.get("decode");
			}
						
			if(StringUtil.isBlank(decode)){
				paramValue = req.getParameter(paramName); //沒設decode
			}else{
				try{
					if(org.apache.commons.lang.StringUtils.equalsIgnoreCase(paramValue, "Y")){ //decode="Y" or decode="y"
						paramValue = URLDecoder.decode(req.getParameter(paramName));
					}else{
						//decode="N" 
						paramValue = req.getParameter(paramName);
					}
				}catch(Exception e){
					LOG.error("decode Exception",e);
				}
			}
			
			// TODO 測試用(Eason Hsu)
			//LOG.info("[Parameter:" + paramName + ", Value:" + paramValue +  "]");
			
			//一銀網銀登入導頁不檢查
			if(!"/IBankLoginCheck".equals(url)) {
				// 檢核參數值是否含 XSS 攻擊字元
				if (StringUtil.validateInputForXSS(paramValue)) {
					LOG.error("輸入或上傳的資料內容, 有疑似 XSS 攻擊字元: [URL] " + url + " [parameter] " + paramName + " [value] " + paramValue);
					return "9000";
				}
			}
			
			
			// 依據欄位名稱取得對應檢核規則
//			Map<String, String> checkElements = validateFieldMap.get(paramName);

			// 檢核參數名稱是否定義在 validation.xml
			if (!StringUtil.isBlank(checkElements)) {
				type   = checkElements.get("type");
				length = checkElements.get("length");
				isNull = checkElements.get("isNull");
				mask   = checkElements.get("mask"); //v1.01
				
				//v1.01
				if(!StringUtil.isBlank(mask) && "Y".equals(mask) && !StringUtil.isBlank(paramValue)){
					maskFlag = true;
				}else{
					maskFlag = false;
				}
				
				// 欄位值不等於 null 才進行檢核
				if ("N".equals(isNull) && !StringUtil.isBlank(paramValue)) {
					// 依據檢核類別, 檢核欄位合理性
					if ("I".equals(type)) {
						// 檢核是否為純數字
						rtnMsg = checkIsNumber(type, paramValue);
						if (!StringUtil.isBlank(rtnMsg)) {
							//v1.01
							LOG.error(StringEscapeUtils.escapeHtml("Parameter isn't number: " + " [parameter] " + paramName + " [value] " + (maskFlag?"****":paramValue)));
							return rtnMsg;
						}
						
					}
					
					// 檢核參數值長度
					rtnMsg = checkParamLength(length, enCoding, paramValue);
					if (!StringUtil.isBlank(rtnMsg)) {
						//v1.01
						LOG.error(StringEscapeUtils.escapeHtml("Parameter length is error:" + " [parameter] " + paramName + " [value] " + (maskFlag?"****":paramValue)));
						return rtnMsg;
					}
					
				} else if ("N".equals(isNull) && StringUtil.isBlank(paramValue)) {
					LOG.error(StringEscapeUtils.escapeHtml("parameter is empty or null: " + " [parameter] " + paramName));
					return APSystem.SERVICE_UNAVAILABLE;
				}
				
			} else {
				LOG.info(StringEscapeUtils.escapeHtml("parameter not define in validation.xml: " + " [parameter] " + paramName));
			}
		}
		LOG.info("========== Vaildate Request Field End ==========");

		return rtnMsg;
	}
	
	/**
	 * 檢核參數值長度
	 * @param length	 檢核長度
	 * @param enCoding	 編碼
	 * @param paramValue 參數值
	 * @return 檢核失敗回傳錯誤訊息, 檢核成功回傳空白字串 
	 */
	private static String checkParamLength(String length, String enCoding, String paramValue) {
		String rtnMsg = "";
		
		if (!StringUtil.isBlank(length) && !StringUtil.isBlank(paramValue)) {
			int checkLen = Integer.parseInt(length);
			
			try {
				
				if (paramValue.trim().getBytes(enCoding).length > checkLen) {
					rtnMsg = "9000"; //欄位檢核錯誤
				}
				
			} catch (UnsupportedEncodingException e) {
				LOG.error("[checkParamLength UnsupportedEncodingException]: ", e);
				rtnMsg = "9000";
			}
		}
		
		return rtnMsg;
	}
	
	/**
	 * 檢核參數值是否為純數字
	 * @param type		 檢核類別
	 * @param paramValue 參數值
	 * @return 檢核失敗回傳錯誤訊息, 檢核成功回傳空白字串
	 */
	private static String checkIsNumber(String type, String paramValue) {
		String rtnMsg = "";
		
		try {
			
			Integer.parseInt(paramValue);
			
		} catch (NumberFormatException e) {
			LOG.error("[checkIsNumber NumberFormatException]: ", e);
			rtnMsg = "9000";
		}
		
		return rtnMsg;
	}
}
