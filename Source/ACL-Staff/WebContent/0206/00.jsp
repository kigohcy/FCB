<%
/*
 * @(#)0206/00.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description:
 * 服務狀態查詢
 *
 * Modify History:
 * v1.00, 2016/02/17, Evan
 * 	1)First Release
 * v1.01, 2018/03/27
 *  1)移除會員登入狀態
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
			// 預設顯示第一個 Tab
			var _showTab = 0;
			var $defaultLi = $("ul.tabs li").eq(_showTab).addClass("active");
			$($defaultLi.find("a").attr("href")).siblings().hide();

			// 當 li 頁籤被點擊時...
			// 若要改成滑鼠移到 li 頁籤就切換時, 把 click 改成 mouseover
			$("ul.tabs li").click(function() {
				// 找出 li 中的超連結 href(#id)
				var $this = $(this), _clickTab = $this.find("a").attr("href");
				// 把目前點擊到的 li 頁籤加上 .active
				// 並把兄弟元素中有 .active 的都移除 class
				$this.addClass("active").siblings(".active").removeClass("active");
				// 淡入相對應的內容並隱藏兄弟元素
				$(_clickTab).stop(false, true).fadeIn().siblings().hide();

				return false;
			}).find("a").focus(function() {
				this.blur();
			});

			$("#accordion").accordion({
				heightStyle : "content",
				collapsible : true
			}).children(".ui-accordion .ui-accordion-content").css("padding", "0px");
			
			//頁面檢核
			$("#form1").validate({
		         rules: {
		        	 custId:{
		        		 required: true,
		        		 ALPHA_CHECKER: true
		         	}
		         },
		         messages:{
		        	 custId:{
		        		 //請輸入身分證字號
		        		 required:"<fmt:message key="message.alert.pleaseKeyIn" />"+"<fmt:message key="F0206.field.custId" />",
		        		 ALPHA_CHECKER:"<fmt:message key="F0206.field.custId" /><fmt:message key="message.alert.onlyEN&NUM" />"
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
			
			//轉大寫
			$("#custId").keyup(function(){
			    this.value = this.value.toUpperCase();
			});
		});
	
		//服務管理查詢
		function send(){
			if($("#form1").valid()){
				$("#form1").attr("action", root + "/0206/query.html");
				$("#form1").submit();
			}
		}
	</script>
</head>
	<body>
	  <!-- Container ============================================================================================== -->
	<div class="container">
		<!-- Menu --------------------------------------------------------------------------->
		<!-- Content ------------------------------------------------------------------------>
		<div class="mainContent">
			<div class="content">
				<div class="fnctTitle"><fmt:message key="function.Id.F0206" /> <c:if test="${not command.initQuery}">> <fmt:message key="common.btn.Query" /></c:if> </div> <%--服務狀態查詢  > 查詢--%>
				<div id="accordion">
					<h3><fmt:message key="common.queryCondition"/><%--查詢條件 --%></h3>
					<div style="width: 978px;">
						<form method="post" name="form" id="form1" action="#" style="margin: 0;">
							<table class="fxdTable" width="100%">
								<tr class="dataRowOdd">
									<td width="90px"><fmt:message key="F0206.field.custId" />*</td><%--身分證字號* --%>
									<td>
										<input type="text"  size="11" maxlength="11" name="custId" id="custId"  />
									</td>
								</tr>
							</table>
						</form>
						<div align="left" style="margin: 10px">
							<input class="btnStyle" type="button" name="btn1" value="<fmt:message key="common.btn.Query" />" <%-- 查詢--%> onclick="send();" />
						</div>
					</div>
				</div>
				<c:if test="${not command.initQuery}">
					<c:choose>
						<c:when test="${not empty command.custData}">
							<table class="fxdTable" width="800px">
								<tr class="titleRow">
									<td nowrap><fmt:message key="F0206.field.custId" /></td><%--身分證字號 --%>
									<td nowrap><fmt:message key="F0206.field.custName" /></td><%--客戶姓名 --%>
									<td nowrap><fmt:message key="F0206.field.stts" /></td><%--服務狀態 --%>
									<td nowrap><fmt:message key="F0206.field.sttsModfyDttm" /></td><%--狀態異動日期 --%>
									<td nowrap><fmt:message key="F0206.field.vrsn" /></td><%--條款版本 --%>
								</tr>
								<tr class="dataRowOdd" align="center">
									<td>${command.custData.custId}</td>
									<td>${command.custData.custName}</td>
									<td><fmt:message key="F0206.field.stts.${command.custData.stts}"/></td>
									<td>
										<fmt:formatDate value="${command.custData.sttsDttm}" pattern="yyyy/MM/dd"/> 
									</td>
									<td>${command.custData.vrsn}</td>
								</tr>
							</table>
							<p></p>
							<c:choose>
								<c:when test="${not empty command.custPltfList}">
								<div class="abgne_tab">
									<ul class="tabs">
									    <c:forEach items="${command.custPltfList}" var="item">
									    	<li><a href="#${item.id.ecId}">${item.ecNameCh}</a></li>
									    </c:forEach>
									</ul>
									<div class="tab_container">
										 <c:forEach items="${command.custPltfList}" var="item">
										 	<div id="${item.id.ecId}" class="tab_content">
										 		<table class="fxdTable" width="800px">
													<tr class="titleRow">
														<td width="250px;" nowrap><fmt:message key="F0206.field.platformName" /></td><%--平台名稱 --%>
														<td width="250px;" nowrap><fmt:message key="F0206.field.srvStts" /></td><%--服務狀態--%>
														<td width="250px;" nowrap><fmt:message key="F0206.field.sttsModfyDttm" /></td><%--狀態異動日期--%>
													</tr>
													<tr class="dataRowOdd">
														<td align="left">${item.ecNameCh}</td>
														<td align="center"><fmt:message key="F0206.field.stts.${item.stts}"/></td>
														<td align="center">
															<fmt:formatDate value="${item.sttsDttm}" pattern="yyyy/MM/dd"/>
														</td>
													</tr>
												</table>
												<c:set var="key" value="${item.id.ecId}" />
												<table class="fxdTable" width="800px">
													<tr class="titleRow">
														<td width="200px;" nowrap><fmt:message key="F0206.field.ecUser" /></td><%--平台會員代號 --%>
														<td width="200px;" nowrap><fmt:message key="F0206.field.realAcnt" /></td><%--實體帳號 --%>
														<td width="200px;" nowrap><fmt:message key="F0206.field.srvStts" /></td><%--服務狀態 --%>
														<td width="150px;" nowrap><fmt:message key="F0206.field.sttsModfyDttm" /></td><%--狀態異動日期 --%>
													</tr>
													<c:choose>
														<c:when test="${not empty command.custAcntLink[key]}">
															<c:forEach items="${command.custAcntLink[key]}" var="acnt" varStatus="theCount">
																<c:choose>
																	<c:when test="${theCount.count % 2 == 1}">
																		<c:set value="dataRowOdd" var="cssClass"></c:set>
																	</c:when>
																	<c:otherwise>
																		<c:set value="dataRowEven" var="cssClass"></c:set>
																	</c:otherwise>
																</c:choose>
																<tr class="${cssClass}">
																	<td align="left">${acnt.id.ecUser}</td>
																	<td align="left"><aclFn:realAcntFormate realAcnt="${acnt.id.realAcnt}"/></td>
																	<td align="center"><fmt:message key="F0206.field.stts.${acnt.stts}"/></td>
																	<td align="center">
																		<fmt:formatDate value="${acnt.sttsDttm}" pattern="yyyy/MM/dd"/>
																	</td>
																</tr>
															</c:forEach>
														</c:when>
														<c:otherwise>
															<%--查無符合條件資料--%>
															<td class="noResult" colspan="4" align="center" ><fmt:message key="message.sys.NoData" /></td>
														</c:otherwise>
													</c:choose>
												</table>
										 	</div>
									  	 </c:forEach>
									</div>
								</div>
								</c:when>
								<c:otherwise>	
									<div class="abgne_tab">
										<ul class="tabs">
										    <li><a href="#noData"><fmt:message key="message.sys.unbound" /></a></li><!-- 尚未綁 定 -->
										</ul>
										<div class="tab_container">
											<div id="#noData" class="tab_content"></div>
										</div>
									</div>	
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
								<%--查無符合條件資料--%>
								<div class="noResult" align="center" ><fmt:message key="message.sys.NoData" /></div>
						</c:otherwise>
					</c:choose>
				</c:if>	
			</div>
		</div>
		<div class="footer_line"></div>
	</div>
	</body>
</html>