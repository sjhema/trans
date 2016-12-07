package com.primovision.lutransport.model.vehiclemaintenance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;

@Entity
@Table(name="repair_order_component")
public class RepairOrderComponent extends AbstractBaseModel {
	@Column(name="component")
	private String component;
	
	@Column(name="description")
	private String description;

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
