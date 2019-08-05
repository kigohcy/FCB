
<%
	/*
	 * @(#)0504/00.jsp
	 *
	 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
	 *
	 * Description: 手續費統計報表
	 *
	 * Modify History:
	 * v1.00, 2016/05/18, Jimmy
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
<%@include file="/include/initialPrint.jsp"%>

<script>
	$(function() {
<%--日期選單--%>
	datePicker(root);
<%--查詢條件--%>
	$("#accordion").accordion({
			heightStyle : "content",
			collapsible : true
		}).children(".ui-accordion .ui-accordion-content")
				.css("padding", "0px");
<%-- 表單驗證 --%>
	$("#form1").validate({
		rules : {
			strtDate : {
				required : true,
				DATE_FRMT_CHECKER : true
			},
			endDate : {
				required : true,
				DATE_FRMT_CHECKER : true,
				DATE_COMPARE : {
					targetDate : 'input[name="strtDate"]',
					// 驗證規則: B-預設日不可小於目標日  L-預設日不可大於目標日 E-預設日不可等於目標日
					compareType : 'B'
				}
			}
		},
		messages : {
			strtDate : {
				required : '<fmt:message key="message.alert.startDate" /><fmt:message key="message.alert.notNull" />',
				DATE_FRMT_CHECKER : '<fmt:message key="message.alert.startDate" /> <fmt:message key="message.alert.formateError" />'
			},
			endDate : {
				required : '<fmt:message key="message.alert.endDate" /><fmt:message key="message.alert.notNull" />',
				DATE_FRMT_CHECKER : '<fmt:message key="message.alert.endDate" /><fmt:message key="message.alert.formateError" />',
				DATE_COMPARE : '<fmt:message key="message.alert.dateCompare" />'
			}
		},
		showErrors : function(errorMap, errorList) {
			var err = [];
			$.each(errorList, function(i, v) {
				err.push(v.message);
			});
			if (err.length > 0) {
				alert(err.join("\n"));
			}
		},
		onkeyup : false,
		onfocusout : false,
		onsubmit : false
	});
<%--查詢--%>
	$("#btnQuery").click(function() {
			if ($("#form1").valid()) {
				$("#form1").attr("action", root + "/0504/query.html");
				$("#form1").submit();
			}
		});
<%--明細資料查詢--%>
	$("#btnDetail").click(function() {
			$("#form2").append("<input type='hidden' name='qTrnsStts' value='"+ $('#qTrnsSttsInForm1').val() + "'/>")
				.attr("action", root + "/0504/queryDetail.html");
			$("#form2").submit();
		});
<%--個別明細資料查詢--%>
	$(".trnsItem").click(function() {
		var trnsItem = $(this);
		$("#form2").append("<input type='hidden' name='trnsType' value='"+ trnsItem.attr("trnsType")+ "'/>")
				   .append("<input type='hidden' name='dEcId' value='"+ trnsItem.attr("ecId")+ "'/>")
				   .append("<input type='hidden' name='trnsTime' value='"+ trnsItem.attr("trnsTime")+ "'/>")
				   .append("<input type='hidden' name='qTrnsStts' value='"+ $('#qTrnsSttsInForm1').val() + "'/>")
				   .attr("action",root + "/0504/queryDetail2.html")
				   .submit();
	});

		$('#btnPrint').click(function() {
			$('#btnPrint').printPreview();
		});

		$("#btnDownload").click(function() {
			var rptType = '<c:out value="${command.rptType}"/>';

			if (rptType == "platform") {
				$("#form2").append('<input type="hidden" name="templateName" value="ACL-S-0504-1" />');
			} else if (rptType == "monthly") {
				$("#form2").append('<input type="hidden" name="templateName" value="ACL-S-0504-2" />');
			} else if (rptType == "daily") {
				$("#form2").append('<input type="hidden" name="templateName" value="ACL-S-0504-3" />');
			}
			
			$("#form2").attr("action",root + "/0504/download.html");
			$("#form2").submit();
		});
	});
</script>
</head>
<body>
	<%-- Container --%>
	<div class="container">
		<%-- Content --%>
		<div class="mainContent">
			<div class="content">
				<%-- Function Title --%>
				<div class="fnctTitle">
					<fmt:message key="function.Id.F0504" />
					<c:if test="${command.initFlag eq false}">
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
					</c:if>
				</div>
				<%-- Query Condition --%>
				<div id="accordion">
					<h3>
						<%--查詢條件--%>
						<fmt:message key="common.queryCondition" />
					</h3>
					<div style="width: 978px;">
						<form method="post" name="form1" id="form1" action="" style="margin: 0;">
							<table class="fxdTable" style="width: 100%;">
								<tr class="dataRowOdd">
									<td width="150px">
										<%--查詢日起訖--%>
										<fmt:message key="F0504.field.queryDate" />
										*
									</td>
									<td>
										<input type="text" size="10" maxlength="10" name="strtDate" value="<c:out value="${command.strtDate}" />" datePicker="true" /> ~ <input
											type="text" size="10" maxlength="10" name="endDate" value="<c:out value="${command.endDate}" />" datePicker="true" />
									</td>
								</tr>
								<tr class="dataRowEven">
									<td>
										<%--電商平台--%>
										<fmt:message key="F0504.field.ecId" />
									</td>
									<td>
										<select name="qEcId">
											<option value=" " selected><fmt:message key="F0504.field.ecId.all" /><%--全部--%></option>
											<c:forEach items="${command.ecData}" var="ecData">
												<option value="<c:out value="${ecData.ecId}" />" <c:if test="${command.qEcId eq ecData.ecId}"> selected</c:if>><c:out
														value="${ecData.ecNameCh}" />&nbsp;
												</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr class="dataRowOdd">
									<td>
										<fmt:message key="F0504.field.reportType" />
									</td>
									<td>
										<input type="radio" name="rptType" value="platform" checked />
										<fmt:message key="F0504.field.reportType.platform" />
										&nbsp; <input type="radio" name="rptType" value="monthly" <c:if test="${command.rptType eq 'monthly'}">checked</c:if> />
										<fmt:message key="F0504.field.reportType.monthly" />
										&nbsp; <input type="radio" name="rptType" value="daily" <c:if test="${command.rptType eq 'daily'}">checked</c:if> />
										<fmt:message key="F0504.field.reportType.daily" />
										&nbsp;
									</td>
								</tr>
								<tr class="dataRowEven">
									<td>
										<%--交易類別--%>
										<fmt:message key="common.queryCondition.trnsStts" />
									</td>
									<td>
										<select name="qTrnsStts" id="qTrnsSttsInForm1">
											<option value="" selected><fmt:message key="common.queryCondition.all" /><%--全部--%></option>
											<option value="01" <c:if test="${command.qTrnsStts eq '01'}"> selected</c:if>><fmt:message key="common.result.unknow" /><%--不明--%></option>
											<option value="02" <c:if test="${command.qTrnsStts eq '02'}"> selected</c:if>><fmt:message key="common.result.success" /><%--成功--%></option>
											<option value="03" <c:if test="${command.qTrnsStts eq '03'}"> selected</c:if>><fmt:message key="common.result.failure" /><%--失敗--%></option>
										</select>
									</td>
								</tr>
							</table>
						</form>
						<%-- Button area --%>
						<div class="btnContent">
							<input id="btnQuery" class="btnStyle" type="button" value="<fmt:message key="common.btn.Query" /><%--查詢--%>" /> &nbsp;
						</div>
					</div>
				</div>
				<%-- Query Results --%>
				<c:if test="${command.initFlag eq false}">
					<c:choose>
						<c:when test="${not empty command.report}">
							<div id=printArea>
								<form method="post" name="form2" id="form2" style="margin: 0;">
									<table class="fxdTable" style="width: 100%;">
										<tr class="titleRow">
											<c:choose>
												<c:when test="${(command.rptType eq 'monthly')}">
													<td rowspan="2" width="80px;" align="center">
														<%-- 月份 --%>
														<fmt:message key="F0504.table.month" />
													</td>
												</c:when>
												<c:when test="${(command.rptType eq 'daily')}">
													<td rowspan="2" width="80px;" align="center">
														<%-- 日期 --%>
														<fmt:message key="F0504.table.date" />
													</td>
												</c:when>
											</c:choose>
											<td rowspan="2" width="80px;" align="center">
												<%-- 平台代號 --%>
												<fmt:message key="F0504.table.ecId" />
											</td>
											<td rowspan="2" width="120px;" align="center">
												<%-- 平台名稱 --%>
												<fmt:message key="F0504.table.ecNameCh" />
											</td>
											<td colspan="2" width="150px;" align="center">
												<%-- 扣款交易 --%>
												<fmt:message key="F0504.table.trns.A" />
											</td>
											<td colspan="2" width="150px;" align="center">
												<%-- 儲值交易 --%>
												<fmt:message key="F0504.table.trns.D" />
											</td>
											<td colspan="2" width="150px;" align="center">
												<%-- 退款交易 --%>
												<fmt:message key="F0504.table.trns.B" />
											</td>
											<td colspan="2" width="150px;" align="center">
												<%-- 提領交易 --%>
												<fmt:message key="F0504.table.trns.C" />
											</td>
											<td colspan="2" width="150px;" align="center">
												<%-- 繳費稅交易 --%>
												<fmt:message key="F0504.table.trns.E" />
											</td>
											<td rowspan="2" width="100px;" align="center">
												<%-- 交易淨額 --%>
												<fmt:message key="F0504.table.trns.amnt" />
											</td>
											<td rowspan="2" width="100px;" align="center">
												<%-- 手續費 --%>
												<fmt:message key="F0504.table.fee" />
											</td>
											<td rowspan="2" width="100px;" align="center">
												<%-- 目前收費方式 --%>
												<fmt:message key="F0504.table.fee.rate" />
											</td>
										</tr>
										<tr class="titleRow">
											<td width="50px;" align="center">
												<%-- 筆數 --%>
												<fmt:message key="F0504.table.count" />
											</td>
											<td width="100px;" align="center">
												<%-- 金額 --%>
												<fmt:message key="F0504.table.amnt" />
											</td>
											<td width="50px;" align="center">
												<%-- 筆數 --%>
												<fmt:message key="F0504.table.count" />
											</td>
											<td width="100px;" align="center">
												<%-- 金額 --%>
												<fmt:message key="F0504.table.amnt" />
											</td>
											<td width="50px;" align="center">
												<%-- 筆數 --%>
												<fmt:message key="F0504.table.count" />
											</td>
											<td width="100px;" align="center">
												<%-- 金額 --%>
												<fmt:message key="F0504.table.amnt" />
											</td>
											<td width="50px;" align="center">
												<%-- 筆數 --%>
												<fmt:message key="F0504.table.count" />
											</td>
											<td width="100px;" align="center">
												<%-- 金額 --%>
												<fmt:message key="F0504.table.amnt" />
											</td>
											<td width="50px;" align="center">
												<%-- 筆數 --%>
												<fmt:message key="F0504.table.count" />
											</td>
											<td width="100px;" align="center">
												<%-- 金額 --%>
												<fmt:message key="F0504.table.amnt" />
											</td>
										</tr>
										<c:forEach items="${command.report}" var="report" varStatus="i">
											<c:choose>
												<c:when test="${i.count%2 ne 0}">
													<tr class="dataRowOdd">
												</c:when>
												<c:otherwise>
													<tr class="dataRowEven">
												</c:otherwise>
											</c:choose>
											<c:if test="${(command.rptType ne 'platform')}">
												<td align="center">
													<!-- 交易時間 -->
													<c:out value="${report.trnsTime}" />
												</td>
											</c:if>
											<td align="center">
												<%-- 平台代號 --%>
												<c:out value="${report.ecId}" />
											</td>
											<td align="center">
												<%-- 平台名稱 --%>
												<c:out value="${report.ecNameCh}" />
											</td>										
											<td align="center">
												<%-- 扣款交易筆數 --%>
												<c:if test="${report.countA eq 0}">
													<c:out value="${report.countA}" />
												</c:if>
												<c:if test="${report.countA ne 0}">
													<a href="#" class="trnsItem" trnsType="A" ecId="${report.ecId}" trnsTime = "${report.trnsTime}">
														<c:out value="${report.countA}" />
													</a>
												</c:if>
											</td>
											<td align="right">
												<%-- 扣款交易金額 --%>
												<c:out value="${report.amuntA}" />
											</td>
											<td align="center">
												<%-- 儲值交易筆數 --%>
												<c:if test="${report.countD eq 0}">
													<c:out value="${report.countD}" />
												</c:if>
												<c:if test="${report.countD ne 0}">
													<a href="#" class="trnsItem" trnsType="D" ecId="${report.ecId}" trnsTime = "${report.trnsTime}">
														<c:out value="${report.countD}" />
													</a>
												</c:if>
											</td>
											<td align="right">
												<%-- 儲值交易金額 --%>
												<c:out value="${report.amuntD}" />
											</td>										
											<td align="center">
												<%-- 退款交易筆數 --%>
												<c:if test="${report.countB eq 0}">
													<c:out value="${report.countB}" />
												</c:if>
												<c:if test="${report.countB ne 0}">
													<a href="#" class="trnsItem" trnsType="B" ecId="${report.ecId}" trnsTime = "${report.trnsTime}">
														<c:out value="${report.countB}" />
													</a>
												</c:if>
											</td>
											<td align="right">
												<%-- 退款交易金額 --%>
												<c:out value="${report.amuntB}" />
											</td>									
											<td align="center">
												<%-- 提領交易筆數 --%>
												<c:if test="${report.countC eq 0}">
													<c:out value="${report.countC}" />
												</c:if>
												<c:if test="${report.countC ne 0}">
													<a href="#" class="trnsItem" trnsType="C" ecId="${report.ecId}" trnsTime = "${report.trnsTime}">
														<c:out value="${report.countC}" />
													</a>
												</c:if>
											</td>
											<td align="right">
												<%-- 提領交易金額 --%>
												<c:out value="${report.amuntC}" />
											</td>
											<td align="center">
												<%-- 繳費稅交易筆數 --%>
												<c:if test="${report.countE eq 0}">
													<c:out value="${report.countE}" />
												</c:if>
												<c:if test="${report.countE ne 0}">
													<a href="#" class="trnsItem" trnsType="E" ecId="${report.ecId}" trnsTime = "${report.trnsTime}">
														<c:out value="${report.countE}" />
													</a>
												</c:if>
											</td>
											<td align="right">
												<%-- 繳費稅交易金額 --%>
												<c:out value="${report.amuntE}" />
											</td>									
											<td align="right">
												<%-- 交易淨額 --%>
												<c:out value="${report.amuntT}" />
											</td>
											<td align="right">
												<%-- 手續費 --%>
												<c:out value="${report.feeAmount}" />
											</td>
											<td align="left">
												<%-- 目前收費方式 --%>
												<c:out value="${report.currentFeeType}" />
												<BR>
												<%-- 目前繳費稅方式 --%>
												<%-- <c:out value="${report.currentFeeType}" /> --%>
											</td>
											</tr>
										</c:forEach>
										<tr class="titleRow">
											<c:if test="${(command.rptType eq 'platform')}">
												<td colspan="2" align="center">
											</c:if>
											<c:if test="${(command.rptType ne 'platform')}">
												<td colspan="3" align="center">
											</c:if>
											<!-- 合計 -->
											<fmt:message key="F0504.table.sum" />
											</td>
											<td align="center">
												<!-- 總扣款筆數 -->
												<c:out value='${command.SUMMARY_COUNT_A}' />
											</td>
											<td align="right">
												<!-- 總扣款金額 -->
												<c:out value='${command.SUMMARY_AMUNT_A}' />
											</td>
											<td align="center">
												<!-- 總儲值筆數 -->
												<c:out value='${command.SUMMARY_COUNT_D}' />
											</td>
											<td align="right">
												<!-- 總儲值金額 -->
												<c:out value='${command.SUMMARY_AMUNT_D}' />
											</td>
											<td align="center">
												<!-- 總退款筆數 -->
												<c:out value='${command.SUMMARY_COUNT_B}' />
											</td>
											<td align="right">
												<!-- 總退款金額 -->
												<c:out value='${command.SUMMARY_AMUNT_B}' />
											</td>
											<td align="center">
												<!-- 總提領筆數 -->
												<c:out value='${command.SUMMARY_COUNT_C}' />
											</td>
											<td align="right">
												<!-- 總提領金額 -->
												<c:out value='${command.SUMMARY_AMUNT_C}' />
											</td>
											<td align="center">
												<!-- 總繳費稅筆數 -->
												<c:out value='${command.SUMMARY_COUNT_E}' />
											</td>
											<td align="right">
												<!-- 總繳費稅金額 -->
												<c:out value='${command.SUMMARY_AMUNT_E}' />
											</td>
											<td align="right">
												<!-- 總交易淨額 -->
												<c:out value='${command.SUMMARY_AMUNT_T}' />
											</td>
											<td align="right">
												<!-- 總手續費 -->
												<c:out value='${command.SUMMARY_FEE_AMUNT}' />
											</td>
											<td align="center"></td>
										</tr>
									</table>
									<input type="hidden" name="strtDate" value="${command.strtDate}" /> <input type="hidden" name="endDate" value="${command.endDate}" /> <input
										type="hidden" name="rptType" value="${command.rptType}" /> <input type="hidden" name="qEcId" value="${command.qEcId}" />
								</form>
							</div>
							<!-- Button area ------------------------------------------------------------------------>
							<div class="btnContent">
								<input class="btnStyle" type="button" id="btnPrint" value='<fmt:message key="common.btn.Print" />' /> &nbsp; 
								<input class="btnStyle" type="button" id="btnDownload" value='<fmt:message key="common.btn.Download" />' /> &nbsp; 
								<input class="btnStyle" type="button" id="btnDetail" value='<fmt:message key="common.btn.Detail" />' /> &nbsp;
							</div>
						</c:when>
						<c:otherwise>
							<div class="noResult" align="center">
								<fmt:message key="message.sys.NoData" />
							</div>
						</c:otherwise>
					</c:choose>
				</c:if>
			</div>
			<div class="footer_line"></div>
		</div>
	</div>
</body>
</html>

