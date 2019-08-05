<%
/*
 * @(#)log/F0201D_B.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 操作記錄查詢 - 扣款平台管理異動前-刪除
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
				<td><fmt:message key="F0209.field.certName"/></td>	<%-- 憑證識別碼 --%>
				<td><fmt:message key="F0209.field.certSn"/></td>	<%-- 憑證序號 --%>
				<td><fmt:message key="F0209.field.sDate"/></td>		<%-- 生效日期 --%>
				<td><fmt:message key="F0209.field.eDate"/></td>		<%-- 到期日期 --%>
	        </tr>
			<c:forEach items="${command.before.ecCertList}" var="item" varStatus="theCount">
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
                    	<input type="radio"  value="${item.certCn}" <c:if test="${item.id.certCn == command.after.certCn}">checked</c:if> disabled />
                    </td>
                    <td>${item.id.certCn}</td>
	                <td>${item.certSn}</td>
	                <td>${item.strtDttm}</td>
	                <td>${item.endDttm}</td>
				</tr>
			</c:forEach>
        