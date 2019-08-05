/**
 * ExcelReport.java
 *
 * Copyright (c) 2009 HiTRUST Incorporated.All rights reserved.
 * 
 * Description: EXCEL報表生成類
 *
 * Modify History:
 *  v1.00, 2009-3-16, Jmiu Han
 *   1) First release
 *  v1.01, 2010-9-15, Vince Lee
 *   1) 增加檔案加密處理
 *  v1.02, 2013/08/20, Kevin Wang
 *   1) TCBBMNB-160, 201307弱點掃瞄修正, Path Manipulation
 */
package com.hitrust.acl.common.report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import net.sf.jxls.tag.TagLib;
import net.sf.jxls.transformer.Configuration;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;

import com.hitrust.acl.common.HitrustJxTaglib;
import com.hitrust.acl.common.HitrustJxslConfiguration;
import com.hitrust.acl.util.DateUtil;

/**
 * EXCEL報表生成類
 * @author Jmiu Han
 *
 */
public class ExcelReport extends ReportView {

	private Logger LOG = Logger.getLogger(getClass().getName());

	public ExcelReport(HttpServletResponse response) {
		this.setResponse(response);
	}

	/**
	 * 功能： 把commandObject 寫入指定的模板
	 * 
	 * @param Object
	 *            commandObject
	 * @param templateName
	 *            模板名字
	 */
	public void exportToExcelFile(Object commandObject, String templateName) {
		this.setTemplateName(templateName + ".xls");
		this.createExcel(commandObject);
	}

	/**
	 * 處理解析Excel模板時會用到的標簽庫。
	 * 當前默認實現只註冊了hitrustJxTaglib中定義的標簽庫，如果子類需要添加新標簽庫，或者修改標簽獲取方法，可覆蓋此方法。
	 * 
	 * @return Map 存放各種標簽庫的Map，以標簽的前綴名為Key，以對應的標簽庫為對像值
	 */
	protected Map processTaglib() {
		Map map = new HashMap();
		map.put("jxh", new HitrustJxTaglib());
		return map;
	}

	/**
	 * 生成JXls配置類的方法。
	 * 當前默認實現只返回了一個新生成的HitrustJxslConfiguration對像，如果子類需要修改獲取方法，可將其覆蓋。
	 * 
	 * @author Arthur Li
	 * @return Configuration
	 */
	protected Configuration jxslConfig() {
		return new HitrustJxslConfiguration();
	}

	/**
	 * 根據commandObject對像數據，解析Excel模板生成目標文件，最後將文件從輸出流輸出到客戶端。
	 * 
	 * @param commandObject
	 */
	private void createExcel(Object commandObject) {
		try {
			response.resetBuffer();
			response.reset();
			setContentType("application/msexcel");//
			initHeadAndContentType();
			// 初始化Excel模板解析器
			XLSTransformer transformer = new XLSTransformer(jxslConfig());
			Configuration config = transformer.getConfiguration();

			// 註冊標簽庫
			Map tags = processTaglib();
			for (Iterator it = tags.keySet().iterator(); it.hasNext();/*
																		 * nothing
																		 * needed
																		 * to do
																		 */) {
				String tagName = (String) it.next();
				config.registerTagLib((TagLib) tags.get(tagName), tagName);
			}

			// 為處理Excel模板準備commandName
			Map beanParams = new HashMap();
			beanParams.put("command", commandObject);

			// 讀入模板文件
            // v1.02, 
            /*
			File template = new File(this.getFilePath() + "xls"
					+ File.separator + this.getTemplateName());
            */
            File template = null;
            File[] files = new File(this.getFilePath() + "xls").listFiles();
            for (int i=0; i<files.length; i++) {
                if (files[i].getName().equals(this.getTemplateName())) {
                    LOG.debug("找到Excel樣本:" + files[i].getName());
                    template = files[i];
                }
            }
            if (template ==null) {
                throw new Exception("Excel樣本檔案不存在!");
            }
			// 處理模板
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			OutputStream httpOut = this.response.getOutputStream();
			Workbook workbook = transformer.transformXLS(new FileInputStream(template), beanParams);// 解析模板，生成目標文件
			workbook.write(out);// 輸出目標文件
			out.flush();

			int len = 1024;
			byte[] outData = out.toByteArray();
			for (int i = 0; i < outData.length; i += len) {
				if (i + len > outData.length) {
					len = outData.length - i;
				}
				httpOut.write(outData, i, len);
			}
			httpOut.flush();
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			e.printStackTrace(ps);
			LOG.error("create Excel  error!");
			LOG.error(new String(baos.toByteArray()));
			// close stream
			try {
				baos.close();
				ps.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 根據本類中的beanName屬性解析Excel模板中所要用到的Bean名字。
	 * 如果子類需要改變獲取Excel模板中所需Bean名字的方法，可將其覆蓋。
	 * 
	 * @return String
	 */
	protected String getCommandName(String beanName) {
		if (beanName == null) {
			LOG.debug("beanName is null!");
			return null;
		}

		if (beanName.indexOf(".") == -1)
			return beanName;

		return beanName.substring(beanName.lastIndexOf(".") + 1);
	}

	protected void initHeadAndContentType() {
		if (fileName == null && "".equals(fileName)) {
			fileName = DateUtil.getCurrentTime("DT", "AD");
		}
        this.contentType = this.avoidHttpResponseSplitting(this.contentType);
        // assign download file name to avoid Header Manipulation
        String downloadFileName = "RP" + DateUtil.getCurrentTime("DT", "AD");
		response.addHeader("ContentType", this.contentType);
		response.addHeader("Content-disposition", "attachment; filename=" + downloadFileName + ".xls");
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
