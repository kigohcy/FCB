/**
 * @(#)TrnsResultSearchController.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 交易結果查詢controller
 * 
 * Modify History:
 *  v1.00, 2016/02/19, Jimmy
 *   1) First release
 *  1.01, 2018/03/19
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
import com.hitrust.bank.model.TrnsData;
import com.hitrust.bank.service.TrnsResultSearchSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;
import com.hitrust.framework.web.BaseAutoCommandController;

public class TrnsResultSearchController extends BaseAutoCommandController {
	
	//============================== Log4j ==============================
	private static Logger LOG = Logger.getLogger(TrnsResultSearchController.class);

	//============================== Constructor ==============================
	public TrnsResultSearchController() {
		setCommandClass(TrnsData.class);
	}

	//============================== service injection ==============================
	private TrnsResultSearchSrv trnsResultSearchSrv;

	//============================== injection beans ==============================
	public void setTrnsResultSearchSrv(TrnsResultSearchSrv trnsResultSearchSrv) {
		this.trnsResultSearchSrv = trnsResultSearchSrv;
	}

	/**
	 * 交易結果查詢初始化
	 * 
	 * @param command
	 */
	public void queryInit(Command command) throws BusinessException, FrameException {
		
		LOG.info("交易結果查詢初始化");
		TrnsData trnsData = (TrnsData) command;
		
		try {
			
			// 取得系統日期
			String today = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("D", "AD"));
			trnsData.setEndDate(today);
			
			// 取得該月份的第一天日期
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			DateFormat df = new SimpleDateFormat("YYYY/MM/dd");
			String firstDay = df.format(calendar.getTime());
			trnsData.setStrtDate(firstDay);
			
			// 取得查詢初始化資料
			TrnsData rtnTrnsData = trnsResultSearchSrv.queryInit();
			trnsData.setQueryLimt(rtnTrnsData.getQueryLimt());
			trnsData.setEcData(rtnTrnsData.getEcData());
			
		} catch (BusinessException e) {
			LOG.error("[TrnsResultSearch queryInit BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[TrnsResultSearch queryInit Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}

	/**
	 * 交易結果查詢
	 * 
	 * @param command
	 */
	public void query(Command command) throws BusinessException, FrameException {
		
		LOG.info("交易結果查詢");
		TrnsData trnsData = (TrnsData) command;
		trnsData.setInitFlag(false);
		
		try {

			// 查詢條件
			Date strtDate = DateUtil.formatStrToDate(trnsData.getStrtDate(), "000000");	// 起始日期
			Date endDate = DateUtil.formatStrToDate(trnsData.getEndDate(), "235959");	// 結束日期
			String custId = trnsData.getCustId();										// 身分證字號
			String realAcnt = trnsData.getRealAcnt();									// 銀行存款帳號
			//v1.01 新增電商平台會員代號查詢條件
			String ecUser = trnsData.getEcUser();										//電商平台會員代號
			//v1.01 新增電商平台會員代號查詢條件 End
			String ecId = trnsData.getqEcId();											// 平台代號
			String trnsType = trnsData.getqTrnsType();									// 交易類別
			String trnsStts = trnsData.getqTrnsStts();									// 交易狀態
			Page page = trnsData.getPage();												// 分頁資訊
			
			//記錄應用系統日誌
			TsbAuditLog log = new TsbAuditLog();
			String fnKey = null;
			
			//身分證字號 或 實體帳號
			if(StringUtil.isBlank(custId)){
				fnKey = realAcnt;
			}else{
				fnKey = custId;
			}
			
			//設定應用系統日誌資料
			trnsData.setFnKeyValue(fnKey);
			log.setStartDate(trnsData.getStrtDate());
			log.setEndDate(trnsData.getEndDate());
			log.setCustId(custId);
			log.setAcnt(realAcnt);
			log.setEcId(ecId);
			log.setTrnsType(trnsType);
			trnsData.setFnProc(JsonUtil.object2Json(log, false));

			// 取得交易結果
			//v1.01 新增電商平台會員代號查詢條件
			PageQuery pageQuery = trnsResultSearchSrv.queryTrnsData(strtDate, endDate, custId, realAcnt, ecUser, ecId, trnsType, trnsStts,  page);
			setQueryPage(pageQuery);
			//v1.01 新增電商平台會員代號查詢條件 End
			
			// ========== 敏感戶查詢處理 ==========
			List<TrnsData> list = (List<TrnsData>) pageQuery.getResult();
			Map<String, String> custIds = new HashMap<String, String>();
			for (TrnsData data : list) {
				custIds.put(data.getCustId(), data.getCustId());
			}

			CommonUtil.checkTxncsSvip(custIds, custId, realAcnt, trnsData.getFuncId(), "");
			
		}  catch (BusinessException e) {
			LOG.error("[TrnsResultSearch query BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[TrnsResultSearch query Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}
}
