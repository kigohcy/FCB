/**
 * @(#) AclFunctionTag.java
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

public class RealAcntFormatTag extends BodyTagSupport {
	
	private static final long serialVersionUID = -5979636183362617261L;

	// Log4j
	private static Logger LOG = Logger.getLogger(RealAcntFormatTag.class);

	// =============== input Attribute ===============
	private String realAcnt; // 實體帳號
	
	@Override
	public int doStartTag() throws JspException {
		JspWriter writer = null;
		String rtnVal = "";
		
		try {
			rtnVal = CommonUtil.relAcntFormat(this.getRealAcnt());

			writer = this.pageContext.getOut();
			writer.print(rtnVal);
			
		} catch (IOException e) {
			LOG.error("========== [RealAcntFormatTag IOException] =====", e);
		} catch (Exception e) {
			LOG.error("========== [RealAcntFormatTag Exception] =====", e);
		}
		
		return SKIP_BODY;
	}
	
	// =============== Getter & Setter ===============
	public String getRealAcnt() {
		return realAcnt;
	}
	public void setRealAcnt(String realAcnt) {
		this.realAcnt = realAcnt;
	}
	
}
