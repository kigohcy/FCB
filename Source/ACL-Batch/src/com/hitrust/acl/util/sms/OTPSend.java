package com.hitrust.acl.util.sms;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import com.firstbank.batch.AlertJob;
import com.google.gson.Gson;
import com.hitrust.acl.util.AppEnv;
public class OTPSend {
	static Logger LOG = Logger.getLogger(OTPSend.class);
	public static String SMSURL = AppEnv.getString("SMS_URL");
	public static String SMSENCDOE = AppEnv.getString("SMS_ENCDOE");
	
	public String UID; 
	public String Pwd; 
	public String DA; 
	public String SM; 
	public String BUSINESSCODE; 
	public String STOPTIME; 
	public String AParty;
	public String TEMPLATEVAR;
	public static OTPReceive[] sendService( OTPSend data ) throws Exception {
		
        OTPReceive[] recvVO = null;
        Gson gson = new Gson();
        try {
        	LOG.info(data.toParameters());
            URL url = new URL(SMSURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(data.toParameters().getBytes("utf-8"));
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            recvVO = gson.fromJson(sb.toString(), OTPReceive[].class);
            LOG.info(sb.toString());
        }catch (Exception e) {
                throw (e);
        }
        return recvVO;
	}
	public String toParameters(){
		return "UID="+this.UID+"&PWD="+this.Pwd+"&DA="+this.DA+"&SM="+this.SM+"&BUSINESSCODE="+this.BUSINESSCODE+"&STOPTIME="+this.STOPTIME+"&AParty="+this.AParty+"&TEMPLATEVAR="+this.TEMPLATEVAR;
	}
}
