/**
 * @(#)SrvSttsMgmtSrvImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 服務狀態管理 serviceImpl
 * 
 * Modify History:
 *  v1.00, 2016/02/16, Evan
 *   1) First release
 *  v1.01, 2016/07/27, Yann
 *   1) 增加限閱戶權限控管
 *  v1.02, 2016/12/21, Yann
 *   1) TSBACL-143, 未綁定解鎖
 *  
 */
package com.hitrust.bank.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

import com.hitrust.acl.common.I18nConverter;
import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.common.UUIDGen;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.CustAcntLinkDAO;
import com.hitrust.bank.dao.CustDataDAO;
import com.hitrust.bank.dao.CustPltfDAO;
import com.hitrust.bank.json.TsbAuditLog;
import com.hitrust.bank.json.TsbAuditLogDetl;
import com.hitrust.bank.model.CustAcntLink;
import com.hitrust.bank.model.CustAcntLog;
import com.hitrust.bank.model.CustData;
import com.hitrust.bank.model.CustPltf;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.base.AbstractCustAcntLink;
import com.hitrust.bank.model.base.AbstractCustPltf;
import com.hitrust.bank.service.SrvSttsMgmtSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;

public class SrvSttsMgmtSrvImpl implements SrvSttsMgmtSrv {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(SrvSttsMgmtSrvImpl.class);
	
	// DAO injection
	private CustDataDAO custDataDAO;
	private CustPltfDAO	custPltfDAO;
	private CustAcntLinkDAO	custAcntLinkDAO;
	
	public void setCustDataDAO(CustDataDAO custDataDAO) {
		this.custDataDAO = custDataDAO;
	}

	public void setCustPltfDAO(CustPltfDAO custPltfDAO) {
		this.custPltfDAO = custPltfDAO;
	}

	public void setCustAcntLinkDAO(CustAcntLinkDAO custAcntLinkDAO) {
		this.custAcntLinkDAO = custAcntLinkDAO;
	}
	
	/**
	 * 查詢服務狀態
	 * @param CustPltf  Command
	 */
	public void querySrvSttsData(CustPltf custPltf) {
		
		List<CustPltf> custPltfList = null;	//會員帳號資料
		HashMap<String, List<CustAcntLink>> hashMap = null;	//平台對應的綁定帳號
		String custId = custPltf.getCustId();	//身分證字號
		
		//沒有operate表示是 按確認或上一頁的查詢
		if(StringUtil.isBlank(custPltf.getOperate())){
			custId = custPltf.getCustData().getCustId();
		}
		
		//取會員資料檔 
		CustData custData = (CustData) custDataDAO.queryById(CustData.class, custId);
		
		//v1.01, 增加限閱戶權限控管
//		LoginUser user = (LoginUser) APLogin.getCurrentUser();
//		List<String> viewLimitIds = user.getViewLimitIds();
//		if(viewLimitIds.contains(custId)){
//			LOG.debug("custId is viewLimitId..");
//			custData = null;
//		}
		
		if(custData != null){
			//取會員已綁定的各平台資料
			custPltfList = custPltfDAO.getEcDataByCust(custData.getCustId());
			
			//取得會員綁定的各平台之帳號資料
			hashMap = new HashMap<String, List<CustAcntLink>>();
			
			for(CustPltf  plft : custPltfList){
				List<CustAcntLink> list = custAcntLinkDAO.getCustAcntLink(plft.getId().getCustId(), plft.getId().getEcId());
				hashMap.put(plft.getId().getEcId(), list);
			}
		}
		
		//把查詢結果放回command
		custPltf.setCustData(custData);
		custPltf.setCustPltfList(custPltfList);
		custPltf.setCustAcntLink(hashMap);
	}
	
	/**
	 * 修改綁定帳號服務狀態
	 * @param custPltf command
	 */
	public void updateAcntStts(CustPltf custPltf) {
		
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		String[] ecKey =  custPltf.getEcKey();		//綁定平台的checkBox	req.getParameterValues("ecKey");
		String[] ecStts = custPltf.getEcStts();		//綁定的平台的狀態		req.getParameterValues("ecStts");
		String[] acntKey = custPltf.getAcntKey();  	//綁定帳號的checkBox   req.getParameterValues("acntKey");
		String[] acntStts = custPltf.getAcntStts();	//綁定帳號的狀態		req.getParameterValues("acntStts");
		
		String[] custPltfKey = null;	//會員平台資料檔 key
		String[] custAcntLink = null;	//會員帳號連結檔 key
		CustPltf bean = null;
		List<CustAcntLink> list= null;	//平台下的會員綁定帳號
		CustAcntLink acntLink = null;	//會員綁定帳號
		
		//記錄應用系統日誌
		TsbAuditLog log = new TsbAuditLog();	
		TsbAuditLogDetl logDetl = null;
		//設定交易log
		custPltf.setFnKeyValue(custPltf.getCustData().getCustId());
		log.setTsbAuditLogDetl(new ArrayList<>());
		
		//2 平台選項 有勾選
		if("EC".equals(custPltf.getWhoUpdate())){
			for(int i=0; i<ecKey.length; i++){
				//[0]:custId, [1]:ecId =>table:custPltf
				custPltfKey = ecKey[i].split(";");
				//2.1 異動 [會員平台資料檔(CUST_PLTF)]
				CustPltf.Id  id = new AbstractCustPltf.Id();
				id.setCustId(custPltfKey[0]);
				id.setEcId(custPltfKey[1]);
				bean  = (CustPltf) custPltfDAO.queryById(CustPltf.class, id);
				
				if(bean == null){
					LOG.info("資料庫 CustPltf 無custId:"+id.getCustId()+", ecId:"+id.getEcId()+"的資料,無法修改");
					throw new BusinessException("message.db.have.no.data");
				}
				bean.setStts(ecStts[i]);
				bean.setSttsDttm(new Date());
				bean.setMdfyUser(user.getUserId());
				
				try{
					custPltfDAO.update(bean);
				}catch(Exception e){
					LOG.error("修改綁定帳號服務狀態失敗", e);
					throw new BusinessException("message.sys.update.failure");	//資料更新失敗
				}
				
				//TSBACL-66, get 系統時間
				Date date = new Date();
				
				//2.2 Insert 一筆 [會員帳號連結記錄檔(CUST_ACNT_LOG)]
				this.insertCustAcntLog(bean, custPltf.getCustData(), "", date);
				//2.3  取得會員平台下有綁定的帳號，並將帳號同步異動狀態(不含終止)
				list = custAcntLinkDAO.getCustAcntLink(custPltfKey[0], custPltf.getSelectEcId());
				
				//記錄應用系統日誌 設定 平台Id
				log.setEcId(id.getEcId());
				
				for(CustAcntLink acnt:list){
					if("02".equals(acnt.getStts())){//(不含終止)
						continue;
					}
					
					//記錄應用系統日誌(TSB_APAUDITLOG),只記五筆
					if(log.getTsbAuditLogDetl().size() <=5){
						logDetl = new TsbAuditLogDetl();
						logDetl.setA(acnt.getId().getRealAcnt());
						logDetl.setStts(ecStts[i]);
						log.getTsbAuditLogDetl().add(logDetl);
					}
					
					//2.4  異動 [會員帳號連結(CUST_ACNT_LINK)]，狀態STTS、狀態異動時間、最後異動人員='System'、最後異動時間。
					acnt.setStts(ecStts[i]);
					acnt.setSttsDttm(new Date());
					acnt.setMdfyUser(user.getUserId());
					acnt.setMdfyDttm(new Date());
					
					try{
						custAcntLinkDAO.update(acnt);
					}catch(Exception e){
						LOG.error("修改綁定帳號服務狀態失敗", e);
						throw new BusinessException("message.sys.update.failure");	//資料更新失敗
					}
					
					//2.5 Insert [會員帳號連結記錄檔(CUST_ACNT_LOG)]
					this.insertCustAcntLog(acnt, custPltf.getCustData(), "EC", date);
				}
				
			}
		}else if("ACNT".equals(custPltf.getWhoUpdate())){
			
			for(int i=0; i<acntKey.length; i++){
				//[0]:custId, [1]:ecId, [2]:ecUser, [3]:realAcnt =>table:custAcntLink
				custAcntLink = acntKey[i].split(";");
				CustAcntLink.Id custAcntLinkId = new AbstractCustAcntLink.Id();
				custAcntLinkId.setCustId(custAcntLink[0]);
				custAcntLinkId.setEcId(custAcntLink[1]);
				custAcntLinkId.setEcUser(custAcntLink[2]);
				custAcntLinkId.setRealAcnt(custAcntLink[3]);
				
				//記錄應用系統日誌 設定 平台Id
				if(i == 0){
					log.setEcId(custAcntLinkId.getEcId());
				}
				
				//記錄應用系統日誌(TSB_APAUDITLOG),只記五筆
				if(log.getTsbAuditLogDetl().size() <=5){
					logDetl = new TsbAuditLogDetl();
					logDetl.setU(custAcntLinkId.getEcUser());
					logDetl.setA(custAcntLinkId.getRealAcnt());
					logDetl.setStts(acntStts[i]);
					log.getTsbAuditLogDetl().add(logDetl);
				}
				
				
				acntLink = (CustAcntLink) custAcntLinkDAO.queryById(CustAcntLink.class, custAcntLinkId);
							
				if(acntLink == null){
					LOG.info("資料庫 CustAcntLink 無custId:"+custAcntLinkId.getCustId()+", ecId:"+custAcntLinkId.getEcId()+
							", ecUser:"+custAcntLinkId.getEcUser()+", realAcnt:"+custAcntLinkId.getRealAcnt()+"的資料,無法修改");
					throw new BusinessException("message.db.have.no.data");
				}	
				//3.1 異動 [會員帳號連結(CUST_ACNT_LINK)]，狀態STTS、狀態異動時間、最後異動人員=登入人員、最後異動時間
				acntLink.setStts(acntStts[i]);
				acntLink.setSttsDttm(new Date());
				acntLink.setMdfyUser(user.getUserId());
				acntLink.setMdfyDttm(new Date());
				
				try{
					custAcntLinkDAO.update(acntLink);
				}catch(Exception e){
					LOG.error("修改綁定帳號服務狀態失敗", e);
					throw new BusinessException("message.sys.update.failure");	//資料更新失敗
				}
				
				//3.2 Insert [會員帳號連結記錄檔(CUST_ACNT_LOG)]
				this.insertCustAcntLog(acntLink, custPltf.getCustData(), "ACNT", new Date());
			}
		}
		
		//記錄應用系統日誌(
		custPltf.setFnProc(JsonUtil.object2Json(log, false));
	}
	/**
	 * 新增會員帳號連結記錄
	 * @param bean	會員綁定帳號
	 * @param custData	會員資料檔 
	 * @param type  EC:台選項有勾選、ACNT:帳號選項有勾選
	 * @param date 系統時間
	 */
	private void insertCustAcntLog(Serializable bean, CustData custData, String type, Date date){
		
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		CustAcntLog custAcntLog = new CustAcntLog();
		custAcntLog.setLogNo(UUIDGen.genUUID());
		 
		if(bean instanceof CustPltf){
			custAcntLog.setCustId(((CustPltf)bean).getId().getCustId());
			custAcntLog.setEcId(((CustPltf)bean).getId().getEcId());
			custAcntLog.setStts(this.sttsForCustAcntLog(((CustPltf)bean).getStts()));
			custAcntLog.setCustSerl(custData.getCustSerl());
			custAcntLog.setExecSrc("C");//客服系統
		}else if (bean instanceof CustAcntLink){
			custAcntLog.setCustId(((CustAcntLink)bean).getId().getCustId());
			custAcntLog.setEcId(((CustAcntLink)bean).getId().getEcId());
			custAcntLog.setEcUser(((CustAcntLink)bean).getId().getEcUser());
			custAcntLog.setRealAcnt(((CustAcntLink)bean).getId().getRealAcnt());
			custAcntLog.setGrad(((CustAcntLink)bean).getGrad());
			custAcntLog.setGradType(((CustAcntLink)bean).getGradType());
			custAcntLog.setStts(this.sttsForCustAcntLog(((CustAcntLink)bean).getStts()));
			custAcntLog.setCustSerl(custData.getCustSerl());
			custAcntLog.setAcntIndt(((CustAcntLink)bean).getAcntIndt());
			if("EC".equals(type)){
				custAcntLog.setExecSrc("D"); //系統 
			}else if("ACNT".equals(type)){
				custAcntLog.setExecSrc("C"); //客服系統
			}
		}
		
		custAcntLog.setCretDttm(date);
		custAcntLog.setExecUser(user.getUserId());
		
		try{
			custAcntLinkDAO.save(custAcntLog);
		}catch(Exception e){
			LOG.error("會員帳號連結記錄檔新增失敗", e);
			throw new BusinessException("message.sys.insert.failure");	//資料新增失敗
		}
	}
	
	/**
	 * 記錄更新後結果
	 * @param custPltf
	 */
	public void recordAfterOpt(CustPltf custPltf) {
		
		//更新後的會員資料
		custPltf.setAfterCustData((CustData)custDataDAO.queryById(CustData.class, custPltf.getCustData().getCustId()));
		//更新後的綁定連結帳號
		custPltf.setAfterAcntLink(custAcntLinkDAO.getCustAcntLink(custPltf.getCustData().getCustId(), custPltf.getSelectEcId()));
		//更新後的綁定平台資料
		List<CustPltf>	list = custPltfDAO.getEcDataByCust(custPltf.getCustData().getCustId());
		for(CustPltf pltf: list){
			if(custPltf.getSelectEcId().equals(pltf.getId().getEcId())){
				custPltf.setAfterCustPltf(pltf);
				break;
			}
		}
	}
	
	/**
	 * 對應會員帳號連結記錄檔  執行狀態 
	 * @param stts
	 * @return
	 */
	private String sttsForCustAcntLog(String stts){
		
		if("00".equals(stts)){
			stts = "04";	//啟用
		}else if("01".equals(stts)){
			stts = "05";    //暫停
		}
		
		return stts;
	}
	
	/**
	 * 修改 custData 服務狀態
	 * @param stts  00:啟用, 01:暫停
	 * @param custSttId
	 * @return 俢改回覆訊息
	 */
	public String updateCustStts(String stts, String custSttId){
		
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		CustData  custData = (CustData) custDataDAO.queryById(CustData.class, custSttId);
		String message = I18nConverter.i18nMapping("message.sys.0003", user.getLocale()); 	//暫停成功
		
		if(custData == null){
			return	I18nConverter.i18nMapping("message.sys.NoData", user.getLocale());//查無符合條件資料
		}else{
			
			if((stts).equals(custData.getStts())){
				return I18nConverter.i18nMapping("message.sys.srvSttsError", user.getLocale());	//會員服務狀態錯誤
			}
			custData.setStts(stts); 
			custData.setMdfyUser(user.getUserId());
			custData.setMdfyDttm(new Date());
			custData.setSttsDttm(new Date());
			
			try{
				custDataDAO.update(custData);
			}catch(Exception e){
				LOG.error("修改 custData 服務狀態失敗", e);
				throw new BusinessException("message.sys.update.failure");	//資料更新失敗
			}
			
			if("00".equals(stts)){
				message = I18nConverter.i18nMapping("message.sys.0002", user.getLocale()); 	//啟用成功
			}
			
			return message;
		}
	}
	
	/**v1.02
	 * 查詢會員資料檔
	 * @param custId
	 * @return
	 */
	public CustData queryCustData(String custId) {
		CustData custData = (CustData) custDataDAO.queryById(CustData.class, custId);
		return custData;
	}
}
