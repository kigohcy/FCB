/**
 * @(#) ACLinkListQueryService.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 查詢可使用連結帳戶Service
 * 
 * Modify History:
 *  v1.00, 2016/03/30, Yann
 *   1) First release
 *  v1.01, 2016/11/21, Yann
 *   1) TSBACL-133, 000400下行 CURT_BAL_AMT改取AVAIL_BAL
 *  
 */
package com.hitrust.bank.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.hitrust.acl.APSystem;
import com.hitrust.acl.exception.ApplicationException;
import com.hitrust.acl.exception.DBException;
import com.hitrust.acl.response.AbstractResponseBean;
import com.hitrust.acl.service.AbstractServiceModel;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.common.CommonUtil;
import com.hitrust.bank.common.TbCodeHelper;
import com.hitrust.bank.dao.beans.CustAcntLink;
import com.hitrust.bank.dao.beans.CustData;
import com.hitrust.bank.dao.beans.CustPltf;
import com.hitrust.bank.dao.beans.DayCrdtCont;
import com.hitrust.bank.dao.beans.EcData;
import com.hitrust.bank.dao.beans.MnthCrdtCont;
import com.hitrust.bank.dao.home.CustAcntLinkHome;
import com.hitrust.bank.dao.home.CustDataHome;
import com.hitrust.bank.dao.home.CustPltfHome;
import com.hitrust.bank.dao.home.DayCrdtContHome;
import com.hitrust.bank.dao.home.EcDataHome;
import com.hitrust.bank.dao.home.MnthCrdtContHome;
import com.hitrust.bank.response.ACLinkListQueryResBean;
import com.hitrust.bank.telegram.TelegramBo;
import com.hitrust.bank.telegram.res.MemberMsgResponseInfo;

public class ACLinkListQueryService extends AbstractServiceModel {
    // log4j
    static Logger LOG = Logger.getLogger(ACLinkListQueryService.class);
    
    /**
     * 
     * @param service
     * @param request
     * @param response
     */
    public ACLinkListQueryService(String service, String operation, AbstractResponseBean resBean, HttpServletRequest request, HttpServletResponse response) {
        super(service, operation, resBean, request, response);
    }
    
    /**
     * 
     */
    public AbstractResponseBean process() throws ApplicationException, DBException {
    	
    	LOG.info(DateUtil.formateDateTime(DateUtil.getCurrentTime("DT", "AD"))+" 開始執行查詢可使用連結帳戶  ["+operation+"]");
    	
        Connection conn = null;
        ACLinkListQueryResBean resBean = null;
        String rtnCode = "9999";
        
        //訊息序號
        String msgNo    = request.getParameter("MSG_NO");
        //平台代碼
        String ecId     = request.getParameter("EC_ID");
        //平台會員代號
        String ecUser   = request.getParameter("EC_USER");
        //使用者身分證號
        String custId   = request.getParameter("CUST_ID");
        //簽章日期時間
        String signTime = request.getParameter("SIGN_TIME");
        //簽章值
        String signValue= request.getParameter("SIGN_VALUE");
        //憑證序號
        String certSn   = request.getParameter("CERT_SN");
        
        String ecStts   = "";
        String custStts = "";
        
        try {
        	resBean = (ACLinkListQueryResBean) this.resBean;
        	resBean.setMSG_NO(msgNo);
        	resBean.setEC_ID(ecId);
        	resBean.setEC_USER(ecUser);
        	
        	conn = APSystem.getConnection(APSystem.DB_ACLINK);
        	
        	DatabaseMetaData mtdt = conn.getMetaData();
			LOG.info("DB 使用者: [" + mtdt.getUserName()+"]");
			LOG.info("用戶IP:["+request.getRemoteAddr()+"]");
			LOG.info("用戶電腦名稱:["+request.getRemoteHost()+"]");
        	
        	// 取得 會員平台服務資料
        	String pltfStts = ""; // 會員平台服務狀態
        	CustPltf pltf = null;
        	CustPltfHome pltfHome = new CustPltfHome(conn);
        	
        	//取得 電商平台資料檔
        	EcDataHome ecDataHome = new EcDataHome(conn);
        	EcData ecData = ecDataHome.fetchEcDataByKey(ecId);
        	if(ecData==null){
        		rtnCode = "5011";
        		resBean.setRTN_CODE(rtnCode);
        		resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
        		return resBean;
        	}else{
        		ecStts = ecData.STTS;
        	}
        	
        	//取得 會員資料檔
        	CustDataHome custDataHome = new CustDataHome(conn);
        	CustData custData = custDataHome.fetchCustDataByKey(custId);
        	if(custData != null){
        		custStts = custData.STTS;
        	}
        	
        	//檢查該客戶是否已開通
        	if(custData==null || "02".equals(custStts)){
        		rtnCode = "5002";
        		resBean.setRTN_CODE(rtnCode);
        		resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
        		return resBean;
        	}
        	
        	//取得 會員帳號連結檔
        	CustAcntLinkHome custAcntLinkHome = new CustAcntLinkHome(conn);
        	CustAcntLink[] custAcntLinks = custAcntLinkHome.getCustAllRealAcnt(custId, ecId, ecUser);
        	if(custAcntLinks==null || custAcntLinks.length==0){
        		rtnCode = "5021";
        		resBean.setRTN_CODE(rtnCode);
        		resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
        		return resBean;
        	}
        	
        	String today = DateUtil.getToday();
        	
        	DayCrdtContHome dayCrdtContHome = new DayCrdtContHome(conn);
        	MnthCrdtContHome mnthCrdtContHome = new MnthCrdtContHome(conn);
        	DayCrdtCont dayCrdtCont = null;
        	MnthCrdtCont mnthCrdtCont = null;
        	
        	List list = new ArrayList();
        	
        	//取得該客戶的各服務狀態，並檢核是否可執行 帳號綁定查詢 處理
        	for (int i=0; i<custAcntLinks.length; i++) {
        		Map detl = new HashMap();
        		detl.put("INDT_ACNT", custAcntLinks[i].ACNT_INDT); //帳號識別碼
        		detl.put("ACNT_STTS", custAcntLinks[i].STTS.substring(1)); //狀態
        		detl.put("AVA_BALANCE" , ""); //可用餘額
        		detl.put("ACNT_BALANCE", ""); //存款餘額
        		
        		//
        		long D_AVA_BALANCE = 0; //日可用餘額
        		long M_AVA_BALANCE = 0; //月可用餘額
        		long DAY_CONT = 0;  //日累計金額
        		long MNTH_CONT= 0;  //月累計金額
        		String linkStts = custAcntLinks[i].STTS;
        		String acntIndt = custAcntLinks[i].ACNT_INDT;
        		pltf = pltfHome.fetchCustPltfByKey(custAcntLinks[i].EC_ID, custAcntLinks[i].CUST_ID);
        		
        		if (!StringUtil.isBlank(pltf)) {
					pltfStts = pltf.STTS;
				}
        		
        		String msg = CommonUtil.checkStatus("04", ecStts, custStts, pltfStts, linkStts);
        		if(StringUtil.isBlank(msg)){
        			detl.put("AVA_BALANCE" , "0"); //可用餘額
        			
        			//
        			if(custAcntLinks[i].DAY_LIMT > 0){
        				//取得 日額度累計檔
            			dayCrdtCont = dayCrdtContHome.getDayCrdtContByPk(acntIndt, today);
            			if(dayCrdtCont != null){
            				DAY_CONT = (int)dayCrdtCont.DAY_CONT;
            			}
            			//日可用餘額 = 每日自訂限額-日累計金額
            			D_AVA_BALANCE = custAcntLinks[i].DAY_LIMT - DAY_CONT;
        			}else{
        				D_AVA_BALANCE = 999999999;
        			}
        			//
        			if(custAcntLinks[i].MNTH_LIMT > 0){
        				//取得 月額度累計檔
            			mnthCrdtCont= mnthCrdtContHome.getMnthCrdtContByPk(acntIndt, today.substring(0,6));
            			if(mnthCrdtCont != null){
            				MNTH_CONT = (int)mnthCrdtCont.MNTH_CONT;
            			}
            			//月可用餘額 = 每月自訂限額-月累計金額
            			M_AVA_BALANCE = custAcntLinks[i].MNTH_LIMT - MNTH_CONT;
        			}else{
        				M_AVA_BALANCE = 999999999;
        			}
        			//比較 日可用餘額、月可用餘額 兩者取較小值者回存至 可用餘額
        			if(D_AVA_BALANCE < M_AVA_BALANCE){
        				detl.put("AVA_BALANCE", String.valueOf(D_AVA_BALANCE));
        			}else{
        				detl.put("AVA_BALANCE", String.valueOf(M_AVA_BALANCE));
        			}
        			
        			//jeff-修改 一銀無餘額查詢電文..start
//        			//TODO 逐筆發送 [000400 存款明細短查詢] 電文至主機取得帳戶餘額
//        			Host000400RequestInfo req000400 = new Host000400RequestInfo();
//        			req000400.setCIF_NO(custAcntLinks[i].REAL_ACNT); //客戶實體帳號
//        			//發送電文
//                	try{
////                		Host000400ResponseInfo res000400 = (Host000400ResponseInfo) AclTelegramBo.sendTelegram(req000400, AclTelegramBo.A000400);
//                		Host000400ResponseInfo res000400 = (Host000400ResponseInfo) new TelegramBo().sendTelegram("000400", "000400", req000400);
//                		LOG.debug("OUTPUT_CODE="+res000400.getOUTPUT_CODE());
//                		LOG.debug("STATUS="+res000400.getSTATUS());
//                		LOG.debug("AVAIL_BAL="+res000400.getAVAIL_BAL());
//                		if("03".equals(res000400.getOUTPUT_CODE())){  //成功
//                			if("正常".equals(res000400.getSTATUS().trim())){ //內容值‘正常’才視為正確資料,其餘為異常資料
//                				String AVAIL_BAL = res000400.getAVAIL_BAL(); //v1.01,改取AVAIL_BAL
//                				int amt = Integer.parseInt(AVAIL_BAL);
//                				detl.put("ACNT_BALANCE", String.valueOf(amt)); //餘額
//                			}
//                		}
//                	}catch(Exception e){
//                		LOG.error("Exception:"+ e.toString(), e);
//                	}
        			//jeff-修改 一銀無餘額查詢電文..end
        			
        		}
        		list.add(detl);
        	}//end loop
    		
        	//
        	rtnCode = "0000";
        	resBean.setACNT_INFO(list);
        	resBean.setRTN_CODE(rtnCode);
        	resBean.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
            return resBean;
        	
        } catch (DBException e) {
        	LOG.error("DBException:"+e.toString(), e);
            throw e;
        } catch (Exception e) {
        	LOG.error("Exception:"+e.toString(), e);
        	throw new ApplicationException(e, "SYS_ERR");
        } finally {
            if (conn != null) {
                APSystem.returnConnection(conn, APSystem.DB_ACLINK);
            }
            LOG.info(DateUtil.formateDateTime(DateUtil.getCurrentTime("DT", "AD"))+" 結束執行查詢可使用連結帳戶  ["+operation+"]");
        }
    }
}
