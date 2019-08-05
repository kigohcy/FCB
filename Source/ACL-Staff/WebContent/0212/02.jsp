<%
/**
 * @(#) 01.jsp 
 *
 * Directions: 訊息代碼管理
 *
 * Copyright (c) 2018 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2018/04/17
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
			datePicker(root);
			$("#form1").validate({
                rules: {
                	codeDesc : {
    					required : true
    				}
                },
                messages:{
                	codeDesc : {
                		required:"<fmt:message key="message.alert.pleaseKeyIn" /><fmt:message key="F0212.field.codeDesc" />"
    				}
                },
                showErrors: function(errorMap, errorList) {
                    var err = [];
                    $.each(errorList, function(i, v) {
                        err.push(v.message);
                    });
                    if (err.length > 0) {
                        alert(err.join("\n"));
                    }
                },
                onkeyup: false,
                onfocusout: false,
                onsubmit: false
            });
			
		});
		
		//type: insert, back
		function send(type){
			
			var url = "";
			
			if(type == "insert"){//執行訊息代碼 action
				if($("#form1").valid()){ //validate form
					if(confirm("<fmt:message key="message.cnfm.updateOrNot" />")){
						url = root+"/0212/updateTbCode.html";
						$("#form1").attr("action", url);
						$("#form1").submit();
					}
				}
			}else if(type == "back"){//執行上一頁 action
				url = root+"/0212/requery.html";
				$("#form1").attr("action", url);
				$("#form1").submit();
			}
		}
	</script>
	
</head>
<body>
	<!-- Container ============================================================================================== -->
	<div class="container">
		<!-- Content ------------------------------------------------------------------------>
		<div class="mainContent">
			<div class="content">
				<div class="fnctTitle">
					<fmt:message key="function.Id.F0212"/><c:out value=" > "/>
                	<fmt:message key="common.btn.Modify" />
				</div>
				<form method="post" name="form1" id="form1" style="margin: 0;">
					<input type="hidden" name="codeType" id="codeType" value="${command.tbCode.codeType}"/>
					<input type="hidden" name="codeId" id="codeId" value="${fn:substring(command.tbCode.id.codeId, 3, fn:length(command.tbCode.id.codeId))}"/>
					<table id="datatable" class="vTable" width="980px;">
						<tr class="dataRowOdd">
							<%-- 類別 --%>
							<th>
								<fmt:message key="F0212.field.codeType"/>
							</th>
							<td>
								<fmt:message key="F0212.field.codeType.${command.tbCode.codeType}" />
							</td>
						</tr>
						<tr class="dataRowEven">
							<%-- 代碼 --%>
							<th>
								<fmt:message key="F0212.field.codeId"/>
							</th>
							<td>
								${fn:substring(command.tbCode.id.codeId, 3, fn:length(command.tbCode.id.codeId))}
							</td>
						</tr>
						<%-- 說明--%>
						<tr class="dataRowOdd">
							<th>
								<fmt:message key="F0212.field.codeDesc"/>*
							</th>
							<td>
								<input type="text" size="100" maxlength="256" name="codeDesc" id="codeDesc" value="${command.tbCode.codeDesc}"/>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<!-- Button area ------------------------------------------------------------------------>
			<div class="btnContent">
				<%--確認--%>
				<input class="btnStyle" type="button" name="btn1" value="<fmt:message key="common.btn.OK" />"  onclick="send('insert');" /> &nbsp;
				<%--回上一頁--%>
				<input class="btnStyle" type="button" name="btn1" value="<fmt:message key="common.btn.Back" />"  onclick="send('back');" />
			</div>
		</div>
		<!-- Footer ============================================================================================== -->
		<div class="footer_line"></div>
	</div>
</body>
</html>