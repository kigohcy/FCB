/**
 * @(#) ACLinkPayService.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 連結帳戶交易扣款Service
 * 
 * Modify History:
 *  v1.00, 2016/03/25, Yann
 *   1) First release
 *  v1.01, 2016/12/02, Ada Chen
 *   1) TSBACL-138, 調整不顯示 Bancs訊息, 只寫 LOG
 *  V2.00, 2018/01/25
 *   1) 修正交易結果mail 沒有發送之問題
 *  V2.01, 2018/03/19
 *   1) 新增電商每月總額度
 *   2) 手續費計算方式為比率，考慮最低、最高收費金額
 *   3) API新增PAY_TYPE
 *   4) 紀錄IP到TRNS_DATA
 */
package com.hitrust.bank.service;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.hitrust.bank.dao.beans.DayCrdtCont;
import com.hitrust.bank.dao.beans.EcData;
import com.hitrust.bank.dao.beans.MnthCrdtCont;
import com.hitrust.bank.dao.beans.SysParm;
import com.hitrust.bank.dao.beans.TrnsData;
import com.hitrust.bank.dao.home.CustAcntHome;
import com.hitrust.bank.dao.home.CustAcntLinkHome;
import com.hitrust.bank.dao.home.CustDataHome;
import com.hitrust.bank.dao.home.CustPltfHome;
import com.hitrust.bank.dao.home.DayCrdtContHome;
import com.hitrust.bank.dao.home.EcDataHome;
import com.hitrust.bank.dao.home.MnthCrdtContHome;
import com.hitrust.bank.dao.home.SysParmHome;
import com.hitrust.bank.dao.home.TrnsDataHome;
import com.hitrust.bank.json.ACLink;
import com.hitrust.bank.response.ACLinkPayResBean;
import com.hitrust.bank.telegram.TelegramBo;
import com.hitrust.bank.telegram.res.TransactionResponseInfo;
import com.hitrust.bank.telegram.res.MemberMsgResponseInfo;

public class ACLinkPayService extends AbstractServiceModel {
	// log4j
	static Logger LOG = Logger.getLogger(ACLinkPayService.class);

	/**
	 * 
	 * @param service
	 * @param request
	 * @param response
	 */
	public ACLinkPayService(String service, String operation, AbstractResponseBean resBean, HttpServletRequest request,
			HttpServletResponse response) {
		super(service, operation, resBean, request, response);
	}

	/**
	 * 
	 */
	public AbstractResponseBean process() throws ApplicationException, DBException {

		LOG.info(DateUtil.formateDateTime(DateUtil.getCurrentTime("DT", "AD"))+" 開始執行連結帳戶交易扣款  ["+operation+"]");
		
		Connection conn = null;
		ACLinkPayResBean resBean = null;

		try {
			//
			conn = APSystem.getConnection(APSystem.DB_ACLINK);
			
			DatabaseMetaData mtdt = conn.getMetaData();
			LOG.info("DB 使用者: [" + mtdt.getUserName()+"]");
			LOG.info("用戶IP:["+request.getRemoteAddr()+"]");
			LOG.info("用戶電腦名稱:["+request.getRemoteHost()+"]");
			//
			HashMap map = this.aclinkPay(conn);

			resBean = (ACLinkPayResBean) map.get("resBean");
			TrnsData trnsData = (TrnsData) map.get("trnsData");
			String CUST_SERL = (String) map.get("CUST_SERL");
			String FEE_TYPE = (String) map.get("FEE_TYPE");
			double FEE_RATE = 0;
			if (map.get("FEE_RATE") != null) {
				FEE_RATE = (double) map.get("FEE_RATE");
			}
			String REAL_ACNT = (String) map.get("REAL_ACNT");
			String TELE_NO = (String) map.get("TELE_NO");
			String RESULT = (String) map.get("RESULT");
			String HOST_CODE = (String) map.get("HOST_CODE");
			String HOST_SEQ = (String) map.get("HOST_SEQ");
			String trnsAmt = (String) map.get("TRNS_AMT");
			int trnsAmount = Integer.parseInt(trnsAmt);
			// V2.01 手續費計算方式為比率，考慮最低、最高收費金額
			Integer MAX_FEE = map.get("MAX_FEE") != null ? (Integer) map.get("MAX_FEE") : null;
			Integer MIN_FEE = map.get("MIN_FEE") != null ? (Integer) map.get("MIN_FEE") : null;
			// V2.01 手續費計算方式為比率，考慮最低、最高收費金額 End

			// update TRNS_DATA
			if (trnsData != null) {
				// Begin Transaction.
				TransactionControl.transactionBegin(conn);

				trnsData.setConnection(conn);
				trnsData.REAL_ACNT = REAL_ACNT; // 轉出帳號
				trnsData.CUST_SERL = CUST_SERL; // 會員服務序號
				trnsData.FEE_TYPE = FEE_TYPE; // 收費方式
				trnsData.FEE_RATE = FEE_RATE; // 費率
				if (FEE_TYPE != null && "B".equals(FEE_TYPE)) { // 比率
					// 四捨五入到整數位
					// trnsData.FEE_AMNT = (int)(trnsAmount * ecData.FEE_RATE / 100);
					BigDecimal feeAmt = new BigDecimal(trnsAmount).multiply(new BigDecimal(FEE_RATE))
							.divide(new BigDecimal(100), 0, BigDecimal.ROUND_HALF_UP);
					// V2.01 手續費計算方式為比率，考慮最低、最高收費金額
					// 手續費最高收費金額
					if (MAX_FEE != null && MAX_FEE != 0 && feeAmt.compareTo(new BigDecimal(MAX_FEE)) > 0) {
						feeAmt = new BigDecimal(MAX_FEE);
					}
					// 手續費最低收費金額
					if (MIN_FEE != null && MIN_FEE != 0 && feeAmt.compareTo(new BigDecimal(MIN_FEE)) < 0) {
						feeAmt = new BigDecimal(MIN_FEE);
					}
					trnsData.MAX_FEE = MAX_FEE;
					trnsData.MAX_FEE = MIN_FEE;
					// V2.01 手續費計算方式為比率，考慮最低、最高收費金額 End
					trnsData.FEE_AMNT = feeAmt.intValue(); // 手續費金額
				} else { // 定額
					trnsData.FEE_AMNT = (int) FEE_RATE; // 手續費金額
				}
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

				//
				String CUST_NAME = (String) map.get("CUST_NAME");
				String CUST_TYPE = (String) map.get("CUST_TYPE");
				String email = "";

				// 關閉電文發送
				// V2.00,2018/01/25 修正交易結果mail 沒有發送之問題 Begin
				// CUST_TYPE = "";

				// TODO 發送 [CIF電文] 取得客戶email
				// if(!StringUtil.isBlank(CUST_TYPE) && ("02".equals(trnsData.TRNS_STTS))) {
				if ("02".equals(trnsData.TRNS_STTS)) { // 一銀只有交易成功才需要發送email
					// V2.00,2018/01/25 修正交易結果mail 沒有發送之問題 End
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
			LOG.info(DateUtil.formateDateTime(DateUtil.getCurrentTime("DT", "AD"))+" 結束執行連結帳戶交易扣款  ["+operation+"]");	
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
	private HashMap aclinkPay(Connection conn) throws ApplicationException, DBException, Exception {
		ACLinkPayResBean resBean = null;
		HashMap map = new HashMap();
		String rtnCode = "9999";

		// ======== 取得參數 ========
		// 訊息序號
		String msgNo = request.getParameter("MSG_NO");
		// 平台代碼
		String ecId = request.getParameter("EC_ID");
		// 平台會員代號
		String ecUser = request.getParameter("EC_USER");
		// 使用者身分證號
		String custId = request.getParameter("CUST_ID");
		// 交易時間(YYYYMMDDhhmmss)
		String trnsTime = request.getParameter("TRNS_TIME");
		// 訂單編號
		String trnsNo = request.getParameter("TRNS_NO");
		// 交易明細(最多為12個全形字)
    	String trnsNote = URLDecoder.decode(request.getParameter("TRNS_NOTE"));
		// 交易金額
		String trnsAmt = request.getParameter("TRNS_AMT");
		// 帳號識別碼
		String idntAcnt = request.getParameter("INDT_ACNT");
		// 轉入虛擬帳號
		String payeeAcnt = request.getParameter("PAYEE_ACNT");
		// 簽章日期時間
		String signTime = request.getParameter("SIGN_TIME");
		// 簽章值
		String signValue = request.getParameter("SIGN_VALUE");
		// 憑證序號
		String certSn = request.getParameter("CERT_SN");
		// V2.01 API新增PAY_TYPE
		// 交易類別
		String payType = request.getParameter("PAY_TYPE");
		// V2.01 API新增PAY_TYPE End

		String ecStts = "";
		String custStts = "";
		String linkStts = "";
		String realAccount = "";
		int trnsAmount = Integer.parseInt(trnsAmt);

		String today = DateUtil.getToday(); // 系統日

		resBean = (ACLinkPayResBean) this.resBean;
		resBean.setMSG_NO(msgNo);
		resBean.setEC_ID(ecId);
		resBean.setEC_USER(ecUser);

		// ======== 資料檢核 ========
		// 檢核 訂單編號 是否重複
		TrnsDataHome trnsDataHome = new TrnsDataHome(conn);
		boolean isOrdrNoExist = trnsDataHome.isOrdrNoExist(ecId, trnsNo);
		if (isOrdrNoExist) {
			LOG.error("訂單編號重複:" + trnsNo);
			rtnCode = "7004";
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			map.put("resBean", resBean);
			map.put("RESULT", "03"); // 交易失敗
			return map;
		}
		
		// 取得 會員帳號連結檔
		CustAcntLinkHome custAcntLinkHome = new CustAcntLinkHome(conn);
		CustAcntLink custAcntLink = custAcntLinkHome.getCustAcntLinkByAcntIdnt(custId, ecId, ecUser, idntAcnt);
		if (custAcntLink == null) {
			LOG.error("CustAcntLink not exist,custId=" + custId + "/ecId=" + ecId + "/ecUser=" + ecUser + "/idntAcnt=" + idntAcnt);
			rtnCode = "5021";
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			map.put("resBean", resBean);
			map.put("RESULT", "03"); // 交易失敗
			return map;
		} else {
			linkStts = custAcntLink.STTS;
			realAccount = custAcntLink.REAL_ACNT;
			map.put("REAL_ACNT", realAccount);
		}
		
		// ======== 新增扣款交易資料(TRNS_DATA) ========
		// Begin Transaction.
		TransactionControl.transactionBegin(conn);

		// insert 扣款交易資料(TRNS_DATA)
		TrnsData trnsData = new TrnsData();
		trnsData.setConnection(conn);
		trnsData.EC_ID = ecId; // 平台代碼
		trnsData.EC_MSG_NO = msgNo; // 平台訊息序號
		trnsData.CUST_ID = custId; // 身分證字號
		trnsData.EC_USER = ecUser; // 平台會員代碼
		// V2.01 API新增PAY_TYPE
		if (StringUtils.isBlank(payType)) {
			if (trnsNote.toUpperCase().indexOf("DEPOSIT") != -1) {
				trnsData.TRNS_TYPE = "D";
			} else {
				trnsData.TRNS_TYPE = "A";
			}
		} else {
			//20190619 Add 繳費稅 Begin
			//trnsData.TRNS_TYPE = StringUtils.equals(payType, "P") ? "A" : "D"; // A:扣款, B:退款, C:提領, D:儲值(街口帶入: P: 扣款, D: 儲值) E:繳費稅
			if(payType.equals("P")) {
				trnsData.TRNS_TYPE="A";
			}else if(payType.equals("A") ||payType.equals("E")) {
			  trnsData.TRNS_TYPE =payType;
			}else {
				trnsData.TRNS_TYPE ="D";	
			}
			//20190619 Add 繳費稅 End
		}
		// V2.01 API新增PAY_TYPE End
		trnsData.RECV_ACNT = payeeAcnt; // 轉入帳號
		trnsData.REAL_ACNT = (String) map.get("REAL_ACNT");//轉出帳號
		trnsData.TRNS_DTTM = DateUtil.formateDateTimeForUser(trnsTime);
		trnsData.TRNS_AMNT = trnsAmount; // 交易金額
		trnsData.TRNS_STTS = "01"; // 01-交易不明
		trnsData.ORDR_NO = trnsNo; // 訂單編號
		trnsData.BACK_AMNT = trnsAmount; // 訂單餘額
		trnsData.TRNS_NOTE = trnsNote; // 交易備註
		trnsData.ACNT_INDT = idntAcnt; // 帳號識別碼
		trnsData.MDFY_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
		// V2.01 紀錄IP到TRNS_DATA
		trnsData.IP = request.getRemoteAddr();
		// V2.02 紀錄IP到TRNS_DATA
		trnsData.MIN_FEE = 0;
		trnsData.MAX_FEE = 0;
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
			//20190619 Add 繳費稅 Begin
			if (!trnsData.TRNS_TYPE.equals("E")) {
				map.put("FEE_TYPE", ecData.FEE_TYPE);
				map.put("FEE_RATE", ecData.FEE_RATE);

				// V2.01 手續費計算方式為比率，考慮最低、最高收費金額
				map.put("MIN_FEE", ecData.MIN_FEE);
				map.put("MAX_FEE", ecData.MAX_FEE);
				// V2.01 手續費計算方式為比率，考慮最低、最高收費金額 End
			}else {
				LOG.info("TAX_TYPE="+ecData.TAX_TYPE);
				String taxType2feeType="A";
				if(ecData.TAX_TYPE.equals("D")) {
					taxType2feeType="B";
				}
				LOG.info("ecData.TAX_RATE="+ecData.TAX_RATE);
				
				BigDecimal bTaxRate=BigDecimal.valueOf(ecData.TAX_RATE);
				
				
				if (bTaxRate.compareTo(BigDecimal.valueOf(0)) == 0) {
					rtnCode = "5013";
					resBean.setRTN_CODE(rtnCode);
					resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
					map.put("resBean", resBean);
					map.put("RESULT", "03"); // 交易失敗
					return map;
				}
				
				LOG.info("TaxType2feeType=="+taxType2feeType);
				map.put("FEE_TYPE", taxType2feeType);
				map.put("FEE_RATE", ecData.TAX_RATE);
				map.put("MIN_FEE", ecData.MIN_TAX);
				map.put("MAX_FEE", ecData.MAX_TAX);
			}
			//20190619 Add 繳費稅 End	
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

		// ======== 資料檢核 ========
		// 檢核 轉入虛擬帳號編碼 是否正確
		if (!payeeAcnt.startsWith(ecData.ENTR_NO)) {
			LOG.error("payeeAcnt error, ENTR_NO=" + ecData.ENTR_NO + "/payeeAcnt=" + payeeAcnt);
			rtnCode = "7005";
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

		String msg = CommonUtil.checkStatus("04", ecStts, custStts, pltfStts, linkStts);
		if (!StringUtil.isBlank(msg)) {
			LOG.error("Service check fail,ecStts=" + ecStts + "/custStts=" + custStts + "/linkStts=" + linkStts);
			rtnCode = msg;
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			map.put("resBean", resBean);
			map.put("RESULT", "03"); // 交易失敗
			return map;
		}

		// ======== 取得 日/月累計金額累計金額 ========
		// 依據系統日、身分證字號、實體帳號, 取得該實體帳號在所有平台日累計金額
		long dayCont_acnt = 0;
		DayCrdtContHome dayCrdtContHome = new DayCrdtContHome(conn);
		dayCont_acnt = dayCrdtContHome.getDaySumByAcnt(today, custId, realAccount, custData.CUST_SERL);

		// 日累計金額(帳號識別碼)
		long dayCont_acntId = 0;
		// 取得 日額度累計檔 by 帳號識別碼
		DayCrdtCont dayCrdtCont = dayCrdtContHome.getDayCrdtContByPk(idntAcnt, today);
		if (dayCrdtCont != null) {
			dayCont_acntId = dayCrdtCont.DAY_CONT;
		} else {
			dayCrdtCont = new DayCrdtCont();
			dayCrdtCont.ACNT_INDT = idntAcnt;
			dayCrdtCont.TRNS_DATE = today;
			dayCrdtCont.CUST_ID = custId;
			dayCrdtCont.EC_ID = ecId;
			dayCrdtCont.EC_USER = ecUser;
			dayCrdtCont.REAL_ACNT = realAccount;
			dayCrdtCont.CUST_SERL = custData.CUST_SERL;
		}

		// 依據系統日、身分證字號、實體帳號, 取得該實體帳號在所有平台月累計金額
		long mnthCont_acnt = 0;
		MnthCrdtContHome mnthCrdtContHome = new MnthCrdtContHome(conn);
		mnthCont_acnt = mnthCrdtContHome.getMnthSumByAcnt(today.substring(0, 6), custId, realAccount,
				custData.CUST_SERL);

		// V2.01 新增電商每月總額度
		// 依據系統日、身分證字號, 取得該實體帳號在所有平台月累計金額
		long mnthCont_custId = 0;
		mnthCont_custId = mnthCrdtContHome.getMnthSumByCustId(today.substring(0, 6), custId, custData.CUST_SERL);
		// V2/01 新增電商每月總額度 End

		// 月累計金額(帳號識別碼)
		long mnthCont_acntId = 0;
		// 取得 月額度累計檔 by 帳號識別碼
		MnthCrdtCont mnthCrdtCont = mnthCrdtContHome.getMnthCrdtContByPk(idntAcnt, today.substring(0, 6));
		if (mnthCrdtCont != null) {
			mnthCont_acntId = mnthCrdtCont.MNTH_CONT;
		} else {
			mnthCrdtCont = new MnthCrdtCont();
			mnthCrdtCont.ACNT_INDT = idntAcnt;
			mnthCrdtCont.TRNS_MNTH = today.substring(0, 6);
			mnthCrdtCont.CUST_ID = custId;
			mnthCrdtCont.EC_ID = ecId;
			mnthCrdtCont.EC_USER = ecUser;
			mnthCrdtCont.REAL_ACNT = realAccount;
			mnthCrdtCont.CUST_SERL = custData.CUST_SERL;
		}
		// V2.01 新增電商每月總額度
		// 取得單一電商每月最高消費金額
		SysParmHome sysParmHome = new SysParmHome(conn);
		SysParm ecMonthLimtParm = sysParmHome.fetchSysParmByKey("EC_MONTH_LIMIT");
		Long ecMonthLimit = new Long(0);
		if (ecMonthLimtParm != null) {
			ecMonthLimit = Long.parseLong(ecMonthLimtParm.PARM_VALUE);
		}
		// V2.01 新增電商每月總額度 End

		// ======== 檢核 交易金額 ========
		// 檢核 交易金額 是否超過 每筆、每日、每月 帳號限額
		// V2.01 新增電商每月總額度
		boolean quotaCheck = this.quotaCheck(custAcnt.TRNS_LIMT, custAcnt.DAY_LIMT, custAcnt.MNTH_LIMT, ecMonthLimit,
				dayCont_acnt, mnthCont_acnt, mnthCont_custId, trnsAmount);
		// V2.01 新增電商每月總額度 End
		if (!quotaCheck) {
			LOG.error("dayCont_acnt=" + dayCont_acnt + "/mnthCont_acnt=" + mnthCont_acnt + "/trnsAmount=" + trnsAmount);
			rtnCode = "6003";
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			map.put("resBean", resBean);
			map.put("RESULT", "03"); // 交易失敗
			return map;
		}

		// 檢核 交易金額 是否超過 每筆、每日、每月 自訂限額
		// V2.01 新增電商每月總額度
		quotaCheck = this.quotaCheck(custAcntLink.TRNS_LIMT, custAcntLink.DAY_LIMT, custAcntLink.MNTH_LIMT,
				ecMonthLimit, dayCont_acntId, mnthCont_acntId, mnthCont_custId, trnsAmount);
		// V2.01 新增電商每月總額度 End
		if (!quotaCheck) {
			LOG.error("dayCont_acntId=" + dayCont_acntId + "/mnthCont_acntId=" + mnthCont_acntId + "/trnsAmount="
					+ trnsAmount);
			rtnCode = "6002";
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			map.put("resBean", resBean);
			map.put("RESULT", "03"); // 交易失敗
			return map;
		}

		// ======== 累算 日/月額度金額 ========
		// Begin Transaction.
		TransactionControl.transactionBegin(conn);

		// 累算日額度金額
		boolean rslt = dayCrdtContHome.calculate(dayCrdtCont, trnsAmount);
		if (!rslt) {
			LOG.error("calculate dayCrdtCont error, ACNT_INDT=" + idntAcnt);
			// 累算失敗 rollback
			TransactionControl.transactionRollback(conn);
			rtnCode = "7021";
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			map.put("resBean", resBean);
			map.put("RESULT", "03"); // 交易失敗
			return map;
		}
		// 累算月額度金額
		rslt = mnthCrdtContHome.calculate(mnthCrdtCont, trnsAmount);
		if (!rslt) {
			LOG.error("calculate mnthCrdtCont error, ACNT_INDT=" + idntAcnt);
			// 累算失敗 rollback
			TransactionControl.transactionRollback(conn);
			rtnCode = "7021";
			resBean.setRTN_CODE(rtnCode);
			resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			map.put("resBean", resBean);
			map.put("RESULT", "03"); // 交易失敗
			return map;
		}

		// commit
		TransactionControl.trasactionCommit(conn);
		// End Transaction.
		TransactionControl.transactionEnd(conn);

		// ======== 發送 [電商平台交易電文] 進行交易扣款 ========
		String trnsStts = "01"; // 01-交易不明
		String hostSeq = ""; // 主機處理序號
		String hostCode = ""; // 主機回應代碼
		String hostCodeMsg = "";

		// 電文交易備註= 平台名稱(4個中文)+付款(2個中文), 平台名稱少於4個中文時,不需要補空白. 平台名稱多於4個中文時,取前4個中文
		String ecName = ecData.EC_NAME_CH;
		if (ecName.getBytes("BIG5").length > 8) { // 取前4個中文字
			ecName = new String(ecName.getBytes(), 0, 8, "BIG5");
		}
		String NARRATIV = ecName + "付款";
		LOG.info("NARRATIV=" + NARRATIV);

		// 發送 [電商平台交易電文] 進行交易扣款
		// 發送電文
		TransactionResponseInfo transactionResponseInfo = new TransactionResponseInfo();
		try {
			// 初始化並記錄 Request 處理的開始時間
			long statTime = System.currentTimeMillis();
			// 交易型態(transactionType): 1: 儲值口款, 2: 交易扣款, 3: 退款, 4: 提領, 行為: 退款: refund, 扣款:
			// debit, 提領: withdraw
			String transactionType = "";
			//20190619 Add 繳費稅 Begin
			//if ("A".equals(trnsData.TRNS_TYPE)){
			if ("A".equals(trnsData.TRNS_TYPE)|| "E".equals(trnsData.TRNS_TYPE)) {
			//20190619 Add 繳費稅 End	
				transactionType = "2";
			} else {
				transactionType = "1";
			}
			transactionResponseInfo = (TransactionResponseInfo) new TelegramBo().sendFCB91148W(ecData, trnsData, realAccount, trnsTime.substring(trnsTime.length() - 6), msgNo.substring(msgNo.length() - 4), transactionType, "debit", conn);
			if ("M1AE".equals(transactionResponseInfo.getERR_CODE())) {
				LOG.info("delayFlag錯誤..發送delayFlag查詢");
				if (new TelegramBo().sendFCB911002_1("CCIVR", conn))
					transactionResponseInfo = (TransactionResponseInfo) new TelegramBo().sendFCB91148W(ecData, trnsData, realAccount, trnsTime.substring(trnsTime.length() - 6), msgNo.substring(msgNo.length() - 4), transactionType, "debit", conn);
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
		

		// ======== IF 交易失敗，回沖扣款限額 ========
		// IF 交易失敗，回沖扣款限額
		if ("03".equals(trnsStts)) {
			// Begin Transaction.
			TransactionControl.transactionBegin(conn);

			rslt = dayCrdtContHome.backflush(idntAcnt, trnsTime.substring(0, 8), trnsAmount);
			if (!rslt) {
				// 回沖失敗??
				LOG.error("回沖日額度失敗:" + trnsTime.substring(0, 8) + "/idntAcnt=" + idntAcnt + "/Amount=" + trnsAmount);
			}
			rslt = mnthCrdtContHome.backflush(idntAcnt, trnsTime.substring(0, 6), trnsAmount);
			if (!rslt) {
				// 回沖失敗??
				LOG.error("回沖月額度失敗:" + trnsTime.substring(0, 6) + "/idntAcnt=" + idntAcnt + "/Amount=" + trnsAmount);
			}

			// commit
			TransactionControl.trasactionCommit(conn);
			// End Transaction.
			TransactionControl.transactionEnd(conn);
		}

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

	/**
	 * 檢核 交易金額 是否超過 每筆、每日、每月 限額
	 * 
	 * @param trnsLimt
	 *            每筆自訂限額
	 * @param dayLimt
	 *            每日自訂限額
	 * @param mnthLimt
	 *            每月自訂限額
	 * @param ecMonthLimit
	 *            電商每月最高消費金額(0表不控管)
	 * @param dayCont
	 *            帳號日累計金額
	 * @param mnthCont
	 *            帳號月累計金額
	 * @param mnthContCustId
	 *            身分證號月累計金額
	 * @param trnsAmount
	 *            交易金額
	 * @return boolean
	 */
	private boolean quotaCheck(long trnsLimt, long dayLimt, long mnthLimt, long ecMonthLimit, long dayCont,
			long mnthCont, long mnthContCustId, int trnsAmount) {
		if (trnsLimt > 0 && trnsAmount > trnsLimt) {
			return false;
		} else if (dayLimt > 0 && (trnsAmount + dayCont) > dayLimt) {
			return false;
		} else if (mnthLimt > 0 && (trnsAmount + mnthCont) > mnthLimt) {
			return false;
		}
		// V2.01 新增電商每月總額度
		else if (ecMonthLimit > 0 && (trnsAmount + mnthContCustId) > ecMonthLimit) {
			return false;
		}
		// V2.01 新增電商每月總額度 End
		return true;
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
			// m by jeff 一銀目前只有交易成功才發mail
			if ("02".equals(trnsData.TRNS_STTS)) { // 交易成功
				if (!StringUtil.isBlank(email)) {
					String EC_NAME_CH = (String) map.get("EC_NAME_CH");
					String TRNS_TIME = (String) map.get("TRNS_TIME");
					String TRNS_NOTE = (String) map.get("TRNS_NOTE");

					// 發送email
					String subject = "";
					String templateFile = "";
					if ("02".equals(trnsData.TRNS_STTS)) { // 交易成功
						templateFile = "ACL-G-05.vm";
						subject = APSystem.getProjectParam("MAIL_SUBJECT5"); // A/C Link交易扣款成功
					} else { // 交易失敗
						templateFile = "ACL-G-06.vm";
						subject = APSystem.getProjectParam("MAIL_SUBJECT6"); // A/C Link交易扣款失敗
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
					mailMap.put("REAL_ACNT", CommonUtil.relAcntMask(trnsData.REAL_ACNT));
					// 訂單編號
					mailMap.put("ORDR_NO", trnsData.ORDR_NO);
					// 交易備註
					mailMap.put("TRNS_NOTE", TRNS_NOTE);
					// 交易金額
					mailMap.put("TRNS_AMNT", CommonUtil.limtFormat(Long.parseLong(trnsAmt)));
					// 錯誤訊息
					mailMap.put("ERR_MSG", resBean.getRTN_MSG());

					String mailContent = MailUtil.renderMailHtmlContent(mailMap, templateFile);
					new MailUtil().addMail(mailContent, subject, email, null, null, null);
				} // end if
			}
		} catch (Exception e) {
			LOG.info("email發送失敗");
			e.printStackTrace();
		}
	}
}
