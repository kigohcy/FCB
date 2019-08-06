<%
/*
 * @(#)log/F0204_A.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 操作記錄查詢 - 服務狀態設定
 *
 * Modify History:
 * v1.00, 2016/02/23, Evan
 *  1) First Release
 * 
*/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

		<table class="fxdTable" style="width: 980px;">
			<tr class="titleRow">
				<td nowrap><fmt:message key="F0202.field.custId" /></td><%--身分證字號 --%>
				<td nowrap><fmt:message key="F0202.field.custName" /></td><%--客戶姓名 --%>
				<td nowrap><fmt:message key="F0202.field.stts" /></td><%--服務狀態 --%>
				<td nowrap><fmt:message key="F0202.field.sttsModfyDttm" /></td><%--狀態異動日期 --%>
				<td nowrap><fmt:message key="F0202.field.custStts" /></td><%--會員登入狀態 --%>
				<td nowrap><fmt:message key="F0202.field.vrsn" /></td><%--條款版本 --%>
				<td nowrap><fmt:message key="F0202.field.exeFnct" /></td><%--執行功能 --%>
			</tr>
			<tr class="dataRowOdd" align="center">
				<c:choose>
					<c:when test="${not empty command.after.afterCustData}">
						<td>${command.after.afterCustData.custId}</td>
						<td>${command.after.afterCustData.custName}</td>
						<td><fmt:message key="F0202.field.stts.${command.after.afterCustData.stts}"/></td>
						<td>
							<fmt:formatDate value="${command.after.afterCustData.sttsDttm}" pattern="yyyy/MM/dd"/>
						</td>
						<td><c:out value="${command.after.custStts}"/></td>
						<td>${command.after.afterCustData.vrsn}</td>
						<td>
							<input class="btnStyle" type="button"  value="<fmt:message key="common.btn.Enable"/>" <%-- 啟用--%> disabled />
							<input class="btnStyle" type="button"  value="<fmt:message key="common.btn.Pause"/>" <%-- 暫停--%> disabled />
							<input class="btnStyle" type="button"  value="<fmt:message key="common.btn.UnlockPsd"/>"  <%-- 密碼解鎖--%> disabled />
						</td>
					</c:when>
					<c:otherwise>
						<%--查無符合條件資料--%>
						<td class="noResult" colspan="7" align="center" ><fmt:message key="message.sys.NoData" /></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		<p></p>
		
			
		<c:choose>
			<c:when test="${not empty command.after.afterCustPltf}">
				<table class="fxdTable" width="800px">
					<tr class="titleRow">
						<td width="50px;"><fmt:message key="F0202.field.option" /></td><!--選項  -->
						<td width="250px;" nowrap><fmt:message key="F0202.field.platformName" /></td><%--平台名稱 --%>
						<td width="250px;" nowrap><fmt:message key="F0202.field.srvStts" /></td><%--服務狀態--%>
						<td width="250px;" nowrap><fmt:message key="F0202.field.sttsModfyDttm" /></td><%--狀態異動日期--%>
					</tr>
					<tr class="dataRowOdd">
						<td align="center">
							<c:set var="isChecked" value="false"/>
						    <c:set var="key" value="${command.after.afterCustPltf.id.custId};${command.after.afterCustPltf.id.ecId}" />
						  	<c:forEach items="${command.after.ecKey}" var="item">
						  		<c:if test="${key eq  item}">  <c:set var="isChecked" value="true"/></c:if>
						  	</c:forEach>
							<input type="checkbox"  <c:if test="${isChecked}">checked</c:if> disabled/>
						</td>
						<td align="left">${command.after.afterCustPltf.ecNameCh}</td>
						<td align="center">
							<select disabled>
							    <option value="00" <c:if test="${command.after.afterCustPltf.stts eq '00'}">selected</c:if> ><fmt:message key="F0202.field.stts.00"/></option>
							    <option value="01" <c:if test="${command.after.afterCustPltf.stts eq '01'}">selected</c:if> ><fmt:message key="F0202.field.stts.01"/></option>
							    <option value="02" <c:if test="${command.after.afterCustPltf.stts eq '02'}">selected</c:if> ><fmt:message key="F0202.field.stts.02"/></option>
							</select>
						</td>
						<td align="center">
							<fmt:formatDate value="${command.after.afterCustPltf.sttsDttm}" pattern="yyyy/MM/dd"/>
						</td>
					</tr>
				</table>
				
				<c:choose>
					<c:when test="${not empty command.after.afterAcntLink}">
						<table class="fxdTable" width="800px">
							<tr class="titleRow">
								<td width="50px;"><fmt:message key="F0202.field.option" /></td><!--選項  -->
								<td width="200px;" nowrap><fmt:message key="F0202.field.ecUser" /></td><%--平台會員代號 --%>
								<td width="200px;" nowrap><fmt:message key="F0202.field.realAcnt" /></td><%--實體帳號 --%>
								<td width="200px;" nowrap><fmt:message key="F0202.field.srvStts" /></td><%--服務狀態 --%>
								<td width="150px;" nowrap><fmt:message key="F0202.field.sttsModfyDttm" /></td><%--狀態異動日期 --%>
							</tr>
							<c:forEach items="${command.after.afterAcntLink}" var="acnt" varStatus="theCount">
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
									    <c:set var="isChecked" value="false"/>
									    <c:set var="key" value="${acnt.id.custId};${acnt.id.ecId};${acnt.id.ecUser};${acnt.id.realAcnt}" />
									  	<c:forEach items="${command.after.acntKey}" var="item">
									  		<c:if test="${key eq  item}">  <c:set var="isChecked" value="true"/></c:if>
									  	</c:forEach>
										<input type="checkbox"  <c:if test="${isChecked}">checked</c:if> disabled/>
									</td>
									<td align="left">${acnt.id.ecUser}</td>
									<td align="left">${acnt.id.realAcnt}</td>
									<td align="center">
										<select disabled>
										    <option value="00" <c:if test="${acnt.stts eq '00'}">selected</c:if> ><fmt:message key="F0202.field.stts.00"/></option>
										    <option value="01" <c:if test="${acnt.stts eq '01'}">selected</c:if> ><fmt:message key="F0202.field.stts.01"/></option>
										    <option value="02" <c:if test="${acnt.stts eq '02'}">selected</c:if> ><fmt:message key="F0202.field.stts.02"/></option>
										</select>
									</td>
									<td align="center">
										<fmt:formatDate value="${acnt.sttsDttm}" pattern="yyyy/MM/dd"/>
									</td>
								</tr>
							</c:forEach>
						</table>
					</c:when>
					<c:otherwise>
						<%--查無符合條件資料--%>
						<td class="noResult" colspan="4" align="center" ><fmt:message key="message.sys.NoData" /></td>
					</c:otherwise>
				</c:choose>
				
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
		
		
		
	