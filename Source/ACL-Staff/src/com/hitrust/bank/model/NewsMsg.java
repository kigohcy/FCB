/**
 * @(#) NewsMsg.java
 * 
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/06/06, Jimmy
 * 	 1)First release, 二階
 *  
 */
package com.hitrust.bank.model;

import java.io.Serializable;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.hitrust.bank.model.base.AbstractNewsMsg;

public class NewsMsg extends AbstractNewsMsg implements Serializable {
	private static final long serialVersionUID = 165393220328775399L;

	private String qBgnDate; 			// 查詢:起日
	private String qEndDate; 			// 查詢:迄日
	private String qType; 				// 查詢:公告類型
	private String qTitle;

	private String queryLimt; 			// 查詢:範圍限制
	private String[] qSeq; 				// 查詢:公告序號

	private String aBgnDate; 			// 新增、修改:起日
	private String aEndDate; 			// 新增、修改:迄日
	
	private List<NewsMsg> newsMsg;		// 操作記錄:pageQuery暫存
	private String fileName;                //上傳圖檔檔名
	private transient MultipartFile upload; //上傳圖檔
	private String CKEditorFuncNum;         //
	
	
	// getters & setters
	public List<NewsMsg> getNewsMsg() {
		return newsMsg;
	}

	public void setNewsMsg(List<NewsMsg> newsMsg) {
		this.newsMsg = newsMsg;
	}

	public String getqTitle() {
		return qTitle;
	}

	public void setqTitle(String qTitle) {
		this.qTitle = qTitle;
	}

	public String getqEndDate() {
		return qEndDate;
	}

	public void setqEndDate(String qEndDate) {
		this.qEndDate = qEndDate;
	}

	public String getQueryLimt() {
		return queryLimt;
	}

	public void setQueryLimt(String queryLimt) {
		this.queryLimt = queryLimt;
	}

	public String getqType() {
		return qType;
	}

	public void setqType(String qType) {
		this.qType = qType;
	}

	public String getaBgnDate() {
		return aBgnDate;
	}

	public void setaBgnDate(String aBgnDate) {
		this.aBgnDate = aBgnDate;
	}

	public String getaEndDate() {
		return aEndDate;
	}

	public void setaEndDate(String aEndDate) {
		this.aEndDate = aEndDate;
	}

	public String getqBgnDate() {
		return qBgnDate;
	}

	public void setqBgnDate(String qBgnDate) {
		this.qBgnDate = qBgnDate;
	}

	public String[] getqSeq() {
		return qSeq;
	}

	public void setqSeq(String[] qSeq) {
		this.qSeq = qSeq;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public MultipartFile getUpload() {
		return upload;
	}
	public void setUpload(MultipartFile upload) {
		this.upload = upload;
	}
	public String getCKEditorFuncNum() {
		return CKEditorFuncNum;
	}
	public void setCKEditorFuncNum(String CKEditorFuncNum) {
		this.CKEditorFuncNum = CKEditorFuncNum;
	}
}
