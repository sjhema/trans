package com.primovision.lutransport.service;

import java.util.List;

import com.primovision.lutransport.model.AuditLog;
import com.primovision.lutransport.model.SearchCriteria;

public interface AuditService {
    public List<AuditLog> findAuditLogs(SearchCriteria searchCriteria);

    public void writeActivityLog(String context, String activityType, String details);
}
