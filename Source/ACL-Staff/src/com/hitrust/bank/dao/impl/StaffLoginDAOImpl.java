/**
 * @(#) StaffLoginImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/01/28, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.bank.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.hitrust.acl.util.DateUtil;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.StaffLoginDAO;
import com.hitrust.bank.model.StaffLogin;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class StaffLoginDAOImpl extends BaseDAOImpl implements StaffLoginDAO {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(StaffLoginDAOImpl.class);

	/**
	 * 依據使用者代號取得登入資訊
	 * @param userId 使用者代號
	 * @return StaffLogin or null
	 */
	@Override
	public StaffLogin fetchStaffLogin(String userId) {
		LOG.info("[fetchStaffLogin userId]: " + userId);
		
		StaffLogin login = null;
		List<StaffLogin> list = new ArrayList<StaffLogin>();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(StaffLogin.class);
		criteria.add(Restrictions.eq("userId", userId));
		
		list = (List<StaffLogin>) this.query(criteria);
		
		if (list.size() > 0) {
			login = list.get(0);
		}
		
		return login;
	}

	/**
	 * 依據使用者代號、登入 SessionId, 更新行員登入控制檔
	 * @param userId	使用者代號
	 * @param sessionId session Id
	 */
	@Override
	public void updateStaffLogin(String userId, String sessionId) {
		
		StaffLogin login = fetchStaffLogin(userId);
		
		if (StringUtil.isBlank(login)) {
			
		} else {
			login.setLoginTime(DateUtil.getCurrentTime("DT", "AD"));
			this.update(login);
		}
		
	}

}
