package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;

@Entity
@Table(name="fuel_discount")
public class FuelDiscount extends AbstractBaseModel {
	
	@ManyToOne
	@JoinColumn(name="fuel_vendor")
	protected FuelVendor fuelvendor;
	
	
	@NotNull
	@Column(name="fuel_discount_percentage")
	protected Double fuelDiscountPercentage;
	
	@NotNull
	@Column(name = "from_date")
	protected Date fromDate;
	
	@NotNull
	@Column(name = "to_date")
	protected Date toDate;

	

	

	public FuelVendor getFuelvendor() {
		return fuelvendor;
	}

	public void setFuelvendor(FuelVendor fuelvendor) {
		this.fuelvendor = fuelvendor;
	}

	public Double getFuelDiscountPercentage() {
		return fuelDiscountPercentage;
	}

	public void setFuelDiscountPercentage(Double fuelDiscountPercentage) {
		this.fuelDiscountPercentage = fuelDiscountPercentage;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	
		
	
}
