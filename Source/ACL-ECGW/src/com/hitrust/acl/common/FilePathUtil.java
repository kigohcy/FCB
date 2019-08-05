/**
 * @(#) FilePathUtil.java
 *
 * Directions:
 *
 * Copyright (c) 2017 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 * 	v1.00, 2017/09/27, Eason Hsu
 *	 1) First release
 *
 */

package com.hitrust.acl.common;

import java.io.UnsupportedEncodingException;

import com.hitrust.acl.util.Base64;

public class FilePathUtil {

	 /**
     * 做異常的路徑文字內容驗證(../)
     * 
     * @param input
     * @return
     */
    public static boolean validateBackPath(String input) {
        String back = "../";

        if ((input == null) || (input.length() == 0)) {
            return true;
        }
        return input.contains(back);
    }

    /**
     * 使用base64逐筆替換路徑文字內容
     * 
     * @param aString
     * @return
     */
    public static String cleanString(String aString) {

        if (aString == null) {
            return null;
        }
        String cleanString = "";
        
        try {
            aString = Base64.encode(aString.getBytes("UTF-8"));
            
            for (int i = 0; i < aString.length(); ++i) {
                cleanString += cleanChar(aString.charAt(i));
            }
            cleanString = new String(Base64.decode(cleanString), "UTF-8");
            
        } catch (UnsupportedEncodingException e) {
            return cleanString;
        }
        return cleanString;
    }

    /**
     * 逐筆替換路徑文字內容
     * 
     * @param aChar
     * @return
     */
    private static char cleanChar(char aChar) {
        // 0 - 9
        for (int i = 48; i < 58; ++i) {
            if (aChar == i) {
                return (char) i;
            }
        }
        // 'A' - 'Z'
        for (int i = 65; i < 91; ++i) {
            if (aChar == i) {
                return (char) i;
            }
        }
        // 'a' - 'z'
        for (int i = 97; i < 123; ++i) {
            if (aChar == i) {
                return (char) i;
            }
        }
        // other valid characters
        switch (aChar) {
            case '/':
                return '/';
            case '+':
                return '+';
            case ' ':
                return ' ';
            case '=':
                return '=';
        }
        return '%';
    }

}
