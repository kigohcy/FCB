<%@ page language="java" contentType="text/html; charset=BIG5" pageEncoding="BIG5"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>導頁</title>

<%@include file="/include/initial.jsp"%>

<script type="text/javascript">
	$(function(){
			$("#loginForm").prop("action",  "https://ibanktest.firstbank.com.tw/apppay/request/0202.do");
			$("#loginForm").submit();	
		}
	);
</script>

</head>
<body>
	<div style="margin: auto; margin-top: 40px; width: 260px;"><!-- v1.01 -->
		<form id="loginForm" name="txForm" method="post" target="_top" autocomplete="off">
			<input type="hidden" name="merchantId" id="merchantId" value="${link.merchantId }" />
			<input type="hidden" name="txReqId" id="txReqId" value="${link.txReqId }" />
			<input type="hidden" name="sign" id="sign" value="${link.sign }" />
		</form>
	</div>
</body>
</html>

