package com.primovision.lutransport.model.report;

import com.primovision.lutransport.model.AbstractBaseModel;

public class NetReportTransportationAmountDrillDownWrapper extends AbstractBaseModel{

	String driver;
	String driverCompany;
	String origin;
	String destination;
	Integer loadCount=0;
	Double payRate=0.0;
	Double transAmount=0.0;
	
	
	public Double getPayRate() {
		return payRate;
	}
	
	public void setPayRate(Double payRate) {
		this.payRate = payRate;
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
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public Integer getLoadCount() {
		return loadCount;
	}
	public void setLoadCount(Integer loadCount) {
		this.loadCount = loadCount;
	}
	public Double getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(Double transAmount) {
		this.transAmount = transAmount;
	}
	
	
	
}
