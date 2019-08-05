<%
/*
 * @(#)aclink/02.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 約定帳戶選擇
 *
 * Modify History:
 * v1.00, 2016/04/08, Eason Hsu
 * 	1) First Release
 * v1.01, 2616/11/18, Eason Hsu
 *  1) TSBACL-131, 同一組 OTP 驗證碼不可重覆驗證
 * v1.02, 2016/12/01, Eason Hsu
 *   1) TSBACL-136, 連結綁定帳號 OTP 驗證流程調整
 * v1.03, 2016/12/01, Eason Hsu
 *   1) TSBACL-137, 連結綁定帳號驗證方式調整
 * v1.04, 2016/12/15, Eason Hsu
 * 	1) TSBACL-141, 帳號連結綁定流程頁面提交時, 需阻擋重覆提交
 * V2.00, 2018/02/05
 *  1) 一銀家宇說只會有 B類限額，如手機電話為空白，則不允許進行綁定
 *
*/
%>

<%@ page language="java" contentType="text/html; charset=BIG5" pageEncoding="BIG5"%>
<%@ taglib uri="/WEB-INF/tlds/realAcntFormat.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tlds/stringMask.tld" prefix="mask"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>約定帳戶選擇</title>

<%@include file="/include/initial.jsp" %>

<style>
	input[type="radio"] {
		margin-bottom: 10px;
	}
	
	#OTPCode {
		height: 27px;
		border-radius: 5px;
	}
</style>

<script type="text/javascript">
	var iOriginalTimeout = '${sessionScope.iOriginalTimeout}';
    var iOtpTimeout = iOriginalTimeout;
    var dTimeout = new Date();
    var isVerify = false;
    var isResendOtp = false;
    var breakTime = false;	<%-- v1.02, 終止計時 falg  --%>
    var isOTP = '<c:out value="${link.idetityAuthType }" />';

    $(function() {

		<%-- v1.03 --%>
        if(isOTP == '01') {
        	isVerify = true;
			$("#sendOTP").hide();
			$("#btnIgnore").hide();
			$("#idetityAuthType").val("01");
			$("#success").text("OTP 驗證成功").show();
			//$("#submit").removeClass("btnNext_G").addClass("btnNext");
        }

	    <%-- 連結帳號設定 (重新登入) --%>
		$("#btnRelogin").click(function () {
			$("#_service").val("reeBankLogin");

			<%-- v1.04 --%>
			confirmBindInfo("form1");
		});

		<%-- 連結帳號設定 (取消) --%>
		$("#btnCancel").click(function () {
			$("#_service").val("cancel");

			<%-- v1.04 --%>
			confirmBindInfo("form1");
		});

		<%-- 連結帳號設定 (下一步) --%>
		$("#btnNext").click(function() {
			<%-- V2.00, 2018/02/05  一銀家宇說只會有 B類限額，如手機電話為空白，則不允許進行綁定 Begin --%>
			//var hasOTP = "${link.idetityAuthType ne '02' and link.tlxNo ne ''}";
			var hasOTP = "true";//強迫一定要OTP
			<%-- V2.00, 2018/02/05  一銀家宇說只會有 B類限額，如手機電話為空白，則不允許進行綁定 End --%>
			//alert("hasOTP: " + hasOTP );
			if (hasOTP == "true") {
				$("h3:eq(1)").trigger("click");
				
			} else {
				
				var isChecked = $("input[name=account]").is(":checked");

				if(!isChecked) {
					alert("請選擇一筆連結綁定帳號!");
					return;
				} else {
					confirmBindInfo("form1");
				}	
			}
		})
		
		<%-- OTP 驗證 (發送驗證碼) --%>
		$("#sendOTP").click(function() {
			$("#OTPCode").prop("value", "");
			
			$.ajax({
				url: "<%=root%>/portal",
				type: "POST",
				cache: false,
				dataType: "text",
				data: {_service: "sendOtpCode"},
				success: function(data) {
					$("#OTPCode").prop("disabled", false);
					$("#sendOTP").hide();
					$("#error").hide();
					$("#confirmOTP").show();
					$("#resendOTP").show();
					$("#clock").show();
					isVerify = false;
					iOtpTimeout = iOriginalTimeout;
					countOtpTimeout();
				}
			});
			
		});

		$("#resendOTP").click(function(){

			<%-- v1.02, OTP驗證碼, 有效期限未到期前不可重發驗證碼 --%>
			if(iOtpTimeout > -1) {
				return;
			}
			
			$("#OTPCode").prop("value", "");

			$.ajax({
				url: "<%=root%>/portal",
				type: "POST",
				cache: false,
				dataType: "text",
				data: {_service: "sendOtpCode"},
				success: function(data) {
					$("#error").hide();
					$("#clock").show();
					
					<%-- v1.02 --%>
					$("#confirmOTP").removeClass("btnOtpVerify_G").addClass("btnOtpVerify");
		        	$("#resendOTP").removeClass("btnResendOTP").addClass("btnResendOTP_G");
		        	
					isVerify = false;
					iOtpTimeout = iOriginalTimeout;
					countOtpTimeout();
				}
			});
		});

		<%-- OTP 驗證 (OTP驗證) --%>
		$("#confirmOTP").click(function() {
			<%-- v1.02, 超過 OTP 有效期限不可驗證 OTP --%>
			if(iOtpTimeout == -1) {
				return;
			}
			
			var otpCode = $("#OTPCode").val();
			
			if(otpCode != "") {
				$.ajax({
					url: "<%=root%>/portal",
					type: "POST",
					cache: false,
					dataType: "text",
					data: {_service: "confirmOTP", OTPCode: $("#OTPCode").val()},
					success: function(data) {
						if (data == "Y") {
							var checkedVal = $("input[type=radio]:checked").val();
							<%-- v1.01, 驗證成功後才可執行下一步 --%>
							isVerify = true;
							<%-- v1.02, 驗證成功停止計時 --%>
							breakTime = true;
							
							//$("#submit").prop("disabled", false);
							$("#clock").hide();
							$("#confirmOTP").hide();
							$("#resendOTP").hide();

							<%-- v1.02 --%>					
							$("#error").hide();
							$("#success").text("OTP 驗證成功").show();
							//$("#submit").removeClass("btnNext_G").addClass("btnNext");
							
							<%-- v1.03 --%>
							$("#idetityAuthType").val("01");
							
							<%-- v1.03 --%>
							<%-- 至下一步--%>
							confirmBindInfo("form1");
						} else {
							<%-- v1.02, OTP 驗證失敗需仍需顯示倒數計時 --%>
							$("#error").text("手機驗證失敗").show();
						}
					}
				});

			} else {
				alert("請輸入手機驗證碼!!");
				return;
			}
			
		});

		<%-- OTP 驗證 (略過) --%>
		$("#btnIgnore").click(function() {
			<%-- v1.03 --%>
			$("#idetityAuthType").val("00");

			confirmBindInfo("form1");
		});

		<%-- OTP 驗證 (下一步) --%>
		/*$("#submit").click(function() {
			// OTP未驗證成功, 不能執行下一步
			if(isVerify == false) {
				alert("您尚未完成手機驗證");
				return;
			}
			
			confirmBindInfo("form1");
		});*/
	
		$("#accordion").accordion({
			heightStyle : "content",
			beforeActivate : function(event, ui) {
				var isChecked = $("input[name=account]").is(":checked");

				if(!isChecked) {
					alert("請選擇一筆連結綁定帳號!");
					event.preventDefault();
				}
			}
		}).children(".ui-accordion-content-active").css("padding", "0px");
		
	});

	function confirmBindInfo(from) {
		<%-- v1.04, form submit 前增加 Mask loding --%>
		$("#msak_loding").fadeIn(function () {
			$("#" + from).prop("action", "<%=root%>/portal");
			$("#" + from).submit();
		});
		
	}

	<%-- OTP timeout --%>
	function countOtpTimeout() {
		<%-- v1.02, OTP 停止倒數計時 --%>
		if(breakTime) {
			return;
		}
		
        dTimeout.setTime(iOtpTimeout * 1000);
        var min = (dTimeout.getMinutes() < 10) ? "0" + dTimeout.getMinutes() : dTimeout.getMinutes();
        var sec = (dTimeout.getSeconds() < 10) ? "0" + dTimeout.getSeconds() : dTimeout.getSeconds();

        $("#clock").text(min + ":" + sec);
        
        iOtpTimeout--;

        if (iOtpTimeout == -1) {
        	$("#clock").hide();
        	<%-- v1.02 --%>
        	$("#confirmOTP").removeClass("btnOtpVerify").addClass("btnOtpVerify_G");
        	$("#resendOTP").removeClass("btnResendOTP_G").addClass("btnResendOTP");
            $("#error").text("手機驗證碼過期, 請重新發送驗證碼").show();
             
            return;
            
        } else {
            setTimeout("countOtpTimeout()", 1000);
            
        }
    }

</script>

</head>
<body>
	<div class="pure-g forfirefox">
		<div id="header" class="pure-u-1">
			<img src='<%=root%>/images/popwin_panel_header.jpg' />
		</div>
	</div>
	<div class="pure-g">
		<div class="pure-u-1 pure-u-sm-3-5 pure-u-lg-5-12 pure-u-xl-1-3">
			
			<%-- v1.03 --%>
			<form name="form1" id="form1" method="post" action="<%=root%>/portal">
				<input type="hidden" id="_service" name="_service" value="confirmBindInfo">
				<input type="hidden" id="idetityAuthType" name="idetityAuthType" value="${link.idetityAuthType }"/>
				
				<div id="container">
					
					<div id="accordion">
						<h3>連結帳號設定</h3>
						<div>
							<div id="content" class="align_center" style="width: 300px;">
								<p class="title">請選擇您要連結之帳號</p>
								<table border="1">
									<tr>
										<th>電商平台</th>
										<td>
											<c:out value="${link.ecName }" />
										</td>
									</tr>
									<tr>
										<th>身分證字號</th>
										<td>
											<c:out value="${link.custId }" />	
										</td>
									</tr>
									<tr>
										<th>電商平台會員帳號</th>
										<td>
											<c:out value="${link.ecUser }" />
										</td>
									</tr>
									<tr>
										<th>手機號碼</th>
										<td>
											<mask:stringMask idStr="${link.tlxNo }" start="4" maskCount="4" symbol="*" />
										</td>
									</tr>
									<tr>
										<th>Email</th>
										<td>
											<mask:stringMask idStr="${link.emailAddr }" start="2" maskCount="4" symbol="*" />
										</td>
									</tr>
									<tr>
										<th>連結帳號</th>
										<td>
											<c:forEach var="i" items="${link.binding.acnts }" varStatus="iStts">
												<c:choose>
													<c:when test="${i.stts eq 'N' }">
														<input type="radio" name="account" value="<c:out value="${i.acnt }"/>" <c:if test="${link.linkAcnt eq i.acnt }">checked</c:if> />&nbsp;<fmt:acntFormat realAcnt="${i.acnt}" />
													</c:when>
													<c:otherwise>
														<input type="radio" name="account" disabled="disabled"/>
														<span style="color:gray">
															<fmt:acntFormat realAcnt="${i.acnt}"/>(已綁定)
														</span>
													</c:otherwise>
												</c:choose>
												<c:if test="${!iStts.last }"> <br></c:if>
											</c:forEach>
										</td>
									</tr>
								</table>
								
								<div id="btnArea" class="text_center margin_top20 ">
									<a href="#" class="btn btnRelogin" id="btnRelogin"></a>
									<a href="#" class="btn btnCancel" id="btnCancel"></a>
									<a href="#" class="btn btnNext" id="btnNext"></a>
								</div>
								<div>
									<div id="content" class="align_center" style="width: 300px;">
										<p><font color="red"></font>請確認您的手機號碼及Email有效性，若您的手機號碼(將於手機驗證使用)或Email(帳戶連結及交易結果通知使用)需要更新，可透過第一銀行全省營業單位、24小時客服專線(02-2181-1111)、個人網路銀行(第e個網)等方式進行變更。</font></p>								
									</div>
								</div>
							</div>
						</div>
						
						<%-- 身分認證方式非 02(晶片卡) 才顯示 OTP 驗證 --%>
						<%-- V2.00, 2018/02/05  一銀家宇說只會有 B類限額，如手機電話為空白，則不允許進行綁定 Begin --%>
							<h3>手機驗證</h3>
					<c:choose>
						<c:when test="${link.tlxNo eq ''}">	
							<div>
								<div id="content" class="align_center" style="width: 300px;">
									<p><font color="red"></font>查無手機號碼，請您先至分行申請手機資料填寫後，再重新申請帳戶連結</font></p>								
								</div>
							</div>								
						</c:when>
						<c:otherwise>	
							<div>
								<div id="content" class="align_center" style="width: 300px;">
									<p>驗證簡訊將發送至您的手機，輸入完畢後請點選確認鍵</p>
									
									<label for="OTPCode"></label>
									<%-- v1.04, 調整 Button 間距 --%>
									<input type="text" id="OTPCode" style="width: 105px; margin-right: 3px" size="8"  maxlength="8" disabled="disabled" />
									<a href="#" class="btn btnSendOTP" 	 id="sendOTP"   style="vertical-align:top;"></a>
									<a href="#" class="btn btnOtpVerify" id="confirmOTP" style="display:none;vertical-align:top;"></a>
									<%-- v1.02 --%>
									<a href="#" class="btn btnResendOTP_G" id="resendOTP" style="display:none;vertical-align:top;"></a>
									<span id="success" class="tsb-text-red btn" style="display:none;vertical-align:clip:;"></span>
									
			                        <p id="clock" style="display: none; padding-left: 50px;">05:00</p>
									<p id="error" class="tsb-text-red" style="display: none; padding-left: 50px;"></p>
									
									<!-- 					
									<div id="btnArea" class="text_center" style="margin-top: 50px;"> -->		
										<!-- <a href="#" class="btn btnSkip" id="btnIgnore"></a>-->
										<!-- 
										<a href="#" class="btn btnNext_G" id="submit"></a>
									</div> -->
								</div>
							</div>
						</c:otherwise>
					</c:choose>
						<%-- V2.00, 2018/02/05  一銀家宇說只會有 B類限額，如手機電話為空白，則不允許進行綁定 End --%>
				</div>
			</form>
		</div>
	</div>
	<div class="pure-g">
		<div id="footer" class="pure-u-1"></div>
	</div>
	<%-- v1.04 --%>
	<div id='msak_loding' class="msak_loding"></div>
</body>
</html>