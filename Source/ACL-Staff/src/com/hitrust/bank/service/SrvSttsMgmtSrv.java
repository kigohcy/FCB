/**
 * @(#)SrvSttsMgmtSrv.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 服務狀態管理 service
 * 
 * Modify History:
 *  v1.00, 2016/02/16, Evan
 *   1) First release
 *  v1.01, 2016/12/21, Yann
 *   1) TSBACL-143, 未綁定解鎖
 *  
 */
package com.hitrust.bank.service;

import com.hitrust.bank.model.CustData;
import com.hitrust.bank.model.CustPltf;

public interface SrvSttsMgmtSrv {
	
	/**
	 * 查詢服務狀態
	 * @param CustPltf Command
	 */
	public void querySrvSttsData(CustPltf custPltf);
	
	/** 
	 * 修改綁定帳號服務狀態
	 * @param custPltf
	 */
	public void updateAcntStts(CustPltf custPltf);
	
	/**
	 * 記錄更新後結果
	 * @param custPltf
	 */
	public void recordAfterOpt(CustPltf custPltf);
	
	/**
	 * 修改 custData 服務狀態
	 * @param stts  00:啟用, 01:暫停
	 * @param custSttId
	 * @return 俢改回覆訊息
	 */
	public String updateCustStts(String stts, String custSttId);
	
	/**v1.01
	 * 查詢會員資料檔
	 * @param custId
	 * @return
	 */
	public CustData queryCustData(String custId);
	
}
