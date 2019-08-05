/**
 * @(#)FileUtil.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description : 文件相關操作工具類
 * 
 * Modify History:
 *  v1.00, 2016/03/11, Woodman
 *   1) First release
 *  
 */
package com.hitrust.acl.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.log4j.Logger;

/**
 * 功能：文件相關操作工具類
 * 
 * @author woodman
 * 
 */
public class FileUtil {

	private static Logger LOG = Logger.getLogger(FileUtil.class);

	/**
	 * Read file content into byte array.
	 * 
	 * @param fileName
	 *            file name to be read.
	 * @return The byte array of file content.
	 */
	public static byte[] readFile(String fileName) throws Exception {
		// Read file content
		FileInputStream in = null;
		try {
			in = new FileInputStream(fileName);
			byte[] data = new byte[in.available()];
			in.read(data);
			return data;
		} finally {
			if (in != null)
				in.close();
		}

	}

	/**
	 * Write data byte[] into file
	 * 
	 * @param fileName
	 *            file name to be write.
	 * @param data
	 *            data for writ to the file.
	 * @throws Exception
	 */
	public static void writeFile(String fileName, byte[] data) throws Exception {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fileName);
			out.write(data);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * Delete a non-empty directory
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	/**
	 * 转移文件目录
	 * 
	 * @param filename
	 *            文件名
	 * @param oldpath
	 *            旧目录
	 * @param newpath
	 *            新目录
	 * @param cover
	 *            若新目录下存在和转移文件具有相同文件名的文件时，是否覆盖新目录下文件，cover=true将会覆盖原文件，否则不操作
	 */
	public static void changeDirectory(String filename, String oldpath, String newpath, boolean cover) {
		boolean flag = false;
		if (!oldpath.equals(newpath)) {
			File oldfile = new File(oldpath + filename);
			File newfile = new File(newpath + filename);
			if (newfile.exists()) {// 若在待转移目录下，已经存在待转移文件
				if (cover) { // 覆盖
					newfile.delete();
					flag = oldfile.renameTo(newfile);
				} else {
					 LOG.info("在新目录下已经存在：" + filename);
				}
			} else {
				flag = oldfile.renameTo(newfile);
			}
		}
		if (!flag) {
			 LOG.info("文件轉移失敗!");
		} else {
			 LOG.info("文件轉移成功!");
		}
	}
}
