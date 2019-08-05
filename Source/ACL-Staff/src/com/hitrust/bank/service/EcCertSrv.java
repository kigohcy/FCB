/**
 * @(#) EcCertSrv.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/21, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */

package com.hitrust.bank.service;

import java.util.List;

import com.hitrust.bank.model.EcCert;
import com.hitrust.bank.model.EcData;
import com.hitrust.bank.model.SysParm;

public interface EcCertSrv {
	
	/**
	 * 取得所有電商平台
	 * @return List<EcData>
	 */
	public List<EcData> fetchEcDataList();
	
	/**
	 * 依據 平台代碼 取得平台資料
	 * @param ecId 平台代碼
	 * @return EcData
	 */
	public EcData fetchEcDataByEcId(String ecId);
	
	/**
	 * 依據平台代碼 取得平台憑證檔清單
	 * @param ecId 平台代碼
	 * @return List<EcCert>
	 */
	public List<EcCert> fetchEcCertListByEcId(String ecId);
	
	/**
	 * 依據 憑證序號取得 電商平台憑證檔
	 * @param certSn
	 * @return EcCert or null
	 */
	public EcCert fetchEcCertByCertSn(String certSn);
	
	/**
	 * 依據 平台代碼 & 憑證識別碼 對應憑證資料
	 * @param ecId 	 憑證編號
	 * @param certCn 憑證識別碼
	 * @return EcCert or null
	 */
	public EcCert fetchEcCertByKey(String ecId, String certCn);
	
	/**
	 * 新增電商平台憑證資料
	 * @param cert 電商平台憑證檔
	 */
	public void insertEcCert(EcCert cert);
	
	/**
	 * 依據 參數名稱 取得系統參數檔
	 * @param code 參數名稱
	 * @return SysParm
	 */
	public SysParm fetchSysParmByCode(String code);
	

	/**
	 * 異動電商平台憑證資料
	 * @param modle 電商平台憑證檔
	 */
	public void updateEcCert(EcCert modle);
	
	/**
	 * 刪除電商平台憑證資料
	 * @param modle 電商平台憑證檔
	 */
	public void deleteEcCert(EcCert modle);
	
}
