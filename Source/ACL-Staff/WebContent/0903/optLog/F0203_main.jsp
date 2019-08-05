
<%
/*
 * @(#)optLog/F0203_main.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 會員操作記錄查詢 - 自訂交易限額 - 限額設定
 *
 * Modify History:
 * v1.00, 2016/06/27, Yann
 *  1) First Release
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

<style>
.mainContent {
	text-align: center;
	height: 350px;
}
</style>

<script>
	$(function() {
		var y_position, x_position;
		$("#before").scroll(function() {
			y_position = $("#before").scrollTop();
			$("#after").scrollTop(y_position);
		});
		$("#after").scroll(function() {
			y_position = $("#after").scrollTop();
			$("#before").scrollTop(y_position);
		});
		$("#before").scroll(function() {
			x_position = $("#before").scrollLeft();
			$("#after").scrollLeft(x_position);
		});
		$("#after").scroll(function() {
			x_position = $("#after").scrollLeft();
			$("#before").scrollLeft(x_position);
		});
		
		$(".jqtransform").jqTransform();
		
		$("#btnBack").click(function() {
			$("#formDetl").attr("action", root + "/0903/pageQuery.html");
			$("#formDetl").submit();
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
					<fmt:message key='function.Id.${command.funcId}' />
					-
					<fmt:message key='function.cust.Id.${command.q_fnctId}' />
					>
					<fmt:message key='operate.cust.Id.${command.q_action}' />
				</div>
				<form id="form1"></form> <%--登出使用 --%>
				<form method="post" class="jqtransform" name="formDetl" id="formDetl" action="" style="margin: 0;">
					<input type="hidden" name="page.pageNo" value="${command.optCurrentPage}" /><!-- 記錄頁數 -->
					
					<div id="before" style="height: 300px; width: 485px; overflow-y: scroll; overflow-x: scroll; float: left;">
						<%@include file="F0203_B.jsp"%>
					</div>
					&nbsp;
					<div id="after" style="height: 300px; width: 485px; overflow-y: scroll; overflow-x: scroll; float: right;">
						<%@include file="F0203_A.jsp"%>
					</div>
				</form>
				<!-- Button area ------------------------------------------------------------------------>
				<div class="btnContent">
					<input id="btnBack" class="btnStyle" type="button" value="<fmt:message key="common.btn.Back"/>" />
				</div>
			</div>
		</div>
		<div class="footer_line"></div>
	</div>
</body>
</html>
