/**
 * @(#)StaffOptLogController.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 操作記錄查詢controller
 * 
 * Modify History:
 *  v1.00, 2016/01/25, Evan
 *   1) First release
 *  
 */
package com.hitrust.bank.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.exception.FrameException;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.bank.json.TsbAuditLog;
import com.hitrust.bank.model.StaffOptLog;
import com.hitrust.bank.service.StaffOptLogSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.model.page.PageQuery;
import com.hitrust.framework.web.BaseAutoCommandController;

public class StaffOptLogController extends BaseAutoCommandController {
	//log4j
	private static Logger LOG = Logger.getLogger(StaffOptLogController.class);

	// service injection
	private StaffOptLogSrv staffOptLogSrv;

	public void setStaffOptLogSrv(StaffOptLogSrv staffOptLogSrv) {
		this.staffOptLogSrv = staffOptLogSrv;
	}

	/**
	 * Construst
	 */
	public StaffOptLogController() {
		setCommandClass(StaffOptLog.class);
	}

	/**
	 * 操作記錄查詢初始
	 * 
	 * @param command
	 */
	public void queryInit(Command command) throws BusinessException, FrameException {
		
		StaffOptLog dataBean = (StaffOptLog) command;
		//設定系統初始頁
		dataBean.setInitQuery(true);
		
		try {
			
			String today = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("D", "AD"));
			StaffOptLog staffOptLog = staffOptLogSrv.queryInit();
			//功能代碼清單
			dataBean.setStaffSysFnct(staffOptLog.getStaffSysFnct());
			//客服系統操作記錄-查詢範圍限制
			dataBean.setSysParam(staffOptLog.getSysParam());
			//設定查詢起迄區間
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			DateFormat df = new SimpleDateFormat("YYYY/MM/dd");
			String firstDay = df.format(calendar.getTime());

			dataBean.setStartDate(firstDay);
			dataBean.setEndDate(today);
			
		} catch (BusinessException e) {
			LOG.error("[StaffOptLog queryInit BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[StaffOptLog queryInit Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}

	/**
	 * 根據頁面的查詢條件查詢DB
	 * 
	 * @param command
	 */
	public void query(Command command) throws BusinessException, FrameException {
		
		StaffOptLog dataBean = (StaffOptLog) command;
		dataBean.setInitQuery(false);
		
		try {

			Date startDate = DateUtil.formatStrToDate(dataBean.getStartDate(), "000000");
			Date endDate = DateUtil.formatStrToDate(dataBean.getEndDate(), "235959");
			
			//記錄應用系統日誌
			TsbAuditLog log = new TsbAuditLog();
			
			//設定應用系統日誌資料
			log.setStartDate(dataBean.getStartDate());
			log.setEndDate(dataBean.getEndDate());
			log.setUserId(dataBean.getQuserId());
			log.setFnctId(dataBean.getQfnctId());
			dataBean.setFnProc(JsonUtil.object2Json(log, false));
			
			PageQuery pageQuery = staffOptLogSrv.queryStaffOptLog(startDate, endDate, dataBean.getQuserId(), dataBean.getQfnctId(), dataBean.getPage());
			setQueryPage(pageQuery);
			
		} catch (BusinessException e) {
			LOG.error("[StaffOptLog query BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[StaffOptLog query Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}

	/**
	 * 查詢異動明細資料
	 * 
	 * @param command
	 */
	public void queryDetail(Command command) throws BusinessException, FrameException {
		
		StaffOptLog dataBean = (StaffOptLog) command;
		
		try {
			
			staffOptLogSrv.queryDetail(dataBean);
			// 返回對應fnctId的頁面
			this.returnView.set("/0902/optLog/" + dataBean.getQ_fnctId() + "_main");
			// LOG.debug("returnView="+this.returnView);
			
		} catch (BusinessException e) {
			LOG.error("[StaffOptLog queryDetail BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[StaffOptLog queryDetail Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}
}
