package com.primovision.lutransport.core.tags;

import javax.servlet.jsp.JspException;

/**
 * This class provides functionality to display string columns.
 * @author Prasad P. Khandekar
 * @version 1.0
 * @since 1.0
 */
public final class StaticDataColumn extends AbstractColumnTag
{
	private static final long serialVersionUID = 5928642885957457925L;

	protected int    maxLength;
	protected String dataType;

	public StaticDataColumn()
	{
		super();
		this.maxLength = -1;
	}

	public int getMaxLength()
	{
		return this.maxLength;
	}

	public void setMaxLength(int pintMaxLen)
	{
		this.maxLength = pintMaxLen;
	}

	/**
	 * @return the dataType
	 */
	 public String getDataType() {
		 return dataType;
	 }

	 /**
	  * @param dataType the dataType to set
	  */
	 public void setDataType(String dataType) {
		 this.dataType = dataType;
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
			 throw new JspException("Error: Column is not a child of Datatable", CCEx);
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
		 if (dataField==null) {
			 return EVAL_BODY_INCLUDE;
		 }
		 else {
			 return SKIP_BODY;
		 }

	 }

	 private StaticDataColumn getCopy()
	 {
		 StaticDataColumn objRet = null;
		 objRet = new StaticDataColumn();
		 super.copyAttributesTo(objRet);
		 objRet.setDataType(dataType);
		 objRet.setDataFormat(this.dataFormat);
		 objRet.setId(this.getId());
		 objRet.setPageContext(this.pageContext);
		 objRet.setParent(this.getParent());
		 return objRet;
	 }

	 @Override
	 protected String renderColumnDetail(Object value) {
		 StringBuffer objBuf = new StringBuffer();
		 if (this.dataType !=null) {
			 objBuf.append(StaticDataUtil.getText(dataType, value.toString()));
		 }
		 else {
			 objBuf.append(value);
		 }
		 return objBuf.toString();
	 }
}
