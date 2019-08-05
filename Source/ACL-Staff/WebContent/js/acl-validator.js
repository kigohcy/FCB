// 身分證格式驗證
$.validator.addMethod("ID_CHECKER", function(value, element) {
	var result = /^[A-Z]{1}[1-2]{1}[0-9]{8}$/.test(value);

	return this.optional(element) || result;
}, "身分證格式錯誤");

// 密碼格式驗證
$.validator.addMethod("PSWD_CHECKER", function(value, element) {
	var result = false;
	var minLength = 8;
	var maxLength = 16;

	if (minLength <= value.length & value.length <= maxLength) {
		var str = value.toUpperCase();
		var flag1 = 0; // 連續遞增驗證flag ex:123 abc Abc ABC
		var flag2 = 0; // 連續遞減驗證flag ex:321 CBa cba CBA
		var flag3 = 0; // 重複驗證flag ex: 111 aaa AaA AAA
		var limit = 3; // 連續次數限制

		for (var i = 1; i < str.length; i++) {
			var currChar = str.charCodeAt(i);
			var preChar = str.charCodeAt(i - 1);

			// 檢查字串是否含有連續遞增的字段
			if (currChar - preChar == 1) {
				flag1++;
				if (flag1 >= limit - 1)
					break;
			} else {
				flag1 = 0;
			}

			// 檢查字串是否含有連續遞減的字段
			if (preChar - currChar == 1) {
				flag2++;
				if (flag2 >= limit - 1)
					break;
			} else {
				flag2 = 0;
			}

			// 檢查字串是否含有連續重複的字元
			if (preChar - currChar == 0) {
				flag3++;
				if (flag3 >= limit - 1)
					break;
			} else {
				flag3 = 0;
			}
		}
		result = !((flag1 >= limit - 1) || (flag2 >= limit - 1) || (flag3 >= limit - 1));
	}

	return this.optional(element) || result;
}, "密碼格式錯誤");

// IP格式驗證
$.validator
		.addMethod(
				"IP_CHECKER",
				function(value, element) {
					var result = /^(\d|[1-9]\d|1\d\d|2([0-4]\d|5[0-5]))\.(\d|[1-9]\d|1\d\d|2([0-4]\d|5[0-5]))\.(\d|[1-9]\d|1\d\d|2([0-4]\d|5[0-5]))\.(\d|[1-9]\d|1\d\d|2([0-4]\d|5[0-5]))$/
							.test(value);
					return this.optional(element) || result;
				}, "IP 格式錯誤");

// 憑證序號格式驗證
$.validator.addMethod("CERTSN_CHECKER", function(value, element) {
	var result = /^[a-fA-F0-9]{8}$/.test(value);
	return this.optional(element) || result;
}, "憑證序號格式錯誤");

// 日期格式驗證
$.validator.addMethod("DATE_FRMT_CHECKER", function(value, element) {
	//修正IE8 parse "08"、"09" 時錯誤
	function parseForIE8(str) {
        var rtnStr;
        if (str == "08" || str == "09") {
            rtnStr = str.substr(1, 1);
        } else {
            rtnStr = str;
        }
        return rtnStr;
    }
	
	var result;
	
	if(/^([1-9]\d{3})\/(0[1-9]|1[0-2])\/(0[1-9]|[1-2]\d|3[0-1])$/.test(value)){
		var splitDate = value.split("\/");
		dateStr=splitDate[0]+splitDate[1]+splitDate[2];
	    
	    //用日期字串產生日期物件，兩者再分別比對年月日是否一致
		var sub1 = parseInt(dateStr.substr(0, 4));
		var sub2 = parseInt(parseForIE8(dateStr.substr(4, 2))-1);
		var sub3 = parseInt(parseForIE8(dateStr.substr(6, 2)));
		
		a = new Date(sub1, sub2, sub3);
	    
	    if(a.getFullYear() != sub1){//比對年
	    	result= false;
	    }else if(a.getMonth() != sub2){//比對月
	    	result= false;
	    }else if(a.getDate() != sub3){//比對日
	    	result= false;
	    }else{
	    	result= true;
	    }
	}else{
		result = false;
	}

	return this.optional(element) || result;
}, "日期格式錯誤");

// 日期比對
$.validator.addMethod("DATE_COMPARE", function(value, element, params) {
	var result = false;
	var date1 = $(params.targetDate).val();
	var date2 = value;
	var compareType = params.compareType;

	var regexRule = /^([1-9]\d{3})\/(0[1-9]|1[0-2])\/(0[1-9]|[1-2]\d|3[0-1])$/;

	if (!regexRule.test(date1)) {
		$.validator.messages["DATE_COMPARE"] = "目標日日期格式錯誤";
		return false;
	}

	if (!regexRule.test(date2)) {
		$.validator.messages["DATE_COMPARE"] = "預設日日期格式錯誤";
		return false;
	}

	switch (compareType) {
	case 'B':
		if (Date.parse(date1) <= Date.parse(date2)) {
			result = true;
		} else {
			$.validator.messages["DATE_COMPARE"] = "預設日不可小於目標日";
		}
		break;
	case 'L':
		if (Date.parse(date1) >= Date.parse(date2)) {
			result = true;
		} else {
			$.validator.messages["DATE_COMPARE"] = "預設日不可大於目標日";
		}
		break;
	case 'E':
		if (Date.parse(date1) == Date.parse(date2)) {
			result = true;
		} else {
			$.validator.messages["DATE_COMPARE"] = "預設日不可等於目標日";
		}
		break;
	}
	return this.optional(element) || result;
}, "");

// 數字格式驗證
$.validator.addMethod("NUM_CHECKER", function(value, element, params) {
	var result = false;
	var rule1 = /^-?[1-9]+\d*$/;
	var rule2 = /^-?(\d|[1-9]+\d*)(\.\d+){1}$/;
	var rule3 = /^([0-9][0-9]*)$/;

	switch (params.isNum) {
	case 0:
		if (value == '0') {
			result = true;
		} else {
			result = rule1.test(value);
		}
		break;
	case 1:
		if (value.charAt(0) == '-' & value == 0) {
			break;
		}

		var digits = params.point;

		if (params.necessary) {
			var test = rule2.test(value);
			if (test) {
				var res = value.split(".");

				if (digits == res[1].length) {
					result = true;
				}
			}
		} else {
			if (value == '0') {
				result = true;
			} else {
				var test = rule1.test(value);
				if (test) {
					result = true;
				} else {
					var test2 = rule2.test(value);
					if (test2) {
						var res = value.split(".");
						if (digits >= res[1].length) {
							result = true;
						}
					}
				}
			}
		}

		break;
	case 2:
		 if(value == ''){
			 result = true;
		 }else if(rule3.test(value)){
			result = true;
		 }
		break;	
	}
	return this.optional(element) || result;
}, "數字格式錯誤");

// 金額不可小於零
$.validator.addMethod("AMOUNT_CHECKER", function(value, element) {
	var result = value >= 0;

	return this.optional(element) || result;
}, "金額不可為負數");

// 檢查文字是否為英數字
$.validator.addMethod("ALPHA_CHECKER", function(value, element) {
	var result = /^[a-zA-Z0-9-_]*$/.test(value);

	return this.optional(element) || result;
}, "只可輸入英數字");

// 檢查文字是否為英數字及-_
$.validator.addMethod("ALPHA_DASH_CHECKER", function(value, element) {

	var result = value.search(/^[a-zA-Z0-9-_]*$/) == 0 ? true :false;
	
	return this.optional(element) || result;
}, "只可輸入英數字");

/* 檢查是否符合長度條件
 * min:最小長度限制
 * max:最大長度限制
 * 若傳入-1則不檢核該條件 
 * ex: min:-1,max:40。則最小長度不做檢核
 */ 
$.validator.addMethod("LENGTH_CHECKER", function(value, element, params) {
	var result = false;
	var minLength = params.min;
	var maxLength = params.max;
	var trimStr = value.replace(/\s+/g, "");
	var str = trimStr.split('');
	var L = 0;

	for(var i = 0 ; i<str.length ; i++){
		if(str[i].match(/[^\x00-\xff]/g)){
			L=L+2;
		}else{
			L=L+1;
		}
	}
	
	if (minLength != -1 && maxLength != -1) {
		if (minLength <= L & L <= maxLength) {
			result = true;
		}
	} else if (minLength != -1) {
		if (minLength <= L) {
			result = true;
		}
	} else if (maxLength != -1) {
		if (L <= maxLength) {
			result = true;
		}
	} else {
		result = true;
	}

	return this.optional(element) || result;
}, "資料長度錯誤");

/**
 * 全/半型驗證
 * 
 * @param fontCase
 *            0:半形驗證 1:全形驗證
 */
$.validator.addMethod("FULL_HALF_CHECKER", function(value, element, params) {
	var result = true;
	var illegal;
	if (params.fontCase == 0) {
		illegal = value.match(/[^\x00-\xff]/g);
	} else if (params.fontCase == 1) {
		illegal = value.match(/[\x00-\xff]/g);
	}

	if (illegal) {
		result = false;
	}

	return this.optional(element) || result;
}, "");

// 驗證表單下的所有checkbox
$.validator.addMethod("CHECKBOX_CHECKER", function(value, element, params) {
	var attr = params.attr;
	var startWith = params.startWith;
	var checkboxes = $(element).parents("form").find("input[" + attr + "^='" + startWith + "']");
	var checked = 0;

	for (var i = 0; i < checkboxes.length; i++) {
		if ($(checkboxes[i]).prop("checked")) {
			checked++;
		}
	}

	return checked;
}, "請至少選擇一筆資料");

/**
 * 檢核至少填入N個欄位
 * 
 * @param atLast
 *            最少應填寫的欄位數
 * @param fields
 *            欲檢核欄位的name值
 */
$.validator.addMethod("ACL_REQUIRE", function(value, element, params) {
	var atLast = params.atLast;
	var fields = params.fields;
	var count = 0;
	var result = false;

	for (var i = 0; i < fields.length; i++) {
		var value = $("input[name=" + fields[i] + "]").val();
		if (value != "") {
			count++;
		}
	}

	if (atLast <= count)
		result = true;

	return result;
}, "請至少選擇一筆資料填入");

$.validator.addMethod("RANGE_VALIDATE", function(value, element, params) {
	var dateStart = $(params.dateStart).val();
	var dateEnd = value;
	var limt = params.limt;
	var result = false;
	if (limt.match(/^[1-9]+\d*M/g)) {
		dateStart = dateStart.split("/");
		dateEnd = dateEnd.split("/");
		limt = limt.split("M");

		// 跨年度
		if ((dateEnd[0] - dateStart[0]) > 0) {
			if ((dateEnd[1] - (dateStart[1] - 12)) < limt[0]) {
				result = true;
			}
			if ((dateEnd[1] - (dateStart[1] - 12)) == limt[0]) {
				// 日期
				if (dateStart[2] >= dateEnd[2]) {
					result = true;
				}
			}
		}

		// 同年度
		if ((dateEnd[0] - dateStart[0]) == 0) {
			// 月份相減小於限制
			if ((dateEnd[1] - dateStart[1]) < limt[0]) {
				result = true;
			}
			// 月份相減等於限制
			if ((dateEnd[1] - dateStart[1]) == limt[0]) {
				// 日期
				if (dateStart[2] >= dateEnd[2]) {
					result = true;
				}
			}
		}
	} else {
		var start = new Date(dateStart);
		var end = new Date(dateEnd)
		limt = limt.split("D");
		var r = end.getTime() - start.getTime();
		if (Math.floor(r / (1000 * 60 * 60 * 24)) <= limt[0]) {
			result = true;
		}
	}
	return result;
}, "查詢區間超出限制範圍");

$.validator.addMethod("RANGE_VALIDATE2", function(value, element, params) {
	var dateStart = $(params.dateStart).val();
	var dateEnd = params.dateEnd;
	var limt = params.limt;
	var result = false;
	if (limt.match(/^[1-9]+\d*M/g)) {
		dateStart = dateStart.split("/");
		dateEnd = dateEnd.split("/");
		limt = limt.split("M");

		// 跨年度
		if ((dateEnd[0] - dateStart[0]) > 0) {
			if ((dateEnd[1] - (dateStart[1] - 12)) < limt[0]) {
				result = true;
			}
			if ((dateEnd[1] - (dateStart[1] - 12)) == limt[0]) {
				// 日期
				if (dateStart[2] >= dateEnd[2]) {
					result = true;
				}
			}
		}

		// 同年度
		if ((dateEnd[0] - dateStart[0]) == 0) {
			// 月份相減小於限制
			if ((dateEnd[1] - dateStart[1]) < limt[0]) {
				result = true;
			}
			// 月份相減等於限制
			if ((dateEnd[1] - dateStart[1]) == limt[0]) {
				// 日期
				if (dateStart[2] >= dateEnd[2]) {
					result = true;
				}
			}
		}
	} else {
		var start = new Date(dateStart);
		var end = new Date(dateEnd)
		limt = limt.split("D");
		var r = end.getTime() - start.getTime();
		if (Math.floor(r / (1000 * 60 * 60 * 24)) <= limt[0]) {
			result = true;
		}
	}
	return result;
}, "查詢區間超出限制範圍");

//check 整數長度
$.validator.addMethod("INT_LENGTH_CHECKER", function(value, element, params) {
	var result = false;
	var len = value.split(".");
	if(params.maxIntLen >= len[0].length){
		result = true;
	}
	
	return result;
},"整數長度有誤");

// mutil email
$.validator.addMethod( "multiemails", function(value, element, params) {
	
     if(value == ''){
    	 return true;
     }

     var email = "[A-Za-z0-9\._%-]+@[A-Za-z0-9\.-]+\.[A-Za-z]{2,4}";
     var re = new RegExp('^'+email+'(;\\n*'+email+')*;?$');
     
     return re.test(value);

},"電子郵件格式有誤");



