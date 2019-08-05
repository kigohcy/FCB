/**
 * @(#) EcCertSrvImpl.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, Mar 21, 2016, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.bank.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.hitrust.acl.common.AppEnv;
import com.hitrust.bank.dao.EcCertDAO;
import com.hitrust.bank.dao.EcDataDAO;
import com.hitrust.bank.dao.SysParmDAO;
import com.hitrust.bank.model.EcCert;
import com.hitrust.bank.model.EcData;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.SysParm;
import com.hitrust.bank.service.EcCertSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;

public class EcCertSrvImpl implements EcCertSrv {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(EcCertSrvImpl.class);
	
	// DAO injection
	private EcDataDAO ecDataDAO;
	private EcCertDAO ecCertDAO;
	private SysParmDAO sysParmDAO;

	/**
	 * 取得所有電商平台
	 * @return List<EcData>
	 */
	@Override
	public List<EcData> fetchEcDataList() {
		//String headOffice = AppEnv.getString("HEAD_OFFICE").trim();
		String headOffice ="0937";
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		String brchId = user.getBrchBank();
		if(StringUtils.equals(brchId, headOffice)) brchId="";
		
		List<EcData> datas = ecDataDAO.getEcDataList();
		
		return datas;
	}
	
	/**
	 * 依據 平台代碼 取得平台資料
	 * @param ecId 平台代碼
	 * @return EcData
	 */
	@Override
	public EcData fetchEcDataByEcId(String ecId) {
		EcData ecData = (EcData) ecDataDAO.queryById(EcData.class, ecId);
		
		return ecData;
	}
	
	/**
	 * 依據平台代碼 取得平台憑證檔清單
	 * @param ecId 平台代碼
	 * @return List<EcCert>
	 */
	@Override
	public List<EcCert> fetchEcCertListByEcId(String ecId) {
		
		LOG.info("========== 依據平台代碼: " + ecId + " 取得平台憑證檔清單 ==========");
		
		List<EcCert> certs = ecCertDAO.fetchEcCertListByEcId(ecId);
		
		return certs;
	}
	
	/**
	 * 依據 憑證序號取得 電商平台憑證檔
	 * @param certSn
	 * @return EcCert or null
	 */
	@Override
	public EcCert fetchEcCertByCertSn(String certSn) {
		EcCert ecCert = ecCertDAO.fetchEcCertByCertSn(certSn);
		
		return ecCert;
	}
	
	/**
	 * 依據 平台代碼 & 憑證識別碼 對應憑證資料
	 * @param ecId 	 憑證編號
	 * @param certCn 憑證識別碼
	 * @return EcCert or null
	 */
	public EcCert fetchEcCertByKey(String ecId, String certCn) {
		EcCert cert = ecCertDAO.fetchEcCertByKey(ecId, certCn);
		
		return cert;
	}
	
	/**
	 * 新增電商平台憑證資料
	 * @param cert 電商平台憑證檔
	 */
	public void insertEcCert(EcCert cert) {
		ecCertDAO.save(cert);
	}
	
	/**
	 * 依據 參數名稱 取得系統參數檔
	 * @param code 參數名稱
	 * @return SysParm
	 */
	@Override
	public SysParm fetchSysParmByCode(String code) {
		SysParm parm = sysParmDAO.fetchSysParmByParm(code);
		
		return parm;
	}
	
	// =============== injection bean ===============
	public void setEcDataDAO(EcDataDAO ecDataDAO) {
		this.ecDataDAO = ecDataDAO;
	}
	public void setEcCertDAO(EcCertDAO ecCertDAO) {
		this.ecCertDAO = ecCertDAO;
	}
	public void setSysParmDAO(SysParmDAO sysParmDAO) {
		this.sysParmDAO = sysParmDAO;
	}
	
	/**
	 * 異動電商平台憑證資料
	 * @param modle 電商平台憑證檔
	 */
	@Override
	public void updateEcCert(EcCert modle) {
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		//檢查資料存不存在
		EcCert ecCert = ecCertDAO.fetchEcCertByKey(modle.getEcId(), modle.getCertCn());
		if( ecCert == null){
			LOG.info("資料庫無"+modle.getEcId()+","+modle.getCertCn()+"的資料,無法修改");
			throw new BusinessException("message.db.have.no.data");
		}
		ecCert.setCertSn(modle.getCertSn());
		ecCert.setStrtDttm(modle.getStrtDttm().replace("/", ""));
		ecCert.setEndDttm(modle.getEndDttm().replace("/", ""));
		try{
			ecCertDAO.update(ecCert);
		}catch(Exception e){
			LOG.error("電商平台憑證更新失敗", e);
			throw new BusinessException("message.sys.update.failure");	//資料更新失敗
		}
	}

	@Override
	public void deleteEcCert(EcCert modle) {
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		//檢查資料存不存在
		EcCert ecCert = ecCertDAO.fetchEcCertByKey(modle.getEcId(), modle.getCertCn());
		if( ecCert == null){
			LOG.info("資料庫無"+modle.getEcId()+","+modle.getCertCn()+"的資料,無法刪除");
			throw new BusinessException("message.db.have.no.data");
		}
		try{
			ecCertDAO.delete(ecCert);
		}catch(Exception e){
			LOG.error("電商平台憑證刪除失敗", e);
			throw new BusinessException("message.sys.delete.failure");	//資料刪除失敗
		}
	}
	
}
