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
                	codeType:{
                		required:true
                	},
                	codeId:{
                		required:true
                	},
                	codeDesc : {
    					required : true
    				}
                },
                messages:{
                	codeType:{
                		required:"<fmt:message key="F0212.field.codeType" /><fmt:message key="message.alert.mustToSelect" />"
                	},
                	codeId:{
                		required:"<fmt:message key="message.alert.pleaseKeyIn" /><fmt:message key="F0212.field.codeId" />"
                	},
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
			
			if(type == "insert"){//執行新增電商憑證 action
				if($("#form1").valid()){ //validate form
					if(confirm("<fmt:message key="message.cnfm.insertOrNot" />")){
						url = root+"/0212/insertTbCode.html";
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
                	<fmt:message key="common.btn.Add" />
				</div>
				<form method="post" name="form1" id="form1" style="margin: 0;">
					<table id="datatable" class="vTable" width="980px;">
						<tr class="dataRowOdd">
							<%-- 電商平台 --%>
							<th>
								<fmt:message key="F0212.field.codeType"/>*
							</th>
							<td>
								<select name="codeType" id="codeType" style="width:150px;">
									<option value='' selected ><fmt:message key="F0212.field.pleaseSelect"/></option>
									<option value='01'  ><fmt:message key="F0212.field.codeType.01"/></option>
									<option value='02'  ><fmt:message key="F0212.field.codeType.02"/></option>
								</select>
							</td>
						</tr>
						<tr class="dataRowEven">
							<%-- 憑證識別碼 --%>
							<th>
								<fmt:message key="F0212.field.codeId"/>*
							</th>
							<td>
								<input type="text" size="7" maxlength="7" name="codeId" id="codeId" />
							</td>
						</tr>
						<%-- 憑證序號 --%>
						<tr class="dataRowOdd">
							<th>
								<fmt:message key="F0212.field.codeDesc"/>*
							</th>
							<td>
								<input type="text" size="100" maxlength="256" name="codeDesc" id="codeDesc" />
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