/**
 * @(#)TbCodeSrvImpl.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : TbCodeSrvImpl
 * 
 * Modify History:
 *  v1.00, 2016/03/03, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.service.impl;

import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.hitrust.bank.dao.TbCodeDAO;
import com.hitrust.bank.model.TbCode;
import com.hitrust.bank.model.base.AbstractTbCode;
import com.hitrust.bank.model.base.AbstractTbCode.Id;
import com.hitrust.bank.service.TbCodeSrv;
import com.hitrust.framework.dao.BaseDAO;
import com.hitrust.framework.exception.BusinessException;

public class TbCodeSrvImpl implements TbCodeSrv {
	private static Logger LOG = Logger.getLogger(TbCodeSrvImpl.class);
	
	private BaseDAO baseDAO;
	private TbCodeDAO tbCodeDAO;
	
	public void setBaseDAO(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}

	public void setTbCodeDAO(TbCodeDAO tbCodeDAO) {
		this.tbCodeDAO = tbCodeDAO;
	}

	/**
	 * 查詢訊息代碼資料
	 * @return
	 */
	@Override
	public TbCode getTbCode(String codeId) {
		
		Locale locale = new Locale("zh", "TW");
		
		TbCode.Id id = new Id();
		id.setLngn(locale.toString());
		id.setCodeId(codeId);
		TbCode tbCode = (TbCode)baseDAO.queryById(TbCode.class, id);
		
		return tbCode;
	}

	@Override
	public List<TbCode> fetchByCodeIdLike(String codeId) {
//		String[] codeIds = new String[2];
//		codeIds[0] = "01-"+codeId+"%";
//		codeIds[1] = "02-"+codeId+"%";
		return tbCodeDAO.fetchByCodeIdsLike(codeId);
	}

	@Override
	public void insert(TbCode tbCode) {
		AbstractTbCode.Id id = new AbstractTbCode.Id();
		id.setCodeId(tbCode.getCodeType()+"-"+tbCode.getCodeId());
		id.setLngn("zh_TW");
		if(tbCodeDAO.queryById(TbCode.class, id) != null){
			LOG.info("資料庫有"+tbCode.getCodeId()+"的資料,無法新增");
			throw new BusinessException("message.db.have.data");
		}
		tbCode.setId(id);
		tbCode.setShowDesc(tbCode.getCodeDesc());
		try{
			tbCodeDAO.save(tbCode);
		}catch(Exception e){
			LOG.error("訊息代碼新增失敗", e);
			throw new BusinessException("message.sys.insert.failure");	//資料新增失敗
		}
	}

	@Override
	public TbCode fetchById(Id id) {
		return (TbCode)tbCodeDAO.queryById(TbCode.class, id);
	}

	@Override
	public void updateTbCode(TbCode tbCode) {
		AbstractTbCode.Id id = new AbstractTbCode.Id();
		id.setCodeId(tbCode.getCodeType()+"-"+tbCode.getCodeId());
		id.setLngn("zh_TW");
		TbCode beanToUpdate = (TbCode)tbCodeDAO.queryById(TbCode.class, id);
		if(beanToUpdate == null){
			LOG.info("查無"+tbCode.getCodeId()+"的資料,無法修改");
			throw new BusinessException("message.db.have.no.data");
		}
		beanToUpdate.setCodeDesc(tbCode.getCodeDesc());
		beanToUpdate.setShowDesc(tbCode.getCodeDesc());
		try{
			tbCodeDAO.update(beanToUpdate);
		}catch(Exception e){
			LOG.error("訊息代碼新增失敗", e);
			throw new BusinessException("message.sys.update.failure");	//資料異動失敗
		}
	}

	@Override
	public void deleteTbCode(TbCode tbCode){
		try{
			AbstractTbCode.Id id = new AbstractTbCode.Id();
			id.setCodeId(tbCode.getCodeType()+"-"+tbCode.getCodeId());
			id.setLngn("zh_TW");
			TbCode beanToDelete = (TbCode)tbCodeDAO.queryById(TbCode.class, id);
			if(beanToDelete == null){
				LOG.info("查無"+tbCode.getCodeId()+"的資料,無法修刪除");
				throw new BusinessException("message.db.have.no.data");
			}
			tbCodeDAO.delete(beanToDelete);
		}catch(Exception e){
			LOG.error("訊息代碼新增失敗", e);
			throw new BusinessException("message.sys.delete.failure");	//資料異動失敗
		}
	}
}
