<%
/*
 * @(#)aclink/00.jsp
 *
 * Copyright (c) 2016 HiTRUST Incorporated. All rights reserved.
 *
 * Description: �P�N����
 *
 * Modify History:
 * v1.00, 2016/04/07, Eason Hsu
 * 	1) First Release
 * v1.01, 2016/10/28, Eason Hsu
 *	1) TSBACL-122, ���x�h�D�B�|�����x�A�P�N���� PDF �������̾� DB �Ѽ�(PRVS_VRSN)���o
 * v1.02, 2016/12/15, Eason Hsu
 *  1) TSBACL-141, �b���s���j�w�y�{���������, �ݪ��׭��д���
 * v1.03, 2016/12/21, Jimmy Yen
 *  1) TSBACL-142, (��B) ECGW, AclCust PDF �P PDF plugin ���Y�ɮפj�p
*/
%>

<%@ page language="java" contentType="text/html; charset=BIG5" pageEncoding="BIG5"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>���w�b��P�N����</title>

<%@include file="/include/initial.jsp" %>

<script type="text/javascript">
	$(function() {
		// �վ��ʸ˸m������ܤj�p
		var md = new MobileDetect(window.navigator.userAgent);
		if ((md.mobile() != null) && ($(window).width() < 568)) {
			// location.reload();
			$("#content").css("margin", "0 5px").removeClass("content");
		} else {
			// location.reload();
			$("#content").css("margin", "0").addClass("content");
		}
		
		$("#btnAgree").click(function() {
			if ($("#chkAgree").prop("checked")) {
				<%-- v1.02, form submit �e�W�[ Mask loding --%>
				$("#msak_loding").fadeIn(function () {
					$("#_service").val("aclinkAuth");
					$("#form1").submit();
				});

			} else {
				alert('�ФĿ�"�ڤw�\Ū�æP�N�H�W�A�Ȭ��w����');
			}
		});
		
		$("#btnDisagree").click(function() {
			<%-- v1.02, form submit �e�W�[ Mask loding --%>
			$("#msak_loding").fadeIn(function () {
				$("#_service").val("cancel");
				$("#form1").submit();
			});
			
		});
		
	});

    $(document).ready(function() {
        //   Hide the border by commenting out the variable below
        var $on = 'section';
        $($on).css({
            'background': 'none',
            'border': 'none',
            'box-shadow': 'none'
        });
    });

    function sleep(sec) {
	    var time = new Date().getTime();
	    while(new Date().getTime() - time < sec * 1000);
	}
</script>

</head>
<body>
	<div class="pure-g forfirefox">
		<div id="header" class="pure-u-1">
			<img src='<%=root%>/images/popwin_panel_header.jpg' />
		</div>
	</div>
	<div class="pure-g">
		<div class="pure-u-1 pure-u-sm-3-5 pure-u-lg-5-12 pure-u-xl-1-3">
			<div id="container">
				<%-- form --%>
				<form name="form1" id="form1" method="post" action="<%=root%>/portal">
					<input type="hidden" name="_service" id="_service" />
					<input type="hidden" name="sessionKey" value="<c:out value="${param.sessionKey}" />" />
				</form>
				<%-- ========== Container ========== --%>
				
				<p class="title">���w�s���s�ڱb��I�ڱ��v���ҪA�ȱ���</p>
				<p style="font-size: 10pt;text-align: right;line-height: 20px;">���ڪ����G<c:out value="${link.prvsVrsn}"/></p>
				<div style="margin: 0 10px;">
					 <table class="noticeTable" cellspacing="0" border="0">
        			<tbody>
	            <tr>
	                <td width="20" style="font-size: 12pt;text-align: left;line-height: 20px;">        
					          �@�B 
					        </td>
					        <td style="font-size: 12pt;text-align: left;line-height: 20px;">
					        	�ӽФH�V�q�l��I���x�ӽЫ��w�s���ӽФH��Ĥ@�ӷ~�Ȧ�ѥ��������q(�H�U²�� �Q���q)���s�x���s�ڱb��(�H�U²�ٳs���b��)�i��I�ڡA�g�ӽФH�� �Q���q�u�W���ҥ��x�H�ӤH�����Ȧ�n�J��²�T�{�ҽX���Ҿ�������v���ҫ�A�ӽФH�P�N�q�l��I���x�V �Q���q���X��������ܡA�����w�g�ӽФH���v�P�N�A�ӥ�����ܵ����ӽФH��������ܡC
	        				</td>
	        		</tr>
	        		<tr>
	                <td width="20" style="font-size: 12pt;text-align: left;line-height: 20px;">        
					          �G�B 
					        </td>
	        				<td style="font-size: 12pt;text-align: left;line-height: 20px;">   
	        					�ӽФH���q�l��I���x�V �Q���q���X��������ܦ��øq�A���V�q�l��I���x�ШD�d���C�q�l��I���x���d���e�}�øq�A�ӽФH�P�N �Q���q�o���ѳs���b�������Ƥ��q�l��I���x�C
	       					</td>
	        		</tr>
	        		<tr>
	                <td width="20" style="font-size: 12pt;text-align: left;line-height: 20px;">        
					          �T�B 
					        </td>
	        				<td style="font-size: 12pt;text-align: left;line-height: 20px;">   
	        					�ӽФH�P�N�]������A�ȩҥͤ�����ɪ�(�]�A��������ӽФH���s���b��D�s�ΩΫ_�ε�)�A�����V�q�l��I���x���X�A�åѹq�l��I���x�t�d�B�z�A���o�V �Q���q�D�i�����v�Q�C
	        				</td>
	        		</tr>
	        		<tr>
	                <td width="20" style="font-size: 12pt;text-align: left;line-height: 20px;">        
					          �|�B 
					        </td>
	        				<td style="font-size: 12pt;text-align: left;line-height: 20px;">   
	        					�ӽФH�p�������s���b�ᤧ�]�w�A���V�q�l�䥭�x���X�ӽСC�L�k�V�ӥ��x�ӽЪ̡A�o�V �Q���q�Ȥ�A�Ȥ���(24�p�ɪA�ȱM�u�G(02)2181-1111)�ӽСC
	       					</td>
	        		</tr>
	        		<tr>
	                <td width="20" style="font-size: 12pt;text-align: left;line-height: 20px;">        
					          ���B 
					        </td>
	       					<td style="font-size: 12pt;text-align: left;line-height: 20px;">   
	        					�ӽФH���s���b���������B�ڶ��ƥI�A�p�](�]�t��������)�s���b��l�B�����B�D�k�|�Ψ�L��������A�q����ĵ�ܱb��Ψ�L���`���Ϊ̡A �Q���q�o�ڵ��q�l��I���x������ܡC
	       					</td>
	        		</tr>
	        		<tr>
	                <td width="20" style="font-size: 12pt;text-align: left;line-height: 20px;">        
					          ���B 
					        </td>
	        				<td style="font-size: 12pt;text-align: left;line-height: 20px;">   
	        					�ӽФH��P�@�q�l��I���x�ӽгs���b��i��I�ڡA�C�@�s���b�ᤧ����̰����B���C���s�O��(�H�U�P)5�U�F�C��10�U�F�C��20�U�C�ӽФH�󤣦P�q�l��I���x�]�w���s���b�ᬰ�P�@ �Q���q���s�x���s�ڱb��A�ӱb�����̰����B���X�֭p��C
	        				</td>
	            </tr> 
	        		<tr>
	                <td width="20" style="font-size: 12pt;text-align: left;line-height: 20px;">        
					          �C�B 
					        </td>
	        				<td style="font-size: 12pt;text-align: left;line-height: 20px;">   
	        					���A�ȱ��ڥ��ɨƩy�A�x�� �Q���q���i�W�w�B �Q���q�u�s�ڷ~�Ȭ��w�ѡv�Ҹ����w�Τ@����ĺD�ҿ�z�C
	        				</td>
	            </tr>   
	        		<tr>
	                <td width="20" style="font-size: 12pt;text-align: left;line-height: 20px;">        
					          �K�B 
					        </td>
	        				<td style="font-size: 12pt;text-align: left;line-height: 20px;">   
	        					�]���A�ȯA�^�ɡA�ӽФH�P�N�H�O�W�O�_�a��k�|���Ĥ@�f���Ҫk�|�A���k�ߥt���W�w�M�ݺ��Ҫ̡A���b�����C
	        				</td>
	            </tr>   
	        		<tr>
	                <td width="20" style="font-size: 12pt;text-align: left;line-height: 20px;">        
					          �E�B 
					        </td>
	        				<td style="font-size: 12pt;text-align: left;line-height: 20px;">   
	        					�ӽФH�w�f�\�e�}�A�ȱ��ڥ������e�A�䤤�ĤT�B���Τ����ݥ��A�ȱ��ڭ��n���e�A�ӽФH�w�R���A�ѨæP�N��u���A�ȱ��کҸ��ƶ��Τ��e�C
	        				</td>
	            </tr>                   
        		</tbody>
    		</table>
				</div>
				<div class="margin_top20" style="padding-left: 5px; text-align: center;">
					<input type="checkbox" id="chkAgree" class="css-checkbox lrg" />
					<label for="chkAgree" class="css-label lrg web-two-style">�ڤw�\Ū�æP�N�H�W�A�Ȭ��w����</label>
				</div>
				<div id="btnArea" class="text_center margin_top20">
					<a href="#" class="btn btnDisagree" id="btnDisagree"></a>
					<a href="#" class="btn btnAgree" id="btnAgree"></a>
				</div>
			</div>
		</div>
	</div>
	<div class="pure-g">
		<div id="footer" class="pure-u-1"></div>
	</div>
	<div id='msak_loding' class="msak_loding"></div>
</body>
</html>