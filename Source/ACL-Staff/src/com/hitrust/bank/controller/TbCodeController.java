/**
 * @(#) TbCodeController.java
 *
 * Directions:
 *
 * Copyright (c) 2018 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2018/04/17
 *    1) JIRA-Number, First release
 * 
 */
package com.hitrust.bank.controller;

import org.apache.log4j.Logger;

import com.hitrust.acl.common.I18nConverter;
import com.hitrust.acl.exception.FrameException;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.TbCode;
import com.hitrust.bank.model.base.AbstractTbCode;
import com.hitrust.bank.service.TbCodeSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.web.BaseAutoCommandController;

public class TbCodeController extends BaseAutoCommandController {
	// Log4
	private static Logger LOG = Logger.getLogger(TbCodeController.class);
	
	private TbCodeSrv tbCodeSrv;
	
	public TbCodeSrv getTbCodeSrv() {
		return tbCodeSrv;
	}

	public void setTbCodeSrv(TbCodeSrv tbCodeSrv) {
		this.tbCodeSrv = tbCodeSrv;
	}
	
	public TbCodeController() {
		super.setCommandClass(TbCode.class);
	}

	/**
	 * 初始畫面
	 * @param commnd Command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void queryInit(Command command) throws BusinessException, FrameException {
	}
	
	/**
	 * 資料查詢
	 * @param commnd Command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void query(Command command) throws BusinessException, FrameException {
		try{
			TbCode dataBean = (TbCode)command;
			dataBean.setTbCodeList(tbCodeSrv.fetchByCodeIdLike(dataBean.getqCodeId()));
			dataBean.setQueryFlag(true);
		} catch (BusinessException e) {
			LOG.error("[query BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[query Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	/**
	 * 新增資料Init
	 * @param command
	 */
	public void insertInit(Command command) throws BusinessException, FrameException {
	}
	
	/**
	 * 新增資料
	 * @param command
	 */
	public void insertTbCode(Command command) throws BusinessException, FrameException {
		TbCode dataBean = (TbCode) command;
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		try{
			tbCodeSrv.insert(dataBean);
			dataBean.setReturnMsg(I18nConverter.i18nMapping("message.sys.insert.success", user.getLocale())); //新增成功
		} catch (BusinessException e) {
			LOG.error("[insertTbCode BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[insertTbCode Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	/**
	 * 修改資料Init
	 * @param command
	 */
	public void updateInit(Command command) throws BusinessException, FrameException {
		TbCode dataBean = (TbCode) command;
		try{
			AbstractTbCode.Id id = new AbstractTbCode.Id();
			id.setLngn("zh_TW");
			id.setCodeId(dataBean.getCodeType()+"-"+dataBean.getCodeId());
			TbCode queryed = tbCodeSrv.fetchById(id);
			dataBean.setTbCode(queryed);
		} catch (BusinessException e) {
			LOG.error("[insertTbCode BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[insertTbCode Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	/**
	 * 異動資料
	 * @param command
	 */
	public void updateTbCode(Command command) throws BusinessException, FrameException {
		TbCode dataBean = (TbCode) command;
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		try{
			tbCodeSrv.updateTbCode(dataBean);
			dataBean.setReturnMsg(I18nConverter.i18nMapping("message.sys.update.success", user.getLocale())); //異動成功
		} catch (BusinessException e) {
			LOG.error("[insertTbCode BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[insertTbCode Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	/**
	 * 刪除資料
	 * @param command
	 */
	public void deleteTbCode(Command command) throws BusinessException, FrameException {
		TbCode dataBean = (TbCode) command;
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		try{
			tbCodeSrv.deleteTbCode(dataBean);
			dataBean.setReturnMsg(I18nConverter.i18nMapping("message.sys.delete.success", user.getLocale())); //異動成功
		} catch (BusinessException e) {
			LOG.error("[insertTbCode BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[insertTbCode Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
}
