/**
 * 
 */
package com.hitrust.acl.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hitrust.acl.dao.SysParmDAO;
import com.hitrust.acl.model.SysParm;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

/**
 * @author jim
 *
 */
public class SysParmDAOImpl extends BaseDAOImpl implements SysParmDAO {

	/* (non-Javadoc)
	 * @see com.hitrust.acl.dao.SysParmDAO#fetchSysParmByParm(java.lang.String)
	 */
	@Override
	public SysParm fetchSysParmByParm(String parm) {
		List<SysParm> lists = new ArrayList<SysParm>();
		SysParm sysParm = null;
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SysParm.class);
		criteria.add(Restrictions.eq("parmCode", parm));
		lists = this.query(criteria);
		
		if (lists.size() > 0) {
			sysParm = lists.get(0);
		}
		
		return sysParm;
	}
	
	/**
	 * 依據指定參數開頭名稱, 取得系統參數值List
	 * @param parm	參數名稱開始字串
	 * @return SysParm or null
	 */
	@SuppressWarnings("unchecked")
    @Override
	public List<SysParm> getSysParmListLike(String beginStr) {
		
		DetachedCriteria  detachedCriteria = DetachedCriteria.forClass(SysParm.class);
		detachedCriteria.add(Restrictions.like("parmCode", beginStr, MatchMode.START));
		detachedCriteria.addOrder(Order.asc("parmCode"));
		
		return query(detachedCriteria);
	}
  
	/**
     * 設定系統參數
     * 
     * @param parmCode
     * @param parmValue
     */
    @Override
    public void updateSysParmByParmCode(String parmCode, String parmValue) {
        
        String sql = " Update SYS_PARM set PARM_VALUE = ? where PARM_CODE = ? ";
        
        this.excuteNativeUpdateSql(sql, new String[]{parmValue, parmCode});
    }
}
