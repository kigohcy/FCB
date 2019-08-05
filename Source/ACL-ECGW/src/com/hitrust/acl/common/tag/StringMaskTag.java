/**
 * @(#) StringMaskTag.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/05/19, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.acl.common.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

import com.hitrust.bank.common.CommonUtil;

public class StringMaskTag extends BodyTagSupport {

	private static final long serialVersionUID = -4133254296775740440L;

	// Log4j
	private static Logger LOG = Logger.getLogger(StringMaskTag.class);
	
	// =============== input Attribute ===============
	private String idStr;  // 遮蔽字串
	private int start;	   // 遮蔽起始位置
	private int maskCount; // 遮蔽數量
	private String symbol; // 遮蔽符號

	@Override
	public int doStartTag() throws JspException {
		JspWriter writer = null;
		String rtnVal = "";
		
		try {
			rtnVal = CommonUtil.stringMask(idStr, start, maskCount, symbol);

			writer = this.pageContext.getOut();
			writer.print(rtnVal);
			
		} catch (IOException e) {
			LOG.error("========== [StringMaskTag IOException] ==========", e);
		} catch (Exception e) {
			LOG.error("========== [StringMaskTag Exception] ==========", e);
		}
		
		return SKIP_BODY;
	}

	// =============== Getter & Setter ===============
	public String getIdStr() {
		return idStr;
	}
	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getMaskCount() {
		return maskCount;
	}
	public void setMaskCount(int maskCount) {
		this.maskCount = maskCount;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
}
