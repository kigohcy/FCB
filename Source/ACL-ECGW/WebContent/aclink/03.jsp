<%
/*
 * @(#)aclink/03.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: �j�w�b��T�{
 *
 * Modify History:
 * v1.00, 2016/04/08, Eason Hsu
 * 	1) First Release
 * v1.01, 2016/12/01, Eason Hsu
 * 	1) TSBACL-137, ECGW �j�w�s���b�����Ҥ覡�վ�
 * v1.02, 2016/12/15, Eason Hsu
 * 	1) TSBACL-141, �b���s���j�w�y�{���������, �ݪ��׭��д���
*/
%>

<%@ page language="java" contentType="text/html; charset=BIG5" pageEncoding="BIG5"%>
<%@ taglib uri="/WEB-INF/tlds/realAcntFormat.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tlds/stringMask.tld" prefix="mask"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>�j�w�b��T�{</title>

<%@include file="/include/initial.jsp" %>

<script type="text/javascript">
	$(function() {
		<%-- v1.01 --%>
		var _error = '<c:out value="${link._error }" />';
		if (_error != "") {
			alert("��Ƥ��i���ŭ�, �Ц^�W�@���ק�");
		}
		
		$("#btnReturn").click(function() {
			$("#_service").val("reModify");
			
			<%-- v1.02, form submit �e�W�[ Mask loding --%>
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
				alert("��Ƥ��i���ŭ�, �Ц^�W�@���ק�");
				return;
			}
			
			<%-- v1.02, form submit �e�W�[ Mask loding --%>
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
				
				<p class="title">�нT�{�H�U�s���b����T</p>
				<div id="content" class="align_center" style="width: 300px;">
					<form name="form1" id="form1" method="post" action="<%=root%>/portal">
						<input type="hidden" id="_service" name="_service" value="">
						<table border="1">
							<tr>
								<th>�q�ӥ��x</th>
								<td>
									<c:out value="${link.ecName }" />
								</td>
							</tr>
							<tr>
								<th>�����Ҧr��</th>
								<td>
									<mask:stringMask idStr="${link.custId }" start="3" maskCount="3" symbol="*" />
								</td>
							</tr>
							<tr>
								<th>�q�ӥ��x�|���b��</th>
								<td>
									<c:out value="${link.ecUser }" />
								</td>
							</tr>
							<tr>
								<th>�s���b��</th>
								<td>
									<fmt:acntFormat realAcnt="${link.linkAcnt }"/>
								</td>
							</tr>
							<tr>
								<th>������X</th>
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
						<p class="tsb-text-red">��������X��Email�Y������q���ϥΡA�нT�{�O�_���T�C�p���ܧ�ЦܦU�����z�C</p> -->
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