package com.primovision.lutransport.model.hr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.primovision.lutransport.model.AbstractBaseModel;
/**
 * @author kishor
 *
 */
@Entity
@Table(name="leave_type")
public class LeaveType extends AbstractBaseModel{
	
	@NotEmpty
	@Column(name="name")
	private String name;
	
	@NotNull
	@Column(name="description")
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

}
