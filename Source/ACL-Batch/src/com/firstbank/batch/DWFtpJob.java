/**
 * @(#) DWFtpJob.java
 *
 * Directions:客戶綁定/解除日報表批次
 *
 * Copyright (c) 2019 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2019/06/12
 *    1) First release
 *
 */
package com.firstbank.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;

import com.hitrust.acl.batch.AbstractAclBatch;
import com.hitrust.acl.batch.exception.CmdArgsException;
import com.hitrust.acl.common.CommonUtil;
import com.hitrust.acl.common.DateUtil;
import com.hitrust.acl.common.StringUtil;
import com.hitrust.acl.model.SysParm;
import com.hitrust.acl.service.DailyBindingRptSrv;
import com.hitrust.acl.service.SysParmService;

public class DWFtpJob  extends AbstractAclBatch {
	// log4j
	static Logger LOG = Logger.getLogger(DWFtpJob.class);
	private SysParmService sysParmService =  (SysParmService)super.getSpringBean("sysParmService");
	private DailyBindingRptSrv dailyBindingRptSrv =(DailyBindingRptSrv)super.getSpringBean("dailyBindingRptSrv");
    
	public static void main(String[] args){
        new DWFtpJob().execute(args);
    }
		
	@Override
	public void execute(String[] args) {
		int rtn = 0;
		LOG.info("\n");
		LOG.info("======================================================");
		//客戶綁定/解除日報表批次開始
		LOG.info(" DW產檔  開始....");

		try {

			String today = DateUtil.getCurrentTime("D", "AD");
			// String yesterday = DateUtil.countDate(today, -1);
			String filename = "CFALLOG." + today + ".1000.TXT";
			LOG.info(" 檔名:" + filename);

			String resultQuery = "";
			String resultFtp = "";
			String resultWritefile = "";

			LOG.info(" 資料日期:" + today);

			LOG.info(DateUtil.getCurrentTime("DT", "AD") + "開始執行批次");

			LOG.info("1．開始執行查詢及產生檔案");

			String filetype = "";
			String ftpip = getSysParm("FTP_IP");
			String ftpUPdir = getSysParm("FTP_UP_DIR");
			String ftp_id = getSysParm("FTP_ID");
			String ftp_mema = getSysParm("FTP_MEMA");
			String ftp_port = getSysParm("FTP_PORT");
			String tempPath = getSysParm("FTP_LOCAL_DIR");
			String ftp_mode = getSysParm("FTP_MODE"); //A:主動，P:被動

			filetype = "asc";

			LOG.info("要ftp的ip:" + ftpip);
			LOG.info("要存放的資料夾:" + ftpUPdir);

			if (!tempPath.endsWith(File.separator)) {
				tempPath += File.separator;
			}

			// IF本地端目錄不存在則建立
			if (!StringUtil.isBlank(tempPath)) {
				try {
					File tmpDir = new File(tempPath);

					if (!tmpDir.exists()) {
						LOG.info("create LOCAL_TEMP_DIR:" + tempPath);
						tmpDir.mkdirs();
					}
				} catch (Exception e) {
					LOG.error("建立路徑失敗:" + e.toString(), e);
					return;
				}
			}
			// //執行上傳
			String fileContent = "";
			try {
				fileContent = dailyBindingRptSrv.generateBindingRpt(today);
				//LOG.info("fileContent = [" + fileContent + "]");
				if (fileContent != null && fileContent.length() > 0) {
					resultQuery = "ok";
				} else {
					LOG.info("本日無資料");
					resultQuery = "ok";
				}
			} catch (Exception e) {
				LOG.error("產生交易資料失敗:" + e.toString(), e);
				resultQuery = "fail";
				return;
			}
			LOG.info("執行查詢結果:" + "(" + resultQuery + ")");
			// 交易明細資料寫入資料暫存路徑案
			if (resultQuery.equals("ok") && fileContent != null) {
				FileOutputStream fos = null;
				OutputStreamWriter osw = null;
				try {
					fos = new FileOutputStream(tempPath + new String((filename).getBytes("big5"), "big5"));
					osw = new OutputStreamWriter(fos, "big5");
					BufferedWriter fWriter = new BufferedWriter(osw);
					fWriter.write(fileContent);
					fWriter.close();
					resultWritefile = "ok";
				} catch (Exception e) {
					LOG.error("寫入檔案失敗:" + e.toString(), e);
					resultWritefile = "fail";
					return;
				} finally {
					if (osw != null) {
						osw.close();
					}
					if (fos != null) {
						fos.close();
					}
				}
			}

			LOG.info("執行產生檔案結果:" + "(" + resultWritefile + ")");
			if (resultQuery.equals("ok") && resultWritefile.equals("ok")) {

				LOG.info("2．上傳檔案");
				// 將檔案上傳FTP 路徑
				rtn = CommonUtil.uploadToFTP(ftpip, Integer.parseInt(ftp_port), ftp_id, ftp_mema, ftp_mode, ftpUPdir,
						tempPath, filename);

				if (rtn != 0) {
					LOG.error("ftp file fail:" + filename);
					// 刪除本地端檔案
					File file = new File(tempPath + filename);
					if (!file.delete()) {
						LOG.error("delete file fail:" + filename);
					}
					resultFtp ="fail";

				} else {
					resultFtp = "ok";
				}

				LOG.info("將結果檔案上傳結果:" + "(" + resultFtp + ")");

				if (resultFtp.equals("ok")) {
					LOG.info("檔案上傳成功!!");
				} else {
					LOG.info("檔案上傳不成功!!");
				}
			}

			if (resultQuery.equals("ok") && resultFtp.equals("ok")) {
				LOG.info("本日批次工作完成無錯誤.....ok");
			} else {
				LOG.info("本日批次工作未完成,請檢查問題!!");
			}

		} catch (Exception e) {
			LOG.error("DWFtpJob Exception:", e);
		} finally {
			//客戶綁定/解除日報表批次  結束
			LOG.info("DW產檔  結束....");
		}
	}

	@Override
	public String[] cmdArgsConvertor(JobExecutionContext jobExecutionContext) throws CmdArgsException {
		return new String[0];
	}
	
	public String getSysParm(String param) {
		SysParm sysParm = sysParmService.fetchSysParmByParm(param);
		if(sysParm==null){
			LOG.error("無法取得參數:["+param+"]");
			return null;
		}
		return sysParm.getParmValue();
	}
}
