<%
/*
 * @(#)log/F0209_A.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: 操作記錄查詢 - 平台憑證管理(異動後畫面)
 *
 * Modify History:
 * v1.00, 2016/04/06, Eason Hsu
 *  1) First Release
 * v1.01, 2018/0329
 *  1) 調整顯示欄位
 * 
*/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

	<tr class="dataRowOdd">
		<%-- 電商平台 --%>
		<th>
			<fmt:message key="F0209.field.ecId"/>
		</th>
		<td>
			<c:out value="${command.after.ecName }" />
		</td>
	</tr>
	<tr class="dataRowEven">
		<%-- 憑證識別碼 --%>
		<th>
			<fmt:message key="F0209.field.certName"/>*
		</th>
		<td>
			<input type="text" size="25" name="certCn" id="certCn" value="<c:out value="${command.after.certCn }" />" readonly="readonly" />
		</td>
	</tr>
	<%-- 憑證序號 --%>
	<tr class="dataRowOdd">
		<th>
			<fmt:message key="F0209.field.certSn"/>*
		</th>
		<td>
			<input type="text" size="8" maxlength="8" name="certSn" id="certSn" value="<c:out value="${command.after.certSn }" />" readonly="readonly" />
		</td>
	</tr>
	<%-- 生效日期 --%>
	<tr class="dataRowEven">
		<th>
			<fmt:message key="F0209.field.sDate"/>*
		</th>
		<td>
			<input type="text" size="10" maxlength="10" name="strtDttm" id="strtDttm" value="${command.after.strtDttm}" readonly="readonly" />
		</td>
	</tr>
	<%-- 到期日期 --%>
	 <tr class="dataRowOdd">
	      <th>
	      	<fmt:message key="F0209.field.eDate"/>*
	      </th>
	      <td>
	      	<input type="text" size="10" maxlength="10" name="endDttm" id="endDttm" value="${command.after.endDttm}" readonly="readonly" />
	      </td>
	  </tr>
