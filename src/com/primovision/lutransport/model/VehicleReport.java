package com.primovision.lutransport.model;

import com.primovision.lutransport.model.ReportModel;

public class VehicleReport implements ReportModel {
	private String vehicle;
	
	private String company;
	
	private String feature;
	
	private String activeStatus;
	
	private String year;
	private String make;
	private String model;
	private String vin;
	
	private String inactiveDate;

	public String getVehicle() {
		return vehicle;
	}

	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}
	
	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getInactiveDate() {
		return inactiveDate;
	}

	public void setInactiveDate(String inactiveDate) {
		this.inactiveDate = inactiveDate;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}
}
