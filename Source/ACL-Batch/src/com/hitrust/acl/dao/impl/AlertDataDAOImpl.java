package com.hitrust.acl.dao.impl;

import com.hitrust.acl.dao.AlertDataDAO;
import com.hitrust.acl.model.AlertData;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class AlertDataDAOImpl extends BaseDAOImpl implements AlertDataDAO {

	/**
	 * 新增ALERT傳送資料
	 * @param AlertData 電商平台憑證檔
	 */
	@Override 
	public void insertAlertData(AlertData alertData) {
       String sql = " INSERT INTO ALERT_DATA(EC_ID,EC_MSG_NO,ALERT_TYPE,TRNS_DTTM,ORDR_NO) VALUES (?,?,?,GETDATE(),?) ";
        
        this.excuteNativeUpdateSql(sql, new String[]{alertData.getId().getEcId(),alertData.getId().getEcMsgNo(),alertData.getId().getAlertType(),alertData.getOrdrNo() });
	}
	
	/**
     * 以setlDate為條件刪除過期通知
     * 
     * @param setlDate
     */
    @Override
    public void deleteBySetlDate(String setlDate) {
    	//改成固定
        String sql = " DELETE from ALERT_DATA where TRNS_DTTM < DATEADD(dd, -2 ,GETDATE())";
        this.excuteNativeSql(sql);
    }
    

}
