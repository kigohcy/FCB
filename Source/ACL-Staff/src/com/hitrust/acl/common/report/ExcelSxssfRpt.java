/**
 * ExcelSxssfReport.java
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
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import net.sf.jxls.transformer.Configuration;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.command.CellDataUpdater;
import org.jxls.common.CellData;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;

import com.hitrust.acl.common.HitrustJxTaglib;
import com.hitrust.acl.common.HitrustJxslConfiguration;
import com.hitrust.acl.util.DateUtil;

/**
 * EXCEL報表生成類
 * @author Jmiu Han
 *
 */
public class ExcelSxssfRpt extends ReportView {

	private Logger LOG = Logger.getLogger(getClass().getName());

	public ExcelSxssfRpt(HttpServletResponse response) {
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
		this.setTemplateName(templateName + ".xlsx");
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
			setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");//
			initHeadAndContentType();
			

			// 為處理Excel模板準備commandName
			Map beanParams = new HashMap();
			beanParams.put("command", commandObject);
          
			LOG.info("Excel樣本:" +this.getTemplateName());
			LOG.info("FilePath()="+this.getFilePath());
			
			File template = null;
            String tempFileName ="";
            boolean recursive = true;

            File lookfolder = new File(this.getFilePath() + "xls"); //這裡的xls是指目錄,不是指檔案型態
           
				Collection files = FileUtils.listFiles(lookfolder, null, recursive);

				for (Iterator iterator = files.iterator(); iterator.hasNext();) {
					File file = (File) iterator.next();
					if (file.getName().equals(this.getTemplateName())) {
						LOG.debug("找到Excel樣本:" + file.getAbsolutePath());
						tempFileName = file.getAbsolutePath();
						template = file;
					}
				}
            
           
            if ("".equals(tempFileName)) {
                throw new Exception("Excel樣本檔案不存在!");
            }
            
			
            
            try(InputStream is =  new FileInputStream(tempFileName)) {
                try (OutputStream os = this.response.getOutputStream()) {
                    Workbook workbook = WorkbookFactory.create(is);
                    PoiTransformer transformer = PoiTransformer.createSxssfTransformer(workbook);
                    AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);
                    List<Area> xlsAreaList = areaBuilder.build();
                    Area xlsArea = xlsAreaList.get(0);
                    Context context = new Context();
                    context.putVar("cellRefUpdater", new CellRefUpdater());
                    context.putVar("command", beanParams.get("command"));
                    xlsArea.applyAt(new CellRef("查詢結果!A1"), context);
                    context.getConfig().setIsFormulaProcessingRequired(false); // with SXSSF you cannot use normal formula processing
                    workbook.setForceFormulaRecalculation(true);
                    workbook.setActiveSheet(1);
                    workbook.removeSheetAt(0);//拿掉template sheet
                    transformer.getWorkbook().write(os);
                    
                    os.flush();
                    os.close();
                    
                 
                }
                
            }
			
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
		response.addHeader("Content-disposition", "attachment; filename=" + downloadFileName + ".xlsx");
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
    
    static class CellRefUpdater implements CellDataUpdater{
        @Override
        public void updateCellData(CellData cellData, CellRef targetCell, Context context) {
            if( cellData.isFormulaCell() && cellData.getFormula() != null ){
                cellData.setEvaluationResult(cellData.getFormula().replaceAll("(?<=[A-Za-z])\\d", Integer.toString(targetCell.getRow()+1)));
            }
        }
    }
}
