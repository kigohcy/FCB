<%
/*
 * @(#)message/notLogin.jsp
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
	<%@include file="/include/initial.jsp" %>
	<script type="text/javascript">

		$(function(){

			$("#btn1").click(function(){
				$("#form1").prop("action", "<%=root%>/index.html");
				$("#form1").submit();
			});
			
		});
	
	</script>
</head>
<body>
	<!-- Container ============================================================================================== -->
	<div class="container">
		<div class="header">
			<div style="text-align:left;" id="brand">
				<img width="200" style="width:200px;" title="" alt="Bank" src="<%=root %>/images/bankHeader.gif" id="tsib_logo">&nbsp;
				<img width="200" style=";width:200px;margin-right:480px;" title="" alt="帳務連結扣款系統" src="<%=root %>/images/sys_name.gif" id="sys_name">&nbsp;
				<img width="105" style=";width:80px;" title="" alt="客服系統" src="<%=root %>/images/staff_title.gif" id="staff_title">&nbsp;
			</div>
			<div style="text-align:left;" id="navbar"></div>
			<div id="userinfo"></div>
			<div id="logoff"></div>
			<div id="logTime"></div>
		</div>
    <!-- Content ------------------------------------------------------------------------>
	    <div class="mainContent">
	        <div class="content">
	            <div class="fnctTitle">
	            	<fmt:message key='function.Id.${_fnctId}'/>
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
		                    			<fmt:message key="${exception.errorCode }"/>
		                    		</c:when>
		                    	</c:choose>
		                    </td>
		                </tr>
		            </table>
	            </form>
	        </div>
	        
	        <!-- Button area ------------------------------------------------------------------------>
	        <div class="btnContent">
	            <input class="btnStyle" type="button" id="btn1" name="btn1" value="<fmt:message key='${"common.button.ok"}'/>"/>&nbsp;
	        </div>
	        
	    </div>        
    	<div class="footer_line"></div>
    	<br>
		<div style="margin-top:20px;"></div>
	</div>
	<div class="footer">
		<div class="footerContent">
			<div class="copyright">&copy;台新國際商業銀行   Copyright Taishin International Bank. All Rights Reserved.</div>
			<div class="tslogo">
				<img src="<%=root %>/images/tsb001364.gif">
			</div>
		</div>
	</div>
</body>
</html>