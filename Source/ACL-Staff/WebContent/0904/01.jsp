
<%
/*
 * @(#)0201/01.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description:
 * 電商平台資料新增編輯頁
 *
 * Modify History:
 * v1.00, 2016/02/05, Evan
 * 	1)First Release
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>帳號連結扣款(Account Link)系統</title>

<%-- include Header, footer and menu --%>
<%@include file="/include/container.jsp"%>
<script type="text/javascript">
	$(function() {
		
		$("#form1").validate({
            rules: {
            	email:{
            		multiemails: true
            	}
            },
            messages:{
            	email:{
            		multiemails:"<fmt:message key="F0201.field.MAIL" /><fmt:message key="message.alert.formateError" />"
            	}
            },
            
            showErrors: function(errorMap, errorList) {
                var err = [];
                $.each(errorList, function(i, v) {
                    err.push(v.message);
                });
                if (err.length > 0) {
                    alert(err.join("\n"));
                }
            },
            onkeyup: false,
            onfocusout: false,
            onsubmit: false
        });
		
		<%-- 確認 --%>
		$("#btnOk").click(function() {
			if ($("#form1").valid()) {
				if(confirm("<fmt:message key="message.cnfm.insertOrNot" />")){
					$("#form1").attr("action", root + "/0904/userAdd.html");
					$("#form1").submit();
				}
			}
		});
		<%-- 回上一頁 --%>
		$("#btnBack").click(function() {
			$("#form1").attr("action", root + "/0904/userQuery.html");
			$("#form1").submit();
		});
	});		
</script>
</head>
<body>
	<!-- Container ============================================================================================== -->
	<div class="container">
		<!-- Content ------------------------------------------------------------------------>
		<div class="mainContent">
			<div class="content">
				<div class="fnctTitle">
					<fmt:message key="function.Id.F0904" />
					<c:out value=">"></c:out>
					<fmt:message key="common.btn.Add" />
				</div>
				<form id="form1" name="form1" action="" method="post">
					<table id="datatable" class="vTable" width="980px;">
						<tr class="dataRowOdd">
							<th><fmt:message key="F0904.field.DEPT_ID" /></th>
							<%--部門別--%>
							<td>
								<select name="deptId" id="deptId" style="width:150px;">
									<c:forEach items="${command.deptList}" var="dept">
										<option value="<c:out value="${dept.deptId}" />" <c:if test="${dept.deptId eq command.deptId }">selected</c:if> ><c:out value="${dept.deptName}" />&nbsp;
										</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr class="dataRowEven">
							<th><fmt:message key="F0904.field.USER_ID" />*</th>
							<%--員工編號* --%>
							<td>
								<input type="text" size="10" maxlength="10" name="userId" id="userId" />
							</td>
						</tr>
						
						<tr class="dataRowOdd">
							<th><fmt:message key="F0904.field.USER_NAME" />*</th>
							<%--姓名 * --%>
							<td>
								<input type="text" size="20" maxlength="20" name="userName" id="userName" />
							</td>
						</tr>
						<tr class="dataRowEven">
							<th><fmt:message key="F0904.field.LOGIN_ID" />*</th>
							<%-- 登入代號* --%>
							<td>
								<input type="text" size="20" maxlength="20" name="loginId" id="loginId" />
							</td>
						</tr>
						<tr class="dataRowOdd">
							<th><fmt:message key="F0904.field.ROLE_ID" /></th>
							<%--角色權限別--%>
							<td>
								<select name="roleId" id="roleId" style="width:150px;">
									<c:forEach items="${command.roleList}" var="role">
										<option value="<c:out value="${role.roleId}" />" <c:if test="${role.roleId eq command.roleId }">selected</c:if> ><c:out value="${role.roleName}" />&nbsp;
										</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr class="dataRowEven">
							<th>EMAIL*</th>
							<%--EMAIL  --%>
							<td>
								<input type="text" size="40" maxlength="40" name="email" id="email" />
							</td>
						</tr>
					</table>
				</form>
			</div>
			<!-- Button area ------------------------------------------------------------------------>
			<div class="btnContent">
				<%-- 確認 --%>
				<input id="btnOk" class="btnStyle" type="button" value="<fmt:message key="common.btn.OK"/>" />
				&nbsp;
				<%-- 回上一頁 --%>
				<input id="btnBack" class="btnStyle" type="button" value="<fmt:message key="common.btn.Back"/>" />
				&nbsp;
			</div>
		</div>
		<div class="footer_line"></div>
	</div>
</body>
</html>