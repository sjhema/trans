package com.primovision.lutransport.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Null;

@Entity
@Table(name="driver_fuelcard")
public class DriverFuelCard extends AbstractBaseModel{
	
	
	@ManyToOne
	@JoinColumn(name="fuel_vendor")
	protected FuelVendor fuelvendor;

	
	@ManyToOne
	@JoinColumn(name="fuelcard")
	protected FuelCard fuelcard; 	
	
	
	@ManyToOne
	@JoinColumn(name="driver")
	protected Driver driver;
	
	
	@ManyToOne
	@JoinColumn(name="vehicle")
	private Vehicle vehicle;
	
	
	public void setFuelcard(FuelCard fuelcard) {
		this.fuelcard = fuelcard;
	}		
	
	
	public FuelCard getFuelcard() {
		return fuelcard;
	}
	
	
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	
	public Driver getDriver() {
		return driver;
	}

	public FuelVendor getFuelvendor() {
		return fuelvendor;
	}

	public void setFuelvendor(FuelVendor fuelvendor) {
		this.fuelvendor = fuelvendor;
	}


	public Vehicle getVehicle() {
		return vehicle;
	}


	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
}
