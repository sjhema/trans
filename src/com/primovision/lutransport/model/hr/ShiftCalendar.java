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
import com.primovision.lutransport.model.Location;


/**
 * @author subodh
 * 
 */

@Entity
@Table(name = "shiftcalendar")
public class ShiftCalendar extends AbstractBaseModel {
	
	
	private static final long serialVersionUID = 1L;

	@NotEmpty
	@Column(name="name")
	protected String name;

	@ManyToOne
	@JoinColumn(name="company")
	protected Location company;
	
	
	@Column(name = "abbreviation")
	protected String abbreviation;
	
	@Column(name = "shift_description")
	protected String shiftdescription;
	
	@NotEmpty
	@Column(name = "starttime_hours")
	protected String starttimehours;
	
	@NotEmpty
	@Column(name = "starttime_minutes")
	protected String starttimeminutes;
	
	@NotEmpty
	@Column(name = "endtime_hours")
	protected String endtimehours;
	
	@NotEmpty
	@Column(name = "endtime_minutes")
	protected String endtimeminutes;
	
	@NotNull
	@Column(name = "dailyregular_hours")
	protected Double dailyregularhours;
	
	@NotNull
	@Column(name = "weeklyregular_hours")
	protected Double weeklyregularhours;
	
	
	@NotNull
	@Column(name="effectivestart_date")
	protected Date effectivestartdate;
	
	@NotNull
	@Column(name="effectiveend_date")
	protected Date effectiveenddate;
	
	
	//created for storing time from to time to for each shift type
	@Column(name="start_time")
	protected String starttime;
	
	@Column(name="end_time")
	protected String endtime;
	//created
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getCompany() {
		return company;
	}

	public void setCompany(Location company) {
		this.company = company;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getShiftdescription() {
		return shiftdescription;
	}

	public void setShiftdescription(String shiftdescription) {
		this.shiftdescription = shiftdescription;
	}

	
	
	
	
	
	
	public String getStarttimehours() {
		return starttimehours;
	}

	public void setStarttimehours(String starttimehours) {
		this.starttimehours = starttimehours;
	}

	public String getStarttimeminutes() {
		return starttimeminutes;
	}

	public void setStarttimeminutes(String starttimeminutes) {
		this.starttimeminutes = starttimeminutes;
	}

	public String getEndtimehours() {
		return endtimehours;
	}

	public void setEndtimehours(String endtimehours) {
		this.endtimehours = endtimehours;
	}

	public String getEndtimeminutes() {
		return endtimeminutes;
	}

	public void setEndtimeminutes(String endtimeminutes) {
		this.endtimeminutes = endtimeminutes;
	}

	public Double getDailyregularhours() {
		return dailyregularhours;
	}

	public void setDailyregularhours(Double dailyregularhours) {
		this.dailyregularhours = dailyregularhours;
	}

	public Double getWeeklyregularhours() {
		return weeklyregularhours;
	}

	public void setWeeklyregularhours(Double weeklyregularhours) {
		this.weeklyregularhours = weeklyregularhours;
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

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	

	
	
	
}
