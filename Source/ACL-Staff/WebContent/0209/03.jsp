<%
/**
 * @(#) 03.jsp 
 *
 * Directions: 平台憑證管理 -> 驗章測試
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/22, Eason Hsu
 *    1) JIRA-Number, First release
 *
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<title>帳號連結扣款(Account Link)系統</title>
	
	<%-- include Header, footer and menu --%>
	<%@include file="/include/container.jsp" %>
	
	<script type="text/javascript">
		$(function(){
			
			<%-- 測試 --%>
			$("#test").click(function(){
				var signatureVal = $("#signatureVal").val();

				if ($.trim(signatureVal) == "") {
					alert('<fmt:message key="message.F0209.alertMsg.004"/>');
					return;
				} else {
					$("#form1").prop("action", root + "/0209/verifySignTest.html");
					$("#form1").submit();
				}
				
			});
			
			<%-- 回上一頁 --%>
			$("#back").click(function(){
				$("#form1").prop("action", root + "/0209/refetchCertList.html");
				$("#form1").submit();
			});

		});

	</script>
	
</head>
<body>
	<!-- Container ============================================================================================== -->
    <div class="container">
        <!-- Content ------------------------------------------------------------------------>
        <div class="mainContent">
            <div class="content">
                <div class="fnctTitle">
					<fmt:message key="function.Id.F0209"/><c:out value=" > "/>
                	<fmt:message key="common.btn.CertTest"/>
				</div>
                <form method="post" name="form1" id="form1" style="margin: 0;">
                    <table id="datatable" class="vTable" width="980px;">
                        <tr class="dataRowOdd">
                        	<%-- 電商平台 --%>
                            <th>
                            	<fmt:message key="F0209.field.ecId"/>
                            </th>
                            <td>
                            	<c:out value="${command.ecName }" />
                            </td>
                        </tr>
                        <%-- 憑證識別碼 --%>
                        <tr class="dataRowEven">
                            <th>
                            	<fmt:message key="F0209.field.certName"/>
                            </th>
                            <td>
                            	<c:out value="${command.certCn }" />
                            </td>
                        </tr>
                        <%-- 簽章明文 --%>
                        <tr class="dataRowOdd">
                            <th>
                            	<fmt:message key="F0209.field.plainText"/>*
                            </th>
                            <td>
                                <textarea name="plainText" id="plainText" rows="6" cols="80"></textarea>
                            </td>
                        </tr>
                        <%-- 簽章值 --%>
                        <tr class="dataRowOdd">
                            <th>
                            	<fmt:message key="F0209.field.signValue"/>*
                            </th>
                            <td>
                                <textarea name="signatureVal" id="signatureVal" rows="15" cols="80"></textarea>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
            <!-- Button area ------------------------------------------------------------------------>
            <div class="btnContent">
            	<%-- 測試 --%>
                <input class="btnStyle" type="button" name="test" id="test" value="<fmt:message key="common.btn.Test"/>" /> &nbsp;
                <%-- 回上一頁 --%>
                <input class="btnStyle" type="button" name="back" id="back" value="<fmt:message key="common.btn.Back"/>" /> &nbsp;
            </div>
        </div>
        <!-- Footer ============================================================================================== -->
        <div class="footer_line"></div>
    </div>
</body>
</html>