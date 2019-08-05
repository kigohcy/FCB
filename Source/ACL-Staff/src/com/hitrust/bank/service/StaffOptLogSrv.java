/**
 * @(#)StaffOptLogSrv.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 操作記錄查詢Srv
 * 
 * Modify History:
 *  v1.00, 2016/01/25, Evan
 *   1) First release
 *  
 */
package com.hitrust.bank.service;

import java.util.Date;

import com.hitrust.bank.model.StaffOptLog;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public interface StaffOptLogSrv {
	
    /**
     * 查詢初始化
     * @return
     */
    public StaffOptLog queryInit();
    
    /**
     * 操作紀錄查詢
     * @param	strtDate	執行時間 (起日)
     * @param	endDate		執行時間 (迄日)
     * @param	userId		使用者代碼 
     * @param   fnctId		功能代碼 
     * @param	page		分頁物件
     * @return
     */
    public PageQuery queryStaffOptLog(Date strtDate, Date endDate, String userId, String fnctId, Page page);
	
    /**
     * description:查詢明細資料
     * @param staffOptLog
     */
	public void queryDetail(StaffOptLog staffOptLog);
	
    /**
     * 新增操作記錄
     * @param fnctId  功能代碼
     * @param operate 執行動作 (Q:查詢, I:新增, U:修改, D:刪除, A:審核, R:駁回)
     * @param before  異動前 Command
     * @return
     */
    public String insertOptLog(String fnctId, String operate, String rslt, Object before);
    
    /**
     * 更新操作記錄
     * @param result   E: 進行中, Y: 成功, N: 失敗
     * @param optLogNo optLogNo STAFF_OPT_LOG.LOG_NO
     * @param after	        異動後 Comand
     */
    public void updateOptLog(String result, String optLogNo, Object after);
    
}
