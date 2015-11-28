package com.primovision.lutransport.model.admin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.primovision.lutransport.core.cache.Cacheable;
import com.primovision.lutransport.model.AbstractBaseModel;

@Entity
@Table(name = "message_resource")
@SuppressWarnings("serial")
public class MessageResource extends AbstractBaseModel implements Cacheable {

    @Column(name = "code", length = 500)
    private String code;

    @Column(name = "text", length = 5000)
    private String text;

    @Column(name = "language", nullable = true, length = 10)
    private String language;

    @Column(name = "resource_type", nullable = true, length = 10)
    private String resourceType;

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public String getLanguage() {
	return language;
    }

    public void setLanguage(String language) {
	this.language = language;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    @Override
    @Transient
    public String getKey() {
	return resourceType + "_" + code + "_" + language;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.primovision.erp.core.cache.Cacheable#getValue()
     */
    @Override
    public String getValue() {
	return text;
    }

    public String getResourceType() {
	return resourceType;
    }

    public void setResourceType(String resourceType) {
	this.resourceType = resourceType;
    }
}
