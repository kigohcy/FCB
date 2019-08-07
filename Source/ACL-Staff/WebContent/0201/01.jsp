
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
			
			// i18n  error wording
			var maxIntLenMsg = "<fmt:message key="message.alert.maxIntLen" />";
			maxIntLenMsg = maxIntLenMsg.replace('#', '5');
			
			 $("#form1").validate({
	                rules: {
	                	ecId:{
	                		required:true,
	                		NUM_CHECKER:{
	                			isNum:2
	                		}
	                	},
	                	ecNameCh:{
	                		required:true,
	                		LENGTH_CHECKER:{
	                			min:-1,
	                			max:20
	                		}
	                	},
	                	ecNameEn:{
	                		required:false,
	                		LENGTH_CHECKER:{
	                			min:-1,
	                			max:20
	                		},
	                		ALPHA_DASH_CHECKER: true
	                	},
	                	feeRate:{
	                		required:true,
	                		NUM_CHECKER:{
	                			isNum:1,
	                			point:0
	                		},
	                		INT_LENGTH_CHECKER:{
	                			maxIntLen:5
	                		}
	                	},
	                	taxRate:{
	                		NUM_CHECKER:{
	                			isNum:1,
	                			point:0
	                		},
	                		INT_LENGTH_CHECKER:{
	                			maxIntLen:5
	                		}
	                	},
	                	minFee:{
	                		NUM_CHECKER:{
	                			isNum:1,
	                			point:0
	                		},
	                		INT_LENGTH_CHECKER:{
	                			maxIntLen:3
	                		}
	                	}, 
	                	maxFee:{
	                		NUM_CHECKER:{
	                			isNum:1,
	                			point:0
	                		},
	                		INT_LENGTH_CHECKER:{
	                			maxIntLen:3
	                		}
	                	}, 
	                	minTax:{
	                		NUM_CHECKER:{
	                			isNum:1,
	                			point:0
	                		},
	                		INT_LENGTH_CHECKER:{
	                			maxIntLen:3
	                		}
	                	}, 
	                	maxTax:{
	                		NUM_CHECKER:{
	                			isNum:1,
	                			point:0
	                		},
	                		INT_LENGTH_CHECKER:{
	                			maxIntLen:3
	                		}
	                	}, 
	                	entrNo:{
	                		required:true,
	                		NUM_CHECKER:{
	                			isNum:2
	                		},
	                		LENGTH_CHECKER:{
	                			min:5,
	                			max:7
	                		}
	                	},
	                	linkLimit:{
	                		required:true,
	                		NUM_CHECKER:{
	                			isNum:1
	                		},
	                		LENGTH_CHECKER:{
	                			min:1,
	                			max:2
	                		}
	                	},
	                	realAcnt:{
	                		required:true,
	                		NUM_CHECKER:{
	                			isNum:2
	                		},
	                		LENGTH_CHECKER:{
	                			min:11,
	                			max:11
	                		}
	                	},
	                	entrId:{
	                		LENGTH_CHECKER:{
	                			min:8,
	                			max:9
	                		},
	                		ALPHA_DASH_CHECKER: true
	                	},
	                	cntc:{
	                		LENGTH_CHECKER:{
	                			min:-1,
	                			max:20
	                		}  
	                	},
	                	tel:{
	                		LENGTH_CHECKER:{
	                			min:-1,
	                			max:20
	                		}  
	                	},
	                	mail:{
	                		multiemails: true
	                	},
	                	note:{
	                		LENGTH_CHECKER:{
	                			min:-1,
	                			max:64
	                		}  
	                	}
	                },
	                messages:{
	                	ecId:{
	                		required:"<fmt:message key="message.alert.pleaseKeyIn" /><fmt:message key="F0201.field.EC_ID" />",
	                		NUM_CHECKER:"<fmt:message key="F0201.field.EC_ID" /><fmt:message key="message.alert.onlyNum" />"
	                	},
	                	ecNameCh:{
	                		required:"<fmt:message key="message.alert.pleaseKeyIn" /><fmt:message key="F0201.field.EC_NAME_CH" />",
	                		LENGTH_CHECKER:"<fmt:message key="F0201.field.EC_NAME_CH" /><fmt:message key="message.alert.overLength" />"
	                	},
	                	ecNameEn:{
	                		LENGTH_CHECKER:"<fmt:message key="F0201.field.EC_NAME_EN" /><fmt:message key="message.alert.overLength" />",
	                		ALPHA_DASH_CHECKER :"<fmt:message key="F0201.field.EC_NAME_EN" /> <fmt:message key="message.alert.onlyEN&NUM" />"
	                	},
	                	feeRate:{
	                		required:"<fmt:message key="message.alert.pleaseKeyIn" /><fmt:message key="F0201.field.FEE_RATE" />", 
	                		NUM_CHECKER:"<fmt:message key="F0201.field.FEE_RATE" /><fmt:message key="message.alert.formateError" />",
	                		INT_LENGTH_CHECKER:"<fmt:message key="F0201.field.FEE_RATE" />" +maxIntLenMsg
	                	},
	                	taxRate:{
	                		NUM_CHECKER:"<fmt:message key="F0201.field.TAX_RATE" /><fmt:message key="message.alert.formateError" />",
	                		INT_LENGTH_CHECKER:"<fmt:message key="F0201.field.TAX_RATE" />" +maxIntLenMsg
	                	},
	                	minFee:{
	                		NUM_CHECKER:"<fmt:message key="F0201.field.MIN_FEE" /><fmt:message key="message.alert.formateError" />",
	                		INT_LENGTH_CHECKER:"<fmt:message key="F0201.field.MIN_FEE" />" +maxIntLenMsg
	                	},
	                	maxFee:{
	                		NUM_CHECKER:"<fmt:message key="F0201.field.MAX_FEE" /><fmt:message key="message.alert.formateError" />",
	                		INT_LENGTH_CHECKER:"<fmt:message key="F0201.field.MAX_FEE" />" +maxIntLenMsg
	                	},
	                	minTax:{
	                		NUM_CHECKER:"<fmt:message key="F0201.field.MIN_TAX" /><fmt:message key="message.alert.formateError" />",
	                		INT_LENGTH_CHECKER:"<fmt:message key="F0201.field.MIN_TAX" />" +maxIntLenMsg
	                	},
	                	maxTax:{
	                		NUM_CHECKER:"<fmt:message key="F0201.field.MAX_TAX" /><fmt:message key="message.alert.formateError" />",
	                		INT_LENGTH_CHECKER:"<fmt:message key="F0201.field.MAX_TAX" />" +maxIntLenMsg
	                	},
	                	entrNo:{
	                		required:"<fmt:message key="message.alert.pleaseKeyIn" /><fmt:message key="F0201.field.ENTR_NO" />",
	                		LENGTH_CHECKER: "<fmt:message key="message.alert.entrNoLenError" />",
	                		NUM_CHECKER:"<fmt:message key="F0201.field.ENTR_NO" /><fmt:message key="message.alert.onlyNum" />"
	                	},
	                	linkLimit:{
	                		required:"<fmt:message key="message.alert.pleaseKeyIn" /><fmt:message key="F0201.field.LINK_LIMIT" />",
	                		LENGTH_CHECKER: "<fmt:message key="message.alert.linkLimitLenError" />",
	                		NUM_CHECKER:"<fmt:message key="F0201.field.LINK_LIMIT" /><fmt:message key="message.alert.onlyNum" />"
	                	},
	                	realAcnt:{
	                		required:"<fmt:message key="message.alert.pleaseKeyIn" /><fmt:message key="F0201.field.REAL_ACNT" />",
	                		LENGTH_CHECKER: '<fmt:message key="message.alert.realAcntLenError" />',
	                		NUM_CHECKER:"<fmt:message key="F0201.field.REAL_ACNT" /><fmt:message key="message.alert.onlyNum" />"
	                	},
	                	entrId:{
	                		NUM_CHECKER:"<fmt:message key="F0201.field.ENTR_ID" /><fmt:message key="message.alert.onlyEN&NUM" />"
	                	},
	                	cntc:{
	                		LENGTH_CHECKER:"<fmt:message key="F0201.field.CNTC" /><fmt:message key="message.alert.overLength" />"
	                	},
	                	tel:{
	                		LENGTH_CHECKER:"<fmt:message key="F0201.field.TEL" /><fmt:message key="message.alert.overLength" />"
	                	},
	                	mail:{
	                		multiemails:"<fmt:message key="F0201.field.MAIL" /><fmt:message key="message.alert.formateError" />"
	                	},
	                	note:{
	                		LENGTH_CHECKER:"<fmt:message key="F0201.field.NOTE" /><fmt:message key="message.alert.overLength" />"
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
			 
			    //點選定額收費加的費率檢核
			  	$("input[name=feeType]:eq(0)").click(function() {
			  		$("input[name=feeRate]").rules("remove"); //移除舊的檢核
			  		$("input[name=feeRate]" ).rules( "add", { //增加新的檢核
			  			required:true,
                		NUM_CHECKER:{
                			isNum:1,
                			point:0
                		},
                		INT_LENGTH_CHECKER:{
                			maxIntLen:5
                		},
				  		messages: {
				  			required:"<fmt:message key="message.alert.pleaseKeyIn" /><fmt:message key="F0201.field.FEE_RATE" />", 
	                		NUM_CHECKER:"<fmt:message key="F0201.field.FEE_RATE" /><fmt:message key="message.alert.formateError" />",
	                		INT_LENGTH_CHECKER:"<fmt:message key="F0201.field.FEE_RATE" />" +maxIntLenMsg
				  		}
			  		});
	            });
			    
			    //點選比率收費加的費率檢核
				$("input[name=feeType]:eq(1)").click(function() {
	                $("input[name=feeRate]").rules("remove");	//移除舊的檢核
	                $( "input[name=feeRate]" ).rules( "add", {	//增加新的檢核
	                	required:true,
                		NUM_CHECKER:{
                			isNum:1,
                			point:2,
                			necessary:false
                		},
						INT_LENGTH_CHECKER:{
							maxIntLen:5
                		},
				  		messages: {
				  			required:"<fmt:message key="message.alert.pleaseKeyIn" /><fmt:message key="F0201.field.FEE_RATE" />", 
	                		NUM_CHECKER:"<fmt:message key="F0201.field.FEE_RATE" /><fmt:message key="message.alert.formateError" />",
	                		INT_LENGTH_CHECKER:"<fmt:message key="F0201.field.FEE_RATE" />" +maxIntLenMsg
				  		}
			  		});
	            });

				
                
				
				//費率單位顯示
				//點選定額收費  單位顯示元
				//點選比率收費  單位顯示%
				$('input:radio').click(function () {
			        if ($(this).prop('checked')) {
			        	if($(this).val() == "A"){
			        		$("#unit").html("<fmt:message key="common.dollar" />");//元
			        		$("#minAmt").empty();
			        	}else if($(this).val() == "B"){
			        		$("#unit").html("%");
			        		$("#minAmt").empty();
			        		$("#minAmt").append('&nbsp;&nbsp;&nbsp;最小收取手續費金額:<input type="text" size="3" maxlength="3" name="minFee" value="" id="minFee"/>&nbsp;元&nbsp; ~ &nbsp;最大收取手續費金額:<input type="text" size="3" maxlength="3" name="maxFee" value="" id="maxFee"/>&nbsp;元');
			        	}
			        	//繳費稅
			        	if($(this).val() == "C"){
			        		$("#unitTax").html("<fmt:message key="common.dollar" />");//元
			        		$("#minTaxAmt").empty();
			        	}else if($(this).val() == "D"){
			        		$("#unitTax").html("%");
			        		$("#minTaxAmt").empty();
			        		$("#minTaxAmt").append('&nbsp;&nbsp;&nbsp;最小收取手續費金額:<input type="text" size="3" maxlength="3" name="minTax" value="" id="minTax"/>&nbsp;元&nbsp; ~ &nbsp;最大收取手續費金額:<input type="text" size="3" maxlength="3" name="maxTax" value="" id="maxTax"/>&nbsp;元');
			        	}
			        }
			    });

				$("tr:even").addClass("dataRowEven");
				$("tr:odd").addClass("dataRowOdd");
			    
	    });
		
		 //type: insert, back
		function send(type){
			
			var url = "";
			
			if(type == "insert"){//執行送審電商平台 action
				if($("#form1").valid()){ //review form
					if(confirm("<fmt:message key="message.cnfm.0011" />")){
						url = root+"/0201/sendEcDataInsert.html";
						$("#form1").attr("action", url);
						$("#form1").submit();
					}
				}
			}else if(type == "back"){//執行上一頁 action
				url = root+"/0201/reEcDataMgmt.html";
				$("#form1").attr("action", url);
				$("#form1").submit();
			}
		}
	</script>
</head>
<body>
	<!-- Container ============================================================================================== -->
	<div class="container">
		<!-- Content ------------------------------------------------------------------------>
		<div class="mainContent">
			<div class="content">
				<div class="fnctTitle">
					<fmt:message key="function.Id.F0201" />
					<c:out value=">"></c:out>
					<fmt:message key="common.btn.Add" />
				</div>
				<form method="post" name="form1" id="form1" action=""
					style="margin: 0;">
					<table id="datatable" class="vTable" width="980px;">
						<tr >
							<th><fmt:message key="F0201.field.EC_ID" />*</th>
							<%--平台代號* --%>
							<td>
								<input type="text" size="4"maxlength="4" name="ecId" id="ecId" />
							</td>
						</tr>
						<tr >
							<th><fmt:message key="F0201.field.EC_NAME_CH" />*</th>
							<%--平台中文名稱* --%>
							<td>
								<input type="text" size="20" maxlength="20" name="ecNameCh" id="ecNameCh" />
							</td>
						</tr>
						
						<tr >
							<th><fmt:message key="F0201.field.EC_NAME_EN" />&nbsp;</th>
							<%--平台英文名稱 --%>
							<td>
								<input type="text" size="20" maxlength="20" name="ecNameEn" id="ecNameEn" />
							</td>
						</tr>
						<tr >
							<th><fmt:message key="F0201.field.FEE_TYPE" />*</th>
							<%-- 收費方式* --%>
							<td>
								<input type="radio" name="feeType" value="A" checked /><fmt:message key="F0201.field.FEE_TYPE.A"/>&nbsp;
							    <input type="radio"	name="feeType" value="B" /><fmt:message key="F0201.field.FEE_TYPE.B"/>&nbsp;
							</td>
						</tr>
						<tr >
							<th><fmt:message key="F0201.field.FEE_RATE" />*</th>
							<%--費率*--%>
							<td>
								<input type="text" size="9"  maxlength="9" name="feeRate" id="feeRate" /><font id="unit"><fmt:message key="common.dollar" /><%--元 --%></font>
								<span id="minAmt"></span>
							</td>
						</tr>
						<tr >
							<th><fmt:message key="F0201.field.TAX_TYPE" /></th>
							<%-- 繳費稅收費方式* --%>
							<td>
								<input type="radio" name="taxType" value="C" checked /><fmt:message key="F0201.field.TAX_TYPE.C"/>&nbsp;
							    <input type="radio"	name="taxType" value="D" /><fmt:message key="F0201.field.TAX_TYPE.D"/>&nbsp;
							</td>
						</tr>
						<tr >
							<th><fmt:message key="F0201.field.TAX_RATE" /></th>
							<%--繳費稅費率*--%>
							<td>
								<input type="text" size="9"  maxlength="9" name="taxRate" id="taxRate" /><font id="unitTax"><fmt:message key="common.dollar" /><%--元 --%></font>
								<span id="minTaxAmt"></span>
							</td>
						</tr>
						<tr >
							<th><fmt:message key="F0201.field.FEE_STTS" />&nbsp;</th>
							<td>
								<select name="stts" id="stts"><%-- 狀態 --%>
									<option value="00" selected><fmt:message key="F0201.field.FEE_STTS.00" /></option><%--啟用 --%>
									<option value="01"><fmt:message key="F0201.field.FEE_STTS.01" /></option><%--暫停 --%>
									<option value="02"><fmt:message key="F0201.field.FEE_STTS.02" /></option><%--終止 --%>
								</select>
							</td>
						</tr>
						<tr >
							<th><fmt:message key="F0201.field.REAL_ACNT" />*</th>
							<%--實體帳號* --%>
							<td>
								<input type="text" size="11" maxlength="11" name="realAcnt" id="realAcnt" />
							</td>
						</tr>
						<tr >
							<th><fmt:message key="F0201.field.ENTR_NO" />*</th>
							<%--銷帳百分百建檔編號* --%>
							<td>
								<input type="text" size="7" maxlength="7" name="entrNo" id="entrNo" />
							</td>
						</tr>
 						<tr >
							<th><fmt:message key="F0201.field.LINK_LIMIT" />*</th>
							<%--使用者可綁定帳戶數* --%> 
							<td>
								<input type="text" size="2" maxlength="2" name="linkLimit" id="linkLimit" />
							</td>
						</tr>
						<tr >
							<th><fmt:message key="F0201.field.SHOW_REAL_ACNT" />&nbsp;</th>
							<%--實體帳號是否明碼 --%>
							<td>
								<select name="showRealAcnt" id="showRealAcnt">
	                               	<option value="Y" <c:if test="${command.ecData.showRealAcnt eq 'Y'}">selected</c:if>><fmt:message key="F0201.field.SHOW_REAL_ACNT.Y" /></option><%--遮罩 --%>
	                                	<option value="N" <c:if test="${command.ecData.showRealAcnt eq 'N'}">selected</c:if>><fmt:message key="F0201.field.SHOW_REAL_ACNT.N" /></option><%--明碼 --%>
	                           	</select>
							</td>
						</tr>
 						<tr >
							<th><fmt:message key="F0201.field.ENTR_ID" />&nbsp;</th>
							<%--公司統編  --%>
							<td>
								<input type="text" size="11" maxlength="9" name="entrId" id="entrId" />
							</td>
						</tr>
						<tr >
							<th><fmt:message key="F0201.field.CNTC" />&nbsp;</th>
							<%--聯絡人  --%>
							<td>
								<input type="text" size="20" maxlength="20" name="cntc" id="cntc" />
							</td>
						</tr>
						<tr >
							<th><fmt:message key="F0201.field.TEL" />&nbsp;</th>
							<%--聯絡電話  --%>
							<td>
								<input type="text" size="20" maxlength="20" name="tel" id="tel" />
							</td>
						</tr>
						<tr >
							<th><fmt:message key="F0201.field.MAIL" />&nbsp;</th>
							<%--電子郵件  --%>
							<td>
								<input type="text" size="50" maxlength="128" name="mail" id="mail" />
							</td>
						</tr>
						<tr >
							<th><fmt:message key="F0201.field.NOTE" />&nbsp;</th>
							<%-- 備註說明 --%>
							<td>
								<input type="text" size="64" maxlength="64" name="note" id="note" />
							</td>
						</tr>
					</table>
				</form>
			</div>
			<!-- Button area ------------------------------------------------------------------------>
			<div class="btnContent">
				<%--送審--%>
				<input class="btnStyle" type="button" name="btn1" value="<fmt:message key="F0902.field.ACTION.S" />"  onclick="send('insert');" /> &nbsp;
				<%--回上一頁--%>
				<input class="btnStyle" type="button" name="btn1" value="<fmt:message key="common.btn.Back" />"  onclick="send('back');" />
			</div>
		</div>
		<div class="footer_line"></div>
	</div>
</body>
</html>