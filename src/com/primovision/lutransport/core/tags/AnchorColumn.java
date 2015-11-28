package com.primovision.lutransport.core.tags;

import javax.servlet.jsp.JspException;

import com.primovision.lutransport.model.User;
import com.primovision.lutransport.service.AuthenticationService;
import com.primovision.lutransport.web.SpringAppContext;


public final class AnchorColumn extends AbstractColumnTag
{
	private static final long serialVersionUID = -3201594724708690415L;

	private String linkText;
    private String linkUrl;
    private String target;
    private String dataFormat;

    public AnchorColumn()
    {
        super();
    }

/*------------------------------------------------------------------------------
 * Getters 
 *----------------------------------------------------------------------------*/
    public String getLinkText()
    {
        return this.linkText;
    }

    public String getLinkUrl()
    {
        return this.linkUrl;
    }

    public String getTarget()
    {
        return this.target;
    }

    public String getDataFormat()
    {
        return this.dataFormat;
    }

/*------------------------------------------------------------------------------
 * Setters 
 *----------------------------------------------------------------------------*/
    public void setLinkText(String pstrLinkText)
    {
        this.linkText = pstrLinkText;
    }

    public void setLinkUrl(String pstrLinkUrl)
    {
        this.linkUrl = pstrLinkUrl;
    }

    public void setTarget(String pstrTarget)
    {
        this.target = pstrTarget;
    }

    public void setDataFormat(String pstrDataFormat)
    {
        this.dataFormat = pstrDataFormat;
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
		User user = (User)pageContext.getSession().getAttribute("userInfo");
		AuthenticationService authenticationService = (AuthenticationService)SpringAppContext.getBean("authenticationService");
		String ctx = (String)pageContext.getAttribute("ctx");
        try
        {
        	String url = linkUrl.substring(ctx.length()+1,linkUrl.length());
			if (authenticationService.hasUserPermission(user, url)) {
	            objTmp = (Datatable) getParent();
	            objTmp.addColumn(getCopy());
			}
        }
        catch (ClassCastException CCEx)
        {
            throw new JspException("Error: ImageColumn tag is not a child of Datatable", CCEx);
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
        if (objTmp != null) objTmp = null;
        return strRet;
    }

    private AnchorColumn getCopy()
    {
        AnchorColumn objRet = null;

        objRet = new AnchorColumn();
        super.copyAttributesTo(objRet);
        objRet.setDataFormat(this.dataFormat);
        objRet.setId(this.getId());
        objRet.setLinkText(this.linkText);
        objRet.setLinkUrl(this.linkUrl);
        objRet.setPageContext(this.pageContext);
        objRet.setParent(this.getParent());
        objRet.setTarget(this.target);
        return objRet;
    }

	@Override
	protected String renderColumnDetail(Object value) {
		StringBuffer objBuf = new StringBuffer();
		String newLinkUrl = resolveFields(this.linkUrl);
		String locale=(String)pageContext.getSession().getAttribute("lang");
		if (newLinkUrl.startsWith("javascript")) {
			objBuf.append("<a href=\"#\" onclick=\"");
		}
		else {
			objBuf.append("<a href=\"");
		}
		if (newLinkUrl.startsWith("/")) {
			objBuf.append(this.pageContext.getServletContext().getContextPath());
		}
        objBuf.append(newLinkUrl);
        objBuf.append("\"");
        if (this.target != null)
            	objBuf.append(" target=\"" + this.target + "\"");
        objBuf.append(">");
        if (this.linkText != null)
            objBuf.append(CacheUtil.getText("messageResourceCache","label_"+this.linkText+"_"+locale));
        else
            objBuf.append(formatField(value, this.dataFormat));

        objBuf.append("</a>");
        
        return objBuf.toString();
	}
}
