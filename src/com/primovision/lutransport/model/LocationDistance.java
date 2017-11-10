package com.primovision.lutransport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;

@Entity
@Table(name = "location_distance")
public class LocationDistance extends AbstractBaseModel {
	@ManyToOne
	@JoinColumn(name="origin")
	private Location origin;
	
	@ManyToOne
	@JoinColumn(name="destination")
	private Location destination;

	@NotNull
	@Column(name="miles")
	private Double miles;

	public Location getOrigin() {
		return origin;
	}

	public void setOrigin(Location origin) {
		this.origin = origin;
	}

	public Location getDestination() {
		return destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}

	public Double getMiles() {
		return miles;
	}

	public void setMiles(Double miles) {
		this.miles = miles;
	}
}
