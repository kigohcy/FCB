/**
 * @(#)TrnsLimitsMgmtSrvImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : TrnsLimitsMgmtSrvImpl
 * 
 * Modify History:
 *  v1.00, 2016/02/18, Evan
 *   1) First release
 *  v1.01, 2016/07/27, Yann
 *   1) 增加限閱戶權限控管
 *  v1.02, 2018/03/26
 *   1) 增加可用餘額顯示
 *  
 */
package com.hitrust.bank.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.dao.BaseLimtDAO;
import com.hitrust.bank.dao.CustAcntDAO;
import com.hitrust.bank.dao.CustAcntLinkDAO;
import com.hitrust.bank.dao.CustDataDAO;
import com.hitrust.bank.dao.CustPltfDAO;
import com.hitrust.bank.dao.DayCrdtContDAO;
import com.hitrust.bank.dao.MnthCrdtContDAO;
import com.hitrust.bank.json.TsbAuditLog;
import com.hitrust.bank.json.TsbAuditLogDetl;
import com.hitrust.bank.model.BaseLimt;
import com.hitrust.bank.model.CustAcnt;
import com.hitrust.bank.model.CustAcntLink;
import com.hitrust.bank.model.CustData;
import com.hitrust.bank.model.CustPltf;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.base.AbstractCustAcnt;
import com.hitrust.bank.model.base.AbstractCustAcntLink;
import com.hitrust.bank.model.base.AbstractCustPltf;
import com.hitrust.bank.service.TrnsLimitsMgmtSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.util.DateUtil;

public class TrnsLimitsMgmtSrvImpl implements TrnsLimitsMgmtSrv {
	
	// Log4j
	private static Logger LOG = Logger.getLogger(TrnsLimitsMgmtSrvImpl.class);
	
	// DAO injection
	private CustDataDAO custDataDAO;
	private CustPltfDAO	custPltfDAO;
	private CustAcntLinkDAO	custAcntLinkDAO;
	private CustAcntDAO custAcntDAO;
	private BaseLimtDAO baseLimtDAO;
	//v1.02 增加可用餘額顯示
	private MnthCrdtContDAO mnthCrdtContDAO;
	private DayCrdtContDAO dayCrdtContDAO;
	//v1.02 增加可用餘額顯示 End
	
	public void setCustDataDAO(CustDataDAO custDataDAO) {
		this.custDataDAO = custDataDAO;
	}

	public void setCustPltfDAO(CustPltfDAO custPltfDAO) {
		this.custPltfDAO = custPltfDAO;
	}

	public void setCustAcntLinkDAO(CustAcntLinkDAO custAcntLinkDAO) {
		this.custAcntLinkDAO = custAcntLinkDAO;
	}

	public void setCustAcntDAO(CustAcntDAO custAcntDAO) {
		this.custAcntDAO = custAcntDAO;
	}

	public void setBaseLimtDAO(BaseLimtDAO baseLimtDAO) {
		this.baseLimtDAO = baseLimtDAO;
	}
	//v1.02 增加可用餘額顯示
	public void setMnthCrdtContDAO(MnthCrdtContDAO mnthCrdtContDAO) {
		this.mnthCrdtContDAO = mnthCrdtContDAO;
	}

	public void setDayCrdtContDAO(DayCrdtContDAO dayCrdtContDAO) {
		this.dayCrdtContDAO = dayCrdtContDAO;
	}
	//v1.02 增加可用餘額顯示 End
	/**
	 * 交易限額查詢服務
	 * @param custAcnt command
	 */
	@Override
	public void queryTrnsLimitsSrvData(CustAcnt custAcnt) {
		
		List<CustPltf> custPltfList = null;	//綁定的平台資料
		List<CustAcnt> custAcntList = null;	//會員帳號資料
		List<BaseLimt> baseLimtList = null;	//限額資料
		HashMap<String, BaseLimt> baseLimtMap = null;	//限額資料
		HashMap<String, List<CustAcntLink>> hashMap = null;	//平台對應的綁定帳號
		String custId = custAcnt.getCustId();	//身分證字號
		
		//沒有operate表示是 按確認或上一頁的查詢
		if(StringUtil.isBlank(custAcnt.getOperate())){
			custId = custAcnt.getCustData().getCustId();
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
			baseLimtMap = new HashMap<String, BaseLimt>();
			hashMap = new HashMap<String, List<CustAcntLink>>();
			
			//取會員已綁定的各平台資料
			custPltfList = custPltfDAO.getEcDataByCust(custData.getCustId());
			
			//取得會員綁定之帳號資料
			custAcntList = custAcntDAO.getCustAcntByCustId(custData.getCustId());
			//v1.02 增加可用餘額顯示
			String trnsDay =DateUtil.getCurrentTime("DT", "AD").substring(0,8);
			Long usedAmount = null;
			for(CustAcnt custAcntData:custAcntList){
				usedAmount = mnthCrdtContDAO.getMnthSumByAcnt(trnsDay.substring(0,6), custId, custAcntData.getId().getRealAcnt(), custData.getCustSerl(), null, null);
				custAcntData.setUsedAmountMonth(usedAmount);
				if(custAcntData.getMnthLimt() == 0){
					custAcntData.setAvailableBalanceMonth(null);
				}else{
					custAcntData.setAvailableBalanceMonth(custAcntData.getMnthLimt() - usedAmount);
				}
				usedAmount = dayCrdtContDAO.getDaySumByCustIdAndAcnt(trnsDay, custId, custAcntData.getId().getRealAcnt(), custData.getCustSerl(), null, null);
				custAcntData.setUsedAmountDay(usedAmount);
				if(custAcntData.getMnthLimt() == 0){
					custAcntData.setAvailableBalanceDay(null);
				}else{
					custAcntData.setAvailableBalanceDay(custAcntData.getDayLimt() - usedAmount);
				}
			}
			//取得會員綁定的各平台之帳號資料
			for(CustPltf  plft : custPltfList){
				List<CustAcntLink> list = custAcntLinkDAO.getCustAcntLink(plft.getId().getCustId(), plft.getId().getEcId());
				for(CustAcntLink custAcntLinkData:list){
					usedAmount = mnthCrdtContDAO.getMnthSumByAcnt(trnsDay.substring(0,6), custId, custAcntLinkData.getId().getRealAcnt(), 
							custData.getCustSerl(), custAcntLinkData.getId().getEcId(), custAcntLinkData.getId().getEcUser());
					custAcntLinkData.setUsedAmountMonth(usedAmount);
					if(custAcntLinkData.getMnthLimt() == 0){
						custAcntLinkData.setAvailableBalanceMonth(null);
					}else{
						custAcntLinkData.setAvailableBalanceMonth(custAcntLinkData.getMnthLimt() - usedAmount);
					}
					usedAmount = dayCrdtContDAO.getDaySumByCustIdAndAcnt(trnsDay, custId, custAcntLinkData.getId().getRealAcnt(), 
							custData.getCustSerl(), custAcntLinkData.getId().getEcId(), custAcntLinkData.getId().getEcUser());
					custAcntLinkData.setUsedAmountDay(usedAmount);
					if(custAcntLinkData.getMnthLimt() == 0){
						custAcntLinkData.setAvailableBalanceDay(null);
					}else{
						custAcntLinkData.setAvailableBalanceDay(custAcntLinkData.getDayLimt() - usedAmount);
					}
				}
				hashMap.put(plft.getId().getEcId(), list);
			}
			//v1.02 增加可用餘額顯示 End
			
			//核定限額資料檔 
			baseLimtList = baseLimtDAO.getBaseLimtList();
			
			for(BaseLimt baseLimt : baseLimtList){
				baseLimtMap.put(baseLimt.getGrad(), baseLimt);
			}
		}
		
		//把查詢結果放回command
		custAcnt.setCustData(custData);
		custAcnt.setCustPltfList(custPltfList);
		custAcnt.setCustAcntList(custAcntList);
		custAcnt.setBaseLimt(baseLimtMap);
		custAcnt.setCustAcntLink(hashMap);
	}
	
	/**
	 * 修改交易限額
	 * @param custAcnt	command
	 */
	@Override
	public void updateTrnsLimtAcnt(CustAcnt custAcnt) {
		
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		
		int updateCount = 0;
		
		//記錄應用系統日誌
		TsbAuditLog log = new TsbAuditLog();	
		TsbAuditLogDetl logDetl = null;
		//設定交易log
		custAcnt.setFnKeyValue(custAcnt.getCustData().getCustId());
		log.setTsbAuditLogDetl(new ArrayList<>());
		
		if("showCustAcnt".equals(custAcnt.getSelectEcId())){//update cust_acnt
			
			CustAcnt updateCustAcnt = null;
			
			updateCount = custAcnt.getCustAcntRealAcnt().length;
			
			if(updateCount >0){
				
				for(int i=0; i<updateCount; i++){
					
					CustAcnt.Id id = new AbstractCustAcnt.Id();
					id.setCustId(custAcnt.getCustData().getCustId());
					id.setRealAcnt(custAcnt.getCustAcntRealAcnt()[i]);
					
					//記錄應用系統日誌(TSB_APAUDITLOG),只記五筆
					if(log.getTsbAuditLogDetl().size() <=5){
						logDetl = new TsbAuditLogDetl();
						logDetl.setA(id.getRealAcnt());
						logDetl.setT(String.valueOf(this.checkLimtValue(custAcnt.getCustAcntTrnsLmt(), i)));
						logDetl.setD(String.valueOf(this.checkLimtValue(custAcnt.getCustAcntDayLmt(), i)));
						logDetl.setM(String.valueOf(this.checkLimtValue(custAcnt.getCustAcntMnthLmt(), i)));
						log.getTsbAuditLogDetl().add(logDetl);
					}
					
					//check cust_acnt  is exit
					updateCustAcnt  =  (CustAcnt) custDataDAO.queryById(CustAcnt.class, id);
					
					if(updateCustAcnt == null){
						LOG.info("資料庫 cust_acnt 無custId:"+id.getCustId()+", realAcnt:"+id.getRealAcnt()+"的資料,無法修改");
						throw new BusinessException("message.db.have.no.data");
					}else{
						//設定cust_acnt限額
						updateCustAcnt.setTrnsLimt(this.checkLimtValue(custAcnt.getCustAcntTrnsLmt(), i));	//單筆限額
						updateCustAcnt.setDayLimt(this.checkLimtValue(custAcnt.getCustAcntDayLmt(), i));	//每日限額
						updateCustAcnt.setMnthLimt(this.checkLimtValue(custAcnt.getCustAcntMnthLmt(), i));	//每月限額
						updateCustAcnt.setMdfyUser(user.getUserId());
						updateCustAcnt.setMdfyDttm(new Date());
						
						try{
							custAcntDAO.update(updateCustAcnt);
						}catch(Exception e){
							LOG.error("修改交易限額失敗", e);
							throw new BusinessException("message.sys.limit.failure");	//限額設定失敗
						}
					}
				}
			}
			
		}else{//update cust_acnt_link
			CustAcntLink updateCustAcntLink = null;
			updateCount = custAcnt.getCustAcntLinkEcUser().length;
			
			if(updateCount >0){
				
				log.setEcId(custAcnt.getSelectEcId());
				
				for(int i=0; i<updateCount; i++){
					
					CustAcntLink.Id id = new AbstractCustAcntLink.Id();
					id.setCustId(custAcnt.getCustData().getCustId());
					id.setEcId(custAcnt.getSelectEcId());
					id.setRealAcnt(custAcnt.getCustAcntLinkRealAcnt()[i]);
					id.setEcUser(custAcnt.getCustAcntLinkEcUser()[i]);
					
					if(i == 0){
						//記錄應用系統日誌 設定 平台Id
						log.setEcId(id.getEcId());
					}
					
					//記錄應用系統日誌(TSB_APAUDITLOG),只記五筆
					if(log.getTsbAuditLogDetl().size() <=5){
						logDetl = new TsbAuditLogDetl();
						logDetl.setU(id.getCustId());
						logDetl.setA(id.getRealAcnt());
						logDetl.setT(String.valueOf(this.checkLimtValue(custAcnt.getCustAcntLinkTrnsLmt(), i)));
						logDetl.setD(String.valueOf(this.checkLimtValue(custAcnt.getCustAcntLinkDayLmt(), i)));
						logDetl.setM(String.valueOf(this.checkLimtValue(custAcnt.getCustAcntLinkMnthLmt(), i)));
						log.getTsbAuditLogDetl().add(logDetl);
					}
					
					//check cust_acnt  is exit
					updateCustAcntLink = (CustAcntLink) custAcntLinkDAO.queryById(CustAcntLink.class, id);
					
					if(updateCustAcntLink == null ){
						LOG.info("資料庫 cust_acnt_link 無custId:"+id.getCustId()+", ecId:"+id.getEcId()+",ecUser:"+id.getEcUser()+",realAcnt:"+id.getRealAcnt()+"的資料,無法修改");
						throw new BusinessException("message.db.have.no.data");
					}else{
						//設定cust_acnt_link 的限額
						updateCustAcntLink.setTrnsLimt(this.checkLimtValue(custAcnt.getCustAcntLinkTrnsLmt(), i));
						updateCustAcntLink.setDayLimt(this.checkLimtValue(custAcnt.getCustAcntLinkDayLmt(), i));
						updateCustAcntLink.setMnthLimt(this.checkLimtValue(custAcnt.getCustAcntLinkMnthLmt(), i));
						updateCustAcntLink.setMdfyUser(user.getUserId());
						updateCustAcntLink.setMdfyDttm(new Date());
						try{
							custAcntLinkDAO.update(updateCustAcntLink);
						}catch(Exception e){
							LOG.error("修改交易限額失敗", e);
							throw new BusinessException("message.sys.limit.failure");	//限額設定失敗
						}
					}
				}
			}
		}
		//記錄應用系統日誌
		custAcnt.setFnProc(JsonUtil.object2Json(log, false));
	}

	/**
	 * 記錄更新後結果
	 * @param custAcnt	command
	 */
	@Override
	public void recordAfterOpt(CustAcnt custAcnt) {
		
		//取會員資料檔 
		CustData custData = (CustData) custDataDAO.queryById(CustData.class, custAcnt.getCustData().getCustId());
		
		if("showCustAcnt".equals(custAcnt.getSelectEcId())){//銀行存款帳號交易限額
			//取得會員綁定之帳號資料
			List<CustAcnt> list = custAcntDAO.getCustAcntByCustId(custData.getCustId());
			custAcnt.setOptCustAcntList(list);
		}else{//平台連結帳號交易限額
			
			//取會員已綁定的各平台資料
			CustPltf custPltf = null;
			CustPltf.Id  id = new AbstractCustPltf.Id();
			id.setCustId(custAcnt.getCustData().getCustId());
			id.setEcId(custAcnt.getSelectEcId());
			custPltf = (CustPltf) custPltfDAO.queryById(CustPltf.class, id);
			//取得會員綁定的各平台之帳號資料
			List<CustAcntLink> list = custAcntLinkDAO.getCustAcntLink(custPltf.getId().getCustId(), custPltf.getId().getEcId());
			custAcnt.setOptCustAcntLink(list);
		}
	}
	
	/**
	 * check String array is null  or array[index] is empty
	 * @param array	限額陣列
	 * @param index	對應修改限額的 index	
	 * @return  如果陣列是空的 回傳 0,不為空則回傳 陣列對應的index的值
	 */
	private long checkLimtValue(String [] array, int index){
		
		if(array.length == 0){
			return 0;
		}
		
		if(StringUtil.isBlank(array[index])){ //陣列是空的 回傳 0
			return 0;
		}else{ 
			return Long.valueOf(array[index]);
		}
	}
}
