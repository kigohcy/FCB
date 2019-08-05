package com.hitrust.acl.common;

import java.io.File;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;

public class CommonUtil {
	static Logger LOG = Logger.getLogger(CommonUtil.class);

	/**
	 * 上傳FTP
	 * 
	 * @param ip
	 *            : FTP IP
	 * @param port
	 *            : FTP port
	 * @param user
	 *            : FTP user
	 * @param pswd
	 *            : FTP pswd
	 * @param mode
	 *            : FTP傳輸模式(A:主動/P:被動)
	 * @param remoteDir
	 *            : FTP遠端目錄
	 * @param localPath
	 *            : 本地端目錄
	 * @param filename
	 *            : 檔名
	 * @return int 0=Success, 1=Fail
	 */
	public static int uploadToFTP(String ip, int port, String user, String pswd, String mode, String remoteDir, String localPath,
			String filename) {

		AclFtpClient client = null;
		try {
			client = AclFtpClient.newInstance(ip, port, user, pswd, mode, remoteDir);
			if (!client.isReady()) {
				LOG.error("FTP 未正確連接!");
				return 1;
			}
			if (!client.upload(filename, FileUtil.readFile(localPath + filename), true)) {
				LOG.error("文件" + filename + "上傳至FTP失敗！");
				return 1;
			}
			LOG.info("文件" + filename + "上傳至FTP成功！");
			return 0;
		} catch (Exception e) {
			LOG.error("uploadToFTP " + filename + " error:" + e.toString(), e);
			return 1;
		} finally {
			if (client != null) {
				client.releaseIfnessary();
			}
		}
	}

	/**
	 * 備份 localPath 下的檔案到 backupPath
	 * 
	 * @param localPath
	 *            本地端目錄
	 * @param backupPath
	 *            備份目錄
	 * @param endsWith
	 *            備份檔名結尾字串
	 * @return int 0=Success, 1=Fail
	 * @throws Exception
	 */
//	public static int backupFiles(String localPath, String backupPath, String endsWith) {
//		int rtn = 0;
//		try {
//			// IF本地端目錄不存在則建立
//			if (!StringUtil.isBlank(localPath)) {
//				try {
//					File tmpDir = new File(localPath);
//					if (!tmpDir.exists()) {
//						LOG.info("create localPath:" + localPath);
//						tmpDir.mkdirs();
//					}
//				} catch (Exception e) {
//					LOG.error("create localPath error:" + e.toString(), e);
//					CawtoHelper.post("", CawtoHelper.CTGY_FT, "03", "目錄建立失敗", e.getMessage());
//					return 1;
//				}
//			}
//
//			// IF備份目錄不存在則建立
//			File backupDir = null;
//			if (!StringUtil.isBlank(backupPath)) {
//				try {
//					backupDir = new File(backupPath);
//					if (!backupDir.exists()) {
//						LOG.info("create backupPath:" + backupPath);
//						backupDir.mkdirs();
//					}
//				} catch (Exception e) {
//					LOG.error("create backupPath error:" + e.toString(), e);
//					CawtoHelper.post("", CawtoHelper.CTGY_FT, "03", "目錄建立失敗", e.getMessage());
//					return 1;
//				}
//			}
//
//			// 備份 localPath 下的檔案到 backupPath
//			String today = DateUtil.getToday();
//			// String today = DateUtil.getCurrentTime("DT","AD");
//
//			File[] files = new File(localPath).listFiles();
//			for (File file : files) {
//				try {
//					if (file.getName().endsWith(endsWith)) {
//						backupDir = new File(localPath + file.getName());
//						String backupName = file.getName().substring(0, file.getName().length() - 4) + "-" + today + ".csv";
//						boolean bakResult = backupDir.renameTo(new File(backupPath + backupName));
//						if (!bakResult) {
//							LOG.debug("backup " + file.getName() + ":" + bakResult);
//						}
//					}
//				} catch (Exception e) {
//					LOG.error("backup file error:" + e.toString(), e);
//					CawtoHelper.post("", CawtoHelper.CTGY_FT, "04", "檔案備份失敗(" + file.getName() + ")", e.getMessage());
//					rtn = 1;
//				}
//			}
//
//		} catch (Exception e) {
//			rtn = 1;
//			LOG.error("Exception:" + e.toString(), e);
//			CawtoHelper.post("", CawtoHelper.CTGY_ER, "01", "其他錯誤", e.getMessage());
//		}
//
//		return rtn;
//	}

	/**
	 * rename檔名, eg:ACL-AllPAY-DAILY.csv -> ACL-AllPAY-DAILY-20160223.csv
	 * 
	 * @param localPath
	 * @param filename
	 * @return String backupName
	 */
	public static String renameFile(String localPath, String filename) {
		String today = DateUtil.getToday();
		File file = new File(localPath + filename);
		String backupName = filename.substring(0, filename.length() - 4) + "-" + today + ".csv";
		boolean bakResult = file.renameTo(new File(localPath + backupName));
		if (!bakResult) {
			LOG.error("renameFile fail:" + file.getName() + ":" + bakResult);
			return null;
		} else {
			return backupName;
		}
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceNull(String str) {
		if (str == null || str.equalsIgnoreCase("null")) {
			return "";
		} else
			return str;
	}
	
	
	/**
     * 將金額格式化為上行電文所需的字串格式，長度不足部分會自動補'0'
     *
     * @param amnt 金額
     * @param decimalLength 整數位長度
     * @param decimalDigits 小數位
     * @return 上行電文金額格式字串
     */
    public static String fmtAmnt4TeleUpld(double amnt, int decimalLength, int decimalDigits) {
        // 整數位長度不可小於0
        if (decimalLength<0 || decimalLength>18) {
            return "";
        }
        // 小數位不可小於0
        if (decimalDigits<0) {
            return "";
        }
        // 組整數位pattern
        StringBuffer patternBuf = new StringBuffer();
        for (int i=0; i<decimalLength; i++) {
            patternBuf.append("0");
        }
        // 組小數位pattern
        if (decimalDigits >0) {
            patternBuf.append(".");
            for (int i=0; i<decimalDigits; i++) {
                patternBuf.append("0");
            }
        }
        // 依pattern格式化金額
        DecimalFormat decimalFormat = new DecimalFormat(patternBuf.toString());
        return decimalFormat.format(amnt);
    }
}
