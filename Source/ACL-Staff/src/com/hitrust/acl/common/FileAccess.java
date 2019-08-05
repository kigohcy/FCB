package com.hitrust.acl.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 文件訪問處理類
 * <p/>
 * Package: com.hitrust.mnb.adm.util <br>
 * File Name: FileAccess.java <br>
 * <p/>
 * Purpose: <br>
 * 實現文件的讀取操作 <br>
 *
 * @author JmiuHan
 * @version 1.0   2010/12/02
 *  <ul>
 *        <li> Created </li>
 *   </ul>
 */
public class FileAccess {
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

}
