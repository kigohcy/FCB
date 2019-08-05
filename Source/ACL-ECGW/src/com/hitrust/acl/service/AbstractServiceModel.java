/*
 * @(#)AbstractBusinessModel.java
 *
 * Copyright (c) 2006 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2006/03/13, Tim Cao
 *   1) First release
 */
package com.hitrust.acl.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hitrust.acl.exception.ApplicationException;
import com.hitrust.acl.exception.DBException;
import com.hitrust.acl.response.AbstractResponseBean;

public abstract class AbstractServiceModel implements ServiceModel {
	
	protected String service;
	protected String operation;
	protected AbstractResponseBean resBean;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	/**
	 * Constructor.
	 * @param function
	 * @param operation
	 * @param login
	 * @param uiBean
	 */
	public AbstractServiceModel(String service, String operation, AbstractResponseBean resBean, HttpServletRequest request, HttpServletResponse response) {
		
		this.service = service;
		this.operation = operation;
		this.resBean = resBean;
		this.request = request;
		this.response = response;

	}
	
	/**
	 * Process operations of each menu. 
	 * @return
	 * @throws ApplicationException
	 * @throws DBException
	 */
	public abstract AbstractResponseBean process() throws ApplicationException, DBException;
}
