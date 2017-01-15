package com.primovision.lutransport.model.equipment;

import com.primovision.lutransport.model.ReportModel;

public class EquipmentReportInput implements ReportModel {
	private String loanNo;
	
	private String vehicle;
	
	private String company;
	private String lender;
	
	private String loanStartDate;
	private String loanEndDate;

	private String titleOwner;
	private String registeredOwner;
	
	private String title;
	private String holdsTitle;
	
	private String buyer;

	public String getLoanNo() {
		return loanNo;
	}
	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}
	public String getVehicle() {
		return vehicle;
	}
	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getLender() {
		return lender;
	}
	public void setLender(String lender) {
		this.lender = lender;
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
	public String getHoldsTitle() {
		return holdsTitle;
	}
	public void setHoldsTitle(String holdsTitle) {
		this.holdsTitle = holdsTitle;
	}
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
}
