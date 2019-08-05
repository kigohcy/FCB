 /** @(#)ValidationFilter.java
  *
  * Copyright (c) 2013 HiTRUST Incorporated.All rights reserved.
  *
  * Description : 輸入檢核過濾器
  *
  * Modify History:
  *  v1.00, 2014/05/02, Yann
  *   1) First release
  *  v1.01, 2017/01/04, Yann
  *   1) TSBACL-145, validation.xml 增加 mask參數
  *  
  */
package com.hitrust.acl.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import com.hitrust.acl.common.ValidationEnv;
import com.hitrust.acl.util.StringUtil;

public class ValidateFilter implements Filter {
    
	private Logger LOG = Logger.getLogger(ValidateFilter.class);
	
    private FilterConfig config = null;
	private ServletContext application = null;
	private String VALID_URL = "/message/validateError.jsp";

	public void init(FilterConfig config) throws ServletException {
		this.config = config;
		this.application = config.getServletContext();
	}

	public void destroy() {
		config = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String url = req.getServletPath().replaceAll(".html", "");
		//LOG.debug("check url="+url);
		
		HashMap configMap = null;
		String type = "";		// 檢核類型 N: 純數字, D: 金額(含小數點), A: 英數字, 符號, E: 純英文
		String length = ""; 	// 欄位長度
		String isNull = "";		// 參數值是否可為 null Y: 可為 null 或空白, N: 不可為 null 或空白 
		String mask   = "";     // v1.01, 印出之Log是否需遮蔽, Y:印出之Log固定為****
		String paramName = "";	// 參數名稱
		String paramValue = ""; // 參數值
		
		boolean simpleXSS = false;
		boolean advancedXSS = false;
		boolean maskFlag = false; //v1.01
		
		Enumeration<String> parameterNames = req.getParameterNames();
		
		configMap = ValidationEnv.getUrlAttributes(url);
		
		if(configMap != null){
			if(!parameterNames.hasMoreElements()){
				LOG.info("url="+url);
				req.getSession().invalidate();
				this.application.getRequestDispatcher(VALID_URL).forward(req, res);
				return;
			}
		}
		
		while (parameterNames.hasMoreElements()) {
			paramName = parameterNames.nextElement();
			
			//LOG.debug("paramName="+paramName);
			configMap = ValidationEnv.getUrlAttributes(url + "/" + paramName);
			
			if(configMap != null){
				String[] paramValues = req.getParameterValues(paramName);
				type  = (String)configMap.get("type");
				length= (String)configMap.get("length");
				isNull= (String)configMap.get("isNull");
				mask  = (String)configMap.get("mask"); //v1.01
				
				for (int i = 0; i < paramValues.length; i++) {
					paramValue = paramValues[i];
					
					simpleXSS = this.validateInputForXSS(paramValue);
					
					advancedXSS = StringUtil.validateInputForXSS(paramValue);
					
					// 檢核是否含有疑似XSS攻擊字元
					if (simpleXSS || advancedXSS) {
						LOG.info("========== 輸入或上傳的資料內容, 有疑似 XSS 攻擊字元 [來源網址]: " + url + " [參數名稱]: " + paramName + " [參數值]: " + paramValue + "==========");
						req.getSession().invalidate();
						this.application.getRequestDispatcher(VALID_URL).forward(req, res);
						return;
						
					}
					
					//v1.01
					if(!StringUtil.isBlank(mask) && "Y".equals(mask) && !StringUtil.isBlank(paramValue)){
						maskFlag = true;
					}else{
						maskFlag = false;
					}
					
					//LOG.debug("paramValue="+paramValue);
					if(!StringUtil.isBlank(type) && !StringUtil.isBlank(paramValue)){
						
						// 依據檢核類型, 檢核參數值是否符合
						if("N".equals(type) && !isNumber(paramValue)){ //純數字
							//v1.01
							LOG.info("url="+url+";paramName="+paramName+";type="+type+";paramValue="+ (maskFlag?"****":paramValue));
							req.getSession().invalidate();
							this.application.getRequestDispatcher(VALID_URL).forward(req, res);
							return;
							
						}else if("D".equals(type) && !isAmount(paramValue)){ //金額(含小數點)
							//v1.01
							LOG.info("url="+url+";paramName="+paramName+";type="+type+";paramValue="+ (maskFlag?"****":paramValue));
							req.getSession().invalidate();
							this.application.getRequestDispatcher(VALID_URL).forward(req, res);
							return;
							
						}else if("A".equals(type) && !isPureAscii(paramValue)){ //英數字,符號
							//v1.01
							LOG.info("url="+url+";paramName="+paramName+";type="+type+";paramValue="+ (maskFlag?"****":paramValue));
							req.getSession().invalidate();
							this.application.getRequestDispatcher(VALID_URL).forward(req, res);
							return;
							
						}else if("E".equals(type) && !isEnglish(paramValue)){ //純英文
							//v1.01
							LOG.info("url="+url+";paramName="+paramName+";type="+type+";paramValue="+ (maskFlag?"****":paramValue));
							req.getSession().invalidate();
							this.application.getRequestDispatcher(VALID_URL).forward(req, res);
							return;
						}
					}
					
					// 檢核參數值長度
					if(!StringUtil.isBlank(length) && !StringUtil.isBlank(paramValue)){
						if(paramValue.trim().length() > Integer.parseInt(length)){
							//v1.01
							LOG.info("url="+url+";paramName="+paramName+";defLen="+length+";paramValue="+(maskFlag?"****":paramValue)+";valueLen="+paramValue.trim().length());
							req.getSession().invalidate();
							this.application.getRequestDispatcher(VALID_URL).forward(req, res);
							return;
						}
					}
					
					// 檢核參數值是否為空白或 null 
					if(!StringUtil.isBlank(isNull) && "N".equals(isNull)){
						if(paramValue==null || "".equals(paramValue.trim())){
							LOG.info("url="+url+";paramName="+paramName+" is null.");
							req.getSession().invalidate();
							this.application.getRequestDispatcher(VALID_URL).forward(req, res);
							return;
						}
					}
				}
				
			}else{
				LOG.info("[來源網址]: " + url + ", [參數名稱: " + paramName + "], 未定義在 Validate.xml");
			}
		}
		
		//move on to the next
		chain.doFilter(request, response);
	}
	
	/**
	 * 功能：是否為金額數字(含小數點)
	 * @param val
	 * @return
	 */
	private boolean isAmount(String val) {
		if (val==null || "".equals(val)) return false;
		try{
			Double.parseDouble(val);
			return true;
		}catch(Exception e){ }
		
		return false;
	}
	
	/**
	 * 功能：是否為0~9(純數字)
	 * @param val
	 * @return
	 */
	private boolean isNumber(String val) {
		if (val==null) return false;
		for(int i=0; i<val.length(); i++) {
			char ch = val.charAt(i);
			if (ch < '0' || ch > '9') {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 功能：判斷純英文字
	 * 
	 * @param val
	 * @return
	 */
	private boolean isEnglish(String val) {
		if (val==null) return false;
		for(int i=0; i<val.length(); i++) {
			char ch = val.charAt(i);
			if ((ch < 'a' || ch > 'z') && (ch < 'A' || ch > 'Z'))
				return false;
		}
		return true;
	}
	
	//英、數字、符號
	private boolean isPureAscii(String val){
		if (val==null) return false;
	    for(int i=0; i<val.length(); i++){
	        char ch = val.charAt(i);
	        if(ch>=127 || ch<0) return false;
	    }
	    return true;
	}
	
	private boolean validateInputForXSS(String input) {
		boolean result = true;
		
		if (StringUtil.isBlank(input) || input.length() == 0) {
			result = false;
		}
		
		String specialKey = "#$%^*'\"\t";
		
		for (int i = 0; i < input.length(); i++) {
			
			if (specialKey.indexOf(input.charAt(i)) >= 0) {
				break;
			} else {
				result = false;
			}
		}
		
		return result;
	}
}
