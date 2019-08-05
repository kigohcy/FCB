<%
/*
 * @(#)customMsg.jsp
 * Copyright (c) 2007 HiTRUST Incorporated. All rights reserved.
 * Description:自定義處理結果頁面（需要在command中定義returnMsg變量）
 * Modify History:
 *  v1.00, 2013/09/11,Krystal Lyu 
 *   1) First release
 */
%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%-- include Header, footer and menu --%>
<%@include file="/include/container.jsp"%>
<script type="text/javascript">

	$(function(){
		$("#btn1").click(function(){
			$("#form1").submit();
		});
	});

</script>
</head>
<body>
<div class="container">
 <div class="mainContent">
	<div class="content">
      <div class="fnctTitle"><fmt:message key='function.Id.${command.funcId}'/></div>
      <form method="post" name="form1" id="form1" action="<%=root%><c:out value="${messageRedirect}"/>.html" style="margin: 0;">
		<input type="hidden" name="_back" value="<c:out value='${_back}'/>">
		      <table class="fxdTable" width="100%" style="margin:auto;">
		          <tr class="secondaryTitleRow">
		              <td colspan="2"><fmt:message key='message.title.resMsg'/></td><!--回覆訊息-->
		          </tr>
		          <tr class="dataRowOdd">
		              <td width="80px;"><fmt:message key='message.item.exeFnct'/></td><!--執行功能-->
		              <td><fmt:message key='function.Id.${command.funcId}'/></td>
		          </tr>
		          <tr class="dataRowEven">
		              <td width="80px;"><fmt:message key='message.item.exeMsg'/></td><!--執行訊息-->
		              <td>
		              	<c:set value="${command.returnMsg}" var="_retMsg_" scope="request"/>
		              	<%
		              		if(request.getAttribute("_retMsg_") == null){
		              		  out.write("");
		              		}else{
		              			out.write(request.getAttribute("_retMsg_").toString());
		              		}
		              	%>
           			  </td>
          		</tr>
      		</table>
      		 <!-- Button area ------------------------------------------------------------------------>
			  <div class="btnContent">
			      <input type="button" id="btn1" class="btnStyle" value="<fmt:message key='common.button.ok'/>"/>&nbsp;
			  </div>
      </form>
  	</div>
  </div>
    <div class="footer_line"></div>
</div>
</body>
</html>
