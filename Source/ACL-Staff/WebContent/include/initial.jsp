<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.hitrust.acl.common.AppEnv"%>
<%@ page import="com.hitrust.acl.util.StringUtil"%>
<%@ page import="com.hitrust.acl.util.DateUtil"%>
<%@ page import="com.hitrust.framework.util.Constants"%>
<%@ page import="com.hitrust.bank.model.LoginUser"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.Date"%>

<%-- include JSLT tag librarys --%>
<%@ include file="/include/taglibs.jsp" %>

<%
	String root = request.getContextPath();
	String remoteAddr = request.getRemoteAddr();
	String localAddr = request.getLocalAddr();
	String sysDt = DateUtil.formateDateTimeForUser(DateUtil.getToday());
	int sessionTime = request.getSession().getMaxInactiveInterval();
	
	LoginUser user = (LoginUser) request.getSession().getAttribute(Constants.LOGIN_USER);

	// Browser 預設語系 繁體中文
	Locale locale = new Locale("zh", "TW");
	
	if(!StringUtil.isBlank(user)) {
		locale = user.getLocale();
	}
	
	// Prevent browser caching
	response.setHeader("Cache-Control", "no-store"); // for HTTP 1.1
	response.setHeader("Pragma", "no-cache");        // for HTTP 1.0
	response.setDateHeader("Expires", 0);            // Expired

%>

<%-- 宣告 i18n --%>
<fmt:setBundle basename="i18n/message" var="msg" />
<fmt:setBundle basename="i18n/common" var="comm" />

<%-- 設定 i18n 語系 --%>
<fmt:setLocale value="<%=locale %>" />

<%-- CSS --%>
<link rel="stylesheet" type="text/css" href="<%=root%>/js/jquery-ui-1.11.4.custom/jquery-ui.theme.min.css" media="all" />
<link rel="stylesheet" type="text/css" href="<%=root%>/js/jquery-ui-1.11.4.custom/jquery-ui.min.css" media="all" />
<link rel="stylesheet" type="text/css" href="<%=root%>/css/jqtransform.css" media="all" />
<link rel="stylesheet" type="text/css" href="<%=root%>/css/acl-staff-style.css" media="all" />
<link rel="stylesheet" type="text/css" href="<%=root%>/css/acl-cust-style.css" media="all" />

<%-- JS --%>
<script type="text/javascript" src="<%=root%>/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="<%=root%>/js/jquery-migrate-1.4.1.js"></script>
<script type="text/javascript" src="<%=root%>/js/jquery-ui-1.11.4.custom/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=root%>/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=root%>/js/acl-validator.js"></script>

<script type="text/javascript" src="<%=root%>/js/jquery.jqtransform.js"></script>

<script type="text/javascript" src="<%=root%>/js/jquery.hoverIntent.js"></script>
<script type="text/javascript" src="<%=root%>/js/acl.js"></script>


<script type="text/javascript" src="<%=root%>/js/ckeditor/config.js"></script>

<script type="text/javascript">
	var specialKey = "#$%\^*\'\"\t"; <%-- 過濾條件 --%>

	<%-- 禁止輸入特殊字元 --%>
	$(document).keypress(function(event){
		var keyCode = event.keyCode;
		var realKey = String.fromCharCode(keyCode);
		
		<%-- alert("[keyCode]: " + keyCode + " [realKey]: " + realKey); --%>
		if (specialKey.indexOf(realKey) >= 0) {
			alert("不可輸入特殊字元");
			return false;
		}
	});

	<%-- 禁止頁面刷新, 切換上一頁或下一頁 --%>
	$(document).keydown(function(event){
		var keyCode = event.keyCode;
		<%-- alert(keyCode); --%>
		
		<%-- 禁止使用 Alt + (方向鍵 ← 或 方向键 →) --%>
		if ((event.altKey) && keyCode === 37 || (event.altKey) && keyCode === 39) {
			alert("不能使用ALT+方向鍵切換上一頁或下一頁");
			return false;
		}
		
		<%-- 禁止使用 F5 刷新頁面 --%>
		if (keyCode === 116) {
			alert("不可使用 F5 刷新頁面");
			event.preventDefault();
		}
		
	});
	
	<%-- 禁止使用滑鼠右鍵 --%>
	$(document).bind("contextmenu", function(event){
		alert("滑鼠右鍵不可用");
		return false
	});

	$(function(){
		<%-- 禁止使用 貼上 --%>
		$("input, textarea").bind("paste", function(event){
			var $this = $(this);
			
			setTimeout(function(){
				var value = $this.val();

				for(var i = 0; i < specialKey.length; i++) {
					var charStr =  specialKey.charAt(i);
					
					if(value.indexOf(charStr) >= 0) {
						$this.val("");
						alert("輸入資料中不可含有特殊字元");
						break;
					}
				}
				
			}, 0);
			
		});
	});

</script>
