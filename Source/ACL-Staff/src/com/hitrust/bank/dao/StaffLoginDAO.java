/**
 * @(#) StaffLoginDAO.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/01/28, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.dao;

import com.hitrust.bank.model.StaffLogin;
import com.hitrust.framework.dao.BaseDAO;

public interface StaffLoginDAO extends BaseDAO {
	
	/**
	 * 依據使用者代號取得登入資訊
	 * @param userId 使用者代號
	 * @return StaffLogin or null
	 */
	public StaffLogin fetchStaffLogin(String userId);
	
	/**
	 * 依據使用者代號、登入 SessionId, 更新行員登入控制檔
	 * @param userId	使用者代號
	 * @param sessionId session Id
	 */
	public void updateStaffLogin(String userId, String sessionId);

}
