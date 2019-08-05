/**
 * @(#)CustOptLogController.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 會員操作記錄查詢controller
 * 
 * Modify History:
 *  v1.00, 2016/06/22, Jimmy Yen
 *   1) First release
 *  
 */
package com.hitrust.bank.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.exception.FrameException;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.bank.common.CommonUtil;
import com.hitrust.bank.json.TsbAuditLog;
import com.hitrust.bank.model.CustOptLog;
import com.hitrust.bank.service.CustOptLogSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.model.page.PageQuery;
import com.hitrust.framework.web.BaseAutoCommandController;

public class CustOptLogController extends BaseAutoCommandController {
	// log4j
	private static Logger LOG = Logger.getLogger(CustOptLogController.class);

	// constructor
	CustOptLogController() {
		setCommandClass(CustOptLog.class);
	}

	// service inject
	CustOptLogSrv custOptLogSrv;

	public void setCustOptLogSrv(CustOptLogSrv custOptLogSrv) {
		this.custOptLogSrv = custOptLogSrv;
	}

	public void queryInit(Command command) throws BusinessException, FrameException {
		CustOptLog custOptLog = (CustOptLog) command;
		try {

			// 取得系統日期
			String today = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("D", "AD"));
			custOptLog.setEndDate(today);

			// 取得該月份的第一天日期
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			DateFormat df = new SimpleDateFormat("YYYY/MM/dd");
			String firstDay = df.format(calendar.getTime());
			custOptLog.setStartDate(firstDay);

			// 取得查詢初始化資料
			CustOptLog rtnCustOptLog = custOptLogSrv.queryInit();

			custOptLog.setQueryLimt(rtnCustOptLog.getQueryLimt());// 查詢範圍限制
			custOptLog.setCustSysFnct(rtnCustOptLog.getCustSysFnct());// 查詢功能清單

		} catch (BusinessException e) {
			LOG.error("[CustOptLog queryInit BusinessException]: ", e);
			throw e;

		} catch (Exception e) {
			LOG.error("[CustOptLog queryInit Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	public void query(Command command) throws BusinessException, FrameException {
		CustOptLog custOptLog = (CustOptLog) command;
		custOptLog.setInitFlag(false);

		try {

			Date startDate = DateUtil.formatStrToDate(custOptLog.getStartDate(), "000000");
			Date endDate = DateUtil.formatStrToDate(custOptLog.getEndDate(), "235959");

			// 記錄應用系統日誌
			TsbAuditLog log = new TsbAuditLog();

			// 設定應用系統日誌資料
			log.setStartDate(custOptLog.getStartDate());
			log.setEndDate(custOptLog.getEndDate());
			log.setUserId(custOptLog.getqUserId());
			log.setFnctId(custOptLog.getqFnctId());
			custOptLog.setFnProc(JsonUtil.object2Json(log, false));

			PageQuery pageQuery = custOptLogSrv.queryCustOptLog(startDate, endDate, custOptLog.getqUserId(), custOptLog.getqFnctId(),
					custOptLog.getPage());
			setQueryPage(pageQuery);
			
			// ========== 敏感戶查詢處理 ==========
			List<CustOptLog> list = (List<CustOptLog>) pageQuery.getResult();
			Map<String, String> custIds = new HashMap<String, String>();
			
			for (CustOptLog optLog : list) {
				custIds.put(optLog.getUserId(), optLog.getUserId());
			}
			
			CommonUtil.checkTxncsSvip(custIds, custOptLog.getUserId(), "", custOptLog.getFuncId(), "");
			
		} catch (BusinessException e) {
			LOG.error("[CustOptLog queryInit BusinessException]: ", e);
			throw e;

		} catch (Exception e) {
			LOG.error("[CustOptLog queryInit Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	/**
	 * 查詢異動明細資料
	 * 
	 * @param command
	 */
	public void queryDetail(Command command) throws BusinessException, FrameException {
		
		CustOptLog dataBean = (CustOptLog) command;
		
		try {
			
			custOptLogSrv.queryDetail(dataBean);
			
			if ("F0201".equals(dataBean.getQ_fnctId())) {
				// 返回對應fnctId的頁面
				this.returnView.set("/0903/optLog/" + dataBean.getQ_fnctId());

			} else {
				// 返回對應fnctId的頁面
				this.returnView.set("/0903/optLog/" + dataBean.getQ_fnctId() + "_main");
				// LOG.debug("returnView="+this.returnView);
				
			}
			
			
		} catch (BusinessException e) {
			LOG.error("[CustOptLog queryDetail BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[CustOptLog queryDetail Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
}
