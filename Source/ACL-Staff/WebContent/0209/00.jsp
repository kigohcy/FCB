<%
/**
 * @(#) 00.jsp
 *
 * Directions: 平台憑證管理
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 *
 * Modify History:
 *   v1.00, 2016/03/21, Eason Hsu
 *    1) JIRA-Number, First release
 *   v1.01, 2018/03/28
 *    1) 移除憑證申請，改單檔維護
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
				var ecId = $("#ecId").val();

				if (ecId == null || ecId == "") {
					alert('<fmt:message key="message.alert.mustToSelect" />');
					return;
				} else {

					$("#form1").prop("action", root + "/0209/fetchCertList.html");
					$("#form1").submit();
				}

			});

			<%-- 新增 --%>
			$("#btnAdd").click(function(){
				$("#form1").prop("action", root + "/0209/initInsertCert.html");
				$("#form1").submit();
			});
			
			<%-- 修改 --%>
			$("#btnModify").click(function(){
				process("/0209/initUpdateCert.html");
				
			});
			
			<%-- 刪除 --%>
			$("#btnDelete").click(function(){
				process("/0209/deleteCert.html");
				
			});
			<%-- 憑證測試 --%>
			$("#test").click(function(){
				process("/0209/initVerifySignTest.html");
				
			});
			
		});

		<%-- form submit --%> 
		function process(url) {
			if (!checkSelected()) {
				alert('<fmt:message key="message.alert.mustToSelect" />');
				return;
				
			} else {
				if(url.indexOf('delete') >=0 && !confirm("<fmt:message key="message.cnfm.0022" />")){
					return;
				}
				$("#form2").prop("action", root + url);
				$("#form2").submit();
			}
		}
		
		<%-- 檢核是否已選取資料 --%> 
		function checkSelected(){
			var isElement = $("#key").length > 0;

			if(!isElement) {
				return isElement;
			}
			
			var key =  $("input[type=radio]:checked").val();
			var ecId = key.split("@")[0];
			var certCn = key.split("@")[1];

			
			$("#ecId4Update").val(ecId);
			$("#certCn").val(certCn);
			
			return true;
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
					<fmt:message key="function.Id.F0209"/>
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
										<fmt:message key="F0209.field.ecId"/>*
									</td>
									<td>
										<select name="ecId" id="ecId" style="width:150px;">
											<c:forEach var="i" items="${command.datas }">
												<option value='<c:out value="${i.ecId}"/>' <c:if test="${i.ecId eq command.ecId }">selected</c:if> ><c:out value="${i.ecNameCh}" /></option>
											</c:forEach>
										</select>
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
						<input type="hidden" name="ecId4Update" id="ecId4Update" />
						<input type="hidden" name="certCn" id="certCn" />
						
						<table class="fxdTable" width="980px">
							<tr class="secondaryTitleRow">
								<td>
									<fmt:message key="F0209.field.ecId"/>：<c:out value="${command.ecName }" />
								</td>
							</tr>
						</table>
						<table class="fxdTable" width="100%">
							<tr class="titleRow">
								<td><fmt:message key="F0209.field.items"/></td>		<%-- 選項 --%>
								<td><fmt:message key="F0209.field.certName"/></td>	<%-- 憑證識別碼 --%>
								<td><fmt:message key="F0209.field.certSn"/></td>		<%-- 憑證序號 --%>
								<td><fmt:message key="F0209.field.applyDate"/></td>	<%-- 申請日期 --%>
								<td><fmt:message key="F0209.field.sDate"/></td>		<%-- 生效日期 --%>
								<td><fmt:message key="F0209.field.eDate"/></td>		<%-- 到期日期 --%>
							</tr>
							
							<c:forEach var="i" items="${command.ecCertList }" varStatus="iStts">
							
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
										<input type="radio" name="key" id="key" value="<c:out value="${i.id.ecId }"/>@<c:out value="${i.id.certCn}"/>" <c:if test="${iStts.count eq 1 }">checked</c:if> />
									</td>
									<td nowrap align="center">
										<c:out value="${i.id.certCn }" />
									</td>
									<td align="center">
										<c:out value="${i.certSn }" />
									</td>
									<td align="center">
										<fmt:parseDate value="${fn:substring(i.strtDttm,0,8) }" var="date" pattern="yyyyMMdd"/>
										<fmt:formatDate value="${date}" pattern="yyyy/MM/dd" />
									</td>
									<td align="center">
										<fmt:parseDate value="${fn:substring(i.strtDttm,0,8) }" var="date" pattern="yyyyMMdd"/>
										<fmt:formatDate value="${date}" pattern="yyyy/MM/dd" />
									</td>
									<td align="center">
										<fmt:parseDate value="${fn:substring(i.endDttm,0,8) }" var="date" pattern="yyyyMMdd"/>
										<fmt:formatDate value="${date}" pattern="yyyy/MM/dd" />
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
							<%-- 驗章測試 --%>
							<input class="btnStyle" type="button" name="btn5" id="test" value="<fmt:message key="common.btn.CertTest"/>" />&nbsp;
						</div>
					</form>
				
				</c:if>
			</div>
	    </div>
	    <div class="footer_line"></div>
	</div>
	
</body>
</html>