/**
 * @(#) ACLinkPayService.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 連結帳號交易提領Service
 * 
 * Modify History:
 *  v1.00, 2017/11/20, Hsien
 *   1) First release
 *  v1.01, 2018/03/20
 *   1) 紀錄IP到TRNS_DATA
 *
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
import com.hitrust.bank.dao.home.EcDataHome;
import com.hitrust.bank.response.ACLinkWithdrawResBean;
import com.hitrust.bank.telegram.TelegramBo;
import com.hitrust.bank.telegram.res.MemberMsgResponseInfo;
import com.hitrust.bank.telegram.res.TransactionResponseInfo;

public class ACLinkWithdrawService extends AbstractServiceModel {
	// log4j
	static Logger LOG = Logger.getLogger(ACLinkWithdrawService.class);

	/**
	 * 
	 * @param service
	 * @param request
	 * @param response
	 */
	public ACLinkWithdrawService(String service, String operation, AbstractResponseBean resBean,
			HttpServletRequest request, HttpServletResponse response) {
		super(service, operation, resBean, request, response);
	}

	/**
	 * 
	 */
	public AbstractResponseBean process() throws ApplicationException, DBException {
		
		LOG.info(DateUtil.formateDateTime(DateUtil.getCurrentTime("DT", "AD"))+" 開始執行連結帳戶交易提領  ["+operation+"]");
		Connection conn = null;
		ACLinkWithdrawResBean resBean = null;

		try {

			conn = APSystem.getConnection(APSystem.DB_ACLINK);
			
			DatabaseMetaData mtdt = conn.getMetaData();
			LOG.info("DB 使用者: [" + mtdt.getUserName()+"]");
			LOG.info("用戶IP:["+request.getRemoteAddr()+"]");
			LOG.info("用戶電腦名稱:["+request.getRemoteHost()+"]");

			HashMap map = this.aclinkWithdrew(conn);

			resBean = (ACLinkWithdrawResBean) map.get("resBean");
			TrnsData trnsData = (TrnsData) map.get("trnsData");
			String CUST_SERL = (String) map.get("CUST_SERL");
			String ACNT_INDT = (String) map.get("ACNT_INDT");
			String REAL_ACNT = (String) map.get("REAL_ACNT"); // 會員入帳帳號
			String EC_REAL_ACNT = (String) map.get("EC_REAL_ACNT");// 電商轉出帳號
			String TELE_NO = (String) map.get("TELE_NO");
			String RESULT = (String) map.get("RESULT");
			String HOST_CODE = (String) map.get("HOST_CODE");
			String HOST_SEQ = (String) map.get("HOST_SEQ");
			String trnsAmt = (String) map.get("TRNS_AMT");

			// update TRNS_DATA
			if (trnsData != null) {
				// Begin Transaction.
				TransactionControl.transactionBegin(conn);

				trnsData.setConnection(conn);
				trnsData.REAL_ACNT = EC_REAL_ACNT; // 轉出帳號(電商帳)
				trnsData.RECV_ACNT = REAL_ACNT; // 轉入帳號(會員帳號
				trnsData.CUST_SERL = CUST_SERL; // 會員服務序號
				trnsData.ACNT_INDT = ACNT_INDT; // 帳號識別碼
				trnsData.TELE_NO = TELE_NO; // 電文序號
				if (!StringUtil.isBlank(RESULT)) {
					trnsData.TRNS_STTS = RESULT; // 02-交易成功 or 03-交易失敗
				}
				if (!StringUtil.isBlank(HOST_CODE)) {
					trnsData.HOST_CODE = HOST_CODE; // 主機回應代碼
				}
				trnsData.ERR_CODE = resBean.getRTN_CODE(); // 錯誤代碼
				trnsData.HOST_SEQ = HOST_SEQ; // 主機處理序號
				trnsData.MDFY_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD")).substring(0,
						10);
				trnsData.update();

				// commit
				TransactionControl.trasactionCommit(conn);
				// End Transaction.
				TransactionControl.transactionEnd(conn);

				String CUST_NAME = (String) map.get("CUST_NAME");
				String CUST_TYPE = (String) map.get("CUST_TYPE");
				String email = "";

				// 關閉電文發送
				// CUST_TYPE = "";

				// TODO 發送 [CIF電文] 取得客戶email
//				if (!StringUtil.isBlank(CUST_TYPE) && ("02".equals(trnsData.TRNS_STTS))) {
				if ("02".equals(trnsData.TRNS_STTS)) { // 一銀只有交易成功才需要發送email
					this.sendMail(map, trnsData, CUST_NAME, REAL_ACNT, trnsAmt);
				} // end if
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
			LOG.info(DateUtil.formateDateTime(DateUtil.getCurrentTime("DT", "AD"))+" 結束執行連結帳戶交易提領  ["+operation+"]");
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
	private HashMap aclinkWithdrew(Connection conn) throws ApplicationException, DBException, Exception {
		ACLinkWithdrawResBean resBean = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		String rtnCode = "9999";

		// ======== 取得參數 ========

		String msgNo = request.getParameter("MSG_NO"); // 訊息序號
		String ecId = request.getParameter("EC_ID"); // 平台代碼
		String ecUser = request.getParameter("EC_USER"); // 平台會員代號
		String custId = request.getParameter("CUST_ID"); // 使用者身分證號
		String trnsTime = request.getParameter("TRNS_TIME"); // 交易時間(YYYYMMDDhhmmss)
		String trnsNote = URLDecoder.decode(request.getParameter("TRNS_NOTE")); // 交易明細(最多為12個全形字)
		String trnsAmt = request.getParameter("TRNS_AMT"); // 交易金額
		String idntAcnt = request.getParameter("INDT_ACNT"); // 帳號識別碼
		String signTime = request.getParameter("SIGN_TIME"); // 簽章日期時間
		String signValue = request.getParameter("SIGN_VALUE"); // 簽章值
		String certSn = request.getParameter("CERT_SN"); // 憑證序號

		String ecStts = "";
		String custStts = "";
		String linkStts = "";
		String realAccount = "";
		int trnsAmount = Integer.parseInt(trnsAmt);
		String today = DateUtil.getToday(); // 系統日

		resBean = (ACLinkWithdrawResBean) this.resBean;
		resBean.setMSG_NO(msgNo);
		resBean.setEC_ID(ecId);
		resBean.setEC_USER(ecUser);

		// 取得 電商平台資料檔
		EcDataHome ecDataHome = new EcDataHome(conn);
		EcData ecData = ecDataHome.fetchEcDataByKey(ecId);
		if (ecData == null) {
			rtnCode = "5011"; // 平台資料不存在
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
		CustAcntLink custAcntLink = custAcntLinkHome.getCustAcntLinkByAcntIdnt(custId, ecId, ecUser, idntAcnt);
		if (custAcntLink == null) {
			LOG.error("CustAcntLink not exist,custId=" + custId + "/ecId=" + ecId + "/ecUser=" + ecUser + "/idntAcnt=" + idntAcnt);
			rtnCode = "5021"; // 連結帳號不存在
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			map.put("resBean", resBean);
			map.put("RESULT", "03"); // 交易失敗
			return map;
		} else {
			linkStts = custAcntLink.STTS;
			realAccount = custAcntLink.REAL_ACNT;
			map.put("ACNT_INDT", custAcntLink.ACNT_INDT);
			map.put("REAL_ACNT", custAcntLink.REAL_ACNT);// 轉入實體帳號
		}
		// ======== 新增交易資料(TRNS_DATA) ========
		// Begin Transaction.
		TransactionControl.transactionBegin(conn);

		// insert 提領交易資料(TRNS_DATA)
		TrnsData trnsData = new TrnsData();
		trnsData.setConnection(conn);
		trnsData.EC_ID = ecId; // 平台代碼
		trnsData.EC_MSG_NO = msgNo; // 平台訊息序號
		trnsData.CUST_ID = custId; // 身分證字號
		trnsData.EC_USER = ecUser; // 平台會員代碼
		trnsData.TRNS_TYPE = "C"; // C-提領
		trnsData.TRNS_DTTM = DateUtil.formateDateTimeForUser(trnsTime);
		trnsData.TRNS_AMNT = trnsAmount; // 交易金額
		trnsData.TRNS_STTS = "01"; // 01-交易不明
		trnsData.BACK_AMNT = 0; // 訂單餘額
		trnsData.TRNS_NOTE = trnsNote; // 交易備註
		trnsData.ACNT_INDT = idntAcnt; // 帳號識別碼
		trnsData.MDFY_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
		// v1.01 紀錄IP到TRNS_DATA
		trnsData.IP = request.getRemoteAddr();
		// v1.01 紀錄IP到TRNS_DATA End
		trnsData.MIN_FEE = 0;
		trnsData.MAX_FEE = 0;
		trnsData.REAL_ACNT = (String) map.get("EC_REAL_ACNT"); // 轉出帳號(電商帳)
		trnsData.RECV_ACNT = (String) map.get("REAL_ACNT"); // 轉入帳號(會員帳號)
		trnsData.insert();
		// commit
		TransactionControl.trasactionCommit(conn);
		// End Transaction.
		TransactionControl.transactionEnd(conn);

		//20190619 Add 交易失敗時記錄上下行電文 Begin
		//map.put("trnsData", trnsData);
		//20190619 Add 交易失敗時記錄上下行電文 End
		
		map.put("TRNS_AMT", trnsAmt);
		map.put("TRNS_TIME", trnsTime);
		map.put("TRNS_NOTE", trnsNote);

		// ======== 取得 會員資料檔/電商平台資料檔/會員帳號連結檔/會員帳號檔 ========
		// 取得 會員資料檔
		CustDataHome custDataHome = new CustDataHome(conn);
		CustData custData = custDataHome.fetchCustDataByKey(custId);
		if (custData == null || "02".equals(custData.STTS)) {
			rtnCode = "5002"; // 會員未開通服務
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

		// 取得 會員帳號檔
		CustAcntHome custAcntHome = new CustAcntHome(conn);
		CustAcnt custAcnt = custAcntHome.fetchCustAcntByKey(custId, realAccount);
		if (custAcnt == null) {
			LOG.error("CustAcnt not exist,custId=" + custId + "/realAccount=" + realAccount);
			rtnCode = "5021";
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			map.put("resBean", resBean);
			map.put("RESULT", "03"); // 交易失敗
			return map;
		}

		// 檢核狀態
		String pltfStts = "";
		CustPltfHome pltfHome = new CustPltfHome(conn);
		CustPltf pltf = pltfHome.fetchCustPltfByKey(ecId, custId);

		if (!StringUtil.isBlank(pltf)) {
			pltfStts = pltf.STTS;
		}

		String msg = CommonUtil.checkStatus("07", ecStts, custStts, pltfStts, linkStts);
		if (!StringUtil.isBlank(msg)) {
			LOG.error("Service check fail,ecStts=" + ecStts + "/custStts=" + custStts + "/linkStts=" + linkStts);
			rtnCode = msg;
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			map.put("resBean", resBean);
			map.put("RESULT", "03"); // 交易失敗
			return map;
		}

		// ======== 發送 [001045 電商平台交易電文] 進行交易扣款 ========
		String trnsStts = "01"; // 01-交易不明
		String hostSeq = ""; // 主機處理序號
		String hostCode = ""; // 主機回應代碼
		String hostCodeMsg = "";

		// 電文交易備註= 平台名稱(4個中文)+付款(2個中文), 平台名稱少於4個中文時,不需要補空白. 平台名稱多於4個中文時,取前4個中文
		String ecName = ecData.EC_NAME_CH;
		if (ecName.getBytes("BIG5").length > 8) { // 取前4個中文字
			ecName = new String(ecName.getBytes(), 0, 8, "BIG5");
		}
		String NARRATIV = ecName + "提領";
		LOG.info("NARRATIV=" + NARRATIV);

		// 發送電文
		TransactionResponseInfo transactionResponseInfo = new TransactionResponseInfo();
		try {
			// 初始化並記錄 Request 處理的開始時間
			long statTime = System.currentTimeMillis();
			// 交易型態(transactionType): 1: 儲值口款, 2: 交易扣款, 3: 退款, 4: 提領, 行為: 退款: refund, 扣款:
			// debit, 提領: withdraw
			transactionResponseInfo = (TransactionResponseInfo) new TelegramBo().sendFCB91148W(ecData, trnsData, realAccount, trnsTime.substring(trnsTime.length() - 6), msgNo.substring(msgNo.length() - 4), "4", "withdraw", conn);
			if ("M1AE".equals(transactionResponseInfo.getERR_CODE())) {
				LOG.info("delayFlag錯誤..發送delayFlag查詢");
				if (new TelegramBo().sendFCB911002_1("CCIVR", conn))
					transactionResponseInfo = (TransactionResponseInfo) new TelegramBo().sendFCB91148W(ecData, trnsData, realAccount, trnsTime.substring(trnsTime.length() - 6), msgNo.substring(msgNo.length() - 4), "4", "withdraw", conn);
			}
			// 計算 Request 的處理結束時間, 並記入 Log 中
			long endTime = System.currentTimeMillis();
			LOG.info("[MSG_NO]" + trnsData.EC_MSG_NO + "[StatTime]" + statTime + "[EndTime]" + endTime + "[TotalTime]"
					+ ((double) (endTime - statTime)) / 1000);

			hostSeq = transactionResponseInfo.getH_JRNL_NO();
			hostCode = transactionResponseInfo.getERR_CODE();
			
			//20190619 Add 交易失敗時記錄上下行電文 Begin
			trnsData.TITA= transactionResponseInfo.getTiTa();
			trnsData.TOTA= transactionResponseInfo.getToTa();
			map.put("trnsData", trnsData);
			
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

	private void sendMail(HashMap map, TrnsData trnsData, String CUST_NAME, String REAL_ACNT, String trnsAmt) {
		try {
			// 發送電文
			String email = "";
			MemberMsgResponseInfo mberMsg = null;
			mberMsg = (MemberMsgResponseInfo) new TelegramBo().sendFCB919713_06(trnsData.CUST_ID);
			if ("03".equals(mberMsg.getOUTPUT_CODE())) { // 成功
				if ("000".equals(mberMsg.getSTATUS().trim())) { // 只針對 000:正常 取用資料,其餘均視為失敗
					email = mberMsg.getEMAIL();
				}
			}
			if (!StringUtil.isBlank(email)) {
				String EC_NAME_CH = (String) map.get("EC_NAME_CH");
				String TRNS_TIME = (String) map.get("TRNS_TIME");
				String TRNS_NOTE = (String) map.get("TRNS_NOTE");

				// 發送email
				String subject = "";
				String templateFile = "";
				if ("02".equals(trnsData.TRNS_STTS)) { // 交易成功
					templateFile = "ACL-G-09.vm";
					subject = APSystem.getProjectParam("MAIL_SUBJECT9"); // A/C Link交易退款成功
				} else { // 交易失敗
					templateFile = "ACL-G-10.vm";
					subject = APSystem.getProjectParam("MAIL_SUBJECT10"); // A/C Link交易退款失敗
				}

				HashMap mailMap = new HashMap();
				// 姓名
				mailMap.put("EC_USER_NAME", CommonUtil.nameMask(CUST_NAME));
				// 交易時間
				mailMap.put("TRNS_DTTM", DateUtil.formateDateTimeForUser(TRNS_TIME));
				// 電商平台
				mailMap.put("EC_NAME", EC_NAME_CH);
				// 平台會員代號
				mailMap.put("EC_USER", trnsData.EC_USER);
				// 平台連結帳號
				mailMap.put("REAL_ACNT", CommonUtil.relAcntMask(REAL_ACNT));
				// 交易備註
				mailMap.put("TRNS_NOTE", TRNS_NOTE);
				// 交易金額
				mailMap.put("TRNS_AMNT", CommonUtil.limtFormat(Long.parseLong(trnsAmt)));
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
