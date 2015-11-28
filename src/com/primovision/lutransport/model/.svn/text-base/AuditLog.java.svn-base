package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "audit_log")
public class AuditLog implements BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private static final long serialVersionUID = 269168041517643087L;

    @Column(name = "entity_id", nullable = false, updatable = false)
    private Long entityId;

    @Column(name = "entity_class", nullable = false, updatable = false)
    private String entityClass;

    @Column(name = "reference")
    private String reference;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Lob
    @Column(name = "message", nullable = false, updatable = false)
    private String message;

    @Column(name = "user_id", updatable = false)
    private String userId;

    @Transient
    private transient Object object;

    @Transient
    private transient User user;

    public AuditLog(String message, Long entityId, String entityClass, String userId) {
	this.message = message;
	this.entityId = entityId;
	this.entityClass = entityClass;
	this.userId = userId;
	this.setCreatedAt(new Date());
    }

    public AuditLog() {
	super();
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
	return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public final void setEntityId(Long entityId) {
	this.entityId = entityId;
    }

    public String getEntityClass() {
	return entityClass;
    }

    public void setEntityClass(String entityClass) {
	this.entityClass = entityClass;
    }

    /**
     * @return the message
     */
    public String getMessage() {
	return message;
    }

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    /**
     * @return the object
     */
    public Object getObject() {
	return object;
    }

    /**
     * @param object
     *            the object to set
     */
    public void setObject(Object object) {
	this.object = object;
    }

    public String getReference() {
	return reference;
    }

    public void setReference(String reference) {
	this.reference = reference;
    }

    public Date getCreatedAt() {
	return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
	this.createdAt = createdAt;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ssms.models.BaseModel#getCreatedBy()
     */
    @Override
    public Long getCreatedBy() {
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ssms.models.BaseModel#getId()
     */
    @Override
    public Long getId() {
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ssms.models.BaseModel#getModifiedAt()
     */
    @Override
    public Date getModifiedAt() {
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ssms.models.BaseModel#getModifiedBy()
     */
    @Override
    public Long getModifiedBy() {
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ssms.models.BaseModel#getStatus()
     */
    @Override
    public Integer getStatus() {
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ssms.models.BaseModel#setCreatedBy(java.lang.Long)
     */
    @Override
    public void setCreatedBy(Long createdBy) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ssms.models.BaseModel#setId(java.lang.Long)
     */
    @Override
    public void setId(Long id) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ssms.models.BaseModel#setModifiedAt(java.util.Date)
     */
    @Override
    public void setModifiedAt(Date modifiedAt) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ssms.models.BaseModel#setModifiedBy(java.lang.Long)
     */
    @Override
    public void setModifiedBy(Long modifiedBy) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ssms.models.BaseModel#setStatus(java.lang.Integer)
     */
    @Override
    public void setStatus(Integer status) {

    }
}