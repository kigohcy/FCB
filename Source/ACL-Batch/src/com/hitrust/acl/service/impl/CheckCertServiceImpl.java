package com.hitrust.acl.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.hitrust.acl.dao.EcCertDAO;
import com.hitrust.acl.service.CheckCertService;

public class CheckCertServiceImpl implements CheckCertService {
	private static Category LOG = Logger.getLogger(CheckCertServiceImpl.class);
	
	private EcCertDAO ecCertDAO;
	
	public void setEcCertDAO(EcCertDAO ecCertDAO) {
		this.ecCertDAO = ecCertDAO;
	}

	@Override
	public List<Map<String, Object>> getCert4Warning(String warnDay) {
		return ecCertDAO.getCert4Warning(warnDay);
	}

}
