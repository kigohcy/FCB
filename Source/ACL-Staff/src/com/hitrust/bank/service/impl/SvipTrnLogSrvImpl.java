/**
 * @(#) SvipTrnLogSrvImpl.java
 *
 * Directions: 敏感客戶查詢LOG檔
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/06/24, Eason Hsu
 *    1) First release
 *   v1.01, 2016/12/22, Yann
 *    1) TSBACL-143, 未綁定解鎖
 *   
 */
package com.hitrust.bank.service.impl;

import org.apache.log4j.Logger;
import com.hitrust.acl.common.I18nConverter;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.CustDataDAO;
import com.hitrust.bank.dao.SvipTrnLogDAO;
import com.hitrust.bank.model.CustData;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.service.SvipTrnLogSrv;
import com.hitrust.framework.model.APLogin;

public class SvipTrnLogSrvImpl implements SvipTrnLogSrv {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(SvipTrnLogSrvImpl.class);
	
	// DAO injection
	private SvipTrnLogDAO svipTrnLogDAO;
	private CustDataDAO custDataDAO;

	/**
	 * 新增 敏感戶查詢記錄
	 * @param custId	 身分證字號
	 * @param condition1 查詢條件-身分證字號
	 * @param condition2 查詢條件-實體帳號
	 * @param funcId	 功能代碼
	 * @param reportId	 報表代碼
	 */
	@Override
	public void insertSvipTrnLog(String custId, String condition1, String condition2, String funcId, String reportId) {
		
		// 取得當前 User 資料
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		
		String currentTime = DateUtil.getCurrentTime("DT", "AD");
		
		String tarnDate = currentTime.substring(0, 8);
		String tarnTime = currentTime.substring(8);
		String aclink = "ACLINK";
		String userId = user.getUserId(); // 員工編號
		String clientIp = user.getIp();	  // 客戶端 IP
		String funcName = I18nConverter.i18nMapping("function.Id." + funcId, user.getLocale()); // 執行功能名稱
		String tranType = (StringUtil.isBlank(reportId) ? funcId : funcId + "-" + reportId);
		
		String memo2 = (StringUtil.isBlank(condition2) ? condition1 : condition1 + "," + condition2);
		String custName = ""; //v1.01
		
		CustData custData = (CustData) custDataDAO.queryById(CustData.class, custId);
		//v1.01
		if(custData!=null){
			custName = custData.getCustName();
		}
		//v1.01
		String[] params = {tarnDate, tarnTime, userId, aclink, aclink, custId, custName, tranType, clientIp, funcName, memo2}; 
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO SVIP_TRN_LOG (TRAN_DATE, TRAN_TIME, EMP_NO, SYSTEM_CODE, SYSTEM_NAME, CUST_ID, CUST_NAME, TRAN_TYPE, TRAN_SOURCE, MEMO1, MEMO2) ");
		sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		svipTrnLogDAO.excuteNativeUpdateSql(sb.toString(), params);
	}

	
	// =============== bean injection ==========
	public void setSvipTrnLogDAO(SvipTrnLogDAO svipTrnLogDAO) {
		this.svipTrnLogDAO = svipTrnLogDAO;
	}
	public void setCustDataDAO(CustDataDAO custDataDAO) {
		this.custDataDAO = custDataDAO;
	}
	
}
