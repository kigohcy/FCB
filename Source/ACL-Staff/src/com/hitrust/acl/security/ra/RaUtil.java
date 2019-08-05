/**
 * @(#)RaUtil.java
 *
 * Copyright (c) 2013 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2013/10/28, Ada Chen
 *   1) First release
 */
package com.hitrust.acl.security.ra;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.hitrust.acl.common.TBCodeHelper;
import com.hitrust.acl.util.StringUtil;
import com.hitrust.framework.exception.ApplicationException;
import com.hitrust.framework.exception.BusinessException;

public class RaUtil {
	static Logger LOG = Logger.getLogger(RaUtil.class);

	public static final String HASH_MD5 = "MD5";
	public static final String HASH_SHA1 = "SHA-1";
	public static final String HASH_SHA = "SHA";
	
	// 憑證效期
	public static final String ONE_SIGN = "ONE_SIGN"; // 一年期憑證
	public static final String TWO_SIGN = "TWO_SIGN"; // 二年期憑證
	
	/**
	 * 初始化 RA Server 連線
	 * @param raUrl RA Server's URL
	 * @param raAccount RA Server account for AP Service
	 * @return RaService object
	 * @throws ApplicationException
	 */
	//TODO raPhrase 密碼處理 
	public static RaService initRaService(String raUrl, String raAccount, String raPhrase) throws BusinessException {
		
        LOG.info("[raUrl]"+raUrl+"[raAccount]"+raAccount);
		// 1.Get RA parameters
        String queue = "N";
        String ipAddress;
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			LOG.info("[UnknownHostException]"+e.toString());
			ipAddress = "";
		}
        // 2.Check RA parameters
        if (StringUtil.isBlank(raUrl) || StringUtil.isBlank(queue) || 
        		StringUtil.isBlank(raAccount) ||  StringUtil.isBlank(raPhrase) || StringUtil.isBlank(ipAddress)) {
            LOG.info("========== " + "[raUrl]: " + raUrl + " [queue]: " + queue + " ==========");
            LOG.info("========== " + "[raAccount]: " + raAccount + "[raPhrase]: " + raPhrase + " [ipAddress]: " + ipAddress + " ==========");
            throw new BusinessException("message.ra.paramSetError");
        }
        // 3.Initial RA service
        RaConfig raConfig = new RaConfig(raUrl, raAccount, raPhrase, ipAddress);
        return new RaService(raConfig);
    }
    
    /**
     * 轉換RAO API回應ReturnCode為對應訊息
     * @param rtnCode Return Code
     * @param raMsg 原API取得之錯誤訊息, 以做Log之用
     * @param locale Locale object
     * @return RA中文錯誤訊息
     */
    public static String getRAErrorMsg(int rtnCode, String raMsg, Locale locale){
    	String raErrorMsg = "";
        LOG.error("================ RA Service Result Start =================");
        LOG.error("RA Error Code: " + rtnCode);
        LOG.error("RA Error Msg: " + raMsg);
        LOG.error("================ RA Service Result Message ===============");
    	
    	// RA Server訊息需顯示於前端
        TBCodeHelper helper = new TBCodeHelper("06", String.valueOf(rtnCode));
        raErrorMsg = helper.getTbCodeMsg();
        
        LOG.error("[RA_" + rtnCode + "] - " + raErrorMsg);
        
        raErrorMsg.concat("(");
        raErrorMsg.concat(String.valueOf(rtnCode));
        raErrorMsg.concat("-");
        raErrorMsg.concat(raMsg);
        raErrorMsg.concat(")");

    	//過濾RA錯誤訊息中, 含有URL格式的字串
        raErrorMsg = replaceURLToSpecChar(raErrorMsg, "#####");
       
        LOG.error("================ RA Service Result End ===================");
        return raErrorMsg;
    }
    
	/**
	 * 將符合URL格式的字串取代成指定字元(串)
	 * @param url 含URL格式字串
	 * @param specChar 欲取代的指定字元(串)
	 * @return 取代URL格式後的新字串
	 */
	private static String replaceURLToSpecChar(String url, String specChar){
		String REGEX = "[a-zA-Z]+://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\s*)?(\\:[0-9]+)*(/[a-zA-Z0-9]*)*";
		String newURL = url;
		
		if (url == null || url.length() == 0) return newURL;
		
		Pattern mask = Pattern.compile(REGEX);
		//get a matcher object
		Matcher matcher = mask.matcher(newURL); 
		//將符合URL格式的字串取代成指定字元
		newURL = matcher.replaceAll(specChar);
		
		return newURL;
	}
    
	/**
	 * @param algorithm
	 * @param plaintext
	 * @return
	 */
	public byte[] hashBySunJCE(String algorithm, String plaintext) {
		if (!algorithm.equals(HASH_MD5) && !algorithm.equals(HASH_SHA1)) {
			return null;
		}
		//
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (java.security.NoSuchAlgorithmException nsa) {
			System.out.println(nsa.toString());
			return null;
		}
		return md.digest(plaintext.getBytes());
	}

	/**
	 * @param b
	 * @param buf
	 */
	public void byte2hex(byte b, StringBuffer buf) {
		char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		int high = ((b & 0xf0) >> 4);
		int low = (b & 0x0f);
		buf.append(hexChars[high]);
		buf.append(hexChars[low]);
	}

	/**
	 * @param block
	 * @return
	 */
	public String toHexString(byte[] block) {
		StringBuffer buf = new StringBuffer();

		int len = block.length;

		for (int i = 0; i < len; i++) {
			byte2hex(block[i], buf);
		}
		return buf.toString();
	}

    /**
     * @param dateOfCert
     * @return
     */
    public static String formatDateOfCert(String dateOfCert) {
        SimpleDateFormat inSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outSdf = new SimpleDateFormat("yyyyMMddHHmmss");
        //
        if (dateOfCert == null || dateOfCert.trim().equals(""))
            return dateOfCert;
        // trim last two char '.0'
        if (dateOfCert.getBytes().length != 21) return dateOfCert;
        dateOfCert = dateOfCert.substring(0, (dateOfCert.getBytes().length-2));
        // format
        try {
            dateOfCert = outSdf.format(inSdf.parse(dateOfCert));
            return dateOfCert;
        } catch (Exception e) {
            return dateOfCert;
        }
    }
    
}
