package com.primovision.lutransport.model.report;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.primovision.lutransport.model.MileageLog;

public class MileageLogReportWrapper {
	long totalRows = 0;
	double totalMiles = 0.0;
	
	String periodFrom = StringUtils.EMPTY;
	String periodTo = StringUtils.EMPTY;
	
	String states = StringUtils.EMPTY;
	String companies = StringUtils.EMPTY;
	
	List<MileageLog> mileageLogList;
	
	public String getCompanies() {
		return companies;
	}

	public void setCompanies(String companies) {
		this.companies = companies;
	}

	public double getTotalMiles() {
		return totalMiles;
	}

	public void setTotalMiles(double totalMiles) {
		this.totalMiles = totalMiles;
	}
	public void setMileageLogList(List<MileageLog> mileageLogList) {
		this.mileageLogList = mileageLogList;
	}

	public long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
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

	public String getStates() {
		return states;
	}

	public void setStates(String states) {
		this.states = states;
	}

	public void setPeriodTo(String periodTo) {
		this.periodTo = periodTo;
	}

	public List<MileageLog> getMileageLogList() {
		return mileageLogList;
	}
}
