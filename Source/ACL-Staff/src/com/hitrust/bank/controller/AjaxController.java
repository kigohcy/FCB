/**
 * @(#)AjaxController.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.All rights reserved.
 *
 * Description : Ajax control
 * 
 * Modify History:
 *  v1.00, 2016/03/03, Yann
 *   1) First release
 *  
 */
package com.hitrust.bank.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.hitrust.acl.common.I18nConverter;
import com.hitrust.bank.model.AjaxBean;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.NewsMsg;
import com.hitrust.bank.model.TbCode;
import com.hitrust.bank.service.NewsSrv;
import com.hitrust.bank.service.TbCodeSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.web.BaseAutoCommandController;

public class AjaxController extends BaseAutoCommandController {
	private static Logger LOG = Logger.getLogger(AjaxController.class);

	private TbCodeSrv tbCodeSrv;
	private NewsSrv newsSrv;

	public void setTbCodeSrv(TbCodeSrv tbCodeSrv) {
		this.tbCodeSrv = tbCodeSrv;
	}

	public void setNewsSrv(NewsSrv newsSrv) {
		this.newsSrv = newsSrv;
	}

	public AjaxController() {
		setCommandClass(AjaxBean.class);
	}

	/**
	 * 取得對應訊息代碼
	 * 
	 * @param command
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getTbCodeDesc(Command command, HttpServletRequest request, HttpServletResponse response) throws IOException {
		LOG.debug("getTbCodeDesc begin...");
		AjaxBean ajaxBean = (AjaxBean) command;

		ajaxBean.setErrorMsg("");
		ajaxBean.setCodeDesc("");
		ajaxBean.setShowDesc("");
		LoginUser user = (LoginUser) APLogin.getCurrentUser();

		try {
			// 取得前端資料
			String codeId = ajaxBean.getCodeId(); // 訊息代碼

			TbCode tbCode = tbCodeSrv.getTbCode(codeId);
			if (tbCode != null) {
				ajaxBean.setCodeDesc(tbCode.getCodeDesc());
				ajaxBean.setShowDesc(tbCode.getShowDesc());
			} else {
				String defualtMsg = I18nConverter.i18nMapping("i18n.def.msg", user.getLocale());
				ajaxBean.setErrorMsg(defualtMsg + " (" + codeId + ")");
			}

		} catch (BusinessException e) {
			String errorMsg = I18nConverter.i18nMapping(e.getErrorCode(), user.getLocale());
			ajaxBean.setErrorMsg(errorMsg);
		} catch (Exception e) {
			LOG.error("getTbCodeDesc Exception: " + e.toString(), e);
			String errorMsg = I18nConverter.i18nMapping("message.sys.error", user.getLocale());
			ajaxBean.setErrorMsg(errorMsg);
		}

		LOG.debug("getTbCodeDesc end...");
	}

	/**
	 * 預覽公告
	 * 
	 * @param command
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getNewsMsg(Command command, HttpServletRequest request, HttpServletResponse response) throws IOException {
		LOG.debug("getNewsMsg begin...");

		String seq = request.getParameter("seq");

		LoginUser user = (LoginUser) APLogin.getCurrentUser();

		try {
			// 取得公告資訊
			NewsMsg rtnNewsMsg = newsSrv.getNewBySeq(seq);

			AjaxBean ajaxBean = (AjaxBean) command;

			// 回傳到前端
			ajaxBean.setTitle(rtnNewsMsg.getTitle());
			ajaxBean.setBgnDate(rtnNewsMsg.getBgnDate());
			ajaxBean.setContent(rtnNewsMsg.getContent());
			ajaxBean.setMdfyDttm(rtnNewsMsg.getMdfyDttm());
		} catch (BusinessException e) {
			String errorMsg = I18nConverter.i18nMapping(e.getErrorCode(), user.getLocale());
		} catch (Exception e) {
			LOG.error("getNewsMsg Exception: " + e.toString(), e);
			String errorMsg = I18nConverter.i18nMapping("message.sys.error", user.getLocale());
		}

		LOG.debug("getNewsMsg end...");
	}
}
