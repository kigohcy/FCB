/**
 * @(#)CustAcntLogController.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description :會員連結設定記錄controller
 * 
 * Modify History:
 *  v1.00, 2016/02/05, Evan
 *   1) First release
 *  v1.01, 2018/03/20
 *   1) 新增電商平台會員代號查詢條件
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
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.common.CommonUtil;
import com.hitrust.bank.json.TsbAuditLog;
import com.hitrust.bank.model.CustAcntLog;
import com.hitrust.bank.service.CustAcntLogSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.model.page.PageQuery;
import com.hitrust.framework.web.BaseAutoCommandController;

public class CustAcntLogController extends BaseAutoCommandController {

	// Log4j
	private static Logger LOG = Logger.getLogger(CustAcntLogController.class);

	// service injection
	private CustAcntLogSrv custAcntLogSrv;

	public void setCustAcntLogSrv(CustAcntLogSrv custAcntLogSrv) {
		this.custAcntLogSrv = custAcntLogSrv;
	}

	// constructor
	public CustAcntLogController() {
		setCommandClass(CustAcntLog.class);
	}

	/**
	 * 會員連結設定記錄查詢初始
	 * 
	 * @param command
	 */
	public void queryInit(Command command) throws BusinessException, FrameException {
			
		CustAcntLog dataBean = (CustAcntLog) command;
		
		
		try {
			
			dataBean.setInitQuery(true);
			String today = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("D", "AD"));
			// 設定查詢初始的起迄時間
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			DateFormat df = new SimpleDateFormat("YYYY/MM/dd");
			String firstDay = df.format(calendar.getTime());

			dataBean.setStrtDate(firstDay);
			dataBean.setEndDate(today);
			// 查詢客服系統帳號連結-查詢範圍限制
			CustAcntLog custAcntLog = custAcntLogSrv.queryInit();
			dataBean.setSysParam(custAcntLog.getSysParam());
			dataBean.setEcData(custAcntLog.getEcData());
			
		} catch (BusinessException e) {
			LOG.error("[CustAcntLog queryInit BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[CustAcntLog queryInit Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}

	/**
	 * 會員連結設定記錄查詢
	 * 
	 * @param command
	 */
	public void query(Command command)  throws BusinessException, FrameException {
		
		CustAcntLog dataBean = (CustAcntLog) command;
		
		try {
			
			dataBean.setInitQuery(false);
			Date strtDate = DateUtil.formatStrToDate(dataBean.getStrtDate(), "000000");
			Date endDate = DateUtil.formatStrToDate(dataBean.getEndDate(), "235959");
			
			//記錄應用系統日誌
			TsbAuditLog log = new TsbAuditLog();
			String fnKey = null;
			
			//身分證字號 或 實體帳號
			if(StringUtil.isBlank(dataBean.getCustId())){
				fnKey = dataBean.getRealAcnt();
			}else{
				fnKey = dataBean.getCustId();
			}
			
			//設定應用系統日誌資料
			dataBean.setFnKeyValue(fnKey);
			log.setStartDate(dataBean.getStrtDate());
			log.setEndDate(dataBean.getEndDate());
			log.setQueryType(dataBean.getQueryType());
			log.setCustId(dataBean.getCustId());
			log.setRealAcnt(dataBean.getRealAcnt());
			log.setEcId(dataBean.getEcId());
			log.setStts(dataBean.getStts());
			log.setExecSrc(dataBean.getExecSrc());
			dataBean.setFnProc(JsonUtil.object2Json(log, false));
			
			//V1.01 新增電商平台會員代號查詢條件
			PageQuery pageQuery = custAcntLogSrv.queryCustAcntLog(strtDate,
					endDate, dataBean.getQueryType(), dataBean.getCustId(),
					dataBean.getRealAcnt(), dataBean.getEcUser(), dataBean.getEcId(), dataBean.getStts(),
					dataBean.getExecSrc(), dataBean.getPage(), dataBean);
			//V1.01 新增電商平台會員代號查詢條件 End
			
			setQueryPage(pageQuery);
			
			// ========== 敏感戶查詢處理 ==========
			Map<String, String> custIds = new HashMap<String, String>();
			List<CustAcntLog> list = (List<CustAcntLog>) pageQuery.getResult();
			
			for (CustAcntLog acntLog : list) {
				custIds.put(acntLog.getCustId(), acntLog.getCustId());
			}
			
			CommonUtil.checkTxncsSvip(custIds, dataBean.getCustId(), dataBean.getRealAcnt(), dataBean.getFuncId(), "");
			
		} catch (BusinessException e) {
			LOG.error("[CustAcntLog query BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[CustAcntLog query Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
}
