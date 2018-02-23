package com.primovision.lutransport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="wm_location")
public class WMLocation extends AbstractBaseModel {
	@Column(name="wm_location_name")
	private String wmLocationName;
	
	@ManyToOne
	@JoinColumn(name = "location")
	private Location location;

	public String getWmLocationName() {
		return wmLocationName;
	}

	public void setWmLocationName(String wmLocationName) {
		this.wmLocationName = wmLocationName;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
