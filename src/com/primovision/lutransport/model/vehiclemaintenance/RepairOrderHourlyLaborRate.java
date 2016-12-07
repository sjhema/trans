package com.primovision.lutransport.model.vehiclemaintenance;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;

@Entity
@Table(name="repair_order_hourly_labor_rate")
public class RepairOrderHourlyLaborRate extends AbstractBaseModel {
	@Column(name="labor_rate")
	private Double laborRate;
	
	@Column(name="effective_start_date")
	private Date effectiveStartDate;
  
	@Column(name="effective_end_date")
	private Date effectiveEndDate;
	
	@Column(name="description")
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getLaborRate() {
		return laborRate;
	}

	public void setLaborRate(Double laborRate) {
		this.laborRate = laborRate;
	}

	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}
}
