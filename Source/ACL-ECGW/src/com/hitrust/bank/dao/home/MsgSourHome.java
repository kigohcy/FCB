/**
 * @(#) MsgSourHome.java
 *
 * Directions: 簡訊資料檔 DAO
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/04/13, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.bank.dao.home;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.hitrust.acl.APSystem;
import com.hitrust.acl.dao.BeanHome;
import com.hitrust.acl.db.DBExec;
import com.hitrust.acl.exception.DBException;
import com.hitrust.acl.exception.UtilException;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.acl.util.MAC;
import com.hitrust.bank.json.ACLink;

import ni.otp.fb.Proc00F2;
import ni.otp.fb.Proc00F3;
import ni.otp.fb.Proc00F7;
import ni.otp.fb.Proc00FA;
import ni.otp.fb.Proc00FB;

public class MsgSourHome extends BeanHome {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(MsgSourHome.class);


	public static String SMSIP = APSystem.getProjectParam("SMS_IP");
	public static String SMSPOPRT = APSystem.getProjectParam("SMS_POPRT");
	public static String SMSUSER = APSystem.getProjectParam("SMS_USER");
	public static String SMSMEMA = APSystem.getProjectParam("SMS_MEMA");
	public static String SMSURL = APSystem.getProjectParam("SMS_URL");
	
	public MsgSourHome(Connection conn) {
		this.conn = conn;
	}

//	/**
//	 * 新增簡訊
//	 * @param tel
//	 * @param msgData
//	 * @throws DBException
//	 * @throws UtilException
//	 */
//	public void insertMsgSour(String tel, String msgData) throws DBException, UtilException {
//		DBExec exec = null;
//		
//		String groupId = APSystem.getProjectParam("SMS_GROUP_ID");			// 簡訊中心群組代號
//    	String userName = APSystem.getProjectParam("SMS_USER_NAME");		// 使用者帳號
//    	String destCategory = APSystem.getProjectParam("SMS_DESTCATEGORY"); // 分行代碼或部門代碼
//		
//		StringBuffer sb = new StringBuffer();
//		sb.append("INSERT INTO MsgSour (GroupID, UserName, UserPassword, ExpireTime, MsgType, DestCategory, DestNo, MsgData, Filler) ");
//		sb.append("VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
//		
//		try {
//			exec = new DBExec(this.conn);
//			exec.prepareStatement(sb.toString());
//			exec.setString(1, groupId);
//			exec.setString(2, userName);
//			exec.setString(3, "");
//			exec.setString(4, DateUtil.countSecond(DateUtil.getCurrentTime("DT", "AD"), 86400));
//			exec.setString(5, "1");
//			exec.setString(6, destCategory);
//			exec.setString(7, tel.trim());
//			exec.setString(8, msgData);
//			exec.setString(9, "5");
//			
//			exec.executeUpdate();
//			
//		} catch (SQLException e) {
//			LOG.error("===== 簡訊新增失敗 ===== ");
//			throw new DBException(e, "DB_INS");
//			
//		} finally {
//			exec.close();
//		}
//	}
	
	
	/**
	 * 使用者帳號解鎖
	 * @param aclink
	 * @throws Exception
	 */
	public String queryOptUserF2(ACLink aclink) throws Exception{
		String retCode = "";
		String custId= aclink.getCustId();
		retCode = new Proc00F2().response(custId, SMSIP,SMSPOPRT);  //帳號查詢確認
		return retCode;
	}
	
	
	/**
	 * 查詢簡訊用戶
	 * @param aclink
	 * @throws Exception
	 */
	public String unlockOptUserF7(ACLink aclink) throws Exception{
		String retCode = "";
		String custId= aclink.getCustId();
		retCode = new Proc00F7().response(custId, "091", "user", SMSIP,SMSPOPRT); //解鎖
		return retCode;
	}
	
	
	/**
	 * 新增簡訊用戶
	 * @param tel
	 * @param msgData
	 * @throws DBException
	 * @throws UtilException
	 */
	public String sendOTPMsgFA(ACLink aclink) throws Exception{
		String retCode = "";
		String custId= aclink.getCustId();
		retCode = new Proc00FA().response(custId ,"091", "user", SMSIP,SMSPOPRT);
		return retCode;
	}
	
	/**
	 * 刪除簡訊用戶
	 * @param tel
	 * @param msgData
	 * @throws DBException
	 * @throws UtilException
	 */
	public String sendOTPMsgF3(ACLink aclink) throws Exception{
		String retCode = "";
		String custId= aclink.getCustId();
		retCode = new Proc00F3().response(custId ,"091", "user", SMSIP,SMSPOPRT);
		return retCode;
	}
	
	
	/**
	 * 傳送簡訊OTP
	 * @param tel
	 * @param msgData
	 * @throws DBException
	 * @throws UtilException
	 */
	public String sendOTPMsgFB(ACLink aclink) throws Exception{
		String retCode = "";
		String custId= aclink.getCustId();
		String phomeNo= aclink.getTlxNo();
//		LOG.info("custId = " + custId);
//		LOG.info("phomeNo = " + phomeNo);
//		LOG.info("SMSUSER = " + SMSUSER);
//		LOG.info("SMSMEMA = " + SMSMEMA);
		String[] PreText= new String[]{"user","pass","mobile","contents","purpose","category","Host-code","brt-no","user-id"};
		String[] Msg= new String[]{SMSUSER,SMSMEMA, phomeNo ,"第一銀行約定連結存款帳戶系統，本次發送之簡訊密碼為:『NIOTP』，請您於時間倒數結束前完成密碼輸入。","帳戶連結-簡訊OTP","帳戶連結","ALP","079", custId};
		retCode = new Proc00FB().response(custId, SMSURL ,"120", PreText, Msg, SMSIP, SMSPOPRT); 
		return retCode;
	}
}
