<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Locale"%>
<%@ page import="com.hitrust.acl.util.StringUtil"%>
<%@ page import="com.hitrust.framework.util.Constants"%>
<%@ page import="com.hitrust.bank.model.LoginUser"%>

<%-- include JSLT tag librarys --%>
<%@ include file="/include/taglibs.jsp" %>

<%
	String root = request.getContextPath();
	int sessionTime = request.getSession().getMaxInactiveInterval();
	
	LoginUser user = (LoginUser) request.getSession().getAttribute(Constants.LOGIN_USER);

	// Browser 預設語系 繁體中文
	Locale locale = new Locale("zh", "TW");
	
	if(!StringUtil.isBlank(user)) {
		locale = user.getLocale();
	}

%>

<%-- 宣告 i18n --%>
<fmt:setBundle basename="i18n/message" var="msg" />
<fmt:setBundle basename="i18n/common" var="comm" />

<%-- 設定 i18n 語系 --%>
<fmt:setLocale value="<%=locale %>" />