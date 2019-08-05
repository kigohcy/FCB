<%
/*
 * @(#)log/F0211.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 操作記錄查詢 - 扣款平台覆核設定
 *
 * Modify History:
 * v1.00, 2018/04/20, Darren Tsai
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
					<fmt:message key='function.Id.${command.funcId}'/> - <fmt:message key='function.Id.${command.q_fnctId}'/> - <fmt:message key='operate.Id.${command.q_action}'/>
				</div>
				<form id="form1"></form> <%--登出使用 --%>
				<form name="formDetl" id="formDetl" action="" method="post" style="margin: 0;">
				<input type="hidden" name="page.pageNo" value="${command.optCurrentPage}" /><!-- 記錄頁數 -->
				<div id="before" style="height: 350px; width: 485px; overflow-y: scroll; overflow-x: scroll; float: left;">
					<table id="datatable1" class="vTable" width="980px">
						<tr class="staffFnctTitleRow">
			            	<td colspan="14"><fmt:message key="common.operate.beforeMdfy"/></td><%--異動前--%>
			            </tr>
			            
			            <c:choose>
					        <c:when test="${command.q_action =='T'}">
			                        <%@include file="F0211_B.jsp"%>
						    </c:when>
						    <c:otherwise>
						    	<tr class="dataRowOdd">
						    		<td colspan="2"><fmt:message key="common.operate.haveNoData"/> <%--無資料--%></td>
						    	</tr>
						    </c:otherwise>
						</c:choose>
		           </table>
               </div>	
	               	&nbsp;
	           <div id="after" style="height: 350px; width: 485px; overflow-y: scroll; overflow-x: scroll; float: right;">
	               	<table id="datatable2" class="vTable" width="980px">
						<tr class="staffFnctTitleRow">
			            	<td colspan="2"><fmt:message key="common.operate.afterMdfy"/></td><%--異動後--%>
			            </tr>
		            	<c:choose>
					        <c:when test="${command.q_action =='T'}">
			                      <%@include file="F0211_A.jsp"%>
						    </c:when>
						    <c:otherwise>
						    	<tr class="dataRowOdd">
						    		<td colspan="2"><fmt:message key="common.operate.haveNoData"/> <%--無資料--%></td>
						    	</tr>
						    </c:otherwise>
						</c:choose>
		            </table>
		            </div>
	               
	               	&nbsp;
				</form>
			</div>
			<%-- Button area --%>
			<div class="btnContent">
				<input id="btnBack" class="btnStyle" type="button" value="<fmt:message key="common.btn.Back"/>" />
			</div>
		</div>
		<div class="footer_line"></div>
	</div>
</body>
</html>
