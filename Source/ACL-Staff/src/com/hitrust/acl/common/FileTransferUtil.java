package com.hitrust.acl.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * 檔案傳輸的共用程式
 * <p/>
 * Package: com.hitrust.mnb.adm.util <br>
 * File Name: FileTransferUtil.java <br>
 * <p/>
 * Purpose: <br>
 * 實現檔案傳輸的共用程式操作 <br>
 *
 * @author Emma
 * @version 1.0   2010/12/02
 *  <ul>
 *        <li> Created </li>
 *   </ul>
 */
public class FileTransferUtil {
	
	private static Logger  LOG = Logger.getLogger(FileTransferUtil.class);
	
   
	/**
	 * 將資料儲存至指定的檔案中
	 * @param fileDir - 檔案目錄(結尾需含"/")
	 * @param fileName - 檔案名稱
	 * @param data - 寫入檔案中的資料
	 * @return true - 寫檔成功 false : 寫檔失敗
	 */
	public boolean saveFile(String filePath, String fileName, String data){
		
		//檢查並建立路徑
		this.chkAndCreateDir(filePath);

		//存放檔案名稱
		String newFileName =filePath +fileName;

		//將檔案寫入
		BufferedWriter bw =null;
		
		try{
			bw = new BufferedWriter(new FileWriter(newFileName));
			bw.write(data);
			bw.flush();
		}catch(IOException e){
			LOG.error("檔案寫入錯誤:(" + newFileName + ")" +e.getMessage());
			return false;
		}finally{
			if(bw != null){
				try {					
					bw.close();
				} catch (IOException e){
					//never happen
					LOG.error("檔案寫入錯誤:(" + newFileName + ")" +e.getMessage());
//					return false;
				}				
			}
		}
		LOG.info("產生檔案 : "+newFileName);
		return true;

	}
	

	
	/**
	 * 檢查路徑是否存在，若不存在則新增(減少 I/O 負荷，由最後一層檢核回來)
	 * @param path 路徑字串 (字串中"\"請以"\\"代替)
	 * @throws ApplicationException 路徑錯誤或路徑不存在建立目錄失敗時，拋出 ApplicationException
	 */
	private void chkAndCreateDir(String path){

		if(path == null || path.equalsIgnoreCase("")){
			LOG.error("傳入路徑字串為空");
			return;
		}

		//使字串中目錄符號(File.separator)統一
		path = new File(path).getPath();
		
		//將路徑字串分割
		List pathPartList = new ArrayList();
		for(int i=0;i<path.length();i++){
			if(File.separator.equals(String.valueOf(path.charAt(i)))){
				pathPartList.add(path.substring(0,i));
			}
		}
		pathPartList.add(path);
		
		//檢查路徑
		this.recursionChk(pathPartList);
	}

	/**
	 * 檢查路徑是否存在，若不存在則新增(減少 I/O 負荷，由最後一層檢核回來)
	 * @param pathPartList 處理後之路徑字串
	 * @throws ApplicationException 建立目錄失敗時，拋出 ApplicationException
	 */
	private String recursionChk(List pathPartList){

		if(pathPartList == null || pathPartList.size()<1) return "";

		File dir= new File((String)pathPartList.get(pathPartList.size()-1));

		//取得路徑字串
		String filePath = (String)pathPartList.get(pathPartList.size()-1);

		try {
			//檢查路徑是否存在
			if (!dir.exists() || !dir.isDirectory()) {

				//檢查上層路徑是否存在
				if(pathPartList.size()>1){
					//將最底層（最後一筆去除，並傳入檢查）
					pathPartList.remove(pathPartList.size()-1);
					this.recursionChk(pathPartList);
				}

				//創建目錄，若失敗則傳回 ""
				if(dir.mkdir()){
		        	LOG.info("建立目錄 : "+filePath);
				}else{
					filePath = "";
					LOG.error("建立目錄失敗:" + filePath);
				}
			}
		} finally{
			dir = null;
		}
		return filePath;
	}	
	
}
