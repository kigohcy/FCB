package com.hitrust.acl.service.impl;

import org.apache.log4j.Logger;

import com.hitrust.acl.dao.AlertDataDAO;
import com.hitrust.acl.model.AlertData;
import com.hitrust.acl.service.AlertDataService;

public class AlertDataServiceImpl implements AlertDataService {
	static Logger LOG = Logger.getLogger(AlertDataService.class);
	
	 private AlertDataDAO alertDataDAO;
	 
	 public AlertDataDAO getAlertDataDAO() {
			return alertDataDAO;
		}

		public void setAlertDataDAO(AlertDataDAO alertDataDAO) {
			this.alertDataDAO = alertDataDAO;
		}

	 /**
		 * 新增ALERT傳送資料
		 * @param AlertData 電商平台憑證檔
		 */
		@Override 
		public void insertAlertData(AlertData alertData) {
			LOG.info("SERVICE INSERT:"+alertData.getId().getEcId()+","+alertData.getId().getEcMsgNo()+","+alertData.getId().getAlertType()+","+alertData.getOrdrNo()+","+alertData.getTrnsDttm()+","+alertData.getFuncId());
			alertDataDAO.save(alertData);
		}
    
		/**
	     * 以setlDate為條件刪除過期通知
	     * 
	     * @param setlDate
	     */
	    @Override
	    public void deleteBySetlDate(String setlDate) {
	    	alertDataDAO.deleteBySetlDate(setlDate);
	    }
	
}
