package com.primovision.lutransport.service;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.dao.admin.UserDAO;
import com.primovision.lutransport.model.AuditLog;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.User;

public class AuditServiceImpl implements AuditService {
    private static Logger log = Logger.getLogger(AuditServiceImpl.class.getName());

    @Autowired
    private GenericDAO genericDAO;

    public void setGenericDAO(GenericDAO genericDAO) {
	this.genericDAO = genericDAO;
    }

    @Autowired
    private UserDAO userDAO;

    public void setUserDAO(UserDAO userDAO) {
	this.userDAO = userDAO;
    }

    public List<AuditLog> findAuditLogs(SearchCriteria searchCriteria) {
	try {
	    String fromDateStr = (String) searchCriteria.getSearchMap().get("fromDate");
	    String toDateStr = (String) searchCriteria.getSearchMap().get("toDate");
	    String username = (String) searchCriteria.getSearchMap().get("username");
	    StringBuffer query = new StringBuffer("");
	    StringBuffer countQuery = new StringBuffer("");
	    fromDateStr = ReportDateUtil.getFromDate(fromDateStr);
	    toDateStr = ReportDateUtil.getToDate(toDateStr);
	    query.append("select obj from AuditLog obj where obj.createdAt >= to_date('" + fromDateStr
		    + "','dd-MM-yyyy') and obj.createdAt <= to_date('" + toDateStr + "','dd-MM-yyyy')");
	    countQuery.append("select count(obj) from AuditLog obj where obj.createdAt >=to_date('" + fromDateStr
		    + "','dd-MM-yyyy') and obj.createdAt <= to_date('" + toDateStr + "','dd-MM-yyyy')");
	    if (username != null) {
		User user = userDAO.getUserByName(username);
		if (user != null) {
		    query.append(" and obj.userId='" + user.getId() + "'");
		    countQuery.append(" and obj.userId='" + user.getId() + "'");
		} else {
		    query.append(" and obj.userId='-1'");
		    countQuery.append(" and obj.userId='-1'");
		}
	    }
	    query.append(" order by obj.createdAt desc");
	    return genericDAO.searchByQuery(AuditLog.class, searchCriteria, query.toString(), countQuery.toString());
	} catch (Exception ex) {
	    ex.printStackTrace();
	    log.warn("Error while find AuditLogs :" + ex);
	    return null;
	}
    }

    @Transactional(readOnly = false)
    public void writeActivityLog(String context, String activityType, String details) {
	log.debug("Audit started for activity:" + details);
	AuditLog auditLog = new AuditLog();
	auditLog.setCreatedAt(Calendar.getInstance().getTime());
	auditLog.setEntityClass(activityType);
	auditLog.setReference(context);
	auditLog.setMessage(details);
	if (SecurityContextHolder.getContext().getAuthentication() != null)
	    auditLog.setUserId(SecurityContextHolder.getContext().getAuthentication().getName());
	genericDAO.saveOrUpdate(auditLog);
	log.debug("Audit complete for activity:" + details);
    }
}
