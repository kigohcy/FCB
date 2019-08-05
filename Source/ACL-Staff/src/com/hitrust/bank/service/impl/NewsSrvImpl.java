/**
 * @(#) NewsSrvImpl.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 * v1.00, 2016年06月06日, Jimmy Yen
 *  1) JIRA-Number, First release
 * 
 */
package com.hitrust.bank.service.impl;

import java.io.IOException;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

import com.hitrust.acl.util.DateUtil;
import com.hitrust.bank.dao.NewsMsgDAO;
import com.hitrust.bank.dao.SysParmDAO;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.bank.model.NewsImag;
import com.hitrust.bank.model.NewsMsg;
import com.hitrust.bank.model.SysParm;
import com.hitrust.bank.model.base.AbstractNewsImag;
import com.hitrust.bank.model.base.AbstractNewsImag.Id;
import com.hitrust.bank.service.NewsSrv;
import com.hitrust.framework.exception.BusinessException;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public class NewsSrvImpl implements NewsSrv {

	// DAO injection
	private SysParmDAO sysParmDAO;
	private NewsMsgDAO newsMsgDAO;

	public void setSysParmDAO(SysParmDAO sysParmDAO) {
		this.sysParmDAO = sysParmDAO;
	}

	public void setNewsMsgDAO(NewsMsgDAO newsMsgDAO) {
		this.newsMsgDAO = newsMsgDAO;
	}

	/**
	 * 查詢初始化
	 * 
	 * @return NewsMsg
	 */
	@Override
	public NewsMsg queryInit() {
		NewsMsg rtnNewsMsg = new NewsMsg();

		// 取得查詢日期區間限制
		SysParm sysParm = (SysParm) sysParmDAO.queryById(SysParm.class, "STAFF_ANNC_QURY_LIMT");
		rtnNewsMsg.setQueryLimt(sysParm.getParmValue());

		return rtnNewsMsg;
	}

	/**
	 * 查詢公告
	 * 
	 * @param beginDate 公告起日
	 * @param endDate 公告迄日
	 * @param type 公告類型
	 * @param title 公告標題
	 * @param page 分頁資訊
	 * @return PageQuery
	 */
	@Override
	public PageQuery queryNewsMsg(Date beginDate, Date endDate, String type, String title, Page page) {

		return newsMsgDAO.queryMsgs(beginDate, endDate, type, title, page);
	}

	/**
	 * 新增公告
	 * 
	 * @param bgnDate 公告起日
	 * @param endDate 公告迄日
	 * @param type 公告類型
	 * @param serl 至頂排序
	 * @param title 公告標題
	 * @param content 公告內容
	 */
	@Override
	public void insert(String seq, Date bgnDate, Date endDate, String type, int serl, String title, String content) {
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		
		NewsMsg newsMsg = new NewsMsg();
		newsMsg.setSeq(seq);
		newsMsg.setBgnDate(bgnDate);
		newsMsg.setEndDate(endDate);
		newsMsg.setType(type);
		newsMsg.setTitle(title);
		newsMsg.setSerl(serl);
		newsMsg.setContent(content);
		newsMsg.setCretUser(user.getUserId());
		newsMsg.setCretDttm(new Date());
		newsMsg.setMdfyUser(user.getUserId());
		newsMsg.setMdfyDttm(new Date());
		
		try {
			newsMsgDAO.save(newsMsg);
		} catch (Exception e) {
			// 新增失敗
			throw new BusinessException("message.sys.insert.failure");
		}
	}

	/**
	 * 刪除公告
	 * 
	 * @param seq 公告序號
	 */
	@Override
	public void delete(String[] seq) {
		try {
			LoginUser user = (LoginUser) APLogin.getCurrentUser();
			String userId = user.getUserId();
			String dttm = DateUtil.formateDateTimeForUser(DateUtil.getCurrentTime("DT", "AD"));
			
			for (int i = 0; i < seq.length; i++) {
				NewsMsg newsMsg = (NewsMsg) newsMsgDAO.queryById(NewsMsg.class, seq[i]);
				
				//delete newsMsg
				newsMsgDAO.delete(newsMsg);
				//delete newsImag, 先不刪除, 以免操作記錄查不到圖檔.
				//newsMsgDAO.deleteImagBySeq(seq[i]);
				//update newsImag.deltFlag=Y, mdfyDttm, mdfyUser
				newsMsgDAO.updateImagBySeq(seq[i], userId, dttm);
			}
		} catch (Exception e) {
			throw new BusinessException("message.sys.delete.failure");
		}
	}

	/**
	 * 依據seq取的公告資訊
	 * 
	 * @param seq 公告序號
	 * @return NewsMsg
	 */
	@Override
	public NewsMsg getNewBySeq(String seq) {
		NewsMsg newsMsg = (NewsMsg) newsMsgDAO.queryById(NewsMsg.class, seq);

		return newsMsg;
	}

	/**
	 * 修改公告
	 * 
	 * @param seq 公告序號
	 * @param bgnDate 公告起日
	 * @param endDate 公告迄日
	 * @param type 公告類型
	 * @param serl 至頂排序
	 * @param title 公告標題
	 * @param content 公告內容
	 */
	@Override
	public void modify(String seq, Date bgnDate, Date endDate, String type, int serl, String title, String content) {
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		
		NewsMsg newsMsg = (NewsMsg) newsMsgDAO.queryById(NewsMsg.class, seq);
		if(newsMsg == null){
			throw new BusinessException("message.db.have.no.data");
		}
		
		newsMsg.setBgnDate(bgnDate);
		newsMsg.setEndDate(endDate);
		newsMsg.setType(type);
		newsMsg.setSerl(serl);
		newsMsg.setTitle(title);
		newsMsg.setContent(content);
		newsMsg.setMdfyUser(user.getUserId());
		newsMsg.setMdfyDttm(new Date());
		
		try {
			newsMsgDAO.update(newsMsg);
		} catch (Exception e) {
			// 修改失敗
			throw new BusinessException("message.sys.update.failure");
		}
	}
	
	/**
	 * 圖檔上傳新增
	 * 
	 * @param seq 公告序號
	 * @param file 圖檔
	 */
	@Override
	public void insertNewsImag(String seq, String filename, MultipartFile file) throws IOException {
		LoginUser user = (LoginUser) APLogin.getCurrentUser();
		
		NewsImag newsImag = new NewsImag();
		AbstractNewsImag.Id id = new Id();
		id.setSeq(seq);
		id.setFileName(filename);
		
		if(filename.length() > 60){
			//檔名長度錯誤
			throw new BusinessException("message.F0401.upload.nameErr");
		}
		
		NewsImag imag = (NewsImag)newsMsgDAO.queryById(NewsImag.class, id);
		if(imag != null){
			//檔名重複上傳
			throw new BusinessException("message.F0401.upload.duplicate");
		}
		
		newsImag.setId(id);
		newsImag.setImag(file.getBytes());
		newsImag.setMdfyUser(user.getUserId());
		newsImag.setMdfyDttm(new Date());
		newsImag.setDeltFlag("N");
		try{
			newsMsgDAO.save(newsImag);
		} catch (Exception e) {
			//檔案上傳失敗
			throw new BusinessException("message.F0401.upload.failure");
		}
	}
}
