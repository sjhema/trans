package com.primovision.lutransport.model.report;

import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.State;

public class IFTAReport {
	private Location company;
	private State state;
	private Double miles;
	private Double gallons = 0.0;
	
	public Location getCompany() {
		return company;
	}
	public void setCompany(Location company) {
		this.company = company;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public Double getMiles() {
		return miles;
	}
	public void setMiles(Double miles) {
		this.miles = miles;
	}
	public Double getGallons() {
		return gallons;
	}
	public void setGallons(Double gallons) {
		this.gallons = gallons;
	}
}
