<%
/*
 * @(#)0501/01.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 約定帳號統計-明細資料
 *
 * Modify History:
 * v1.00, 2016/06/07, Yann
 * 	1)First Release
 * 
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/query-page.tld" prefix="t"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>帳號連結扣款(Account Link)系統</title>

<%-- include Header, footer and menu --%>
<%@include file="/include/container.jsp"%>

<%-- include print preview --%>
<%@include file="/include/initialPrint.jsp" %>

<%-- style --%>
<style>
.pageContent {
	width: 950px;
}
</style>
<script type="text/javascript" src="<%=root%>/js/queryPage.js"></script>
<script>
	$(function() {
		$("#btnBack").click(function() {
			$("#form1").attr("action", root + "/0501/requery.html");
			$("#form1").submit();
		});

		$('#btnPrint').click(function(){
	    	$('#btnPrint').printPreview();
		});
		
		<%--Excel檔案下載--%>
		$("#btnDownload").click(function() {
			$("#form1").append("<input type='hidden' name='templateName' value=''/>").attr("action", root + "/0501/download.html");
			$("#form1").submit();
		});
	});
</script>
</head>
<body>
	<!-- Container ============================================================================================== -->
	<div class="container">
		<!-- Content ------------------------------------------------------------------------>
		<div class="mainContent">
			<div class="content">
				<div class="fnctTitle">
					<fmt:message key="function.Id.F0501" />
					<c:out value=">" />
					<c:if test="${command.rptType eq 'total'}">
						<fmt:message key="F0501.field.reportType.total" />
					</c:if>
					<c:if test="${command.rptType eq 'monthly'}">
						<fmt:message key="F0501.field.reportType.monthly" />
					</c:if>
					<c:if test="${command.rptType eq 'daily'}">
						<fmt:message key="F0501.field.reportType.daily" />
					</c:if>
					<c:out value=">" />
					<c:if test="${not empty command.dStts}">
						<fmt:message key="F0501.table.cnt${command.dStts}" />
					</c:if>
					<fmt:message key="F0501.field.detail" />
				</div>
				<div id="printArea">
					<form method="post" name="form1" id="form1" style="margin: 0;">
						<table class="fxdTable" style="width: 100%;">
							<tr class="titleRow">
								<td align="center">
									<%-- 平台代號 --%>
									<fmt:message key="F0501.table.ecId" />
								</td>
								<td align="center">
									<%-- 平台名稱 --%>
									<fmt:message key="F0501.table.ecNameCh" />
								</td>
								<td align="center">
									<%-- 執行日期 --%>
									<fmt:message key="F0501.table.sttsDttm" />
								</td>
								<td align="center">
									<%-- 服務狀態 --%>
									<fmt:message key="F0501.table.stts" />
								</td>
								<td align="center">
									<%-- 平台會員代號 --%>
									<fmt:message key="F0501.table.ecUser" />
								</td>
								<td align="center">
									<%-- 實體帳號 --%>
									<fmt:message key="F0501.table.realAcnt" />
								</td>
								<td align="center">
									<%-- 身分證字號 --%>
									<fmt:message key="F0501.table.custId" />
								</td>
							</tr>
							<c:if test="${not empty result}">
								<c:forEach items="${result}" var="item" varStatus="i">
									<c:choose>
										<c:when test="${i.count%2 ne 0}">
											<tr class="dataRowOdd">
										</c:when>
										<c:otherwise>
											<tr class="dataRowEven">
										</c:otherwise>
									</c:choose>
									<td align="center">
										<%-- 平台代號 --%>
										<c:out value="${item.id.ecId}" />
									</td>
									<td align="center">
										<%-- 平台名稱 --%>
										<c:out value="${item.ecNameCh}" />
									</td>
									<td align="center">
										<%-- 執行日期 --%>
										<fmt:formatDate type="both" pattern="yyyy/MM/dd" value="${item.sttsDttm}" />
									</td>
									<td align="center">
										<%-- 服務狀態 --%>
										<c:out value="${data.stts}" />
										<fmt:message key="F0501.field.STTS.${item.stts}" />
									</td>
									<td align="center">
										<%-- 平台會員代號 --%>
										<c:out value="${item.id.ecUser}" />
									</td>
									<td align="center">
										<%-- 實體帳號 --%>
										<aclFn:realAcntFormate realAcnt="${item.id.realAcnt}" />
									</td>
									<td align="center">
										<%-- 身分證字號 --%>
										<c:out value="${item.id.custId}" />
									</td>
								</c:forEach>
							</c:if>
						</table>
						<input type="hidden" name="strtDate" value="${command.strtDate}" />
						<input type="hidden" name="endDate" value="${command.endDate}" />
						<input type="hidden" name="rptType" value="${command.rptType}" />
						<input type="hidden" name="templateName" value="ACL-S-0501-4" />
					</form>
				</div>
			</div>
			<%-- Paging area --%>
			<t:Page action="../0501/queryDetail.html" btnName="GO" />
			<!-- Button area ------------------------------------------------------------------------>
			<div class="btnContent">
				<input class="btnStyle" type="button" id="btnPrint" value="<fmt:message key="common.btn.Print" />" />
				&nbsp;
				<input class="btnStyle" type="button" id="btnDownload" value='<fmt:message key="common.btn.Download" />' />
				&nbsp;
				<input class="btnStyle" type="button" id="btnBack" value="<fmt:message key="common.btn.Back" />" />
				&nbsp;
			</div>
		</div>
	</div>
	<!-- Footer ============================================================================================== -->
	<div class="footer_line"></div>
</body>
</html>
