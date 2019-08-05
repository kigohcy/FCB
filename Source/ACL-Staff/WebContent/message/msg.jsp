<%
/*
 * @(#)msg.jsp
 *
 * Copyright (c) 2006 HiTRUST Incorporated. All rights reserved.
 *
 * Description:
 *  處理結果或錯誤訊息統一導到此頁，再決定要 forward 到哪一頁

 *
 * Modify History:
 *  v1.00, 2007/07/15, Jmiu Han
 *   1) First release
 */
%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%-- include Header, footer and menu --%>
<%@include file="/include/container.jsp"%>
<script type="text/javascript">

	$(function(){
		$("#btn1").click(function(){
			$("#form1").submit();
		});
	});
</script>
</head>
<body>
<div class="container">
    <div class="mainContent">
		  <div class="content">
		      <div class="fnctTitle"><fmt:message key='function.Id.${command.funcId}'/></div>
		      <form method="post" name="form1" id="form1" action="<%=root%><c:out value="${messageRedirect}"/>.html" style="margin: 0;">
				<input type="hidden" name="_back" value="<c:out value='${_back}'/>">
			      <table class="fxdTable" width="100%" style="margin:auto;">
			          <tr class="secondaryTitleRow">
			              <td colspan="2"><fmt:message key='${"message.title.resMsg"}'/></td><!-- 回覆訊息 -->
			          </tr>
			          <tr class="dataRowOdd">
			              <td width="80px;"><fmt:message key='${"message.item.exeFnct"}'/></td><!-- 執行功能 -->
			              <td><fmt:message key='function.Id.${command.funcId}'/></td>
			          </tr>
			          <tr class="dataRowEven">
			              <td width="80px;"><fmt:message key='${"message.item.exeMsg"}'/></td><!-- 執行訊息 -->
			              <td>
			               <c:choose>
								<c:when test="${not empty _error}"> 
										<c:if test="${not empty _error.errorCode }">
											<fmt:message key='${_error.errorCode}' var="_error_" scope="request"/>
											<fmt:message key='${"message.sys.undefined"}' var="_sys_" scope="request"/>
											<%if(request.getAttribute("_error_").toString().startsWith("???")){
											    out.write(request.getAttribute("_sys_").toString());
											}else{
											  	out.write(request.getAttribute("_error_").toString());
											}%>
										</c:if>
										<c:if test="${not empty _error.errorMsg and empty _error.errorCode}"><c:out value="${_error.errorMsg }"/></c:if>
										<c:if test="${not empty _error.parameters and empty _error.errorMsg and empty _error.errorCode}">
											<c:forEach items="${_error.parameters}" var="msg" varStatus="i">
												<c:out value="${msg}"/><br/>
											</c:forEach>
										</c:if>
								</c:when>
								<c:when test="${ not empty exceptions}">
									<font color="red"> 
										<c:out value="${exceptions}"/>
									</font>
								</c:when>
								<c:otherwise>
									<fmt:message key='${"message.ok"}'/><!-- 資料處理成功! -->
								</c:otherwise>
							</c:choose>
		              </td>
		          </tr>
		      </table>
		      </form>
		  </div>
		  <!-- Button area ------------------------------------------------------------------------>
		  <div class="btnContent">
		      <input class="btnStyle" type="button" id="btn1" value="<fmt:message key='${"common.button.ok"}'/>" />&nbsp;<!-- 確認 -->
		  </div>
    </div>
    <div class="footer_line"></div>
</div>
</body>
</html>
