/*
 * @(#)TelegramBo.java
 *
 * Copyright (c) 2007 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2007/04/04, Tim Cao
 *   1)first release
 *  v2.00, 2010/04/23, Eva Lin
 *   1)二階每日餘額比對檔  
 *   2)2010/05/18 從ftp獲取一階每日餘額比對檔資料
 *  v2.01, 2010/07/08, Eva Lin  
 *   1)YIYINVI-6136 OBU央行報送資料
 *   2)YIYINVI-6141 日終主機資料同步
 *  V2.02, 2010/07/13, Marvin
 *   1)YIYINVI-6138,逾催報送檔  
 *  V2.03, 2010/12/09, Eva 
 *   1)YIYINVI-6207,32492 與 50016 請另外再產生兩個檔案，核對類別、是否核對餘額欄位值為3和0      
 *  V3.00, 2018/02/05 For 一銀調整
 *   1) 調整街口使用當作儲值交易，由TRNS_NOTE 判斷，若TRNS_NOTE="JKO Deposit" 則視為儲值交易 
 */
package com.hitrust.bank.telegram;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.hitrust.acl.APSystem;
import com.hitrust.acl.exception.DBException;
import com.hitrust.bank.common.CommonUtil;
import com.hitrust.bank.dao.beans.CustAcntLink;
import com.hitrust.bank.dao.beans.EcData;
import com.hitrust.bank.dao.beans.SysParm;
import com.hitrust.bank.dao.beans.TrnsData;
import com.hitrust.bank.dao.home.SysParmHome;
import com.hitrust.bank.telegram.exception.MessageException;
import com.hitrust.bank.telegram.exception.UtilException;
import com.hitrust.bank.telegram.message.FBGenericMessage;
import com.hitrust.bank.telegram.req.FCB91148WRequestInfo;
import com.hitrust.bank.telegram.req.FCB91920YRequestInfo;
import com.hitrust.bank.telegram.req.FCB91970266RequestInfo;
import com.hitrust.bank.telegram.req.FCB91970363RequestInfo;
import com.hitrust.bank.telegram.req.FCB91971306RequestInfo;
import com.hitrust.bank.telegram.req.FCB9110021RequestInfo;
import com.hitrust.bank.telegram.req.FCB91103WRequestInfo;
import com.hitrust.bank.telegram.res.AccountListResponseInfo;
import com.hitrust.bank.telegram.res.FCB9110021ResponseInfo;
import com.hitrust.bank.telegram.res.FCB91103WResponseInfo;
import com.hitrust.bank.telegram.res.FCB91148WResponseInfo;
import com.hitrust.bank.telegram.res.FCB91920YResponseInfo;
import com.hitrust.bank.telegram.res.FCB91970266Record;
import com.hitrust.bank.telegram.res.FCB91970266ResponseInfo;
import com.hitrust.bank.telegram.res.FCB91970363Record;
import com.hitrust.bank.telegram.res.FCB91970363ResponseInfo;
import com.hitrust.bank.telegram.res.FCB91971306ResponseInfo;
import com.hitrust.bank.telegram.res.GenericHostResponseInfo;
import com.hitrust.bank.telegram.res.MemberMsgResponseInfo;
import com.hitrust.bank.telegram.res.NotificationHostMsgResponseInfo;
import com.hitrust.bank.telegram.res.TransactionResponseInfo;
import com.hitrust.framework.exception.ApplicationException;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.util.DateUtil;
import com.hitrust.telegram.HostRequestInfo;
import com.hitrust.telegram.HostResponseInfo;
import com.hitrust.bank.telegram.util.StringUtil;

/**
 * The Class TelegramBo.
 */
public class TelegramBo {

	// Log4J
	/** The LOG. */
	private static Logger LOG = Logger.getLogger(TelegramBo.class);

	// Telegram status
	/** The Constant SUCCESS_STATUS. */
	public final static String SUCCESS_STATUS = "1"; // 成功

	/** The Constant UNKNOWN_STATUS. */
	public final static String UNKNOWN_STATUS = "2"; // 未知

	/** The Constant ERROR_STATUS. */
	public final static String ERROR_STATUS = "3"; // 失敗
	// Approval status
	/** The Constant REGULAR_APPROVAL. */
	public final static String REGULAR_APPROVAL = "1"; // 連動放行

	/** The Constant ABNORMAL_APPROVAL. */
	public final static String ABNORMAL_APPROVAL = "0"; // 不連動放行
	// Send type
	/** The Constant REGULAR_SEND. */
	public final static String REGULAR_SEND = "1"; // 連動放行

	/** The Constant ABNORMAL_SEND. */
	public final static String ABNORMAL_SEND = "0"; // 不連動放行

	String[] successCode = { "0000", "T100", "T611", "T92J", "T901"};

	/**
	 * Constructor.
	 */
	public TelegramBo() {

	}
	
	public HostResponseInfo sendTelegramGetCIF(String custID) throws Exception {
		MemberMsgResponseInfo memberMsgResponseInfo = new MemberMsgResponseInfo();
		
		MemberMsgResponseInfo FCB91971306ResponseInfo = (MemberMsgResponseInfo) this.sendFCB919713_06(custID);
		if("03".equals(FCB91971306ResponseInfo.getOUTPUT_CODE())) {
			MemberMsgResponseInfo FCB91970266ResponseInfo = (MemberMsgResponseInfo) this.sendFCB919702_66(custID);
			if("03".equals(FCB91970266ResponseInfo.getOUTPUT_CODE())) {
				BeanUtils.copyProperties(memberMsgResponseInfo, FCB91971306ResponseInfo);
				memberMsgResponseInfo.setNAME(FCB91970266ResponseInfo.getNAME());
				//20190725 雙因子認證 Begin
				LOG.info("2. Get 2FA Tel:"+FCB91970266ResponseInfo.getTELE());
				memberMsgResponseInfo.setTELE(FCB91970266ResponseInfo.getTELE());
				//20190725 雙因子認證 End
				
			}else {
				return FCB91970266ResponseInfo;
			}
		}else {
			return FCB91971306ResponseInfo;
		}
		return memberMsgResponseInfo;
	}

	public HostResponseInfo sendFCB919713_06(String custID) throws ApplicationException {
		FCB91971306RequestInfo fcb91971306RequestInfo = new FCB91971306RequestInfo();
		MemberMsgResponseInfo memberMsgResponseInfo = new MemberMsgResponseInfo();
		
		// jeff修改-set上行電文所需資訊 start
		custID = StringUtil.paddingaddzero(custID, 11, "R");
		fcb91971306RequestInfo.setHCustID(custID);
		fcb91971306RequestInfo.setFuncID("05");// 05查詢, 06變更
		fcb91971306RequestInfo.setCustID(custID);// 統一編號(重覆序號)11碼
		// set上行電文所需資訊 end

		FCB91971306ResponseInfo fcb91971306ResponseInfo = (FCB91971306ResponseInfo) this.sendTelegram("FCB919713_06", "FCB919713_06", fcb91971306RequestInfo);
		if(fcb91971306ResponseInfo != null && Arrays.asList(successCode).contains(fcb91971306ResponseInfo.getHstatusCode())) {
			if (!"Y".equals(fcb91971306ResponseInfo.getIsAlertOrDerConAccNo())) {
				memberMsgResponseInfo.setOUTPUT_CODE("03");
				memberMsgResponseInfo.setSTATUS("000");
				//20190725 雙因子認證 Begin
				//memberMsgResponseInfo.setTELE(fcb91971306ResponseInfo.getCPhone());
				//20190725 雙因子認證 End
				memberMsgResponseInfo.setEMAIL(fcb91971306ResponseInfo.getEmail());
			}else {
				memberMsgResponseInfo.setOUTPUT_CODE("01");
				memberMsgResponseInfo.setS_HOST_CODE(fcb91971306ResponseInfo.getHstatusCode());
			}
		}else {
			memberMsgResponseInfo.setOUTPUT_CODE("01");
			memberMsgResponseInfo.setS_HOST_CODE(fcb91971306ResponseInfo.getHstatusCode());
		}
		memberMsgResponseInfo.setStatusCode(fcb91971306ResponseInfo.getHstatusCode());
		return memberMsgResponseInfo;
	}
	
	public HostResponseInfo sendFCB919702_66(String custID) throws ApplicationException {
		FCB91970266RequestInfo fcb91970266RequestInfo = new FCB91970266RequestInfo();
		MemberMsgResponseInfo memberMsgResponseInfo = new MemberMsgResponseInfo();
		
		custID = StringUtil.paddingaddzero(custID, 11, "R");
		fcb91970266RequestInfo.setHCustID(custID);
		fcb91970266RequestInfo.setOBUCD("D");
		fcb91970266RequestInfo.setCustID(custID);
		fcb91970266RequestInfo.setRepeatSeq("0");// 重複序號
		fcb91970266RequestInfo.setInqNo("66");// 查詢代號
		
		FCB91970266ResponseInfo fcb91970266ResponseInfo = (FCB91970266ResponseInfo) this.sendTelegram("FCB919702_66", "FCB919702_66", fcb91970266RequestInfo);
		List<FCB91970266Record> list = fcb91970266ResponseInfo.getRecords();
		if(fcb91970266ResponseInfo != null && Arrays.asList(successCode).contains(fcb91970266ResponseInfo.getHstatusCode()) && list != null && list.size() > 0) {
			memberMsgResponseInfo.setOUTPUT_CODE("03");
			memberMsgResponseInfo.setNAME(list.get(0).getCustName());
			//20190725 雙因子認證 Begin
			if (StringUtil.isBlank(list.get(0).getTFATelNo())) {
				memberMsgResponseInfo.setTELE(fcb91970266ResponseInfo.getTFATelNo());
				LOG.info("1. Set 2FA Tel:" + fcb91970266ResponseInfo.getTFATelNo());
			} else {
				memberMsgResponseInfo.setTELE(list.get(0).getTFATelNo());
				LOG.info("1A. Set 2FA Tel:" + list.get(0).getTFATelNo());
			}
			
			//20190725 雙因子認證 End
		} else {
			memberMsgResponseInfo.setOUTPUT_CODE("01");
			memberMsgResponseInfo.setS_HOST_CODE(fcb91970266ResponseInfo.getHstatusCode());
		}
		memberMsgResponseInfo.setStatusCode(fcb91970266ResponseInfo.getHstatusCode());
		return memberMsgResponseInfo;
	}
	
	public HostResponseInfo sendFCB919703_63(String custID) throws ApplicationException {
		FCB91970363RequestInfo requestInfo = new FCB91970363RequestInfo();
		//jeff修改-set上行電文所需資訊 start
		requestInfo.setOBUCD("D");
		requestInfo.setCustID(custID);//身分證統一編號(10位)
		requestInfo.setCuCode(" ");
		requestInfo.setRptNo("0");//重複序號都先帶0
		requestInfo.setInqType("63");//帳戶查詢帶63
		//set上行電文所需資訊 end
		
		FCB91970363ResponseInfo responseInfo= (FCB91970363ResponseInfo) this.sendTelegram("FCB919703_63", "FCB919703_63", requestInfo);
		
		AccountListResponseInfo accountListResponseInfo = new AccountListResponseInfo();
		if(responseInfo != null && Arrays.asList(successCode).contains(responseInfo.getHstatusCode())) {
			int index = 0;
			accountListResponseInfo.setOUTPUT_CODE("03");
			List<FCB91970363Record> records = responseInfo.getRecords();
			List<String> accountLists = new ArrayList<String>();
			if(responseInfo.getRecords()!=null && responseInfo.getRecords().size()>0) {
				for(int i=0;i<records.size();i++) {
					FCB91970363Record record = records.get(i);
					//帳號第4及第5碼為：20、21、50～57、68、69才可以做帳號連結 && CrossFlag須為N
					String[] a = {"20","21","50","51","52","53","54","55","56","57","68","69"};
					if(Arrays.asList(a).contains(record.getAcctNo().substring(3, 5)) && "N".equals(record.getCrossFlag()) && !"39".equals(record.getNatureType())) {
						accountLists.add(record.getAcctNo());
					}
				}
			}
			accountListResponseInfo.setRecords(accountLists);
		}else {
			accountListResponseInfo.setOUTPUT_CODE("01");
			accountListResponseInfo.setS_HOST_CODE(responseInfo.getHstatusCode());
		}
		accountListResponseInfo.setStatusCode(responseInfo.getHstatusCode());
		return accountListResponseInfo;
	}
	
	public HostResponseInfo sendFCB91920Y(String accNo, String agencyCode, String action) throws ApplicationException {
		String movCode = "";
		if ("link".equals(action))//綁定
			movCode = "1";
		else if ("cancel".equals(action))//解綁
			movCode = "2";
		else if ("request".equals(action))//查詢
			movCode = "5";

		FCB91920YRequestInfo requestInfo = new FCB91920YRequestInfo();
		requestInfo.setHAcctNo(accNo);
		requestInfo.setDesAccNo(accNo);
		requestInfo.setMovCode(movCode);
		requestInfo.setAgencyCode(agencyCode);
		requestInfo.setChannelType("E");

		FCB91920YResponseInfo responseInfo = (FCB91920YResponseInfo) this.sendTelegram("FCB91920Y", "FCB91920Y", requestInfo);
		NotificationHostMsgResponseInfo notificationHostMsgResponseInfo = new NotificationHostMsgResponseInfo();
		if (responseInfo != null && (Arrays.asList(successCode).contains(responseInfo.getHstatusCode())) ||
				("1".equals(movCode) && Arrays.asList(APSystem.getProjectParam("BIND_PASS")).contains(responseInfo.getHstatusCode())) || 
				("2".equals(movCode) && Arrays.asList(APSystem.getProjectParam("CANCEL_PASS")).contains(responseInfo.getHstatusCode()))) {
			notificationHostMsgResponseInfo.setOUTPUT_CODE("03");
			notificationHostMsgResponseInfo.setMovResult(responseInfo.getMovResult());
		} else {
			notificationHostMsgResponseInfo.setOUTPUT_CODE("01");
			notificationHostMsgResponseInfo.setS_HOST_CODE(responseInfo.getHstatusCode());
		}
		notificationHostMsgResponseInfo.setStatusCode(responseInfo.getHstatusCode());
		return notificationHostMsgResponseInfo;
	}

	public boolean sendFCB911002_1(String qryCode, Connection conn) throws ApplicationException {
		try {
			String delayCode = "";
			FCB9110021RequestInfo requestInfo = new FCB9110021RequestInfo();
			requestInfo.setQryCode(qryCode);
			FCB9110021ResponseInfo responseInfo = (FCB9110021ResponseInfo) this.sendTelegram("FCB911002_1", "FCB911002_1", requestInfo);
			if (responseInfo != null && Arrays.asList(successCode).contains(responseInfo.getHstatusCode())) {
				delayCode = responseInfo.getDelayCode();
				SysParmHome sysParmHome = new SysParmHome(conn);
				sysParmHome.updateSysParmByKey("DELAY_FLAG", delayCode);
				LOG.info("目前delayCode="+delayCode);
				return true;
			}
			return false;
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public HostResponseInfo sendFCB91148W(EcData ecData, TrnsData trnsData, String realAccount, String txSeqNo, String txSubSeq, String transactionType, String action, Connection conn) throws ApplicationException {
		TransactionResponseInfo transactionResponseInfo = null;
		try {
			FCB91148WRequestInfo requestInfo = new FCB91148WRequestInfo();
			SysParmHome sysParmHome = new SysParmHome(conn);
			SysParm sysParm = sysParmHome.fetchSysParmByKey("DELAY_FLAG");
			String delayFlag = sysParm.PARM_VALUE;
			
			String agencyCode=ecData.EC_ID;//平台代碼
//			String cMemo = ecData.EC_NAME_CH;//平台名稱
			String cMemo = APSystem.getProjectParam("EC_PAY_NAME_"+agencyCode);
			// jeff修改-set上行電文所需資訊 start
			requestInfo.setPayerBank("00700000");// 轉出行號
			
			if("debit".equals(action)) { //扣款
				if(cMemo.length()==2)
					cMemo = cMemo+"扣款";
				else
					cMemo = cMemo+"扣";
				requestInfo.setPayerAcctNo(realAccount);// 轉出帳號
				requestInfo.setPayerID(StringUtil.paddingaddzero(trnsData.CUST_ID, 11, "R"));
				requestInfo.setPayeeAcctNo("00000000000");// 轉入帳號
				requestInfo.setPmtType("96");// 固定帶96
			}else if("refund".equals(action)) { //退款
				if(cMemo.length()==2)
					cMemo = cMemo+"退款";
				else
					cMemo = cMemo+"退";
				requestInfo.setPayerAcctNo(ecData.REAL_ACNT);// 轉出帳號(電商平台的實體帳號)
				requestInfo.setPayerID(StringUtil.paddingaddzero(StringUtil.paddingaddzero(ecData.ENTR_ID, 9, "R"), 11, "L"));
				requestInfo.setPayeeAcctNo(realAccount);// 轉入帳號
				requestInfo.setPmtType("02");// 固定帶02
			}else if("withdraw".equals(action)) {//提領
				if(cMemo.length()==2)
					cMemo = cMemo+"提領";
				else
					cMemo = cMemo+"提";
				requestInfo.setPayerAcctNo(ecData.REAL_ACNT);// 轉出帳號(電商平台的實體帳號)
				requestInfo.setPayerID(StringUtil.paddingaddzero(StringUtil.paddingaddzero(ecData.ENTR_ID, 9, "R"), 11, "L"));
				requestInfo.setPayeeAcctNo(realAccount);// 轉入帳號
				requestInfo.setPmtType("02");// 固定帶02
			}
			
			//V2.01, 2018/02/05 For 一銀調整街口使用當作儲值交易，由TRNS_NOTE 判斷，若TRNS_NOTE="JKO Deposit" 則視為儲值交易  Begin
			//當街口交易(agencyCode="0001")備註(TRNS_NOTE) 中有帶"Deposit" 則視為儲值交易
//			if ("0001".equals(agencyCode) && trnsData.TRNS_NOTE.toUpperCase().indexOf("DEPOSIT") != -1) {
//				transactionType = "1";
//     		}
			
			//V2.01, 2018/02/05  調整街口使用當作儲值交易，由TRNS_NOTE 判斷，若TRNS_NOTE="JKO Deposit" 則視為儲值交易  End
			if (cMemo.getBytes("BIG5").length > 10) { // 取前5個中文字
				cMemo = new String(cMemo.getBytes(), 0, 8, "BIG5");
			}
			requestInfo.setCMemo(cMemo);//平台名稱
			requestInfo.setTxSeqNo(txSeqNo);
			requestInfo.setTxSubSeq(txSubSeq);
			requestInfo.setPayeeBank("00700000");// 轉入行號
			requestInfo.setTxAmt(CommonUtil.fmtAmnt4TeleUpld(trnsData.TRNS_AMNT, 11, 2));// 交易金額
			requestInfo.setSNo(trnsData.RECV_ACNT);
			requestInfo.setDelayFlag(delayFlag);//延遲註記
			requestInfo.setClsDate("000000");// 固定帶000000
			requestInfo.setTransType(StringUtil.padding("L"+agencyCode + transactionType + StringUtil.paddingaddzero(trnsData.CUST_ID, 11, "R"), 20));//街口支付是0001
			requestInfo.setEcFlag("L000000000");// 沖正序號
			// set上行電文所需資訊 end

			FCB91148WResponseInfo responseInfo = (FCB91148WResponseInfo) this.sendTelegram("FCB91148W", "FCB91148W",requestInfo);
			transactionResponseInfo = new TransactionResponseInfo();
			transactionResponseInfo.setH_CYCL_NO(responseInfo.getTxSeqNo()+responseInfo.getTxSubSeq());// 電文序號主序號+次序號
			transactionResponseInfo.setH_JRNL_NO("");// 主機處理序號 
			
			//20190619 Add 交易失敗時記錄上下行電文 Begin
			transactionResponseInfo.setTiTa(responseInfo.getTiTa().replaceAll(System.getProperty("line.separator"),"")); 
			transactionResponseInfo.setToTa(responseInfo.getToTa().replaceAll(System.getProperty("line.separator"),""));  
			//20190619 Add 交易失敗時記錄上下行電文 End
			
			if (responseInfo != null && Arrays.asList(successCode).contains(responseInfo.getHstatusCode())) {
				transactionResponseInfo.setHOST_DATE(responseInfo.getTxDate());
				transactionResponseInfo.setHOST_TIME(responseInfo.getTxTime());
				transactionResponseInfo.setOUTPUT_CODE("RF");
				transactionResponseInfo.setERR_CODE("0000");
				transactionResponseInfo.setH_JRNL_NO(responseInfo.getHostSeqNo1());// 主機處理序號 
			} else {
				transactionResponseInfo.setOUTPUT_CODE("");
				transactionResponseInfo.setERR_CODE(responseInfo.getHstatusCode());
			}
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return transactionResponseInfo;
	}
	
	public HostResponseInfo sendFCB91103W(TrnsData trnsData) throws ApplicationException {
		FCB91103WRequestInfo requestInfo = new FCB91103WRequestInfo();
		// jeff修改-set上行電文所需資訊 start
		requestInfo.setPayerAcctNo(trnsData.REAL_ACNT);
		requestInfo.setCustID(trnsData.CUST_ID);
		requestInfo.setTxnDate(trnsData.EC_MSG_NO.substring(0,8));
		requestInfo.setTxnSeqNo(trnsData.TELE_NO.substring(0,6));
		requestInfo.setTxnSubSeq(trnsData.TELE_NO.substring(6,10));
		requestInfo.setTxnAmt(CommonUtil.fmtAmnt4TeleUpld(trnsData.TRNS_AMNT, 11, 2));
		requestInfo.setEcFlag("L000000000");// 沖正序號
		// set上行電文所需資訊 end

		FCB91103WResponseInfo responseInfo = (FCB91103WResponseInfo) this.sendTelegram("FCB91103W", "FCB91103W", requestInfo);
		TransactionResponseInfo transactionResponseInfo = new TransactionResponseInfo();
		
		if (responseInfo != null && Arrays.asList(successCode).contains(responseInfo.getHstatusCode())) {
			transactionResponseInfo.setOUTPUT_CODE("03");
			transactionResponseInfo.setJRNL_NO(responseInfo.getHostSeqNo1());
		} else {
			transactionResponseInfo.setOUTPUT_CODE("01");

		}
		transactionResponseInfo.setS_HOST_CODE(responseInfo.getHstatusCode());
		transactionResponseInfo.setH_CYCL_NO("");// 電文序號
		transactionResponseInfo.setH_JRNL_NO("");// 主機處理序號 
		return transactionResponseInfo;
	}

	/**
	 * Send telegram
	 * 
	 * @param msg_code
	 * @param txn_code
	 * @param hostRequestInfo
	 * @return
	 * @throws ApplicationException
	 */
	private HostResponseInfo sendTelegram(String msg_code, String txn_code, HostRequestInfo hostRequestInfo)
			throws ApplicationException, BusinessException {
		FBGenericMessage message;
		try {
			LOG.debug("sendTelegram msg_code:" + msg_code);
			LOG.debug("sendTelegram txn_code:" + txn_code);
			message = new FBGenericMessage(msg_code, txn_code, hostRequestInfo, null);
			LOG.debug("message=" + message);
			// 3.Send to Host
			LOG.debug("hostRequestInfo=" + hostRequestInfo);
			message.setHostRequestInfo(hostRequestInfo);

			// message.postTelegram();
			message.postTelegram(null);
			// 4.Get response

			GenericHostResponseInfo genericHostResponseInfo = (GenericHostResponseInfo) message.getHostResponseInfo();

			LOG.debug("GenericHostResponseInfo=" + (GenericHostResponseInfo) message.getHostResponseInfo());
			genericHostResponseInfo.setMsgCode(msg_code);
			
			//20190619 Add 交易失敗時記錄上下行電文 Begin
			genericHostResponseInfo.setTiTa(new String(message.getSendTelegram(), message.getSTelegramEncoding()));
			genericHostResponseInfo.setToTa(new String(message.getRecvTelegram(), message.getRTelegramEncoding()));
			//20190619 Add 交易失敗時記錄上下行電文 End
			
			return genericHostResponseInfo;
		} catch (ApplicationException e) {
			LOG.error("Arf debug - ApplicationException:[" + e.toString() + "]");
			throw e;
		} catch (MessageException e) {
			e.printStackTrace();
			// throw new ApplicationException(e.getErrorMsg(), e.getErrorCode(),
			// e.getParameters(), e.getErrorCause());
			LOG.error("Arf debug - MessageException:[" + e.toString() + "]");
			throw new ApplicationException(e.getMessage());
			// } catch (CommunicationException e) {
			// throw new ApplicationException(e.getErrorMsg(), e.getErrorCode(),
			// e.getParameters(), e.getErrorCause());
		} catch (UtilException e) {
			LOG.error("Arf debug - UtilException:[" + e.toString() + "]");
			if (e.getErrorCause() instanceof ApplicationException) {
				throw (ApplicationException) e.getErrorCause();
			} else {
				throw e;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationException("telegram.module.error.noCode", e);
		}
	}
}