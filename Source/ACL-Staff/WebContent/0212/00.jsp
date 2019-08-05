<%
/**
 * @(#) 00.jsp
 *
 * Directions: 訊息代碼管理
 *
 * Copyright (c) 2018 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2018/07/17
 *    1) JIRA-Number, First release
 *
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
			$("#accordion").accordion({
				heightStyle : "content",
				collapsible : true
			}).children(".ui-accordion .ui-accordion-content").css("padding", "0px");

			<%-- 查詢 --%>
			$("#btn1").click(function(){
				var qCodeId = $("#qCodeId").val();

				if (qCodeId == null || qCodeId == "") {
					alert('<fmt:message key="message.alert.pleaseKeyIn" /><fmt:message key="F0212.field.codeId"/>');
					$("#qCodeId").focus();
					return;
				} else {

					$("#form1").prop("action", root + "/0212/query.html");
					$("#form1").submit();
				}

			});

			<%-- 新增 --%>
			$("#btnAdd").click(function(){
				$("#form1").prop("action", root + "/0212/insertInit.html");
				$("#form1").submit();
			});
			
			<%-- 修改 --%>
			$("#btnModify").click(function(){
				process("/0212/updateInit.html");
				
			});
			
			<%-- 刪除 --%>
			$("#btnDelete").click(function(){
				process("/0212/deleteTbCode.html");
				
			});
			
		});
		
		function process(url) {
			var isElement = $("#key").length > 0;

			if(!isElement) {
				alert('<fmt:message key="message.alert.mustToSelect" />');
				return;
			}
			
			var key =  $("input[type=radio]:checked").val();
			var datas= key.split("-"); 
			
			if(url.indexOf('delete') >=0){
				deleteTbCode(datas[1],datas[0]);
			}else{
				modifyTbCode(datas[1],datas[0]);
			}
		}
		
		function modifyTbCode(codeId, codeType){
			$("#codeId").val(codeId);
			$("#codeType").val(codeType);
			$("#form2").prop("action", root + "/0212/updateInit.html");
			$("#form2").submit();
		}
		
		
		function deleteTbCode(codeId, codeType){
			if(confirm("<fmt:message key="message.cnfm.deleteOrNot" />")){
				$("#codeId").val(codeId);
				$("#codeType").val(codeType);
				$("#form2").prop("action", root + "/0212/deleteTbCode.html");
				$("#form2").submit();
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
					<fmt:message key="function.Id.F0212"/>
				</div>
				<%-- 查詢條件區 --%>
				<div id="accordion">
					<h3>
						<%--查詢條件--%>
						<fmt:message key="common.queryCondition" />
					</h3>
					<div style="width:978px;">
						<form method="post" name="form1" id="form1" style="margin: 0;">
							<table class="fxdTable" width="100%">
								<tr class="dataRowOdd">
									<td width="90px">
										<fmt:message key="F0212.field.codeId"/>*
									</td>
									<td>
										<input type="text" size="7" maxlength="7" name="qCodeId" id="qCodeId" value="${command.qCodeId}" />
									</td>
								</tr>
							</table>
						</form>
						<div align="left" style="margin: 10px">
							<%-- 查詢 --%>
							<input class="btnStyle" type="button" name="btn1" id="btn1" value='<fmt:message key="common.btn.Query"/>' />
						</div>
					</div>
				</div>
				<c:if test="${command.queryFlag eq true }">
				<form method="post" name="form2" id="form2">
					<input type="hidden" id="codeId" name="codeId">
					<input type="hidden" id="codeType" name="codeType">
					<table class="fxdTable" width="100%">
						<tr class="titleRow">
							<td><fmt:message key="F0209.field.items"/></td>		<%-- 選項 --%>
							<td><fmt:message key="F0212.field.codeType"/></td>	<%-- 類別 --%>
							<td><fmt:message key="F0212.field.codeId"/></td>	<%-- 訊息代碼 --%>
							<td><fmt:message key="F0212.field.codeDesc"/></td>	<%-- 說明 --%>
						</tr>
						
						<c:forEach var="i" items="${command.tbCodeList }" varStatus="iStts">
							<c:choose>
								<c:when test="${iStts.count % 2 == 1}">
									<c:set value="dataRowOdd" var="cssClass"/>
								</c:when>
								<c:otherwise>
									<c:set value="dataRowEven" var="cssClass"/>
								</c:otherwise>
							</c:choose>
							<tr class="${cssClass }">
								<td align="center">
									<input type="radio" name="key" id="key" value="<c:out value="${i.id.codeId}"/>" <c:if test="${iStts.count eq 1 }">checked</c:if> />
								</td>
								<td align="center">
									<fmt:message key="F0212.field.codeType.${i.codeType }" />
								</td>
								<td nowrap align="center">
									${fn:substring(i.id.codeId, 3, fn:length(i.id.codeId))}
								</td>
								<td>
									<c:out value="${i.codeDesc }" />
								</td>
							</tr>
						</c:forEach>
						
					</table>
					<!-- Button area ------------------------------------------------------------------------>
					<div class="btnContent">
						<%-- 新增 --%>
						<input class="btnStyle" type="button" name="btnAdd" id="btnAdd" value="<fmt:message key="common.btn.Add"/>" />&nbsp;
						<%-- 修改 --%>
						<input class="btnStyle" type="button" name="btnModify" id="btnModify" value="<fmt:message key="common.btn.Modify"/>" />&nbsp;
						<%-- 刪除 --%>
						<input class="btnStyle" type="button" name="btnDelete" id="btnDelete" value="<fmt:message key="common.btn.Delete"/>" />
					</div>
				</form>
				</c:if>
			</div>
	    </div>
	    <div class="footer_line"></div>
	</div>
	
</body>
</html>