
<%
	/*
	 * @(#)0903/00.jsp
	 *
	 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
	 *
	 * Description: 會員操作記錄查詢結
	 *
	 * Modify History:
	 * v1.00, 2016/06/22, Jimmy Yen
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
						limt : '${command.queryLimt}'
					}
				},
				qUserId:{
					required : true
				}
			},
			messages : {
				startDate : {
					required : "<fmt:message key="message.alert.startDate" /><fmt:message key="message.alert.notNull" />",
					DATE_FRMT_CHECKER : "<fmt:message key="message.alert.startDate" /><fmt:message key="message.alert.formateError" />"
				},
				endDate : {
					required : "<fmt:message key="message.alert.endDate" /><fmt:message key="message.alert.notNull" />",
					DATE_FRMT_CHECKER : "<fmt:message key="message.alert.endDate" /><fmt:message key="message.alert.formateError" />",
					DATE_COMPARE : "<fmt:message key="message.alert.dateCompare" />",
					RANGE_VALIDATE : "<fmt:message key="message.alert.dateOutOfRange" />"
				},
				qUserId:{
					required : "<fmt:message key="message.alert.custId" /><fmt:message key="message.alert.notNull" />"
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
			$("#form1").attr("action", root + "/0903/query.html");
			$("#form1").submit();
		}
	}
	/*
	function btnBack(){
		$("#form2").attr('action', root+'/0903/queryInit.html');
		$("#form2").submit();
	}*/

	function btnDetail(logNo) {
		$("#logNo").val(logNo);
		$("#form2").attr("action", root + "/0903/detail.html");
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
					<fmt:message key="function.Id.F0903" />
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
										<span class="helperText">
											&nbsp;&nbsp;
											<fmt:message key="message.memo.Memo1" />
											<c:set var="theString" value="${command.queryLimt}" />
											<c:if test="${fn:endsWith(theString, 'M')}">
												<c:set var="string1" value="${command.queryLimt}" />
												<c:set var="string2" value="${fn:split(string1, 'M')}" />
												<c:out value="${string2[0]}" />
												<fmt:message key="message.memo.Memo4" />
											</c:if>
											<c:if test="${fn:endsWith(theString, 'D')}">
												<c:set var="string1" value="${command.queryLimt}" />
												<c:set var="string2" value="${fn:split(string1, 'D')}" />
												<c:out value="${string2[0]}" />
												<fmt:message key="message.memo.Memo5" />
											</c:if>
										</span>
									</td>
								</tr>
								<tr class="dataRowEven">
									<td>
										<fmt:message key="F0903.field.userId" />*
									</td>
									<%--操作人員代號 --%>
									<td>
										<input type="text" size="10" maxlength="11" name="qUserId" value="${command.qUserId}" />
									</td>
								</tr>
								<tr class="dataRowOdd">
									<td>
										<fmt:message key="F0903.field.fnctId" />
									</td>
									<%--功能代碼 --%>
									<td>
										<select name="qFnctId">
											<option value="" selected><fmt:message key="function.Id.All" /></option>
											<%--全部 --%>
											<c:forEach items="${command.custSysFnct}" var="item">
												<option value="${item.id.fnctId}" <c:if test="${command.qFnctId eq item.id.fnctId}"> selected</c:if>><c:out value="${item.id.fnctId}"/>-<fmt:message key="function.cust.Id.${item.id.fnctId}" /></option>
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
				<c:if test="${not command.initFlag}">
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
											<%--操作日期時間 --%>
											<fmt:message key="F0903.field.oprtDttm" />
										</td>
										<td width="150px;">
											<%--功能名稱 --%>
											<fmt:message key="F0903.field.fnctName" />
										</td>
										<td width="100px;">
											<%--操作人員代號 --%>
											<fmt:message key="F0903.field.userId" />
										</td>
										<td width="100px;">
											<%--執行動作 --%>
											<fmt:message key="F0903.field.action" />
										</td>
										<td width="100px;">
											<%--操作結果 --%>
											<fmt:message key="F0903.field.rslt" />
										</td>
										<td width="120px;">
											<%--查詢選項 --%>
											<fmt:message key="F0903.field.queryOption" />
										</td>
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
											<td>${item.fnctId}-<fmt:message key="function.cust.Id.${item.fnctId}" />
											</td>
											<td>${item.userId}</td>
											<td>
												<fmt:message key="operate.cust.Id.${item.action}" />
											</td>
											<td align="center">
												<fmt:message key="F0903.field.rslt.${item.rslt}" />
											</td>
											<td align="center" style="color: blue;">
												<c:choose>
													<c:when test="${item.action =='E4'}">
													
													</c:when>
													<c:when test="${not empty item.beforeId or not empty item.afterId}">
														<a href="#" onclick="btnDetail('<c:out value="${item.logNo}"/>')" style="color: blue;">
															<fmt:message key="common.btn.ModifyData" />
														</a>
													<%--異動資料--%>
													</c:when>
												</c:choose>
											</td>
										</tr>
									</c:forEach>
								</table>
							</form>
							<!-- Paging area ------------------------------------------------------------------------>
							<t:Page action="../0903/pageQuery.html" btnName="GO" />
							<!-- Button area ------------------------------------------------------------------------>
						</c:otherwise>
					</c:choose>
				</c:if>
			</div>
		</div>
		<div class="footer_line"></div>
	</div>
</body>
</html>
