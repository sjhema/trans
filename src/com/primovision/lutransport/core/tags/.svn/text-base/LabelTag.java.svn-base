package com.primovision.lutransport.core.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class LabelTag extends BodyTagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}


	@Override
	public int doEndTag() throws JspException {
		String locale=(String)pageContext.getSession().getAttribute("lang");
		if (locale==null) {
			locale="en_US";
		}
		try {
			pageContext.getOut().write(CacheUtil.getText("messageResourceCache","label_"+code+"_"+locale));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}

}
