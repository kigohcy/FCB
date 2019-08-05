<%
/*
 * @(#)log/F0901_A.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 操作記錄查詢 - 角色設定異動後
 *
 * Modify History:
 * v1.00, 2016/02/02, Yann
 *  1) First Release
 * 
*/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<table class="fxdTable" width="100%">
		<tr class="secondaryTitleRow">
			<td colspan="4">
				<fmt:message key="F0901.title.Role_Code&Name" /> <%--角色代碼及名稱--%>
			</td>
		</tr>
		<tr class="dataRowOdd">
			<td width="100px;">
				<fmt:message key="F0901.field.Role_Code" /> <%--角色代碼--%>
			</td>
			<td width="150px;">
				<c:out value="${command.after.roleId}" />
				<input name="roleId" type="hidden" value="${command.after.roleId}" />
			</td>
			<td width="100x;">
				<fmt:message key="F0901.field.Role_Name" /> <%--角色名稱--%>
				*
			</td>
			<td width="150px;">
				<input name="roleName" type="text" size="40" maxlength="10" value="${command.after.roleName}" readonly />
			</td>
		</tr>
		<tr class="secondaryTitleRow">
			<td colspan="4">
				<fmt:message key="F0901.title.Permissions" /> <%--功能權限--%>
			</td>
		</tr>
	</table>
	<table class="staffFnctTable" width="100%">
		<c:forEach items="${command.after.staffSysMenu}" var="menu">
			<c:if test="${not empty menu.fncts }">
				<tr class="staffFnctTitleRow">
					<td colspan="6">
						<fmt:message key='${"menu.Id."}${menu.id.menuId}' />  <%--模組名稱--%>
					</td>
				</tr>
				
				<c:forEach items="${menu.fncts}" var="fnct" varStatus="i">
					<c:if test="${i.index%6 eq 0}">
						<tr class="staffFnctRowOdd">
					</c:if>
					<td>
						<input type="checkbox" name="fnctIds" value='<c:out value="${fnct.id.fnctId}"/>' <c:if test="${fnct.checkFlag}">CHECKED</c:if> disabled />
						<fmt:message key='${"function.Id."}${fnct.id.fnctId}' />  <%--功能名稱--%>
					</td>
					<c:if test="${(i.index+1)%6 eq 0}">
						</tr>
					</c:if>
				</c:forEach>
			</c:if>
		</c:forEach>
	</table>
