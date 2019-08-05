/**
 * @(#)EcDataMgmtSrvImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 電商平台管理 ECDataMgmtImpl
 * 
 * Modify History:
 *  v1.00, 2016/01/29, Evan
 *   1) First release
 * 
 *  v1.01, 2019/06/19, Organ  
 *   1) Add 繳費稅收費方式及費率   
 *  
 */
package com.hitrust.bank.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.hitrust.bank.dao.CustPltfDAO;
import com.hitrust.bank.dao.EcDataAprvDAO;
import com.hitrust.bank.dao.EcDataDAO;
import com.hitrust.bank.model.EcData;
import com.hitrust.bank.model.EcDataAprv;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.base.AbstractEcDataAprv.Id;
import com.hitrust.bank.service.EcDataMgmtSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;

public class EcDataMgmtSrvImpl implements EcDataMgmtSrv {
	private static Logger	LOG	= Logger.getLogger(EcDataMgmtSrvImpl.class);
	// DAO injection
	private EcDataDAO		ecDataDAO;
	private CustPltfDAO		custPltfDAO;
	private EcDataAprvDAO	ecDataAprvDAO;

	public void setEcDataDAO(EcDataDAO ecDataDAO) {
		this.ecDataDAO = ecDataDAO;
	}

	public void setCustPltfDAO(CustPltfDAO custPltfDAO) {
		this.custPltfDAO = custPltfDAO;
	}

	public void setEcDataAprvDAO(EcDataAprvDAO ecDataAprvDAO) {
		this.ecDataAprvDAO = ecDataAprvDAO;
	}

	/**
	 * 查詢所有電商平台資料
	 * 
	 * @return List<EcData> 有電商平台資料
	 */
	@Override
	public List<EcData> queryEcDataList() {
		return ecDataDAO.getEcDataList();
	}

	/**
	 * 依據畫面資料新增電商平台資料
	 * 
	 * @param hashMap 將畫面料資封裝
	 */
	@Override
	public void insertEcData(HashMap<String, Object> hashMap) {
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		EcData ecData = new EcData();
		try {
			BeanUtils.populate(ecData, hashMap);
		}
		catch (Exception e) {
			throw new BusinessException("insertEcData error", e);
		}
		if (ecDataDAO.queryById(EcData.class, ecData.getEcId()) != null) {
			LOG.info("資料庫有" + ecData.getEcId() + "的資料,無法新增");
			throw new BusinessException("message.db.have.data");
		}
		Date date = new Date();
		ecData.setCretDttm(date);
		ecData.setCretUser(user.getUserId());
		ecData.setMdfyDttm(date);
		ecData.setMdfyUser(user.getUserId());
		// TSBACL-71, 實體帳號 右補0, 補滿16位
		// if(!StringUtil.isBlank(ecData.getRealAcnt())){
		// String realAcnt = ecData.getRealAcnt();
		// if(realAcnt.length() < 16){
		// realAcnt = "000000000000000" + realAcnt;
		// realAcnt = realAcnt.substring(realAcnt.length()-16);
		// ecData.setRealAcnt(realAcnt);
		// }
		// }
		try {
			ecDataDAO.save(ecData);
		}
		catch (Exception e) {
			LOG.error("電商平台資料新增失敗", e);
			throw new BusinessException("message.sys.insert.failure"); // 資料新增失敗
		}
	}

	/**
	 * 依據 平台代碼(ecId)查詢修改編輯的資料
	 * 
	 * @param ecId
	 * @return
	 */
	@Override
	public EcData queryEcData(String ecId) {
		Object ecData = ecDataDAO.queryById(EcData.class, ecId);
		if (ecData == null) {
			LOG.info("資料庫無" + ecId + "的資料,無法修改或刪除");
			throw new BusinessException("message.db.have.no.data");
		}
		return (EcData) ecData;
	}

	/**
	 * 依據畫面資料修改電商平台資料
	 * 
	 * @param hashMap 將畫面料資封裝
	 */
	@Override
	public void updateEcData(HashMap<String, Object> hashMap) {
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		// 檢查資料存不存在
		EcData ecData = this.queryEcData(String.valueOf(hashMap.get("ecId")));
		// 頁面修改更新的資料
		ecData.setEcNameCh(String.valueOf(hashMap.get("ecNameCh")));
		ecData.setEcNameEn(String.valueOf(hashMap.get("ecNameEn")));
		ecData.setFeeType(String.valueOf(hashMap.get("feeType")));
		ecData.setFeeRate(Double.valueOf(String.valueOf(hashMap.get("feeRate"))));
		ecData.setStts(String.valueOf(hashMap.get("stts")));
		ecData.setRealAcnt(String.valueOf(hashMap.get("realAcnt")));
		ecData.setEntrNo(String.valueOf(hashMap.get("entrNo")));
		ecData.setShowRealAcnt(String.valueOf(hashMap.get("showRealAcnt")));
		ecData.setEntrId(String.valueOf(hashMap.get("entrId")));
		ecData.setCntc(String.valueOf(hashMap.get("cntc")));
		ecData.setTel(String.valueOf(hashMap.get("tel")));
		ecData.setMail(String.valueOf(hashMap.get("mail")));
		ecData.setNote(String.valueOf(hashMap.get("note")));
		ecData.setMaxFee(hashMap.get("maxFee") != null ? Integer.parseInt(String.valueOf(hashMap.get("maxFee"))) : null);
		ecData.setMinFee(hashMap.get("minFee") != null ? Integer.parseInt(String.valueOf(hashMap.get("minFee"))) : null);
		ecData.setLinkLimit(Integer.valueOf(String.valueOf(hashMap.get("linkLimit"))));
		ecData.setMdfyDttm(new Date());
		ecData.setMdfyUser(user.getUserId());
		
		//20190619 Add 繳費稅收費方式及費率 Begin
		ecData.setTaxType(String.valueOf(hashMap.get("taxType")));
		ecData.setTaxRate(Double.valueOf(String.valueOf(hashMap.get("taxRate"))));
		ecData.setMaxTax(hashMap.get("maxTax") != null ? Integer.parseInt(String.valueOf(hashMap.get("maxTax"))) : null);
		ecData.setMinTax(hashMap.get("minTax") != null ? Integer.parseInt(String.valueOf(hashMap.get("minTax"))) : null);
		//20190619 Add 繳費稅收費方式及費率 End
		
		// TSBACL-71, 實體帳號 右補0, 補滿16位
		// if(!StringUtil.isBlank(ecData.getRealAcnt())){
		// String realAcnt = ecData.getRealAcnt();
		// if(realAcnt.length() < 16){
		// realAcnt = "000000000000000" + realAcnt;
		// realAcnt = realAcnt.substring(realAcnt.length()-16);
		// ecData.setRealAcnt(realAcnt);
		// }
		// }
		try {
			ecDataDAO.update(ecData);
		}
		catch (Exception e) {
			LOG.error("電商平台資料更新失敗", e);
			throw new BusinessException("message.sys.update.failure"); // 資料更新失敗
		}
	}

	/**
	 * 依據畫面資料更動電商平台狀態
	 * 
	 * @param hashMap
	 */
	@Override
	public void updateEcDataStatus(HashMap<String, Object> hashMap) {
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		// 檢查資料存不存在
		EcData ecData = this.queryEcData(String.valueOf(hashMap.get("ecId")));
		// 頁面修改更新的資料
		ecData.setStts(String.valueOf(hashMap.get("stts")));
		ecData.setMdfyDttm(new Date());
		ecData.setMdfyUser(user.getUserId());
		try {
			ecDataDAO.update(ecData);
		}
		catch (Exception e) {
			LOG.error("電商平台狀態更新失敗", e);
			throw new BusinessException("message.sys.update.failure");
		}
	}

	/**
	 * 依據 平台代碼(ecId) 刪除電商平台資料
	 * 
	 * @param ecId
	 */
	@Override
	public void deleteEcData(String ecId) {
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		// 檢查資料存不存在
		EcData ecData = this.queryEcData(ecId);
		// 檢查平台是否有綁定
		if (custPltfDAO.getCustPltfByEcId(ecId).size() > 0) {
			LOG.info("平台代號:" + ecId + "已有綁定資料，不可刪除");
			throw new BusinessException("message.db.ecDataHadBinded");
		}
		try {
			ecDataDAO.delete((EcData) ecData);
		}
		catch (Exception e) {
			LOG.error("電商平台資料刪除失敗", e);
			throw new BusinessException("message.sys.delete.failure"); // 資料刪除失敗
		}
	}

	/**
	 * 依據畫面資料送審新增電商平台資料
	 * 
	 * @param ecDataAprv
	 */
	@Override
	public void sendEcData(EcDataAprv ecDataAprv) {
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		ecDataAprv.getId().setCretUser(user.getUserId());
		ecDataAprv.getId().setCretDttm(new Date());
		ecDataAprv.setDataStts("1");
		ecDataAprvDAO.save(ecDataAprv);
	}

	/**
	 * 查詢所有待覆核電商平台資料
	 * 
	 * @param strtDate
	 * @param endDate
	 * @param oprtType
	 * @param dataStts
	 * @return List<EcDataAprv> 所有待覆核電商平台資料
	 */
	@Override
	public List<EcDataAprv> queryEcDataAprvList(String strtDate, String endDate, String oprtType, String dataStts) {
		return ecDataAprvDAO.getEcDataAprvList(strtDate, endDate, oprtType, dataStts);
	}

	/**
	 * 依據平台代碼(id)查詢修改編輯的資料
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public EcDataAprv queryEcDataAprv(Id id) {
		Object ecDataAprv = ecDataAprvDAO.queryById(EcDataAprv.class, id);
		if (ecDataAprv == null) {
			LOG.info("資料庫無" + id.getEcId() + "的資料,無法修改或刪除。");
			throw new BusinessException("message.db.have.no.data");
		}
		return (EcDataAprv) ecDataAprv;
	}

	/**
	 * 依據畫面資料核准新增電商平台資料
	 * 
	 * @param user, id
	 */
	@Override
	public void insertEcData(LoginUser user, Id id) {
		try {
			Date aprvDttm = new Date();
			EcData ecData = new EcData();
			EcDataAprv ecDataAprv = (EcDataAprv) ecDataAprvDAO.queryById(EcDataAprv.class, id);
			if (null != ecDataDAO.queryById(EcData.class, id.getEcId())) {
				LOG.info("資料庫有 " + id.getEcId() + " 的資料，無法新增。");
				throw new BusinessException("message.db.have.data");
			}
			else {
				ecData.setEcId(id.getEcId());
				ecData.setEcNameCh(ecDataAprv.getEcNameCh());
				ecData.setEcNameEn(ecDataAprv.getEcNameEn());
				ecData.setFeeType(ecDataAprv.getFeeType());
				ecData.setFeeRate(ecDataAprv.getFeeRate());
				ecData.setStts(ecDataAprv.getStts());
				ecData.setRealAcnt(ecDataAprv.getRealAcnt());
				ecData.setEntrNo(ecDataAprv.getEntrNo());
				ecData.setEntrId(ecDataAprv.getEntrId());
				ecData.setCntc(ecDataAprv.getCntc());
				ecData.setShowRealAcnt(ecDataAprv.getShowRealAcnt());
				ecData.setMinFee(ecDataAprv.getMinFee());
				ecData.setMaxFee(ecDataAprv.getMaxFee());
				ecData.setTel(ecDataAprv.getTel());
				ecData.setMail(ecDataAprv.getMail());
				ecData.setNote(ecDataAprv.getNote());
				ecData.setLinkLimit(ecDataAprv.getLinkLimit());
				ecData.setCretUser(user.getUserId());
				ecData.setMdfyUser(user.getUserId());
				ecData.setCretDttm(aprvDttm);
				ecData.setMdfyDttm(aprvDttm);
				ecData.setActvSendId(ecDataAprv.getId().getCretUser()); // 覆核時若為新增，多異動EC_DATA建檔人員、啟用核可人員、啟用時間
				ecData.setActvAprvId(user.getUserId());
				ecData.setActvAprvDttm(aprvDttm);
				
				//20190619 Add 繳費稅收費方式及費率 Begin
				ecData.setTaxType(ecDataAprv.getTaxType() != null ? ecDataAprv.getTaxType() : null);
				ecData.setTaxRate(ecDataAprv.getTaxRate() != null ? ecDataAprv.getTaxRate() : null);
				ecData.setMinTax(ecDataAprv.getMinTax() != null ? ecDataAprv.getMinTax() : null);
				ecData.setMaxTax(ecDataAprv.getMaxTax() != null ? ecDataAprv.getMaxTax() : null);
				//20190619 Add 繳費稅收費方式及費率 End
				
				ecDataDAO.save(ecData);
				ecDataAprv.setDataStts("2");
				ecDataAprv.setAprvUser(user.getUserId());
				ecDataAprv.setAprvDttm(aprvDttm);
				ecDataAprvDAO.update(ecDataAprv);
			}
		}
		catch (Exception e) {
			LOG.error("電商平台資料核准新增失敗", e);
			throw new BusinessException("message.sys.insert.failure"); // 資料新增失敗
		}
	}

	/**
	 * 依據畫面資料核准修改電商平台資料
	 * 
	 * @param user, id
	 */
	@Override
	public void updateEcData(LoginUser user, Id id) {
		try {
			EcData ecData = this.queryEcData(id.getEcId());
			Date aprvDttm = new Date();
			EcDataAprv ecDataAprv = (EcDataAprv) ecDataAprvDAO.queryById(EcDataAprv.class, id);
			// 頁面修改更新的資料
			ecData.setEcNameCh(ecDataAprv.getEcNameCh());
			ecData.setEcNameEn(ecDataAprv.getEcNameEn());
			ecData.setFeeType(ecDataAprv.getFeeType());
			ecData.setFeeRate(ecDataAprv.getFeeRate());
			ecData.setRealAcnt(ecDataAprv.getRealAcnt());
			ecData.setEntrNo(ecDataAprv.getEntrNo());
			ecData.setEntrId(ecDataAprv.getEntrId());
			ecData.setCntc(ecDataAprv.getCntc());
			ecData.setShowRealAcnt(ecDataAprv.getShowRealAcnt());
			ecData.setMaxFee(ecDataAprv.getMaxFee() != null ? ecDataAprv.getMaxFee() : null);
			ecData.setMinFee(ecDataAprv.getMinFee() != null ? ecDataAprv.getMinFee() : null);
			ecData.setTel(ecDataAprv.getTel());
			ecData.setMail(ecDataAprv.getMail());
			ecData.setNote(ecDataAprv.getNote());
			ecData.setLinkLimit(ecDataAprv.getLinkLimit());
			ecData.setCretUser(user.getUserId());
			ecData.setMdfyUser(user.getUserId());
			ecData.setCretDttm(aprvDttm);
			ecData.setMdfyDttm(aprvDttm);
			//20190619 Add 繳費稅收費方式及費率 Begin
			ecData.setTaxType(ecDataAprv.getTaxType() != null ? ecDataAprv.getTaxType() : null);
			ecData.setTaxRate(ecDataAprv.getTaxRate() != null ? ecDataAprv.getTaxRate() : null);
			ecData.setMinTax(ecDataAprv.getMinTax() != null ? ecDataAprv.getMinTax() : null);
			ecData.setMaxTax(ecDataAprv.getMaxTax() != null ? ecDataAprv.getMaxTax() : null);
			//20190619 Add 繳費稅收費方式及費率 End
			
			if ("02".equals(ecDataAprv.getStts())) { // 覆核時若狀態修改為終止，異動EC_DATA終止建檔人員、終止核可人員、終止時間
				ecData.setStts("02");
				ecData.setTrmnSendId(ecDataAprv.getId().getCretUser());
				ecData.setTrmnAprvId(user.getUserId());
				ecData.setTrmnAprvDttm(aprvDttm);
			}
			else {
				ecData.setStts(ecDataAprv.getStts());
			}
			ecDataDAO.update(ecData);
			ecDataAprv.setDataStts("2");
			ecDataAprv.setAprvUser(user.getUserId());
			ecDataAprv.setAprvDttm(aprvDttm);
			ecDataAprvDAO.update(ecDataAprv);
		}
		catch (Exception e) {
			LOG.error("電商平台資料核准更新失敗", e);
			throw new BusinessException("message.sys.update.failure"); // 資料更新失敗
		}
	}

	/**
	 * 依據畫面資料核准刪除電商平台資料
	 * 
	 * @param user, id
	 */
	@Override
	public void deleteEcData(LoginUser user, Id id) {
		try {
			EcDataAprv ecDataAprv = (EcDataAprv) ecDataAprvDAO.queryById(EcDataAprv.class, id);
			ecDataAprv.setDataStts("3");
			ecDataAprv.setAprvUser(user.getUserId());
			ecDataAprv.setAprvDttm(new Date());
			ecDataAprvDAO.update(ecDataAprv);
		}
		catch (Exception e) {
			LOG.error("電商平台資料核准刪除失敗", e);
			throw new BusinessException("message.sys.delete.failure"); // 資料刪除失敗
		}
	}
}
