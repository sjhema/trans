package com.primovision.lutransport.model.report;

import java.util.List;

import com.primovision.lutransport.model.FuelLog;

public class FuelOverLimitReportWrapper {


	
	double totalGallons=0.0;
	
	List<FuelLog> fuellog=null;

	public double getTotalGallons() {
		return totalGallons;
	}

	public void setTotalGallons(double totalGallons) {
		this.totalGallons = totalGallons;
	}

	public List<FuelLog> getFuellog() {
		return fuellog;
	}

	public void setFuellog(List<FuelLog> fuellog) {
		this.fuellog = fuellog;
	}
	
	
	

}
