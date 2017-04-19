package com.primovision.lutransport.model.hr;

import com.primovision.lutransport.model.ReportModel;

public class MiscellaneousPayReportInput implements ReportModel {
	private String company;
	private String terminal;
	
	private String employee;
	
	String miscellaneousDesc;
	
	String batchDateFromStart;
	String batchDateFromEnd;
	String batchDateToStart;
	String batchDateToEnd;
	
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
	public String getBatchDateFromStart() {
		return batchDateFromStart;
	}
	public void setBatchDateFromStart(String batchDateFromStart) {
		this.batchDateFromStart = batchDateFromStart;
	}
	public String getBatchDateFromEnd() {
		return batchDateFromEnd;
	}
	public void setBatchDateFromEnd(String batchDateFromEnd) {
		this.batchDateFromEnd = batchDateFromEnd;
	}
	public String getBatchDateToStart() {
		return batchDateToStart;
	}
	public void setBatchDateToStart(String batchDateToStart) {
		this.batchDateToStart = batchDateToStart;
	}
	public String getBatchDateToEnd() {
		return batchDateToEnd;
	}
	public void setBatchDateToEnd(String batchDateToEnd) {
		this.batchDateToEnd = batchDateToEnd;
	}
}
