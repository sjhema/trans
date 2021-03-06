package com.primovision.lutransport.model.report;

public class IFTAReportInput {
	public static final String REPORT_TYPE_IFTA = "IFTA";
	public static final String REPORT_TYPE_MPG = "MPG";
	
	public static final String REPORT_FOR_GPS = "Y";
	public static final String REPORT_FOR_NO_GPS = "N";
	
	private String periodFrom;
	private String periodTo;
	private String state;
	private String company;
	private String unit;
	
	private String firstInStateFrom;
	private String firstInStateTo;
	private String lastInStateFrom;
	private String lastInStateTo;
	
	private String gps;
	
	private String reportType;
	
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPeriodFrom() {
		return periodFrom;
	}
	public void setPeriodFrom(String periodFrom) {
		this.periodFrom = periodFrom;
	}
	public String getPeriodTo() {
		return periodTo;
	}
	public void setPeriodTo(String periodTo) {
		this.periodTo = periodTo;
	}
	public String getFirstInStateFrom() {
		return firstInStateFrom;
	}
	public void setFirstInStateFrom(String firstInStateFrom) {
		this.firstInStateFrom = firstInStateFrom;
	}
	public String getFirstInStateTo() {
		return firstInStateTo;
	}
	public void setFirstInStateTo(String firstInStateTo) {
		this.firstInStateTo = firstInStateTo;
	}
	public String getLastInStateFrom() {
		return lastInStateFrom;
	}
	public void setLastInStateFrom(String lastInStateFrom) {
		this.lastInStateFrom = lastInStateFrom;
	}
	public String getLastInStateTo() {
		return lastInStateTo;
	}
	public void setLastInStateTo(String lastInStateTo) {
		this.lastInStateTo = lastInStateTo;
	}
	public String getGps() {
		return gps;
	}
	public void setGps(String gps) {
		this.gps = gps;
	}
}
