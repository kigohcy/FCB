<%
/*
 * @(#)0202/00.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 服務狀態管理
 *
 * Modify History:
 * v1.00, 2016/02/17, Evan
 * 	1)First Release
 * v1.01, 2018/03/27
 *  1)移除解鎖/ 會員登入狀態
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
	
	    var goToOrigin;
		// 預設顯示第一個 Tab
		var _showTab = 0; //記錄點選那個頁籤
		var $defaultLi = null;
		var selectEcId = "${command.selectEcId}"; //卻編輯的平台
	
		$(function(){
			//還原資料
		    goToOrigin = function(){
				
				$("input:checkbox").removeAttr('checked');
				$("select").attr("disabled", "disabled");
				
				var ecKey =  $("input:checkbox[name='ecKey']");
				var originEcStts = $("input[name='originEcStts']").map(function(){return $(this).val();}).get();
				
				for(var i=0; i<ecKey.length; i++){
					if(originEcStts[i] != '02'){
						$("input:checkbox[name='ecKey']").eq(i).prop("disabled", "");
					}
					//回復原有狀態
					$("select[name='ecStts']").eq(i).val(originEcStts[i]);
				}
				//acnt 
				var acntKey =  $("input:checkbox[name='acntKey']");
				var originAcntStts = $("input[name='originAcntStts']").map(function(){return $(this).val();}).get();
				for(var i=0; i<acntKey.length; i++){
					if(originAcntStts[i] != '02'){
						$("input:checkbox[name='acntKey']").eq(i).prop("disabled", "");
					}
					//回復原有狀態
					$("select[name='acntStts']").eq(i).val(originAcntStts[i]);
				}
			};
			
			
			if(selectEcId == ""){ // 預設顯示第一個 Tab
				$defaultLi = $("ul.tabs li").eq(_showTab).addClass("active");
				_showTab = 0;
			}else{//修改完，應回到對應的平台tab
				$("ul.tabs li").find("a").each(function(index) {
				    if('#'+selectEcId == $(this).attr('href')){
				    	$defaultLi = $("ul.tabs li").eq(index).addClass("active");
				    	_showTab = index;
				    	return true;
				    }
				});
			}
			
			if($defaultLi != null){
				$($defaultLi.find("a").attr("href")).siblings().hide();
			}
			
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
				
				//不重覆觸發
				if( _showTab != $(this).index()){
					goToOrigin();
				}
				//記錄點選那個頁籤
				_showTab = $(this).index();
				
				return false;
			}).find('a').focus(function() {
				this.blur();
			});
			
			
			
			//查詢條件
			$("#accordion").accordion({
				heightStyle : "content",
				collapsible : true
			}).children(".ui-accordion .ui-accordion-content").css("padding", "0px");
			
			//勾選時變更該行select元素的狀態Enable/Disable
			$("input[name=ecKey]").click(function() {
				var tableRow = $(this).parentsUntil("table", "tr");
				if ($(this).prop("checked")) {
					tableRow.find("select").prop("disabled", "");
					$("input:checkbox[name='acntKey']").prop("checked", "");
					$("input:checkbox[name='acntKey']").prop("disabled", "disabled");
					$("select[name='acntStts']").prop("disabled", "disabled");
					
					var acntKey =  $("input:checkbox[name='acntKey']");
					var originAcntStts = $("input[name='originAcntStts']").map(function(){return $(this).val();}).get();
					for(var i=0; i<acntKey.length; i++){
						//回復原有狀態
						$("select[name='acntStts']").eq(i).val(originAcntStts[i]);
					}
					
				} else {
					goToOrigin();
					tableRow.find("select").prop("disabled", "disabled");
				}
			});
			
			$("input[name=acntKey]").click(function() {
				var tableRow = $(this).parentsUntil("table", "tr");
				
				
				if ($(this).prop("checked")) {
					tableRow.find("select").prop("disabled", "");
				} else {
					tableRow.find("select").prop("disabled", "disabled");
					
					//回復原有狀態
					var originAcntStts = $("input[name='originAcntStts']").map(function(){return $(this).val();}).get();
					$("input[name=acntKey]").each(function(index) {
				        if (!this.checked) {
				        	$("select[name='acntStts']").eq(index).val(originAcntStts[index]);
				        }
				    });
				}
			});
			//預設初始所有 select disabled
			 $("select").attr("disabled", "disabled");
			
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
		        		 required:"<fmt:message key="message.alert.pleaseKeyIn" />"+"<fmt:message key="F0202.field.custId" />",
		        		 ALPHA_CHECKER:"<fmt:message key="F0202.field.custId" /><fmt:message key="message.alert.onlyEN&NUM" />"
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
		//查詢
		function send(){
			
			if($("#form1").valid()){
				$("#form1").attr("action", root + "/0202/query.html");
				$("#form1").submit();
			 }
		}
		
		//回復原有狀態
		function cancel(){
			goToOrigin();
		}
		
		
		//修改約定帳號服務狀態
		function update(){
			
			selectEcId =  $("ul.tabs li a").eq(_showTab).attr("href").replace("#","");
			$("#selectEcId").val(selectEcId);
			
			var pass = false;
			var count = 0;			//計算 有勾選 checkbox 帳號個數
			var platCount = 0;		//計算 非diasable 帳號個數
			var bAcntStts = '';		//記錄前一個狀態
			var checkCount = 0;		//計算有沒有勾選checkbox
			var ecKey =  $("input:checkbox[name='ecKey']");	//所有的 平台 checkbox
			var acntKey =  $("input:checkbox[name='acntKey']");	//所有的 帳號 checkbox
			var acntStts = $("select[name='acntStts']").map(function(){return $(this).val();}).get(); //所有帳號的狀態 ，不包含平台帳號狀態
			
			var ecKeyCount = 0;
			
			//判斷有沒有勾選checkbox
			for(var i=0; i<ecKey.length; i++){
				if(ecKey.eq(i).is(":checked") && !ecKey.eq(i).is(":disabled")){
					checkCount++;
					ecKeyCount ++;
					break;
				}
			}
			
			for(var i=0; i<acntKey.length; i++){
				if(acntKey.eq(i).is(":checked") && !acntKey.eq(i).is(":disabled")){
					checkCount++;
					break;
				}
			}
			
			if(checkCount <= 0){
				alert("<fmt:message key="message.alert.mustToSelect" />"); //請至少選擇一筆資料
				return;
			}
			
			//平台服務態與帳號服務狀態檢核
			for(var i=0; i<ecKey.length; i++){
				if(ecKey.eq(i).val().split(";")[1] == selectEcId){
					//console.log(ecKey.eq(i).val());
					//console.log(ecKey.eq(i).is(":checked"));
					pass = ecKey.eq(i).is(":checked");
					break;
				}
			}
			
			//有勾選checkBox
			if(!pass){
				//TSBACL-34
				var bEcStts = "";
				var originEcStts = $("input[name='originEcStts']").map(function(){return $(this).val();}).get();
				for(var i=0; i<ecKey.length; i++){
					if(ecKey.eq(i).val().split(";")[1] == selectEcId){
						bEcStts = originEcStts[i];
						break;
					}
				}
				if(bEcStts=='00') bEcStts='01';
				else if(bEcStts=='01') bEcStts='00';
				
				for(var i=0; i<acntKey.length; i++){
					if(acntKey.eq(i).val().split(";")[1] == selectEcId){
						//console.log(acntKey.eq(i).val());
						//console.log(acntKey.eq(i).is(":checked"));
						//console.log(acntKey.eq(i).is(":disabled"));
						
						if(!acntKey.eq(i).is(":disabled")){
							//TSBACL-34
							if(acntStts[i] == bEcStts){
								count ++;
							}
							bAcntStts = acntStts[i];
							platCount ++;
						}
					}
				}
				//alert("platCount="+platCount+",count="+count);
				if(platCount  == count){
					
					if(bAcntStts == "00"){
						alert("<fmt:message key="message.cnfm.0007" />");//帳號服務狀態皆為啟用，請直接勾選平台服務狀態為啟用
					}else if(bAcntStts == '01'){
						alert("<fmt:message key="message.cnfm.0008" />");//帳號服務狀態皆為暫停，請直接勾選平台服務狀態為暫停
					}
					
					return;
				}
			}
			
			
			
			if(confirm("<fmt:message key="message.cnfm.0009" />"+"??")){ //是否確定進行平台/帳號服務狀態異動??
				
				if(ecKeyCount > 0){
					$("#whoUpdate").val("EC");
				}else{
					$("#whoUpdate").val("ACNT");
				}
					
				$("#form1").attr("action", root + "/0202/updateAcntStts.html");
				$("#form1").submit();
			}
			
		}
		
		//修改會員服務狀態
		function updaeCustData(type){
			
			if($("ul.tabs li a").length){
				selectEcId =  $("ul.tabs li a").eq(_showTab).attr("href").replace("#","");
				$("#selectEcId").val(selectEcId);
			}
			
			if(type == "start"){//啟用
				$("#form1").attr("action", root + "/0202/updateOpenCustStts.html");
				if(confirm("<fmt:message key="message.cnfm.0006" />"+"??")){ //是否確定進行會員服務狀態為啟用??
					cancel();
					$("#form1").submit();
				}
				
			}else if(type == "stop"){//暫停
				$("#form1").attr("action", root + "/0202/updateStopCustStts.html");
				if(confirm("<fmt:message key="message.cnfm.0004" />"+"??")){ //是否確定進行會員服務狀態為暫停??
					cancel();
					$("#form1").submit();
				}
			}else if(type == "unlock") {
				$("#form1").attr("action", root + "/0202/updateLockCustStts.html");
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
				<div class="fnctTitle"><fmt:message key="function.Id.F0202" /> <c:if test="${not command.initQuery}">> <fmt:message key="common.btn.Query" /></c:if></div> <%--服務狀態管理 >查詢--%>
				<form method="post" name="form1" id="form1" action="" style="margin: 0;">
				
				<input type="hidden" name="whoUpdate"  id="whoUpdate" />
				<div id="accordion">
						<h3><fmt:message key="common.queryCondition"/><%--查詢條件 --%></h3>
					<div style="width: 978px;">
							<input type="hidden" name="selectEcId" id="selectEcId"  />
							<table class="fxdTable" width="100%">
								<tr class="dataRowOdd">
									<td width="90px"><fmt:message key="F0202.field.custId" />*</td><%--身分證字號* --%>
									<td>
										<input type="text" size="11" maxlength="11" name="custId" id="custId"  />
									</td>
								</tr>
							</table>
						<div align="left" style="margin: 10px">
							<input class="btnStyle" type="button" name="btn1" value="<fmt:message key="common.btn.Query" />" <%-- 查詢--%> onclick="send();" />
						</div>
					</div>
				</div>
					<c:if test="${not command.initQuery}">
					<c:choose>
						<c:when test="${not empty command.custData}">
							<table class="fxdTable" style="width: 980px;">
								<tr class="titleRow">
									<td nowrap><fmt:message key="F0202.field.custId" /></td><%--身分證字號 --%>
									<td nowrap><fmt:message key="F0202.field.custName" /></td><%--客戶姓名 --%>
									<td nowrap><fmt:message key="F0202.field.stts" /></td><%--服務狀態 --%>
									<td nowrap><fmt:message key="F0202.field.sttsModfyDttm" /></td><%--狀態異動日期 --%>
									<td nowrap><fmt:message key="F0202.field.vrsn" /></td><%--條款版本 --%>
									<td nowrap width="205px"><fmt:message key="F0202.field.exeFnct" /></td><%--執行功能 --%>
								</tr>
								<tr class="dataRowOdd" align="center">
									<td>${command.custData.custId}</td>
									<td>${command.custData.custName}</td>
									<td><fmt:message key="F0202.field.stts.${command.custData.stts}"/></td>
									<td>
										<fmt:formatDate value="${command.custData.sttsDttm}" pattern="yyyy/MM/dd"/>
									</td>
									<td>${command.custData.vrsn}</td>
									<td>
										<%-- 啟用--%>
										<input class="btnStyle" type="button" name="btn1" value="<fmt:message key="common.btn.Enable"/>" onclick="updaeCustData('start');" <c:if test="${command.custData.stts eq '00' || command.custData.stts eq '02'}" > disabled </c:if> />
										<%-- 暫停--%> 
										<input class="btnStyle" type="button" name="btn2" value="<fmt:message key="common.btn.Pause"/>" onclick="updaeCustData('stop');" <c:if test="${command.custData.stts eq '01' || command.custData.stts eq '02'}" > disabled </c:if>  />
									</td>
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
														<td width="50px;"><fmt:message key="F0202.field.option" /></td><!--選項  -->
														<td width="250px;" nowrap><fmt:message key="F0202.field.platformName" /></td><%--平台名稱 --%>
														<td width="250px;" nowrap><fmt:message key="F0202.field.srvStts" /></td><%--服務狀態--%>
														<td width="250px;" nowrap><fmt:message key="F0202.field.sttsModfyDttm" /></td><%--狀態異動日期--%>
													</tr>
													<tr class="dataRowOdd">
														<td align="center">
															<input type="checkbox" name="ecKey" <c:if test="${item.stts eq '02'}">disabled </c:if> value="${item.id.custId};${item.id.ecId}"/>
														</td>
														<td align="left">${item.ecNameCh}</td>
														<td align="center">
															<input type="hidden" name="originEcStts" value="${item.stts}"/>
															<select name="ecStts" <c:if test="${item.stts eq '02'}">disabled </c:if>>
															    <option value="00" <c:if test="${item.stts eq '00'}">selected</c:if> ><fmt:message key="F0202.field.stts.00"/></option>
															    <option value="01" <c:if test="${item.stts eq '01'}">selected</c:if> ><fmt:message key="F0202.field.stts.01"/></option>
														    	<c:if test="${item.stts eq '02'}">
														    		<option value="02" selected><fmt:message key="F0202.field.stts.02"/></option>
														    	</c:if> 
															</select>
														</td>
														<td align="center">
															<fmt:formatDate value="${item.sttsDttm}" pattern="yyyy/MM/dd"/>
														</td>
													</tr>
												</table>
												<c:set var="key" value="${item.id.ecId}" />
												<table class="fxdTable" width="800px">
													<tr class="titleRow">
														<td width="50px;"><fmt:message key="F0202.field.option" /></td><%--選項  --%>
														<td width="200px;" nowrap><fmt:message key="F0202.field.ecUser" /></td><%--平台會員代號 --%>
														<td width="200px;" nowrap><fmt:message key="F0202.field.realAcnt" /></td><%--實體帳號 --%>
														<td width="200px;" nowrap><fmt:message key="F0202.field.srvStts" /></td><%--服務狀態 --%>
														<td width="150px;" nowrap><fmt:message key="F0202.field.sttsModfyDttm" /></td><%--狀態異動日期 --%>
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
																	<td align="center">
																		<input type="checkbox" name="acntKey" <c:if test="${acnt.stts eq '02'}">disabled </c:if>value="${acnt.id.custId};${acnt.id.ecId};${acnt.id.ecUser};${acnt.id.realAcnt}"/>
																	</td>
																	<td align="left">${acnt.id.ecUser}</td>
																	<td align="left"><aclFn:realAcntFormate realAcnt="${acnt.id.realAcnt}"/></td>
																	<td align="center">
																	    <input type="hidden" name="originAcntStts" value="${acnt.stts}"/>
																		<select name="acntStts" <c:if test="${acnt.stts eq '02'}">disabled </c:if>>
																		    <option value="00" <c:if test="${acnt.stts eq '00'}">selected</c:if> ><fmt:message key="F0202.field.stts.00"/></option>
																		    <option value="01" <c:if test="${acnt.stts eq '01'}">selected</c:if> ><fmt:message key="F0202.field.stts.01"/></option>
																	    	<c:if test="${acnt.stts eq '02'}">
																	    		<option value="02"  selected ><fmt:message key="F0202.field.stts.02"/></option>
																	    	</c:if>
																		</select>
																	</td>
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
												<c:if test="${not empty command.custAcntLink[key]}">
													<%--確定--%>
													<input class="btnStyle btnSpace" type="button" name="btn31" value="<fmt:message key="common.btn.OK" />"  onclick="update('${key}');" />
													<%--取消 --%>
													<input class="btnStyle btnSpace" type="button" name="btn32" value="<fmt:message key="common.btn.Cancel" />"  onclick="cancel();" />
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
			</form>
			</div>
		</div>
		<div class="footer_line"></div>
	</div>
	</body>
</html>
