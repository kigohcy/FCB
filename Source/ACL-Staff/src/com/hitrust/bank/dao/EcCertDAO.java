/**
 * @(#) EcCertDAO.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/21, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.bank.dao;

import java.util.List;

import com.hitrust.bank.model.EcCert;
import com.hitrust.framework.dao.BaseDAO;

public interface EcCertDAO extends BaseDAO {
	
	/**
	 * 依據 電商平台代碼取得 電商平台憑證資料清單
	 * @param ecId	平台代碼
	 * @return List<EcCert>
	 */
	public List<EcCert> fetchEcCertListByEcId(String ecId);
	
	/**
	 * 依據 平台代碼 & 憑證識別碼 對應憑證資料
	 * @param ecId 	 憑證編號
	 * @param certCn 憑證識別碼
	 * @return EcCert or null
	 */
	public EcCert fetchEcCertByKey(String ecId, String certCn);
	
	/**
	 * 依據 憑證序號取得 電商平台憑證檔
	 * @param certSn
	 * @return EcCert or null
	 */
	public EcCert fetchEcCertByCertSn(String certSn);

}
