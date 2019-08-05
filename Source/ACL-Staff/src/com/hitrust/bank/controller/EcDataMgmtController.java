/**
 * @(#)EcDataMgmtController.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 電商平台管理 controller
 * 
 * Modify History:
 *  v1.00, 2016/01/29, Evan
 *   1) First release
 *   
 *  v1.01, 2019/06/19, Organ  
 *   1) Add 繳費稅收費方式及費率	
 *  
 */
package com.hitrust.bank.controller;

import org.apache.log4j.Logger;

import com.hitrust.acl.common.I18nConverter;
import com.hitrust.acl.exception.FrameException;
import com.hitrust.bank.model.EcData;
import com.hitrust.bank.model.EcDataAprv;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.base.AbstractEcDataAprv;
import com.hitrust.bank.service.EcDataMgmtSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.web.BaseAutoCommandController;

public class EcDataMgmtController extends BaseAutoCommandController {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(EcDataMgmtController.class);
	
	// service injection
	private EcDataMgmtSrv  ecDataMgmtSrv;
	
	public void setEcDataMgmtSrv(EcDataMgmtSrv ecDataMgmtSrv) {
		this.ecDataMgmtSrv = ecDataMgmtSrv;
	}
	
	// constructor
	public EcDataMgmtController() {
		setCommandClass(EcData.class);
	}
	
	/**
	 * 電商平台資料查詢
	 * @param command
	 */
	public void queryEcData(Command command)  throws BusinessException, FrameException {
		
		EcData dataBean = (EcData) command;
		try {
			
			dataBean.setEcDataList(ecDataMgmtSrv.queryEcDataList());
			
		} catch (BusinessException e) {
			LOG.error("[queryEcData BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[queryEcData Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}
	
	/**
	 * 新增電商平台資料送審
	 * @param command
	 */
	public void sendEcDataInsert(Command command) throws BusinessException, FrameException {
		
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		EcData dataBean = (EcData) command;
		
		try {
			
			//新增EC_DATA
			ecDataMgmtSrv.sendEcData(this.convertToAprvBean(dataBean, "I"));
			dataBean.setReturnMsg(I18nConverter.i18nMapping("message.sys.send.success", user.getLocale())); //送審成功
			
		} catch (BusinessException e) {
			LOG.error("[sendEcDataInsert BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[sendEcDataInsert Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	/**
	 * 修改電商平台資料送審
	 * @param command
	 */
	public void sendEcDataUpdate(Command command) throws BusinessException, FrameException {
		
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		EcData dataBean = (EcData) command;
		
		try {
			
			//新增EC_DATA
			ecDataMgmtSrv.sendEcData(this.convertToAprvBean(dataBean, "U"));
			dataBean.setReturnMsg(I18nConverter.i18nMapping("message.sys.send.success", user.getLocale())); //送審成功
			
		} catch (BusinessException e) {
			LOG.error("[sendEcDataUpdate BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[sendEcDataUpdate Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 修改電商平台資料編輯頁
	 * @param command 
	 */
	public void updateEcDataInit(Command command) throws BusinessException, FrameException {
		
		EcData dataBean = (EcData) command;
		
		try {
			
			EcData  queryBean  = ecDataMgmtSrv.queryEcData(dataBean.getEcId());	//查詢修改編輯的資料
			dataBean.setEcData(queryBean);
			
		} catch (BusinessException e) {
			LOG.error("[updateEcDataInit BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[updateEcDataInit Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}
	
	/**
	 * 將頁面的的資料轉成送審物件
	 * @param orgBean
	 * @param oprtType - I:新增, U:異動
	 */
	private EcDataAprv convertToAprvBean(EcData orgBean, String oprtType){
		EcDataAprv returnBean = new EcDataAprv();
		AbstractEcDataAprv.Id id = new AbstractEcDataAprv.Id();
		id.setEcId(orgBean.getEcId());
		id.setCretUser(orgBean.getCretUser());
		id.setCretDttm(orgBean.getCretDttm());
		returnBean.setId(id);
		returnBean.setEcNameCh(orgBean.getEcNameCh());
		returnBean.setEcNameEn(orgBean.getEcNameEn());
		returnBean.setSorcIp(orgBean.getSorcIp());
		returnBean.setFeeType(orgBean.getFeeType());
		returnBean.setFeeRate(orgBean.getFeeRate());
		returnBean.setStts(orgBean.getStts());
		returnBean.setRealAcnt(orgBean.getRealAcnt());
		returnBean.setEntrId(orgBean.getEntrId());
		returnBean.setEntrNo(orgBean.getEntrNo());
		returnBean.setCntc(orgBean.getCntc());
		returnBean.setTel(orgBean.getTel());
		returnBean.setMail(orgBean.getMail());
		returnBean.setNote(orgBean.getNote());
		returnBean.setShowSerl(orgBean.getShowSerl());
		returnBean.setShowRealAcnt(orgBean.getShowRealAcnt());
		returnBean.setMinFee(orgBean.getMinFee());
		returnBean.setMaxFee(orgBean.getMaxFee());
		returnBean.setLinkLimit(orgBean.getLinkLimit());
		returnBean.setOprtType(oprtType);
		 //20190619 Add 繳費稅收費方式及費率 Begin
		returnBean.setTaxType(orgBean.getTaxType());
		returnBean.setTaxRate(orgBean.getTaxRate());
		returnBean.setMinTax(orgBean.getMinTax());
		returnBean.setMaxTax(orgBean.getMaxTax());
		 //20190619 Add 繳費稅收費方式及費率 End
		return returnBean;
	}
	
}
