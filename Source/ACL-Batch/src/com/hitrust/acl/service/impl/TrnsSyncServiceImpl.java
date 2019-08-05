/**
 * @(#) DayCustContServiceImpl.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2018/03/28
 * 
 */
package com.hitrust.acl.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hitrust.acl.dao.EcDataDAO;
import com.hitrust.acl.dao.TrnsDataDAO;
import com.hitrust.acl.model.EcData;
import com.hitrust.acl.model.TrnsData;
import com.hitrust.acl.service.TrnsSyncService;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.telegram.TelegramBo;
import com.hitrust.bank.telegram.res.TransactionResponseInfo;

public class TrnsSyncServiceImpl implements TrnsSyncService {
	
	private static Logger LOG = Logger.getLogger(TrnsSyncServiceImpl.class);
	
	private TrnsDataDAO trnsDataDAO;
	
	private EcDataDAO ecDataDAO;
	
	public void setTrnsDataDAO(TrnsDataDAO trnsDataDAO) {
		this.trnsDataDAO = trnsDataDAO;
	}
	
	public void setEcDataDAO(EcDataDAO ecDataDAO) {
		this.ecDataDAO = ecDataDAO;
	}
	
	@Override
	public void TrnsSync(String date) throws Exception{
		Date dateStrtDate = DateUtil.formatStrToDate(DateUtil.countDate(date, -1), "000000");
	    Date dateEndDate = DateUtil.formatStrToDate(date, "235959");
	    
	    LOG.info(dateStrtDate + " ~ " + dateEndDate);
		List<TrnsData> trnsDataList = trnsDataDAO.fetchNukonwSttsTrns(dateStrtDate,  dateEndDate);
		
		EcData ecData = null;
		String trnsStts=null;
		String journalNum=null;
		String hostCode=null;
		
		if(trnsDataList != null && trnsDataList.size()>=1) {
			LOG.info("trnsDataList size = " + trnsDataList.size());
			for(TrnsData trnsData:trnsDataList){
				trnsStts=null;
				journalNum=null;
				hostCode=null;
				TransactionResponseInfo transactionResponseInfo = new TransactionResponseInfo();
				try {
					ecData = ecDataDAO.fetchEcDataByKey(trnsData.getId().getEcId());
					if(StringUtil.isBlank(ecData)){
						LOG.error("查無電商資料:"+trnsData.getId().getEcId());
						continue;
					}
					
					DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
					String trnsDate = df.format(trnsData.getTrnsDttm());
					
					transactionResponseInfo = (TransactionResponseInfo) new TelegramBo().sendFCB91103W(trnsData, ecData);
					hostCode = transactionResponseInfo.getERR_CODE(); //v1.01, 取得 error code
					String outPutCode = transactionResponseInfo.getOUTPUT_CODE();
					journalNum = transactionResponseInfo.getH_JRNL_NO(); //主機交易序號
					LOG.debug("OUTPUT_CODE="+outPutCode);
					if("01".equals(outPutCode)) {
						//01暫時不異動任何資料....
					}else {
						if("02".equals(outPutCode)){ //02-成功
							trnsStts = "02"; //交易成功
						}else if("03".equals(outPutCode)){ //03-失敗
							trnsStts = "03"; //交易失敗
							LOG.error(hostCode); //v1.02, 調整不顯示 Bancs訊息, 只寫 LOG
						}
						trnsData.setTrnsStts(trnsStts);
						trnsData.setErrCode(StringUtils.equals("02", trnsStts)?"0000":"7010");
						
						if(StringUtil.isBlank(hostCode))
							trnsData.setHostCode(hostCode);
						if(StringUtil.isBlank(journalNum))
							trnsData.setHostSeq(journalNum);
						if(StringUtil.isBlank(transactionResponseInfo.getH_CYCL_NO()))
							trnsData.setTeleNo(transactionResponseInfo.getH_CYCL_NO());
						
						trnsDataDAO.update(trnsData);
					}
					
				} catch (Exception e) {
					LOG.error("[Exception]: ", e);
				}
			}
		}else {
			LOG.info("無狀態不明交易");
		}
	}
}
