package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="loading_fee")
public class LoadingFee extends AbstractBaseModel {
	@NotEmpty
	@Column(name="code")
	private String code;
	
	@NotNull
	private Double rate;
	
	@NotNull
	@Column(name="valid_from")
	private Date validFrom;
	
	@NotNull
	@Column(name="valid_to")
	private Date validTo;

	@Column(name="notes")
	private String notes;
	
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

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Transient
	public Date getValidFromTemp() {
		return validFromTemp;
	}

	@Transient
	public void setValidFromTemp(Date validFromTemp) {
		this.validFromTemp = validFromTemp;
	}

	@Transient
	public Date getValidToTemp() {
		return validToTemp;
	}

	@Transient
	public void setValidToTemp(Date validToTemp) {
		this.validToTemp = validToTemp;
	}
}
