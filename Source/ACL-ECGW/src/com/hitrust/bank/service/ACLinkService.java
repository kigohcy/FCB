/**
 * @(#) ACLinkService.java
 *
 * Directions: 帳號綁定連結
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *	v1.00, 2016/04/07, Eason Hsu
 *	 1) First release
 *	v1.01, 2016/10/28, Eason Hsu
 *	 1) TSBACL-122, 平台閘道、會員平台，同意條款 PDF 版本號依據 DB 參數(PRVS_VRSN)取得
 *	v1.02, 2016/11/04, Eason Hsu
 *	 1) TSBACL-123, 平台閘道 連結綁定完成後回覆給電商訊息中實體帳號未遮蔽
 *	v1.03, 2016/11/18, Eason Hsu
 *	 1) TSBACL-130, 平台閘道, 執行 OTP 密碼確認後, 確認頁未顯示已選擇綁定帳號
 *  v1.04, 2016/11/18, Eason Hsu
 *   1) TSBACL-131, 同一組 OTP 驗證碼不可重覆驗證 
 *  v1.05, 2016/11/21, Ada Chen
 *   1) TSBACL-132, 綁定之存款帳戶排除清單增加「1604」
 *  v1.06, 2016/11/29, Ada Chen
 *   1) TSBACL-134, 1045 電文回應錯誤時, 錯誤代碼拆解錯誤
 *  v1.07, 2016/12/01, Eason Hsu
 *   1) TSBACL-136, 連結綁定帳號 OTP 驗證流程調整
 *  v1.08, 2016/12/02, Eason Hsu
 *   1) TSBACL-137, ECGW 綁定連結帳號驗證方式調整
 *  v1.09, 2016/12/02, Ada Chen
 *   1) TSBACL-138, 調整錯誤代碼拆解順序
 *  v1.10, 2016/12/23, Eason Hsu
 *   1) TSBACL-144, 網銀 & 晶片卡認證失敗, 錯誤訊息 Mapping 調整
 *   V2.00, 2018/01/25
 *   1) 網頁增加顯示電話及email資訊
 *   V2.01, 2018/02/05
 *   1) 新增可設定電商最大可綁定人數(先借用 EC_DATA.NOTE 欄位)，超過綁定人數，則回覆1999-超過電商最大可綁定人數
 *   V2.02, 2018/3/19
 *   1) 新增可設定電商最大可綁定人數(改抓EC_DATA.LINK_LIMIT 欄位)，超過綁定人數，則回覆1999-超過電商最大可綁定人數
 *   2) 紀錄IP到CUST_ACNT_LOG
 */

package com.hitrust.bank.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import com.hitrust.acl.APSystem;
import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.common.TransactionControl;
import com.hitrust.acl.common.UUIDGen;
import com.hitrust.acl.exception.ApplicationException;
import com.hitrust.acl.exception.DBException;
import com.hitrust.acl.exception.UtilException;
import com.hitrust.acl.mail.MailUtil;
import com.hitrust.acl.response.AbstractResponseBean;
import com.hitrust.acl.service.AbstractServiceModel;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.acl.util.MathUtil;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.bean.Accounts;
import com.hitrust.bank.bean.BindingAcnt;
import com.hitrust.bank.common.AuthenticationHelper;
import com.hitrust.bank.common.CommonUtil;
import com.hitrust.bank.common.TbCodeHelper;
import com.hitrust.bank.common.WSResBean;
import com.hitrust.bank.dao.beans.BaseLimt;
import com.hitrust.bank.dao.beans.CustAcnt;
import com.hitrust.bank.dao.beans.CustAcntLink;
import com.hitrust.bank.dao.beans.CustAcntLog;
import com.hitrust.bank.dao.beans.CustData;
import com.hitrust.bank.dao.beans.CustPltf;
import com.hitrust.bank.dao.beans.EcData;
import com.hitrust.bank.dao.beans.SessionTempTable;
import com.hitrust.bank.dao.beans.SysParm;
import com.hitrust.bank.dao.beans.TbCode;
import com.hitrust.bank.dao.home.BaseLimtHome;
import com.hitrust.bank.dao.home.CustAcntHome;
import com.hitrust.bank.dao.home.CustAcntLinkHome;
import com.hitrust.bank.dao.home.CustDataHome;
import com.hitrust.bank.dao.home.CustPltfHome;
import com.hitrust.bank.dao.home.EcDataHome;
import com.hitrust.bank.dao.home.MsgSourHome;
import com.hitrust.bank.dao.home.SessionTempTableHome;
import com.hitrust.bank.dao.home.SysParmHome;
import com.hitrust.bank.json.ACLink;
import com.hitrust.bank.json.LoginResBean;
import com.hitrust.bank.response.ACLinkResBeanOne;
import com.hitrust.bank.response.ACLinkResBeanTwo;
import com.hitrust.bank.response.IBankLoginCheckResBean;
import com.hitrust.bank.telegram.TelegramBo;
import com.hitrust.bank.telegram.res.MemberMsgResponseInfo;
import com.hitrust.bank.telegram.res.NotificationHostMsgResponseInfo;

import ni.otp.fb.Proc00F8;

import com.hitrust.bank.telegram.res.AccountListRecord;
import com.hitrust.bank.telegram.res.AccountListResponseInfo;

public class ACLinkService extends AbstractServiceModel {
    
	// Log4j
    private static Logger LOG = Logger.getLogger(ACLinkService.class);
    
    public ACLinkService(String service, String operation, AbstractResponseBean resBean, HttpServletRequest request, HttpServletResponse response) {
        super(service, operation, resBean, request, response);
    }
    
    public AbstractResponseBean process() throws ApplicationException, DBException {
        
    	LOG.info(DateUtil.formateDateTime(DateUtil.getCurrentTime("DT", "AD"))+" 開始執行連結帳戶綁定  ["+operation+"]");
    	
    	Connection conn = null;
    	Connection msgConn = null;
        
        AbstractResponseBean resBesn = null;
        try {
        	conn = APSystem.getConnection(APSystem.DB_ACLINK);
        	
        	DatabaseMetaData mtdt = conn.getMetaData();
			LOG.info("DB 使用者: [" + mtdt.getUserName()+"]");
			LOG.info("用戶IP:["+request.getRemoteAddr()+"]");
			LOG.info("用戶電腦名稱:["+request.getRemoteHost()+"]");
        	
        	LOG.debug("Tel and Mail Debug ...... operation="+operation);
        	if ("checkCustStts".equals(this.operation)) {
        		// 檢核會員狀態
				resBesn = this.checkCustStts(request, conn);
			} else if ("aclinkLicense".equals(this.operation)) {
				// 初始網銀登入畫面
				resBesn = this.initLogin(request, conn);
			} else if ("aclinkAuth".equals(this.operation)) {
				// 初始網銀登入畫面
				resBesn = this.initLogin(request, conn);				
			} else if ("eBankLogin".equals(this.operation)) {
				// 網銀登入
				//m by jeff for 一銀ibank登入驗證 start
				resBesn = this.eBankLogin(request, conn);
				//m by jeff for 一銀ibank登入驗證 end
			} else if ("eBankLoginTest".equals(this.operation)) {
				// 網銀登入
				resBesn = this.eBankLoginTest(request, conn);
			} else if ("eCardLogin".equals(this.operation)) {
				// 晶片卡登入
				resBesn = this.eCardLogin(request, conn);
			} else if ("cancel".equals(this.operation)) {
				// 使用者取消連結綁定
				resBesn = this.cancel(request, conn);
			} else if ("sendOtpCode".equals(this.operation)) {
				// 產生 OTP
//				msgConn = APSystem.getConnection(APSystem.DB_SMS);
//				this.sendOtpCode(request, conn, msgConn);
				this.sendOtpCode(request, conn);
			} else if ("confirmOTP".equals(this.operation)) {
				// 驗證 OTP
				this.confirmOTP(request);
			} else if ("confirmBindInfo".equals(this.operation)) {
				// 確認綁定連結帳號資訊
				this.confirmBindInfo(request);
			} else if ("confirmSubmit".equals(this.operation)) {
				// 確認送出
				resBesn = this.confirmSubmit(request, conn);
			} 
        	
        } catch (DBException e) {
        	LOG.error("[aclink DBException]: ", e);
            throw e;
            
        } catch (Exception e) {
        	LOG.error("[aclink Exception]: ", e);
            throw new ApplicationException(e, "SYS_ERR");
            
        } finally {
            if (conn != null) {
                APSystem.returnConnection(conn, APSystem.DB_ACLINK);
            }
            
            if (msgConn != null) {
            	APSystem.returnConnection(msgConn, APSystem.DB_SMS);
			}
            
            LOG.info(DateUtil.formateDateTime(DateUtil.getCurrentTime("DT", "AD"))+" 結束執行連結帳戶綁定  ["+operation+"]");
        }
        
        
        
        return resBesn;
    }
    
	/**
     * 檢核會員服務狀態
     * @param req
     * @param conn
     * @return ACLinkResBeanOne
     * @throws DBException 
	 * @throws IOException 
	 * @throws ServletException 
	 * @throws UtilException 
     */
    private AbstractResponseBean checkCustStts(HttpServletRequest req, Connection conn) throws DBException, ServletException, IOException, UtilException {
    	
    	ACLinkResBeanOne resBesn = (ACLinkResBeanOne) this.resBean;
    	
    	String rtnCode = "0000";
    	String rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
    	
    	String msgNo = req.getParameter("MSG_NO");     // 訊息序號
    	String ecId = req.getParameter("EC_ID");	   // 平台代碼
    	String ecUser  = req.getParameter("EC_USER");  // 平台會員代號
		String custId  = req.getParameter("CUST_ID");  // 身分證字號
		String rsltUrl = req.getParameter("RSLT_URL"); // 綁定結果回傳URL
		String succUrl = req.getParameter("SUCC_URL"); // 綁定成功導向頁
		String failUrl = req.getParameter("FAIL_URL"); // 綁定失敗導向頁
		String linkUrl = APSystem.getProjectParam("LINK_URL");
		
		if (!StringUtil.isBlank(this.checkMaxLinkedCount(ecId, custId, conn))) {
			rtnCode="1999";
			resBesn.setRTN_CODE(rtnCode);
     		resBesn.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			return resBesn;
		}
		
		// 產生 sessionKey
		String sessionKey = MathUtil.randomNumberChar(12);
 
		String prvsVrsn = ""; // v1.01, 同意條款版本號

		// 設定回傳值
		resBesn.setMSG_NO(msgNo);
		resBesn.setEC_ID(ecId);
		resBesn.setEC_USER(ecUser);
		resBesn.setS_KEY(sessionKey);
		resBesn.setRTN_CODE(rtnCode);
		resBesn.setRTN_MSG(rtnMsg);
		
		ACLink aclink = new ACLink();
		
		EcData ecData = null;
		CustData custData = null;
		CustPltf pltf = null;
		SysParm parm = null;	// v1.01
		
		EcDataHome ecDataHome = new EcDataHome(conn);
		CustDataHome dataHome = new CustDataHome(conn);
		CustPltfHome pltfHome = new CustPltfHome(conn);
		SysParmHome parmHome = new SysParmHome(conn); // v1.01
		
		// 依據平台代碼取得平台資料
		ecData = ecDataHome.fetchEcDataByKey(ecId);
		
		// 依據身分證字號取得會員資料
		custData = dataHome.fetchCustDataByKey(custId);
		
		// 取得會員平台資料
		pltf = pltfHome.fetchCustPltfByKey(ecId, custId);
		
		// v1.01, 取得同意條款版本號
		parm = parmHome.fetchSysParmByKey("PRVS_VRSN");
		if (!StringUtil.isBlank(parm)) {
			prvsVrsn = parm.PARM_VALUE;
		}
		
		aclink.setMsgNo(msgNo);
		aclink.setEcId(ecId);
		aclink.setEcUser(ecUser);
		aclink.setCustId(custId);
		aclink.setPrvsVrsn(prvsVrsn); // v1.01
		//V2.02 紀錄IP到CUST_ACNT_LOG
		aclink.setIp(req.getRemoteAddr());
		//V2.02 紀錄IP到CUST_ACNT_LOG End
		
		// 檢核平台資料是否存在 檢核平台狀態是否啟用
		if (StringUtil.isBlank(ecData) || !"00".equals(ecData.STTS)) {
			rtnCode="5011";
			resBesn.setRTN_CODE(rtnCode);
     		resBesn.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			this.saveCustAcntLog(aclink, null, rtnCode, "", "", "01", conn, "");
			
			return resBesn;
		}
		
		// 檢核會員平台為啟用或終止
		if (!StringUtil.isBlank(pltf) && "01".equals(pltf.STTS)) {
			rtnCode="5030";
			resBesn.setRTN_CODE(rtnCode);
     		resBesn.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			this.saveCustAcntLog(aclink, null, rtnCode, "", "", "01", conn, "");
			
			return resBesn;
		}
		
		aclink.setRsltUrl(rsltUrl);
		aclink.setSuccUrl(succUrl);
		aclink.setFailUrl(failUrl);
		aclink.setSessionKey(sessionKey);
		aclink.setEcStts(ecData.STTS);
		aclink.setEcName(ecData.EC_NAME_CH);
		aclink.setCustSerl(StringUtil.isBlank(custData) ? "" : custData.CUST_SERL);
		aclink.setCustStts(StringUtil.isBlank(custData) ? "X" : custData.STTS);
		aclink.setPltfStts(StringUtil.isBlank(pltf) ? "X" : pltf.STTS);
		// 晶片卡登入驗證參數
//		aclink.setIframeUrl(APSystem.getProjectParam("IFRAME_URL"));
//		aclink.setSysId(APSystem.getProjectParam("SYSID"));
//		aclink.setParam(this.generateVerifyJSON(custId, sessionKey, conn));
		
//		// 查無會員資料或會員狀態為終止(02), 需轉導至同意條款頁 
//		if (StringUtil.isBlank(custData) || "02".equals(aclink.getCustStts())) {
//			LOG.info("查無會員資料或會員狀態為終止(02), 需轉導至同意條款頁,[service]aclinkLicense");
			resBesn.setLINK_URL(linkUrl + "index.jsp?_service=aclinkLicense&sessionKey=" + sessionKey );		
//		} else {		
//			LOG.info("查會員資料成功,[service]aclinkAuth");
//			resBesn.setLINK_URL(linkUrl + "index.jsp?_service=aclinkAuth&sessionKey=" + sessionKey );
//		}
		
		this.insertTempData(aclink, conn);
		
		return resBesn;
	}
    
    /**
     * 初始網銀登入畫面
     * @param req
     * @param conn
     * @throws DBException 
     */
	private AbstractResponseBean initLogin(HttpServletRequest req, Connection conn) throws DBException {
		ACLinkResBeanTwo resBesn = null;
		
		HttpSession session = this.validateSession(req);
		
		String sessionKey = req.getParameter("sessionKey");
		String tempData = "";
		String userAgent = req.getHeader("user-agent");	// 使用者 Browser user-agent
		String isMobile = "N";							// 是否為行動裝置(預設:N)
		
		LOG.info("========== [User-Agent]: " + userAgent + " ==========");
		
		SessionTempTable tempTable = null;
		SessionTempTableHome home = new SessionTempTableHome(conn);

		ACLink acLink = null;
		
		tempTable = home.fetchSessionDataByKey(sessionKey);
		
		if (StringUtil.isBlank(tempTable)) {
			LOG.info("===== [查無對應 sessionKey 資料:" + sessionKey + " =====");
			
			resBesn = (ACLinkResBeanTwo) this.resBean;
			String rtnCode="5024"; //連結帳號綁定處理異常
			resBesn.setRTN_CODE(rtnCode);
     		resBesn.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			
			return resBesn;
		}
		
		//m by jeff for 一銀ibank登入驗證 start
		LoginResBean loginResBean = this.httpsPost();
		if(StringUtil.isBlank(loginResBean.getTxReqId())) {
			LOG.info("=====個人網銀Login頁面錯誤=====");
			
			resBesn = (ACLinkResBeanTwo) this.resBean;
			String rtnCode="5024"; //連結帳號綁定處理異常
			resBesn.setRTN_CODE(rtnCode);
     		resBesn.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
			
			return resBesn;
		}
		//m by jeff for 一銀ibank登入驗證 end
		
		tempData = tempTable.SESSION_DATA;
		acLink = (ACLink) JsonUtil.json2Object(tempData, ACLink.class);
		
		if ("aclinkAuth".equals(this.operation)) {
			// 刪除 sessionTempTable
			home.deleteSessionTempTableByKey(sessionKey);
		}
		
		// 判斷是否為行動裝置
		if (StringUtil.isBlank(userAgent)) {
			isMobile = "Y"; // 當取不到 http header user-agent 時, 預設為行動裝置
		} else {
			isMobile = (userAgent.indexOf("Mobile") > 0) ? "Y" : isMobile;
		}
		
		acLink.setUserAgent(userAgent);
		acLink.setIsMobile(isMobile);
		//m by jeff for 一銀ibank登入驗證 start
		acLink.setMerchantId(loginResBean.getMerchantId());
		acLink.setTxReqId(loginResBean.getTxReqId());
		acLink.setSign(loginResBean.getSign());
		acLink.setUri0202(loginResBean.getUri0202());
		session.setAttribute("txReqId", loginResBean.getTxReqId());
		//m by jeff for 一銀ibank登入驗證 end
	
		session.setAttribute("link", acLink);
		
		return resBesn;
	}
    
    /**
     * 網銀登入
     * @param req
     * @param conn
     * @return
     * @throws DBException 
     * @throws UtilException 
     */
    private AbstractResponseBean eBankLoginTest(HttpServletRequest req, Connection conn) throws DBException, UtilException {
    	
    	HttpSession session = req.getSession(true);
    	String txReqId = (String) session.getAttribute("txReqId");
    	
    	ACLinkResBeanTwo resBesn = null;
    	ACLink aclink = (ACLink) session.getAttribute("link");
    	
    	String userCode = req.getParameter("userCode");
	String userPaswd = req.getParameter("userPaswd");

		// ========== v1.10, 網銀身份驗證 ==========
		resBesn = this.verifyIdentity(aclink, userCode, userPaswd, conn);
		if (!StringUtil.isBlank(resBesn)) {
			return resBesn;
		}
		
//		String rtnCode = (String) ibankLoginMap.get(txReqId);
//		String rtnMsg = "";
//		if(StringUtil.isBlank(rtnCode)) {
//			rtnCode="9205";
//		}else if("0000".equals(rtnCode)) {
//			rtnCode="";
//		}else {
//			rtnCode="9200";
//		}
//			
//		rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
//		resBesn = this.compseResponseObj(rtnCode, rtnMsg, aclink);
		
		// ========== 依據 身分證字號 取得帳號資訊 ==========
		resBesn = this.fetchAcntInfo(aclink, session, conn);
		// V2.00, 2018/01/25，網頁增加顯示電話及email資訊  Begin
		LOG.debug("eBankLoginTest:Tel and Mail Debug ...... Tel="+aclink.getTlxNo());
		LOG.debug("eBankLoginTest:Tel and Mail Debug ...... email="+aclink.getEmailAddr());
		//將取得的電話及email 資訊異動到session ACLink.ACLINK 物件中，讓前端網頁可以取得到值
		session.setAttribute(ACLink.ACLINK, aclink);
		// V2.00, 2018/01/25，網頁增加顯示電話及email資訊  End
		if (!StringUtil.isBlank(resBesn)) {
			return resBesn;
		}
		
		aclink.setIdetityAuthType("00"); // 身分認證方式 00: 網銀, 01: 簡訊 OTP, 02: 晶片金融卡 
		// v1.08
		aclink.setLinkAcnt("");
		session.setAttribute(ACLink.ACLINK, aclink);
		
		SysParmHome sysParmHome = new SysParmHome(conn);
    		SysParm sysParm = sysParmHome.fetchSysParmByKey("OTP_EXPR_TIME");
		session.setAttribute("iOriginalTimeout", sysParm.PARM_VALUE);
		
		return resBesn;
	}
    
    /**
     * 網銀登入
     * @param req
     * @param conn
     * @return
     * @throws DBException 
     * @throws UtilException 
     */
    private AbstractResponseBean eBankLogin(HttpServletRequest req, Connection conn) throws DBException, UtilException {
    	
    	HttpSession session = req.getSession(true);
    	String txReqId = (String) session.getAttribute("txReqId");
    	
    	ACLinkResBeanTwo resBesn = null;
    	ACLink aclink = (ACLink) session.getAttribute("link");

    	String rtnMsg = "";
//		String returnCustId = IBankLoginChecService.ibankLoginMap(txReqId);
    	SysParmHome sysParmHome = new SysParmHome(conn);
    	if(txReqId.length()>=30) {
			txReqId = txReqId.substring(0, 30);
		}
    	SysParm sysParm = sysParmHome.fetchSysParmByName(txReqId);
    	sysParmHome.delete(sysParm);
    	
		// 檢核會員身份別是否為APP登入相同
		if (sysParm != null) {
			if(!aclink.getCustId().equals(sysParm.PARM_NAME)) {
				rtnMsg = "會員身份錯誤";
				String rtnCode = "9200";
				
				LOG.info("========== web service 身分認證檢核失敗: " + rtnMsg + "(街口登入ID="+aclink.getCustId()+"/ 網銀登入ID="+sysParm.PARM_NAME+")"+" ==========");
				
				rtnMsg = new TbCodeHelper("9200", "01").getTbCodeMsg();
				resBesn = this.compseResponseObj("9200", rtnMsg, aclink);
				return resBesn;
			}
		}else {
			rtnMsg = "會員身份錯誤";
			String rtnCode = "9200";
			
			LOG.info("========== web service 身分認證檢核失敗: DB撈取資料異常==========");
			
			rtnMsg = new TbCodeHelper("9200", "01").getTbCodeMsg();
			resBesn = this.compseResponseObj("9200", rtnMsg, aclink);
			return resBesn;
		}
			
		rtnMsg = new TbCodeHelper("0000", "01").getTbCodeMsg();
		resBesn = this.compseResponseObj("0000", rtnMsg, aclink);
		
		// ========== 依據 身分證字號 取得帳號資訊 ==========
		resBesn = this.fetchAcntInfo(aclink, session, conn);
		// V2.00,2017/01/25，網頁增加顯示電話及email資訊  Begin
		LOG.debug("eBankLogin:Tel and Mail Debug ...... Tel="+aclink.getTlxNo());
		LOG.debug("eBankLogin:Tel and Mail Debug ...... email="+aclink.getEmailAddr());
		//將取得的電話及email 資訊異動到session ACLink.ACLINK 物件中，讓前端網頁可以取得到值
		session.setAttribute(ACLink.ACLINK, aclink);
		// V2.00,2017/01/25，網頁增加顯示電話及email資訊  End
		
		if (!StringUtil.isBlank(resBesn)) {
			return resBesn;
		}
		
		aclink.setIdetityAuthType("00"); // 身分認證方式 00: 網銀, 01: 簡訊 OTP, 02: 晶片金融卡 
		// v1.08
		aclink.setLinkAcnt("");
		session.setAttribute(ACLink.ACLINK, aclink);
		
//		SysParmHome sysParmHome = new SysParmHome(conn);
//    	SysParm sysParm = sysParmHome.fetchSysParmByKey("OTP_EXPR_TIME");
		sysParm = sysParmHome.fetchSysParmByKey("OTP_EXPR_TIME");
		session.setAttribute("iOriginalTimeout", sysParm.PARM_VALUE);
		
		return resBesn;
	}
    
    /**
     * 晶片卡登入
     * @param req
     * @param conn
     * @return AbstractResponseBean
     * @throws DBException 
     * @throws UtilException 
     */
    private AbstractResponseBean eCardLogin(HttpServletRequest req, Connection conn) throws DBException, UtilException {
    	
    	HttpSession session = req.getSession(true);

    	ACLinkResBeanTwo resBesn = null;
    	ACLink aclink = (ACLink) session.getAttribute("link");
    	
    	String input = req.getParameter("param"); // 解密前 JSON 字串
    	
    	// v1.10
    	aclink.setIdetityAuthType("02"); // 身分認證方式 00: 網銀, 01: 簡訊 OTP, 02: 晶片金融卡 
    	
		// ========== 晶片卡驗證結果解密 ==========
		resBesn = this.decryptVerifyJSON(input, aclink, conn);
		if (!StringUtil.isBlank(resBesn)) {
			return resBesn;
		}
		
		// ========== 依據 身分證字號 取得帳號資訊 ========== 
		resBesn = this.fetchAcntInfo(aclink, session, conn);
		if (!StringUtil.isBlank(resBesn)) {
			return resBesn;
		}
		
		session.setAttribute("link", aclink);
		
		SysParmHome sysParmHome = new SysParmHome(conn);
    	SysParm sysParm = sysParmHome.fetchSysParmByKey("OTP_EXPR_TIME");
		session.setAttribute("iOriginalTimeout", sysParm.PARM_VALUE);
		
		return resBesn;
    }

	/**
     * 驗證網銀身分登入
     * @param custId
     * @param userCode
     * @param userPaswd
     * @param conn
     * @return
	 * @throws DBException 
	 * @throws UtilException 
     */
    private ACLinkResBeanTwo verifyIdentity(ACLink link, String userCode, String userPaswd, Connection conn) throws UtilException, DBException {
    	
    	ACLinkResBeanTwo resBesn = null;
    	
    	String rtnMsg = "";
    	String verifyRslt = ""; // 驗證結果
    	String custType = ""; 	// 身份別 11: 本國人, 12: 外藉個人, 13: 大陸人士
    	String errorMsg = "";	// 網銀錯誤訊息
    	String rtnCode = "";	// 錯誤代碼
    	String hostCode = "";	// 
    	
    	List<String> typeList = Arrays.asList("11", "12", "13");

    	AuthenticationHelper authHelper = null;
    	WSResBean resBean = null;
    	
    	try {
    		authHelper = new AuthenticationHelper();
    		resBean = authHelper.authenticationLogin(link.getCustId(), userCode, userPaswd);
    		
    		verifyRslt = resBean.getResult();
    		errorMsg = resBean.getErrorMsg(); // 取得網銀錯誤訊息
    		
    		custType = resBean.getType();
    		LOG.info("[verifyRslt]: " + verifyRslt + ", [authenticationLogin TYPE]: " + custType);
    		
    		// v1.10, 網銀帳號認證失敗
    		if (!"1".equals(verifyRslt)) {
    			
    			if ("2".equals(verifyRslt)) { // 網銀帳號為首次登入
    				rtnCode = "9205";
    				hostCode = "E015";
    				
				} else if ("5".equals(verifyRslt)) { // 查無帳號資料
					rtnCode = "9206";
    				hostCode = "E016";
    				
				} else {
					
					TbCode tbCode = null;
					TbCodeHelper codeHelper = new TbCodeHelper();
					
					tbCode = codeHelper.fetchTbCodeByErrorMsg(errorMsg);
					
					if (!StringUtil.isBlank(tbCode)) {
						rtnCode = tbCode.REF_CODE_ID.substring(3);
						hostCode = tbCode.CODE_ID.substring(3);
						
					} else {
						LOG.warn("[查無對應網銀訊息]: " + errorMsg);
						rtnCode = "9200";
					}
					
				}
    		
				rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
				resBesn = this.compseResponseObj(rtnCode, rtnMsg, link);
				// 新增連結紀錄 log 並發 mail
				this.saveCustAcntLog(link, null, rtnCode, "", "", "01", conn, hostCode);
				
				return resBesn;
    		}
    		
    		// 會員身份別為空白時, 身份別預設為 11
    		if ("".equals(custType)) {
				custType = "11";
			}
    		
    		// 檢核會員身份別是否為 11, 12, 13
    		if (!typeList.contains(custType)) {
    			rtnMsg = "會員身份別錯誤";
    			rtnCode = "9200";
    			
    			LOG.info("========== web service 身分認證檢核失敗: " + rtnMsg + " ==========");
    			
    			rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
				resBesn = this.compseResponseObj(rtnCode, rtnMsg, link);
				
				// 新增連結紀錄 log 並發 mail
				this.saveCustAcntLog(link, null, rtnCode, "", "", "01", conn, hostCode);
				return resBesn;
				
			}
    		
    		link.setIdentityType(custType);
    		
		} catch (Exception e) {
			rtnMsg = e.getMessage();
			rtnCode = "9200";
			
			LOG.info("========== web service 身分認證檢核失敗: " + rtnMsg + " ==========");
			
			rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
			resBesn = this.compseResponseObj(rtnCode, rtnMsg, link);
			
			// 新增連結紀錄 log 並發 mail
			this.saveCustAcntLog(link, null, rtnCode, "", "", "01", conn, hostCode);
			return resBesn;
			
		}
    	
    	return resBesn;
    }
    
    /**
     * 檢核各服務狀態
     * @param aclink
     * @param conn
     * @return
     * @throws DBException
     */
    private String checkServiceStts(ACLink aclink, Connection conn) throws DBException {
    	String rtnMsg = "";

    	String ecDataStts = "";
    	String custDataStts = "";
    	
    	EcData ecData = null;
    	CustData custData = null;
    	
    	EcDataHome ecDataHome = new EcDataHome(conn);
    	CustDataHome custDataHome = new CustDataHome(conn); 
    	
    	// 依據平台代碼, 取得電商平台資料
    	ecData = ecDataHome.fetchEcDataByKey(aclink.getEcId());
    	custData = custDataHome.fetchCustDataByKey(aclink.getCustId());
    	
    	if (!StringUtil.isBlank(ecData)) {
    		ecDataStts = ecData.STTS;
		}
    	
    	if (!StringUtil.isBlank(custData)) {
    		custDataStts = custData.STTS;
		}
    	
    	
    	rtnMsg = CommonUtil.checkStatus("01", ecDataStts, custDataStts, aclink.getPltfStts(), "");
    	
    	return rtnMsg;
    }
    
    /**
     * 取得客戶所有帳號
     * @param custId 身分證字號
     * @param ecId   平台代碼
     * @param ecUser 平台會員代碼
     * @param conn
     * @return
     * @throws DBException
     */
    private List<CustAcntLink> fetchCustAcntLinkByKey(String custId, String ecId, String ecUser, Connection conn) throws DBException {
    	CustAcntLink[] temps = null;
    	List<CustAcntLink> acnts = new ArrayList<CustAcntLink>();
    	CustAcntLinkHome home = new CustAcntLinkHome(conn);
    	
    	temps = home.getCustAllRealAcnt(custId, ecId, ecUser);
    	
    	acnts = Arrays.asList(temps);
    	
    	return acnts;
    }
    
    /**
     * 發電文取得會員狀態及相關資料
     * @param link
     * @return 回傳錯誤代碼 or 空白字串
     */
    private String sendTelegramFetchCIF(ACLink link) {
		String rtnCode = "";
    	String outPutCode = "";  // 電文回傳結果 03: 成功, 01: 失敗
		String surName = "";	 // 會員姓名
    	String tlxNo = "";		 // 行動電話
		String restrictStat = "";// 會員狀態
		String emailAddr = "";   // 電子郵件
		
		// 發電文取得會員狀態及相關資料
		MemberMsgResponseInfo mberMsg = null;
		try {
			mberMsg = (MemberMsgResponseInfo) new TelegramBo().sendTelegramGetCIF(link.getCustId());
			outPutCode = mberMsg.getOUTPUT_CODE();
			
		} catch (Exception e) {
			LOG.error("[Exception]:", e);
			rtnCode = "1001"; //基本資料取得失敗或異常 (取得 CIF 資料失敗)
			return rtnCode;
		}
		
		if ("03".equals(outPutCode)) {
			surName = mberMsg.getNAME();
			tlxNo = mberMsg.getTELE();
			emailAddr = mberMsg.getEMAIL();
			restrictStat = mberMsg.getSTATUS();
			
			if (StringUtil.isBlank(tlxNo)) {
				LOG.info("========== [身分證號: " + CommonUtil.stringMask(link.getCustId(), 3, 3, "*") + " 未約定行動電話] ==========");
				rtnCode = "1998";
				return rtnCode;
			}else if (StringUtil.isBlank(emailAddr)) {
				LOG.info("========== [身分證號: " + CommonUtil.stringMask(link.getCustId(), 3, 3, "*") + " 未約定電子郵件] ==========");
				rtnCode = "1998";
				return rtnCode;
			}else if (StringUtil.isBlank(restrictStat) || !"000".equals(restrictStat)) {
				rtnCode = "1001";
				return rtnCode;
			}
			
			link.setSurName(surName);
			link.setTlxNo(tlxNo);
			link.setEmailAddr(emailAddr);
			
			return rtnCode;
		} else {
			rtnCode = "1001";
			//get 下行.Error Message Number 記錄在 CUST_ACNT_LOG.HOST_CODE
			String hostCode = mberMsg.getS_HOST_CODE();  //v1.06, 取得 error code
			if(!StringUtil.isBlank(hostCode)){
				rtnCode = rtnCode + "," + hostCode;
			}
		}
		
		return rtnCode;
	}
    
    /**
     * 發電文取得會員銀行存款帳號清單
     * @param aclink
     * @return
     */
    private String sendTelegramFetchAccount(ACLink aclink, List<String> acnts) {
    	String rtnCode = "";
    	

    	String outPutCode = ""; // 電文回傳結果 03: 成功, 01: 失敗
    	String acntNo = "";		// 存款帳號
    	String acntType = "";   // 產品代碼
    	String nextKey = "";	// 下一頁查詢
//    	List<AccountListRecord> records = null;
    	List<String> returnAcnts = null;
    	// 發電文取得會員銀行存款帳號清單
    	AccountListResponseInfo accountListResponseInfo = null;
    	
    	try {
    		accountListResponseInfo = (AccountListResponseInfo) new TelegramBo().sendFCB919703_63(aclink.getCustId()); 
    		outPutCode = accountListResponseInfo.getOUTPUT_CODE();
    		
    		
		} catch (Exception e) {
			LOG.error("[Exception]: ", e);
			rtnCode = "1002"; //帳戶資料取得失敗或異常(歸戶電文發送失敗)
			return rtnCode;
		}
    	
    	if ("03".equals(outPutCode)) {
    		returnAcnts =  accountListResponseInfo.getRecords();

    		for(int i=0;i<returnAcnts.size();i++) {
    			acnts.add(returnAcnts.get(i));
    		}
    		//mark by jeff規則判斷改在電文抓回來時候判斷
    		
        	//v1.05, 綁定之存款帳戶排除清單增加「1604」
//        	List<String> filterType = Arrays.asList("1011", "1101", "1103", "1104", "1203", "1204", "1303", "1304", "1403", "1404", "1503", "1504", "1604");
//    		for (AccountListRecord record : records) {
//    			acntType = record.getACNT_TYPE();
//    			acntNo = record.getACNT_NO();
//
//    			if (acntType.startsWith("1") && !acntType.startsWith("18") && !filterType.contains(acntType)) {
//    				acnts.add(acntNo);
//    			}
//			}
    		if(acnts==null || acnts.size()==0){
    			rtnCode = "1003"; //無可綁定之帳號資料 (沒有符合條件的帳號)
    		}
		} else {
			rtnCode = "1002"; //帳戶資料取得失敗或異常(歸戶電文發送失敗)
			//get 下行.Error Message Number 記錄在 CUST_ACNT_LOG.HOST_CODE
			String hostCode = accountListResponseInfo.getS_HOST_CODE();
			if(!StringUtil.isBlank(hostCode)){
				rtnCode = rtnCode + "," + hostCode;
			}
		}
    	
    	return rtnCode;
    }
    
    /**
     * 發電文通知一銀主機綁定帳號
     * @param aclink
     * @return
     */
    private String sendTelegramFetchHost(ACLink aclink) {
    	String rtnCode = "";
    	String outPutCode = "";  // 電文回傳結果 03: 成功, 01: 失敗
		
		// 發電文通知一銀主機綁定帳號
		NotificationHostMsgResponseInfo notificationHostMsgResponseInfo = null;
		try {
			//綁定: link, 解綁: cancel, 查詢: request
			notificationHostMsgResponseInfo = (NotificationHostMsgResponseInfo) new TelegramBo().sendFCB91920Y(aclink.getLinkAcnt(), aclink.getEcId(), "link");
			outPutCode = notificationHostMsgResponseInfo.getOUTPUT_CODE();
		} catch (Exception e) {
			LOG.error("[Exception]:", e);
			rtnCode = "5024"; //連結帳號綁定處理異常...
			return rtnCode;
		}
		
		if ("03".equals(outPutCode)) {
			return rtnCode;
		} else {
			rtnCode = "1005"; // 綁定失敗....
			//get 下行.Error Message Number 記錄在 CUST_ACNT_LOG.HOST_CODE
			String hostCode = notificationHostMsgResponseInfo.getS_HOST_CODE();  //v1.06, 取得 error code
			if(!StringUtil.isBlank(hostCode)){
				rtnCode = rtnCode + "," + hostCode;
			}
		}
		
		return rtnCode;
    }
    
    private String sendTelegramFetchHost() {
    	return "";
    }
    
//    /**
//     * 發送 OTP 
//     * @param req
//     * @param conn
//     * @param msgConn
//     * @throws DBException 
//     */
//    private void sendOtpCode(HttpServletRequest req, Connection conn, Connection msgConn) throws DBException {
//    	HttpSession session = req.getSession(true);
//    	
//    	ACLink aclink = (ACLink) session.getAttribute("link");
//    	
//    	String otpCode = MathUtil.randomNumber(6); // 產生六碼 OTP 亂數
//    	// TODO for 測試用
//    	LOG.info("========== [otpCode]: " + otpCode + " ==========");
//    	
//    	try {
//    		MsgSourHome msgSourHome = new MsgSourHome(msgConn);
//    		
//    		LOG.info("========== [phone number] ==========: " + aclink.getTlxNo());
//    		
//    		//產生訊息字串
//    		String msgData = this.generateMsgData(conn,otpCode);
//
//    		msgSourHome.insertMsgSour(aclink.getTlxNo(), msgData);
//    		
//    		LOG.info("========== [insert Msgsour finish] ==========");
//    		
//		} catch (DBException e) {
//			LOG.error("[sendOtpCode DBException]: ", e);
//		} catch (UtilException e) {
//			LOG.error("[sendOtpCode UtilException]", e);
//		}
//    	
//    	session.setAttribute("otpCode", otpCode);
//    }
    
    /**
     * 發送 OTP 
     * @param req
     * @param conn
     * @param msgConn
     * @throws Exception 
     */
    private void sendOtpCode(HttpServletRequest req, Connection conn) throws Exception {
    	HttpSession session = req.getSession(true);
    	
    	ACLink aclink = (ACLink) session.getAttribute("link");
    	boolean needSendFlag = false;
    	try {
    		MsgSourHome msgSourHome = new MsgSourHome(conn);
    		
    		String queryOotUserF2 = msgSourHome.queryOptUserF2(aclink);
    		LOG.info("查詢簡訊用戶[" + aclink.getCustId() + "]，簡訊OTP主機回覆代碼[" + queryOotUserF2 + "]");
    		if("0000".equals(queryOotUserF2)){
    			needSendFlag = true;
    		}else if("6102".equals(queryOotUserF2) || "6094".equals(queryOotUserF2)){ //6102 =>表示該帳號不存, 6094帳號已被註銷
    			//新增簡訊用戶
    			String sendOTPMsgFA = msgSourHome.sendOTPMsgFA(aclink);
    			if ("0000".equals(sendOTPMsgFA) || "6008".equals(sendOTPMsgFA)) {
        			LOG.info("新增簡訊用戶[" + aclink.getCustId() + "]成功，簡訊OTP主機回覆代碼[" + sendOTPMsgFA + "]");
        			needSendFlag = true;
        		}else{
        			//新增失敗
        			LOG.error("新增簡訊用戶[" + aclink.getCustId() + "]失敗，簡訊OTP主機回覆代碼[" + sendOTPMsgFA + "]");
        		}
    		}else if("6096".equals(queryOotUserF2)){ //帳號已鎖卡
    			//使用者帳號解鎖
    			String unlockOptUserF7 = msgSourHome.unlockOptUserF7(aclink);
    			if ("0000".equals(unlockOptUserF7)) {
    				//成功
    				LOG.info("使用者帳號解鎖[" + aclink.getCustId() + "]成功，簡訊OTP主機回覆代碼[" + unlockOptUserF7 + "]");
    				needSendFlag = true;
    			}else{
    				LOG.error("使用者帳號解鎖[" + aclink.getCustId() + "]失敗，簡訊OTP主機回覆代碼[" + unlockOptUserF7 + "]");
    			}
    		}
    		
    		if(needSendFlag){
	    		String sendOTPMsgFB = msgSourHome.sendOTPMsgFB(aclink);
				if (!"0000".equals(sendOTPMsgFB)) {
	    			LOG.error("[sendOtpCode Exception]: 簡訊OTP發送件敗，簡訊OTP主機回覆錯誤代碼[" + sendOTPMsgFB + "]");
	    		} else {
	    			LOG.info("行動電話[" + aclink.getTlxNo() + "]簡訊OTP發送成功，簡訊OTP主機回覆代碼[" + sendOTPMsgFB + "]");
	    		}
    		}
    		
			/*
    		String sendOTPMsgFA = msgSourHome.sendOTPMsgFA(aclink);
    		
    		if ("0000".equals(sendOTPMsgFA)) {
    			LOG.info("新增簡訊用戶[" + aclink.getCustId() + "]成功，簡訊OTP主機回覆代碼[" + sendOTPMsgFA + "]");
    			String sendOTPMsgFB = msgSourHome.sendOTPMsgFB(aclink);
    			if (!"0000".equals(sendOTPMsgFB)) {
        			LOG.error("[sendOtpCode Exception]: 簡訊OTP發送件敗，簡訊OTP主機回覆錯誤代碼[" + sendOTPMsgFB + "]");
        		} else {
        			LOG.info("行動電話[" + aclink.getTlxNo() + "]簡訊OTP發送成功，簡訊OTP主機回覆代碼[" + sendOTPMsgFB + "]");
        		}
    		} else if("6008".equals(sendOTPMsgFA)){
    			String sendOTPMsgF3 = msgSourHome.sendOTPMsgF3(aclink);
    			if("0000".equals(sendOTPMsgF3)) {
    				sendOTPMsgFA = msgSourHome.sendOTPMsgFA(aclink);
    				if ("0000".equals(sendOTPMsgFA)) {
    	    			LOG.info("新增簡訊用戶[" + aclink.getCustId() + "]成功，簡訊OTP主機回覆代碼[" + sendOTPMsgFA + "]");
    	    			String sendOTPMsgFB = msgSourHome.sendOTPMsgFB(aclink);
    	    			if (!"0000".equals(sendOTPMsgFB)) {
    	        			LOG.error("[sendOtpCode Exception]: 簡訊OTP發送件敗，簡訊OTP主機回覆錯誤代碼[" + sendOTPMsgFB + "]");
    	        		} else {
    	        			LOG.info("行動電話[" + aclink.getTlxNo() + "]簡訊OTP發送成功，簡訊OTP主機回覆代碼[" + sendOTPMsgFB + "]");
    	        		}
    	    		} else {
    	    			LOG.info("新增簡訊用戶[" + aclink.getTlxNo() + "]失敗，簡訊OTP主機回覆錯誤代碼[" + sendOTPMsgFA + "]");
    	    		}
    			}else {
    				LOG.info("行動電話[" + aclink.getTlxNo() + "]簡訊OTP使用者刪除失敗");
    			}
    		} else {
    			LOG.info("新增簡訊用戶[" + aclink.getTlxNo() + "]失敗，簡訊OTP主機回覆錯誤代碼[" + sendOTPMsgFA + "]");
    		}
    		*/
		} catch (Exception e) {
			LOG.error("[sendOtpCode Exception]: ", e);
		} 
    }
    
//    /**
//     * 產生訊息字串
//     * @param conn
//     * @param otpCode
//     * @return
//     * @throws DBException
//     */
//    private String generateMsgData(Connection conn,String otpCode) throws DBException{
//    	SysParmHome sysParmHome = new SysParmHome(conn);
//    	SysParm sysParm = sysParmHome.fetchSysParmByKey("OTP_EXPR_TIME");
//		String OTP_EXPR_TIME = sysParm.PARM_VALUE;
//		
//		String time = String.valueOf(Integer.parseInt(OTP_EXPR_TIME)/60);
//		
//		String msgData = APSystem.getProjectParam("SMS_MESSAGE");
//		
//		msgData = msgData.replace("#1", otpCode);
//		msgData = msgData.replace("#2", time);
//		
//		System.out.println(msgData);
//		return msgData;
//    }
    
//    /**
//     * 驗證 OTP
//     * @param request
//     * @return
//     */
//    private void confirmOTP(HttpServletRequest req) {
//		HttpSession session = req.getSession(true);
//		
//		String orgOtpCode = (String) session.getAttribute("otpCode");
//		String inputOtpCode = req.getParameter("OTPCode");
//		String result = "N";
//		
//		// v1.07
//		LOG.info("[orgOtpCode]:" + orgOtpCode + ", [inputOtpCode]:" + inputOtpCode);
//		
//		if (orgOtpCode.equals(inputOtpCode)) {
//			result = "Y";
//		}
//		// v1.07, 刪除 OTP 不可覆驗證
//    	
//		req.setAttribute("_result", result);
//	}
    
    /**
     * 驗證 OTP
     * @param request
     * @return
     */
    private void confirmOTP(HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		String SMSIP = APSystem.getProjectParam("SMS_IP");
		String SMSPOPRT = APSystem.getProjectParam("SMS_POPRT");
		
		String retCode = "";
		ACLink aclink = (ACLink) session.getAttribute("link");
		
		String inputOtpCode = req.getParameter("OTPCode");
		String result = "N";
		retCode = new Proc00F8().response(aclink.getCustId(), inputOtpCode, SMSIP,SMSPOPRT);
		if("0000".equals(retCode)) {
			result = "Y";
			LOG.info("驗證 OTP[" + aclink.getCustId() + "]成功，簡訊OTP主機回覆代碼[" + retCode + "]");
		}else {
			LOG.info("驗證 OTP[" + aclink.getCustId() + "]失敗，簡訊OTP主機回覆代碼[" + retCode + "]");
		}
		
		req.setAttribute("_result", result);
	}
    
    /**
     * 確認連結帳號資訊
     * @param req
     * @return
     * @throws DBException 
     */
	private void confirmBindInfo(HttpServletRequest req) throws DBException {
		HttpSession session = req.getSession(true);
		
		String linkAcnt = req.getParameter("account");
		String idetityAuthType = req.getParameter("idetityAuthType");
		
		ACLink link = (ACLink) session.getAttribute("link");
		
		// =============== 更新 aclink ===============
		link.setIdetityAuthType(idetityAuthType);
		link.setLinkAcnt(linkAcnt);

		session.setAttribute("link", link);
		
	}
	
	/**
	 * 確認送出
	 * @param req
	 * @param conn
	 * @return
	 * @throws DBException 
	 */
	private AbstractResponseBean confirmSubmit(HttpServletRequest req, Connection conn) throws DBException {
		ACLinkResBeanTwo resBesn = (ACLinkResBeanTwo) this.resBean;

		HttpSession session = req.getSession(true);
		
		String indtAcnt = CommonUtil.genAcntIdnt();
		String rtnCode = "0000";   // 回確代碼	
		String rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg(); // 回確訊息說明
		String linkGrad = "A"; 	   // 身分認證等級
		String acntLogStts = "00"; //
		String hostCode=" ";
		int rsltCont = 0;			// v1.08
		
		// v1.08
		ACLink aclink = (ACLink) session.getAttribute(ACLink.ACLINK);
		
		if(StringUtil.isBlank(indtAcnt)) {
			String BANK_ID = APSystem.getProjectParam("BANK_ID");
			String today = DateUtil.getToday();
			String newSeq = aclink.getMsgNo().substring(aclink.getMsgNo().length()-6);
			indtAcnt = BANK_ID + today.substring(2,6) + newSeq;
		}
		
		
		// 依據身分認證等級取得法定限額, 作為後續自訂限額預設值
		BaseLimt baseLimt = null;
		CustAcntLink acntLink = null;
		
		try {
			// v1.03, 檢核連結綁定帳號必輸資料是否為空
			rsltCont = this.validateBindingVal(aclink);
			// v1.08
			if (rsltCont > 0) {
				session.setAttribute(ACLink.ACLINK, aclink);
				return null;
			}
			
			// 依據身分認證方式, 判斷額度等級 (00: A, 01: B, 02: C)
			if ("01".equals(aclink.getIdetityAuthType())) {
				linkGrad = "B";
				
			} else if ("02".equals(aclink.getIdetityAuthType())) {
				linkGrad = "C";
			} 
			
			BaseLimtHome baseLimtHome = new BaseLimtHome(conn);
			CustAcntLinkHome acntLinkHome = new CustAcntLinkHome(conn);
			
			baseLimt = baseLimtHome.fetchBaseLimtByKey(linkGrad);
			acntLink = acntLinkHome.getCustAcntLinkByPk(aclink.getCustId(), aclink.getEcId(), aclink.getEcUser(), aclink.getLinkAcnt());
			
			if (StringUtil.isBlank(acntLink)) {
				aclink.setLinkStts("X");
			} else {
				if (!"02".equals(acntLink.STTS) ) {
					resBesn.setMSG_NO(aclink.getMsgNo());
					rtnCode="5023"; //連結帳號已綁定
					resBesn.setRTN_CODE(rtnCode);
		     		resBesn.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
					resBesn.setEC_ID(aclink.getEcId());
					resBesn.setEC_USER(aclink.getEcUser());
					return resBesn;
				} else {
					aclink.setLinkStts("02");
				}
			}
			
			LOG.info("欲綁定連結帳號與歸戶清單確認...開始");
			List<String> listAccounts = new ArrayList();
			for(Accounts account:aclink.getBinding().getAcnts()){
				listAccounts.add(account.getAcnt());
			}
			LOG.debug("歸戶帳號 = "+listAccounts);
			
			if(!listAccounts.contains(aclink.getLinkAcnt())) {
				resBesn.setMSG_NO(aclink.getMsgNo());
				rtnCode="5024"; //連結帳號綁定處理異常
				resBesn.setRTN_CODE(rtnCode);
	     		resBesn.setRTN_MSG(new TbCodeHelper(rtnCode, "01").getTbCodeMsg());
				resBesn.setEC_ID(aclink.getEcId());
				resBesn.setEC_USER(aclink.getEcUser());
				LOG.info("欲綁定連結帳號不在歸戶清單");
				return resBesn;
			}
			LOG.info("欲綁定連結帳號在歸戶清單中....");
			
			//若無發送主機綁定帶this.sendTelegramFetchHost(); 
			rtnCode = this.sendTelegramFetchHost(aclink); 
			
			if("5024".equals(rtnCode)) {//綁定電文發送異常
				SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
			    Date now = df.parse(df.format(new Date()));
			    Date beginTime = df.parse("00:00");
			    Date endTime = df.parse("00:15");
			    if(!CommonUtil.belongCalendar(now,beginTime,endTime)) {
			    		rtnCode = this.sendTelegramFetchHost(aclink);
			    }
			}
			
			if(StringUtil.isBlank(rtnCode)) {
				rtnCode = "0000";
				// =============== 異動會員資料 ===============
				TransactionControl.transactionBegin(conn);
				// 依據會員服務狀態異動會員資料檔 (X: 新增, 02: 終止)
				LOG.info("========== start 異動會員資料檔 ==========");
				this.saveOrUpdateCustData(aclink, conn);
				LOG.info("========== end 異動會員資料檔 ==========");
				
				// 依據會員平台服務狀態異動 會員平台資料 (X: 新平台, 02: 終止)
				LOG.info("========== start 異動會員平台資料 ==========");
				this.saveOrUpdateCustPltf(aclink, conn);
				LOG.info("========== end 異動會員平台資料 ==========");
				
				// 依據 連結帳號服務狀態異動 會員連結帳號資料(X: 新增, 02: 終止)
				LOG.info("========== start 異動會員連結帳號資料 ==========");
				this.saveOrUpdateCustAcntLink(aclink, baseLimt, linkGrad, indtAcnt, conn);
				LOG.info("========== end 異動會員連結帳號資料 ==========");
				
				// 依據 會員帳號狀態異動 會員帳號檔(X: 新增, 02: 終止)
				LOG.info("========== start 異動會員帳號檔 ==========");
				this.saveOrUpdatecCustAcnt(aclink, baseLimt, conn);
				LOG.info("========== end 異動會員帳號檔 ==========");
				
				TransactionControl.trasactionCommit(conn);
			} else {
				LOG.info("========== 帳號綁定: " + rtnCode + " ==========");
				if(rtnCode.contains(",")){
					//v1.09, 調整拆解順序, 避免造成ArrayIndexOutOfBoundsException
					hostCode = rtnCode.split(",")[1];
					rtnCode = rtnCode.split(",")[0];
				}
				acntLogStts = "01";
				rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
				resBesn = this.compseResponseObj(rtnCode, rtnMsg, aclink);
				// 新增連結紀錄 log 並發 mail
//				this.saveCustAcntLog(aclink, null, rtnCode, "", "", "01", conn, hostCode);
				return resBesn;
			}
		} catch (DBException e) {
			acntLogStts = "01";
			LOG.error("[confirmSubmit DBException]: ", e);
			TransactionControl.transactionRollback(conn);
			rtnCode = "5024";
			rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
			
		} catch (UtilException e) {
			acntLogStts = "01";
			LOG.error("[confirmSubmit UtilException]: ", e);
			TransactionControl.transactionRollback(conn);
			rtnCode = "5024";
			rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
			
		} catch (Exception e) {
			acntLogStts = "01";
			LOG.error("[confirmSubmit Exception]: ", e);
			TransactionControl.transactionRollback(conn);
			rtnCode = "5024";
			rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
			
		} finally {
			// v1.08
			if (rsltCont == 0) {
				try {
					// 新增會員帳號連結紀錄
					this.saveCustAcntLog(aclink, baseLimt, rtnCode, indtAcnt, linkGrad, acntLogStts, conn, "");
					
				} catch (UtilException e) {
					LOG.error("========== 新增會員帳號連結紀錄失敗 ==========", e);
				}
				
				TransactionControl.transactionEnd(conn);
			}
		}

     	EcData ecData = new EcDataHome(conn).fetchEcDataByKey(aclink.getEcId());
		// =============== set return message ===============
		resBesn.setMSG_NO(aclink.getMsgNo());
		resBesn.setRTN_CODE(rtnCode);
		resBesn.setRTN_MSG(rtnMsg);
		resBesn.setEC_ID(aclink.getEcId());
		resBesn.setEC_USER(aclink.getEcUser());
		resBesn.setINDT_ACNT(indtAcnt);
		// v1.02, 增加帳號遮蔽
		if("N".equals(ecData.SHOW_REAL_ACNT)){
			resBesn.setLINK_ACNT(CommonUtil.relAcntMask(aclink.getLinkAcnt()));
		}else{
			resBesn.setLINK_ACNT(aclink.getLinkAcnt());
		}			
		resBesn.setLINK_GRAD(linkGrad);
		
		return resBesn;
	}
	
	/**
	 * 檢核 HttpSession 是否為新生成
	 * @param session
	 */
	private HttpSession validateSession (HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		
		/*
		 * 每次登入時, 先將當前 Session 銷毀在重新產生, 確保 Session 是最新的
		 * 避免同一個 browser 開啟二個頁籤, Session 視為同一個的狀況
		 */
		if (!StringUtil.isBlank(session)) {
			LOG.info("[銷毀當前 Session]: " + session.getId());
			session.invalidate();
		}
		
		session = req.getSession(false);
		
		if (StringUtil.isBlank(session)) {
			session = req.getSession();
			LOG.info("[產生全新 Session]: " + session.getId());
		}
		
		return session;
	}
	
	/**
	 * 取消連結綁定
	 * @param req
	 * @param conn
	 * @return AbstractResponseBean
	 * @throws DBException 
	 * @throws UtilException 
	 */
	private AbstractResponseBean cancel(HttpServletRequest req, Connection conn) throws DBException, UtilException {
		HttpSession session = req.getSession(true);
		
		ACLinkResBeanTwo resBesn = (ACLinkResBeanTwo) this.resBean;
		ACLink aclink = (ACLink) session.getAttribute("link");
		
		String sessionKey = req.getParameter("sessionKey");
		String linkAcnt = req.getParameter("account");
		
		// 在同意條款頁 點選不同意時需要刪除 TempData
		if (!StringUtil.isBlank(sessionKey)) {
			SessionTempTableHome home = new SessionTempTableHome(conn);
			home.deleteSessionTempTableByKey(sessionKey);
			
		}

		aclink.setLinkAcnt((StringUtil.isBlank(linkAcnt)) ? "" : linkAcnt);
		
		String rtnCode="9900"; //使用者取消操作
		String rtnMsg =new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
		this.saveCustAcntLog(aclink, null, rtnCode, "", "", "01", conn, "");
		
		//
		resBesn.setMSG_NO(aclink.getMsgNo());
		resBesn.setRTN_CODE(rtnCode);
		resBesn.setRTN_MSG(rtnMsg);
		resBesn.setEC_ID(aclink.getEcId());
		resBesn.setEC_USER(aclink.getEcUser());
		
		return resBesn;
	}
    
    /**
     * 新增 temp 資料
     * @param link
     * @param conn
     * @throws DBException 
     */
    private void insertTempData(ACLink link, Connection conn) throws DBException {
    	String sessionKey = link.getSessionKey();
    	String jsonStr = JsonUtil.object2Json(link, false);
    	
    	SessionTempTable tempTable = new SessionTempTable();
    	SessionTempTableHome home = new SessionTempTableHome(conn);
    	
    	tempTable.SESSION_KEY = sessionKey;
    	tempTable.SESSION_DATA = jsonStr;
    	
    	home.insert(tempTable);
    	
    }
    
    /**
     * 依據會員服務狀態異動會員資料檔 (X: 新增, 02: 終止)
     * @param aclink
     * @param conn
     * @throws DBException
     * @throws UtilException
     */
    private void saveOrUpdateCustData(ACLink aclink, Connection conn) throws DBException, UtilException {
    	CustData custData = null;
    	SysParm sysParm = new SysParm();
    	
    	CustDataHome custDataHome = new CustDataHome(conn);
    	SysParmHome sysParmHome = new SysParmHome(conn);
    	
    	sysParm = sysParmHome.fetchSysParmByKey("PRVS_VRSN");
    	
    	LOG.info("= [Cust_Id]: " + aclink.getCustId() + " [Cust_Stts]: " + aclink.getCustStts());
    	LOG.info("= [Cust_Type]: " + aclink.getIdentityType() + " [Srv_Vrsn]: " + sysParm.PARM_VALUE);
    	LOG.info("= [Cust_Tel]: " + CommonUtil.stringMask(aclink.getTlxNo(), 4, 3, "*") + " [Cust_Mail]: " + CommonUtil.stringMask(aclink.getEmailAddr(), 3, 3, "*"));
    	
    	if ("X".equals(aclink.getCustStts())) {
    		custData = new CustData();
    		custData.CUST_ID = aclink.getCustId();
        	custData.CUST_NAME = aclink.getSurName();
        	custData.CUST_SERL = "00001";
        	custData.CUST_TYPE = "11";
        	custData.TEL = aclink.getTlxNo();
        	custData.MAIL = aclink.getEmailAddr();
        	custData.VRSN = sysParm.PARM_VALUE;
        	custData.STTS = "00";
        	custData.STTS_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
        	custData.CRET_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
        	custData.MDFY_USER = aclink.getCustId();
        	custData.MDFY_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
        	
			custDataHome.insert(custData);
			
		} else if ("02".equals(aclink.getCustStts())) {
			// 依據 身分證字號取得 會員資料
			custData = custDataHome.fetchCustDataByKey(aclink.getCustId());
			
			custData.CUST_ID = aclink.getCustId();
        	custData.CUST_NAME = aclink.getSurName();
        	custData.CUST_SERL = this.serlIncrease(custData.CUST_SERL);
        	custData.CUST_TYPE = "11";
        	custData.TEL = aclink.getTlxNo();
        	custData.MAIL = aclink.getEmailAddr();
        	custData.VRSN = sysParm.PARM_VALUE;
        	custData.STTS = "00";
        	custData.STTS_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
        	custData.MDFY_USER = aclink.getCustId();
        	custData.MDFY_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
        	
        	custDataHome.update(custData);
        	
		}
    	
    	if (!StringUtil.isBlank(custData)) {
    		LOG.info("= [Cust_Serl]: " + custData.CUST_SERL);
    		// 更新會員服務序號
    		aclink.setCustSerl(custData.CUST_SERL);
		}
    	
    }
    
    /**
     * 依據會員平台服務狀態異動 會員平台資料 (X: 新平台, 02: 終止)
     * @param aclink
     * @param conn
     * @throws UtilException 
     * @throws DBException 
     */
    private void saveOrUpdateCustPltf(ACLink aclink, Connection conn) throws UtilException, DBException {
    	CustPltf pltf = new CustPltf();
    	CustPltfHome home = new CustPltfHome(conn);
    	
    	LOG.info("= [Ec_Id]: " + aclink.getEcId() + " [Cust_Pltf_Stts]: " + aclink.getPltfStts());
    	
    	if ("X".equals(aclink.getPltfStts())) {
			pltf.CUST_ID = aclink.getCustId();
			pltf.EC_ID = aclink.getEcId();
			pltf.STTS = "00";
			pltf.STTS_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
			pltf.CRET_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
			pltf.MDFY_USER = aclink.getCustId();
			
			home.insert(pltf);
			
		} else if ("02".equals(aclink.getPltfStts())) {
			pltf = home.fetchCustPltfByKey(aclink.getEcId(), aclink.getCustId());
			
			pltf.STTS = "00";
			pltf.STTS_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
			pltf.MDFY_USER = aclink.getCustId();
			
			home.update(pltf);
		}
    }
    
    /**
     * 依據 連結帳號服務狀態異動 會員連結帳號資料(X: 新增, 02: 終止)
     * @param aclink
     * @param limt
     * @param linkGrad
     * @param indtAcnt
     * @param conn
     * @throws DBException
     * @throws UtilException 
     */
    private void saveOrUpdateCustAcntLink(ACLink aclink, BaseLimt limt, String linkGrad, String indtAcnt, Connection conn) throws DBException, UtilException {
    	CustAcntLink acntLink = new CustAcntLink();
    	CustAcntLinkHome home = new CustAcntLinkHome(conn);
    	
    	LOG.info("= [Ec_User]: " + aclink.getEcUser() + " [Real_Acnt]: " + CommonUtil.relAcntMask(aclink.getLinkAcnt()));
    	LOG.info("= [Grad_Type]: " + aclink.getIdetityAuthType() + " [Grad]: " + linkGrad);
    	
    	if ("X".equals(aclink.getLinkStts())) {
    		acntLink.CUST_ID = aclink.getCustId();
    		acntLink.EC_ID = aclink.getEcId();
    		acntLink.EC_USER = aclink.getEcUser();
    		acntLink.REAL_ACNT = aclink.getLinkAcnt();
    		acntLink.GRAD_TYPE = aclink.getIdetityAuthType();
    		acntLink.GRAD = linkGrad;
    		acntLink.STTS = "00";
    		acntLink.STTS_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
    		acntLink.TRNS_LIMT = limt.TRNS_LIMT;
    		acntLink.DAY_LIMT = limt.DAY_LIMT;
    		acntLink.MNTH_LIMT = limt.MNTH_LIMT;
    		acntLink.CRET_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
    		acntLink.MDFY_USER = aclink.getCustId();
    		acntLink.MDFY_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
    		acntLink.ACNT_INDT = indtAcnt;
    		
			home.insert(acntLink);
			
		} else if ("02".equals(aclink.getLinkStts())) {
			acntLink = home.getCustAcntLinkByPk(aclink.getCustId(), aclink.getEcId(), aclink.getEcUser(), aclink.getLinkAcnt());
			
			acntLink.GRAD_TYPE = aclink.getIdetityAuthType();
    		acntLink.GRAD = linkGrad;
    		acntLink.STTS = "00";
    		acntLink.STTS_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
    		acntLink.TRNS_LIMT = limt.TRNS_LIMT;
    		acntLink.DAY_LIMT = limt.DAY_LIMT;
    		acntLink.MNTH_LIMT = limt.MNTH_LIMT;
    		acntLink.MDFY_USER = aclink.getCustId();
    		acntLink.MDFY_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
    		acntLink.ACNT_INDT = indtAcnt;
    		
			home.update(acntLink);
		}
    }
    
    /**
     * 依據 會員帳號狀態異動 會員帳號檔(X: 新增, 02: 終止)
     * @param aclink
     * @param limt
     * @param conn
     * @throws DBException 
     * @throws UtilException 
     */
    private void saveOrUpdatecCustAcnt(ACLink aclink, BaseLimt limt, Connection conn) throws DBException, UtilException {
    	CustAcnt acnt = null;
    	CustAcntHome home = new CustAcntHome(conn);

    	acnt = home.fetchCustAcntByKey(aclink.getCustId(), aclink.getLinkAcnt());
    	
    	if ("02".equals(aclink.getCustStts()) && !StringUtil.isBlank(acnt)) {
			
			acnt.TRNS_LIMT = 0L;
    		acnt.DAY_LIMT  = 0L;
    		acnt.MNTH_LIMT = 0L;
    		acnt.MDFY_USER = aclink.getCustId();
    		acnt.MDFY_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
    		
			home.update(acnt);

		} else if (StringUtil.isBlank(acnt)) {
			acnt = new CustAcnt();
			acnt.CUST_ID = aclink.getCustId();
    		acnt.REAL_ACNT = aclink.getLinkAcnt();
    		acnt.TRNS_LIMT = 0L;
    		acnt.DAY_LIMT  = 0L;
    		acnt.MNTH_LIMT = 0L;
    		acnt.CRET_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
    		acnt.MDFY_USER = aclink.getCustId();
    		acnt.MDFY_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
    		
    		home.insert(acnt);
			
		}
    }
    
    /**
     * 新增會員帳號連結紀錄
     * @param aclink
     * @param rtnCode
     * @param indtAcnt
     * @param linkGrad
     * @param stts
     * @param conn
     * @return
     * @throws UtilException
     * @throws DBException
     */
    private void saveCustAcntLog(ACLink aclink, BaseLimt limt, String rtnCode, String indtAcnt, String linkGrad, 
    		String stts, Connection conn, String hostCode) throws UtilException, DBException {
    	CustAcntLog acntLog = new CustAcntLog();
    	CustAcntHome home = new CustAcntHome(conn);
    	
    	acntLog.LOG_NO = UUIDGen.genUUID();
    	acntLog.CUST_ID = aclink.getCustId();
    	acntLog.EC_ID	= aclink.getEcId();
    	acntLog.EC_USER	= aclink.getEcUser();
    	acntLog.REAL_ACNT = aclink.getLinkAcnt();
    	acntLog.CRET_DTTM = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
    	acntLog.GRAD = linkGrad;
    	acntLog.GRAD_TYPE = aclink.getIdetityAuthType();
    	acntLog.STTS = stts; 
    	acntLog.CUST_SERL = aclink.getCustSerl(); 
    	acntLog.ACNT_INDT = indtAcnt;
    	acntLog.EXEC_SRC = "A";
    	acntLog.EXEC_USER = "";
    	acntLog.EC_MSG_NO = aclink.getMsgNo();
    	acntLog.ERR_CODE = rtnCode;
    	acntLog.HOST_CODE = hostCode;
    	//V2.02 紀錄IP到CUST_ACNT_LOG
    	acntLog.IP = aclink.getIp();
    	//V2.02 紀錄IP到CUST_ACNT_LOG End
    	
    	home.insert(acntLog);
    	
    	try {
    		
    		if (!StringUtil.isBlank(aclink.getEmailAddr())) {
    			this.sendMail(aclink, limt, acntLog);
			}
    		
		} catch (Exception e) {
			LOG.error("[Mail 發送失敗]: ", e);
		}
    }
    
    private void sendMail(ACLink link, BaseLimt limt, CustAcntLog acntLog) throws Exception {
    	//m by jeff for 一銀目前只有成功發送mail
    	if ("00".equals(acntLog.STTS)) {
    		if (!StringUtil.isBlank(link.getEmailAddr())) {
        		String subject = "";
        		String templateFile = "";
        		String mailContent = "";
        		
        		if ("00".equals(acntLog.STTS)) {
        			subject = APSystem.getProjectParam("MAIL_SUBJECT1");
        			templateFile = "ACL-G-01.vm"; // 連結綁定成功
        		}else if ("01".equals(acntLog.STTS)) {
        			subject = APSystem.getProjectParam("MAIL_SUBJECT2");
        			templateFile = "ACL-G-02.vm"; // 連結綁定失敗
        		}
        		
        		HashMap<String, String> mailMap = new HashMap<String, String>();
        		// =============== 明細資料 ===============
        		mailMap.put("EC_USER_NAME", CommonUtil.nameMask(link.getSurName()));						  // 使用者名稱
        		mailMap.put("CRET_DTTM", acntLog.CRET_DTTM.substring(0, 10)); 		  // 執行日期
        		mailMap.put("EC_NAME_CH", link.getEcName());						  // 電商平台
        		mailMap.put("EC_USER", link.getEcUser());	  							  // 平台會員代號
        		mailMap.put("REAL_ACNT", CommonUtil.relAcntMask(link.getLinkAcnt())); // 平台連結帳號
        		mailMap.put("GRAD_TYPE", this.mappingGradType(acntLog.GRAD_TYPE)); 	  // 身分認證
        		if ("00".equals(acntLog.STTS)) {
        			mailMap.put("TRNS_LIMT", CommonUtil.limtFormat(limt.TRNS_LIMT));	  // 每筆限額
        			mailMap.put("DAY_LIMT", CommonUtil.limtFormat(limt.DAY_LIMT));		  // 每日限額
        			mailMap.put("MNTH_LIMT", CommonUtil.limtFormat(limt.MNTH_LIMT));	  // 每月限額
        		}else{
        			// =============== 錯誤原因 ===============
            		mailMap.put("CODE_DESC", new TbCodeHelper(acntLog.ERR_CODE, "01").getTbCodeMsg());
        		}
        		
        		mailContent = MailUtil.renderMailHtmlContent(mailMap, templateFile);
        		
        		new MailUtil().addMail(mailContent, subject, link.getEmailAddr(), null, null, null);
    			
    		} 
    	}
    }
    
    /**
     * 依據 傳入會員服務序號 +1
     * @param custSerl
     * @return 
     */
    private String serlIncrease(String custSerl) {
    	int serl = Integer.parseInt(custSerl);
    	serl++;
    	String temp = String.valueOf(serl);
    	String rtnVal = "";
    	
    	for (int i = 0; i < (5 - temp.length()); i++) {
    		rtnVal = rtnVal.concat("0");
		}
    	
    	rtnVal = rtnVal.concat(temp);
    	
    	return rtnVal;
    }
    
    /**
     * 兜組回應訊息物件
     * @param rtnCode
     * @param rtnMsg
     * @param link
     * @return ACLinkResBeanTwo
     */
    private ACLinkResBeanTwo compseResponseObj(String rtnCode, String rtnMsg, ACLink link) {
    	ACLinkResBeanTwo resBesn = (ACLinkResBeanTwo) this.resBean;
    	
    	resBesn.setMSG_NO(link.getMsgNo());
		resBesn.setEC_ID(link.getEcId());
		resBesn.setEC_USER(link.getEcUser());
		resBesn.setRTN_CODE(rtnCode);
		resBesn.setRTN_MSG(rtnMsg);
    	
    	return resBesn;
    }
    
    /**
     * 身分認證代碼 mapping 
     * @param gradType
     * @return
     */
    private String mappingGradType(String gradType) {
    	String rtnVal = "";
    	
    	if("00".equals(gradType)) {
    		rtnVal = "網銀認證";
    	} else if ("01".equals(gradType)) {
    		rtnVal = "簡訊OTP";
    	} else if ("02".equals(gradType)) {
    		rtnVal = "晶片金融卡";
    	} else if ("03".equals(gradType)) {
    		rtnVal = "OTP設備";
    	} else if ("04".equals(gradType)) {
    		rtnVal = "Two Factors";
    	}
    	
    	return rtnVal;
    }
    
    /**
     * 產生 AES256 加密 JSON 字串
     * @param custId
     * @param sKey
     * @param conn
     * @return
     */
    private String generateVerifyJSON(String custId, String sKey, Connection conn) {
    	
    	String input = "";
    	String output = ""; // 經過 AES256 JSON 字串 
    	String key = "";
    	String iv = "";
    	String sysId = APSystem.getProjectParam("SYSID");
    	String forwardUrl = APSystem.getProjectParam("LINK_URL") + APSystem.getProjectParam("FORWARD_CARD_LOGIN");
    	
    	SysParm authWsAesKey = null;
    	SysParm authWsAesIv = null;
    	SysParmHome home = new SysParmHome(conn);
    	
    	try {
    		authWsAesKey = home.fetchSysParmByKey("AUTH_WS_AES_KEY");
    		authWsAesIv = home.fetchSysParmByKey("AUTH_WS_AES_IV");
    		
    		if (StringUtil.isBlank(authWsAesKey) || StringUtil.isBlank(authWsAesIv)) {
				
			} else {
				key = authWsAesKey.PARM_VALUE;
    			iv  = authWsAesIv.PARM_VALUE;
			}
    		
    		Map<String, Object> jsonStr = new HashMap<String, Object>();
        	jsonStr.put("SYSID", sysId);
        	jsonStr.put("SUCCESS_URL", forwardUrl);
        	jsonStr.put("FAIL_URL", forwardUrl);
        	jsonStr.put("NBFlag", "Y");
        	jsonStr.put("ID", custId);
        	jsonStr.put("SKEY", sKey);
        	
        	input = JsonUtil.object2Json(jsonStr, true);
        	output = CommonUtil.aesEncrypt(input, key, iv); 
	
    	} catch (Exception e) {
			LOG.error("[產生晶片卡驗證 JSON 字串失敗]", e);
		}
    	
    	return output;
    }

    /**
     * 依據 身分證字號 取得帳號資訊
     * @param aclink
     * @param session
     * @param conn
     * @return ACLinkResBeanTwo or null
     * @throws UtilException
     * @throws DBException
     */
    private ACLinkResBeanTwo fetchAcntInfo(ACLink aclink, HttpSession session, Connection conn) throws UtilException, DBException {
    	ACLinkResBeanTwo resBesn = null;
    	
    	String rtnCode = ""; // 回傳代碼
    	String rtnMsg = "";  // 回傳訊息
    	String hostCode = "";// 主機代碼
		
		BindingAcnt bindingAcnt = new BindingAcnt();
		
		List<Accounts> accounts = new ArrayList<Accounts>();
		List<CustAcntLink> acntLinks = new ArrayList<CustAcntLink>();
		List<String> acnts = new ArrayList<String>();
		List<String> acntStts = Arrays.asList("00", "01"); // 帳號已綁定狀態清單
		
		// ========== 發電文取得會員狀態及相關資料 ==========
		rtnCode = this.sendTelegramFetchCIF(aclink);
		if (!StringUtil.isBlank(rtnCode)) {
			LOG.info("========== 取得會員狀態及相關資料: " + rtnCode + " ==========");
			if(rtnCode.contains(",")){
				//v1.09, 調整拆解順序, 避免造成ArrayIndexOutOfBoundsException
				hostCode = rtnCode.split(",")[1];
				rtnCode = rtnCode.split(",")[0];
			}
			rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
			resBesn = this.compseResponseObj(rtnCode, rtnMsg, aclink);
			// 新增連結紀錄 log 並發 mail
			this.saveCustAcntLog(aclink, null, rtnCode, "", "", "01", conn, hostCode);
			return resBesn;
		}
		
		// ========== check service status ==========
		rtnCode = this.checkServiceStts(aclink, conn);
		if (!StringUtil.isBlank(rtnCode)) {
			LOG.info("========== 服務狀態檢核失敗: " + rtnCode + " ==========");
			rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
			resBesn = this.compseResponseObj(rtnCode, rtnMsg, aclink);
			// 新增連結紀錄 log 並發 mail
			this.saveCustAcntLog(aclink, null, rtnCode, "", "", "01", conn, "");
			return resBesn;
		}
		
		// ========== 取得已綁定清單 ==========
		acntLinks = this.fetchCustAcntLinkByKey(aclink.getCustId(), aclink.getEcId(), aclink.getEcUser(), conn);
		
		// ========== 發電文取得會員銀行存款帳號清單 ==========
		rtnCode = this.sendTelegramFetchAccount(aclink, acnts);  
		if (!StringUtil.isBlank(rtnCode)) {
			LOG.info("========== 取得會員銀行存款帳號清單: " + rtnCode + " ==========");
			if(rtnCode.contains(",")){
				//v1.09, 調整拆解順序, 避免造成ArrayIndexOutOfBoundsException
				hostCode = rtnCode.split(",")[1];
				rtnCode = rtnCode.split(",")[0];
			}
			rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
			resBesn = this.compseResponseObj(rtnCode, rtnMsg, aclink);
			// 新增連結紀錄 log 並發 mail
			this.saveCustAcntLog(aclink, null, rtnCode, "", "", "01", conn, hostCode);
			return resBesn;
		}
		
		LOG.info("[acnts]: " + acnts);
		
		bindingAcnt.setAcnts(accounts);
		
		// ========== 已綁定帳號註記處理 ==========
		for (String acnt : acnts) {
			Accounts temp = null;
			String isUse = "N";
			
			for (CustAcntLink acntLink : acntLinks) {
				
				if (acnt.equals(acntLink.REAL_ACNT)) {
					// 檢核帳號狀態是否為 00(啟用) 或 01(暫停) 視為已綁定 
					if (acntStts.contains(acntLink.STTS)) {
						isUse = "Y";
					} 
					break;
				} 
			}
			
			temp = new Accounts(acnt, isUse);
			accounts.add(temp);
		}

		aclink.setBinding(bindingAcnt);
		session.setAttribute("link", aclink);
    	
    	return resBesn;
    }
    
    /**
     * 晶片卡驗證結果解密
     * @param input
     * @param aclink
     * @param conn
     * @return ACLinkResBeanTwo or null
     * @throws UtilException
     * @throws DBException
     */
    private ACLinkResBeanTwo decryptVerifyJSON(String input, ACLink aclink, Connection conn) throws UtilException, DBException {
    	
    	ACLinkResBeanTwo resBesn = null;
    	
    	String rtnCode = "";  // 錯誤代碼
    	String rtnMsg = "";   // 錯誤訊息
    	String result = "";   // 驗證結果 0: 成功, 1: 失敗
    	String errorMsg = ""; // 驗證結果訊息
    	String output = "";   // 解密後 JSON 字串
    	String key = "";	  // AES KEY(32 bytes)
    	String iv = "";		  // AES IV(16 bytes)
    	String sKey = "";	  // 驗證用 SessionKey
    	
    	// v1.10
    	String hostCode = "";   // 自訂錯誤代碼
    	
    	SysParm authWsAesKey = null;
    	SysParm authWsAesIv = null;
    	SysParmHome parmHome = new SysParmHome(conn);

    	Map<String, String> jsonStr = new HashMap<String, String>();
    	
		// 晶片卡登入-驗證結果解密
    	try {
    		authWsAesKey = parmHome.fetchSysParmByKey("AUTH_WS_AES_KEY");
    		authWsAesIv = parmHome.fetchSysParmByKey("AUTH_WS_AES_IV");
    		
    		if (StringUtil.isBlank(authWsAesKey) || StringUtil.isBlank(authWsAesIv)) {
				LOG.info("[取得 AES KEY or AES IV 失敗]");
			} else {
				key = authWsAesKey.PARM_VALUE;
    			iv  = authWsAesIv.PARM_VALUE;
			}
    		
    		output =  CommonUtil.aesDecrypt(input, key, iv);
    		
    		LOG.info("========== [解密後字串]: " + output + " ==========");
    		
    		jsonStr = (Map<String, String>) JsonUtil.json2Object(output, Map.class);
    		result = jsonStr.get("RESULT");
    		errorMsg = jsonStr.get("ERRORMSG");
    		sKey = jsonStr.get("SKEY");
    		
    		LOG.info("========== [晶片卡驗證結果]: " + result + " [錯誤訊息]: " + errorMsg +" ==========");
    		
    		// 檢核 是否為當前的使用者
    		if (!sKey.equals(aclink.getSessionKey())) {
    			LOG.info("========== 使用者檢核失敗 [SKEY]: " + sKey + ", [SessionKey]: " + aclink.getSessionKey() + " ==========");
    			rtnCode = "5024";
    			rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
    			resBesn = this.compseResponseObj(rtnCode, rtnMsg, aclink);
    			// 新增連結紀錄 log 並發 mail
    			this.saveCustAcntLog(aclink, null, rtnCode, "", "", "01", conn, "");
    			return resBesn;
			}
    		
    		// v1.10, 晶片卡驗證結果, 0: 失敗, 1: 成功, 2: 首登, 3: 取消
    		if (!"1".equals(result)) {
    			TbCode tbCode = null;
				TbCodeHelper codeHelper = new TbCodeHelper();
				
				tbCode = codeHelper.fetchTbCodeByErrorMsg(errorMsg);
				
				if (!StringUtil.isBlank(tbCode)) {
					rtnCode = tbCode.REF_CODE_ID.substring(3);
					hostCode = tbCode.CODE_ID.substring(3);
					
				} else {
					LOG.warn("[查無對應網銀訊息]: " + errorMsg);
					rtnCode = "9200";
				}
    		
				rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
				resBesn = this.compseResponseObj(rtnCode, rtnMsg, aclink);
				// 新增連結紀錄 log 並發 mail
				this.saveCustAcntLog(aclink, null, rtnCode, "", "", "01", conn, hostCode);
				
				return resBesn;
    		}
	
    	} catch (Exception e) {
    		LOG.error("[晶片卡驗證 JSON 字串解密失敗]", e);
			rtnCode = "9200";
			rtnMsg = new TbCodeHelper(rtnCode, "01").getTbCodeMsg();
			resBesn = this.compseResponseObj(rtnCode, rtnMsg, aclink);
			// 新增連結紀錄 log 並發 mail
			this.saveCustAcntLog(aclink, null, rtnCode, "", "", "01", conn, "");
			return resBesn;
			
		}
    	
    	aclink.setCustId(jsonStr.get("ID"));
    	aclink.setIdentityType(jsonStr.get("TYPE"));
    	
    	return resBesn;
    }
    
    /**
     * 檢核連結綁定必輸資料
     * @param acLink
     * @throws Exception
     * @return return 0: 檢核成功, 大於0 檢核失敗
     * @since v1.3
     * 
     */
    private int validateBindingVal(ACLink acLink) throws Exception {
    	int rslt = 0;
    	
    	List<String> authType = Arrays.asList("00", "01", "02");
    	
    	// 檢核驗證方式是否為 00, 01, 02
    	if (!authType.contains(acLink.getIdetityAuthType())) {
    		LOG.error("[身分認證方式非 00, 01, 02]"+ acLink.getIdetityAuthType());
    		// v1.08
    		acLink.set_error("NG");
    		rslt++;
		}
    	
    	// 檢綁定連結帳號是否為空白
    	if(StringUtil.isBlank(acLink.getLinkAcnt())) {
    		LOG.error("[綁定連結帳號為空白]: " + acLink.getLinkAcnt());
    		// v1.08
    		acLink.set_error("NG");
    		rslt++;
    	}
    	
    	return rslt;
    }
    
    /**
     * 網銀 & 晶片卡登入認證失敗, 錯誤訊息與錯誤代碼 Mapping
     * @param type 	   認證方式 0: 網銀, 1: 晶片卡
     * @param errorMsg 錯誤訊息
     * @return 錯誤代碼 
     * @since v1.10
     */
    private String errorCodeMapping(String type, String errorMsg) {
    	
    	
    	return "";
    }
    
    public LoginResBean httpsPost() {
		try {
			String IBANK_MERCHANTID = APSystem.getProjectParam("IBANK_MERCHANTID");
			String IBANK_RETURNURL = APSystem.getProjectParam("IBANK_RETURNURL");
			String IBANK_KEY = APSystem.getProjectParam("IBANK_KEY");
			String IBANK_0201 = APSystem.getProjectParam("IBANK_0201");
			String IBANK_0202 = APSystem.getProjectParam("IBANK_0202");
			
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}
			} };
	    	
	    	SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			
			
			String merchantId = IBANK_MERCHANTID;//商戶ID
			String returnUri = IBANK_RETURNURL;
			String key = IBANK_KEY;
    		long unixTime = System.currentTimeMillis() / 1000L;
    		
        	URL url = new URL(IBANK_0201);
        	String urlParameters  = "callbackTarget=_self&callbackUri="+returnUri+"&fnct=2&merchantId="+merchantId+"&timestamp="+unixTime+"&version=1";
    		String sign = org.apache.commons.codec.digest.DigestUtils.sha1Hex(urlParameters + "&key="+key);
    		urlParameters = urlParameters + "&sign="+sign;
    		
			byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int postDataLength = postData.length;
            
        	HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
    		conn.setRequestMethod( "POST" );
            conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty( "charset", "utf-8");
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setInstanceFollowRedirects( false );
            conn.setDoOutput( true );
            
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            
    		int responseCode = conn.getResponseCode();

    		LOG.info("Sending 'POST' request to ibankURL : " + url);
            LOG.info("Post parameters : " + urlParameters);
            LOG.info("Response Code : " + responseCode);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            
            while ((line = br.readLine()) != null) {
            	result.append(line+"\n");
            }
            
            br.close();
            
            LOG.info("WEB return value is : " + result);
            
            LoginResBean loginResBean = (LoginResBean) JsonUtil.json2Object(result.toString(), LoginResBean.class);
            urlParameters  = "merchantId="+merchantId+"&txReqId="+loginResBean.getTxReqId();
            sign = DigestUtils.sha1Hex(urlParameters + "&key="+key);
            loginResBean.setSign(sign);
            loginResBean.setMerchantId(merchantId);
            loginResBean.setUri0202(IBANK_0202);
            LOG.info("merchantId : " + loginResBean.getMerchantId());
            LOG.info("TxReqId : " + loginResBean.getTxReqId());
            LOG.info("sign : " + loginResBean.getSign());
            return loginResBean;
		}catch (Exception e){
            e.printStackTrace();
        }
		return null;
	}   
    
    //V2.01, 2018/02/05 新增可設定電商最大可綁定人數(先借用 EC_DATA.NOTE 欄位)，超過綁定人數，則回覆1999-超過電商最大可綁定人數 Begin
    //V2.02, 2018/03/19 新增可設定電商最大可綁定人數(改抓EC_DATA.NOTE 欄位)，超過綁定人數，則回覆1999-超過電商最大可綁定人數 Begin
    /**
     * 檢核電商最大綁定人數
     * @param ecId
     * @param max_count
     * @param conn
     * @return
     * @throws DBException
     */
    private String checkMaxLinkedCount(String ecId, String custId, Connection conn) throws DBException {
	    EcDataHome ecDataHome = new EcDataHome(conn); 
	    CustAcntLinkHome custAcntLinkHome = new CustAcntLinkHome(conn);
		String rtn_code="";
		//取得電商最大可綁定人數
	    EcData ecData = ecDataHome.fetchEcDataByKey(ecId);
//	    int max_count = Integer.parseInt(ecData.NOTE);//借用NOTE 欄位存放最大可綁定人數
	    
	    //取得電商以綁定人數
	    int linked_count = custAcntLinkHome.fetchCustAcntLinkedCountByKey(ecId, custId); 
	    
	    if (ecData.LINK_LIMIT != null && ecData.LINK_LIMIT != 0 && linked_count >= ecData.LINK_LIMIT) {
			LOG.info("電商代碼:" + ecId+ "custId:" + custId + "以有綁定之用戶");
			rtn_code = "1999";
	    }

		return rtn_code;
    }
    //V2.02, 2018/03/19 電商最大可綁定人數改抓EC_DATA.LINK_LIMIT End
    //V2.01, 2018/02/05 新增可設定電商最大可綁定人數(改抓EC_DATA.NOTE 欄位)，超過綁定人數，則回覆1999-超過電商最大可綁定人數 End
    
}
