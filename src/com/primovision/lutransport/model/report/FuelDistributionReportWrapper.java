package com.primovision.lutransport.model.report;

import java.util.List;

import com.primovision.lutransport.model.FuelLog;

public class FuelDistributionReportWrapper
{
	
	long totalColumn=0;
	double totalGallons=0.0;
	double totalFees=0.0;
	double totaldiscounts=0.0;
	double totalAmounts=0.0;
	double totalGrossCost=0.0;
	

	
	
	List<FuelLog> fuellog=null;




	public long getTotalColumn() {
		return totalColumn;
	}




	public void setTotalColumn(long totalColumn) {
		this.totalColumn = totalColumn;
	}




	public double getTotalGallons() {
		return totalGallons;
	}




	public void setTotalGallons(double totalGallons) {
		this.totalGallons = totalGallons;
	}




	public double getTotalFees() {
		return totalFees;
	}




	public void setTotalFees(double totalFees) {
		this.totalFees = totalFees;
	}




	public double getTotaldiscounts() {
		return totaldiscounts;
	}




	public void setTotaldiscounts(double totaldiscounts) {
		this.totaldiscounts = totaldiscounts;
	}




	public double getTotalAmounts() {
		return totalAmounts;
	}




	public void setTotalAmounts(double totalAmounts) {
		this.totalAmounts = totalAmounts;
	}




	public List<FuelLog> getFuellog() {
		return fuellog;
	}




	public void setFuellog(List<FuelLog> fuellog) {
		this.fuellog = fuellog;
	}




	public double getTotalGrossCost() {
		return totalGrossCost;
	}




	public void setTotalGrossCost(double totalGrossCost) {
		this.totalGrossCost = totalGrossCost;
	}
	
	
	
	
	
	
}
