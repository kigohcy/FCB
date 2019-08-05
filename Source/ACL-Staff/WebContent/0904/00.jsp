<%
	/*
	 * @(#)0904/00.jsp
	 *
	 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
	 *
	 * Description: 使用者管理頁面
	 *
	 * Modify History:
	 * v1.00, 2016/02/15, Jimmy
	 * 	1)First Release
	 * v1.01, 2018/05/29, Darren Tsai
	 * 	2)Second Release
	 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>帳號連結扣款(Account Link)系統</title>

<%-- include Header, footer and menu --%>
<%@include file="/include/container.jsp"%>

<style>
.mainContent {
	text-align: center;
	height: 350px;
}
</style>

<script>

	$(function() {
		$(".fxdTable tbody tr:odd").addClass("dataRowEven");
		$(".fxdTable tbody tr:even").addClass("dataRowOdd");
		
		<%-- 新增 --%>
		$("#btnNew").click(function() {
			$("#form1").attr("action", root + '/0904/userAddInit.html');
			$("#form1").submit();
		});
		
		<%-- 修改 --%>
		$("#btnModify").click(function() {
			var loginId = $('input[name=loginId]:checked', '#form1').val();
			var states = $('#'+loginId+'states').val();

			if(states=='D'){
				alert("<fmt:message key="message.cnfm.0017" />");
				return;
			}
			$("#form1").attr("action", root + "/0904/userUpdateInit.html");
			$("#form1").submit();
		});
		
		<%-- 密碼重設 --%>
		$("#btnView").click(function() {
			var loginId = $('input[name=loginId]:checked', '#form1').val();
			var states = $('#'+loginId+'states').val();

			if(states=='D'){
				alert("<fmt:message key="message.cnfm.0018" />");
				return;
			}
			$("#form1").attr("action", root + "/0904/resetMema.html");
			$("#form1").submit();
		});
		
		<%-- 終止 --%>
		$("#btnDel").click(function() {
			var loginId = $('input[name=loginId]:checked', '#form1').val();
			var states = $('#'+loginId+'states').val();

			if(states=='D'){
				alert("<fmt:message key="message.cnfm.0019" />");
				return;
			}
			if(confirm("<fmt:message key="message.cnfm.disable" />")){
				$("#form1").attr("action", root + "/0904/userDisable.html");
				$("#form1").submit();
			}
		});
	});
	
</script>
</head>
<body>
	<%-- Container --%>
	<div class="container">
		<%-- Content --%>
		<div class="mainContent">
			<div class="content">
				<div class="fnctTitle">
					<fmt:message key="function.Id.F0904" /><%-- 角色設定  --%>
				</div>
				<form id="form1" name="form1" action="" method="post">
				<input type="hidden" id="states" name="states" value="" />
					<table class="fxdTable">
						<thead>
							<tr class="titleRow">
								<td>&nbsp;</td>
								<td width="150px;">
									<fmt:message key="F0904.field.DEPT_ID" /><%-- 部門別 --%>
								</td>
								<td width="180x;">
									<fmt:message key="F0904.field.USER_ID" /><%-- 員工編號 --%>
								</td>
								<td width="150px;">
									<fmt:message key="F0904.field.USER_NAME" /><%-- 姓名 --%>
								</td>
								<td width="180x;">
									<fmt:message key="F0904.field.LOGIN_ID" /><%-- 登入代號 --%>
								</td>
								<td width="150px;">
									<fmt:message key="F0904.field.ROLE_ID" /><%-- 角色權限別 --%>
								</td>
								<td width="180x;">
									<fmt:message key="F0904.field.STATES" /><%-- 狀態 --%>
								</td>
								<td width="180x;">
									<fmt:message key="F0904.field.CREATE_DATE" /><%-- 建檔日期 --%>
								</td>
								<td width="180x;">
									<fmt:message key="F0904.field.EMAIL" /><%-- EMAIL --%>
								</td>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${command.userList}" var="user" varStatus="i">
								<tr>
									<td align="center">
										<input type="radio" name=loginId value="${user.loginId}" <c:if test="${i.index eq 0}">checked</c:if> />
										<input type="hidden" id="${user.loginId}states" value="${user.states}" />
									</td>
									<td align="center">
										<c:out value="${user.deptId}" />
									</td>
									<td align="center">
										<c:out value="${user.userId}" />
									</td>
									<td align="center">
										<c:out value="${user.userName}" />
									</td>
									<td align="center">
										<c:out value="${user.loginId}" />
									</td>
									<td align="center">
										<c:out value="${user.roleId}" />
									</td>
									<td align="center">
										<fmt:message key="F0904.user.states.${user.states}" />
									</td>
									<td align="center">
										<fmt:parseDate var="date" value="${user.createDate}"  pattern="yyyyMMddHHmmss" />
										<fmt:formatDate type="both" pattern="yyyy/MM/dd HH:mm:ss" value="${date}" />
									</td>
									<td align="left">
										<c:out value="${user.email}" />
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</form>
			</div>
			<%-- Button Area --%>
			<div class="btnContent">
				<input id="btnNew" class="btnStyle" type="button" value="<fmt:message key="common.btn.New"/><%-- 新增 --%>" />
				&nbsp;
				<input id="btnModify" class="btnStyle" type="button" value="<fmt:message key="common.btn.Modify"/><%-- 修改 --%>" />
				&nbsp;
				<input id="btnView" class="btnStyle" type="button" value="<fmt:message key="common.btn.resetMema"/><%-- 密碼重設 --%>" />
				&nbsp;
				<input id="btnDel" class="btnStyle" type="button" value="<fmt:message key="common.btn.stop"/><%-- 終止 --%>" />
				&nbsp;
			</div>
		</div>
		<div class="footer_line"></div>
	</div>
</body>
</html>