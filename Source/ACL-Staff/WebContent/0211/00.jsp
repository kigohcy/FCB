
<%
	/*
	 * @(#)0211/00.jsp
	 *
	 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
	 *
	 * Description: 待覆核電商平台資料
	 *
	 * Modify History:
	 * v1.00, 2018/04/16, Darrn Tsai
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
<%@include file="/include/container.jsp"%>
<script type="text/javascript">
	$(function() {
<%--日期選單--%>
	datePicker(root);
<%-- 查詢條件區 --%>
	$("#accordion").accordion({
			heightStyle : "content",
			collapsible : true
		}).children(".ui-accordion .ui-accordion-content")
				.css("padding", "0px");
<%--查詢--%>
	$("#btnQuery").click(function() {
			if ($("#form1").valid()) {
				$("#form1").attr("action", root + "/0211/ecDataAprv.html");
				$("#form1").submit();
			}
		});
	});

	$(function() {
		var qOprtType = '${command.oprtType}';
		var qDataStts = '${command.dataStts}';

		if (qOprtType != '') {
			$('#sort-item01').val(qOprtType);
		}
		if (qDataStts != '') {
			$('#sort-item02').val(qDataStts);
		}

		$("#form1")
				.validate(
						{
							rules : {
								strtDate : {
									required : true,
									DATE_FRMT_CHECKER : true
								},
								endDate : {
									required : true,
									DATE_FRMT_CHECKER : true,
									DATE_COMPARE : {
										targetDate : 'input[name="strtDate"]',
										// 驗證規則: B-預設日不可小於目標日  L-預設日不可大於目標日 E-預設日不可等於目標日
										compareType : 'B'
									}
								}
							},
							messages : {
								strtDate : {
									required : '<fmt:message key="message.alert.startDate" /><fmt:message key="message.alert.notNull" />',
									DATE_FRMT_CHECKER : '<fmt:message key="message.alert.startDate" /> <fmt:message key="message.alert.formateError" />'
								},
								endDate : {
									required : '<fmt:message key="message.alert.endDate" /><fmt:message key="message.alert.notNull" />',
									DATE_FRMT_CHECKER : '<fmt:message key="message.alert.endDate" /><fmt:message key="message.alert.formateError" />',
									DATE_COMPARE : '<fmt:message key="message.alert.dateCompare" />'
								}
							},
							showErrors : function(errorMap, errorList) {
								var err = [];
								$.each(errorList, function(i, v) {
									err.push(v.message);
								});
								if (err.length > 0) {
									alert(err.join("\n"));
								}
							},
							onkeyup : false,
							onfocusout : false,
							onsubmit : false
						});

		$("#form2")
				.validate(
						{
							rules : {
								ecId : {
									required : true
								}
							},
							messages : {
								ecId : {
									required : "<fmt:message key="message.alert.mustToSelect" />"
								}
							},
							showErrors : function(errorMap, errorList) {
								var err = [];
								$.each(errorList, function(i, v) {
									err.push(v.message);
								});
								if (err.length > 0) {
									alert(err.join("\n"));
								}
							},
							onkeyup : false,
							onfocusout : false,
							onsubmit : false
						});
	});

	/**
	 *open dialog
	 *minFee	最小手續費
	 *maxFee	最大手續費
	 *minTax	     繳費稅 比率收費下限
	 *maxTax	     繳費稅 比率收費上限
	 *linkLimit 使用者可綁定帳戶數
	 *entrId 	公司統編
	 *contact 	聯絡人
	 *tel      	聯絡電話
	 *email		電子郵件
	 *note		備註說明
	 */
	function openEcDialog(minFee, maxFee, minTax, maxTax, linkLimit, entrId, contact, tel,
			email, note) {

		$("#minFee").html(minFee);
		$("#maxFee").html(maxFee);
		$("#minTax").html(minTax);
		$("#maxTax").html(maxTax);
		$("#linkLimit").html(linkLimit);
		$("#entrId").html(entrId);
		$("#contact").html(contact);
		$("#tel").html(tel);
		$("#email").html(email);
		$("#note").html(note);

		$("#ecDialog").dialog({
			resizable : false,
			height : "auto",
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

	//type: delete, insertInit, updateInit
	function send(type) {
		var url = "";
		var datas = $('input[name=ecId]:checked', '#form2').val().split(',');

		$('#cretUser').val(datas[1]);
		$('#cretDttm').val(datas[2]);
		$('#oprtType').val(datas[3]);

		if (type == "delete") { // 刪除電商平台送審資料
			if (datas[4] == '3') {
				alert("<fmt:message key="message.cnfm.0012" />")
				return;
			} else if (datas[4] == '2') {
				alert("<fmt:message key="message.cnfm.0013" />")
				return;
			} else if ($("#form2").valid()) { // validate form
				if (confirm("<fmt:message key="message.cnfm.0016" />")) {
					url = root + "/0211/executeEcData.html";
					$('#dataStts').val('3');
					$("#form2").attr("action", url);
					$("#form2").submit();
				}
			}
		} else if (type == "acceptInit") { // 放行電商平台送審資料
			if (datas[4] == '2') {
				alert("<fmt:message key="message.cnfm.0013" />")
				return;
			} else if (datas[4] == '3') {
				alert("<fmt:message key="message.cnfm.0012" />")
				return;
			} else if ($("#form2").valid()) {//validate form
				if (confirm("<fmt:message key="message.cnfm.0015" />")) {
					url = root + "/0211/executeEcData.html";
					$('#dataStts').val('2');
					$("#form2").attr('action', url);
					$("#form2").submit();
				}
			}
		}
	}
</script>
</head>
<body>
	<!-- dialog -->
	<div id="ecDialog" style="display: none;">
		<table style="width: 100%;">
			<tr>
				<td class="titleRow" style="width: 80px;">
					<fmt:message key="F0201.field.MIN_FEE" />
				</td>
				<!-- 最小手續費 -->
				<td class="dataRowEven">
					<div id="minFee"></div>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 80px;">
					<fmt:message key="F0201.field.MAX_FEE" />
				</td>
				<!-- 最大手續費 -->
				<td class="dataRowEven">
					<div id="maxFee"></div>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 80px;">
					<fmt:message key="F0201.field.MIN_TAX2" />
				</td><!-- 繳費稅比率收費下限 -->
				<td class="dataRowEven">
					<div id="minTax"></div>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 80px;">
					<fmt:message key="F0201.field.MAX_TAX2" />
				</td><!-- 繳費稅比率收費上限 -->
				<td class="dataRowEven">
					<div id="maxTax"></div>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 80px;">
					<fmt:message key="F0201.field.LINK_LIMIT" />
				</td>
				<!-- 使用者可綁定帳戶數 -->
				<td class="dataRowEven">
					<div id="linkLimit"></div>
				</td>
			</tr>
			<tr>
				<td class="titleRow" style="width: 80px;">
					<fmt:message key="F0201.field.ENTR_ID" />
				</td>
				<!-- 公司統編 -->
				<td class="dataRowEven">
					<div id="entrId"></div>
				</td>
			</tr>
			<tr>
				<td class="titleRow">
					<fmt:message key="F0201.field.CNTC" />
				</td>
				<!-- 聯絡人 -->
				<td class="dataRowEven">
					<div id="contact"></div>
				</td>
			</tr>
			<tr>
				<td class="titleRow">
					<fmt:message key="F0201.field.TEL" />
				</td>
				<!-- 聯絡電話 -->
				<td class="dataRowEven">
					<div id="tel"></div>
				</td>
			</tr>
			<tr>
				<td class="titleRow">
					<fmt:message key="F0201.field.MAIL" />
				</td>
				<!-- 電子郵件 -->
				<td class="dataRowEven" style="word-break: break-all;">
					<div id="email"></div>
				</td>
			</tr>
			<tr>
				<td class="titleRow">
					<fmt:message key="F0201.field.NOTE" />
				</td>
				<!-- 備註說明 -->
				<td class="dataRowEven">
					<div id="note"></div>
				</td>
			</tr>
		</table>
	</div>
	<!-- Container ============================================================================================== -->
	<div class="container">
		<!-- Content ------------------------------------------------------------------------>
		<div class="mainContent">
			<div class="content">
				<div class="fnctTitle">
					<fmt:message key="function.Id.F0211" />
				</div>
				<%-- 電商平台覆核 --%>
				<div id="accordion">
					<%-- 查詢條件區 --%>
					<h3>
						<fmt:message key="common.queryCondition" />
						<%--查詢條件--%>
					</h3>
					<div style="width: 978px;">
						<form method="post" id=form1 name="form1" action="#" style="margin: 0;">
							<table class="fxdTable" style="width: 100%">
								<tr class="dataRowOdd">
									<td width="150px">
										<fmt:message key="F0201.field.SendDate" />
										*
										<%--送審日期--%>
									</td>
									<td>
										<input type="text" size="10" maxlength="10" name="strtDate" value="<c:out value="${command.strtDate}" />" datePicker="true" />
										~
										<input type="text" size="10" maxlength="10" name="endDate" value="<c:out value="${command.endDate}" />" datePicker="true" />
									</td>
								</tr>
								<tr class="dataRowEven">
									<td width="150px">
										<fmt:message key="F0201.field.OPRT_TYPE" />
										<%--異動類別--%>
									</td>
									<td>
										<select name="oprtType" id="sort-item01">
											<option value="" selected><fmt:message key="common.queryCondition.all" /></option>
											<%--全部--%>
											<option value="I"><fmt:message key="F0201.field.OPRT_TYPE.I" /></option>
											<%--新增--%>
											<option value="U"><fmt:message key="F0201.field.OPRT_TYPE.U" /></option>
											<%--修改--%>
										</select>
									</td>
								</tr>
								<tr class="dataRowOdd">
									<td width="150px">
										<fmt:message key="F0201.field.DATA_STTS" />
										<%--覆核狀態--%>
									</td>
									<td>
										<select name="dataStts" id="sort-item02">
											<option value="" selected><fmt:message key="common.queryCondition.all" /></option>
											<%--全部--%>
											<option value="1"><fmt:message key="F0201.field.DATA_STTS.1" /></option>
											<%--送審--%>
											<option value="2"><fmt:message key="F0201.field.DATA_STTS.2" /></option>
											<%--通過--%>
											<option value="3"><fmt:message key="F0201.field.DATA_STTS.3" /></option>
											<%--刪除--%>
										</select>
									</td>
								</tr>
							</table>
						</form>
						<div align="left" style="margin: 10px">
							<%-- 查詢 --%>
							<input class="btnStyle" type="button" name="btnQuery" id="btnQuery" value='<fmt:message key="common.btn.Query"/>' />
						</div>
					</div>
				</div>
				<%-- 查詢結果區 --%>
				<c:if test="${command.queryFlag eq true }">
					<c:choose>
						<c:when test="${not empty command.ecDataAprvList}">
							<form method="post" name="form2" id="form2" action="" style="margin: 0;">
								<input type="hidden" id="cretUser" name="cretUser" value="" />
								<input type="hidden" id="cretDttm" name="cretDttm" value="" />
								<input type="hidden" id="oprtType" name="oprtType" value="" />
								<input type="hidden" id="dataStts" name="dataStts" value="" />
								<table class="fxdTable" width="100%">
									<tr class="titleRow">
										<%-- <c:if test="${command.ecDataAprvList[0].dataStts eq '1'}"> --%>
											<td>
												<fmt:message key="F0201.field.OPTION" />
											</td>
											<%--選項--%>
										<%-- </c:if> --%>
										<td>
											<fmt:message key="F0201.field.EC_ID" />
										</td>
										<%--平台代碼--%>
										<td>
											<fmt:message key="F0201.field.EC_NAME_CH" />
										</td>
										<%--平台中文名稱--%>
										<td>
											<fmt:message key="F0201.field.EC_NAME_EN" />
										</td>
										<%--平台英文名稱--%>
										<td>
											<fmt:message key="F0201.field.FEE_TYPE" />
										</td>
										<%--收費方式--%>
										<td>
											<fmt:message key="F0201.field.FEE_RATE" />
										</td>
										<%--費率--%>
										<td>
											<fmt:message key="F0201.field.TAX_TYPE" />
										</td><%-- 繳費税收費方式 --%>
	                                    <td>
	                                    	<fmt:message key="F0201.field.TAX_RATE" />
	                                    </td><%-- 繳費税費率 --%>
										<td>
											<fmt:message key="F0201.field.FEE_STTS" />
										</td>
										<%--狀態--%>
										<td>
											<fmt:message key="F0201.field.REAL_ACNT" />
										</td>
										<%--實體帳號--%>
										<td>
											<fmt:message key="F0201.field.ENTR_NO" />
										</td>
										<%--企業編號--%>
										<td>
											<fmt:message key="F0201.field.SHOW_REAL_ACNT" />
										</td>
										<%--實體帳號是否明碼--%>
										<td>
											<fmt:message key="F0201.field.OPRT_TYPE" />
										</td>
										<!--異動類別-->
										<td>
											<fmt:message key="F0201.field.DATA_STTS" />
										</td>
										<!--覆核狀態-->
										<td>
											<fmt:message key="F0201.field.SendDate" />
										</td>
										<%--送審日期--%>
										<td>
											<fmt:message key="F0201.field.DETAIL" />
										</td>
										<!--明細  -->
									</tr>
									<c:forEach items="${command.ecDataAprvList}" var="item" varStatus="theCount">
										<c:choose>
											<c:when test="${theCount.count % 2 == 1}">
												<c:set value="dataRowOdd" var="cssClass"></c:set>
											</c:when>
											<c:otherwise>
												<c:set value="dataRowEven" var="cssClass"></c:set>
											</c:otherwise>
										</c:choose>
										<tr class="${cssClass}">
											<%-- <c:if test="${command.ecDataAprvList[0].dataStts eq '1'}"> --%>
											<c:if test="${item.dataStts eq '1'}">
												<td align="center">
													<input type="radio" name="ecId" value="${item.id.ecId},${item.id.cretUser},<fmt:formatDate value="${item.id.cretDttm}" pattern="yyyy-MM-dd HH:mm:ss.SSS" />,${item.oprtType},${item.dataStts}" <c:if test="${theCount.count == 1}">checked</c:if> />
												</td>
											</c:if>
											<c:if test="${item.dataStts ne '1'}">
												<td align="center">
													
												</td>
											</c:if>
											<td align="center">${item.id.ecId}</td>
											<td align="left">${item.ecNameCh}</td>
											<td align="left">${item.ecNameEn}</td>
											<td align="center">
												<fmt:message key="F0201.field.FEE_TYPE.${item.feeType}" />
											</td>
											<td align="center">
												<c:if test="${item.feeType eq 'A'}">
													<fmt:parseNumber var="value" integerOnly="true" type="number" value="${item.feeRate}" />
	                            			${value}<fmt:message key="common.dollar" />
												</c:if>
												<c:if test="${item.feeType eq 'B'}">${item.feeRate}%</c:if>
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
	                            				<c:if test="${item.taxType eq 'D' && not empty item.taxRate}">${item.taxRate}%</c:if>
		                            		</td>
											<td align="center">
												<fmt:message key="F0201.field.FEE_STTS.${item.stts}" />
											</td>
											<td align="center">
												<aclFn:realAcntFormate realAcnt="${item.realAcnt}" />
											</td>
											<td align="center">${item.entrNo}</td>
											<td align="center">
												<fmt:message key="F0201.field.SHOW_REAL_ACNT.${item.showRealAcnt}" />
											</td>
											<td align="center">
												<fmt:message key="F0201.field.OPRT_TYPE.${item.oprtType}" />
											</td>
											<td align="center">
												<fmt:message key="F0201.field.DATA_STTS.${item.dataStts}" />
											</td>
											<td align="center">
												<fmt:formatDate value="${item.id.cretDttm}" pattern="yyyy-MM-dd HH:mm:ss" />
											</td>
											<td align="center">
												<img class="icon-detail" src="../images/doc_24.png" alt="" onclick="openEcDialog('${item.minFee}','${item.maxFee}','${item.minTax}','${item.maxTax}','${item.linkLimit}','${item.entrId}','${item.cntc}','${item.tel}','${item.mail}','${item.note}');" />
											</td>
										</tr>
									</c:forEach>
								</table>
								<!-- Button area ------------------------------------------------------------------------>
								<%-- <c:if test="${command.ecDataAprvList[0].dataStts eq '1'}"> --%>
									<div class="btnContent">
										<%--放行--%>
										<input class="btnStyle" type="button" name="btn1" value="<fmt:message key="common.btn.Accept" />" onclick="send('acceptInit');" />
										&nbsp;
										<%--刪除--%>
										<input class="btnStyle" type="button" name="btn1" value="<fmt:message key="common.btn.Delete" />" onclick="send('delete');" />
										&nbsp; &nbsp;
									</div>
								<%-- </c:if> --%>
							</form>
						</c:when>
						<c:otherwise>
							<%--查無符合條件資料--%>
							<div class="noResult" align="center">
								<fmt:message key="message.sys.NoData" />
							</div>
						</c:otherwise>
					</c:choose>
				</c:if>

				<!-- Footer ============================================================================================== -->
				<div class="footer_line"></div>
			</div>
</body>
</html>