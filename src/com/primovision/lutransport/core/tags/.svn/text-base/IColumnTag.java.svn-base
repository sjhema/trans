package com.primovision.lutransport.core.tags;

import javax.servlet.jsp.JspException;

/**
 * An interface defining basic HTML element attributes.
 * 
 * @author
 * @version 1.0
 * @since 1.0
 */
public interface IColumnTag {
	
	public String getType();
	
	public void setType(String type);
	
    public String getWidth();

    public String getHeight();

    public int getBorder();

    public String getBodyContent();

    public String getBgColor();

    public String getForeColor();

    public String getCssClass();

    public String getHAlign();

    public String getVAlign();

    public String getHeaderText();

    public String getDataField();

    public String getDataFormat();

    public boolean getSortable();

    public void setWidth(String width);

    public void setHeight(String height);

    public void setBorder(int border);

    public void setBodyContent(String bodyContent);

    public void setBgColor(String color);

    public void setForeColor(String color);

    public void setCssClass(String cssClass);

    public void setHAlign(String hAlign);

    public void setVAlign(String vAlign);

    public void setHeaderText(String hdrText);

    public void setDataField(String field);

    public void setDataFormat(String dataFormat);

    public void setSortable(boolean sortable);

    public void renderDetail(Object value) throws JspException;

    public void renderHeader() throws JspException;

    public void renderBlank() throws JspException;

    public void copyAttributesTo(IColumnTag pobjDest);
}