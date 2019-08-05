<%
/*
 * @(#)0204/00.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description:
 * 交易限額修改
 *
 * Modify History:
 * v1.00, 2016/02/22, Evan
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
		var errorMsg = "";
		var rowMsg = "<fmt:message key="message.alert.row" />";  //第#筆
		var mustBeIntegerMsg = "<fmt:message key="message.alert.mustBeInteger" />"; //必須為正整數
		var notZeroMsg = "<fmt:message key="message.alert.notZero" />"; //不可為0
	    var notNullMsg = "<fmt:message key="message.alert.notNull" />"; //不可為空值
	    var notMoreThanLawLimt = "<fmt:message key="message.alert.notMoreThanTheLawLimt" />";//不可太於法定限額
		var limtRule = "<fmt:message key="message.alert.limtRule" />"; //不符合每月限額>=每日限額>=每筆限額
	    
		//修改限額action
	    function send(){
			
			this.checkUpdateData();
			
			if(errorMsg != ""){
				alert(errorMsg);
			}else{
				if(confirm("<fmt:message key="message.cnfm.0005" />"+"??")){ //是否確定變更??
					$("#form1").attr("action", root + "/0204/updateTrnsLimtAcnt.html");
					$("#form1").submit();
				}
			}
		}
		
		//回一頁
		function back(){
			$("#form1").attr("action", root + "/0204/requery.html");
			$("#form1").submit();
		}
		
		//俢改檢核
		function checkUpdateData(){
			
			var selectEcId = "${command.selectEcId}";
			var limtName;
			var limtValue;
			errorMsg = "";
			
			if("showCustAcnt" == ""){
				return false;
			}
			
			if("showCustAcnt" == selectEcId){
				//每筆交易限額, 每日交易限額, 每月交易限額
				var  custAcntTrnsLmt = $("input[name='custAcntTrnsLmt']").map(function(){return $(this).val();}).get();
				var  custAcntDayLmt = $("input[name='custAcntDayLmt']").map(function(){return $(this).val();}).get();
				var  custAcntMnthLmt = $("input[name='custAcntMnthLmt']").map(function(){return $(this).val();}).get();
				//[每筆交易限額,每日交易限額,每月交易限額]
				limtName = ["<fmt:message key="F0204.field.trnsLimt" />:", 
				            "<fmt:message key="F0204.field.dayLimt" />:", 
				            "<fmt:message key="F0204.field.mnthLimt" />:"];
				
				for(var i=0; i<custAcntTrnsLmt.length; i++){
					//console.log(custAcntTrnsLmt[i]+","+custAcntDayLmt[i]+","+custAcntMnthLmt[i]);
					limtValue = [custAcntTrnsLmt[i], custAcntDayLmt[i], custAcntMnthLmt[i]];
					//檢查是否為數字且不為0
					if(!this.isNumber(limtName, limtValue, i+1)){
						continue;
					}
					//限額=>每月>每日>每筆
					this.correctLimt(limtValue,i+1);
				}
				
			}else{
				//每筆交易自定限額, 每日交易自定限額, 每月交易自定限額, 等級
				var custAcntLinkTrnsLmt = $("input[name='custAcntLinkTrnsLmt']").map(function(){return $(this).val();}).get();
				var custAcntLinkDayLmt = $("input[name='custAcntLinkDayLmt']").map(function(){return $(this).val();}).get();
				var custAcntLinkMnthLmt = $("input[name='custAcntLinkMnthLmt']").map(function(){return $(this).val();}).get();
				var custAcntLinkGrad = $("input[name='custAcntLinkGrad']").map(function(){return $(this).val();}).get();
				//[每筆交易限額[自訂限額] ,每日交易限額[自訂限額], 每月交易限額[自訂限額]]
				limtName = ["<fmt:message key="F0204.field.trnsLimt" /><fmt:message key="F0204.field.customLimt" />:", 
				            "<fmt:message key="F0204.field.dayLimt" /><fmt:message key="F0204.field.customLimt" />:", 
				            "<fmt:message key="F0204.field.mnthLimt" /><fmt:message key="F0204.field.customLimt" />:"];
				
				for(var i=0; i<custAcntLinkTrnsLmt.length; i++){
					//console.log(custAcntLinkTrnsLmt[i]+","+custAcntLinkDayLmt[i]+","+custAcntLinkMnthLmt[i]);
					limtValue = [custAcntLinkTrnsLmt[i], custAcntLinkDayLmt[i], custAcntLinkMnthLmt[i]];
					//檢查是否為數字且不為0
					if(!this.isNumber(limtName, limtValue, i+1)){
						continue;
					}
					
					//檢查限額規則
					limtValue = [custAcntLinkTrnsLmt[i], custAcntLinkDayLmt[i], custAcntLinkMnthLmt[i]];
					this.limtRules(limtName, limtValue, custAcntLinkGrad[i], i+1);
					//限額=>每月>每日>每筆
					this.correctLimt(limtValue,i+1);
				}
			}
		}
		
		//檢查是否為數字，且不為0
		function isNumber(limtName, limtValue, row){
			
			var rule1 = /^(0|[1-9][0-9]*)$/;
			var value;
			var pass = true;
			
			for(var i=0; i<limtValue.length; i++){
				
				if($.trim(limtValue[i]) != ""){
					
					value = limtValue[i];
					
					if(value == "0"){
						errorMsg += rowMsg.replace("#", row)+","+limtName[i]+limtValue[i]+","+notZeroMsg+"\n"; //第#筆,限額名稱,不可以為0
						pass = false;
					}else if(!rule1.test(value)){
						errorMsg += rowMsg.replace("#", row)+","+limtName[i]+limtValue[i]+","+mustBeIntegerMsg+"\n"; //第#筆,限額名稱，必須為正整數
						pass = false;
					} 
				}
			}
			
			return pass;
		}
		//檢查限額規則
		//1	連結扣款帳號額度-等級A，須要檢核各「自訂限額」不可大於各對應之「核定限額」。
		//2	連結扣款帳號額度-等級B，須要檢核各「自訂限額」不可大於各對應之「核定限額」。
		function limtRules(limtName, limtValue, grad, row){
			
			var lawLimt = [];
			
			for(var i=0; i<limtValue.length; i++){
				
				if(grad != "C"){
					
					if(limtValue[i] == ''){
						errorMsg += rowMsg.replace("#", row)+","+limtName[i]+limtValue[i]+","+notNullMsg+"\n"; //第#筆,限額名稱,不可為空值
					}else{
						
						lawLimt =  this.getLawLimt(grad);
						lawLimt[i] = lawLimt[i].replace(",","");
						limtValue[i] = limtValue[i].replace(",","");
						
						if(parseInt(limtValue[i]) > parseInt(lawLimt[i])){
							errorMsg += rowMsg.replace("#", row)+","+limtName[i]+limtValue[i]+","+notMoreThanLawLimt+":"+lawLimt[i]+"\n"; //第#筆,限額名稱,不可大於法定限額:xx
						}
					}
				}
			}
		}
		
		//限額規則:每月>=每日>=每筆
		function correctLimt(limtValue, row){
			
			var value = 9999999999999999;
			
			if($.trim(limtValue[2]) == ""){
				limtValue[2] = value;
			}
			if($.trim(limtValue[1]) == ""){
				limtValue[1] = value;
			}
			if($.trim(limtValue[0]) == ""){
				limtValue[0] = value;
			}
			if(!(parseInt(limtValue[2])>=parseInt(limtValue[1]) && parseInt(limtValue[1])>= parseInt(limtValue[0]))){
				errorMsg += rowMsg.replace("#", row)+","+limtRule+"\n"; //第#筆,不符合每月限額>=每日限額>=每筆限額
			}
		}
		
		//取得法定美額, [每筆, 每日, 每月]
		//type 限額等級  A、B、C
		function getLawLimt(type){
			
			var  value = [];
			
			if(type == "A"){
				value =["${command.baseLimt['A'].trnsLimt}", "${command.baseLimt['A'].dayLimt}", "${command.baseLimt['A'].mnthLimt}"];
			}else if(type == "B"){
				value =["${command.baseLimt['B'].trnsLimt}", "${command.baseLimt['B'].dayLimt}", "${command.baseLimt['B'].mnthLimt}"];
			}else if(type == "C"){
				value =["${command.baseLimt['C'].trnsLimt}", "${command.baseLimt['C'].dayLimt}", "${command.baseLimt['C'].mnthLimt}"];
			}
			
			return value;
		}
		
		
	</script> 
</head>
<body>
<!-- Container ============================================================================================== -->
	<div class="container">
		<!-- Content ------------------------------------------------------------------------>
		<div class="mainContent">
			<form method="post" name="form1" id="form1" action="" style="margin: 0;">
				<div class="content">
					<div class="fnctTitle"><fmt:message key="function.Id.F0204" /> ><fmt:message key="common.btn.SetLimt" /></div><%--交易限額管理 > 限額設定 --%>
					<table class="fxdTable" width="980px">
						<tr class="secondaryTitleRow">
							<%--身分證字號 --%>
							<td colspan="6"><fmt:message key="F0204.field.custId" />：${command.custData.custId} ${command.custData.custName}</td>
						</tr>
					</table>
					<c:choose>
						<c:when test="${command.selectEcId eq 'showCustAcnt'}">
							<table class="fxdTable" width="980px">
								<tr class="titleRow">
									<td align="left" colspan="6">&nbsp;<fmt:message key="F0204.field.bankAcntLimt" /></td><%--銀行存款帳號交易限額 --%>
								</tr>
								<tr class="titleRow">
									<td width="13%;" nowrap><fmt:message key="F0204.field.realAcnt" /></td><%--實體帳號 --%>
									<td width="13%;" nowrap><fmt:message key="F0204.field.trnsLimt" />(NT$)</td><%--每筆交易限額(NT$) --%>
									<td width="13%;" nowrap><fmt:message key="F0204.field.dayLimt" />(NT$)</td><%--每日交易限額(NT$) --%>
									<td width="13%;" nowrap><fmt:message key="F0204.field.mnthLimt" />(NT$)</td><%--每月交易限額(NT$) --%>
								</tr>
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
										<c:set value="" var="limtValue" />
										<td align="center">
											<input type="hidden" name="custAcntRealAcnt" value="${custAcnt.id.realAcnt}" />
											<aclFn:realAcntFormate realAcnt="${custAcnt.id.realAcnt}"/>
										</td>
										<td align="center">
										    <c:choose>
										    	<c:when test="${custAcnt.trnsLimt eq '0'}"><c:set var="limtValue" value=""/></c:when>
										    	<c:otherwise><c:set var="limtValue" value="${custAcnt.trnsLimt}"/></c:otherwise>
										    </c:choose>
											<input type="text" size="15" maxlength="15" name="custAcntTrnsLmt" value="${limtValue}" />
										</td>
										<td align="center">
											<c:choose>
										    	<c:when test="${custAcnt.dayLimt eq '0'}"><c:set var="limtValue" value=""/></c:when>
										    	<c:otherwise><c:set var="limtValue" value="${custAcnt.dayLimt}"/></c:otherwise>
										    </c:choose>
											<input type="text" size="15" maxlength="15" name="custAcntDayLmt"  value="${limtValue}" />
										</td>
										<td align="center">
											<c:choose>
										    	<c:when test="${custAcnt.mnthLimt eq '0'}"><c:set var="limtValue" value=""/></c:when>
										    	<c:otherwise><c:set var="limtValue" value="${custAcnt.mnthLimt}"/></c:otherwise>
										    </c:choose>
											<input type="text" size="15" maxlength="15" name="custAcntMnthLmt" value="${limtValue}" />
										</td>
									</tr>
								</c:forEach>
							</table>
						</c:when>
						<c:otherwise>
							<c:set var="ecId" value="${command.selectEcId}"/>
							<table class="fxdTable" width="980px">
								<tr class="titleRow">
									<c:forEach items="${command.custPltfList}" var="pf">
										<c:if test="${pf.id.ecId eq ecId}">
											<td align="left" colspan="11"><fmt:message key="F0204.field.tagCustomLimt"/> - <c:out value="${pf.ecNameCh}"/></td><%--平台連結帳號交易限額 --%>
										</c:if>
									</c:forEach>
								</tr>
								<tr class="titleRow">
									<td rowspan="2" width="10%;" nowrap><fmt:message key="F0204.field.ecUser" /></td><%--平台會員代號 --%>
									<td rowspan="2" width="13%;" nowrap><fmt:message key="F0204.field.realAcnt" /></td><%--實體帳號 --%>
									<td rowspan="2" width="13%;" nowrap><fmt:message key="F0204.field.gradeType" /></td><%--身分認證 --%>
									<td rowspan="2" width="5%;"  nowrap><fmt:message key="F0204.field.grade" /></td><%--等級 --%>
									<td rowspan="2" width="5%;"  nowrap><fmt:message key="F0204.field.stts" /></td><%--狀態 --%>
									<td colspan="2" width="13%;" nowrap><fmt:message key="F0204.field.trnsLimt" />(NT$)</td><%--每筆交易限額(NT$) --%>
									<td colspan="2" width="13%;" nowrap><fmt:message key="F0204.field.dayLimt" />(NT$)</td><%--每日交易限額(NT$) --%>
									<td colspan="2" width="13%;" nowrap><fmt:message key="F0204.field.mnthLimt" />(NT$)</td><%--每月交易限額(NT$) --%>
								</tr>
								<tr class="titleRow">
									<td><fmt:message key="F0204.field.legalLimt" /></td><%--法定限額 --%>
									<td><fmt:message key="F0204.field.customLimt" /></td><%--自訂限額 --%>
									<td><fmt:message key="F0204.field.legalLimt" /></td><%--法定限額 --%>
									<td><fmt:message key="F0204.field.customLimt" /></td><%--自訂限額 --%>
									<td><fmt:message key="F0204.field.legalLimt" /></td><%--法定限額--%>
									<td><fmt:message key="F0204.field.customLimt" /></td><%--自訂限額 --%>
								</tr>
								<c:forEach items="${command.custAcntLink[ecId]}" var="acnt" varStatus="theCount">
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
										<td align="center">
											<input type="hidden" name="custAcntLinkAcntIndt" value="${acnt.acntIndt}"/>
											<input type="hidden" name="custAcntLinkEcUser" value="${acnt.id.ecUser}"/>
											${acnt.id.ecUser}
										</td>
										<td align="center">
											<input type="hidden" name="custAcntLinkRealAcnt" value="${acnt.id.realAcnt}"/>
											<aclFn:realAcntFormate realAcnt="${acnt.id.realAcnt}"/>
										</td>
										<td align="center"><fmt:message key="F0204.field.queryType.${acnt.gradType}"/></td>
										<td align="center">
											<input type="hidden"  name="custAcntLinkGrad" value="${acnt.grad}" />
											${acnt.grad}
										</td>
										<td align="center"><fmt:message key="F0204.field.stts.${acnt.stts}"/></td>
										<td align="right">
											<c:choose>
												<c:when test="${command.baseLimt[grad].trnsLimt  eq '0'}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
												<c:otherwise>&lt;<fmt:formatNumber type="number" pattern="###,###,###" value="${command.baseLimt[grad].trnsLimt}" /></c:otherwise>
											</c:choose>
										</td>
										<td align="center">
											<c:choose>
										    	<c:when test="${acnt.trnsLimt eq '0'}"><c:set var="limtValue" value=""/></c:when>
										    	<c:otherwise><c:set var="limtValue" value="${acnt.trnsLimt}"/></c:otherwise>
										    </c:choose>
										  	<c:choose>
										  		<c:when test="${acnt.stts eq '02'}">
										  			<input type="text" size="6" value="${limtValue}" disabled />
													<input type="hidden"  name="custAcntLinkTrnsLmt" value="${limtValue}"/>
										  		</c:when>
										  		<c:otherwise>
										  			<input type="text" size="6"  maxlength="15" name="custAcntLinkTrnsLmt" value="${limtValue}" />
										  		</c:otherwise>
										  	</c:choose>
										</td>
										<td align="right">
										    <c:choose>
												<c:when test="${command.baseLimt[grad].dayLimt  eq '0'}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
												<c:otherwise>&lt;<fmt:formatNumber type="number" pattern="###,###,###" value="${command.baseLimt[grad].dayLimt}" /></c:otherwise>
											</c:choose>
										</td>
										<td align="center">
											<c:choose>
										    	<c:when test="${acnt.dayLimt eq '0'}"><c:set var="limtValue" value=""/></c:when>
										    	<c:otherwise><c:set var="limtValue" value="${acnt.dayLimt}"/></c:otherwise>
										    </c:choose>
											<c:choose>
										  		<c:when test="${acnt.stts eq '02'}">
										  			<input type="text" size="6"  value="${limtValue}" disabled />
													<input type="hidden"  name="custAcntLinkDayLmt" value="${limtValue}"/>
										  		</c:when>
										  		<c:otherwise>
										  			<input type="text" size="6"	 maxlength="15" name="custAcntLinkDayLmt" value="${limtValue}" />
										  		</c:otherwise>
										  	</c:choose>
										</td>
										<td align="right">
											<c:choose>
												<c:when test="${command.baseLimt[grad].mnthLimt  eq '0'}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
												<c:otherwise>&lt;<fmt:formatNumber type="number" pattern="###,###,###" value="${command.baseLimt[grad].mnthLimt}" /></c:otherwise>
											</c:choose>
										</td>
										<td align="center">
											<c:choose>
										    	<c:when test="${acnt.mnthLimt eq '0'}"><c:set var="limtValue" value=""/></c:when>
										    	<c:otherwise><c:set var="limtValue" value="${acnt.mnthLimt}"/></c:otherwise>
										    </c:choose>
											<c:choose>
										  		<c:when test="${acnt.stts eq '02'}">
										  			<input type="text" size="6"value="${limtValue}" disabled />
													<input type="hidden"  name="custAcntLinkMnthLmt" value="${limtValue}"/>
										  		</c:when>
										  		<c:otherwise>
										  			<input type="text" size="6"	maxlength="15" name="custAcntLinkMnthLmt" value="${limtValue}" />
										  		</c:otherwise>
										  	</c:choose>
										</td>
									</tr>
								</c:forEach>
							</table>
							<table class="fxdTable" width="980px">
								<tr>
									<td class="helperText">
										<!-- 注意事項：<br /> 1. 平台會員代號的自訂限額不得超過該帳號之法定限額。<br /> 2. 若平台會員代號為等級C時, 可將自訂限額設定為空白，即表示該帳號之交易限額為無限額。 -->
										<fmt:message key="F0204.desc.A" />
									</td>
								</tr>
							</table>
						</c:otherwise>
					</c:choose>
				</div>
				<!-- Button area ------------------------------------------------------------------------>
				<div class="btnContent">
					<input class="btnStyle" type="button" name="btn1" value="<fmt:message key="common.btn.OK"/>"  onclick="send();" /> <%--確認 --%>
					&nbsp;
					<input class="btnStyle" type="button" name="btn1" value="<fmt:message key="common.btn.Back"/>"  onclick="back();" /> <%--回上一頁 --%>
					&nbsp;
				</div>
			</form>
		</div>
		<div class="footer_line"></div>
	</div>
</body>
</html>