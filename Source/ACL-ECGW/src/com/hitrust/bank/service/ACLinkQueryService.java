/**
 * @(#) ACLinkQueryService.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/28, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.bank.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.hitrust.acl.APSystem;
import com.hitrust.acl.exception.ApplicationException;
import com.hitrust.acl.exception.DBException;
import com.hitrust.acl.response.AbstractResponseBean;
import com.hitrust.acl.service.AbstractServiceModel;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.common.CommonUtil;
import com.hitrust.bank.common.TbCodeHelper;
import com.hitrust.bank.dao.beans.CustAcntLink;
import com.hitrust.bank.dao.beans.CustAcntLog;
import com.hitrust.bank.dao.beans.CustData;
import com.hitrust.bank.dao.beans.CustPltf;
import com.hitrust.bank.dao.beans.EcData;
import com.hitrust.bank.dao.home.CustAcntLinkHome;
import com.hitrust.bank.dao.home.CustAcntLogHome;
import com.hitrust.bank.dao.home.CustDataHome;
import com.hitrust.bank.dao.home.CustPltfHome;
import com.hitrust.bank.dao.home.EcDataHome;
import com.hitrust.bank.response.ACLinkQueryResBean;

public class ACLinkQueryService extends AbstractServiceModel {

	// Log4j
	private static Logger LOG = Logger.getLogger(ACLinkQueryService.class);

	public ACLinkQueryService(String service, String operation, AbstractResponseBean resBean, HttpServletRequest request, HttpServletResponse response) {
		super(service, operation, resBean, request, response);
	}
	
	@Override
	public AbstractResponseBean process() throws ApplicationException, DBException {
		
		LOG.info(DateUtil.formateDateTime(DateUtil.getCurrentTime("DT", "AD"))+" 開始執行平台交易結果查詢  ["+operation+"]");
		// 取得 connection
		Connection conn = null;
		
		ACLinkQueryResBean resBesn = (ACLinkQueryResBean) this.resBean;
		
		
		try {
			conn = APSystem.getConnection(APSystem.DB_ACLINK);
			
			DatabaseMetaData mtdt = conn.getMetaData();
			LOG.info("DB 使用者: [" + mtdt.getUserName()+"]");
			LOG.info("用戶IP:["+request.getRemoteAddr()+"]");
			LOG.info("用戶電腦名稱:["+request.getRemoteHost()+"]");
			
			String MSG_NO 	  = this.request.getParameter("MSG_NO");	 // 訊息序號
			String EC_ID 	  = this.request.getParameter("EC_ID");		 // 平台代碼
			String EC_USER 	  = this.request.getParameter("EC_USER");	 // 平台會員代號
			String CUST_ID 	  = this.request.getParameter("CUST_ID");	 // 使用者身分證號
			String SER_MSG_NO = this.request.getParameter("SER_MSG_NO"); // 查詢訊息序號
			String SIGN_TIME  = this.request.getParameter("SIGN_TIME");  // 簽章日期時間
			String SIGN_VALUE = this.request.getParameter("SIGN_VALUE"); // 簽章值
			String CERT_SN 	  = this.request.getParameter("CERT_SN");	 // 憑證序號
			
			String rtnMsg = "";   // 回傳訊息
			String acntIndt = ""; // 帳號識別碼
			String realAcnt = ""; // 綁定實體帳號 
			String pltfStts = ""; // 會員平台服務狀態
			
			resBesn.setMSG_NO(MSG_NO);
			
			List<CustAcntLog> acntLogs = new ArrayList<CustAcntLog>();
			CustAcntLog acntLog = null;
			EcData ecData = null;
			CustData custData = null;
			CustPltf custPltf = null;
			CustAcntLink acntLink = null;
			
			CustAcntLogHome acntLogHome = new CustAcntLogHome(conn);
			EcDataHome ecDataHome = new EcDataHome(conn);
			CustDataHome custDataHome = new CustDataHome(conn);
			CustPltfHome custPltfHome = new CustPltfHome(conn);
			CustAcntLinkHome acntLinkHome = new CustAcntLinkHome(conn);
			
			// 依據 平台代碼(EC_ID) & 查詢訊息序號(SER_MSG_NO) 取得會員連結記錄, 並檢核資料是否存在
			acntLogs = acntLogHome.fetchCustAcntLog(EC_ID, SER_MSG_NO);
			if (acntLogs.isEmpty() || acntLogs.size() != 1) {
				resBesn.setRTN_CODE("5025");
        		resBesn.setRTN_MSG(new TbCodeHelper("5025", "01").getTbCodeMsg());
				return resBesn;
			}
			
			// 檢核 平台會員代號是否正確
			acntLog = acntLogs.get(0);
			if (!acntLog.EC_USER.equals(EC_USER)) {
				resBesn.setRTN_CODE("5025");
        		resBesn.setRTN_MSG(new TbCodeHelper("5025", "01").getTbCodeMsg());
				return resBesn;
			}

			// 檢核使用者身分證字號是否與資料庫一致
			if (!acntLog.CUST_ID.equals(CUST_ID)) {
				resBesn.setRTN_CODE("5025");
        		resBesn.setRTN_MSG(new TbCodeHelper("5025", "01").getTbCodeMsg());
				return resBesn;
			}
			
			// 依據 平台代碼(EC_ID) 取得平台資料, 檢核平台資料是否存在
			ecData = ecDataHome.fetchEcDataByKey(acntLog.EC_ID);
			if (StringUtil.isBlank(ecData)) {
				resBesn.setRTN_CODE("5011");
        		resBesn.setRTN_MSG(new TbCodeHelper("5011", "01").getTbCodeMsg());
				return resBesn;
			}
			
			// 依據 身分證字號(CUST_ID), 取得會員資料, 並檢核會員資料是否存在
			custData = custDataHome.fetchCustDataByKey(acntLog.CUST_ID);
			if (StringUtil.isBlank(custData)) {
				resBesn.setRTN_CODE("5002");
        		resBesn.setRTN_MSG(new TbCodeHelper("5002", "01").getTbCodeMsg());
				return resBesn;
			}
			
			// 依據 身分證字號(CUST_ID), 平台代碼(EC_ID), 平台會員代號(EC_USER), 帳號識別碼(ACNT_INDT) 
			// 取得會員連結帳號資料, 並檢核會員連結資料是否存在
			acntLink = acntLinkHome.getCustAcntLinkByAcntIdnt(acntLog.CUST_ID, acntLog.EC_ID, acntLog.EC_USER, acntLog.ACNT_INDT);
			if (StringUtil.isBlank(acntLink)) {
				resBesn.setRTN_CODE("5021");
        		resBesn.setRTN_MSG(new TbCodeHelper("5021", "01").getTbCodeMsg());
				return resBesn;
			}
			
			// 檢核各服務狀態
			custPltf = custPltfHome.fetchCustPltfByKey(EC_ID, CUST_ID);
			if (!StringUtil.isBlank(custPltf)) {
				pltfStts = custPltf.STTS;
			}
			
			rtnMsg = CommonUtil.checkStatus("03", ecData.STTS, custData.STTS, pltfStts, acntLink.STTS);
			if (!StringUtil.isBlank(rtnMsg)) {
				resBesn.setRTN_CODE(rtnMsg);
        		resBesn.setRTN_MSG(new TbCodeHelper(rtnMsg, "01").getTbCodeMsg());
				return resBesn;
			}
			
			// 依據 狀態(CUST_ACNT_LOG.STTS), 處理回傳值 
			LOG.info("[AcntLog]: " + acntLog.STTS);
			if ("00".equals(acntLog.STTS) || "01".equals(acntLog.STTS)) {
				acntIndt = acntLog.ACNT_INDT;
//				realAcnt = CommonUtil.relAcntMask(acntLog.REAL_ACNT);
				if("N".equals(ecData.SHOW_REAL_ACNT)){
					realAcnt = CommonUtil.relAcntMask(acntLog.REAL_ACNT);
				}else{
					realAcnt = acntLog.REAL_ACNT;
				}
				resBesn.setLINK_GRAD(acntLog.GRAD);
			}
			
			// =============== 回傳訊息 ===============
			if("00".equals(acntLog.STTS) || "02".equals(acntLog.STTS)){ //00-連結成功 or 02-取消成功
	     		resBesn.setRTN_CODE("0000");
	     	}else if("01".equals(acntLog.STTS) || "03".equals(acntLog.STTS)){ //01-連結失敗 or 03-取消失敗
	     		resBesn.setRTN_CODE(acntLog.ERR_CODE);
	     	}
			
			resBesn.setRTN_MSG(new TbCodeHelper(resBesn.getRTN_CODE(), "01").getTbCodeMsg());
			resBesn.setEC_ID(acntLog.EC_ID);	 // 平台代碼
			resBesn.setEC_USER(acntLog.EC_USER); // 平台會員代號
			resBesn.setINDT_ACNT(acntIndt);		 // 帳號識別碼
			resBesn.setLINK_ACNT(realAcnt);		 // 綁定實體帳號
			
			LOG.info("[帳號識別碼]: " + resBesn.getINDT_ACNT() + " [綁定實體帳號]: " + resBesn.getLINK_ACNT() + " [身分認證等級]: " + resBesn.getLINK_GRAD());
			
		} catch (DBException e) {
			LOG.error("[aclinkQuery DBException]: ", e);
			throw e;
			
		} 
		catch (Exception e) {
			LOG.error("[aclinkQuery Exception]: ", e);
			throw new ApplicationException(e, "SYS_ERR");
			
		} finally {
			if (conn != null) {
				APSystem.returnConnection(conn, APSystem.DB_ACLINK);
			}
			LOG.info(DateUtil.formateDateTime(DateUtil.getCurrentTime("DT", "AD"))+" 結束執行平台交易結果查詢  ["+operation+"]");
		}
		
		return resBesn;
	}
}
