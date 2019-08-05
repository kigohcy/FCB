/**
 * @(#)EcDataMgmtSrv.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 電商平台管理ECDataMngrSrv
 * 
 * Modify History:
 *  v1.00, 2016/01/29, Evan
 *   1) First release
 *  
 */
package com.hitrust.bank.service;

import java.util.HashMap;
import java.util.List;

import com.hitrust.bank.model.EcData;
import com.hitrust.bank.model.EcDataAprv;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.base.AbstractEcDataAprv;

public interface EcDataMgmtSrv {
	
	/**
	 * 查詢所有電商平台資料
	 * @return List<EcData> 有電商平台資料
	 */
	public List<EcData> queryEcDataList();
	
	/**
	 * 依據畫面資料新增電商平台資料
	 * @param hashMap 將畫面料資封裝 
	 */
	public void insertEcData(HashMap<String, Object> hashMap); 
	
	/**
	 * 依據 平台代碼(ecId)查詢修改編輯的資料
	 * @param ecId
	 * @return
	 */
	public EcData queryEcData(String ecId);
	
	/**
	 * 依據畫面資料修改電商平台資料
	 * @param hashMap 將畫面料資封裝 
	 */
	public void updateEcData(HashMap<String, Object> hashMap);
	
	/**
	 * 依據畫面資料修改電商平台狀態
	 * @param hashMap 將畫面料資封裝 
	 */
	public void updateEcDataStatus(HashMap<String, Object> hashMap);
	
	/**
	 * 依據 平台代碼(ecId) 刪除電商平台資料
	 * @param ecId
	 */
	public void deleteEcData(String ecId);
	
	/**
	 * 依據畫面資料送審新增電商平台資料
	 * @param ecDataAprv
	 */
	public void sendEcData(EcDataAprv ecDataAprv); 
	
	/**
	 * 查詢所有覆核電商平台資料
	 * @param strtDate
	 * @param endDate
	 * @param oprtType
	 * @param dataStts
	 * @return List<EcDataAprv> 所有覆核電商平台資料
	 */
	public List<EcDataAprv> queryEcDataAprvList(String strtDate, String endDate, String oprtType, String dataStts);

	/**
	 * 依據平台代碼(id)查詢修改編輯的資料
	 * @param id
	 * @return
	 */
	public EcDataAprv queryEcDataAprv(AbstractEcDataAprv.Id id);
	
	/**
	 * 依據畫面資料新增電商平台資料
	 * @param AbstractEcDataAprv.Id 將畫面料資封裝 
	 */
	public void insertEcData(LoginUser user, AbstractEcDataAprv.Id id); 
	
	/**
	 * 依據畫面資料修改電商平台資料
	 * @param AbstractEcDataAprv.Id 將畫面料資封裝 
	 */
	public void updateEcData(LoginUser user, AbstractEcDataAprv.Id id);
	
	/**
	 * 依據畫面資料修改電商平台資料
	 * @param AbstractEcDataAprv.Id 將畫面料資封裝 
	 */
	public void deleteEcData(LoginUser user, AbstractEcDataAprv.Id id);
}
