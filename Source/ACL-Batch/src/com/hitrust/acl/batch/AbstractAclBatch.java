/*
 * @(#)AbstractAclBatch.java
 *
 * Copyright (c) 2008 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 * v1.00, 2008年8月13日, Kevin
 *   1) First release
 *
 */
package com.hitrust.acl.batch;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.context.ApplicationContext;

import com.hitrust.acl.batch.exception.CmdArgsException;
import com.hitrust.acl.model.ApAclBatchAuditLog;
import com.hitrust.acl.service.BatchAuditLogSrv;
import com.hitrust.acl.util.AclApplicationContextAwarer;
import com.hitrust.acl.util.SpringHelper;
import com.hitrust.util.DateUtil;

/**
 * 批次程式抽象物件<br>
 * 支援Quartz及Command line模式，所有系統所需的批次程式，均需繼承此抽象物件，並實作cmdArgsConvertor函示。
 *
 * @author Kevin Wang
 */
public abstract class AbstractAclBatch implements StatefulJob, AclBatch {
    
	public ApplicationContext ac;
	
	private BatchAuditLogSrv batchAuditLogSrv = (BatchAuditLogSrv)this.getSpringBean("batchAuditLogSrv");
	
    /**
     * Logging utiltity
     */
    protected Logger LOG = Logger.getLogger(getClass().getName());
    
    /**
     * Creates a new instance of AbstractEoiBatch
     */
    public AbstractAclBatch() {
    }

    /**
     * Quartz Job execute entry point, this will invoke command line mode "execute" method to execute job
     * @param jobExecutionContext Quartz jobExecutionContext
     * @throws org.quartz.JobExecutionException Thrown while exception occurs in job execcution
     */
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        
        ApAclBatchAuditLog log = null;
        String errMsg = StringUtils.EMPTY;
        
        // init audit log
        try {
//            log = batchAuditLogSrv.initAuditLog(jobExecutionContext);
        } catch (Exception ex) {
            LOG.error("Exception for the initAuditLog！", ex);
        }
        
        try {
            // excute job
            this.execute(this.cmdArgsConvertor(jobExecutionContext));
        } catch (Exception ex) {
            LOG.error("Exception occurs while processing job execution, please refer to the following exception messages！", ex);
            errMsg = StringUtils.isNotEmpty(ex.getMessage()) && ex.getMessage().length() > 200 ? ex.getMessage().substring(0, 200) : ex.getMessage() ;
//            this.sendSuccessMail(ex);
        } finally {
            try {
                String endDatetime = DateUtil.getCurrentTime("DT", "AD");
                
//                batchAuditLogSrv.updateAuditStatus(log.getId().getBatchId(), log.getId().getStartDatetime(),
//                        endDatetime, StringUtils.isEmpty(errMsg) ? "Y" : errMsg);
                
            } catch (Exception ex) {
                LOG.error("Exception for the updateAuditStatus！", ex);
            }
        }
    }

    /**
     * 轉換Quartz Job定義參數成Command Line模式所需參數
     * @param jobExecutionContext Quartz jobExecutionContext
     * @return Command line arguments
     * @throws com.hitrust.eoi.batch.exception.CmdArgsException Thrown when failed to convert command line args from jobExecutionContext
     */
    public abstract String[] cmdArgsConvertor(JobExecutionContext jobExecutionContext) throws CmdArgsException;
    
    /**
     * 當批次程式發生錯誤時，發郵件通知資訊室人員
     * @param e
     */
    private void sendSuccessMail(Exception e){
        try{
        }catch(Exception ee){
            LOG.error("sendSuccessMail error!", e);
        }    	
    }
    
    private String getClassName(){
    	String name = this.getClass().getName();
    	if(name.lastIndexOf(".")!=-1){
    		name = name.substring(name.lastIndexOf(".")+1, name.length());
    	}
    	return name;
    }
    
    /**
     * 透過spring提供的contextAware介面取得的ApplicationContext
     * 依據beanId向spirng取得bean。
     * 本方法與springHelper的機制最大的差異在於web端的運用；
     * 由於web端在啟動時，spring context便會存在於jvm中，
     * 透過EoiApplicationContextAwarer便能取得該context。
     * 但若是於web端以SpringHelper去取得需要的bean時，
     * 變會再次載入定義檔中所有的bean，這不僅會影響效能，
     * 也讓記憶體的使用加倍。透過EoiApplicationContextAwarer
     * 便能消彌這個缺點。
     * @param beanId
     * @return
     */
    protected Object getSpringBean(String beanId){
    	if (ac == null){
    		ac = AclApplicationContextAwarer.getApplicationContext();//首先嘗試取得framework自動載入的ac
    		if (ac == null){
    			ac = SpringHelper.getApplicationContext();//取不到freamwork的ac，則載入預設的SrpingHelper
    		}
    	}
    	return ac.getBean(beanId);
    }
    
}
