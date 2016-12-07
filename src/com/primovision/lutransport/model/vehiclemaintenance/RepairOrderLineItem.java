package com.primovision.lutransport.model.vehiclemaintenance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;

@Entity
@Table(name="repair_order_line_item")
public class RepairOrderLineItem extends AbstractBaseModel {
	  @ManyToOne
	  @JoinColumn(name="repair_order")
	  private RepairOrder repairOrder;
	  
	  @ManyToOne
	  @JoinColumn(name="line_item_type")
	  private RepairOrderLineItemType lineItemType;
	  
	  @ManyToOne
	  @JoinColumn(name="component")
	  private RepairOrderComponent component;
	  
	  @Column(name="description")
	  private String description;
	
	  @Column(name="no_of_hours")
	  private Double noOfHours;
	
	  @Column(name="labor_rate")
	  private Double laborRate;
	
	  @Column(name="total_labor_cost")
	  private Double totalLaborCost;
	
	  @Column(name="total_parts_cost")
	  private Double totalPartsCost;
	
	  @Column(name="total_cost")
	  private Double totalCost;

	  public RepairOrder getRepairOrder() {
		return repairOrder;
	}
	
	public void setRepairOrder(RepairOrder repairOrder) {
		this.repairOrder = repairOrder;
	}
	
	public RepairOrderLineItemType getLineItemType() {
		return lineItemType;
	}

	public void setLineItemType(RepairOrderLineItemType lineItemType) {
		this.lineItemType = lineItemType;
	}

	public RepairOrderComponent getComponent() {
		return component;
	}

	public void setComponent(RepairOrderComponent component) {
		this.component = component;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Double getNoOfHours() {
		return noOfHours;
	}
	
	public void setNoOfHours(Double noOfHours) {
		this.noOfHours = noOfHours;
	}
	
	public Double getLaborRate() {
		return laborRate;
	}
	
	public void setLaborRate(Double laborRate) {
		this.laborRate = laborRate;
	}
	
	public Double getTotalLaborCost() {
		return totalLaborCost;
	}
	
	public void setTotalLaborCost(Double totalLaborCost) {
		this.totalLaborCost = totalLaborCost;
	}
	
	public Double getTotalPartsCost() {
		return totalPartsCost;
	}
	
	public void setTotalPartsCost(Double totalPartsCost) {
		this.totalPartsCost = totalPartsCost;
	}
	
	public Double getTotalCost() {
		return totalCost;
	}
	
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}
}
