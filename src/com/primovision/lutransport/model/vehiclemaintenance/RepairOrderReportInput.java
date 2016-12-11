package com.primovision.lutransport.model.vehiclemaintenance;

import com.primovision.lutransport.model.ReportModel;

public class RepairOrderReportInput implements ReportModel {
	private String orderId;
	
	private String repairOrderDateFrom;
	private String repairOrderDateTo;
	
	private String company;
	private String subcontractor;
	
	private String vehicle;
	private String mechanic;
	
	private String lineItemType;
	private String lineItemComponent;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getRepairOrderDateFrom() {
		return repairOrderDateFrom;
	}
	public void setRepairOrderDateFrom(String repairOrderDateFrom) {
		this.repairOrderDateFrom = repairOrderDateFrom;
	}
	public String getRepairOrderDateTo() {
		return repairOrderDateTo;
	}
	public void setRepairOrderDateTo(String repairOrderDateTo) {
		this.repairOrderDateTo = repairOrderDateTo;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getSubcontractor() {
		return subcontractor;
	}
	public void setSubcontractor(String subcontractor) {
		this.subcontractor = subcontractor;
	}
	public String getVehicle() {
		return vehicle;
	}
	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}
	public String getMechanic() {
		return mechanic;
	}
	public void setMechanic(String mechanic) {
		this.mechanic = mechanic;
	}
	public String getLineItemType() {
		return lineItemType;
	}
	public void setLineItemType(String lineItemType) {
		this.lineItemType = lineItemType;
	}
	public String getLineItemComponent() {
		return lineItemComponent;
	}
	public void setLineItemComponent(String lineItemComponent) {
		this.lineItemComponent = lineItemComponent;
	}
}
