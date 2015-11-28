package com.primovision.lutransport.model.hr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import com.primovision.lutransport.model.AbstractBaseModel;

/**
 * @author kishor
 *
 */
@Entity
@Table(name="employee_catagory")
public class EmployeeCatagory extends AbstractBaseModel {
	@NotEmpty
	@Column(name="name")
	private String name;
	
	@Column(name="code")
	private String code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
