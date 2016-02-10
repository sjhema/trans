package com.primovision.lutransport.model.report;

import java.util.List;

import com.primovision.lutransport.model.EzToll;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.ReportModel;

public class TollDistributionReportInput /*implements ReportModel*/{
	private String toolcompany;
	private String company;
	private String terminal;

	private String transactionDateFrom;
	private String transactionDateTo;
	
	private String fromInvoiceDate;
	private String invoiceDateTo;
	
	public String getFromInvoiceDate() {
		return fromInvoiceDate;
	}
	public void setFromInvoiceDate(String fromInvoiceDate) {
		this.fromInvoiceDate = fromInvoiceDate;
	}
	public String getInvoiceDateTo() {
		return invoiceDateTo;
	}
	public void setInvoiceDateTo(String invoiceDateTo) {
		this.invoiceDateTo = invoiceDateTo;
	}
	
	List<EzToll> eztolls=null;
	
	long totalColumn=0;
	double totalAmounts=0.0;
	public String getToolcompany() {
		return toolcompany;
	}
	public void setToolcompany(String toolcompany) {
		this.toolcompany = toolcompany;
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
	public double getTotalAmounts() {
		return totalAmounts;
	}
	public void setTotalAmounts(double totalAmounts) {
		this.totalAmounts = totalAmounts;
	}
	
    	
	
	
	

}
