/**
 * @(#) AclDcRpt03Job.java
 *
 * Directions:日終累算作業批次
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

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;

import com.hitrust.acl.batch.AbstractAclBatch;
import com.hitrust.acl.batch.exception.CmdArgsException;
import com.hitrust.acl.common.DateUtil;
import com.hitrust.acl.service.DayAcntContService;
import com.hitrust.acl.service.DayCustContService;

public class AclDcRpt03Job extends AbstractAclBatch {
	// log4j
	static Logger LOG = Logger.getLogger(AclDcRpt03Job.class);
	
	private DayAcntContService dayAcntContService = (DayAcntContService)super.getSpringBean("dayAcntContService");
	
	private DayCustContService dayCustContService = (DayCustContService)super.getSpringBean("dayCustContService");
	
	public static void main(String[] args){
        new AclDcRpt03Job().execute(args);
    }
		
	@Override
	public void execute(String[] args) {
		LOG.info("======>AP 日終累算作業批次  開始....");
		try{
			
			String today = DateUtil.getCurrentTime("D", "AD");
			String yesterday = DateUtil.countDate(today, -1);
			LOG.info("======>資料日期:"+yesterday);
			dayAcntContService.genRpt03(yesterday);
			dayCustContService.genRpt03(yesterday);
		}catch(Exception e){
			LOG.error("AclDcRpt03Job Exception:",e);
		}finally{
			LOG.info("======>AP 日終累算作業批次  結束....");
		}
	}
	
	

	@Override
	public String[] cmdArgsConvertor(JobExecutionContext jobExecutionContext) throws CmdArgsException {
		return new String[0];
	}

}
