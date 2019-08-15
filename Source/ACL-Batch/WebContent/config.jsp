<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Iterator" %>
<%@page import="org.springframework.context.ApplicationContext" %>
<%@page import="com.hitrust.acl.util.SpringHelper" %>
<%@page import="com.hitrust.acl.model.SysParm" %>
<%@page import="com.hitrust.acl.service.SysParmService" %>
<%@page import="com.hitrust.acl.util.AclApplicationContextAwarer" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger("config.jsp");
    
	String root = request.getContextPath();
	String rtnMsg = (String)request.getAttribute("_rtnMsg");
	
	ApplicationContext ac=null;
    
    if (ac == null){
		ac = AclApplicationContextAwarer.getApplicationContext();//首先嘗試取得framework自動載入的ac
		if (ac == null){
	ac = SpringHelper.getApplicationContext();//取不到freamwork的ac，則載入預設的SrpingHelper
		}
	}
    
    SysParmService sysParmService =  (SysParmService) ac.getBean("sysParmService");
    
    List<SysParm> SysParmList = new ArrayList<SysParm>();
    SysParmList =  sysParmService.getSysParmListLike("ALERT_%");
    
    String alert_frequency     ="";
    String alert_mail_receiver ="";
    String alert_period        ="";
    String alert_sms_end       ="";
    String alert_sms_frequency ="";
    String alert_sms_period    ="";
    String alert_sms_start     ="";
    String alert_sms_status    ="";
    String alert_sms_telno     ="";
    String alert_except        ="";
    
   	 for (Iterator<SysParm> it = SysParmList.iterator(); it.hasNext();) {
		SysParm SYPM = (SysParm) it.next();
		if (SYPM.getParmCode().equals("ALERT_FREQUENCY"))    {alert_frequency     =SYPM.getParmValue();}
		if (SYPM.getParmCode().equals("ALERT_MAIL_RECEIVER")){alert_mail_receiver =SYPM.getParmValue();}
		if (SYPM.getParmCode().equals("ALERT_PERIOD"))       {alert_period        =SYPM.getParmValue();}
		if (SYPM.getParmCode().equals("ALERT_SMS_END"))      {alert_sms_end       =SYPM.getParmValue();}
		if (SYPM.getParmCode().equals("ALERT_SMS_FREQUENCY")){alert_sms_frequency =SYPM.getParmValue();}
		if (SYPM.getParmCode().equals("ALERT_SMS_PERIOD"))   {alert_sms_period    =SYPM.getParmValue();}
		if (SYPM.getParmCode().equals("ALERT_SMS_START"))    {alert_sms_start     =SYPM.getParmValue();}
		if (SYPM.getParmCode().equals("ALERT_SMS_STATUS"))   {alert_sms_status    =SYPM.getParmValue();}
		if (SYPM.getParmCode().equals("ALERT_SMS_TELNO"))    {alert_sms_telno     =SYPM.getParmValue();}
		if (SYPM.getParmCode().equals("ALERT_EXCEPT"))       {alert_except        =SYPM.getParmValue();}
	 }

	try {
%>
<html>
	<head>
		<title>Batch config</title>
		<style type="text/css">
		<!--
		.content {  font-family: Verdana, Arial; font-size: 10pt; color: #000000}
		p {  font-family: Verdana, Arial; font-size: 10pt; color: #000000}
		-->
		</style>
		<script type="text/javascript" src="./js/jquery-3.4.1.min.js"></script>
		
		<script language="JavaScript">
		
			$(document).ready(function() {
				$('input[type="text"]').keyup(function(event) {
					$('input[type="text"]').each(function() {
						if ($(this).val() == "") {
							alert('請勿輸入空白資料!');
							$(this).focus();
							return false;
						}
					});
				});

				$('input[id$=frequency]').on("keypress keyup blur",function (event) {    
			           $(this).val($(this).val().replace(/[^\d].+/, ""));
			            if ((event.which < 48 || event.which > 57)) {
			                event.preventDefault();
			            }
			        });
				$('input[id$=period]').on("keypress keyup blur",function (event) {    
			           $(this).val($(this).val().replace(/[^\d].+/, ""));
			            if ((event.which < 48 || event.which > 57)) {
			                event.preventDefault();
			            }
			        });
			});
		</script>	
		
	</head>
  	<body>
	<h1>設定MAIL及簡訊發送條件</h1>  
	<form name="form1" id="form1" method="post" action="<%=root%>/ConfigServlet">
		<input type="hidden" name="jspUrl" value="/config.jsp"/>
		<input type="hidden" name="dataDate" value=""/>
		<table border="1" width="80%">
		
			<tr bgcolor="#63ad5c" >
                <th align='center' width="10%"><font color="white" >設定項目</font></th>
                <th align='center' width="70%"><font color="white" >設定內容</font></th>
            </tr>
			<tr>
                <td rowspan="2" align='center'>MAIL發送條件</td>
                <td>
                                                  前&nbsp;<input type="text" name="alert_period"  id="alert_period" maxlength="3" size="3" value="<%=alert_period%>">分鐘累積錯誤達
                  <input type="text" name="alert_frequency" id="alert_frequency" maxlength="2" size="2" value="<%=alert_frequency%>">次(含)以上，發送MAIL
                </td>
			</tr>
			<tr>
			   <td>
			            收件者信箱：  <input type="text" name="alert_mail_receiver"  size="90" value="<%=alert_mail_receiver%>">
			             <BR>(二個以上信箱以逗號分隔)
			   </td>
			</tr>
			<tr>
                <td rowspan="4" align='center'>簡訊發送條件</td>
                 <td>
			                   簡訊發送： 
			         <input type="radio" name="alert_sms_status" id="alert_sms_status" value="ON" 
			         <% if ("ON".equals(alert_sms_status)) { %>
			        	checked
			         <%} %> />開啟
			         <input type="radio" name="alert_sms_status" id="alert_sms_status" value="OFF"
			         <% if ("OFF".equals(alert_sms_status)) { %>
			        	checked
			         <%} %> />關閉
			     </td>
			</tr>
			<tr>   
			    <td>
                                                  前&nbsp;<input type="text" name="alert_sms_period" id="alert_sms_period" maxlength="3" size="3" value="<%=alert_sms_period%>">分鐘累積錯誤達
                  <input type="text" name="alert_sms_frequency"  id="alert_sms_frequency" maxlength="2" size="2" value="<%=alert_sms_frequency%>">次(含)以上，發送簡訊
                </td>
             </tr>
			
			<tr>
			   <td>
			               手機號碼：  <input type="text" name="alert_sms_telno"  size="90" value="<%=alert_sms_telno%>">
			     <BR>(二個以上手機號碼以逗號分隔)
			   </td>
			</tr>
			<tr>   
			    <td>
                                                 簡訊發送時段：每日由<input type="text" name="alert_sms_start" maxlength="5" size="5" value="<%=alert_sms_start%>">至
                  <input type="text" name="alert_sms_end" maxlength="5" size="5" value="<%=alert_sms_end%>">(時間格式hh:mm)，發送簡訊，其餘時間不發送
                </td>
             </tr>
             <tr>
                <td  align='center'>不發送錯誤通知的錯誤代碼</td>
                <td>
                   <input type="text" name="alert_except" id="alert_except" size="90" value="<%=alert_except%>">
                   <BR>(二個以上錯誤代碼以逗號分隔)
                </td>
			</tr>
		</table>
		<br>
		<input type="submit" name="btn1" id="btn1" value="變更設定" >
		<input type ="button" onclick="javascript:location.href='<%=root%>/rerun.jsp'" value="回手動執行批次"></input>
	</form>
	<hr>
	<p class="content">
<%
	if(!(rtnMsg == null || rtnMsg.trim().equals(""))){
		out.println("<p>執行結果：");
		out.println(rtnMsg.toString());
		out.println("<p>");
	}
%>
	</p>
	<script language="JavaScript">
			
	
		$('#form1').submit(function( event ) {
			
						if (!ValidateEmail($(
							'input[name="alert_mail_receiver"]').val())) {
							alert("Email格式輸入錯誤，請再次確認。");
							$('input[name="alert_mail_receiver"]').focus();
							 return false;
						}

						if (!ValidateTelno($(
							'input[name="alert_sms_telno"]').val())) {
					    	alert("手機號碼格式輸入錯誤，請再次確認。");
							$('input[name="alert_sms_telno"]').focus();
					 		return false;
				        }

						if (!ValidateTime($(
							'input[name="alert_sms_start"]').val())) {
					    	alert("開始時間格式輸入錯誤，請再次確認。");
							$('input[name="alert_sms_start"]').focus();
					 		return false;
				        }
				        
						if (!ValidateTime($(
							'input[name="alert_sms_end"]').val())) {
					    	alert("結束時間格式輸入錯誤，請再次確認。");
							$('input[name="alert_sms_end"]').focus();
					 		return false;
				        }	


				        var istarttime=parseInt($('input[name="alert_sms_start"]').val().replace(":",""));
				        var iendtime=parseInt($('input[name="alert_sms_end"]').val().replace(":",""));

				        if (istarttime >= iendtime){
				        	alert("開始時間要小於結束時間!");
							return false;
					    }

						if (!$('input[name="alert_sms_status"]:checked').length) {
							alert("請選擇是否開啟簡訊服務!");
							 return false;
						}

						document.getElementById("btn1").value = "執行中..";
						document.getElementById("btn1").disabled = true;
		   });
		
			function ValidateEmail(inputText) {
				var mailformat = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
				var mailarray = inputText.split(',');
				for (i = 0; i < mailarray.length; i++) {
					if (mailarray[i].match(mailformat)) {
					} else {
						return false;
					}
				}
				return true;
			}

			function ValidateTelno(inputText) {
				var Telnoformat = /^\d+$/;
				var Telnoarray = inputText.split(',');
				for (i = 0; i < Telnoarray.length; i++) {
					if (Telnoarray[i].match(Telnoformat) ) {
					} else {
						return false;
					}
				}
			  return true;
		    }

			
			function ValidateTime(inputText) {
				var Timeformat = /([01][0-9]|[02][0-3]):[0-5][0-9]/;
					if (inputText.match(Timeformat) ) {
					} else {
						return false;
					}
			  return true;
		    }
			
		</script>
	</body>
</html>
<%
} catch(Exception ex) {
	LOG.error(ex.toString());
}
%>
