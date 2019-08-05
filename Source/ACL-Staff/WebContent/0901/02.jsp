
<%
	/*
	 * @(#)0901/02.jsp
	 *
	 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
	 *
	 * Description:
	 * 角色設定-修改角色頁面
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
<%-- 確認 --%>
	$("#btnOk").click(function() {
			if ($("#form1").valid()) {
				if(confirm("<fmt:message key="message.cnfm.updateOrNot" />")){
					$("#form1").attr("action", root + "/0901/roleUpdate.html");
					$("#form1").submit();
				}
			}
		});
<%-- 回上一頁 --%>
	$("#btnBack").click(function() {
			$("#form1").attr("action", root + "/0901/roleQuery.html");
			$("#form1").submit();
		});
<%-- 表單驗證邏輯 --%>
	$("#form1").validate({
			rules : {
				roleName : {
					required : true,
					LENGTH_CHECKER: {
                        min: -1,
                        max: 40
                    }
				},
				fnctIds : {
					required : true
				}
			},
			messages : {
				roleName : {
					required : "<fmt:message key="message.alert.roleName" /><fmt:message key="message.alert.notNull" />",
					LENGTH_CHECKER:"<fmt:message key="message.alert.roleName" /><fmt:message key="message.alert.overLength" />"
				},
				fnctIds : {
					required : "<fmt:message key="message.alert.mustToSelectFunction"/>"
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
								<input name="roleId" type="hidden" maxlength="10" value="<c:out value="${command.roleId}" />" />
							</td>
							<td width="100x;">
								<%-- 角色名稱  --%>
								<fmt:message key="F0901.field.Role_Name" />
								*
							</td>
							<td width="150px;">
								<input name="roleName" type="text" maxlength="40" value='<c:out value="${command.roleName}" />' />
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
						<c:forEach items="${command.staffSysMenu}" var="menu">
							<c:if test="${not empty menu.fncts }">
								<tr class="staffFnctTitleRow">
									<td colspan="7">
										<%-- 模組名稱 --%>
										<fmt:message key='${"menu.Id."}${menu.id.menuId}' />
									</td>
								</tr>
								<c:forEach items="${menu.fncts}" var="fnct" varStatus="i">
									<c:if test="${i.index%6 eq 0}">
										<tr class="staffFnctRowOdd">
									</c:if>
									<td>
										<input type="checkbox" name="fnctIds" value='<c:out value="${fnct.id.fnctId}"/>' <c:if test="${fnct.checkFlag}">CHECKED</c:if> />
										<%-- 功能名稱 --%>
										<fmt:message key='${"function.Id."}${fnct.id.fnctId}' />
									</td>
									<c:if test="${(i.index+1)%6 eq 0}">
										</tr>
									</c:if>
								</c:forEach>
							</c:if>
						</c:forEach>
					</table>
				</form>
			</div>
			<%-- Button area --%>
			<div class="btnContent">
				<%-- 確認 --%>
				<input id="btnOk" class="btnStyle" type="button" value="<fmt:message key="common.btn.OK"/>" />
				&nbsp;
				<%-- 回上一頁 --%>
				<input id="btnBack" class="btnStyle" type="button" value="<fmt:message key="common.btn.Back"/>" />
				&nbsp;
			</div>
		</div>
		<div class="footer_line"></div>
	</div>
</body>
</html>