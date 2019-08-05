/**
 * @(#) ACLinkRefundService.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 連結帳戶交易退款Service
 * 
 * Modify History:
 *  v1.00, 2016/03/29, Yann
 *   1) First release
 *  v1.01, 2016/12/02, Ada Chen
 *   1) TSBACL-138, 調整不顯示 Bancs訊息, 只寫 LOG
 *  v1.02, 2018/03/20
 *   1) 紀錄IP到TRNS_DATA
 */
package com.hitrust.bank.service;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.hitrust.acl.APSystem;
import com.hitrust.acl.common.TransactionControl;
import com.hitrust.acl.exception.ApplicationException;
import com.hitrust.acl.exception.DBException;
import com.hitrust.acl.mail.MailUtil;
import com.hitrust.acl.response.AbstractResponseBean;
import com.hitrust.acl.service.AbstractServiceModel;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.common.CommonUtil;
import com.hitrust.bank.common.TbCodeHelper;
import com.hitrust.bank.dao.beans.CustAcnt;
import com.hitrust.bank.dao.beans.CustAcntLink;
import com.hitrust.bank.dao.beans.CustData;
import com.hitrust.bank.dao.beans.CustPltf;
import com.hitrust.bank.dao.beans.EcData;
import com.hitrust.bank.dao.beans.TrnsData;
import com.hitrust.bank.dao.home.CustAcntHome;
import com.hitrust.bank.dao.home.CustAcntLinkHome;
import com.hitrust.bank.dao.home.CustDataHome;
import com.hitrust.bank.dao.home.CustPltfHome;
import com.hitrust.bank.dao.home.DayCrdtContHome;
import com.hitrust.bank.dao.home.EcDataHome;
import com.hitrust.bank.dao.home.MnthCrdtContHome;
import com.hitrust.bank.dao.home.TrnsDataHome;
import com.hitrust.bank.response.ACLinkRefundResBean;
import com.hitrust.bank.telegram.TelegramBo;
import com.hitrust.bank.telegram.res.MemberMsgResponseInfo;
import com.hitrust.bank.telegram.res.TransactionResponseInfo;

public class ACLinkRefundService extends AbstractServiceModel {
	// log4j
	static Logger LOG = Logger.getLogger(ACLinkRefundService.class);

	/**
	 * 
	 * @param service
	 * @param request
	 * @param response
	 */
	public ACLinkRefundService(String service, String operation, AbstractResponseBean resBean,
			HttpServletRequest request, HttpServletResponse response) {
		super(service, operation, resBean, request, response);
	}

	/**
	 * 
	 */
	public AbstractResponseBean process() throws ApplicationException, DBException {
		
		LOG.info(DateUtil.formateDateTime(DateUtil.getCurrentTime("DT", "AD"))+" 開始執行連結帳戶交易退款   ["+operation+"]");
		Connection conn = null;
		ACLinkRefundResBean resBean = null;

		try {
			//
			conn = APSystem.getConnection(APSystem.DB_ACLINK);
			
			DatabaseMetaData mtdt = conn.getMetaData();
			LOG.info("DB 使用者: [" + mtdt.getUserName()+"]");
			LOG.info("用戶IP:["+request.getRemoteAddr()+"]");
			LOG.info("用戶電腦名稱:["+request.getRemoteHost()+"]");
			//
			HashMap map = this.aclinkRefund(conn);

			resBean = (ACLinkRefundResBean) map.get("resBean");
			TrnsData refundData = (TrnsData) map.get("refundData");
			String CUST_SERL = (String) map.get("CUST_SERL");
			String ACNT_INDT = (String) map.get("ACNT_INDT");
			String REAL_ACNT = (String) map.get("REAL_ACNT"); // 會員入帳帳號
			String EC_REAL_ACNT = (String) map.get("EC_REAL_ACNT");// 電商轉出帳號
			String TELE_NO = (String) map.get("TELE_NO");
			String RESULT = (String) map.get("RESULT");
			String HOST_CODE = (String) map.get("HOST_CODE");
			String HOST_SEQ = (String) map.get("HOST_SEQ");
			String refundAmt = (String) map.get("REFUND_AMT");
			int refundAmount = Integer.parseInt(refundAmt);

			// update TRNS_DATA
			if (refundData != null) {
				// Begin Transaction.
				TransactionControl.transactionBegin(conn);

				refundData.setConnection(conn);
				refundData.CUST_SERL = CUST_SERL; // 會員服務序號
				refundData.REAL_ACNT = EC_REAL_ACNT; // 轉出帳號
				refundData.ACNT_INDT = ACNT_INDT; // 帳號識別碼
				refundData.TELE_NO = TELE_NO; // 電文序號
				if (!StringUtil.isBlank(RESULT)) {
					refundData.TRNS_STTS = RESULT; // 02-交易成功 or 03-交易失敗
				}
				if (!StringUtil.isBlank(HOST_CODE)) {
					refundData.HOST_CODE = HOST_CODE; // 主機回應代碼
				}
				refundData.ERR_CODE = resBean.getRTN_CODE(); // 錯誤代碼
				refundData.HOST_SEQ = HOST_SEQ; // 主機處理序號
				refundData.MDFY_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD")).substring(0,
						10);
				refundData.update();

				// commit
				TransactionControl.trasactionCommit(conn);
				// End Transaction.
				TransactionControl.transactionEnd(conn);

				//
				String CUST_NAME = (String) map.get("CUST_NAME");
				String CUST_TYPE = (String) map.get("CUST_TYPE");
				String email = "";

				// TODO 發送 [CIF電文] 取得客戶email
//				if (!StringUtil.isBlank(CUST_TYPE) && ("02".equals(refundData.TRNS_STTS))) {
				if ("02".equals(refundData.TRNS_STTS)) { // 一銀只有交易成功才需要發送email
					this.sendMail(map, refundData, CUST_NAME, REAL_ACNT, refundAmt);
				}
			} // end if

		} catch (DBException e) {
			LOG.error("DBException:" + e.toString(), e);
			TransactionControl.transactionRollback(conn);
			throw e;
		} catch (Exception e) {
			LOG.error("Exception:" + e.toString(), e);
			TransactionControl.transactionRollback(conn);
			throw new ApplicationException(e, "SYS_ERR");
		} finally {
			if (conn != null) {
				TransactionControl.transactionEnd(conn);
				APSystem.returnConnection(conn, APSystem.DB_ACLINK);
			}
			LOG.info(DateUtil.formateDateTime(DateUtil.getCurrentTime("DT", "AD"))+" 結束執行連結帳戶交易退款   ["+operation+"]");
		}

		return resBean;
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws ApplicationException
	 * @throws DBException
	 * @throws Exception
	 */
	private HashMap aclinkRefund(Connection conn) throws ApplicationException, DBException, Exception {
		ACLinkRefundResBean resBean = null;
		HashMap map = new HashMap();
		String rtnCode = "9999";

		// 訊息序號
		String msgNo = request.getParameter("MSG_NO");
		// 平台代碼
		String ecId = request.getParameter("EC_ID");
		// 平台會員代號
		String ecUser = request.getParameter("EC_USER");
		// 使用者身分證號
		String custId = request.getParameter("CUST_ID");
		// 退款交易時間(YYYYMMDDhhmmss)
		String refundTime = request.getParameter("REFUND_TIME");
		// 退款(原)訂單編號
		String trnsNo = request.getParameter("TRNS_NO");
		// 退款交易明細
		String refundNote = URLDecoder.decode(request.getParameter("REFUND_NOTE"));
		// 退款金額
		String refundAmt = request.getParameter("REFUND_AMT");
		// 簽章日期時間
		String signTime = request.getParameter("SIGN_TIME");
		// 簽章值
		String signValue = request.getParameter("SIGN_VALUE");
		// 憑證序號
		String certSn = request.getParameter("CERT_SN");

		String ecStts = "";
		String custStts = "";
		String linkStts = "";
		String realAccount = "";
		String orgTrnsDate = "";

		resBean = (ACLinkRefundResBean) this.resBean;
		resBean.setMSG_NO(msgNo);
		resBean.setEC_ID(ecId);
		resBean.setEC_USER(ecUser);

		map.put("REFUND_AMT", refundAmt);
		map.put("REFUND_TIME", refundTime);
		map.put("REFUND_NOTE", refundNote);

		int refundAmount = Integer.parseInt(refundAmt);

		// 查詢 原訂單資料
		TrnsDataHome trnsDataHome = new TrnsDataHome(conn);
		TrnsData orgTrnsData = trnsDataHome.fetchOrderDataByOrderNo(ecId, custId, trnsNo);
		if (orgTrnsData == null) {
			rtnCode = "7001";
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			map.put("resBean", resBean);
			map.put("RESULT", "03"); // 交易失敗
			return map;
		}

		// 原交易日期 (eg:2016-03-31 14:20:48.0000000)
		orgTrnsDate = DateUtil.revertDateTime(orgTrnsData.TRNS_DTTM.substring(0, 10));
		// 實體帳號
		realAccount = orgTrnsData.REAL_ACNT;
		map.put("REAL_ACNT", realAccount);

		// Begin Transaction.
		TransactionControl.transactionBegin(conn);

		// insert 退款交易資料(TRNS_DATA)
		TrnsData refundData = new TrnsData();
		refundData.setConnection(conn);
		refundData.EC_ID = ecId; // 平台代碼
		refundData.EC_MSG_NO = msgNo; // 平台訊息序號
		refundData.CUST_ID = custId; // 身分證字號
		refundData.EC_USER = ecUser; // 平台會員代碼
		refundData.TRNS_TYPE = "B"; // B-退款
		refundData.RECV_ACNT = realAccount; // 轉入帳號
		refundData.TRNS_DTTM = DateUtil.formateDateTimeForUser(refundTime);
		refundData.TRNS_AMNT = refundAmount; // 退款金額
		refundData.TRNS_STTS = "01"; // 01-交易不明
		refundData.ORDR_NO = trnsNo; // 訂單編號
		refundData.BACK_AMNT = 0; // 訂單餘額
		refundData.TRNS_NOTE = refundNote; // 交易備註
		refundData.MDFY_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
		// V1.02 紀錄IP到TRNS_DATA
		refundData.IP = request.getRemoteAddr();
		// V1.02 紀錄IP到TRNS_DATA End
		refundData.MIN_FEE = 0;
		refundData.MAX_FEE = 0;
		refundData.insert();
		// commit
		TransactionControl.trasactionCommit(conn);
		// End Transaction.
		TransactionControl.transactionEnd(conn);

		//20190619 Add 交易失敗時記錄上下行電文 Begin
		//map.put("refundData", refundData);
		//20190619 Add 交易失敗時記錄上下行電文 Begin

		// 取得 會員資料檔
		CustDataHome custDataHome = new CustDataHome(conn);
		CustData custData = custDataHome.fetchCustDataByKey(custId);
		if (custData == null || "02".equals(custData.STTS)) {
			rtnCode = "5002";
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			map.put("resBean", resBean);
			map.put("RESULT", "03"); // 交易失敗
			return map;
		} else {
			custStts = custData.STTS;
			map.put("CUST_NAME", custData.CUST_NAME);
			map.put("CUST_TYPE", custData.CUST_TYPE);
			map.put("CUST_SERL", custData.CUST_SERL);
		}

		// 取得 電商平台資料檔
		EcDataHome ecDataHome = new EcDataHome(conn);
		EcData ecData = ecDataHome.fetchEcDataByKey(ecId);

		if (ecData == null) {
			rtnCode = "5011";
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			map.put("resBean", resBean);
			map.put("RESULT", "03"); // 交易失敗
			return map;
		} else {
			ecStts = ecData.STTS;
			map.put("EC_NAME_CH", ecData.EC_NAME_CH);
			map.put("EC_REAL_ACNT", ecData.REAL_ACNT);
		}

		// 取得 會員帳號連結檔
		CustAcntLinkHome custAcntLinkHome = new CustAcntLinkHome(conn);
		CustAcntLink custAcntLink = custAcntLinkHome.getCustAcntLinkByPk(custId, ecId, ecUser, realAccount);
		if (custAcntLink == null) {
			LOG.debug("CustAcntLink not exist,custId=" + custId + "/ecId=" + ecId + "/ecUser=" + ecUser
					+ "/realAccount=" + realAccount);
			rtnCode = "5021";
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			map.put("resBean", resBean);
			map.put("RESULT", "03"); // 交易失敗
			return map;
		} else {
			linkStts = custAcntLink.STTS;
			map.put("ACNT_INDT", custAcntLink.ACNT_INDT);
		}

		// 取得 會員帳號檔
		CustAcntHome CustAcntHome = new CustAcntHome(conn);
		CustAcnt custAcnt = CustAcntHome.fetchCustAcntByKey(custId, realAccount);
		if (custAcnt == null) {
			LOG.debug("CustAcnt not exist,custId=" + custId + "/realAccount=" + realAccount);
			rtnCode = "5021";
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			map.put("resBean", resBean);
			map.put("RESULT", "03"); // 交易失敗
			return map;
		}

		// 檢核狀態
		String pltfStts = ""; // 會員平台服務狀態
		CustPltfHome pltfHome = new CustPltfHome(conn);
		CustPltf pltf = pltfHome.fetchCustPltfByKey(ecId, custId);
		if (!StringUtil.isBlank(pltf)) {
			pltfStts = pltf.STTS;
		}

		String msg = CommonUtil.checkStatus("05", ecStts, custStts, pltfStts, linkStts);
		if (!StringUtil.isBlank(msg)) {
			rtnCode = msg;
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			map.put("resBean", resBean);
			map.put("RESULT", "03"); // 交易失敗
			return map;
		}

		// 檢核該筆交易退款金額是否已超過原交易金額
		if (refundAmount > orgTrnsData.BACK_AMNT) {
			rtnCode = "7003";
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			map.put("resBean", resBean);
			map.put("RESULT", "03"); // 交易失敗
			return map;
		}

		// 檢核該筆交易是否超過可退款期限(系統日的上個月1號)
		String lastMonth = DateUtil.countMonth(DateUtil.getToday(), -1);
		lastMonth = lastMonth.substring(0, 6) + "01";
		if (Integer.parseInt(orgTrnsDate) < Integer.parseInt(lastMonth)) {
			rtnCode = "7002";
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			map.put("resBean", resBean);
			map.put("RESULT", "03"); // 交易失敗
			return map;
		}

		//
		String trnsStts = "01"; // 01-交易不明
		String hostSeq = ""; // 主機處理序號
		String hostCode = ""; // 主機回應代碼
		String hostCodeMsg = "";

		// 電文交易備註= 平台名稱(4個中文)+退款(2個中文), 平台名稱少於4個中文時,不需要補空白. 平台名稱多於4個中文時,取前4個中文
		String ecName = ecData.EC_NAME_CH;
		if (ecName.getBytes("BIG5").length > 8) { // 取前4個中文字
			ecName = new String(ecName.getBytes(), 0, 8, "BIG5");
		}
		String NARRATIV = ecName + "退款";
		LOG.debug("NARRATIV=" + NARRATIV);

		// TODO 發送 [電商平台交易電文] 進行交易退款
		// 發送電文
		TransactionResponseInfo transactionResponseInfo = new TransactionResponseInfo();
		try {
			// 交易型態(transactionType): 1: 儲值口款, 2: 交易扣款, 3: 退款, 4: 提領, 行為: 退款: refund, 扣款:
			// debit, 提領: withdraw
			transactionResponseInfo = (TransactionResponseInfo) new TelegramBo().sendFCB91148W(ecData, refundData, realAccount, signTime.substring(signTime.length() - 6), msgNo.substring(msgNo.length() - 4), "3", "refund", conn);
			if ("M1AE".equals(transactionResponseInfo.getERR_CODE())) {
				LOG.info("delayFlag錯誤..發送delayFlag查詢");
				if (new TelegramBo().sendFCB911002_1("CCIVR", conn))
					transactionResponseInfo = (TransactionResponseInfo) new TelegramBo().sendFCB91148W(ecData, refundData, realAccount, signTime.substring(signTime.length() - 6), msgNo.substring(msgNo.length() - 4), "3", "refund", conn);
			}
			hostSeq = transactionResponseInfo.getH_JRNL_NO();
			hostCode = transactionResponseInfo.getERR_CODE();
			
			
			//20190619 Add 交易失敗時記錄上下行電文 Begin
			refundData.TITA= transactionResponseInfo.getTiTa();
			refundData.TOTA= transactionResponseInfo.getToTa();
			map.put("refundData", refundData);
			
			//20190619 Add 交易失敗時記錄上下行電文 End
			
			if ("RF".equals(transactionResponseInfo.getOUTPUT_CODE()) && "0000".equals(hostCode)) {
				trnsStts = "02"; // 成功
			} else {
				trnsStts = "03"; // 失敗
				hostCodeMsg = new TbCodeHelper(hostCode, "02").getTbCodeMsg();
				LOG.error(hostCodeMsg); // v1.01, 調整不顯示 Bancs訊息, 只寫 LOG
			}
		} catch (Exception ex) {
			LOG.error("Exception:" + ex.toString(), ex);
			// errMsg = ex.getMessage();
		}

		//
		map.put("RESULT", trnsStts);
		map.put("TELE_NO", transactionResponseInfo.getH_CYCL_NO()); // 電文序號
		map.put("HOST_CODE", hostCode); // 主機回應代碼
		map.put("HOST_SEQ", hostSeq); // 主機處理序號

		// IF 退款成功，回沖 每日/每月之累計交易額
		if ("02".equals(trnsStts)) {

			// Begin Transaction.
			TransactionControl.transactionBegin(conn);

			// 更新 原訂單資料
			orgTrnsData.BACK_AMNT = orgTrnsData.BACK_AMNT - refundAmount;
			orgTrnsData.MDFY_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
			orgTrnsData.setConnection(conn);
			orgTrnsData.update();

			// 當日 回沖日累計金額
			if (orgTrnsDate.equals(DateUtil.getToday())) {
				DayCrdtContHome dayCrdtContHome = new DayCrdtContHome(conn);
				boolean rslt = dayCrdtContHome.backflush(custAcntLink.ACNT_INDT, orgTrnsDate.substring(0, 8),
						refundAmount);
				if (!rslt) {
					// 回沖失敗??
					LOG.info("回沖日額度失敗:" + orgTrnsDate.substring(0, 8) + "/idntAcnt=" + custAcntLink.ACNT_INDT
							+ "/Amount=" + refundAmount);
				}
			}

			// 當月 回沖月累計金額
			if (orgTrnsDate.substring(0, 6).equals(DateUtil.getToday().substring(0, 6))) {
				MnthCrdtContHome mnthCrdtContHome = new MnthCrdtContHome(conn);
				boolean rslt = mnthCrdtContHome.backflush(custAcntLink.ACNT_INDT, orgTrnsDate.substring(0, 6),
						refundAmount);
				if (!rslt) {
					// 回沖失敗??
					LOG.info("回沖月額度失敗:" + orgTrnsDate.substring(0, 6) + "/idntAcnt=" + custAcntLink.ACNT_INDT
							+ "/Amount=" + refundAmount);
				}
			}

			// commit
			TransactionControl.trasactionCommit(conn);
			// End Transaction.
			TransactionControl.transactionEnd(conn);
		} // end if

		//
		if ("01".equals(trnsStts)) { // 01-交易不明
			rtnCode = "7011";
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
		} else if ("02".equals(trnsStts)) { // 02-交易成功
			rtnCode = "0000";
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
		} else if ("03".equals(trnsStts)) { // 03-交易失敗
			rtnCode = "7010";
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg()); // v1.01, 調整不顯示 Bancs訊息
		}

		map.put("resBean", resBean);
		return map;
	}

	private void sendMail(HashMap map, TrnsData refundData, String CUST_NAME, String REAL_ACNT, String refundAmt) {
		try {
			// 發送電文
			String email = "";
			MemberMsgResponseInfo mberMsg = null;
			mberMsg = (MemberMsgResponseInfo) new TelegramBo().sendFCB919713_06(refundData.CUST_ID);
			if ("03".equals(mberMsg.getOUTPUT_CODE())) { // 成功
				if ("000".equals(mberMsg.getSTATUS().trim())) { // 只針對 000:正常 取用資料,其餘均視為失敗
					email = mberMsg.getEMAIL();
				}
			}
			if (!StringUtil.isBlank(email)) {
				String EC_NAME_CH = (String) map.get("EC_NAME_CH");
				String REFUND_TIME = (String) map.get("REFUND_TIME");
				String REFUND_NOTE = (String) map.get("REFUND_NOTE");

				// 發送email
				String subject = "";
				String templateFile = "";
				if ("02".equals(refundData.TRNS_STTS)) { // 交易成功
					templateFile = "ACL-G-07.vm";
					subject = APSystem.getProjectParam("MAIL_SUBJECT7"); // A/C Link交易退款成功
				} else { // 交易失敗
					templateFile = "ACL-G-08.vm";
					subject = APSystem.getProjectParam("MAIL_SUBJECT8"); // A/C Link交易退款失敗
				}

				HashMap mailMap = new HashMap();
				// 姓名
				mailMap.put("EC_USER_NAME", CommonUtil.nameMask(CUST_NAME));
				// 交易時間
				mailMap.put("TRNS_DTTM", DateUtil.formateDateTimeForUser(REFUND_TIME));
				// 電商平台
				mailMap.put("EC_NAME", EC_NAME_CH);
				// 平台會員代號
				mailMap.put("EC_USER", refundData.EC_USER);
				// 平台連結帳號
				mailMap.put("REAL_ACNT", CommonUtil.relAcntMask(REAL_ACNT));
				// 訂單編號
				mailMap.put("ORDR_NO", refundData.ORDR_NO);
				// 交易備註
				mailMap.put("TRNS_NOTE", REFUND_NOTE);
				// 交易金額
				mailMap.put("TRNS_AMNT", CommonUtil.limtFormat(Long.parseLong(refundAmt)));
				// 錯誤訊息
				mailMap.put("ERR_MSG", resBean.getRTN_MSG());

				String mailContent = MailUtil.renderMailHtmlContent(mailMap, templateFile);
				new MailUtil().addMail(mailContent, subject, email, null, null, null);

			} // end if
		} catch (Exception e) {
			LOG.info("email發送失敗");
			e.printStackTrace();
		}
	}
}
