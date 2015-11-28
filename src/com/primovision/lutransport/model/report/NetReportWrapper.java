package com.primovision.lutransport.model.report;

import java.util.List;

import javax.persistence.Transient;

import com.primovision.lutransport.model.AbstractBaseModel;

public class NetReportWrapper  extends AbstractBaseModel{
	double sumNetAmount=0.0;
	double sumNet=0.0;
	double sumBillableTon=0.0;
	double sumOriginTon=0.0;
	double sumDestinationTon=0.0;
	double sumAmount=0.0;
	double sumFuelSurcharge=0.0;
	double sumTonnage=0.0;
	double sumDemmurage=0.0;
	double sumTotal=0.0;
	int totalColumn=0;
	String driver="";
	String terminal="";
	String company="";
	String unit="";
	String unitID="";
	double amount=0.0;
	double tonnagePremium=0.0;
	double demurrageCharge=0.0;
	double fuellogCharge=0.0;
	double subcontractorCharge=0.0;
	double transportationAmount=0.0;
	double tollTagAmount=0.0;
	double netAmount=0.0;
	
	
	
	public void setTonnagePremium(double tonnagePremium) {
		this.tonnagePremium = tonnagePremium;
	}
	
	public double getTonnagePremium() {
		return tonnagePremium;
	}
	
	public void setTransportationAmount(double transportationAmount) {
		this.transportationAmount = transportationAmount;
	}
	
	public double getTransportationAmount() {
		return transportationAmount;
	}
	
	
	String fromDate;
	String toDate;
	
	public String getFromDate() {
		return fromDate;
	}
	
	public String getToDate() {
		return toDate;
	}
	
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	
	public void setUnitID(String unitID) {
		this.unitID = unitID;
	}

	public String getUnitID() {
		return unitID;
	}
	
	
	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getSubcontractorCharge() {
		return subcontractorCharge;
	}

	public void setSubcontractorCharge(double subcontractorCharge) {
		this.subcontractorCharge = subcontractorCharge;
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

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	/*@Transient
	List<Billing> billings=null;*/

	
	

	public double getSumNet() {
		return sumNet;
	}

	public double getSumNetAmount() {
		return sumNetAmount;
	}

	public void setSumNetAmount(double sumNetAmount) {
		this.sumNetAmount = sumNetAmount;
	}

	public void setSumNet(double sumNet) {
		this.sumNet = sumNet;
	}

	public double getSumBillableTon() {
		return sumBillableTon;
	}

	public void setSumBillableTon(double sumBillableTon) {
		this.sumBillableTon = sumBillableTon;
	}

	public double getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(double sumAmount) {
		this.sumAmount = sumAmount;
	}

	public double getSumFuelSurcharge() {
		return sumFuelSurcharge;
	}

	public void setSumFuelSurcharge(double sumFuelSurcharge) {
		this.sumFuelSurcharge = sumFuelSurcharge;
	}

	public double getSumTonnage() {
		return sumTonnage;
	}

	public void setSumTonnage(double sumTonnage) {
		this.sumTonnage = sumTonnage;
	}

	public double getSumTotal() {
		return sumTotal;
	}

	public void setSumTotal(double sumTotal) {
		this.sumTotal = sumTotal;
	}

	/*public List<Billing> getBillings() {
		return billings;
	}

	public void setBillings(List<Billing> billings) {
		this.billings = billings;
	}*/

	public double getSumDemmurage() {
		return sumDemmurage;
	}

	public void setSumDemmurage(double sumDemmurage) {
		this.sumDemmurage = sumDemmurage;
	}

	public double getSumOriginTon() {
		return sumOriginTon;
	}

	public void setSumOriginTon(double sumOriginTon) {
		this.sumOriginTon = sumOriginTon;
	}

	public double getSumDestinationTon() {
		return sumDestinationTon;
	}

	public void setSumDestinationTon(double sumDestinationTon) {
		this.sumDestinationTon = sumDestinationTon;
	}

	public int getTotalColumn() {
		return totalColumn;
	}

	public void setTotalColumn(int totalColumn) {
		this.totalColumn = totalColumn;
	}

	public double getFuellogCharge() {
		return fuellogCharge;
	}

	public void setFuellogCharge(double fuellogCharge) {
		this.fuellogCharge = fuellogCharge;
	}

	/*public int getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
	}*/
	
	public void setTollTagAmount(double tollTagAmount) {
		this.tollTagAmount = tollTagAmount;
	}
	
	
	public double getTollTagAmount() {
		return tollTagAmount;
	}
	
	

}
