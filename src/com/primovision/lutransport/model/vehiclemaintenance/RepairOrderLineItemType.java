package com.primovision.lutransport.model.vehiclemaintenance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;

@Entity
@Table(name="repair_order_line_item_type")
public class RepairOrderLineItemType extends AbstractBaseModel {
	@Column(name="type")
	private String type;
	
	@Column(name="description")
	private String description;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
