package com.primovision.lutransport.core.tags;

import javax.servlet.jsp.JspException;

/**
 * This class is responsible for date time value columns.
 * @author Prasad P. Khandekar
 * @version 1.0
 * @since 1.0
 */
public final class DateColumn extends AbstractColumnTag
{
	private static final long serialVersionUID = 3193015881427040863L;

	public DateColumn()
	{
		super();
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

	private DateColumn getCopy()
	{
		DateColumn objRet = null;

		objRet = new DateColumn();
		super.copyAttributesTo(objRet);
		objRet.setId(this.getId());
		objRet.setPageContext(this.pageContext);
		objRet.setParent(this.getParent());
		return objRet;
	}
	@Override
	protected String renderColumnDetail(Object value) {
		return formatField(value, dataFormat);
	}
}
