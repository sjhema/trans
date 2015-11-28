package com.primovision.lutransport.core.tags;

import javax.servlet.jsp.JspException;

/**
 * This class provides functionality to display string columns.
 * 
 * @author Prasad P. Khandekar
 * @version 1.0
 * @since 1.0
 */
public final class TextColumn extends AbstractColumnTag {
    private static final long serialVersionUID = 5928642885957457925L;

    protected int maxLength;
    protected String value;

    public TextColumn() {
	super();
	this.maxLength = -1;
    }

    public int getMaxLength() {
	return this.maxLength;
    }

    public void setMaxLength(int pintMaxLen) {
	this.maxLength = pintMaxLen;
    }

    /**
     * @return the value
     */
    public String getValue() {
	return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
	this.value = value;
    }

    /*------------------------------------------------------------------------------
     * Overridden methods
     * @see javax.servlet.jsp.tagext.Tag
     *----------------------------------------------------------------------------*/
    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
	Datatable objTmp = null;

	try {
	    objTmp = (Datatable) getParent();
	    objTmp.addColumn(getCopy());
	} catch (ClassCastException CCEx) {
	    throw new JspException("Error: Column is not a child of Datatable", CCEx);
	} finally {
	    if (objTmp != null)
		objTmp = null;
	}
	return EVAL_PAGE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException {
	if (!(this.getParent() instanceof Datatable))
	    throw new JspException("Error: Column tag needs to be a child of Datatable!");
	if (dataField == null) {
	    return EVAL_BODY_INCLUDE;
	} else {
	    return SKIP_BODY;
	}

    }

    private TextColumn getCopy() {
	TextColumn objRet = null;
	objRet = new TextColumn();
	super.copyAttributesTo(objRet);
	objRet.setDataFormat(this.dataFormat);
	objRet.setId(this.getId());
	objRet.setPageContext(this.pageContext);
	objRet.setParent(this.getParent());
	return objRet;
    }

    @Override
    protected String renderColumnDetail(Object value) {
	StringBuffer objBuf = new StringBuffer();
	if (this.value != null) {
	    objBuf.append(this.value);
	    return objBuf.toString();
	} else {
	    if (value instanceof byte[]) {
		value = new String((byte[]) value);
	    }
	    String strVal = formatField(value, this.dataFormat);
	    if (strVal != null && this.maxLength > 0)
		if (strVal.length() > this.maxLength)
		    strVal = strVal.substring(0, this.maxLength) + "...";
	    objBuf.append(strVal);
	    return objBuf.toString();
	}
    }
}
