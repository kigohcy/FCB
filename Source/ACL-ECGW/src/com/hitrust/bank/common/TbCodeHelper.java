/**
 * @(#) TbCodeHelper.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/25, Eason Hsu
 *    1) JIRA-Number, First release
 *   v1.01, 2016/12/28, Eason Hsu
 *    1) TSBACL-144, 網銀 & 晶片卡認證失敗, 錯誤訊息 Mapping 調整
 */
package com.hitrust.bank.common;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.hitrust.acl.APSystem;
import com.hitrust.acl.exception.DBException;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.beans.TbCode;
import com.hitrust.bank.dao.home.TBCodeHome;

public class TbCodeHelper {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(TbCodeHelper.class);
	
	private String tbCodeMsg = "";
	
	private TbCode tbCode = null; // v1.01, 參考訊息代碼
	
	// v1.01
	public TbCodeHelper () {}

	public TbCodeHelper(String codeId, String codeType) {
		this.fetchTbCodeByKey(codeId, codeType); 
	}
	
	// =============== private Method ===============
	private void fetchTbCodeByKey(String codeId, String codeType) {
		Connection conn = null;
		String CODE_ID = codeType + "-" + codeId;
		try {
			// 取得 connection
			conn = APSystem.getConnection(APSystem.DB_ACLINK);
			
			TbCode code = null;
			TBCodeHome home = new TBCodeHome(conn);
			
			code = home.fetchTbCodeByKey(CODE_ID, codeType);
			
			if (!StringUtil.isBlank(code)) {
				this.tbCodeMsg = code.CODE_DESC;
				
			} else {
				this.tbCodeMsg = "訊息代碼不存在(" + CODE_ID + ")";
			}
			
		} catch (DBException e) {
			LOG.error("[TbCodeHelper DBException]: ", e);
			this.tbCodeMsg = "訊息代碼不存在(" + codeId + ")";
			
		} finally {
			if (conn != null) {
				APSystem.returnConnection(conn, APSystem.DB_ACLINK);
			}
		}
		
	}
	
	/**
	 * 依據 錯誤訊息取得錯訊代碼
	 * @param errorMsg
	 * @return TbCode
	 * @since v1.01
	 */
	public TbCode fetchTbCodeByErrorMsg(String errorMsg) {
		Connection conn = null;
		
		TbCode code = null;
		TBCodeHome home = null;
		
		try {
			// 取得 connection
			conn = APSystem.getConnection(APSystem.DB_ACLINK);
			home = new TBCodeHome(conn);
			code = home.fetchTbCodeByErrorMsg(errorMsg);
			
		} catch (DBException e) {
			LOG.error("[TbCodeHelper DBException]: ", e);
			
		} finally {
			if (conn != null) {
				APSystem.returnConnection(conn, APSystem.DB_ACLINK);
			}
		}
		
		return code;
	}

	// =============== Getter & Setter ===============
	public String getTbCodeMsg() {
		return tbCodeMsg;
	}
	
	// v1.01
	public TbCode getTbCode() {
		return tbCode;
	}
	
}
