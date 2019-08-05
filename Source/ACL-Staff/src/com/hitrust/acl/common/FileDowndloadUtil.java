/**
 * FileDowndloadUtil.java
 *
 * Copyright (c) 2009 HiTRUST Incorporated.All rights reserved.
 *
 * Modify History: 
 *  v1.00, 2009-9-8, Royal Shen
 *   1) First release
 */
package com.hitrust.acl.common;

import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.util.DateUtil;
import com.hitrust.acl.common.report.ExcelReport;
import com.hitrust.acl.util.StringUtil;

/**
 * 檔案下載Util
 * @author Royal Shen
 */
public class FileDowndloadUtil {

	private Logger LOG = Logger.getLogger(getClass().getName());
	
	
	/**
	 * 功能：後臺代碼下載XLS
	 * @param command 下載command
	 * @param fileName 下載檔案名(暫時無需傳入)
	 * @param templateName 模板名稱
	 * @param response 
	 */
	public static void downExcel(Command command,String fileName,String templateName,HttpServletResponse response){
		//產生excel
		ExcelReport report=new ExcelReport(response);
		if(StringUtil.isBlank(fileName)){
			report.setFileName(templateName+"_"+DateUtil.getCurrentTime("DT", "AD"));
		}else{
			report.setFileName(fileName);
		}
		//產生excel
		report.exportToExcelFile(command,templateName);
	}
	
}
