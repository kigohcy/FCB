package com.hitrust.acl.service;

import com.hitrust.acl.model.AlertData;

public interface AlertDataService {

	/**
	 * 新增ALERT傳送資料
	 * @param AlertData 電商平台憑證檔
	 */
	public void insertAlertData(AlertData alertData);
	
	/**
     * 以setlDate為條件刪除過期通知
     * 
     * @param setlDate
     */
    public void deleteBySetlDate(String setlDate);

}
