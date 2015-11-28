package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="fuel_surcharge_padd")
public class FuelSurchargePadd extends AbstractBaseModel {
	
	@NotEmpty
	@Column(name="tcode")
	private String code;
	
	@NotNull
	private Double amount;
	
	@NotNull
	@Column(name="valid_from")
	private Date validFrom;
	
	@NotNull
	@Column(name="valid_to")
	private Date validTo;
	
	@Transient
    private String ratestatus;


public String getRatestatus() {
	return ratestatus;
}


@Transient
private Date validFromTemp;

@Transient
private Date validToTemp;



public void setRatestatus(String ratestatus) {
	this.ratestatus = ratestatus;
}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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
