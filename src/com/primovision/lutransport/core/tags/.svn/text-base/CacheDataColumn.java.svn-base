package com.primovision.lutransport.core.tags;

import javax.servlet.jsp.JspException;

/**
 * This class provides functionality to display string columns.
 * @author Prasad P. Khandekar
 * @version 1.0
 * @since 1.0
 */
public final class CacheDataColumn extends AbstractColumnTag
{
	private static final long serialVersionUID = 5928642885957457925L;

	protected int    maxLength;
	protected String cacheName;

    public CacheDataColumn()
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

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	/*------------------------------------------------------------------------------
 * Overridden methods
 * @see javax.servlet.jsp.tagext.Tag
 *----------------------------------------------------------------------------*/
    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
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
            if (objTmp != null) objTmp = null;
        }
        return EVAL_PAGE;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException
    {
        if (!(this.getParent() instanceof Datatable))
            throw new JspException("Error: Column tag needs to be a child of Datatable!");
        if (dataField==null) {
        	return EVAL_BODY_INCLUDE;
        }
        else {
        	return SKIP_BODY;
        }
        	
    }


    

    private CacheDataColumn getCopy()
    {
        CacheDataColumn objRet = null;
        objRet = new CacheDataColumn();
        super.copyAttributesTo(objRet);
        objRet.setCacheName(cacheName);
        objRet.setDataFormat(this.dataFormat);
        objRet.setId(this.getId());
        objRet.setPageContext(this.pageContext);
        objRet.setParent(this.getParent());
        return objRet;
    }

	@Override
	protected String renderColumnDetail(Object value) {
		StringBuffer objBuf = new StringBuffer();
		if (this.cacheName !=null && value!=null) {
			objBuf.append(CacheUtil.getText(cacheName, value.toString()));
		}
		else {
			objBuf.append(value);
		}
		return objBuf.toString();
	}
}
