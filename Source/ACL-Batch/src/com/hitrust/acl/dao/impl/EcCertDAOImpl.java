package com.hitrust.acl.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hitrust.acl.dao.EcCertDAO;
import com.hitrust.framework.dao.impl.BaseDAOImpl;

public class EcCertDAOImpl extends BaseDAOImpl implements EcCertDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getCert4Warning(String warnDay) {
		StringBuffer sql =new StringBuffer();
		sql.append("SELECT EC_CERT.*, DATEDIFF(day, GETDATE(), CONVERT(DATETIME,SUBSTRING(END_DTTM,1,8),114)) AS warnDay, ");
		sql.append("EC_DATA.MAIL AS EMAIL, EC_DATA.EC_NAME_CH ");
		sql.append("FROM EC_CERT, EC_DATA ");
		sql.append("WHERE EC_CERT.EC_ID = EC_DATA.EC_ID ");
		int count=0;
		String warnDays[] = StringUtils.split(warnDay,",");
		for(String day:warnDays){
			sql.append(count>0?" OR ":" AND ( ");
			sql.append("DATEDIFF(day, GETDATE(), CONVERT(DATETIME,SUBSTRING(END_DTTM,1,8),114)) = "+day);
			count++;
		}
		if(count>0){
			sql.append(")");
		}
		 return this.queryNativeSql(sql.toString());
	}

}
