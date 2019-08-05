<%
/*
 * @(#)log/F0901D_B.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 操作記錄查詢 - 角色設定刪除 異動前
 *
 * Modify History:
 * v1.00, 2016/02/04, Yann
 *  1) First Release
 * 
*/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

	<table class="fxdTable">
		<thead>
			<tr class="titleRow">
				<td>&nbsp;</td>
				<td width="150px;">
					<fmt:message key="F0901.field.Role_Code" /> <%--角色代碼--%>
				</td>
				<td width="180x;">
					<fmt:message key="F0901.field.Role_Name" /> <%--角色名稱--%>
				</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${command.before.roleList}" var="role" varStatus="i">
				<tr>
					<td align="center">
						<input type="radio" name="roleId" value="${role.roleId}" <c:if test="${command.after.roleId eq role.roleId}">checked</c:if> disabled />
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
