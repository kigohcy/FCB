/**
 * 
 */
package com.hitrust.acl.util;

import org.apache.log4j.Logger;
import com.ca.ppm.clients.sdk.PUPMSDK;
import com.ca.ppm.clients.sdk.PUPMSDKInterface;

public class CAHelper {
	static Logger LOG = Logger.getLogger(CAHelper.class);
	
	public CAHelper() {
	}
	
	public static synchronized String getFtpPwd(){
		
		//回覆訊息
		String rtn= "";
		//指定該端點的類型
		String endpointTypeVal = AppEnv.getString("endpointTypeVal");
		//該帳號Endpoint名稱
		String endpointNameVal = AppEnv.getString("endpointNameVal");
        //該特權帳號名稱
        String accountNameVal = AppEnv.getString("accountNameVal");
        //登入帳號的類型
		String accountContainerVal = AppEnv.getString("accountContainerVal");
		
		PUPMSDKInterface sdk = new PUPMSDK();
        try{
        	
        	rtn = sdk.checkout(endpointTypeVal, endpointNameVal, accountNameVal, accountContainerVal);
            
        }catch(Exception e){
        	LOG.error("Exception:"+e.toString(), e);
        }catch (Throwable t) {
        	LOG.error("Throwable:"+t.toString(), t);
		}

        return rtn;
	}
}
