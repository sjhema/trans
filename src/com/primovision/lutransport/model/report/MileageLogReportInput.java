package com.primovision.lutransport.model.report;

public class MileageLogReportInput {
	public static final String REPORT_TYPE_TOTALS = "TOTALS";
	public static final String REPORT_TYPE_DETAILS = "DETAILS";
	public static final String REPORT_TYPE_IFTA = "IFTA";
	public static final String REPORT_TYPE_SERVICE_TRUCK_MPG = "SERVICE_TRUCK_MPG";
	
	private String periodFrom;
	private String periodTo;
	private String state;
	private String company;
	private String subcontractor;
	private String unit;
	private String firstInStateFrom;
	private String firstInStateTo;
	private String lastInStateFrom;
	private String lastInStateTo;
	
	private String drillDownState;
	private String drillDownCompany;
	private String drillDownUnit;
	
	private String gps;
	
	private String reportType;
	
	private boolean serviceTruck = false;
	
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
	public String getDrillDownState() {
		return drillDownState;
	}
	public void setDrillDownState(String drillDownState) {
		this.drillDownState = drillDownState;
	}
	public String getDrillDownCompany() {
		return drillDownCompany;
	}
	public void setDrillDownCompany(String drillDownCompany) {
		this.drillDownCompany = drillDownCompany;
	}
	public String getDrillDownUnit() {
		return drillDownUnit;
	}
	public void setDrillDownUnit(String drillDownUnit) {
		this.drillDownUnit = drillDownUnit;
	}
	public String getSubcontractor() {
		return subcontractor;
	}
	public void setSubcontractor(String subcontractor) {
		this.subcontractor = subcontractor;
	}
	public boolean isServiceTruck() {
		return serviceTruck;
	}
	public void setServiceTruck(boolean serviceTruck) {
		this.serviceTruck = serviceTruck;
	}
	public String getGps() {
		return gps;
	}
	public void setGps(String gps) {
		this.gps = gps;
	}
}
