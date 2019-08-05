package com.hitrust.acl.service;

import java.util.Date;
import java.util.List;

import com.hitrust.acl.model.VwHostErrorLog;

public interface VwHostErrorLogService {

	public List<VwHostErrorLog> getVwHostErrorLogByTrnsDttm(Date fromDttm);

	public int getVwHErrLogCountByTrnsDttm(Date fromDttm);

	public List<VwHostErrorLog> getVHErrLogByTrnsDttm(int beforeMin);

	public List<VwHostErrorLog> getVHErrLogByTrnsDttmAlertType(int beforeMin, String alertType);


}
