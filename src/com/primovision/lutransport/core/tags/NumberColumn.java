/*------------------------------------------------------------------------------
 * PACKAGE: com.freeware.gridtag
 * FILE   : NumberColumn.java
 * CREATED: Jul 26, 2004
 * AUTHOR : Prasad P. Khandekar
 *------------------------------------------------------------------------------
 * Change Log:
 *-----------------------------------------------------------------------------*/
package com.primovision.lutransport.core.tags;

import java.text.DecimalFormat;

import javax.servlet.jsp.JspException;

/**
 * This class is respnsible for rendering number columns. e.g. Integer, Long,
 * Double etc.
 * @author Prasad P. Khandekar
 * @version 1.0
 * @since 1.0
 */
public final class NumberColumn extends AbstractColumnTag
{
	private static final long serialVersionUID = -5393135421956784602L;

	private Integer maxFractionDigits;

	private Integer minFractionDigits;

	public Integer getMaxFractionDigits() {
		return maxFractionDigits;
	}

	public void setMaxFractionDigits(Integer maxFractionDigits) {
		this.maxFractionDigits = maxFractionDigits;
	}

	public Integer getMinFractionDigits() {
		return minFractionDigits;
	}

	public void setMinFractionDigits(Integer minFractionDigits) {
		this.minFractionDigits = minFractionDigits;
	}

	public NumberColumn()
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

	private NumberColumn getCopy()
	{
		NumberColumn objRet = null;
		objRet = new NumberColumn();
		super.copyAttributesTo(objRet);
		objRet.setMaxFractionDigits(this.maxFractionDigits);
		objRet.setMinFractionDigits(this.minFractionDigits);
		objRet.setId(this.getId());
		objRet.setPageContext(this.pageContext);
		objRet.setParent(this.getParent());
		return objRet;
	}

	@Override
	protected String renderColumnDetail(Object value) {
		if (value==null) {
			return "";
		}
		if (dataFormat!=null) {
			return formatField(value, dataFormat);
		}
		else {
			DecimalFormat objFmt = new DecimalFormat();
			if (maxFractionDigits !=null) {
				objFmt.setMaximumFractionDigits(maxFractionDigits);
			}
			if (minFractionDigits !=null) {
				objFmt.setMinimumFractionDigits(minFractionDigits);
			}
			return objFmt.format(value);
		}
	}
}
