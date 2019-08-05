/**
 * @(#) NewsImageServlet.java
 *
 * Directions: 公告訊息圖檔 Servlet
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/08/01, Yann
 *    1) First release
 *   
 */
package com.hitrust.bank.servlet;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import com.hitrust.bank.model.NewsImag;
import com.hitrust.bank.model.base.AbstractNewsImag;
import com.hitrust.bank.model.base.AbstractNewsImag.Id;
import com.hitrust.framework.APSystem;
import com.hitrust.framework.dao.BaseDAO;

public class NewsImageServlet extends HttpServlet {
	private static final long serialVersionUID = -6618884512041163897L;
	
	//LOG4J 
	static Logger LOG = Logger.getLogger(NewsImageServlet.class);
    
	/**
	 * 
	 */
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		// Processing
		this.ImgControl(request, response);
	}

	/**
	 * 顯示圖檔
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void ImgControl(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String seq = request.getParameter("seq");
		String fileName = request.getParameter("fileName");
		
		LOG.debug("seq="+seq+"/fileName="+fileName);
		
		BaseDAO baseDAO = (BaseDAO) APSystem.getApplicationContext().getBean("baseDAO");
		
		AbstractNewsImag.Id id = new Id();
		id.setSeq(seq);
		id.setFileName(fileName);
		NewsImag newsImag = (NewsImag)baseDAO.queryById(NewsImag.class, id);
		if(newsImag != null){
			byte[] imag = newsImag.getImag();
			OutputStream os = response.getOutputStream();
			os.write(imag);
			os.flush();
			os.close();
			LOG.debug("outputStream write finish....");
		}
	}
}
