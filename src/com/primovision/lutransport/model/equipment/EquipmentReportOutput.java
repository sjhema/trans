package com.primovision.lutransport.model.equipment;

import org.apache.commons.lang.StringUtils;

import com.primovision.lutransport.model.ReportModel;

public class EquipmentReportOutput implements ReportModel {
	private String unit = StringUtils.EMPTY;
	
	private String company = StringUtils.EMPTY;
	
	private String vin = StringUtils.EMPTY;
	private String year = StringUtils.EMPTY;
	private String make = StringUtils.EMPTY;
	private String model = StringUtils.EMPTY;
	
	private String lender = StringUtils.EMPTY;
	private String loanNo = StringUtils.EMPTY;
	
	private Double paymentAmount = new Double(0.00);
	private String paymentDueDom = StringUtils.EMPTY;
	
	private String loanStartDate = StringUtils.EMPTY;
	private String loanEndDate = StringUtils.EMPTY;
	private Double interestRate = new Double(0.00);;
	private Integer noOfPayments = 0;
	private Integer paymentsLeft = 0;
	private String hasTitle = StringUtils.EMPTY;
	private String owner = StringUtils.EMPTY;
	private String title = StringUtils.EMPTY;
	private String soldDate = StringUtils.EMPTY;
	private String buyer = StringUtils.EMPTY;
	private Double salePrice = new Double(0.00);
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getLender() {
		return lender;
	}
	public void setLender(String lender) {
		this.lender = lender;
	}
	public String getLoanNo() {
		return loanNo;
	}
	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}
	public Double getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getPaymentDueDom() {
		return paymentDueDom;
	}
	public void setPaymentDueDom(String paymentDueDom) {
		this.paymentDueDom = paymentDueDom;
	}
	public String getLoanStartDate() {
		return loanStartDate;
	}
	public void setLoanStartDate(String loanStartDate) {
		this.loanStartDate = loanStartDate;
	}
	public String getLoanEndDate() {
		return loanEndDate;
	}
	public void setLoanEndDate(String loanEndDate) {
		this.loanEndDate = loanEndDate;
	}
	public Double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}
	public Integer getNoOfPayments() {
		return noOfPayments;
	}
	public void setNoOfPayments(Integer noOfPayments) {
		this.noOfPayments = noOfPayments;
	}
	public Integer getPaymentsLeft() {
		return paymentsLeft;
	}
	public void setPaymentsLeft(Integer paymentsLeft) {
		this.paymentsLeft = paymentsLeft;
	}
	public String getHasTitle() {
		return hasTitle;
	}
	public void setHasTitle(String hasTitle) {
		this.hasTitle = hasTitle;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSoldDate() {
		return soldDate;
	}
	public void setSoldDate(String soldDate) {
		this.soldDate = soldDate;
	}
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public Double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}
	
	
}
