/*
 * @(#)GenericHostRequestRule.java
 *
 * Copyright (c) 2007 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2007/04/04, Tim Cao
 *   1)first release
 */
package com.hitrust.bank.telegram.req.rule;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.hitrust.bank.telegram.req.GenericHostRequestInfo;
import com.hitrust.bank.telegram.util.StringUtil;
//import com.hitrust.lib.APSystem;
import com.hitrust.framework.model.APLogin;
import com.hitrust.telegram.HostRequestInfo;
import com.hitrust.telegram.rule.HostRequestRule;
import com.hitrust.util.DateUtil;
import com.hitrust.util.MathUtil;

public abstract class GenericHostRequestRule implements HostRequestRule {

	protected HostRequestInfo hostRequestInfo; // Host request info

	/**
	 * Constructor
	 * @param hostRequestInfo
	 */
	public GenericHostRequestRule(HostRequestInfo hostRequestInfo) {
		this.hostRequestInfo = hostRequestInfo;
	}

	/**
	 * Process business rule
	 */
	public HostRequestInfo[] process(String transactionCode) {
		// 1.Set request header
		this.initialMessageHeader(transactionCode);
		// 2.Business to message
		return this.businessToMessage();
	}

	/**
	 * Initial message header
	 * @param transactionCode
	 */
	protected void initialMessageHeader(String transactionCode) {
		String random = MathUtil.randomNumber(6);
		
		((GenericHostRequestInfo)this.hostRequestInfo).setHTxID(transactionCode);
//		((GenericHostRequestInfo)this.hostRequestInfo).setHSystemKey(AppEnv.getString("TELEGRAM_SYSTEM_KEY"));
		((GenericHostRequestInfo)this.hostRequestInfo).setHSystemKey("FCB3A");
		
		if (StringUtil.isBlank(((GenericHostRequestInfo)this.hostRequestInfo).getHTxSeqNo())){
			((GenericHostRequestInfo)this.hostRequestInfo).setHTxSeqNo(DateUtil.getToday() + random);
//			((GenericHostRequestInfo)this.hostRequestInfo).setCorrelationID(txSeqNo);
			((GenericHostRequestInfo)this.hostRequestInfo).setHMsgSeqNo(random.substring(1));
		}else{
			((GenericHostRequestInfo)this.hostRequestInfo).setCorrelationID(((GenericHostRequestInfo)this.hostRequestInfo).getHTxSeqNo());
			((GenericHostRequestInfo)this.hostRequestInfo).setHMsgSeqNo();
		}
		
		((GenericHostRequestInfo)this.hostRequestInfo).setHCltTimeStamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		
		if(((GenericHostRequestInfo)this.hostRequestInfo).getHCustID() == null ){
			((GenericHostRequestInfo)this.hostRequestInfo).setHCustID("00000000");
		}
		
		if(((GenericHostRequestInfo)this.hostRequestInfo).getHUserID() == null ){
			String userId = "0000";
			//mark by jeff start
//			try{
//				LoginOpt loginOpt = (LoginOpt)APLogin.get();
//				userId = loginOpt.getUserId();  
//			}catch(Exception e){
//				userId = "0000";
//			}	
			//mark by jeff start
			
			//((GenericHostRequestInfo)this.hostRequestInfo).setHUserID("0000");
			((GenericHostRequestInfo)this.hostRequestInfo).setHUserID(userId);
		}
		
		if(((GenericHostRequestInfo)this.hostRequestInfo).getHAcctNo() == null ){
			((GenericHostRequestInfo)this.hostRequestInfo).setHAcctNo("00000000000");
		}
		
		((GenericHostRequestInfo)this.hostRequestInfo).setHMsgDirection("RQ");
		
	}
	
	

	/**
	 * Copy header attribute for multi telegram
	 * @param info
	 */
	protected void copyHeader(GenericHostRequestInfo info) {
		info.setHTxID(((GenericHostRequestInfo)this.hostRequestInfo).getHTxID());
		info.setHSystemKey(((GenericHostRequestInfo)this.hostRequestInfo).getHSystemKey());
		info.setHTxSeqNo(((GenericHostRequestInfo)this.hostRequestInfo).getHTxSeqNo());
		info.setHMsgSeqNo(((GenericHostRequestInfo)this.hostRequestInfo).getHMsgSeqNo());
		info.setHCltTimeStamp(((GenericHostRequestInfo)this.hostRequestInfo).getHCltTimeStamp());
		info.setHCustID(((GenericHostRequestInfo)this.hostRequestInfo).getHCustID());
		info.setHUserID(((GenericHostRequestInfo)this.hostRequestInfo).getHUserID());
		info.setHAcctNo(((GenericHostRequestInfo)this.hostRequestInfo).getHAcctNo());
		info.setHMsgDirection(((GenericHostRequestInfo)this.hostRequestInfo).getHMsgDirection());
	}

	/**
	 * Business to message
	 */
	protected abstract HostRequestInfo[] businessToMessage();
}
