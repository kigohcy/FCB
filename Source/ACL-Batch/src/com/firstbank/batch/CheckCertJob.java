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
import com.hitrust.acl.service.CheckCertService;
import com.hitrust.acl.service.SysParmService;

public class CheckCertJob extends AbstractAclBatch {
	
	private SysParmService sysParmService =  (SysParmService)super.getSpringBean("sysParmService");
	private CheckCertService checkCertService = (CheckCertService)super.getSpringBean("checkCertService");
	public static void main(String[] args){
        new CheckCertJob().execute(args);
    }
	
	@Override
	public void execute(String[] args) {
		LOG.info("======>AP 憑證到期通知批次  開始....");
		try{
			SysParm sysParm = sysParmService.fetchSysParmByParm("CERT_CHECK_DAY");
			if(sysParm==null){
				LOG.error("無法取得參數:CERT_CHECK_DAY");
				return;
			}
			String checkDays = sysParm.getParmValue();
			sysParm = sysParmService.fetchSysParmByParm("CERT_CHECK_MAIL");
			if(sysParm==null){
				LOG.error("無法取得參數:CERT_CHECK_MAIL");
				return;
			}
			String bankReceiver = sysParm.getParmValue();;
			sysParm = sysParmService.fetchSysParmByParm("CERT_CHECK_MAIL_SUBJECT");
			if(sysParm==null){
				LOG.error("無法取得參數:CERT_CHECK_MAIL_SUBJECT");
				return;
			}
			String mailSubject = sysParm.getParmValue();;
			LOG.info("checkDays==>"+checkDays);
			List<Map<String,Object>> warnList = checkCertService.getCert4Warning(checkDays);
			LOG.debug("需通知筆數:"+warnList.size());
			if(warnList.size()==0){
				return;
			}
			String mailContent=null;
			String endDay;
			MailUtil mailUtil = new MailUtil();
			for(Map<String,Object> noticeMap:warnList){
				try{
					endDay = ((String)noticeMap.get("END_DTTM")).substring(0, 8);
					noticeMap.put("endDate", DateUtil.formateDateTimeForUser(endDay));
					noticeMap.put("cYear", Integer.parseInt(endDay.substring(0,4))-1911);
					noticeMap.put("cMonth", endDay.substring(4,6));
					noticeMap.put("cDay", endDay.substring(6));
					if(noticeMap.get("EMAIL")!=null){
						mailContent = MailUtil.renderMailHtmlContent(noticeMap, "CheckCertMail.vm");
						mailUtil.addMail(mailContent, mailSubject, (String)noticeMap.get("EMAIL"), StringUtils.isNotBlank(bankReceiver)?bankReceiver:null, null, null);
					}else{
						if(StringUtils.isNotBlank(bankReceiver)){
							mailContent = mailUtil.renderMailHtmlContent(noticeMap, "CheckCertMail.vm");
							mailUtil.addMail(mailContent, mailSubject, bankReceiver, null, null, null);
						}
					}
					LOG.info("################################");
					LOG.info(mailContent);
					LOG.info("################################");
				}catch(Exception e){
					LOG.error("send Mail Exception,",e);
				}
			}
		}catch(Exception e){
			LOG.error("CheckCertJob Exception:",e);
		}finally{
			LOG.info("======>AP 憑證到期通知批次  結束....");
		}
	}

	@Override
	public String[] cmdArgsConvertor(JobExecutionContext jobExecutionContext) throws CmdArgsException {
		return new String[0];
	}

}
