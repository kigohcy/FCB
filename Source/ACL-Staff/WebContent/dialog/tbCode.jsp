<%
/*
 * @(#)dialog/tbCoe.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: tbCoe
 *
 * Modify History:
 * v1.00, 2016/03/07, Evan
 * 	1)First Release
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/include/initialDialog.jsp" %>

<table width="100%">
  <c:choose>
    <c:when test="${command.errorMsg eq ''}">
		<tr>
			<td class="titleRow" style="width: 80px;"><fmt:message key="message.dialog.codeId" /></td><%--錯誤代碼 --%>
			<td class="dataRowEven">${command.codeId}</td>
		</tr>
		<tr>
			<td class="titleRow"><fmt:message key="message.dialog.codeDesc" /></td><%--錯誤原因 --%>
			<td class="dataRowEven">${command.codeDesc}</td>
		</tr>
		<tr>
			<td class="titleRow"><fmt:message key="message.dialog.showDesc" /></td><%--錯誤說明 --%>
			<td class="dataRowEven">${command.showDesc}</td>
		</tr>
	</c:when>
	<c:otherwise>
		<tr>
			<td>${command.errorMsg}</td>
		</tr>
	</c:otherwise>
 </c:choose>
</table>
