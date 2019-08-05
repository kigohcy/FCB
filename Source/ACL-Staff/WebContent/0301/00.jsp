
<%
	/*
	 * @(#)0301/00.jsp
	 *
	 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
	 *
	 * Description: 交易結果查詢 
	 *
	 * Modify History:
	 * v1.00, 2016/02/17, Jimmy
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

<style type="text/css">
.pageContent {
	width: 950px;
}
</style>

<script type="text/javascript" src="<%=root%>/js/queryPage.js"></script>
<script>
	$(function() {

<%--日期選單--%>
	datePicker(root);
<%--查詢條件--%>
	$("#accordion").accordion({
			heightStyle : "content",
			collapsible : true
		}).children(".ui-accordion .ui-accordion-content").css("padding", "0px");
<%--表單驗證--%>
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
					},
					RANGE_VALIDATE : {
						dateStart : '#strtDate',
						limt : '<c:out value="${command.queryLimt}"/>'
					}
				},
				custId : {
					//ACL_REQUIRE : {
					//	atLast : 1,
					//	//name of field
					//	fields : [ "custId", "realAcnt", "ecUser" ]
					//},
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
					//ACL_REQUIRE : '<fmt:message key="message.memo.Memo2" />',
					ALPHA_CHECKER : "<fmt:message key="common.queryCondition.idNo" /><fmt:message key="message.alert.onlyEN&NUM" />"
				},
				realAcnt : {
					NUM_CHECKER : "<fmt:message key="common.queryCondition.realAcnt" /><fmt:message key="message.alert.onlyNUM" />"
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
				$("#form1").attr("action", root + "/0301/query.html");
				$("#form1").submit();
			}
		});

	
	});
<%-- 透過 ajax 取得錯誤原因並以燈箱效果顯示 --%>
	function openDiaglog(code,tita,tota) {

		aclAjax("/ajax_getTbCodeDesc.html", "html", {
			codeId : code
		}, function(msg) {
			$("#dialogBlock").empty();
			$("#dialogBlock").html(msg);
			tita='<xmp>'+tita+'</xmp>';
			
			if (tita != '<xmp></xmp>' && code.substr(0,3)=='02-'){
				tota='<xmp>'+tota+'</xmp>';
				var titaTitle=$("#titaTitle").val();
				var totaTitle=$("#totaTitle").val();
				//上行電文
				$("#dialogBlock").append('<BR><BR><table>');
				$("#dialogBlock").append('<tr><td class="titleRow"  style="text-align:left;font-size:12pt">&nbsp;&nbsp;'+titaTitle+'&nbsp;&nbsp;</td></tr><tr><td class="dataRowEven" style="text-align:left;font-size:12pt">'+tita+'</td></tr>');
				//下行電文
				$("#dialogBlock").append('<tr><td class="titleRow"  style="text-align:left;font-size:12pt">&nbsp;&nbsp;'+totaTitle+'&nbsp;&nbsp;</td></tr><tr><td class="dataRowEven" style="text-align:left;font-size:12pt">'+tota+'</td></tr>');
				$("#dialogBlock").append('</table>');
			}
			
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

	function doPrint() {
		$('#btnPrint').printPreview();
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
					<fmt:message key="function.Id.F0301" />
				</div>
				<%-- 查詢條件區 --%>
				<div id="accordion">
					<h3>
						<%--查詢條件--%>
						<fmt:message key="common.queryCondition" />
					</h3>
					<div style="width: 978px;">
						<form method="post" id=form1 name="form1" action="#" style="margin: 0;">
							<table class="fxdTable" style="width: 100%">
								<tr class="dataRowOdd">
									<td width="150px">
										<%--查詢日期--%>
										<fmt:message key="common.queryCondition.queryDate" />
										*
									</td>
									<td>
										<input type="text" size="10" maxlength="10" name="strtDate" id="strtDate" value="<c:out value="${command.strtDate}" />" datePicker="true" />
										~
										<input type="text" size="10" maxlength="10" name="endDate" value="<c:out value="${command.endDate}" />" datePicker="true" />
										<span class="helperText">&nbsp;&nbsp;<fmt:message key="message.memo.Memo1" /> <c:set var="theString" value="${command.queryLimt}" /> <c:if test="${fn:endsWith(theString, 'M')}">
												<c:set var="string1" value="${command.queryLimt}" />
												<c:set var="string2" value="${fn:split(string1, 'M')}" />
												<c:out value="${string2[0]}" />
												<fmt:message key="message.memo.Memo4" />
											</c:if> <c:if test="${fn:endsWith(theString, 'D')}">
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
										<%--身分證字號--%>
										<fmt:message key="common.queryCondition.idNo" />
									</td>
									<td>
										<input type="text" maxlength="11" name="custId" value="<c:out value="${command.custId}" />" style="text-transform: uppercase" />
										<!-- <span class="helperText">&nbsp;&nbsp;<fmt:message key="message.memo.Memo2" /> --> <%--身分證字號、銀行存款帳號、電商平台會員代號請擇一輸入--%><!-- </span> -->
									</td>
								</tr>
								<tr class="dataRowOdd">
									<td>
										<%--銀行存款帳號--%>
										<fmt:message key="common.queryCondition.realAcnt" />
									</td>
									<td>
										<input class="requiredGroup" type="text" maxlength="16" name="realAcnt" value="<c:out value="${command.realAcnt}" />" />
									</td>
								</tr>
								<tr class="dataRowEven">
									<td>
										<%--電商平台會員代號--%>
										<fmt:message key="common.queryCondition.ecUser" />
									</td>
									<td>
										<input class="requiredGroup" type="text" maxlength="12" name="ecUser" value="<c:out value="${command.ecUser}" />" />
									</td>
								</tr>
								<tr class="dataRowOdd">
									<td>
										<%--平台代號--%>
										<fmt:message key="common.queryCondition.ecId" />
									</td>
									<td>
										<select name="qEcId">
											<option value="" selected><fmt:message key="common.queryCondition.all" /><%--全部--%></option>
											<c:forEach items="${command.ecData}" var="ecData">
												<option value="<c:out value="${ecData.ecId}" />" <c:if test="${command.qEcId eq ecData.ecId}"> selected</c:if>><c:out value="${ecData.ecNameCh}" />&nbsp;
												</option>
											</c:forEach>
										</select>
									</td>
								</tr>

								<tr class="dataRowEven">
									<td>
										<%--交易類別--%>
										<fmt:message key="common.queryCondition.trnsType" />
									</td>
									<td>
										<select name="qTrnsType">
											<option value="" selected><fmt:message key="common.queryCondition.all" /><%--全部--%></option>
											<option value="A" <c:if test="${command.qTrnsType eq 'A'}"> selected</c:if>><fmt:message key="common.queryCondition.trnsType.debit" /><%--扣款--%></option>
											<option value="D" <c:if test="${command.qTrnsType eq 'D'}"> selected</c:if>><fmt:message key="common.queryCondition.trnsType.deposit" /><%--儲值交易--%></option>
											<option value="B" <c:if test="${command.qTrnsType eq 'B'}"> selected</c:if>><fmt:message key="common.queryCondition.trnsType.refund" /><%--退款--%></option>
											<option value="C" <c:if test="${command.qTrnsType eq 'C'}"> selected</c:if>><fmt:message key="common.queryCondition.trnsType.withdraw" /><%--提領--%></option>
											<option value="E" <c:if test="${command.qTrnsType eq 'E'}"> selected</c:if>><fmt:message key="common.queryCondition.trnsType.payTax" /><%--繳費稅--%></option>
										</select>
									</td>
								</tr>
								<tr class="dataRowOdd">
									<td>
										<%--交易類別--%>
										<fmt:message key="common.queryCondition.trnsStts" />
									</td>
									<td>
										<select name="qTrnsStts">
											<option value="" selected><fmt:message key="common.queryCondition.all" /><%--全部--%></option>
											<option value="01" <c:if test="${command.qTrnsStts eq '01'}"> selected</c:if>><fmt:message key="common.result.unknow" /><%--不明--%></option>
											<option value="02" <c:if test="${command.qTrnsStts eq '02'}"> selected</c:if>><fmt:message key="common.result.success" /><%--成功--%></option>
											<option value="03" <c:if test="${command.qTrnsStts eq '03'}"> selected</c:if>><fmt:message key="common.result.failure" /><%--失敗--%></option>
										</select>
									</td>
								</tr>
							</table>
						</form>
						<div class="btnContent">
							<input id="btnQuery" class="btnStyle" type="button" value="<fmt:message key="common.btn.Query" /><%--查詢--%>" />
							&nbsp;
						</div>
					</div>
				</div>
				<%-- 查詢結果區 --%>
				<c:if test="${command.initFlag eq false}">
					<c:choose>
						<c:when test="${not empty result}">
							<div id="printArea">
								<form method="post" name="form2" action="" style="margin: 0;">
									<table class="fxdTable" width="100%">
										<tr class="titleRow" align="center">
											<td>
												<%--交易日期時間--%>
												<fmt:message key="common.queryCondition.trnsDateTime" />
											</td>
											<td rowspan="2">
												<%--身分證字號 --%>
												<fmt:message key="common.queryCondition.idNo" />
											</td>
											<td>
												<%--電商平台--%>
												<fmt:message key="common.queryCondition.ecName" />
											</td>
											<td>
												<%--轉出帳號--%>
												<fmt:message key="common.queryCondition.turnOutAcnt" />
											</td>
	
											<td>
												<%--訂單編號--%>
												<fmt:message key="common.queryCondition.orderNo" />
											</td>
											<td>
												<%--交易類別--%>
												<fmt:message key="common.queryCondition.trnsType" />
											</td>
											<td rowspan="2">
												<%--交易金額--%>
												<fmt:message key="common.queryCondition.trnsAmount" />
											</td>
											<td>
												<%--錯誤原因--%>
												<fmt:message key="common.queryCondition.errReason" />
											</td>
											<td rowspan="2">
												<fmt:message key="common.queryCondition.serviceCode" />
											</td>
											<td>
												<%--帳號識別碼--%>
												<fmt:message key="common.queryCondition.acntIdentifier" />
											</td>
										</tr>
										<tr class="titleRow" align="center">
											<td>
												<fmt:message key="F0301.field.ip" />
											</td>
											<td>
												<fmt:message key="common.queryCondition.ecUser" />
											</td>
											<td>
												<%--轉入帳號--%>
												<fmt:message key="common.queryCondition.turnInAcnt" />
											</td>
											<td>
												<%--交易備註--%>
												<fmt:message key="common.queryCondition.trnsRemark" />
											</td>
											<td>
												<%--交易結果--%>
												<fmt:message key="common.queryCondition.trnsResult" />
											</td>
											<td>
												<%--主機訊息--%>
												<fmt:message key="common.queryCondition.hostCode" />
											</td>
											<td>
												<%--平台訊息序號--%>
												<fmt:message key="common.queryCondition.ecMsgId" />
											</td>
										</tr>
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
												<fmt:formatDate type="both" pattern="yyyy/MM/dd HH:mm:ss" value="${trnsData.trnsDttm}" />
											</td>
											<td align="center" rowspan="2">
												<c:out value="${trnsData.custId}" />
												&nbsp;<br>
												<c:out value="${trnsData.custName}" />
												&nbsp;
											</td>
											<td align="center">
												<c:out value="${trnsData.ecNameEn}" />
												&nbsp;
											</td>
											<td align="center">
												<aclFn:realAcntFormate realAcnt="${trnsData.realAcnt}" />
											</td>
											<td align="center">
												<c:out value="${trnsData.ordrNo}" />
												&nbsp;
											</td>
											<td align="center">
												<c:if test="${trnsData.trnsType eq 'A'}">
													<%--扣款--%>
													<fmt:message key="common.queryCondition.trnsType.debit" />
												</c:if>
												<c:if test="${trnsData.trnsType eq 'B'}">
													<%--退款--%>
													<fmt:message key="common.queryCondition.trnsType.refund" />
												</c:if>
												<c:if test="${trnsData.trnsType eq 'C'}">
													<%--提領--%>
													<fmt:message key="common.queryCondition.trnsType.withdraw" />
												</c:if>
												<c:if test="${trnsData.trnsType eq 'D'}">
													<%--儲值交易--%>
													<fmt:message key="common.queryCondition.trnsType.deposit" />
												</c:if>
												<c:if test="${trnsData.trnsType eq 'E'}">
													<%--繳費稅--%>
													<fmt:message key="common.queryCondition.trnsType.payTax" />
												</c:if>    
												   
											</td>
											<td align="right" rowspan="2">
												<fmt:formatNumber type="number" maxFractionDigits="3" value="${trnsData.trnsAmnt}" />
											</td>
											<td>
												<c:if test="${trnsData.trnsStts ne '02' }">
													<c:choose>
														<c:when test="${not empty trnsData.errCode }">
															<a href="#" onClick="openDiaglog('01-'+'<c:out value="${trnsData.errCode}" />','','');"> <c:out value="${trnsData.errCode}" /></a>
														</c:when>
														<c:otherwise>&nbsp;</c:otherwise>
													</c:choose>
												</c:if>
												
											</td>
											<td align="center" rowspan="2">
												<c:out value="${trnsData.custSerl}" />
												&nbsp;
											</td>
											<td align="center">
												<c:out value="${trnsData.acntIndt}" />
												&nbsp;
											</td>
											</tr>
											<c:choose>
												<c:when test="${i.count%2 ne 0}">
													<tr class="dataRowOdd">
												</c:when>
												<c:otherwise>
													<tr class="dataRowEven">
												</c:otherwise>
											</c:choose>
											<td align="center">
												<c:out value="${trnsData.ip}" default="　" />
											</td>
											<td align="center">
												<c:out value="${trnsData.ecUser}" />
												&nbsp;
											</td>
											<td align="center">
												<c:if test="${trnsData.trnsType eq 'A' || trnsData.trnsType eq 'E'}">
													<%--扣款--%>
													<c:out value="${trnsData.recvAcnt}" />
													&nbsp;
												</c:if>
												<c:if test="${trnsData.trnsType eq 'B' || trnsData.trnsType eq 'C' || trnsData.trnsType eq 'D'}">
													<%--退款, 儲值, 提領--%>
													<aclFn:realAcntFormate realAcnt="${trnsData.recvAcnt}" />
												</c:if>
											</td>
											<td align="center">${trnsData.trnsNote}</td>
											<td align="center">
												<c:if test="${trnsData.trnsStts eq '01'}">
													<%--不明--%>
													<fmt:message key="common.result.unknow" />
												</c:if>
												<c:if test="${trnsData.trnsStts eq '02'}">
													<%--成功--%>
													<fmt:message key="common.result.success" />
												</c:if>
												<c:if test="${trnsData.trnsStts eq '03'}">
													<%--失敗--%>
													<fmt:message key="common.result.failure" />
												</c:if>
											</td>
											<td>
												<c:if test="${trnsData.trnsStts ne '02'}">
													<c:choose>
														<c:when test="${not empty trnsData.hostCode}">
														    <input type="hidden" id="titaTitle" name="titaTitle" value='<fmt:message key="F0301.field.tiTa" />'  />
														    <input type="hidden" id="totaTitle" name="totaTitle" value='<fmt:message key="F0301.field.toTa" />' />
														    <%-- <input type="hidden" id="tita" name="tita" value='<xmp>${trnsData.tiTa}</xmp>' />
													        <input type="hidden" id="tota" name="tota" value='<xmp>${trnsData.toTa}</xmp>' /> --%>
															<a href="#" onClick="openDiaglog('02-'+'<c:out value="${trnsData.hostCode}" />','${fn:escapeXml(trnsData.tiTa)}','${fn:escapeXml(trnsData.toTa)}');"> <c:out value="${trnsData.hostCode}" /></a>
														</c:when>
														<c:otherwise>&nbsp;</c:otherwise>
													</c:choose>
												</c:if>
											</td>
											<td align="center">
												<c:out value="${trnsData.id.ecMsgNo}" />
												&nbsp;
											</td>
											</tr>
										</c:forEach>
									</table>
								</form>
							</div>
							<%-- Paging area --%>
							<t:Page action="../0301/changePage.html" btnName="GO" />
							<%-- Button area --%>
							<div class="btnContent">
								<%--列印--%>
								<input class="btnStyle" type="button" id="btnPrint" name="btnPrint" value="<fmt:message key="common.btn.Print" />" onclick="doPrint()" />
								&nbsp;
							</div>
						</c:when>
						<c:otherwise>
							<%--查無符合條件資料--%>
							<div class="noResult" align="center">
								<fmt:message key="message.sys.NoData" />
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