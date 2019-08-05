
<%
	/*
	 * @(#)0401/00_0.jsp
	 *
	 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
	 *
	 * Description: 進入功能後預設顯示交易結果
	 *
	 * Modify History:
	 * v1.00, 2016/06/06, Jimmy
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
<%--validate--%>
	$("#form1").validate({
			rules : {
				qBgnDate : {
					required : true,
					DATE_FRMT_CHECKER : true
				},
				qEndDate : {
					required : true,
					DATE_FRMT_CHECKER : true,
					DATE_COMPARE : {
						targetDate : 'input[name="qBgnDate"]',
						// 驗證規則: B-預設日不可小於目標日  L-預設日不可大於目標日 E-預設日不可等於目標日
						compareType : 'B'
					},
					RANGE_VALIDATE : {
						dateStart : '#qBgnDate',
						limt : '<c:out value="${command.queryLimt}"/>'
					}
				}
			},
			messages : {
				qBgnDate : {
					required : '<fmt:message key="message.alert.startDate" /><fmt:message key="message.alert.notNull" />',
					DATE_FRMT_CHECKER : '<fmt:message key="message.alert.startDate" /><fmt:message key="message.alert.formateError" />'
				},
				qEndDate : {
					required : '<fmt:message key="message.alert.endDate" /><fmt:message key="message.alert.notNull" />',
					DATE_FRMT_CHECKER : '<fmt:message key="message.alert.endDate" /><fmt:message key="message.alert.formateError" />',
					DATE_COMPARE : '<fmt:message key="message.alert.dateCompare" />',
					RANGE_VALIDATE : '<fmt:message key="message.alert.dateOutOfRange" />'
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

		$("#form2").validate({
			rules : {
				qSeq : {
					required : true
				}
			},
			messages : {
				qSeq : {
					required : "<fmt:message key="message.alert.mustToSelect" />"
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
<%--查詢條件--%>
	$("#accordion").accordion({
			heightStyle : "content",
			collapsible : true
		}).children(".ui-accordion .ui-accordion-content").css("padding", "0px");
<%--查詢--%>
	$("#btnQuery").click(function() {
			if ($("#form1").valid()) {
				$("#form1").attr("action", root + "/0401/query.html");
				$("#form1").submit();
			}
		});
<%--新增--%>
	$("#btnNew").click(function() {
			$("#form1").attr("action", root + "/0401/insertInit.html");
			$("#form1").submit();
		});
<%--修改--%>
	$("#btnModify").click(function() {
			if ($("#form2").valid()) {
				var checkedItems = $("input[name=qSeq]:checked").length;
				if (checkedItems > 1) {
					alert("<fmt:message key="message.alert.onlyModifyOne"/>");
				} else {
					$("#form2").attr("action", root + "/0401/modifyInit.html");
					$("#form2").submit();
				}
			}
		});
<%--刪除--%>
	$("#btnDelete").click(function() {
			if ($("#form2").valid()) {
				if (confirm("<fmt:message key="message.alert.deleteConfirm"/>")) {
					$("#form2").attr("action", root + "/0401/delete.html");
					$("#form2").submit();
				}
			}
		});
<%--查詢條件保留--%>
	selectType();
<%-- preview --%>
	$("a[name=preview]").click(function() {
			$("div#newsDialog").remove();

			var getSeq = $(this).parent().parent().find("input[type=checkbox]").val();

			aclAjax("/ajax_getNewsMsg.html", "html", {
				seq : getSeq
			}, function(msg) {
				$("<div/>").prependTo("body").attr({
					id : "newsDialog"
				}).css("display", "none");

				$("#newsDialog").html(msg);
				$("#newsDialog").dialog({
					resizable : false,
					height : 500,
					width : 800,
					modal : true,
					title : "<fmt:message key="message.dialog.newsPreview" />", //預覽公告
					buttons : {
						"<fmt:message key="message.dialog.ok" />" : function() {
							$("#dialogBlock table").remove();
							$(this).dialog("close");
						}
					}
				});
			});
		});
	});

	function selectType() {
		var rtnType = "<c:out value="${command.qType}"/>"

		var optioins = $("select[name=qType] option");

		for (var i = 0; i < optioins.length; i++) {
			var option = $(optioins[i]);

			if (option.val() == rtnType) {
				option.prop('selected', true);
			}
		}
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
					<fmt:message key="function.Id.F0401" />
				</div>
				<%-- 查詢條件區 --%>
				<div id="accordion">
					<h3>
						<%--查詢條件--%>
						<fmt:message key="common.queryCondition" />
					</h3>
					<div style="width: 978px;">
						<form method="post" name="form1" id="form1" action="#" style="margin: 0;">
							<table class="fxdTable" style="width: 100%;">
								<tr class="dataRowOdd">
									<td width="150px">
										<%-- 公告日期 --%>
										<fmt:message key="F0401.field.newsDate" />
										*
									</td>
									<td>
										<input type="text" size="10" maxlength="10" name="qBgnDate" id="qBgnDate" value="<c:out value="${command.qBgnDate}" />" datePicker="true" />
										~
										<input type="text" size="10" maxlength="10" name="qEndDate" id="qEndDate" value="<c:out value="${command.qEndDate}" />" datePicker="true" />
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
										<%-- 公告類型 --%>
										<fmt:message key="F0401.field.type" />
									</td>
									<td>
										<select name="qType" id="">
											<%-- 全部 --%>
											<option value=""><fmt:message key="F0401.field.type.all" /></option>
											<%-- 最新消息 --%>
											<option value="A"><fmt:message key="F0401.field.type.A" /></option>
											<%-- 優惠活動 --%>
											<option value="B"><fmt:message key="F0401.field.type.B" /></option>
											<%-- 重要公告 --%>
											<option value="C"><fmt:message key="F0401.field.type.C" /></option>
											<%-- 其他 --%>
											<option value="D"><fmt:message key="F0401.field.type.D" /></option>
										</select>
									</td>
								</tr>
								<tr class="dataRowOdd">
									<td>
										<%-- 公告標題 --%>
										<fmt:message key="F0401.field.title" />
									</td>
									<td>
										<input name="qTitle" type="text" maxlength="50" value="<c:out value="${command.qTitle}" />"/>
									</td>
								</tr>
							</table>
						</form>
						<div class="btnContent">
							<%-- 查詢 --%>
							<input class="btnStyle" type="button" name="btn1" id="btnQuery" value="<fmt:message key="common.btn.Query" />" />
							&nbsp;
						</div>
					</div>
				</div>
				<!-- 查詢結果 -->
				<c:choose>
					<c:when test="${not empty result}">
						<form method="post" name="form2" id="form2" action="#" style="margin: 0;">
							<table class="fxdTable" style="width: 100%;">
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
								<c:forEach items="${result}" var="newsData" varStatus="i">
									<c:choose>
										<c:when test="${i.count%2 ne 0}">
											<tr class="dataRowOdd" align="center">
										</c:when>
										<c:otherwise>
											<tr class="dataRowEven" align="center">
										</c:otherwise>
									</c:choose>
									<td>
										<input type="checkbox" name="qSeq" value="${newsData.seq}">
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
						</form>
						<t:Page action="../0401/changePage.html" btnName="GO" />
						<!-- Button area ------------------------------------------------------------------------>
						<div class="btnContent">
							<%-- 新增 --%>
							<input class="btnStyle" type="button" name="btnNew" id="btnNew" value="<fmt:message key="common.btn.Add" />" />
							&nbsp;
							<%-- 修改 --%>
							<input class="btnStyle" type="button" name="btnModify" id=btnModify value="<fmt:message key="common.btn.Modify" />" />
							&nbsp;
							<%-- 刪除 --%>
							<input class="btnStyle" type="button" name="btnDelete" id="btnDelete" value="<fmt:message key="common.btn.Delete" />" />
							&nbsp;
						</div>
					</c:when>
					<c:otherwise>
						<div class="btnContent">
							<%-- 新增 --%>
							<input class="btnStyle" type="button" name="btnNew" id="btnNew" value="<fmt:message key="common.btn.Add" />" />
							&nbsp;
							<%-- 修改 --%>
							<input class="btnStyle" type="button" name="btnModify" id=btnModify value="<fmt:message key="common.btn.Modify" />" />
							&nbsp;
							<%-- 刪除 --%>
							<input class="btnStyle" type="button" name="btnDelete" id="btnDelete" value="<fmt:message key="common.btn.Delete" />" />
							&nbsp;
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
	<div class="footer_line"></div>
</body>
</html>