<%
	/*
	 * @(#)aclink/forwardCardLogin.jsp
	 *
	 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
	 *
	 * Description: 晶片卡登入轉導頁
	 *
	 * Modify History:
	 * v1.00, 2016/07/12, Eason Hsu
	 * 	1) First Release
	*/
%>

<%@ page language="java" contentType="text/html; charset=BIG5" pageEncoding="BIG5"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>晶片卡登入轉導頁</title>

<%@include file="/include/initial.jsp"%>

<script type="text/javascript">

	function forward(){
		$("#form1").prop("action",  "<%=root%>/portal");
		$("#form1").submit();
	}
	
</script>
 
</head>
<body onload="forward();">
	<form name="form1" id="form1" method="post" >
		<input type="hidden" name="_service" value="eCardLogin"/>
		<input type="hidden" name="param" value="<c:out value="${param.param }"/>"/>
	</form>
</body>
</html>