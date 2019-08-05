/**
 * @(#) DailyBindingRptSrvImpl.java
 * 
 * Copyright (c) 2019 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2019/06/12
 * 
 */
package com.hitrust.acl.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hitrust.acl.dao.CustAcntLogDAO;
import com.hitrust.acl.service.DailyBindingRptSrv;


public class DailyBindingRptSrvImpl implements DailyBindingRptSrv {
	static Logger LOG = Logger.getLogger(DailyBindingRptSrvImpl.class);
	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private CustAcntLogDAO custAcntLogDAO;

	
	public void setCustAcntLogDAO(CustAcntLogDAO custAcntLogDAO) {
		this.custAcntLogDAO = custAcntLogDAO;
	}
	
	@Override
	public List<Map<String, Object>> getCustAcntLog(String today) {
		return custAcntLogDAO.getCustAcntLog(today);
	}
	
	@Override
	public String generateBindingRpt(String today) {

		String result=null;
        
        StringBuffer bftowrite= new StringBuffer();
        
        String  EC_ID="";
        String  CUST_ID="";
        String  STATUS="";
        String  REAL_ACNT="";
        String  CRET_DTTM="";    
        
        List<Map<String, Object>> resultList = getCustAcntLog(today);
        
        for (Map<String, Object> m : resultList)  
        {  
          //  LOG.info( resultList+ " : " + resultList);
          //  LOG.info(m + " : " + m);
          //LOG.info("======================================================");	
          for (String k : m.keySet()) 
          //set是一個集合，keyset（）返回的就是一個set集合比如map裡面的鍵值對是這樣的<1,one>,<2,two><3,three><4,four><5,five><6,six>那麼keyset（）函數就是把1,2,3,4,5,6放到一個set集合裡面
          {  
            //LOG.info(k + " : " + m.get(k)); 
            if("EC_ID".equals(k))     EC_ID=((String)m.get(k)).trim();
            if("CUST_ID".equals(k))   CUST_ID=((String)m.get(k)).trim();
            if("STATUS".equals(k))    STATUS=((String)m.get(k)).trim();
            if("REAL_ACNT".equals(k)) REAL_ACNT=((String)m.get(k)).trim();
            if("CRET_DTTM".equals(k)) CRET_DTTM=timestamp2Str((Timestamp) m.get(k));
            
				if (CUST_ID != null && CUST_ID.length() == 10) {
					CUST_ID = CUST_ID + "0";
				}

				if (REAL_ACNT != null && REAL_ACNT.length() == 11) {
					REAL_ACNT = REAL_ACNT + "   ";
				} else if (REAL_ACNT != null && REAL_ACNT.length() < 14) {
					for (int j = 0; j < 14 - REAL_ACNT.length(); j++) {
						REAL_ACNT = REAL_ACNT + " ";
					}

				}

				if (CRET_DTTM != null && CRET_DTTM.length() >= 19) {
					CRET_DTTM = CRET_DTTM.substring(0, 19);
				}
            
          }  
          bftowrite.append(EC_ID).append(";").append(CUST_ID).append(";").append(STATUS).append(";").append(REAL_ACNT).append(";").append(CRET_DTTM).append(";\r\n");
     
        }  
        
        LOG.info("檔案共有 ["+resultList.size()+"]筆資料.");
        
        result=(bftowrite.length()>0)?bftowrite.toString():null;
        
        return result;
	}
	

	 /** 日期轉換為字串
	  * @param date 日期
	  * @param format 日期格式
	  * @return 字串
	  */
	 public static String date2Str(Date date, String format) {
	  if (null == date) {
	   return null;
	  }
	  SimpleDateFormat sdf = new SimpleDateFormat(format);
	  return sdf.format(date);
	 }
	/**
	  * 時間戳記轉換為字串
	  * @param time
	  * @return
	  */
	 public static String timestamp2Str(Timestamp time) {
	  Date date = null;
	  if(null != time){
	   date = new Date(time.getTime());
	  }
	  return date2Str(date, DEFAULT_FORMAT);
	 }


}
