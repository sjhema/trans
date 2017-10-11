package com.primovision.lutransport.model.injury;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;

@Entity
@Table(name = "injury_incident_type")
public class InjuryIncidentType extends AbstractBaseModel {
	@Column(name="incident_type")
	private String incidentType;

	@Column(name="description")
	private String description;
	
	public String getIncidentType() {
		return incidentType;
	}

	public void setIncidentType(String incidentType) {
		this.incidentType = incidentType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
