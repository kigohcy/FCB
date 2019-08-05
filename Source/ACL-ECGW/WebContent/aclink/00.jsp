<%
/*
 * @(#)aclink/00.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 同意條款
 *
 * Modify History:
 * v1.00, 2016/04/07, Eason Hsu
 * 	1) First Release
 * v1.01, 2016/10/28, Eason Hsu
 *	1) TSBACL-122, 平台閘道、會員平台，同意條款 PDF 版本號依據 DB 參數(PRVS_VRSN)取得
 * v1.02, 2016/12/15, Eason Hsu
 *  1) TSBACL-141, 帳號連結綁定流程頁面提交時, 需阻擋重覆提交
 * v1.03, 2016/12/21, Jimmy Yen
 *  1) TSBACL-142, (營運) ECGW, AclCust PDF 與 PDF plugin 壓縮檔案大小
*/
%>

<%@ page language="java" contentType="text/html; charset=BIG5" pageEncoding="BIG5"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>約定帳戶同意條款</title>

<%@include file="/include/initial.jsp" %>

<script type="text/javascript">
	$(function() {
		// 調整行動裝置頁面顯示大小
		var md = new MobileDetect(window.navigator.userAgent);
		if ((md.mobile() != null) && ($(window).width() < 568)) {
			// location.reload();
			$("#content").css("margin", "0 5px").removeClass("content");
		} else {
			// location.reload();
			$("#content").css("margin", "0").addClass("content");
		}
		
		$("#btnAgree").click(function() {
			if ($("#chkAgree").prop("checked")) {
				<%-- v1.02, form submit 前增加 Mask loding --%>
				$("#msak_loding").fadeIn(function () {
					$("#_service").val("aclinkAuth");
					$("#form1").submit();
				});

			} else {
				alert('請勾選"我已閱讀並同意以上服務約定條款');
			}
		});
		
		$("#btnDisagree").click(function() {
			<%-- v1.02, form submit 前增加 Mask loding --%>
			$("#msak_loding").fadeIn(function () {
				$("#_service").val("cancel");
				$("#form1").submit();
			});
			
		});
		
	});

    $(document).ready(function() {
        //   Hide the border by commenting out the variable below
        var $on = 'section';
        $($on).css({
            'background': 'none',
            'border': 'none',
            'box-shadow': 'none'
        });
    });

    function sleep(sec) {
	    var time = new Date().getTime();
	    while(new Date().getTime() - time < sec * 1000);
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
			<div id="container">
				<%-- form --%>
				<form name="form1" id="form1" method="post" action="<%=root%>/portal">
					<input type="hidden" name="_service" id="_service" />
					<input type="hidden" name="sessionKey" value="<c:out value="${param.sessionKey}" />" />
				</form>
				<%-- ========== Container ========== --%>
				
				<p class="title">約定連結存款帳戶付款授權驗證服務條款</p>
				<p style="font-size: 10pt;text-align: right;line-height: 20px;">條款版本：<c:out value="${link.prvsVrsn}"/></p>
				<div style="margin: 0 10px;">
					 <table class="noticeTable" cellspacing="0" border="0">
        			<tbody>
	            <tr>
	                <td width="20" style="font-size: 12pt;text-align: left;line-height: 20px;">        
					          一、 
					        </td>
					        <td style="font-size: 12pt;text-align: left;line-height: 20px;">
					        	申請人向電子支付平台申請指定連結申請人於第一商業銀行股份有限公司(以下簡稱 貴公司)之新台幣存款帳戶(以下簡稱連結帳戶)進行付款，經申請人於 貴公司線上驗證平台以個人網路銀行登入及簡訊認證碼驗證機制完成授權驗證後，申請人同意電子支付平台向 貴公司提出之交易指示，視為已經申請人授權同意，該交易指示視為申請人之交易指示。
	        				</td>
	        		</tr>
	        		<tr>
	                <td width="20" style="font-size: 12pt;text-align: left;line-height: 20px;">        
					          二、 
					        </td>
	        				<td style="font-size: 12pt;text-align: left;line-height: 20px;">   
	        					申請人對於電子支付平台向 貴公司提出之交易指示有疑義，應向電子支付平台請求查明。電子支付平台為查明前開疑義，申請人同意 貴公司得提供連結帳戶相關資料予電子支付平台。
	       					</td>
	        		</tr>
	        		<tr>
	                <td width="20" style="font-size: 12pt;text-align: left;line-height: 20px;">        
					          三、 
					        </td>
	        				<td style="font-size: 12pt;text-align: left;line-height: 20px;">   
	        					申請人同意因本交易服務所生之任何紛爭(包括但不限於申請人之連結帳戶遭盜用或冒用等)，均應向電子支付平台提出，並由電子支付平台負責處理，不得向 貴公司主張任何權利。
	        				</td>
	        		</tr>
	        		<tr>
	                <td width="20" style="font-size: 12pt;text-align: left;line-height: 20px;">        
					          四、 
					        </td>
	        				<td style="font-size: 12pt;text-align: left;line-height: 20px;">   
	        					申請人如欲取消連結帳戶之設定，應向電子支平台提出申請。無法向該平台申請者，得向 貴公司客戶服務中心(24小時服務專線：(02)2181-1111)申請。
	       					</td>
	        		</tr>
	        		<tr>
	                <td width="20" style="font-size: 12pt;text-align: left;line-height: 20px;">        
					          五、 
					        </td>
	       					<td style="font-size: 12pt;text-align: left;line-height: 20px;">   
	        					申請人之連結帳戶應有足額款項備付，如因(包含但不限於)連結帳戶餘額不足、遭法院或其他機關扣押，通報為警示帳戶或其他異常情形者， 貴公司得拒絕電子支付平台交易指示。
	       					</td>
	        		</tr>
	        		<tr>
	                <td width="20" style="font-size: 12pt;text-align: left;line-height: 20px;">        
					          六、 
					        </td>
	        				<td style="font-size: 12pt;text-align: left;line-height: 20px;">   
	        					申請人於同一電子支付平台申請連結帳戶進行付款，每一連結帳戶之交易最高限額為每筆新臺幣(以下同)5萬；每日10萬；每月20萬。申請人於不同電子支付平台設定之連結帳戶為同一 貴公司之新台幣存款帳戶，該帳戶交易最高限額應合併計算。
	        				</td>
	            </tr> 
	        		<tr>
	                <td width="20" style="font-size: 12pt;text-align: left;line-height: 20px;">        
					          七、 
					        </td>
	        				<td style="font-size: 12pt;text-align: left;line-height: 20px;">   
	        					本服務條款未盡事宜，悉依 貴公司公告規定、 貴公司「存款業務約定書」所載約定及一般金融慣例辦理。
	        				</td>
	            </tr>   
	        		<tr>
	                <td width="20" style="font-size: 12pt;text-align: left;line-height: 20px;">        
					          八、 
					        </td>
	        				<td style="font-size: 12pt;text-align: left;line-height: 20px;">   
	        					因本服務涉訟時，申請人同意以臺灣臺北地方法院為第一審管轄法院，但法律另有規定專屬管轄者，不在此限。
	        				</td>
	            </tr>   
	        		<tr>
	                <td width="20" style="font-size: 12pt;text-align: left;line-height: 20px;">        
					          九、 
					        </td>
	        				<td style="font-size: 12pt;text-align: left;line-height: 20px;">   
	        					申請人已審閱前開服務條款全部內容，其中第三、五及六條屬本服務條款重要內容，申請人已充分瞭解並同意遵守本服務條款所載事項及內容。
	        				</td>
	            </tr>                   
        		</tbody>
    		</table>
				</div>
				<div class="margin_top20" style="padding-left: 5px; text-align: center;">
					<input type="checkbox" id="chkAgree" class="css-checkbox lrg" />
					<label for="chkAgree" class="css-label lrg web-two-style">我已閱讀並同意以上服務約定條款</label>
				</div>
				<div id="btnArea" class="text_center margin_top20">
					<a href="#" class="btn btnDisagree" id="btnDisagree"></a>
					<a href="#" class="btn btnAgree" id="btnAgree"></a>
				</div>
			</div>
		</div>
	</div>
	<div class="pure-g">
		<div id="footer" class="pure-u-1"></div>
	</div>
	<div id='msak_loding' class="msak_loding"></div>
</body>
</html>