/**
 * @(#) CSVUtil.java
 *
 * Directions: CSV 檔案工具類
 *
 * Copyright (c) 2017 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *   v1.00, 2017/10/03, Caleb Chen
 *    1) First release
 *   
 */
package com.hitrust.acl.util;

import java.io.IOException;
import java.io.Writer;

public class CSVUtil {

    private static final char DEFAULT_SEPARATOR = ',';
    private static final String NEW_LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * 產生 CSV 格式一行資料
     * 
     * @param w :writer
     * @param values
     * @throws IOException
     */
    public static void writeLine(Writer w, String... values) throws IOException {

        boolean first = true;

        StringBuilder sb = new StringBuilder();
        
        for (String value : values) {
            if (!first) {
                sb.append(DEFAULT_SEPARATOR);
            }
            sb.append(value);

            first = false;
        }
        sb.append(NEW_LINE_SEPARATOR);
        w.append(sb.toString());
    }
}
