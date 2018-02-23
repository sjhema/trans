package com.primovision.lutransport.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Vehicle;

@Entity
@Table(name="no_gps_vehicle")
public class NoGPSVehicle extends AbstractBaseModel{
	@ManyToOne
	@JoinColumn(name="vehicle")
	private Vehicle vehicle;

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
}
