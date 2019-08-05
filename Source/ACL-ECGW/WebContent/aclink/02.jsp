<%
/*
 * @(#)aclink/02.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: ���w�b����
 *
 * Modify History:
 * v1.00, 2016/04/08, Eason Hsu
 * 	1) First Release
 * v1.01, 2616/11/18, Eason Hsu
 *  1) TSBACL-131, �P�@�� OTP ���ҽX���i��������
 * v1.02, 2016/12/01, Eason Hsu
 *   1) TSBACL-136, �s���j�w�b�� OTP ���Ҭy�{�վ�
 * v1.03, 2016/12/01, Eason Hsu
 *   1) TSBACL-137, �s���j�w�b�����Ҥ覡�վ�
 * v1.04, 2016/12/15, Eason Hsu
 * 	1) TSBACL-141, �b���s���j�w�y�{���������, �ݪ��׭��д���
 * V2.00, 2018/02/05
 *  1) �@�Ȯa�t���u�|�� B�����B�A�p����q�ܬ��ťաA�h�����\�i��j�w
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
<title>���w�b����</title>

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
    var breakTime = false;	<%-- v1.02, �פ�p�� falg  --%>
    var isOTP = '<c:out value="${link.idetityAuthType }" />';

    $(function() {

		<%-- v1.03 --%>
        if(isOTP == '01') {
        	isVerify = true;
			$("#sendOTP").hide();
			$("#btnIgnore").hide();
			$("#idetityAuthType").val("01");
			$("#success").text("OTP ���Ҧ��\").show();
			//$("#submit").removeClass("btnNext_G").addClass("btnNext");
        }

	    <%-- �s���b���]�w (���s�n�J) --%>
		$("#btnRelogin").click(function () {
			$("#_service").val("reeBankLogin");

			<%-- v1.04 --%>
			confirmBindInfo("form1");
		});

		<%-- �s���b���]�w (����) --%>
		$("#btnCancel").click(function () {
			$("#_service").val("cancel");

			<%-- v1.04 --%>
			confirmBindInfo("form1");
		});

		<%-- �s���b���]�w (�U�@�B) --%>
		$("#btnNext").click(function() {
			<%-- V2.00, 2018/02/05  �@�Ȯa�t���u�|�� B�����B�A�p����q�ܬ��ťաA�h�����\�i��j�w Begin --%>
			//var hasOTP = "${link.idetityAuthType ne '02' and link.tlxNo ne ''}";
			var hasOTP = "true";//�j���@�w�nOTP
			<%-- V2.00, 2018/02/05  �@�Ȯa�t���u�|�� B�����B�A�p����q�ܬ��ťաA�h�����\�i��j�w End --%>
			//alert("hasOTP: " + hasOTP );
			if (hasOTP == "true") {
				$("h3:eq(1)").trigger("click");
				
			} else {
				
				var isChecked = $("input[name=account]").is(":checked");

				if(!isChecked) {
					alert("�п�ܤ@���s���j�w�b��!");
					return;
				} else {
					confirmBindInfo("form1");
				}	
			}
		})
		
		<%-- OTP ���� (�o�e���ҽX) --%>
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

			<%-- v1.02, OTP���ҽX, ���Ĵ���������e���i���o���ҽX --%>
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

		<%-- OTP ���� (OTP����) --%>
		$("#confirmOTP").click(function() {
			<%-- v1.02, �W�L OTP ���Ĵ������i���� OTP --%>
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
							<%-- v1.01, ���Ҧ��\��~�i����U�@�B --%>
							isVerify = true;
							<%-- v1.02, ���Ҧ��\����p�� --%>
							breakTime = true;
							
							//$("#submit").prop("disabled", false);
							$("#clock").hide();
							$("#confirmOTP").hide();
							$("#resendOTP").hide();

							<%-- v1.02 --%>					
							$("#error").hide();
							$("#success").text("OTP ���Ҧ��\").show();
							//$("#submit").removeClass("btnNext_G").addClass("btnNext");
							
							<%-- v1.03 --%>
							$("#idetityAuthType").val("01");
							
							<%-- v1.03 --%>
							<%-- �ܤU�@�B--%>
							confirmBindInfo("form1");
						} else {
							<%-- v1.02, OTP ���ҥ��ѻݤ�����ܭ˼ƭp�� --%>
							$("#error").text("������ҥ���").show();
						}
					}
				});

			} else {
				alert("�п�J������ҽX!!");
				return;
			}
			
		});

		<%-- OTP ���� (���L) --%>
		$("#btnIgnore").click(function() {
			<%-- v1.03 --%>
			$("#idetityAuthType").val("00");

			confirmBindInfo("form1");
		});

		<%-- OTP ���� (�U�@�B) --%>
		/*$("#submit").click(function() {
			// OTP�����Ҧ��\, �������U�@�B
			if(isVerify == false) {
				alert("�z�|�������������");
				return;
			}
			
			confirmBindInfo("form1");
		});*/
	
		$("#accordion").accordion({
			heightStyle : "content",
			beforeActivate : function(event, ui) {
				var isChecked = $("input[name=account]").is(":checked");

				if(!isChecked) {
					alert("�п�ܤ@���s���j�w�b��!");
					event.preventDefault();
				}
			}
		}).children(".ui-accordion-content-active").css("padding", "0px");
		
	});

	function confirmBindInfo(from) {
		<%-- v1.04, form submit �e�W�[ Mask loding --%>
		$("#msak_loding").fadeIn(function () {
			$("#" + from).prop("action", "<%=root%>/portal");
			$("#" + from).submit();
		});
		
	}

	<%-- OTP timeout --%>
	function countOtpTimeout() {
		<%-- v1.02, OTP ����˼ƭp�� --%>
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
            $("#error").text("������ҽX�L��, �Э��s�o�e���ҽX").show();
             
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
						<h3>�s���b���]�w</h3>
						<div>
							<div id="content" class="align_center" style="width: 300px;">
								<p class="title">�п�ܱz�n�s�����b��</p>
								<table border="1">
									<tr>
										<th>�q�ӥ��x</th>
										<td>
											<c:out value="${link.ecName }" />
										</td>
									</tr>
									<tr>
										<th>�����Ҧr��</th>
										<td>
											<c:out value="${link.custId }" />	
										</td>
									</tr>
									<tr>
										<th>�q�ӥ��x�|���b��</th>
										<td>
											<c:out value="${link.ecUser }" />
										</td>
									</tr>
									<tr>
										<th>������X</th>
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
										<th>�s���b��</th>
										<td>
											<c:forEach var="i" items="${link.binding.acnts }" varStatus="iStts">
												<c:choose>
													<c:when test="${i.stts eq 'N' }">
														<input type="radio" name="account" value="<c:out value="${i.acnt }"/>" <c:if test="${link.linkAcnt eq i.acnt }">checked</c:if> />&nbsp;<fmt:acntFormat realAcnt="${i.acnt}" />
													</c:when>
													<c:otherwise>
														<input type="radio" name="account" disabled="disabled"/>
														<span style="color:gray">
															<fmt:acntFormat realAcnt="${i.acnt}"/>(�w�j�w)
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
										<p><font color="red"></font>�нT�{�z��������X��Email���ĩʡA�Y�z��������X(�N�������Ҩϥ�)��Email(�b��s���Υ�����G�q���ϥ�)�ݭn��s�A�i�z�L�Ĥ@�Ȧ������~���B24�p�ɫȪA�M�u(02-2181-1111)�B�ӤH�����Ȧ�(��e�Ӻ�)���覡�i���ܧ�C</font></p>								
									</div>
								</div>
							</div>
						</div>
						
						<%-- �����{�Ҥ覡�D 02(�����d) �~��� OTP ���� --%>
						<%-- V2.00, 2018/02/05  �@�Ȯa�t���u�|�� B�����B�A�p����q�ܬ��ťաA�h�����\�i��j�w Begin --%>
							<h3>�������</h3>
					<c:choose>
						<c:when test="${link.tlxNo eq ''}">	
							<div>
								<div id="content" class="align_center" style="width: 300px;">
									<p><font color="red"></font>�d�L������X�A�бz���ܤ���ӽФ����ƶ�g��A�A���s�ӽбb��s��</font></p>								
								</div>
							</div>								
						</c:when>
						<c:otherwise>	
							<div>
								<div id="content" class="align_center" style="width: 300px;">
									<p>����²�T�N�o�e�ܱz������A��J��������I��T�{��</p>
									
									<label for="OTPCode"></label>
									<%-- v1.04, �վ� Button ���Z --%>
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
						<%-- V2.00, 2018/02/05  �@�Ȯa�t���u�|�� B�����B�A�p����q�ܬ��ťաA�h�����\�i��j�w End --%>
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