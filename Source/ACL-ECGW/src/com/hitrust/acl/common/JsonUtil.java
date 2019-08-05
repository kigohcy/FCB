/*
 * @(#) JsonUtil.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/01/18, Eason Hsu
 * 	 1) First release
 * 
 */

package com.hitrust.acl.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {

	/**
	 * 依據傳入物件產生 JSON 字串
	 * @param obj	 要轉換的物件
	 * @param isNull 欄位 null 值控制
	 * 			true: 當欄位為null, 會轉換為"null"字串; 
	 * 			false:當欄位為null, 不會顯示該欄位名稱
	 * @return
	 */
	public static String object2Json(Object obj, boolean isNull) {
		
		GsonBuilder builder = new GsonBuilder();
		
		// 若預期欄位值可能有 NULL 的情況, GsonBuilder 需先執行 serializeNulls, 否則欄位值為 NULL 則不會被轉換
		if (isNull) {
			builder.serializeNulls();
		}
		
		Gson gson = builder.create();
		
		return gson.toJson(obj);
	}
	
	/**
	 * JSON 字串轉換成物件
	 * @param jsonStr   JSON 字串
	 * @param pojoClass JSON 字串對應物件
	 * e.g1: String jsonStr1 = "{name:Test, year:18}";
	 * MyJson json = json2Object(jsonStr1, MyJson.class);
	 * 
	 * e.g2: String jsonStr2 = "{name:Test, year:18},{name:Test, year:18}";
	 * MyJson[] jsons = json2Object(jsonStr1, MyJson[].class);
	 * @return obj
	 */
	public static Object json2Object(String jsonStr, Class<?> pojoClass) {
		
		Object obj = null;
		
		GsonBuilder builder = new GsonBuilder();
		
		Gson gson = builder.create();
		
		obj = gson.fromJson(jsonStr, pojoClass);
		
		return obj;
	}
}
