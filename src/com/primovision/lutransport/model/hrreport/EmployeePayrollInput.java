package com.primovision.lutransport.model.hrreport;

import java.util.List;

import javax.persistence.Column;

import com.primovision.lutransport.model.hr.TimeSheet;;

public class EmployeePayrollInput {
	
	
	private String driver;
	private String company;
	private String terminal;
	private String category;
	private String batchDatefrom;
	private String batchDateto;
	private String hourlypayrollstatus;
	
	List<TimeSheet> timesheets=null;
	
	
	
	public List<TimeSheet> getTimesheets() {
		return timesheets;
	}
	public void setTimesheets(List<TimeSheet> timesheets) {
		this.timesheets = timesheets;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getBatchDatefrom() {
		return batchDatefrom;
	}
	public void setBatchDatefrom(String batchDatefrom) {
		this.batchDatefrom = batchDatefrom;
	}
	public String getBatchDateto() {
		return batchDateto;
	}
	public void setBatchDateto(String batchDateto) {
		this.batchDateto = batchDateto;
	}
	public String getHourlypayrollstatus() {
		return hourlypayrollstatus;
	}
	public void setHourlypayrollstatus(String hourlypayrollstatus) {
		this.hourlypayrollstatus = hourlypayrollstatus;
	}
	
	
}
