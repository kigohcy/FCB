
<%
/*
 * @(#)0201/01.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description:
 * 電商平台資料新增編輯頁
 *
 * Modify History:
 * v1.00, 2016/02/05, Evan
 * 	1)First Release
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>帳號連結扣款(Account Link)系統</title>

<%-- include Header, footer and menu --%>
<%@include file="/include/container.jsp"%>

</head>
<body>
	<!-- Container ============================================================================================== -->
	<div class="container">
		<!-- Content ------------------------------------------------------------------------>
		<div class="mainContent">
			<div class="content">
				<div class="fnctTitle">
					<fmt:message key="function.Id.F0905" />
					<c:out value=">"></c:out>
					<fmt:message key="common.btn.Modify" />
				</div>
				<form id="form1" name="form1" action="" method="post">
					<table id="datatable" class="vTable" width="480px;">
						<tr class="dataRowOdd">
							<th><fmt:message key="F0905.field.OLD_MEMA" />*</th>
							<%--請輸入舊登入密碼 * --%>
							<td>
								<input type="password" size="20" maxlength="20" name="oldMema" id="oldMema" tocomplete="off"/>
							</td>
						</tr>
						<tr class="dataRowEven">
							<th><fmt:message key="F0905.field.NEW_MEMA" />*</th>
							<%-- 請輸入新登入密碼 * --%>
							<td>
								<input type="password" size="20" maxlength="20" name="newMema" id="newMema" autocomplete="off"/>
							</td>
						</tr>
						<tr class="dataRowOdd">
							<th><fmt:message key="F0905.field.NEW_MEMA_AGAIN" />*</th>
							<%--請再輸入新登入密碼 * --%>
							<td>
								<input type="password" size="20" maxlength="20" name="newMemaAgain" id="newMemaAgain" autocomplete="off"/>
							</td>
						</tr>
					</table>
					<textarea align="center" rows="6" cols="54" id="area" readonly="readonly" style="color: blue;">
				
					</textarea>
				</form>
			</div>
			<!-- Button area ------------------------------------------------------------------------>
			<div class="btnContent">
				<%-- 確認 --%>
				<input type="button" id="btnOk" class="btnStyle"  onclick="return CheckPWD();" value="<fmt:message key="common.btn.OK"/>" />
				&nbsp;
			</div>
		</div>
		<div class="footer_line"></div>
	</div>
</body>

<script type="text/javascript">
	var textarea=document.getElementById("area");
	textarea.value="<fmt:message key="changeMema.field.textarea01" />"+"\n"+
					"<fmt:message key="changeMema.field.textarea02" />"+"\n"+
					"<fmt:message key="changeMema.field.textarea03" />"+"\n";
	
	function CheckPWD() {
		var pwd1_val = document.getElementById("oldMema").value;
		var pwd2_val = document.getElementById("newMema").value;
		var pwd3_val = document.getElementById("newMemaAgain").value;
		
		if(pwd1_val==''){
			alert("<fmt:message key="message.alert.pleaseKeyIn" /><fmt:message key="changeMema.field.oldMema" />");
			document.getElementById("oldMema").focus();
			return false;
		}
		
		if(pwd2_val==''){
			alert("<fmt:message key="message.alert.pleaseKeyIn" /><fmt:message key="changeMema.field.newMema" />");
			document.getElementById("newMema").focus();
			return false;
		}
		
		if(pwd2_val==''){
			alert("<fmt:message key="message.alert.pleaseKeyIn" /><fmt:message key="changeMema.field.newMemaAgan" />");
			document.getElementById("newMemaAgain").focus();
			return false;
		}
		
		if(pwd1_val==pwd2_val){
			alert("<fmt:message key="message.alert.memaErr01" />");
			document.getElementById("newMema").focus();
			return false;
		}
		if(pwd2_val.length < 8){
			alert("<fmt:message key="message.alert.memaErr02" />");
			document.getElementById("newMema").focus();
			return false;
		}
		if (!/^([a-zA-Z]+\d+|\d+[a-zA-Z]+)[a-zA-Z0-9]*$/.test(pwd2_val)) {  
			alert("<fmt:message key="message.alert.memaErr03" />");
			document.getElementById("newMema").focus();
			return false;  
		}
		
		var old = 0;
		var a; 
		var b; 
		var c;
		
		for(var i in pwd2_val){
			var next = pwd2_val[i].charCodeAt();
			if(old - next == -1){
				a = a+1;
				if(a>2){
					alert("<fmt:message key="message.alert.memaErr04" />");
					document.getElementById("newMema").focus();
					return false;
				}
			}else
				a = 0

 			if(old == next){
 				b = b+1;
 				if(b>2){
 					alert("<fmt:message key="message.alert.memaErr04" />");
 					document.getElementById("newMema").focus();
 					return false;
 				}
 			}else
 				b = 0;
			
 			if(old - next == 1){
 				c = c+1;
 				if(c>2){
 					alert("<fmt:message key="message.alert.memaErr04" />");
 					document.getElementById("newMema").focus();
 					return false;
 				}
 			}else
 				c = 0
 				
			old = next;
		}

		if (pwd2_val != pwd3_val) {
			alert("<fmt:message key="message.alert.memaErr05" />");
			document.getElementById("newMemaAgain").focus();
			return false;
		}else{
			if(confirm("<fmt:message key="message.cnfm.changeMema" />")){
				$("#form1").attr("action", root + "/0905/memaReSetUpdate.html");
				$("#form1").submit();
			}
		}
	}
			
</script>
</html>