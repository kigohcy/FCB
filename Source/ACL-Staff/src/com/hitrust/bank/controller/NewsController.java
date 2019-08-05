/**
 * @(#) NewsController.java
 *
 * Directions: 最新消息公告設定
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *  v1.00, 2016年06月06日, Jimmy Yen
 * 	 1) First release
 *  v1.01, 2016/07/07, Jimmy Yen
 *   1) 記錄應用系統日誌
 *  
 */
package com.hitrust.bank.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.hitrust.acl.common.I18nConverter;
import com.hitrust.acl.common.JsonUtil;
import com.hitrust.acl.common.UUIDGen;
import com.hitrust.acl.exception.FrameException;
import com.hitrust.acl.util.DateUtil;
import com.hitrust.bank.json.TsbAuditLog;
import com.hitrust.bank.json.TsbAuditLogDetl;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.NewsMsg;
import com.hitrust.bank.service.NewsSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.Command;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;
import com.hitrust.framework.web.BaseAutoCommandController;

public class NewsController extends BaseAutoCommandController {

	// ============================== Log4j ==============================
	private static Logger LOG = Logger.getLogger(NewsController.class);

	// ============================== Constructor ==============================
	public NewsController() {
		setCommandClass(NewsMsg.class);
	}

	private NewsSrv newsSrv;

	public void setNewsSrv(NewsSrv newsSrv) {
		this.newsSrv = newsSrv;
	}

	/**
	 * 查詢初始化
	 * 
	 * @param command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void queryInit(Command command) throws BusinessException, FrameException {

		NewsMsg newsMsg = (NewsMsg) command;

		try {
			Page page = newsMsg.getPage(); // 分頁資訊

			// 取得系統日期
			String sysDate = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("D", "AD"));
			newsMsg.setqBgnDate(sysDate);
			newsMsg.setqEndDate(sysDate);

			// 取得查詢初始化資料
			NewsMsg rtnNewsMsg = newsSrv.queryInit();
			newsMsg.setQueryLimt(rtnNewsMsg.getQueryLimt()); // 查詢區間限制

			// 取得公告訊息
			PageQuery pageQuery = newsSrv.queryNewsMsg(null, null, "", "", page);
			
			newsMsg.setNewsMsg((List<NewsMsg>)pageQuery.getResult());
			setQueryPage(pageQuery);
		} catch (BusinessException e) {
			LOG.error("[NewsSetting queryInit BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[NewsSetting queryInit Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 查詢
	 * 
	 * @param command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void query(Command command) throws BusinessException, FrameException {

		NewsMsg newsMsg = (NewsMsg) command;

		try {
			//v1.01
			this.audiLog(newsMsg);
			
			Page page = newsMsg.getPage(); // 分頁資訊

			Date strtDate = DateUtil.formatStrToDate(newsMsg.getqBgnDate(), "000000"); // 起始日期
			Date endDate = DateUtil.formatStrToDate(newsMsg.getqEndDate(), "235959"); // 結束日期
			String type = newsMsg.getqType(); // 公告類型
			String title = newsMsg.getqTitle(); // 公告標題

			// 取得公告訊息
			PageQuery pageQuery = newsSrv.queryNewsMsg(strtDate, endDate, type, title, page);
			newsMsg.setNewsMsg((List<NewsMsg>)pageQuery.getResult());
			setQueryPage(pageQuery);
		} catch (BusinessException e) {
			LOG.error("[NewsSetting query BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[NewsSetting query Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	/**
	 * 新增初始化
	 * 
	 * @param command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void insertInit(Command command) throws BusinessException, FrameException {
		NewsMsg newsMsg = (NewsMsg) command;
		String seq = UUIDGen.genUUID();
		newsMsg.setSeq(seq);
	}

	/**
	 * 新增資料
	 * 
	 * @param command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void insert(Command command) throws BusinessException, FrameException {

		LoginUser user = (LoginUser) APLogin.getCurrentUser();

		NewsMsg newsMsg = (NewsMsg) command;

		try {
			//v1.01
			this.audiLog(newsMsg);

			Date bgnDate = DateUtil.formatStrToDate(newsMsg.getaBgnDate(), "000000"); // 起始日期
			Date endDate = DateUtil.formatStrToDate(newsMsg.getaEndDate(), "235959"); // 結束日期

			int serl = newsMsg.getSerl(); // 是否置頂排序
			String seq  = newsMsg.getSeq();
			String type = newsMsg.getType(); // 公告類型
			String title = newsMsg.getqTitle().trim(); // 公告標題
			String content = newsMsg.getContent(); // 公告內容

			// 新增公告
			newsSrv.insert(seq, bgnDate, endDate, type, serl, title, content);
			
			// 回應訊息
			newsMsg.setReturnMsg(I18nConverter.i18nMapping("message.sys.insert.success", user.getLocale()));

		} catch (BusinessException e) {
			LOG.error("[NewsSetting insert BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[NewsSetting insert Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 刪除公告
	 * 
	 * @param command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void delete(Command command) throws BusinessException, FrameException {

		LoginUser user = (LoginUser) APLogin.getCurrentUser();

		NewsMsg newsMsg = (NewsMsg) command;

		try {
			//v1.01
			this.audiLog(newsMsg);
			
			String[] qSeq = newsMsg.getqSeq(); // 公告序號

			// 刪除公告訊息
			newsSrv.delete(qSeq);

			// 回應訊息
			newsMsg.setReturnMsg(I18nConverter.i18nMapping("message.sys.delete.success", user.getLocale()));
		} catch (BusinessException e) {
			LOG.error("[NewsSetting delete BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[NewsSetting delete Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}

	/**
	 * 修改初始化
	 * 
	 * @param command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void modifyInit(Command command) throws BusinessException, FrameException {

		NewsMsg newsMsg = (NewsMsg) command;

		try {
			String[] qSeq = newsMsg.getqSeq(); // 公告序號

			// 根據序號取得公告內容
			NewsMsg rtnNewsMsg = newsSrv.getNewBySeq(qSeq[0]);

			// 回傳公告資訊到前端
			newsMsg.setSeq(rtnNewsMsg.getSeq()); // 公告序號
			newsMsg.setBgnDate(rtnNewsMsg.getBgnDate());// 公告起日
			newsMsg.setEndDate(rtnNewsMsg.getEndDate());// 公告迄日
			newsMsg.setSerl(rtnNewsMsg.getSerl());// 至頂排序
			newsMsg.setType(rtnNewsMsg.getType());// 公告類型
			newsMsg.setTitle(rtnNewsMsg.getTitle());// 公告標題
			newsMsg.setContent(rtnNewsMsg.getContent());// 公告內容
		} catch (BusinessException e) {
			LOG.error("[NewsSetting delete BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[NewsSetting delete Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	/**
	 * 修改公告
	 * 
	 * @param command
	 * @throws BusinessException
	 * @throws FrameException
	 */
	public void modify(Command command) throws BusinessException, FrameException {
		LoginUser user = (LoginUser) APLogin.getCurrentUser();

		NewsMsg newsMsg = (NewsMsg) command;

		try {
			//v1.01
			this.audiLog(newsMsg);			
			
			Date bgnDate = DateUtil.formatStrToDate(newsMsg.getaBgnDate(), "000000");// 公告起日
			Date endDate = DateUtil.formatStrToDate(newsMsg.getaEndDate(), "235959");// 公告迄日

			int serl = newsMsg.getSerl();// 至頂排序

			String type = newsMsg.getType();// 公告類型
			
			String title = newsMsg.getqTitle().trim();// 公告標題
			String content = newsMsg.getContent();// 公告內容
			String seq = newsMsg.getSeq();// 公告序號

			// 修改公告
			newsSrv.modify(seq, bgnDate, endDate, type, serl, title, content);

			// 回應訊息
			newsMsg.setReturnMsg(I18nConverter.i18nMapping("message.sys.update.success", user.getLocale()));
		} catch (BusinessException e) {
			LOG.error("[NewsSetting modify BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[NewsSetting modify Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	/**
	 * 記錄應用系統日誌(TSB_APAUDITLOG)
	 * @param dataBean command
	 */
	//v1.01
	private void audiLog(NewsMsg dataBean) throws BusinessException, FrameException {
		
		try {
			//記錄應用系統日誌(TSB_APAUDITLOG) 準備資料
			TsbAuditLog log = new TsbAuditLog();
			
			if("Q".equals(dataBean.getOperate())){
				log.setStartDate(dataBean.getqBgnDate());
				log.setEndDate(dataBean.getqEndDate());
				log.setNewsType(dataBean.getqType());
				log.setTitle(dataBean.getqTitle());
			}
			
			if("A".equals(dataBean.getOperate()) || "E".equals(dataBean.getOperate())){
				log.setStartDate(dataBean.getaBgnDate());
				log.setEndDate(dataBean.getaEndDate());
				log.setNewsType(dataBean.getType());
				log.setTitle(dataBean.getqTitle());
				log.setSerl(dataBean.getSerl().toString());
			}
			
			if("D".equals(dataBean.getOperate())){
				TsbAuditLogDetl logDetl = null;
				
				log.setTsbAuditLogDetl(new ArrayList<>());
				
				//只記十筆
				for(String seq : dataBean.getqSeq()){
					
					if(log.getTsbAuditLogDetl().size() == 10){
						break;
					}
					logDetl = new TsbAuditLogDetl();
					logDetl.setSeq(seq);
					log.getTsbAuditLogDetl().add(logDetl);
				}
			}
			
			dataBean.setFnProc(JsonUtil.object2Json(log, false));
			
		} catch (BusinessException e) {
			LOG.error("[EcData audiLog BusinessException]: ", e);
			throw e;
			
		} catch (Exception e) {
			LOG.error("[EcData audiLog Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
	
	/**
	 * 圖檔上傳
	 * @param command
	 * @param request
	 * @param response
	 * @throws BusinessException
	 * @throws FrameException
	 * @throws IOException
	 */
	public void uploadImg(Command command, HttpServletRequest request, HttpServletResponse response) 
			throws BusinessException, FrameException, IOException {
		
		NewsMsg newsMsg = (NewsMsg) command;
		
		String CKEditorFuncNum = newsMsg.getCKEditorFuncNum();
		String seq = newsMsg.getSeq();
		MultipartFile file = newsMsg.getUpload();
		String filename = file.getOriginalFilename();
		
		int startIndex = filename.lastIndexOf(".") + 1;
	    int endIndex = filename.length();
	    String extension = filename.substring(startIndex, endIndex);
	    
		Calendar srcCal = Calendar.getInstance();
		String timestamp= String.valueOf(srcCal.getTimeInMillis());
		filename = timestamp + "." + extension;
		
		LOG.debug("seq="+seq+"/filename="+filename);
		
		try {
			LoginUser user = (LoginUser) APLogin.getCurrentUser();
			String rtnMsg = I18nConverter.i18nMapping("message.F0401.upload.success", user.getLocale());
			String rtnUrl = "./newsImgSrv.html?seq="+seq+"&fileName="+filename;
			
			//insert NewsImag
			try{
				newsSrv.insertNewsImag(seq, filename, file);
			}catch (BusinessException e) {
				rtnMsg = I18nConverter.i18nMapping(e.getErrorCode(), user.getLocale());
				rtnUrl = "";
			}
			
			String callback = "<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction(";
			callback += CKEditorFuncNum + ", ";
			callback += "\"" + rtnUrl + "\", ";
			callback += "\"" + rtnMsg + "\");</script>";
			
			LOG.debug("callback="+callback);
			
			OutputStream os = response.getOutputStream();
			os.write(callback.getBytes("UTF-8"));
			os.flush();
			os.close();
			
		} catch (BusinessException e) {
			LOG.error("[uploadImg BusinessException]: ", e);
			throw e;
		} catch (Exception e) {
			LOG.error("[uploadImg Exception]: ", e);
			throw new FrameException("message.sys.error");
		}
	}
}
