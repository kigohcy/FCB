<%
/*
 * @(#)include/init.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 綁定帳戶確認
 *
 * Modify History:
 * v1.00, 2016/04/15, Eason Hsu
 * 	1) First Release
 * v1.01, 2016/12/15, Eason Hsu
 * 	1) TSBACL-141, 帳號連結綁定流程頁面提交時, 需阻擋重覆提交
*/
%>

<%@ page import="com.hitrust.acl.APSystem"%>
<%@ page language="java" contentType="text/html; charset=BIG5" pageEncoding="BIG5"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String root = request.getContextPath();

	// 是否啟用「晶片卡登入」1: 啟用, 0: 停用
	String has_ecard = APSystem.getProjectParam("HAS_ECARD");
%>

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<%-- CSS --%>
<link rel="stylesheet" type="text/css" href="<%=root%>/css/pure-release-0.6.0/pure-min.css">
<link rel="stylesheet" type="text/css" href="<%=root%>/css/pure-release-0.6.0/grids-responsive-min.css">
<link rel="stylesheet" type="text/css" href="<%=root%>/css/jqtransform.css">

<link rel="stylesheet" type="text/css" href="<%=root%>/js/jquery-ui-1.11.4.custom/jquery-ui.min.css" media="all" />
<link rel="stylesheet" type="text/css" href="<%=root%>/js/jquery-ui-1.11.4.custom/jquery-ui.theme.min.css" media="all" />
<link rel="stylesheet" type="text/css" href="<%=root%>/css/acl-gw-style.css" >
<link rel="stylesheet" type="text/css" href="<%=root%>/css/checkbox-style.css" >
<%-- v1.01, 增加 css --%>
<link rel="stylesheet" type="text/css" href="<%=root%>/css/mask-style.css" >

<!--[if lte IE 8]>
    <link rel="stylesheet" href="css/pure-release-0.6.0/grids-responsive-old-ie-min.css">
<![endif]-->

<%-- JS --%>
<script src="<%=root%>/js/jquery-1.12.4.min.js"></script>
<script src="<%=root%>/js/acl-gw.js"></script>
<%-- 裝置分析 --%>
<script src="<%=root%>/js/mobile-detect.min.js"></script>
<%-- validate --%>
<script src="<%=root%>/js/acl-gw-validate.js"></script>
<script src="<%=root%>/js/jquery.validate.min.js"></script>
<%-- jQuery UI --%>
<script src="<%=root%>/js/jquery-ui-1.11.4.custom/jquery-ui.min.js"></script>
<script src="<%=root%>/js/jquery-migrate-1.4.1.js"></script>
<script src="<%=root%>/js/jquery.jqtransform.js"></script>

<script type="text/javascript">
$(function(){
	
	<%-- 初始 Browser 回上/下頁, Browser 關閉時需處理的事件 --%>
	$(window).on("beforeunload", function(event) {
		
		$.ajax({
			url: "<%=root%>/portal",
			type: "GET",
			data: {_service: "cancel", account: $("input[type=radio]:checked").val()}
		});
	});

	$("form").submit(function() {
		$(window).off("beforeunload");
	});

	<%-- v1.01, 增加按下 enter 不可 submit --%>
	$("input").keypress(function(event){
		var keyCode = event.keyCode;

		if(keyCode == 13) {
			event.preventDefault();
		}
	});
	
});

</script>

