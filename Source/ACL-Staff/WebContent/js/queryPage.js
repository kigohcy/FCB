function isnum(val) {
	
	if(val == null || val == ""){
		return false  
	}  
	var oneDecimal = false
	var str = val.toString()
	
	for(var i=0; i < str.length;i++) {
		var ch = str.charAt(i);
		if(ch < '0' || ch > '9') return false
	};
	
	return true;
}

function gotoPage(page) {
	$("input[name='page.pageNo']").val(page);
	var totalPage = parseInt($("#totalPage").val(), 10);
	var tagetPage = parseInt(page, 10);
	var nowPage = parseInt($("#nowPage").val(), 10);
	
	if(!isnum(page)) {
		alert("輸入頁數僅可輸入數字");
		return;
	}

	if(tagetPage < 1) {
		alert("輸入頁數必須大於0, 且小於等於總頁數");
		return;
	}

	if(tagetPage > totalPage){
		alert("輸入頁數必須大於0, 且小於等於總頁數");
		return;
	}
	
	if(tagetPage == nowPage){
		return;
	}
	
	$("#formPage").submit();
	
}

