<%
/*
 * @(#)0205/00.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description:
 * 電商平台資料查詢
 *
 * Modify History:
 * v1.00, 2016/02/05, Evan
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
	
    /**
     *open dialog
     *minFee	     比率收費下限
     *maxFee	     比率收費上限
     *minTax	     繳費稅 比率收費下限
	 *maxTax	     繳費稅 比率收費上限
     *linkLimit      使用者可綁定帳戶數
     *entrId 	     公司統編
     *contact 	     聯絡人
     *tel      	     聯絡電話
     *email		     電子郵件
     *actvSendId     啟用建檔人員
     *actvAprvId     啟用核可人員 
     *actvAprvDttm   啟用時間
     *trmnSendId     終止建檔人員 
     *trmnAprvId     終止核可人員
     *trmnAprvDttm   終止時間
     */
	function openEcDialog(minFee, maxFee, minTax, maxTax, linkLimit, entrId, contact, tel, email, actvSendId, actvAprvId, actvAprvDttm, trmnSendId, trmnAprvId, trmnAprvDttm){
		$("#minFee").html(minFee);
		$("#maxFee").html(maxFee);
		$("#minTax").html(minTax);
		$("#maxTax").html(maxTax);
		$("#linkLimit").html(linkLimit);
		$("#entrId").html(entrId);
		$("#contact").html(contact);
		$("#tel").html(tel);
		$("#email").html(email);
        $("#actvSendId").html(actvSendId);
        $("#actvAprvId").html(actvAprvId);
        $("#actvAprvDttm").html(actvAprvDttm);
        $("#trmnSendId").html(trmnSendId);
        $("#trmnAprvId").html(trmnAprvId);
        $("#trmnAprvDttm").html(trmnAprvDttm);
        
			$("#ecDialog").dialog({
				resizable: false,
				height: "auto",
				width : 500,
				modal : true,
				buttons : {
					"<fmt:message key="common.btn.OK" />" : function() {//確認
						$(this).dialog("close");
					}
				},
				title : "<fmt:message key="message.dialog.detail" />" //明細資料
			});
		}
	</script>
</head>
	<body>
	<!-- dialog -->
	<div id="ecDialog"  style="display:none;">
		<table style="width: 100%;">
			<tr>
				<td class="titleRow" style="width: 80px;"><fmt:message key="F0201.field.MIN_FEE" /></td><!-- 比率收費下限 -->
				<td class="dataRowEven"><div id="minFee"></div></td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 80px;"><fmt:message key="F0201.field.MAX_FEE" /></td><!-- 比率收費上限 -->
				<td class="dataRowEven"><div id="maxFee"></div></td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 80px;"><fmt:message key="F0201.field.MIN_TAX2" /></td><!-- 繳費稅比率收費下限 -->
				<td class="dataRowEven"><div id="minTax"></div></td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 80px;"><fmt:message key="F0201.field.MAX_TAX2" /></td><!-- 繳費稅比率收費上限 -->
				<td class="dataRowEven"><div id="maxTax"></div></td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 80px;"><fmt:message key="F0201.field.LINK_LIMIT" /></td><!-- 使用者可綁定帳戶數 -->
				<td class="dataRowEven"><div id="linkLimit"></div></td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 80px;"><fmt:message key="F0201.field.ENTR_ID" /></td><!-- 公司統編 -->
				<td class="dataRowEven"><div id="entrId"></div></td>
			</tr>
			<tr>
				<td class="titleRow"><fmt:message key="F0201.field.CNTC" /></td><!-- 聯絡人 -->
				<td class="dataRowEven"><div id="contact"></div></td>
			</tr>
			<tr>
				<td class="titleRow"><fmt:message key="F0201.field.TEL" /></td><!--聯絡電話  -->
				<td class="dataRowEven"><div id="tel"></div></td>
			</tr>
			<tr>
				<td class="titleRow"><fmt:message key="F0201.field.MAIL" /></td><!-- 電子郵件 -->
				<td class="dataRowEven" style="word-break: break-all;"><div id="email"></div></td>
			</tr>
			<tr>
				<td class="titleRow"><fmt:message key="F0201.field.ACTV_SEND_ID" /></td><!-- 啟用建檔人員 -->
				<td class="dataRowEven"><div id="actvSendId"></div></td>
			</tr>
			<tr>
				<td class="titleRow"><fmt:message key="F0201.field.ACTV_APRV_ID" /></td><!-- 啟用核可人員 -->
				<td class="dataRowEven"><div id="actvAprvId"></div></td>
			</tr>
			<tr>
				<td class="titleRow"><fmt:message key="F0201.field.ACTV_APRV_DTTM" /></td><!-- 啟用時間 -->
				<td class="dataRowEven"><div id="actvAprvDttm"></div></td>
			</tr>
			<tr>
				<td class="titleRow"><fmt:message key="F0201.field.TRMN_SEND_ID" /></td><!-- 終止建檔人員 -->
				<td class="dataRowEven"><div id="trmnSendId"></div></td>
			</tr>
			<tr>
				<td class="titleRow"><fmt:message key="F0201.field.TRMN_APRV_ID" /></td><!-- 終止核可人員 -->
				<td class="dataRowEven"><div id="trmnAprvId"></div></td>
			</tr>
			<tr>
				<td class="titleRow"><fmt:message key="F0201.field.TRMN_APRV_DTTM" /></td><!-- 終止時間 -->
				<td class="dataRowEven"><div id="trmnAprvDttm"></div></td>
			</tr>
		</table>
	</div>
	  <!-- Container ============================================================================================== -->
	    <div class="container">
	        <!-- Content ------------------------------------------------------------------------>
	        <div class="mainContent">
	            <div class="content">
	                <div class="fnctTitle"><fmt:message key="function.Id.F0205" /></div><%-- 電商平台查詢 --%>
	                <form method="post" name="form1" id="form1" action="" style="margin: 0;"> 
	                    <table class="fxdTable text-center" width="100%">
	                        <tr class="titleRow">
	                            <td><fmt:message key="F0201.field.EC_ID" /></td><%-- 平台代碼 --%>
	                            <td><fmt:message key="F0201.field.EC_NAME_CH" /></td><%-- 平台中文名稱 --%>
	                            <td><fmt:message key="F0201.field.EC_NAME_EN" /></td><%-- 平台英文名稱 --%>
	                            <td><fmt:message key="F0201.field.FEE_TYPE" /></td><%-- 收費方式 --%>
	                            <td><fmt:message key="F0201.field.FEE_RATE" /></td><%-- 費率 --%>
	                            <td><fmt:message key="F0201.field.TAX_TYPE" /></td><%-- 繳費税收費方式 --%>
	                            <td><fmt:message key="F0201.field.TAX_RATE" /></td><%-- 繳費税費率 --%>
	                            <td><fmt:message key="F0201.field.FEE_STTS" /></td><%-- 狀態 --%>
	                            <td><fmt:message key="F0201.field.REAL_ACNT" /></td><%-- 實體帳號 --%>
	                            <td><fmt:message key="F0201.field.ENTR_NO" /></td><%-- 企業編號--%>
	                            <td><fmt:message key="F0201.field.NOTE" /></td><%-- 備註說明項 --%>
	                            <td><fmt:message key="F0201.field.DETAIL" /></td><!--明細  -->
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
		                            <td>${item.ecId}</td>
		                            <td align="left">${item.ecNameCh}</td>
		                            <td align="left">${item.ecNameEn}</td>
		                            <td align="center"><fmt:message key="F0201.field.FEE_TYPE.${item.feeType}" /></td>
		                            <td align="center">
			                            <c:if test="${item.feeType eq 'A'}">
	                                		<fmt:parseNumber var="value" integerOnly="true"  type="number" value="${item.feeRate}" />
	                                		${value}<fmt:message key="common.dollar" />
	                                	</c:if>
	                                	<c:if test="${item.feeType eq 'B'}">
	                                		 ${item.feeRate}%
	                               		</c:if>
		                            </td>
		                            <td align="center">
		                               <c:if test="${not empty item.taxType && not empty item.taxRate}" >
		                                <fmt:message key="F0201.field.TAX_TYPE.${item.taxType}" />
		                               </c:if> 
		                             </td>
		                            <td align="center">
	                            		<c:if test="${item.taxType eq 'C' && not empty item.taxRate}">
	                            			<fmt:parseNumber var="value" integerOnly="true"  type="number" value="${item.taxRate}" />
	                            			${value}<fmt:message key="common.dollar" />
                            			</c:if>
	                            		<c:if test="${item.taxType eq 'D'&& not empty item.taxRate}">${item.taxRate}%</c:if>
		                            </td>
		                            <td align="center"><fmt:message key="F0201.field.FEE_STTS.${item.stts}" /></td>
		                            <td><aclFn:realAcntFormate realAcnt="${item.realAcnt}"/></td>
		                            <td>${item.entrNo}</td> 
		                            <td align="left">${item.note}</td>
									<fmt:formatDate value="${item.actvAprvDttm}" var="actvAprvDttm" pattern="yyyy-MM-dd HH:mm:ss" />
		                            <fmt:formatDate value="${item.trmnAprvDttm}" var="trmnAprvDttm" pattern="yyyy-MM-dd HH:mm:ss" />
		                            <td><img class="icon-detail" src="../images/doc_24.png" alt=""  onclick="openEcDialog('${item.minFee}','${item.maxFee}','${item.minTax}','${item.maxTax}','${item.linkLimit}','${item.entrId}','${item.cntc}','${item.tel}','${item.mail}','${item.actvSendId}','${item.actvAprvId}','${actvAprvDttm}','${item.trmnSendId}','${item.trmnAprvId}','${trmnAprvDttm}');" /></td>
							</c:forEach>
	                    </table>
	            	</form>
	            </div>
	        </div>
	        <!-- Footer ============================================================================================== -->
	        <div class="footer_line"></div>
	    </div>
	</body>
</html>