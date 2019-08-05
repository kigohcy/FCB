
<%
	/*
	 * @(#)0901/03.jsp
	 *
	 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
	 *
	 * Description:
	 * 角色設定-功能檢視頁面
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
<%-- 回上一頁 --%>
	$("#btnBack").click(function() {
			$("#form1").attr("action", root + "/0901/roleQuery.html");
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
				<form id="form1" name="form1" action="" method="post">
					<table class="fxdTable" width="100%">
						<tr class="secondaryTitleRow">
							<td colspan="4">
								<%-- 角色代碼及名稱  --%>
								<fmt:message key="F0901.title.Role_Code&Name" />
							</td>
						</tr>
						<tr class="dataRowOdd">
							<td width="100px;">
								<%-- 角色代碼  --%>
								<fmt:message key="F0901.field.Role_Code" />
							</td>
							<td width="150px;">
								<c:out value="${command.roleId}" />
							</td>
							<td width="100x;">
								<%-- 角色名稱  --%>
								<fmt:message key="F0901.field.Role_Name" />
							</td>
							<td width="150px;">
								<c:out value="${command.roleName}" />
							</td>
						</tr>
						<tr class="secondaryTitleRow">
							<td colspan="4">
								<%-- 功能權限 --%>
								<fmt:message key="F0901.title.Permissions" />
							</td>
						</tr>
					</table>
					<table class="staffFnctTable" width="100%">
						<c:choose>
							<c:when test="${command.staffSysMenu eq null}">
								<tr class="staffFnctRowOdd">
									<td>
										<!-- 查無對應權限 -->
										<fmt:message key="message.F0901.noRight" />
									</td>
								</tr>
							</c:when>
							<c:otherwise>
								<c:forEach items="${command.staffSysMenu}" var="menu">
									<c:if test="${not empty menu.fncts}">
										<tr class="staffFnctTitleRow">
											<td colspan="6">
												<%-- 模組名稱 --%>
												<fmt:message key='${"menu.Id."}${menu.id.menuId}' />
											</td>
										</tr>
										<c:forEach items="${menu.fncts}" var="fnct" varStatus="i">
											<c:if test="${i.index%6 eq 0}">
												<tr class="staffFnctRowOdd">
											</c:if>
											<td>
												<%-- 功能名稱 --%>
												<fmt:message key='${"function.Id."}${fnct.id.fnctId}' />
											</td>
											<c:if test="${(i.index+1)%6 eq 0}">
												</tr>
											</c:if>
										</c:forEach>
										<c:set value="${fn:length(menu.fncts)}" var="length"></c:set>
										<c:if test="${length lt 6}">
											<c:forEach begin="1" end="${6 - length}" step="1">
												<td style="width: 17%">&nbsp;</td>
											</c:forEach>
										</c:if>
									</c:if>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</table>
				</form>
			</div>
			<%-- Button area --%>
			<div class="btnContent">
				<%-- 回上一頁 --%>
				<input id="btnBack" class="btnStyle" type="button" value="<fmt:message key="common.btn.Back"/>" />
				&nbsp;
			</div>
		</div>
		<div class="footer_line"></div>
	</div>
</body>
</html>