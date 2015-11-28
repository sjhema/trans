package com.primovision.lutransport.model.hrreport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.primovision.lutransport.model.Location;

/**
 * @author kishor
 *
 */
public class DriverPayWrapper {
	
	int totalRowCount=0;
	double sumTotal=0.0;
	String driver="";
	String batchDateFrom="";
	String batchDateTo="";
	String company="";
	String payRollBatch="";
	double sumAmount=0.0;
	String miscNotes="";
	double miscAmount=0.0;
	Map<String,Double> sumAmountsMap = new HashMap<String,Double>();
	Map<String,Integer> totolcounts = new HashMap<String,Integer>();
	List<String> driverNames = new ArrayList<String>();
	Map<Long,Double> driverPayRateDataMap = new HashMap<Long,Double>();
	
	private Location terminal;
	private Location companylocation;
	
	List<DriverPay> driverPays=null;
	
	List<String> list = new ArrayList<String>();

	
	
	public Map<String, Integer> getTotolcounts() {
		return totolcounts;
	}
	
	public void setTotolcounts(Map<String, Integer> totolcounts) {
		this.totolcounts = totolcounts;
	}
	
	
	public void setSumAmountsMap(Map<String, Double> sumAmountsMap) {
		this.sumAmountsMap = sumAmountsMap;
	}
	
	public Map<String, Double> getSumAmountsMap() {
		return sumAmountsMap;
	}
	
	public void setMiscAmount(double miscAmount) {
		this.miscAmount = miscAmount;
	}
	
	public void setMiscNotes(String miscNotes) {
		this.miscNotes = miscNotes;
	}
	public String getMiscNotes() {
		return miscNotes;
	}
	
	public double getMiscAmount() {
		return miscAmount;
	}
	
	
	public List<String> getDriverNames() {
		return driverNames;
	}
	
	public void setDriverNames(List<String> driverNames) {
		this.driverNames = driverNames;
	}
	

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getBatchDateFrom() {
		return batchDateFrom;
	}

	public void setBatchDateFrom(String batchDateFrom) {
		this.batchDateFrom = batchDateFrom;
	}

	public String getBatchDateTo() {
		return batchDateTo;
	}

	public void setBatchDateTo(String batchDateTo) {
		this.batchDateTo = batchDateTo;
	}

	public int getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

	public double getSumTotal() {
		return sumTotal;
	}

	public void setSumTotal(double sumTotal) {
		this.sumTotal = sumTotal;
	}

	public List<DriverPay> getDriverPays() {
		return driverPays;
	}

	public void setDriverPays(List<DriverPay> driverPays) {
		this.driverPays = driverPays;
	}
	
	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public String getPayRollBatch() {
		return payRollBatch;
	}

	public void setPayRollBatch(String payRollBatch) {
		this.payRollBatch = payRollBatch;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Location getTerminal() {
		return terminal;
	}

	public void setTerminal(Location terminal) {
		this.terminal = terminal;
	}

	public Location getCompanylocation() {
		return companylocation;
	}

	public void setCompanylocation(Location companylocation) {
		this.companylocation = companylocation;
	}

	public double getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(double sumAmount) {
		this.sumAmount = sumAmount;
	}

	public Map<Long, Double> getDriverPayRateDataMap() {
		return driverPayRateDataMap;
	}

	public void setDriverPayRateDataMap(Map<Long, Double> driverPayRateDataMap) {
		this.driverPayRateDataMap = driverPayRateDataMap;
	}

}
