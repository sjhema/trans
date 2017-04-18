package com.primovision.lutransport.model.report;

// Subcontractor summary report - 16thMar2016
public class SubcontractorSummary {
	String subcontractorName;
	
	// Subcontractor summary by load report - 14thApr2017
	String origin;
	String destination;
	
	Double revenue = 0.0;
	Double amountPaid = 0.0;
	Double netAmount = 0.0;
	
	// Subcontractor summary by load report - 14thApr2017
	Integer loadCount = 0;
	
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public Integer getLoadCount() {
		return loadCount;
	}
	public void setLoadCount(Integer loadCount) {
		this.loadCount = loadCount;
	}
	public String getSubcontractorName() {
		return subcontractorName;
	}
	public void setSubcontractorName(String subcontractorName) {
		this.subcontractorName = subcontractorName;
	}
	public Double getRevenue() {
		return revenue;
	}
	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}
	public Double getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}
	public Double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}
}
