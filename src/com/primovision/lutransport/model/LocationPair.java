package com.primovision.lutransport.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="location_pair")
public class LocationPair extends AbstractBaseModel {
	@ManyToOne
	@JoinColumn(name = "destination")
	private Location destination;
	
	@ManyToOne
	@JoinColumn(name = "origin")
	private Location origin;

	public Location getDestination() {
		return destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}

	public Location getOrigin() {
		return origin;
	}

	public void setOrigin(Location origin) {
		this.origin = origin;
	}
}
