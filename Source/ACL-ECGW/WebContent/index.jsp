<%
/*
 * @(#)index.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 轉導頁
 *
 * Modify History:
 * v1.00, 2016/07/29, Eason Hsu
 * 	1) First Release
*/
%>

<%@ page language="java" contentType="text/html; charset=BIG5" pageEncoding="BIG5"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	String root = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">

<script src="<%=root%>/js/jquery-1.12.4.min.js"></script>

<%
	String _service = request.getParameter("_service");
	String sessionKey = request.getParameter("sessionKey");
	
	System.out.println("_service: " + _service + "sessionKey: " + sessionKey);
%>
<script type="text/javascript">
	$(function(){

		$("#form1").prop("action", "<%=root%>/portal");
		$("#form1").submit();
	});

</script>
</head>
<body>
	<form name="form1" id="form1" method="post">
		<input type="hidden" name="_service" id="_service" value="<c:out value="${param._service }"/>"/>
		<input type="hidden" name="sessionKey" id="sessionKey" value="<c:out value="${param.sessionKey }"/>"/>
	</form>
</body>
</html>