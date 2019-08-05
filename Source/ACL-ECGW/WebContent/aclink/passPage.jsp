<%
/*
 * @(#)aclink/passPage.jsp
 *
 * Copyright (c) 2018 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 導頁
 * 
 *  * Modify History: 
 * v1.00, 2018/01/16, jeffLin
 *  1) First release
*/
%>

<%@ page language="java" contentType="text/html; charset=BIG5" pageEncoding="BIG5"%>
<%@ taglib uri="/WEB-INF/tlds/stringMask.tld" prefix="mask"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>導頁</title>

<%@include file="/include/initial.jsp"%>

<script type="text/javascript">
	$(function(){
			$("#loginForm").append('<input type="hidden" name="_service" id="_service" value="eBankLogin"/>')
			$("#loginForm").prop("action",  "<%=root%>/portal");
			$("#loginForm").submit();	
		}
	);
	
</script>

</head>
<body>
	<div style="margin: auto; margin-top: 40px; width: 260px;"><!-- v1.01 -->
		<form id="loginForm" name="txForm" method="post" target="_top" autocomplete="off">
		</form>
	</div>
</body>
</html>
