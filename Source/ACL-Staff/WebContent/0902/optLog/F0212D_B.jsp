<%
/*
 * @(#)log/F0201D_B.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 操作記錄查詢 - 訊息代碼管理異動前-刪除
 *
 * Modify History:
 * v1.00, 2018/03/29
 *  1) First Release
 * 
*/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

		
	        <tr class="titleRow">
	        	<td><fmt:message key="F0209.field.items"/></td>		<%-- 選項 --%>
	        	<td><fmt:message key="F0212.field.codeType"/></td>	<%-- 類別 --%>
				<td><fmt:message key="F0212.field.codeId"/></td>	<%-- 訊息代碼 --%>
				<td><fmt:message key="F0212.field.codeDesc"/></td>	<%-- 說明 --%>${command.after.codeId}
	        </tr>
			<c:forEach items="${command.before.tbCodeList}" var="item" varStatus="theCount">
				<c:choose>
					<c:when test="${theCount.count % 2 == 1}">
						<c:set value="dataRowOdd" var="cssClass"></c:set>
					</c:when>
					<c:otherwise>
						<c:set value="dataRowEven" var="cssClass"></c:set>
					</c:otherwise>
				</c:choose>	
				<tr class="${cssClass}">
					<td align="center">
                    	<input type="radio"  value="${item.id.codeId}" <c:if test="${fn:substring(item.id.codeId, 3, fn:length(item.id.codeId)) == command.after.codeId && item.codeType ==  command.after.codeType}">checked</c:if> disabled />
                    </td>
					<td align="center">
						<fmt:message key="F0212.field.codeType.${item.codeType }" />
					</td>
					<td nowrap>
						${fn:substring(item.id.codeId, 3, fn:length(item.id.codeId))}
					</td>
					<td>
						<c:out value="${item.codeDesc }" />
					</td>
				</tr>
			</c:forEach>
        