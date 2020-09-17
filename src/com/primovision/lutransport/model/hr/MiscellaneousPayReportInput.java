package com.primovision.lutransport.model.hr;

import com.primovision.lutransport.model.ReportModel;

public class MiscellaneousPayReportInput implements ReportModel {
	private String company;
	private String terminal;
	
	private String employee;
	
	String miscellaneousDesc;
	
	String batchDateFrom;
	String batchDateTo;
	
	String ssn;
	
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
	public String getEmployee() {
		return employee;
	}
	public void setEmployee(String employee) {
		this.employee = employee;
	}
	public String getMiscellaneousDesc() {
		return miscellaneousDesc;
	}
	public void setMiscellaneousDesc(String miscellaneousDesc) {
		this.miscellaneousDesc = miscellaneousDesc;
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
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
}
