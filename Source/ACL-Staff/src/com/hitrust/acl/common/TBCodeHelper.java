/**
 * @(#) TBCodeHelper.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/21, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */
package com.hitrust.acl.common;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.TbCode;
import com.hitrust.bank.service.TbCodeSrv;
import com.hitrust.framework.APSystem;
import com.hitrust.framework.model.APLogin;

public class TBCodeHelper {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(TBCodeHelper.class);
	
	private String tbCodeMsg = "";
	
	private TbCodeSrv tbCodeSrv = (TbCodeSrv) APSystem.getApplicationContext().getBean("tbCodeSrv");
	
	/**
	 * @param codeType 訊息類別
	 * @param codeId   訊息代碼
	 */
	public TBCodeHelper(String codeType, String codeId) {
		this.fetchTBCode(codeType, codeId);
	}
	
	// =============== private Method ===============
	private void fetchTBCode(String codeType, String codeId) {
		
		Locale locale = new Locale("zh", "TW");
		
		String defualtMsg = I18nConverter.i18nMapping("i18n.def.msg", locale);
		
		LOG.info("[codeType]: " + codeType + " [codeId]: " + codeId);
		
		TbCode tbCode = tbCodeSrv.getTbCode(codeType + "-" + codeId);
		
		if (!StringUtil.isBlank(tbCode)) {
			this.tbCodeMsg = tbCode.getCodeDesc();
		
		} else {
			this.tbCodeMsg = defualtMsg.concat("(").concat(codeId).concat(")");
			LOG.info("========== 查無符合對應訊息 [rtnCode]: " + codeId + " =====");
		}
	}

	// =============== Getter & Setter ===============
	public String getTbCodeMsg() {
		return tbCodeMsg;
	}
	
}
