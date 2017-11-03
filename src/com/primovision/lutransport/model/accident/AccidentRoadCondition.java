package com.primovision.lutransport.model.accident;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;

@Entity
@Table(name = "accident_road_condition")
public class AccidentRoadCondition extends AbstractBaseModel {
	@Column(name="road_condition")
	private String roadCondition;

	@Column(name="description")
	private String description;

	public String getRoadCondition() {
		return roadCondition;
	}

	public void setRoadCondition(String roadCondition) {
		this.roadCondition = roadCondition;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
