/**
 * @(#) NewsSrv.java
 *
 * Directions:
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 * v1.00, 2016年06月06日, Jimmy Yen
 * 1) JIRA-Number, First release
 *
 */
package com.hitrust.bank.service;

import java.io.IOException;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.hitrust.bank.model.NewsMsg;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageQuery;

public interface NewsSrv {
	
	/**
	 * 查詢初始化
	 * 
	 * @return NewsMsg
	 */
	public NewsMsg queryInit();

	/**
	 * 查詢公告
	 * 
	 * @param beginDate
	 *            公告起日
	 * @param endDate
	 *            公告迄日
	 * @param type
	 *            公告類型
	 * @param title
	 *            公告標題
	 * @param page
	 *            分頁資訊
	 * @return PageQuery
	 */
	public PageQuery queryNewsMsg(Date beginDate, Date endDate, String type, String title, Page page);

	/**
	 * 新增公告
	 * 
	 * @param bgnDate
	 *            公告起日
	 * @param endDate
	 *            公告迄日
	 * @param type
	 *            公告類型
	 * @param serl
	 *            至頂排序
	 * @param title
	 *            公告標題
	 * @param content
	 *            公告內容
	 */
	public void insert(String seq, Date bgnDate, Date endDate, String type, int serl, String title, String content);

	/**
	 * 刪除公告
	 * 
	 * @param seq
	 *            公告序號
	 */
	public void delete(String[] seq);

	/**
	 * 依據seq取的公告資訊
	 * 
	 * @param seq
	 *            公告序號
	 * @return NewsMsg
	 */
	public NewsMsg getNewBySeq(String seq);

	/**
	 * 修改公告
	 * 
	 * @param seq
	 *            公告序號
	 * @param bgnDate
	 *            公告起日
	 * @param endDate
	 *            公告迄日
	 * @param type
	 *            公告類型
	 * @param serl
	 *            至頂排序
	 * @param title
	 *            公告標題
	 * @param content
	 *            公告內容
	 */
	public void modify(String seq, Date bgnDate, Date endDate, String type, int serl, String title, String content);
	
	/**
	 * 新增上傳圖檔
	 * @param seq
	 * @param fileName
	 * @param file
	 */
	public void insertNewsImag(String seq, String filename, MultipartFile file) throws IOException;
	
}
