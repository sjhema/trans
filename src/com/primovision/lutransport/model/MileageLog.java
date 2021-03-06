package com.primovision.lutransport.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "mileage_log")
public class MileageLog extends AbstractBaseModel implements ReportModel {
	public static final String SOURCE_OLD_GPS = "OLD_GPS";
	public static final String SOURCE_NEW_GPS = "NEW_GPS";
	public static final String SOURCE_MANUAL = "MANUAL";
	public static final String SOURCE_OLD_NEW_GPS = "OLD_NEW_GPS";
	public static final String SOURCE_TICKET = "TICKET";
	
	@NotNull
	@Column(name="period")
	private Date period;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="state")
	private State state;
	
	@NotNull
	@Column(name="miles")
	private Double miles;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="unit")
	private Vehicle unit;
	
	@NotNull
	@NotEmpty
	@Column(name="unit_num")
	private String unitNum;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="company")
	private Location company;
	
	@NotNull
	@NotEmpty
	@Column(name="vin")
	private String vin;
	
	@NotNull
   @Column(name="first_in_state")
   private Date firstInState;
	
	@NotNull
   @Column(name="last_in_state")
   private Date lastInState;
	
	@Column(name="groups")
   private String groups;
	
	@Column(name="gps")
   private String gps;
	
	@Column(name="source")
   private String source;
	
	@ManyToOne
	@JoinColumn(name="vehicle_permit")
	private VehiclePermit vehiclePermit;
	
	@Column(name="vehicle_permit_number")
	private String vehiclePermitNumber;
	
	@Transient
	private String periodStr;
	
	@Transient
	private String subcontractorStr;
	
	public Date getFirstInState() {
		return firstInState;
	}

	public void setFirstInState(Date firstInState) {
		this.firstInState = firstInState;
	}

	public String getVehiclePermitNumber() {
		return vehiclePermitNumber;
	}

	public void setVehiclePermitNumber(String vehiclePermitNumber) {
		this.vehiclePermitNumber = vehiclePermitNumber;
	}

	public Date getLastInState() {
		return lastInState;
	}

	public void setLastInState(Date lastInState) {
		this.lastInState = lastInState;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}
	
	public VehiclePermit getVehiclePermit() {
		return vehiclePermit;
	}

	public void setVehiclePermit(VehiclePermit vehiclePermit) {
		this.vehiclePermit = vehiclePermit;
	}

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Double getMiles() {
		return miles;
	}

	public void setMiles(Double miles) {
		this.miles = miles;
	}

	public Vehicle getUnit() {
		return unit;
	}

	public void setUnit(Vehicle unit) {
		this.unit = unit;
	}

	public String getUnitNum() {
		return unitNum;
	}

	public void setUnitNum(String unitNum) {
		this.unitNum = unitNum;
	}

	public Location getCompany() {
		return company;
	}

	public void setCompany(Location company) {
		this.company = company;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Transient
	public String getPeriodStr() {
		return periodStr;
	}

	@Transient
	public void setPeriodStr(String periodStr) {
		this.periodStr = periodStr;
	}

	@Transient
	public String getSubcontractorStr() {
		return subcontractorStr;
	}
	@Transient
	public void setSubcontractorStr(String subcontractorStr) {
		this.subcontractorStr = subcontractorStr;
	}
}
