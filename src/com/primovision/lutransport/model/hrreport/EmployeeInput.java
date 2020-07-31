package com.primovision.lutransport.model.hrreport;

import java.util.List;

import javax.persistence.Column;

import com.primovision.lutransport.model.hr.TimeSheet;;

public class EmployeeInput {
	
	private String staffId;
	private String OldStaffId;
	private String driver;
	private String company;
	private String terminal;
	private String category;
	
	private String dateHiredfrom;
	private String dateHiredto;
	
	private String dateReHiredfrom;
	private String dateReHiredto;
	
	private String dateTerminatedfrom;
	private String dateTerminatedto;
	
	private String ssn;
	
	private String status;
	
	
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public String getOldStaffId() {
		return OldStaffId;
	}
	public void setOldStaffId(String oldStaffId) {
		OldStaffId = oldStaffId;
	}
	public String getDateHiredfrom() {
		return dateHiredfrom;
	}
	public void setDateHiredfrom(String dateHiredfrom) {
		this.dateHiredfrom = dateHiredfrom;
	}
	public String getDateHiredto() {
		return dateHiredto;
	}
	public void setDateHiredto(String dateHiredto) {
		this.dateHiredto = dateHiredto;
	}
	public String getDateReHiredfrom() {
		return dateReHiredfrom;
	}
	public void setDateReHiredfrom(String dateReHiredfrom) {
		this.dateReHiredfrom = dateReHiredfrom;
	}
	public String getDateReHiredto() {
		return dateReHiredto;
	}
	public void setDateReHiredto(String dateReHiredto) {
		this.dateReHiredto = dateReHiredto;
	}
	public String getDateTerminatedfrom() {
		return dateTerminatedfrom;
	}
	public void setDateTerminatedfrom(String dateTerminatedfrom) {
		this.dateTerminatedfrom = dateTerminatedfrom;
	}
	public String getDateTerminatedto() {
		return dateTerminatedto;
	}
	public void setDateTerminatedto(String dateTerminatedto) {
		this.dateTerminatedto = dateTerminatedto;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	
	
	
}
