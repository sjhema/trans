package com.primovision.lutransport.model.accident;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;

@Entity
@Table(name = "accident_type")
public class AccidentType extends AbstractBaseModel {
	@Column(name="accident_type")
	private String accidentType;

	@Column(name="description")
	private String description;

	public String getAccidentType() {
		return accidentType;
	}

	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
