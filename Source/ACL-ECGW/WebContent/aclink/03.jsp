<%
/*
 * @(#)aclink/03.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 綁定帳戶確認
 *
 * Modify History:
 * v1.00, 2016/04/08, Eason Hsu
 * 	1) First Release
 * v1.01, 2016/12/01, Eason Hsu
 * 	1) TSBACL-137, ECGW 綁定連結帳號驗證方式調整
 * v1.02, 2016/12/15, Eason Hsu
 * 	1) TSBACL-141, 帳號連結綁定流程頁面提交時, 需阻擋重覆提交
*/
%>

<%@ page language="java" contentType="text/html; charset=BIG5" pageEncoding="BIG5"%>
<%@ taglib uri="/WEB-INF/tlds/realAcntFormat.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tlds/stringMask.tld" prefix="mask"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>綁定帳戶確認</title>

<%@include file="/include/initial.jsp" %>

<script type="text/javascript">
	$(function() {
		<%-- v1.01 --%>
		var _error = '<c:out value="${link._error }" />';
		if (_error != "") {
			alert("資料不可為空值, 請回上一頁修改");
		}
		
		$("#btnReturn").click(function() {
			$("#_service").val("reModify");
			
			<%-- v1.02, form submit 前增加 Mask loding --%>
			$("#msak_loding").fadeIn(function () {

				$("#form1").submit();
			});
			
		});
		
		$("#btnComplete").click(function() {
			<%-- v1.02 --%>
			$("#_service").val("confirmSubmit");
			
			<%-- v1.01 --%>
			var acnt = '<c:out value="${link.linkAcnt }" />';
			var authType = '<c:out value="${link.idetityAuthType }" />';
			
			if (acnt == "" || authType == "") {
				alert("資料不可為空值, 請回上一頁修改");
				return;
			}
			
			<%-- v1.02, form submit 前增加 Mask loding --%>
			$("#msak_loding").fadeIn(function () {
				$("#form1").submit();

			});
			
		});
	});

	
	function checkVal() {
		
	}
</script>

</head>
<body>
	<div class="pure-g forfirefox">
		<div id="header" class="pure-u-1">
			<img src='<%=root%>/images/popwin_panel_header.jpg' />
		</div>
	</div>
	<div class="pure-g">
		<div class="pure-u-1">
			<div id="container">
				
				<p class="title">請確認以下連結帳號資訊</p>
				<div id="content" class="align_center" style="width: 300px;">
					<form name="form1" id="form1" method="post" action="<%=root%>/portal">
						<input type="hidden" id="_service" name="_service" value="">
						<table border="1">
							<tr>
								<th>電商平台</th>
								<td>
									<c:out value="${link.ecName }" />
								</td>
							</tr>
							<tr>
								<th>身分證字號</th>
								<td>
									<mask:stringMask idStr="${link.custId }" start="3" maskCount="3" symbol="*" />
								</td>
							</tr>
							<tr>
								<th>電商平台會員帳號</th>
								<td>
									<c:out value="${link.ecUser }" />
								</td>
							</tr>
							<tr>
								<th>連結帳戶</th>
								<td>
									<fmt:acntFormat realAcnt="${link.linkAcnt }"/>
								</td>
							</tr>
							<tr>
								<th>手機號碼</th>
								<td>
									<mask:stringMask idStr="${link.tlxNo }" start="4" maskCount="4" symbol="*" />
								</td>
							</tr>
							<tr>
								<th>Email</th>
								<td>
									<mask:stringMask idStr="${link.emailAddr }" start="2" maskCount="4" symbol="*" />
								</td>
							</tr>
						</table>
						<!-- 
						<p class="tsb-text-red">※手機號碼及Email係為交易通知使用，請確認是否正確。如須變更請至各分行辦理。</p> -->
					</form>
				</div>
				<div id="btnArea" class="text_center margin_top20">
					<a href="#" class="btn btnBack" id="btnReturn"></a>
					<a href="#" class="btn btnSubmit" id="btnComplete"></a>
				</div>
			</div>
		</div>
	</div>
	<div class="pure-g">
		<div id="footer" class="pure-u-1"></div>
	</div>
	<%-- v1.02 --%>
	<div id='msak_loding' class="msak_loding"></div>
</body>
</html>