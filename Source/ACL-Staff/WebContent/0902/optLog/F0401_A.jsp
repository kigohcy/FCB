
<%
	/*
	 * @(#)log/F0401_A.jsp
	 *
	 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
	 *
	 * Description: 操作記錄查詢 - 最新消息公告設定 異動後
	 *
	 * Modify History:
	 * v1.00, 2016/06/15, Jimmy Yen
	 *  1) First Release
	 * 
	 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
	$(function() {
<%-- datePicker --%>
	datePicker(root);
<%-- type --%>
	selectType2();
<%-- CKEDITOR --%>
	CKEDITOR.replace('content2');
	});
	function selectType2() {
		var rtnType = "<c:out value="${command.after.type}"/>"

		var optioins = $("select[name=type2] option");

		for (var i = 0; i < optioins.length; i++) {
			var option = $(optioins[i]);

			if (option.val() == rtnType) {
				option.prop('selected', true);
			}
		}
	}
</script>
<tr class="dataRowOdd">
	<th width="100px">
		<%-- 公告日期 --%> <fmt:message key="F0401.field.newsDate" /> *
	</th>
	<td>
		<input type="text" maxlength="10" name="aBgnDate2" datePicker="true" value="${command.after.aBgnDate}" readonly="readonly" /> ~ <input type="text"
			maxlength="10" name="aEndDate2" datePicker="true" value="${command.after.aEndDate}" readonly="readonly" />
	</td>
</tr>
<tr class="dataRowEven">
	<th>
		<%-- 公告類型 --%> <fmt:message key="F0401.field.type" /> *
	</th>
	<td>
		<select name="type2" disabled="disabled">
			<%-- 最新消息 --%>
			<option value="A"><fmt:message key="F0401.field.type.A" /></option>
			<%-- 優惠活動 --%>
			<option value="B"><fmt:message key="F0401.field.type.B" /></option>
			<%-- 重要公告 --%>
			<option value="C"><fmt:message key="F0401.field.type.C" /></option>
			<%-- 其他 --%>
			<option value="D"><fmt:message key="F0401.field.type.D" /></option>
		</select>
	</td>
</tr>
<tr class="dataRowOdd">
	<th>
		<%-- 置頂 --%> <fmt:message key="F0401.field.serl" /> *
	</th>
	<td>
		<c:choose>
			<c:when test="${command.after.serl eq 99}">
				<input type="radio" name="serl2" value="99" checked="checked" disabled="disabled">
				<%-- 否 --%>
				<fmt:message key="F0401.field.serl.N" />
				<input type="radio" name="serl2" value="1" disabled="disabled">
				<%-- 是 --%>
				<fmt:message key="F0401.field.serl.Y" />
			</c:when>
			<c:otherwise>
				<input type="radio" name="serl2" value="99" disabled="disabled">
				<%-- 否 --%>
				<fmt:message key="F0401.field.serl.N" />
				<input type="radio" name="serl2" value="1" checked="checked" disabled="disabled">
				<%-- 是 --%>
				<fmt:message key="F0401.field.serl.Y" />
			</c:otherwise>
		</c:choose>
	</td>
</tr>
<tr class="dataRowEven">
	<th>
		<%-- 公告標題 --%> <fmt:message key="F0401.field.title" /> *
	</th>
	<td>
		<input type="text" size="50" name="title" value="${command.after.qTitle }" readonly="readonly" /> 
		<span class="helperText"> &nbsp;&nbsp; <%-- 系統禁止輸入特殊字元包含 #, $, %, ^, *, ', ", TAB鍵(\t) --%>
			<fmt:message key="F0401.field.warm" />
		</span>

	</td>
</tr>
<tr class="dataRowOdd">
	<th>
		<%-- 公告內容 --%> <fmt:message key="F0401.table.content" /> *
	</th>
	<td>
		<textarea name="content2" readonly="readonly">${command.after.content }</textarea>
	</td>
</tr>
