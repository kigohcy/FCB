/** 分頁產生器
 * @(#) QueryPageTag.java
 *
 * Copyright (c) 2016 HiTRUST Incorporated.
 * All rights reserved.
 * 
 * Modify History:
 *	v1.00, 2016/01/19, Eason Hsu
 * 	 1) JIRA-Number, First release
 * 
 */

package com.hitrust.acl.common.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import com.hitrust.acl.util.StringUtil;
import com.hitrust.bank.model.LoginUser;
import com.hitrust.framework.APSystem;
import com.hitrust.framework.model.APLogin;
import com.hitrust.framework.model.page.Page;
import com.hitrust.framework.model.page.PageHelper;

public class QueryPageTag extends BodyTagSupport {

	private static final long serialVersionUID = 2347804388798942163L;
	
	private static Logger LOG = Logger.getLogger(QueryPageTag.class);
	
	private String formName = "formPage";
	private String action = "";  // submit Url
	private String btnName = ""; // 按鍵名稱
	
	private String pages = this.getI18n("common.page.pages"); // 頁數
	private String prev = this.getI18n("common.page.Prev");   // 上一頁
	private String next	= this.getI18n("common.page.next");   // 下一頁

	// =============== Default CSS Style ===============
	private final String DIV_CLASS = "pageContent"; 
	private final String SPAN_CLASS = "pageSpace";
	private final String BTN_CLASS = "choiceStyle";
	
	// =============== CSS Style ===============
	private String divClass = "";
	private String spanClass = "";
	private String btnClass = "";
	
	@Override
	public int doStartTag() throws JspException {
				
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		PageHelper helper = new PageHelper(request);
		Page result = helper.getQueryResult();
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<form method=\"post\" id=\"").append(formName + "\"").append(" name=\"").append(formName + "\" ");
		if (!StringUtil.isBlank(this.getAction())) {
			sb.append("action=\"").append(this.getAction()).append("\" ");
		}
		sb.append(">");
		
		sb.append("<input type=\"hidden\" name=\"page.pageNo\" />");
		
		// 提供前端畫面判斷使用
		sb.append("<input type=\"hidden\" id=\"nowPage\" value='" + result.getPageNo() + "' />");
		sb.append("<input type=\"hidden\" id=\"totalPage\" value=\"").append(result.getTotalPage()).append("\" />");
		
		sb.append("<div class='");
		if (!StringUtil.isBlank(this.getDivClass())) {
			sb.append(this.getDivClass());
		} else {
			sb.append(DIV_CLASS);
		}
		sb.append("' >");
		
		sb.append("<span class='");
		if (!StringUtil.isBlank(this.getSpanClass())) {
			sb.append(this.getSpanClass());
		} else {
			sb.append(SPAN_CLASS);
		}
		
		sb.append("' >");
		sb.append(pages + "&nbsp;");
		sb.append("<input type='text' size='2' maxlength='5' name='input_page' value='").append(result.getPageNo() + "' />");
		sb.append("&nbsp;/&nbsp;").append(result.getTotalPage());
		sb.append("</span>");
		
		sb.append("<span class='");
		if (!StringUtil.isBlank(this.getSpanClass())) {
			sb.append(this.getSpanClass());
		}
		else {
			sb.append(SPAN_CLASS);
		}
		sb.append("' >");
		sb.append("<input type='button' name='menu1' class='");
		
		if (!StringUtil.isBlank(this.getBtnClass())) {
			sb.append(this.getBtnClass());
		} else {
			sb.append(BTN_CLASS);
		}
		sb.append("' value='").append(this.getBtnName());
		sb.append("' onClick='gotoPage(").append(formName + ".input_page.value").append(");");
		sb.append( "' />");
		sb.append("</span>");
		
		sb.append("<span>");
		// 頁數為 1 則不提供超鏈結
		if (result.hasPrevious()) {
			sb.append("<a href='#' ");
			sb.append("onClick='gotoPage(\"").append(((result.getPageNo() - 1) < 1 ? 1 :(result.getPageNo() -1))).append("\");' >");
			sb.append(prev);
			sb.append("</a>");
		} else {
			sb.append(prev);
		}
		sb.append("&nbsp;|&nbsp; ");
		// 頁數為 1 則不提供超鏈結
		if (result.hasNext()) {
			sb.append("<a href='#' ");
			sb.append("onClick='gotoPage(\"").append(result.getPageNo() +1).append("\");' >");
			sb.append(next);
			sb.append("</a>");
		} else {
			sb.append(next);
		}
		sb.append("</span>");
		
		sb.append("</div>");
		sb.append("</form>");
		
		try {
			JspWriter writer = this.pageContext.getOut();
			writer.println(sb.toString());
			
		} catch (IOException e) {
			LOG.error("[QueryPageTag]: ", e);
		}
		
		return SKIP_BODY;
	}

	// =============== Getter & Setter ===============
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getBtnName() {
		return btnName;
	}
	public void setBtnName(String btnName) {
		this.btnName = btnName;
	}
	public String getDivClass() {
		return divClass;
	}
	public void setDivClass(String divClass) {
		this.divClass = divClass;
	}
	public String getSpanClass() {
		return spanClass;
	}
	public void setSpanClass(String spanClass) {
		this.spanClass = spanClass;
	}
	public String getBtnClass() {
		return btnClass;
	}
	public void setBtnClass(String btnClass) {
		this.btnClass = btnClass;
	}
	
	/**
	 * 依據 i18n Key 值 取得對應 i18n Message
	 * @param key
	 * @return
	 */
	private String getI18n(String key) {
		String i18n = "";
		
		try {
			// 取得目前 LoginUser 物件 
			LoginUser user = (LoginUser) APLogin.getCurrentUser();
			
			// 取得 i18n
			MessageSource source = (MessageSource) APSystem.getApplicationContext().getBean("messageSource");
			
			i18n = source.getMessage(key, null, user.getLocale());
			
		} catch (NoSuchMessageException e) {
			LOG.info("[getI18n NoSuchMessageException]: ", e);
			
		} catch (Exception e) {
			LOG.info("[getI18n Exception]: ", e);
		}
		
		return i18n;
	}
	
}
