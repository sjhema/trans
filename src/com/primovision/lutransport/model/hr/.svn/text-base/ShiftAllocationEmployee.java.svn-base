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
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;


/**
 * @author subodh
 * 
 */

@Entity
@Table(name ="shiftallocationemployee")
public class ShiftAllocationEmployee extends AbstractBaseModel {
	
	
	private static final long serialVersionUID = 1L;

	

	@ManyToOne
	@JoinColumn(name="shift_calendar")
	protected ShiftCalendar shift_calendar;
	
	@ManyToOne
	@JoinColumn(name="company")
	protected Location company;
	
	@ManyToOne
	@JoinColumn(name="terminal")
	protected Location terminal;
	
	
	
	@ManyToOne
	@JoinColumn(name="employee")
	protected Driver driver;
	
	
	
	@NotNull
	@Column(name="effectivestart_date")
	protected Date effectivestartdate;
	
	@NotNull
	@Column(name="effectiveend_date")
	protected Date effectiveenddate;

	public ShiftCalendar getShift_calendar() {
		return shift_calendar;
	}

	public void setShift_calendar(ShiftCalendar shift_calendar) {
		this.shift_calendar = shift_calendar;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver employee) {
		this.driver = driver;
	}

	public Date getEffectivestartdate() {
		return effectivestartdate;
	}

	public void setEffectivestartdate(Date effectivestartdate) {
		this.effectivestartdate = effectivestartdate;
	}

	public Date getEffectiveenddate() {
		return effectiveenddate;
	}

	public void setEffectiveenddate(Date effectiveenddate) {
		this.effectiveenddate = effectiveenddate;
	}

	public Location getCompany() {
		return company;
	}

	public void setCompany(Location company) {
		this.company = company;
	}

	public Location getTerminal() {
		return terminal;
	}

	public void setTerminal(Location terminal) {
		this.terminal = terminal;
	}
	
	

	
	
	
}
