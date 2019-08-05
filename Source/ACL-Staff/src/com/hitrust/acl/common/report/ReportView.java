/**
 * @(#)ReportView.java
 *
 * Copyright(c)2009 HiTRUST Incorporated.All rights reserved.
 * 
 * Description :報表實體
 *
 * Modify History:
 *  v1.00, 2009-7-26, Royal Shen
 *   1) First release
 */
package com.hitrust.acl.common.report;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import com.hitrust.framework.APSystem;

/**
 * 報表實體
 * @author Royal Shen
 *
 */
public abstract class ReportView {
	
	protected String contentType = "";
	
	protected String fileName;
	
	protected String filePath = APSystem.getSysDir()+File.separator+ "template"+File.separator;//路徑
	
	protected String templateName ;//路徑+模板文件名字
	
	HttpServletResponse response;
	
	public ReportView(){
	}
	/**
	 * @return the contentTye
	 */
	public String getContentType() {
		return contentType;
	}
	/**
	 * @param contentTye the contentTye to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	protected abstract void initHeadAndContentType();
	/**
	 * @return the response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}
	/**
	 * @param response the response to set
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}
	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
}
