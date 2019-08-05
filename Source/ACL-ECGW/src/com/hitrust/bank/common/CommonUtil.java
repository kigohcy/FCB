/**
 * @(#) CommonUtil.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 共用
 * 
 * Modify History:
 *  v1.00, 2016/03/25, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.common;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.hitrust.acl.APSystem;
import com.hitrust.acl.common.TransactionControl;
import com.hitrust.acl.util.AES;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.acl.util.HexBin;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.beans.SeqStore;
import com.hitrust.bank.dao.home.SeqStoreHome;

public class CommonUtil {
	//log4j
	static Logger LOG = Logger.getLogger(CommonUtil.class);
	
	/**
	 * 狀態檢核
	 * @param type 01:帳號綁定/02:帳號取消/03:帳號綁定查詢/04:交易扣款/05:交易退款/06:交易結果查詢/07:提領
	 * @param ecStts 電商平台狀態
	 * @param custStts 會員服務狀態
	 * @param pltfStts 會員平台服務狀態
	 * @param linkStts 連結帳號服務狀態
	 * @return 錯誤訊息
	 */
	public synchronized static String checkStatus(String type, String ecStts, String custStts, String plftStts, String linkStts){
		
		String RTN_5012 = "5012"; //平台服務狀態異常
		String RTN_5001 = "5001"; //會員服務狀態異常
		String RTN_5022 = "5022"; //連結帳號狀態異常
		String RTN_5030 = "5030"; //會員平台服務狀態異常
		
		if ("01".equals(type)) {
			// 帳號綁定
			if (StringUtil.isBlank(ecStts) || !"00".equals(ecStts)) {
				return RTN_5012;
			} else if (!StringUtil.isBlank(custStts) && "01".equals(custStts)) {
				return RTN_5001;
			} else if (!StringUtil.isBlank(plftStts) && "01".equals(plftStts)) {
				return RTN_5030;
			} else if (!StringUtil.isBlank(linkStts) && "01".equals(linkStts)) {
				return RTN_5022;
			}
			
		} else if ("02".equals(type)) {
			// 帳號取消
			if (StringUtil.isBlank(ecStts) || "02".equals(ecStts)) {
				return RTN_5012;
			} else if (StringUtil.isBlank(custStts) || "02".equals(custStts)) {
				return RTN_5001;
			} else if (StringUtil.isBlank(plftStts) || "02".equals(plftStts)) {
				return RTN_5030;
			} else if (StringUtil.isBlank(linkStts) || "02".equals(linkStts)) {
				return RTN_5022;
			}
			
		} else if ("03".equals(type)) {
			// 帳號綁定查詢
			if (StringUtil.isBlank(ecStts) || "02".equals(ecStts)) {
				return RTN_5012;
			} else if (StringUtil.isBlank(custStts) || "02".equals(custStts)) {
				return RTN_5001;
			}
			
		} else if ("04".equals(type)) {
			// 交易扣款
			if (StringUtil.isBlank(ecStts) || !"00".equals(ecStts)) {
				return RTN_5012;
			} else if (StringUtil.isBlank(custStts) || !"00".equals(custStts)) {
				return RTN_5001;
			} else if (StringUtil.isBlank(plftStts) || !"00".equals(plftStts)) {
				return RTN_5030;
			} else if (StringUtil.isBlank(linkStts) || !"00".equals(linkStts)) {
				return RTN_5022;
			}
			
		} else if ("05".equals(type)) {
			// 交易退款
			if (StringUtil.isBlank(ecStts) || "02".equals(ecStts)) {
				return RTN_5012;
			} else if (StringUtil.isBlank(custStts) || !"00".equals(custStts)) {
				return RTN_5001;
			} else if (StringUtil.isBlank(plftStts) || !"00".equals(plftStts)) {
				return RTN_5030;
			} else if (StringUtil.isBlank(linkStts) || !"00".equals(linkStts)) {
				return RTN_5022;
			}
			
		} else if ("06".equals(type)) {
			// 交易結果查詢
			if (StringUtil.isBlank(ecStts) || "02".equals(ecStts)) {
				return RTN_5012;
			} else if (StringUtil.isBlank(custStts) || "02".equals(custStts)) {
				return RTN_5001;
			} else if (StringUtil.isBlank(plftStts) || "02".equals(plftStts)) {
				return RTN_5030;
			} else if (StringUtil.isBlank(linkStts) || "02".equals(linkStts)) {
				return RTN_5022;
			}
		} else if ("07".equals(type)) {
			// 交易提領
			if (StringUtil.isBlank(ecStts) || "02".equals(ecStts)) {
				return RTN_5012;
			} else if (StringUtil.isBlank(custStts) || !"00".equals(custStts)) {
				return RTN_5001;
			} else if (StringUtil.isBlank(plftStts) || !"00".equals(plftStts)) {
				return RTN_5030;
			} else if (StringUtil.isBlank(linkStts) || !"00".equals(linkStts)) {
				return RTN_5022;
			}
			
		}
		
		return "";
	}
	
	/**
	 * 產生電文序號 (共12碼,當日不可重覆)
	 * @return '9'+DDHHMM+5碼流水號 
	 */
	public synchronized static String genTelegramNo() {
		String newSeq = "";
		Connection conn = null;
		String type = "TELE";
		try{
			String todayTime = DateUtil.getCurrentTime("DT", "AD");
			String today = todayTime.substring(0, 8);
			conn = APSystem.getConnection(APSystem.DB_ACLINK);
			//Begin Transaction.
        	TransactionControl.transactionBegin(conn);
			
			SeqStoreHome seqStoreHome = new SeqStoreHome(conn);
			SeqStore seqStore = seqStoreHome.fetchSeqStoreByKey(type);
			if(seqStore==null){
				newSeq = "00001";
				seqStore = new SeqStore();
				seqStore.setConnection(conn);
				seqStore.TYPE = type;
				seqStore.SEQ = newSeq;
				seqStore.REFLESHDATE = today;
				//insert SEQ_STORE
				seqStore.insert();
			}else{
				String oldSeq = seqStore.SEQ;
				String refleshDate = "";
				if(!today.equals(seqStore.REFLESHDATE)){
					newSeq = "00001";
					refleshDate = today;
				}else{
					newSeq = "0000" + String.valueOf(Integer.parseInt(oldSeq)+1);
					newSeq = newSeq.substring(newSeq.length()-5);
				}
				//udpate SEQ_STORE
				boolean rslt = seqStoreHome.updateSeqStore(type, newSeq, oldSeq, refleshDate);
				if(!rslt){
					return "";
				}
			}
			
			//commit
        	TransactionControl.trasactionCommit(conn);
        	//'9'+yymmDDHHMM+5碼流水號
        	newSeq = "9"+todayTime.substring(4,10) + newSeq;
        	
		}catch(Exception e){
			LOG.error("Exception:"+e.toString(), e);
			newSeq = "";
		}finally{
			if (conn != null) {
            	TransactionControl.transactionEnd(conn);
                APSystem.returnConnection(conn, APSystem.DB_ACLINK);
            }
		}
		
		return newSeq;
	}
	
	/**
	 * 產生帳號識別碼 (共14碼,每月重編)
	 * @return 銀行代碼+YYMM+7碼流水號
	 */
	public synchronized static String genAcntIdnt() {
		String BANK_ID = APSystem.getProjectParam("BANK_ID");
		
		String newSeq = "";
		Connection conn = null;
		String today = DateUtil.getToday();
		String type = "ACNT_INDT";
		try{
			conn = APSystem.getConnection(APSystem.DB_ACLINK);
			//Begin Transaction.
        	TransactionControl.transactionBegin(conn);
			
			SeqStoreHome seqStoreHome = new SeqStoreHome(conn);
			SeqStore seqStore = seqStoreHome.fetchSeqStoreByKey(type);
			if(seqStore==null){
				seqStore = new SeqStore();
				seqStore.setConnection(conn);
				newSeq = "0000001";
				seqStore.TYPE = type;
				seqStore.SEQ = newSeq;
				seqStore.REFLESHDATE = today;
				//insert SEQ_STORE
				seqStore.insert();
			}else{
				String oldSeq = seqStore.SEQ;
				String refleshDate = "";
				if(!today.substring(0,6).equals(seqStore.REFLESHDATE.substring(0,6))){
					newSeq = "0000001";
					refleshDate = today;
				}else{
					newSeq = "000000" + String.valueOf(Integer.parseInt(oldSeq)+1);
					newSeq = newSeq.substring(newSeq.length()-7);
					if(Integer.parseInt(newSeq)==0){
						newSeq = "0000001";
					}
				}
				//udpate SEQ_STORE
				boolean rslt = seqStoreHome.updateSeqStore(type, newSeq, oldSeq, refleshDate);
				if(!rslt){
					return "";
				}
			}
			
			//commit
        	TransactionControl.trasactionCommit(conn);
        	//
        	newSeq = BANK_ID + today.substring(2,6) + newSeq;
        	
		}catch(Exception e){
			LOG.error("Exception:"+e.toString(), e);
			newSeq = "";
		}finally{
			if (conn != null) {
            	TransactionControl.transactionEnd(conn);
                APSystem.returnConnection(conn, APSystem.DB_ACLINK);
            }
		}
		
		return newSeq;
	}
	
	/**
	 * 實體帳號遮蔽, 留前4碼後6碼,其餘顯示*
	 * @param realAcnt
	 * @return
	 */
	public static String relAcntMask(String realAcnt){
		if(StringUtil.isBlank(realAcnt)) return "";
		
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
	 * 傳入datetime格式字串 eg:2016-03-10 14:00:00.0000000 轉換為 2016/03/10 14:00:00
	 * @param dateStr: datetime格式字串
	 * @return String YYYY/MM/DD hh:mm:ss
	 */
	public static synchronized String formatDatetimeToDateStr(String dateStr) {
		String str = "";
		try{
			Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateStr);
			Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        str = DateUtil.formateDateTimeForUser(DateUtil.getDateTime(calendar, "DT", "AD"));
		}catch(Exception e){
			LOG.error("formatDatetimeToDateStr error: " + e.toString(), e);
		}
		
        return str;
	}
	
	/**
	 * 半型轉全型
	 * @param str
	 * @return String
	 */
	public static String half2Full(String str) {
		if(StringUtil.isBlank(str)) return "";
		
		char c[] = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		String returnString = new String(c);
		
		return returnString;
	}
	
	/**
	 * 交易限額格式化 
	 * @param limt
	 * @return 
	 */
	public static String limtFormat(Long limt) {
		String rtnVal = "";
		
		String pattern = "###,###,###,###,###,###";
		DecimalFormat format = new DecimalFormat(pattern);
		
		rtnVal = format.format(limt);
		
		return rtnVal;
	}
	
	/**
	 * 字串遮蔽
	 * @param idStr	    遮蔽字串	
	 * @param start		遮
	 * @param maskCount
	 * @param symbol
	 * @return
	 */
	public static String stringMask(String idStr, int start, int maskCount, String symbol) {
		if(StringUtil.isBlank(idStr)) return "";
		
		StringBuffer sb = new StringBuffer();
		int strLen = idStr.length();
		
		for (int i = 0; i < strLen; i++) {
			if (i == start) {
				for (int j = 0; j < maskCount; j++) {
					i++;
					sb.append(symbol);
				}
			}
			sb.append(String.valueOf(idStr.charAt(i)));
		}
		
		return sb.toString();
	}
	
	/**
	 * 實體帳號格式化
	 * @param realAcnt
	 * @return
	 */
	public static String relAcntFormat(String realAcnt){
		if(StringUtil.isBlank(realAcnt)) return "";
		
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
     * AES 256 加密
     * @param input
     * @param key
     * @param iv
     * @return String
     * @throws Exception
     */
    public static String aesEncrypt(String input, String key, String iv) throws Exception{
		String cipherStr = null;
		byte[] cipherText = AES.encrypt(input.getBytes(), key.getBytes(), iv.getBytes(), "CBC", true);
		cipherStr = new String(HexBin.bin2Hex(cipherText));
		return cipherStr;
	}
    
    /**
     * AES 256 解密
     * @param input
     * @param key
     * @param iv
     * @return String
     * @throws Exception
     */
	public static String aesDecrypt(String input, String key, String iv) throws Exception{
		
		byte[] cipherText = HexBin.hex2Bin(input.getBytes());
		byte[] plainText = AES.decrypt(cipherText, key.getBytes(), iv.getBytes(), "CBC", true);
		return new String(plainText, "utf-8");
	}
	
	/**
	 * 姓名遮蔽
	 * @param nameStr
	 * @return
	 */
	public static String nameMask(String nameStr) {
		if(StringUtil.isBlank(nameStr)) return "";
		if(nameStr.length()==1) return nameStr;
		if(nameStr.length()==2) return nameStr.charAt(0)+"Ｏ";
		
		StringBuffer sb = new StringBuffer();
		int strLen = nameStr.length();
		
		for (int i=0; i<strLen; i++) {
			if(i==0){
				sb.append(nameStr.charAt(i));
			}
			if(i>0 && i<strLen-1){
				sb.append("Ｏ");
			}
			if(i == strLen-1){
				sb.append(nameStr.charAt(i));
			}
		}
		return sb.toString();
	}
	
	/**
     * 將金額格式化為上行電文所需的字串格式，長度不足部分會自動補'0'
     *
     * @param amnt 金額
     * @param decimalLength 整數位長度
     * @param decimalDigits 小數位
     * @return 上行電文金額格式字串
     */
    public static String fmtAmnt4TeleUpld(double amnt, int decimalLength, int decimalDigits) {
        // 整數位長度不可小於0
        if (decimalLength<0 || decimalLength>18) {
            return "";
        }
        // 小數位不可小於0
        if (decimalDigits<0) {
            return "";
        }
        // 組整數位pattern
        StringBuffer patternBuf = new StringBuffer();
        for (int i=0; i<decimalLength; i++) {
            patternBuf.append("0");
        }
        // 組小數位pattern
        if (decimalDigits >0) {
            patternBuf.append(".");
            for (int i=0; i<decimalDigits; i++) {
                patternBuf.append("0");
            }
        }
        // 依pattern格式化金額
        DecimalFormat decimalFormat = new DecimalFormat(patternBuf.toString());
        return decimalFormat.format(amnt);
    }
    
    /**
     * 判断时间是否在时间段内
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
}
