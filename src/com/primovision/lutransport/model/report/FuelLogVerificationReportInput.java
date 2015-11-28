package com.primovision.lutransport.model.report;

public class FuelLogVerificationReportInput {

	private String company;
	private String terminal;
	private String driver;
	private String transactionDateFrom;
	private String transactionDateTo;
	
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
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
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
	
	
	
}
