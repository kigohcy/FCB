/**
 * @(#) ACLinkCancelService.java
 *
 * Directions: 取消連結帳戶綁定
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/30, Eason Hsu
 *    1) JIRA-Number, First release
 *   
 */

package com.hitrust.bank.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.hitrust.acl.APSystem;
import com.hitrust.acl.common.TransactionControl;
import com.hitrust.acl.common.UUIDGen;
import com.hitrust.acl.exception.ApplicationException;
import com.hitrust.acl.exception.DBException;
import com.hitrust.acl.exception.UtilException;
import com.hitrust.acl.mail.MailUtil;
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
import com.hitrust.bank.json.ACLink;
import com.hitrust.bank.response.ACLinkCancelResBean;
import com.hitrust.bank.telegram.TelegramBo;
import com.hitrust.bank.telegram.res.MemberMsgResponseInfo;
import com.hitrust.bank.telegram.res.NotificationHostMsgResponseInfo;

public class ACLinkCancelService extends AbstractServiceModel {

	// Log4j
	private static Logger LOG = Logger.getLogger(ACLinkCancelService.class);

	public ACLinkCancelService(String service, String operation, AbstractResponseBean resBean,
			HttpServletRequest request, HttpServletResponse response) {
		super(service, operation, resBean, request, response);
	}

	@Override
	public AbstractResponseBean process() throws ApplicationException, DBException {

		LOG.info(DateUtil.formateDateTime(DateUtil.getCurrentTime("DT", "AD"))+" 開始執行取消連結帳戶綁定  ["+operation+"]");
		
		
		Connection conn = null;

		ACLinkCancelResBean resBesn = (ACLinkCancelResBean) this.resBean;

		String MSG_NO = this.request.getParameter("MSG_NO"); // 訊息代碼
		String EC_ID = this.request.getParameter("EC_ID"); // 平台代碼
		String EC_USER = this.request.getParameter("EC_USER"); // 平台會員代號
		String CUST_ID = this.request.getParameter("CUST_ID"); // 使用者身分證字號
		String INDT_ACNT = this.request.getParameter("INDT_ACNT"); // 帳號識別碼
		String SIGN_TIME = this.request.getParameter("SIGN_TIME"); // 簽章日期時間
		String SIGN_VALUE = this.request.getParameter("SIGN_VALUE"); // 簽章值
		String CERT_SN = this.request.getParameter("CERT_SN"); // 憑證序號

		String acntLogStts = "02"; // 02-取消成功

		int count = 0;
		List<CustAcntLink> acntLinks = null;
		CustAcntLog acntLog = null;
		EcData ecData = null;
		CustData custData = null;
		CustAcntLink acntLink = null;

		try {
			conn = APSystem.getConnection(APSystem.DB_ACLINK);
			
			DatabaseMetaData mtdt = conn.getMetaData();
			LOG.info("DB 使用者: [" + mtdt.getUserName()+"]");
			LOG.info("用戶IP:["+request.getRemoteAddr()+"]");
			LOG.info("用戶電腦名稱:["+request.getRemoteHost()+"]");

			String rtnMsg = "";

			// =============== 設定回傳訊息 ===============
			resBesn.setMSG_NO(MSG_NO);
			resBesn.setEC_ID(EC_ID);
			resBesn.setEC_USER(EC_USER);

			CustAcntLogHome acntLogHome = new CustAcntLogHome(conn);
			EcDataHome ecDataHome = new EcDataHome(conn);
			CustDataHome custDataHome = new CustDataHome(conn);
			CustAcntLinkHome acntLinkHome = new CustAcntLinkHome(conn);
			CustPltfHome custPltfHome = new CustPltfHome(conn);

			// 依據 平台代碼(EC_ID) 取得平台資料, 檢核平台資料是否存在
			LOG.info("========== start 檢核平台資料是否存在 ==========");
			ecData = ecDataHome.fetchEcDataByKey(EC_ID);
			if (StringUtil.isBlank(ecData)) {
				LOG.info("= [EC_ID]: " + EC_ID + " 資料不存在");
				acntLogStts = "03"; // 03-取消失敗
				resBesn.setRTN_CODE("5011");
				resBesn.setRTN_MSG(new TbCodeHelper("5011", "01").getTbCodeMsg());
				return resBesn;
			}
			LOG.info("========== end 檢核平台資料是否存在 ==========");

			// 依據 身分證字號(CUST_ID), 取得會員資料, 並檢核會員資料是否存在
			LOG.info("========== start 檢核會員資料是否存在 ==========");
			custData = custDataHome.fetchCustDataByKey(CUST_ID);
			if (StringUtil.isBlank(custData)) {
				LOG.info("= [CUST_ID]: " + CommonUtil.stringMask(CUST_ID, 3, 3, "*") + " 資料不存在");
				acntLogStts = "03";
				resBesn.setRTN_CODE("5002");
				resBesn.setRTN_MSG(new TbCodeHelper("5002", "01").getTbCodeMsg());
				return resBesn;
			}
			LOG.info("========== end 檢核會員資料是否存在 ==========");

			// 依據 平台代碼(EC_ID), 身分證字號(CUST_ID) & 帳號識別碼(INDT_ACNT) 取得會員連結資料, 並檢核資料是否存在
			LOG.info("========== start 檢核會員連結資料是否存在 ==========");
			acntLinks = acntLinkHome.fetchCustAcntLink(EC_ID, CUST_ID, INDT_ACNT);
			if (acntLinks.isEmpty() || acntLinks.size() > 1) {
				LOG.info("= [INDT_ACNT]: " + INDT_ACNT + " 資料不存在, 或資料筆數 " + acntLinks.size() + " > 1");
				acntLogStts = "03";
				resBesn.setRTN_CODE("5021");
				resBesn.setRTN_MSG(new TbCodeHelper("5021", "01").getTbCodeMsg());
				return resBesn;
			}
			LOG.info("========== end 檢核會員連結資料是否存在 ==========");

			// 檢核 平台會員代號是否正確
			LOG.info("========== start 檢核平台會員代號是否正確 ==========");
			acntLink = acntLinks.get(0);
			if (!acntLink.EC_USER.equals(EC_USER)) {
				LOG.info("[input]: " + EC_USER + " [exist data]: " + acntLink.EC_USER);
				acntLogStts = "03";
				resBesn.setRTN_CODE("5021");
				resBesn.setRTN_MSG(new TbCodeHelper("5021", "01").getTbCodeMsg());
				return resBesn;
			}
			LOG.info("========== end 檢核平台會員代號是否正確 ==========");

			// 檢核使用者身分證字號是否與資料庫一致
			LOG.info("========== start 檢核使用者身分證字號是否與資料庫一致 ==========");
			if (!acntLink.CUST_ID.equals(CUST_ID)) {
				LOG.info("= [input CUST_ID:" + CUST_ID + "]" + " [db Cust_Acnt_Link.CUST_ID:" + acntLink.CUST_ID + "]");
				acntLogStts = "03";
				resBesn.setRTN_CODE("5021");
				resBesn.setRTN_MSG(new TbCodeHelper("5021", "01").getTbCodeMsg());
				return resBesn;
			}
			LOG.info("========== end 檢核使用者身分證字號是否與資料庫一致 ==========");

			// 依據 身分證字號(CUST_ID), 平台代碼(EC_ID), 平台會員代號(EC_USER), 帳號識別碼(ACNT_INDT)
			// 取得會員連結帳號資料, 並檢核會員連結資料是否存在
			LOG.info("========== start 檢核會員連結資料是否存在 ==========");
			acntLink = acntLinkHome.getCustAcntLinkByAcntIdnt(CUST_ID, EC_ID, EC_USER, INDT_ACNT);
			if (StringUtil.isBlank(acntLink)) {
				LOG.info("= [CUST_ID: " + CUST_ID + "EC_ID: " + EC_ID + "EC_USER:" + EC_USER + "INDT_ACNT:" + INDT_ACNT
						+ "查無資料]");
				acntLogStts = "03";
				resBesn.setRTN_CODE("5021");
				resBesn.setRTN_MSG(new TbCodeHelper("5021", "01").getTbCodeMsg());
				return resBesn;
			}
			LOG.info("========== end 檢核會員連結資料是否存在 ==========");

			// 檢核各服務狀態
			LOG.info("========== start 檢核各服務狀態 ==========");
			String pltfStts = ""; // 會員平台服務狀態
			CustPltf pltf = custPltfHome.fetchCustPltfByKey(EC_ID, CUST_ID);

			if (!StringUtil.isBlank(pltf)) {
				pltfStts = pltf.STTS;
			}

			rtnMsg = CommonUtil.checkStatus("02", ecData.STTS, custData.STTS, pltfStts, acntLink.STTS);
			if (!StringUtil.isBlank(rtnMsg)) {
				LOG.info("= rtnMsg: " + rtnMsg);
				acntLogStts = "03";
				resBesn.setRTN_CODE(rtnMsg);
				resBesn.setRTN_MSG(new TbCodeHelper(rtnMsg, "01").getTbCodeMsg());
				return resBesn;
			}
			LOG.info("========== end 檢核各服務狀態 ==========");

			// 若無發送主機綁定帶this.sendTelegramFetchHost();
			String rtnCode = this.sendTelegramFetchHost(acntLink);

			if ("5026".equals(rtnCode)) {// 綁定電文發送異常
				SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
			    Date now = df.parse(df.format(new Date()));
			    Date beginTime = df.parse("00:00");
			    Date endTime = df.parse("00:15");
			    if(!CommonUtil.belongCalendar(now,beginTime,endTime)) {
			    		rtnCode = this.sendTelegramFetchHost(acntLink);
			    }
			}
			
			if (StringUtil.isBlank(rtnCode)) {
				TransactionControl.transactionBegin(conn);
				// 異動會員帳號連結
				acntLinkHome.updateCustAcntLinkSttsByKey(acntLink.CUST_ID, acntLink.EC_ID, acntLink.EC_USER,
						acntLink.REAL_ACNT, "02");

				// 檢核是否進行會員平台終止處理
				acntLinks = acntLinkHome.fetchCustAcntLink(EC_ID, CUST_ID);

				for (CustAcntLink link : acntLinks) {
					if ("02".equals(link.STTS)) {
						count++;
					}
				}

				if (count == acntLinks.size()) {
					custPltfHome.updateCustPltfSttsByKey(EC_ID, CUST_ID, "02");

					// 新增會員帳號連結記錄
					acntLog = new CustAcntLog();
					acntLog.LOG_NO = UUIDGen.genUUID();
					acntLog.CUST_ID = CUST_ID;
					acntLog.EC_ID = EC_ID;
					acntLog.CRET_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
					acntLog.STTS = "06"; // 06-終止
					acntLog.CUST_SERL = custData.CUST_SERL;
					acntLog.EXEC_SRC = "D"; // D-系統
					acntLogHome.insert(acntLog);
				}

				// =============== 執行成功設定回傳訊息 ===============
				resBesn.setRTN_CODE("0000");
				resBesn.setRTN_MSG(new TbCodeHelper("0000", "01").getTbCodeMsg());

				TransactionControl.trasactionCommit(conn);
			} else {
				acntLogStts = "03";
				resBesn.setRTN_CODE(rtnCode);
				resBesn.setRTN_MSG(new TbCodeHelper(rtnMsg, "01").getTbCodeMsg());
				return resBesn;
			}
		} catch (DBException e) {
			acntLogStts = "03";
			resBesn.setRTN_CODE("9997");
			LOG.error("[aclinkcancel DBException]: ", e);
			TransactionControl.transactionRollback(conn);
			throw e;

		} catch (Exception e) {
			acntLogStts = "03";
			resBesn.setRTN_CODE("9999");
			LOG.error("[aclinkcancel Exception]: ", e);
			TransactionControl.transactionRollback(conn);
			throw new ApplicationException(e, "SYS_ERR");

		} finally {

			// 新增會員連結帳號紀錄
			try {
				this.insertCustAcntLog(MSG_NO, EC_ID, EC_USER, CUST_ID, INDT_ACNT, acntLogStts, resBesn.getRTN_CODE(),
						ecData, custData, acntLink, conn);

			} catch (UtilException e) {

			}

			if (conn != null) {
				TransactionControl.transactionEnd(conn);
				APSystem.returnConnection(conn, APSystem.DB_ACLINK);
			}
			
			LOG.info(DateUtil.formateDateTime(DateUtil.getCurrentTime("DT", "AD"))+" 結束執行取消連結帳戶綁定  ["+operation+"]");
		}

		
		return resBesn;
	}

	private void insertCustAcntLog(String msgNo, String ecId, String ecUser, String custId, String indtAcnt,
			String stts, String rtnCode, EcData ecData, CustData custData, CustAcntLink acntLink, Connection conn)
			throws DBException, UtilException {

		String ecName = "";
		String custName = "";
		String custType = "";
		String email = "";

		TransactionControl.transactionBegin(conn);
		CustAcntLog acntLog = new CustAcntLog();
		CustAcntLogHome acntLogHome = new CustAcntLogHome(conn);

		acntLog.LOG_NO = UUIDGen.genUUID();
		acntLog.CUST_ID = custId;
		acntLog.EC_ID = ecId;
		acntLog.EC_USER = ecUser;
		acntLog.ACNT_INDT = indtAcnt;
		acntLog.STTS = stts;
		if (custData != null) {
			acntLog.CUST_SERL = custData.CUST_SERL;
			custName = custData.CUST_NAME;
			custType = custData.CUST_TYPE;
		}
		if (acntLink != null) {
			acntLog.REAL_ACNT = acntLink.REAL_ACNT;
			acntLog.GRAD = acntLink.GRAD;
			acntLog.GRAD_TYPE = acntLink.GRAD_TYPE;
		}
		acntLog.CRET_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
		acntLog.EXEC_SRC = "A"; // A-平台閘道
		acntLog.EC_MSG_NO = msgNo;
		acntLog.ERR_CODE = rtnCode;
		acntLogHome.insert(acntLog);
		// commit
		TransactionControl.trasactionCommit(conn);

		// TODO 發送 [CIF電文] 取得客戶email
		if (!StringUtil.isBlank(custType)) {
			// 發送電文
			MemberMsgResponseInfo mberMsg = null;
			try {
				mberMsg = (MemberMsgResponseInfo) new TelegramBo().sendFCB919713_06(custId);
				if ("03".equals(mberMsg.getOUTPUT_CODE())) { // 成功
					if ("000".equals(mberMsg.getSTATUS().trim())) { // 只針對 000:正常 取用資料,其餘均視為失敗
						email = mberMsg.getEMAIL();
					}
				}
			} catch (Exception e) {
				LOG.error("Exception:" + e.toString(), e);
			}
		}

		if (ecData != null) {
			ecName = ecData.EC_NAME_CH;
		}
		if (!StringUtil.isBlank(email)) {
			try {
				this.sendMail(acntLog, ecName, custName, email);
			} catch (Exception e) {
				LOG.error("========== Mail 發送失敗 ==========", e);
			}
		}

	}

	/**
	 * 發送 Mail
	 * 
	 * @param acntLog
	 * @param ecData
	 * @param custData
	 * @throws Exception
	 */
	private void sendMail(CustAcntLog acntLog, String ecName, String custName, String email) throws Exception {
		// m by jeff 目前一銀只有取消綁定成功才發mail
		if ("02".equals(acntLog.STTS)) { // 02-取消綁定成功
			String subject = "";
			String templateFile = "";
			String mailContent = "";

			if ("02".equals(acntLog.STTS)) { // 02-取消綁定成功
				subject = APSystem.getProjectParam("MAIL_SUBJECT3");
				templateFile = "ACL-G-03.vm";
			} else if ("03".equals(acntLog.STTS)) { // 03-取消綁定失敗
				subject = APSystem.getProjectParam("MAIL_SUBJECT4");
				templateFile = "ACL-G-04.vm";
			}

			HashMap<String, String> mailMap = new HashMap<String, String>();
			// =============== 明細資料 ===============
			mailMap.put("EC_USER_NAME", CommonUtil.nameMask(custName)); // 使用者名稱
			mailMap.put("CRET_DTTM", acntLog.CRET_DTTM.substring(0, 10)); // 執行日期
			mailMap.put("EC_NAME_CH", ecName); // 電商平台
			mailMap.put("EC_USER", acntLog.EC_USER); // 平台會員代號
			mailMap.put("REAL_ACNT", CommonUtil.relAcntMask(acntLog.REAL_ACNT)); // 平台連結帳號
			if ("03".equals(acntLog.STTS)) {
				// =============== 錯誤原因 ===============
				mailMap.put("CODE_DESC", new TbCodeHelper(acntLog.ERR_CODE, "01").getTbCodeMsg());
			}

			mailContent = MailUtil.renderMailHtmlContent(mailMap, templateFile);

			new MailUtil().addMail(mailContent, subject, email, null, null, null);
		}
	}

	/**
	 * 發電文通知一銀主機取消綁定帳號
	 * 
	 * @param aclink
	 * @return
	 */
	private String sendTelegramFetchHost(CustAcntLink acntLink) {
		String rtnCode = "";
		String outPutCode = ""; // 電文回傳結果 03: 成功, 01: 失敗

		// 發電文通知一銀主機取消綁定帳號
		NotificationHostMsgResponseInfo notificationHostMsgResponseInfo = null;
		try {
			// 綁定: link, 解綁: cancel, 查詢: request
			notificationHostMsgResponseInfo = (NotificationHostMsgResponseInfo) new TelegramBo()
					.sendFCB91920Y(acntLink.REAL_ACNT, acntLink.EC_ID, "cancel");
			outPutCode = notificationHostMsgResponseInfo.getOUTPUT_CODE();
		} catch (Exception e) {
			LOG.error("[Exception]:", e);
			rtnCode = "5026"; // 連結帳號取消綁定處理異常...
			return rtnCode;
		}

		if ("03".equals(outPutCode)) {
			return rtnCode;
		} else {
			rtnCode = "1006"; // 取消綁定失敗....
			// get 下行.Error Message Number 記錄在 CUST_ACNT_LOG.HOST_CODE
			String hostCode = notificationHostMsgResponseInfo.getS_HOST_CODE(); // v1.06, 取得 error code
			if (!StringUtil.isBlank(hostCode)) {
				rtnCode = rtnCode + "," + hostCode;
			}
		}
		return rtnCode;
	}

	private String sendTelegramFetchHost() {
		return "";
	}
}
