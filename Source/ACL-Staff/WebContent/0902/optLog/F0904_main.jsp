<%
/*
 * @(#)log/F0904.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 操作記錄查詢 - 使用者管理
 *
 * Modify History:
 * v1.00, 2018/05/28, Darren Tsai
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
		$(".fxdTable tbody tr:odd").addClass("dataRowEven");
		$(".fxdTable tbody tr:even").addClass("dataRowOdd");
		
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
		<!-- Content ============================================================================================ -->
		<div class="mainContent">
			<div class="content">
				<div class="fnctTitle">
					<fmt:message key='function.Id.${command.funcId}'/> - <fmt:message key='function.Id.${command.q_fnctId}'/> - <fmt:message key='operate.Id.${command.q_action}'/>
				</div>
				<form id="form1"></form> <%--登出使用 --%>
				<form name="formDetl" id="formDetl" method="post" style="margin: 0;">
		         	<input type="hidden" name="page.pageNo" value="${command.optCurrentPage}" /><!-- 記錄頁數 -->
		         	<div id="before" style="height: 305px; width: 485px; overflow-y: scroll; overflow-x: scroll; float: left;">
						<table id="datatable" class="vTable" width="980px;">
							<tr class="staffFnctTitleRow">
			                    <td colspan="2"><fmt:message key="common.operate.beforeMdfy"/></td><%--異動前 --%>
			                </tr>
			                <tr>
			                	<td>
			                		<c:choose>
								        <c:when test="${command.q_action =='E' or command.q_action =='F'}">
						                    <c:if test="${command.q_action =='E'}"> <%--變更--%>
						                        <%@include file="F0904_B.jsp"%>
						                    </c:if>
											<c:if test="${command.q_action =='F'}"> <%--終止--%>
					        					<%@include file="F0904F_B.jsp"%>
			                    			</c:if>
									    </c:when>
									    <c:otherwise>
									    	<fmt:message key="common.operate.haveNoData"/> <%--無資料 --%>
									    </c:otherwise>
									</c:choose>
				                </td>
			                </tr>
			              </table>
					</div>
		         	&nbsp;
	           	 	<div id="after" style="height: 305px; width: 485px; overflow-y: scroll; overflow-x: scroll; float: right;">
	           	 		 <table id="datatable" class="vTable" width="980px;">
				         	 	<tr class="staffFnctTitleRow">
				                    <td colspan="2"><fmt:message key="common.operate.afterMdfy"/></td><%--異動後 --%>
				                </tr>
				                <tr>
				                	<td>
				                		<c:choose>
									        <c:when test="${command.q_action =='E' or command.q_action =='A' or command.q_action =='F'}">
												<c:if test="${command.q_action =='E' or command.q_action =='A'}"> <%--變更 or 新增--%>
							                    	<%@include file="F0904_A.jsp"%>
							                    </c:if>
												<c:if test="${command.q_action =='F'}"> <%--終止--%>
							                    	<%@include file="F0904F_A.jsp"%>
							                    </c:if>
										    </c:when>
										    <c:otherwise>
										    	<fmt:message key="common.operate.haveNoData"/> <%--無資料 --%>
										    </c:otherwise>
										</c:choose>
					                </td>
				                </tr>
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
