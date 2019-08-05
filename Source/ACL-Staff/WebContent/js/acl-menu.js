$(function(){
	//var json ='[{"title":"帳號連結服務","detail":[{"url":"../0201/00.htm","name":"扣款平台管理"},{"url":"../0202/00.htm","name":"服務狀態管理"},{"url":"","name":"帳號連結扣款平台管理"},{"url":"../0204/00.htm","name":"帳號連結扣款限額管理"}]},{"title":"交易管理","detail":[{"url":"../0301/00.htm","name":"交易結果查詢"}]},{"title":"最新消息公告設定","detail":[{"url":"","name":"公告內容管理"},{"url":"","name":"圖形化活動公告管理"}]},{"title":"報表管理","detail":[{"url":"../0501/00.htm","name":"約定客戶統計"},{"url":"../0502/00.htm","name":"交易量統計"},{"url":"","name":"退貨統計"},{"url":"../0504/00.htm","name":"手續費統計"}]},{"title":"權限管理","detail":[{"url":"../0901/00.htm","name":"角色設定"},{"url":"../0902/00.htm","name":"操作記錄查詢"}]}]';
    var json = '[{"title":"帳號連結服務","detail":[{"url":"../0201/00.htm","name":"扣款平台管理"},{"url":"../0202/00.htm","name":"服務狀態管理"},{"url":"../0204/00.htm","name":"交易限額管理"},{"url":"../0205/00.htm","name":"扣款平台查詢"},{"url":"../0206/00.htm","name":"服務狀態查詢"},{"url":"../0207/00.htm","name":"交易限額查詢"}]},{"title":"交易管理","detail":[{"url":"../0301/00.htm","name":"交易結果查詢"}]},{"title":"公告訊息管理","detail":[{"url":"../0401/00.htm","name":"最新消息公告設定"}]},{"title":"報表管理","detail":[{"url":"../0501/00.htm","name":"約定客戶統計"},{"url":"../0502/00.htm","name":"交易量統計"},{"url":"../0504/00.htm","name":"手續費統計"}]},{"title":"權限管理","detail":[{"url":"../0901/00.htm","name":"角色設定"},{"url":"../0902/00.htm","name":"操作記錄查詢"}]}]';
	
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
});

function addMega() {
	$(this).children("div").slideDown(300);
}

function removeMega() {
	$(this).children("div").slideUp(300);
}

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
					menu += "<tr><td><a href='"+obj[list].detail[detail].url+"'>"+obj[list].detail[detail].name+"</a></td></tr>";
				}
			}
			menu += "</tbody></table></div>";
		};
		menu += "</li>";
	}
	menu += "</ui>";
	return menu;
}

function createHeader(){
	$("div.container").prepend("<div class='header'></div>");
	$("div.header").append("<div id='brand' style='text-align:left;'></div>");
	$("div#brand").append("<img id='tsib_logo' width='200' src='../images/bankHeader.gif' alt='Bank' title='' style='width:200px;'/>&nbsp;");
	$("div#brand").append("<img id='sys_name' width='200' src='../images/sys_name.gif' alt='帳務連結扣款系統' title='' style=';width:200px;margin-right:480px;' />&nbsp;");
	$("div#brand").append("<img id='staff_title' width='105' src='../images/staff_title.gif' alt='銀行端' title='' style=';width:80px;' />&nbsp;");
//	$("div#brand").append("<span style='text-align:center; font-size:14pt; color:#000000; width:100px;float:right;margin-top:5px;'>銀行端</span>");
	$("div.header").append("<div id='navbar' style='text-align:left;'></div>");
	$("div.header").append("<div id='userinfo'>歡迎 李大同</div>");
	$("div.header").append("<div id='logoff'><a href='#' onclick='logOff();'>離開</a></div>");
	$("div.header").append("<div id='logTime'>上次登入時間為: 2015/09/23 13:10:02</div>");
}

function createFooter(){
	$("div.container").append("<br/><div style='margin-top:20px;'></div>");
	$("body").append("<br/><div class='footer'></div>");
	$("div.footer").append("<div class='footerLeft'></div>");
	$("div.footer").append("<div class='footerRight'></div>");
	$("div.footer div.footerLeft").append("&copy;台新國際商業銀行   Copyright Taishin International Bank. All Rights Reserved.");
	//$("div.footer div.footerLeft").append("建議瀏覽器版本:IE8以上");
	$("div.footer div.footerRight").append("<img src='../images/tsb001364.gif'>");
}