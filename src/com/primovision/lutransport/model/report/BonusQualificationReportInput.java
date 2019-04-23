package com.primovision.lutransport.model.report;

import com.primovision.lutransport.model.ReportModel;

public class BonusQualificationReportInput implements ReportModel {
	private String dateFrom;
	private String dateTo;
	
	private String company;
	private String driver;
	
	public String getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}
	public String getDateTo() {
		return dateTo;
	}
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
}
