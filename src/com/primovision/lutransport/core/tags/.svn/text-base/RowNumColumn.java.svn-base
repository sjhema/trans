package com.primovision.lutransport.core.tags;

import javax.servlet.jsp.JspException;

/**
 * This class is responsible for row number display.
 * 
 * @author Prasad P. Khandekar
 * @version 1.0
 * @since 1.0
 */
public final class RowNumColumn extends AbstractColumnTag {
    private static final long serialVersionUID = 4269818630991011588L;

    public RowNumColumn() {
	super();
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

    private RowNumColumn getCopy() {
	RowNumColumn objRet = null;

	objRet = new RowNumColumn();
	super.copyAttributesTo(objRet);
	objRet.setId(this.getId());
	objRet.setPageContext(this.pageContext);
	objRet.setParent(this.getParent());
	return objRet;
    }

    @Override
    protected String renderColumnDetail(Object value) {
	return value.toString();
    }
}
