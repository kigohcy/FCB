/**
 * @(#) DayCustContServiceImpl.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2018/03/28
 * 
 */
package com.hitrust.acl.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hitrust.acl.common.DateUtil;
import com.hitrust.acl.dao.DayCustContDAO;
import com.hitrust.acl.model.DayCustCont;
import com.hitrust.acl.model.base.AbstractDayCustCont;
import com.hitrust.acl.service.DayCustContService;

public class DayCustContServiceImpl implements DayCustContService {
	
	private DayCustContDAO dayCustContDAO;
	

	public void setDayCustContDAO(DayCustContDAO dayCustContDAO) {
		this.dayCustContDAO = dayCustContDAO;
	}


	@Override
	public List<Map<String, Object>> countCustDataByStts() {
		return dayCustContDAO.countCustDataByStts();
	}


	@Override
	public void genRpt03(String setlDate) throws Exception{
		String sysTime = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
		String[] sttsArray = {"00","01","02"};
		List<Map<String, Object>> resultList = dayCustContDAO.countCustDataByStts();
		dayCustContDAO.deleteBySetlDate(setlDate);
		//insert 會員日終統計檔(DAY_CUST_CONT)
		List<DayCustCont> listToInsert = new ArrayList<DayCustCont >();
		for(String stts:sttsArray){
			int cont = 0;
			for(Map<String, Object> resultMap:resultList){
				if(StringUtils.equals((String)resultMap.get("STTS"), stts)){
					cont = (Integer)resultMap.get("STTS_COUNT");
					break;
				}
			}
			listToInsert.add(genDayCustCont(setlDate, stts, cont));
		}//end loop
		if (!listToInsert.isEmpty()) {
			 dayCustContDAO.saveOrUpdateAll(listToInsert);
        }
	}
	
	private DayCustCont genDayCustCont(String setlDate, String stts, Integer count){
		DayCustCont dayCustCont = new DayCustCont();
		AbstractDayCustCont.Id id = new AbstractDayCustCont.Id();
		id.setSetlDate(setlDate);
		id.setStts(stts);
		dayCustCont.setId(id);
		dayCustCont.setTotlCont(count);
		dayCustCont.setMdfyDttm(new Date());
		return dayCustCont;
	}

}
