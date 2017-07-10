package com.primovision.lutransport.model.report;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.primovision.lutransport.model.MileageLog;

public class MileageLogReportWrapper {
	long totalRows = 0;
	double totalMiles = 0.0;
	
	String periodFrom = StringUtils.EMPTY;
	String periodTo = StringUtils.EMPTY;
	
	String firstInStateFrom = StringUtils.EMPTY;
	String firstInStateTo = StringUtils.EMPTY;
	
	String lastInStateFrom = StringUtils.EMPTY;
	String lastInStateTo = StringUtils.EMPTY;
	
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
}
