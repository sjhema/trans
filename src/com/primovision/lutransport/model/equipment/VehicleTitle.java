package com.primovision.lutransport.model.equipment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Vehicle;

@Entity
@Table(name="vehicle_title")
public class VehicleTitle extends AbstractBaseModel {
	@ManyToOne
	@JoinColumn(name="vehicle")
	private Vehicle vehicle;
	
	@ManyToOne
	@JoinColumn(name="title_owner")
	private Location titleOwner;
	
	@ManyToOne
	@JoinColumn(name="registered_owner")
	private Location registeredOwner;
	
	@Column(name="title")
	private String title;
	
	@Column(name="holds_title")
	private String holdsTitle;
	
	@Column(name="description")
	private String description;

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Location getTitleOwner() {
		return titleOwner;
	}

	public void setTitleOwner(Location titleOwner) {
		this.titleOwner = titleOwner;
	}

	public Location getRegisteredOwner() {
		return registeredOwner;
	}

	public void setRegisteredOwner(Location registeredOwner) {
		this.registeredOwner = registeredOwner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHoldsTitle() {
		return holdsTitle;
	}

	public void setHoldsTitle(String holdsTitle) {
		this.holdsTitle = holdsTitle;
	}
}
