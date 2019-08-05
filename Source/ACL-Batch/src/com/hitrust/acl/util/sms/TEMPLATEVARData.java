package com.hitrust.acl.util.sms;

import com.google.gson.Gson;
public class TEMPLATEVARData {
	public String smsdate; 
	public String smstime; 
	public String purpose; 
	public String category; 
	public String Hostcode; 
	public String brtno; 
	public String Userid;
	public String Accno;
	public String Username;
	public String Hostno;
	public String ToJson(){
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
