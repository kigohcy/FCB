<%
/*
 * @(#)log/F0201D_B.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 操作記錄查詢 - 扣款平台管理異動前-刪除
 *
 * Modify History:
 * v1.00, 2016/02/04, Evan
 *  1) First Release
 * 
*/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

		
	        <tr class="titleRow">
	            <td><fmt:message key="F0201.field.OPTION" /></td><%-- 選項 --%>
	            <td><fmt:message key="F0201.field.EC_ID" /></td><%-- 平台代碼 --%>
	            <td><fmt:message key="F0201.field.EC_NAME_CH" /></td><%-- 平台中文名稱 --%>
	            <td><fmt:message key="F0201.field.EC_NAME_EN" /></td><%-- 平台英文名稱 --%>
	            <td><fmt:message key="F0201.field.FEE_TYPE" /></td><%-- 收費方式 --%>
	            <td><fmt:message key="F0201.field.FEE_RATE" /></td><%-- 費率 --%>
	            <td><fmt:message key="F0201.field.FEE_STTS" /></td><%-- 狀態 --%>
	            <td><fmt:message key="F0201.field.REAL_ACNT" /></td><%-- 實體帳號 --%>
	            <td><fmt:message key="F0201.field.ENTR_NO" /></td><%-- 企業編號--%>
	            <td><fmt:message key="F0201.field.ENTR_ID" /></td><%-- 公司統編--%>
	            <td><fmt:message key="F0201.field.CNTC" /></td><%-- 聯絡人--%>
	            <td><fmt:message key="F0201.field.TEL" /></td><%-- 聯絡電話--%>
	            <td><fmt:message key="F0201.field.MAIL" /></td><%-- 電子郵件--%>
	            <td><fmt:message key="F0201.field.NOTE" /></td><%-- 備註說明項 --%>
	        </tr>
			<c:forEach items="${command.before.ecDataList}" var="item" varStatus="theCount">
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
                    	<input type="radio"  value="${item.ecId}" <c:if test="${item.ecId == command.after.ecId}">checked</c:if> disabled />
                    </td>
	                <td>${item.ecId}</td>
	                <td>${item.ecNameCh}</td>
	                <td>${item.ecNameEn}</td>
	                <td align="center"><fmt:message key="F0201.field.FEE_TYPE.${item.feeType}" /></td>
	                <td align="center">
	                	<c:if test="${item.feeType eq 'A'}">
                    		<fmt:parseNumber var="value" integerOnly="true"  type="number" value="${item.feeRate}" />
                    		${value}
                    	</c:if>
                    	<c:if test="${item.feeType eq 'B'}">
                    		 ${item.feeRate}%
                   		</c:if>
	                </td>
	                <td align="center"><fmt:message key="F0201.field.FEE_STTS.${item.stts}" /></td>
	                <td>${item.realAcnt}</td>
	                <td>${item.entrNo}</td> 
	                <td>${item.entrId}</td>
	                <td>${item.cntc}</td>
	                <td>${item.tel}</td>
	                <td>${item.mail}</td>
	                <td>${item.note}</td>
				</tr>
			</c:forEach>
        