
<%
	/*
	 * @(#)log/F0904F_A.jsp
	 *
	 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
	 *
	 * Description: 操作記錄查詢 - 使用者管理異動後_終止
	 *
	 * Modify History:
	 * v1.00, 2018/05/28, Darren Tsai
	 *  1) First Release
	 * 
	*/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<c:forEach items="${command.before.userList}" var="user" varStatus="i">
	<c:choose>
		<c:when test="${user.loginId eq command.after.loginId}">
			<tr class="dataRowOdd">
				<th>
					<fmt:message key="F0904.field.DEPT_ID" />
				</th>
				<%-- 部門別 --%>
				<td>
					<c:out value="${user.deptId}" />
				</td>
			</tr>
			<tr class="dataRowEven">
				<th>
					<fmt:message key="F0904.field.USER_ID" />
				</th>
				<%-- 員工編號 --%>
				<td>
					<c:out value="${user.userId}" />
				</td>
			</tr>
			<tr class="dataRowOdd">
				<th>
					<fmt:message key="F0904.field.USER_NAME" />
				</th>
				<%-- 姓名 --%>
				<td>
					<c:out value="${user.userName}" />
				</td>
			</tr>
			<tr class="dataRowEven">
				<th>
					<fmt:message key="F0904.field.LOGIN_ID" />
				</th>
				<%-- 登入代號 --%>
				<td>
					<c:out value="${user.loginId}" />
				</td>
			</tr>
			<tr class="dataRowOdd">
				<th>
					<fmt:message key="F0904.field.ROLE_ID" />
				</th>
				<%-- 角色權限別 --%>
				<td>
					<c:out value="${user.roleId}" />
				</td>
			</tr>
			<tr class="dataRowEven">
				<th>
					<fmt:message key="F0904.field.STATES" />
				</th>
				<%-- 狀態 --%>
				<td>
					<fmt:message key="F0904.user.states.${command.after.states}" />
				</td>
			</tr>
			<tr class="dataRowOdd">
				<th>
					<fmt:message key="F0904.field.CREATE_DATE" />
				</th>
				<%-- 建檔日期 --%>
				<td>
					<fmt:parseDate var="date" value="${user.createDate}" pattern="yyyyMMddHHmmss" />
					<fmt:formatDate type="both" pattern="yyyy/MM/dd HH:mm:ss" value="${date}" />
				</td>
			</tr>
			<tr class="dataRowEven">
				<th>
					<fmt:message key="F0904.field.EMAIL" />
				</th>
				<%-- EMAIL --%>
				<td>
					<c:out value="${user.email}" />
				</td>
			</tr>
		</c:when>
	</c:choose>
</c:forEach>

