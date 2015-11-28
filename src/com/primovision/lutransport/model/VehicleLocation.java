package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="vehicle_location")
public class VehicleLocation extends AbstractBaseModel{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@ManyToOne
	@JoinColumn(name="vehicle_num")
	private Vehicle vehicleNum;
	
	
	@ManyToOne
	@JoinColumn(name="location_id")
	private Location location;
	
	@NotNull
	@Column(name="valid_from")
	private Date validFrom;
	
	@NotNull
	@Column(name="valid_to")
	private Date validTo;

	public Vehicle getVehicleNum() {
		return vehicleNum;
	}

	public void setVehicleNum(Vehicle vehicleNum) {
		this.vehicleNum = vehicleNum;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
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
	
	
	
	
	
}
