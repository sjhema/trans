package com.primovision.lutransport.core.tags;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

/**
 * The base class for all HTML elements.
 */
public abstract class AbstractColumnTag extends TagSupport implements IColumnTag {
    private static final long serialVersionUID = 4361100806161442614L;

    private static Logger log = Logger.getLogger(AbstractColumnTag.class);

    protected String width;

    protected String height;

    protected int border;

    protected String bodyContent;

    protected String bgColor;

    protected String foreColor;

    protected String cssClass;

    protected String hAlign;

    protected String vAlign;

    protected String headerText;

    protected String dataField;

    protected String dataFormat;
    
    protected String type;

    protected boolean sortable;

    public AbstractColumnTag() {
	super();
	this.border = -1;
    }

    /**
     * @return the width
     */
    public String getWidth() {
	return width;
    }

    /**
     * @param width
     *            the width to set
     */
    public void setWidth(String width) {
	this.width = width;
    }

    /**
     * @return the height
     */
    public String getHeight() {
	return height;
    }

    /**
     * @param height
     *            the height to set
     */
    public void setHeight(String height) {
	this.height = height;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#getBorder()
     */
    public int getBorder() {
	return this.border;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#getBgColor()
     */
    public String getBgColor() {
	return this.bgColor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#getForeColor()
     */
    public String getForeColor() {
	return this.foreColor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#getCssClass()
     */
    public String getCssClass() {
	return this.cssClass;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#getHAlign()
     */
    public String getHAlign() {
	return this.hAlign;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#getVAlign()
     */
    public String getVAlign() {
	return this.vAlign;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#getHeaderText()
     */
    public String getHeaderText() {
	return this.headerText;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#getDataField()
     */
    public String getDataField() {
	return this.dataField;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#getSortable()
     */
    public boolean getSortable() {
	return this.sortable;
    }

    @Override
    public String getDataFormat() {
	return dataFormat;
    }

    @Override
    public void setDataFormat(String dataFormat) {
	this.dataFormat = dataFormat;
    }

    /*------------------------------------------------------------------------------
     * Setters
     *----------------------------------------------------------------------------*/

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#setBorder(int)
     */
    public void setBorder(int pintBorder) {
	this.border = pintBorder;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#setBgColor(java.lang.String)
     */
    public void setBgColor(String pstrColor) {
	this.bgColor = pstrColor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#setForeColor(java.lang.String)
     */
    public void setForeColor(String pstrColor) {
	this.foreColor = pstrColor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#setCssClass(java.lang.String)
     */
    public void setCssClass(String pstrCssClass) {
	this.cssClass = pstrCssClass;
    }

    /**
     * @return the bodyContent
     */
    public String getBodyContent() {
	return bodyContent;
    }

    /**
     * @param bodyContent
     *            the bodyContent to set
     */
    public void setBodyContent(String bodyContent) {
	this.bodyContent = bodyContent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#setHAlign(java.lang.String)
     */
    public void setHAlign(String pstrHAlign) {
	this.hAlign = pstrHAlign;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#setVAlign(java.lang.String)
     */
    public void setVAlign(String pstrVAlign) {
	this.vAlign = pstrVAlign;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#setHeaderText(java.lang.String)
     */
    public void setHeaderText(String pstrHdrText) {
	this.headerText = pstrHdrText;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#setDataField(java.lang.String)
     */
    public void setDataField(String pstrDataField) {
	this.dataField = pstrDataField;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#setSortable(boolean)
     */
    public void setSortable(boolean pblnSortable) {
	this.sortable = pblnSortable;
    }

    /*------------------------------------------------------------------------------
     * Methods
     *----------------------------------------------------------------------------*/
    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#renderHeader()
     */
    public void renderHeader() throws JspException {
	String strTxt = null;
	StringBuffer objBuf = null;

	try {
	    if (this.headerText == null)
		strTxt = "&nbsp;";
	    else
		strTxt = this.headerText;

	    objBuf = new StringBuffer();
	    objBuf.append("<th");
	    if (this.width != null)
		objBuf.append(" width=\"" + this.width + "\"");
	    if (this.height != null)
		objBuf.append(" height=\"" + this.height + "\"");
	    if (this.cssClass != null)
		objBuf.append(" class=\"" + this.cssClass + "\"");
	    if (this.hAlign != null)
		objBuf.append(" align=\"" + this.hAlign.toLowerCase() + "\"");
	    if (this.vAlign != null)
		objBuf.append(" valign=\"" + this.vAlign.toLowerCase() + "\"");
	    if (this.bgColor != null)
		objBuf.append(" bgcolor=\"" + this.bgColor + "\"");
	    if (this.foreColor != null)
		objBuf.append(" color=\"" + this.foreColor + "\"");
	    objBuf.append(">");
	    // Setting Locale to the header
	    String locale = (String) pageContext.getSession().getAttribute("lang");
	    if (locale == null || locale.equals(""))
		locale = "en_US";
	    objBuf.append(CacheUtil.getText("messageResourceCache", "label_" + strTxt + "_" + locale));
	    // objBuf.append(strTxt);
	    objBuf.append("</th>");
	    // Write created HTML to output stream.
	    this.pageContext.getOut().print(objBuf.toString());
	} catch (IOException IOEx) {
	    throw new JspException("Error: IOException while writing to client!", IOEx);
	} catch (Exception ex) {
	    throw new JspException("Error: Exception while writing to client!", ex);
	} finally {
	    if (objBuf != null)
		objBuf = null;
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freeware.gridtag.IColumnTag#renderBlank()
     */
    public void renderDetail(Object value) throws JspException {
	StringBuffer objBuf = null;

	try {
	    objBuf = new StringBuffer();
	    objBuf.append("<td");
	    if (this.width != null)
		objBuf.append(" width=\"" + this.width + "\"");
	    if (this.height != null)
		objBuf.append(" height=\"" + this.height + "\"");
	    if (this.cssClass != null)
		objBuf.append(" class=\"" + this.cssClass + "\"");
	    if (this.hAlign != null)
		objBuf.append(" align=\"" + this.hAlign.toLowerCase() + "\"");
	    if (this.vAlign != null)
		objBuf.append(" valign=\"" + this.vAlign.toLowerCase() + "\"");
	    if (this.bgColor != null)
		objBuf.append(" bgcolor=\"" + this.bgColor + "\"");
	    if (this.foreColor != null)
		objBuf.append(" color=\"" + this.foreColor + "\"");

	    objBuf.append(">");
	    objBuf.append(renderColumnDetail(value));
	    objBuf.append("</td>");
	    // Write created HTML to output stream.
	    this.pageContext.getOut().print(objBuf.toString());
	} catch (IOException IOEx) {
	    throw new JspException("Error: IOException while writing to client!", IOEx);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    log.warn("Exception while writing to client :" + ex);
	    throw new JspException("Error: Exception while writing to client!", ex);

	} finally {
	    if (objBuf != null)
		objBuf = null;
	}
    }

    public void renderBlank() throws JspException {
	StringBuffer objBuf = null;

	try {
	    objBuf = new StringBuffer();
	    objBuf.append("<td");
	    if (this.width != null)
		objBuf.append(" width=\"" + this.width + "%\"");
	    if (this.height != null)
		objBuf.append(" height=\"" + this.height + "\"");
	    if (this.cssClass != null)
		objBuf.append(" class=\"" + this.cssClass + "\"");
	    if (this.hAlign != null)
		objBuf.append(" align=\"" + this.hAlign.toLowerCase() + "\"");
	    if (this.vAlign != null)
		objBuf.append(" valign=\"" + this.vAlign.toLowerCase() + "\"");
	    if (this.bgColor != null)
		objBuf.append(" bgcolor=\"" + this.bgColor + "\"");
	    if (this.foreColor != null)
		objBuf.append(" color=\"" + this.foreColor + "\"");

	    objBuf.append(">&nbsp;</td>");

	    // Write created HTML to output stream.
	    this.pageContext.getOut().print(objBuf.toString());
	} catch (IOException IOEx) {
	    throw new JspException("Error: IOException while writing to client!", IOEx);
	} catch (Exception ex) {
	    throw new JspException("Error: Exception while writing to client!", ex);
	} finally {
	    if (objBuf != null)
		objBuf = null;
	}
    }

    protected String formatField(Object pobjVal, String pstrFmt) throws ClassCastException {
	String strRet = null;
	Format objFmt = null;
	try {
	    if (pstrFmt == null)
		return pobjVal.toString();
	    if (pobjVal instanceof java.sql.Date || pobjVal instanceof java.util.Date || pobjVal instanceof java.sql.Timestamp) {
		objFmt = new SimpleDateFormat(pstrFmt);
		strRet = objFmt.format(pobjVal);
	    } else if (pobjVal instanceof Number) {
		objFmt = new DecimalFormat(pstrFmt);
		strRet = objFmt.format(pobjVal);
	    } else
		strRet = pobjVal.toString();
	} catch (NullPointerException NPExIgnore) {
	} catch (IllegalArgumentException IArgExIgnore) {
	} finally {
	    if (objFmt != null)
		objFmt = null;
	}
	if (strRet == null)
	    strRet = Datatable.DEFAULT_NULLTEXT;
	return strRet;
    }

    public void copyAttributesTo(IColumnTag dest) {
		dest.setBgColor(this.bgColor);
		dest.setBorder(this.border);
		dest.setCssClass(this.cssClass);
		dest.setDataField(this.dataField);
		dest.setDataFormat(this.dataFormat);
		dest.setForeColor(this.foreColor);
		dest.setHAlign(this.hAlign);
		dest.setHeaderText(this.headerText);
		dest.setHeight(this.height);
		dest.setBodyContent(this.bodyContent);
		dest.setSortable(this.sortable);
		dest.setVAlign(this.vAlign);
		dest.setWidth(this.width);
		dest.setType(this.type);
    }

    protected abstract String renderColumnDetail(Object value);

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}