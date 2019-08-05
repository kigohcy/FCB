<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>帳號連結扣款(Account Link)系統</title>
	
	<%-- include Header, footer and menu --%>
	<%@include file="/include/container.jsp" %>
	
	<style>
		.mainContent{
			text-align:center;
			height:350px;
		}
		.mainContent img{
			margin-top:150px;
		} 
	</style>

</head>
<body>
	<!-- Container ============================================================================================== -->
	<div class="container">
	    <!-- Content ------------------------------------------------------------------------>
	    <div class="mainContent">
	    	<form name="form1" id="form1" method="post" action=""></form>
			<div class = "imgContent"style="">
				<img width='200' src='<%=request.getContextPath()%>/images/bankBody.gif' alt='HSBCnet'/>
			</div>
	    </div>
	    <div class="footer_line"></div>
	</div>
	
</body>
</html>