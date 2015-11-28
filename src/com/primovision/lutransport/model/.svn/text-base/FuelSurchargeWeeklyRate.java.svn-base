package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="fuel_surcharge_weeklyrate")
public class FuelSurchargeWeeklyRate extends AbstractBaseModel {
	
	@ManyToOne
	@JoinColumn(name="transfer_station")
	private Location transferStations;
	
	@ManyToOne
	@JoinColumn(name="landfill_station")
	private Location landfillStations;
	
	@Column(name="rate_type")
	private Integer rateType;
	
	@NotNull
	@Column(name="fuel_surcharge_rate")
	private Double fuelSurchargeRate;
	
	@NotNull
	@Column(name = "from_date")
	protected Date fromDate;
	
	@NotNull
	@Column(name = "to_date")
	protected Date toDate;
	
	@Transient
    private String ratestatus;
	
	@Transient
	private Date validFromTemp;
	
	@Transient
	private Date validToTemp;
	
	


public String getRatestatus() {
	return ratestatus;
}

public void setRatestatus(String ratestatus) {
	this.ratestatus = ratestatus;
}
	
	public Location getTransferStations() {
		return transferStations;
	}

	public void setTransferStations(Location transferStations) {
		this.transferStations = transferStations;
	}

	public Location getLandfillStations() {
		return landfillStations;
	}

	public void setLandfillStations(Location landfillStations) {
		this.landfillStations = landfillStations;
	}

	public Integer getRateType() {
		return rateType;
	}

	public void setRateType(Integer rateType) {
		this.rateType = rateType;
	}

	public Double getFuelSurchargeRate() {
		return fuelSurchargeRate;
	}

	public void setFuelSurchargeRate(Double fuelSurchargeRate) {
		this.fuelSurchargeRate = fuelSurchargeRate;
	}

	public Date getFromDate() {
		setValidFromTemp(fromDate);
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		setValidToTemp(toDate);
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
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
