/**
 * DownloadController.java
 *
 * Copyright (c) 2009 HiTRUST Incorporated.All rights reserved.
 * 
 * Description:   檔案下載公用Controller
 *
 * Modify History:
 *  v1.00, 2009-7-29, Royal Shen
 *   1) First release
 *  
 */
package com.hitrust.bank.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import com.hitrust.framework.model.Command;
import com.hitrust.framework.web.BaseAutoCommandController;
import com.hitrust.acl.common.FileDowndloadUtil;
import com.hitrust.bank.model.DownloadCommand;
import com.hitrust.util.StringUtil;

/**
 * 檔案下載公用 控制器
 * @author Royal Shen
 *
 */
public class DownloadController extends BaseAutoCommandController {

	private Logger LOG = Logger.getLogger(getClass().getName());
	
	public DownloadController(){
		setCommandClass(DownloadCommand.class);
	}
	
	/**
	 * Excel下載
	 * @param command
	 * @param renponse
	 * @param request
	 */
	public void toExcel(Command command, HttpServletResponse response, HttpServletRequest request){
		DownloadCommand form = (DownloadCommand)command;
		String fileName = form.getFileName();//頁面指定檔案名稱(預留處理，暫時無指定)
		//模板文件名稱
		String tempFilename="";
		String templateName=request.getParameter("templateName");
		if(StringUtil.isBlank(templateName)){
			templateName = form.getFileName();
		}
		tempFilename = this.avoidHttpResponseSplitting(templateName);
		//產生excel
		FileDowndloadUtil.downExcel(form, fileName, tempFilename, response);
		returnView.set(RETURN_NULL);
		
	}
	
	/**
	 * Excel下載
	 * @param command
	 * @param renponse
	 * @param request
	 */
	public void toExcel(Command command, HttpServletResponse response, String templateName){
		DownloadCommand form = (DownloadCommand)command;
		String tempFilename="";
		String fileName = form.getFileName();//頁面指定檔案名稱(預留處理，暫時無指定)
		//模板文件名稱
		tempFilename = this.avoidHttpResponseSplitting(templateName);
		//產生excel
		FileDowndloadUtil.downExcel(form,fileName,tempFilename,response);
	}

    private String avoidHttpResponseSplitting(String data) {
        // check carriage return
        if (data.indexOf('\r') != -1) {
            data = data.replace('\r', ' ');
        }
        if (data.indexOf('\n') != -1) {
            data = data.replace('\n', ' ');
        }
        if (data.indexOf(':') != -1) {
            data = data.replace(':', ' ');
        }
        if (data.indexOf('=') != -1) {
            data = data.replace('=', ' ');
        }
        return data;
    }
}
