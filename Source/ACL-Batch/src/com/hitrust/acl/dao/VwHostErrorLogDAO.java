/**
 * @(#)VwHostErrorLogDAO.java
 *
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 
 * 
 * Modify History:
 *  v1.00, 2017/10/02, Caleb Chen
 *   1) First release
 *  
 */
package com.hitrust.acl.dao;

import java.util.Date;
import java.util.List;

import com.hitrust.acl.model.VwHostErrorLog;
import com.hitrust.framework.dao.BaseDAO;


public interface VwHostErrorLogDAO extends BaseDAO {

	public List<VwHostErrorLog> getVwHostErrorLogByTrnsDttm(Date fromDttm);

	public int getVwHErrLogCountByTrnsDttm(Date fromDttm);

	public List<VwHostErrorLog> getVHErrLogByTrnsDttm(int beforeMin);

	public List<VwHostErrorLog> getVHErrLogByTrnsDttmAlertType(int beforeMin, String alertType);

}
