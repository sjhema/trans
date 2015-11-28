package com.primovision.lutransport.core.tags;

import javax.servlet.jsp.JspException;


public final class InputColumn extends AbstractColumnTag
{
	private static final long serialVersionUID = -3201594724708690415L;

	private String type;
	private String fieldName;
	private String value;

	public InputColumn()
	{
		super();
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}



	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}



	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}



	/*------------------------------------------------------------------------------
	 * Overridden methods
	 * @see javax.servlet.jsp.tagext.Tag
	 *----------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException
	{
		Datatable objTmp = null;

		try
		{
			objTmp = (Datatable) getParent();
			objTmp.addColumn(getCopy());
		}
		catch (ClassCastException CCEx)
		{
			throw new JspException("Error: ImageColumn tag is not a child of Datatable", CCEx);
		}
		finally
		{
			if (objTmp != null) {
				objTmp = null;
			}
		}
		return EVAL_PAGE;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException
	{
		if (!(this.getParent() instanceof Datatable)) {
			throw new JspException("Error: Column tag needs to be a child of Datatable!");
		}
		// This tag does not have body contents.
		return SKIP_BODY;
	}

	private String resolveFields(String pstrUrl) throws ClassCastException
	{
		int    intPos = 0;
		int    intEnd = 0;
		String strCol = null;
		String strRet = null;
		Datatable objTmp = null;

		strRet = pstrUrl;
		objTmp = (Datatable) getParent();
		intPos = strRet.indexOf("{");
		while (intPos >= 0)
		{
			intEnd = strRet.indexOf("}", intPos + 1);
			if (intEnd != -1)
			{
				strCol = strRet.substring(intPos + 1, intEnd);
				strRet = strRet.substring(0, intPos) +
				objTmp.getColumnValue(strCol) +
				strRet.substring(intEnd + 1);
			}
			intPos = strRet.indexOf("{", intPos +1);
			}
			if (objTmp != null) {
				objTmp = null;
			}
			return strRet;
	}

	private InputColumn getCopy()
	{
		InputColumn objRet = null;

		objRet = new InputColumn();
		super.copyAttributesTo(objRet);
		objRet.setDataFormat(this.dataFormat);
		objRet.setId(this.getId());
		objRet.setType(this.type);
		objRet.setFieldName(this.fieldName);
		objRet.setPageContext(this.pageContext);
		objRet.setParent(this.getParent());
		objRet.setValue(this.value);
		return objRet;
	}

	@Override
	protected String renderColumnDetail(Object value) {
		StringBuffer objBuf = new StringBuffer();
		String newValue = resolveFields(this.value);
		objBuf.append("<input type=\""+this.type+"\" name=\""+this.fieldName+"\" value=\""+newValue+"\"/>");
		return objBuf.toString();
	}
}
