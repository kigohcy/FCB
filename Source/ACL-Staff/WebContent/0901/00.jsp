<%
	/*
	 * @(#)0901/00.jsp
	 *
	 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
	 *
	 * Description:
	 * 角色設定頁面
	 *
	 * Modify History:
	 * v1.00, 2016/02/15, Jimmy
	 * 	1)First Release
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
			$("#form1").attr("action", root + '/0901/roleAddInit.html');
			$("#form1").submit();
		});
		<%-- 修改 --%>
		$("#btnModify").click(function() {
			$("#form1").attr("action", root + "/0901/roleUpdateInit.html");
			$("#form1").submit();
		});
		<%-- 刪除 --%>
		$("#btnDel").click(function() {
			if(confirm("<fmt:message key="message.cnfm.deleteOrNot" />")){
				$("#form1").attr("action", root + "/0901/roleDelete.html");
				$("#form1").submit();
			}
		});
		<%-- 功能檢視 --%>
		$("#btnView").click(function() {
			$("#form1").attr("action", root + "/0901/functionView.html");
			$("#form1").submit();
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
					<fmt:message key="function.Id.F0901" /><%-- 角色設定  --%>
				</div>
				<form id="form1" name="form1" action="" method="post">
					<table class="fxdTable">
						<thead>
							<tr class="titleRow">
								<td>&nbsp;</td>
								<td width="150px;">
									<fmt:message key="F0901.field.Role_Code" /><%-- 角色代碼 --%>
								</td>
								<td width="180x;">
									<fmt:message key="F0901.field.Role_Name" /><%-- 角色名稱 --%>
								</td>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${command.roleList}" var="role" varStatus="i">
								<tr>
									<td align="center">
										<input type="radio" name="roleId" value="${role.roleId}" <c:if test="${i.index eq 0}">checked</c:if> />
									</td>
									<td align="center">
										<c:out value="${role.roleId}" />
									</td>
									<td align="center">
										<c:out value="${role.roleName}" />
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
				<input id="btnDel" class="btnStyle" type="button" value="<fmt:message key="common.btn.Delete"/><%-- 刪除 --%>" />
				&nbsp;
				<input id="btnView" class="btnStyle" type="button" value="<fmt:message key="common.btn.Function_View"/><%-- 功能檢視 --%>" />
				&nbsp;
			</div>
		</div>
		<div class="footer_line"></div>
	</div>
</body>
</html>