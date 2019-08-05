
<%
	/*
	 * @(#)log/F0401D_B.jsp
	 *
	 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
	 *
	 * Description: 操作記錄查詢 - 最新消息公告設定 異動前
	 *
	 * Modify History:
	 * v1.00, 2016/07/15, Jimmy Yen
	 *  1) First Release
	 * 
	*/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<table class="fxdTable" style="width: 960px;">
	<tr class="titleRow" align="center">
		<td width="30px;">
			<%-- 選項 --%>
			<fmt:message key="F0401.table.item" />
		</td>
		<td width="80px;">
			<%-- 公告起日 --%>
			<fmt:message key="F0401.table.bgnDate" />
		</td>
		<td width="80px;">
			<%-- 公告迄日 --%>
			<fmt:message key="F0401.table.endDate" />
		</td>
		<td width="80px;">
			<%-- 公告類型 --%>
			<fmt:message key="F0401.field.type" />
		</td>
		<td width="40px;">
			<%-- 置頂 --%>
			<fmt:message key="F0401.field.serl" />
		</td>
		<td>
			<%-- 公告標題 --%>
			<fmt:message key="F0401.field.title" />
		</td>
		<td width="80px;">
			<%-- 最後修改時間 --%>
			<fmt:message key="F0401.table.lastModify" />
		</td>
		<td width="100px;">
			<%-- 公告內容 --%>
			<fmt:message key="F0401.table.content" />
		</td>
	</tr>

	<c:forEach items="${command.before.newsMsg}" var="newsData" varStatus="i">
		<c:choose>
			<c:when test="${i.count%2 ne 0}">
				<tr class="dataRowOdd" align="center">
			</c:when>
			<c:otherwise>
				<tr class="dataRowEven" align="center">
			</c:otherwise>
		</c:choose>
		<td>
			<c:set var="checkFlag" value="" />
			<c:forEach items="${command.after.qSeq}" var="seq" varStatus="i">
				<c:if test="${seq eq newsData.seq}">
					<c:set var="checkFlag" value="checked" />
				</c:if>
			</c:forEach>
			<input type="checkbox" name="qSeq" value="${newsData.seq}" ${checkFlag} disabled="disabled">
		</td>
		<td>
			<fmt:formatDate pattern="yyyy/MM/dd" value="${newsData.bgnDate}" />
		</td>
		<td>
			<fmt:formatDate pattern="yyyy/MM/dd" value="${newsData.endDate}" />
		</td>
		<td>
			<c:choose>
				<c:when test="${newsData.type eq 'A' }">
					<%-- 最新消息 --%>
					<option value="A"><fmt:message key="F0401.field.type.A" /></option>
				</c:when>
				<c:when test="${newsData.type eq 'B' }">
					<%-- 優惠活動 --%>
					<option value="A"><fmt:message key="F0401.field.type.B" /></option>
				</c:when>
				<c:when test="${newsData.type eq 'C' }">
					<%-- 重要公告 --%>
					<option value="A"><fmt:message key="F0401.field.type.C" /></option>
				</c:when>
				<c:when test="${newsData.type eq 'D' }">
					<%-- 其他 --%>
					<option value="A"><fmt:message key="F0401.field.type.D" /></option>
				</c:when>
			</c:choose>
		</td>
		<td>
			<c:choose>
				<c:when test="${newsData.serl == '1'}">
					<%-- 是 --%>
					<fmt:message key="F0401.field.serl.Y" />
				</c:when>
				<c:when test="${newsData.serl == '99'}">
					<%-- 否 --%>
					<fmt:message key="F0401.field.serl.N" />
				</c:when>
			</c:choose>
		</td>
		<td align="left">${newsData.title}</td>
		<td>
			<fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${newsData.mdfyDttm}" />
		</td>
		<td>
			<a href="#" style="color: blue;" name="preview" seq="${newsData.seq}">
				<%-- 預覽 --%>
				<fmt:message key="F0401.table.preview" />
			</a>
		</td>
		</tr>
	</c:forEach>
</table>
