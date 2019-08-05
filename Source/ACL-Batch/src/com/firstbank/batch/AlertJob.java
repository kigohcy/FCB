/* 
 * Directions:交易失敗次數過多通知批次
 *
 * Copyright (c) 2019 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2019/07/03
 *    1) First release Hitrust
 *
 */
package com.firstbank.batch;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;

import com.hitrust.acl.batch.AbstractAclBatch;
import com.hitrust.acl.batch.exception.CmdArgsException;
import com.hitrust.acl.mail.MailUtil;
import com.hitrust.acl.model.AlertData;
import com.hitrust.acl.model.SysParm;
import com.hitrust.acl.model.VwHostErrorLog;
import com.hitrust.acl.model.base.AbstractAlertData.Id;
import com.hitrust.acl.service.AlertDataService;
import com.hitrust.acl.service.SysParmService;
import com.hitrust.acl.service.VwHostErrorLogService;
import com.hitrust.acl.util.AppEnv;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.acl.util.sms.OTPReceive;
import com.hitrust.acl.util.sms.OTPSend;
import com.hitrust.acl.util.sms.TEMPLATEVARData;


public class AlertJob extends AbstractAclBatch{
	static Logger LOG = Logger.getLogger(AlertJob.class);
	private SysParmService sysParmService =  (SysParmService)super.getSpringBean("sysParmService");
	private AlertDataService alertDataService =(AlertDataService) super.getSpringBean("alertDataService");
	private VwHostErrorLogService vwHostErrorLogService = (VwHostErrorLogService)super.getSpringBean("vwHostErrorLogService");
	
	public static void main(String[] args){
        new AlertJob().execute(args);
    }
	
	public static String SMSUSER = AppEnv.getString("SMS_USER");
	public static String SMSMEMA = AppEnv.getString("SMS_MEMA");
	public static String SMSENCDOE = AppEnv.getString("SMS_ENCDOE");
	
	@Override
	public void execute(String[] args) {
		
		boolean runSMS=false;
		
		LOG.info("\n");
		LOG.info("======================================================");
		LOG.info(" 交易失敗次數過多通知批次  開始....");
		try {
			/*
			 * ALERT_FREQUENCY 交易失敗次數過多MAIL通知批次的錯誤次數 
			 * ALERT_PERIOD 交易失敗次數過多MAIL通知批次的時間區間
			 * ALERT_SMS_FREQUENCY 交易失敗次數過多簡訊通知批次的錯誤次數
			 * ALERT_SMS_PERIOD  簡訊通知批次的時間區間
			 * ALERT_SMS_STATUS  是否開啟簡訊傳送
			 * ALERT_MAIL_RECEIVER 收件者信箱
			 * ALERT_SMS_START   簡訊服務開始時間
			 * ALERT_SMS_END     簡訊服務結束時間
			 * ALERT_SMS_TELNO   簡訊接收人手機號碼
		
			 */
			
			//刪除過期(二天前)MAIL和SMS通知ALERT_DATA TABLE
			alertDataService.deleteBySetlDate("-2");
			
			
			//Mail條件設定
			String alertPeriod = (getSysParm("ALERT_PERIOD")!=null)?getSysParm("ALERT_PERIOD"):"30";
			String alertFrequency = (getSysParm("ALERT_FREQUENCY")!=null)?getSysParm("ALERT_FREQUENCY"):"5";
			
			
			//簡訊條件設定
			String alertSMSStatus= (getSysParm("ALERT_SMS_STATUS")!=null)?getSysParm("ALERT_SMS_STATUS"):"OFF";
			int ialertSMSPeriod=0;
			int ialertSMSFrequency=0;
			String alertSMSPeriod ="";
			String alertSMSFrequency = "";
			String alertSMSTelno ="";
			String now = DateUtil.getCurrentTime("DT", "AD");
			LOG.info("[now]"+now);
			
			if ("ON".equals(alertSMSStatus)) {
				alertSMSPeriod = (getSysParm("ALERT_SMS_PERIOD")!=null)?getSysParm("ALERT_SMS_PERIOD"):"30";
				alertSMSFrequency = (getSysParm("ALERT_SMS_FREQUENCY")!=null)?getSysParm("ALERT_SMS_FREQUENCY"):"5";	
				ialertSMSPeriod=Integer.parseInt(alertSMSPeriod);
				ialertSMSFrequency=Integer.parseInt(alertSMSFrequency);
				alertSMSTelno = (getSysParm("ALERT_SMS_TELNO")!=null)?getSysParm("ALERT_SMS_TELNO"):"0944000001";
				
				String start_time_sms=(getSysParm("ALERT_SMS_START")!=null)?getSysParm("ALERT_SMS_START").replace(":",""):"30";
				String stop_time_sms=(getSysParm("ALERT_SMS_END")!=null)?getSysParm("ALERT_SMS_END").replace(":",""):"2355";
				
				int Istart_time_sms=(StringUtil.isBlank(start_time_sms))?35:Integer.parseInt(start_time_sms);
				int Istop_time_sms=(StringUtil.isBlank(stop_time_sms))?2355:Integer.parseInt(stop_time_sms);
				int nowHHmm=Integer.parseInt(now.substring(8, 12));
				
				
				runSMS=(nowHHmm<Istart_time_sms || nowHHmm > Istop_time_sms)?false:true; 
				if(!runSMS) {
					LOG.info("本時段不提供發送簡訊服務!!");
				}
			}
			
			
			//MAIL 處理 Begin
			int ialertPeriod=Integer.parseInt(alertPeriod);
			
			//Calendar calendar = Calendar.getInstance();
			//取得n分鐘前時間			
			//calendar.add(Calendar.MINUTE, ialertPeriod * -1);
			//Date MinAgoOfdate = calendar.getTime();
			
			//List<VwHostErrorLog> VwHostErrorLogList = vwHostErrorLogService.getVwHostErrorLogByTrnsDttm(MinAgoOfdate);
			List<VwHostErrorLog> VwHostErrorLogList = vwHostErrorLogService.getVHErrLogByTrnsDttmAlertType(ialertPeriod,"MAIL");
			//不用公司Native SQL Begin
			//List<VwHostErrorLog> VwHostErrorLogs= vwHostErrorLogService.getVHErrLogByTrnsDttm(ialertPeriod);
			//int imailCount=VwHostErrorLogs.size();
			//不用公司Native SQL End
			int imailCount= VwHostErrorLogList.size();
			int ialertFrequency=Integer.parseInt(alertFrequency);
			LOG.info("發送MAIL的條件是 ["+alertPeriod+"] 分鐘內,發生 ["+alertFrequency+"] 次的主機交易失敗!!");
			LOG.info("過去 ["+alertPeriod+"] 分鐘內,發生 ["+imailCount+"] 次的主機交易失敗!!");
			if(ialertFrequency>imailCount) {
			  LOG.info("未達需通知相關人員標準,不用寄送交易失敗次數過多通知郵件");	
			}else {
			  LOG.info("已達需通知相關人員標準,需寄送交易失敗次數過多通知郵件");	
			  
			  String emailAdrs = getSysParm("ALERT_MAIL_RECEIVER");
			  // 發送email
				String subject = "交易失敗次數過多通知";
				String templateFile = "AlertErr.vm";

				HashMap mailMap = new HashMap();
				
				
				
				List<Map<String,Object>> LIST=new ArrayList<Map<String,Object>>();
				
				
				
				for (Iterator<VwHostErrorLog> it = VwHostErrorLogList.iterator(); it.hasNext();) {
					VwHostErrorLog VHEL = (VwHostErrorLog) it.next();
					//LOG.info(VHEL.getId().getEcId()+"-"+VHEL.getId().getEcMsgNo());
					
					HashMap<String, Object> vhelHashMap = new HashMap<String, Object>();
					vhelHashMap.put("EC_ID", VHEL.getId().getEcId());
					vhelHashMap.put("EC_MSG_NO", VHEL.getId().getEcMsgNo());
					vhelHashMap.put("TRNS_DTTM", VHEL.getTrnsDttm());
					vhelHashMap.put("HOST_CODE", VHEL.getHostCode());
					vhelHashMap.put("CODE_DESC", VHEL.getCodeDesc());
					LIST.add(vhelHashMap);
					
					
					
				}	
				
				//不用公司Native SQL Begin
				//mailMap.put("LIST", VwHostErrorLogs);
				//不用公司Native SQL End
				//所以要自己組LIST
				mailMap.put("LIST", LIST);
				

				String mailContent = MailUtil.renderMailHtmlContent(mailMap, templateFile);
				new MailUtil().addMail(mailContent, subject, emailAdrs, null, null, null);
							
				for (Iterator<VwHostErrorLog> it = VwHostErrorLogList.iterator(); it.hasNext();) {
					VwHostErrorLog VHEL = (VwHostErrorLog) it.next();
					saveToAlertData(VHEL, "MAIL"); //存入AlertData Table
				}
								
				/* 不用公司Native SQL Begin
				 * for (Object key : mailMap.keySet()) { LOG.info(key + " : " +
				 * mailMap.get(key)); LinkedList list2 = (LinkedList) mailMap.get(key); int size
				 * = list2.size(); for (int i = 0; i < size; i++) { LOG.info(list2.get(i)); } }
				 * 不用公司Native SQL End
				 */
				 
			    
			  
			}
			//MAIL 處理 End
			
			//SMS簡訊處理 Begin
			if ("ON".equals(alertSMSStatus) && runSMS) {
				String SMSBeginContent="AccountLink ";
				String SMSEndContent="次，請儘速處理";
				boolean SendSMSOK=false;
				//Calendar calendarSMS = Calendar.getInstance();
				//calendarSMS.add(Calendar.MINUTE, ialertSMSPeriod * -1);
				//Date MinAgoOfdateSMS = calendarSMS.getTime();
				//List<VwHostErrorLog> VwHostErrorLogListSMS = vwHostErrorLogService.getVwHostErrorLogByTrnsDttm(MinAgoOfdateSMS);
				List<VwHostErrorLog> VwHostErrorLogListSMS = vwHostErrorLogService.getVHErrLogByTrnsDttmAlertType(ialertSMSPeriod,"SMS");
				int iSMSCount= VwHostErrorLogListSMS.size();
				LOG.info("發送SMS的條件是 ["+alertSMSPeriod+"] 分鐘內,發生 ["+alertSMSFrequency+"] 次的主機交易失敗!!");
				LOG.info("過去 ["+alertSMSPeriod+"] 分鐘內,發生 ["+iSMSCount+"] 次的主機交易失敗!!");
				if(ialertSMSFrequency>iSMSCount) {
				  LOG.info("未達需通知相關人員標準,不用傳送交易失敗次數過多通知簡訊!!");	
				}else {
				  LOG.info("已達需通知相關人員標準,需寄送交易失敗次數過多通知簡訊!!");
					/*
					 * for (Iterator<VwHostErrorLog> it = VwHostErrorLogListSMS.iterator();
					 * it.hasNext();) { VwHostErrorLog VHEL = (VwHostErrorLog) it.next();
					 * SMSBeginContent=SMSBeginContent+"["+VHEL.getHostCode()+"]"+VHEL.getCodeDesc()
					 * ; }
					 */
				  VwHostErrorLog lastError=VwHostErrorLogListSMS.get(iSMSCount-1);
				  String lastErrorMsg=(StringUtil.isBlank(lastError.getCodeDesc()))?"":lastError.getCodeDesc().trim().replace("　", "");
				  SMSBeginContent=SMSBeginContent+"["+lastError.getHostCode()+"]"+lastErrorMsg+
				                               ", 錯誤合計已達"+String.valueOf(iSMSCount)+SMSEndContent;
				  LOG.info(SMSBeginContent);
				  List<String> alertSMSTelnoList =  new ArrayList<String>(Arrays.asList(alertSMSTelno.split("\\s*,\\s*")));
				  for (Iterator<String> telno=alertSMSTelnoList.iterator(); telno.hasNext();) {
					  String SMSTelno=(String) telno.next();
					  LOG.info("傳送簡訊給電話號碼:["+SMSTelno+"]"); 
					  try {
				        send(SMSBeginContent,SMSTelno);
				        SendSMSOK=true;
					  }catch (Exception e) {
							LOG.error("傳送簡訊給 ["+SMSTelno+"] 發生 Exception:", e);
							SendSMSOK=false;
					  } 
				  }
					if (SendSMSOK) {
						for (Iterator<VwHostErrorLog> it = VwHostErrorLogListSMS.iterator(); it.hasNext();) {
							VwHostErrorLog VHEL = (VwHostErrorLog) it.next();
							saveToAlertData(VHEL, "SMS"); //存入AlertData Table
						}
					}
			    }
			}
			//SMS簡訊處理 End
			
		}catch (Exception e) {
			LOG.error("AlertJob Exception:", e);
		} finally {
			LOG.info("交易失敗次數過多通知批次  結束....");
			LOG.info("======================================================");
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
	
	public static OTPReceive[] send(String SMSContent,String SMSTelNo) throws Exception {
        OTPSend data = new OTPSend();
        //Base64.Encoder encoder = Base64.getEncoder();
        TEMPLATEVARData tmp = new TEMPLATEVARData();
        tmp.smsdate = DateUtil.getCurrentTime("D", "AD");
        tmp.smstime = DateUtil.getCurrentTime("T", "AD");
        tmp.purpose = "ACLINK錯誤通知";
        tmp.category = "ACLINK";
        tmp.Hostcode = "056";
        tmp.brtno = "056";
        tmp.Userid = "A1234567890";
        tmp.Accno = "12345678";
        tmp.Username = "12345678";
        tmp.Hostno = "12345678";
        data.UID = SMSUSER;
        //String tmpstr = "succ11d!";
        //byte[] strByte = tmpstr.getBytes(SMSENCDOE);
        //data.Pwd = encoder.encodeToString(strByte);
        data.Pwd = SMSMEMA;
        data.DA = SMSTelNo;
        data.SM = SMSContent;
        data.BUSINESSCODE ="";
        data.STOPTIME = "";
        data.AParty = "";
        //LOG.info(tmp.ToJson());
        data.TEMPLATEVAR = URLEncoder.encode(tmp.ToJson(), SMSENCDOE);
        LOG.info(tmp.ToJson());
        LOG.info(data);
        return OTPSend.sendService(data);
	}
	
	
	public void saveToAlertData(VwHostErrorLog VHEL, String alertType) {
		try {
			AlertData ADT = new AlertData();
			// com.hitrust.acl.model.base.AbstractAlertData.Id
			Id ADTID = new Id();
			ADTID.setEcId(VHEL.getId().getEcId());
			ADTID.setEcMsgNo(VHEL.getId().getEcMsgNo());
			ADTID.setAlertType(alertType);
			ADT.setId(ADTID);
			ADT.setOrdrNo(VHEL.getOrdrNo());
			ADT.setTrnsDttm(VHEL.getTrnsDttm());
			if (ADT != null) {
				alertDataService.insertAlertData(ADT);
			}
		} catch (Exception e) {
			LOG.error("Insert AlertData fail with Exception:", e);
		}
	}
	
}
