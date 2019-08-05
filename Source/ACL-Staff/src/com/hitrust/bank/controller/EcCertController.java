/**
 * @(#) EcCertController.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/21, Eason Hsu
 *    1) JIRA-Number, First release
 *   v1.01, 2018/06/14
 *    1) 移除連接RA申請憑證相關Code
 *   v1.11, 2019/02/26
 *    1) 增加驗章測試功能
 * 
 */
package com.hitrust.bank.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.hitrust.acl.common.AppEnv;
import com.hitrust.acl.common.I18nConverter;
import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.common.TBCodeHelper;
import com.hitrust.acl.exception.FrameException;
import com.hitrust.acl.security.isec.Secure;
import com.hitrust.acl.security.isec.VerifyRequest;
import com.hitrust.bank.json.TsbAuditLog;
import com.hitrust.bank.model.EcCert;
import com.hitrust.bank.model.EcData;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.base.AbstractEcCert.Id;
import com.hitrust.bank.service.EcCertSrv;
import com.hitrust.bank.service.EcDataMgmtSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.web.BaseAutoCommandController;

public class EcCertController extends BaseAutoCommandController {
	
	// Log4
	private static Logger LOG = Logger.getLogger(EcCertController.class);
	
	// service injection
	private EcCertSrv ecCertSrv;
	private EcDataMgmtSrv ecDataMgmtSrv;

	// =============== bean injection ===============
	public void setEcCertSrv(EcCertSrv ecCertSrv) {
		this.ecCertSrv = ecCertSrv;
	}
	public void setEcDataMgmtSrv(EcDataMgmtSrv ecDataMgmtSrv) {
		this.ecDataMgmtSrv = ecDataMgmtSrv;
	}
	
	
	public EcCertController() {
		super.setCommandClass(EcCert.class);
	}

	/**
	 * 初始畫面
	 * @param commnd Command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void init(Command commnd) throws BusinessException, FrameException {
		EcCert ecCert = (EcCert) commnd;
		
		try {
			List<EcData> datas = ecCertSrv.fetchEcDataList();
			
			ecCert.setDatas(datas);
			
		} catch (BusinessException e) {
			LOG.error("[EcCertController init]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[EcCertController Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}
	
	/**
	 * 初始 憑證新增頁面
	 * @param commnd
	 */
	public void initInsertCert(Command commnd) {
		EcCert model = (EcCert) commnd;
	}
	
	/**
	 * 新增憑證資料
	 * @param command
	 */
	public void insertCert(Command command) throws BusinessException, FrameException {
		
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		EcCert dataBean = (EcCert)command;
		try{
			//記錄應用系統日誌(BANK_APAUDITLOG)
			this.audiLog(dataBean);
			// 檢核憑證是否存在
			if(ecCertSrv.fetchEcCertByKey(dataBean.getEcId(), dataBean.getCertCn()) != null){
//				dataBean.setReturnMsg(I18nConverter.i18nMapping("message.F0209.certAlreadyExists", user.getLocale()).replaceAll("#",  dataBean.getCertCn()));
				dataBean.setReturnMsg(I18nConverter.i18nMapping("message.db.have.data", user.getLocale()));
			}else{
				EcCert ecCert = new EcCert();
		    	EcCert.Id id = new Id();
		    	id.setEcId(dataBean.getEcId());
		    	id.setCertCn(dataBean.getCertCn());
		    	ecCert.setId(id);
		    	ecCert.setCertSn(dataBean.getCertSn());
		    	ecCert.setStrtDttm(dataBean.getStrtDttm().replace("/", "")+"000000");
		    	ecCert.setEndDttm(dataBean.getEndDttm().replace("/", "")+"000000");
				// 新增電商平台憑證檔
		    	ecCertSrv.insertEcCert(ecCert);
		    	dataBean.setReturnMsg(I18nConverter.i18nMapping("message.sys.insert.success", user.getLocale())); //新增成功
			}
		} catch (BusinessException e) {
			LOG.error("[insertEcData BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[insertEcData Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	
	public void initUpdateCert(Command commnd) {
		EcCert model = (EcCert) commnd;
		try {
			
			//記錄應用系統日誌(BANK_APAUDITLOG)
			//this.audiLog(model);
			EcCert ecCert = ecCertSrv.fetchEcCertByKey(model.getEcId4Update(), model.getCertCn());
            ecCert.setCertCn(model.getCertCn());
            
			/*
			 * LOG.debug("[EC_ID]" +model.getEcId()+"[Q]"+ecCert.getEcId());
			 * LOG.debug("[CERT_CN]" +model.getCertCn()+"[Q]"+ecCert.getCertCn());
			 * LOG.debug("[CERT_SN]" +model.getCertSn()+"[Q]"+ecCert.getCertSn());
			 * LOG.debug("[CERT_ID]" +model.getCertId()+"[Q]"+ecCert.getCertId());
			 * LOG.debug("[STRT_DTTM]"+model.getStrtDttm()+"[Q]"+ecCert.getStrtDttm());
			 * LOG.debug("[END_DTTM]" +model.getEndDttm()+"[Q]"+ecCert.getEndDttm());
			 * LOG.debug("[RA_ACNT]" +model.getRaAcnt()+"[Q]"+ecCert.getRaAcnt());
			 * LOG.debug("[CERT_FEE_DATA]"+model.getCertFeeData()+"[Q]"+ecCert.
			 * getCertFeeData());
			 */
			model.setEcCert(ecCert);
			model.setCertCn(ecCert.getCertCn());
			model.setCertSn(ecCert.getCertSn());
			model.setStrtDttm(convertyyyyMMdd2yyyySmmSdd(ecCert.getStrtDttm()));
			model.setEndDttm(convertyyyyMMdd2yyyySmmSdd(ecCert.getEndDttm()));
			
			this.audiLog(model);
		} catch (BusinessException e) {
			LOG.info("[initUpdateCert BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.info("[initUpdateCert Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}
	
	public void updateCert(Command commnd){
		EcCert model = (EcCert) commnd;
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		try {
			//記錄應用系統日誌(BANK_APAUDITLOG)
			this.audiLog(model);
			/*
			 * LOG.debug("[EC_ID]"+model.getEcId());
			 * 
			 * LOG.debug("[CERT_CN]"+model.getCertCn());
			 * 
			 * LOG.debug("[CERT_SN]"+model.getCertSn());
			 * 
			 * LOG.debug("[CERT_ID]"+model.getCertId());
			 * 
			 * LOG.debug("[STRT_DTTM]"+model.getStrtDttm());
			 * 
			 * LOG.debug("[END_DTTM]"+model.getEndDttm());
			 * 
			 * LOG.debug("[RA_ACNT]"+model.getRaAcnt());
			 * 
			 * LOG.debug("[CERT_FEE_DATA]"+model.getCertFeeData());
			 */
			
			ecCertSrv.updateEcCert(model);
			model.setReturnMsg(I18nConverter.i18nMapping("message.sys.update.success", user.getLocale())); //修改成功
		}catch (BusinessException e) {
			LOG.info("[updateCert BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.info("[updateCert Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	
	public void deleteCert(Command commnd){
		EcCert model = (EcCert) commnd;
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		try {
			//記錄應用系統日誌(BANK_APAUDITLOG)
			this.audiLog(model);
			ecCertSrv.deleteEcCert(model);
			model.setReturnMsg(I18nConverter.i18nMapping("message.sys.delete.success", user.getLocale())); //修改成功
		}catch (BusinessException e) {
			LOG.info("[deleteCert BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.info("[deleteCert Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	/**
	 * 依據平台代碼 取得平台憑證檔清單
	 * @param commnd Command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void fetchCertList(Command commnd, HttpServletRequest req) throws BusinessException, FrameException {
		EcCert ecCert = (EcCert) commnd;
		List<String> checkStts = Arrays.asList("0", "10", "12", "13", "20", "30", "31", "50");
		
		try {
			
			//記錄應用系統日誌(BANK_APAUDITLOG)
			this.audiLog(ecCert);
			
			String ecId = ecCert.getEcId(); // 平台代碼
			String ecName = "";	// 平台中文名稱
			
			EcData ecData = null;
			List<EcCert> certs = new ArrayList<EcCert>();
//			List<RaCertInfo> certInfos = new ArrayList<RaCertInfo>();

			// 依據平台代碼取得平台資料
			ecData = ecCertSrv.fetchEcDataByEcId(ecId);
			// 依據平台代碼取得平台憑證檔清單
			certs = ecCertSrv.fetchEcCertListByEcId(ecId);

			ecName = ecData.getEcNameCh();
			
			ecCert.setEcName(ecName);
//			ecCert.setCertInfos(certInfos);
			ecCert.setQueryFlag(true);
			ecCert.setEcCertList(certs);
			
		} catch (BusinessException e) {
			LOG.error("[fetchCertList BusinessException ]; ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[fetchCertList FrameException ]; ", e);
			throw new FrameException("message.sys.error");
		}
		
	}
	
	
	/**
	 * 記錄應用系統日誌(BANK_APAUDITLOG)
	 * @param dataBean command
	 */
	private void audiLog(EcCert dataBean) throws BusinessException, FrameException {
		
		try {
			
			//記錄應用系統日誌(BANK_APAUDITLOG) 準備資料
			TsbAuditLog log = new TsbAuditLog();
			log.setEcId(dataBean.getEcId());
			
			if(!"Q".equals(dataBean.getOperate())){
				log.setCertCn(dataBean.getCertCn());
			}
			
			dataBean.setFnProc(JsonUtil.object2Json(log, false));
			
		} catch (BusinessException e) {
			LOG.error("[EcData audiLog BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[EcData audiLog Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	/**
	 * 初始 驗章測試 頁面
	 * @param commnd Command
	 */
	public void initVerifySignTest(Command commnd) {
		EcCert model = (EcCert) commnd;
		
	}
	
	/**
	 * 驗章測試
	 * @param commnd Command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void verifySignTest(Command commnd) throws BusinessException, FrameException {
		EcCert model = (EcCert) commnd;
		
		try {
			
			//記錄應用系統日誌(TSB_APAUDITLOG)
			this.audiLog(model);
			
			String returnMsg = "";
			
			int rtnCode = 0;
			String certCn = model.getCertCn();            //憑證識別碼(common name)
			String plainText = model.getPlainText();      //簽章明文
			String signatureVal = model.getSignatureVal();//簽章值
			
			LoginUser user = (LoginUser) APLogin.getCurrentUser();

			LOG.debug("[certCn]" + certCn);
			LOG.debug("[plainText]" + plainText);
			LOG.debug("[signatureVal]" + signatureVal);
			
			VerifyRequest verifyRequest = new VerifyRequest(signatureVal, certCn, plainText);
			Secure secure = new Secure();
			
			//設置驗章 Key Label
			String keyLabel = AppEnv.getString("VERIFY_KEY_LABEL");
			verifyRequest.setKeyLabel(keyLabel);
			
			rtnCode = secure.pkcs7Verify(verifyRequest);
			
			if (rtnCode != 0) {
				LOG.info("========== Failed to verify cert! [" + rtnCode + "] " + secure.getErrorMessage() + " ==========");
				TBCodeHelper helper = new TBCodeHelper("01", String.valueOf(rtnCode));
				throw new BusinessException(helper.getTbCodeMsg(), "");
			}
			
			returnMsg = I18nConverter.i18nMapping("message.F0209.testSucc", user.getLocale());
			
			model.setReturnMsg(returnMsg);
			
		} catch (BusinessException e) {
			LOG.error("[verifySignTest BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[verifySignTest Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
		
	}
	
	/**
	 * 轉換日期由yyyyMMdd為yyyy/mm/dd
	  * @param oriDateStr String
	 */
	
	private String convertyyyyMMdd2yyyySmmSdd(String oriDateStr) {
		SimpleDateFormat fromUser = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy/MM/dd");
		
		if (oriDateStr==null || oriDateStr.length()<8) {
			LOG.error("wrong date format!!");
			throw new FrameException("message.sys.error");
		}else {
			oriDateStr=oriDateStr.substring(0, 8);
			try {
				oriDateStr = myFormat.format(fromUser.parse(oriDateStr));
				} catch (ParseException e) {
				e.printStackTrace();
				}
		}
		return oriDateStr;
		
	}
}
