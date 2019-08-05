/**
 * @(#) ACLinkPayQueryService.java
 *
 * Directions: 平台交易結果查詢
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *  v1.00, 2016/03/25, Eason Hsu
 *   1) JIRA-Number, First release
 *  v1.01, 2016/11/29, Ada Chen
 *   1) TSBACL-134, 1045 電文回應錯誤時, 錯誤代碼拆解錯誤
 *  v1.02, 2016/12/02, Ada Chen
 *   1) TSBACL-138, 調整不顯示 Bancs訊息, 只寫 LOG
 *  v1.03, 2017/09/13, Eason Hsu
 *   1) TSBACL-161, [Fortify] Null Dereference
 *  v1.04, 2017/09/29, Eason Hsu
 *   1) TSBACL-161, [Fortify] Null Dereference
 */
package com.hitrust.bank.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.hitrust.acl.APSystem;
import com.hitrust.acl.common.TransactionControl;
import com.hitrust.acl.exception.ApplicationException;
import com.hitrust.acl.exception.DBException;
import com.hitrust.acl.response.AbstractResponseBean;
import com.hitrust.acl.service.AbstractServiceModel;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.common.CommonUtil;
import com.hitrust.bank.common.TbCodeHelper;
import com.hitrust.bank.dao.beans.CustAcntLink;
import com.hitrust.bank.dao.beans.CustData;
import com.hitrust.bank.dao.beans.CustPltf;
import com.hitrust.bank.dao.beans.EcData;
import com.hitrust.bank.dao.beans.TrnsData;
import com.hitrust.bank.dao.home.CustAcntLinkHome;
import com.hitrust.bank.dao.home.CustDataHome;
import com.hitrust.bank.dao.home.CustPltfHome;
import com.hitrust.bank.dao.home.DayCrdtContHome;
import com.hitrust.bank.dao.home.EcDataHome;
import com.hitrust.bank.dao.home.MnthCrdtContHome;
import com.hitrust.bank.dao.home.TrnsDataHome;
import com.hitrust.bank.response.ACLinkPayResBean;
import com.hitrust.bank.telegram.TelegramBo;
import com.hitrust.bank.telegram.res.TransactionResponseInfo;

public class ACLinkPayQueryService extends AbstractServiceModel {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(ACLinkPayQueryService.class);

	public ACLinkPayQueryService(String service, String operation, AbstractResponseBean resBean, HttpServletRequest request, HttpServletResponse response) {
		super(service, operation, resBean, request, response);
	}

	@Override
	public AbstractResponseBean process() throws ApplicationException, DBException {
		
		LOG.info(DateUtil.formateDateTime(DateUtil.getCurrentTime("DT", "AD"))+" 開始執行平台交易結果查詢  ["+operation+"]");
		// 取得 connection
		Connection conn = APSystem.getConnection(APSystem.DB_ACLINK);
		
		
		
		ACLinkPayResBean resBesn = (ACLinkPayResBean) this.resBean; 
		String rtnCode = "9999";
		
		try {
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
			
			String pltfStts = ""; // 會員平台服務狀態
			
			resBesn.setMSG_NO(MSG_NO);
			resBesn.setEC_ID(EC_ID);
			resBesn.setEC_USER(EC_USER);
			
			EcData ecData = null; 	  	  // 平台資料
			TrnsData trnsData = null; 	  // 交易資料
			CustData custData = null;	  // 會員資料
			CustPltf custPltf = null;	  // 會員平台服務
			CustAcntLink acntLink = null; // 會員帳號連結資料
			TrnsData originalData = null; // 原訂單資料
			
			EcDataHome ecDataHome = new EcDataHome(conn);
			TrnsDataHome trnsDataHome = new TrnsDataHome(conn);
			CustDataHome custDataHome = new CustDataHome(conn);
			CustPltfHome pltfHome = new CustPltfHome(conn);
			CustAcntLinkHome acntLinkHome = new CustAcntLinkHome(conn);
			
			// 依據 平台代碼 & 查誴訊息序號 取得交易資料
			trnsData = trnsDataHome.fetchTrnsDataByKey(EC_ID, SER_MSG_NO);
			
			if (StringUtil.isBlank(trnsData)) {
				rtnCode="7006";
				resBesn.setRTN_CODE(rtnCode);
				resBesn.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
				return resBesn;
			}
			
			// 交易類別(TRNS_DATA.TRNS_TYPE)為退款(B)時, 依據身分證字號與訂單編號取得原交易資料
			// v1.04, 修正 Fortify 白箱掃描(Null Dereference)
			if ("B".equals(trnsData.TRNS_TYPE) && StringUtil.isBlank(originalData)) {
				originalData = trnsDataHome.fetchOrderDataByOrderNo(trnsData.EC_ID, trnsData.CUST_ID, trnsData.ORDR_NO);
				
				if (StringUtil.isBlank(originalData)) {
					rtnCode="7001";
					resBesn.setRTN_CODE(rtnCode);
					resBesn.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
					return resBesn;
				}
			}
			
			// 依據 平台代碼(EC_ID) 取得平台資料
			ecData = ecDataHome.fetchEcDataByKey(EC_ID); 
			if (StringUtil.isBlank(ecData)) {
				rtnCode = "5011";
				resBesn.setRTN_CODE(rtnCode);
	     		resBesn.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
				return resBesn;
			}
			
			// 依據 身分證字號(CUST_ID), 取得會員資料, 並檢核會員資料是否存在
			custData = custDataHome.fetchCustDataByKey(CUST_ID);
			if (StringUtil.isBlank(custData)) {
				rtnCode = "5002";
				resBesn.setRTN_CODE(rtnCode);
	     		resBesn.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
				return resBesn;
			}

			// 依據 身分證字號(CUST_ID), 平台代碼(EC_ID), 平台會員代號(EC_USER), 帳號識別碼(ACNT_INDT) 
			// 取得會員連結帳號資料, 並檢核會員連結資料是否存在
			acntLink = acntLinkHome.getCustAcntLinkByAcntIdnt(CUST_ID, EC_ID, EC_USER, trnsData.ACNT_INDT);
			if (StringUtil.isBlank(acntLink)) {
				rtnCode = "5021";
				resBesn.setRTN_CODE(rtnCode);
	     		resBesn.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
				return resBesn;
			}
			
			// 檢核各服務狀態
			custPltf = pltfHome.fetchCustPltfByKey(EC_ID, CUST_ID);
			if (!StringUtil.isBlank(custPltf)) {
				pltfStts = custPltf.STTS;
			}
			
			rtnCode = CommonUtil.checkStatus("06", ecData.STTS, custData.STTS, pltfStts, acntLink.STTS);
			if (!StringUtil.isBlank(rtnCode)) {
				resBesn.setRTN_CODE(rtnCode);
				resBesn.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
				return resBesn;
			}
			
			//
			String hostCode = "";   //主機回應錯誤代碼
			String hostCodeMsg = "";
			String journalNum = "";  // 主機交易序號
			String trnsStts = "01";  //01-交易不明
			
			if ("01".equals(trnsData.TRNS_STTS)) { //01-交易不明
				
				if(StringUtil.isBlank(trnsData.TELE_NO)){
					//直接回應交易不明, and return
					rtnCode = "7011";
					resBesn.setRTN_CODE(rtnCode);
		     		resBesn.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
					return resBesn;
				}
				
				// 發送 [交易結果查詢電文] 查詢交易結果
				try {
					TransactionResponseInfo transactionResponseInfo = (TransactionResponseInfo) new TelegramBo().sendFCB91103W(trnsData);
					String outPutCode = transactionResponseInfo.getOUTPUT_CODE();
					LOG.debug("OUTPUT_CODE="+outPutCode);
					if("03".equals(outPutCode)){ //03-成功
						trnsStts = "02"; //交易成功
						journalNum = transactionResponseInfo.getJRNL_NO(); //主機交易序號
					}else{ //01-失敗
						trnsStts = "03"; //交易失敗
						//get 下行.主機回應錯誤代碼, Error Message No
						hostCode = transactionResponseInfo.getS_HOST_CODE(); //v1.01, 取得 error code
						if(!StringUtil.isBlank(hostCode)){
							hostCodeMsg = new TbCodeHelper(hostCode, "02").getTbCodeMsg();
							LOG.error(hostCodeMsg); //v1.02, 調整不顯示 Bancs訊息, 只寫 LOG
						}
					}
					
				} catch (Exception e) {
					LOG.error("[Exception]: ", e);
					//errMsg = e.getMessage();
				}
				
			}else{
				//直接回覆電商平台 交易結果
				if("02".equals(trnsData.TRNS_STTS)){ //02-交易成功
					rtnCode = "0000";
					resBesn.setRTN_CODE(rtnCode);
					resBesn.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
				}else if("03".equals(trnsData.TRNS_STTS)){ //03-交易失敗
					resBesn.setRTN_CODE(trnsData.ERR_CODE);
					String rtnMsg = new TbCodeHelper(trnsData.ERR_CODE, "01").getTbCodeMsg();
					//v1.02, 調整不顯示 Bancs訊息, 只寫 LOG
					if(!StringUtil.isBlank(trnsData.HOST_CODE)){
						LOG.error("("+  new TbCodeHelper(trnsData.HOST_CODE, "02").getTbCodeMsg() +")");
					}
					resBesn.setRTN_MSG(rtnMsg);
				}
				return resBesn;
			}
			
			// =============== 更新電文回覆結果 ===============
			if ("02".equals(trnsStts) || "03".equals(trnsStts)) {
				TransactionControl.transactionBegin(conn);
				
				String errCode = "0000";
				if ("03".equals(trnsStts)){
					errCode = "7010";
				}
				trnsData.TRNS_STTS = trnsStts;
				trnsDataHome.updateTrnsDataByKey(EC_ID, SER_MSG_NO, trnsStts, errCode, hostCode, journalNum);
				
				
				// 回沖每日/月累計交易額
				if (("A".equals(trnsData.TRNS_TYPE) && "03".equals(trnsStts)) || ("B".equals(trnsData.TRNS_TYPE) && "02".equals(trnsStts))) {
					//
					String trnsDttm = CommonUtil.formatDatetimeToDateStr(trnsData.TRNS_DTTM);
					trnsDttm = DateUtil.revertDateTime(trnsDttm);
					String trnsDate = trnsDttm.substring(0, 8); // 交易日期 YYYYMMDD
					String trnsMoth = trnsDttm.substring(0, 6); // 交易月份 YYYYMM
					
					String sysDttm = DateUtil.getCurrentTime("DT", "AD");
					String sysDate = sysDttm.substring(0, 8);	// 系統日期 YYYYMMDD
					String sysMoth = sysDttm.substring(0, 6);	// 系統月份 YYYYMM
					
					String originalTrnsDttm = "";
					String originalTrnsDate = "";
					String originalTrnsMonth= "";
					
					// v1.03, 修正 Fortify 白箱掃描(Null Dereference)
					if ("B".equals(trnsData.TRNS_TYPE) && !StringUtil.isBlank(originalData)){
						originalTrnsDttm = CommonUtil.formatDatetimeToDateStr(originalData.TRNS_DTTM);
						originalTrnsDttm = DateUtil.revertDateTime(originalTrnsDttm);
						originalTrnsDate = originalTrnsDttm.substring(0, 8); // 原交易日期 YYYYMMDD
						originalTrnsMonth = originalTrnsDttm.substring(0, 6);// 原交易月份 YYYYMM
					}
					
					DayCrdtContHome crdtContHome = new DayCrdtContHome(conn);
					MnthCrdtContHome mnthCrdtContHome = new MnthCrdtContHome(conn);
					
					// 扣款交易失敗 回沖日/月額度金額
					if ("A".equals(trnsData.TRNS_TYPE) && "03".equals(trnsStts)) {
						
						// YYYYMMDD(回沖日額度金額)
						if (trnsDate.equals(sysDate)) {
							crdtContHome.backflush(trnsData.ACNT_INDT, trnsDate, trnsData.TRNS_AMNT);
						}
						
						// YYYYMM(回沖月額度金額)
						if (trnsMoth.equals(sysMoth)) {
							mnthCrdtContHome.backflush(trnsData.ACNT_INDT, trnsMoth, trnsData.TRNS_AMNT);
						}
					}
					
					// 退款交易成功 回沖日/月額度金額
					// v1.03, 修正 Fortify 白箱掃描(Null Dereference)
					if ("B".equals(trnsData.TRNS_TYPE) && "02".equals(trnsStts) && !StringUtil.isBlank(originalData)) {
						
						// 依據 PK 更新 原訂資料單餘額
						trnsDataHome.updateTrnsDataBackAmntByKey(EC_ID, originalData.EC_MSG_NO, (originalData.BACK_AMNT - trnsData.TRNS_AMNT));
						
						// YYYYMMDD(回沖日額度金額)
						if (originalTrnsDate.equals(sysDate)) {
							crdtContHome.backflush(originalData.ACNT_INDT, originalTrnsDate, trnsData.TRNS_AMNT);
						}
						
						// YYYYMM(回沖月額度金額)
						if (originalTrnsMonth.equals(sysMoth)) {
							mnthCrdtContHome.backflush(originalData.ACNT_INDT, originalTrnsMonth, trnsData.TRNS_AMNT);
						}
					}
				}
				TransactionControl.trasactionCommit(conn);
			}
			
			
			if("01".equals(trnsStts)){ //01-交易不明
				rtnCode="7011";
				resBesn.setRTN_CODE(rtnCode);
				resBesn.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
        	}else if("02".equals(trnsStts)){ //02-交易成功
        		rtnCode="0000";
        		resBesn.setRTN_CODE(rtnCode);
        		resBesn.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
        	}else if("03".equals(trnsStts)){ //03-交易失敗
        		rtnCode="7010";
        		resBesn.setRTN_CODE(rtnCode);
        		resBesn.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg()); //v1.02, 調整不顯示 Bancs訊息
        	}
			return resBesn;
			
		} catch (DBException e) {
			LOG.error("[aclinkPayQuery DBException]: ", e);
			TransactionControl.transactionRollback(conn);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[aclinkPayQuery Exception]: ", e);
			TransactionControl.transactionRollback(conn);
			throw new ApplicationException(e, "SYS_ERR");
			
		} finally {
			if (conn != null) {
				TransactionControl.transactionEnd(conn);
				APSystem.returnConnection(conn, APSystem.DB_ACLINK);
			}
			LOG.info(DateUtil.formateDateTime(DateUtil.getCurrentTime("DT", "AD"))+" 結束執行平台交易結果查詢  ["+operation+"]");
		}
	}
}
