<%
/*
 * @(#)log/F0904_B.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 操作記錄查詢 - 使用者管理異動前
 *
 * Modify History:
 * v1.00, 2018/05/28, Darren Tsai
 *  1) First Release
 * 
*/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
			<tr class="dataRowOdd">
				<th><fmt:message key="F0904.field.DEPT_ID" /></th><%-- 部門別 --%>
				<c:forEach items="${command.before.deptList}" var="item">
					<c:choose>
						<c:when test="${item.deptId eq command.before.deptId}">
						<td>
							<c:out value="${item.deptName}" />
						</td>
						</c:when>
					</c:choose>
				</c:forEach>
			</tr>
			<tr class="dataRowEven">
				<th><fmt:message key="F0904.field.USER_ID" /></th><%-- 員工編號 --%>
				<td>
					<c:out value="${command.before.userId}" />
				</td>
			</tr>
			<tr class="dataRowOdd">
				<th><fmt:message key="F0904.field.USER_NAME" /></th><%-- 姓名 --%>
				<td>
					<c:out value="${command.before.userName}" />
				</td>
			</tr>
			<tr class="dataRowEven">
				<th><fmt:message key="F0904.field.LOGIN_ID" /></th><%-- 登入代號 --%>
				<td>
					<c:out value="${command.before.loginId}" />
				</td>
			</tr>
			<tr class="dataRowOdd">
				<th><fmt:message key="F0904.field.ROLE_ID" /></th><%-- 角色權限別 --%>
					<c:forEach items="${command.before.roleList}" var="roles">
						<c:choose>
							<c:when test="${roles.roleId eq command.before.roleId}">
								<td>
									<c:out value="${roles.roleName}" />
								</td>
							</c:when>
						</c:choose>
					</c:forEach>
			</tr>
			<tr class="dataRowEven">
				<th><fmt:message key="F0904.field.STATES" /></th><%-- 狀態 --%>
				<td>
					<fmt:message key="F0904.user.states.${command.before.states}" />
				</td>
			</tr>
			<tr class="dataRowOdd">
				<th><fmt:message key="F0904.field.CREATE_DATE" /></th><%-- 建檔日期 --%>
				<td>
					<fmt:parseDate var="date" value="${command.before.createDate}"  pattern="yyyyMMddHHmmss" />
					<fmt:formatDate type="both" pattern="yyyy/MM/dd HH:mm:ss" value="${date}" />
				</td>
			</tr>
			<tr class="dataRowEven">
				<th><fmt:message key="F0904.field.EMAIL" /></th><%-- EMAIL --%>
				<td>
					<c:out value="${command.before.email}" />
				</td>
			</tr>
