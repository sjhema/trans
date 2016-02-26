package com.primovision.lutransport.model.report;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.primovision.lutransport.model.EzToll;
import com.primovision.lutransport.model.FuelLog;

public class TollDistributionReportWrapper
{
	
	long totalColumn=0;
	double totalGallons=0.0;
	double totalFees=0.0;
	double totaldiscounts=0.0;
	double totalAmounts=0.0;
	
	String invoiceDateFrom = StringUtils.EMPTY;
	String invoiceDateTo = StringUtils.EMPTY;
	
	String transactionDateFrom = StringUtils.EMPTY;
	String transactionDateTo = StringUtils.EMPTY;
	
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
	List<EzToll> eztolls=null;
    public List<EzToll> getEztolls() {
		return eztolls;
	}
	public void setEztolls(List<EzToll> eztolls) {
		this.eztolls = eztolls;
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
   
}
