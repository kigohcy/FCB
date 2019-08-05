/**
 * @(#) EncodingFileter.java
 *
 * Copyright (c) 2015 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2015/10/, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.acl.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class EncodingFilter implements Filter {
	
	private Logger LOG = Logger.getLogger(EncodingFilter.class);
	
	private FilterConfig config = null;
	private String encoding = "ASCII";		// 預設編碼

	@Override
	public void destroy() {
		this.config = null;
		this.encoding = null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		request.setCharacterEncoding(this.encoding);
		
		LOG.info("[Request CharacterEncoding]: " + request.getCharacterEncoding());

		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
		this.encoding = this.config.getInitParameter("encoding");

	}

}
