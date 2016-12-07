package com.primovision.lutransport.model.vehiclemaintenance;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.Vehicle;

@Entity
@Table(name="repair_order")
public class RepairOrder extends AbstractBaseModel {
	@Column(name="repair_order_date")
	private Date repairOrderDate;
	
	@ManyToOne
	@JoinColumn(name="vehicle")
	private Vehicle vehicle;
	
	@ManyToOne
	@JoinColumn(name="company")
	private Location company;
	
	@ManyToOne
	@JoinColumn(name = "subcontractor")
	protected SubContractor subcontractor;
	
	@ManyToOne
	@JoinColumn(name="mechanic")
	protected Driver mechanic;
	
	@Column(name="total_cost")
	private Double totalCost;
	
	@Column(name="description")
	private String description;
	
	@Transient
	private String lineItemId;
	
	@Transient
	private String lineItemType;
		
	@Transient
	private String lineItemComponent;
	
	@Transient
	private String lineItemDescription;
		
	@Transient
	private Double lineItemNoOfHours;
		
	@Transient
	private Double lineItemLaborRate;
		
	@Transient
	private Double lineItemTotalLaborCost;
		
	@Transient
	private Double lineItemTotalPartsCost;
		
	@Transient
	private Double lineItemTotalCost;

	public Date getRepairOrderDate() {
		return repairOrderDate;
	}

	public void setRepairOrderDate(Date repairOrderDate) {
		this.repairOrderDate = repairOrderDate;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Location getCompany() {
		return company;
	}

	public void setCompany(Location company) {
		this.company = company;
	}

	public SubContractor getSubcontractor() {
		return subcontractor;
	}

	public void setSubcontractor(SubContractor subcontractor) {
		this.subcontractor = subcontractor;
	}

	public Driver getMechanic() {
		return mechanic;
	}

	public void setMechanic(Driver mechanic) {
		this.mechanic = mechanic;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Transient
	public String getLineItemType() {
		return lineItemType;
	}

	@Transient
	public void setLineItemType(String lineItemType) {
		this.lineItemType = lineItemType;
	}

	@Transient
	public String getLineItemDescription() {
		return lineItemDescription;
	}
	
	@Transient
	public void setLineItemDescription(String lineItemDescription) {
		this.lineItemDescription = lineItemDescription;
	}

	

	@Transient
	public Double getLineItemNoOfHours() {
		return lineItemNoOfHours;
	}

	@Transient
	public void setLineItemNoOfHours(Double lineItemNoOfHours) {
		this.lineItemNoOfHours = lineItemNoOfHours;
	}

	@Transient
	public Double getLineItemLaborRate() {
		return lineItemLaborRate;
	}

	@Transient
	public void setLineItemLaborRate(Double lineItemLaborRate) {
		this.lineItemLaborRate = lineItemLaborRate;
	}

	@Transient
	public Double getLineItemTotalLaborCost() {
		return lineItemTotalLaborCost;
	}

	@Transient
	public void setLineItemTotalLaborCost(Double lineItemTotalLaborCost) {
		this.lineItemTotalLaborCost = lineItemTotalLaborCost;
	}

	@Transient
	public Double getLineItemTotalPartsCost() {
		return lineItemTotalPartsCost;
	}

	@Transient
	public void setLineItemTotalPartsCost(Double lineItemTotalPartsCost) {
		this.lineItemTotalPartsCost = lineItemTotalPartsCost;
	}

	@Transient
	public Double getLineItemTotalCost() {
		return lineItemTotalCost;
	}

	@Transient
	public void setLineItemTotalCost(Double lineItemTotalCost) {
		this.lineItemTotalCost = lineItemTotalCost;
	}

	@Transient
	public String getLineItemId() {
		return lineItemId;
	}

	@Transient
	public void setLineItemId(String lineItemId) {
		this.lineItemId = lineItemId;
	}

	@Transient
	public String getLineItemComponent() {
		return lineItemComponent;
	}

	@Transient
	public void setLineItemComponent(String lineItemComponent) {
		this.lineItemComponent = lineItemComponent;
	}
}
