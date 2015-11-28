package com.primovision.lutransport.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Rakesh Interface for auditable entities
 */
@SuppressWarnings("unchecked")
public interface Auditable extends Serializable {
    /**
     * Retrieve the primary key
     * 
     * @return
     */
    public Long getId();

    /**
     * Returns the list of fields that should be audited.
     * 
     * @return
     */
    public List getAuditableFields();

    /**
     * Returns the field of the primary identifier (e.g. title). This field is used to uniquely identify the record.
     * 
     * @return
     */
    public String getPrimaryField();

    /**
     * Returns customized audit log message. When empty, audit logging uses standard audit message.
     * 
     * @return
     */
    public String getAuditMessage();

    public boolean skipAudit();

}
