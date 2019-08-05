/**
 * @(#)AclFtpClient.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : FTP共用
 * 
 * Modify History:
 *  v1.00, 2016/03/11, Woodman
 *   1) First release
 *  
 */
package com.hitrust.acl.common;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

/**
 * ftp 工具類
 * 
 * @author Woodman
 */
public class AclFtpClient {
	Logger LOG = Logger.getLogger(AclFtpClient.class);

	private FTPClient ftp;
	private String workdir;
	private boolean ready = false;
	
	/**
	 * Ftp 連接Client
	 * 
	 * @param host
	 *            主機
	 * @param port
	 *            端口
	 * @param username
	 *            用戶名
	 * @param password
	 *            密碼
	 * @param mode
	 *            傳輸模式
	 * @param root
	 *            路徑
	 * @return
	 */
	public static AclFtpClient newInstance(String host, int port, String username, String password, String mode, String root) {
		return new AclFtpClient(host, port, username, password, mode, root);
	}

	private AclFtpClient(String host, int port, String username, String password, String mode, String root) {
		ftp = new FTPClient();
		workdir = root;
		try {
			ftp.connect(host, port);
			int reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				LOG.error("FTP server refused connection");
				ftp.disconnect();
			}
			boolean login = ftp.login(username, password);
			LOG.info("Login To Server " + login);

			// Use passive mode as default because most of us are
			// behind firewalls these days.
			
			if("A".equals(mode)){
				//主動模式A
				LOG.info("enterLocalActiveMode ");
				ftp.enterLocalActiveMode();
			}else{
				//被動模式P
				LOG.info("enterLocalPassiveMode ");
				ftp.enterLocalPassiveMode();
			}
			
			if (!ftp.changeWorkingDirectory(workdir)) {// 無此遠程服務器路徑
				LOG.error("No Remote path :" + workdir);
			} else {
				LOG.info("changeWorkingDirectory to " + workdir);
				ready = true;
			}
		} catch (Exception e) {
			LOG.error("connection error", e);
		}

	}

	/**
	 * 是否已正確連接ftp server
	 * 
	 * @return
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * 上傳文件
	 * 
	 * @param remotefilename
	 *            文件名
	 * @param content
	 *            文件內容
	 * @param canLoadEmpty
	 *            是否允許上傳空擋
	 * @return
	 */
	public boolean upload(String remotefilename, byte[] content, boolean canLoadEmpty) {
		boolean result = false;
		if (StringUtil.isBlank(remotefilename)) {
			LOG.error("remotefilename can't be blank");
			return false;
		}
		if (!canLoadEmpty) {
			if (content == null || content.length == 0) {
				LOG.error("content can't be empty");
				return false;
			}
		}
		InputStream input;
		try {
			input = new ByteArrayInputStream(content);
			result = ftp.storeFile(remotefilename, input);
			//ftp.setControlEncoding("big5");
			//result = ftp.storeFile(new String(remotefilename.getBytes("big5")), input);
			if(result==false){
				LOG.info("ftp Reply:"+ftp.getReplyString());
			}
			input.close();
		} catch (Exception e) {
			LOG.error("upload file (" + remotefilename + ") error", e);
			result = false;
		}
		LOG.info("upload file " + remotefilename + "(" + result + ")");
		return result;
	}

	/**
	 * 刪除文件
	 * 
	 * @param remotefilename
	 *            文件名
	 * @return
	 */
	public boolean delete(String remotefilename) {
		boolean result = false;
		try {
			result = ftp.deleteFile(remotefilename);
			LOG.info(ftp.getReplyString());
		} catch (Exception e) {
			LOG.error("delete file (" + remotefilename + ") error", e);
		}
		LOG.info("delete file (" + remotefilename + ") [" + result + "]");
		return result;
	}

	/**
	 * 列出所有文件
	 * 
	 * @return
	 */
	public String[] listFiles() {
		FTPFile[] ftpFile;
		ArrayList files = new ArrayList();
		try {
			ftpFile = ftp.listFiles();
			for (int i = 0; ftpFile != null && i < ftpFile.length; i++) {
				if (ftpFile[i].isFile()) {
					files.add(ftpFile[i].getName());
				}
			}
		} catch (Exception e) {
			LOG.error("listFiles  error", e);
		}
		return (String[]) files.toArray(new String[] {});
	}

	/**
	 * 關閉ftp連接
	 */
	public void releaseIfnessary() {
		try {
			if (ftp != null && ftp.isConnected()) {
				ftp.logout();
				ftp.disconnect();
				LOG.info("ftp  disconnected ");
			}
		} catch (Exception e) {
			LOG.error("ftp.disconnect() error", e);
		}
	}

	/**
	 * 功能：保存FTP文件到本地
	 * 
	 * @param localFilePath
	 * @param remoteFileName
	 * @return
	 */
	public boolean downloadFile(String localFilePath, String remoteFileName) {
		BufferedOutputStream outStream = null;
		boolean success = false;
		try {
			outStream = new BufferedOutputStream(new FileOutputStream(localFilePath + remoteFileName));
			success = ftp.retrieveFile(remoteFileName, outStream);
		} catch (FileNotFoundException e) {
			LOG.error("downloadFile,ftp文件無法找到!" + e.getMessage());
		} catch (IOException e) {
			LOG.error("downloadFile,ftp文件IO讀寫異常!" + e.getMessage());
		} finally {
			if (outStream != null) {
				try {
					outStream.flush();
					outStream.close();
				} catch (IOException e) {
					LOG.error("downloadFile,輸出流關閉異常!" + e.getMessage());
				}
			}
		}
		return success;
	}
}
