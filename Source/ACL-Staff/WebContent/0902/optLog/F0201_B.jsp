<%
/*
 * @(#)log/F0201_B.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 操作記錄查詢 - 扣款平台管理異動前
 *
 * Modify History:
 * v1.00, 2016/02/02, Evan
 *  1) First Release
 * 
*/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
        
			<tr class="dataRowOdd">
			    <th><fmt:message key="F0201.field.EC_ID" />*</th><%--平台代號* --%>
				<td>
			  		${command.before.ecData.ecId}
			    </td>
			</tr>
			 <tr class="dataRowEven">
                 <th><fmt:message key="F0201.field.EC_NAME_CH" />*</th><%--平台中文名稱* --%>
                 <td>
                     <input type="text" size="20"  value="${command.before.ecData.ecNameCh}"  readonly/>
                 </td>
             </tr>
             <tr class="dataRowOdd">
                 <th><fmt:message key="F0201.field.EC_NAME_EN" />&nbsp;</th><%--平台英文名稱 --%>
                 <td>
                     <input type="text" size="20"  value="${command.before.ecData.ecNameEn}" readonly/>
                 </td>
             </tr>
             <tr class="dataRowEven">
                 <th><fmt:message key="F0201.field.FEE_TYPE" />*</th><%-- 收費方式* --%>
                 <td>
                     <input type="radio"  value="A" <c:if test="${command.before.ecData.feeType eq 'A'}">checked</c:if> disabled/><fmt:message key="F0201.field.FEE_TYPE.A"/>&nbsp;
                     <input type="radio"  value="B" <c:if test="${command.before.ecData.feeType eq 'B'}">checked</c:if> disabled/><fmt:message key="F0201.field.FEE_TYPE.B"/>&nbsp;
                 </td>
             </tr>
             <tr class="dataRowOdd">
                  <th><fmt:message key="F0201.field.FEE_RATE" />*</th><%--費率*--%>
                 <td>
                     <c:if test="${command.before.ecData.feeType eq 'A'}">
                		<fmt:parseNumber var="value" integerOnly="true"  type="number" value="${command.before.ecData.feeRate}"  />
                		<input type="text"  value="${value}" readonly /> <fmt:message key="common.dollar" /><%--元 --%>
                	</c:if>
                	<c:if test="${command.before.ecData.feeType eq 'B'}">
                		 <input type="text" value="${command.before.ecData.feeRate}" readonly />
                		 <font id="unit">%
                		 &nbsp;&nbsp;&nbsp;最小收取手續費金額:<input type="text" size="10" name="minFee" value="${command.before.ecData.minFee}" id="minFee" readonly/>&nbsp;元&nbsp; ~ &nbsp;最大收取手續費金額:<input type="text" size="10" name="maxFee" value="${command.before.ecData.maxFee}" id="maxFee" readonly/>&nbsp;元
                		 </font> 
               		</c:if>
                 </td>
             </tr>
             <tr class="dataRowEven">
				 <th><fmt:message key="F0201.field.TAX_TYPE" /></th>
					 <%-- 繳費稅收費方式* --%>
					<td>
						<input type="radio"  value="C" <c:if test="${command.before.ecData.taxType eq 'C' || empty command.before.ecData.taxType || empty command.before.ecData.taxRate}">checked</c:if> disabled/><fmt:message key="F0201.field.TAX_TYPE.C"/>&nbsp;
	                    <input type="radio"  value="D" <c:if test="${command.before.ecData.taxType eq 'D' && not empty command.before.ecData.taxRate}">checked</c:if> disabled/><fmt:message key="F0201.field.TAX_TYPE.D"/>&nbsp;
					</td>
			 </tr>
			 <tr class="dataRowOdd">
			 	<th><fmt:message key="F0201.field.TAX_RATE" /></th>
					<%--繳費稅費率*--%>
					<td>
						<c:if test="${command.before.ecData.taxType eq 'C' || empty command.before.ecData.taxType || empty command.before.ecData.taxRate}">
                       		<fmt:parseNumber var="value" integerOnly="true"  type="number" value="${command.before.ecData.taxRate}" />
                       		<input type="text" size="9"  maxlength="9" name="taxRate" id="taxRate" value="${value}" readonly/> 
                       		<font id="unitTax"><fmt:message key="common.dollar" /><%--元 --%></font>
                       	</c:if>
                       	<c:if test="${command.before.ecData.taxType eq 'D' && not empty command.before.ecData.taxRate}">
                       		 <input type="text" size="9"  maxlength="9" name="taxRate" id="taxRate" value="${command.before.ecData.taxRate}" readonly/>
                       		 <font id="unitTax">% &nbsp;&nbsp;&nbsp;最小收取手續費金額:<input type="text" size="3" maxlength="3" name="mintax" value="${command.before.ecData.minTax}" id="mintax" readonly/>&nbsp;元&nbsp; ~ &nbsp;最大收取手續費金額:<input type="text" size="3" maxlength="3" name="maxtax" value="${command.before.ecData.maxTax}" id="maxtax" readonly/>&nbsp;元</font> 
                   		</c:if>
					</td>
			 </tr>
             <tr class="dataRowEven">
                 <th><fmt:message key="F0201.field.FEE_STTS" />&nbsp;</th><%-- 狀態 --%>
                 <td>
                     <select name="stts" id="stts" disabled>
                         <option value="00" <c:if test="${command.before.ecData.stts eq '00'}">selected</c:if>><fmt:message key="F0201.field.FEE_STTS.00" /></option><%--啟用 --%>
                         <option value="01" <c:if test="${command.before.ecData.stts eq '01'}">selected</c:if>><fmt:message key="F0201.field.FEE_STTS.01" /></option><%--暫停 --%>
                         <option value="02" <c:if test="${command.before.ecData.stts eq '02'}">selected</c:if>><fmt:message key="F0201.field.FEE_STTS.02" /></option><%--終止 --%>
                     </select>
                 </td>
             </tr>
             <tr class="dataRowOdd">
                  <th><fmt:message key="F0201.field.REAL_ACNT" />*</th><%--實體帳號* --%>
                 <td>
                     <input type="text" size="16"  value="${command.before.ecData.realAcnt}" readonly/>
                 </td>
             </tr>
             <tr class="dataRowEven">
				<th><fmt:message key="F0201.field.ENTR_NO" />*</th><%--銷帳百分百建檔編號* --%>
				<td>
					<input type="text" size="7"  value="${command.before.ecData.entrNo}" readonly/>
					</td>
				</tr>
				
				<tr class="dataRowOdd">
				<th><fmt:message key="F0201.field.LINK_LIMIT" />*</th><%--使用者可綁定帳戶數* --%>
				<td>
					<input type="text" size="2"  value="${command.before.ecData.linkLimit}" readonly/>
					</td>
				</tr>
             <tr class="dataRowEven">
				<th><fmt:message key="F0201.field.SHOW_REAL_ACNT" />&nbsp;</th><%--實體帳號明碼  --%>
                 <td>
                     <select name="showRealAcnt" id="showRealAcnt" disabled>
                         <option value="Y" <c:if test="${command.before.ecData.showRealAcnt eq 'Y'}">selected</c:if>><fmt:message key="F0201.field.SHOW_REAL_ACNT.Y" /></option><%--是--%>
                         <option value="N" <c:if test="${command.before.ecData.showRealAcnt eq 'N'}">selected</c:if>><fmt:message key="F0201.field.SHOW_REAL_ACNT.N" /></option><%--否--%>
                     </select>
                 </td>
				</tr>
				<tr class="dataRowOdd">
				<th><fmt:message key="F0201.field.ENTR_ID" />&nbsp;</th><%--公司統編  --%>
				<td>
					<input type="text" size="8"  value="${command.before.ecData.entrId}" readonly/>
					</td>
				</tr>
				<tr class="dataRowEven">
				<th><fmt:message key="F0201.field.CNTC" />&nbsp;</th><%--聯絡人  --%>
				<td>
					<input type="text" size="20"  value="${command.before.ecData.cntc}" readonly/>
					</td>
				</tr>
				<tr class="dataRowOdd">
				<th><fmt:message key="F0201.field.TEL" />&nbsp;</th><%--聯絡電話  --%>
				<td>
					<input type="text" size="20"  value="${command.before.ecData.tel}" readonly/>
					</td>
				</tr>
				<tr class="dataRowEven">
				<th><fmt:message key="F0201.field.MAIL" />&nbsp;</th><%--電子郵件  --%>
				<td>
					<input type="text" size="50" value="${command.before.ecData.mail}" readonly/>
					</td>
			 </tr>
             <tr class="dataRowOdd">
                 <th><fmt:message key="F0201.field.NOTE" />&nbsp;</th><%-- 備註說明 --%>
                 <td>
                     <input type="text" size="64"  value="${command.before.ecData.note}" readonly/>
                 </td>
             </tr>
         
