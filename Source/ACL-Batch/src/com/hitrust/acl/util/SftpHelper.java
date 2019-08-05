/**
 * 
 */
package com.hitrust.acl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpHelper {
	static Logger LOG = Logger.getLogger(SftpHelper.class);

	/**
	 * 連接sftp伺服器
	 * 
	 * @param host 主機
	 * @param port 端口
	 * @param username 用戶名
	 * @param pwd 密碼
	 * @return
	 */
	public ChannelSftp connect(String host, int port, String username, String pwd) {
		ChannelSftp sftp = null;
		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			Session sshSession = jsch.getSession(username, host, port);
			LOG.debug("Session created.");
			sshSession.setPassword(pwd);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			LOG.debug("Session begin connect....");
			sshSession.connect();
			LOG.debug("Session connected.");
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			LOG.debug("Connected to " + host + ".");
		} catch (Exception e) {
			LOG.error("connect Exception:"+ e.toString(), e);
		}
		return sftp;
	}

	/**
	 * 上傳文件
	 * 
	 * @param directory
	 *            上傳的目錄
	 * @param uploadFile
	 *            要上傳的文件
	 * @param sftp
	 */
	public boolean upload(String directory, String uploadFile, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			File file = new File(uploadFile);
			sftp.put(new FileInputStream(file), file.getName());
			return true;
		} catch (Exception e) {
			LOG.error("upload Exception:"+ e.toString(), e);
			return false;
		}
	}

	/**
	 * 下載文件
	 * 
	 * @param directory
	 *            下載目錄
	 * @param downloadFile
	 *            下載的文件
	 * @param saveFile
	 *            存在本地的路徑
	 * @param sftp
	 */
	public boolean download(String directory, String downloadFile, String saveFile, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			File file = new File(saveFile);
			sftp.get(downloadFile, new FileOutputStream(file));
			return true;
		} catch (Exception e) {
			LOG.error("download Exception:"+ e.toString(), e);
			return false;
		}
	}

	/**
	 * 刪除文件
	 * 
	 * @param directory
	 *            要刪除文件所在目錄
	 * @param deleteFile
	 *            要刪除的文件
	 * @param sftp
	 */
	public boolean delete(String directory, String deleteFile, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			sftp.rm(deleteFile);
			return true;
		} catch (Exception e) {
			LOG.error("delete Exception:"+ e.toString(), e);
			return false;
		}
	}

	/**
	 * 列出目錄下的文件
	 * 
	 * @param directory
	 *            要列出的目錄
	 * @param sftp
	 * @return
	 * @throws SftpException
	 */
	public Vector listFiles(String directory, ChannelSftp sftp) throws SftpException {
		return sftp.ls(directory);
	}
}
