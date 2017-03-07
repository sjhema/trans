package com.primovision.lutransport.model.equipment;

import org.apache.commons.lang.StringUtils;

import com.primovision.lutransport.model.ReportModel;

public class EquipmentReportOutput implements ReportModel {
	private Integer unit;
	
	private String vin = StringUtils.EMPTY;
	private String year = StringUtils.EMPTY;
	private String make = StringUtils.EMPTY;
	private String model = StringUtils.EMPTY;
	
	private String company = StringUtils.EMPTY;
	
	private String lender = StringUtils.EMPTY;
	private String loanNo = StringUtils.EMPTY;
	
	private Double paymentAmount;
	private String paymentDueDom = StringUtils.EMPTY;
	
	private String loanStartDate = StringUtils.EMPTY;
	private String loanEndDate = StringUtils.EMPTY;
	private Double interestRate;
	private Integer noOfPayments;
	private Integer paymentsLeft;
	
	private String holdsTitle = StringUtils.EMPTY;
	private String titleOwner = StringUtils.EMPTY;
	private String registeredOwner = StringUtils.EMPTY;
	private String title = StringUtils.EMPTY;
	
	private String saleDate = StringUtils.EMPTY;
	private String buyer = StringUtils.EMPTY;
	private Double salePrice;
	
	public Integer getUnit() {
		return unit;
	}
	public void setUnit(Integer unit) {
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
	
	public String getHoldsTitle() {
		return holdsTitle;
	}
	public void setHoldsTitle(String holdsTitle) {
		this.holdsTitle = holdsTitle;
	}
	public String getTitleOwner() {
		return titleOwner;
	}
	public void setTitleOwner(String titleOwner) {
		this.titleOwner = titleOwner;
	}
	public String getRegisteredOwner() {
		return registeredOwner;
	}
	public void setRegisteredOwner(String registeredOwner) {
		this.registeredOwner = registeredOwner;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getSaleDate() {
		return saleDate;
	}
	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
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
