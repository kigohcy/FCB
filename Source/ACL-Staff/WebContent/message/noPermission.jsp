<%
/*
 * @(#)message/noPermission.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description:
 * 
 * Modify History:
 *  First Release, Eason Hsu
*/
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>帳號連結扣款(Account Link)系統</title>
	<%@include file="/include/container.jsp" %>
	
</head>
<body>
	<!-- Container ============================================================================================== -->
	<div class="container">
    <!-- Content ------------------------------------------------------------------------>
	    <div class="mainContent">
	        <div class="content">
	            <div class="fnctTitle">
	            	<fmt:message key='function.Id.${command.funcId}'/>
	            </div>
	            
	            <form method="post" id="form1" name="form1" style="margin: 0;">
		            <table class="fxdTable" width="100%">
		                <tr class="secondaryTitleRow">
		                    <td colspan="2"><%-- 回覆訊息 --%>
		                    	<fmt:message key='${"message.title.resMsg"}'/>
		                    </td>
		                </tr>
		                <tr class="dataRowOdd">
							<td width="80px;"><%-- 執行功能 --%>
		        				<fmt:message key='${"message.item.exeFnct"}'/>       
		                   	</td>
		                    <td>
		                    	<fmt:message key='function.Id.${_fnctId}'/>
		                    </td>
		                </tr>
		                <tr class="dataRowEven"><%-- 執行訊息 --%>
		                    <td width="80px;">
		                    	<fmt:message key='${"message.item.exeMsg"}'/>
		                    </td>
		                    <td>
		                    	<c:choose>
		                    		<c:when test="${not empty exception }">
		                    			<%-- <fmt:message key="${exception.errorCode }"/> --%>
		                    		</c:when>
		                    	</c:choose>
		                    </td>
		                </tr>
		            </table>
	            </form>
	        </div>
	        
	    </div>        
    	<div class="footer_line"></div>
	</div>
</body>
</html>