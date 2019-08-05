package com.hitrust.acl.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.hitrust.acl.dao.VwHostErrorLogDAO;
import com.hitrust.acl.model.VwHostErrorLog;
import com.hitrust.acl.service.VwHostErrorLogService;

public class VwHostErrorLogServiceImpl implements VwHostErrorLogService {
	static Logger LOG = Logger.getLogger(VwHostErrorLogServiceImpl.class);
	
	private VwHostErrorLogDAO vwHostErrorLogDAO;

	public void setVwHostErrorLogDAO(VwHostErrorLogDAO vwHostErrorLogDAO) {
		this.vwHostErrorLogDAO = vwHostErrorLogDAO;
	}
	
	@Override
	public List<VwHostErrorLog> getVwHostErrorLogByTrnsDttm(Date fromDttm){
		return vwHostErrorLogDAO.getVwHostErrorLogByTrnsDttm(fromDttm);
	}

	@Override
	public int getVwHErrLogCountByTrnsDttm(Date fromDttm) {
		return vwHostErrorLogDAO.getVwHErrLogCountByTrnsDttm(fromDttm);
	}

	@Override
	public List<VwHostErrorLog> getVHErrLogByTrnsDttm(int beforeMin){
		return vwHostErrorLogDAO.getVHErrLogByTrnsDttm(beforeMin);
	}

	@Override 
	public List<VwHostErrorLog> getVHErrLogByTrnsDttmAlertType(int beforeMin, String alertType){
		return vwHostErrorLogDAO.getVHErrLogByTrnsDttmAlertType(beforeMin, alertType);
	}
}
