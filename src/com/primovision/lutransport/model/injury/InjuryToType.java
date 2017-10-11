package com.primovision.lutransport.model.injury;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;

@Entity
@Table(name = "injury_to_type")
public class InjuryToType extends AbstractBaseModel {
	@Column(name="injury_to")
	private String injuryTo;

	@Column(name="description")
	private String description;

	public String getInjuryTo() {
		return injuryTo;
	}

	public void setInjuryTo(String injuryTo) {
		this.injuryTo = injuryTo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
