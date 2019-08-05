/**
 * @(#)DayAcntContServiceImpl.java
 *
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 
 * 
 * Modify History:
 *  v1.00, 2018/04/22,
 *   1) First release
 *  
 */
package com.hitrust.acl.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hitrust.acl.dao.CustAcntLinkDAO;
import com.hitrust.acl.dao.DayAcntContDAO;
import com.hitrust.acl.dao.EcDataDAO;
import com.hitrust.acl.model.DayAcntCont;
import com.hitrust.acl.model.EcData;
import com.hitrust.acl.model.base.AbstractDayAcntCont;
import com.hitrust.acl.service.DayAcntContService;
import com.hitrust.acl.util.DateUtil;

public class DayAcntContServiceImpl implements DayAcntContService {
	
	private EcDataDAO ecDataDAO;
	
	private DayAcntContDAO dayAcntContDAO;
	
	private CustAcntLinkDAO custAcntLinkDAO;
	
	public void setEcDataDAO(EcDataDAO ecDataDAO) {
		this.ecDataDAO = ecDataDAO;
	}
	
	public void setDayAcntContDAO(DayAcntContDAO dayAcntContDAO) {
		this.dayAcntContDAO = dayAcntContDAO;
	}

	public void setCustAcntLinkDAO(CustAcntLinkDAO custAcntLinkDAO) {
		this.custAcntLinkDAO = custAcntLinkDAO;
	}

	@Override
	public void genRpt03(String setlDate) throws Exception{
		String sysTime = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
		String[] sttsArray = {"00","01","02"};
		List<EcData> ecDataList = ecDataDAO.getEcDataList();
		Map<String, Object> map2Insert = new HashMap<String,Object>();
		map2Insert.put("setlDate", setlDate);
		dayAcntContDAO.deleteBySetlDate(setlDate);
		map2Insert.put("mdfyDttm", sysTime);
		List<DayAcntCont> listToInsert = new ArrayList<>();
		for(EcData ecData:ecDataList){
			List<Map<String,Object>> summaryList = custAcntLinkDAO.countCustAcntLink(ecData.getEcId());
			for(String stts:sttsArray){
				int cont = 0;
				for(Map<String, Object> resultMap:summaryList){
					if(StringUtils.equals((String)resultMap.get("STTS"), stts)){
						cont = (Integer)resultMap.get("cnt");
								break;
					}
				}
				listToInsert.add(genDayAcntCont(setlDate, ecData.getEcId(), stts, cont));
			}//end loop
		}
		if (!listToInsert.isEmpty()) {
			dayAcntContDAO.saveOrUpdateAll(listToInsert);
		}
	}
	
	private DayAcntCont genDayAcntCont(String setlDate, String ecId, String stts, Integer count){
		DayAcntCont dayAcntCont = new DayAcntCont();
		AbstractDayAcntCont.Id id = new AbstractDayAcntCont.Id();
		id.setSetlDate(setlDate);
		id.setEcId(ecId);
		id.setStts(stts);
		dayAcntCont.setId(id);
		dayAcntCont.setTotlCont(count);
		dayAcntCont.setMdfyDttm(new Date());
		return dayAcntCont;
	}

}
