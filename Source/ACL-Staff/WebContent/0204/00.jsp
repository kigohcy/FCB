<%
/*
 * @(#)0204/00.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description:
 * 交易限額查詢
 *
 * Modify History:
 * v1.00, 2016/02/22, Evan
 * 	1)First Release
 * v1.01, 2018/03/26
 *  1)增加可用餘額顯示
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
			
			var _showTab = 0;
			var $defaultLi = null;
			var selectEcId = "${command.selectEcId}"; //卻編輯的平台
			
			if(selectEcId == ""){ // 預設顯示第一個 Tab
				$defaultLi = $("ul.tabs li").eq(_showTab).addClass("active");
			}else{//編輯回上一頁時，應回到對應的平台tab
				$("ul.tabs li").find("a").each(function(index) {
				    if("#"+selectEcId == $(this).attr("href")){
				    	$defaultLi = $("ul.tabs li").eq(index).addClass("active");
				    	return true;
				    }
				});
			}
			
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
		        		 required:"<fmt:message key="message.alert.pleaseKeyIn" />"+"<fmt:message key="F0204.field.custId" />",
		        		 ALPHA_CHECKER:"<fmt:message key="F0204.field.custId" /><fmt:message key="message.alert.onlyEN&NUM" />"
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
			$('#custId').keyup(function(){
			    this.value = this.value.toUpperCase();
			});
		});
		
		//查詢
		function send(){
			if($("#form1").valid()){ // form validate
				$("#form1").attr("action", root + "/0204/query.html");
				$("#form1").submit();
			}
		}
		
		//修改 查詢
		//selectKey 卻修改的 平台 id
		function updateInit(selectKey){
			$("#selectEcId").val(selectKey);
			$("#form1").attr("action", root + "/0204/updateInit.html");
			$("#form1").submit();
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
				<div class="fnctTitle"><fmt:message key="function.Id.F0204" /><c:if test="${not command.initQuery}"> > <fmt:message key="common.btn.Query" /></c:if></div><%--交易限額管理  > 查詢--%> 
				<div id="accordion">
					<h3><fmt:message key="common.queryCondition"/><%--查詢條件 --%></h3>
					<div style="width: 978px;">
						<form method="post" name="form" id="form1" action="#" style="margin: 0;">
							<input type="hidden" name="selectEcId" id="selectEcId"  />
							<table class="fxdTable" width="100%">
								<tr class="dataRowOdd">
									<td width="90px"><fmt:message key="F0204.field.custId" />*</td><%--身分證字號* --%>
									<td>
										<input type="text"  size="11" maxlength="11" name="custId"  id="custId"  />
									</td>
								</tr>
							</table>
						</form>
						<div align="left" style="margin: 10px">
							<%-- 查詢--%>
							<input class="btnStyle" type="button" name="btn1" value="<fmt:message key="common.btn.Query" />"  onclick="send();" />
						</div>
					</div>
				</div>
				<c:if test="${not command.initQuery}">
					<c:choose>
						<c:when test="${not empty command.custData}">
							<table class="fxdTable" width="980px">
								<tr class="secondaryTitleRow">
									<%--身分證字號 --%>
									<td colspan="6"><fmt:message key="F0204.field.custId" />：${command.custData.custId} ${command.custData.custName}</td>
								</tr>
							</table>
							<p></p>
							<c:choose>
								<c:when test="${not empty command.custPltfList}">
								<div class="abgne_tab">
									<ul class="tabs">
										<li><a href="#showCustAcnt"><fmt:message key="F0204.field.bankAcntLimt" /></a></li><%--銀行存款帳號交易限額 --%>
									    <c:forEach items="${command.custPltfList}" var="item">
									    	<li ><a href="#${item.id.ecId}" ><fmt:message key="F0204.field.tagCustomLimt"/> - <c:out value="${item.ecNameCh}" /></a></li>
									    </c:forEach>
									</ul>
									<div class="tab_container">
										<div id="showCustAcnt" class="tab_content">
											<table class="fxdTable" width="920px">
												<tr class="titleRow">
													<td align="left" colspan="6">&nbsp;<fmt:message key="F0204.field.bankAcntLimt" /></td><%--銀行存款帳號交易限額 --%>
												</tr>
												<tr class="titleRow">
													<td width="13%;" nowrap><fmt:message key="F0204.field.realAcnt" /></td><%--實體帳號 --%>
													<td width="13%;" nowrap><fmt:message key="F0204.field.trnsLimt" />(NT$)</td><%--每筆交易限額(NT$) --%>
													<td width="13%;" nowrap><fmt:message key="F0204.field.dayLimt" />(NT$)</td><%--每日交易限額(NT$) --%>
													<td width="13%;" nowrap><fmt:message key="F0204.field.mnthLimt" />(NT$)</td><%--每月交易限額(NT$) --%>
													<!-- v1.01 增加可用餘額顯示 -->
													<td width="13%;" nowrap><fmt:message key="F0204.field.availableBalanceDay" />(NT$)</td><%--每日剩餘可用餘額(NT$) --%>
													<td width="13%;" nowrap><fmt:message key="F0204.field.availableBalanceMonth" />(NT$)</td><%--每月剩餘可用餘額(NT$) --%>
													<!-- v1.01 增加可用餘額顯示 End-->
												</tr>
												<c:choose>
													<c:when test="${not empty command.custAcntList}">
														<c:forEach items="${command.custAcntList}" var="custAcnt" varStatus="theCount">
															<c:choose>
																<c:when test="${theCount.count % 2 == 1}">
																	<c:set value="dataRowOdd" var="cssClass"></c:set>
																</c:when>
																<c:otherwise>
																	<c:set value="dataRowEven" var="cssClass"></c:set>
																</c:otherwise>
															</c:choose>
															<tr class="${cssClass}">
																<td align="left"><aclFn:realAcntFormate realAcnt="${custAcnt.id.realAcnt}"/></td>
																<td align="right">
																	<c:choose>
																		<c:when test="${custAcnt.trnsLimt  eq '0'}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
																		<c:otherwise><fmt:formatNumber type="number" pattern="###,###,###" value="${custAcnt.trnsLimt}" /></c:otherwise>
																	</c:choose>
																</td>
																<td align="right">
																	<c:choose>
																		<c:when test="${custAcnt.dayLimt  eq '0'}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
																		<c:otherwise><fmt:formatNumber type="number" pattern="###,###,###" value="${custAcnt.dayLimt}" /></c:otherwise>
																	</c:choose>
																</td>
																<td align="right">
																	<c:choose>
																		<c:when test="${custAcnt.mnthLimt  eq '0'}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
																		<c:otherwise><fmt:formatNumber type="number" pattern="###,###,###" value="${custAcnt.mnthLimt}" /></c:otherwise>
																	</c:choose>
																</td>
																<!-- v1.01 增加可用餘額顯示 -->
																<td align="right">
																	<c:choose>
																		<c:when test="${empty custAcnt.availableBalanceDay}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
																		<c:otherwise><fmt:formatNumber type="number" pattern="###,###,###" value="${custAcnt.availableBalanceDay}" /></c:otherwise>
																	</c:choose>
																</td>
																<td align="right">
																	<c:choose>
																		<c:when test="${empty custAcnt.availableBalanceMonth}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
																		<c:otherwise><fmt:formatNumber type="number" pattern="###,###,###" value="${custAcnt.availableBalanceMonth}" /></c:otherwise>
																	</c:choose>
																</td>
																<!-- v1.01 增加可用餘額顯示 End-->
															</tr>
														</c:forEach>
													</c:when>
													<c:otherwise>
														<%--查無符合條件資料--%>
														<tr class="dataRowOdd">
															<td class="noResult" align="center" colspan="4"><fmt:message key="message.sys.NoData" /></td>
														</tr>
													</c:otherwise>
												</c:choose>
											</table>
											<c:if test="${not empty command.custAcntList}">
												<div>
													<%-- 限額設定--%>
													<input class="btnStyle btnSpace" type="button" name="btn1" value="<fmt:message key="common.btn.SetLimt" />"  onclick="updateInit('showCustAcnt');" />
													&nbsp;
												</div>
											</c:if>
										</div>
										 <c:forEach items="${command.custPltfList}" var="item">
										 	<div id="${item.id.ecId}" class="tab_content">
												<c:set var="key" value="${item.id.ecId}" />
												<table class="fxdTable" width="920px">
													<tr class="titleRow">
														<td align="left" colspan="13"><fmt:message key="F0204.field.tagCustomLimt"/> - <c:out value="${item.ecNameCh}" /></td><%--平台連結帳號交易限額 --%>
													</tr>
													<tr class="titleRow">
														<td rowspan="2" width="10%;" nowrap><fmt:message key="F0204.field.ecUser" /></td><%--平台會員代號 --%>
														<td rowspan="2" width="13%;" nowrap><fmt:message key="F0204.field.realAcnt" /></td><%--實體帳號 --%>
														<td rowspan="2" width="9%;" nowrap><fmt:message key="F0204.field.gradeType" /></td><%--身分認證 --%>
														<td rowspan="2" width="5%;"  nowrap><fmt:message key="F0204.field.grade" /></td><%--等級 --%>
														<td rowspan="2" width="5%;"  nowrap><fmt:message key="F0204.field.stts" /></td><%--狀態 --%>
														<td colspan="2" width="13%;" nowrap><fmt:message key="F0204.field.trnsLimt" />(NT$)</td><%--每筆交易限額(NT$) --%>
														<td colspan="3" width="20%;" nowrap><fmt:message key="F0204.field.dayLimt" />(NT$)</td><%--每日交易限額(NT$) --%>
														<td colspan="3" width="20%;" nowrap><fmt:message key="F0204.field.mnthLimt" />(NT$)</td><%--每月交易限額(NT$) --%>
													</tr>
													<tr class="titleRow">
														<td><fmt:message key="F0204.field.legalLimt" /></td><%--法定限額 --%>
														<td><fmt:message key="F0204.field.customLimt" /></td><%--自訂限額 --%>
														<td><fmt:message key="F0204.field.legalLimt" /></td><%--法定限額 --%>
														<td><fmt:message key="F0204.field.customLimt" /></td><%--自訂限額 --%>
														<td><fmt:message key="F0204.field.availableBalance" /></td><%--可用餘額 --%> <!-- v1.01 增加可用餘額顯示 -->
														<td><fmt:message key="F0204.field.legalLimt" /></td><%--法定限額--%>
														<td><fmt:message key="F0204.field.customLimt" /></td><%--自訂限額 --%>
														<td><fmt:message key="F0204.field.availableBalance" /></td><%--可用餘額 --%> <!-- v1.01 增加可用餘額顯示 -->
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
																<c:set var="grad" value="${acnt.grad}" />
																
																<tr class="${cssClass}">
																	<td align="left">${acnt.id.ecUser}</td>
																	<td align="left"><aclFn:realAcntFormate realAcnt="${acnt.id.realAcnt}"/></td>
																	<td align="center"><fmt:message key="F0204.field.queryType.${acnt.gradType}"/></td>
																	<td align="center">${acnt.grad}</td>
																	<td align="center"><fmt:message key="F0204.field.stts.${acnt.stts}"/></td>
																	<td align="right">
																		<c:choose>
																			<c:when test="${command.baseLimt[grad].trnsLimt  eq '0'}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
																			<c:otherwise>&lt;<fmt:formatNumber  type="number" pattern="###,###,###"   value="${command.baseLimt[grad].trnsLimt}" /></c:otherwise>
																		</c:choose>
																	</td>
																	<td align="right">
																		<c:choose>
																			<c:when test="${acnt.trnsLimt  eq '0'}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
																			<c:otherwise><fmt:formatNumber type="number" pattern="###,###,###" value="${acnt.trnsLimt}" /></c:otherwise>
																		</c:choose>
																	</td>
																	<td align="right">
																		<c:choose>
																			<c:when test="${command.baseLimt[grad].dayLimt  eq '0'}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
																			<c:otherwise>&lt;<fmt:formatNumber  type="number" pattern="###,###,###"   value="${command.baseLimt[grad].dayLimt}" /></c:otherwise>
																		</c:choose>
																	</td>
																	<td align="right">
																		<c:choose>
																			<c:when test="${acnt.dayLimt  eq '0'}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
																			<c:otherwise><fmt:formatNumber type="number" pattern="###,###,###" value="${acnt.dayLimt}" /></c:otherwise>
																		</c:choose>
																	</td>
																	<!-- v1.01 增加可用餘額顯示 -->
																	<td align="right">
																		<c:choose>
																			<c:when test="${empty acnt.availableBalanceDay}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
																			<c:otherwise><fmt:formatNumber type="number" pattern="###,###,###" value="${acnt.availableBalanceDay}" /></c:otherwise>
																		</c:choose>
																	</td>
																	<!-- v1.01 增加可用餘額顯示 End-->
																	<td align="right">
																		<c:choose>
																			<c:when test="${command.baseLimt[grad].mnthLimt  eq '0'}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
																			<c:otherwise>&lt;<fmt:formatNumber  type="number" pattern="###,###,###"   value="${command.baseLimt[grad].mnthLimt}" /></c:otherwise>
																		</c:choose>
																	</td>
																	<td align="right">
																		<c:choose>
																			<c:when test="${acnt.mnthLimt  eq '0'}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
																			<c:otherwise><fmt:formatNumber type="number" pattern="###,###,###" value="${acnt.mnthLimt}" /></c:otherwise>
																		</c:choose>
																	</td>
																	<!-- v1.01 增加可用餘額顯示 -->
																	<td align="right">
																		<c:choose>
																			<c:when test="${empty acnt.availableBalanceMonth}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
																			<c:otherwise><fmt:formatNumber type="number" pattern="###,###,###" value="${acnt.availableBalanceMonth}" /></c:otherwise>
																		</c:choose>
																	</td>
																	<!-- v1.01 增加可用餘額顯示 End-->
																</tr>
															</c:forEach>
														</c:when>
														<c:otherwise>
															<%--查無符合條件資料--%>
															<td class="noResult" colspan="11" align="center" ><fmt:message key="message.sys.NoData" /></td>
														</c:otherwise>
													</c:choose>
												</table>
												<c:if test="${not empty command.custAcntLink[key]}">
													<div>
														 <%--限額設定 --%>
														<input class="btnStyle btnSpace" type="button" name="btn1" value="<fmt:message key="common.btn.SetLimt" />" onclick="updateInit('${key}');" />
														&nbsp;
													</div>
												</c:if>
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