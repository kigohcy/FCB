
<%
	/*
	 * @(#)dialog/tbCoe.jsp
	 *
	 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
	 *
	 * Description: tbCoe
	 *
	 * Modify History:
	 * v1.00, 2016/06/07, Jimmy Yen
	 * 	1)First Release
	 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/include/initialDialog.jsp"%>

<style>
div.newsTitle {
	margin-bottom: 20px;
	border-bottom-width: 1px;
	border-bottom-style: solid;
	border-bottom-color: #DADADA;
}

div.newsTitle h3 {
	color: #ca0306;
	font-size: 20pt;
}

div.newsTitle h3 font {
	FONT-SIZE: 13px;
	FONT-WEIGHT: normal;
	COLOR: #666666
}
</style>

<DIV class=newsTitle>
	<H3>${command.title}
		<FONT> (<fmt:formatDate pattern="yyyy/MM/dd" value="${command.bgnDate}" />)
		</FONT>
	</H3>
</DIV>
<div>${command.content}</div>
