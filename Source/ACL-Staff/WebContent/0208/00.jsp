
<%
/*
 * @(#)0208/01.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 會員連結設定記錄查詢
 *
 * Modify History:
 * v1.00, 2016/02/05, Evan
 * 	1)First Release
 * v1.01, 2016/06/13, Yann
 *  1)不join CustData
 * v1.02, 2016/12/29, Eason Hsu
 *   1) TSBACL-144, 網銀 & 晶片卡認證失敗, 錯誤訊息 Mapping 調整
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

<script type="text/javascript">
	$(function() {

		datePicker(root);
		$("#accordion").accordion({
			heightStyle : "content",
			collapsible : true
		}).children(".ui-accordion .ui-accordion-content").css("padding", "0px");

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
						targetDate : '#strtDate',
						// 驗證規則: B-預設日不可小於目標日  L-預設日不可大於目標日 E-預設日不可等於目標日
						compareType : 'B'
					},
					RANGE_VALIDATE : {
						dateStart : '#strtDate',
						limt : '${command.sysParam}'
					}
				},
				custId : {
					ACL_REQUIRE : {
						atLast : 1,
						//name of field
						fields : [ "custId", "realAcnt", "ecUser" ]
					},
					ALPHA_CHECKER : true
				},
				realAcnt : {
					NUM_CHECKER : {
						isNum : 2
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
					DATE_COMPARE : '<fmt:message key="message.alert.dateCompare" />',
					RANGE_VALIDATE : '<fmt:message key="message.alert.dateOutOfRange" />'
				},
				custId : {
					ACL_REQUIRE : '<fmt:message key="message.memo.Memo3" />',
					ALPHA_CHECKER : "<fmt:message key="common.queryCondition.idNo" /><fmt:message key="message.alert.onlyEN&NUM" />"
				},
				realAcnt : {
					NUM_CHECKER : "<fmt:message key="F0208.field.realAcnt" /><fmt:message key="message.alert.onlyNUM" />"
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

		$("#accordion").accordion({
			heightStyle : "content",
			collapsible : true
		}).children(".ui-accordion .ui-accordion-content").css("padding", "0px");
<%-- 針對"查詢類別"選項 enable or disable"執行結果" --%>
	$("input[name=queryType]:eq(0),input[name=queryType]:eq(1),input[name=queryType]:eq(2)").click(function() {
			$("select[name=stts]").prop("disabled", "");
		});

		$("input[name=queryType]:eq(3),input[name=queryType]:eq(4),input[name=queryType]:eq(5)").click(function() {
			$("select[name=stts]>option:eq(0)").prop("selected", "true").parent().prop("disabled", "true");
		});

		if ($("input[name=queryType]:eq(3),input[name=queryType]:eq(4),input[name=queryType]:eq(5)").is(":checked")) {
			$("select[name=stts]").prop("disabled", "true");
		}
	});
<%-- 透過 ajax 取得錯誤原因並以燈箱效果顯示 --%>
	function openDiaglog(code) {

		aclAjax("/ajax_getTbCodeDesc.html", "html", {
			codeId : code
		}, function(msg) {
			$("#dialogBlock").html(msg);
			$("#dialogBlock").dialog({
				resizable : false,
				height : "auto",
				width : 400,
				modal : true,
				title : "<fmt:message key="message.dialog.title" />", //訊息內容
				buttons : {
					"<fmt:message key="message.dialog.ok" />" : function() {
						$("#dialogBlock table").remove();
						$(this).dialog("close");
					}
				}
			});
		});

		$(".pageContent").css("width", "950px");
	}

	function send() {
		if ($("#form1").valid()) {
			$("#form1").attr("action", root + "/0208/query.html");
			$("#form1").submit();
		}
	}
	//列印
	function doPrint() {
		$('#btnPrint').printPreview();
	}
	
</script>
<script type="text/javascript" src="<%=root%>/js/queryPage.js"></script>

</head>
<body>
	<!-- Container ============================================================================================== -->
	<div class="container">
		<!-- Content ------------------------------------------------------------------------>
		<div class="mainContent">
			<div class="content">
				<div class="fnctTitle">
					<fmt:message key="function.Id.F0208" />
					<%--會員連結設定記錄 --%>
				</div>
				<div id="accordion">
					<h3>
						<fmt:message key="common.queryCondition" />
						<%--查詢條件 --%>
					</h3>
					<div style="width: 978px;">
						<form id="form1" name="form1" action="" method="post" tyle="margin: 0;">
							<table class="fxdTable" width="100%">
								<tr class="dataRowOdd">
									<td width="150px">
										<fmt:message key="common.queryCondition.queryDate" />
										*
									</td>
									<%--查詢日期 --%>
									<td>
										<input type="text" size="10" maxlength="10" name="strtDate" id="strtDate" value="${command.strtDate}" datePicker="true" />
										~
										<input type="text" size="10" maxlength="10" name="endDate" id="endDate" value="${command.endDate}" datePicker="true" />
										<span class="helperText">
											&nbsp;&nbsp;
											<fmt:message key="message.memo.Memo1" />
											<c:set var="theString" value="${command.sysParam}" />
											<c:if test="${fn:endsWith(theString, 'M')}">
												<c:set var="string1" value="${command.sysParam}" />
												<c:set var="string2" value="${fn:split(string1, 'M')}" />
												<c:out value="${string2[0]}" />
												<fmt:message key="message.memo.Memo4" />
											</c:if>
											<c:if test="${fn:endsWith(theString, 'D')}">
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
										<fmt:message key="F0208.field.queryType" />
										*
									</td>
									<%--查詢類別 --%>
									<td>
										<%--全部 --%>
										<input type="radio" name="queryType" value="00" checked />
										<fmt:message key="F0208.field.ALL" />
										<%--連結綁定 --%>
										&nbsp;
										<input type="radio" name="queryType" value="01" <c:if test="${command.queryType eq '01'}">checked</c:if> />
										<fmt:message key="F0208.field.binding" />
										<%--取消綁定 --%>
										&nbsp;
										<input type="radio" name="queryType" value="02" <c:if test="${command.queryType eq '02'}">checked</c:if> />
										<fmt:message key="F0208.field.unbinding" />
										<%--啟用 --%>
										&nbsp;
										<input type="radio" name="queryType" value="04" <c:if test="${command.queryType eq '04'}">checked</c:if> />
										<fmt:message key="F0208.field.enable" />
										<%--暫停 --%>
										&nbsp;
										<input type="radio" name="queryType" value="05" <c:if test="${command.queryType eq '05'}">checked</c:if> />
										<fmt:message key="F0208.field.pause" />
										<%--終止 --%>
										&nbsp;
										<input type="radio" name="queryType" value="06" <c:if test="${command.queryType eq '06'}">checked</c:if> />
										<fmt:message key="F0208.field.stop" />
										&nbsp;
									</td>
								</tr>
								<tr class="dataRowOdd">
									<td>
										<fmt:message key="common.queryCondition.idNo" />
									</td>
									<%--身分證字號 --%>
									<td>
										<input type="text" maxlength="11" name="custId" value="${command.custId}" style="text-transform: uppercase" />
										<span class="helperText">
											&nbsp;&nbsp;
											<fmt:message key="message.memo.Memo3" />
											<%--身分證字號、銀行存款帳號請擇一輸入--%>
										</span>
									</td>
								</tr>
								<tr class="dataRowEven">
									<td>
										<fmt:message key="F0208.field.realAcnt" />
									</td>
									<%--實體帳號 --%>
									<td>
										<input type="text" maxlength="16" name="realAcnt" value="${command.realAcnt}" />
									</td>
								</tr>
								<tr class="dataRowOdd">
									<td>
										<%--電商平台會員代號--%>
										<fmt:message key="common.queryCondition.ecUser" />
									</td>
									<td>
										<input class="requiredGroup" type="text" maxlength="12" name="ecUser" value="<c:out value="${command.ecUser}" />" />
									</td>
								</tr>
								<tr class="dataRowEven">
									<td>
										<fmt:message key="common.queryCondition.ecId" />
									</td>
									<%--平台代號 --%>
									<td>
										<select name="ecId">
											<option value="" selected><fmt:message key="F0208.field.ALL" /></option>
											<%--全部 --%>
											<c:forEach items="${command.ecData}" var="item">
												<option value="${item.ecId}" <c:if test="${command.ecId eq item.ecId}"> selected</c:if>>${item.ecId}-${item.ecNameCh}</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr class="dataRowOdd">
									<td>
										<fmt:message key="F0208.field.result" />
									</td>
									<%--執行結果 --%>
									<td>
										<select name="stts">
											<option value="00" selected><fmt:message key="F0208.field.ALL" /></option>
											<%--全部 --%>
											<option value="01" <c:if test="${command.stts eq '01'}"> selected</c:if>><fmt:message key="F0208.field.STTS.Y" /></option>
											<%--成功 --%>
											<option value="02" <c:if test="${command.stts eq '02'}"> selected</c:if>><fmt:message key="F0208.field.STTS.N" /></option>
											<%--失敗 --%>
										</select>
									</td>
								</tr>
								<tr class="dataRowEven">
									<td>
										<%-- 執行來源 --%>
										<fmt:message key="common.queryCondition.execSrc" />
									</td>

									<td>
										<select name="execSrc">
											<%--全部 --%>
											<option value="" selected><fmt:message key="F0208.field.ALL" /></option>
											<option value="A" <c:if test="${command.execSrc eq 'A'}"> selected</c:if>><fmt:message key="F0208.field.gateway" /></option>
											<%--<option value="B" <c:if test="${command.execSrc eq 'B'}"> selected</c:if>><fmt:message key="F0208.field.staff" /></option>--%>
											<option value="C" <c:if test="${command.execSrc eq 'C'}"> selected</c:if>><fmt:message key="F0208.field.cust" /></option>
											<option value="D" <c:if test="${command.execSrc eq 'D'}"> selected</c:if>><fmt:message key="F0208.field.system" /></option>
										</select>
									</td>
								</tr>
							</table>
						</form>
						<!-- Button area ------------------------------------------------------------------------>
						<div class="btnContent">
							<input class="btnStyle" type="button" name="btn1" value="<fmt:message key="common.btn.Query" />" <%--查詢--%> onclick="send();" />
							&nbsp;
						</div>
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
							<div id="printArea">
								<table class="fxdTable" width="100%">
									<tr class="titleRow">
										<%--身分證字號 --%>
										<td rowspan="2">
											<fmt:message key="F0208.field.custId" />
										</td>
										<%--電商平台 --%>
										<td>
											<fmt:message key="F0208.field.ecName" />
										</td>
										<%--實體帳號 --%>
										<td>
											<fmt:message key="F0208.field.realAcnt" />
										</td>
										<%--身分認證 --%>
										<td>
											<fmt:message key="F0208.field.gradeType" />
										</td>
										<%--等級 --%>
										<td rowspan="2">
											<fmt:message key="F0208.field.grade" />
										</td>
										<%--執行日期 --%>
										<td rowspan="2">
											<fmt:message key="F0208.field.excuteDate" />
										</td>
										<%--執行結果 --%>
										<td rowspan="2">
											<fmt:message key="F0208.field.result" />
										</td>
										<%--執行來源 --%>
										<td rowspan="2">
											<fmt:message key="F0208.field.execSrc" />
										</td>
										<%--錯誤原因 --%>
										<td>
											<fmt:message key="F0208.field.errorMsg" />
										</td>
										<%--會員服務序號 --%>
										<td>
											<fmt:message key="F0208.field.custSerl" />
										</td>
									</tr>
									<tr class="titleRow">
										<%--平台會員代號--%>
										<td>
											<fmt:message key="F0208.field.ecUser" />
										</td>
										<%--帳號識別碼--%>
										<td>
											<fmt:message key="F0208.field.acntIndt" />
										</td>
										<%--IP位置--%>
										<td>
											<fmt:message key="F0208.field.ip" />
										</td>
										<%--主機訊息--%>
										<td>
											<fmt:message key="F0208.field.hostCode" />
										</td>
										<%--平台訊息序號--%>
										<td>
											<fmt:message key="F0208.field.ecMsgNo" />
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
											<td align="center" rowspan="2">${item.custId}
												<br>${command.custName}</td> <%--v1.01--%>
											<td align="center">${item.ecNameCh}</td>
											<td align="center">
												<aclFn:realAcntFormate realAcnt="${item.realAcnt}" />
											</td>
											<td align="center">
												<c:if test="${not empty item.gradType}">
													<fmt:message key="F0208.field.queryType.${item.gradType}" />
												</c:if>
											</td>
											<td align="center" rowspan="2">${item.grad}</td>
											<td align="center" rowspan="2">
												<fmt:formatDate value="${item.cretDttm}" pattern="yyyy/MM/dd HH:mm:ss" />
											</td>
											<td align="center" rowspan="2">
												<fmt:message key="F0208.field.STTS.${item.stts}" />
											</td>
											<td align="center" rowspan="2">
												<c:if test="${item.execSrc=='A'}">
													<fmt:message key="F0208.field.gateway" />
												</c:if>
												<c:if test="${item.execSrc=='B'}">
													<fmt:message key="F0208.field.staff" />
												</c:if>
												<c:if test="${item.execSrc=='C'}">
													<c:out value="${item.execUser}"></c:out>
												</c:if>
												<c:if test="${item.execSrc=='D'}">
													<fmt:message key="F0208.field.system" />
												</c:if>
											</td>
											<td align="center">
												<c:if test="${item.stts eq '01' or item.stts eq '03'}">
													<c:choose>
														<c:when test="${not empty item.errCode}">
															<a href="#" onClick="openDiaglog('01-'+'${item.errCode}');">${item.errCode}</a>
														</c:when>
														<c:otherwise>&nbsp;</c:otherwise>
													</c:choose>
												</c:if>
											</td>
											<td align="center">${item.custSerl}</td>
										</tr>
										<tr class="${cssClass}">
											<td align="center">
												<c:choose>
													<c:when test="${not empty item.ecUser}">
														<c:out value="${item.ecUser}"/>
													</c:when>
													<c:otherwise>&nbsp;</c:otherwise>
												</c:choose>
											</td>
											<td align="center">
												<c:out value="${item.acntIndt}" default="　" />
											</td>
											<td align="center">
												<c:out value="${item.ip}" default="　" />
											</td>
											<td align="center">
												<c:if test="${item.stts eq '01' or item.stts eq '03'}">
													<c:choose>
														<c:when test="${not empty item.hostCode}">
															<%-- v1.02 --%>
															<c:choose>
																<%-- 網銀系統訊息 --%>
																<c:when test="${fn:startsWith(item.hostCode, 'E') }">
																	<a href="#" onClick="openDiaglog('03-'+'${item.hostCode}');">${item.hostCode}</a>
																	
																</c:when>
																<%-- 網銀系統訊息 --%>
																<c:otherwise>
																	<a href="#" onClick="openDiaglog('02-'+'${item.hostCode}');">${item.hostCode}</a>
																	
																</c:otherwise>
															</c:choose>
														</c:when>
														<c:otherwise>&nbsp;</c:otherwise>
													</c:choose>
												</c:if>

											</td>
											<td align="center">
												<c:choose>
													<c:when test="${not empty item.ecMsgNo}">
														<c:out value="${item.ecMsgNo}"/>
													</c:when>
													<c:otherwise>&nbsp;</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:forEach>
								</table>
							</div>
							<%-- Paging area --%>
							<t:Page action="../0208/requery.html" btnName="GO" />
							<%-- Button area --%>
							<div class="btnContent">
								<%--列印--%>
								<input class="btnStyle" type="button" id="btnPrint" name="btnPrint" value="<fmt:message key="common.btn.Print" />" onclick="doPrint();" />
								&nbsp;
							</div>
						</c:otherwise>
					</c:choose>
				</c:if>
			</div>
		</div>
		<div class="footer_line"></div>
	</div>
</body>
</html>
