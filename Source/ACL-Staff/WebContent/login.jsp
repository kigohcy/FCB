<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>帳號連結扣款(Account Link)系統</title>

<%@include file="/include/initial.jsp"%>

<script type="text/javascript">
	$(function() {
		$("#form1").validate({
			rules : {
				userId : {
					required : true
				},
				userPswd : {
					required : true
				},
				kaptcha : {
					required : true
				}
			},
			messages : {
				userId : {
					required : '<fmt:message key="message.alert.userId" /><fmt:message key="message.alert.notNull" />'
				},
				userPswd : {
					required : '<fmt:message key="message.alert.userPw" /><fmt:message key="message.alert.notNull" />'
				},
				kaptcha : {
					required : '<fmt:message key="message.alert.kaptcha" /><fmt:message key="message.alert.notNull" />'
				}
			},
			showErrors : function(errorMap, errorList) {
				var err = [];
				$.each(errorList, function(i, v) {
					err.push(v.message);
				});
				if (err.length > 0) {
					alert(err.join("\n"));
				}
			},
			onkeyup : false,
			onfocusout : false,
			onsubmit : false
		});
		$("#btn1").click(function() {
			if($("#form1").valid()){
				$("#form1").prop("action", "<%=root%>/login.html");
				$("#form1").submit();
			}
		});
		
	});
	
	function rereKaptcha(){
		$("#kaptchaImage").hide().attr('src', "<%=root%>/Kaptcha.jpg").fadeIn();  
	}
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
			<div style="margin-top: 10px; padding-top: 100px; padding-bottom: 100px; width: 550px; border: 1px solid #990000;">
				<form name="form1" id="form1" method="post" action="/login.html">
					<div align="center">
					<table border="0">
						<tr>
							<td align="right">
								<fmt:message key="login.field.userId" />
							</td>
							<td align="left">
								<input type="text" name="userId" maxlength="10" />
							</td>
						</tr>
						<tr>
							<td align="right">
								<fmt:message key="login.field.userPw" />
							</td>
							<td align="left">
								<input type="password" name="userPswd" maxlength="20" autocomplete="off"/>
							</td>
						</tr>
						<tr>
							<td align="right">
								<fmt:message key="login.field.kaptcha" />
							</td>
							<td align="left">
								<input type="kaptcha" name="kaptcha" size="6" maxlength="4" autocomplete="off"/>&nbsp; <img src="Kaptcha.jpg" id="kaptchaImage" align="absbottom" height="25" onclick="rereKaptcha();">&nbsp; &nbsp; 
							</td>
						</tr>
					</table>
					</div>
				</form>
				<!-- Button area ------------------------------------------------------------------------>
					<input class="btnStyle marginTOP20" type="button" name="btn1" id="btn1" value="登入" />
					&nbsp;
			</div>
		</div>
	</div>

</body>
</html>