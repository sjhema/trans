package com.primovision.lutransport.model.hr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.primovision.lutransport.model.AbstractBaseModel;

@Entity
@Table(name="emp_leave")
public class Leave extends AbstractBaseModel{
	
	
	
	@NotEmpty
	@Column(name = "leave_type")
	protected String leavetype;
	
	@NotEmpty
	@Column(name="leave_description")
	private String description;
	
	
	public String getLeavetype() {
		return leavetype;
	}
	public void setLeavetype(String leavetype) {
		this.leavetype = leavetype;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	
	
}
