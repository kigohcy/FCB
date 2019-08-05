/**
 * @(#)AclFunTag.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : ACL Function Tag
 * 
 * Modify History:
 *  v1.00, 2016/03/04, Evan
 *   1) First release
 *  
 */
package com.hitrust.acl.common.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.hitrust.bank.common.CommonUtil;

public class AclFunTag extends SimpleTagSupport {
	
	
	private String realAcnt;	//實體帳號

	public AclFunTag() {
		// TODO Auto-generated constructor stub
	}

	public String getRealAcnt() {
		return realAcnt;
	}

	public void setRealAcnt(String realAcnt) {
		this.realAcnt = realAcnt;
	}

	@Override
	public void doTag() throws JspException, IOException {
		
		getJspContext().getOut().write(CommonUtil.relAcntFormat(this.realAcnt));
		
	}
}
