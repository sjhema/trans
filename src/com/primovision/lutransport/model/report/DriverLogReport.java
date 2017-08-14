package com.primovision.lutransport.model.report;

import java.util.Date;

public class DriverLogReport {
	private String vendor;
	
	private String company;
	private String terminal;
	
	private String tollTagNumber;
	
	private String unit;
	private String trailer;
	
	private String transferDate;
	private Date transactionDate;
	private String transactionDateStr;
	
	private String transferTimeIn;
	private String transferTimeOut;
	private String landfillTimeIn;
	private String transactionTime;
	
	private String agency;
	
	private String driver;
	
	private String customerCity;
	private String state;
	
	private Double driverPayAmount;
	private Double grossCost;
	private Double fees;
	private Double discount;
	private Double totalAmount;
	
	String origin;
	String destination;
	
	String originTicket;
	String destinationTicket;
	
	String loadDateStr;
	String unloadDateStr;
	Date unloadDate;
	
	private Double tonsGallons;
	
	private Double rateUnitPrice;
	
	private Double driverPayRate;

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
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

	public String getTollTagNumber() {
		return tollTagNumber;
	}

	public void setTollTagNumber(String tollTagNumber) {
		this.tollTagNumber = tollTagNumber;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getTrailer() {
		return trailer;
	}

	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}

	public String getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionDateStr() {
		return transactionDateStr;
	}

	public void setTransactionDateStr(String transactionDateStr) {
		this.transactionDateStr = transactionDateStr;
	}

	public String getTransferTimeIn() {
		return transferTimeIn;
	}

	public void setTransferTimeIn(String transferTimeIn) {
		this.transferTimeIn = transferTimeIn;
	}

	public String getTransferTimeOut() {
		return transferTimeOut;
	}

	public void setTransferTimeOut(String transferTimeOut) {
		this.transferTimeOut = transferTimeOut;
	}

	public String getLandfillTimeIn() {
		return landfillTimeIn;
	}

	public void setLandfillTimeIn(String landfillTimeIn) {
		this.landfillTimeIn = landfillTimeIn;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getCustomerCity() {
		return customerCity;
	}

	public void setCustomerCity(String customerCity) {
		this.customerCity = customerCity;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Double getDriverPayAmount() {
		return driverPayAmount;
	}

	public void setDriverPayAmount(Double driverPayAmount) {
		this.driverPayAmount = driverPayAmount;
	}

	public Double getGrossCost() {
		return grossCost;
	}

	public void setGrossCost(Double grossCost) {
		this.grossCost = grossCost;
	}

	public Double getFees() {
		return fees;
	}

	public void setFees(Double fees) {
		this.fees = fees;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
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

	public String getOriginTicket() {
		return originTicket;
	}

	public void setOriginTicket(String originTicket) {
		this.originTicket = originTicket;
	}

	public String getDestinationTicket() {
		return destinationTicket;
	}

	public void setDestinationTicket(String destinationTicket) {
		this.destinationTicket = destinationTicket;
	}

	public String getLoadDateStr() {
		return loadDateStr;
	}

	public void setLoadDateStr(String loadDateStr) {
		this.loadDateStr = loadDateStr;
	}

	public String getUnloadDateStr() {
		return unloadDateStr;
	}

	public void setUnloadDateStr(String unloadDateStr) {
		this.unloadDateStr = unloadDateStr;
	}

	public Date getUnloadDate() {
		return unloadDate;
	}

	public void setUnloadDate(Date unloadDate) {
		this.unloadDate = unloadDate;
	}

	public Double getTonsGallons() {
		return tonsGallons;
	}

	public void setTonsGallons(Double tonsGallons) {
		this.tonsGallons = tonsGallons;
	}

	public Double getRateUnitPrice() {
		return rateUnitPrice;
	}

	public void setRateUnitPrice(Double rateUnitPrice) {
		this.rateUnitPrice = rateUnitPrice;
	}

	public Double getDriverPayRate() {
		return driverPayRate;
	}

	public void setDriverPayRate(Double driverPayRate) {
		this.driverPayRate = driverPayRate;
	}
}
