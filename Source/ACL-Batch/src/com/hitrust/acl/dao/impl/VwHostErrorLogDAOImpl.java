package com.hitrust.acl.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.hitrust.acl.dao.VwHostErrorLogDAO;
import com.hitrust.acl.model.VwHostErrorLog;
import com.hitrust.acl.model.base.AbstractVwHostErrorLog.Id;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class VwHostErrorLogDAOImpl extends BaseDAOImpl implements VwHostErrorLogDAO{

	
	/**
     * 取得大於某特定時間以後的錯誤交易
     * 
     * @param fromDttm
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<VwHostErrorLog> getVwHostErrorLogByTrnsDttm(Date fromDttm) {
        DetachedCriteria dc = DetachedCriteria.forClass(VwHostErrorLog.class);

        dc.add(Restrictions.ge("trnsDttm", fromDttm));
        dc.addOrder(Order.asc("trnsDttm"));

        return this.query(dc);
    }
    
    
	/**
     * 取得大於某特定時間以後的錯誤交易筆數
     * 
     * @param fromDttm
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public int getVwHErrLogCountByTrnsDttm(Date fromDttm) {
        DetachedCriteria dc = DetachedCriteria.forClass(VwHostErrorLog.class);

        dc.add(Restrictions.ge("trnsDttm", fromDttm));
        dc.setProjection(Projections.rowCount());
 
        List results =getHibernateTemplate().findByCriteria(dc);
        int count =(((Integer) results.get(0)).intValue());
        return count;
         
        
    }
    
	/**
     * 取得大於某特定時間以後的錯誤交易
     * 
     * @param fromDttm
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<VwHostErrorLog> getVHErrLogByTrnsDttm(int beforeMin) {
    	 StringBuilder sb = new StringBuilder();
         
         beforeMin=beforeMin*-1;
         
         String sbeforeMin=Integer.toString(beforeMin);
         
         sb.append(" select EC_ID, EC_MSG_NO, ORDR_NO, TRNS_DTTM, HOST_CODE, CODE_DESC ");
         sb.append(" from VW_HOST_ERR_LOG ");
         sb.append(" where TRNS_DTTM > DATEADD(mi,");
         sb.append(sbeforeMin);
         sb.append(",GETDATE()) ");
         sb.append(" order by TRNS_DTTM ");
         
         List<Map<String,Object>> mlogs = this.queryNativeSql(sb.toString());
         
         return this.mapToObject(mlogs);
    }
    
    
    /**
     * 取得大於某特定時間以後的錯誤交易
     * 
     * @param fromDttm
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<VwHostErrorLog> getVHErrLogByTrnsDttmAlertType(int beforeMin,String alertType) {
    	 StringBuilder sb = new StringBuilder();
         
         beforeMin=beforeMin*-1;
         
         String sbeforeMin=Integer.toString(beforeMin);
         
         sb.append(" select A.EC_ID, A.EC_MSG_NO, A.ORDR_NO, A.TRNS_DTTM, A.HOST_CODE, A.CODE_DESC ");
         sb.append(" from VW_HOST_ERR_LOG A ");
         sb.append(" where A.TRNS_DTTM > DATEADD(mi,");
         sb.append(sbeforeMin);
         sb.append(",GETDATE()) ");
         sb.append(" and (SELECT COUNT(*) FROM ALERT_DATA B WHERE A.EC_ID=B.EC_ID AND A.EC_MSG_NO=B.EC_MSG_NO AND B.ALERT_TYPE='");
         sb.append(alertType);
         sb.append("')=0 ");
         sb.append(" order by A.TRNS_DTTM ");
         
         List<Map<String,Object>> mlogs = this.queryNativeSql(sb.toString());
         
         return this.mapToObject(mlogs);
    }
    
    
    
    
    private List<VwHostErrorLog> mapToObject(List<Map<String,Object>> mlogs){
        List<VwHostErrorLog> Logs=new ArrayList<VwHostErrorLog>();
        Map<String,Object> map=null;
    	
		
		/*
		 * ADTID.setEcMsgNo(VHEL.getId().getEcMsgNo()); ADTID.setAlertType("MAIL");
		 * ADT.setId(ADTID)
		 */;
		
		for (int i = 0; i < mlogs.size(); i++) {
                VwHostErrorLog VWHLog=new VwHostErrorLog();
                Id VWHLogID =new Id();
                map=mlogs.get(i);
                
                //primary key
                if(null!= map.get("EC_ID")){
                	VWHLogID.setEcId(map.get("EC_ID").toString());
                }
                
                if(null!=map.get("EC_MSG_NO")){
                    VWHLogID.setEcMsgNo(map.get("EC_MSG_NO").toString());
                }
                VWHLog.setId(VWHLogID);
                
                //非primarykey
                if(null!=map.get("ORDR_NO")){
                    VWHLog.setOrdrNo(map.get("ORDR_NO").toString());
                }
                
                if(null!=map.get("TRNS_DTTM")){
                    VWHLog.setTrnsDttm((Date)map.get("TRNS_DTTM"));
                }
                
                
                if(null!=map.get("HOST_CODE")){
                    VWHLog.setHostCode(map.get("HOST_CODE").toString());
                }
                
                
                if(null!=map.get("CODE_DESC")){
                    VWHLog.setCodeDesc(map.get("CODE_DESC").toString());
                }
                
                                
                Logs.add(VWHLog);
            }
            return Logs;
    }
}
