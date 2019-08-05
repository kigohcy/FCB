<%
/*
 * @(#)0501/00.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 約定帳號統計
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

<script>
	
	function hideDatePicker(){
		if(form1.rptType[0].checked){
			$("input:text[name='strtDate']").prop("disabled", "disabled");
	        $("input:text[name='endDate']").prop("disabled", "disabled");
			$(".ui-datepicker-trigger").hide();
		}
    };
    
	$(function() {
<%--日期選單--%>
	datePicker(root);
<%--查詢條件--%>
	$("#accordion").accordion({
			heightStyle : "content",
			collapsible : true
		}).children(".ui-accordion .ui-accordion-content").css("padding", "0px");
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
				$("#form1").attr("action", root + "/0501/query.html");
				$("#form1").submit();
			}
		});
<%--明細資料查詢--%>
	$("#btnDetail").click(function() {
			$("#form2").append("<input type='hidden' name='dEcId' value='" + '<c:out value="${command.qEcId}" />' + "'/>").append(
			                   "<input type='hidden' name='dStts' value=''/>").attr(
			                   "action", root + "/0501/queryDetail.html");
			$("#form2").submit();
		});

<%--超連結明細資料查詢--%>
	$(".trnsItem").click(
				function() {
					var trnsItem = $(this);
					$("#form2").append("<input type='hidden' name='dEcId' value='" + trnsItem.attr("ecId") + "'/>").append(
							"<input type='hidden' name='dStts' value='" + trnsItem.attr("stts") + "'/>").attr("action",
							root + "/0501/queryDetail.html").submit();
				});

<%--Excel檔案下載--%>
	$("#btnDownload").click(function() {
			$("#form2").append("<input type='hidden' name='templateName' value=''/>").attr(
			                   "action", root + "/0501/download.html");
			$("#form2").submit();
		});

		//點選總表時, 查詢起迄日欄位 disabled
		$('input:radio').click(function () {
	        if ($(this).prop('checked')) {
	        	
	        	if($(this).val() == "total"){
	        		$("input:text[name='strtDate']").prop("disabled", "disabled");
	        		$("input:text[name='endDate']").prop("disabled", "disabled");
	        		$(".ui-datepicker-trigger").hide();
	        	}else if($(this).val() != "total"){
	        		$("input:text[name='strtDate']").prop("disabled", "");
	        		$("input:text[name='endDate']").prop("disabled", "");
	        		$(".ui-datepicker-trigger").show();
	        	}
	        }
	    });

	    $('#btnPrint').click(function(){
	    	$('#btnPrint').printPreview();
		});
	});

</script>
</head>
<body onload="hideDatePicker();">
	<%-- Container --%>
	<div class="container">
		<%-- Content --%>
		<div class="mainContent">
			<div class="content">
				<%-- Function Title --%>
				<div class="fnctTitle">
					<fmt:message key="function.Id.F0501" />
					<c:if test="${command.initFlag eq false}">
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
										<fmt:message key="F0501.field.queryDate" />
										*
									</td>
									<td>
										<input type="text" size="10" maxlength="10" name="strtDate" value="<c:out value="${command.strtDate}" />" datePicker="true" />
										~
										<input type="text" size="10" maxlength="10" name="endDate" value="<c:out value="${command.endDate}" />" datePicker="true" />
									</td>
								</tr>
								<tr class="dataRowEven">
									<td>
										<%--電商平台--%>
										<fmt:message key="F0501.field.ecId" />
									</td>
									<td>
										<select name="qEcId">
											<option value=" " selected><fmt:message key="F0501.field.ecId.all" /><%--全部--%></option>
											<c:forEach items="${command.ecDataList}" var="ecData">
												<option value="<c:out value="${ecData.ecId}" />" <c:if test="${command.qEcId eq ecData.ecId}"> selected</c:if>><c:out value="${ecData.ecNameCh}" />&nbsp;
												</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr class="dataRowOdd">
									<td>
										<fmt:message key="F0501.field.reportType" />
									</td>
									<td>
										<input type="radio" name="rptType" value="total" checked />
										<fmt:message key="F0501.field.reportType.total" />
										&nbsp;
										<input type="radio" name="rptType" value="monthly" <c:if test="${command.rptType eq 'monthly'}">checked</c:if> />
										<fmt:message key="F0501.field.reportType.monthly" />
										&nbsp;
										<input type="radio" name="rptType" value="daily" <c:if test="${command.rptType eq 'daily'}">checked</c:if> />
										<fmt:message key="F0501.field.reportType.daily" />
										&nbsp;
									</td>
								</tr>
							</table>
						</form>
						<%-- Button area --%>
						<div class="btnContent">
							<input id="btnQuery" class="btnStyle" type="button" value="<fmt:message key="common.btn.Query" /><%--查詢--%>" />
							&nbsp;
						</div>
					</div>
				</div>
				<%-- Query Results --%>
				<c:if test="${command.initFlag eq false}">
					<c:choose>
						<c:when test="${not empty command.reportData}">
							<%-- 總表 --%>
							<c:if test="${command.rptType eq 'total'}">
								<div id="printArea">
									<form method="post" name="form2" id="form2" style="margin: 0;">
										<table class="fxdTable" width="450px">
											<tr class="titleRow">
												<td rowspan="2" width="150px;" align="center">
													<%-- 平台代號 --%>
													<fmt:message key="F0501.table.ecId" />
												</td>
												<td rowspan="2" width="150px;" align="center">
													<%-- 平台名稱 --%>
													<fmt:message key="F0501.table.ecNameCh" />
												</td>
												<td colspan="4" width="150px;" align="center">
													<%-- 筆數 --%>
													<fmt:message key="F0501.table.count" />
												</td>
											</tr>
											<tr class="titleRow">
												<td width="150px;" align="center">
													<%-- 啟用交易 --%>
													<fmt:message key="F0501.table.cnt00" />
												</td>
												<td width="150px;" align="center">
													<%-- 暫停交易 --%>
													<fmt:message key="F0501.table.cnt01" />
												</td>
												<td width="150px;" align="center">
													<%-- 終止交易 --%>
													<fmt:message key="F0501.table.cnt02" />
												</td>
												<td width="150px;" align="center">
													<%-- 帳號總數 --%>
													<fmt:message key="F0501.table.cntTotl" />
												</td>
											</tr>
											<c:forEach items="${command.reportData}" var="data" varStatus="i">
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
															<c:out value="${data.EC_ID}" />
														</td>
														<td align="center">
															<%-- 平台名稱 --%>
															<c:out value="${data.EC_NAME}" />
														</td>
														<td align="center">
															<%-- 啟用交易筆數 --%>
															<c:if test="${data.CNT_00 eq 0}">
																<c:out value="${data.CNT_00}" />
															</c:if>
															<c:if test="${data.CNT_00 ne 0}">
																<a href="#" class="trnsItem" stts="00" ecId="${data.EC_ID}">
																	<c:out value="${data.CNT_00}" />
																</a>
															</c:if>
														</td>
														<td align="center">
															<%-- 暫停交易筆數 --%>
															<c:if test="${data.CNT_01 eq 0}">
																<c:out value="${data.CNT_01}" />
															</c:if>
															<c:if test="${data.CNT_01 ne 0}">
																<a href="#" class="trnsItem" stts="01" ecId="${data.EC_ID}">
																	<c:out value="${data.CNT_01}" />
																</a>
															</c:if>
														</td>
														<td align="center">
															<%-- 終止交易筆數 --%>
															<c:if test="${data.CNT_02 eq 0}">
																<c:out value="${data.CNT_02}" />
															</c:if>
															<c:if test="${data.CNT_02 ne 0}">
																<a href="#" class="trnsItem" stts="02" ecId="${data.EC_ID}">
																	<c:out value="${data.CNT_02}" />
																</a>
															</c:if>
														</td>
														<td align="center">
															<%-- 帳號總數 --%>
															<c:out value="${data.CNT_TOTL}" />
														</td>
												</tr>
											</c:forEach>
											
										</table>
										<input type="hidden" name="strtDate" value="${command.strtDate}" />
										<input type="hidden" name="endDate" value="${command.endDate}" />
										<input type="hidden" name="rptType" value="${command.rptType}" />
										<input type="hidden" name="qEcId" value="${command.qEcId}" />
										<input type="hidden" name="templateName" value="ACL-S-0501-1" />
									</form>
								</div>
								<!-- Button area ------------------------------------------------------------------------>
								<div class="btnContent">
									<input class="btnStyle" type="button" id="btnPrint" value='<fmt:message key="common.btn.Print" />' />
									&nbsp;
									<input class="btnStyle" type="button" id="btnDownload" value='<fmt:message key="common.btn.Download" />' />
									&nbsp;
									<input class="btnStyle" type="button" id="btnDetail" value='<fmt:message key="common.btn.Detail" />' />
									&nbsp;
								</div>
							</c:if>
							
							<%-- 月報 --%>
							<c:if test="${command.rptType eq 'monthly'}">
								<div id="printArea">
									<form method="post" name="form2" id="form2" style="margin: 0;">
										<table class="fxdTable" width="600px">
											<tr class="titleRow">
												<td rowspan="2" width="150px;" align="center">
													<%-- 統計月份 --%>
													<fmt:message key="F0501.table.monthly" />
												</td>
												<td rowspan="2" width="150px;" align="center">
													<%-- 平台代號 --%>
													<fmt:message key="F0501.table.ecId" />
												</td>
												<td rowspan="2" width="150px;" align="center">
													<%-- 平台名稱 --%>
													<fmt:message key="F0501.table.ecNameCh" />
												</td>
												<td colspan="4" width="150px;" align="center">
													<%-- 筆數 --%>
													<fmt:message key="F0501.table.count" />
												</td>
											</tr>
											<tr class="titleRow">
												<td width="150px;" align="center">
													<%-- 啟用交易 --%>
													<fmt:message key="F0501.table.cnt00" />
												</td>
												<td width="150px;" align="center">
													<%-- 暫停交易 --%>
													<fmt:message key="F0501.table.cnt01" />
												</td>
												<td width="150px;" align="center">
													<%-- 終止交易 --%>
													<fmt:message key="F0501.table.cnt02" />
												</td>
												<td width="150px;" align="center">
													<%-- 帳號總數 --%>
													<fmt:message key="F0501.table.cntTotl" />
												</td>
											</tr>
											<c:forEach items="${command.reportData}" var="data" varStatus="i">
												<c:choose>
													<c:when test="${i.count%2 ne 0}">
														<tr class="dataRowOdd">
													</c:when>
													<c:otherwise>
														<tr class="dataRowEven">
													</c:otherwise>
												</c:choose>
												
												    <td align="center">
														<%-- 統計月份 --%>
														<c:out value="${data.DATE}" />
													</td>
													<td align="center">
														<%-- 平台代號 --%>
														<c:out value="${data.EC_ID}" />
													</td>
													<td align="center">
														<%-- 平台名稱 --%>
														<c:out value="${data.EC_NAME}" />
													</td>
													<td align="center">
														<%-- 啟用交易筆數 --%>
														<c:out value="${data.CNT_00}" />
													</td>
													<td align="center">
														<%-- 暫停交易筆數 --%>
														<c:out value="${data.CNT_01}" />
													</td>
													<td align="center">
														<%-- 終止交易筆數 --%>
														<c:out value="${data.CNT_02}" />
													</td>
													<td align="center">
														<%-- 帳號總數 --%>
														<c:out value="${data.CNT_TOTL}" />
													</td>
												</tr>
											</c:forEach>
											
										</table>
										<input type="hidden" name="strtDate" value="${command.strtDate}" />
										<input type="hidden" name="endDate" value="${command.endDate}" />
										<input type="hidden" name="rptType" value="${command.rptType}" />
										<input type="hidden" name="qEcId" value="${command.qEcId}" />
										<input type="hidden" name="templateName" value="ACL-S-0501-2" />
									</form>
								</div>
								<!-- Button area ------------------------------------------------------------------------>
								<div class="btnContent">
									<input class="btnStyle" type="button" id="btnPrint" value='<fmt:message key="common.btn.Print" />' />
									&nbsp;
									<input class="btnStyle" type="button" id="btnDownload" value='<fmt:message key="common.btn.Download" />' />
									&nbsp;
								</div>
							</c:if>
							
							<%-- 日報 --%>
							<c:if test="${command.rptType eq 'daily'}">
								<div id="printArea">
									<form method="post" name="form2" id="form2" style="margin: 0;">
										<table class="fxdTable" width="600px">
											<tr class="titleRow">
												<td rowspan="2" width="150px;" align="center">
													<%-- 統計日期 --%>
													<fmt:message key="F0501.table.daily" />
												</td>
												<td rowspan="2" width="150px;" align="center">
													<%-- 平台代號 --%>
													<fmt:message key="F0501.table.ecId" />
												</td>
												<td rowspan="2" width="150px;" align="center">
													<%-- 平台名稱 --%>
													<fmt:message key="F0501.table.ecNameCh" />
												</td>
												<td colspan="4" width="150px;" align="center">
													<%-- 筆數 --%>
													<fmt:message key="F0501.table.count" />
												</td>
											</tr>
											<tr class="titleRow">
												<td width="150px;" align="center">
													<%-- 啟用交易 --%>
													<fmt:message key="F0501.table.cnt00" />
												</td>
												<td width="150px;" align="center">
													<%-- 暫停交易 --%>
													<fmt:message key="F0501.table.cnt01" />
												</td>
												<td width="150px;" align="center">
													<%-- 終止交易 --%>
													<fmt:message key="F0501.table.cnt02" />
												</td>
												<td width="150px;" align="center">
													<%-- 帳號總數 --%>
													<fmt:message key="F0501.table.cntTotl" />
												</td>
											</tr>
											<c:forEach items="${command.reportData}" var="data" varStatus="i">
												<c:choose>
													<c:when test="${i.count%2 ne 0}">
														<tr class="dataRowOdd">
													</c:when>
													<c:otherwise>
														<tr class="dataRowEven">
													</c:otherwise>
												</c:choose>
												
												    <td align="center">
														<%-- 統計日期 --%>
														<c:out value="${data.DATE}" />
													</td>
													<td align="center">
														<%-- 平台代號 --%>
														<c:out value="${data.EC_ID}" />
													</td>
													<td align="center">
														<%-- 平台名稱 --%>
														<c:out value="${data.EC_NAME}" />
													</td>
													<td align="center">
														<%-- 啟用交易筆數 --%>
														<c:out value="${data.CNT_00}" />
													</td>
													<td align="center">
														<%-- 暫停交易筆數 --%>
														<c:out value="${data.CNT_01}" />
													</td>
													<td align="center">
														<%-- 終止交易筆數 --%>
														<c:out value="${data.CNT_02}" />
													</td>
													<td align="center">
														<%-- 帳號總數 --%>
														<c:out value="${data.CNT_TOTL}" />
													</td>
												</tr>
											</c:forEach>
											
										</table>
										<input type="hidden" name="strtDate" value="${command.strtDate}" />
										<input type="hidden" name="endDate" value="${command.endDate}" />
										<input type="hidden" name="rptType" value="${command.rptType}" />
										<input type="hidden" name="qEcId" value="${command.qEcId}" />
										<input type="hidden" name="templateName" value="ACL-S-0501-3" />
									</form>
								</div>
								<!-- Button area ------------------------------------------------------------------------>
								<div class="btnContent">
									<input class="btnStyle" type="button" id="btnPrint" value='<fmt:message key="common.btn.Print" />' />
									&nbsp;
									<input class="btnStyle" type="button" id="btnDownload" value='<fmt:message key="common.btn.Download" />' />
									&nbsp;
								</div>
							</c:if>
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
