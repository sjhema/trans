package com.primovision.lutransport.model.hrreport;

public class DriverPayHistoryInput {
	
	private String drivers;
	
	private String companies;
	
	private String terminals;
	
	private String batchDateFrom;
	
	private String batchDateTo;
	
	private String payrollBatchDate;
	
	private String payrollstatus;
	
	

	public String getPayrollstatus() {
		return payrollstatus;
	}

	public void setPayrollstatus(String payrollstatus) {
		this.payrollstatus = payrollstatus;
	}

	public String getPayrollBatchDate() {
		return payrollBatchDate;
	}

	public void setPayrollBatchDate(String payrollBatchDate) {
		this.payrollBatchDate = payrollBatchDate;
	}

	public String getDrivers() {
		return drivers;
	}

	public void setDrivers(String drivers) {
		this.drivers = drivers;
	}

	public String getCompanies() {
		return companies;
	}

	public void setCompanies(String companies) {
		this.companies = companies;
	}

	public String getTerminals() {
		return terminals;
	}

	public void setTerminals(String terminals) {
		this.terminals = terminals;
	}

	public String getBatchDateFrom() {
		return batchDateFrom;
	}

	public void setBatchDateFrom(String batchDateFrom) {
		this.batchDateFrom = batchDateFrom;
	}

	public String getBatchDateTo() {
		return batchDateTo;
	}

	public void setBatchDateTo(String batchDateTo) {
		this.batchDateTo = batchDateTo;
	}
	
	

}
