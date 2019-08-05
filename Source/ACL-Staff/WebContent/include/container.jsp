<%
/*
 * @(#)include/initialPrint.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description:
 *
 * Modify History:
 * v1.00, 2016/01/25, Eason Hsu
 * 	1) First Release
 * v1.01, 2017/05/03, Eason Hsu
 *  1) TSBACL-147, 客服系統輸入欄位無法 Key in "p" 或 "P"
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="/include/initial.jsp" %>

<script type="text/javascript">
	var root = '<%=root%>';
    var userId = '${loginUser.userId }';
    var userName = '${loginUser.userName }';
    var json = '${loginUser.jsonMenu }';
    var sessionId = '${loginUser.sessionId}';

    <%-- session timeout --%>
    var iOriginalTimeout = <%=sessionTime%>;
    var iSessionTimeout = iOriginalTimeout;
    var iAlertTimeout = 60;
    var dTimeout = new Date();

	$(function(){

		createHeader();
		$("div#navbar").append(cerateMenu(json));
 		createFooter();

		var megaConfig = {    
			interval: 100,
			sensitivity: 4,
			over: addMega,
			timeout: 100,

			out: removeMega
		}; 
		
		$("li.menuList").hoverIntent(megaConfig);

		<%-- AP 端檢核是否為同 sessionId 使用--%>
		$("form").append("<input type='hidden' name='sessionKey' value='" + sessionId + "' />");
		
		<%-- 將分頁初始值設定為 1 --%>
		$("form[id!=formPage][id!=formDetl]").append("<input type='hidden' name='page.pageNo' value='1' />");

		<%-- 初始 Browser 回上/下頁, Browser 關閉時需處理的事件 --%>
		initBeforeunload();

		<%-- session timeout--%>
		countSessionTimeout();

		<%-- 無列印權限不顯示列印按鈕 --%>
// 		if (!hasPrinting) {
// 			$("div.btnContent #btnPrint").hide();
			<%-- v1.01, 移除限制 P 不可輸入 --%>
// 		}

		<%-- 無另存權限不顯示下載按鈕 --%>
// 		if (!hasSaveAs) {
// 			$("div.btnContent #btnDownload").hide();
// 		}
		
	});

	function addMega() {
		$(this).children("div").slideDown(300);
	}

	function removeMega() {
		$(this).children("div").slideUp(300);
	}
		
	// 產生功能 Menu
	function cerateMenu(json){
		var obj = jQuery.parseJSON(json);
		var menu = "<ui class='menu'>";
		for(var list = 0 ; list < obj.length ; list++){
			menu += "<li class='menuList'>";
			menu += "<a href='#'>" + obj[list].title + "</a>"
			var dLength = obj[list].detail.length;
			if(dLength > 0){
				menu += "<div class='menuDetail'><table>"
				menu += "<thead><tr><td>"+obj[list].title+"</td></tr></thead><tbody>"
				for(var detail = 0 ; detail < dLength ; detail++){
					if(obj[list].detail[detail].url == ""){
						menu += "<tr><td>"+obj[list].detail[detail].name+"</td></tr>";
					}else{
						menu += "<tr><td><a href='" + root + obj[list].detail[detail].url + "?sessionKey=" + sessionId + "'>"+obj[list].detail[detail].name+"</a></td></tr>";
					}
				}
				menu += "</tbody></table></div>";
			};
			menu += "</li>";
		}
		menu += "</ui>";
		return menu;
	}
	
	// 產生 Header
	function createHeader(){
		$("div.container").prepend("<div class='header'></div>");
		$("div.header").append("<div id='brand' style='text-align:left;'></div>");
		$("div#brand").append("<img id='tsib_logo' width='200' src='"+ root + "/images/bankHeader.gif' alt='Bank' title='' style='width:200px;'/>&nbsp;");
		$("div#brand").append("<img id='sys_name' width='200' src='" + root + "/images/sys_name.gif' alt='帳務連結扣款系統' title='' style=';width:200px;margin-right:480px;' />&nbsp;");
		$("div#brand").append("<img id='staff_title' width='105' src='"+ root + "/images/staff_title.gif' alt='銀行端' title='' style=';width:80px;' />&nbsp;");
// 		$("div#brand").append("<span style='text-align:center; font-size:14pt; color:#000000; width:100px;float:right;margin-top:5px;'>銀行端</span>");
		$("div.header").append("<div id='navbar' style='text-align:left;'></div>");
		$("div.header").append("<div id='userinfo'>" + '<fmt:message key="message.sys.Welcome" />' + " " + userName + "</div>");
		$("div.header").append("<div id='logoff'><a href='#' onclick='logOff(false);'>" + '<fmt:message key="message.sys.Logout" />'  +"</a></div>");
		$("div.header").append("<div id='logTime'>" + '<fmt:message key="message.sys.sessionTimeout" />' + "</div>");
	}
	
	// 產生 footer
	function createFooter(){
 		$("div.container").parent().append("<div id='dialogBlock' style='display:none;'></div>");
// 		$("div.container").append("<br/><div style='margin-top:20px;'></div>");
// 	    $("body").append("<br/><div class='push'></div>");
// 	    $("body").append("<div class='footer'></div>");
// 	    $("div.footer").append("<div class='footerContent'></div>");
// 	    $("div.footerContent").append("<div class='copyright'></div>");
// 	    $("div.footerContent").append("<div class='tslogo'></div>");
// 	    $("div.footerContent div.copyright").append("&copy;xxxx商業銀行   Copyright xxxxx International Bank. All Rights Reserved.");
// 	    $("div.footerContent div.tslogo").append("<img src='"+ root + "/images/tsb001364.gif'>");
	}

	// 登出系統
	function logOff(flag) {

		if (flag == true) {
			$("#dialogBlock").text("<fmt:message key="message.sys.confirmLogout" />");
			$("#dialogBlock").dialog({
				resizable: false,
				height: 140,
				width: 300,
				modal: true,
				title: "<fmt:message key="message.sys.ConfirmDialog" />",
				buttons: {
	  	        	"<fmt:message key="common.btn.OK" />": function() {
	  	        		$("#form1").prop("action", root + "/logout.html");
	  					$("#form1").submit();
	  	          		$(this).dialog("close");
	  	        	},
	  	        	"<fmt:message key="common.btn.Cancel" />": function() {
	  	          		$(this).dialog("close");
	  	      		}
	  	      	}
			});
			
		} else {
			$("#form1").prop("action", root + "/logout.html");
			$("#form1").submit();
		}
	}

	<%-- Session timeout --%>
	function countSessionTimeout() {
        dTimeout.setTime(iSessionTimeout * 1000);
        var min = (dTimeout.getMinutes() < 10) ? "0" + dTimeout.getMinutes() : dTimeout.getMinutes();
        var sec = (dTimeout.getSeconds() < 10) ? "0" + dTimeout.getSeconds() : dTimeout.getSeconds();

        $("#logTime").html('<fmt:message key="message.sys.AutoLogout1" />' + "<span style='color:red;font-size:11pt;'> " + min + ":" + sec + " </span>" + '<fmt:message key="message.sys.AutoLogout2" />');
        
		iSessionTimeout--;

        if (iSessionTimeout == -1) {
           logOff(false);
        } else {
            if (min == "01" && sec == "00") {
            	timeoutConfirm(); 
            }
            setTimeout("countSessionTimeout()", 1000);
        }
    }

	<%-- 倒數計時結束前確認訊息 --%>
    function timeoutConfirm() {
        $("#dialogBlock").html('<span style="font-size:13px;"><fmt:message key="message.sys.sessionTimeout" /></span>');
    	$("#dialogBlock").dialog({
  	    	resizable: false,
  	    	height:140, 
  	    	width: 300,
  	    	modal: true,
  	    	title: "<fmt:message key="message.sys.ConfirmDialog" />",
  	      	buttons: {
  	        	"<fmt:message key="common.btn.Continue" />": function() {
      	        	resetTime();
  	          		$(this).dialog("close");
  	        	},
  	        	"<fmt:message key="common.btn.Cancel" />": function() {
  	          		$(this).dialog("close");
  	      		}
  	      	}
  	      
    	});
    }

    <%-- 重新計時, 倒數計時器 --%>
    function resetTime() {
    	iSessionTimeout = iOriginalTimeout;

    	<%-- 同步 session time --%>
		aclAjax("/resetSessionTime.html", null, {sessionKey: sessionId}, null);
    }

    <%-- 初始 Browser 回上/下頁, Browser 關閉時需處理的事件 --%>
	function initBeforeunload() {
		var userAgent = window.navigator.userAgent;
		
		$(window).on("beforeunload", function(event) {
			$.ajax({
				url: root + "/logout.html",
				type: "GET",
				data: {sessionKey: sessionId}
			});
		});

		<%-- form submit & link  不觸發 beforeunload --%>
		$("form").submit(function() {
			$(window).off("beforeunload");
			
		});

		$("a").click(function(){
			$(window).off("beforeunload");
		});

	}

</script>
