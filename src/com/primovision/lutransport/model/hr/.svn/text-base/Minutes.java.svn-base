package com.primovision.lutransport.model.hr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Ticket;


/**
 * @author subodh
 * 
 */

@Entity
@Table(name ="minutes")
public class Minutes extends AbstractBaseModel {
	

	private static final long serialVersionUID = 1L;

	@NotEmpty
	@Column(name ="minutes_value")
	protected String minutesvalue;

	public String getMinutesvalue() {
		return minutesvalue;
	}

	public void setMinutesvalue(String minutesvalue) {
		this.minutesvalue = minutesvalue;
	}

	
	
    
	
	
}
