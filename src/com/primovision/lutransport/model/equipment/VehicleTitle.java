package com.primovision.lutransport.model.equipment;

import java.util.Date;

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
	@JoinColumn(name="owner")
	private Location owner;
	
	@Column(name="title")
	private String title;
	
	@Column(name="title_date")
	private Date titleDate;
	
	@Column(name="holds_title")
	private String holdsTitle;

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

	public Location getOwner() {
		return owner;
	}

	public void setOwner(Location owner) {
		this.owner = owner;
	}

	public Date getTitleDate() {
		return titleDate;
	}

	public void setTitleDate(Date titleDate) {
		this.titleDate = titleDate;
	}

	public String getHoldsTitle() {
		return holdsTitle;
	}

	public void setHoldsTitle(String holdsTitle) {
		this.holdsTitle = holdsTitle;
	}
}
