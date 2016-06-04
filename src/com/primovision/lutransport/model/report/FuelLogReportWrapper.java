package com.primovision.lutransport.model.report;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.primovision.lutransport.model.FuelLog;

public class FuelLogReportWrapper
{
	
	long totalColumn=0;
	double totalGallons=0.0;
	double totalFees=0.0;
	double totaldiscounts=0.0;
	double totalAmounts=0.0;
	double totalGrossCost=0.0;
	
	String invoiceDateFrom = StringUtils.EMPTY;
	String invoiceDateTo = StringUtils.EMPTY;
	
	String transactionDateFrom = StringUtils.EMPTY;
	String transactionDateTo = StringUtils.EMPTY;
	
	String invoiceNumberFrom = StringUtils.EMPTY;
	String invoiceNumberTo = StringUtils.EMPTY;
	
	// Fuel log - truck report 3rd Jun 2016
	String company = StringUtils.EMPTY;
	
	List<FuelLog> fuellog=null;


	public String getCompany() {
		return company;
	}
	
	public void setCompany(String company) {
		this.company = company;
	}




	public String getInvoiceNumberFrom() {
		return invoiceNumberFrom;
	}




	public void setInvoiceNumberFrom(String invoiceNumberFrom) {
		this.invoiceNumberFrom = invoiceNumberFrom;
	}




	public String getInvoiceNumberTo() {
		return invoiceNumberTo;
	}




	public void setInvoiceNumberTo(String invoiceNumberTo) {
		this.invoiceNumberTo = invoiceNumberTo;
	}




	public String getInvoiceDateFrom() {
		return invoiceDateFrom;
	}




	public void setInvoiceDateFrom(String invoiceDateFrom) {
		this.invoiceDateFrom = invoiceDateFrom;
	}




	public String getInvoiceDateTo() {
		return invoiceDateTo;
	}




	public void setInvoiceDateTo(String invoiceDateTo) {
		this.invoiceDateTo = invoiceDateTo;
	}




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




	public long getTotalColumn() {
		return totalColumn;
	}




	public void setTotalColumn(long totalColumn) {
		this.totalColumn = totalColumn;
	}




	public double getTotalGallons() {
		return totalGallons;
	}




	public void setTotalGallons(double totalGallons) {
		this.totalGallons = totalGallons;
	}




	public double getTotalFees() {
		return totalFees;
	}




	public void setTotalFees(double totalFees) {
		this.totalFees = totalFees;
	}




	public double getTotaldiscounts() {
		return totaldiscounts;
	}




	public void setTotaldiscounts(double totaldiscounts) {
		this.totaldiscounts = totaldiscounts;
	}




	public double getTotalAmounts() {
		return totalAmounts;
	}




	public void setTotalAmounts(double totalAmounts) {
		this.totalAmounts = totalAmounts;
	}




	public List<FuelLog> getFuellog() {
		return fuellog;
	}




	public void setFuellog(List<FuelLog> fuellog) {
		this.fuellog = fuellog;
	}




	public double getTotalGrossCost() {
		return totalGrossCost;
	}




	public void setTotalGrossCost(double totalGrossCost) {
		this.totalGrossCost = totalGrossCost;
	}
	
	
	
	
	
	
}
