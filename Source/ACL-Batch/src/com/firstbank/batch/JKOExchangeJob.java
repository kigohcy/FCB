/**
 * @(#) JKOExchangeJob.java
 *
 * Directions:街口對帳批次
 *
 * Copyright (c) 2018 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2018/03/31
 *    1) First release
 *
 */
package com.firstbank.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;

import com.hitrust.acl.batch.AbstractAclBatch;
import com.hitrust.acl.batch.exception.CmdArgsException;
import com.hitrust.acl.common.CommonUtil;
import com.hitrust.acl.common.DateUtil;
import com.hitrust.acl.common.StringUtil;
import com.hitrust.acl.model.EcData;
import com.hitrust.acl.model.TrnsData;
import com.hitrust.acl.service.TrnsDataService;
import com.hitrust.acl.service.TrnsSyncService;
import com.hitrust.acl.util.AppEnv;

public class JKOExchangeJob extends AbstractAclBatch {
	// log4j
	static Logger LOG = Logger.getLogger(AbstractAclBatch.class);
	private static String JKO_ECID = "0001";  //街口支付電商平台代碼
	private static String FILENAME = "AccountLink_For_JKOS"; //檔名 for 街口
	private static String FILENAME_OTHERS = "AccountLink_For"; //檔名 for 其他
	private TrnsDataService trnsDataService = (TrnsDataService)super.getSpringBean("trnsDataService");
	private TrnsSyncService trnsSyncService = (TrnsSyncService)super.getSpringBean("trnsSyncService");
	
	public static void main(String[] args){
        new JKOExchangeJob().execute(args);
    }
	
	@Override
	public void execute(String[] args) {
		int rtn = 0;
		LOG.info("======>AP 對帳檔產生批次  開始....");
		try{
			//取得前一天交易日期
			String today = DateUtil.getCurrentTime("D", "AD");
			String yesterday = DateUtil.countDate(today, -1);
			String dataDate;
			if(args == null || args.length==0){
				dataDate = yesterday; 
			}else{
				dataDate = args[0];
			}
			LOG.info("======>資料日期:"+dataDate);
			List<EcData> ecDataList = trnsDataService.getEcDataList();
			for(EcData ecData:ecDataList){
				if(StringUtils.equals(ecData.getStts(),"02")){
					if(DateUtil.compareDateTime(DateUtil.revertDateTime(DateUtil.formateDBDateTimeForUser(ecData.getTrmnAprvDttm())).substring(0, 8),yesterday)!=0) {
						//狀態為終止, 不處理
						continue;
					}
				}
				//街口抓取舊設定
				//新電商, 抓取各自設定
				String FILE_FTP_USER = StringUtils.equals(ecData.getEcId(), JKO_ECID)?AppEnv.getString("FILE_FTP_USER"):AppEnv.getString("FILE_FTP_USER_"+ecData.getEcId());//FTP 使用者名稱
				String FILE_FTP_PSWD = StringUtils.equals(ecData.getEcId(), JKO_ECID)?AppEnv.getString("FILE_FTP_PSWD"):AppEnv.getString("FILE_FTP_PSWD_"+ecData.getEcId());//FTP 使用者密碼
				String FILE_FTP_IP =  StringUtils.equals(ecData.getEcId(), JKO_ECID)?AppEnv.getString("FILE_FTP_IP"):AppEnv.getString("FILE_FTP_IP_"+ecData.getEcId());    //FTP Server IP
				String FILE_FTP_PORT = StringUtils.equals(ecData.getEcId(), JKO_ECID)?AppEnv.getString("FILE_FTP_PORT"):AppEnv.getString("FILE_FTP_PORT_"+ecData.getEcId());//FTP Server Port		
				String FILE_FTP_MODE = StringUtils.equals(ecData.getEcId(), JKO_ECID)?AppEnv.getString("FILE_FTP_MODE"):AppEnv.getString("FILE_FTP_MODE_"+ecData.getEcId());//報表上傳FTP傳輸模式 (A:主動模式/P:被動模式)
				String FILE_FTP_DIR = StringUtils.equals(ecData.getEcId(), JKO_ECID)?AppEnv.getString("FILE_FTP_DIR"):AppEnv.getString("FILE_FTP_DIR_"+ecData.getEcId());  //FTP 路徑
				
				String tempPath = StringUtils.equals(ecData.getEcId(), JKO_ECID)?AppEnv.getString("LOCAL_TEMP_DIR"):AppEnv.getString("LOCAL_TEMP_DIR_"+ecData.getEcId());
				
//				
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
				
				//依交易日期產生交易明細檔名
				String fileName = (StringUtils.equals(ecData.getEcId(), JKO_ECID)?FILENAME:(FILENAME_OTHERS+"_"+ecData.getEcId())) + "-" + dataDate + ".txt";
		        Date strtDttm = DateUtil.formatStrToDate(dataDate, "000000");
		        Date endDttm = DateUtil.formatStrToDate(dataDate, "235959");
		        
		        List<TrnsData> trnsDataList = trnsDataService.getTrnsData4JKOS(ecData.getEcId(), strtDttm, endDttm, "02", "id.ecMsgNo");
		        LOG.info("ecId=" + ecData.getEcId() + ", count=" + trnsDataList.size());
		        //產生交易資料
				String fileContent = "";
				try {
					fileContent = genTrnsData(ecData.getEcId(), trnsDataList);
				} catch (Exception e) {
					LOG.error("產生交易資料失敗:" + e.toString(), e);
					return;
				}
				
				//交易明細資料寫入資料暫存路徑案
				FileOutputStream fos = null;
				OutputStreamWriter osw = null;
				try {
					fos = new FileOutputStream(tempPath + new String((fileName).getBytes("big5"), "big5"));
					osw = new OutputStreamWriter(fos, "big5");
					BufferedWriter fWriter = new BufferedWriter(osw);
					fWriter.write(fileContent);
					fWriter.close();
				} catch (Exception e) {
					LOG.error("寫入檔案失敗:" + e.toString(), e);
					return;
				}finally{
					if(osw!=null){
						osw.close();
					}
					if(fos!=null){
						fos.close();
					}
				}
				
				//將檔案上傳FTP 路徑
				rtn = CommonUtil.uploadToFTP(FILE_FTP_IP, Integer.parseInt(FILE_FTP_PORT), FILE_FTP_USER, FILE_FTP_PSWD, 
						FILE_FTP_MODE, FILE_FTP_DIR, tempPath, fileName);
					
				if (rtn != 0) {
					LOG.error("ftp file fail:" + fileName);
					//刪除本地端檔案
					File file = new File(tempPath + fileName);
					if(!file.delete()){
						LOG.error("delete file fail:" + fileName);
					}
				}
			}
		}catch(Exception e){
			LOG.error("JKOExchangeJob Exception:",e);
		}finally{
			LOG.info("======>AP 對帳檔產生批次  結束....");
		}
	}
	
	/**
	 * 產生交易資料明細
	 * 
	 * @param dailyTrnsDataList
	 *            交易明細資料
	 * @return String trnsData內容
	 * @throws Exception
	 */
	private String genTrnsData(String ecId,List<TrnsData> trnsDataList) throws Exception {
		StringBuffer bf = new StringBuffer();
		String trnsKind = "";
		String trnsTypeFromDb ="";
		String userAccount="";
		
		for(TrnsData trnsData:trnsDataList){
			//A:扣款, B:退款, C:提領, D:儲值
			trnsKind = "";
			userAccount="";
			trnsTypeFromDb ="";
			trnsTypeFromDb = trnsData.getTrnsType();
			if ("A".equals(trnsTypeFromDb)) {
				trnsKind = "P";//付款(含儲值)
				userAccount=trnsData.getRealAcnt();
			} else if ("B".equals(trnsTypeFromDb)) {
				userAccount=trnsData.getRecvAcnt();
				trnsKind = "R";//退款
			} else if ("C".equals(trnsTypeFromDb)) {
				userAccount=trnsData.getRecvAcnt();
				trnsKind = "N";//負向(提領)
			} else if("D".equals(trnsTypeFromDb)){
				userAccount=trnsData.getRealAcnt();
				trnsKind = "P";//付款(含儲值) //TODO:待確認
			} else if ("E".equals(trnsTypeFromDb)) {
				trnsKind = "P";//繳費稅
				userAccount=trnsData.getRealAcnt();
			}
			
			//1. 交易序號(平台訊息序號)，X(20) 左靠右補空白
			bf.append(paddingWhiteSpaceRigthToFixedLen(CommonUtil.replaceNull(trnsData.getId().getEcMsgNo()),20));	
			
			//2. 交易日期時間，X(14)
			String trnsDateTime = DateUtil.converToString(trnsData.getTrnsDttm(), "yyyyMMddHHmmss" );
			bf.append(paddingWhiteSpaceRigthToFixedLen(trnsDateTime, 14));
			
			//3. 街口代碼，X(4)
			bf.append(ecId);
			
			//4. 使用者街口會員代碼，X(20)
			bf.append(paddingWhiteSpaceRigthToFixedLen(trnsData.getEcUser(),20));
			
			//5. 使用者銀行帳戶號碼，X(14)
			bf.append(paddingWhiteSpaceRigthToFixedLen(CommonUtil.replaceNull(userAccount),14));
			
			//6. 帳戶種類(取帳號4~5碼)，X(3)
			if(StringUtils.isNotBlank(userAccount) && userAccount.length() >=5){
				bf.append(paddingWhiteSpaceRigthToFixedLen(CommonUtil.replaceNull(userAccount).substring(3,5),3));
			}else{
				bf.append(paddingWhiteSpaceRigthToFixedLen("",3));
			}
			
			//7. 交易代號，X(1)
			bf.append(trnsKind);
			
			//8. 訂單編號，X(24)
			bf.append(paddingWhiteSpaceRigthToFixedLen(CommonUtil.replaceNull(trnsData.getOrdrNo()),24));
			
			//9. 交易說明，X(40)
			bf.append(paddingWhiteSpaceRigthToFixedLen(CommonUtil.replaceNull(trnsData.getTrnsNote()),40));
			
			//10.交易金額，X(10)
			bf.append(paddingWhiteSpaceRigthToFixedLen(CommonUtil.replaceNull(String.valueOf(trnsData.getTrnsAmnt())),10));	
			
			//11.幣別，X(3)
			bf.append("NTD");
			
			//12.交易類別，X(1)
			//若交易代號為P(付款)，則1:付款，2:儲值
			//若交易代號為R(退款)，則放空白
			//若交易代號為N(負向(提領)，則放空白
			if ("P".equals(trnsKind)) {
				if(StringUtils.equals(trnsTypeFromDb, "A")){
					bf.append("1");
				}else if(StringUtils.equals(trnsTypeFromDb, "E")) {
					bf.append("3"); //繳費稅改為3
				}
				else{
					bf.append("2");
				}
			} else{
				bf.append(" ");
			}
					
			//13 保留欄位，X(16)
			bf.append(paddingWhiteSpaceRigthToFixedLen("" ,16));
		
			bf.append("\r\n");
		}
		return bf.toString();
	}
	
	private String  paddingWhiteSpaceRigthToFixedLen(String input, int fixedLength){
		if(input == null) {
			input = "";
		}
		
		int paddingSpaceCount = 0;
		
		try {
			paddingSpaceCount = fixedLength - input.getBytes("BIG5").length;
		} catch (Exception e) {
			LOG.error("String getBytes(\"BIG5\") Error : " + e.toString());
			e.printStackTrace();
		}
			
		StringBuffer sb = new StringBuffer(input);
			
		while(paddingSpaceCount >0){
			sb.append(" ");
			paddingSpaceCount--;
		}
			
		return sb.toString();
	}

	@Override
	public String[] cmdArgsConvertor(JobExecutionContext jobExecutionContext) throws CmdArgsException {
		return new String[0];
	}
	
	

}
