package com.primovision.lutransport.core.tags;

import javax.servlet.jsp.JspException;

/**
 * This class provides functionality similar to Oracle's DECODE function. i.e.
 * it allows to display different value based on a value of column.
 * @author Prasad P. Khandekar
 * @version 1.0
 * @since 1.0
 */
public final class DecodeColumn extends AbstractColumnTag
{
	private static final long serialVersionUID = -6868221442424930015L;

	private String mstrDecodeValues;
	private String mstrDisplayValues;
	private String mstrValueSeperator;

	public DecodeColumn()
	{
		super();
	}

	/*------------------------------------------------------------------------------
	 * Getters
	 *----------------------------------------------------------------------------*/
	public String getDecodeValues()
	{
		return this.mstrDecodeValues;
	}

	public String getDisplayValues()
	{
		return this.mstrDisplayValues;
	}

	public String getValueSeperator()
	{
		return this.mstrValueSeperator;
	}

	/*------------------------------------------------------------------------------
	 * Setters
	 *----------------------------------------------------------------------------*/
	public void setDecodeValues(String mstrValues)
	{
		this.mstrDecodeValues = mstrValues;
	}

	public void setDisplayValues(String mstrValues)
	{
		this.mstrDisplayValues = mstrValues;
	}

	public void setValueSeperator(String pstrSeperator)
	{
		this.mstrValueSeperator = pstrSeperator;
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

		if (!checkValues()) {
			throw new JspException("Error: For every decode value a display value must be specified!");
		}

		// This tag does not have body contents.
		return SKIP_BODY;
	}

	private String formatField(Object pobjVal) throws ClassCastException
	{
		int    intCnt = 0;
		String strRet = null;

		String[] arrDecode  = null;
		String[] arrDisplay = null;

		arrDecode = this.mstrDecodeValues.split(this.mstrValueSeperator);
		arrDisplay = this.mstrDisplayValues.split(this.mstrValueSeperator);
		for (intCnt = 0; intCnt < arrDecode.length; intCnt++)
		{
			if (pobjVal.equals(arrDecode[intCnt]))
			{
				strRet = arrDisplay[intCnt];
				break;
			}
			else if (pobjVal.toString().equals(arrDecode[intCnt]))
			{
				strRet = arrDisplay[intCnt];
				break;
			}
		}

		if (arrDecode != null) {
			arrDecode = null;
		}
		if (arrDisplay != null) {
			arrDisplay = null;
		}

		if (strRet == null) {
			strRet = Datatable.DEFAULT_NULLTEXT;
		}
		return strRet;
	}

	private DecodeColumn getCopy()
	{
		DecodeColumn objRet = null;

		objRet = new DecodeColumn();
		super.copyAttributesTo(objRet);
		objRet.setDecodeValues(this.mstrDecodeValues);
		objRet.setDisplayValues(this.mstrDisplayValues);
		objRet.setId(this.getId());
		objRet.setPageContext(this.pageContext);
		objRet.setParent(this.getParent());
		objRet.setValueSeperator(this.mstrValueSeperator);
		return objRet;
	}

	private boolean checkValues()
	{
		boolean  blnRet     = false;
		String[] arrDecode  = null;
		String[] arrDisplay = null;

		arrDecode = this.mstrDecodeValues.split(this.mstrValueSeperator);
		arrDisplay = this.mstrDisplayValues.split(this.mstrValueSeperator);

		if (arrDecode.length == arrDisplay.length) {
			blnRet = true;
		} else if (arrDecode.length < arrDisplay.length) {
			blnRet = true;
		}

		arrDecode = null;
		arrDisplay = null;
		return blnRet;
	}

	@Override
	protected String renderColumnDetail(Object value) {

		return formatField(value);
	}
}
