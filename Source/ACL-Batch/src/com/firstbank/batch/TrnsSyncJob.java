/**
 * @(#) CheckCertJob.java
 *
 * Directions:憑證到期通知批次
 *
 * Copyright (c) 2018 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2018/04/02
 *    1) First release
 *
 */
package com.firstbank.batch;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;

import com.hitrust.acl.batch.AbstractAclBatch;
import com.hitrust.acl.batch.exception.CmdArgsException;
import com.hitrust.acl.common.DateUtil;
import com.hitrust.acl.mail.MailUtil;
import com.hitrust.acl.model.SysParm;
import com.hitrust.acl.model.TrnsData;
import com.hitrust.acl.service.CheckCertService;
import com.hitrust.acl.service.SysParmService;
import com.hitrust.acl.service.TrnsSyncService;

public class TrnsSyncJob extends AbstractAclBatch {
	
	private TrnsSyncService trnsSyncService =  (TrnsSyncService)super.getSpringBean("trnsSyncService");
	public static void main(String[] args){
        new TrnsSyncJob().execute(args);
    }
	
	@Override
	public void execute(String[] args) {
		LOG.info("======>交易不明同步批次  開始....");
		try{
			String date = DateUtil.getCurrentTime("D", "AD");
			trnsSyncService.TrnsSync(date);
		}catch(Exception e){
			LOG.error("TrnsSyncJob Exception:",e);
		}finally{
			LOG.info("======>交易不明同步批次  結束....");
		}
	}

	@Override
	public String[] cmdArgsConvertor(JobExecutionContext jobExecutionContext) throws CmdArgsException {
		return new String[0];
	}

}
