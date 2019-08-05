<%
/*
 * @(#)log/F0203_B.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 操作記錄查詢 - 電商平台狀態管理異動前
 *
 * Modify History:
 * v1.00, 2018/04/10, Darren Tsai 
 *  1) First Release
 * 
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:forEach items="${command.before.ecDataList}" var="item">
	<c:choose>
		<c:when test="${item.ecId eq command.after.ecId}">

				<tr class="dataRowOdd">
					<th><fmt:message key="F0201.field.EC_ID" /></th><%--平台代號--%>
					<td>
						${item.ecId}
					</td>
				</tr>
				<tr class="dataRowEven">
					<th><fmt:message key="F0201.field.EC_NAME_CH" /></th><%--平台中文名稱--%>
					<td>
						${item.ecNameCh}
					</td>
				</tr>
				<tr class="dataRowOdd">
					<th><fmt:message key="F0201.field.EC_NAME_EN" /></th><%--平台英文名稱--%>
					<td>
						${item.ecNameEn}
					</td>
				</tr>
				<tr class="dataRowEven">
					<th><fmt:message key="F0201.field.FEE_STTS" /></th><%--狀態--%>
					<td>
						<fmt:message key="F0201.field.FEE_STTS.${item.stts}" />
					</td>
				</tr>
		</c:when>
	</c:choose>
</c:forEach>

