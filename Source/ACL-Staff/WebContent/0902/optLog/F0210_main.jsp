
<%
	/*
	 * @(#)log/F0210.jsp
	 *
	 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
	 *
	 * Description: 操作記錄查詢 - 未綁定解鎖
	 *
	 * Modify History:
	 * v1.00, 2016/12/21, Yann
	 *  1) TSBACL-143, First Release
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

		$("ul.tabs li").addClass('active');

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

		$("#btnBack").click(function() {
			$("#formDetl").attr("action", root + "/0902/requery.html");
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
					<fmt:message key='function.Id.${command.q_fnctId}' />
					-
					<fmt:message key='operate.Id.${command.q_action}' />
				</div>
				<form id="form1"></form> <%--登出使用 --%>
				<form method="post" name="formDetl" id="formDetl" action="" style="margin: 0;">
					<input type="hidden" name="page.pageNo" value="${command.optCurrentPage}" /><!-- 記錄頁數 -->
					<div id="before" style="height: 350px; width: 485px; overflow-y: scroll; overflow-x: scroll; float: left;">
						<%@include file="F0210_B.jsp"%>
					</div>
					&nbsp;
					<div id="after" style="height: 350px; width: 485px; overflow-y: scroll; overflow-x: scroll; float: right;">
						<c:if test="${command.q_action ne 'E3'}">
							
						</c:if>
					</div>

					<!-- Button area ------------------------------------------------------------------------>
					<div class="btnContent">
						<input id="btnBack" class="btnStyle" type="button" value="<fmt:message key="common.btn.Back"/>" />
					</div>
				</form>
			</div>
		</div>
		<div class="footer_line"></div>
	</div>
</body>
</html>
