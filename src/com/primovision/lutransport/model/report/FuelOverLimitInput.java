package com.primovision.lutransport.model.report;

import java.util.List;

import com.primovision.lutransport.model.FuelLog;

public class FuelOverLimitInput {


	
	private String company;
	private String terminal;	
	private String transactionDateFrom;
	private String transactionDateTo;
	private String driver;	
	private String maxGallons;
	List<FuelLog> fuellogs=null;	
	
	
	public List<FuelLog> getFuellogs() {
		return fuellogs;		
	}
	public void setFuellogs(List<FuelLog> fuellogs) {
		this.fuellogs = fuellogs;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	
	public String getTransactionDateFrom() {
		return transactionDateFrom;
	}
	public void setTransactionDateFrom(String transactionDateFrom) {
		this.transactionDateFrom = transactionDateFrom;
	}
	public String getTransactionDateTo() {
		return transactionDateTo;
	}
	public void setTransactionDateTo(String transactionDateTo) {
		this.transactionDateTo = transactionDateTo;
	}	
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}	
	
	
	public String getMaxGallons() {
		return maxGallons;
	}
	
	public void setMaxGallons(String maxGallons) {
		this.maxGallons = maxGallons;
	}

}
