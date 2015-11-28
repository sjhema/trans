package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="tonnage_premium")
public class TonnagePremium extends AbstractBaseModel {
	
	@NotEmpty
	@Column(name="tcode")
	private String code;
	
	@NotNull
	@Column(name="premium_tonne")
	private Double premiumTonne;
	
	@NotNull
	private Double rate;
	
	@NotNull
	@Column(name="valid_from")
	private Date validFrom;
	
	@NotNull
	@Column(name="valid_to")
	private Date validTo;

	
	@Transient
	private Date validFromTemp;

	@Transient
	private Date validToTemp;
	
	
	
	
	
	public String getCode() {
		return code;
	}
	

	public void setCode(String code) {
		this.code = code;
	}

	public Double getPremiumTonne() {
		return premiumTonne;
	}

	public void setPremiumTonne(Double premiumTonne) {
		this.premiumTonne = premiumTonne;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Date getValidFrom() {
		setValidFromTemp(validFrom);
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		setValidToTemp(validTo);
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}
	
	
	public Date getValidFromTemp() {		
		return validFromTemp;
	}
	
	public void setValidFromTemp(Date validFromTemp) {
		this.validFromTemp = validFromTemp;
	}
	
	public Date getValidToTemp() {		
		return validToTemp;
	}
	
	public void setValidToTemp(Date validToTemp) {
		this.validToTemp = validToTemp;
	}
}
