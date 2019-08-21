<%
/*
 * @(#)optLog/F0203_A.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 會員操作記錄查詢 - 自訂交易限額 - 限額設定 - 異動後
 *
 * Modify History:
 * v1.00, 2016/06/27, Yann
 *  1) First Release
 * 
*/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<c:choose>
	<c:when test="${not empty command.before.custAcntList }">
		<div>
			<div class="containerCust">
				<div class="dataContainer">
					<h2 class="section_title noborder" style="display: inline-block;">
						<%-- 實體帳號總表 --%>
						<fmt:message key="common.F0903.title.allRealAcnt" />
					</h2>
					<table class="table table-bordered setthbg table-vm" style="width: 800px;">
						<thead>
							<tr>
								<%-- 實體帳號 --%>
								<th rowspan="2"><fmt:message key="common.F0903.table.realAcnt" /></th>
								<%-- 自訂交易限額 - 各電商平台總計 --%>
								<th colspan="3"><fmt:message key="common.F0903.table.customLimt" /></th>
							</tr>
							<tr>
								<%-- 單筆 --%>
								<th><fmt:message key="common.F0903.table.single" /></th>
								<%-- 每日 --%>
								<th><fmt:message key="common.F0903.table.dayLimt" /></th>
								<%-- 每月 --%>
								<th><fmt:message key="common.F0903.table.mnthLimt" /></th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${command.after.custAcntRealAcnt}" var="custAcnt" varStatus="i">
							<tr>
								<%-- 實體帳號 --%>
								<td style="text-align: center;">
									<aclFn:realAcntFormate realAcnt="${custAcnt}"/>
								</td>
								<td>
									<%-- 自訂交易限額-單筆 --%>
									<c:choose>
								    	<c:when test="${empty command.after.custAcntTrnsLmt}"><c:set var="limtValue" value=""/></c:when>
								    	<c:otherwise><c:set var="limtValue" value="${command.after.custAcntTrnsLmt[i.index]}"/></c:otherwise>
								    </c:choose>
									<input type="text" size="15" maxlength="15" name="custAcntTrnsLmt" value="${limtValue}" readonly />
								</td>
								<td>
									<%-- 自訂交易限額-每日 --%>
									<c:choose>
								    	<c:when test="${empty command.after.custAcntDayLmt}"><c:set var="limtValue" value=""/></c:when>
								    	<c:otherwise><c:set var="limtValue" value="${command.after.custAcntDayLmt[i.index]}"/></c:otherwise>
								    </c:choose>
									<input type="text" size="15" maxlength="15" name="custAcntDayLmt"  value="${limtValue}" readonly />
								</td>
								<td>
									<%-- 自訂交易限額-每月 --%>
									<c:choose>
								    	<c:when test="${empty command.after.custAcntMnthLmt}"><c:set var="limtValue" value=""/></c:when>
								    	<c:otherwise><c:set var="limtValue" value="${command.after.custAcntMnthLmt[i.index]}"/></c:otherwise>
								    </c:choose>
									<input type="text" size="15" maxlength="15" name="custAcntMnthLmt" value="${limtValue}" readonly />
								</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<div>
			<div class="containerCust">
				<div class="dataContainer">
					<h2 class="section_title noborder" style="display: inline-block;">
						<%-- 平台名稱 --%>
						<c:out value="${command.after.ecName}" />
					</h2>
					<table class="table table-bordered setthbg table-vm" style="width: 800px;">
						<thead>
							<tr>
								<th rowspan="2">
									<%-- 實體帳號 --%> <fmt:message key="common.F0903.table.realAcnt" />
								</th>
								<th rowspan="2">
									<%-- 電商平台 --%> <fmt:message key="common.F0903.table.ecName" /> <br /> <%-- 會員帳號 --%> <fmt:message key="common.F0903.table.ecUser" />
								</th>
								<th colspan="5">
									<%-- 交易限額 --%> <fmt:message key="common.F0903.table.customLimt2" />
								</th>
							</tr>
							<tr>
								<%-- 等級 --%>
								<th><fmt:message key="common.F0903.table.grad" /></th>
								<%-- 類別 --%>
								<th><fmt:message key="common.F0903.table.category" /></th>
								<%-- 單筆 --%>
								<th><fmt:message key="common.F0903.table.single" /></th>
								<%-- 每日 --%>
								<th><fmt:message key="common.F0903.table.dayLimt" /></th>
								<%-- 每月 --%>
								<th><fmt:message key="common.F0903.table.mnthLimt" /></th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<c:forEach items="${command.after.custAcntLinkRealAcnt}" var="acntLink" varStatus="i">
								<c:set var="grad" value="${item.grad}" />
								<tr>
									<td rowspan="2">
										<%-- 實體帳號  --%>
										<aclFn:realAcntFormate realAcnt="${acntLink}" />
									</td>
									<td rowspan="2">
										<%-- 電商平台會員帳號  --%>
										<c:out value="${command.after.custAcntLinkEcUser[i.index]}" />
									</td>
									<td rowspan="2">
										<c:set var="grad" value="${command.after.custAcntLinkGrad[i.index]}" />
										<%-- 交易限額-等級 --%>
										<c:out value="${command.after.custAcntLinkGrad[i.index]}" />
									</td>
									<td class="zebraEven">
										<%-- 法定 --%>
										<fmt:message key="common.F0903.field.legal" />
									</td>
									<td class="zebraEven">
										<%-- 法定交易限額-單筆 --%>
										<c:choose>
											<c:when test="${command.before.baseLimt[grad].trnsLimt eq '0'}">
												<fmt:message key="common.F0903.field.noLimt" />
											</c:when>
											<%--無限額 --%>
											<c:otherwise>&le;<fmt:formatNumber type="number" pattern="###,###,###" value="${command.before.baseLimt[grad].trnsLimt}" />
											</c:otherwise>
										</c:choose>
									</td>
									<td class="zebraEven">
										<%-- 法定交易限額-每日 --%>
										<c:choose>
											<c:when test="${command.before.baseLimt[grad].dayLimt eq '0'}">
												<fmt:message key="common.F0903.field.noLimt" />
											</c:when>
											<%--無限額 --%>
											<c:otherwise>&le;<fmt:formatNumber type="number" pattern="###,###,###" value="${command.before.baseLimt[grad].dayLimt}" />
											</c:otherwise>
										</c:choose>
									</td>
									<td class="zebraEven">
										<%-- 法定交易限額-每月 --%>
										<c:choose>
											<c:when test="${command.before.baseLimt[grad].mnthLimt eq '0'}">
												<fmt:message key="common.F0903.field.noLimt" />
											</c:when>
											<%--無限額 --%>
											<c:otherwise>&le;<fmt:formatNumber type="number" pattern="###,###,###" value="${command.before.baseLimt[grad].mnthLimt}" />
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
								<tr>
									<td>
										<%-- 自訂 --%>
										<fmt:message key="common.F0903.field.custom" />
									</td>
									<td class="w100">
										<%-- 自訂交易限額-單筆 --%>
										<c:choose>
											<c:when test="${item.trnsLimt eq '0'}">
												<c:set var="limtValue" value="" />
											</c:when>
											<c:otherwise>
												<c:set var="limtValue" value="${item.trnsLimt}" />
											</c:otherwise>
										</c:choose>
										<c:choose>
									    	<c:when test="${empty command.after.custAcntLinkTrnsLmt}"><c:set var="limtValue" value=""/></c:when>
									    	<c:otherwise><c:set var="limtValue" value="${command.after.custAcntLinkTrnsLmt[i.index]}"/></c:otherwise>
									    </c:choose>
										<input type="text" size="10" maxlength="15" name="custAcntLinkTrnsLmt" value="${limtValue}" readonly />
									</td>
									<td class="w100">
										<%-- 自訂交易限額-每日 --%>
										<c:choose>
									    	<c:when test="${empty command.after.custAcntLinkDayLmt}"><c:set var="limtValue" value=""/></c:when>
									    	<c:otherwise><c:set var="limtValue" value="${command.after.custAcntLinkDayLmt[i.index]}"/></c:otherwise>
									    </c:choose>
										<input type="text" size="10" maxlength="15" name="custAcntLinkDayLmt" value="${limtValue}" readonly />
									</td>
									<td class="w100">
										<%-- 自訂交易限額-每月 --%>
										<c:choose>
									    	<c:when test="${empty command.after.custAcntLinkMnthLmt}"><c:set var="limtValue" value=""/></c:when>
									    	<c:otherwise><c:set var="limtValue" value="${command.after.custAcntLinkMnthLmt[i.index]}"/></c:otherwise>
									    </c:choose>
										<input type="text" size="10" maxlength="15" name="custAcntLinkMnthLmt" value="${limtValue}" readonly />
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</c:otherwise>
</c:choose>
