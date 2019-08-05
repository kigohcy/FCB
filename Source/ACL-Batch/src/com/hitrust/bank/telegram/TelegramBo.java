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

import java.util.Arrays;
import org.apache.log4j.Logger;
import com.hitrust.acl.common.CommonUtil;
import com.hitrust.acl.common.DateUtil;
import com.hitrust.acl.model.EcData;
import com.hitrust.acl.model.TrnsData;
import com.hitrust.bank.telegram.exception.MessageException;
import com.hitrust.bank.telegram.exception.UtilException;
import com.hitrust.bank.telegram.message.FBGenericMessage;
import com.hitrust.bank.telegram.req.FCB91103WRequestInfo;
import com.hitrust.bank.telegram.res.FCB91103WResponseInfo;
import com.hitrust.bank.telegram.res.GenericHostResponseInfo;
import com.hitrust.bank.telegram.res.TransactionResponseInfo;
import com.hitrust.framework.exception.ApplicationException;
import com.hitrust.framework.exception.BusinessException;

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
	
	String[] trnsType = { "B", "C"};

	/**
	 * Constructor.
	 */
	public TelegramBo() {

	}
	
	public HostResponseInfo sendFCB91103W(TrnsData trnsData, EcData ecData) throws ApplicationException {
		FCB91103WRequestInfo requestInfo = new FCB91103WRequestInfo();
		
		String trnsDate = DateUtil.formatDate2Str(trnsData.getTrnsDttm());

		// jeff修改-set上行電文所需資訊 start
		requestInfo.setPayerAcctNo(trnsData.getRealAcnt());
		if(Arrays.asList(trnsType).contains(trnsData.getTrnsType())) 
			requestInfo.setCustID(StringUtil.paddingaddzero(StringUtil.paddingaddzero(ecData.getEntrId(), 10, "L"),11,"R"));
		else
			requestInfo.setCustID(StringUtil.paddingaddzero(trnsData.getCustId(), 11, "R"));
		
		requestInfo.setTxnDate(DateUtil.getDateOfROC(trnsData.getId().getEcMsgNo().substring(0,8)));
		requestInfo.setTxnSeqNo(trnsDate.substring(trnsDate.length() - 6));
		requestInfo.setTxnSubSeq(trnsData.getId().getEcMsgNo().substring(trnsData.getId().getEcMsgNo().length() - 4));
		requestInfo.setTxnAmt(CommonUtil.fmtAmnt4TeleUpld(trnsData.getTrnsAmnt(), 11, 2));
		requestInfo.setEcFlag("L000000000");// 沖正序號
		// set上行電文所需資訊 end

		FCB91103WResponseInfo responseInfo = (FCB91103WResponseInfo) this.sendTelegram("FCB91103W", "FCB91103W", requestInfo);
		TransactionResponseInfo transactionResponseInfo = new TransactionResponseInfo();
		transactionResponseInfo.setH_CYCL_NO(responseInfo.getTxSeqNo()+responseInfo.getTxSubSeq());// 電文序號
		if (responseInfo != null) {
			transactionResponseInfo.setH_JRNL_NO(responseInfo.getHostSeqNo1());// 主機處理序號 
			if(Arrays.asList(successCode).contains(responseInfo.getHstatusCode())) {
				transactionResponseInfo.setOUTPUT_CODE("02");
				transactionResponseInfo.setERR_CODE("0000");
			}else {
				transactionResponseInfo.setOUTPUT_CODE("03"); 
				transactionResponseInfo.setERR_CODE(responseInfo.getHstatusCode());
			}
		}else {
			transactionResponseInfo.setOUTPUT_CODE("01"); 
		}
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