<%
/*
 * @(#)log/F0212.jsp
 *
 * Copyright (c) 2018 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 操作記錄查詢 - 訊息代碼管理
 *
 * Modify History:
 * v1.00, 2018/03/29
 * 
*/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

	<tr class="dataRowOdd">
		<%-- 類別 --%>
		<th>
			<fmt:message key="F0212.field.codeType"/>
		</th>
		<td>
			<fmt:message key="F0212.field.codeType.${command.after.codeType}" />
		</td>
	</tr>
	<tr class="dataRowEven">
		<%-- 代碼 --%>
		<th>
			<fmt:message key="F0212.field.codeId"/>
		</th>
		<td>
			${command.after.codeId }
		</td>
	</tr>
	<%-- 說明 --%>
	<tr class="dataRowOdd">
		<th>
			<fmt:message key="F0212.field.codeDesc"/>*
		</th>
		<td>
			<input type="text" size="100" maxlength="256" name="codeDesc" id="codeDesc" value="${command.after.codeDesc}"/>
		</td>
	</tr>