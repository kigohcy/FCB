
<%
	/*
	 * @(#)0504/00.jsp
	 *
	 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
	 *
	 * Description: 手續費統計報表_明細資料(超連結)
	 *
	 * Modify History:
	 * v1.00, 2016/06/01, Jimmy
	 * 	1)First Release
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
			$("#form1").attr("action", root + "/0504/requery.html");
			$("#form1").submit();
		});

		$('#btnPrint').click(function(){
	    	$('#btnPrint').printPreview();
		});
		
		<%--Excel檔案下載--%>
		$("#btnDownload").click(function() {
			$("#form1").append("<input type='hidden' name='templateName' value='ACL-S-0504-4'/>").attr("action", root + "/0504/download.html");
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
					<fmt:message key="function.Id.F0504" />
					<c:out value=">" />
					<c:if test="${command.rptType eq 'platform'}">
						<fmt:message key="F0504.field.reportType.platform" />
					</c:if>
					<c:if test="${command.rptType eq 'monthly'}">
						<fmt:message key="F0504.field.reportType.monthly" />
					</c:if>
					<c:if test="${command.rptType eq 'daily'}">
						<fmt:message key="F0504.field.reportType.daily" />
					</c:if>
					<c:out value=">" />
					<c:if test="${command.trnsType eq 'A'}">
						<fmt:message key="F0504.field.trnsType.A" /><fmt:message key="F0504.field.detail" />
					</c:if>
					<c:if test="${command.trnsType eq 'B'}">
						<fmt:message key="F0504.field.trnsType.B" /><fmt:message key="F0504.field.detail" />
					</c:if>
					<c:if test="${command.trnsType eq 'C'}">
						<fmt:message key="F0504.field.trnsType.C" /><fmt:message key="F0504.field.detail" />
					</c:if>
					<c:if test="${command.trnsType eq 'D'}">
						<fmt:message key="F0504.field.trnsType.D" /><fmt:message key="F0504.field.detail" />
					</c:if>
					<c:if test="${command.trnsType eq 'E'}">
						<fmt:message key="F0504.field.trnsType.E" /><fmt:message key="F0504.field.detail" />
					</c:if>
					
				</div>
				<div id="printArea">
					<form method="post" name="form1" id="form1" style="margin: 0;">
						<table class="fxdTable" style="width: 100%;">
							<tr class="titleRow">
								<td align="center">
									<%-- 身分證字號 --%>
									<fmt:message key="F0504.table.custId" />
								</td>
								<td align="center">
									<%-- 電商平台 --%>
									<fmt:message key="F0504.field.ecId" />
								</td>
								<td align="center">
									<%-- 平台會員代號 --%>
									<fmt:message key="F0504.table.custSerl" />
								</td>
								<td align="center">
									<%-- 轉出帳號 --%>
									<fmt:message key="F0504.table.realAcnt" />
								</td>
								<td align="center">
									<%-- 轉入帳號 --%>
									<fmt:message key="F0504.table.recvAcnt" />
								</td>
								<td align="center">
									<%-- 交易日期時間 --%>
									<fmt:message key="F0504.table.trnsDttm" />
								</td>
								<td align="center">
									<%-- 交易金額 --%>
									<fmt:message key="F0504.table.trnsAmnt" />
								</td>
								<td align="center">
									<%-- 手續費 --%>
									<fmt:message key="F0504.table.fee" />
								</td>
								<td align="center">
									<%-- 交易結果 --%>
									<fmt:message key="F0504.table.result" />
								</td>
								<td align="center">
									<%-- 平台訊息序號 --%>
									<fmt:message key="F0504.table.ecMsgNo" />
								</td>
							</tr>
							<c:if test="${not empty result}">
								<c:forEach items="${result}" var="trnsData" varStatus="i">
									<c:choose>
										<c:when test="${i.count%2 ne 0}">
											<tr class="dataRowOdd">
										</c:when>
										<c:otherwise>
											<tr class="dataRowEven">
										</c:otherwise>
									</c:choose>
									<td align="center">
										<%-- 身分證字號 --%>
										<c:out value="${trnsData.custId}" />
									</td>
									<td align="Left">
										<%-- 平台代號 --%>
										<c:out value="${trnsData.id.ecId}" />
										&nbsp;
										<%-- 平台名稱 --%>
										<c:out value="${trnsData.ecNameCh}" />
									</td>
									<td align="center">
										<%-- 平台會員代號 --%>
										<c:out value="${trnsData.ecUser}" />
									</td>
									<td align="center">
										<%-- 轉出帳號 --%>
										<aclFn:realAcntFormate realAcnt="${trnsData.realAcnt}" />
									</td>
									<td align="center">
										<%-- 轉入帳號 --%>
										<aclFn:realAcntFormate realAcnt="${trnsData.recvAcnt}" />
									</td>
									<td align="center">
										<%-- 交易日期時間 --%>
										<fmt:formatDate type="both" pattern="yyyy/MM/dd HH:mm:ss" value="${trnsData.trnsDttm}" />
									</td>
									<td align="right">
										<%-- 交易金額 --%>
										<fmt:formatNumber type="number" maxFractionDigits="3" value="${trnsData.trnsAmnt}" />
									</td>
									<td align="right">
										<%-- 手續費 --%>
										<fmt:formatNumber type="number" maxFractionDigits="3" value="${trnsData.feeAmnt}" />
									</td>
									<td align="center">
										<c:if test="${trnsData.trnsStts eq '01'}">
											<%--不明--%>
											<fmt:message key="common.result.unknow.${trnsData.trnsType}" />
										</c:if>
										<c:if test="${trnsData.trnsStts eq '02'}">
											<%--成功--%>
											<fmt:message key="common.result.success.${trnsData.trnsType}" />
										</c:if>
										<c:if test="${trnsData.trnsStts eq '03'}">
											<%--失敗--%>
											<fmt:message key="common.result.failure.${trnsData.trnsType}" />
										</c:if>
									</td>
									<td align="center">
										<%-- 平台訊息序號 --%>
										<c:out value="${trnsData.id.ecMsgNo}" />
									</td>
								</c:forEach>
							</c:if>
						</table>
						<input type="hidden" name="strtDate" value="${command.strtDate}" />
						<input type="hidden" name="endDate" value="${command.endDate}" />
						<input type="hidden" name="rptType" value="${command.rptType}" />
						<input type="hidden" name="qEcId" value="${command.qEcId}" />
					</form>
				</div>
			</div>
			<%-- Paging area --%>
			<t:Page action="../0504/queryDetail2.html" btnName="GO" />
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

