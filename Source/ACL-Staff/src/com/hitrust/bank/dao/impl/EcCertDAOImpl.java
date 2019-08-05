/**
 * @(#) EcCertDAOImpl.java
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

package com.hitrust.bank.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.hitrust.bank.dao.EcCertDAO;
import com.hitrust.bank.model.EcCert;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class EcCertDAOImpl extends BaseDAOImpl implements EcCertDAO {

	// Log4j
	private static Logger LOG = Logger.getLogger(EcCertDAOImpl.class);
	
	/**
	 * 依據 電商平台代碼取得 電商平台憑證資料清單
	 * @param ecId	平台代碼
	 * @return List<EcCert>
	 */
	@Override
	public List<EcCert> fetchEcCertListByEcId(String ecId) {
		
		List<EcCert> certs = new ArrayList<EcCert>();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(EcCert.class);
		criteria.add(Restrictions.eq("id.ecId", ecId));
		
		certs = this.query(criteria);
		
		return certs;
	}

	/**
	 * 依據 平台代碼 & 憑證識別碼 對應憑證資料
	 * @param ecId 	 憑證編號
	 * @param certCn 憑證識別碼
	 * @return EcCert or null
	 */
	@Override
	public EcCert fetchEcCertByKey(String ecId, String certCn) {
		
		EcCert cert = null;
		List<EcCert> certs = new ArrayList<EcCert>();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(EcCert.class);
		criteria.add(Restrictions.eq("id.ecId", ecId));
		criteria.add(Restrictions.eq("id.certCn", certCn));
		
		certs = this.query(criteria);
		
		if (!certs.isEmpty()) {
			cert = certs.get(0);
		}
		
		return cert;
	}

	/**
	 * 依據 憑證序號取得 電商平台憑證檔
	 * @param certSn
	 * @return EcCert or null
	 */
	@Override
	public EcCert fetchEcCertByCertSn(String certSn) {
		
		EcCert cert = null;
		List<EcCert> certs = new ArrayList<EcCert>();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(EcCert.class);
		criteria.add(Restrictions.eq("certSn", certSn));
		
		certs = query(criteria);
		
		if (!certs.isEmpty()) {
			cert = certs.get(0);
		}
		
		return cert;
	}

}
