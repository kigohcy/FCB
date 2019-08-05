<%
/**
 * @(#) 01.jsp 
 *
 * Directions: 平台憑證管理 -> 憑證新增
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/22, Eason Hsu
 *    1) JIRA-Number, First release
 *   v1.01, 2018/03/28
 *    1) 功能變更為新增
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
                	certCn:{
                		required:true
                	},
                	certSn:{
                		required:true,
                		LENGTH_CHECKER:{
                			min:-1,
                			max:20
                		}
                	},
                	strtDttm : {
    					required : true,
    					DATE_FRMT_CHECKER : true
    				},
    				endDttm : {
    					required : true,
    					DATE_FRMT_CHECKER : true,
    					DATE_COMPARE : {
    						targetDate : '#strtDttm',
    						// 驗證規則: B-預設日不可小於目標日  L-預設日不可大於目標日 E-預設日不可等於目標日
    						compareType : 'B'
    					}
    				}
                },
                messages:{
                	certCn:{
                		required:"<fmt:message key="message.alert.pleaseKeyIn" /><fmt:message key="F0209.field.certName" />"
                	},
                	certSn:{
                		required:"<fmt:message key="message.alert.pleaseKeyIn" /><fmt:message key="F0209.field.certSn" />"
                	},
                	strtDttm : {
    					required : '<fmt:message key="F0209.field.sDate" /><fmt:message key="message.alert.notNull" />',
    					DATE_FRMT_CHECKER : '<fmt:message key="F0209.field.sDate" /> <fmt:message key="message.alert.formateError" />'
    				},
    				endDttm : {
    					required : '<fmt:message key="F0209.field.eDate" /><fmt:message key="message.alert.notNull" />',
    					DATE_FRMT_CHECKER : '<fmt:message key="F0209.field.eDate" /><fmt:message key="message.alert.formateError" />',
    					DATE_COMPARE : '<fmt:message key="message.F0901.dateCompare" />'
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
					if(confirm("<fmt:message key="message.cnfm.0020" />")){
						url = root+"/0209/insertCert.html";
						$("#form1").attr("action", url);
						$("#form1").submit();
					}
				}
			}else if(type == "back"){//執行上一頁 action
				url = root+"/0209/refetchCertList.html";
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
					<fmt:message key="function.Id.F0209"/><c:out value=" > "/>
                	<fmt:message key="common.btn.Add" />
				</div>
				<form method="post" name="form1" id="form1" style="margin: 0;">
					<table id="datatable" class="vTable" width="980px;">
						<tr class="dataRowOdd">
							<%-- 電商平台 --%>
							<th>
								<fmt:message key="F0209.field.ecId"/>
							</th>
							<td>
								<input type="hidden" name="ecId" id="ecId" value="${command.ecId}" />
								<c:out value="${command.ecName }" />
							</td>
						</tr>
						<tr class="dataRowEven">
							<%-- 憑證識別碼 --%>
							<th>
								<fmt:message key="F0209.field.certName"/>*
							</th>
							<td>
								<input type="text" size="25" maxlength="25" name="certCn" id="certCn" />
							</td>
						</tr>
						<%-- 憑證序號 --%>
						<tr class="dataRowOdd">
							<th>
								<fmt:message key="F0209.field.certSn"/>*
							</th>
							<td>
								<input type="text" size="8" maxlength="8" name="certSn" id="certSn" />
							</td>
						</tr>
						<%-- 生效日期 --%>
						<tr class="dataRowEven">
							<th>
								<fmt:message key="F0209.field.sDate"/>*
							</th>
							<td>
								<input type="text" size="10" maxlength="10" name="strtDttm" id="strtDttm" value="" datePicker="true" />
							</td>
						</tr>
						<%-- 到期日期 --%>
                       <tr class="dataRowOdd">
                            <th>
                            	<fmt:message key="F0209.field.eDate"/>*
                            </th>
                            <td>
                            	<input type="text" size="10" maxlength="10" name="endDttm" id="endDttm" value="" datePicker="true" />
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