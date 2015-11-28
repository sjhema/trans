package com.primovision.lutransport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "language")
public class Language extends AbstractBaseModel {

    private static final long serialVersionUID = -668486759401102084L;

    @Column(name = "name")
    private String name;

    @Column(name = "locale")
    private String locale;

    @Column(name = "rtl")
    private Boolean rtl;

    @Column(name = "base")
    private Boolean base;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getLocale() {
	return locale;
    }

    public void setLocale(String locale) {
	this.locale = locale;
    }

    public Boolean getRtl() {
	return rtl;
    }

    public void setRtl(Boolean rtl) {
	this.rtl = rtl;
    }

    public Boolean getBase() {
	return base;
    }

    public void setBase(Boolean base) {
	this.base = base;
    }

}
