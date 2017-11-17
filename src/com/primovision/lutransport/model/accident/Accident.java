package com.primovision.lutransport.model.accident;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.insurance.InsuranceCompany;
import com.primovision.lutransport.model.insurance.InsuranceCompanyRep;

@Entity
@Table(name = "accident")
public class Accident extends AbstractBaseModel {
	@Transient
	public static Integer ACCIDENT_STATUS_OPEN = 1;
	@Transient
	public static Integer ACCIDENT_STATUS_CLOSED = 2;
	@Transient
	public static Integer ACCIDENT_STATUS_NOT_REPORTED = 3;
	
	@Column(name = "incident_date")
	private Date incidentDate;
	
	@Column(name = "driver_hired_date")
	private Date driverHiredDate;
	
	@Column(name = "incident_day_of_week")
	private String incidentDayOfWeek;
	
	private String location;
	
	@ManyToOne
	@JoinColumn(name="driver")
	private Driver driver;
	
	@ManyToOne
	@JoinColumn(name="vehicle")
	private Vehicle vehicle;
	
   @ManyToOne
   @JoinColumn(name = "insurance_company")
   private InsuranceCompany insuranceCompany;
   
   @ManyToOne
   @JoinColumn(name = "claim_rep")
   private InsuranceCompanyRep claimRep;
	
   @ManyToOne
	@JoinColumn(name="driver_company")
	private Location driverCompany;
   
   @ManyToOne
 	@JoinColumn(name="driver_terminal")
 	private Location driverTerminal;
   
   @ManyToOne
 	@JoinColumn(name="state")
 	private State state;
  
   @Column(name="driver_months_of_service")
  	private Integer driverMonthsOfService;
   
	@Column(name="accident_status")
	private Integer accidentStatus;
	
	@ManyToOne
	@JoinColumn(name="accident_type")
	private AccidentType accidentType;
	
	@ManyToOne
	@JoinColumn(name="weather")
	private AccidentWeather weather;
	
	@ManyToOne
	@JoinColumn(name="road_condition")
	private AccidentRoadCondition roadCondition;
	
	@Column(name="claim_no")
	private String claimNumber;
	
	@Column(name="notes")
	private String notes;
	
	@ManyToOne
	@JoinColumn(name="accident_cause")
	private AccidentCause accidentCause;
	
	@Column(name="vehicle_damage")
	private String vehicleDamage;
	
	@Column(name="no_injured")
	private String noInjured;
	
	@Column(name="towed")
	private String towed;
	
	@Column(name="citation")
	private String citation;
	
	@Column(name="recordable")
	private String recordable;
	
	@Column(name="hm_release")
	private String hmRelease;
	
	@Column(name="incident_time")
	private String incidentTime;
	
	@Column(name="fatality")
	private String fatality;
	
	@Column(name="paid")
	private Double paid;
	
	@Column(name="deductible")
	private Double deductible;
	
	@Column(name="expense")
	private Double expense;
	
	@Column(name="reserve")
	private Double reserve;
	
	@Column(name="total_cost")
	private Double totalCost;

	public Date getIncidentDate() {
		return incidentDate;
	}

	public void setIncidentDate(Date incidentDate) {
		this.incidentDate = incidentDate;
	}

	public Date getDriverHiredDate() {
		return driverHiredDate;
	}

	public void setDriverHiredDate(Date driverHiredDate) {
		this.driverHiredDate = driverHiredDate;
	}

	public String getIncidentDayOfWeek() {
		return incidentDayOfWeek;
	}

	public void setIncidentDayOfWeek(String incidentDayOfWeek) {
		this.incidentDayOfWeek = incidentDayOfWeek;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public InsuranceCompany getInsuranceCompany() {
		return insuranceCompany;
	}

	public void setInsuranceCompany(InsuranceCompany insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	public InsuranceCompanyRep getClaimRep() {
		return claimRep;
	}

	public void setClaimRep(InsuranceCompanyRep claimRep) {
		this.claimRep = claimRep;
	}

	public Location getDriverCompany() {
		return driverCompany;
	}

	public void setDriverCompany(Location driverCompany) {
		this.driverCompany = driverCompany;
	}

	public Location getDriverTerminal() {
		return driverTerminal;
	}

	public void setDriverTerminal(Location driverTerminal) {
		this.driverTerminal = driverTerminal;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Integer getDriverMonthsOfService() {
		return driverMonthsOfService;
	}

	public void setDriverMonthsOfService(Integer driverMonthsOfService) {
		this.driverMonthsOfService = driverMonthsOfService;
	}

	public Integer getAccidentStatus() {
		return accidentStatus;
	}

	public void setAccidentStatus(Integer accidentStatus) {
		this.accidentStatus = accidentStatus;
	}

	public AccidentType getAccidentType() {
		return accidentType;
	}

	public void setAccidentType(AccidentType accidentType) {
		this.accidentType = accidentType;
	}

	public AccidentWeather getWeather() {
		return weather;
	}

	public void setWeather(AccidentWeather weather) {
		this.weather = weather;
	}

	public AccidentRoadCondition getRoadCondition() {
		return roadCondition;
	}

	public void setRoadCondition(AccidentRoadCondition roadCondition) {
		this.roadCondition = roadCondition;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public AccidentCause getAccidentCause() {
		return accidentCause;
	}

	public void setAccidentCause(AccidentCause accidentCause) {
		this.accidentCause = accidentCause;
	}

	public String getVehicleDamage() {
		return vehicleDamage;
	}

	public void setVehicleDamage(String vehicleDamage) {
		this.vehicleDamage = vehicleDamage;
	}

	public String getNoInjured() {
		return noInjured;
	}

	public void setNoInjured(String noInjured) {
		this.noInjured = noInjured;
	}

	public String getTowed() {
		return towed;
	}

	public void setTowed(String towed) {
		this.towed = towed;
	}

	public String getCitation() {
		return citation;
	}

	public void setCitation(String citation) {
		this.citation = citation;
	}

	public String getHmRelease() {
		return hmRelease;
	}

	public void setHmRelease(String hmRelease) {
		this.hmRelease = hmRelease;
	}

	public Double getPaid() {
		return paid;
	}

	public void setPaid(Double paid) {
		this.paid = paid;
	}

	public Double getDeductible() {
		return deductible;
	}

	public void setDeductible(Double deductible) {
		this.deductible = deductible;
	}

	public Double getExpense() {
		return expense;
	}

	public void setExpense(Double expense) {
		this.expense = expense;
	}

	public Double getReserve() {
		return reserve;
	}

	public void setReserve(Double reserve) {
		this.reserve = reserve;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public String getRecordable() {
		return recordable;
	}

	public void setRecordable(String recordable) {
		this.recordable = recordable;
	}
	
	public String getIncidentTime() {
		return incidentTime;
	}

	public void setIncidentTime(String incidentTime) {
		this.incidentTime = incidentTime;
	}

	public String getFatality() {
		return fatality;
	}

	public void setFatality(String fatality) {
		this.fatality = fatality;
	}

	@Transient
	public String getWeatherStr() {
		return (weather == null ? StringUtils.EMPTY : weather.getWeather());
	}
	
	@Transient
	public String getRoadConditionStr() {
		return (roadCondition == null ? StringUtils.EMPTY : roadCondition.getRoadCondition());
	}
	
	@Transient
	public String getAccidentCauseStr() {
		return (accidentCause == null ? StringUtils.EMPTY : accidentCause.getCause());
	}
	
	@Transient
	public String getStateStr() {
		return (state == null ? StringUtils.EMPTY : state.getName());
	}
}
