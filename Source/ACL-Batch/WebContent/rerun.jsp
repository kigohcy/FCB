<%@page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
    org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger("rerun.jsp");
    
	String root = request.getContextPath();
	String rtnMsg = (String)request.getAttribute("_rtnMsg");
%>
<%
try
{
%>
<html>
	<head>
		<title>Batch rerun</title>
		<style type="text/css">
		<!--
		.content {  font-family: Verdana, Arial; font-size: 10pt; color: #000000}
		p {  font-family: Verdana, Arial; font-size: 10pt; color: #000000}
		-->
		</style>
		<script language="JavaScript">
		function doSubmit() {
			
			var count = 0;
			var radioCnt = 0;
			var inputDateArray = document.getElementsByName("inputDate");
			for(var i=0; i < form1.length; i++) {
				// check if type is Radio
				if(form1.elements[i].type =='radio') {
					// check if checked
					if(form1.elements[i].checked) {
						if("AclDcRpt03Job" != form1.elements[i].value && 
								"CheckCertJob" != form1.elements[i].value &&
 								"TrnsSyncJob" != form1.elements[i].value &&
								"DWFtpJob" != form1.elements[i].value &&
								"AlertJob" != form1.elements[i].value ){
							if(inputDateArray[radioCnt].value==''){
								alert("請輸入資料日期!");
								return;
							}
							//alert(form1.dataDate1[radioCnt].value);
							form1.dataDate.value = inputDateArray[radioCnt].value;
						}
						count++;
						break;
					}
					radioCnt++;
				}
			}
			
			if(count==0){
				alert("請選擇執行批次!");
				return;
			}
			
			document.getElementById("btn1").value= "執行中..";
			document.getElementById("btn1").disabled = true;
			//document.forms[0].submit();
			form1.submit();
		}
		</script>
	</head>
  	<body>
	<h1>手動執行批次</h1>  
	<form name="form1" method="post" action="<%=root%>/RerunServlet">
		<input type="hidden" name="jspUrl" value="/rerun.jsp"/>
		<input type="hidden" name="dataDate" value=""/>
		<table border="1" width="520">
			<tr bgcolor="#63ad5c" >
                <th align="center" width="10%"><font color="white" >選項</font></th>
                <th width="50%"><font color="white" >批次名稱</font></th>
                <th width="50%"><font color="white" >資料日期</font></th>
            </tr>
			<tr>
				<td align="center">
                    <input type="radio" name="batchId" value="JKOExchangeJob" />
                </td>
                <td>電商對帳檔產生批次</td>
                <td><input type="text" name="inputDate" maxlength="8" size="8" value="">
                	<span style="font-size:13px;">YYYYMMDD</span>
                </td>
			</tr>
			<tr>
				<td align="center">
                    <input type="radio" name="batchId" value="AclDcRpt03Job" />
                </td>
                <td>日終累算作業批次</td>
                <td><input type="hidden" name="inputDate" value="">
                </td>
			</tr>
			<tr>
				<td align="center">
                    <input type="radio" name="batchId" value="CheckCertJob" />
                </td>
                <td>憑證到期通知批次</td>
                <td><input type="hidden" name="inputDate" value="">
                </td>
			</tr>
			<tr>
				<td align="center">
                    <input type="radio" name="batchId" value="TrnsSyncJob" />
                </td>
                <td>不明交易同步</td>
                <td><input type="hidden" name="inputDate" value="">
                </td>
			</tr>
			<tr>
				<td align="center">
                    <input type="radio" name="batchId" value="DWFtpJob" />
                </td>
                <td>DW產檔</td>
                <td><input type="hidden" name="inputDate" value="">
                </td>
			</tr>
			<tr>
				<td align="center">
                    <input type="radio" name="batchId" value="AlertJob" />
                </td>
                <td>交易失敗次數過多<BR>發送MAIL及簡訊通知批次</td>
                <td><input type="hidden" name="inputDate" value="">
                <input type ="button" onclick="javascript:location.href='<%=root%>/config.jsp'" value="設定MAIL及簡訊發送條件"></input>
                </td>
			</tr>
		</table>
		<br>
		<input type="button" name="btn1" id="btn1" value="執行" onclick="doSubmit();">
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
	</body>
</html>
<%
} catch(Exception ex) {
	LOG.error(ex.toString());
}
%>
