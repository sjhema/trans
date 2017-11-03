package com.primovision.lutransport.model.accident;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;

@Entity
@Table(name = "accident_cause")
public class AccidentCause extends AbstractBaseModel {
	@Column(name="cause")
	private String cause;

	@Column(name="description")
	private String description;

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
