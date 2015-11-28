/**
 * 
 */
package com.primovision.lutransport.core.util;

import javax.servlet.http.HttpSession;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.primovision.lutransport.web.SpringAppContext;

/**
 * @author gaurav
 */
public class HibernateTemplateUtil {

    /*
     * Utility method to return HibernateTemplate based on passed theme/tenant name
     */
    public static HibernateTemplate getHibernateTemplate() {
	String tenant = null;
	if (RequestContextHolder.getRequestAttributes() != null) {
	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpSession session = attr.getRequest().getSession();
	    tenant = (String) session.getAttribute("theme");
	}
	String hibernateTemplate = "";
	if (tenant != null && tenant.equalsIgnoreCase("company1"))
	    hibernateTemplate = "company1Template";
	else if (tenant != null && tenant.equalsIgnoreCase("company2"))
	    hibernateTemplate = "company2Template";
	else
	    hibernateTemplate = "company1Template";
	return (HibernateTemplate) SpringAppContext.getBean(hibernateTemplate);

    }
}
