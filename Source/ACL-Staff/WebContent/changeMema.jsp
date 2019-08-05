
<%
/*
 * @(#)changeMema.jsp
 *
 * Copyright (c) 2018 HiTRUST Incorporated. All rights reserved.
 *
 * Description:
 * 新戶或密碼到期變更密碼
 *
 * Modify History:
 * v1.00, 2018/04/16
 * 	1)First Release
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>帳號連結扣款(Account Link)系統</title>

<%@include file="/include/initial.jsp"%>

<script type="text/javascript">
	var root = '<%=root%>';
    var userId = '${loginUser.userId }';
    var userName = '${loginUser.userName }';
    var json = '${loginUser.jsonMenu }';
    var sessionId = '${loginUser.sessionId}';

    <%-- session timeout --%>
    var iOriginalTimeout = <%=sessionTime%>;
    var iSessionTimeout = iOriginalTimeout;
    var iAlertTimeout = 60;
    var dTimeout = new Date();

	$(function(){
	});
</script>
</head>
<body background="">
	<!-- Container ============================================================================================== -->
	<div class="container">
		<!-- Content ------------------------------------------------------------------------>
		<div class="header" style="width: 550px; margin: 0 auto; text-align: left;">
			<img src='<%=root%>/images/bankHeader.gif' alt='Bank' /> <img src="<%=root%>/images/sys_name.gif" alt="帳務連結扣款系統" /> <img src="<%=root%>/images/staff_title.gif" alt="客服系統" />
		</div>
		<div class="mainContent" style="text-align: center; width: 550px;">
			<%-- 顯示錯誤訊息 --%>
			<c:if test="${not empty exception }">
				<div class="error">
					<c:choose>
						<c:when test="${not empty exception.errorCode }">
							<fmt:message key="${exception.errorCode }" />
						</c:when>
						<c:otherwise>
							<c:out value="${exception.errorMsg }"></c:out>
						</c:otherwise>
					</c:choose>
				</div>
			</c:if>
			<div class="fnctTitle">
				<c:if test="${loginUser.userStts != 'A'}">
					<fmt:message key="common.title.changeMema1" />
				</c:if>
				<c:if test="${loginUser.userStts == 'A'}">
					<fmt:message key="common.title.changeMema2" />
				</c:if>
			</div>
			<div style="margin-top: 10px; padding-top: 100px; padding-bottom: 100px; width: 550px; border: 1px solid #990000;">
				<form name="form1" id="form1" method="post" action="/login.html">
					<input type="hidden" id="userId" name="userId" value="${loginUser.userId }">
					<input type="hidden" id="sessionKey" name="sessionKey" value="${loginUser.sessionId}">
					<fmt:message key="F0905.field.OLD_MEMA" />*&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="password" size="20" maxlength="20" name="oldMema" id="oldMema" autocomplete="off"/>
					<br /><br />
					<fmt:message key="F0905.field.NEW_MEMA" />*&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="password" size="20" maxlength="20" name="newMema" id="newMema" autocomplete="off"/>
					<br /><br />
					<fmt:message key="F0905.field.NEW_MEMA_AGAIN" />*
					<input type="password" size="20" maxlength="20" name="newMemaAgain" id="newMemaAgain" autocomplete="off"/><br /><br />
					<div aling="center">
					<textarea align="center" rows="6" cols="54" id="area" readonly="readonly" style="color: blue;">
				
					</textarea>
					</div>
					<br /><br />
				
				<!-- Button area ------------------------------------------------------------------------>
					<%-- 確認 --%>
					<input type="button" id="btnOk" class="btnStyle"  onclick="return CheckPWD();" value="<fmt:message key="common.btn.OK"/>" />
					&nbsp;&nbsp;
					<c:if test="${loginUser.userStts != 'A'}">
						<input type="button" id="btnOk" class="btnStyle"  onclick="return skipChange();" value="<fmt:message key="common.btn.KeeeMema"/>" />
					</c:if>
			</div>
		</div>
	</div>

</body>
<script type="text/javascript">
	var textarea=document.getElementById("area");
	textarea.value=  "<fmt:message key="changeMema.field.textarea01" />"+"\n"+
						"<fmt:message key="changeMema.field.textarea02" />"+"\n"+
						"<fmt:message key="changeMema.field.textarea03" />"+"\n";
	
	function skipChange(){
		if(confirm("<fmt:message key="message.cnfm.keepMema" />")){
			$("#form1").attr("action", root + "/0905/keepMema.html");
			$("#form1").submit();
		}
	}
	
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
				$("#form1").attr("action", root + "/0905/updateMema.html");
				$("#form1").submit();
			}
		}
	}
			
</script>
</html>