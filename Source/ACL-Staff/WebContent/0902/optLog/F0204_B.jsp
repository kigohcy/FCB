<%
/*
 * @(#)log/F0204_B.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 操作記錄查詢 - 限額設定
 *
 * Modify History:
 * v1.00, 2016/02/23, Evan
 *  1) First Release
 * 
*/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

			<c:choose>
				<c:when test="${command.before.selectEcId eq 'showCustAcnt'}">
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
						<c:forEach items="${command.before.custAcntList}" var="custAcnt" varStatus="theCount">
							<c:choose>
								<c:when test="${theCount.count % 2 == 1}">
									<c:set value="dataRowOdd" var="cssClass"></c:set>
								</c:when>
								<c:otherwise>
									<c:set value="dataRowEven" var="cssClass"></c:set>
								</c:otherwise>
							</c:choose>
							<tr class="${cssClass}">
								<td align="left">
									<aclFn:realAcntFormate realAcnt="${custAcnt.id.realAcnt}"/>
								</td>
								<td align="center">
									<c:choose>
								    	<c:when test="${custAcnt.trnsLimt eq '0'}"><c:set var="limtValue" value=""/></c:when>
								    	<c:otherwise><c:set var="limtValue" value="${custAcnt.trnsLimt}"/></c:otherwise>
								    </c:choose>
									<input type="text" size="15"  value="${limtValue}" readonly/>
								</td>
								<td align="center">
									<c:choose>
								    	<c:when test="${custAcnt.dayLimt eq '0'}"><c:set var="limtValue" value=""/></c:when>
								    	<c:otherwise><c:set var="limtValue" value="${custAcnt.dayLimt}"/></c:otherwise>
								    </c:choose>
									<input type="text" size="15"  value="${limtValue}" readonly/>
								</td>
								<td align="center">
									<c:choose>
								    	<c:when test="${custAcnt.mnthLimt eq '0'}"><c:set var="limtValue" value=""/></c:when>
								    	<c:otherwise><c:set var="limtValue" value="${custAcnt.mnthLimt}"/></c:otherwise>
								    </c:choose>
									<input type="text" size="15" value="${limtValue}" readonly/>
								</td>
							</tr>
						</c:forEach>
					</table>
				</c:when>
				<c:otherwise>
					<table class="fxdTable" width="920px">
						<tr class="titleRow">
							<td align="left" colspan="11">&nbsp;<fmt:message key="F0204.field.platformAcntLinkLimt" /></td><%--平台連結帳號交易限額 --%>
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
						<c:set var="ecId" value="${command.before.selectEcId}"/>
						<c:forEach items="${command.before.custAcntLink[ecId]}" var="acnt" varStatus="theCount">
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
										<c:when test="${command.before.baseLimt[grad].trnsLimt  eq '0'}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
										<c:otherwise>&le;<fmt:formatNumber type="number" pattern="###,###,###" value="${command.before.baseLimt[grad].trnsLimt}" /></c:otherwise>
									</c:choose>
								</td>
								<td align="center">
									<c:choose>
								    	<c:when test="${acnt.trnsLimt eq '0'}"><c:set var="limtValue" value=""/></c:when>
								    	<c:otherwise><c:set var="limtValue" value="${acnt.trnsLimt}"/></c:otherwise>
								    </c:choose>
									<input type="text" size="6" value="${limtValue}" readonly />
								</td>
								<td align="right">
									<c:choose>
										<c:when test="${command.before.baseLimt[grad].dayLimt  eq '0'}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
										<c:otherwise>&le;<fmt:formatNumber type="number" pattern="###,###,###" value="${command.before.baseLimt[grad].dayLimt}" /></c:otherwise>
									</c:choose>
								</td>
								<td align="center">
									<c:choose>
								    	<c:when test="${acnt.dayLimt eq '0'}"><c:set var="limtValue" value=""/></c:when>
								    	<c:otherwise><c:set var="limtValue" value="${acnt.dayLimt}"/></c:otherwise>
								    </c:choose>
									<input type="text" size="6"	 value="${limtValue}" readonly />
								</td>
								<td align="right">
									<c:choose>
										<c:when test="${command.before.baseLimt[grad].mnthLimt  eq '0'}"><fmt:message key="F0204.field.noLimt" /></c:when><%--無限額 --%>
										<c:otherwise>&le;<fmt:formatNumber type="number" pattern="###,###,###" value="${command.before.baseLimt[grad].mnthLimt}" /></c:otherwise>
									</c:choose>
								</td>
								<td align="center">
									<c:choose>
								    	<c:when test="${acnt.mnthLimt eq '0'}"><c:set var="limtValue" value=""/></c:when>
								    	<c:otherwise><c:set var="limtValue" value="${acnt.mnthLimt}"/></c:otherwise>
								    </c:choose>
									<input type="text" size="6"	 value="${limtValue}" readonly />
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