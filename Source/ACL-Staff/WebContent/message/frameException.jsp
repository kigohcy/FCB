<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>帳號連結扣款(Account Link)系統</title>
	<%-- include Header, footer and menu --%>
	<%@include file="/include/container.jsp" %>
	
</head>
<body>
	<div class="container">
		<div class="mainContent">
			<div class="content">
				<form method="post" name="form1" id="form1" ></form>
				<div class="error">
					<c:if test="${not empty exception.errorCode }">
						<fmt:message key='${exception.errorCode }'/>
					</c:if>
				</div>
	  		</div>	
		</div>
	    <div class="footer_line"></div>
	</div>
</body>
</html>