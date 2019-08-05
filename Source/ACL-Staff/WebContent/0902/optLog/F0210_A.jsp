<%
/*
 * @(#)log/F0210_A.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 操作記錄查詢 - 未綁定解鎖 - 異動後
 *
 * Modify History:
 * v1.00, 2016/12/21, Yann
 *  1) TSBACL-143, First Release
 * 
*/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

		<table class="fxdTable" style="width: 540px;">
			<tr class="titleRow">
				<td nowrap width="120px"><fmt:message key="F0202.field.custId" /></td><%--身分證字號 --%>
				<td nowrap width="300px"><fmt:message key="F0210.field.custStts" /></td><%--非會員代碼/密碼狀態 --%>
				<td nowrap width="120px"><fmt:message key="F0202.field.exeFnct" /></td><%--執行功能 --%>
			</tr>
			<tr class="dataRowOdd" align="center">
				<c:choose>
					<c:when test="${not empty command.after.qCustId}">
						<td>${command.after.qCustId}</td>
						<td><c:out value="${command.after.custStts}"/></td>
						<td>
							<input class="btnStyle" type="button"  value="<fmt:message key="common.btn.UnlockPsd"/>"  <%-- 密碼解鎖--%> disabled />
						</td>
					</c:when>
					<c:otherwise>
						<%--查無符合條件資料--%>
						<td class="noResult" colspan="7" align="center" ><fmt:message key="message.sys.NoData" /></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		