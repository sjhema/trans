package com.primovision.lutransport.model.report;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.primovision.lutransport.model.EzToll;
import com.primovision.lutransport.model.FuelLog;

public class DriverLogReportWrapper {
	String transactionDateFrom = StringUtils.EMPTY;
	String transactionDateTo = StringUtils.EMPTY;
	
	List<DriverLogReport> driverLogs;

	public String getTransactionDateFrom() {
		return transactionDateFrom;
	}
	public void setTransactionDateFrom(String transactionDateFrom) {
		this.transactionDateFrom = transactionDateFrom;
	}
	public String getTransactionDateTo() {
		return transactionDateTo;
	}
	public void setTransactionDateTo(String transactionDateTo) {
		this.transactionDateTo = transactionDateTo;
	}
	public List<DriverLogReport> getDriverLogs() {
		return driverLogs;
	}
	public void setDriverLogs(List<DriverLogReport> driverLogs) {
		this.driverLogs = driverLogs;
	}
}
