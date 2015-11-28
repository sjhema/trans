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
@Table(name ="hoursminutes")
public class HoursMinutes extends AbstractBaseModel {
	

	private static final long serialVersionUID = 1L;

	

	
	@NotEmpty
	@Column(name ="hours_values")
	protected String hoursvalues;
	
	@NotEmpty
	@Column(name ="hours_format")
	protected String hoursformat;

	/*@NotEmpty*/
	@Column(name ="minutes")
	protected String minutes;

	public String getHoursvalues() {
		return hoursvalues;
	}

	public void setHoursvalues(String hoursvalues) {
		this.hoursvalues = hoursvalues;
	}

	public String getHoursformat() {
		return hoursformat;
	}

	public void setHoursformat(String hoursformat) {
		this.hoursformat = hoursformat;
	}

	public String getMinutes() {
		return minutes;
	}

	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}
	
	
    
	
	
}
