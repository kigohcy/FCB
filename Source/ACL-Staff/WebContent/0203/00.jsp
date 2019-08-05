<%
/*
 * @(#)0203/00.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description:
 * 電商平台狀態管理顯示頁面
 *
 * Modify History:
 * v1.00, 2018/04/09, Darren Tsai
 * 	1)First Release
*/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>帳號連結扣款(Account Link)系統</title>
	
	<%-- include Header, footer and menu --%>
	<%@include file="/include/container.jsp" %>
	<script type="text/javascript">
	
		$(function(){
			$("#form1").validate({
		         rules: {
		        	 ecId:{
		        		 required: true
		         	}
		         },
		         messages:{
		        	 ecId:{
		        		 required:"<fmt:message key="message.alert.mustToSelect" />"
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
		
	    //type: enableInit, pauseInit
		function send(type){
			var ecId = $('input[name=ecId]:checked', '#form1').val();
			var stts = $('#'+ecId+'stts').val();
			$('#ecNameCh').val($('#'+ecId+'ecNameCh').val());
			$('#ecNameEn').val($('#'+ecId+'ecNameEn').val());
	    	
			var url = "";
			if(type == "enableInit"){ //啟動電商平台狀態action
				if(stts!='01'){
					alert("<fmt:message key="message.cnfm.0010" />")
					return;
				}
				url = root+"/0203/enableEcDataStatus.html";
				$('#stts').val('00');
				$("#form1").attr('action', url);
				$("#form1").submit();
			}else if(type == "pauseInit"){ //暫停電商平台狀態action
				if(stts!='00'){
					alert("<fmt:message key="message.cnfm.0010" />")
					return;
				}
				url = root+"/0203/pauseEcDataStatus.html";
				$('#stts').val('01');
				$("#form1").attr('action', url);
				if($("#form1").valid()){
					$("#form1").submit();
				}
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
	                <div class="fnctTitle"><fmt:message key="function.Id.F0203" /></div><%-- 電商平台狀態管理 --%>
	                <form method="post" name="form1" id="form1" action="" style="margin: 0;"> 
	                	<input type="hidden" id="stts" name="stts" value="" />
	                	<input type="hidden" id="ecNameCh" name="ecNameCh" value="" />
	                	<input type="hidden" id="ecNameEn" name="ecNameEn" value="" />
	                    <table class="fxdTable" width="100%">
	                        <tr class="titleRow">
	                            <td><fmt:message key="F0201.field.OPTION" /></td><%-- 選項 --%>
	                            <td><fmt:message key="F0201.field.EC_ID" /></td><%-- 平台代碼 --%>
	                            <td><fmt:message key="F0201.field.EC_NAME_CH" /></td><%-- 平台中文名稱 --%>
	                            <td><fmt:message key="F0201.field.EC_NAME_EN" /></td><%-- 平台英文名稱 --%>
	                            <td><fmt:message key="F0201.field.FEE_STTS" /></td><%-- 狀態 --%>
	                        </tr>
							<c:forEach items="${command.ecDataList}" var="item" varStatus="theCount">
								<c:choose>
									<c:when test="${theCount.count % 2 == 1}">
										<c:set value="dataRowOdd" var="cssClass"></c:set>
									</c:when>
									<c:otherwise>
										<c:set value="dataRowEven" var="cssClass"></c:set>
									</c:otherwise>
								</c:choose>
								<tr class="${cssClass}">
							    	<td align="center">
		                                <input type="radio" name="ecId" value="${item.ecId}" <c:if test="${theCount.count == 1}">checked</c:if>  />
		                           		<input type="hidden" id="${item.ecId}stts" value="${item.stts}" />
		                            </td>
		                            <td align="center">${item.ecId}</td>
		                            <td align="center">${item.ecNameCh}
		                            <input type="hidden" id="${item.ecId}ecNameCh" value="${item.ecNameCh}" />
		                            </td>
		                            <td align="center">${item.ecNameEn}
		                            <input type="hidden" id="${item.ecId}ecNameEn" value="${item.ecNameEn}" />
		                            </td>
		                            <td align="center"><fmt:message key="F0201.field.FEE_STTS.${item.stts}" /></td>
								</tr>
							</c:forEach>
	                    </table>
	            	</form>
	            </div>
	            <!-- Button area ------------------------------------------------------------------------>
	            <div class="btnContent">
	            	<%--啟用--%> 
	                <input class="btnStyle" type="button" name="btn1" value="<fmt:message key="common.btn.Enable" />" onclick="send('enableInit');" /> &nbsp;
	                <%--暫停--%>
	                <input class="btnStyle" type="button" name="btn1" value="<fmt:message key="common.btn.Pause" />"  onclick="send('pauseInit');" /> &nbsp;
	            </div>
	       
	        </div>
	        <!-- Footer ============================================================================================== -->
	        <div class="footer_line"></div>
	    </div>
	</body>
</html>