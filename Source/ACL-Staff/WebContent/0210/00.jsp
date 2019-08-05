<%
/*
 * @(#)0210/00.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 未綁定解鎖
 *
 * Modify History:
 * v1.00, 2016/12/21, Yann
 * 	1) TSBACL-143, First Release
 * 
*/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>帳號連結扣款(Account Link)系統</title>

	<%-- include Header, footer and menu --%>
	<%@include file="/include/container.jsp" %>
	
	<script type="text/javascript">
		
		$(function(){
			
			//查詢條件
			$("#accordion").accordion({
				heightStyle : "content",
				collapsible : true
			}).children(".ui-accordion .ui-accordion-content").css("padding", "0px");
			
			//頁面檢核
			$("#form1").validate({
		         rules: {
		        	 custId:{
		        		 required: true,
		        		 ALPHA_CHECKER: true
		         	}
		         },
		         messages:{
		        	 custId:{
		        		 //請輸入身分證字號
		        		 required:"<fmt:message key="message.alert.pleaseKeyIn" />"+"<fmt:message key="F0202.field.custId" />",
		        		 ALPHA_CHECKER:"<fmt:message key="F0202.field.custId" /><fmt:message key="message.alert.onlyEN&NUM" />"
		         	}
		         },
		         showErrors: function(errorMap, errorList) {
		             var err = [];
		             $.each(errorList, function(i, v) {
		                 err.push(v.message);
		             });
		             if (err.length > 0) {
		                 alert(err.join("\n"));
		             }
		         },
		         onkeyup: false,
		         onfocusout: false,
		         onsubmit: false
		     });	
			
			//轉大寫
			$("#custId").keyup(function(){
			    this.value = this.value.toUpperCase();
			});
		});
		
		//查詢
		function query(){
			
			if($("#form1").valid()){
				$("#form1").attr("action", root + "/0210/query.html");
				$("#form1").submit();
			 }
		}
		
		//解鎖
		function unlock(){
			$("#form1").attr("action", root + "/0210/unlock.html");
			$("#form1").submit();
		}
	</script>
</head>
	<body>
	  <!-- Container ============================================================================================== -->
	<div class="container">
		<!-- Menu --------------------------------------------------------------------------->
		<!-- Content ------------------------------------------------------------------------>
		<div class="mainContent">
			<div class="content">
				<div class="fnctTitle"><fmt:message key="function.Id.F0210" /> <c:if test="${not command.initQuery}">> <fmt:message key="common.btn.Query" /></c:if></div> <%--未綁定解鎖 >查詢--%>
				<form method="post" name="form1" id="form1" action="" style="margin: 0;">
				
				<div id="accordion">
						<h3><fmt:message key="common.queryCondition"/><%--查詢條件 --%></h3>
					<div style="width: 978px;">
							<table class="fxdTable" width="100%">
								<tr class="dataRowOdd">
									<td width="90px"><fmt:message key="F0202.field.custId" />*</td><%--身分證字號* --%>
									<td>
										<input type="text" size="11" maxlength="11" name="custId" id="custId" />
									</td>
								</tr>
							</table>
						<div align="left" style="margin: 10px">
							<input class="btnStyle" type="button" name="btn1" value="<fmt:message key="common.btn.Query" />" <%-- 查詢--%> onclick="query();" />
						</div>
					</div>
				</div>
				<c:if test="${not command.initQuery}">
					<table class="fxdTable" style="width: 540px;">
						<tr class="titleRow">
							<td nowrap width="120px"><fmt:message key="F0202.field.custId" /></td><%--身分證字號 --%>
							<td nowrap width="300px"><fmt:message key="F0210.field.custStts" /></td><%--非會員代碼/密碼狀態 --%>
							<td nowrap width="120px"><fmt:message key="F0202.field.exeFnct" /></td><%--執行功能 --%>
						</tr>
						<tr class="dataRowOdd" align="center">
							<td><c:out value="${command.qCustId}"/></td>
							<td><c:out value="${command.custStts}"/></td>
							<td>
								<%-- 解鎖 --%>
								<input class="btnStyle" type="button" name="btn5" value="<fmt:message key="common.btn.UnlockPsd"/>" onclick="unlock();" />
							</td>
						</tr>
					</table>
				</c:if>
			</form>
			</div>
		</div>
		<div class="footer_line"></div>
	</div>
	</body>
</html>
