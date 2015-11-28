package com.primovision.lutransport.model.report;

import java.util.Date;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.DriverFuelCard;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Vehicle;

public class FuelLogVerificationReportData {


	protected String transactionDate;	
	
	protected Double driverGallons;
	
	protected Double uploadedGallons;
	
	
	protected String driverFuelCard;
	
	
	protected String driver;
	
	protected String driverCompany;
	
	protected String terminal;
	
	protected String truck;
	
	
	

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Double getDriverGallons() {
		return driverGallons;
	}

	public void setDriverGallons(Double driverGallons) {
		this.driverGallons = driverGallons;
	}

	public Double getUploadedGallons() {
		return uploadedGallons;
	}

	public void setUploadedGallons(Double uploadedGallons) {
		this.uploadedGallons = uploadedGallons;
	}

	public String getDriverFuelCard() {
		return driverFuelCard;
	}

	public void setDriverFuelCard(String driverFuelCard) {
		this.driverFuelCard = driverFuelCard;
	}

	public String getDriver() {
		return driver;
	}
	
	 public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDriverCompany() {
		return driverCompany;
	}

	public void setDriverCompany(String driverCompany) {
		this.driverCompany = driverCompany;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getTruck() {
		return truck;
	}

	public void setTruck(String truck) {
		this.truck = truck;
	}

	
	
}
