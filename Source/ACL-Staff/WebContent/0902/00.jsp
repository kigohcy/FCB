
<%
	/*
	 * @(#)0902/01.jsp
	 *
	 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
	 *
	 * Description:
	 * 操作記錄查詢結果頁
	 *
	 * Modify History:
	 * v1.00, 2016/02/05, Evan
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
<script type="text/javascript" src="<%=root%>/js/queryPage.js"></script>
<script type="text/javascript">
	$(function() {
		datePicker(root);

		$("#accordion").accordion({
			heightStyle : "content",
			collapsible : true
		}).children(".ui-accordion .ui-accordion-content").css("padding", "0px");

		$("#form1").validate({
			rules : {
				startDate : {
					required : true,
					DATE_FRMT_CHECKER : true
				},
				endDate : {
					required : true,
					DATE_FRMT_CHECKER : true,
					DATE_COMPARE : {
						targetDate : '#startDate',
						// 驗證規則: B-預設日不可小於目標日  L-預設日不可大於目標日 E-預設日不可等於目標日
						compareType : 'B'
					},
					RANGE_VALIDATE : {
						dateStart : '#startDate',
						limt : '${command.sysParam}'
					}
				}
			},
			messages : {
				startDate : {
					required : "<fmt:message key="message.alert.startDate" /><fmt:message key="message.alert.notNull" />",
					DATE_FRMT_CHECKER : "<fmt:message key="message.alert.startDate" /> <fmt:message key="message.alert.formateError" />"
				},
				endDate : {
					required : "<fmt:message key="message.alert.endDate" /><fmt:message key="message.alert.notNull" />",
					DATE_FRMT_CHECKER : "<fmt:message key="message.alert.endDate" /><fmt:message key="message.alert.formateError" />",
					DATE_COMPARE : "<fmt:message key="message.alert.dateCompare" />",
					RANGE_VALIDATE : "<fmt:message key="message.alert.dateOutOfRange" />"
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
	
		$("#optCurrentPage").val($("input[name=input_page]").val());
	});

	function query() {
		if ($("#form1").valid()) {
			$("#form1").attr("action", root + "/0902/query.html");
			$("#form1").submit();
		}
	}
	/*
	function btnBack(){
		$("#form2").attr('action', root+'/0902/queryInit.html');
		$("#form2").submit();
	}*/

	function btnDetail(logNo) {
		$("#logNo").val(logNo);
		//alert($("#logNo").val());
		$("#form2").attr("action", root + "/0902/detail.html");
		$("#form2").submit();
	}
</script>
</head>
<body>
	<!-- Container ============================================================================================== -->
	<div class="container">
		<!-- Content ------------------------------------------------------------------------>
		<div class="mainContent">
			<div class="content">
				<div class="fnctTitle">
					<fmt:message key="function.Id.F0902" />
				</div>
				<%-- 操作記錄查詢--%>
				<div id="accordion">
					<h3>
						<fmt:message key="F0902.tilte.QUERY" />
						<%--查詢條件 --%>
					</h3>
					<div style="width: 978px;">
						<form method="post" name="form1" id="form1" action="" style="margin: 0;">
							<table class="fxdTable" width="100%">
								<tr class="dataRowOdd">
									<td width="150px">
										<fmt:message key="F0902.field.OPRT_DATE" />
										*
									</td>
									<%--操作日期* --%>
									<td>
										<input type="text" size="10" maxlength="10" name="startDate" id="startDate" value="${command.startDate}" datePicker="true" />
										~
										<input type="text" size="10" maxlength="10" name="endDate" id="endDate" value="${command.endDate}" datePicker="true" />
										<span class="helperText">&nbsp;&nbsp;<fmt:message key="message.memo.Memo1" /> <c:set var="theString" value="${command.sysParam}" /> <c:if test="${fn:endsWith(theString, 'M')}">
												<c:set var="string1" value="${command.sysParam}" />
												<c:set var="string2" value="${fn:split(string1, 'M')}" />
												<c:out value="${string2[0]}" />
												<fmt:message key="message.memo.Memo4" />
											</c:if> <c:if test="${fn:endsWith(theString, 'D')}">
												<c:set var="string1" value="${command.sysParam}" />
												<c:set var="string2" value="${fn:split(string1, 'D')}" />
												<c:out value="${string2[0]}" />
												<fmt:message key="message.memo.Memo5" />
											</c:if>
										</span>
									</td>
								</tr>
								<tr class="dataRowEven">
									<td>
										<fmt:message key="F0902.field.USER_ID" />
									</td>
									<%--操作人員代號 --%>
									<td>
										<input type="text" size="10" maxlength="10" name="quserId" value="${command.quserId}" />
									</td>
								</tr>
								<tr class="dataRowOdd">
									<td>
										<fmt:message key="F0902.field.FNCT_ID" />
									</td>
									<%--功能代碼 --%>
									<td>
										<select name="qfnctId">
											<option value="" selected><fmt:message key="function.Id.All" /></option>
											<%--全部 --%>
											<c:forEach items="${command.staffSysFnct}" var="item">
												<option value="${item.id.fnctId}" <c:if test="${command.qfnctId eq item.id.fnctId}"> selected</c:if>>${item.id.fnctId}-<fmt:message key="function.Id.${item.id.fnctId}" /></option>
											</c:forEach>
										</select>
									</td>
								</tr>
							</table>
							<div align="left" style="margin: 10px">
								<input class="btnStyle" type="button" name="btn1" value="<fmt:message key="common.btn.Query" />" <%--查詢--%> onclick="query();" />
								&nbsp;
							</div>
						</form>
					</div>
				</div>
				<c:if test="${not command.initQuery}">
					<c:choose>
						<c:when test="${empty result}">
							<%--查無符合條件資料--%>
							<div class="noResult" align="center">
								<fmt:message key="message.sys.NoData" />
							</div>
						</c:when>
						<c:otherwise>
							<form method="post" name="form2" id="form2" action="" style="margin: 0;">
								<input type="hidden" name="logNo" id="logNo" value="" />
								<input type="hidden" name="optCurrentPage" id="optCurrentPage" value="" />
								<table class="fxdTable" width="800px">
									<tr class="titleRow">
										<td width="150px;">
											<fmt:message key="F0902.field.OPRT_DTTM" />
										</td>
										<%--操作日期時間 --%>
										<td width="150px;">
											<fmt:message key="F0902.field.FNCT_NAME" />
										</td>
										<%--功能名稱 --%>
										<td width="100px;">
											<fmt:message key="F0902.field.USER_ID" />
										</td>
										<%--操作人員代號 --%>
										<td width="100px;">
											<fmt:message key="F0902.field.ACTION" />
										</td>
										<%--執行動作 --%>
										<td width="100px;">
											<fmt:message key="F0902.field.RSLT" />
										</td>
										<%--操作結果 --%>
										<td width="120px;">
											<fmt:message key="F0902.field.QUERY_OPTION" />
										</td>
										<%--查詢選項 --%>
									</tr>
									<c:forEach items="${result}" var="item" varStatus="theCount">
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
												<fmt:formatDate value="${item.oprtDttm}" pattern="yyyy/MM/dd HH:mm:ss" />
											</td>
											<td>${item.fnctId}-<fmt:message key="function.Id.${item.fnctId}" />
											</td>
											<td align="center">${item.userId}</td>
											<td align="center">
												<fmt:message key="F0902.field.ACTION.${item.action}" />
											</td>
											<td align="center">
												<fmt:message key="F0902.field.RSLT.${item.rslt}" />
											</td>
											<td align="center" style="color: blue;">
												<c:choose>
													<c:when test="${item.action =='Q' and not empty item.beforeId}">
														<!-- <a href="#" onclick="">查詢條件</a> -->
													</c:when>
													<c:when test="${not empty item.beforeId or not empty item.afterId}">
														<a href="#" onclick="btnDetail('<c:out value="${item.logNo}"/>')" style="color: blue;"><fmt:message key="common.btn.ModifyData" /></a>
														<%--異動資料--%>
													</c:when>
												</c:choose>
											</td>
										</tr>
									</c:forEach>
								</table>
							</form>
							<!-- Paging area ------------------------------------------------------------------------>
							<t:Page action="../0902/requery.html" btnName="GO" />
							<!-- Button area ------------------------------------------------------------------------>
							<!-- <div class="btnContent">-->
							<!--	<input class="btnStyle" type="button" name="btn1" value="<fmt:message key="common.btn.Back" />"回上一頁 onclick="btnBack();" />&nbsp;-->
							<!--</div> -->
						</c:otherwise>
					</c:choose>
				</c:if>
			</div>
		</div>
		<div class="footer_line"></div>
	</div>
</body>
</html>
