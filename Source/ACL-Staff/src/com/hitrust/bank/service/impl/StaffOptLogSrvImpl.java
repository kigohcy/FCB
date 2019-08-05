/**
 * @(#)StaffOptLogSrvImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 操作記錄查詢Srv實作
 * 
 * Modify History:
 *  v1.00, 2016/01/25, Evan
 *   1) First release
 *  
 */
package com.hitrust.bank.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.hitrust.acl.common.ObjectSerialUtil;
import com.hitrust.acl.common.UUIDGen;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.StaffOptLogDAO;
import com.hitrust.bank.dao.StaffSysFnctDAO;
import com.hitrust.bank.dao.SysParmDAO;
import com.hitrust.bank.model.BlobData;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.StaffOptLog;
import com.hitrust.bank.model.SysParm;
import com.hitrust.bank.service.StaffOptLogSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public class StaffOptLogSrvImpl implements StaffOptLogSrv {
	// Log4j
	private static Logger LOG = Logger.getLogger(StaffOptLogSrvImpl.class);
	
	
	// DAO injection
	private StaffOptLogDAO staffOptLogDAO;
	private StaffSysFnctDAO staffSysFnctDAO;
	private SysParmDAO sysParmDAO;
	
	public void setStaffOptLogDAO(StaffOptLogDAO staffOptLogDAO) {
		this.staffOptLogDAO = staffOptLogDAO;
	}

	public void setStaffSysFnctDAO(StaffSysFnctDAO staffSysFnctDAO) {
		this.staffSysFnctDAO = staffSysFnctDAO;
	}
	
	public void setSysParmDAO(SysParmDAO sysParmDAO) {
		this.sysParmDAO = sysParmDAO;
	}

	/**
	 * 查詢初始化
	 * @return
	 */
	@Override
	public StaffOptLog queryInit() {
		StaffOptLog staffOptLog = new StaffOptLog();
		
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		String lang = user.getLocale().getLanguage() + "_" + user.getLocale().getCountry();
		staffOptLog.setStaffSysFnct(staffSysFnctDAO.getEnableStaffSysFnctList(lang));
		
		SysParm sysParm = (SysParm)sysParmDAO.queryById(SysParm.class, "STAFF_OPT_QURY_LIMT");
		staffOptLog.setSysParam(sysParm.getParmValue());
		return staffOptLog;
	}
	
	/**
     * 操作紀錄查詢
     * @param	strtDate	執行時間 (起日)
     * @param	endDate		執行時間 (迄日)
     * @param	userId		使用者代碼 
     * @param   fnctId		功能代碼 
     * @param	page		分頁物件
     * @return
     */
	@Override
	public PageQuery queryStaffOptLog(Date strtDate, Date endDate,
			String userId, String fnctId, Page page) {
		PageQuery pageQuery = staffOptLogDAO.getStaffOptLog(strtDate, endDate, userId, fnctId, page);
		
		return pageQuery;
	}
	
	/**
	 * 操作紀錄異動明細查詢
	 *  @param	staffOptLog command
	 */
	public void queryDetail(StaffOptLog staffOptLog) {
		String logNo = staffOptLog.getLogNo();
		staffOptLog.setBefore(null);
		staffOptLog.setAfter(null);
		StaffOptLog staffOptLogTemp = (StaffOptLog)staffOptLogDAO.queryById(StaffOptLog.class, logNo);
		String fnctId = staffOptLogTemp.getFnctId();
		String id= "";
        String action = staffOptLogTemp.getAction();
        try {
            //將XML檔案內容組回StaffOptLog物件，before
        	if(!StringUtil.isBlank(staffOptLogTemp.getBeforeId())){
        		LOG.debug("將XML檔案內容組回StaffOptLog物件，before begin...");
        		id = staffOptLogTemp.getBeforeId();
        		BlobData blobData = (BlobData)staffOptLogDAO.queryById(BlobData.class, id);
        		if(blobData!=null){
        			Command command = (Command) ObjectSerialUtil.bytesToObject(blobData.getLogData());
            		staffOptLog.setBefore(command);
        		}
        		LOG.debug("將XML檔案內容組回StaffOptLog物件，before end...");
        	}
        	//將XML檔案內容組回StaffOptLog物件，after
        	if(!StringUtil.isBlank(staffOptLogTemp.getAfterId())){
        		LOG.debug("將XML檔案內容組回StaffOptLog物件，after begin...");
        		id = staffOptLogTemp.getAfterId();
        		BlobData blobData = (BlobData) staffOptLogDAO.queryById(BlobData.class, id);
        		if(blobData!=null){
        			Command command = (Command) ObjectSerialUtil.bytesToObject(blobData.getLogData());
        			staffOptLog.setAfter(command);
        		}
        		LOG.debug("將XML檔案內容組回StaffOptLog物件，after end...");
        	}
        } catch (Exception e) {
            LOG.error("LoadLog Error: " + e.toString(), e);
        }
        staffOptLog.setQ_fnctId(fnctId);
        staffOptLog.setQ_action(action);
	}
	
	/**
     * 新增操作記錄
     * @param fnctId  功能代碼
     * @param operate 執行動作 (Q:查詢, I:新增, U:修改, D:刪除, A:審核, R:駁回)
     * @param rslt 	  (E: 進行中, Y: 成功, N: 失敗)
     * @param before  異動前 Command
     * @return
     */
	@Override
	public String insertOptLog(String fnctId, String operate, String rslt, Object before) {
		// 取得目前 User 資料物件
		LoginUser user = (LoginUser)APLogin.getCurrentUser();
       
		if(user == null) {
            LOG.warn("insertOptLogReady: APLogin.get() -> Loginopt is null !");
            return "";
        }
        
        String userId = user.getUserId();
        String userName = user.getUserName();
        String ip = user.getIp();
        // String roleIds = composeStrWithSymbol(user.getRoleIds(), ",");
        
        // 產生操作記錄 32 位 UUID
        String logNo = UUIDGen.genUUID();
        
        StaffOptLog optLog = (StaffOptLog) staffOptLogDAO.queryById(StaffOptLog.class, logNo);
        
        if(optLog == null){
            optLog = new StaffOptLog();
            optLog.setLogNo(logNo);
            optLog.setUserId(userId);
            optLog.setUserName(userName);
            optLog.setOprtDttm(new Date());
            optLog.setFnctId(fnctId);
            optLog.setAction(operate);
            optLog.setRslt(rslt);
            optLog.setIpAddr(ip);
            staffOptLogDAO.save(optLog);
            
        }else{
            LOG.error("[insertOptLog]: " + logNo + " 已存在!");
            throw new BusinessException("message.db.exist"); //message.db.exist=資料已存在!
        }
        
        try {
            // 儲存異動前資料
            if (before != null) {
                LOG.info("儲存異動前資料...");
                optLog.setBeforeId(UUIDGen.genUUID());  //異動前交易序號
                
                BlobData blobData = new BlobData();
                blobData.setLogType("1");  //1:行員端 2:客戶端
                blobData.setOprtDttm(new Date());
                blobData.setLogNo(optLog.getBeforeId());//異動前交易序號
                byte[] bs = ObjectSerialUtil.objectToBytes(before);
                blobData.setLogData(bs);
               
                LOG.info("save Blob 資料(異動前)...");
                staffOptLogDAO.save(blobData);
            }
            
        } catch (Exception e) {
            LOG.error("InsertLog Error: " + e.toString(), e);
            throw new BusinessException("message.sys.opt.insertFail");
        }
        
        return optLog.getLogNo();
    }
	
	/**
     * 更新操作記錄
     * @param result   E: 進行中, Y: 成功, N: 失敗
     * @param optLogNo optLogNo STAFF_OPT_LOG.LOG_NO
     * @param after	        異動後 Comand
     */
	public void updateOptLog(String result, String optLogNo, Object after) {
		
        if(StringUtil.isBlank(optLogNo)){
            LOG.warn("updateOptLogOk: staffOptLog is null!");
            
        } else {
        	try {
        		StaffOptLog optLog = (StaffOptLog) staffOptLogDAO.queryById(StaffOptLog.class, optLogNo);
        		
        		if (after != null) {
        			LOG.debug("儲存異動後資料...");
        			optLog.setAfterId(UUIDGen.genUUID());   //異動後交易序號
        			
        			BlobData blobData = new BlobData();
        			blobData.setLogType("1");  //1:行員端 2:客戶端
        			blobData.setOprtDttm(new Date());
        			blobData.setLogNo(optLog.getAfterId()); //異動後交易序號
        			byte[] bs = ObjectSerialUtil.objectToBytes(after);
        			blobData.setLogData(bs);
        			
        			LOG.debug("save Blob資料(異動後)...");
        			staffOptLogDAO.save(blobData);
				}
        		
        		optLog.setRslt(result);
        		staffOptLogDAO.update(optLog);
                
			} catch (Exception e) {
				 LOG.error("[updateOptLog Exception]: optLogNo: " + optLogNo, e);
			}
        }
    }
	
	/**
	 * 依據指定符號組合字串
	 * @param values
	 * @param symbol
	 * @return
	 */
	private String composeStrWithSymbol(List<String> values, String symbol) {
		String val = "";
		int valSize = values.size();
		
		for (int i = 0; i < valSize; i++) {
			
			if ((i + 1) == valSize) {
				val = val.concat(values.get(i));
				
			} else {
				val = val.concat(values.get(i) + symbol) ;
			}
		}
		
		return val;
	}
}
