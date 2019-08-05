/**
 * @(#) CommonUtil.java
 *
 * Directions: 
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/02/18, Eason Hsu
 *    1) JIRA-Number, First release
 *   
 */
package com.hitrust.bank.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.web.context.WebApplicationContext;

import com.hitrust.acl.util.MAC;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.Rfci301SvipDAO;
import com.hitrust.bank.dao.TxncsSvipDAO;
import com.hitrust.bank.model.Rfci301Svip;
import com.hitrust.bank.service.SvipTrnLogSrv;
import com.hitrust.framework.APSystem;

public class CommonUtil {

	/**
	 * 產生指定長度亂數
	 * @param len	字串長度
	 * @return 回傳 指定長度亂數, 內容含(0 ~ 9 & A ~ Z) 
	 */
	public static String randomGenerator (int len) {
		
		String jimYao = "0123456789abcdefghijklmnopqrstuvwxyz";
		String rmVal = "";
		
		for (int i = 0; i < len; i++) {
			
			int rmKey = (int) (Math.random() * jimYao.length());
			
			rmVal = rmVal.concat(String.valueOf(jimYao.charAt(rmKey)));
		}
		
		return rmVal.toUpperCase();
	}
	
	public static String generatePassWord(){
		java.util.Random r = new java.util.Random();
		int strLen = 8;			// default length:6
		int num = 0;			// 隨機字符碼
		String outStr = "";		// 產生的密碼
		while(outStr.length() < strLen)  {
			num = (int)(Math.random()*(90-50+1))+50;	//亂數取編號為 50~90 的字符	(排除 0 和 1)
			if (num > 57 && num < 65)
				continue;			//排除非數字非字母
			else if (num == 73 || num == 76 || num == 79)
				continue;			//排除 I、L、O
			outStr += (char)num;
		}
	    return outStr;
	}
	
	/**
	 * 實體帳號格式化
	 * @param realAcnt
	 * @return
	 */
	public static String relAcntFormat(String realAcnt){
		
		String rtnVal = "";
		int length = realAcnt.length();
		
		if(length == 16 && realAcnt.startsWith("00")){// IF帳號為16碼and前2碼為00，則 帳號=取後14碼
			rtnVal = realAcnt.substring(2);
		} else if (length == 15 && realAcnt.startsWith("0")){//1.2	IF帳號為15碼and前1碼為0，則 帳號=取後14碼
			rtnVal = realAcnt.substring(1);
		} else {
			rtnVal = realAcnt;
		}
		
		if(rtnVal.length() == 14){// 帳號為14碼，則進行以下格式化，不足14碼直接回傳
			if(rtnVal.startsWith("0")){//第一碼不為0, 採用4-2-7-1格式顯示, e.g. 2045-00-1234567-8
				rtnVal = rtnVal.substring(0,3) + "-" + rtnVal.substring(3,5) + "-"
					   + rtnVal.substring(5,13) + "-" + rtnVal.substring(13,14);
			} else {// 第一碼為0, 採用 3-2-8-1格式顯示, e.g. 045-10-12345678-9
				rtnVal = rtnVal.substring(0,4) + "-" + rtnVal.substring(4,6) + "-"
					   + rtnVal.substring(6,13) + "-" + rtnVal.substring(13,14);
			}
		}
		
		return rtnVal;
	}
	
	/**
	 * 實體帳號遮蔽, 留前4碼後6碼,其餘顯示*
	 * @param realAcnt
	 * @return
	 */
	public static String relAcntMask(String realAcnt){
		
		int length = realAcnt.length();
		
		if(length == 16 &&  realAcnt.startsWith("00")){//IF帳號為16碼and前2碼為00，則 帳號=取後14碼
			realAcnt = realAcnt.substring(2);
		}else if (length == 15 &&  realAcnt.startsWith("0")){//1.2	IF帳號為15碼and前1碼為0，則 帳號=取後14碼
			realAcnt = realAcnt.substring(1);
		}
		
		if(length >= 8){
			realAcnt = StringUtil.toMask(realAcnt, "L", 5, 8, "*"); //帳號:遮蔽第5~8碼 
		}
		return realAcnt;
	}
	
	/**
	 * 檢核 查訊資料 是否含敏感客戶資料
	 * @param custIds	 身分證字號
	 * @param condition1 查詢條件-身分證字號
	 * @param condition2 查詢條件-實體帳號
	 * @param funcId	 功能代碼
	 * @param reportId	 報表代碼
	 */
	public static void checkTxncsSvip(Map<String, String> custIds, String condition1, String condition2, String funcId, String reportId) {
		
		String temp1 = "";
		String temp2 = "";
		
		TxncsSvipDAO txncsSvipDAO = (TxncsSvipDAO) APSystem.getApplicationContext().getBean("txncsSvipDAO");
		SvipTrnLogSrv svipTrnLogSrv = (SvipTrnLogSrv) APSystem.getApplicationContext().getBean("svipTrnLogSrv");
		
		List<String> list = new ArrayList<String>();
		
		// 身分證字號不足11碼時, 需補足11碼並加密
		for (Map.Entry<String, String> entry : custIds.entrySet()) {
			
			if (entry.getValue().length() < 11) {
				list.add(MAC.encrypteData(entry.getValue() + "0"));
			} else {
				list.add(MAC.encrypteData(entry.getValue()));
			}
		}
		
		temp1 = StringUtil.isBlank(condition1) ? "" : condition1;
		temp2 = StringUtil.isBlank(condition2) ? "" : condition2;
		
		if (!StringUtil.isBlank(temp2)) {
			temp2 = (temp2.length() == 14) ? "00" + temp2 : temp2;
		}
		
		if (!list.isEmpty()) {
			// 依據加密後的 身分證字號 取得敏感戶清單
			List<String> svips = txncsSvipDAO.fetchTxncsSvipList(list);
			
			// 依據 敏感戶清單 新增 敏感客戶查詢LOG檔
			for (String svip : svips) {
				String custId = MAC.decrypteData(svip);
				custId = custId.substring(0, custId.length() -1);
				svipTrnLogSrv.insertSvipTrnLog(custId, temp1, temp2, funcId, reportId);
			}
		}
		
	}
	
	/**
	 * 取得限閱戶客戶ID
	 * @return
	 */
	public static List<String> getRfci301SvipIds() {
		
		Rfci301SvipDAO rfci301SvipDAO = (Rfci301SvipDAO) APSystem.getApplicationContext().getBean("rfci301SvipDAO");
		List<Rfci301Svip> list = rfci301SvipDAO.fetchRfci301SvipList();
		List<String> custIds = new ArrayList();
		
		for(int i=0; list!=null && i<list.size(); i++){
			Rfci301Svip svip = (Rfci301Svip)list.get(i);
			String custId = svip.getId().getId1No();
			custId = MAC.decrypteData(custId);
			custId = custId.substring(0, custId.length() -1);
			custIds.add(custId);
		}
		return custIds;
	}
	
	/**
	 * 功能：取得系統預設的資料庫Schema名稱，供手動寫SQL時，指定Table的Schema
	 * 
	 * @return 系統預設的資料庫Schema名稱
	 */
	public static synchronized String getDefaultSchema() {
		WebApplicationContext wc = APSystem.getApplicationContext();
		LocalSessionFactoryBean lsfb = (LocalSessionFactoryBean) wc
				.getBean("&sessionFactory");
		Properties hibernateProperties = lsfb.getHibernateProperties();
		String defaultSchema = hibernateProperties
				.getProperty("hibernate.default_schema");
		if (defaultSchema == null) {
			defaultSchema = "";
		}
		return defaultSchema;
	}
	
	/**
	 * 功能：取得二日期間隔天數
	 *
	 * @param beginDate yyyyMMdd
	 * @param endDate yyyyMMdd
	 * @return
	 * @throws Exception
	 */
	public static int getDay(String beginDate, String endDate) throws Exception {
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMdd");
		Date d1 = sim.parse(beginDate);
		Date d2 = sim.parse(endDate);
		return (int) ((d2.getTime() - d1.getTime()) / (3600L * 1000 * 24));
	}
}
