/**
 * @(#)EcDataAprvController.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 電商平台覆核 controller
 * 
 * Modify History:
 *  v1.00, 2018/04/16, Darren Tsai
 *   1) First release
 *  
 *  v1.01, 2019/06/19, Organ  
 *   1) Add 繳費稅收費方式及費率	
 */
package com.hitrust.bank.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.hitrust.acl.common.DateTimeUtil;
import com.hitrust.acl.common.I18nConverter;
import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.exception.FrameException;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.bank.json.TsbAuditLog;
import com.hitrust.bank.model.EcDataAprv;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.base.AbstractEcDataAprv;
import com.hitrust.bank.service.EcDataMgmtSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.web.BaseAutoCommandController;

public class EcDataAprvController extends BaseAutoCommandController {
	private static Logger	LOG	= Logger.getLogger(EcDataAprvController.class);
	private EcDataMgmtSrv	ecDataMgmtSrv;

	public void setEcDataMgmtSrv(EcDataMgmtSrv ecDataMgmtSrv) {
		this.ecDataMgmtSrv = ecDataMgmtSrv;
	}

	public EcDataAprvController() {
		setCommandClass(EcDataAprv.class);
	}

	/**
	 * 電商平台覆核查詢初始化
	 * 
	 * @param command
	 */
	public void queryEcDataAprvInit(Command command) throws BusinessException, FrameException {
		EcDataAprv dataBean = (EcDataAprv) command;
		try {
			// 取得系統日期
			String today = DateUtil.getToday();
			today = DateUtil.formateDateTimeForUser(today);
			dataBean.setEndDate(today);
			// 取得該月份的第一天日期
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			DateFormat df = new SimpleDateFormat("YYYY/MM/dd");
			String firstDay = df.format(calendar.getTime());
			dataBean.setStrtDate(firstDay);
			dataBean.setEcDataList(ecDataMgmtSrv.queryEcDataList());
		}
		catch (BusinessException e) {
			LOG.error("[queryEcDataAprvInit BusinessException]: ", e);
			throw e;
		}
		catch (Exception e) {
			LOG.error("[queryEcDataAprvInit Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 電商平台覆核查詢
	 * 
	 * @param command
	 */
	public void queryEcDataAprv(Command command) throws BusinessException, FrameException {
		EcDataAprv dataBean = (EcDataAprv) command;
		try {
			dataBean.setEcDataAprvList(ecDataMgmtSrv.queryEcDataAprvList(dataBean.getStrtDate(), dataBean.getEndDate(), dataBean.getOprtType(), dataBean.getDataStts()));
			dataBean.setQueryFlag(true);
		}
		catch (BusinessException e) {
			LOG.error("[queryEcDataAprv BusinessException]: ", e);
			throw e;
		}
		catch (Exception e) {
			LOG.error("[queryEcDataAprv Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 執行待覆核電商平台的新增、異動或刪除
	 * 
	 * @param command
	 */
	public void executeEcData(Command command) throws BusinessException, FrameException {
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		EcDataAprv dataBean = (EcDataAprv) command;
		try {
			if ("I".equals(dataBean.getOprtType()) && "2".equals(dataBean.getDataStts())) { // 新增且放行
				this.addEcDataAprv(user, dataBean);
			}
			else if ("U".equals(dataBean.getOprtType()) && "2".equals(dataBean.getDataStts())) { // 修改且放行
				this.modifyEcDataAprv(user, dataBean);
			}
			else {
				this.deleteEcDataAprv(user, dataBean);
			}
		}
		catch (BusinessException e) {
			LOG.error("[executeEcData BusinessException]: ", e);
			throw e;
		}
		catch (Exception e) {
			LOG.error("[executeEcData Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 核准新增電商平台資料
	 * 
	 * @param user, dataBean
	 */
	private void addEcDataAprv(LoginUser user, EcDataAprv dataBean) throws BusinessException, FrameException {
		try {
			// 記錄應用系統日誌(TSB_APAUDITLOG)
			this.audiLog(dataBean);
			// 新增EC_DATA
			AbstractEcDataAprv.Id id = new AbstractEcDataAprv.Id();
			String[] ecIdData = dataBean.getEcId().split("\\,");
			id.setEcId(ecIdData[0]);
			id.setCretUser(dataBean.getCretUser());
			id.setCretDttm(DateTimeUtil.formatStr(dataBean.getCretDttm(), "yyyy-MM-dd hh:mm:ss.SSS"));
			ecDataMgmtSrv.insertEcData(user, id);
			dataBean.setReturnMsg(I18nConverter.i18nMapping("message.sys.insert.success", user.getLocale())); // 新增成功
		}
		catch (BusinessException e) {
			LOG.error("[addEcDataAprv BusinessException]: ", e);
			throw e;
		}
		catch (Exception e) {
			LOG.error("[addEcDataAprv Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 核准修改電商平台資料
	 * 
	 * @param user, dataBean
	 */
	private void modifyEcDataAprv(LoginUser user, EcDataAprv dataBean) throws BusinessException, FrameException {
		try {
			// 記錄應用系統日誌(TSB_APAUDITLOG)
			this.audiLog(dataBean);
			// 修改EC_DATA
			AbstractEcDataAprv.Id id = new AbstractEcDataAprv.Id();
			String[] ecIdData = dataBean.getEcId().split("\\,");
			id.setEcId(ecIdData[0]);
			id.setCretUser(dataBean.getCretUser());
			id.setCretDttm(DateTimeUtil.formatStr(dataBean.getCretDttm(), "yyyy-MM-dd hh:mm:ss.SSS"));
			ecDataMgmtSrv.updateEcData(user, id);
			dataBean.setReturnMsg(I18nConverter.i18nMapping("message.sys.update.success", user.getLocale()));// 修改成功
		}
		catch (BusinessException e) {
			LOG.error("[modifyEcDataAprv BusinessException]: ", e);
			throw e;
		}
		catch (Exception e) {
			LOG.error("[modifyEcDataAprv Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 刪除待覆核電商平台資料
	 * 
	 * @param user, dataBean
	 */
	private void deleteEcDataAprv(LoginUser user, EcDataAprv dataBean) throws BusinessException, FrameException {
		try {
			// 記錄應用系統日誌(TSB_APAUDITLOG)
			this.audiLog(dataBean);
			// 修改EC_DATA
			AbstractEcDataAprv.Id id = new AbstractEcDataAprv.Id();
			String[] ecIdData = dataBean.getEcId().split("\\,");
			id.setEcId(ecIdData[0]);
			id.setCretUser(dataBean.getCretUser());
			id.setCretDttm(DateTimeUtil.formatStr(dataBean.getCretDttm(), "yyyy-MM-dd hh:mm:ss.SSS"));
			ecDataMgmtSrv.deleteEcData(user, id);
			dataBean.setReturnMsg(I18nConverter.i18nMapping("message.sys.delete.success", user.getLocale()));// 刪除成功
		}
		catch (BusinessException e) {
			LOG.error("[deleteEcDataAprv BusinessException]: ", e);
			throw e;
		}
		catch (Exception e) {
			LOG.error("[deleteEcDataAprv Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 記錄應用系統日誌(TSB_APAUDITLOG)
	 * 
	 * @param dataBean
	 */
	private void audiLog(EcDataAprv dataBean) throws BusinessException, FrameException {
		try {
			// 記錄應用系統日誌(TSB_APAUDITLOG) 準備資料
			TsbAuditLog log = new TsbAuditLog();
			AbstractEcDataAprv.Id id = new AbstractEcDataAprv.Id();
			String[] ecIdData = dataBean.getEcId().split("\\,");
			id.setEcId(ecIdData[0]);
			id.setCretUser(ecIdData[1]);
			id.setCretDttm(DateTimeUtil.formatStr(ecIdData[2], "yyyy-MM-dd HH:mm:ss.SSS"));
			EcDataAprv ecDataAprv = ecDataMgmtSrv.queryEcDataAprv(id);
			log.setEcId(id.getEcId());
			if (!"D".equals(ecDataAprv.getOperate())) {
				log.setEcNameCh(ecDataAprv.getEcNameCh());
				log.setEcNameEn(ecDataAprv.getEcNameEn());
				log.setFeeType(ecDataAprv.getFeeType());
				log.setFeeRate(ecDataAprv.getFeeRate() + "");
				log.setStts(ecDataAprv.getStts());
				log.setRealAcnt(ecDataAprv.getRealAcnt());
				log.setEntrNo(ecDataAprv.getEntrNo());
				log.setEntrId(ecDataAprv.getEntrId());
				log.setCntc(ecDataAprv.getCntc());
				log.setTel(ecDataAprv.getTel());
				log.setMail(ecDataAprv.getMail());
				log.setNote(ecDataAprv.getNote());
				//20190619 Add 繳費稅收費方式及費率 Begin
				log.setTaxType(ecDataAprv.getTaxType());
				log.setTaxRate(ecDataAprv.getTaxRate());
				//20190619 Add 繳費稅收費方式及費率 End
				
			}
			dataBean.setFnProc(JsonUtil.object2Json(log, false));
		}
		catch (BusinessException e) {
			LOG.error("[EcDataAprv audiLog BusinessException]: ", e);
			throw e;
		}
		catch (Exception e) {
			LOG.error("[EcDataAprv audiLog Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
}
