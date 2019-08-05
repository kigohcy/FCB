/**
 * DownloadCommand.java
 *
 * Copyright (c) 2009 HiTRUST Incorporated.All rights reserved.
 * 
 * Description: 檔案下載公用Command
 *
 * Modify History:
 *  v1.00, 2009-7-29, Royal Shen
 *   1) First release
 */
package com.hitrust.bank.model;

import com.hitrust.framework.model.BaseCommand;

/**
 * 檔案下載公用Command
 * @author Royal Shen
 */
public class DownloadCommand extends AclCommand {

	private static final long serialVersionUID = 1L;
	
	private String fileName;
	
	private String downloadKey;//PK
	
	private String downloadKind; //類別
	
	
	private String[] keys;//勾選項

	public String[] getKeys() {
		return keys;
	}

	public void setKeys(String[] keys) {
		this.keys = keys;
	}

	public String getDownloadKey() {
		return downloadKey;
	}

	public void setDownloadKey(String downloadKey) {
		this.downloadKey = downloadKey;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDownloadKind() {
		return downloadKind;
	}

	public void setDownloadKind(String downloadKind) {
		this.downloadKind = downloadKind;
	}

}
