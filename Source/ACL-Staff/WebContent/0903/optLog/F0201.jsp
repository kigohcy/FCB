<%
/*
 * @(#) /0903/F2001.jsp
 *
 * Directions: 
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/06/29, Eason Hsu
 *    1) First release
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

<script type="text/javascript">

$(function(){
	$(".btnStyle").click(function() {
		$("#formDetl").attr("action", root + "/0903/pageQuery.html");
		$("#formDetl").submit();
	});
});

</script>

</head>
<body>
	<div class="container">
		<!-- Content ------------------------------------------------------------------------>
		<div class="mainContent">
			<form name="form1" id="form1" method="post"></form>
			<form name="formDetl" id="formDetl" method="post">
				<input type="hidden" name="page.pageNo" value="${command.optCurrentPage}" /><!-- 記錄頁數 -->
				<div class="content">
					<div class="fnctTitle">
						<fmt:message key='function.Id.${command.funcId}' /> - 
						<fmt:message key='function.cust.Id.${command.q_fnctId}' /> > 
						<fmt:message key='operate.cust.Id.${command.q_action}' />
					</div>
					<div style="border: 1px solid #DDD; padding-left: 10px; margin-top: 10px;">
						<h2 class="section_title noborder">
							<fmt:message key='function.cust.title.${command.after.title}' />
						</h2>
						<c:choose>
							<c:when test="${command.after.title eq 'pltfService'}">
								<fmt:message key='message.F0201.disablePltfConfirm_1' />
								<span style="font-weight: bold;">［<c:out value="${command.after.ecName}"/>］</span>
								<fmt:message key='message.F0201.disablePltfConfirm_2' />
								<span style="font-weight: bold;">［<c:out value="${command.after.ecName}"/>］</span>
								<fmt:message key='message.F0201.disablePltfConfirm_3' />
							</c:when>
							<c:otherwise>
								<fmt:message key="message.F0201.disableAcntConfirm_1"/>：<span style="font-weight: bold;"><aclFn:realAcntFormate realAcnt="${command.after.realAcnt }"/></span>
								<fmt:message key="message.F0201.disableAcntConfirm_2"/>［ <span style="font-weight: bold;"><c:out value="${command.after.ecName }" /></span> ］
								<fmt:message key="message.F0201.disableAcntConfirm_3"/>：<span style="font-weight: bold;"><c:out value="${command.after.ecUser }" /></span> 
								<fmt:message key="message.F0201.disableAcntConfirm_4"/>［ <span style="font-weight: bold;"><c:out value="${command.after.ecName }" /></span> ］
								<fmt:message key="message.F0201.disableAcntConfirm_5"/>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</form>
			<!-- Button area ------------------------------------------------------------------------>
			<div class="btnContent">
				<input class="btnStyle" type="button" name="btn1" value='<fmt:message key="common.btn.Back"/>' />
			</div>
		</div>
		<div class="footer_line"></div>
	</div>

</body>
</html>