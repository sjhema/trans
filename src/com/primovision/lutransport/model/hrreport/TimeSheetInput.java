package com.primovision.lutransport.model.hrreport;

import java.util.List;

import com.primovision.lutransport.model.hr.Attendance;

public class TimeSheetInput /*implements ReportModel*/{
	
	private String employeesNo;
	private String driver;
	private String company;
	private String terminal;
	private String category;
	private String ssn;

	private String weekstartDateFrom;
	private String weekendDateTo;
	
	
	
	List<Attendance> attendance=null;


	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}



	public String getEmployeesNo() {
		return employeesNo;
	}



	public void setEmployeesNo(String employeesNo) {
		this.employeesNo = employeesNo;
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



	public String getWeekstartDateFrom() {
		return weekstartDateFrom;
	}



	public void setWeekstartDateFrom(String weekstartDateFrom) {
		this.weekstartDateFrom = weekstartDateFrom;
	}



	


	public String getWeekendDateTo() {
		return weekendDateTo;
	}



	public void setWeekendDateTo(String weekendDateTo) {
		this.weekendDateTo = weekendDateTo;
	}



	public List<Attendance> getAttendance() {
		return attendance;
	}



	public void setAttendance(List<Attendance> attendance) {
		this.attendance = attendance;
	}
	
	
	
	
	
	

}
