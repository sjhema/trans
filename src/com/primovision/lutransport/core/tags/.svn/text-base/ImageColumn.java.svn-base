package com.primovision.lutransport.core.tags;

import javax.servlet.jsp.JspException;

/**
 * A class to represent the image column.
 */
public final class ImageColumn extends AbstractColumnTag {
    private static final long serialVersionUID = -3220922539015030763L;

    private int imageWidth;
    private int imageHeight;
    private int imageBorder;

    private String imageSrc;
    private String linkUrl;
    private String alterText;
    private String target;

    public ImageColumn() {
	super();
	this.imageWidth = -1;
	this.imageHeight = -1;
	this.imageBorder = -1;
    }

    /*------------------------------------------------------------------------------
     * Getters 
     *----------------------------------------------------------------------------*/
    public String getImageSrc() {
	return this.imageSrc;
    }

    public String getLinkUrl() {
	return this.linkUrl;
    }

    public String getAlterText() {
	return this.alterText;
    }

    public String getTarget() {
	return this.target;
    }

    public int getImageWidth() {
	return this.imageWidth;
    }

    public int getImageHeight() {
	return this.imageHeight;
    }

    public int getImageBorder() {
	return this.imageBorder;
    }

    /*------------------------------------------------------------------------------
     * Setters 
     *----------------------------------------------------------------------------*/
    public void setImageSrc(String pstrSrc) {
	this.imageSrc = pstrSrc;
    }

    public void setLinkUrl(String pstrUrl) {
	this.linkUrl = pstrUrl;
    }

    public void setAlterText(String pstrAltText) {
	this.alterText = pstrAltText;
    }

    public void setTarget(String pstrTarget) {
	this.target = pstrTarget;
    }

    public void setImageWidth(int pintWidth) {
	this.imageWidth = pintWidth;
    }

    public void setImageHeight(int pintHeight) {
	this.imageHeight = pintHeight;
    }

    public void setImageBorder(int pintBorder) {
	this.imageBorder = pintBorder;
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
	    throw new JspException("Error: ImageColumn tag is not a child of Datatable", CCEx);
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

	// This tag does not have body contents.
	return SKIP_BODY;
    }

    private String resolveFields(String pstrUrl) throws ClassCastException {
	int intPos = 0;
	int intEnd = 0;
	String strCol = null;
	String strRet = null;
	Datatable objTmp = null;

	strRet = pstrUrl;
	objTmp = (Datatable) getParent();
	intPos = strRet.indexOf("{");
	while (intPos >= 0) {
	    intEnd = strRet.indexOf("}", intPos + 1);
	    if (intEnd != -1) {
		strCol = strRet.substring(intPos + 1, intEnd);
		strRet = strRet.substring(0, intPos) + objTmp.getColumnValue(strCol) + strRet.substring(intEnd + 1);
	    }
	    intPos = strRet.indexOf("{", intPos + 1);
	}
	return strRet;
    }

    private ImageColumn getCopy() {
	ImageColumn objRet = null;

	objRet = new ImageColumn();
	super.copyAttributesTo(objRet);
	objRet.setId(this.getId());
	objRet.setImageBorder(this.imageBorder);
	objRet.setImageHeight(this.imageHeight);
	objRet.setImageSrc(this.imageSrc);
	objRet.setImageWidth(this.imageWidth);
	objRet.setLinkUrl(this.linkUrl);
	objRet.setPageContext(this.pageContext);
	objRet.setParent(this.getParent());
	objRet.setTarget(this.target);
	return objRet;
    }

    @Override
    protected String renderColumnDetail(Object value) {
	// TODO Auto-generated method stub
	StringBuffer objBuf = new StringBuffer();
	if (linkUrl != null) {
	    objBuf.append("<a href=\"");
	    objBuf.append(resolveFields(this.linkUrl));
	    objBuf.append("\">");
	}
	objBuf.append("<img src=\"");
	objBuf.append(this.imageSrc);
	objBuf.append("\"");
	if (this.imageWidth != -1)
	    objBuf.append(" width=" + this.imageWidth);
	if (this.imageHeight != -1)
	    objBuf.append(" height=" + this.imageHeight);
	if (this.imageBorder != -1)
	    objBuf.append(" border=" + this.imageBorder);
	objBuf.append(" alt=\"" + this.alterText + "\"");
	if (this.target != null)
	    objBuf.append(" target=\"" + this.target + "\"");
	objBuf.append("/>");
	if (linkUrl != null) {
	    objBuf.append("</a>");
	}
	return objBuf.toString();
    }

}