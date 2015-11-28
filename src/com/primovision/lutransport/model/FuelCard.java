package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name="fuelcard")
public class FuelCard extends AbstractBaseModel{
	
	@ManyToOne
	@JoinColumn(name="fuel_vendor")
	protected FuelVendor fuelvendor;
	
	@NotEmpty
	@Column(name="fuelcardnumber")
	private String fuelcardNum;
	
	
	@NotNull
	@Column(name="valid_from")
	private Date validFrom;
	
	@NotNull
	@Column(name="valid_to")
	private Date validTo;
	
	
	
	
	public FuelVendor getFuelvendor() {
		return fuelvendor;
	}

	public void setFuelvendor(FuelVendor fuelvendor) {
		this.fuelvendor = fuelvendor;
	}
	
	public void setFuelcardNum(String fuelcardNum) {
		this.fuelcardNum = fuelcardNum;
	}	
	
	public String getFuelcardNum() {
		return fuelcardNum;
	}
	
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}
	
	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}
	
	public Date getValidFrom() {
		return validFrom;
	}
	
	public Date getValidTo() {
		return validTo;
	}
	
	
}
