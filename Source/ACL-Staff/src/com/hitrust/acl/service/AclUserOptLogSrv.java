/**
 * @(#) AbstractAclController.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/02/02, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.acl.service;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.hitrust.bank.service.StaffOptLogSrv;
import com.hitrust.bank.service.TsbApauditlogSrv;
import com.hitrust.framework.APSystem;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.BaseCommand;
import com.hitrust.framework.web.UserOptLog;

public class AclUserOptLogSrv implements UserOptLog {

	// Log4j
	private static Logger LOG = Logger.getLogger(AclUserOptLogSrv.class);
	
	// service injection
	private StaffOptLogSrv staffOptLogSrv = (StaffOptLogSrv) APSystem.getApplicationContext().getBean("staffOptLogSrv");
	private TsbApauditlogSrv tsbApauditlogSrv = (TsbApauditlogSrv) APSystem.getApplicationContext().getBean("tsbApauditlogSrv");
	
	
	// 操作記錄 Key 值
	private String optLogNo;
	
	// ====================
	// 不記錄異動前異動後例外清單
	// Q: 查詢, R: 報表, O: 匯出下載, P: 列印
	// L: 系統登入, X: 系統登出, M: 密碼變更, W: 密碼重設 
	// ====================
	List<String> operates = Arrays.asList("Q", "R", "O", "P", "L", "X", "M", "W"); 
	
	/**
	 * 新增操作記錄
	 * @param rslt 執行結果 (E: 進行中, Y: 成功, N: 失敗)
	 * @param commnd 異動前 command
	 * @throws BusinessException
	 */
	@Override
	public void insertOptLog(String rslt, BaseCommand commnd) throws BusinessException {
		
		Object before = null;	// 取得上一個 baseCommand
		String funcId = commnd.getFuncId();
		String operate = commnd.getOperate();
		
		try {
			// 例外清單, 不記錄「異動前」操作記錄
			if (!operates.contains(operate)) {
				before = commnd.getLastComd();
			}
			
			// 新增「異動前」操作記錄
			LOG.info("========== 新增操作記錄 [功能代碼]: " + funcId + " [執行動作]: " + operate + " [執行結果]: " + rslt +  " [是否記錄異動前資料]: " + !operates.contains(operate) + " ==========");
			optLogNo = staffOptLogSrv.insertOptLog(funcId, operate, rslt, before);
			
		} catch (Exception e) {
			LOG.error("[insertRecord Exception]: ", e);
	
		}
	}

	/**
	 * 更新操作記錄
	 * @param rslt 執行結果 (E: 進行中, Y: 成功, N: 失敗)
	 * @param commnd 異動後 command
	 * @throws BusinessException
	 */
	@Override
	public void updateOptLog(String rslt, BaseCommand commnd) throws BusinessException {
		
		Object after = null; // 取得目前 baseCommand
		String funcId = commnd.getFuncId();
		String operate = commnd.getOperate();
		
		try {
			
			// 例外清單內, 不記錄「異動後」操作記錄
			if (!operates.contains(operate)) {
				after = commnd;
			}
			
			// 更新「異動後」操作記錄
			LOG.info("========== 更新操作記錄 [功能代碼]: " + funcId + " [執行動作]: " + operate + " [執行結果]: " + rslt +  " [是否記錄異動前資料]: " + !operates.contains(operate) + " ==========");
			staffOptLogSrv.updateOptLog(rslt, optLogNo, after);
			
		} catch (Exception e) {
			LOG.error("[updateOptLog Exception]: ", e);
		}
	}

	/**
	 * 新增應用系統日誌
	 * @param rslt	  交易執行結果, 成功: Y, 失敗: N
	 * @param rsltMsg 交易執行失敗錯誤代碼或訊息
	 * @param commnd
	 * @param req
	 */
	@Override
	public void insertTsbApauditLog(String rslt, String rsltMsg, BaseCommand commnd, HttpServletRequest req) throws BusinessException {
		
		tsbApauditlogSrv.insertTsbApauditLog(rslt, rsltMsg, commnd, req);
	}

	

}
