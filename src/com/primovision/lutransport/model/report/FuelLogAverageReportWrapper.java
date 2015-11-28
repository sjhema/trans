package com.primovision.lutransport.model.report;

import java.util.List;

import com.primovision.lutransport.model.FuelLog;

public class FuelLogAverageReportWrapper {

	
	String company="";
	String truck="";
	double netAmount=0.0;
	double totalGallon=0.0;
	double averageAmount=0.0;
	
	
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getTruck() {
		return truck;
	}
	public void setTruck(String truck) {
		this.truck = truck;
	}
	public double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}
	public double getTotalGallon() {
		return totalGallon;
	}
	public void setTotalGallon(double totalGallon) {
		this.totalGallon = totalGallon;
	}
	public double getAverageAmount() {
		return averageAmount;
	}
	public void setAverageAmount(double averageAmount) {
		this.averageAmount = averageAmount;
	}
	
	
}
