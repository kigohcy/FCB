/**
 * @(#)CustOptLogSrvImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 會員操作記錄查詢 service
 * 
 * Modify History:
 *  v1.00, 2016/06/22, Jimmy Yen
 *   1) First release
 *  
 */
package com.hitrust.bank.service.impl;

import java.util.Date;
import org.apache.log4j.Logger;
import com.hitrust.acl.common.ObjectSerialUtil;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.CustOptLogDAO;
import com.hitrust.bank.dao.CustSysFnctDAO;
import com.hitrust.bank.dao.SysParmDAO;
import com.hitrust.bank.model.BlobData;
import com.hitrust.bank.model.CustOptLog;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.SysParm;
import com.hitrust.bank.service.CustOptLogSrv;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public class CustOptLogSrvImpl implements CustOptLogSrv {
	// Log4j
	private static Logger LOG = Logger.getLogger(CustOptLogSrvImpl.class);
	
	// DAO inject
	private SysParmDAO sysParmDAO;
	private CustSysFnctDAO custSysFnctDAO;
	private CustOptLogDAO custOptLogDAO;

	public void setSysParmDAO(SysParmDAO sysParmDAO) {
		this.sysParmDAO = sysParmDAO;
	}
	public void setCustSysFnctDAO(CustSysFnctDAO custSysFnctDAO) {
		this.custSysFnctDAO = custSysFnctDAO;
	}
	public void setCustOptLogDAO(CustOptLogDAO custOptLogDAO) {
		this.custOptLogDAO = custOptLogDAO;
	}

	/**
	 * 查詢初始化
	 * 
	 * @return CustOptLog
	 */
	@Override
	public CustOptLog queryInit() {
		CustOptLog custOptLog = new CustOptLog();

		// 取得功能清單
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		String lang = user.getLocale().getLanguage() + "_" + user.getLocale().getCountry();
		custOptLog.setCustSysFnct(custSysFnctDAO.getEnableCustSysFnctList(lang));

		// 取得查詢範圍限制
		SysParm sysParm = (SysParm) sysParmDAO.queryById(SysParm.class, "CUST_OPT_QURY_LIMT");
		custOptLog.setQueryLimt(sysParm.getParmValue());
		return custOptLog;
	}

	/**
	 * 查詢
	 * 
	 * @param strtDate
	 *            查詢起日
	 * @param endDate
	 *            查詢迄日
	 * @param userId
	 *            身分證字號
	 * @param fnctId
	 *            功能代碼
	 * @param page
	 *            分頁資訊
	 * @return PageQuery
	 */
	@Override
	public PageQuery queryCustOptLog(Date strtDate, Date endDate, String userId, String fnctId, Page page) {
		PageQuery pageQuery = custOptLogDAO.getCustOptLog(strtDate, endDate, userId, fnctId, page);

		return pageQuery;
	}
	
	/**
	 * 操作紀錄異動明細查詢
	 *  @param	custOptLog command
	 */
	public void queryDetail(CustOptLog custOptLog) {
		String logNo = custOptLog.getLogNo();
		custOptLog.setBefore(null);
		custOptLog.setAfter(null);
		CustOptLog custOptLogTemp = (CustOptLog)custOptLogDAO.queryById(CustOptLog.class, logNo);
		String fnctId = custOptLogTemp.getFnctId();
		String id= "";
        String action = custOptLogTemp.getAction();
        try {
            //將XML檔案內容組回CustOptLog物件，before
        	if(!StringUtil.isBlank(custOptLogTemp.getBeforeId())){
        		LOG.debug("將XML檔案內容組回CustOptLog物件，before begin...");
        		id = custOptLogTemp.getBeforeId();
        		BlobData blobData = (BlobData)custOptLogDAO.queryById(BlobData.class, id);
        		if(blobData!=null){
        			Command command = (Command) ObjectSerialUtil.bytesToObject(blobData.getLogData());
        			custOptLog.setBefore(command);
        		}
        		LOG.debug("將XML檔案內容組回CustOptLog物件，before end...");
        	}
        	//將XML檔案內容組回CustOptLog物件，after
        	if(!StringUtil.isBlank(custOptLogTemp.getAfterId())){
        		LOG.debug("將XML檔案內容組回CustOptLog物件，after begin...");
        		id = custOptLogTemp.getAfterId();
        		BlobData blobData = (BlobData) custOptLogDAO.queryById(BlobData.class, id);
        		if(blobData!=null){
        			Command command = (Command) ObjectSerialUtil.bytesToObject(blobData.getLogData());
        			custOptLog.setAfter(command);
        		}
        		LOG.debug("將XML檔案內容組回CustOptLog物件，after end...");
        	}
        } catch (Exception e) {
            LOG.error("LoadLog Error: " + e.toString(), e);
        }
        custOptLog.setQ_fnctId(fnctId);
        custOptLog.setQ_action(action);
	}
	
}
