package com.hitrust.bank.telegram.util;

import java.util.ResourceBundle;

public class ConstantUtil {
	
	public static String APPLY_PIID_REQ_PARAM = "PIID";//
	
	static ResourceBundle bundle = ResourceBundle.getBundle("ecgw");
	
	//public static String SYSTEM_CODE = bundle.getString("SYSTEM_CODE");//系統代碼
	
	//public static String SYSTEM_KEY = bundle.getString("SYSTEM_KEY");//
	
	//public static String PDID_KEY = bundle.getString("PDID_KEY");//
	
	//public static String SYSTEM_DISTRICT = bundle.getString("system.district");
	
	public static String STR_DISABLED = "DISABLED";
	
	/**
	 * Schedule Constant
	 */
	// Job config key
	public static final String SCHEDULE_OBJ="SCHEDULE_OBJ";
	
	// Job data key
	public static final String SCHEDULE_DATA="SCHEDULE_DATA";
	
	// Job execute keys
	public static final String SCHEDULE_EXECUTE_TYPE="SCHEDULE_EXECUTE_TYPE";
	public static final String SCHEDULE_EXECUTE_DATE="SCHEDULE_EXECUTE_DATE";
	public static final String SCHEDULE_EXECUTE_TIME="SCHEDULE_EXECUTE_TIME";
	
	// Job状态
	public static final String STATUS_ACTIVE="0";
	public static final String STATUS_INACTIVE="1";
	
	// Job运行状态
	public static final String RUNNING="0";
	public static final String NOT_RUNNING="1";
	
	// Job是否待更新
	public static final String MANAGEMENT_NEED_UPDATE="0";
	public static final String DAYEND_NEED_UPDATE="2";
	public static final String HAVE_UPDATED="1";
	public static final String REDO_NEED_UPDATE="3";
	
	// Job執行類型
	public static final String REGULAR_EXECUTE = "1";
	public static final String DAYEND_EXECUTE  = "2";
	public static final String REDO_EXECUTE    = "3";
	
	public static final String MQ_COMMUNICATION_MESSAGE = "主機連線中斷，請稍後再試";
}
