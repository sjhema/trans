package com.primovision.lutransport.model.report;

import java.util.List;

import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.ReportModel;

public class FuelDistributionReportInput /*implements ReportModel*/{
	
	private String fuelVendor;
	private String company;
	private String terminal;
	private String fromInvoiceDate;
	private String invoiceDateTo;
	private String transactionDateFrom;
	private String transactionDateTo;
	private String transactionTimeFrom;
	private String transactionTimeTo;
	private String unit;
	private String driver;
	private String cardno;
	private String state;
	private String gallonsFrom;
	private String gallonsTo;
	private String unitPriceFrom;
	private String unitPriceTo;
	private String feesFrom;
	private String feesTo;
	private String discountFrom;
	private String discountTo;
	private String amountFrom;
	private String amountTo;
	private String fuelCardNumber;
	private String fromInvoiceNo;
	private String invoiceNoTo;
	private String fueltype;
	
	
	List<FuelLog> fuellogs=null;
	
	
	long totalColumn=0;
	double totalGallons=0.0;
	double totalFees=0.0;
	double totaldiscounts=0.0;
	double totalAmounts=0.0;
	
	
	
	
	public String getFueltype() {
		return fueltype;
	}
	public void setFueltype(String fueltype) {
		this.fueltype = fueltype;
	}
	public String getFromInvoiceNo() {
		return fromInvoiceNo;
	}
	public void setFromInvoiceNo(String fromInvoiceNo) {
		this.fromInvoiceNo = fromInvoiceNo;
	}
	public String getInvoiceNoTo() {
		return invoiceNoTo;
	}
	public void setInvoiceNoTo(String invoiceNoTo) {
		this.invoiceNoTo = invoiceNoTo;
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
	
	
	
	
	
	public String getFuelCardNumber() {
		return fuelCardNumber;
	}
	public void setFuelCardNumber(String fuelCardNumber) {
		this.fuelCardNumber = fuelCardNumber;
	}
	public String getFuelVendor() {
		return fuelVendor;
	}
	public List<FuelLog> getFuellogs() {
		return fuellogs;
	}




	public void setFuellogs(List<FuelLog> fuellogs) {
		this.fuellogs = fuellogs;
	}




	public void setFuelVendor(String fuelVendor) {
		this.fuelVendor = fuelVendor;
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
	public String getTransactionTimeFrom() {
		return transactionTimeFrom;
	}
	public void setTransactionTimeFrom(String transactionTimeFrom) {
		this.transactionTimeFrom = transactionTimeFrom;
	}
	public String getTransactionTimeTo() {
		return transactionTimeTo;
	}
	public void setTransactionTimeTo(String transactionTimeTo) {
		this.transactionTimeTo = transactionTimeTo;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	
	
	
	
	
	
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getGallonsFrom() {
		return gallonsFrom;
	}
	public void setGallonsFrom(String gallonsFrom) {
		this.gallonsFrom = gallonsFrom;
	}
	public String getGallonsTo() {
		return gallonsTo;
	}
	public void setGallonsTo(String gallonsTo) {
		this.gallonsTo = gallonsTo;
	}
	public String getUnitPriceFrom() {
		return unitPriceFrom;
	}
	public void setUnitPriceFrom(String unitPriceFrom) {
		this.unitPriceFrom = unitPriceFrom;
	}
	public String getUnitPriceTo() {
		return unitPriceTo;
	}
	public void setUnitPriceTo(String unitPriceTo) {
		this.unitPriceTo = unitPriceTo;
	}
	public String getFeesFrom() {
		return feesFrom;
	}
	public void setFeesFrom(String feesFrom) {
		this.feesFrom = feesFrom;
	}
	public String getFeesTo() {
		return feesTo;
	}
	public void setFeesTo(String feesTo) {
		this.feesTo = feesTo;
	}
	public String getDiscountFrom() {
		return discountFrom;
	}
	public void setDiscountFrom(String discountFrom) {
		this.discountFrom = discountFrom;
	}
	public String getDiscountTo() {
		return discountTo;
	}
	public void setDiscountTo(String discountTo) {
		this.discountTo = discountTo;
	}
	public String getAmountFrom() {
		return amountFrom;
	}
	public void setAmountFrom(String amountFrom) {
		this.amountFrom = amountFrom;
	}
	public String getAmountTo() {
		return amountTo;
	}
	public void setAmountTo(String amountTo) {
		this.amountTo = amountTo;
	}
	
	

}
