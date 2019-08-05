/**
 * @(#)CustPltfDAOImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : CustPltfDAOImpl
 * 
 * Modify History:
 *  v1.00, 2016/02/17, Evan
 *   1) First release
 *  
 */
package com.hitrust.bank.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.hitrust.bank.dao.CustPltfDAO;
import com.hitrust.bank.model.CustPltf;
import com.hitrust.framework.criterion.HqlDetachedCriteria;
import com.hitrust.framework.criterion.HqlRestrictions;
import com.hitrust.framework.criterion.ReturnEntry;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class CustPltfDAOImpl extends BaseDAOImpl implements CustPltfDAO {

	/**
	 * 取會員已綁定的各平台資料
	 * @param custId 身分證號
	 * @return	List<CustPltf> 會員平台資料檔 
	 */
	@Override
	public List<CustPltf> getEcDataByCust(String custId) {
		
		HqlDetachedCriteria hqlDc = HqlDetachedCriteria.forEntityNames(new String[]{"CustPltf a","EcData b"});
		hqlDc.add(HqlRestrictions.eq("a.id.ecId","{b.ecId}"));
		hqlDc.setReturnEntry(ReturnEntry.forName("new CustPltf(a, b.ecNameCh, b.ecNameEn)"));
		hqlDc.add(HqlRestrictions.eq("a.id.custId", custId));
		
		return query(hqlDc);
	}
	
	/**
	 * 取得綁定會員平台資料 
	 * @param ecId	平台代碼
	 * @return	List<CustPltf> 會員平台資料檔 
	 */
	@Override
	public List<CustPltf> getCustPltfByEcId(String ecId) {
		
		DetachedCriteria dCriteria =  DetachedCriteria.forClass(CustPltf.class);
		dCriteria.add(Restrictions.eq("id.ecId", ecId));
		
		return query(dCriteria);
	}

}
