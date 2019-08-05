/**
 * @(#) TrnsDataServiceImpl.java
 *
 * Copyright (c) 2018 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2018/04/08
 * 	 1) JIRA-Number, First release
 * 
 */
package com.hitrust.acl.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.hitrust.acl.dao.EcDataDAO;
import com.hitrust.acl.dao.TrnsDataDAO;
import com.hitrust.acl.model.EcData;
import com.hitrust.acl.model.TrnsData;
import com.hitrust.acl.service.TrnsDataService;

public class TrnsDataServiceImpl implements TrnsDataService {
	private Logger LOG = Logger.getLogger(TrnsDataServiceImpl.class);
	
	private TrnsDataDAO trnsDataDAO;
	
	private EcDataDAO ecDataDAO;
	
	public void setTrnsDataDAO(TrnsDataDAO trnsDataDAO) {
		this.trnsDataDAO = trnsDataDAO;
	}

	public void setEcDataDAO(EcDataDAO ecDataDAO) {
		this.ecDataDAO = ecDataDAO;
	}

	@Override
	public List<TrnsData> getTrnsData4JKOS(String ecId, Date strtDate, Date endDate, String trnsStts,
			String orderAttrName) {
		return trnsDataDAO.getTrnsData4JKOS(ecId, strtDate, endDate, trnsStts, orderAttrName);
	}

	@Override
	public List<EcData> getEcDataList() {
		return ecDataDAO.getEcDataList();
	}

}
