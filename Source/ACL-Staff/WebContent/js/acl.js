
function formValidate(form, rules, messages) {
	$("#" + form).validate({
        rules: rules,
        messages: messages,
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
}

function datePicker(ContextPath) {
    $("input[datePicker=true]").datepicker({
        showOn: "button",
        buttonImage: ContextPath+"/images/calendar.png",
        buttonImageOnly: true,
        buttonText: "選擇日期",
        changeMonth: true,
        changeYear: true,
        dateFormat: "yy/mm/dd"
    });
}

function logOff(url) {
    if (confirm('是否確認登出系統?')) {
        if (url != undefined) {
            window.location.href = url;
        } else {
            window.location.href = "../login.htm";
        }
    }
}

function openLeglCode(from1) {
    var nurl = "../popup/popup_legcodes.htm";
    var dialogFeatures = "dialogWidth:800px;dialogHeight:500px;center:yes;resizable=yes;help:no;status:yes";
    var rtnData = window.showModalDialog(nurl, window, dialogFeatures);
    
    //for chrome
    if (rtnData == undefined) {
        rtnData = window.ReturnValue;
    }
    if (rtnData != null) {
        if (form1.legdesc != null) form1.legdesc.value = rtnData[1]; ;
    }
}

function btnNew(url) {
    if (confirm("是否確定 新增 此筆資料?")) {
        window.location.href = url;
    }
}

function btnMdfy(url) {
    if (confirm("是否確定 修改 此筆資料?")) {
        window.location.href = url;
    }
}

function btnDel(url) {
    if (confirm("是否確定 刪除 此筆資料?")) {
        window.location.href = url;
    }
}


/**
 * Ajax function
 * @param url		URL 	e.g /login.html
 * @param dataType	回傳類別 e.g json or html
 * @param params	請求參數 e.g {userId: $("#userId").val(), userPswd: $("#userPswd").val()} 
 * @param callback	callback function
 */
function aclAjax(url, dataType, params, callback) {
	$.ajax({
		url: root + url + "?sessionKey=" + sessionId,
		type: "POST",
		cache: false,
		dataType: dataType,
		data: params,
		success: callback,
		error: function(xhr, textStts, errorThrown) {
			
		}
	});
}
