
<%
	/*
	 * @(#)0401/01.jsp
	 *
	 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
	 *
	 * Description: 最新消息公告設定-新增公告
	 *
	 * Modify History:
	 * v1.00, 2016/06/08, Jimmy Yen
	 * 	1)First Release
	 * 
	 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>帳號連結扣款(Account Link)系統</title>

<%-- include Header, footer and menu --%>
<%@include file="/include/container.jsp"%>

<script>
	$(function() {
<%-- 日期選單 --%>
	datePicker(root);
<%-- validate --%>
	$("#form1").validate({
			rules : {
				aBgnDate : {
					required : true,
					DATE_FRMT_CHECKER : true
				},
				aEndDate : {
					required : true,
					DATE_FRMT_CHECKER : true,
					DATE_COMPARE : {
						targetDate : 'input[name="aBgnDate"]',
						// 驗證規則: B-預設日不可小於目標日  L-預設日不可大於目標日 E-預設日不可等於目標日
						compareType : 'B'
					}
				},
				qTitle : {
					required : true
				},
				content : {
					required : true
				}
			},
			messages : {
				aBgnDate : {
					required : '<fmt:message key="message.alert.startDate" /><fmt:message key="message.alert.notNull" />',
					DATE_FRMT_CHECKER : '<fmt:message key="message.alert.startDate" /><fmt:message key="message.alert.formateError" />'
				},
				aEndDate : {
					required : '<fmt:message key="message.alert.endDate" /><fmt:message key="message.alert.notNull" />',
					DATE_FRMT_CHECKER : '<fmt:message key="message.alert.endDate" /><fmt:message key="message.alert.formateError" />',
					DATE_COMPARE : '<fmt:message key="message.alert.dateCompare" />'
				},
				qTitle : {
					required : '<fmt:message key="F0401.field.title" /><fmt:message key="message.alert.notNull" />'
				},
				content : {
					required : '<fmt:message key="F0401.table.content" /><fmt:message key="message.alert.notNull" />'
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
<%-- CKEDITOR --%>
	CKEDITOR.replace('content', {filebrowserUploadUrl : './uploadImg.html?sessionKey=${loginUser.sessionId}'});

<%-- 確定 --%>
	$("#btnOK").click(function() {
			if ($("#form1").valid()) {
				$("#form1").attr("action", root + "/0401/insert.html");
				$("#form1").submit();
			}
		});
<%-- 取消 --%>
	$("#btnCancel").click(function() {
			$("#form1").attr("action", root + "/0401/queryInit.html");
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
					<fmt:message key="function.Id.F0401" />
					<c:out value=">" />
					<fmt:message key="F0401.field.new" />
				</div>
				<form action="#" id="form1" method="post">
					<table class="fxdTable" width="100%">
						<tr class="secondaryTitleRow">
							<td colspan="2">
								<%--  新增公告 --%>
								<fmt:message key="F0401.field.insertNews" />
							</td>
						</tr>
						<tr class="dataRowOdd">
							<td width="100px">
								<%-- 公告日期 --%>
								<fmt:message key="F0401.field.newsDate" />
								*
							</td>
							<td>
								<input type="text" maxlength="10" name="aBgnDate" datePicker="true" />
								~
								<input type="text" maxlength="10" name="aEndDate" datePicker="true" />
							</td>
						</tr>
						<tr class="dataRowEven">
							<td>
								<%-- 公告類型 --%>
								<fmt:message key="F0401.field.type" />
								*
							</td>
							<td>
								<select name="type">
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
								<%-- 置頂 --%>
								<fmt:message key="F0401.field.serl" />
								*
							</td>
							<td>
								<input type="radio" name="serl" value="99" checked="checked">
								<%-- 否 --%>
								<fmt:message key="F0401.field.serl.N" />
								<input type="radio" name="serl" value="1">
								<%-- 是 --%>
								<fmt:message key="F0401.field.serl.Y" />
							</td>
						</tr>
						<tr class="dataRowEven">
							<td>
								<%-- 公告標題 --%>
								<fmt:message key="F0401.field.title" />
								*
							</td>
							<td>
								<input type="text" size="50" name="qTitle" maxlength="50"/>
								<span class="helperText">
									&nbsp;&nbsp;
									<%-- 系統禁止輸入特殊字元包含 #, $, %, ^, *, ', ", TAB鍵(\t) --%>
									<fmt:message key="F0401.field.warm" />
								</span>
							</td>
						</tr>
						<tr class="dataRowOdd">
							<td>
								<%-- 公告內容 --%>
								<fmt:message key="F0401.table.content" />
								*
							</td>
							<td>
								<textarea name="content"></textarea>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<!-- Button area ------------------------------------------------------------------------>
			<div class="btnContent">
				<%-- 確認 --%>
				<input class="btnStyle" type="button" name="btnOK" id="btnOK" value="<fmt:message key="common.btn.OK" />" />
				&nbsp;
				<%-- 取消 --%>
				<input class="btnStyle" type="button" name="btnCancel" id="btnCancel" value="<fmt:message key="common.btn.Cancel" />" />
				&nbsp;
			</div>
		</div>
	</div>
	<!-- Footer ============================================================================================== -->
	<div class="footer_line"></div>
</body>
</html>
