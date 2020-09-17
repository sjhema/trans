package com.primovision.lutransport.model.hrreport;

import java.util.List;

import javax.persistence.Column;

import com.primovision.lutransport.model.hr.EmployeeBonus;
import com.primovision.lutransport.model.hr.TimeSheet;;

public class EmployeeBonusInput {
	private String driver;
	private String category;
	private String company;
	private String terminal;
	private String bonustype;
	private String employeeNo;
	
	private String ssn;
	
	String batchFrom;
	String batchTo;
	
	List<EmployeeBonus> employeeBonus=null;

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getBonustype() {
		return bonustype;
	}

	public void setBonustype(String bonustype) {
		this.bonustype = bonustype;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public List<EmployeeBonus> getEmployeeBonus() {
		return employeeBonus;
	}

	public void setEmployeeBonus(List<EmployeeBonus> employeeBonus) {
		this.employeeBonus = employeeBonus;
	}

	public String getBatchFrom() {
		return batchFrom;
	}

	public void setBatchFrom(String batchFrom) {
		this.batchFrom = batchFrom;
	}

	public String getBatchTo() {
		return batchTo;
	}

	public void setBatchTo(String batchTo) {
		this.batchTo = batchTo;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
}
